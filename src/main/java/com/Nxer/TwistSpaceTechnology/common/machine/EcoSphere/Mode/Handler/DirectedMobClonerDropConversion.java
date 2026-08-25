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
        register(Items.wooden_sword, dust(Materials.Wood, 2.5));
        register(Items.stone_sword, dust(Materials.Stone, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.iron_sword, dust(Materials.Iron, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.golden_sword, dust(Materials.Gold, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.diamond_sword, dust(Materials.Diamond, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.stone_axe, dust(Materials.Stone, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.iron_axe, dust(Materials.Iron, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.golden_axe, dust(Materials.Gold, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.stone_pickaxe, dust(Materials.Stone, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.iron_pickaxe, dust(Materials.Iron, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.golden_pickaxe, dust(Materials.Gold, 3), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.golden_hoe, dust(Materials.Gold, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.stone_shovel, dust(Materials.Stone, 1), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.iron_shovel, dust(Materials.Iron, 1), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.golden_shovel, dust(Materials.Gold, 1), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(Items.shears, dust(Materials.Iron, 2), dust(Materials.Wood, 1, 1d / 4 * 2));

        register(Items.leather_helmet, item(Items.leather, 5));
        register(Items.leather_chestplate, item(Items.leather, 8));
        register(Items.leather_leggings, item(Items.leather, 7));
        register(Items.leather_boots, item(Items.leather, 4));
        register(Items.chainmail_helmet, dust(Materials.Steel, 5));
        register(Items.chainmail_chestplate, dust(Materials.Steel, 8));
        register(Items.chainmail_leggings, dust(Materials.Steel, 7));
        register(Items.chainmail_boots, dust(Materials.Steel, 4));
        register(Items.iron_helmet, dust(Materials.Iron, 5));
        register(Items.iron_chestplate, dust(Materials.Iron, 8));
        register(Items.iron_leggings, dust(Materials.Iron, 7));
        register(Items.iron_boots, dust(Materials.Iron, 4));
        register(Items.golden_helmet, dust(Materials.Gold, 5));
        register(Items.golden_chestplate, dust(Materials.Gold, 8));
        register(Items.golden_leggings, dust(Materials.Gold, 7));
        register(Items.golden_boots, dust(Materials.Gold, 4));
        register(Items.diamond_helmet, dust(Materials.Diamond, 5));
        register(Items.diamond_chestplate, dust(Materials.Diamond, 8));
        register(Items.diamond_leggings, dust(Materials.Diamond, 7));
        register(Items.diamond_boots, dust(Materials.Diamond, 4));

        register(Items.bow, item(Items.string, 3), dust(Materials.Wood, 1, 1d / 4 * 6 * 0.95));
        register(Items.fishing_rod, item(Items.string, 2), dust(Materials.Wood, 1, 1d / 4 * 6 * 0.95));
        register(Items.flint_and_steel, dust(Materials.Steel, 1), dust(Materials.Flint, 1, 1d / 4 * 2));
        register(Items.flint, dust(Materials.Flint, 0.5));

        register(new ItemStack(Items.gold_nugget), dust(Materials.Gold, 1, 1d / 9));
        register(new ItemStack(Items.gold_ingot), dust(Materials.Gold, 1));
        register(new ItemStack(Items.iron_ingot), dust(Materials.Iron, 1));
        register(new ItemStack(Items.emerald), dust(Materials.Emerald, 1));
        register(new ItemStack(Items.coal), dust(Materials.Coal, 1));
        register(new ItemStack(Items.book), dust(Materials.Paper, 3));
        register(new ItemStack(Items.writable_book), dust(Materials.Paper, 3));
        register(new ItemStack(Items.bucket), dust(Materials.Iron, 3));
        register(new ItemStack(Items.golden_apple), dust(Materials.Gold, 8));
        register(new ItemStack(Items.wheat), dust(Materials.Wheat, 1));
        register(new ItemStack(Items.melon), item(new ItemStack(Items.melon_seeds)));
        register(new ItemStack(Items.speckled_melon), dust(Materials.Gold, 1, 1d / 9 * 8));
        register(new ItemStack(Items.glass_bottle), dust(Materials.Glass, 1));
        register(new ItemStack(Items.snowball), dust(Materials.Snow, 1));
        register(new ItemStack(Items.snowball, 1, 1), dust(Materials.Snow, 1));
        register(new ItemStack(Items.blaze_rod), output(new ItemStack(Items.blaze_powder), 3.5));
        register(new ItemStack(Items.chicken), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(new ItemStack(Items.cooked_fished), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(new ItemStack(Items.beef), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(new ItemStack(Items.porkchop), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(new ItemStack(Blocks.gravel), dust(Materials.Stone, 1), output(new ItemStack(Items.flint), 0.1));
        register(new ItemStack(Blocks.stone), dust(Materials.Stone, 1));
        register(new ItemStack(Blocks.obsidian), dust(Materials.Obsidian, 2));
        register(new ItemStack(Blocks.packed_ice), dust(Materials.Ice, 2));
        register(new ItemStack(Items.bone), item(new ItemStack(Items.dye, 4, 15)));
        register(new ItemStack(Items.dye, 1, 15), output(new ItemStack(Items.stick), 0.25));
        register(new ItemStack(Items.stick), dust(Materials.Wood, 2));
        register(new ItemStack(Items.nether_star), dust(Materials.NetherStar, 1));
        register(new ItemStack(Blocks.wool), output(new ItemStack(Items.string), 2.5));
        register(new ItemStack(Items.paper), dust(Materials.Paper, 1));
        register(new ItemStack(Blocks.web), output(new ItemStack(Items.string), 1.5));
        register(new ItemStack(Items.ender_pearl), dust(Materials.EnderPearl, 1));
        register(new ItemStack(Items.ender_eye), dust(Materials.EnderEye, 1));
        register(new ItemStack(Blocks.ice), dust(Materials.Ice, 1));
        register(new ItemStack(Blocks.sand), dust(Materials.Sand, 1, 1.6));

        // Et Futurum
        register(GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "rabbit_raw", 1, 0), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));

        // Forbidden Magic
        register(GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "FMResource", 1, 0), dust(Materials.Emerald, 1, 1d / 9));

        // Galacticraft
        register(new ItemStack(GCItems.meteoricIronRaw), dust(Materials.MeteoricIron, 1));

        // HarvestCraft
        register(GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "muttonrawItem", 1, 0), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));

        // GregTech
        register(GTOreDictUnificator.get(OrePrefixes.rawOre, Materials.MeteoricIron, 1), dust(Materials.MeteoricIron, 1));
        register(Materials.Silver.getNuggets(1), dust(Materials.Silver, 1, 1d / 9));

        // GT++
        register(MaterialsElements.STANDALONE.DRAGON_METAL.getNugget(1), output(MaterialsElements.STANDALONE.DRAGON_METAL.getDust(1), 1d / 9));
        register(MaterialsElements.STANDALONE.DRAGON_METAL.getIngot(1), item(MaterialsElements.STANDALONE.DRAGON_METAL.getDust(1)));
        register(MaterialsAlloy.STABALLOY.getIngot(1), item(MaterialsAlloy.STABALLOY.getDust(1)));
        register(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "material", 1, 1), dust(Materials.Blizz, 3.5));
        register(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 0), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 2), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 4), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 6), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(GTModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicMetaFood", 1, 8), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));

        // Magic Bees
        register(GTModHandler.getModItem(Mods.MagicBees.ID, "beeNugget", 1, NuggetType.SILVER.ordinal()), dust(Materials.Silver, 1, 1d / 9));
        register(GTModHandler.getModItem(Mods.MagicBees.ID, "beeNugget", 1, NuggetType.EMERALD.ordinal()), dust(Materials.Emerald, 1, 1d / 9));

        // Thaumcraft
        register(ConfigItems.itemSwordThaumium, dust(Materials.Thaumium, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(ConfigItems.itemSwordVoid, dust(Materials.Void, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(ConfigItems.itemBootsCultist);
        register(ConfigItems.itemChestCultistLeaderPlate);
        register(ConfigItems.itemChestCultistPlate);
        register(ConfigItems.itemChestCultistRobe);
        register(ConfigItems.itemHelmetCultistLeaderPlate);
        register(ConfigItems.itemHelmetCultistPlate);
        register(ConfigItems.itemHelmetCultistRobe);
        register(ConfigItems.itemLegsCultistLeaderPlate);
        register(ConfigItems.itemLegsCultistPlate);
        register(ConfigItems.itemLegsCultistRobe);
        register(ConfigItems.itemSwordCrimson);
        register(new ItemStack(ConfigItems.itemResource, 1, 2), dust(Materials.Thaumium, 1));
        register(new ItemStack(ConfigItems.itemResource, 1, 18), dust(Materials.Gold, 1, 1d / 9));
        register(new ItemStack(ConfigItems.itemNugget, 1, 3), dust(Materials.Silver, 1, 1d / 9));

        // Twilight Forest
        register(TFItems.arcticHelm, item(TFItems.arcticFur, 5));
        register(TFItems.arcticPlate, item(TFItems.arcticFur, 8));
        register(TFItems.ironwoodHelm, dust(Materials.IronWood, 5));
        register(TFItems.ironwoodPlate, dust(Materials.IronWood, 8));
        register(TFItems.ironwoodSword, dust(Materials.IronWood, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(TFItems.knightlyHelm, dust(Materials.Knightmetal, 5));
        register(TFItems.knightlyPlate, dust(Materials.Knightmetal, 8));
        register(TFItems.knightlySword, dust(Materials.Knightmetal, 2), dust(Materials.Wood, 1, 1d / 4 * 2));
        register(TFItems.steeleafHelm, dust(Materials.Steeleaf, 5));
        register(TFItems.steeleafPlate, dust(Materials.Steeleaf, 8));
        register(TFItems.steeleafSword, dust(Materials.Steeleaf, 2), dust(Materials.Wood, 1, 1d / 4 * 2));

        register(new ItemStack(TFItems.armorShard), dust(Materials.Knightmetal, 1, 1d / 9));
        register(new ItemStack(TFBlocks.towerWood), dust(Materials.Wood, 1));
        register(new ItemStack(TFItems.venisonRaw), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(new ItemStack(TFItems.meefRaw), dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
        register(new ItemStack(TFItems.giantPick), dust(Materials.Stone, 64), dust(Materials.Wood, 64));
        register(new ItemStack(TFItems.giantSword), dust(Materials.Stone, 64), dust(Materials.Wood, 64));

        // Witchery
        register(Witchery.Items.BABAS_HAT);
        register(Witchery.Items.GULGS_GURDLE);
        register(Witchery.Items.HUNTSMANS_SPEAR);
        register(Witchery.Items.MOGS_QUIVER);
        register(Witchery.Items.VAMPIRE_BOOTS);
        register(Witchery.Items.VAMPIRE_COAT_CHAIN);
        register(Witchery.Items.VAMPIRE_COAT_FEMALE_CHAIN);
        register(Witchery.Items.VAMPIRE_COAT);
        register(Witchery.Items.VAMPIRE_COAT_FEMALE);
        register(Witchery.Items.VAMPIRE_LEGS);
        register(Witchery.Items.VAMPIRE_LEGS_KILT);
        register(new ItemStack(Witchery.Blocks.SAPLING, 1, 0), dust(Materials.Wood, 0.5));
        register(new ItemStack(Witchery.Blocks.SAPLING, 1, 1), dust(Materials.Wood, 0.5));
        register(new ItemStack(Witchery.Blocks.SAPLING, 1, 2), dust(Materials.Wood, 0.5));
        //spotless:on

        // Dynamically registered aquatic items may come from several mods.
        for (TST_ItemID aquaticItem : AquaticZoneSimulatorFakeRecipe.AquaticItems) {
            ItemStack stack = aquaticItem.getItemStackWithoutNBT();
            if (stack != null) {
                register(stack, dust(Materials.MeatRaw, 1), dust(Materials.Bone, 1, 1d / 10 / 9));
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

    private static void register(Item input, ConvertedOutput... outputs) {
        if (input == null) return;
        register(TST_ItemID.createAsWildcard(new ItemStack(input)), outputs);
    }

    private static void register(ItemStack input, ConvertedOutput... outputs) {
        if (input == null || input.getItem() == null || input.stackSize <= 0) return;
        register(TST_ItemID.createNoNBT(input), outputs);
    }

    private static void register(TST_ItemID input, ConvertedOutput... outputs) {
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
