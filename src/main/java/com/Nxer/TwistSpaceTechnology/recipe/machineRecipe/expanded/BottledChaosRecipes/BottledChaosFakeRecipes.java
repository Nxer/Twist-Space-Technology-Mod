package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Fruit;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosRecipeMetadata;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialRoutes;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;

/** Display-only batches read from the same prepared routes used by machine execution. */
public final class BottledChaosFakeRecipes {

    public static void registerRecipes() {
        GTCMRecipe.BottledChaosRecipes.getBackend()
            .clearRecipes();
        GTCMRecipe.BottledChaosExpandedRecipes.getBackend()
            .clearRecipes();
        var displays = BottledChaosRecipeProcessor.createDisplayRecipes();
        Map<RecipeCategory, Integer> familySizes = new LinkedHashMap<>();
        for (var display : displays) familySizes.merge(categoryForRoute(display), 1, Integer::sum);
        int registered = 0;
        for (var display : displays) {
            try {
                var plan = display.plan();
                ItemStack[] candidates = display.inputAlternatives();
                int inputMultiplier = candidates == null ? 0 : candidates[1].stackSize / candidates[0].stackSize;
                RecipeMap<?> recipeMap = displayMap(display);
                var ui = recipeMap.getFrontend()
                    .getUIProperties();
                if (display.itemInputs()
                    .size() > ui.maxItemInputs
                    || display.fluidInputs()
                        .size() > ui.maxFluidInputs
                    || plan.itemOutputs()
                        .size() > ui.maxItemOutputs
                    || plan.fluidOutputs()
                        .size() > ui.maxFluidOutputs) {
                    throw new IllegalArgumentException("display batch exceeds NEI slot capacity");
                }
                ItemStack[] itemInputs = new ItemStack[display.itemInputs()
                    .size()];
                Object[] alternativeInputs = null;
                for (int i = 0; i < itemInputs.length; i++) {
                    var input = display.itemInputs()
                        .get(i);
                    itemInputs[i] = GTUtility.copyAmountUnsafe(displayAmount(input.stackSize()), input.itemStack());
                    if (Fruit.equalItemStack(input.itemStack())) {
                        if (alternativeInputs == null) alternativeInputs = new Object[itemInputs.length];
                        alternativeInputs[i] = new Object[] { "listAllfruit", itemInputs[i].stackSize };
                    }
                    if (candidates != null && input.stackSize() > 0
                        && input.stackSize() <= Integer.MAX_VALUE / inputMultiplier
                        && TST_ItemID.create(candidates[0])
                            .equalItemStack(input.itemStack())) {
                        if (alternativeInputs == null) alternativeInputs = new Object[itemInputs.length];
                        alternativeInputs[i] = new ItemStack[] { itemInputs[i],
                            GTUtility.copyAmountUnsafe((int) (input.stackSize() * inputMultiplier), candidates[1]) };
                    }
                }
                if (alternativeInputs != null) for (int i = 0; i < itemInputs.length; i++) {
                    if (alternativeInputs[i] == null) alternativeInputs[i] = itemInputs[i];
                }
                FluidStack[] fluidInputs = new FluidStack[display.fluidInputs()
                    .size()];
                for (int i = 0; i < fluidInputs.length; i++) {
                    var input = display.fluidInputs()
                        .get(i);
                    fluidInputs[i] = GTUtility.copyAmount(displayAmount(input.amount()), input.fluidStack());
                }
                ItemStack[] itemOutputs = new ItemStack[plan.itemOutputs()
                    .size()];
                for (int i = 0; i < itemOutputs.length; i++) {
                    var output = plan.itemOutputs()
                        .get(i);
                    itemOutputs[i] = GTUtility.copyAmountUnsafe(displayAmount(output.stackSize()), output.itemStack());
                }
                FluidStack[] fluidOutputs = new FluidStack[plan.fluidOutputs()
                    .size()];
                for (int i = 0; i < fluidOutputs.length; i++) {
                    var output = plan.fluidOutputs()
                        .get(i);
                    fluidOutputs[i] = GTUtility.copyAmount(displayAmount(output.amount()), output.fluidStack());
                }
                if (!Double.isFinite(plan.durationTicks()) || plan.durationTicks() <= 0) {
                    throw new IllegalArgumentException("invalid display batch duration");
                }
                var route = CredentialRoutes.get(display.routeIndex());
                if (route == null) throw new IllegalStateException("missing credential route " + display.routeIndex());
                long routeMask = display.requiredTagMask();
                int requiredWeight = ItemBottledChaosCredential.getRequiredWeight(routeMask);
                ItemStack credential = null;
                for (int meta = 0; meta <= ItemBottledChaosCredential.MAX_META; meta++) {
                    credential = ItemBottledChaosCredential.createCredential(meta, routeMask, requiredWeight);
                    if (credential != null) break;
                }
                if (credential == null)
                    throw new IllegalStateException("no credential frame can hold route " + display.routeIndex());
                var recipeBuilder = GTValues.RA.stdBuilder();
                RecipeCategory category = categoryForRoute(display);
                if (recipeMap == GTCMRecipe.BottledChaosRecipes && familySizes.get(category) > 1)
                    recipeBuilder.recipeCategory(category);
                if (alternativeInputs == null) recipeBuilder.itemInputsUnsafe(itemInputs);
                else recipeBuilder.itemInputs(alternativeInputs);
                registered += recipeBuilder.fluidInputs(fluidInputs)
                    .itemOutputs(itemOutputs)
                    .fluidOutputs(fluidOutputs)
                    .special(credential)
                    .metadata(BottledChaosRecipeMetadata.REQUIRED_CATEGORY_MASK, plan.requiredCategories())
                    .metadata(BottledChaosRecipeMetadata.DISPLAY_BATCH, display)
                    .duration((int) Math.min(Integer.MAX_VALUE, Math.ceil(plan.durationTicks())))
                    .eut(display.baseEUt())
                    .fake()
                    .ignoreCollision()
                    .addTo(recipeMap)
                    .size();
            } catch (RuntimeException exception) {
                TwistSpaceTechnology.LOG.error(
                    "Could not register Bottled Chaos NEI sample for route " + display.routeIndex() + '.',
                    exception);
            }
        }
        TwistSpaceTechnology.LOG.info("Registered {} Bottled Chaos NEI route examples.", registered);
    }

