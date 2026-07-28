package com.Nxer.TwistSpaceTechnology.common.item;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

public final class ItemMegaTreeFarmModeSymbol extends AbstractTstMetaItem {

    public ItemMegaTreeFarmModeSymbol() {
        super("MegaTreeFarmModeSymbol");
    }

    @Override
    public void registerIcons(net.minecraft.client.renderer.texture.IIconRegister register) {
        this.iconMap = registerAllVariantIcons(
            register,
            meta -> TwistSpaceTechnology.RESOURCE_ROOT_ID + ":MetaItem01/0");
        this.itemIcon = iconMap.get(0);
    }
}
