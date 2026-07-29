package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

public final class ItemMegaTreeFarmModeSymbol extends AbstractTstMetaItem {

    private IIcon backgroundIcon;
    private IIcon frameIcon;

    public ItemMegaTreeFarmModeSymbol() {
        super("MegaTreeFarmModeSymbol");
    }

    @Override
    public void registerIcons(net.minecraft.client.renderer.texture.IIconRegister register) {
        this.backgroundIcon = register
            .registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":MegaTreeFarmModeSymbol/mode_symbol_background");
        this.frameIcon = register.registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":MegaTreeFarmModeSymbol/frame");
        for (int meta : usedMetaIds) {
            iconMap.put(meta, backgroundIcon);
        }
        this.itemIcon = backgroundIcon;
    }

    public IIcon getBackgroundIcon() {
        return backgroundIcon;
    }

    public IIcon getFrameIcon() {
        return frameIcon;
    }
}
