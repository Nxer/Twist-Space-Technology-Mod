package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.getModeMultiplier;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.queryTreeProduct;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.treeProductsMap;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import galaxyspace.BarnardsSystem.BRFluids;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.Mode;

public class TreeGrowthSimulatorWithoutToolFakeRecipe implements IRecipePool {

    static FluidStack WaterStack = Materials.Water.getFluid(1000);
    static FluidStack UnknowWaterStack = new FluidStack(BRFluids.UnknowWater, 1000);
    static FluidStack TemporalLiquidStack = new FluidStack(FluidRegistry.getFluid("temporalfluid"), 144);
    static final ItemStack[] allSaplings = new ItemStack[treeProductsMap.size()];
    static {
        ItemStack sapling;
        int count = 0;
        for (Map.Entry<String, EnumMap<Mode, ItemStack>> entry : treeProductsMap.entrySet()) {
            String key = entry.getKey();
            String[] keyPart;
            keyPart = key.split(":");
            sapling = entry.getValue()
                .get(Mode.SAPLING);
            if (!key.contains("Forestry:")) sapling = GT_ModHandler
                .getModItem(keyPart[0], keyPart[1], 0, keyPart[2] == null ? 0 : Integer.parseInt(keyPart[2]));
            sapling.stackSize = 0;
            allSaplings[count] = sapling;
            count++;
        }
    }

    @Override
    public void loadRecipes() {
        loadTreeFarmWithoutToolRecipe();
        loadManualRecipes();
    }

    void loadTreeFarmWithoutToolRecipe() {
        for (ItemStack Sapling : allSaplings) {
            addFakeRecipe(Sapling, new ItemStack[] { Sapling }, WaterStack);
        }
    }

    void loadManualRecipes() {
        if (Mods.GalaxySpace.isModLoaded()) addSpecialFakeRecipe(
            GT_ModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 0, 1),
            UnknowWaterStack);
        if (Mods.TwilightForest.isModLoaded()) addSpecialFakeRecipe(
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 0, 5),
            TemporalLiquidStack);

    }

    void addSpecialFakeRecipe(ItemStack SpecialSapling, FluidStack SpecialFluid) {
        addFakeRecipe(SpecialSapling, allSaplings, SpecialFluid);
    }

    void addFakeRecipe(ItemStack Sapling, ItemStack[] specialStacks, FluidStack inputFluid) {
        EnumMap<Mode, ItemStack> ProductMap = queryTreeProduct(Sapling);
        ItemStack[] inputStacks = new ItemStack[Mode.values().length];
        ItemStack[] outputStacks = new ItemStack[Mode.values().length];
        int count = 0;
        for (Mode mode : Mode.values()) {
            if (ProductMap != null && ProductMap.get(mode) != null) {
                inputStacks[count] = GT_Utility.getIntegratedCircuit(count + 1)
                    .copy();
                outputStacks[count] = ProductMap.get(mode)
                    .copy();
                outputStacks[count].stackSize *= getModeMultiplier(mode);
            }
            count++;
        }
        GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes.addFakeRecipe(
            false,
            new GT_Recipe(
                inputStacks,
                outputStacks,
                specialStacks,
                null,
                new FluidStack[] { inputFluid },
                null,
                20,
                0,
                GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes.getAllRecipes()
                    .size() + 1));
    }
}