    public static void registerCredentialRecipes() {
        GTCMRecipe.BottledChaosCredentialRecipes.getBackend()
            .clearRecipes();
        int registered = 0;
        try {
            ItemStack frame = ItemBottledChaosCredential.createEmptyFrame(1);
            ItemStack first = ItemBottledChaosCredential.createDisplayCredential(0, 1);
            ItemStack second = ItemBottledChaosCredential.createDisplayCredential(0, 2);
            ItemStack result = ItemBottledChaosCredential.createDisplayCredential(1, 3);
            if (frame == null || first == null || second == null || result == null)
                throw new IllegalStateException("missing credential succession sample");
            registered += GTValues.RA.stdBuilder()
                .itemInputsUnsafe(frame, first, second)
                .itemOutputs(result)
                .duration(1)
                .eut(0)
                .fake()
                .ignoreCollision()
                .addTo(GTCMRecipe.BottledChaosCredentialRecipes)
                .size();
        } catch (RuntimeException exception) {
            TwistSpaceTechnology.LOG.error("Could not register Bottled Chaos credential succession sample.", exception);
        }

        try {
            ItemStack frame = ItemBottledChaosCredential.createEmptyFrame(0);
            ItemStack source = ItemBottledChaosCredential.createDisplayCredential(1, 3);
            ItemStack remainder = ItemBottledChaosCredential.createDisplayCredential(1, 2);
            ItemStack transferred = ItemBottledChaosCredential.createDisplayCredential(0, 1);
            if (frame == null || source == null || remainder == null || transferred == null)
                throw new IllegalStateException("missing credential splitting sample");
            registered += GTValues.RA.stdBuilder()
                .itemInputsUnsafe(frame, source)
                .itemOutputs(remainder, transferred)
                .duration(1)
                .eut(0)
                .fake()
                .ignoreCollision()
                .addTo(GTCMRecipe.BottledChaosCredentialRecipes)
                .size();
        } catch (RuntimeException exception) {
            TwistSpaceTechnology.LOG.error("Could not register Bottled Chaos credential splitting sample.", exception);
        }

        for (int index : CredentialModel.displayOrder()) {
            try {
                var route = CredentialRoutes.get(index);
                if (route == null) throw new IllegalStateException("missing credential route " + index);
                int requiredWeight = ItemBottledChaosCredential.getRequiredWeight(route.mask());
                int meta = 0;
                while (meta <= ItemBottledChaosCredential.MAX_META && (1L << (2 * meta)) < requiredWeight) meta++;
                ItemStack frame = ItemBottledChaosCredential.createEmptyFrame(meta);
                ItemStack marker = route.rootStartStack();
                ItemStack result = ItemBottledChaosCredential.createCredential(meta, route.mask(), requiredWeight);
                if (frame == null || marker == null || result == null)
                    throw new IllegalStateException("missing credential injection sample for route " + index);
                registered += GTValues.RA.stdBuilder()
                    .itemInputsUnsafe(frame, marker)
                    .itemOutputs(result)
                    .duration(1)
                    .eut(0)
                    .fake()
                    .ignoreCollision()
                    .addTo(GTCMRecipe.BottledChaosCredentialRecipes)
                    .size();
            } catch (RuntimeException exception) {
                TwistSpaceTechnology.LOG.error(
                    "Could not register Bottled Chaos credential injection sample for route " + index + '.',
                    exception);
            }
        }
        TwistSpaceTechnology.LOG.info("Registered {} Bottled Chaos credential NEI examples.", registered);
    }

