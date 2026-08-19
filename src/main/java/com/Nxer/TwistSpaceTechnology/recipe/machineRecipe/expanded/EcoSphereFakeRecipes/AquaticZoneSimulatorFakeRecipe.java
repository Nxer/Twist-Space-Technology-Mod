package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator.MODE_RECIPE_DURATION;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereFluidCache.AQUATIC_MODE;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereFluidCache.cacheRecipeFluids;
import static com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereModeSupport.getItemStackString;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorBeaconRequirementKey.INSTANCE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorTierRequirementKey;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class AquaticZoneSimulatorFakeRecipe {

    public static final int DISTILLED_WATER_PER_PARALLEL = 10000;
    public static final int UNKNOWN_WATER_PER_PARALLEL = 10000;
    public static final int CHANCE_SCALE = 100000;
    public static final FluidStack DISTILLED_WATER_STACK = FluidRegistry
        .getFluidStack("ic2distilledwater", DISTILLED_WATER_PER_PARALLEL);
    public static final FluidStack UNKNOWN_WATER_STACK = FluidRegistry
        .getFluidStack("unknowwater", UNKNOWN_WATER_PER_PARALLEL);
    public static final ItemStack OFFSPRING = GTCMItemList.OffSpring.get(1);
    public static ArrayList<ItemStack> WatersOutputs = new ArrayList<>();
    public static HashMap<String, Integer> WatersChances = new HashMap<>();
    public static final List<ItemStack> UnknownWaterOutputs = new ArrayList<>();
    public static final Map<String, Integer> UnknownWaterChances = new HashMap<>();
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
        loadFakeRecipes(WatersOutputs, DISTILLED_WATER_STACK, 1);
        loadFakeRecipes(UnknownWaterOutputs, UNKNOWN_WATER_STACK, 2);
        loadOutputChances(WatersOutputs, WatersChances, 0);
        WatersOutputs.add(OFFSPRING);
        WatersChances.put(getItemStackString(OFFSPRING), 1);
        loadOutputChances(UnknownWaterOutputs, UnknownWaterChances, 18);
        cacheRecipeFluids(AQUATIC_MODE, GTCMRecipe.AquaticZoneSimulatorFakeRecipes);
    }

    static void initStatics() {
        // Distilled Water
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

        // Unknown Water
        UnknownWaterOutputs.clear();
        UnknownWaterChances.clear();
        if (!Mods.GalaxySpace.isModLoaded()) return;
        for (int meta = 0; meta < 6; meta++) {
            ItemStack algae = GTModHandler.getModItem(Mods.GalaxySpace.ID, "tcetiedandelions", 3, meta);
            if (algae != null) UnknownWaterOutputs.add(algae);
        }
        Collections.addAll(
            UnknownWaterOutputs,
            GregtechItemList.AlgaeBiomass.get(6),
            GregtechItemList.GreenAlgaeBiomass.get(16),
            GregtechItemList.BrownAlgaeBiomass.get(13),
            GregtechItemList.GoldenBrownAlgaeBiomass.get(10),
            GregtechItemList.RedAlgaeBiomass.get(9));
        UnknownWaterOutputs.removeIf(stack -> stack == null || stack.getItem() == null);
    }

    static void loadFakeRecipes(List<ItemStack> outputs, FluidStack inputFluid, int requiredBeaconTier) {
        if (inputFluid == null || outputs.isEmpty()) return;
        for (ItemStack output : outputs) {
            ItemStack focusInput = output.copy();
            focusInput.stackSize = 0;
            addFakeRecipe(focusInput, output, inputFluid, requiredBeaconTier);
        }
    }

    static void loadOutputChances(List<ItemStack> outputs, Map<String, Integer> chances, int emptyWeight) {
        if (outputs.isEmpty()) return;
        int totalWeight = emptyWeight;
        for (ItemStack output : outputs) totalWeight += output.stackSize;
        int basicChance = CHANCE_SCALE / totalWeight;
        for (ItemStack output : outputs) {
            chances.put(getItemStackString(output), basicChance * output.stackSize);
        }
    }

    static void addFakeRecipe(ItemStack inputStacks, ItemStack outputStacks, FluidStack inputFluid,
        int requiredBeaconTier) {
        GTValues.RA.stdBuilder()
            .itemInputs(inputStacks)
            .itemOutputs(outputStacks)
            .fluidInputs(inputFluid)
            .metadata(EcoSphereSimulatorTierRequirementKey.INSTANCE, 1)
            .metadata(INSTANCE, requiredBeaconTier)
            .fake()
            .duration(MODE_RECIPE_DURATION)
            .eut(0)
            .addTo(GTCMRecipe.AquaticZoneSimulatorFakeRecipes);
    }
}
