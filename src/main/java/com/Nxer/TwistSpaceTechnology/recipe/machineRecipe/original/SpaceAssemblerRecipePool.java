package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.dreammaster.gthandler.CustomItemList.PicoWafer;
import static com.dreammaster.gthandler.CustomItemList.RawPicoWafer;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class SpaceAssemblerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final RecipeMap<?> SA = com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;

        {
            // adv radiation proof plate
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.ElectrumFlux, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Trinium, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.VibrantAlloy, 4),
                    ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 0, 3))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8),
                    Materials.Lead.getMolten(144 * 16 * 9))
                .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(9))
                .specialValue(2)
                .eut(RECIPE_UEV)
                .duration(250)
                .noOptimize()
                .addTo(SA);

            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Lanthanum, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.ElectrumFlux, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Trinium, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.VibrantAlloy, 4),
                    ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 0, 12))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 48),
                    Materials.Lead.getPlasma(144 * 16 * 64))
                .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(64))
                .specialValue(3)
                .eut(RECIPE_UXV)
                .duration(250)
                .noOptimize()
                .addTo(SA);
        }

        {
            // ultimate battery

            // UMV
            // TODO -- Temporarily, be revised in the next version
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.SixPhasedCopper, 1),
                    GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64),
                    GTCMItemList.PerfectEngravedEnergyChip.get(1),
                    GTCMItemList.PerfectEngravedLaptronChip.get(1),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 4),
                    GTCMItemList.SiliconBasedNeuron.get(16),
                    ItemList.Circuit_Wafer_PPIC.get(64),
                    ItemList.Circuit_Wafer_PPIC.get(64),

                    ItemList.Circuit_Parts_DiodeXSMD.get(64),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(64),
                    ItemList.Circuit_Parts_ResistorXSMD.get(64),
                    ItemList.Circuit_Parts_TransistorXSMD.get(64),

                    MaterialsTST.Axonium.getNanite(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.screw, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                    MaterialsUEVplus.WhiteDwarfMatter.getMolten(144 * 12),
                    MaterialsUEVplus.BlackDwarfMatter.getMolten(144 * 12),
                    MaterialPool.ConcentratedUUMatter.getFluidOrGas(1))
                .itemOutputs(ItemList.ZPM3.get(1))
                .eut(RECIPE_UIV)
                .duration(20 * 50)
                .specialValue(2)
                .addTo(SA);

            // UXV
            // TODO -- Temporarily, be revised in the next version
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsTST.AxonisAlloy, 1),
                    GTOreDictUnificator.get(OrePrefixes.foil, MaterialsTST.Axonium, 64),
                    GTCMItemList.PerfectEngravedEnergyChip.get(8),
                    GTCMItemList.PerfectEngravedLaptronChip.get(8),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 4),
                    GTCMItemList.EnergyFluctuationSelfHarmonizer.get(8),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    ItemList.Circuit_Wafer_QPIC.get(64),

                    RawPicoWafer.get(64),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(64),
                    ItemList.Circuit_Parts_ResistorXSMD.get(64),
                    ItemList.Circuit_Parts_TransistorXSMD.get(64),

                    MaterialsUEVplus.WhiteDwarfMatter.getNanite(16),
                    MaterialsUEVplus.BlackDwarfMatter.getNanite(16),
                    GTOreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 32))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 96),
                    MaterialsUEVplus.Eternity.getMolten(144 * 24),
                    MaterialsTST.Axonium.getPlasma(144 * 24),
                    MaterialPool.ConcentratedUUMatter.getFluidOrGas(10))
                .itemOutputs(ItemList.ZPM4.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 50)
                .specialValue(3)
                .addTo(SA);

            // MAX
            // TODO -- Temporarily, be revised in the next version
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.Universium, 1),
                    GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Eternity, 64),
                    GTCMItemList.PerfectEngravedEnergyChip.get(32),
                    GTCMItemList.PerfectEngravedLaptronChip.get(32),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 4),
                    GTCMItemList.OpticalSOC.get(8),
                    PicoWafer.get(64),
                    PicoWafer.get(64),

                    ItemList.Circuit_Parts_CapacitorXSMD.get(64),
                    ItemList.Circuit_Parts_TransistorXSMD.get(64),
                    MaterialsUEVplus.MagMatter.getNanite(4),
                    GTOreDictUnificator
                        .get(OrePrefixes.screw, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 32))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 36),
                    MaterialsUEVplus.QuarkGluonPlasma.getFluid(4000),
                    MaterialPool.ConcentratedUUMatter.getFluidOrGas(100))
                .itemOutputs(ItemList.ZPM5.get(1))
                .eut(RECIPE_UXV)
                .duration(20 * 50)
                .specialValue(3)
                .addTo(SA);

            // ERR
            // TODO -- Temporarily, be revised in the next version

        }

    }
}
