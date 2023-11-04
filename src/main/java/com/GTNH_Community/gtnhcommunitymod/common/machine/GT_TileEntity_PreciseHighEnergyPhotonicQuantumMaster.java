package com.GTNH_Community.gtnhcommunitymod.common.machine;

import static com.GTNH_Community.gtnhcommunitymod.common.block.BasicBlocks.PhotonControllerUpgrade;
import static com.GTNH_Community.gtnhcommunitymod.common.block.blockClass.Casings.PhotonControllerUpgradeCasing.speedIncrement;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.BLUE_PRINT_INFO;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.ModName;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.StructureTooComplex;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_00;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_02;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_03;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_04;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_05;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_06;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_07;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_08;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_PhC_09;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textAroundController;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textCasingAdvIrPlated;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textCasingTT_0;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textCenterOfLRSides;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textFrontCenter;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textHighPowerCasingUDSides;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textUpgradeCasingAndLocation;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.GTNH_Community.gtnhcommunitymod.common.machine.recipeMap.GTCMRecipe;
import com.GTNH_Community.gtnhcommunitymod.util.TextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;

public class GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster>
    implements IConstructable, ISurvivalConstructable {

    // region ClassConstructors
    public GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(String aName) {
        super(aName);
    }

    // endregion

    // region Member Variables

    private boolean mode = false;

    private boolean enablePerfectOverclockSignal = false;
    private int totalSpeedIncrement = 0;

    public int getTotalSpeedIncrement() {
        return this.totalSpeedIncrement;
    }

    // endregion

    // region Structure

    private static final String STRUCTURE_PIECE_MAIN = "main";

    // spotless:off
    private final String[][] shape = new String[][] {
        { "               ", "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD", "               ", "               ", "               ", "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD", "     DDDDD     " },
        { "     DDDDD     ", "D   DCCCCCD   D", "DEEEEEEEEEEEEED", "DXXXXXXXXXXXXXD", "DXXXXXXXXXXXXXD", "DXXXXXXXXXXXXXD", "DEEEEEEEEEEEEED", "DDDDDCCCCCDDDDD", "     DAAAD     " },
        { "     DMMMD     ", "DDDDDCCCCCDDDDD", "DBBBBBBBBBBBBBD", " I           I ", " I           I ", " I           I ", "DBBBBBBBBBBBBBD", "DDDDDCCCCCDDDDD", "DDDDDDAAADDDDDD" },
        { "     DM~MD     ", "DDDDDCCCCCDDDDD", "DCCCCCCCCCCCCCD", " I           I ", " I           I ", " I           I ", "DCCCCCCCCCCCCCD", "DDDDDCCCCCDDDDD", "DAAAAAAAAAAAAAD" },
        { "     DMMMD     ", "DDDDDCCCCCDDDDD", "DBBBBBBBBBBBBBD", " I           I ", " I           I ", " I           I ", "DBBBBBBBBBBBBBD", "DDDDDCCCCCDDDDD", "DDDDDDAAADDDDDD" },
        { "     DDDDD     ", "D   DCCCCCD   D", "DEEEEEEEEEEEEED", "DXXXXXXXXXXXXXD", "DXXXXXXXXXXXXXD", "DXXXXXXXXXXXXXD", "DEEEEEEEEEEEEED", "DDDDDCCCCCDDDDD", "     DAAAD     " },
        { "               ", "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD", "               ", "               ", "               ", "DDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDD", "     DDDDD     " } };
    
    // spotless:on
    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 3;
    private final int depthOffSet = 0;

    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */
    @Override
    public IStructureDefinition<GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster> getStructureDefinition() {
        IStructureDefinition<GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster> Structure = StructureDefinition
            .<GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            .addElement('B', ofBlock(GregTech_API.sBlockCasings2, 5))
            .addElement('C', ofBlock(GregTech_API.sBlockCasings2, 9))
            .addElement('D', ofBlock(GregTech_API.sBlockCasings8, 7))
            .addElement('E', ofBlock(GregTech_API.sBlockCasings9, 1))
            .addElement(
                'M',
                GT_StructureUtility.buildHatchAdder(GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster.class)
                    .atLeast(Maintenance)
                    .dot(1)
                    .casingIndex(183)
                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
            .addElement(
                'I',
                GT_StructureUtility.buildHatchAdder(GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster.class)
                    .atLeast(InputBus, InputHatch, OutputBus, OutputHatch)
                    .dot(2)
                    .casingIndex(183)
                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
            .addElement(
                'X',
                GT_StructureUtility.buildHatchAdder(GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster.class)
                    .atLeast(Energy.or(ExoticEnergy))
                    .dot(3)
                    .casingIndex(1024)
                    .buildAndChain(sBlockCasingsTT, 0/* ofBlockUnlocalizedName("tectech","gt.blockcasingsTT",0) */))
            .addElement(
                'A',
                ofChain(
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[0], ofBlock(PhotonControllerUpgrade, 0)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[1], ofBlock(PhotonControllerUpgrade, 1)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[2], ofBlock(PhotonControllerUpgrade, 2)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[3], ofBlock(PhotonControllerUpgrade, 3)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[4], ofBlock(PhotonControllerUpgrade, 4)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[5], ofBlock(PhotonControllerUpgrade, 5)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[6], ofBlock(PhotonControllerUpgrade, 6)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[7], ofBlock(PhotonControllerUpgrade, 7)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[8], ofBlock(PhotonControllerUpgrade, 8)),
                    onElementPass(x -> x.totalSpeedIncrement += speedIncrement[9], ofBlock(PhotonControllerUpgrade, 9)),
                    onElementPass(
                        x -> x.totalSpeedIncrement += speedIncrement[10],
                        ofBlock(PhotonControllerUpgrade, 10)),
                    onElementPass(x -> {
                        x.totalSpeedIncrement += speedIncrement[11];
                        x.enablePerfectOverclockSignal = true;
                    }, ofBlock(PhotonControllerUpgrade, 11)),
                    onElementPass(x -> {
                        x.totalSpeedIncrement += speedIncrement[12];
                        x.enablePerfectOverclockSignal = true;
                    }, ofBlock(PhotonControllerUpgrade, 12)),
                    onElementPass(x -> {
                        x.totalSpeedIncrement += speedIncrement[13];
                        x.enablePerfectOverclockSignal = true;
                    }, ofBlock(PhotonControllerUpgrade, 13)),
                    ofBlock(GregTech_API.sBlockCasings8, 7)))
            .build();

        return Structure;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
            false,
            true);
    }

    // endregion

    // region Processing Logic

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        if (this.mode) {
            return GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes;
        }
        return GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            public ProcessingLogic enablePerfectOverclock() {
                if (enablePerfectOverclockSignal) {
                    return this.setOverclock(1, 2);
                }
                return this.setOverclock(2, 2);
            }

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setSpeedBoost((mode ? 10000F : 5000F) / (10000F + totalSpeedIncrement));
            }
        }.enablePerfectOverclock()
            .setMaxParallel(this.mode ? 16 : 256);
    }

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     *
     * @param aBaseMetaTileEntity
     * @param aStack
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.totalSpeedIncrement = 0;
        this.enablePerfectOverclockSignal = false;
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = !this.mode;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("PreciseHighEnergyPhotonicQuantumMaster.mode." + (this.mode ? 1 : 0)));
        }
    }

    // endregion

    // region General Overrides
    
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        
        aNBT.setBoolean("mode", mode);
        aNBT.setBoolean("enablePerfectOverclockSignal", enablePerfectOverclockSignal);
    }
    
    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        
        mode = aNBT.getBoolean("mode");
        enablePerfectOverclockSignal = aNBT.getBoolean("enablePerfectOverclockSignal");
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length - 1] = "Total Speed Increment: " + String.valueOf(this.totalSpeedIncrement);
        ret[origin.length] = "Enable Perfect Overclock: " + String.valueOf(this.enablePerfectOverclockSignal);
        return ret;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    // tooltips
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_PhC_MachineType)
            .addInfo(Tooltip_PhC_00)
            .addInfo(Tooltip_PhC_01)
            .addInfo(Tooltip_PhC_02)
            .addInfo(Tooltip_PhC_03)
            .addInfo(Tooltip_PhC_04)
            .addInfo(Tooltip_PhC_05)
            .addInfo(Tooltip_PhC_06)
            .addInfo(Tooltip_PhC_07)
            .addInfo(Tooltip_PhC_08)
            .addInfo(Tooltip_PhC_09)
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(15, 7, 9, false)
            .addController(textFrontCenter)
            .addCasingInfoRange(textCasingAdvIrPlated, 296, 347, false)
            .addCasingInfoRange(textCasingTT_0, 0, 78, false)
            .addCasingInfoRange(textUpgradeCasingAndLocation, 0, 25, false)
            .addInputHatch(textCenterOfLRSides, 2)
            .addOutputHatch(textCenterOfLRSides, 2)
            .addInputBus(textCenterOfLRSides, 2)
            .addOutputBus(textCenterOfLRSides, 2)
            .addMaintenanceHatch(textAroundController, 1)
            .addEnergyHatch(textHighPowerCasingUDSides, 3)
            .toolTipFinisher(ModName);
        return tt;
    }

    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_PreciseHighEnergyPhotonicQuantumMaster(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183) };
    }

    // endregion
}
