package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.EcoSphereFakeRecipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GT_ModHandler;

public class AquaticZoneSimulatorFakeRecipe implements IRecipePool {

    private static final Logger LOGGER = LogManager.getLogger(AquaticZoneSimulatorFakeRecipe.class);
    static FluidStack WaterStack = Materials.Water.getFluid(1000);
    ArrayList<ItemStack> WatersOutputs = new ArrayList<>();
    ItemStack[] ExtraOutputs = { GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.HugeLilyPad", 1, 0),
        GT_ModHandler.getModItem(Mods.Minecraft.ID, "waterlily", 1, 0),
        GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 0),
        GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 1),
        GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 2),
        GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "lilyBop", 1, 3),
        GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "cranberryItem", 1, 0),
        GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "riceItem", 1, 0),
        GT_ModHandler.getModItem(Mods.Minecraft.ID, "vine", 2, 0),
        GT_ModHandler.getModItem(Mods.Minecraft.ID, "bone", 2, 0),
        GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "seaweedItem", 3, 0),
        GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "waterchestnutItem", 3, 0),
        GT_ModHandler.getModItem(Mods.Minecraft.ID, "dye", 3, 0) };

    EnumMap<ItemStack, Integer> OutputChance = new EnumMap<>(ItemStack.class);

    public GT_Recipe AquaticZoneSimulatorTrueRecipe;

    @Override
    public void loadRecipes() {
        initStatics();
        loadAquaticZoneFakeRecipes();
        loadAquaticZoneTrueRecipes();
    }

    private static List<WeightedRandomFishable> cachedFishList = null;

    void initStatics() {
        // generate fishes
        Collections.addAll(WatersOutputs, ExtraOutputs);
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
                LOGGER.error("TST_AquaticZoneFakeRecipeGenerateFail_2", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static List<WeightedRandomFishable> getFishList() {
        if (cachedFishList == null) {
            try {
                Field fishField = FishingHooks.class.getDeclaredField("fish");
                fishField.setAccessible(true);
                cachedFishList = (ArrayList<WeightedRandomFishable>) fishField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error("TST_AquaticZoneFakeRecipeGenerateFail_1", e);
                return new ArrayList<>();
            }
        }
        return cachedFishList;
    }

    void loadAquaticZoneFakeRecipes() {
        for (ItemStack aStack : WatersOutputs) {
            if (aStack == null) aStack = GTCMItemList.TestItem0.get(1);
            addFakeRecipe(GTCMItemList.TestItem0.get(1), aStack, WaterStack);
        }
    }

    void loadAquaticZoneTrueRecipes(){
        // generate recipe chance
        int TotalSize=0;
        for (ItemStack aStack:WatersOutputs)
            TotalSize += aStack.stackSize;
        int BasicSize = 10000/TotalSize;
        int[] Chances = new int[WatersOutputs.size()+1];
        int count=0;
        for (ItemStack aStack:WatersOutputs){
            //OutputChance.put(aStack,BasicSize*aStack.stackSize);
            Chances[0]=BasicSize*aStack.stackSize;
            count++;
        }
        WatersOutputs.add(GTCMItemList.OffSpring.get(1));
        ItemStack[] TrueOutputs=WatersOutputs.toArray(new ItemStack[0]);
        //OutputChance.put(GTCMItemList.OffSpring.get(1),1);
        Chances[count]=1;

        AquaticZoneSimulatorTrueRecipe=new GT_Recipe(
            new ItemStack[]{GTCMItemList.TestItem0.get(1)},
            TrueOutputs,
            null,
            Chances,
            new FluidStack[]{WaterStack},
            null,
            20,
            0,
            0
            );
    }
    void addFakeRecipe(ItemStack inputStacks, ItemStack outputStacks, FluidStack inputFluid) {
        GT_Values.RA.stdBuilder()
            .itemInputs(inputStacks)
            .itemOutputs(outputStacks)
            .fluidInputs(inputFluid)
            .noOptimize()
            .fake()
            .duration(20)
            .eut(0)
            .addTo(GTCMRecipe.AquaticZoneSimulatorFakeRecipes);
    }
}
