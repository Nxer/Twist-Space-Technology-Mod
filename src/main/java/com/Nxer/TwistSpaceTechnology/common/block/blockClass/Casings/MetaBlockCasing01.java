package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

import static com.Nxer.TwistSpaceTechnology.util.MetaItemStackUtils.initMetaItemStack;

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

import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.block.MetaBlockConstructors;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.BlockStaticDataClientOnly;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

/**
 * Use {@link MetaBlockCasing} with {@link MetaBlockConstructors}
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
     * Init a meta block of MetaBlockCasing01.
     * <p>
     * Put the texture file at MetaBlockCasing01/meta.png
     * </p>
     *
     * @param enName The default English name of this meta block.
     * @param metaID The meta ID.
     * @return An item stack of this meta block.
     */
    public static ItemStack initMetaCasing01(String enName, int metaID) {
        Textures.BlockIcons.setCasingTextureForId(
            getTextureIndexStatic(metaID),
            TextureFactory.of(BasicBlocks.MetaBlockCasing01, metaID));
        return initMetaItemStack(enName, metaID, BasicBlocks.MetaBlockCasing01, USED_META);
    }

    /**
     * Init a meta block of MetaBlockCasing01 with tooltips.
     * <p>
     * Put the texture file at MetaBlockCasing01/meta.png
     * </p>
     *
     * @param enName   The default English name of this meta block.
     * @param metaID   The meta ID.
     * @param tooltips A String[] to hold tooltips.
     * @return An item stack of this meta block.
     */
    public static ItemStack initMetaCasing01(String enName, int metaID, String[] tooltips) {
        TOOLTIPS.put(metaID, tooltips);
        return initMetaCasing01(enName, metaID);
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
