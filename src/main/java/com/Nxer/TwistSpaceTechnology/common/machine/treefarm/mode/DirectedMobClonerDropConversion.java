package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe.AquaticItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;

public enum DirectedMobClonerDropConversion {

    WOODEN_SWORD("minecraft", "wooden_sword", Materials.Wood, 2, true),
    STONE_SWORD("minecraft", "stone_sword", Materials.Stone, 2, true),
    IRON_SWORD("minecraft", "iron_sword", Materials.Iron, 2, true),
    GOLDEN_SWORD("minecraft", "golden_sword", Materials.Gold, 2, true),
    DIAMOND_SWORD("minecraft", "diamond_sword", Materials.Diamond, 2, true),
    STONE_AXE("minecraft", "stone_axe", Materials.Stone, 3, true),
    IRON_AXE("minecraft", "iron_axe", Materials.Iron, 3, true),
    GOLDEN_AXE("minecraft", "golden_axe", Materials.Gold, 3, true),
    STONE_PICKAXE("minecraft", "stone_pickaxe", Materials.Stone, 3, true),
    IRON_PICKAXE("minecraft", "iron_pickaxe", Materials.Iron, 3, true),
    GOLDEN_PICKAXE("minecraft", "golden_pickaxe", Materials.Gold, 3, true),
    GOLDEN_HOE("minecraft", "golden_hoe", Materials.Gold, 2, true),
    STONE_SHOVEL("minecraft", "stone_shovel", Materials.Stone, 1, true),
    IRON_SHOVEL("minecraft", "iron_shovel", Materials.Iron, 1, true),
    GOLDEN_SHOVEL("minecraft", "golden_shovel", Materials.Gold, 1, true),
    SHEARS("minecraft", "shears", Materials.Iron, 2, true),

    LEATHER_HELMET("minecraft", "leather_helmet", Materials.Leather, 5, false),
    LEATHER_CHESTPLATE("minecraft", "leather_chestplate", Materials.Leather, 8, false),
    LEATHER_LEGGINGS("minecraft", "leather_leggings", Materials.Leather, 7, false),
    LEATHER_BOOTS("minecraft", "leather_boots", Materials.Leather, 4, false),
    CHAINMAIL_HELMET("minecraft", "chainmail_helmet", Materials.Steel, 5, false),
    CHAINMAIL_CHESTPLATE("minecraft", "chainmail_chestplate", Materials.Steel, 8, false),
    CHAINMAIL_LEGGINGS("minecraft", "chainmail_leggings", Materials.Steel, 7, false),
    CHAINMAIL_BOOTS("minecraft", "chainmail_boots", Materials.Steel, 4, false),
    IRON_HELMET("minecraft", "iron_helmet", Materials.Iron, 5, false),
    IRON_CHESTPLATE("minecraft", "iron_chestplate", Materials.Iron, 8, false),
    IRON_LEGGINGS("minecraft", "iron_leggings", Materials.Iron, 7, false),
    IRON_BOOTS("minecraft", "iron_boots", Materials.Iron, 4, false),
    GOLDEN_HELMET("minecraft", "golden_helmet", Materials.Gold, 5, false),
    GOLDEN_CHESTPLATE("minecraft", "golden_chestplate", Materials.Gold, 8, false),
    GOLDEN_LEGGINGS("minecraft", "golden_leggings", Materials.Gold, 7, false),
    GOLDEN_BOOTS("minecraft", "golden_boots", Materials.Gold, 4, false),
    DIAMOND_HELMET("minecraft", "diamond_helmet", Materials.Diamond, 5, false),
    DIAMOND_CHESTPLATE("minecraft", "diamond_chestplate", Materials.Diamond, 8, false),
    DIAMOND_LEGGINGS("minecraft", "diamond_leggings", Materials.Diamond, 7, false),
    DIAMOND_BOOTS("minecraft", "diamond_boots", Materials.Diamond, 4, false),

    BOW("minecraft", "bow", SpecialConversion.BOW),
    FISHING_ROD("minecraft", "fishing_rod", SpecialConversion.FISHING_ROD),
    FLINT_AND_STEEL("minecraft", "flint_and_steel", SpecialConversion.FLINT_AND_STEEL),

