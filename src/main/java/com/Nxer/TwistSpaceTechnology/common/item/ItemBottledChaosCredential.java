package com.Nxer.TwistSpaceTechnology.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;

/** Capacity is selected by metadata; the route binding is stored on each individual stack. */
public final class ItemBottledChaosCredential extends AbstractTstMetaItem {

    public static final int MAX_META = 5;
    private static final String DISPLAY_TAG_MASK_NBT_KEY = "bottledChaosDisplayTagMask";

    private final IIcon[] boundIcons = new IIcon[MAX_META + 1];

    // #tr tst.bottledchaos.credential.0.name
    // # Bottled Chaos Credential Frame 1
    // #zh_CN 瓶中混沌凭证框架 1

    // #tr tst.bottledchaos.credential.1.name
    // # Bottled Chaos Credential Frame 4
    // #zh_CN 瓶中混沌凭证框架 4

    // #tr tst.bottledchaos.credential.2.name
    // # Bottled Chaos Credential Frame 16
    // #zh_CN 瓶中混沌凭证框架 16

    // #tr tst.bottledchaos.credential.3.name
    // # Bottled Chaos Credential Frame 64
    // #zh_CN 瓶中混沌凭证框架 64

    // #tr tst.bottledchaos.credential.4.name
    // # Bottled Chaos Credential Frame 256
    // #zh_CN 瓶中混沌凭证框架 256

    // #tr tst.bottledchaos.credential.5.name
    // # Bottled Chaos Credential Frame 1024
    // #zh_CN 瓶中混沌凭证框架 1024

    public ItemBottledChaosCredential() {
        super("BottledChaosCredential", "bottled_chaos.credential");
        setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (isBound(stack) && !stack.getTagCompound()
            .hasKey(DISPLAY_TAG_MASK_NBT_KEY)) {
            // #tr tst.bottledchaos.credential.bound.name
            // # Bottled Chaos Credential %s
            // #zh_CN 瓶中混沌凭证 %s
            return StatCollector
                .translateToLocalFormatted("tst.bottledchaos.credential.bound.name", getCapacity(stack));
        }
        return StatCollector.translateToLocal("tst.bottledchaos.credential." + stack.getItemDamage() + ".name");
    }

    public static long getCapacity(ItemStack stack) {
        if (stack == null || stack.getItem() != TstItems.BottledChaosCredential || stack.stackSize != 1) return 0;
        int meta = stack.getItemDamage();
        return meta >= 0 && meta <= MAX_META ? 1L << (2 * meta) : 0;
    }

    public static ItemStack getRecipeFamily(ItemStack stack) {
        return isBound(stack) ? stack.copy() : null;
    }

    public static ItemStack createEmptyFrame(int meta) {
        return meta >= 0 && meta <= MAX_META ? new ItemStack(TstItems.BottledChaosCredential, 1, meta) : null;
    }

    public static ItemStack createCredential(int meta, long routeMask, int occupiedWeight) {
        ItemStack stack = createEmptyFrame(meta);
        return setBinding(stack, routeMask, occupiedWeight) ? stack : null;
    }

    public static ItemStack createDisplayCredential(int meta, long displayTagMask) {
        ItemStack stack = createCredential(meta, displayTagMask, getRequiredWeight(displayTagMask));
        if (stack != null) stack.getTagCompound()
            .setLong(DISPLAY_TAG_MASK_NBT_KEY, displayTagMask);
        return stack;
    }

