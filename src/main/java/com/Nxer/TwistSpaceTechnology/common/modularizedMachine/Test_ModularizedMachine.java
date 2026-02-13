package com.Nxer.TwistSpaceTechnology.common.modularizedMachine;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.AllModule;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.MultiExecutionCoreMachineSupportAllModuleBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class Test_ModularizedMachine extends MultiExecutionCoreMachineSupportAllModuleBase<Test_ModularizedMachine> {

    // region Class Constructor
    public Test_ModularizedMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public Test_ModularizedMachine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new Test_ModularizedMachine(this.mName);
    }

    // endregion

    // region Processing Logic

    // TODO delete this method
    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.wiremillRecipes;
    }

    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final String[][] shape = new String[][] { { "AAA", "AAA", "AAA" }, { "A~A", "AAA", "AAA" },
        { "AAA", "AAA", "AAA" } };
    private static final int horizontalOffSet = 1;
    private static final int verticalOffSet = 1;
    private static final int depthOffSet = 0;
    private static IStructureDefinition<Test_ModularizedMachine> STRUCTURE_DEFINITION;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
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
     * Tier of this machine, setting from the casing block.
     */
    public int tierMachine = 0;

    /**
     * Save and load NBT data.
     */
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("tierMachine", tierMachine);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tierMachine = aNBT.getInteger("tierMachine");
    }

    /**
     * Show custom information when player use scanner right-click this machine.
     */
    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = "tierMachine = " + tierMachine;

        return ret;
    }

    /**
     * To update hatches' texture after checkMachine.
     */
    protected void updateHatchTexture() {
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mInputBusses) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mEnergyHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mOutputBusses) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mInputHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mOutputHatches) h.updateTexture(getCasingTextureID());
        for (IModularHatch h : allModularHatches) ((MTEHatch) h).updateTexture(getCasingTextureID());
    }

    /**
     * Package a method to get Texture ID for hatches from machine tier.
     */
    private int getCasingTextureID() {
        if (tierMachine > 1) return ((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(10);
        return ((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0);
    }

    /**
     * The method checkMachine in this custom base class. Same as the origin checkMachine.
     */
    @Override
    public boolean checkMachineMM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tierMachine = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (tierMachine == 0) return false;
        updateHatchTexture();
        return true;
    }

    @Override
    protected boolean canMultiplyModularHatchType() {
        return true;
    }

    @Override
    public IStructureDefinition<Test_ModularizedMachine> getStructureDefinition() {

        // The STRUCTURE_DEFINITION should be design only one time.
        if (STRUCTURE_DEFINITION == null) {

            // Make a variable to hold the StructureElement of tiered part
            IStructureElement<Test_ModularizedMachine> tierBlockElement =

                // `withChannel(String channelName, StructureElement element)`
                // To let player set machine tier more conveniently if machine has multiple tier settings.
                withChannel(
                    "tier",
                    ofBlocksTiered(

                        // Method first input :
                        // A method to confirm this tier from the structure block
                        // (Block block, int meta) -> return (int) tier;
                        (b, m) -> {
                            if (b == GregTechAPI.sBlockCasings2 && m == 0) {
                                return 1;
                            } else if (b == GregTechAPI.sBlockCasings4 && m == 10) {
                                return 2;
                            }
                            return 0;
                        },
                        // --------------------------------------------------------

                        // Method second input :
                        // An ordered List contains all blocks in tier.
                        // List< Pair<Block block, Integer meta> >
                        ImmutableList
                            .of(Pair.of(GregTechAPI.sBlockCasings2, 0), Pair.of(GregTechAPI.sBlockCasings4, 10)),
                        // --------------------------------------------------------

                        // Method third input :
                        // If check a block not in tier list, return this value.
                        -1,
                        // --------------------------------------------------------

                        // Method fourth input :
                        // Setter method.
                        // (BaseMachineClass thisMachine, int tier) -> thisMachine.tier = tier;
                        (m, t) -> m.tierMachine = t,
                        // --------------------------------------------------------

                        // Method fourth input :
                        // Getter method.
                        // BaseMachineClass thisMachine -> return thisMachine.tier;
                        m -> m.tierMachine));

            STRUCTURE_DEFINITION = StructureDefinition.<Test_ModularizedMachine>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    HatchElementBuilder.<Test_ModularizedMachine>builder()
                        .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy), AllModule)
                        .adder(Test_ModularizedMachine::addToMachineList)
                        .casingIndex(TstBlocks.MetaBlockCasing01.getTextureIndex(0))
                        .dot(1)
                        .buildAndChain(tierBlockElement))
                .build();
        }

        return STRUCTURE_DEFINITION;
    }
    // endregion

    // region General

    private static MultiblockTooltipBuilder tooltip;

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        if (tooltip == null) {
            tooltip = new MultiblockTooltipBuilder();
            tooltip.addMachineType("test")
                .addInfo("testing")
                .addSeparator()
                .addInfo(TextLocalization.StructureTooComplex)
                .addInfo(TextLocalization.BLUE_PRINT_INFO)
                .beginStructureBlock(3, 3, 3, false)
                .addInputHatch(TextLocalization.textUseBlueprint, 1)
                .addOutputHatch(TextLocalization.textUseBlueprint, 1)
                .addInputBus(TextLocalization.textUseBlueprint, 2)
                .addOutputBus(TextLocalization.textUseBlueprint, 2)
                .addEnergyHatch(TextLocalization.textUseBlueprint, 3)
                .toolTipFinisher(TextLocalization.ModName);

        }
        return tooltip;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

}
