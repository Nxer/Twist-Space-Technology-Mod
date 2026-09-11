package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

public final class ItemEcoSphereUpgrade extends AbstractTstMetaItem {

    private IIcon backgroundIcon;
    private IIcon frameIcon;

    public ItemEcoSphereUpgrade() {
        super("EcoSphereUpgrade");
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.backgroundIcon = register
            .registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":EcoSphereModeBeacon/upgrade_background");
        this.frameIcon = register
            .registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":EcoSphereModeBeacon/upgrade_frame");
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
