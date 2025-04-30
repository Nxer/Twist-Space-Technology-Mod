package com.Nxer.TwistSpaceTechnology.system.ExtremeCrafting;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.OreDictItem;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_GeneralFrontend;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.dreammaster.gthandler.CustomItemList;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public class ExtremeCraftRecipeHandler {

    public static final RecipeMap<RecipeMapBackend> visualExtremeCraftRecipes = RecipeMapBuilder
        .of("gtcm.recipe.visualExtremeCraftRecipes")
        .maxIO(16, 1, 0, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.ExtremeCraftCenter.get(1))
                .setMaxRecipesPerPage(1))
        .build();

    public static final Collection<ExtremeCraftRecipe> extremeCraftRecipes = new ArrayList<>();

    /**
     * Output item to recipe.
     */
    public static final Map<TST_ItemID, Collection<ExtremeCraftRecipe>> extremeCraftRecipesMap = new HashMap<>();

    /**
     * temp method
     */
    public static ItemStack mergeStackSize(ItemStack a, ItemStack b) {
        if (a == null) return b;
        if (b == null) return a;
        a.stackSize += b.stackSize;
        return a;
    }

    protected ItemStack[] sortOutInputs(ItemStack... in) {
        Map<TST_ItemID, ItemStack> inputsMap = new LinkedHashMap<>();
        for (ItemStack is : in) {
            if (is == null || is.stackSize < 1) continue;
            TST_ItemID itemID = TST_ItemID.create(is);
            inputsMap.merge(itemID, is.copy(), ExtremeCraftRecipeHandler::mergeStackSize);
        }
        return inputsMap.values()
            .toArray(new ItemStack[0]);
    }

    protected Object[] sortOutInputs(Object... in) {
        Map<Object, Integer> inputsMap = new LinkedHashMap<>();
        for (Object o : in) {
            if (o == null) continue;
            inputsMap.merge(o, 1, Integer::sum);
        }

        List<Object> sorted = new ArrayList<>();
        for (Map.Entry<Object, Integer> pair : inputsMap.entrySet()) {
            Object ingredient = pair.getKey();
            if (ingredient instanceof ItemStack i) {
                sorted.add(GTUtility.copyAmountUnsafe(pair.getValue(), i));
            } else if (ingredient instanceof ArrayList<?>list) {
                @SuppressWarnings("unchecked")
                ArrayList<ItemStack> itemList = (ArrayList<ItemStack>) list;

                String oreName = TstUtils.getOreNameByOreList(itemList);
                if (oreName != null) {
                    // if we managed to get the ore name of the list, we use the ore name.
                    sorted.add(new Object[] { oreName, pair.getValue() });
                } else {
                    // otherwise, just fallback to the first valid item in the list.
                    ItemStack firstValidItem = itemList.stream()
                        .filter(GTUtility::isStackValid)
                        .findFirst()
                        .orElse(null);
                    if (firstValidItem == null) throw new IllegalArgumentException();
                    ItemStack unifiedItem = GTOreDictUnificator.get(false, firstValidItem, true);
                    sorted.add(GTUtility.copyAmountUnsafe(pair.getValue(), unifiedItem));
                }
            } else {
                LOG.info("ERROR ExtremeCraftRecipeHandler.sortOutInputs catch unknown type");
            }
        }

        return sorted.toArray(new Object[0]);

    }

    public void initECRecipe() {

        ExtremeCraftingManager.getInstance()
            .addExtremeShapedOreRecipe(
                ItemList.Cover_SolarPanel_LV.get(1L),
                "---------",
                "---------",
                "---aba---",
                "---cdc---",
                "---efe---",
                "---cdc---",
                "---aba---",
                "---------",
                "---------",
                'a',
                "wireGt01SuperconductorMV",
                'b',
                CustomItemList.IrradiantReinforcedAluminiumPlate.get(1L),
                'c',
                ItemList.Circuit_Silicon_Wafer2.get(1L),
                'd',
                "platePolytetrafluoroethylene",
                'e',
                "circuitAdvanced",
                'f',
                ItemList.Cover_SolarPanel_8V.get(1L));

        List<IRecipe> originRecipes = ExtremeCraftingManager.getInstance()
            .getRecipeList();

        LOG.info("start init extreme craft table recipe :" + originRecipes.size());

        // pre init
        {
            // Ore Dict "cropSpace" and "cropTcetiESeaweed" are same in list content
            // so we need to pre-process these two Ore Dicts
            OreDictItem odi = new OreDictItem("cropSpace");
            OreDictItem.UsedOreDictItems.put("cropSpace", odi);
            OreDictItem.UsedOreDictItems.put("cropTcetiESeaweed", odi);
        }

        for (var Recipe : originRecipes) {
            Object[] inputs = null;
            ItemStack output = null;
            if (Recipe instanceof ExtremeShapedRecipe recipe) {
                inputs = sortOutInputs(recipe.recipeItems);
                output = recipe.getRecipeOutput();
            } else if (Recipe instanceof ExtremeShapedOreRecipe recipe) {
                inputs = sortOutInputs(recipe.getInput());
                output = recipe.getRecipeOutput();
            } else if (Recipe instanceof ShapelessOreRecipe recipe) {
                inputs = sortOutInputs(
                    recipe.getInput()
                        .toArray());
                output = recipe.getRecipeOutput();
            }

            if (inputs != null && output != null) {
                GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                    .ignoreCollision()
                    .itemInputs(inputs)
                    .itemOutputs(output)
                    .eut(0)
                    .duration(Config.TickEveryProcess_MegaCraftingCenter);

                Optional<GTRecipe.GTRecipe_WithAlt> oRecipe = builder.buildWithAlt();
                if (oRecipe.isPresent()) {
                    visualExtremeCraftRecipes.add(oRecipe.get());
                } else {
                    builder.addTo(visualExtremeCraftRecipes);
                }

                ExtremeCraftRecipe ecr = new ExtremeCraftRecipe().itemInputs(inputs)
                    .itemOutputs(output);
                extremeCraftRecipes.add(ecr);

                TST_ItemID oi = TST_ItemID.create(output);
                if (extremeCraftRecipesMap.containsKey(oi)) {
                    extremeCraftRecipesMap.get(oi)
                        .add(ecr);
                } else {
                    Set<ExtremeCraftRecipe> s = new HashSet<>();
                    s.add(ecr);
                    extremeCraftRecipesMap.put(oi, s);
                }

            } else {
                LOG.info("ExtremeCraftRecipeHandler get a null recipe.");
            }

        }

        LOG.info("complete init extreme craft table recipe :" + extremeCraftRecipes.size());

    }

}
