package com.Nxer.TwistSpaceTechnology.common.machine;

import com.github.bartimaeusnek.crossmod.galacticgreg.GT_TileEntity_VoidMiner_Base;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class TST_MegaVoidMiner extends GT_TileEntity_VoidMiner_Base {

    // region Class Constructor
    public TST_MegaVoidMiner(int aID, String aName, String aNameRegional, int tier) {
        super(aID, aName, aNameRegional, tier);
    }

    public TST_MegaVoidMiner(String aName, int tier) {
        super(aName, tier);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaVoidMiner(this.mName, this.TIER_MULTIPLIER);
    }
    // endregion

    // region Processing Logic

    // endregion

    // region Structure

    // endregion

    // region General

    @Override
    protected ItemList getCasingBlockItem() {
        return null;
    }

    @Override
    protected Materials getFrameMaterial() {
        return null;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 0;
    }

}
