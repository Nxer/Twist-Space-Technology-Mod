package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AnnihilationConstrainer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Antimatter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DysonSphereFrameComponent;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.EmptySmallLaunchVehicle;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ParticleTrapTimeSpaceShield;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SmallLaunchVehicle;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SolarSail;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StrangeAnnihilationFuelRod;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.IonThrusterJet;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.LightWeightPlate;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.ShuttleNoseCone;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.ArtificialStarGeneratingRecipes;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.CrystallineInfinitierRecipes;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.DSP_LauncherRecipes;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.DSP_ReceiverRecipes;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MiracleTopRecipes;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.QuantumInversionRecipes;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.StrangeMatterAggregatorRecipes;
import static com.Nxer.TwistSpaceTechnology.config.Config.EUEveryStrangeAnnihilationFuelRod;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUEveryAntimatter;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUEveryAntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUTOfLaunchingNode;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUTOfLaunchingSolarSail;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.ticksOfLaunchingNode;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.ticksOfLaunchingSolarSail;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.Utils.addStringToStackName;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;
import static gregtech.api.enums.Materials.RadoxPolymer;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UXV;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.ASTRAL_TITANIUM;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.HYPOGEN;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.Particle;

public class DSPRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // launcher
        GTValues.RA.stdBuilder()
            .itemInputs(SolarSail.get(1))

            .itemOutputs(SolarSail.get(1))

            .eut(EUTOfLaunchingSolarSail)
            .duration(ticksOfLaunchingSolarSail)
            .addTo(DSP_LauncherRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(SmallLaunchVehicle.get(1))

            .itemOutputs(
                SmallLaunchVehicle.get(1)
                    .setStackDisplayName(
                        texter("99%% Return an Empty Small Launch Vehicle.", "NEI.EmptySmallLaunchVehicleRecipe.0")))

            .eut(EUTOfLaunchingNode)
            .duration(ticksOfLaunchingNode)
            .addTo(DSP_LauncherRecipes);

        // receiver fake recipe
        GTValues.RA.stdBuilder()

            .itemOutputs(CriticalPhoton.get(1))

            .specialValue(Integer.MAX_VALUE)
            .eut(0)
            .duration(0)
            .addTo(DSP_ReceiverRecipes);

        // inversion
        GTValues.RA.stdBuilder()
            .itemInputs(CriticalPhoton.get(2))

            .itemOutputs(Antimatter.get(1))
            .fluidOutputs(Materials.Hydrogen.getPlasma(1000))
            .eut(16000)
            .duration(20 * 64 * 8)
            .addTo(QuantumInversionRecipes);

        // Artificial Star Generating
        // spotless:off
        GTValues.RA.stdBuilder()
            .itemInputs(AntimatterFuelRod.get(1))
            .itemOutputs(StellarConstructionFrameMaterial.get(1).setStackDisplayName(texter("Chance to recover some raw materials. Probability is affected by module tier.","NEI.AntimatterFuelRodGeneratingRecipe.01")))
            .specialValue((int) (EUEveryAntimatterFuelRod / Integer.MAX_VALUE))
            .eut(0)
            .duration(0)
            .addTo(ArtificialStarGeneratingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(StrangeAnnihilationFuelRod.get(1))
            .itemOutputs(StellarConstructionFrameMaterial.get(1).setStackDisplayName(texter("Chance to recover some raw materials. Probability is affected by module tier.","NEI.AntimatterFuelRodGeneratingRecipe.01")))
            .specialValue((int) (EUEveryStrangeAnnihilationFuelRod / Integer.MAX_VALUE))
            .eut(0)
            .duration(0)
            .addTo(ArtificialStarGeneratingRecipes);
        // spotless:on

        GTValues.RA.stdBuilder()
            .itemInputs(Antimatter.get(1))

            .specialValue((int) (EUEveryAntimatter / Integer.MAX_VALUE))
            .eut(0)
            .duration(0)
            .addTo(ArtificialStarGeneratingRecipes);

        // Stellar Construction Frame Material
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                setStackSize(LightWeightPlate.getLeft(), 6),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.BlackPlutonium, 16),
                Materials.Neutronium.getNanite(1))
            .fluidInputs(
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 16),
                ASTRAL_TITANIUM.getFluidStack(144 * 18),
                CELESTIAL_TUNGSTEN.getFluidStack(144 * 18))
            .itemOutputs(StellarConstructionFrameMaterial.get(1))

            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 128)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                setStackSize(LightWeightPlate.getLeft(), 9),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 8),
                Materials.Neutronium.getNanite(1))
            .fluidInputs(
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 16),
                ASTRAL_TITANIUM.getFluidStack(144 * 18),
                CELESTIAL_TUNGSTEN.getFluidStack(144 * 18))
            .itemOutputs(StellarConstructionFrameMaterial.get(4))

            .noOptimize()
            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 64)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(23),
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 48),
                setStackSize(LightWeightPlate.getLeft(), 36),
                ItemList.EnergisedTesseract.get(12),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 12),
                Materials.Neutronium.getNanite(1))
            .fluidInputs(
                MaterialsUEVplus.RawStarMatter.getFluid(144 * 32),
                ASTRAL_TITANIUM.getFluidStack(144 * 72),
                CELESTIAL_TUNGSTEN.getFluidStack(144 * 72))
            .itemOutputs(StellarConstructionFrameMaterial.get(32))

            .noOptimize()
            .specialValue(3)
            .eut(RECIPE_UEV)
            .duration(20 * 64)
            .addTo(spaceAssemblerRecipes);

        // Lightweight Plate
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, MaterialsKevlar.Kevlar, 32),
                Materials.Carbon.getNanite(4),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.BlackPlutonium, 64))
            .fluidInputs(
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 4),
                RadoxPolymer.getMolten(144 * 4),
                Materials.Neutronium.getMolten(144 * 2))
            .itemOutputs(setStackSize(LightWeightPlate.getLeft(), 1))

            .noOptimize()
            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 24)
            .addTo(spaceAssemblerRecipes);

        // Empty Small Launch Vehicle
        GTValues.RA.stdBuilder()
            .itemInputs(
                setStackSize(ShuttleNoseCone.getLeft(), 1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 16),
                SpaceWarper.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 32),

                StellarConstructionFrameMaterial.get(32),
                setStackSize(LightWeightPlate.getLeft(), 32),
                setStackSize(IonThrusterJet.getLeft(), 4))
            .fluidInputs(
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 4),
                RadoxPolymer.getMolten(144 * 4),
                Materials.Infinity.getMolten(144 * 128),
                Materials.UUMatter.getFluid(1000 * 256))
            .itemOutputs(EmptySmallLaunchVehicle.get(1))

            .noOptimize()
            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 1200)
            .addTo(spaceAssemblerRecipes);

        // Small Launch Vehicle
        GTValues.RA.stdBuilder()
            .itemInputs(
                EmptySmallLaunchVehicle.get(1),
                SpaceWarper.get(1),
                DysonSphereFrameComponent.get(24),
                copyAmount(16, Particle.getBaseParticle(Particle.GRAVITON)),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1))
            .fluidInputs(Materials.Hydrogen.getPlasma(1000 * 16))
            .itemOutputs(SmallLaunchVehicle.get(1))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 600)
            .addTo(assemblerRecipes);

        // Dyson Sphere Frame Component
        GTValues.RA.stdBuilder()
            .itemInputs(
                StellarConstructionFrameMaterial.get(18),
                SolarSail.get(18),
                ItemList.Circuit_OpticalMainframe.get(18),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 6),

                CustomItemList.HighEnergyFlowCircuit.get(64),
                ItemList.Electric_Motor_UEV.get(64),
                ItemList.Field_Generator_UEV.get(32),
                ItemList.Emitter_UEV.get(32))
            .fluidInputs(MaterialsUEVplus.TranscendentMetal.getMolten(144 * 128))
            .itemOutputs(DysonSphereFrameComponent.get(1))

            .noOptimize()
            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 300)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                StellarConstructionFrameMaterial.get(18),
                SolarSail.get(18),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 15),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 6),

                CustomItemList.HighEnergyFlowCircuit.get(64),
                ItemList.Electric_Motor_UIV.get(32),
                ItemList.Field_Generator_UIV.get(16),
                ItemList.Emitter_UIV.get(16))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 128))
            .itemOutputs(DysonSphereFrameComponent.get(3))

            .noOptimize()
            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 600)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                StellarConstructionFrameMaterial.get(18),
                SolarSail.get(18),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 12),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 6),

                CustomItemList.HighEnergyFlowCircuit.get(64),
                ItemList.Electric_Motor_UMV.get(16),
                ItemList.Field_Generator_UMV.get(8),
                ItemList.Emitter_UMV.get(8))
            .fluidInputs(
                MaterialsUEVplus.Eternity.getMolten(144 * 128),
                MaterialsUEVplus.Space.getMolten(144 * 64),
                MaterialsUEVplus.Time.getMolten(144 * 64),
                MaterialsUEVplus.RawStarMatter.getFluid(144 * 32))
            .itemOutputs(DysonSphereFrameComponent.get(9))

            .noOptimize()
            .specialValue(3)
            .eut(RECIPE_UEV)
            .duration(20 * 300)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                StellarConstructionFrameMaterial.get(18),
                SolarSail.get(18),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 6),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 6),

                CustomItemList.HighEnergyFlowCircuit.get(64),
                ItemList.Electric_Motor_UXV.get(8),
                ItemList.Field_Generator_UXV.get(4),
                ItemList.Emitter_UXV.get(4))
            .fluidInputs(
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 32),
                MaterialsUEVplus.Universium.getMolten(144 * 128))
            .itemOutputs(DysonSphereFrameComponent.get(27))

            .noOptimize()
            .specialValue(3)
            .eut(RECIPE_UEV)
            .duration(20 * 300)
            .addTo(spaceAssemblerRecipes);

        // Solar Sail
        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 16),
                ItemList.Circuit_Silicon_Wafer7.get(16),
                ItemList.Emitter_UEV.get(16))
            .fluidInputs(
                Materials.SiliconSG.getMolten(144 * 1024),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 64))
            .itemOutputs(SolarSail.get(3))

            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 600)
            .addTo(CrystallineInfinitierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_OpticalMainframe.get(24),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 32),
                ItemList.Circuit_Silicon_Wafer7.get(12),
                ItemList.Emitter_UIV.get(12))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 2048), MaterialsUEVplus.SpaceTime.getMolten(144 * 48))
            .itemOutputs(SolarSail.get(9))

            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 900)
            .addTo(CrystallineInfinitierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                ItemList.Circuit_Silicon_Wafer7.get(8),
                ItemList.Emitter_UMV.get(8))
            .fluidInputs(
                Materials.SiliconSG.getMolten(144 * 4096),
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 16),
                MaterialsUEVplus.Eternity.getMolten(144 * 16))
            .itemOutputs(SolarSail.get(27))

            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 1500)
            .addTo(CrystallineInfinitierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                ItemList.Circuit_Silicon_Wafer7.get(6),
                ItemList.Emitter_UXV.get(2))
            .fluidInputs(
                Materials.SiliconSG.getMolten(144 * 8192),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 8),
                MaterialsUEVplus.Universium.getMolten(144 * 8))
            .itemOutputs(SolarSail.get(128))

            .noOptimize()
            .eut(RECIPE_UXV)
            .duration(20 * 1200)
            .addTo(CrystallineInfinitierRecipes);

        // Annihilation Constrainer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1),
                SpaceWarper.get(3),
                ParticleTrapTimeSpaceShield.get(8),

                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1),
                ItemList.EnergisedTesseract.get(1),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.TranscendentMetal, 16))
            .fluidInputs(
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 16),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 16))
            .itemOutputs(AnnihilationConstrainer.get(1))

            .specialValue(1)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1),
                SpaceWarper.get(8),
                ParticleTrapTimeSpaceShield.get(8),

                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1),
                ItemList.EnergisedTesseract.get(1),
                GGMaterial.shirabon.get(OrePrefixes.foil, 64))
            .fluidInputs(
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 16),
                MaterialsUEVplus.RawStarMatter.getFluid(144 * 16))
            .itemOutputs(AnnihilationConstrainer.get(8))

            .specialValue(2)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(23),
                copyAmount(0, MaterialsUEVplus.Universium.getNanite(1)),
                copyAmount(0, MaterialsUEVplus.Eternity.getNanite(1)),
                GTOreDictUnificator
                    .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1),

                SpaceWarper.get(4),
                ParticleTrapTimeSpaceShield.get(8),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1),

                ItemList.EnergisedTesseract.get(1),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 64))
            .fluidInputs(
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 16),
                MaterialsUEVplus.Eternity.getMolten(144 * 16))
            .itemOutputs(AnnihilationConstrainer.get(64))
            .specialValue(3)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(spaceAssemblerRecipes);

        // Advanced Annihilation Constrainer recipe
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.MatterRecombinator.get(0),
                SpaceWarper.get(4),
                ParticleTrapTimeSpaceShield.get(64),
                CriticalPhoton.get(1),

                // TODO Quantum circuit
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2),
                ItemList.EnergisedTesseract.get(1))
            .fluidInputs(
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 8),
                MaterialsUEVplus.Universium.getMolten(144 * 64),
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 24),
                MaterialsUEVplus.Eternity.getMolten(144 * 24))
            .itemOutputs(AnnihilationConstrainer.get(128))
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(200_000))
            .eut(RECIPE_MAX)
            .duration(20 * 60)
            .addTo(MiracleTopRecipes);

        // Antimatter Fuel Rod
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                AnnihilationConstrainer.get(1),
                Antimatter.get(32),
                ItemList.Tesseract.get(1),
                StellarConstructionFrameMaterial.get(6),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64))
            .fluidInputs(Materials.Hydrogen.getPlasma(1000 * 32))
            .itemOutputs(AntimatterFuelRod.get(2))
            .specialValue(2)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                AnnihilationConstrainer.get(1),
                Antimatter.get(64),
                ItemList.Tesseract.get(1),
                StellarConstructionFrameMaterial.get(16),
                GGMaterial.shirabon.get(OrePrefixes.foil, 16))
            .fluidInputs(
                Materials.Hydrogen.getPlasma(1000 * 64),
                MaterialsUEVplus.Space.getMolten(144 * 4),
                MaterialsUEVplus.Time.getMolten(144 * 4))
            .itemOutputs(AntimatterFuelRod.get(8))
            .specialValue(2)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(spaceAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(23),
                AnnihilationConstrainer.get(1),
                Antimatter.get(64),
                Antimatter.get(64),
                ItemList.Timepiece.get(1),
                StellarConstructionFrameMaterial.get(64),
                GTOreDictUnificator
                    .get(OrePrefixes.foil, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 6))
            .fluidInputs(Materials.Hydrogen.getPlasma(1000 * 128))
            .itemOutputs(AntimatterFuelRod.get(64))
            .specialValue(3)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(spaceAssemblerRecipes);

        // Gravitational Lens
        GTValues.RA.stdBuilder()
            .itemInputs(
                SpaceWarper.get(1),
                Laser_Lens_Special.get(4),
                copyAmount(64, Particle.getBaseParticle(Particle.GRAVITON)),
                copyAmount(64, Particle.getBaseParticle(Particle.GRAVITON)),
                copyAmount(64, Particle.getBaseParticle(Particle.GRAVITON)),
                copyAmount(64, Particle.getBaseParticle(Particle.GRAVITON)))
            .fluidInputs(Materials.MysteriousCrystal.getMolten(144 * 9 * 64 * 2))
            .itemOutputs(
                GravitationalLens.get(1),
                GravitationalLens.get(1),
                GravitationalLens.get(1),
                GravitationalLens.get(1),
                GravitationalLens.get(1))
            .outputChances(10000, 9000, 8000, 7000, 6000)
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 1200)
            .addTo(GTPPRecipeMaps.cyclotronRecipes);

        // region Strange Matter Aggregation

        // spotless:off
        {

            GTValues.RA.stdBuilder()
                .itemInputs(
                    // first slot is the general input , amount is set by machine internal parameters
                    // #tr StrangeMatterAggregation.RecipeDescription.firstSlot
                    // # basic material, input from general input bus, actual amount is set by machine internal parameters
                    // #zh_CN 基础材料, 从通用输入总线输入, 实际数量与机器内部参数有关
                    addStringToStackName(
                        Antimatter.get(256),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.firstSlot")),
                    // second slot is the right input bus input and consumption rate set by structure
                    // #tr StrangeMatterAggregation.RecipeDescription.secondSlot
                    // # input from the right input bus and consumption rate set by structure
                    // #zh_CN 由右侧输入总线输入, 消耗率与结构有关
                    addStringToStackName(
                        AnnihilationConstrainer.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.secondSlot")),
                    // third slot is only consume one time per process
                    // #tr StrangeMatterAggregation.RecipeDescription.thirdSlot
                    // # auxiliary material, input from general input bus, only consume 1 per parallel
                    // #zh_CN 辅助材料, 从通用输入总线输入, 每并行只消耗1个
                    addStringToStackName(
                        ItemList.Tesseract.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.thirdSlot")),
                    // fourth slot is consume same amount with output, can be saved by high tier structure
                    // #tr StrangeMatterAggregation.RecipeDescription.fourthSlot
                    // # auxiliary material, input from general input bus, consumed amount same as output amount, affected by structure
                    // #zh_CN 辅助材料, 从通用输入总线输入, 消耗量等于产物数量, 受结构等级影响
                    addStringToStackName(
                        StellarConstructionFrameMaterial.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.fourthSlot")))
                .fluidInputs(
                    // general input , amount is set by machine internal parameters
                    Materials.Hydrogen.getPlasma(256 * 1000),
                    // T1 maintenance fluid with basic amount
                    MaterialsUEVplus.SpaceTime.getMolten(576))
                .itemOutputs(
                    // first output is T1 output
                    // #tr StrangeMatterAggregation.RecipeDescription.output1
                    // # T1 production
                    // #zh_CN 1级产物
                    addStringToStackName(
                        AntimatterFuelRod.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.output1")),
                    // second output is T2 output, when input special item then turn to output this instead of T1 output
                    // #tr StrangeMatterAggregation.RecipeDescription.output2
                    // # T2 production
                    // #zh_CN 2级产物
                    addStringToStackName(
                        StrangeAnnihilationFuelRod.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.output2")))
                .fluidOutputs(
                    // here is T1 byproduct
                    Materials.Infinity.getMolten(Config.ByproductBaseAmount_T1_StrangeMatterAggregator),
                    HYPOGEN.getFluidStack(Config.ByproductBaseAmount_T1_StrangeMatterAggregator))
                // #tr StrangeMatterAggregation.RecipeDescription.specialSlot
                // # input from the right input bus, upgrades a portion of the product to T2 product, same ratio as the annihilation constrainer
                // #zh_CN 由右侧输入总线输入, 将一部分产物升级为2级产物, 比率与湮灭约束器相同
                .special(
                    addStringToStackName(
                        GTCMItemList.CoreElement.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.specialSlot")))
                // machine will always use a fixed power, adjusted by structure
                .eut(RECIPE_MAX)
                .duration(20 * 120)
                .addTo(StrangeMatterAggregatorRecipes);
            // spotless:on

            GTValues.RA.stdBuilder()
                .itemInputs(
                    // first slot is the general input , amount is set by machine internal parameters
                    addStringToStackName(
                        Antimatter.get(256),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.firstSlot")),
                    // second slot is the right input bus input and consumption rate set by structure
                    addStringToStackName(
                        AnnihilationConstrainer.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.secondSlot")),
                    // third slot is only consume one time per process
                    addStringToStackName(
                        ItemList.Tesseract.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.thirdSlot")),
                    // fourth slot is consume same amount with output, can be saved by high tier structure
                    addStringToStackName(
                        StellarConstructionFrameMaterial.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.fourthSlot")))
                .fluidInputs(
                    // general input , amount is set by machine internal parameters
                    Materials.Hydrogen.getPlasma(256 * 1000),
                    // T2 maintenance fluid with basic amount
                    MaterialsUEVplus.Universium.getMolten(96))
                .itemOutputs(
                    // first output is T1 output
                    addStringToStackName(
                        AntimatterFuelRod.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.output1")),
                    // second output is T2 output, when input special item then turn to output this instead of T1 output
                    addStringToStackName(
                        StrangeAnnihilationFuelRod.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.output2")))
                .fluidOutputs(
                    // here is T2 byproduct
                    MaterialsUEVplus.SpaceTime.getMolten(Config.ByproductBaseAmount_T2_StrangeMatterAggregator),
                    GGMaterial.shirabon.getMolten(Config.ByproductBaseAmount_T2_StrangeMatterAggregator))
                .special(
                    addStringToStackName(
                        GTCMItemList.CoreElement.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.specialSlot")))
                // machine will always use a fixed power, adjusted by structure
                .eut(RECIPE_MAX)
                .duration(20 * 120)
                .addTo(StrangeMatterAggregatorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    // first slot is the general input , amount is set by machine internal parameters
                    addStringToStackName(
                        Antimatter.get(256),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.firstSlot")),
                    // second slot is the right input bus input and consumption rate set by structure
                    addStringToStackName(
                        AnnihilationConstrainer.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.secondSlot")),
                    // third slot is only consume one time per process
                    addStringToStackName(
                        ItemList.Tesseract.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.thirdSlot")),
                    // fourth slot is consume same amount with output, can be saved by high tier structure
                    StellarConstructionFrameMaterial.get(1))
                .fluidInputs(
                    // general input , amount is set by machine internal parameters
                    Materials.Hydrogen.getPlasma(256 * 1000),
                    // T3 maintenance fluid with basic amount
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(16))
                .itemOutputs(
                    // first output is T1 output
                    addStringToStackName(
                        AntimatterFuelRod.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.output1")),
                    // second output is T2 output, when input special item then turn to output this instead of T1 output
                    addStringToStackName(
                        StrangeAnnihilationFuelRod.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.output2")))
                .fluidOutputs(
                    // here is T3 byproduct
                    MaterialsUEVplus.Universium.getMolten(Config.ByproductBaseAmount_T3_StrangeMatterAggregator))
                .special(
                    addStringToStackName(
                        GTCMItemList.CoreElement.get(1),
                        "// " + TextEnums.tr("StrangeMatterAggregation.RecipeDescription.specialSlot")))
                // machine will always use a fixed power, adjusted by structure
                .eut(RECIPE_MAX)
                .duration(20 * 120)
                .addTo(StrangeMatterAggregatorRecipes);

            // Core Element
            TST_RecipeBuilder.builder()
                .itemInputs(GTCMItemList.MatterRecombinator.get(0), ItemList.ZPM.get(1))
                .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(1))
                .itemOutputs(GTCMItemList.EnergyShard.get(1), GTCMItemList.CoreElement.get(1))
                .outputChances(9990, 10)
                .eut(RECIPE_MAX)
                .duration(20 * 60)
                .addTo(QuantumInversionRecipes);

            // Matter Recombinator
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(11),
                    ItemList.Field_Generator_UMV.get(4),
                    ItemList.Emitter_UMV.get(8),

                    GTCMItemList.GravitationalLens.get(32),
                    Laser_Lens_Special.get(16),
                    Materials.Void.getPlates(64),

                    MaterialsUEVplus.WhiteDwarfMatter.getNanite(1),
                    MaterialsUEVplus.BlackDwarfMatter.getNanite(1))
                .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(100))
                .itemOutputs(GTCMItemList.MatterRecombinator.get(1))
                .outputChances(114)
                .eut(RECIPE_UMV)
                .duration(20 * 200)
                .addTo(RecipeMaps.assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(11),
                    ItemList.Field_Generator_UMV.get(4),
                    ItemList.Emitter_UMV.get(8),

                    GTCMItemList.GravitationalLens.get(32),
                    Laser_Lens_Special.get(16),
                    Materials.Void.getPlates(64),

                    MaterialsUEVplus.Eternity.getNanite(1),
                    MaterialsUEVplus.Universium.getNanite(1))
                .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(100))
                .itemOutputs(GTCMItemList.MatterRecombinator.get(1))
                .outputChances(1140)
                .eut(RECIPE_UXV)
                .duration(20 * 300)
                .addTo(RecipeMaps.assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(11),
                    ItemList.Field_Generator_UMV.get(4),
                    ItemList.Emitter_UMV.get(8),

                    GTCMItemList.GravitationalLens.get(32),
                    Laser_Lens_Special.get(16),
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getPlates(16),

                    MaterialsUEVplus.Eternity.getNanite(1),
                    MaterialsUEVplus.Universium.getNanite(1))
                .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(100))
                .itemOutputs(GTCMItemList.MatterRecombinator.get(1))
                .outputChances(5140)
                .eut(RECIPE_MAX)
                .duration(20 * 300)
                .addTo(RecipeMaps.assemblerRecipes);

            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTCMItemList.MatterRecombinator.get(0),
                    ItemList.Field_Generator_UMV.get(1),
                    ItemList.Emitter_UMV.get(2),
                    GTCMItemList.EnergyShard.get(16),

                    GTCMItemList.GravitationalLens.get(6),
                    Laser_Lens_Special.get(6),
                    Materials.Void.getPlates(64))
                .fluidInputs(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 8))
                .itemOutputs(GTCMItemList.MatterRecombinator.get(1))
                .eut(RECIPE_MAX)
                .duration(20 * 300)
                .addTo(MiracleTopRecipes);

            // endregion
        }

    }
}
