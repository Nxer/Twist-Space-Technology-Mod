package com.Nxer.TwistSpaceTechnology.system.ExtremeCrafting;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static fox.spiteful.avaritia.items.LudicrousItems.cosmic_meatballs;
import static fox.spiteful.avaritia.items.LudicrousItems.ultimate_stew;
import static gregtech.api.util.GTModHandler.getModItem;

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
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.OreDictItem;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_GeneralFrontend;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import cpw.mods.fml.common.Loader;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import gregtech.api.enums.GTValues;
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

    ItemStack createItemStack(String aModID, String aItem, long aAmount, int aMeta, String aNBTString) {
        ItemStack s = getModItem(aModID, aItem, aAmount, aMeta);
        try {
            s.stackTagCompound = (NBTTagCompound) JsonToNBT.func_150315_a(aNBTString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return s;
    }

    public void initECRecipe() {

        List<IRecipe> originRecipes = ExtremeCraftingManager.getInstance()
            .getRecipeList();

        Set<TST_ItemID> ignore = new HashSet<>();
        {
            ignore.add(TST_ItemID.create(new ItemStack(ultimate_stew)));
            ignore.add(TST_ItemID.create(new ItemStack(cosmic_meatballs)));
            if (Loader.isModLoaded("sciencenotleisure")) {
                ignore.add(TST_ItemID.create(getModItem("sciencenotleisure", "CompressedStargateTier7")));
            }
        }

        LOG.info("start init extreme craft table recipe :{}", originRecipes.size());

        // pre init
        {
            // Ore Dict "cropSpace" and "cropTcetiESeaweed" are same in list content
            // so we need to pre-process these two Ore Dicts
            OreDictItem odi = new OreDictItem("cropSpace");
            OreDictItem.UsedOreDictItems.put("cropSpace", odi);
            OreDictItem.UsedOreDictItems.put("cropTcetiESeaweed", odi);
            LOG.info(
                "UsedOreDictItems pre init finished. UsedOreDictItems.size = {}",
                OreDictItem.UsedOreDictItems.size());
        }

        for (int i = 0; i < originRecipes.size(); i++) {
            LOG.info(
                "index = {}, output = {}",
                i,
                originRecipes.get(i)
                    .getRecipeOutput()
                    .getDisplayName());
        }

        int checkIndex = 0;

        for (IRecipe Recipe : originRecipes) {
            checkIndex++;

            LOG.info("checkIndex = {}, IRecipe = {}", checkIndex, Recipe);
            Object[] inputs = null;
            ItemStack output = Recipe.getRecipeOutput();
            LOG.info("output = {}", output.getDisplayName());
            if (ignore.contains(TST_ItemID.create(output))) {
                LOG.info("ignore");
                continue;
            }

            if (Recipe instanceof ExtremeShapedRecipe recipe) {
                LOG.info("Recipe instanceof ExtremeShapedRecipe recipe");
                inputs = sortOutInputs(recipe.recipeItems);
            } else if (Recipe instanceof ExtremeShapedOreRecipe recipe) {
                LOG.info("Recipe instanceof ExtremeShapedOreRecipe recipe");
                inputs = sortOutInputs(recipe.getInput());
            } else if (Recipe instanceof ShapelessOreRecipe recipe) {
                LOG.info("Recipe instanceof ShapelessOreRecipe recipe");
                inputs = sortOutInputs(
                    recipe.getInput()
                        .toArray());
            }

            if (inputs != null && output != null) {
                LOG.info("Inputs and outputs both are valid. Building GTRecipe.");
                LOG.info("Inputs = {}", inputs);
                LOG.info("Output = {}", output);
                GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                    .ignoreCollision()
                    .itemInputs(inputs)
                    .itemOutputs(output)
                    .eut(0)
                    .duration(Config.TickEveryProcess_MegaCraftingCenter);

                Optional<GTRecipe.GTRecipe_WithAlt> oRecipe = builder.buildWithAlt();
                if (oRecipe.isPresent()) {
                    LOG.info("oRecipe is present");
                    visualExtremeCraftRecipes.add(oRecipe.get());
                    LOG.info("visual recipe adding finish : {}", output.getDisplayName());
                } else {
                    LOG.info("oRecipe is not present");
                    builder.addTo(visualExtremeCraftRecipes);
                }

                LOG.info("Creating ExtremeCraftRecipe");
                ExtremeCraftRecipe ecr = new ExtremeCraftRecipe().itemInputs(inputs)
                    .itemOutputs(output);
                extremeCraftRecipes.add(ecr);

                LOG.info("Creating TST_ItemID for output stack sign");
                TST_ItemID oi = TST_ItemID.create(output);
                if (extremeCraftRecipesMap.containsKey(oi)) {
                    LOG.info("just adding ExtremeCraftRecipe");
                    extremeCraftRecipesMap.get(oi)
                        .add(ecr);
                    LOG.info("finish adding ExtremeCraftRecipe : {}", output.getDisplayName());

                } else {
                    LOG.info("create new set to contain the new ExtremeCraftRecipe");
                    Set<ExtremeCraftRecipe> s = new HashSet<>();
                    s.add(ecr);
                    extremeCraftRecipesMap.put(oi, s);
                }

            } else {
                LOG.info("ExtremeCraftRecipeHandler get a null recipe.");
            }

            if (output != null) {
                LOG.info("finish recipe adding for {}", output.getDisplayName());
            } else {
                LOG.info("Final check : output is null");
            }
        }

        LOG.info("complete init extreme craft table recipe :" + extremeCraftRecipes.size());

    }

}
