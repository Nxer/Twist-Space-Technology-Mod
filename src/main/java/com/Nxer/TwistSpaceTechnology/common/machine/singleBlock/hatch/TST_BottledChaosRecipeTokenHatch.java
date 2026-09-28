package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

@IMetaTileEntity.SkipGenerateDescription
public class TST_BottledChaosRecipeTokenHatch extends TST_BottledChaosAuthorizationHatch {

    private static final String[] DESCRIPTION = { "bottled_chaos.recipe_token_hatch.tooltip.0",
        "bottled_chaos.recipe_token_hatch.tooltip.1" };

    public TST_BottledChaosRecipeTokenHatch(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, DESCRIPTION);
    }

    public TST_BottledChaosRecipeTokenHatch(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, description, textures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new TST_BottledChaosRecipeTokenHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected boolean isAuthorizationItem(ItemStack stack) {
        return ItemBottledChaosCredential.getCapacity(stack) > 0;
    }
}
