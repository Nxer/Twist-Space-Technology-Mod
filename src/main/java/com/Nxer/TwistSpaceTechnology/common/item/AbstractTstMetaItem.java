package com.Nxer.TwistSpaceTechnology.common.item;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.api.IHasTooltips;
import com.Nxer.TwistSpaceTechnology.common.api.IHasVariantAndTooltips;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractTstMetaItem extends Item implements IHasVariantAndTooltips {

    protected final HashSet<Integer> usedMetaIds = new HashSet<>();
    protected final HashMap<Integer, String[]> tooltipMap = new HashMap<>();

    protected Map<Integer, IIcon> iconMap = new HashMap<>();

    public AbstractTstMetaItem(String unlocalizedName) {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TstCreativeTabs.TabMetaItems);
        this.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

    /**
     * Returns the unlocalized name of this item.
     * <p>
     * The item damage is used as a part of key. eg: {@code item.{ITEM_NAME}.{ITEM_DAMAGE}}.
     * <p>
     * NOTE: "final", because we don't want subclasses to modify this
     */
    @Override
    public final String getUnlocalizedName(ItemStack aItemStack) {
        return super.getUnlocalizedName() + "." + aItemStack.getItemDamage();
    }

    @Override
    public final String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List<ItemStack> aList) {
        ItemStack[] variants = getVariants();
        aList.addAll(Arrays.asList(variants));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack aItemStack, EntityPlayer aEntityPlayer, List<String> aTooltipsList,
        boolean isAdvancedMode) {
        String[] tooltips = getTooltips(aItemStack.getItemDamage(), IHasTooltips.isShiftKeyDown());
        if (tooltips != null) {
            aTooltipsList.addAll(Arrays.asList(tooltips));
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.iconMap = registerAllVariantIcons(
            register,
            meta -> TwistSpaceTechnology.RESOURCE_ROOT_ID + ":" + unlocalizedName + "/" + meta);
        this.itemIcon = iconMap.get(0);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return iconMap.get(meta);
    }

    @Override
    public ItemStack getVariant(int meta) throws IllegalArgumentException {
        return checkAndGetVariant(this, meta, usedMetaIds);
    }

    @Override
    public ItemStack[] getVariants() {
        return getAllVariants(this, usedMetaIds);
    }

    @Override
    public ItemStack registerVariant(int meta) throws IllegalArgumentException {
        return checkAndRegisterVariant(this, meta, usedMetaIds);
    }

    @Override
    public @Unmodifiable Set<Integer> getVariantIds() {
        return new HashSet<>(usedMetaIds);
    }

    @Override
    public void setTooltips(int metaValue, @Nullable String[] tooltips, boolean advanced) {
        tooltipMap.put(metaValue, tooltips);
    }

    @Override
    public @Nullable String[] getTooltips(int metaValue, boolean advanced) {
        return tooltipMap.get(metaValue);
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
    protected static ItemStack checkAndGetVariant(Item self, int meta, Collection<Integer> allowMetaValues)
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
    protected static ItemStack checkAndRegisterVariant(Item self, int meta, Collection<Integer> allowMetaValues)
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
    protected static ItemStack[] getAllVariants(Item self, Collection<Integer> allowMetaValues) {
        return allowMetaValues.stream()
            .map(m -> TstUtils.newItemWithMeta(self, m))
            .toArray(ItemStack[]::new);
    }

}
