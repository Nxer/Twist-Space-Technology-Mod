package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.emoniph.witchery.Witchery;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import magicbees.item.types.NuggetType;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import thaumcraft.common.config.ConfigItems;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

public final class DirectedMobClonerDropConversion {

    private static final Map<TST_ItemID, List<ConvertedOutput>> CONVERSIONS = new HashMap<>();

    static {
        // spotless:off
        // Minecraft
        put(Items.wooden_sword, Materials.Wood, 2.5);
        put(Items.stone_sword, dust(Materials.Stone, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.iron_sword, dust(Materials.Iron, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.golden_sword, dust(Materials.Gold, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.diamond_sword, dust(Materials.Diamond, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.stone_axe, dust(Materials.Stone, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.iron_axe, dust(Materials.Iron, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.golden_axe, dust(Materials.Gold, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.stone_pickaxe, dust(Materials.Stone, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.iron_pickaxe, dust(Materials.Iron, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.golden_pickaxe, dust(Materials.Gold, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.golden_hoe, dust(Materials.Gold, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.stone_shovel, dust(Materials.Stone, 1), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.iron_shovel, dust(Materials.Iron, 1), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.golden_shovel, dust(Materials.Gold, 1), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(Items.shears, dust(Materials.Iron, 2), dust(Materials.Wood, 1, 1d / 4 * 2));

        put(Items.leather_helmet, item(Items.leather, 5));
        put(Items.leather_chestplate, item(Items.leather, 8));
        put(Items.leather_leggings, item(Items.leather, 7));
        put(Items.leather_boots, item(Items.leather, 4));
        put(Items.chainmail_helmet, Materials.Steel, 5);
        put(Items.chainmail_chestplate, Materials.Steel, 8);
        put(Items.chainmail_leggings, Materials.Steel, 7);
        put(Items.chainmail_boots, Materials.Steel, 4);
        put(Items.iron_helmet, Materials.Iron, 5);
        put(Items.iron_chestplate, Materials.Iron, 8);
        put(Items.iron_leggings, Materials.Iron, 7);
        put(Items.iron_boots, Materials.Iron, 4);
        put(Items.golden_helmet, Materials.Gold, 5);
        put(Items.golden_chestplate, Materials.Gold, 8);
        put(Items.golden_leggings, Materials.Gold, 7);
        put(Items.golden_boots, Materials.Gold, 4);
        put(Items.diamond_helmet, Materials.Diamond, 5);
        put(Items.diamond_chestplate, Materials.Diamond, 8);
        put(Items.diamond_leggings, Materials.Diamond, 7);
        put(Items.diamond_boots, Materials.Diamond, 4);

        put(Items.bow, item(Items.string, 3), dust(Materials.Wood, 1, 1d / 4 * 6 * 0.95));
        put(Items.fishing_rod, item(Items.string, 2), dust(Materials.Wood, 1, 1d / 4 * 6 * 0.95));
        put(Items.flint_and_steel, dust(Materials.Steel, 1), dust(Materials.Flint, 1, 1d / 4 * 2));
        put(Items.flint, Materials.Flint, 0.5);

        put(new ItemStack(Items.gold_nugget), dust(Materials.Gold, 1, 1d / 9));
        put(new ItemStack(Items.gold_ingot), Materials.Gold, 1);
        put(new ItemStack(Items.iron_ingot), Materials.Iron, 1);
        put(new ItemStack(Items.emerald), Materials.Emerald, 1);
        put(new ItemStack(Items.coal), Materials.Coal, 1);
        put(new ItemStack(Items.book), Materials.Paper, 3);
        put(new ItemStack(Items.writable_book), Materials.Paper, 3);
        put(new ItemStack(Items.bucket), Materials.Iron, 3);
        put(new ItemStack(Items.golden_apple), Materials.Gold, 8);
        put(new ItemStack(Items.wheat), Materials.Wheat, 1);
        put(new ItemStack(Items.melon), item(new ItemStack(Items.melon_seeds)));
        put(new ItemStack(Items.speckled_melon), dust(Materials.Gold, 1, 1d / 9 * 8));
        put(new ItemStack(Items.glass_bottle), Materials.Glass, 1);
        put(new ItemStack(Items.snowball), Materials.Snow, 1);
        put(new ItemStack(Items.snowball, 1, 1), Materials.Snow, 1);
        put(new ItemStack(Items.blaze_rod), output(new ItemStack(Items.blaze_powder), 3.5));
        put(new ItemStack(Items.chicken), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(new ItemStack(Items.cooked_fished), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(new ItemStack(Items.beef), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(new ItemStack(Items.porkchop), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(new ItemStack(Blocks.gravel), dust(Materials.Stone, 1), output(new ItemStack(Items.flint), 0.1));
        put(new ItemStack(Blocks.stone), Materials.Stone, 1);
        put(new ItemStack(Blocks.obsidian), Materials.Obsidian, 2);
        put(new ItemStack(Blocks.packed_ice), Materials.Ice, 2);
        put(new ItemStack(Items.bone), item(new ItemStack(Items.dye, 4, 15)));
        put(new ItemStack(Items.dye, 1, 15), output(new ItemStack(Items.stick), 0.25));
        put(new ItemStack(Items.stick), Materials.Wood, 2);
        put(new ItemStack(Items.nether_star), Materials.NetherStar, 1);
        put(new ItemStack(Blocks.wool), output(new ItemStack(Items.string), 2.5));
        put(new ItemStack(Items.paper), Materials.Paper, 1);
        put(new ItemStack(Blocks.web), output(new ItemStack(Items.string), 1.5));
        put(new ItemStack(Items.ender_pearl), Materials.EnderPearl, 1);
        put(new ItemStack(Items.ender_eye), Materials.EnderEye, 1);
        put(new ItemStack(Blocks.ice), Materials.Ice, 1);
        put(new ItemStack(Blocks.sand), dust(Materials.Sand, 1, 1.6));

        // Et Futurum
        put(GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "rabbit_raw", 1, 0), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));

        // Forbidden Magic
        put(GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "FMResource", 1, 0), dust(Materials.Emerald, 1, 1d / 9));

        // Galacticraft
        put(new ItemStack(GCItems.meteoricIronRaw), Materials.MeteoricIron, 1);

        // HarvestCraft
        put(GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "muttonrawItem", 1, 0), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));

        // GregTech
        put(GTOreDictUnificator.get(OrePrefixes.rawOre, Materials.MeteoricIron, 1), Materials.MeteoricIron, 1);
        put(Materials.Silver.getNuggets(1), dust(Materials.Silver, 1, 1d / 9));

        // GT++
        put(MaterialsElements.STANDALONE.DRAGON_METAL.getNugget(1), output(MaterialsElements.STANDALONE.DRAGON_METAL.getDust(1), 1d / 9));
        put(MaterialsElements.STANDALONE.DRAGON_METAL.getIngot(1), item(MaterialsElements.STANDALONE.DRAGON_METAL.getDust(1)));
        put(MaterialsAlloy.STABALLOY.getIngot(1), item(MaterialsAlloy.STABALLOY.getDust(1)));
        put(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "material", 1, 1), Materials.Blizz, 3.5);
        put(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 0), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 2), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 4), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 6), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 8), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));

        // Magic Bees
        put(GTModHandler.getModItem(Mods.MagicBees.ID, "beeNugget", 1, NuggetType.SILVER.ordinal()), dust(Materials.Silver, 1, 1d / 9));
        put(GTModHandler.getModItem(Mods.MagicBees.ID, "beeNugget", 1, NuggetType.EMERALD.ordinal()), dust(Materials.Emerald, 1, 1d / 9));

        // Thaumcraft
        put(ConfigItems.itemSwordThaumium, dust(Materials.Thaumium, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(ConfigItems.itemSwordVoid, dust(Materials.Void, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(ConfigItems.itemBootsCultist);
        put(ConfigItems.itemChestCultistLeaderPlate);
        put(ConfigItems.itemChestCultistPlate);
        put(ConfigItems.itemChestCultistRobe);
        put(ConfigItems.itemHelmetCultistLeaderPlate);
        put(ConfigItems.itemHelmetCultistPlate);
        put(ConfigItems.itemHelmetCultistRobe);
        put(ConfigItems.itemLegsCultistLeaderPlate);
        put(ConfigItems.itemLegsCultistPlate);
        put(ConfigItems.itemLegsCultistRobe);
        put(ConfigItems.itemSwordCrimson);
        put(new ItemStack(ConfigItems.itemResource, 1, 2), Materials.Thaumium, 1);
        put(new ItemStack(ConfigItems.itemResource, 1, 18), dust(Materials.Gold, 1, 1d / 9));
        put(new ItemStack(ConfigItems.itemNugget, 1, 3), dust(Materials.Silver, 1, 1d / 9));

        // Twilight Forest
        put(TFItems.arcticHelm, item(TFItems.arcticFur, 5));
        put(TFItems.arcticPlate, item(TFItems.arcticFur, 8));
        put(TFItems.ironwoodHelm, Materials.IronWood, 5);
        put(TFItems.ironwoodPlate, Materials.IronWood, 8);
        put(TFItems.ironwoodSword, dust(Materials.IronWood, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(TFItems.knightlyHelm, Materials.Knightmetal, 5);
        put(TFItems.knightlyPlate, Materials.Knightmetal, 8);
        put(TFItems.knightlySword, dust(Materials.Knightmetal, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        put(TFItems.steeleafHelm, Materials.Steeleaf, 5);
        put(TFItems.steeleafPlate, Materials.Steeleaf, 8);
        put(TFItems.steeleafSword, dust(Materials.Steeleaf, 2), dust(Materials.Wood, 1, 1d / 4 * 2));

        put(new ItemStack(TFItems.armorShard), dust(Materials.Knightmetal, 1, 1d / 9));
        put(new ItemStack(TFBlocks.towerWood), Materials.Wood, 1);
        put(new ItemStack(TFItems.venisonRaw), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(new ItemStack(TFItems.meefRaw), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        put(new ItemStack(TFItems.giantPick), dust(Materials.Stone, 64), dust(Materials.Wood, 64));
        put(new ItemStack(TFItems.giantSword), dust(Materials.Stone, 64), dust(Materials.Wood, 64));

        // Witchery
        put(Witchery.Items.BABAS_HAT);
        put(Witchery.Items.GULGS_GURDLE);
        put(Witchery.Items.HUNTSMANS_SPEAR);
        put(Witchery.Items.MOGS_QUIVER);
        put(Witchery.Items.VAMPIRE_BOOTS);
        put(Witchery.Items.VAMPIRE_COAT_CHAIN);
        put(Witchery.Items.VAMPIRE_COAT_FEMALE_CHAIN);
        put(Witchery.Items.VAMPIRE_COAT);
        put(Witchery.Items.VAMPIRE_COAT_FEMALE);
        put(Witchery.Items.VAMPIRE_LEGS);
        put(Witchery.Items.VAMPIRE_LEGS_KILT);
        put(new ItemStack(Witchery.Blocks.SAPLING, 1, 0), Materials.Wood, 0.5);
        put(new ItemStack(Witchery.Blocks.SAPLING, 1, 1), Materials.Wood, 0.5);
        put(new ItemStack(Witchery.Blocks.SAPLING, 1, 2), Materials.Wood, 0.5);
        //spotless:on

        // Dynamically registered aquatic items may come from several mods.
        for (TST_ItemID aquaticItem : AquaticZoneSimulatorFakeRecipe.AquaticItems) {
            ItemStack stack = aquaticItem.getItemStackWithoutNBT();
            if (stack != null) {
                put(stack, dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
            }
        }
    }

    private DirectedMobClonerDropConversion() {}

    public static ConversionResult convert(ItemStack source) {
        if (source == null || source.getItem() == null) return ConversionResult.unmatched();
        List<ConvertedOutput> conversion = CONVERSIONS.get(TST_ItemID.createNoNBT(source));
        if (conversion == null) conversion = CONVERSIONS.get(TST_ItemID.createAsWildcard(source));
        if (conversion == null) return ConversionResult.unmatched();
        if (conversion.isEmpty()) return ConversionResult.discarded();

        List<ConvertedOutput> outputs = new ArrayList<>(conversion.size());
        for (ConvertedOutput output : conversion) {
            ItemStack stack = output.stack()
                .copy();
            stack.stackSize *= source.stackSize;
            outputs.add(new ConvertedOutput(stack, output.probabilityMultiplier()));
        }
        return ConversionResult.converted(outputs);
    }

    private static void put(Item input, ConvertedOutput... outputs) {
        if (input == null) return;
        put(TST_ItemID.createAsWildcard(new ItemStack(input)), outputs);
    }

    private static void put(Item input, Materials material, double amount) {
        put(input, dust(material, amount));
    }

    private static void put(ItemStack input, ConvertedOutput... outputs) {
        if (input == null || input.getItem() == null || input.stackSize <= 0) return;
        put(TST_ItemID.createNoNBT(input), outputs);
    }

    private static void put(ItemStack input, Materials material, double amount) {
        put(input, dust(material, amount));
    }

    private static void put(TST_ItemID input, ConvertedOutput... outputs) {
        if (input == null || input == TST_ItemID.NULL) return;
        List<ConvertedOutput> validOutputs = new ArrayList<>(outputs.length);
        for (ConvertedOutput output : outputs) {
            if (output != null) validOutputs.add(output);
        }
        CONVERSIONS.put(input, validOutputs);
    }

    private static ConvertedOutput dust(Materials material, int amount) {
        return dust(material, amount, 1d);
    }

    private static ConvertedOutput dust(Materials material, double amount) {
        int wholeAmount = (int) amount;
        return amount == wholeAmount ? dust(material, wholeAmount) : dust(material, 1, amount);
    }

    private static ConvertedOutput dust(Materials material, int amount, double probabilityMultiplier) {
        return output(material.getDust(amount), probabilityMultiplier);
    }

    private static ConvertedOutput item(Item item, int amount) {
        return item == null ? null : output(new ItemStack(item, amount), 1d);
    }

    private static ConvertedOutput item(ItemStack stack) {
        return output(stack, 1d);
    }

    private static ConvertedOutput output(ItemStack stack, double probabilityMultiplier) {
        return stack == null ? null : new ConvertedOutput(stack, probabilityMultiplier);
    }

    @Desugar
    public record ConversionResult(boolean matched, List<ConvertedOutput> outputs) {

        private static final ConversionResult UNMATCHED = new ConversionResult(false, Collections.emptyList());
        private static final ConversionResult DISCARDED = new ConversionResult(true, Collections.emptyList());

        private static ConversionResult unmatched() {
            return UNMATCHED;
        }

        private static ConversionResult discarded() {
            return DISCARDED;
        }

        private static ConversionResult converted(List<ConvertedOutput> outputs) {
            return new ConversionResult(true, outputs);
        }
    }

    @Desugar
    public record ConvertedOutput(ItemStack stack, double probabilityMultiplier) {}
}
