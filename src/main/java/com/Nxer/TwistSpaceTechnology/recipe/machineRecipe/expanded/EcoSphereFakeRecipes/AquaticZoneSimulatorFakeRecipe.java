package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.getItemStackString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public class AquaticZoneSimulatorFakeRecipe {

    private static final Logger LOGGER = LogManager.getLogger(AquaticZoneSimulatorFakeRecipe.class);
    static FluidStack WaterStack = Materials.Water.getFluid(10000);
    private static final ItemStack Offspring = GTCMItemList.OffSpring.get(1);
    public static ArrayList<ItemStack> WatersOutputs = new ArrayList<>();
    public static HashMap<String, Integer> WatersChances = new HashMap<>();

    public static void loadRecipes() {
        initStatics();
        loadAquaticZoneFakeRecipes();
        loadAquaticZoneTrueRecipes();
    }

    private static List<WeightedRandomFishable> cachedFishList = null;

    static void initStatics() {
        // generate fishes
        List<WeightedRandomFishable> fishList = getFishList();

        for (WeightedRandomFishable fishable : fishList) {
            try {
                Field itemStackField = WeightedRandomFishable.class.getDeclaredField("field_150711_b");
                itemStackField.setAccessible(true);
                ItemStack itemStack = (ItemStack) itemStackField.get(fishable);

                if (itemStack != null) {
                    ItemStack stackCopy = itemStack.copy();
                    stackCopy.stackSize = 4;
                    WatersOutputs.add(stackCopy);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error("TST_AquaticZoneFakeRecipeGenerateFail", e);
            }
        }
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
    }

    @SuppressWarnings("unchecked")
    private static List<WeightedRandomFishable> getFishList() {
        if (cachedFishList == null) {
            try {
                Field fishField = FishingHooks.class.getDeclaredField("fish");
                fishField.setAccessible(true);
                cachedFishList = (ArrayList<WeightedRandomFishable>) fishField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error("TST_AquaticZoneFakeRecipeGenerateFail", e);
                return new ArrayList<>();
            }
        }
        return cachedFishList;
    }

    static void loadAquaticZoneFakeRecipes() {
        for (ItemStack aStack : WatersOutputs) {
            ItemStack Input = aStack.copy();
            Input.stackSize = 0;
            // addEnchantmentLight(Input);
            addFakeRecipe(Input, aStack, WaterStack);
        }
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
            .fake()
            .duration(20 * 5)
            .eut(0)
            .addTo(GTCMRecipe.AquaticZoneSimulatorFakeRecipes);
    }
}
