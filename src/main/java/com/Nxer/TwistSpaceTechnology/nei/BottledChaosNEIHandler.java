package com.Nxer.TwistSpaceTechnology.nei;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosRecipeMetadata.DISPLAY_BATCH;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.nei.GTNEIDefaultHandler;

/** Restricts ingredient lookup while retaining GT's full layout, alternatives, fluid lookup and category cache. */
@SideOnly(Side.CLIENT)
public final class BottledChaosNEIHandler extends GTNEIDefaultHandler {

    public BottledChaosNEIHandler(RecipeCategory category) {
        super(category);
    }

    /** GT still supplies category icons, layout information and controller catalysts for these maps. */
    public static void register() {
        if (!GTCMItemList.BottledChaos.hasBeenSet()) return;
        for (RecipeCategory category : RecipeCategory.ALL_RECIPE_CATEGORIES.values()) {
            if (category.recipeMap != GTCMRecipe.BottledChaosRecipes
                && category.recipeMap != GTCMRecipe.BottledChaosExpandedRecipes) continue;
            var handler = new BottledChaosNEIHandler(category);
            API.registerRecipeHandler(handler);
            API.registerUsageHandler(handler);
        }
    }

    @Override
    public String getHandlerId() {
        return getOverlayIdentifier();
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new BottledChaosNEIHandler(recipeCategory);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!isController(result) && !loadCredential(result)) super.loadCraftingRecipes(result);
    }

    @Override
    public void loadUsageRecipes(ItemStack input) {
        if (isController(input)) arecipes.addAll(getCache());
        else if (!loadCredential(input)) super.loadUsageRecipes(input);
    }

    @Override
    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
        // Controller and credential browsing must not inherit GT's catalyst voltage filter.
        if (inputId.equals("item") && ingredients.length > 0
            && ingredients[0] instanceof ItemStack stack
            && (isController(stack) || ItemBottledChaosCredential.getCapacity(stack) > 0))
            return getUsageHandler(inputId, ingredients);
        return super.getUsageAndCatalystHandler(inputId, ingredients);
    }

    private static boolean isController(ItemStack stack) {
        return GTCMItemList.BottledChaos.hasBeenSet()
            && GTUtility.areStacksEqual(stack, GTCMItemList.BottledChaos.get(1), true);
    }

    /** Blank frames browse all entries; bound credentials browse every entry their tags authorize. */
    private boolean loadCredential(ItemStack stack) {
        if (ItemBottledChaosCredential.getCapacity(stack) == 0) return false;
        long tags = ItemBottledChaosCredential.getRouteMask(stack);
        for (CachedDefaultRecipe recipe : getCache()) {
            var display = recipe.mRecipe.getMetadata(DISPLAY_BATCH);
            if (display != null && (tags == 0 || (tags & display.requiredTagMask()) == display.requiredTagMask()))
                arecipes.add(recipe);
        }
        return true;
    }

    @Override
    protected CachedDefaultRecipe createCachedRecipe(GTRecipe recipe) {
        return new SearchableRecipe(recipe);
    }

    /** Search-only slot lists are prepared once; the original lists remain available for rendering and tooltips. */
    private final class SearchableRecipe extends CachedDefaultRecipe {

        private final List<PositionedStack> lookupInputs;
        private final List<PositionedStack> lookupOutputs;

        private SearchableRecipe(GTRecipe recipe) {
            super(recipe);
            var display = recipe.getMetadata(DISPLAY_BATCH);
            if (display == null) {
                lookupInputs = mInputs;
                lookupOutputs = mOutputs;
            } else {
                // Query direction belongs to the marker definition, independently of the displayed material side.
                List<PositionedStack> materialSlots = new ArrayList<>(mInputs);
                materialSlots.addAll(mOutputs);
                lookupInputs = markedSlots(materialSlots, display.lookupInputs());
                lookupOutputs = markedSlots(materialSlots, display.lookupOutputs());
            }
        }

        @Override
        public boolean contains(Collection<PositionedStack> stacks, ItemStack ingredient) {
            return super.contains(
                stacks == mInputs ? lookupInputs : stacks == mOutputs ? lookupOutputs : stacks,
                ingredient);
        }
    }

    private static List<PositionedStack> markedSlots(List<PositionedStack> stacks, Object... keys) {
        List<PositionedStack> result = new ArrayList<>();
        for (PositionedStack stack : stacks) {
            for (Object key : keys) {
                if (isMarkedSlot(stack, key)) {
                    result.add(stack);
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isMarkedSlot(PositionedStack stack, Object key) {
        if (key instanceof TST_ItemID item) return stack.containsWithNBT(item.getItemStack());
        if (key instanceof FluidId fluid) for (ItemStack alternative : stack.items) {
            FluidStack displayed = GTUtility.getFluidFromDisplayStack(alternative);
            if (displayed != null && fluid.equals(FluidId.create(displayed))) return true;
        }
        return false;
    }
}
