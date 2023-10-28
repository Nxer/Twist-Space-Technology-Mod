package com.GTNH_Community.gtnhcommunitymod.common.block.blockClass;

import static com.GTNH_Community.gtnhcommunitymod.common.GTCMCreativeTabs.tabMetaBlock01;
import static com.GTNH_Community.gtnhcommunitymod.common.block.blockList01.MetaBlock01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_LanguageManager;

public class ItemBlockBase01 extends ItemBlock {

    // region Constructors

    public ItemBlockBase01(Block aBlock) {
        super(aBlock);
        setHasSubtypes(true);
        setMaxDamage(0);
        this.setCreativeTab(tabMetaBlock01);
    }

    // endregion
    // -----------------------
    // region MetaBlock Generators

    public static ItemStack metaBlock01(int Meta, Block baseBlock) {
        ItemStack generatingItemBlock = new ItemStack(baseBlock, 1, Meta);
        MetaBlockMap01.put(Meta, generatingItemBlock);
        return generatingItemBlock;
    }

    public static ItemStack metaBlock01(String i18nName, int Meta, Block baseBlock) {
        texter(i18nName, baseBlock.getUnlocalizedName() + "." + Meta + ".name");
        ItemStack generatingItemBlock = new ItemStack(baseBlock, 1, Meta);
        MetaBlockMap01.put(Meta, generatingItemBlock);
        return generatingItemBlock;
    }

    public static ItemStack metaBlock01(String i18nName, int Meta) {
        return metaBlock01(i18nName, Meta, MetaBlock01);
    }

    // endregion
    // -----------------------
    // region Member Variables

    public static final Map<Integer, String[]> MetaBlockTooltipsMap01 = new HashMap<>();
    public static final Map<Integer, ItemStack> MetaBlockMap01 = new HashMap<>();

    public final String mNoMobsToolTip = GT_LanguageManager
        .addStringLocalization("gt.nomobspawnsonthisblock", "Mobs cannot Spawn on this Block");
    public final String mNoTileEntityToolTip = GT_LanguageManager
        .addStringLocalization("gt.notileentityinthisblock", "This is NOT a TileEntity!");

    // endregion
    // -----------------------
    // region Overrides

    /**
     * Handle the tooltips.
     *
     * @param aItemStack
     * @param theTooltipsList
     */
    @SideOnly(Side.CLIENT)
    @Override
    @SuppressWarnings({ "unchecked" })
    public void addInformation(ItemStack aItemStack, EntityPlayer p_77624_2_, List theTooltipsList,
        boolean p_77624_4_) {
        int meta = aItemStack.getItemDamage();
        if (null != MetaBlockTooltipsMap01.get(meta)) {
            String[] tooltips = MetaBlockTooltipsMap01.get(meta);
            theTooltipsList.addAll(Arrays.asList(tooltips));
        }
        theTooltipsList.add(mNoMobsToolTip);
        theTooltipsList.add(mNoTileEntityToolTip);
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + this.getDamage(aStack);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

    // endregion
}
