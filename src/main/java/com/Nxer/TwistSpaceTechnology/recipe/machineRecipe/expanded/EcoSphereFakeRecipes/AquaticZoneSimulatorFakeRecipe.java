package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.getItemStackString;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.MegaTreeFarmRequirementKey.INSTANCE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.MegaTreeFarmTierRequirementKey;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public class AquaticZoneSimulatorFakeRecipe {

    static FluidStack DistilledWaterStack = FluidRegistry.getFluidStack("ic2distilledwater", 10000);
    private static final ItemStack Offspring = GTCMItemList.OffSpring.get(1);
    public static ArrayList<ItemStack> WatersOutputs = new ArrayList<>();
    public static HashMap<String, Integer> WatersChances = new HashMap<>();

    public static void loadRecipes() {
        initStatics();
        loadAquaticZoneFakeRecipes();
        loadAquaticZoneTrueRecipes();
        // loadOffspringFakeRecipe();
    }

    static void initStatics() {
        WatersOutputs.clear();
        WatersChances.clear();
        Collections.addAll(
            WatersOutputs,
            GTModHandler.getModItem(Mods.Minecraft.ID, "waterlily", 1, 0),
            GTModHandler.getModItem(Mods.Minecraft.ID, "vine", 3, 0),
            GTModHandler.getModItem(Mods.Minecraft.ID, "bone", 3, 0),
            GTModHandler.getModItem(Mods.Minecraft.ID, "dye", 2, 0));
        if (Mods.BiomesOPlenty.isModLoaded()) {
            Collections.addAll(
                WatersOutputs,
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 0),
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 1),
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 2),
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 3),
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 3),
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "coral1", 2, 12),
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "coral1", 2, 13),
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "coral1", 2, 14),
                GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "coral1", 2, 15));
        }
        if (Mods.PamsHarvestCraft.isModLoaded()) Collections.addAll(
            WatersOutputs,
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "cranberryItem", 1, 0),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "riceItem", 1, 0),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "seaweedItem", 5, 0),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "waterchestnutItem", 2, 0));
        if (Mods.TwilightForest.isModLoaded()) Collections
            .addAll(WatersOutputs, GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.HugeLilyPad", 1, 0));
        addSwampLootFish();
        deduplicateWatersOutputs();
    }

    private static void addSwampLootFish() {
        WeightedRandomChestContent[] contents = ChestGenHooks.getInfo("loot_swamp")
            .getItems(new Random());
        if (contents == null) return;
        for (WeightedRandomChestContent content : contents) {
            if (content == null || content.theItemId == null || !isSupportedFish(content.theItemId)) continue;
            WatersOutputs.add(content.theItemId.copy());
        }
    }

    private static boolean isSupportedFish(ItemStack stack) {
        String registryName = Item.itemRegistry.getNameForObject(stack.getItem());
        if (registryName == null) return false;
        if ("minecraft:fish".equals(registryName)) return true;
        int separator = registryName.indexOf(':');
        if (separator < 0 || !registryName.substring(0, separator)
            .equalsIgnoreCase(Mods.PamsHarvestCraft.ID)) return false;
        return registryName.substring(separator + 1)
            .toLowerCase(Locale.ROOT)
            .endsWith("rawitem");
    }

    private static void deduplicateWatersOutputs() {
        Map<String, ItemStack> uniqueOutputs = new LinkedHashMap<>();
        for (ItemStack stack : WatersOutputs) {
            if (stack == null || stack.getItem() == null) continue;
            uniqueOutputs.putIfAbsent(getItemStackString(stack), stack);
        }
        WatersOutputs.clear();
        WatersOutputs.addAll(uniqueOutputs.values());
    }

    static void loadAquaticZoneFakeRecipes() {
        if (DistilledWaterStack == null) return;
        for (ItemStack aStack : WatersOutputs) {
            ItemStack Input = aStack.copy();
            Input.stackSize = 0;
            // addEnchantmentLight(Input);
            addFakeRecipe(Input, aStack, DistilledWaterStack);
        }
    }

    static void loadOffspringFakeRecipe() {
        if (DistilledWaterStack == null) return;
        ItemStack jellyfish = Mods.PamsHarvestCraft.isModLoaded()
            ? GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "jellyfishrawItem", 5, 0)
            : null;
        if (jellyfish == null && Offspring != null) jellyfish = Offspring.copy();
        if (jellyfish == null) return;
        jellyfish.stackSize = 5;
        jellyfish.setStackDisplayName(TextEnums.tr("MegaTreeFarm.nei.strangeJellyfish"));
        // #tr MegaTreeFarm.nei.strangeJellyfish
        // # Strange Jellyfish??
        // #zh_CN 奇怪的水母？？
        GTValues.RA.stdBuilder()
            .itemOutputs(jellyfish)
            .fluidInputs(DistilledWaterStack)
            .metadata(INSTANCE, StatCollector.translateToLocalFormatted("GT5U.nei.tier", "2??"))
            .fake()
            .duration(20 * 5)
            .eut(0)
            .addTo(GTCMRecipe.AquaticZoneSimulatorFakeRecipes);
    }

    static void loadAquaticZoneTrueRecipes() {
        // generate recipe chance
        int TotalSize = 0;
        for (ItemStack aStack : WatersOutputs) TotalSize += aStack.stackSize;
        int BasicSize = 10000 / TotalSize;
        for (ItemStack aStack : WatersOutputs) {
            if (aStack == null) continue;
            WatersChances.put(getItemStackString(aStack), BasicSize * aStack.stackSize);
        }
        WatersOutputs.add(Offspring);
        WatersChances.put(getItemStackString(Offspring), 1);
    }

    static void addFakeRecipe(ItemStack inputStacks, ItemStack outputStacks, FluidStack inputFluid) {
        GTValues.RA.stdBuilder()
            .itemInputs(inputStacks)
            .itemOutputs(outputStacks)
            .fluidInputs(inputFluid)
            .metadata(MegaTreeFarmTierRequirementKey.INSTANCE, 1)
            .fake()
            .duration(20 * 5)
            .eut(0)
            .addTo(GTCMRecipe.AquaticZoneSimulatorFakeRecipes);
    }
}
