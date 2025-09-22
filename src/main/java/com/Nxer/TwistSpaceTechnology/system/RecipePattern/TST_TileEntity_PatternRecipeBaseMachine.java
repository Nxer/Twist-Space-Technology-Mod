package com.Nxer.TwistSpaceTechnology.system.RecipePattern;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public abstract class TST_TileEntity_PatternRecipeBaseMachine extends TTMultiblockBase {

    private final ArrayList<TST_TileEntity_Hatch_UltimateMEIO> ultimateIOHatchesList = new ArrayList<>();

    protected TST_TileEntity_PatternRecipeBaseMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected TST_TileEntity_PatternRecipeBaseMachine(String aName) {
        super(aName);
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        return super.checkProcessing_EM();
    }

    public boolean addUltimateMEIOHatchToHatchList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof TST_TileEntity_Hatch_UltimateMEIO hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return ultimateIOHatchesList.add(hatch);
        }
        return false;
    }

}
