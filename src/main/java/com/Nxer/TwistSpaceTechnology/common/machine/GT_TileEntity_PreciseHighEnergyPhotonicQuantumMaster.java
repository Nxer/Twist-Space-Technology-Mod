package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.PhotonControllerUpgrade;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.PhotonControllerUpgradeCasing;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

@SkipGenerateDescription
public class GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster
    extends GTCM_MultiMachineBase<GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster> {

    // region Class Constructor
    public GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(this.mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "main";

    // spotless:off
    private static final String[][] shape = new String[][] {
        { "               ", "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD", "               ", "               ", "               ", "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD", "     DDDDD     " },
        { "     DDDDD     ", "D   DCCCCCD   D", "DEEEEEEEEEEEEED", "DXXXXXXXXXXXXXD", "DXXXXXXXXXXXXXD", "DXXXXXXXXXXXXXD", "DEEEEEEEEEEEEED", "DDDDDCCCCCDDDDD", "     DAAAD     " },
        { "     DMMMD     ", "DDDDDCCCCCDDDDD", "DBBBBBBBBBBBBBD", " I           I ", " I           I ", " I           I ", "DBBBBBBBBBBBBBD", "DDDDDCCCCCDDDDD", "DDDDDDAAADDDDDD" },
        { "     DM~MD     ", "DDDDDCCCCCDDDDD", "DCCCCCCCCCCCCCD", " I           I ", " I           I ", " I           I ", "DCCCCCCCCCCCCCD", "DDDDDCCCCCDDDDD", "DAAAAAAAAAAAAAD" },
        { "     DMMMD     ", "DDDDDCCCCCDDDDD", "DBBBBBBBBBBBBBD", " I           I ", " I           I ", " I           I ", "DBBBBBBBBBBBBBD", "DDDDDCCCCCDDDDD", "DDDDDDAAADDDDDD" },
        { "     DDDDD     ", "D   DCCCCCD   D", "DEEEEEEEEEEEEED", "DXXXXXXXXXXXXXD", "DXXXXXXXXXXXXXD", "DXXXXXXXXXXXXXD", "DEEEEEEEEEEEEED", "DDDDDCCCCCDDDDD", "     DAAAD     " },
        { "               ", "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD", "               ", "               ", "               ", "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD", "     DDDDD     " } };
    // spotless:on

    private static final int horizontalOffSet = 7;
    private static final int verticalOffSet = 3;
    private static final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster> STRUCTURE_DEFINITION = null;

    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */
    @Override
    public IStructureDefinition<GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 5))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 9))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 7))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings9, 1))
                .addElement(
                    'M',
                    GTStructureUtility.buildHatchAdder(GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster.class)
                        .atLeast(Maintenance)
                        .hint(1)
                        .casingIndex(183)
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'I',
                    GTStructureUtility.buildHatchAdder(GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster.class)
                        .atLeast(InputBus, InputHatch, OutputBus, OutputHatch)
                        .hint(2)
                        .casingIndex(183)
                        .buildAndChain(GregTechAPI.sBlockCasings8, 7))
                .addElement(
                    'X',
                    GTStructureUtility.buildHatchAdder(GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster.class)
                        .atLeast(Energy.or(ExoticEnergy))
                        .hint(3)
                        .casingIndex(1024)
                        .buildAndChain(sBlockCasingsTT, 0))
                .addElement(
                    'A',
                    ofChain(
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[0],
                            ofBlock(PhotonControllerUpgrade, 0)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[1],
                            ofBlock(PhotonControllerUpgrade, 1)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[2],
                            ofBlock(PhotonControllerUpgrade, 2)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[3],
                            ofBlock(PhotonControllerUpgrade, 3)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[4],
                            ofBlock(PhotonControllerUpgrade, 4)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[5],
                            ofBlock(PhotonControllerUpgrade, 5)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[6],
                            ofBlock(PhotonControllerUpgrade, 6)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[7],
                            ofBlock(PhotonControllerUpgrade, 7)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[8],
                            ofBlock(PhotonControllerUpgrade, 8)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[9],
                            ofBlock(PhotonControllerUpgrade, 9)),
                        onElementPass(
                            x -> x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[10],
                            ofBlock(PhotonControllerUpgrade, 10)),
                        onElementPass(x -> {
                            x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[11];
                            x.enablePerfectOverclock = true;
                        }, ofBlock(PhotonControllerUpgrade, 11)),
                        onElementPass(x -> {
                            x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[12];
                            x.enablePerfectOverclock = true;
                        }, ofBlock(PhotonControllerUpgrade, 12)),
                        onElementPass(x -> {
                            x.totalSpeedIncrement += PhotonControllerUpgradeCasing.speedIncrement[13];
                            x.enablePerfectOverclock = true;
                        }, ofBlock(PhotonControllerUpgrade, 13)),
                        ofBlock(GregTechAPI.sBlockCasings8, 7)))
                .build();
        }
        return STRUCTURE_DEFINITION;
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

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     *
     * @param aBaseMetaTileEntity
     * @param aStack
     */
    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        this.totalSpeedIncrement = 0;
        this.enablePerfectOverclock = false;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet, errors)) {
            return;
        }

        speedBonus = 10000F / (10000F + totalSpeedIncrement);

    }
    // endregion

    // region Processing Logic
    protected int totalSpeedIncrement = 0;

    public static final UITexture[] tMachineModeIcons = new UITexture[] {
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_COMPRESSING, GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY };

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (machineMode == 1) {
            return GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipeMap;
        }
        return RecipeMaps.laserEngraverRecipes;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays
            .asList(GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipeMap, RecipeMaps.laserEngraverRecipes);
    }

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Laser Engraver
         * 1 - Photon Manipulator
         */
        return 2;
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return tMachineModeIcons;
    }

    @Override
    public String getMachineModeName() {
        // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.mode.0
        // # Mode: Laser Engraver
        // #zh_CN 激光蚀刻机模式

        // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.mode.1
        // # Mode: Photon Manipulator
        // #zh_CN 光子掌控者模式
        return StatCollector
            .translateToLocal("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.mode." + machineMode);
    }

    @Override
    public int getMaxParallelRecipes() {
        return machineMode == 1 ? Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster
            : Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster;
    }

    @Override
    protected float getSpeedBonus() {
        return speedBonus / (machineMode == 1 ? SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster
            : SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster);
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = "Total Speed Increment: " + this.totalSpeedIncrement;
        ret[origin.length + 1] = "Enable" + EnumChatFormatting.GOLD
            + " Perfect Overclock"
            + EnumChatFormatting.RESET
            + ": "
            + this.enablePerfectOverclock;
        return ret;
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setBoolean("mode", machineMode == 1);
        aNBT.setInteger("totalSpeedIncrement", totalSpeedIncrement);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        machineMode = aNBT.getBoolean("mode") ? 1 : 0;
        totalSpeedIncrement = aNBT.getInteger("totalSpeedIncrement");
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183) };
    }

    // endregion

    // region Tooltip

    // tooltips
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.machine_type
        // # Photon Controller/Laser Engraver
        // #zh_CN 光子掌控者/激光蚀刻机
        tt.addMachineType(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.machine_type"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.01
            // # Controller block for the Precise High-Energy Photonic Quantum Master
            // #zh_CN 精密高能光量子掌控者的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.01"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.02
            // # {\BLUE}Prism tank in order, sir.
            // #zh_CN {\BLUE}Prism tank in order, sir.
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.02"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.03
            // # Control Photons on the scale of 10⁻² meters.
            // #zh_CN 在10¯¹² m 尺度上掌控光子.
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.03"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.04
            // # Install Photonic Intensifier on the back side of the structure to dramatically increase production speeds.
            // #zh_CN 可以在机器背面对应位置安装光量子增幅器,大幅提高处理速度.
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.04"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.05
            // # Multi upgrade modules can be stacked. Also can be uninstalled. Replace using normal Casing.
            // #zh_CN 多个光量子增幅器效果可以叠加,当然也可以不安装,使用强化镀铱机械方块占位.
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.05"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.06
            // # Use screwdriver to change mode.
            // #zh_CN 使用螺丝刀切换模式.
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.06"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.07
            // # {\GOLD}Photon Controller mode:
            // #zh_CN {\GOLD}光子掌控者模式:
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.07"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.08
            // # {\AQUA}16x{\GRAY} Parallel
            // #zh_CN {\AQUA}16x{\GRAY} 并行
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.08"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.09
            // # {\GOLD}Laser Engraver mode:
            // #zh_CN {\GOLD}激光蚀刻机模式:
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.09"))
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.10
            // # {\AQUA}256x{\GRAY} Parallel | Extra reduce {\RED}50%{\GRAY} recipe time spent
            // #zh_CN {\AQUA}256x{\GRAY} 并行 | 额外降低{\RED}50%{\GRAY}耗时
            .addInfo(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.info.10"))
            .beginStructureBlock(15, 7, 9, false)
            .addController(TSTSharedLocalization.Structure.textFrontCenter)
            .addCasingInfoRange(TSTSharedLocalization.Structure.textCasingAdvIrPlated, 296, 347, false)
            .addCasingInfoRange(TSTSharedLocalization.Structure.textCasingTT_0, 0, 78, false)
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.structure.01
            // # Upgrade module casing at backside area wrapped by AdvIrPlated Casing
            // #zh_CN Upgrade module casing at backside area wrapped by AdvIrPlated Casing
            .addCasingInfoRange(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.structure.01"), 0, 25, false)
            .addInputHatch(TSTSharedLocalization.Structure.textCenterOfLRSides, 2)
            .addOutputHatch(TSTSharedLocalization.Structure.textCenterOfLRSides, 2)
            .addInputBus(TSTSharedLocalization.Structure.textCenterOfLRSides, 2)
            .addOutputBus(TSTSharedLocalization.Structure.textCenterOfLRSides, 2)
            // #tr tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.structure.02
            // # High Power Casing area of up and down side
            // #zh_CN 上下两侧的超能机械方块区域
            .addEnergyHatch(TSTUtils.tr("tst.common.machine.PreciseHighEnergyPhotonicQuantumMaster.tooltip.structure.02"), 3)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

    // region Hatch Registration

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    // endregion

}