    public static boolean setBinding(ItemStack stack, long routeMask, int occupiedWeight) {
        long capacity = getCapacity(stack);
        if (capacity == 0 || (routeMask & ~CredentialModel.ALL_ROUTE_MASK) != 0
            || (routeMask == 0) != (occupiedWeight == 0)
            || occupiedWeight < getRequiredWeight(routeMask)
            || occupiedWeight > capacity) {
            return false;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (routeMask == 0) {
            if (tag != null) {
                tag.removeTag(CredentialModel.ROUTE_MASK_NBT_KEY);
                tag.removeTag(CredentialModel.OCCUPIED_WEIGHT_NBT_KEY);
            }
            return true;
        }
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        CredentialModel.writeRouteMask(tag, routeMask);
        tag.setInteger(CredentialModel.OCCUPIED_WEIGHT_NBT_KEY, occupiedWeight);
        return true;
    }

    public static long getRouteMask(ItemStack stack) {
        return getCapacity(stack) == 0 ? 0 : CredentialModel.readRouteMask(stack.getTagCompound());
    }

    public static int getOccupiedWeight(ItemStack stack) {
        if (getCapacity(stack) == 0 || stack.getTagCompound() == null) return 0;
        return stack.getTagCompound()
            .getInteger(CredentialModel.OCCUPIED_WEIGHT_NBT_KEY);
    }

    public static int getRequiredWeight(long routeMask) {
        return CredentialModel.getRequiredWeight(routeMask);
    }

    public static boolean isBound(ItemStack stack) {
        int weight = getOccupiedWeight(stack);
        long mask = getRouteMask(stack);
        return mask != 0 && weight >= getRequiredWeight(mask) && weight <= getCapacity(stack);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        String path = TwistSpaceTechnology.RESOURCE_ROOT_ID + ":BottledChaosCredential/";
        for (int meta : usedMetaIds) {
            iconMap.put(meta, register.registerIcon(path + "credential_t" + meta + "_blank"));
            boundIcons[meta] = register.registerIcon(path + "credential_t" + meta + "_bound");
        }
        itemIcon = iconMap.get(0);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        return isBound(stack) ? boundIcons[stack.getItemDamage()] : getIconFromDamage(stack.getItemDamage());
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconIndex(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean advanced) {
        // #tr tst.bottledchaos.credential.tooltip.capacity
        // # Capacity: %s/%s
        // #zh_CN 容量: %s/%s
        lines.add(
            StatCollector.translateToLocalFormatted(
                "tst.bottledchaos.credential.tooltip.capacity",
                getOccupiedWeight(stack),
                getCapacity(stack)));
        if (!isBound(stack)) {
            // #tr tst.bottledchaos.credential.tooltip.empty
            // # Inject route tags on the controller's injection page.
            // #zh_CN 在主机注入页面写入路线标签.
            lines.add(StatCollector.translateToLocal("tst.bottledchaos.credential.tooltip.empty"));
        } else {
            long displayTagMask = stack.getTagCompound()
                .getLong(DISPLAY_TAG_MASK_NBT_KEY);
            if (displayTagMask == 0) {
                for (String key : getRouteKeyList(stack)) lines.add(StatCollector.translateToLocal(key));
            } else {
                // #tr tst.bottledchaos.credential.tooltip.example.any_tag_1
                // # Any Tag 1
                // #zh_CN 任意标签 1

                // #tr tst.bottledchaos.credential.tooltip.example.any_tag_2
                // # Any Tag 2
                // #zh_CN 任意标签 2
                for (int index = 0; index < 2; index++) {
                    if ((displayTagMask & (1L << index)) != 0) {
                        lines.add(
                            StatCollector.translateToLocal(
                                "tst.bottledchaos.credential.tooltip.example.any_tag_" + (index + 1)));
                    }
                }
            }
        }
    }

    public static List<String> getRouteKeyList(ItemStack stack) {
        // #tr tst.bottledchaos.route.platinum_metallic_powder
        // # Platinum Metallic Powder
        // #zh_CN 铂金属粉

        // #tr tst.bottledchaos.route.platinum_residue
        // # Platinum Residue Dust
        // #zh_CN 铂渣粉

        // #tr tst.bottledchaos.route.crude_rhodium_metal
        // # Crude Rhodium Metal Dust
        // #zh_CN 粗制铑金属粉

        // #tr tst.bottledchaos.route.leach_residue
        // # Leach Residue Dust
        // #zh_CN 浸出渣粉

        // #tr tst.bottledchaos.route.palladium_metallic_powder
        // # Palladium Metallic Powder
        // #zh_CN 钯金属粉

        // #tr tst.bottledchaos.route.rarest_metal_residue
        // # Rare Metal Residue Dust
        // #zh_CN 稀有金属渣粉

        // #tr tst.bottledchaos.route.iridium_metal_residue
        // # Iridium Metal Residue Dust
        // #zh_CN 铱金属渣粉

        // #tr tst.bottledchaos.route.ancient_debris
        // # Netherite
        // #zh_CN 下界合金

        // #tr tst.bottledchaos.route.crystalline_alloy_molten
        // # Molten Crystalline Alloy
        // #zh_CN 熔融晶化合金

        // #tr tst.bottledchaos.route.prismatic_acid
        // # Prismatic Acid
        // #zh_CN 海晶酸

        // #tr tst.bottledchaos.route.bastnasite
        // # Bastnasite
        // #zh_CN 氟碳镧铈

        // #tr tst.bottledchaos.route.monazite
        // # Monazite
        // #zh_CN 独居石

        // #tr tst.bottledchaos.route.rare_earth_cerium
        // # Monazite + Bastnasite
        // #zh_CN 独居石 + 氟碳镧铈

        // #tr tst.bottledchaos.route.cerium
        // # Cerium
        // #zh_CN 铈

        // #tr tst.bottledchaos.route.samarium
        // # Samarium
        // #zh_CN 钐

        // #tr tst.bottledchaos.route.naquadah
        // # Naquadah
        // #zh_CN 硅岩

        // #tr tst.bottledchaos.route.enriched_naquadah
        // # Enriched Naquadah
        // #zh_CN 富集硅岩

        // #tr tst.bottledchaos.route.naquadria
        // # Naquadria
        // #zh_CN 超能硅岩

        // #tr tst.bottledchaos.route.naquadah_fuel_preparation
        // # Naquadah Fuel Precursors
        // #zh_CN 硅岩燃料前置

        // #tr tst.bottledchaos.route.naquadah_fuel_3
        // # Naquadah Fuel III
        // #zh_CN 硅岩燃料 III

        // #tr tst.bottledchaos.route.naquadah_fuel_4
        // # Naquadah Fuel IV
        // #zh_CN 硅岩燃料 IV

        // #tr tst.bottledchaos.route.naquadah_fuel_5
        // # Naquadah Fuel V
        // #zh_CN 硅岩燃料 V

        // #tr tst.bottledchaos.route.naquadah_fuel_6
        // # Naquadah Fuel VI
        // #zh_CN 硅岩燃料 VI

        // #tr tst.bottledchaos.route.fusion
        // # Fusion
        // #zh_CN 聚变

        // #tr tst.bottledchaos.route.growth_medium_sterilized
        // # Sterile Growth Medium
        // #zh_CN 无菌培养基

        // #tr tst.bottledchaos.route.stem_cells
        // # Stem Cells
        // #zh_CN 干细胞

        // #tr tst.bottledchaos.route.seaweed_broth
        // # Seaweed Broth
        // #zh_CN 海藻基质

        // #tr tst.bottledchaos.route.tau_ceti_e_seaweed_extract
        // # Tau Ceti E Seaweed Extract
        // #zh_CN 鲸鱼座T星E藻类提取物

        // #tr tst.bottledchaos.route.bio_medium_sterilized
        // # Sterile Biological Medium
        // #zh_CN 灭菌生物培养基

        // #tr tst.bottledchaos.route.bio_cells
        // # Biological Cells
        // #zh_CN 生物细胞

        // #tr tst.bottledchaos.route.xenoxene
        // # Xenoxene Fractionation
        // #zh_CN 异氙分馏

        // #tr tst.bottledchaos.route.super_heavy_radox
        // # Superheavy Radox
        // #zh_CN 超重拉多X

        // #tr tst.bottledchaos.route.heavy_radox
        // # Heavy Radox
        // #zh_CN 重拉多X

        // #tr tst.bottledchaos.route.light_radox
        // # Light Radox
        // #zh_CN 轻拉多X

        // #tr tst.bottledchaos.route.super_light_radox
        // # Superlight Radox
        // #zh_CN 超轻拉多X

        // #tr tst.bottledchaos.route.kevlar
        // # Kevlar
        // #zh_CN 凯芙拉

        // #tr tst.bottledchaos.route.purified_water_1
        // # Grade 1 Purified Water
        // #zh_CN 1级净化水

        // #tr tst.bottledchaos.route.purified_water_2
        // # Grade 2 Purified Water
        // #zh_CN 2级净化水

        // #tr tst.bottledchaos.route.purified_water_3
        // # Grade 3 Purified Water
        // #zh_CN 3级净化水

        // #tr tst.bottledchaos.route.purified_water_4
        // # Grade 4 Purified Water
        // #zh_CN 4级净化水

        // #tr tst.bottledchaos.route.purified_water_5
        // # Grade 5 Purified Water
        // #zh_CN 5级净化水

        // #tr tst.bottledchaos.route.purified_water_6
        // # Grade 6 Purified Water
        // #zh_CN 6级净化水

        // #tr tst.bottledchaos.route.purified_water_7
        // # Grade 7 Purified Water
        // #zh_CN 7级净化水

        // #tr tst.bottledchaos.route.purified_water_8
        // # Grade 8 Purified Water
        // #zh_CN 8级净化水

        // #tr tst.bottledchaos.route.magmatter
        // # Magmatter
        // #zh_CN 磁物质

        // #tr tst.bottledchaos.route.quark_gluon_plasma
        // # Quark-Gluon Plasma
        // #zh_CN 夸克胶子等离子体

        // #tr tst.bottledchaos.route.titanium
        // # Titanium
        // #zh_CN 钛

        // #tr tst.bottledchaos.route.tungsten
        // # Tungsten
        // #zh_CN 钨

        // #tr tst.bottledchaos.route.indium
        // # Indium
        // #zh_CN 铟

        // #tr tst.bottledchaos.route.phonon_medium
        // # Lossless Phonon Transfer Medium
        // #zh_CN 无损声子传输介质

        // #tr tst.bottledchaos.route.eternity
        // # Eternity
        // #zh_CN 永恒

        // #tr tst.bottledchaos.route.honeycomb
        // # Honeycomb Processing
        // #zh_CN 蜂窝处理

        // #tr tst.bottledchaos.route.isamill
        // # IsaMill Processing
        // #zh_CN 艾萨矿物处理
        List<String> keys = new ArrayList<>();
        long mask = isBound(stack) ? getRouteMask(stack) : 0;
        for (int index : CredentialModel.displayOrder()) {
            if ((mask & (1L << index)) != 0) keys.add(CredentialModel.translationKey(index));
        }
        return keys;
    }
}
