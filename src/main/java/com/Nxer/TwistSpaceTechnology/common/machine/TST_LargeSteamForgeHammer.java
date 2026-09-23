package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.internal_structure_issue;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TST_SteamMultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTSharedLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;

@SkipGenerateDescription
public class TST_LargeSteamForgeHammer extends TST_SteamMultiMachineBase<TST_LargeSteamForgeHammer>
    implements ISurvivalConstructable {

    // region Class Constructor
    public TST_LargeSteamForgeHammer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.NXER);
    }

    public TST_LargeSteamForgeHammer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_LargeSteamForgeHammer(this.mName);
    }
    // endregion

    // region Structure
    protected static IStructureDefinition<TST_LargeSteamForgeHammer> STRUCTURE_DEFINITION = null;

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
                            .hint(1)
                            .build(),

                        buildHatchAdder(TST_LargeSteamForgeHammer.class)
                            .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                            .casingIndex(10)
                            .hint(1)
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        steamCasingTier = -1;
        if (!checkPiece(mName, 1, 1, 0, errors)) return;
        if (steamCasingTier < 1) {
            errors.add(internal_structure_issue);
            return;
        }
        updateHatchTexture();
        parallel = 16 * steamCasingTier;
    }
    // endregion

    // region Processing Logic
    protected int steamCasingTier = 1;
    protected int parallel = 1;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.hammerRecipes;
    }

    @Override
    public int getMaxParallelRecipes() {
        return parallel;
    }

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

    /**
     * No more machine error
     */
    @Override
    public void checkMaintenance() {}

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FORGE_HAMMER;
    }

    @Override
    public String getMachineType() {
        return "Forge Hammer";
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
    public int getTierRecipes() {
        // todo
        return 0;
    }

    @Override
    protected boolean isHighPressure() {
        return steamCasingTier > 1;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        steamCasingTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) steamCasingTier;
    }

    /**
     * No more machine error
     */
    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    // endregion

    // region NBT

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

    // endregion

    // region Textures

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_FORGE_HAMMER_ACTIVE;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_STEAM_FORGE_HAMMER;
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr tst.common.machine.LargeSteamForgeHammer.tooltip.machine_type
        // # Forge Hammer
        // #zh_CN 锻造锤
        tt.addMachineType(TSTUtils.tr("tst.common.machine.LargeSteamForgeHammer.tooltip.machine_type"))
            // #tr tst.common.machine.LargeSteamForgeHammer.tooltip.controller
            // # Controller block for the Large Steam Forge Hammer
            // #zh_CN 大型蒸汽锻造锤的控制器方块
            .addInfo(TSTUtils.tr("tst.common.machine.LargeSteamForgeHammer.tooltip.controller"))
            // #tr tst.common.machine.LargeSteamForgeHammer.tooltip.info.01
            // # He has a hammer. Who has the Sickle?
            // #zh_CN 他有一柄锤子. 谁有镰刀?
            .addInfo(TSTUtils.tr("tst.common.machine.LargeSteamForgeHammer.tooltip.info.01"))
            .beginStructureBlock(3, 3, 3, true)
            .addController(TSTSharedLocalization.Structure.textFrontCenter)
            .addInputBus(TSTSharedLocalization.Structure.textAnyCasing, 2)
            .addOutputBus(TSTSharedLocalization.Structure.textAnyCasing, 2)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
