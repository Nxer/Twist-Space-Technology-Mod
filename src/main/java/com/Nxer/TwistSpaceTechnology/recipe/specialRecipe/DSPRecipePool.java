package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AnnihilationConstrainer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Antimatter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ArtificialStar;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DSPLauncher;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DSPReceiver;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DysonSphereFrameComponent;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.EmptySmallLaunchVehicle;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ParticleTrapTimeSpaceShield;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SmallLaunchVehicle;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SolarSail;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUEveryAntimatter;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUEveryAntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUTOfLaunchingNode;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUTOfLaunchingSolarSail;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.ticksOfLaunchingNode;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.ticksOfLaunchingSolarSail;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.github.technus.tectech.thing.CustomItemList.eM_Coil;
import static com.github.technus.tectech.thing.CustomItemList.eM_Containment;
import static com.github.technus.tectech.thing.CustomItemList.eM_Hollow;
import static com.github.technus.tectech.thing.CustomItemList.eM_Power;
import static com.github.technus.tectech.thing.CustomItemList.eM_Spacetime;
import static com.github.technus.tectech.thing.CustomItemList.eM_Ultimate_Containment;
import static com.github.technus.tectech.thing.CustomItemList.eM_Ultimate_Containment_Advanced;
import static com.github.technus.tectech.thing.CustomItemList.eM_Ultimate_Containment_Field;
import static de.katzenpapst.amunra.item.ARItems.jetItemIon;
import static de.katzenpapst.amunra.item.ARItems.lightPlating;
import static de.katzenpapst.amunra.item.ARItems.noseCone;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UXV;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.ASTRAL_TITANIUM;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.HYPOGEN;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.CustomItemList;
import com.dreammaster.gthandler.GT_CoreModSupport;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps;

import galaxyspace.core.register.GSBlocks;
import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.Particle;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;

