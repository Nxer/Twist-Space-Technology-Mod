package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TST_SteamMultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;

public class TST_LargeSteamForgeHammer extends TST_SteamMultiMachineBase<TST_LargeSteamForgeHammer>
    implements ISurvivalConstructable {

    // region Class Constructor
    public TST_LargeSteamForgeHammer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_LargeSteamForgeHammer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_LargeSteamForgeHammer(this.mName);
    }

    // endregion
    protected int steamCasingTier = 1;
    protected int parallel = 1;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("steamCasingTier", steamCasingTier);
        aNBT.setInteger("parallel", parallel);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        steamCasingTier = aNBT.getInteger("steamCasingTier");
        parallel = aNBT.getInteger("parallel");
    }

    @Override
    public int getMaxParallelRecipes() {
        return parallel;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.hammerRecipes;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        steamCasingTier = -1;
        if (!checkPiece(mName, 1, 1, 0)) return false;
        if (steamCasingTier < 1) return false;
        updateHatchTexture();

        parallel = 16 * steamCasingTier;
        return true;
    }

    protected static IStructureDefinition<TST_LargeSteamForgeHammer> STRUCTURE_DEFINITION = null;

    @Override
    public String getMachineType() {
        return "Forge Hammer";
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    protected void updateHatchTexture() {
        int casingIndex = getCasingTextureID(steamCasingTier);
        for (MTEHatch h : mSteamInputs) h.updateTexture(casingIndex);
        for (MTEHatch h : mSteamOutputs) h.updateTexture(casingIndex);
        for (MTEHatch h : mSteamInputFluids) h.updateTexture(casingIndex);
    }

    public void setSteamCasingTier(int steamCasingTier) {
        this.steamCasingTier = steamCasingTier;
    }

    public int getSteamCasingTier() {
        return steamCasingTier;
    }

    @Override
    public IStructureDefinition<TST_LargeSteamForgeHammer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_LargeSteamForgeHammer>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildSteamInput(TST_LargeSteamForgeHammer.class).casingIndex(10)
                            .dot(1)
                            .build(),

                        buildHatchAdder(TST_LargeSteamForgeHammer.class)
                            .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                            .casingIndex(10)
                            .dot(1)
                            .build(),

                        ofBlocksTiered(
                            TST_LargeSteamForgeHammer::checkSteamCasingTier,
                            TST_LargeSteamForgeHammer.STEAM_CASING_LIST,
                            -1,
                            TST_LargeSteamForgeHammer::setSteamCasingTier,
                            TST_LargeSteamForgeHammer::getSteamCasingTier)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_LargeSteamForgeHammer_MachineType)
            .addInfo(TextLocalization.Tooltip_LargeSteamForgeHammer_Controller)
            .addInfo(TextLocalization.Tooltip_LargeSteamForgeHammer_01)
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController(TextLocalization.textFrontCenter)
            .addInputBus(TextLocalization.textAnyCasing, 2)
            .addOutputBus(TextLocalization.textAnyCasing, 2)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    protected ITexture getFrontOverlay() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_STEAM_HAMMER)
            .extFacing()
            .build();
    }

    @Override
    protected ITexture getFrontOverlayActive() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_STEAM_HAMMER_ACTIVE)
            .extFacing()
            .build();
    }

    @Override
    public int getTierRecipes() {
        // todo
        return 0;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        steamCasingTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) steamCasingTier;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {

        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID(steamCasingTier)),
                aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID(steamCasingTier)) };
    }

    /**
     * No more machine error
     */
    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    /**
     * No more machine error
     */
    @Override
    public void checkMaintenance() {}

    /**
     * No more machine error
     */
    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    /**
     * No more machine error
     */
    @Override
    public final boolean shouldCheckMaintenance() {
        return false;
    }

}
