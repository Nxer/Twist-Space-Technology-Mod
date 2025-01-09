package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.multiuse.BlockMultiUseCore;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.github.bsideup.jabel.Desugar;
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
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class TST_ManufacturingCenter extends GTPPMultiBlockBase<TST_ManufacturingCenter>
    implements ISurvivalConstructable {

    public static final int LOWEST_CORE_TIER = 5;

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

    @Desugar
    private record ManufacturingMachineType(String typeName, RecipeMap<?> recipeMap, String unlocalizedName) {

        public static ManufacturingMachineType of(RecipeMap<?> recipeMap) {
            var recipeMapNameParts = recipeMap.unlocalizedName.split("\\.");
            return new ManufacturingMachineType(
                recipeMapNameParts[recipeMapNameParts.length - 1],
                recipeMap,
                recipeMap.unlocalizedName);
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
        return (2 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private long getMaxVoltageTierAtCurrentMode() {
        return GTValues.V[coreTier];
    }

    private float getSpeedBonusAtCurrentCore() {
        return (float) 1 / (1 + 0.5f * (coreTier - LOWEST_CORE_TIER));
    }

    private double getEuModifierAtCurrentCore() {
        return 1.0 - (0.2 * Math.max(0, coreTier - LOWEST_CORE_TIER - 2));
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
                if (recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0) > 0) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(
            Math.min(getMaxVoltageTierAtCurrentMode(), GTUtility.roundUpVoltage(this.getMaxInputVoltage())));
        logic.setAvailableAmperage(1);
        logic.setSpeedBonus(getSpeedBonusAtCurrentCore());
        logic.setEuModifier(getEuModifierAtCurrentCore());
    }

    // endregion

    // region Tooltips

    @Override
    public String getMachineType() {
        return "Nine in One";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        var tt = new MultiblockTooltipBuilder();

        tt.addMachineType(getMachineType())
            .addInfo("A Combination of Machines.")
            .addInfo("Voltages are limited by the MultiUse Core.")
            .addInfo(
                "Manufacturing Center cannot handle recipes over " + GTUtility.getColoredTierNameFromTier((byte) 9)
                    + EnumChatFormatting.GRAY
                    + ".")
            .addInfo(
                "Each core tier over " + GTUtility.getColoredTierNameFromTier((byte) 5)
                    + EnumChatFormatting.GRAY
                    + " gains 50% speed bonus comparing to single block machines.")
            .addInfo(
                GTUtility.getColoredTierNameFromTier((byte) 7) + EnumChatFormatting.GRAY
                    + " core saves 20% EU energy, "
                    + GTUtility.getColoredTierNameFromTier((byte) 8)
                    + EnumChatFormatting.GRAY
                    + " core saves 40% EU energy.")
            .addInfo("Max Parallel is limited by 2x Max Voltage.")
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
            .toolTipFinisher();

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
        return TexturesGtBlock.oMCAIndustrialMultiMachineActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialMultiMachine;
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
                            HatchElement.Energy.or(HatchElement.ExoticEnergy),
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
        return checkPiece("main", 1, 1, 0) && casingCount >= 6 && checkHatch();
    }

    // endregion

}
