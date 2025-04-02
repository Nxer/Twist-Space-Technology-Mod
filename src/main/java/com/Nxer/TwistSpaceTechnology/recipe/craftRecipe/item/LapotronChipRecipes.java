package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static com.Nxer.TwistSpaceTechnology.util.TstUtils.copyAmount;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;
import com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;

public class LapotronChipRecipes {

    public static void loadRecipes() {

        // Shard
        GTValues.RA.stdBuilder()
            .itemInputs(GTCMItemList.PerfectEnergyCrystal.get(1))
            .itemOutputs(GTCMItemList.EnergyCrystalShard.get(64))
            .eut(TierEU.RECIPE_ZPM)
            .duration(20 * 5)
            .addTo(RecipeMaps.hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTCMItemList.PerfectLapotronCrystal.get(1))
            .itemOutputs(GTCMItemList.LapotronShard.get(64))
            .eut(TierEU.RECIPE_UV)
            .duration(20 * 5)
            .addTo(RecipeMaps.hammerRecipes);

        // The first piece
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(64), ItemList.IC2_Energium_Dust.get(64))
            .fluidInputs(Materials.Redstone.getMolten(144 * 1024))
            .itemOutputs(GTCMItemList.EnergyCrystalShard.get(1))
            .outputChances(1111)
            .eut(TierEU.RECIPE_ZPM)
            .duration(20 * 3000)
            .addTo(RecipeMaps.autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.RawLapotronCrystal.get(16),
                CustomItemList.LapotronDust.get(64),
                CustomItemList.LapotronDust.get(64),
                CustomItemList.LapotronDust.get(64))
            .fluidInputs(MaterialPool.HolmiumGarnet.getMolten(144 * 256))
            .itemOutputs(GTCMItemList.LapotronShard.get(1))
            .outputChances(999)
            .eut(TierEU.RECIPE_UHV)
            .duration(20 * 3000)
            .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

        // Growth
        if (Config.PerfectCrystalRecipeNonCyclized) {
            // Energy Crystal
            GTValues.RA.stdBuilder()
                .itemInputs(GTCMItemList.EnergyCrystalShard.get(1), ItemList.IC2_Energium_Dust.get(64))
                .fluidInputs(Materials.EnergeticAlloy.getMolten(144 * 2))
                .itemOutputs(GTCMItemList.PerfectEnergyCrystal.get(16))
                .eut(TierEU.RECIPE_ZPM)
                .duration(20 * 30)
                .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTCMItemList.PerfectEnergyCrystal.get(0),
                    ItemList.IC2_Energium_Dust.get(64),
                    ItemList.IC2_Energium_Dust.get(64),
                    ItemList.IC2_Energium_Dust.get(64),
                    ItemList.IC2_Energium_Dust.get(64))
                .fluidInputs(Materials.EnergeticAlloy.getMolten(144 * 64))
                .itemOutputs(GTCMItemList.PerfectEnergyCrystal.get(1))
                .eut(TierEU.RECIPE_ZPM)
                .duration(20 * 300)
                .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

            // Lapotron
            GTValues.RA.stdBuilder()
                .itemInputs(GTCMItemList.LapotronShard.get(1), MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))
                .fluidInputs(Materials.VibrantAlloy.getMolten(144 * 2))
                .itemOutputs(GTCMItemList.PerfectLapotronCrystal.get(16))
                .eut(TierEU.RECIPE_UHV)
                .duration(20 * 30)
                .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTCMItemList.PerfectLapotronCrystal.get(0),
                    MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                    MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                    MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                    MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64))
                .fluidInputs(Materials.VibrantAlloy.getMolten(144 * 64))
                .itemOutputs(GTCMItemList.PerfectLapotronCrystal.get(1))
                .eut(TierEU.RECIPE_UHV)
                .duration(20 * 300)
                .addTo(GTCMRecipe.CrystallineInfinitierRecipes);
        } else {
            // Energy Crystal
            GTValues.RA.stdBuilder()
                .itemInputs(GTCMItemList.EnergyCrystalShard.get(1), ItemList.IC2_Energium_Dust.get(64))
                .fluidInputs(Materials.EnergeticAlloy.getMolten(144 * 2))
                .itemOutputs(GTCMItemList.PerfectEnergyCrystal.get(4))
                .eut(TierEU.RECIPE_ZPM)
                .duration(20 * 30)
                .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

            // Lapotron
            GTValues.RA.stdBuilder()
                .itemInputs(GTCMItemList.LapotronShard.get(1), MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))
                .fluidInputs(Materials.VibrantAlloy.getMolten(144 * 2))
                .itemOutputs(GTCMItemList.PerfectLapotronCrystal.get(4))
                .eut(TierEU.RECIPE_UHV)
                .duration(20 * 30)
                .addTo(GTCMRecipe.CrystallineInfinitierRecipes);
        }

        // Chip
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensBlue")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTCMItemList.PerfectLapotronCrystal.get(1), GTUtility.copyAmountUnsafe(0, itemStack))
                .itemOutputs(
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(64))
                .noOptimize()
                .eut(TierEU.RECIPE_UHV)
                .duration(20 * 15)
                .addTo(RecipeMaps.laserEngraverRecipes);
        }

        for (ItemStack itemStack : OreDictionary.getOres("craftingLensRed")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTCMItemList.PerfectEnergyCrystal.get(1), GTUtility.copyAmountUnsafe(0, itemStack))
                .itemOutputs(CustomItemList.EngravedEnergyChip.get(64), CustomItemList.EngravedEnergyChip.get(64))
                .noOptimize()
                .eut(TierEU.RECIPE_UV)
                .duration(20 * 15)
                .addTo(RecipeMaps.laserEngraverRecipes);
        }

        // Perfect Energy Chip
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.PerfectEnergyCrystal.get(64),
                copyAmount(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Force, 1), 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(200))
            .itemOutputs(GTCMItemList.EngravedEnergyExposedChip.get(1))
            .noOptimize()
            .eut(TierEU.RECIPE_UHV)
            .duration(20 * 120)
            .addTo(RecipeMaps.laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.PerfectEnergyCrystal.get(64),
                copyAmount(GTOreDictUnificator.get(OrePrefixes.lens, Materials.Force, 1), 0),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(100))
            .itemOutputs(GTCMItemList.EngravedEnergyExposedChip.get(2))
            .noOptimize()
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 60)
            .addTo(RecipeMaps.laserEngraverRecipes);

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
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

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
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

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
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

        // Perfect Lapotron Chip
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.PerfectLapotronCrystal.get(64),
                copyAmount(0, GTOreDictUnificator.get(OrePrefixes.lens, Materials.Tanzanite, 1)),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade7PurifiedWater.getFluid(200))
            .itemOutputs(GTCMItemList.EngravedLaptronExposedChip.get(1))
            .noOptimize()
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 240)
            .addTo(RecipeMaps.laserEngraverRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.PerfectLapotronCrystal.get(64),
                copyAmount(0, GTOreDictUnificator.get(OrePrefixes.lens, Materials.Tanzanite, 1)),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0),
                Laser_Lens_Special.get(0))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(100))
            .itemOutputs(GTCMItemList.EngravedLaptronExposedChip.get(2))
            .noOptimize()
            .eut(TierEU.RECIPE_UIV)
            .duration(20 * 120)
            .addTo(RecipeMaps.laserEngraverRecipes);

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
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

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
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

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
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

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
            .addTo(RecipeMaps.circuitAssemblerRecipes);
    }
}
