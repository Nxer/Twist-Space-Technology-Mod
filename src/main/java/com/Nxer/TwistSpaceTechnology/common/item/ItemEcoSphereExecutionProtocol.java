package com.Nxer.TwistSpaceTechnology.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.text.Style;

public final class ItemEcoSphereExecutionProtocol extends AbstractTstMetaItem {

    private IIcon backgroundIcon;
    private IIcon frameIcon;

    public ItemEcoSphereExecutionProtocol() {
        super("EcoSphereExecutionProtocol", "ecosphere.execution_protocol");
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        String displayName = super.getItemStackDisplayName(itemStack);
        int meta = itemStack.getItemDamage();
        // Odd metas keep the original rainbow rule; cloning tier 3 is the explicit even-meta exception.
        if ((meta & 1) == 0 && meta != 8) return displayName;

        int separatorIndex = displayName.indexOf(": ");
        if (separatorIndex < 0) return Style.RAINBOW.apply(displayName)
            .get();

        String prefix = displayName.substring(0, separatorIndex + 2);
        String name = displayName.substring(separatorIndex + 2);
        return prefix + Style.RAINBOW.apply(name)
            .get();
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.backgroundIcon = register.registerIcon(
            TwistSpaceTechnology.RESOURCE_ROOT_ID + ":EcoSphereExecutionProtocol/execution_protocol_background");
        this.frameIcon = register
            .registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":EcoSphereExecutionProtocol/frame");
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
