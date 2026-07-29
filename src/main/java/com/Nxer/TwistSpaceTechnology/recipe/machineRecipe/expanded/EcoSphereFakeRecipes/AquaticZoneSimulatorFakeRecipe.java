package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.MODE_RECIPE_DURATION;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode.EcoSphereModeSupport.getItemStackString;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.MegaTreeFarmRequirementKey.INSTANCE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.MegaTreeFarmTierRequirementKey;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public class AquaticZoneSimulatorFakeRecipe {

    static FluidStack DistilledWaterStack = FluidRegistry.getFluidStack("ic2distilledwater", 10000);
    private static final ItemStack Offspring = GTCMItemList.OffSpring.get(1);
    public static ArrayList<ItemStack> WatersOutputs = new ArrayList<>();
    public static HashMap<String, Integer> WatersChances = new HashMap<>();
    public static final List<TST_ItemID> AquaticItems = new ArrayList<>();

    static {
        addAquaticItem(Mods.Minecraft.ID, "fish", 0);
        addAquaticItem(Mods.Minecraft.ID, "fish", 1);
        addAquaticItem(Mods.Minecraft.ID, "fish", 2);
        addAquaticItem(Mods.Minecraft.ID, "fish", 3);
        if (Mods.PamsHarvestCraft.isModLoaded()) {
            addAquaticItem(Mods.PamsHarvestCraft.ID, "calamarirawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "anchovyrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "bassrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "carprawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "catfishrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "charrrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "crayfishrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "eelrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "grouperrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "herringrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "mudfishrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "octopusrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "perchrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "scalloprawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "shrimprawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "snailrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "snapperrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "tilapiarawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "troutrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "tunarawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "walleyerawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "greenheartfishItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "jellyfishrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "clamrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "crabrawItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "frograwItem", 0);
            addAquaticItem(Mods.PamsHarvestCraft.ID, "turtlerawItem", 0);
        }
    }

    private static void addAquaticItem(String modId, String itemName, int meta) {
        ItemStack stack = GTModHandler.getModItem(modId, itemName, 1, meta);
        if (stack != null) AquaticItems.add(TST_ItemID.createNoNBT(stack));
    }

    public static void loadRecipes() {
        initStatics();
        loadAquaticZoneFakeRecipes();
        loadAquaticZoneTrueRecipes();
        loadOffspringFakeRecipe();
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
        for (TST_ItemID aquaticItem : AquaticItems) {
            WatersOutputs.add(aquaticItem.getItemStackWithoutNBT(4));
        }
        deduplicateWatersOutputs();
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
            ? GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "jellyfishrawItem", 41, 0)
            : null;
        if (jellyfish == null && Offspring != null) jellyfish = Offspring.copy();
        if (jellyfish == null) return;
        jellyfish.stackSize = 41;
        jellyfish.setStackDisplayName(TextEnums.tr("MegaTreeFarm.nei.strangeJellyfish"));
        // #tr MegaTreeFarm.nei.strangeJellyfish
        // # Strange Jellyfish??
        // #zh_CN 奇怪的水母？？
        GTValues.RA.stdBuilder()
            .itemOutputs(jellyfish)
            .fluidInputs(DistilledWaterStack)
            .metadata(INSTANCE, StatCollector.translateToLocalFormatted("GT5U.nei.tier", "2??"))
            .fake()
            .duration(MODE_RECIPE_DURATION)
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
            .duration(MODE_RECIPE_DURATION)
            .eut(0)
            .addTo(GTCMRecipe.AquaticZoneSimulatorFakeRecipes);
    }
}
