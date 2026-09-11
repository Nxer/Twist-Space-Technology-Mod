package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

public final class ItemEcoSphereModeBeacon extends AbstractTstMetaItem {

    private IIcon backgroundIcon;
    private IIcon frameIcon;

    public ItemEcoSphereModeBeacon() {
        super("EcoSphereModeBeacon");
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String displayName = super.getItemStackDisplayName(itemStack);
        int meta = itemStack.getItemDamage();
        // Odd metas keep the original rainbow rule; cloning tier 3 is the explicit even-meta exception.
        if ((meta & 1) == 0 && meta != 8) return displayName;

        int separatorIndex = Math.max(displayName.lastIndexOf(':'), displayName.lastIndexOf('\uFF1A'));
        int rainbowStartIndex = separatorIndex < 0 ? 0 : separatorIndex + 1;
        while (rainbowStartIndex < displayName.length()
            && Character.isWhitespace(displayName.charAt(rainbowStartIndex))) {
            rainbowStartIndex++;
        }
        return TstUtils.animatedRainbowText(displayName, rainbowStartIndex);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.backgroundIcon = register
            .registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":EcoSphereModeBeacon/mode_beacon_background");
        this.frameIcon = register.registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":EcoSphereModeBeacon/frame");
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
