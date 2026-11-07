package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler;
import com.Nxer.TwistSpaceTechnology.common.material.BECCondensates;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.dreammaster.item.NHItemList;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TTRecipeAdder;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;

/** Field containment and feedback, with previous-tier component materials as superdense shells. */
public final class StabilisationFieldGeneratorRecipes {

    // spotless:off
    public static void loadRecipes() {
        // Framework: an accessible ZPM foundation, four blocks per batch.
        if (Mods.GraviSuite.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 4),
                    ModItemHandler.GraviSuite.GravitationEngine.get(16),
                    ItemList.Field_Generator_ZPM.get(2),
                    ItemList.Sensor_ZPM.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.NiobiumTitanium, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Trinium, 6))
                .circuit(10)
                .fluidInputs(Materials.Polybenzimidazole.getMolten(2304))
                .itemOutputs(GTCMItemList.StabilisationFieldGeneratorFramework.get(4))
                .duration(20 * 30)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(RecipeMaps.assemblerRecipes);
        }

        if (!Mods.NewHorizonsCoreMod.isModLoaded()) return;

        // Tiered slots: previous block, casing, photon upgrade, shielding;
        // field generator, field control, feedback, particle confinement;
        // circuit, optical/spatial component, nanites, power IC; four shell materials.
        // Stabilisation Field Generator UEV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.StabilisationFieldGeneratorFramework.get(1),
            32_768_000,
            2048,
            (int) TierEU.RECIPE_UEV,
            8,
            new Object[] {
                GTCMItemList.StabilisationFieldGeneratorFramework.get(1),
                CustomItemList.eM_Containment.get(16),
                GTCMItemList.PhotonControllerUpgradeUEV.get(2),
                NHItemList.IrradiantReinforcedBedrockiumPlate.get(16),

                ItemList.Field_Generator_UEV.get(16),
                ItemList.Emitter_UEV.get(16),
                ItemList.Sensor_UEV.get(16),
                ItemList.NuclearStar.get(16),

                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 32 },
                GregtechItemList.Laser_Lens_Special.get(32),
                Materials.Neutronium.getNanite(32),
                ItemList.Circuit_Chip_FPIC.get(64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bedrockium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 64),
                MaterialsAlloy.QUANTUM.getPlateSuperdense(64),
            },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                Materials.StableBaryonicMatter.getFluid(32_000),
                GGMaterial.tairitsu.getMolten(144 * 256),
                Materials.UUMatter.getFluid(200_000)},
            GTCMItemList.StabilisationFieldGeneratorUEV.get(1),
            20 * 60 * 4,
            (int) TierEU.RECIPE_UEV);

        // UIV: concentrated UU provides a small production-line entry requirement.
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.StabilisationFieldGeneratorUEV.get(1),
            131_072_000,
            4096,
            (int) TierEU.RECIPE_UIV,
            16,
            new Object[] {
                GTCMItemList.StabilisationFieldGeneratorUEV.get(1),
                CustomItemList.eM_Containment_Advanced.get(16),
                GTCMItemList.PhotonControllerUpgradeUIV.get(2),
                ItemList.MetaMaterial_Shielding1.get(16),

                ItemList.Field_Generator_UIV.get(32),
                GTCMItemList.SpaceWarper.get(8),
                ItemList.Optically_Perfected_CPU.get(32),
                ItemList.StableBosonContainmentUnit.get(32),

                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 32 },
                GTCMItemList.CriticalPhoton.get(64),
                Materials.TranscendentMetal.getNanite(32),
                ItemList.Circuit_Chip_APIC.get(64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Tritanium, 64),
                MaterialsElements.STANDALONE.HYPOGEN.getPlateSuperdense(64),
            },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 384),
                Materials.DimensionallyShiftedSuperfluid.getFluid(512_000),
                GGMaterial.metastableOganesson.getMolten(144 * 1024),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(100)},
            GTCMItemList.StabilisationFieldGeneratorUIV.get(1),
            20 * 60 * 16,
            (int) TierEU.RECIPE_UIV);

        // Stabilisation Field Generator UMV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.StabilisationFieldGeneratorUIV.get(1),
                CustomItemList.eM_Ultimate_Containment.get(16),
                GTCMItemList.PhotonControllerUpgradeUMV.get(2),
                ItemList.MetaMaterial_Shielding2.get(32),

                ItemList.Field_Generator_UMV.get(64),
                ItemList.MetaMaterial_FieldManipulator3.get(64),
                GTCMItemList.EnergyFluctuationSelfHarmonizer.get(64),
                GTCMItemList.ParticleTrapTimeSpaceShield.get(32),

                NHItemList.CircuitUXV.get(32),
                GTCMItemList.SpaceWarper.get(32),
                Materials.BlackDwarfMatter.getNanite(64),
                ItemList.Circuit_Chip_ZPIC.get(64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, 64),
                MaterialsElements.STANDALONE.RHUGNOR.getPlateSuperdense(64),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlateSuperdense(64),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlateSuperdense(64))
            .metadata(
                NANITE_TIERS,
                new NaniteTier[] {
                    NaniteTier.WhiteDwarf, NaniteTier.SixPhasedCopper, NaniteTier.BlackDwarf, NaniteTier.BlackDwarf,

                    NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf, NaniteTier.BlackDwarf, NaniteTier.BlackDwarf,

                    NaniteTier.BlackDwarf, NaniteTier.SixPhasedCopper, NaniteTier.BlackDwarf, NaniteTier.SixPhasedCopper,

                    NaniteTier.Transcendent, NaniteTier.SixPhasedCopper, NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf })
            .fluidInputs(
                CondensateType.BoundlessCosmicSolder.getEntangled(2_048_000),
                CondensateType.Infinity.getEntangled(1_728_000),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(8_192_000),
                BECCondensates.ConcentratedUUMatter.getEntangled(200_000))
            .itemOutputs(GTCMItemList.StabilisationFieldGeneratorUMV.get(1))
            .duration(20 * 60 * 64)
            .eut(TierEU.RECIPE_UMV)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);

        // Stabilisation Field Generator UXV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.StabilisationFieldGeneratorUMV.get(1),
                CustomItemList.eM_Ultimate_Containment_Advanced.get(16),
                GTCMItemList.PhotonControllerUpgradeUXV.get(2),
                ItemList.MetaMaterial_Shielding3.get(64),

                ItemList.Field_Generator_UXV.get(64),
                ItemList.MetaMaterial_FieldManipulator4.get(64),
                ItemList.MetaMaterial_SensorArray4.get(64),
                ItemList.MetaMaterial_ElectrograviticValve3.get(64),

                NHItemList.CircuitMAX.get(32),
                GTCMItemList.AnnihilationConstrainer.get(64),
                Materials.Universium.getNanite(64),
                ItemList.Circuit_Chip_YPIC.get(64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.MHDCSM, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.MagMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsTST.Axonium, 64))
            .metadata(
                NANITE_TIERS,
                new NaniteTier[] {
                    NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.Universium, NaniteTier.MagMatter,

                    NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.Eternity, NaniteTier.MagMatter,

                    NaniteTier.MagMatter, NaniteTier.MagMatter, NaniteTier.Universium, NaniteTier.BlackDwarf,

                    NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.Eternity, NaniteTier.BlackDwarf })
            .fluidInputs(
                CondensateType.BoundlessCosmicSolder.getEntangled(4_096_000),
                CondensateType.QuarkGluonPlasma.getEntangled(1_024_000),
                CondensateType.PhononMedium.getEntangled(16_384_000),
                BECCondensates.ConcentratedUUMatter.getEntangled(400_000_000))
            .itemOutputs(GTCMItemList.StabilisationFieldGeneratorUXV.get(1))
            // 64 minutes with 64 matching nanites, in addition to upstream BEC processing.
            .duration(20 * 60 * 4096)
            .eut(TierEU.RECIPE_UXV)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);
    }
    // spotless:on
}