    THAUMIUM_SWORD("Thaumcraft", "ItemSwordThaumium", Materials.Thaumium, 2, true),
    VOID_SWORD("Thaumcraft", "ItemSwordVoid", Materials.Void, 2, true),
    CULTIST_BOOTS("Thaumcraft", "ItemBootsCultist"),
    CULTIST_LEADER_CHESTPLATE("Thaumcraft", "ItemChestplateCultistLeaderPlate"),
    CULTIST_CHESTPLATE("Thaumcraft", "ItemChestplateCultistPlate"),
    CULTIST_ROBE("Thaumcraft", "ItemChestplateCultistRobe"),
    CULTIST_LEADER_HELMET("Thaumcraft", "ItemHelmetCultistLeaderPlate"),
    CULTIST_HELMET("Thaumcraft", "ItemHelmetCultistPlate"),
    CULTIST_HOOD("Thaumcraft", "ItemHelmetCultistRobe"),
    CULTIST_LEADER_LEGGINGS("Thaumcraft", "ItemLeggingsCultistLeaderPlate"),
    CULTIST_LEGGINGS("Thaumcraft", "ItemLeggingsCultistPlate"),
    CULTIST_ROBE_LEGGINGS("Thaumcraft", "ItemLeggingsCultistRobe"),
    CRIMSON_SWORD("Thaumcraft", "ItemSwordCrimson"),

    ARCTIC_HELMET("TwilightForest", "item.arcticHelm", SpecialConversion.ARCTIC_HELMET),
    ARCTIC_CHESTPLATE("TwilightForest", "item.arcticPlate", SpecialConversion.ARCTIC_CHESTPLATE),
    IRONWOOD_HELMET("TwilightForest", "item.ironwoodHelm", Materials.IronWood, 5, false),
    IRONWOOD_CHESTPLATE("TwilightForest", "item.ironwoodPlate", Materials.IronWood, 8, false),
    IRONWOOD_SWORD("TwilightForest", "item.ironwoodSword", Materials.IronWood, 2, true),
    KNIGHTMETAL_HELMET("TwilightForest", "item.knightlyHelm", Materials.Knightmetal, 5, false),
    KNIGHTMETAL_CHESTPLATE("TwilightForest", "item.knightlyPlate", Materials.Knightmetal, 8, false),
    KNIGHTMETAL_SWORD("TwilightForest", "item.knightlySword", Materials.Knightmetal, 2, true),
    STEELEAF_HELMET("TwilightForest", "item.steeleafHelm", Materials.Steeleaf, 5, false),
    STEELEAF_CHESTPLATE("TwilightForest", "item.steeleafPlate", Materials.Steeleaf, 8, false),
    STEELEAF_SWORD("TwilightForest", "item.steeleafSword", Materials.Steeleaf, 2, true),

    BABA_YAGA_HAT("witchery", "babashat"),
    GULG_GIRDLE("witchery", "gurdleofgulg"),
    HUNTSMAN_SPEAR("witchery", "huntsmanspear"),
    MOG_QUIVER("witchery", "quiverofmog"),
    VAMPIRE_BOOTS("witchery", "vampireboots"),
    VAMPIRE_CHAIN_COAT("witchery", "vampirechaincoat"),
    VAMPIRE_CHAIN_COAT_FEMALE("witchery", "vampirechaincoat_female"),
    VAMPIRE_COAT("witchery", "vampirecoat"),
    VAMPIRE_COAT_FEMALE("witchery", "vampirecoat_female"),
    VAMPIRE_LEGS("witchery", "vampirelegs"),
    VAMPIRE_LEGS_KILT("witchery", "vampirelegs_kilt");

    private static final double TWO_SMALL_DUSTS = 0.5d;
    private static final double SIX_SMALL_DUSTS_AT_95_PERCENT = 1.425d;
    private static final double TEN_PERCENT_TINY_DUST = 1d / 90d;
    private static final Map<String, DirectedMobClonerDropConversion> CONVERSIONS = new HashMap<>();

    static {
        for (DirectedMobClonerDropConversion conversion : values()) {
            CONVERSIONS.put(conversion.modId + ':' + conversion.registryName, conversion);
        }
    }

    private final String modId;
    private final String registryName;
    private final Materials material;
    private final int materialAmount;
    private final boolean addWoodPulp;
    private final SpecialConversion specialConversion;
    private final boolean discard;

    DirectedMobClonerDropConversion(String modId, String registryName, Materials material, int materialAmount,
        boolean addWoodPulp) {
        this.modId = modId;
        this.registryName = registryName;
        this.material = material;
        this.materialAmount = materialAmount;
        this.addWoodPulp = addWoodPulp;
        this.specialConversion = null;
        this.discard = false;
    }

    DirectedMobClonerDropConversion(String modId, String registryName, SpecialConversion specialConversion) {
        this.modId = modId;
        this.registryName = registryName;
        this.material = null;
        this.materialAmount = 0;
        this.addWoodPulp = false;
        this.specialConversion = specialConversion;
        this.discard = false;
    }

