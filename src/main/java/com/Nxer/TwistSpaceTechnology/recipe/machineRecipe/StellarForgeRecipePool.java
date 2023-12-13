package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.google.common.collect.Sets;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.fluidStackEqualFuzzy;
import static com.Nxer.TwistSpaceTechnology.util.Utils.itemStackArrayEqualFuzzy;
import static com.Nxer.TwistSpaceTechnology.util.Utils.metaItemEqual;

public class StellarForgeRecipePool implements IRecipePool {
    public static final boolean OutputMoltenFluidInsteadIngotInStellarForgeRecipe = true;
    public static final HashSet<String> IngotHotOreDictNames = new HashSet<>();
    public static final HashSet<String> IngotOreDictNames = new HashSet<>();
    public static final HashSet<TST_ItemID> IngotHots = new HashSet<>();
    public static final HashSet<TST_ItemID> Ingots = new HashSet<>();
    public static final HashMap<TST_ItemID, ItemStack> IngotHotToIngot = new HashMap<>();

    public void initData(){
        //
        for (String name : OreDictionary.getOreNames()){
            if (name.startsWith("ingotHot")) {
                IngotHotOreDictNames.add(name);
                for (ItemStack items : OreDictionary.getOres(name)){
                    IngotHots.add(TST_ItemID.createNoNBT(items));
                }
            } else if (name.startsWith("ingot")){
                IngotOreDictNames.add(name);
                for (ItemStack items : OreDictionary.getOres(name)){
                    Ingots.add(TST_ItemID.createNoNBT(items));
                }
            }

        }

        // iterate Vacuum Freezer recipes
        for (GT_Recipe recipeFreezer : GT_Recipe.GT_Recipe_Map.sVacuumRecipes.mRecipeList) {
            if (recipeFreezer.mInputs == null || recipeFreezer.mInputs.length < 1 || recipeFreezer.mOutputs == null || recipeFreezer.mOutputs.length < 1) continue;
            ItemStack input = recipeFreezer.mInputs[0].copy();
            // check input is ingotHot
//            if (!IngotHots.contains(copyAmount(1, input))) continue;
            ItemStack outputIngot = recipeFreezer.mOutputs[0].copy();

            // add ingotHot - ingot to map
            IngotHotToIngot.put(TST_ItemID.createNoNBT(input), outputIngot);

        }


    }
    public void testPrepareEBFRecipes(){
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList){
            for (ItemStack itemStack : recipe.mInputs) {
                if (TST_ItemID.createNoNBT(itemStack).equalItemStack(Materials.Osmium.getDust(1))) {
                    TwistSpaceTechnology.LOG.info("catch Osmium EBF recipe");
                }
            }

            var ra = GT_Values.RA.stdBuilder();
            if (recipe.mInputs != null && recipe.mInputs.length > 0){
                ra.itemInputs(recipe.mInputs);
            } else {
                ra.noItemInputs();
            }
            if (recipe.mOutputs != null && recipe.mOutputs.length > 0){
                ra.itemOutputs(recipe.mOutputs);
            } else {
                ra.noItemOutputs();
            }
            if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0){
                ra.fluidInputs(recipe.mFluidInputs);
            } else {
                ra.noFluidInputs();
            }
            if (recipe.mFluidOutputs != null && recipe.mFluidOutputs.length > 0){
                ra.fluidOutputs(recipe.mFluidOutputs);
            } else {
                ra.noFluidOutputs();
            }
            ra.eut(recipe.mEUt).duration(recipe.mDuration).addTo(GTCMRecipe.instance.StellarForgeRecipes);
        }
    }
    public void prepareEBFRecipes(){
        Set<Fluid> protectionGas = Sets.newHashSet(
            Materials.Hydrogen.mGas,
            Materials.Oxygen.mGas,
            Materials.Nitrogen.mGas,
            WerkstoffLoader.Xenon.getFluidOrGas(1).getFluid(),
            WerkstoffLoader.Oganesson.getFluidOrGas(1).getFluid(),
            WerkstoffLoader.Krypton.getFluidOrGas(1).getFluid(),
            WerkstoffLoader.Neon.getFluidOrGas(1).getFluid(),
            Materials.Radon.mGas,
            Materials.Argon.mGas,
            Materials.Helium.mGas
        );

        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList){
            Set<ItemStack> inputItems = new HashSet<>();
            Set<FluidStack> inputFluids = new HashSet<>();
            Set<ItemStack> outputItems = new HashSet<>();
            Set<FluidStack> outputFluids = new HashSet<>();

            // process Item input
            byte integrateNum = 0;
            for (ItemStack inputs : recipe.mInputs) {

                if (metaItemEqual(inputs, GT_Utility.getIntegratedCircuit(1))) {
                    integrateNum = 1;
                    continue;
                }
                if (metaItemEqual(inputs, GT_Utility.getIntegratedCircuit(11))) {
                    integrateNum = 11;
                    continue;
                }

                inputItems.add(inputs.copy());

            }

            // process Item output
            for (ItemStack outputs : recipe.mOutputs) {
                TST_ItemID outputItemID = TST_ItemID.createNoNBT(outputs);
                if (IngotHots.contains(outputItemID)) {
                    // if this output item is Hot Ingot
                    ItemStack normalIngot = IngotHotToIngot.get(outputItemID);

                    if (OutputMoltenFluidInsteadIngotInStellarForgeRecipe){
                        FluidStack fluidStack = getMoltenFluids(normalIngot, outputs.stackSize);
                        if (fluidStack != null){
                            outputFluids.add(fluidStack);
                        }
                    } else {
                        ItemStack out = normalIngot.copy();
                        out.stackSize = outputs.stackSize;
                        outputItems.add(out);
                    }

                } else if (Ingots.contains(outputItemID)) {
                    // if this output item is normal Ingot
                    if (OutputMoltenFluidInsteadIngotInStellarForgeRecipe){
                        FluidStack fluidStack = getMoltenFluids(copyAmount(1,outputs), outputs.stackSize);
                        if (fluidStack != null){
                            outputFluids.add(fluidStack);
                        }
                    } else {
                        outputItems.add(outputs.copy());
                    }

                } else {
                    // if this output item is not Ingot
                    outputItems.add(outputs.copy());
                }
            }

            // process Fluid input
            for (FluidStack fluids : recipe.mFluidInputs){
                if (!protectionGas.contains(fluids.getFluid())){
                    inputFluids.add(fluids.copy());
                }
            }

            // process Fluid output
            for (FluidStack fluids : recipe.mFluidOutputs){
                outputFluids.add(fluids.copy());
            }

            ItemStack[] inputItemsArray = inputItems.toArray(new ItemStack[0]);
            FluidStack[] outputFluidsArray = outputFluids.toArray(new FluidStack[0]);
            boolean canAddNewRecipe = true;

            int duration = Math.max(1, recipe.mDuration / 3);
            if (integrateNum != 0 ){
                for (GT_Recipe recipeCheck : GTCMRecipe.instance.StellarForgeRecipes.mRecipeList) {
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
                    Math.max(1,recipe.mDuration/3)
                );
            }

        }
    }

    public void addToRecipes(ItemStack[] inputItems, FluidStack[] inputFluids, ItemStack[] outputItems, FluidStack[] outputFluids,
        int eut, int duration){
        GT_RecipeBuilder ra = GT_Values.RA.stdBuilder();

        if (inputItems != null && inputItems.length > 0) {
            ra.itemInputs(inputItems);
        } else {
            ra.noItemInputs();
        }

        if (inputFluids != null && inputFluids.length > 0){
            ra.fluidInputs(inputFluids);
        } else {
            ra.noFluidInputs();
        }

        if (outputItems != null && outputItems.length > 0){
            ra.itemOutputs(outputItems);
        } else {
            ra.noItemOutputs();
        }

        if (outputFluids != null && outputFluids.length > 0){
            ra.fluidOutputs(outputFluids);
        } else {
            ra.noFluidOutputs();
        }

        ra.eut(eut).duration(duration).addTo(GTCMRecipe.instance.StellarForgeRecipes);
    }

    public FluidStack getMoltenFluids(ItemStack ingot, int ingotAmount){
        FluidStack out = null;
        for (GT_Recipe recipeMolten : GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.mRecipeList) {
            if (metaItemEqual(ingot, recipeMolten.mInputs[0])){
                if (recipeMolten.mFluidOutputs[0] != null) {
                    out = recipeMolten.mFluidOutputs[0].copy();
                    out.amount = 144 * ingotAmount;
                }
                break;
            }
        }
        return out;
    }

    @Override
    public void loadRecipes() {
        initData();
        prepareEBFRecipes();
    }
}
