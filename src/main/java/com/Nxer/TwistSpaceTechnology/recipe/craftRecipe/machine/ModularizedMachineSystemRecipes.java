package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.UXV;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
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
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraftforge.fluids.FluidStack;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class ModularizedMachineSystemRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        if (!Config.EnableModularizedMachineSystem) return;
        // spotless:off
        if (Config.EnableDimensionallyTranscendentMatterPlasmaForgePrototypeMK2) {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Machine_Multi_PlasmaForge.get(1),
                65_536_000,
                16384,
                (int) RECIPE_UMV,
                64,
                new Object[] {
                    ItemList.Machine_Multi_PlasmaForge.get(64),
                    CustomItemList.eM_Teleportation.get(64),
                    CustomItemList.eM_Containment_Advanced.get(64),
                    GTCMItemList.StabilisationFieldGeneratorUMV.get(64),

                    GTCMItemList.AnnihilationConstrainer.get(64),
                    GTCMItemList.SpaceWarper.get(64),
                    GTCMItemList.SpaceWarper.get(64),
                    GTCMItemList.AnnihilationConstrainer.get(64),

                    ItemList.Emitter_UMV.get(64),
                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),

                    GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                    GTCMItemList.GravitationalLens.get(64),
                    GTCMItemList.GravitationalLens.get(64),
                    GTCMItemList.AdvancedHighPowerCoilBlock.get(64)},
                new FluidStack[] {
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 2097152),
                    MaterialsUEVplus.Eternity.getMolten(144 * 524288),
                    MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(2_000_000_000),
                    MaterialsUEVplus.Protomatter.getFluid(2_000_000_000)},
                GTCMItemList.DimensionallyTranscendentMatterPlasmaForgePrototypeMK2.get(1),
                20 * 3600 * 24,
                (int) RECIPE_UXV);
        }

        if (Config.EnableLargeNeutronOscillator) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    copyAmount(64, Loaders.NA),
                    ItemRefer.HiC_T5.get(64),
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
                    GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64),
                    GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                    GTCMItemList.IndistinctTentacle.get(16),
                    GTCMItemList.HarmoniousWirelessEnergyHatch.get(4),

                    GTCMItemList.PerfectExecutionCore.get(1),
                    copyAmount(16384, ItemList.EnergisedTesseract.get(1)),
                    GTCMItemList.StellarConstructionFrameMaterial.get(512),
                    GTCMItemList.DysonSphereFrameComponent.get(512),

                    GTCMItemList.AnnihilationConstrainer.get(512),
                    copyAmount(1024, GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UXV,1)),
                    copyAmount(1024, CustomItemList.eM_Ultimate_Containment_Advanced.get(1)))
                .fluidInputs(
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 524288),
                    MaterialsUEVplus.Universium.getMolten(144 * 524288),
                    MaterialsUEVplus.Eternity.getMolten(144 * 1048576),
                    MaterialPool.ConcentratedUUMatter.getFluidOrGas(2_000_000_000))
                .itemOutputs(GTCMItemList.IndistinctTentaclePrototypeMK2.get(1))
                .eut(RECIPE_MAX)
                .duration(20 * 3600 * 24)
                .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
        }

        // Mass Fabricator Genesis
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MassFabricatorUMV.get(1),
            16_384_000,
            8192,
            (int) RECIPE_UMV,
            64,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 64),
                GregtechItemList.Industrial_MassFab.get(64),
                CustomItemList.Machine_Multi_Transformer.get(64),
                GTCMItemList.StabilisationFieldGeneratorUEV.get(64),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.SpaceTime, 64),

                ItemList.Field_Generator_UMV.get(64),
                ItemList.Field_Generator_UMV.get(64),
                ItemList.Emitter_UMV.get(64),
                ItemList.Emitter_UMV.get(64),

                GregtechItemList.Laser_Lens_Special.get(64),
                GregtechItemList.Laser_Lens_Special.get(64),
                GregtechItemList.Laser_Lens_Special.get(64),
                GregtechItemList.Laser_Lens_Special.get(64)},
            new FluidStack[] {
                MaterialsUEVplus.SpaceTime.getMolten(144 * 1024),
                MaterialsUEVplus.Space.getMolten(144 * 256),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(1000 * 1024),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000 * 16)},
            GTCMItemList.MassFabricatorGenesis.get(1),
            20 * 3600,
            (int) RECIPE_UMV);

        // spotless:on
    }
}
