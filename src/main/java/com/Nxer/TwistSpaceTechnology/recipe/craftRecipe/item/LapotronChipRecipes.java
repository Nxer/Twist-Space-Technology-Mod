package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static com.Nxer.TwistSpaceTechnology.util.TstUtils.copyAmount;
import static com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;

public class LapotronChipRecipes {

    public static void loadRecipes() {
        // Energy Chip
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.PerfectEnergyCrystal.get(64),
                GGMaterial.orundum.get(OrePrefixes.lens, 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(200))
            .itemOutputs(GTCMItemList.EngravedEnergyExposedChip.get(1))
            .noOptimize()
            .eut(TierEU.RECIPE_UHV)
            .duration(20 * 120)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.PerfectEnergyCrystal.get(64),
                GGMaterial.orundum.get(OrePrefixes.lens, 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(100))
            .itemOutputs(GTCMItemList.EngravedEnergyExposedChip.get(2))
            .noOptimize()
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 60)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.EngravedEnergyExposedChip.get(1),
                Materials.Gold.getNanite(1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 16),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 16),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.EnergeticAlloy, 1),
                ModItemHandler.EternalSingularity.MeteoricSingularity.get(1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.StellarAlloy, 16),
                MaterialsAlloy.LAURENIUM.getFoil(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 32),
                MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144),
                MaterialsUEVplus.ExcitedDTEC.getFluid(8000),
                Materials.Infinity.getMolten(144 * 16))
            .itemOutputs(GTCMItemList.PerfectEngravedEnergyChip.get(1))
            .noOptimize()
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 60)
            .specialValue(1)
            .addTo(spaceAssemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.EngravedEnergyExposedChip.get(4),
                Materials.Gold.getNanite(4),
                GGMaterial.lumiium.get(OrePrefixes.foil, 8),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFoil(8),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.EnergeticAlloy, 1),
                ModItemHandler.EternalSingularity.MeteoricSingularity.get(1),
                MaterialsElements.STANDALONE.RHUGNOR.getFoil(8),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Rubidium, 8))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 16),
                MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144 * 4),
                MaterialsUEVplus.PhononMedium.getFluid(500 * 4),
                MaterialsUEVplus.Time.getMolten(72 * 8))
            .itemOutputs(GTCMItemList.PerfectEngravedEnergyChip.get(4))
            .noOptimize()
            .eut(TierEU.RECIPE_UIV)
            .duration(20 * 60)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.EngravedEnergyExposedChip.get(16),
                Materials.Gold.getNanite(16),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SixPhasedCopper, 4),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.BlackDwarfMatter, 4),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.EnergeticAlloy, 1),
                ModItemHandler.EternalSingularity.MeteoricSingularity.get(1),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Creon, 4),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFoil(4))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8),
                new FluidStack(MaterialsElements.STANDALONE.HYPOGEN.getPlasma(), 144 * 16),
                MaterialsUEVplus.PhononMedium.getFluid(500 * 8),
                MaterialsUEVplus.Time.getMolten(72 * 16))
            .itemOutputs(GTCMItemList.PerfectEngravedEnergyChip.get(16))
            .noOptimize()
            .eut(TierEU.RECIPE_UMV)
            .duration(20 * 60)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        // Lapotron Chip
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.PerfectLapotronCrystal.get(64),
                copyAmount(0, GTOreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1)),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(200))
            .itemOutputs(GTCMItemList.EngravedLaptronExposedChip.get(1))
            .noOptimize()
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 240)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.PerfectLapotronCrystal.get(64),
                copyAmount(0, GTOreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1)),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(100))
            .itemOutputs(GTCMItemList.EngravedLaptronExposedChip.get(2))
            .noOptimize()
            .eut(TierEU.RECIPE_UIV)
            .duration(20 * 120)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.EngravedLaptronExposedChip.get(1),
                MaterialsUEVplus.TranscendentMetal.getNanite(1),
                GGMaterial.enrichedNaquadahAlloy.get(OrePrefixes.foil, 16),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFoil(16),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.VibrantAlloy, 1),
                ModItemHandler.EternalSingularity.NitronicSingularity.get(1),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFoil(16),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.DeepIron, 16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 32),
                MaterialsUEVplus.SpaceTime.getMolten(72),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(2000),
                MaterialsUEVplus.Space.getMolten(72 * 4))
            .itemOutputs(GTCMItemList.PerfectEngravedLaptronChip.get(1))
            .noOptimize()
            .eut(TierEU.RECIPE_UIV)
            .duration(20 * 60)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.EngravedLaptronExposedChip.get(4),
                MaterialsUEVplus.TranscendentMetal.getNanite(4),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.EnrichedHolmium, 8),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.WhiteDwarfMatter, 8),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.VibrantAlloy, 1),
                ModItemHandler.EternalSingularity.NitronicSingularity.get(1),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Mellion, 8),
                GGMaterial.tairitsu.get(OrePrefixes.foil, 8))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 16),
                MaterialsUEVplus.SpaceTime.getMolten(72 * 4),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(4000),
                MaterialsUEVplus.Space.getMolten(72 * 8))
            .itemOutputs(GTCMItemList.PerfectEngravedLaptronChip.get(4))
            .noOptimize()
            .eut(TierEU.RECIPE_UMV)
            .duration(20 * 60)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.EngravedLaptronExposedChip.get(16),
                MaterialsUEVplus.TranscendentMetal.getNanite(16),
                GGMaterial.shirabon.get(OrePrefixes.foil, 4),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 4),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.VibrantAlloy, 1),
                ModItemHandler.EternalSingularity.NitronicSingularity.get(1),
                GGMaterial.metastableOganesson.get(OrePrefixes.foil, 4),
                MaterialsAlloy.QUANTUM.getFoil(4))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8),
                MaterialsUEVplus.SpaceTime.getMolten(72 * 16),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(8000),
                MaterialsUEVplus.Space.getMolten(72 * 16))
            .itemOutputs(GTCMItemList.PerfectEngravedLaptronChip.get(16))
            .noOptimize()
            .eut(TierEU.RECIPE_UXV)
            .duration(20 * 60)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        // UltimateEnergyFlowCircuit
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.SpaceTimeSuperconductingInlaidMotherboard.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 2),
                MaterialsUEVplus.SixPhasedCopper.getNanite(1),

                GTCMItemList.PerfectEngravedEnergyChip.get(2),
                GTCMItemList.PerfectEngravedLaptronChip.get(2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 64))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8))
            .itemOutputs(GTCMItemList.UltimateEnergyFlowCircuit.get(1))
            .noOptimize()
            .eut(TierEU.RECIPE_UIV)
            .duration(20 * 120)
            .addTo(circuitAssemblerRecipes);
    }
}
