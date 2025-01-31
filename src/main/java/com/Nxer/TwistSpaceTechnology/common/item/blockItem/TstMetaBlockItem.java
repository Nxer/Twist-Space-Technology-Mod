package com.Nxer.TwistSpaceTechnology.common.item.blockItem;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.block.IHasMoreBlockInfo;
import com.Nxer.TwistSpaceTechnology.common.block.meta.AbstractTstMetaBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTLanguageManager;

/**
 * The {@link ItemBlock} for {@link AbstractTstMetaBlock} and its subclasses.
 */
public class TstMetaBlockItem extends ItemBlock {

    // region statics

    // endregion
    // -----------------------
    // region Constructors

    public TstMetaBlockItem(Block aBlock) {
        super(requireTstMetaBlock(aBlock));
        setHasSubtypes(true);
        setMaxDamage(0);
        this.setCreativeTab(TstCreativeTabs.TabMetaBlocks);
    }

    private static AbstractTstMetaBlock requireTstMetaBlock(Block block) {
        try {
            return (AbstractTstMetaBlock) block;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(
                "Block is not a TstMetaBlockBase: " + block.getUnlocalizedName()
                    + " ("
                    + block.getClass()
                        .getSimpleName()
                    + ")");
        }
    }

    /**
     * Cast {@link #field_150939_a} to {@link AbstractTstMetaBlock}.
     */
    protected final AbstractTstMetaBlock getMetaBlock() {
        return (AbstractTstMetaBlock) this.field_150939_a;
    }

    // endregion
    // -----------------------
    // region MetaBlock Generators

    // endregion
    // -----------------------
    // region Member Variables

    public final String mNoMobsToolTip = GTLanguageManager
        .addStringLocalization("gt.nomobspawnsonthisblock", "Mobs cannot Spawn on this Block");
    public final String mNoTileEntityToolTip = GTLanguageManager
        .addStringLocalization("gt.notileentityinthisblock", "This is NOT a TileEntity!");

    // endregion
    // -----------------------
    // region Overrides

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack aItemStack, EntityPlayer p_77624_2_, List<String> theTooltipsList,
        boolean p_77624_4_) {
        int meta = aItemStack.getItemDamage();

        var tooltips = getMetaBlock().getTooltips(meta);
        if (tooltips != null) {
            theTooltipsList.addAll(Arrays.asList(tooltips));
        }

        // additional information about the block
        // see IHasMoreBlockInfo
        if (getMetaBlock() instanceof IHasMoreBlockInfo hasMoreBlockInfo) {
            if (hasMoreBlockInfo.isNoMobSpawn()) {
                theTooltipsList.add(mNoMobsToolTip);
            }
            if (hasMoreBlockInfo.isNotTileEntity()) {
                theTooltipsList.add(mNoTileEntityToolTip);
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getMetaBlock().getUnlocalizedName() + "." + this.getDamage(aStack);
    }

    @Override
    public int getMetadata(int aMeta) {
        return getMetaBlock().isValidVariant(aMeta) ? aMeta : 0;
    }

    // endregion
}
