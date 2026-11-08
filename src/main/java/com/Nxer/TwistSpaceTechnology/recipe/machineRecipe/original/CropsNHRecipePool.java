package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.gtnewhorizon.cropsnh.recipes.CropsNHGTRecipeMaps.CROPSNH_CROP_METADATAKEY;
import static com.gtnewhorizon.cropsnh.recipes.CropsNHGTRecipeMaps.CROPSNH_CROP_MUTATION_METADATAKEY;
import static com.gtnewhorizon.cropsnh.recipes.CropsNHGTRecipeMaps.fakeCropBreederRecipeMap;
import static com.gtnewhorizon.cropsnh.recipes.CropsNHGTRecipeMaps.fakeCropGeneExtractorRecipeMap;
import static com.gtnewhorizon.cropsnh.recipes.CropsNHGTRecipeMaps.fakeCropSynthesizerRecipeMap;
import static com.gtnewhorizon.cropsnh.recipes.CropsNHGTRecipeMaps.fakeSeedGeneratorRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ICropMutation;
import com.gtnewhorizon.cropsnh.farming.SeedStats;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.CropGeneExtractorFakeRecipeLoader.CircuitResult;
import com.gtnewhorizon.cropsnh.reference.Constants;
import com.gtnewhorizon.cropsnh.reference.Names;
import com.gtnewhorizon.cropsnh.tileentity.singleblock.MTECropBreeder;
import com.gtnewhorizon.cropsnh.tileentity.singleblock.MTECropGeneExtractor;
import com.gtnewhorizon.cropsnh.tileentity.singleblock.MTECropSynthesizer;
import com.gtnewhorizon.cropsnh.tileentity.singleblock.MTESeedGenerator;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourDataOrb;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;

public class CropsNHRecipePool {

    public static void loadPurpleMagnoliaRecipes(ICropMutation mutation) {
        ICropCard crop = mutation.getOutput();
        // TST registers its mutation at load-complete, after CropsNH generates its recipe pools.
        // Crop-only entries may already exist depending on the post-init order.
        if (!fakeSeedGeneratorRecipes.getBackend().cropIndex.containsKey(crop)) {
            loadSeedGeneratorRecipes(crop);
        }
        loadCropBreederRecipes(mutation);
        if (!fakeCropGeneExtractorRecipeMap.getBackend().cropIndex.containsKey(crop)) {
            loadGeneExtractorRecipes(crop);
        }
        if (!fakeCropSynthesizerRecipeMap.getBackend().cropIndex.containsKey(crop)) {
            loadCropSynthesizerRecipe(crop);
        }
    }

    private static void loadSeedGeneratorRecipes(ICropCard crop) {
        ItemStack seed = getSeedWithoutStats(crop, 0);
        for (Object2FloatMap.Entry<Fluid> fertilizer : MTESeedGenerator.ALLOWED_LIQUID_FERTILIZER
            .object2FloatEntrySet()) {
            // Duplicate Purple Magnolia seeds with liquid fertilizer.
            GTValues.RA.stdBuilder()
                .itemInputs(seed)
                .itemOutputs(seed)
                .fluidInputs(
                    new FluidStack(
                        fertilizer.getKey(),
                        MTESeedGenerator.getFluidAmount(
                            Constants.MAX_SEED_STAT,
                            Constants.MAX_SEED_STAT,
                            Constants.MAX_SEED_STAT,
                            fertilizer.getFloatValue())))
                .duration(MTESeedGenerator.BASE_RECIPE_DURATION)
                .eut(MTESeedGenerator.BASE_RECIPE_EUT)
                .metadata(CROPSNH_CROP_METADATAKEY, crop)
                .nbtSensitive()
                .ignoreCollision()
                .fake()
                .addTo(fakeSeedGeneratorRecipes);
        }
    }

    private static void loadCropBreederRecipes(ICropMutation mutation) {
        ItemStack[] parents = mutation.getParents()
            .stream()
            .map(parent -> getSeedWithoutStats(parent, 1))
            .toArray(ItemStack[]::new);
        for (Object2FloatMap.Entry<Fluid> fertilizer : MTECropBreeder.ALLOWED_LIQUID_FERTILIZER
            .object2FloatEntrySet()) {
            // Breed Purple Magnolia from its parent seeds.
            GTValues.RA.stdBuilder()
                .itemInputs(parents)
                .itemOutputs(
                    mutation.getOutput()
                        .getSeedItem(SeedStats.DEFAULT_ANALYZED))
                .fluidInputs(
                    new FluidStack(
                        fertilizer.getKey(),
                        MTECropBreeder.getFluidAmount(
                            mutation.getOutput()
                                .getTier(),
                            Constants.MAX_SEED_STAT,
                            Constants.MAX_SEED_STAT,
                            Constants.MAX_SEED_STAT,
                            fertilizer.getFloatValue())))
                .duration(mutation.getBreedingMachineRecipeDuration())
                .eut(mutation.getBreedingMachineRecipeEUt())
                .metadata(CROPSNH_CROP_MUTATION_METADATAKEY, mutation)
                .nbtSensitive()
                .addTo(fakeCropBreederRecipeMap);
        }
    }

