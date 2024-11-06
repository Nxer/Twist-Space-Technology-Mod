package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.fluidStackEqualFuzzy;
import static com.Nxer.TwistSpaceTechnology.util.Utils.itemStackArrayEqualFuzzy;
import static com.Nxer.TwistSpaceTechnology.util.Utils.metaItemEqual;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.google.common.collect.Sets;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public class StellarForgeRecipePool implements IRecipePool {

    public static final HashSet<String> IngotHotOreDictNames = new HashSet<>();
    public static final HashSet<String> IngotOreDictNames = new HashSet<>();
    public static final HashSet<TST_ItemID> IngotHots = new HashSet<>();
    public static final HashSet<TST_ItemID> Ingots = new HashSet<>();
    public static final HashMap<TST_ItemID, ItemStack> IngotHotToIngot = new HashMap<>();
    public static final HashSet<TST_ItemID> SpecialRecipeOutputs = new HashSet<>();

    public void initData() {

        for (String name : OreDictionary.getOreNames()) {
            if (name.startsWith("ingotHot")) {
                IngotHotOreDictNames.add(name);
                for (ItemStack items : OreDictionary.getOres(name)) {
                    IngotHots.add(TST_ItemID.createNoNBT(items));
                }
            } else if (name.startsWith("ingot")) {
                IngotOreDictNames.add(name);
                for (ItemStack items : OreDictionary.getOres(name)) {
                    Ingots.add(TST_ItemID.createNoNBT(items));
                }
            }
        }

        // iterate Vacuum Freezer recipes
        for (GTRecipe recipeFreezer : RecipeMaps.vacuumFreezerRecipes.getAllRecipes()) {
            if (recipeFreezer.mInputs == null || recipeFreezer.mInputs.length < 1
                || recipeFreezer.mOutputs == null
                || recipeFreezer.mOutputs.length < 1) continue;
            ItemStack input = recipeFreezer.mInputs[0].copy();
            // check input is ingotHot
            // if (!IngotHots.contains(copyAmount(1, input))) continue;
            ItemStack outputIngot = recipeFreezer.mOutputs[0].copy();

            // add ingotHot - ingot to map
            IngotHotToIngot.put(TST_ItemID.createNoNBT(input), outputIngot);

        }

        // add SpecialRecipeOutputs
        SpecialRecipeOutputs.add(TST_ItemID.create(WerkstoffLoader.CubicZirconia.get(OrePrefixes.gemFlawed, 1)));
        SpecialRecipeOutputs.add(TST_ItemID.create(Materials.MeteoricIron.getIngots(1)));
        SpecialRecipeOutputs.add(TST_ItemID.create(Materials.MeteoricSteel.getIngots(1)));
        SpecialRecipeOutputs.add(TST_ItemID.create(GTModHandler.getModItem("gregtech", "gt.metaitem.01", 1, 12129)));

    }

    public void prepareEBFRecipes() {
        Set<Fluid> protectionGas = Sets.newHashSet(
            Materials.Hydrogen.mGas,
            Materials.Oxygen.mGas,
            Materials.Nitrogen.mGas,
            WerkstoffLoader.Xenon.getFluidOrGas(1)
                .getFluid(),
            WerkstoffLoader.Oganesson.getFluidOrGas(1)
                .getFluid(),
            WerkstoffLoader.Krypton.getFluidOrGas(1)
                .getFluid(),
            WerkstoffLoader.Neon.getFluidOrGas(1)
                .getFluid(),
            Materials.Radon.mGas,
            Materials.Argon.mGas,
            Materials.Helium.mGas);

        for (GTRecipe recipe : RecipeMaps.blastFurnaceRecipes.getAllRecipes()) {
            if (recipe.mOutputs.length == 1 && SpecialRecipeOutputs.contains(TST_ItemID.create(recipe.mOutputs[0])))
                continue;

            Set<ItemStack> inputItems = new HashSet<>();
            Set<FluidStack> inputFluids = new HashSet<>();
            Set<ItemStack> outputItems = new HashSet<>();
            Set<FluidStack> outputFluids = new HashSet<>();

            // process Item input
            byte integrateNum = 0;
            for (ItemStack inputs : recipe.mInputs) {

                if (metaItemEqual(inputs, GTUtility.getIntegratedCircuit(1))) {
                    integrateNum = 1;
                    continue;
                }
                if (metaItemEqual(inputs, GTUtility.getIntegratedCircuit(11))) {
                    integrateNum = 11;
                    continue;
                }

                inputItems.add(inputs.copy());

            }

            // process Item output
            for (ItemStack outputs : recipe.mOutputs) {
                TST_ItemID outputItemID = TST_ItemID.createNoNBT(outputs);
                boolean isRecipeAdded = false;
                if (IngotHots.contains(outputItemID)) {
                    // if this output item is Hot Ingot
                    ItemStack normalIngot = IngotHotToIngot.get(outputItemID);

                    FluidStack fluidStack = getMoltenFluids(normalIngot, outputs.stackSize);
                    if (fluidStack != null) {
                        outputFluids.add(fluidStack);
                        isRecipeAdded = true;
                    }

                } else if (Ingots.contains(outputItemID)) {
                    // if this output item is normal Ingot
                    FluidStack fluidStack = getMoltenFluids(copyAmount(1, outputs), outputs.stackSize);
                    if (fluidStack != null) {
                        outputFluids.add(fluidStack);
                        isRecipeAdded = true;
                    }
                }
                if (!isRecipeAdded) {
                    // if this output item is not Ingot
                    outputItems.add(outputs.copy());
                }
            }

            // process Fluid input
            for (FluidStack fluids : recipe.mFluidInputs) {
                if (!protectionGas.contains(fluids.getFluid())) {
                    inputFluids.add(fluids.copy());
                }
            }

            // process Fluid output
            for (FluidStack fluids : recipe.mFluidOutputs) {
                outputFluids.add(fluids.copy());
            }

            ItemStack[] inputItemsArray = inputItems.toArray(new ItemStack[0]);
            FluidStack[] outputFluidsArray = outputFluids.toArray(new FluidStack[0]);
            boolean canAddNewRecipe = true;

            int duration = Math.max(1, recipe.mDuration / 3);
            if (integrateNum != 0) {
                for (GTRecipe recipeCheck : GTCMRecipe.StellarForgeRecipes.getAllRecipes()) {
                    if (!itemStackArrayEqualFuzzy(recipeCheck.mInputs, inputItemsArray)) continue;
                    if (!fluidStackEqualFuzzy(recipeCheck.mFluidOutputs, outputFluidsArray)) continue;
                    canAddNewRecipe = false;
                    recipeCheck.mDuration = Math.min(recipeCheck.mDuration, duration);
                    break;
                }
            }

            // add to recipe map
            if (canAddNewRecipe) {
                addToRecipes(
                    inputItemsArray,
                    inputFluids.toArray(new FluidStack[0]),
                    outputItems.toArray(new ItemStack[0]),
                    outputFluidsArray,
                    recipe.mEUt,
                    Math.max(1, recipe.mDuration / 3),
                    GTCMRecipe.StellarForgeRecipes);
            }

        }
    }

    public static void addToRecipes(ItemStack[] inputItems, FluidStack[] inputFluids, ItemStack[] outputItems,
        FluidStack[] outputFluids, int eut, int duration, IRecipeMap aRecipeMap) {
        GTRecipeBuilder ra = GTValues.RA.stdBuilder();

        if (inputItems != null && inputItems.length > 0) {
            ra.itemInputs(inputItems);
        }

        if (inputFluids != null && inputFluids.length > 0) {
            ra.fluidInputs(inputFluids);
        }

        if (outputItems != null && outputItems.length > 0) {
            ra.itemOutputs(outputItems);
        }

        if (outputFluids != null && outputFluids.length > 0) {
            ra.fluidOutputs(outputFluids);
        }

        ra.eut(eut)
            .duration(duration)
            .addTo(aRecipeMap);
    }

    public static FluidStack getMoltenFluids(ItemStack ingot, int ingotAmount) {
        FluidStack out = null;
        for (GTRecipe recipeMolten : fluidExtractionRecipes.getAllRecipes()) {
            if (metaItemEqual(ingot, recipeMolten.mInputs[0])) {
                if (recipeMolten.mFluidOutputs[0] != null) {
                    out = recipeMolten.mFluidOutputs[0].copy();
                    out.amount = 144 * ingotAmount;
                }
                break;
            }
        }
        // ItemData data = getAssociation(ingot);
        // Materials ingotMaterial = data != null ? data.mMaterial.mMaterial : null;
        // if (ingotMaterial != null) {
        // if (ingotMaterial.mStandardMoltenFluid != null) {
        // out = ingotMaterial.getMolten(INGOTS);
        // } else if (ingotMaterial.mFluid != null) {
        // out = ingotMaterial.getFluid(BUCKETS);
        // }
        // }
        return out;
    }

    public void loadManualRecipes() {

        // Meteoric Iron
        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(1), Materials.MeteoricIron.getDust(1))
            .fluidOutputs(Materials.MeteoricIron.getMolten(144))
            .eut(RECIPE_MV)
            .duration(20 * 25)
            .addTo(GTCMRecipe.StellarForgeRecipes);

        // Meteoric Steel
        TST_RecipeBuilder.builder()
            .itemInputs(GTUtility.getIntegratedCircuit(2), Materials.MeteoricIron.getDust(1))
            .fluidOutputs(Materials.MeteoricSteel.getMolten(144))
            .eut(RECIPE_MV)
            .duration(20 * 10)
            .addTo(GTCMRecipe.StellarForgeRecipes);

        // Neutronium
        TST_RecipeBuilder.builder()
            .itemInputs(Materials.Neutronium.getDust(1))
            .fluidOutputs(Materials.Neutronium.getMolten(144))
            .eut(RECIPE_UV)
            .duration(20 * 112)
            .addTo(GTCMRecipe.StellarForgeRecipes);

    }

    public static Collection<GTRecipe> stellarForgeRecipeListCache;

    private void cacheRecipeList() {
        stellarForgeRecipeListCache = new HashSet<>(GTCMRecipe.StellarForgeRecipes.getAllRecipes());
    }

    private void loadRecipeListCache() {
        for (GTRecipe recipe : stellarForgeRecipeListCache) {
            GTCMRecipe.StellarForgeRecipes.addRecipe(recipe);
        }
    }

    @Override
    public void loadRecipes() {
        initData();
        prepareEBFRecipes();
        // prepareABSRecipes();
        loadManualRecipes();
        cacheRecipeList();
    }

    public void loadOnServerStarted() {
        prepareEBFRecipes();
        loadRecipeListCache();
    }
}
