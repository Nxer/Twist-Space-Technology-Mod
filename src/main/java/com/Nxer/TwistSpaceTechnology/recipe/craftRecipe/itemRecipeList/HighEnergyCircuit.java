package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.itemRecipeList;

import bartworks.system.material.WerkstoffLoader;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.EngravedEnergyExposedChip;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.EngravedLaptronExposedChip;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PerfectEnergyCrystal;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PerfectLapotronCrystal;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

public class HighEnergyCircuit implements IRecipePool {
    @Override
    public void loadRecipes() {
        // Energy Chip
        TST_RecipeBuilder.builder()
            .itemInputs(
                PerfectEnergyCrystal.get(64),
                GGMaterial.orundum.get(OrePrefixes.lens, 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0)
            )
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(200))
            .itemOutputs(EngravedEnergyExposedChip.get(1))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(120 * 20)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                PerfectEnergyCrystal.get(64),
                GGMaterial.orundum.get(OrePrefixes.lens, 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0)
            )
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(100))
            .itemOutputs(EngravedEnergyExposedChip.get(2))
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(60 * 20)
            .addTo(laserEngraverRecipes);

        // Lapotron Chip
        TST_RecipeBuilder.builder()
            .itemInputs(
                PerfectLapotronCrystal.get(64),
                GGMaterial.orundum.get(OrePrefixes.lens, 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0)
            )
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(200))
            .itemOutputs(EngravedEnergyExposedChip.get(1))
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(240 * 20)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                PerfectLapotronCrystal.get(64),
                GGMaterial.orundum.get(OrePrefixes.lens, 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0)
            )
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(100))
            .itemOutputs(EngravedLaptronExposedChip.get(2))
            .noOptimize()
            .eut(RECIPE_UIV)
            .duration(120 * 20)
            .addTo(laserEngraverRecipes);

        //PreciseHighEnergyPhotonicQuantumMasterRecipes
    }
}