    private static void loadGeneExtractorRecipes(ICropCard crop) {
        GTRecipeBuilder template = GTValues.RA.stdBuilder()
            .eut(MTECropGeneExtractor.getRecipeEUt(crop))
            .metadata(CROPSNH_CROP_METADATAKEY, crop)
            .special(ItemList.Tool_DataOrb.get(1))
            .ignoreCollision()
            .fake();

        // Extract Purple Magnolia specimen data.
        template.copy()
            .itemInputs(crop.getSeedItem(SeedStats.DEFAULT_ANALYZED), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(new CircuitResult(1).orb)
            .duration(MTECropGeneExtractor.getRecipeDuration(1))
            .addTo(fakeCropGeneExtractorRecipeMap);

        // Extract growth genes.
        template.copy()
            .itemInputs(crop.getSeedItem(SeedStats.DEFAULT_ANALYZED), GTUtility.getIntegratedCircuit(2))
            .itemOutputs(new CircuitResult(2).orb)
            .duration(MTECropGeneExtractor.getRecipeDuration(2))
            .addTo(fakeCropGeneExtractorRecipeMap);

        // Extract gain genes.
        template.copy()
            .itemInputs(crop.getSeedItem(SeedStats.DEFAULT_ANALYZED), GTUtility.getIntegratedCircuit(3))
            .itemOutputs(new CircuitResult(3).orb)
            .duration(MTECropGeneExtractor.getRecipeDuration(3))
            .addTo(fakeCropGeneExtractorRecipeMap);

        // Extract resistance genes.
        template.copy()
            .itemInputs(crop.getSeedItem(SeedStats.DEFAULT_ANALYZED), GTUtility.getIntegratedCircuit(4))
            .itemOutputs(new CircuitResult(4).orb)
            .duration(MTECropGeneExtractor.getRecipeDuration(4))
            .addTo(fakeCropGeneExtractorRecipeMap);
    }

    private static void loadCropSynthesizerRecipe(ICropCard crop) {
        ItemStack specimenOrb = ItemList.Tool_DataOrb.get(0);
        BehaviourDataOrb.setDataTitle(specimenOrb, Names.DataOrb.specimen);
        BehaviourDataOrb.setDataName(specimenOrb, crop.getId());
        ItemStack growthOrb = ItemList.Tool_DataOrb.get(0);
        BehaviourDataOrb.setDataTitle(growthOrb, Names.DataOrb.growth);
        ItemStack gainOrb = ItemList.Tool_DataOrb.get(0);
        BehaviourDataOrb.setDataTitle(gainOrb, Names.DataOrb.gain);
        ItemStack resistanceOrb = ItemList.Tool_DataOrb.get(0);
        BehaviourDataOrb.setDataTitle(resistanceOrb, Names.DataOrb.resistance);

        // Synthesize Purple Magnolia seeds from gene data and UU-Matter.
        GTValues.RA.stdBuilder()
            .itemInputs(specimenOrb, growthOrb, gainOrb, resistanceOrb)
            .itemOutputs(crop.getSeedItem(SeedStats.DEFAULT_ANALYZED))
            .fluidInputs(
                Materials.UUMatter.getFluid(
                    MTECropSynthesizer.getFluidAmount(
                        crop,
                        Constants.MAX_SEED_STAT,
                        Constants.MAX_SEED_STAT,
                        Constants.MAX_SEED_STAT)))
            .duration(MTECropSynthesizer.getRecipeDuration(crop))
            .eut(MTECropSynthesizer.getRecipeEUt(crop))
            .metadata(CROPSNH_CROP_METADATAKEY, crop)
            .ignoreCollision()
            .fake()
            .addTo(fakeCropSynthesizerRecipeMap);
    }

    private static ItemStack getSeedWithoutStats(ICropCard crop, int amount) {
        ItemStack seed = crop.getSeedItem(SeedStats.DEFAULT_ANALYZED);
        seed.stackSize = amount;
        seed.getTagCompound()
            .removeTag(Names.NBT.growth);
        seed.getTagCompound()
            .removeTag(Names.NBT.gain);
        seed.getTagCompound()
            .removeTag(Names.NBT.resistance);
        return seed;
    }
}