public class DSPRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap DSPLauncherRecipe = GTCMRecipe.DSP_LauncherRecipes;
        final IRecipeMap SpaceAssembler = IGRecipeMaps.spaceAssemblerRecipes;
        final IRecipeMap Assembler = RecipeMaps.assemblerRecipes;
        final Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        // DSP Ray Receiving Station
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, DSPLauncher.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                ItemList.ZPM3.get(1),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                CustomItemList.HighEnergyFlowCircuit.get(64),
                CustomItemList.PikoCircuit.get(64),

                ItemList.Sensor_UEV.get(64),
                ItemList.Sensor_UEV.get(64),
                ItemList.Emitter_UEV.get(64),
                ItemList.Emitter_UEV.get(64),

                StellarConstructionFrameMaterial.get(64),
                ItemList.Field_Generator_UEV.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64),
                Laser_Lens_Special.get(64),

                SpaceWarper.get(64),
                new ItemStack(IGBlocks.SpaceElevatorCasing, 64),
                new ItemStack(GSBlocks.DysonSwarmBlocks, 64, 9))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 8192),
                Materials.UUMatter.getFluid(1000 * 1024),
                FluidRegistry.getFluidStack("cryotheum", 1000 * 8192),
                Materials.CosmicNeutronium.getMolten(144 * 1024))
            .itemOutputs(DSPReceiver.get(1))

            .eut(RECIPE_UIV)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // DSP Launch Site machine
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(GCBlocks.landingPad, 1, 0))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                new ItemStack(GCBlocks.landingPad, 64),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                ItemList.Field_Generator_UEV.get(64),
                CustomItemList.PikoCircuit.get(64),

                ItemList.Electric_Motor_UEV.get(64),
                ItemList.Electric_Piston_UEV.get(64),
                ItemList.Robot_Arm_UEV.get(64),
                ItemList.Sensor_UEV.get(64),

                StellarConstructionFrameMaterial.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 16),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 16),

                lightPlating.getItemStack(64),
                eM_Power.get(64),
                new ItemStack(IGBlocks.SpaceElevatorCasing, 64),
                new ItemStack(GSBlocks.DysonSwarmBlocks, 64, 9))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 8192),
                Materials.UUMatter.getFluid(1000 * 128),
                Materials.SuperCoolant.getFluid(1000 * 1024),
                Materials.CosmicNeutronium.getMolten(144 * 1024))
            .itemOutputs(DSPLauncher.get(1))

            .eut(RECIPE_UIV)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // eM_Ultimate_Containment_Advanced casing 13
        GT_Values.RA.stdBuilder()
            .itemInputs(
                eM_Ultimate_Containment.get(64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64),

                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64),
                ItemList.Tesseract.get(16),
                ItemList.EnergisedTesseract.get(16),

                ItemList.Field_Generator_UMV.get(8),
                copyAmount(64, Particle.getBaseParticle(Particle.HIGGS_BOSON)))
            .fluidInputs(MyMaterial.metastableOganesson.getMolten(144 * 256))
            .itemOutputs(eM_Ultimate_Containment_Advanced.get(8))

            .noOptimize()
            .eut(RECIPE_UXV)
            .duration(20 * 300)
            .addTo(Assembler);

        // eM_Ultimate_Containment casing 12
        GT_Values.RA.stdBuilder()
            .itemInputs(
                eM_Containment.get(1),
                StellarConstructionFrameMaterial.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 16),

                HYPOGEN.getFoil(64),
                CELESTIAL_TUNGSTEN.getScrew(64),
                CELESTIAL_TUNGSTEN.getRing(64),

                ItemList.Field_Generator_UIV.get(1))
            .fluidInputs(MyMaterial.preciousMetalAlloy.getMolten(144 * 64))
            .itemOutputs(eM_Ultimate_Containment.get(1))

            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 300)
            .addTo(Assembler);

        // ArtificialStar
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, AnnihilationConstrainer.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                StellarConstructionFrameMaterial.get(64),
                eM_Spacetime.get(64),
                eM_Ultimate_Containment_Field.get(64),
                ItemList.Casing_Dim_Bridge.get(64),

                eM_Ultimate_Containment_Advanced.get(64),
                eM_Coil.get(64),
                eM_Hollow.get(64),
                ItemList.Electric_Pump_UMV.get(64),

                AnnihilationConstrainer.get(64),
                ItemList.Field_Generator_UMV.get(64),
                CustomItemList.QuantumCircuit.get(64),
                CustomItemList.PikoCircuit.get(64),

                CustomItemList.HighEnergyFlowCircuit.get(64),
                ItemList.Tesseract.get(64),
                ItemList.EnergisedTesseract.get(64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 64))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 8192),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 8192),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 8192),
                Materials.SuperconductorUMVBase.getMolten(144 * 8192))
            .itemOutputs(ArtificialStar.get(1))

            .eut(RECIPE_UXV)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // launcher
        GT_Values.RA.stdBuilder()
            .itemInputs(SolarSail.get(1))

            .itemOutputs(SolarSail.get(1))

            .eut(EUTOfLaunchingSolarSail)
            .duration(ticksOfLaunchingSolarSail)
            .addTo(DSPLauncherRecipe);

        GT_Values.RA.stdBuilder()
            .itemInputs(SmallLaunchVehicle.get(1))

            .itemOutputs(
                SmallLaunchVehicle.get(1)
                    .setStackDisplayName(
                        texter("99%% Return an Empty Small Launch Vehicle.", "NEI.EmptySmallLaunchVehicleRecipe.0")))

            .eut(EUTOfLaunchingNode)
            .duration(ticksOfLaunchingNode)
            .addTo(DSPLauncherRecipe);

        // receiver fake recipe
        GT_Values.RA.stdBuilder()

            .itemOutputs(CriticalPhoton.get(1))

            .specialValue(Integer.MAX_VALUE)
            .eut(0)
            .duration(0)
            .addTo(GTCMRecipe.DSP_ReceiverRecipes);

        // inversion
        GT_Values.RA.stdBuilder()
            .itemInputs(CriticalPhoton.get(2))

            .itemOutputs(Antimatter.get(1))
            .fluidOutputs(Materials.Hydrogen.getPlasma(1000))
            .eut(16000)
            .duration(20 * 64 * 8)
            .addTo(GTCMRecipe.QuantumInversionRecipes);

        // Artificial Star Generating
        // spotless:off
        GT_Values.RA.stdBuilder()
            .itemInputs(AntimatterFuelRod.get(1))

            .itemOutputs(StellarConstructionFrameMaterial.get(3).setStackDisplayName(texter("Chance to recover some raw materials. Probability is affected by module tier.","NEI.AntimatterFuelRodGeneratingRecipe.01")))

            .specialValue((int) (EUEveryAntimatterFuelRod / Integer.MAX_VALUE))
            .eut(0)
            .duration(0)
            .addTo(GTCMRecipe.ArtificialStarGeneratingRecipes);
        // spotless:on

        GT_Values.RA.stdBuilder()
            .itemInputs(Antimatter.get(1))

            .specialValue((int) (EUEveryAntimatter / Integer.MAX_VALUE))
            .eut(0)
            .duration(0)
            .addTo(GTCMRecipe.ArtificialStarGeneratingRecipes);

        // Stellar Construction Frame Material
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(21),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                lightPlating.getItemStack(6),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.BlackPlutonium, 16),
                Materials.Neutronium.getNanite(1))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 16),
                ASTRAL_TITANIUM.getFluidStack(144 * 18),
                CELESTIAL_TUNGSTEN.getFluidStack(144 * 18))
            .itemOutputs(StellarConstructionFrameMaterial.get(1))

            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 128)
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                lightPlating.getItemStack(9),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 8),
                Materials.Neutronium.getNanite(1))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 16),
                ASTRAL_TITANIUM.getFluidStack(144 * 18),
                CELESTIAL_TUNGSTEN.getFluidStack(144 * 18))
            .itemOutputs(StellarConstructionFrameMaterial.get(4))

            .noOptimize()
            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 64)
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(23),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 48),
                lightPlating.getItemStack(36),
                ItemList.EnergisedTesseract.get(12),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 12),
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
            .addTo(SpaceAssembler);

        // Lightweight Plate
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsKevlar.Kevlar, 32),
                Materials.Carbon.getNanite(4),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.BlackPlutonium, 64))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 4),
                GT_CoreModSupport.RadoxPolymer.getMolten(144 * 4),
                Materials.Neutronium.getMolten(144 * 2))
            .itemOutputs(lightPlating.getItemStack(1))

            .noOptimize()
            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 24)
            .addTo(SpaceAssembler);

        // Empty Small Launch Vehicle
        GT_Values.RA.stdBuilder()
            .itemInputs(
                noseCone.getItemStack(1),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 16),
                SpaceWarper.get(16),
                CustomItemList.PikoCircuit.get(32),

                StellarConstructionFrameMaterial.get(32),
                lightPlating.getItemStack(32),
                jetItemIon.getItemStack(4))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 4),
                GT_CoreModSupport.RadoxPolymer.getMolten(144 * 4),
                Materials.Infinity.getMolten(144 * 128),
                Materials.UUMatter.getFluid(1000 * 256))
            .itemOutputs(EmptySmallLaunchVehicle.get(1))

            .noOptimize()
            .specialValue(2)
            .eut(RECIPE_UEV)
            .duration(20 * 1200)
            .addTo(SpaceAssembler);

        // Small Launch Vehicle
        GT_Values.RA.stdBuilder()
            .itemInputs(
                EmptySmallLaunchVehicle.get(1),
                SpaceWarper.get(1),
                DysonSphereFrameComponent.get(24),
                copyAmount(16, Particle.getBaseParticle(Particle.GRAVITON)),
                CustomItemList.QuantumCircuit.get(1))
            .fluidInputs(Materials.Hydrogen.getPlasma(1000 * 16))
            .itemOutputs(SmallLaunchVehicle.get(1))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 600)
            .addTo(Assembler);

        // Dyson Sphere Frame Component
        GT_Values.RA.stdBuilder()
            .itemInputs(
                StellarConstructionFrameMaterial.get(18),
                SolarSail.get(18),
                ItemList.Circuit_OpticalMainframe.get(18),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 6),

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
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                StellarConstructionFrameMaterial.get(18),
                SolarSail.get(18),
                CustomItemList.PikoCircuit.get(15),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 6),

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
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                StellarConstructionFrameMaterial.get(18),
                SolarSail.get(18),
                CustomItemList.QuantumCircuit.get(12),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 6),

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
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                StellarConstructionFrameMaterial.get(18),
                SolarSail.get(18),
                CustomItemList.QuantumCircuit.get(6),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 6),

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
            .addTo(SpaceAssembler);

        // Solar Sail
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.HighEnergyFlowCircuit.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 16),
                ItemList.Circuit_Silicon_Wafer7.get(16),
                ItemList.Emitter_UEV.get(16))
            .fluidInputs(
                Materials.SiliconSG.getMolten(144 * 1024),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 64))
            .itemOutputs(SolarSail.get(4))

            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 600)
            .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_OpticalMainframe.get(24),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 32),
                ItemList.Circuit_Silicon_Wafer7.get(12),
                ItemList.Emitter_UIV.get(12))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 2048), MaterialsUEVplus.SpaceTime.getMolten(144 * 48))
            .itemOutputs(SolarSail.get(8))

            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 900)
            .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.PikoCircuit.get(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                ItemList.Circuit_Silicon_Wafer7.get(8),
                ItemList.Emitter_UMV.get(8))
            .fluidInputs(
                Materials.SiliconSG.getMolten(144 * 4096),
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 16),
                MaterialsUEVplus.Eternity.getMolten(144 * 16))
            .itemOutputs(SolarSail.get(16))

            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 1500)
            .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.QuantumCircuit.get(8),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                ItemList.Circuit_Silicon_Wafer7.get(6),
                ItemList.Emitter_UXV.get(2))
            .fluidInputs(
                Materials.SiliconSG.getMolten(144 * 8192),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 8),
                MaterialsUEVplus.Universium.getMolten(144 * 8))
            .itemOutputs(SolarSail.get(64))

            .noOptimize()
            .eut(RECIPE_UXV)
            .duration(20 * 1200)
            .addTo(GTCMRecipe.CrystallineInfinitierRecipes);

        // Annihilation Constrainer
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(21),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1),
                SpaceWarper.get(3),
                ParticleTrapTimeSpaceShield.get(8),

                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                CustomItemList.QuantumCircuit.get(1),
                ItemList.EnergisedTesseract.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.TranscendentMetal, 16))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 16), MaterialsUEVplus.TranscendentMetal.getMolten(144 * 16))
            .itemOutputs(AnnihilationConstrainer.get(1))

            .specialValue(1)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1),
                SpaceWarper.get(8),
                ParticleTrapTimeSpaceShield.get(8),

                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                CustomItemList.QuantumCircuit.get(1),
                ItemList.EnergisedTesseract.get(1),
                MyMaterial.shirabon.get(OrePrefixes.foil, 64))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 16),
                MaterialsUEVplus.RawStarMatter.getFluid(144 * 16))
            .itemOutputs(AnnihilationConstrainer.get(8))

            .specialValue(2)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(23),
                copyAmount(0, MaterialsUEVplus.Universium.getNanite(1)),
                copyAmount(0, MaterialsUEVplus.Eternity.getNanite(1)),
                GT_OreDictUnificator
                    .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1),

                SpaceWarper.get(4),
                ParticleTrapTimeSpaceShield.get(8),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                CustomItemList.QuantumCircuit.get(1),

                ItemList.EnergisedTesseract.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 64))
            .fluidInputs(
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 16),
                MaterialsUEVplus.Eternity.getMolten(144 * 16))
            .itemOutputs(AnnihilationConstrainer.get(64))

            .specialValue(3)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

        // Antimatter Fuel Rod
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(21),
                AnnihilationConstrainer.get(1),
                Antimatter.get(32),
                ItemList.Tesseract.get(1),
                StellarConstructionFrameMaterial.get(6),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64))
            .fluidInputs(Materials.Hydrogen.getPlasma(1000 * 32))
            .itemOutputs(AntimatterFuelRod.get(2))

            .specialValue(2)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                AnnihilationConstrainer.get(1),
                Antimatter.get(64),
                ItemList.Tesseract.get(1),
                StellarConstructionFrameMaterial.get(24),
                MyMaterial.shirabon.get(OrePrefixes.foil, 16))
            .fluidInputs(
                Materials.Hydrogen.getPlasma(1000 * 64),
                MaterialsUEVplus.Space.getMolten(144 * 4),
                MaterialsUEVplus.Time.getMolten(144 * 4))
            .itemOutputs(AntimatterFuelRod.get(8))

            .specialValue(2)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(23),
                AnnihilationConstrainer.get(1),
                Antimatter.get(64),
                Antimatter.get(64),
                ItemList.Timepiece.get(1),
                StellarConstructionFrameMaterial.get(64),
                StellarConstructionFrameMaterial.get(64),
                StellarConstructionFrameMaterial.get(64),
                GT_OreDictUnificator
                    .get(OrePrefixes.foil, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 6))
            .fluidInputs(Materials.Hydrogen.getPlasma(1000 * 128))
            .itemOutputs(AntimatterFuelRod.get(64))

            .specialValue(3)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

        // Gravitational Lens
        GT_Values.RA.stdBuilder()
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

    }
}
