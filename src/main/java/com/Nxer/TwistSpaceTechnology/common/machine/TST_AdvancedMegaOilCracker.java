package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.EnablePerfectOverclock_AdvancedMegaOilCracker;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_AdvancedMegaOilCracker;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedBonus_AdvancedMegaOilCracker;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

public class TST_AdvancedMegaOilCracker extends GTCM_MultiMachineBase<TST_AdvancedMegaOilCracker> {

    // region Class Constructor
    public TST_AdvancedMegaOilCracker(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_AdvancedMegaOilCracker(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_AdvancedMegaOilCracker(this.mName);
    }

    // endregion

    // region Processing Logic
    public byte glassTier = 1;
    private HeatingCoilLevel coilLevel;

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("glassTier", glassTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        glassTier = aNBT.getByte("glassTier");
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.crackingRecipes;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FIRE;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return EnablePerfectOverclock_AdvancedMegaOilCracker || enablePerfectOverclock;
    }

    @Override
    protected float getSpeedBonus() {
        return SpeedBonus_AdvancedMegaOilCracker;
    }

    @Override
    public int getMaxParallelRecipes() {
        return Parallel_AdvancedMegaOilCracker;
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        // #tr MachineInfoData.GlassTier
        // # {\AQUA}Glass Tier
        // #zh_CN {\AQUA}玻璃等级
        ret[origin.length] = TextEnums.tr("MachineInfoData.GlassTier") + ": "
            + EnumChatFormatting.GOLD
            + this.glassTier;
        return ret;
    }

    // endregion

    // region Structure
    private final String STRUCTURE_PIECE_MAIN = "mainAdvancedMegaOilCracker";
    private final int horizontalOffSet = 6;
    private final int verticalOffSet = 6;
    private final int depthOffSet = 0;

    // spotless:off
    private final String[][] shapeMain = new String[][] {
        { " D         D ", "DDAAAAAAAAADD", " DAAAAAAAAAD ", " DAAADDDAAAD ", " DAAADDDAAAD ", " DAAADDDAAAD ", " DAAAAAAAAAD ", "DDAAAAAAAAADD", " D         D " },
        { " D         D ", "DAAAAAAAAAAAD", " A C C C C A ", " A C C C C A ", " A C C C C A ", " A C C C C A ", " A C C C C A ", "DAAAAAAAAAAAD", " D         D " },
        { " D         D ", "DAAAAAAAAAAAD", " A C C C C A ", " DFFFFFFFFFD ", " D C C C C D ", " DFFFFFFFFFD ", " A C C C C A ", "DAAAAAAAAAAAD", " D         D " },
        { " D         D ", "DAAAAAAAAAAAD", " A C C C C A ", " D C C C C D ", " D C C C C D ", " D C C C C D ", " A C C C C A ", "DAAAAAAAAAAAD", " D         D " },
        { " D         D ", "DAAAAAAAAAAAD", " A C C C C A ", " DFFFFFFFFFD ", " D C C C C D ", " DFFFFFFFFFD ", " A C C C C A ", "DAAAAAAAAAAAD", " D         D " },
        { " D         D ", "DAAAAAAAAAAAD", " A C C C C A ", " A C C C C A ", " A C C C C A ", " A C C C C A ", " A C C C C A ", "DAAAAAAAAAAAD", " D         D " },
        { "DDDDDD~DDDDDD", "DDDDDDDDDDDDD", "DDDDDDDDDDDDD", "DDDDDDDDDDDDD", "DDDDDDDDDDDDD", "DDDDDDDDDDDDD", "DDDDDDDDDDDDD", "DDDDDDDDDDDDD", "DDDDDDDDDDDDD" } };
    // spotless:on
    private static IStructureDefinition<TST_AdvancedMegaOilCracker> STRUCTURE_DEFINITION = null;

    /*
     * Blocks:
     * A -> ofBlock...(BW_GlasBlocks2, 0, ...); //glass
     * B -> ofBlock...(gt.blockcasings4, 1, ...);
     * C -> ofBlock...(gt.blockcasings5, 13, ...); //coil
     * D -> ofBlock...(tile.stone, 0, ...); // bus, energy
     * E -> ofBlock...(tile.stonebrick, 0, ...); // hatch
     * F -> ofFrame...(Materials.Vanadium);
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.coilLevel = HeatingCoilLevel.None;
        this.glassTier = 0;
        this.enablePerfectOverclock = false;
        clearHatches();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (this.glassTier <= 0 || coilLevel == HeatingCoilLevel.None) return false;
        this.euModifier = 1F / (coilLevel.getTier() + 1);
        this.enablePerfectOverclock = coilLevel.getTier() >= HeatingCoilLevel.UXV.getTier();
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public IStructureDefinition<TST_AdvancedMegaOilCracker> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_AdvancedMegaOilCracker>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addElement(
                    'A',
                    withChannel(
                        "glass",
                        BorosilicateGlass.ofBoroGlass(
                            (byte) 0,
                            (byte) 1,
                            Byte.MAX_VALUE,
                            (te, t) -> te.glassTier = t,
                            te -> te.glassTier)))
                .addElement(
                    'C',
                    withChannel(
                        "coil",
                        ofCoil(TST_AdvancedMegaOilCracker::setCoilLevel, TST_AdvancedMegaOilCracker::getCoilLevel)))
                .addElement(
                    'D',
                    HatchElementBuilder.<TST_AdvancedMegaOilCracker>builder()
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_AdvancedMegaOilCracker::addToMachineList)
                        .dot(1)
                        .casingIndex(49)
                        .buildAndChain(GregTechAPI.sBlockCasings4, 1))
                .addElement('F', ofFrame(Materials.Vanadium))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // endregion

    // region General
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltips_AdvancedMegaOilCracker_MachineType)
            .addInfo(TextLocalization.Tooltips_AdvancedMegaOilCracker_Controller)
            .addInfo(TextLocalization.Tooltips_AdvancedMegaOilCracker_01)
            .addInfo(TextLocalization.Tooltips_AdvancedMegaOilCracker_02)
            .addInfo(TextLocalization.Tooltips_AdvancedMegaOilCracker_03)
            .addInfo(TextLocalization.Tooltips_AdvancedMegaOilCracker_04)
            .addInfo(TextLocalization.Tooltips_AdvancedMegaOilCracker_05)
            .addInfo(TextLocalization.Tooltips_AdvancedMegaOilCracker_06)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addInputHatch(TextLocalization.textUseBlueprint, 2)
            .addOutputHatch(TextLocalization.textUseBlueprint, 2)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .addStructureInfo(TextLocalization.Tooltip_DoNotNeedMaintenance)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49) };
    }
}
