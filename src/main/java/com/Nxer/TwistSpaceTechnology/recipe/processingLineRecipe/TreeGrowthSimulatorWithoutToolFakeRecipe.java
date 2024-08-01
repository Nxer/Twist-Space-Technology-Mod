package com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe;

import static cofh.api.transport.RegistryEnderAttuned.inputFluid;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.getModeMultiplier;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.queryTreeProduct;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.treeProductsMap;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import galaxyspace.BarnardsSystem.BRFluids;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm.Mode;

public class TreeGrowthSimulatorWithoutToolFakeRecipe implements IRecipePool {

    static FluidStack WaterStack = Materials.Water.getFluid(1000);
    static FluidStack UnknowWaterStack = new FluidStack(BRFluids.UnknowWater, 1000);
    static FluidStack TemporalLiquidStack = new FluidStack(FluidRegistry.getFluid("temporalfluid"), 144);
    static FluidStack DeathWaterStack = Materials.Water.getFluid(1000);;
    static FluidStack UUMatterStack = Materials.UUMatter.getFluid(1000);

    static ItemStack[] IntegratedCircuitStack = { GT_Utility.getIntegratedCircuit(1),
        GT_Utility.getIntegratedCircuit(2), GT_Utility.getIntegratedCircuit(3), GT_Utility.getIntegratedCircuit(4), };
    static ItemStack[] allSaplingsIn;
    static ItemStack[] allLogs;
    static ItemStack[] allSaplings;
    static ItemStack[] allLeaves;
    static ItemStack[] allFruits;
    static ItemStack[] allProducts;

    @Override
    public void loadRecipes() {
        initStatic();
        loadTreeFarmWithoutToolRecipe();
        loadManualRecipes();
    }

    void initStatic() {
        ArrayList<ItemStack> allSaplingsInCopy = new ArrayList<>();
        EnumMap<Mode, ArrayList<ItemStack>> allProductsMap = new EnumMap<>(Mode.class);
        // init allSaplingsIn
        ItemStack saplingIn;
        for (Map.Entry<String, EnumMap<Mode, ItemStack>> entry : treeProductsMap.entrySet()) {
            String key = entry.getKey();
            String[] keyPart;
            keyPart = key.split(":");
            saplingIn = entry.getValue()
                .get(Mode.SAPLING);
            if (!key.contains("Forestry:")) saplingIn = GT_ModHandler
                .getModItem(keyPart[0], keyPart[1], 0, keyPart[2] == null ? 0 : Integer.parseInt(keyPart[2]));
            saplingIn.stackSize = 0;
            allSaplingsInCopy.add(saplingIn);
        }
        allSaplingsIn = allSaplingsInCopy.toArray(new ItemStack[0]);

        // // init allOuts
        // ItemStack aStack;
        // for (ItemStack aSapling : allSaplingsIn) {
        // EnumMap<Mode, ItemStack> productMap = queryTreeProduct(aSapling);
        // for (Mode mode : Mode.values()) {
        // aStack = productMap.get(mode);
        // if (aStack == null) continue;
        // aStack.stackSize *= getModeMultiplier(mode);
        // ArrayList<ItemStack> productList = allProductsMap.computeIfAbsent(mode, k -> new ArrayList<>());
        // productList.add(aStack);
        // }
        // }
        //
        // allLogs = allProductsMap.get(Mode.LOG)
        // .toArray(new ItemStack[0]);
        // allSaplings = allProductsMap.get(Mode.SAPLING)
        // .toArray(new ItemStack[0]);
        // allLeaves = allProductsMap.get(Mode.LEAVES)
        // .toArray(new ItemStack[0]);
        // allFruits = allProductsMap.get(Mode.FRUIT)
        // .toArray(new ItemStack[0]);
        // allProducts = allProductsMap.values()
        // .stream()
        // .flatMap(ArrayList::stream)
        // .toArray(ItemStack[]::new);
    }

    void loadTreeFarmWithoutToolRecipe() {
        for (ItemStack Sapling : allSaplingsIn) {
            addFakeRecipe(Sapling, new ItemStack[] { Sapling }, WaterStack);
        }
    }

    void loadManualRecipes() {
        // Barnarda C
        if (Mods.GalaxySpace.isModLoaded()) addSpecialFakeRecipe(
            GT_ModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 0, 1),
            UnknowWaterStack);
        // Time
        if (Mods.TwilightForest.isModLoaded()) addSpecialFakeRecipe(
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 0, 5),
            TemporalLiquidStack);
        // Death Water
        // Thaumic Tentacle

        // UU Matter
        // addFakeRecipe(IntegratedCircuitStack, new ItemStack[]{
        // new ItemStack()
        // }, allSaplingsIn, UUMatterStack);
    }

    void addSpecialFakeRecipe(ItemStack SpecialSapling, FluidStack SpecialFluid) {
        addFakeRecipe(SpecialSapling, allSaplingsIn, SpecialFluid);
    }

    void addFakeRecipe(ItemStack Sapling, ItemStack[] specialStacks, FluidStack inputFluid) {
        EnumMap<Mode, ItemStack> ProductMap = queryTreeProduct(Sapling);
        ItemStack[] inputStacks = new ItemStack[Mode.values().length];
        ItemStack[] outputStacks = new ItemStack[Mode.values().length];
        int count = 0;
        for (Mode mode : Mode.values()) {
            if (ProductMap != null && ProductMap.get(mode) != null) {
                inputStacks[count] = IntegratedCircuitStack[count];
                outputStacks[count] = ProductMap.get(mode)
                    .copy();
                outputStacks[count].stackSize *= getModeMultiplier(mode);
            }
            count++;
        }
        addFakeRecipe(inputStacks, outputStacks, specialStacks, inputFluid);
    }

    void addFakeRecipe(ItemStack[] inputStacks, ItemStack[] outputStacks, ItemStack[] specialStacks,
        FluidStack inputFluid) {
        GT_Values.RA.stdBuilder()
            .itemInputs(inputStacks)
            .itemOutputs(outputStacks)
            .fluidInputs(inputFluid)
            .special(specialStacks)
            .noOptimize()
            .fake()
            .duration(20)
            .eut(0)
            .addTo(GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes);

        // GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes.addFakeRecipe(
        // false,
        // new GT_Recipe(
        // inputStacks,
        // outputStacks,
        // specialStacks,
        // null,
        // new FluidStack[] { inputFluid },
        // null,
        // 20,
        // 0,
        // GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes.getAllRecipes()
        // .size() + 1));
    }
}
