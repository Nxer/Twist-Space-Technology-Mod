package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.EngravedEnergyExposedChip;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.EngravedLaptronExposedChip;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PerfectEnergyCrystal;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PerfectEngravedEnergyChip;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PerfectEngravedLaptronChip;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PerfectLapotronCrystal;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceTimeSuperconductingInlaidMotherboard;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.UltimateEnergyFlowCircuit;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.MeteoricSingularity;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.NitronicSingularity;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;

public class LapotronChipRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        // Energy Chip
        TST_RecipeBuilder.builder()
            .itemInputs(
                PerfectEnergyCrystal.get(64),
                GGMaterial.orundum.get(OrePrefixes.lens, 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
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
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(100))
            .itemOutputs(EngravedEnergyExposedChip.get(2))
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(60 * 20)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                EngravedEnergyExposedChip.get(1),
                Materials.Gold.getNanite(1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 16),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 16),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.EnergeticAlloy, 1),
                MeteoricSingularity.getLeft(),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.StellarAlloy, 16),
                MaterialsAlloy.LAURENIUM.getFoil(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 32),
                MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144),
                MaterialsUEVplus.ExcitedDTEC.getFluid(8000),
                Materials.Infinity.getMolten(144 * 16))
            .itemOutputs(PerfectEngravedEnergyChip.get(1))
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(60 * 20)
            .specialValue(1)
            .addTo(spaceAssemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                EngravedEnergyExposedChip.get(4),
                Materials.Gold.getNanite(4),
                GGMaterial.lumiium.get(OrePrefixes.foil, 8),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFoil(8),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.EnergeticAlloy, 1),
                MeteoricSingularity.getLeft(),
                MaterialsElements.STANDALONE.RHUGNOR.getFoil(8),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Rubidium, 8))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 16),
                MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144 * 4),
                MaterialsUEVplus.PhononMedium.getFluid(500 * 4),
                MaterialsUEVplus.Time.getMolten(72 * 8))
            .itemOutputs(PerfectEngravedEnergyChip.get(4))
            .noOptimize()
            .eut(RECIPE_UIV)
            .duration(60 * 20)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                EngravedEnergyExposedChip.get(16),
                Materials.Gold.getNanite(16),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SixPhasedCopper, 4),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.BlackDwarfMatter, 4),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.EnergeticAlloy, 1),
                MeteoricSingularity.getLeft(),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Creon, 4),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFoil(4))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8),
                new FluidStack(MaterialsElements.STANDALONE.HYPOGEN.getPlasma(), 144 * 16),
                MaterialsUEVplus.PhononMedium.getFluid(500 * 8),
                MaterialsUEVplus.Time.getMolten(72 * 16))
            .itemOutputs(PerfectEngravedEnergyChip.get(16))
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(60 * 20)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        // Lapotron Chip
        TST_RecipeBuilder.builder()
            .itemInputs(
                PerfectLapotronCrystal.get(64),
                copyAmount(0, GTOreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1)),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(200))
            .itemOutputs(EngravedLaptronExposedChip.get(1))
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(240 * 20)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                PerfectLapotronCrystal.get(64),
                copyAmount(0, GTOreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1)),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(100))
            .itemOutputs(EngravedLaptronExposedChip.get(2))
            .noOptimize()
            .eut(RECIPE_UIV)
            .duration(120 * 20)
            .addTo(laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                EngravedLaptronExposedChip.get(1),
                MaterialsUEVplus.TranscendentMetal.getNanite(1),
                GGMaterial.enrichedNaquadahAlloy.get(OrePrefixes.foil, 16),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFoil(16),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.VibrantAlloy, 1),
                NitronicSingularity.getLeft(),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFoil(16),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.DeepIron, 16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 32),
                MaterialsUEVplus.SpaceTime.getMolten(72),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(2000),
                MaterialsUEVplus.Space.getMolten(72 * 4))
            .itemOutputs(PerfectEngravedLaptronChip.get(1))
            .noOptimize()
            .eut(RECIPE_UIV)
            .duration(60 * 20)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                EngravedLaptronExposedChip.get(4),
                MaterialsUEVplus.TranscendentMetal.getNanite(4),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.EnrichedHolmium, 8),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.WhiteDwarfMatter, 8),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.VibrantAlloy, 1),
                NitronicSingularity.getLeft(),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Mellion, 8),
                GGMaterial.tairitsu.get(OrePrefixes.foil, 8))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 16),
                MaterialsUEVplus.SpaceTime.getMolten(72 * 4),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(4000),
                MaterialsUEVplus.Space.getMolten(72 * 8))
            .itemOutputs(PerfectEngravedLaptronChip.get(4))
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(60 * 20)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                EngravedLaptronExposedChip.get(16),
                MaterialsUEVplus.TranscendentMetal.getNanite(16),
                GGMaterial.shirabon.get(OrePrefixes.foil, 4),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 4),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.VibrantAlloy, 1),
                NitronicSingularity.getLeft(),
                GGMaterial.metastableOganesson.get(OrePrefixes.foil, 4),
                MaterialsAlloy.QUANTUM.getFoil(4))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8),
                MaterialsUEVplus.SpaceTime.getMolten(72 * 16),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(8000),
                MaterialsUEVplus.Space.getMolten(72 * 16))
            .itemOutputs(PerfectEngravedLaptronChip.get(16))
            .noOptimize()
            .eut(RECIPE_UXV)
            .duration(60 * 20)
            .specialValue(2)
            .addTo(spaceAssemblerRecipes);

        // UltimateEnergyFlowCircuit
        TST_RecipeBuilder.builder()
            .itemInputs(
                SpaceTimeSuperconductingInlaidMotherboard.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 2),
                MaterialsTST.Axonium.getNanite(2),

                PerfectEngravedEnergyChip.get(2),
                PerfectEngravedLaptronChip.get(2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 64))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8))
            .itemOutputs(UltimateEnergyFlowCircuit.get(1))
            .noOptimize()
            .eut(RECIPE_UIV)
            .duration(120 * 20)
            .addTo(circuitAssemblerRecipes);

        // TST_RecipeBuilder.builder()
        // .itemInputs(
        // GTUtility.getIntegratedCircuit(16),
        // SpaceTimeSuperconductingInlaidMotherboard.get(12),
        // GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 24),
        // MaterialsTST.Axonium.getNanite(24),
        //
        // PerfectEngravedEnergyChip.get(24),
        // PerfectEngravedLaptronChip.get(24),
        // GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 48))
        // .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8))
        // .itemOutputs(UltimateEnergyFlowCircuit.get(16))
        // .noOptimize()
        // .eut(RECIPE_UMV)
        // .duration(360 * 20)
        // .addTo(MiracleTopRecipes);
    }
}
