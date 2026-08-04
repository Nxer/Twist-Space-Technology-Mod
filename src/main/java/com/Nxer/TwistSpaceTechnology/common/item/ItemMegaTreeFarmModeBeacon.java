package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

public final class ItemMegaTreeFarmModeBeacon extends AbstractTstMetaItem {

    private IIcon backgroundIcon;
    private IIcon frameIcon;

    public ItemMegaTreeFarmModeBeacon() {
        super("MegaTreeFarmModeBeacon");
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String displayName = super.getItemStackDisplayName(itemStack);
        if (itemStack.getItemDamage() != 4) return displayName;

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
