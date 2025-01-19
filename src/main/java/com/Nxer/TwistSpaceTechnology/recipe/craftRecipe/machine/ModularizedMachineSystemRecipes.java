package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AdvancedHighPowerCoilBlock;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AnnihilationConstrainer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DysonSphereFrameComponent;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MiracleDoor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static com.dreammaster.item.ItemList.CircuitUXV;
import static goodgenerator.util.ItemRefer.HiC_T5;
import static gregtech.api.enums.ItemList.ZPM3;
import static gregtech.api.enums.ItemList.ZPM6;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_MassFab;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.thing.CustomItemList;

public class ModularizedMachineSystemRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        if (Config.EnableModularizedMachineSystem) {

            if (Config.EnableDimensionallyTranscendentMatterPlasmaForgePrototypeMK2) {
                GTValues.RA.stdBuilder()
                    .metadata(RESEARCH_ITEM, MiracleDoor.get(1))
                    .metadata(RESEARCH_TIME, 24 * HOURS)
                    .itemInputs(
                        CustomItemList.eM_Teleportation.get(64),
                        ItemList.Machine_Multi_PlasmaForge.get(64),
                        ItemRefer.Compact_Fusion_Coil_T4.get(64),
                        GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64),

                        AnnihilationConstrainer.get(64),
                        SpaceWarper.get(64),
                        SpaceWarper.get(64),
                        AnnihilationConstrainer.get(64),

                        ItemList.Emitter_UMV.get(64),
                        ItemList.Field_Generator_UMV.get(64),
                        ItemList.Field_Generator_UMV.get(64),
                        ItemList.Emitter_UMV.get(64),

                        AdvancedHighPowerCoilBlock.get(64),
                        ZPM6.get(64),
                        GravitationalLens.get(64),
                        AdvancedHighPowerCoilBlock.get(64))
                    .fluidInputs(
                        MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(2_000_000_000),
                        MaterialsUEVplus.Eternity.getMolten(144 * 524288),
                        GGMaterial.shirabon.getMolten(144 * 524288),
                        MaterialsUEVplus.SpaceTime.getMolten(144 * 2097152))
                    .itemOutputs(GTCMItemList.DimensionallyTranscendentMatterPlasmaForgePrototypeMK2.get(1))
                    .eut(RECIPE_UXV)
                    .duration(20 * 3600 * 24)
                    .addTo(AssemblyLine);
            }

            if (Config.EnableLargeNeutronOscillator) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        copyAmount(64, Loaders.NA),
                        HiC_T5.get(64),
                        ItemRefer.Compact_Fusion_Coil_T3.get(8),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 8))
                    .fluidInputs(
                        MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144 * 514),
                        GGMaterial.metastableOganesson.getMolten(144 * 514),
                        GGMaterial.dalisenite.getMolten(144 * 514))
                    .itemOutputs(GTCMItemList.LargeNeutronOscillator.get(1))
                    .specialValue(3)
                    .eut(RECIPE_UEV)
                    .duration(20 * 600)
                    .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);
            }

            if (Config.EnableRecipeRegistry_IndistinctTentacle) {
                TST_RecipeBuilder.builder()
                    .itemInputs(
                        GTOreDictUnificator.get(
                            OrePrefixes.frameGt,
                            MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                            64),
                        GTCMItemList.PerfectExecutionCore.get(1),
                        GTCMItemList.IndistinctTentacle.get(16),
                        AdvancedHighPowerCoilBlock.get(64),

                        copyAmount(16384, ItemList.EnergisedTesseract.get(1)),
                        StellarConstructionFrameMaterial.get(512),
                        DysonSphereFrameComponent.get(512),
                        AnnihilationConstrainer.get(512),
                        // TODO Quantum Circuit
                        copyAmount(1024, CircuitUXV.getIS(1)),
                        copyAmount(1024, CustomItemList.eM_Ultimate_Containment_Advanced.get(1)))
                    .fluidInputs(
                        Materials.UUMatter.getFluid(2_000_000_000),
                        MaterialsUEVplus.Eternity.getMolten(144 * 524288),
                        MaterialsUEVplus.Universium.getMolten(144 * 524288),
                        MaterialsUEVplus.SpaceTime.getMolten(144 * 524288))
                    .itemOutputs(GTCMItemList.IndistinctTentaclePrototypeMK2.get(1))
                    .eut(RECIPE_MAX)
                    .duration(20 * 3600 * 24)
                    .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
            }

            // Mass Fabricator Genesis
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.MassFabricatorUMV.get(1))
                .metadata(RESEARCH_TIME, 24 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 64),
                    Industrial_MassFab.get(64),
                    ZPM3.get(64),
                    // TODO quantum circuit
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),

                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),

                    Laser_Lens_Special.get(64),
                    Laser_Lens_Special.get(64),
                    Laser_Lens_Special.get(64),
                    Laser_Lens_Special.get(64),

                    HighEnergyFlowCircuit.get(64),
                    CustomItemList.Machine_Multi_Transformer.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 64))
                .fluidInputs(
                    MaterialsUEVplus.Space.getMolten(144 * 256),
                    MaterialsUEVplus.Time.getMolten(144 * 256),
                    MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144 * 1024),
                    Materials.CosmicNeutronium.getMolten(144 * 2048))
                .itemOutputs(GTCMItemList.MassFabricatorGenesis.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 3600)
                .addTo(AssemblyLine);

        }
    }
}
