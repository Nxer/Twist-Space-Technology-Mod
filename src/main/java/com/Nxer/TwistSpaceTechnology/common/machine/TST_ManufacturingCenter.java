package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures;
import com.Nxer.TwistSpaceTechnology.common.block.meta.multiuse.BlockMultiUseCore;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstSharedFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.TAE;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_ManufacturingCenter extends GTPPMultiBlockBase<TST_ManufacturingCenter>
    implements ISurvivalConstructable {

    @MagicConstant(valuesFromClass = VoltageIndex.class)
    private static final int LOWEST_CORE_TIER = VoltageIndex.IV;

    private static final double SPEED_BONUS_BASE = Config.ManufacturingCenter_SpeedBonus_Base;
    private static final double SPEED_BONUS_FOR_CORE_TIER = Config.ManufacturingCenter_SpeedBonus_Tier;

    private static final double EU_REDUCTION_FOR_CORE_TIER = Config.ManufacturingCenter_PowerReduction;

    private static final int MAX_PARALLEL_MODIFIER = Config.ManufacturingCenter_MaxParallelModifier;

    public int coreTier = -1;
    public int casingCount = 0;

    public TST_ManufacturingCenter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_ManufacturingCenter(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ManufacturingCenter(mName);
    }

    // region Machine Settings

    private static final BiMap<Integer, ManufacturingMachineType> MODES = HashBiMap.create();

    private static final class ManufacturingMachineType {

        private final String typeName;
        private final RecipeMap<?> recipeMap;
        private final String unlocalizedName;

        private ManufacturingMachineType(String typeName, RecipeMap<?> recipeMap, String unlocalizedName) {
            this.typeName = typeName;
            this.recipeMap = recipeMap;
            this.unlocalizedName = unlocalizedName;
        }

        public static ManufacturingMachineType of(RecipeMap<?> recipeMap) {
            var recipeMapNameParts = recipeMap.unlocalizedName.split("\\.");
            return new ManufacturingMachineType(
                recipeMapNameParts[recipeMapNameParts.length - 1],
                recipeMap,
                recipeMap.unlocalizedName);
        }

        public String typeName() {
            return typeName;
        }

        public RecipeMap<?> recipeMap() {
            return recipeMap;
        }

        public String unlocalizedName() {
            return unlocalizedName;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ManufacturingMachineType) obj;
            return Objects.equals(this.typeName, that.typeName) && Objects.equals(this.recipeMap, that.recipeMap)
                && Objects.equals(this.unlocalizedName, that.unlocalizedName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(typeName, recipeMap, unlocalizedName);
        }

        @Override
        public String toString() {
            return "ManufacturingMachineType[" + "typeName="
                + typeName
                + ", "
                + "recipeMap="
                + recipeMap
                + ", "
                + "unlocalizedName="
                + unlocalizedName
                + ']';
        }

    }

    static {
        MODES.put(0, ManufacturingMachineType.of(RecipeMaps.compressorRecipes));
        MODES.put(1, ManufacturingMachineType.of(RecipeMaps.latheRecipes));
        MODES.put(2, ManufacturingMachineType.of(RecipeMaps.polarizerRecipes));
        MODES.put(3, ManufacturingMachineType.of(RecipeMaps.fermentingRecipes));
        MODES.put(4, ManufacturingMachineType.of(RecipeMaps.fluidExtractionRecipes));
        MODES.put(5, ManufacturingMachineType.of(RecipeMaps.extractorRecipes));
        MODES.put(6, ManufacturingMachineType.of(RecipeMaps.laserEngraverRecipes));
        MODES.put(7, ManufacturingMachineType.of(RecipeMaps.autoclaveRecipes));
        MODES.put(8, ManufacturingMachineType.of(RecipeMaps.fluidSolidifierRecipes));
    }

    private RecipeMap<?> getRecipeMapAtCurrentMode() {
        if (!MODES.containsKey(machineMode)) {
            machineMode = 0;
        }

        return MODES.get(machineMode).recipeMap;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (MAX_PARALLEL_MODIFIER * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private long getMaxVoltageTierAtCurrentMode() {
        return GTValues.V[coreTier];
    }

    private double getSpeedBonusAtCurrentCore() {
        return 1.0 / (1 + SPEED_BONUS_BASE + SPEED_BONUS_FOR_CORE_TIER * (coreTier - LOWEST_CORE_TIER));
    }

    private double getEuModifierAtCurrentCore() {
        return 1.0 - (EU_REDUCTION_FOR_CORE_TIER * Math.max(0, coreTier - LOWEST_CORE_TIER));
    }

    // endregion

    // region Processing!

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 200;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return getRecipeMapAtCurrentMode();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {

                // check core tier
                if (GTUtility.getTier(recipe.mEUt) > coreTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(GTUtility.getTier(recipe.mEUt));
                }

                if (recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0) > 0) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getAverageInputVoltage());
        logic.setAvailableAmperage(getMaxInputAmps());
        logic.setAmperageOC(mEnergyHatches.size() > 1);
        logic.setSpeedBonus(getSpeedBonusAtCurrentCore());
        logic.setEuModifier(getEuModifierAtCurrentCore());
    }

    // endregion

    // region Tooltips

    @Override
    @Deprecated
    public String getMachineType() {
        return null;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        var tt = new MultiblockTooltipBuilder();

        // spotless:off
        TstSharedFormat.setDefaultColor(EnumChatFormatting.GRAY);

        // #tr ManufacturingCenter_Tooltips_MachineType
        // # Manufacturing Center | Nine in One
        // #zh_CN 加工中心 | 九合一
        tt.addMachineType(TextEnums.tr("ManufacturingCenter_Tooltips_MachineType"))
            // #tr ManufacturingCenter_Tooltips_1
            // # A Combination of Machines.
            // #zh_CN 一些机器的组合。
            .addInfo(TextEnums.tr("ManufacturingCenter_Tooltips_1"))
            // #tr ManufacturingCenter_Tooltips_2
            // # Recipe voltages are limited by the §aMultiUse Core§7.
            // #zh_CN §a多功能核心§7限制配方电压等级。
            .addInfo(TextEnums.tr("ManufacturingCenter_Tooltips_2"))
            // #tr ManufacturingCenter_Tooltips_3
            // # Manufacturing Center cannot handle recipes over %s.
            // #zh_CN 加工中心不能制作%s及以上的配方。
            .addInfo(TextEnums.tr("ManufacturingCenter_Tooltips_3", TstSharedFormat.getTierName(VoltageIndex.UHV)))
            // #tr ManufacturingCenter_Tooltips_4
            // # §b20%§7 faster than single blocks.
            // #zh_CN 比单方块机器快§b20%§7。
            .addInfo(TextEnums.tr("ManufacturingCenter_Tooltips_4"))
            // #tr ManufacturingCenter_Tooltips_5
            // # Each Core Tier over %s gains §b%s§7 Speed Bonus comparing to single block machines.
            // #zh_CN 每级超过%s的核心等级获得§b%s§7的速度提升。
            .addInfo(
                TextEnums.tr(
                    "ManufacturingCenter_Tooltips_5",
                    TstSharedFormat.getTierName(LOWEST_CORE_TIER),
                    TstSharedFormat.percentage(SPEED_BONUS_FOR_CORE_TIER * 100)))
            // #tr ManufacturingCenter_Tooltips_6
            // # Each Core Tier over %s gains §b%s§7 EU/t Reduction.
            // #zh_CN 每级超过%s的核心等级获得§b%s§7的能量减免。
            .addInfo(
                TextEnums.tr(
                    "ManufacturingCenter_Tooltips_6",
                    TstSharedFormat.getTierName(LOWEST_CORE_TIER),
                    TstSharedFormat.percentage(EU_REDUCTION_FOR_CORE_TIER * 100)))
            // #tr ManufacturingCenter_Tooltips_7
            // # Max parallel is §b%sx§7 max voltage tier.
            // #zh_CN 最大并行为§b%sx§7最大电压等级。
            .addInfo(TextEnums.tr("ManufacturingCenter_Tooltips_7", MAX_PARALLEL_MODIFIER))
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, false)
            .addController("Front Center")
            .addOtherStructurePart("MultiUse Core", "At Center")
            .addCasingInfoMin("Multi-Use Casings", 6, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addSeparator()
            .addInfo(TextEnums.Author_Taskeren.toString())
            .toolTipFinisher(TextEnums.Mod_TwistSpaceTechnology.toString());

        // spotless:on
        return tt;
    }

    // endregion

    // region Misc

    @Override
    public @NotNull Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(
            RecipeMaps.compressorRecipes,
            RecipeMaps.latheRecipes,
            RecipeMaps.polarizerRecipes,
            RecipeMaps.fermentingRecipes,
            RecipeMaps.fluidExtractionRecipes,
            RecipeMaps.extractorRecipes,
            RecipeMaps.laserEngraverRecipes,
            RecipeMaps.autoclaveRecipes,
            RecipeMaps.fluidSolidifierRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    public String[] getInfoData() {
        List<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add(EnumChatFormatting.GOLD + "Machine-Mode: " + getMachineModeName());
        info.add(EnumChatFormatting.GOLD + "Core-Tier: " + GTUtility.getColoredTierNameFromTier((byte) coreTier));
        return info.toArray(new String[0]);
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        PlayerUtils.messagePlayer(
            aPlayer,
            String.format(StatCollector.translateToLocal("GT5U.MULTI_MACHINE_CHANGE"), getMachineModeName()));
    }

    private static int getMachineModesCount() {
        return MODES.size();
    }

    @Override
    public int getMachineMode() {
        return machineMode;
    }

    private ManufacturingMachineType getManufacturingMachineMode() {
        return MODES.get(getMachineMode());
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal(getManufacturingMachineMode().unlocalizedName);
    }

    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal(MODES.get(mode).unlocalizedName);
    }

    @Override
    public void setMachineMode(int index) {
        machineMode = index % getMachineModesCount();
    }

    @Override
    public int nextMachineMode() {
        return getMachineMode() + 1 % getMachineModesCount();
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.clear();
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_COMPRESSING);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_METAL); // TODO: Lathe
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_POLARIZER); // Magnetic
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID); // TODO: Fermenting
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID); // TODO: Fluid Extractor
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_METAL); // TODO: Extract
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_METAL); // TODO: Laser Engraver
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID); // TODO: Autoclave
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT); // TODO: Fluid Solidifier
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("mode", machineMode);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " "
                + EnumChatFormatting.WHITE
                + getMachineModeName(tag.getInteger("mode"))
                + EnumChatFormatting.RESET);
    }

    // endregion

    // region Data Persist

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    // endregion

    // region Texture

    public int getTextureIndex() {
        return TAE.getIndexFromPage(2, 2);
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TstMachineTextures.oMCAIndustrialMultiMachineActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TstMachineTextures.oMCAIndustrialMultiMachine;
    }

    @Override
    protected int getCasingTextureId() {
        return getTextureIndex();
    }

    // endregion

    // region Structure

    private static IStructureDefinition<TST_ManufacturingCenter> StructureDef = null;

    @Override
    public IStructureDefinition<TST_ManufacturingCenter> getStructureDefinition() {
        if (StructureDef == null) {
            StructureDef = StructureDefinition.<TST_ManufacturingCenter>builder()
                .addShape(
                    "main",
                    StructureUtility.transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "CAC", "CCC" }, { "CCC", "CCC", "CCC" } }))
                .addElement(
                    'C',
                    GTStructureUtility.buildHatchAdder(TST_ManufacturingCenter.class)
                        .atLeast(
                            HatchElement.InputBus,
                            HatchElement.OutputBus,
                            HatchElement.InputHatch,
                            HatchElement.OutputHatch,
                            HatchElement.Energy,
                            HatchElement.Maintenance,
                            HatchElement.Muffler)
                        .casingIndex(getTextureIndex())
                        .dot(1)
                        .buildAndChain(
                            onElementPass((te) -> te.casingCount++, ofBlock(ModBlocks.blockCasings3Misc, 2))))
                .addElement(
                    'A',
                    withChannel(
                        "core",
                        BlockMultiUseCore.ofMultiUseCore(-1, (te, tier) -> te.coreTier = tier, (te) -> te.coreTier)))
                .build();
        }

        return StructureDef;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingCount = 0;
        if (!checkPiece("main", 1, 1, 0)) return false;
        return mExoticEnergyHatches.isEmpty() && casingCount >= 6;
    }

    // endregion

}