    DirectedMobClonerDropConversion(String modId, String registryName) {
        this.modId = modId;
        this.registryName = registryName;
        this.material = null;
        this.materialAmount = 0;
        this.addWoodPulp = false;
        this.specialConversion = null;
        this.discard = true;
    }

    public static ConversionResult convert(ItemStack source) {
        if (source == null || source.getItem() == null) return ConversionResult.unmatched();
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(source.getItem());
        if (AquaticItems.contains(TST_ItemID.createNoNBT(source))) {
            return ConversionResult.converted(convertRawFish(source.stackSize));
        }
        if (identifier == null || "TConstruct".equals(identifier.modId)) return ConversionResult.unmatched();
        DirectedMobClonerDropConversion conversion = CONVERSIONS.get(identifier.modId + ':' + identifier.name);
        if (conversion == null) return ConversionResult.unmatched();
        if (conversion.discard) return ConversionResult.discarded();
        if (conversion.specialConversion != null) {
            return ConversionResult.converted(conversion.specialConversion.createOutputs(source.stackSize));
        }
        return ConversionResult.converted(conversion.createMaterialOutputs(source.stackSize));
    }

    private List<ConvertedOutput> createMaterialOutputs(int sourceAmount) {
        List<ConvertedOutput> outputs = new ArrayList<>(2);
        if (material == Materials.Leather) {
            outputs.add(new ConvertedOutput(new ItemStack(Items.leather, materialAmount * sourceAmount), 1d));
        } else {
            addDust(outputs, material, materialAmount * sourceAmount, 1d);
        }
        if (addWoodPulp) addDust(outputs, Materials.Wood, sourceAmount, TWO_SMALL_DUSTS);
        return outputs;
    }

    private static List<ConvertedOutput> convertRawFish(int sourceAmount) {
        List<ConvertedOutput> outputs = new ArrayList<>(2);
        addDust(outputs, Materials.MeatRaw, sourceAmount, 1d);
        addDust(outputs, Materials.Bone, sourceAmount, TEN_PERCENT_TINY_DUST);
        return outputs;
    }

    private static void addDust(List<ConvertedOutput> outputs, Materials material, int amount,
        double probabilityMultiplier) {
        ItemStack dust = material.getDust(amount);
        if (dust != null) outputs.add(new ConvertedOutput(dust, probabilityMultiplier));
    }

    private static void addItem(List<ConvertedOutput> outputs, Item item, int amount, double probabilityMultiplier) {
        if (item != null && amount > 0)
            outputs.add(new ConvertedOutput(new ItemStack(item, amount), probabilityMultiplier));
    }

    private enum SpecialConversion {

        BOW {

            @Override
            List<ConvertedOutput> createOutputs(int sourceAmount) {
                List<ConvertedOutput> outputs = new ArrayList<>(2);
                addItem(outputs, Items.string, 3 * sourceAmount, 1d);
                addDust(outputs, Materials.Wood, sourceAmount, SIX_SMALL_DUSTS_AT_95_PERCENT);
                return outputs;
            }
        },
        FISHING_ROD {

            @Override
            List<ConvertedOutput> createOutputs(int sourceAmount) {
                List<ConvertedOutput> outputs = new ArrayList<>(2);
                addItem(outputs, Items.string, 2 * sourceAmount, 1d);
                addDust(outputs, Materials.Wood, sourceAmount, SIX_SMALL_DUSTS_AT_95_PERCENT);
                return outputs;
            }
        },
        FLINT_AND_STEEL {

            @Override
            List<ConvertedOutput> createOutputs(int sourceAmount) {
                List<ConvertedOutput> outputs = new ArrayList<>(2);
                addDust(outputs, Materials.Steel, sourceAmount, 1d);
                addDust(outputs, Materials.Flint, sourceAmount, TWO_SMALL_DUSTS);
                return outputs;
            }
        },
        ARCTIC_HELMET {

            @Override
            List<ConvertedOutput> createOutputs(int sourceAmount) {
                return createArcticFurOutput(5 * sourceAmount);
            }
        },
        ARCTIC_CHESTPLATE {

            @Override
            List<ConvertedOutput> createOutputs(int sourceAmount) {
                return createArcticFurOutput(8 * sourceAmount);
            }
        };

        abstract List<ConvertedOutput> createOutputs(int sourceAmount);

        private static List<ConvertedOutput> createArcticFurOutput(int amount) {
            Item arcticFur = GameRegistry.findItem("TwilightForest", "item.arcticFur");
            if (arcticFur == null) return Collections.emptyList();
            return Collections.singletonList(new ConvertedOutput(new ItemStack(arcticFur, amount), 1d));
        }
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
