package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static cofh.lib.util.helpers.FluidHelper.isFluidEqual;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.fluidEqual;
import static com.Nxer.TwistSpaceTechnology.util.Utils.fluidStackEqualFuzzy;
import static com.Nxer.TwistSpaceTechnology.util.Utils.itemStackArrayEqualFuzzy;
import static com.Nxer.TwistSpaceTechnology.util.Utils.metaItemEqual;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.google.common.collect.Sets;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class StellarForgeRecipePool implements IRecipePool {

    public static final HashSet<String> IngotHotOreDictNames = new HashSet<>();
    public static final HashSet<String> IngotOreDictNames = new HashSet<>();
    public static final HashSet<TST_ItemID> IngotHots = new HashSet<>();
    public static final HashSet<TST_ItemID> Ingots = new HashSet<>();
    public static final HashMap<TST_ItemID, ItemStack> IngotHotToIngot = new HashMap<>();
    public static final HashSet<TST_ItemID> SpecialRecipeOutputs = new HashSet<>();
    public static final HashMap<Fluid, ItemStack> MoltenToIngot = new HashMap<>();

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

        // generate molten fluid to ingot map
        for (GTRecipe recipeSolidifier : fluidSolidifierRecipes.getAllRecipes()) {
            if (recipeSolidifier.mInputs == null || recipeSolidifier.mInputs.length < 1
                || recipeSolidifier.mOutputs == null
                || recipeSolidifier.mOutputs.length < 1) continue;
            if (metaItemEqual(
                GTModHandler.getModItem(Mods.GregTech.ID, "gt.metaitem.01", 1, 32306),
                recipeSolidifier.mInputs[0])) {
                if (!isFluidEqual(recipeSolidifier.mFluidInputs[0], Materials.AnnealedCopper.getMolten(1)))
                    MoltenToIngot.put(recipeSolidifier.mFluidInputs[0].getFluid(), recipeSolidifier.mOutputs[0]);
            }
            MoltenToIngot.put(
                Materials.AnnealedCopper.getMolten(1)
                    .getFluid(),
                Materials.AnnealedCopper.getIngots(1));
        }
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

            ArrayList<ItemStack> inputItems = new ArrayList<>();
            ArrayList<FluidStack> inputFluids = new ArrayList<>();
            ArrayList<ItemStack> outputItems = new ArrayList<>();
            ArrayList<FluidStack> outputFluids = new ArrayList<>();

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

            // New Alloy Recipe in Blast Furnace conflicts with some single item recipes
            if (integrateNum != 0 || inputItems.size() < 2) {
                inputItems.add(GTUtility.getIntegratedCircuit(1));
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

                // Move the Integrated Circuit to the end of the recipe
                int InputItemLength = inputItemsArray.length;
                for (int i = 0; i < InputItemLength; i++) {
                    if (inputItemsArray[i].getItem() == ItemList.Circuit_Integrated.getItem()
                        && i != InputItemLength - 1) {
                        ItemStack IntegratedCircuit = inputItemsArray[i];
                        for (int j = i; j < InputItemLength - 1; j++) {
                            inputItemsArray[j] = inputItemsArray[j + 1];
                        }
                        inputItemsArray[InputItemLength - 1] = IntegratedCircuit;
                        break;
                    }
                }

                addToMiracleDoorRecipes(
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

    public void prepareABSRecipes() {

        for (GTRecipe recipe : GTPPRecipeMaps.alloyBlastSmelterRecipes.getAllRecipes()) {
            int minOutputFluidAmount = 144;
            // if there is more than one output fluid, find the fewest
            for (FluidStack aOutputFluid : recipe.mFluidOutputs) {
                // if (aOutputFluid.amount % 144 == 0) continue;
                minOutputFluidAmount = Math.min(minOutputFluidAmount, aOutputFluid.amount);
            }
            ArrayList<ItemStack> inputItemList = new ArrayList<>();
            ArrayList<ItemStack> outputItemList = new ArrayList<>();
            ArrayList<FluidStack> inputFluidList = new ArrayList<>();
            ArrayList<FluidStack> outputFluidList = new ArrayList<>();
            ArrayList<FluidStack> outputFluidListTemp = new ArrayList<>();

            int RecipeMultiplier = 1;
            if (minOutputFluidAmount < 144) {
                int CorrectFluidAmount = minOutputFluidAmount;
                while (CorrectFluidAmount % 144 != 0) {
                    CorrectFluidAmount += minOutputFluidAmount;
                }
                RecipeMultiplier = CorrectFluidAmount / minOutputFluidAmount;
            }
            // Multiply the recipe

            if (recipe.mInputs != null) for (ItemStack aItemStack : recipe.mInputs) {
                ItemStack aStackCopy = aItemStack.copy();
                aStackCopy.stackSize *= RecipeMultiplier;
                inputItemList.add(aStackCopy);
            }

            if (recipe.mFluidInputs != null) for (FluidStack aFluidStack : recipe.mFluidInputs) {
                FluidStack aFluidCopy = aFluidStack.copy();
                aFluidCopy.amount *= RecipeMultiplier;
                inputFluidList.add(aFluidCopy);
            }

            if (recipe.mOutputs != null) for (ItemStack aItemStack : recipe.mOutputs) {
                if (getMoltenFluids(aItemStack, 1) != null) {
                    outputFluidListTemp.add(getMoltenFluids(aItemStack, aItemStack.stackSize));
                } else {
                    ItemStack aStackCopy = aItemStack.copy();
                    aStackCopy.stackSize *= RecipeMultiplier;
                    outputItemList.add(aStackCopy);
                }

            }

            if (recipe.mOutputs != null) for (FluidStack aFluidStack : recipe.mFluidOutputs) {
                FluidStack aFluidCopy = aFluidStack.copy();
                aFluidCopy.amount *= RecipeMultiplier;
                outputFluidListTemp.add(aFluidCopy);
            }

            for (FluidStack aFluidTemp : outputFluidListTemp) {
                boolean isFluidRepetitious = false;
                for (FluidStack aFluidOut : outputFluidList) {
                    if (fluidEqual(aFluidTemp, aFluidOut)) {
                        aFluidOut.amount += aFluidTemp.amount;
                        isFluidRepetitious = true;
                    }
                }

                if (!isFluidRepetitious) {
                    outputFluidList.add(aFluidTemp);
                }
            }

            // Move the Integrated Circuit to the start of the recipe
            for (int i = 0; i < inputItemList.size(); i++) {
                if (inputItemList.get(i)
                    .getItem() == ItemList.Circuit_Integrated.getItem() && i != 0) {
                    ItemStack integratedCircuit = inputItemList.remove(i);
                    inputItemList.add(0, integratedCircuit);
                    break;
                }
            }

            addToMiracleDoorRecipes(
                inputItemList.toArray(new ItemStack[0]),
                inputFluidList.toArray(new FluidStack[0]),
                outputItemList.toArray(new ItemStack[0]),
                outputFluidList.toArray(new FluidStack[0]),
                recipe.mEUt,
                recipe.mDuration,
                GTCMRecipe.StellarForgeAlloySmelterRecipes);
        }

    }

    public static void addToMiracleDoorRecipes(ItemStack[] inputItems, FluidStack[] inputFluids,
        ItemStack[] outputItems, FluidStack[] outputFluids, int eut, int duration, IRecipeMap aRecipeMap) {
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

        ra.special(GTCMItemList.WhiteDwarfMold_Ingot.get(0))
            .eut(eut)
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
        return out;
    }

    public void loadManualRecipes() {

        // Meteoric Iron
        addToMiracleDoorRecipes(
            new ItemStack[] { GTUtility.getIntegratedCircuit(1), Materials.MeteoricIron.getDust(1) },
            null,
            null,
            new FluidStack[] { Materials.MeteoricIron.getMolten(144) },
            (int) RECIPE_MV,
            20 * 25,
            GTCMRecipe.StellarForgeRecipes);

        // Meteoric Steel
        addToMiracleDoorRecipes(
            new ItemStack[] { GTUtility.getIntegratedCircuit(2), Materials.MeteoricIron.getDust(1) },
            null,
            null,
            new FluidStack[] { Materials.MeteoricSteel.getMolten(144) },
            (int) RECIPE_MV,
            20 * 10,
            GTCMRecipe.StellarForgeRecipes);

        // Neutronium
        addToMiracleDoorRecipes(
            new ItemStack[] { Materials.Neutronium.getDust(1) },
            null,
            null,
            new FluidStack[] { Materials.Neutronium.getMolten(144) },
            (int) RECIPE_UV,
            20 * 112,
            GTCMRecipe.StellarForgeRecipes);

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
        prepareABSRecipes();
        loadManualRecipes();
        // cacheRecipeList();
        // New version no longer requires this
    }

    public void loadOnServerStarted() {
        // prepareEBFRecipes();
        // prepareABSRecipes();
        // loadRecipeListCache();
    }
}