    /** Family tabs use the stable registered tags; a single recipe remains on the default machine tab. */
    private static RecipeCategory categoryForRoute(BottledChaosRecipeProcessor.DisplayRecipe display) {
        int routeIndex = display.routeIndex();
        if ("honeycomb".equals(CredentialModel.tag(routeIndex)))
            routeIndex = Long.numberOfTrailingZeros(display.requiredTagMask() & ~(1L << routeIndex));
        return switch (CredentialModel.tag(routeIndex)) {
            case "titanium" -> GTCMRecipe.BottledChaosTitaniumCategory;
            case "tungsten" -> GTCMRecipe.BottledChaosTungstenCategory;
            case "platinum_metallic_powder", "platinum_residue", "crude_rhodium_metal", "leach_residue", "palladium_metallic_powder", "rarest_metal_residue", "iridium_metal_residue" -> GTCMRecipe.BottledChaosPlatinumCategory;
            case "crystalline_alloy_molten", "prismatic_acid" -> GTCMRecipe.BottledChaosPrismaticCategory;
            case "bastnasite", "monazite", "rare_earth_cerium", "cerium", "samarium" -> GTCMRecipe.BottledChaosLanthanideCategory;
            case "naquadah", "enriched_naquadah", "naquadria" -> GTCMRecipe.BottledChaosNaquadahCategory;
            case "growth_medium_sterilized", "stem_cells", "seaweed_broth", "tau_ceti_e_seaweed_extract", "bio_medium_sterilized", "bio_cells" -> GTCMRecipe.BottledChaosBiologicalCategory;
            case "xenoxene", "super_heavy_radox", "heavy_radox", "light_radox", "super_light_radox" -> GTCMRecipe.BottledChaosRadoxCategory;
            case "kevlar" -> GTCMRecipe.BottledChaosKevlarCategory;
            case "purified_water_1", "purified_water_2", "purified_water_3", "purified_water_4", "purified_water_5", "purified_water_6", "purified_water_7", "purified_water_8" -> GTCMRecipe.BottledChaosPurifiedWaterCategory;
            default -> GTCMRecipe.BottledChaosRecipes.getDefaultRecipeCategory();
        };
    }

    /** Oversized entries have their own format; existing recipe slot geometry is unchanged. */
    private static RecipeMap<?> displayMap(BottledChaosRecipeProcessor.DisplayRecipe display) {
        var ui = GTCMRecipe.BottledChaosRecipes.getFrontend()
            .getUIProperties();
        return display.itemInputs()
            .size() > ui.maxItemInputs
            || display.fluidInputs()
                .size() > ui.maxFluidInputs
            || display.plan()
                .itemOutputs()
                .size() > ui.maxItemOutputs
            || display.plan()
                .fluidOutputs()
                .size() > ui.maxFluidOutputs ? GTCMRecipe.BottledChaosExpandedRecipes : GTCMRecipe.BottledChaosRecipes;
    }

    private static int displayAmount(long amount) {
        // The frontend reads exact long quantities from DISPLAY_BATCH for oversized stacks.
        return amount > Integer.MAX_VALUE ? 1 : Math.toIntExact(amount);
    }
}
