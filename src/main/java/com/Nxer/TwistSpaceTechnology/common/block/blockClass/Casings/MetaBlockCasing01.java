package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStaticDataClientOnly;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.MetaBlockBase;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;

/**
 * Use {@link MetaBlockCasing} with {@link TstUtils#newMetaBlockItemStack(MetaBlockBase, int, String[])}.
 */
@Deprecated
public class MetaBlockCasing01 extends MetaBlockCasingBase {

    // TODO: Prepare a generic meta block class to handle all those meta blocks in different basic blocks.
    private static final String unlocalizedName = "MetaBlockCasing01";
    private static final byte TEXTURE_ID_OFFSITE = 0;
    private static final Set<Integer> USED_META = new HashSet<>(16);
    public static final Map<Integer, String[]> TOOLTIPS = new HashMap<>(16);

    public MetaBlockCasing01() {
        super(unlocalizedName);
    }

    private static int getTextureIndexStatic(int aMeta) {
        return GTUtility.getTextureId(TEXTURE_PAGE_INDEX, getTextureIndexInPageStatic(aMeta));
    }

    private static byte getTextureIndexInPageStatic(int meta) {
        return (byte) (meta + TEXTURE_ID_OFFSITE);
    }

    /**
     * The standard method to get the Texture Index from this block casing.
     * <p>
     * Every Block only can have 16 meta blocks (0 - 15).
     *
     * @param aMeta block meta
     * @return The Texture Index
     */
    @Override
    public int getTextureIndex(int aMeta) {
        return getTextureIndexStatic(aMeta);
    }

    @Override
    public byte getTexturePageIndex() {
        return TEXTURE_PAGE_INDEX;
    }

    @Override
    public byte getTextureIndexInPage(int meta) {
        return getTextureIndexInPageStatic(meta);
    }

    @Override
    public String getUnlocalizedName() {
        return MetaBlockCasing01.unlocalizedName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return BlockStaticDataClientOnly.iconsMetaBlockCasing01.get(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gtnhcommunitymod:MetaBlockCasing01/0");
        for (int Meta : USED_META) {
            BlockStaticDataClientOnly.iconsMetaBlockCasing01
                .put(Meta, reg.registerIcon("gtnhcommunitymod:MetaBlockCasing01/" + Meta));
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List list) {
        for (int Meta : USED_META) {
            list.add(new ItemStack(aItem, 1, Meta));
        }
    }

}
