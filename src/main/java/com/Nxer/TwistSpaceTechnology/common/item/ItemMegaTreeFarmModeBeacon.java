package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

public final class ItemMegaTreeFarmModeBeacon extends AbstractTstMetaItem {

    private IIcon backgroundIcon;
    private IIcon frameIcon;

    public ItemMegaTreeFarmModeBeacon() {
        super("MegaTreeFarmModeBeacon");
    }

    @Override
    public void registerIcons(net.minecraft.client.renderer.texture.IIconRegister register) {
        this.backgroundIcon = register
            .registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":MegaTreeFarmModeBeacon/mode_beacon_background");
        this.frameIcon = register.registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":MegaTreeFarmModeBeacon/frame");
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
