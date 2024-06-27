package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTech_API.sBlockCasings1;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_SteamMultiBase;

public class TST_LargeSteamForgeHammer extends GregtechMeta_SteamMultiBase<TST_LargeSteamForgeHammer>
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

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.33F)
                    .setSpeedBoost(1.5F);
            }

        }.setMaxParallel(getMaxParallelRecipes());
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.hammerRecipes;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        fixAllMaintenanceIssue();
        return checkPiece(mName, 1, 1, 0);
    }

    private static IStructureDefinition<TST_LargeSteamForgeHammer> STRUCTURE_DEFINITION = null;

    @Override
    protected GT_RenderedTexture getFrontOverlay() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_HAMMER);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlayActive() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_HAMMER_ACTIVE);
    }

    @Override
    public String getMachineType() {
        return "Forge Hammer";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 16;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
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
                        ofBlock(sBlockCasings1, 10)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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

}
