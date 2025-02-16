package com.Nxer.TwistSpaceTechnology.common.block.meta;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.api.IHasVariantAndTooltips;
import com.Nxer.TwistSpaceTechnology.common.api.IHasVariant;
import com.Nxer.TwistSpaceTechnology.common.item.blockItem.TstMetaBlockItem;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * An abstract class for meta blocks, which has many variants within one prototype block.
 *
 * @see TstMetaBlockItem
 */
public abstract class AbstractTstMetaBlock extends Block implements IHasVariantAndTooltips, IHasVariant {

    protected final Set<Integer> usedMetaSet = new HashSet<>(16);
    protected final Map<Integer, String[]> tooltipMap = new HashMap<>();

    protected Map<Integer, IIcon> iconMap = new HashMap<>();

    public AbstractTstMetaBlock(String unlocalizedName) {
        this(Material.iron, unlocalizedName);
    }

    public AbstractTstMetaBlock(Material material, String unlocalizedName) {
        super(material);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(TstCreativeTabs.TabMetaBlocks);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return iconMap.get(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        iconMap = registerAllVariantIcons(register, meta -> RESOURCE_ROOT_ID + ":" + unlocalizedName + "/" + meta);
        blockIcon = iconMap.get(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTabs, List<ItemStack> list) {
        list.addAll(Arrays.asList(getVariants()));
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    public String[] getTooltips(int meta) {
        return tooltipMap.get(meta);
    }

    @Override
    public void setTooltips(int metaValue, @Nullable String[] tooltips) {
        tooltipMap.put(metaValue, tooltips);
    }

    @Override
    public ItemStack getVariant(int meta) throws IllegalArgumentException {
        return checkAndGetVariant(this, meta, usedMetaSet);
    }

    @Override
    public ItemStack[] getVariants() {
        return getAllVariants(this, usedMetaSet);
    }

    @Override
    public ItemStack registerVariant(int meta) throws IllegalArgumentException {
        return checkAndRegisterVariant(this, meta, usedMetaSet);
    }

    @Override
    public @Unmodifiable Set<Integer> getVariantIds() {
        return new HashSet<>(usedMetaSet);
    }

    /**
     * A util function for meta items that manage its variants by a collection of meta values.
     * <p>
     * This function will check if the meta value is allowed, and return the instance of self if allowed.
     *
     * @param self            the item reference
     * @param meta            the meta value
     * @param allowMetaValues the allow list of meta values
     * @return the new instance of self with meta value
     * @throws IllegalArgumentException if meta value is not allowed.
     */
    @ApiStatus.Internal
    protected static ItemStack checkAndGetVariant(Block self, int meta, Collection<Integer> allowMetaValues)
        throws IllegalArgumentException {
        if (allowMetaValues.contains(meta)) {
            return TstUtils.newItemWithMeta(self, meta);
        } else {
            throw new IllegalArgumentException("Invalid meta value: " + meta);
        }
    }

    /**
     * A util function for meta items that manage its variants by a collection of meta values.
     * <p>
     * This function will check if the meta value is used, then register and return the instance if not.
     *
     * @param self            the item reference
     * @param meta            the meta value
     * @param allowMetaValues the allow list of the meta values
     * @return the new instance of self with meta value
     * @throws IllegalArgumentException if the meta value is already taken.
     */
    @ApiStatus.Internal
    protected static ItemStack checkAndRegisterVariant(Block self, int meta, Collection<Integer> allowMetaValues)
        throws IllegalArgumentException {
        if (allowMetaValues.contains(meta)) {
            throw new IllegalArgumentException(
                "Meta value already exists: " + meta
                    + " in "
                    + self.getUnlocalizedName()
                    + " ("
                    + self.getClass()
                        .getSimpleName()
                    + ")");
        } else {
            allowMetaValues.add(meta);
            return TstUtils.newItemWithMeta(self, meta);
        }
    }

    /**
     * A util function for meta items that manage its variants by a collection of meta values.
     * <p>
     * This function will generate an array of new instances of self with allowed meta values.
     *
     * @param self            the item reference
     * @param allowMetaValues the allow list of meta values
     * @return the array of new instances of self with allowed meta values.
     */
    @ApiStatus.Internal
    protected static ItemStack[] getAllVariants(Block self, Collection<Integer> allowMetaValues) {
        return allowMetaValues.stream()
            .map(m -> TstUtils.newItemWithMeta(self, m))
            .toArray(ItemStack[]::new);
    }
}
