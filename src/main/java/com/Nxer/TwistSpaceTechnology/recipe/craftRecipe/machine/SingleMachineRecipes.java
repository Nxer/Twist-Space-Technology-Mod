package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static bartworks.common.loaders.ItemRegistry.cal;
import static bartworks.common.loaders.ItemRegistry.megaMachines;
import static bartworks.common.loaders.ItemRegistry.voidminer;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.CoolingCore;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_IV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Controller_IndustrialRockBreaker;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_CuttingFactoryController;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_MacerationStack;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Mega_AlloyBlastSmelter;
import static kekztech.common.Blocks.lscLapotronicEnergyUnit;
import static kubatech.api.enums.ItemList.ExtremeIndustrialApiary;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.gtnewhorizons.gtnhintergalactic.item.IGItems;
import com.gtnewhorizons.gtnhintergalactic.item.ItemMiningDrones;

import appeng.api.AEApi;
import appeng.items.materials.MaterialType;
import bartworks.common.loaders.BioItemList;
import bartworks.system.material.WerkstoffLoader;
import ggfab.GGItemList;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;
import singulariteam.eternalsingularity.item.EternalSingularityItem;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;
import wanion.avaritiaddons.block.extremeautocrafter.BlockExtremeAutoCrafter;

public class SingleMachineRecipes implements IRecipePool {

    // spotless:off
    @Override
    public void loadRecipes() {

        // Intensify Chemical Distorter
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                copyAmount(1, megaMachines[3]),
                Materials.Carbon.getNanite(16),

                ItemList.Emitter_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))
            .itemOutputs(GTCMItemList.IntensifyChemicalDistorter.get(1))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(assemblerRecipes);

        // Miracle Top
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList. CircuitAssemblerUIV.get(1),
            16_384_000,
            8192,
            (int) RECIPE_UIV,
            64,
            new Object[] {
                copyAmount(64,cal),
                ItemRefer.Component_Assembly_Line.get(64),
                ItemRefer.Compact_Fusion_Coil_T4.get(64),
                CustomItemList.eM_Spacetime.get(64),

                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),

                GTCMItemList.OpticalSOC.get(64),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                GTCMItemList.OpticalSOC.get(64),

                GTCMItemList.SpaceWarper.get(64),
                ItemList.Field_Generator_UIV.get(64),
                MaterialsUEVplus.TranscendentMetal.getNanite(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Infinity, 64) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1024 * 144),
                Materials.SuperconductorUIVBase.getMolten(512 * 144),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(64 * 1000),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000)},
            GTCMItemList.MiracleTop.get(1),
            20 * 3600,
            (int) RECIPE_UMV);

        // Magnetic Drive Pressure Former
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 1),
            512_000,
            256,
            (int) RECIPE_UV,
            64,
            new Object[] {
                ItemList.Casing_MiningOsmiridium.get(64),
                GregtechItemList.Industrial_Extruder.get(64),
                GregtechItemList.Industrial_PlatePress.get(64),
                GregtechItemList.Controller_IndustrialForgeHammer.get(64),

                ItemList.UV_Coil.get(64), GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64), ItemList.UV_Coil.get(64),

                ItemList.Field_Generator_UV.get(8), ItemList.Robot_Arm_UV.get(16), ItemList.Conveyor_Module_UV.get(32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 64),

                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Iridium, 64) },
            new FluidStack[] { new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 256),
                Materials.Terbium.getMolten(144 * 256),
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 1000 * 256),
                Materials.Lubricant.getFluid(1000 * 256) },
            GTCMItemList.MagneticDrivePressureFormer.get(1),
            20 * 256,
            (int) RECIPE_UHV);

        // Physical Form Switcher
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTModHandler.getModItem(Mods.GregTech.ID, "gt.metaitem.99", 1, 397),
            1_024_000,
            512,
            (int) RECIPE_UHV,
            16,
            new Object[] {
                ItemList.Machine_Multi_Solidifier.get(64),
                ItemList.LargeFluidExtractor.get(64),
                ItemList.FluidSolidifierUV.get(64),
                ItemList.FluidExtractorUV.get(64),

                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 64),
                ItemList.Superconducting_Magnet_Solenoid_UHV.get(28),
                GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.NetherStar, 16),
                ItemList.Electric_Pump_UV.get(8),

                ItemList.Field_Generator_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),
                MaterialsAlloy.QUANTUM.getRotor(32),
                MaterialsAlloy.OCTIRON.getRotor(32),

                ItemRefer.Fluid_Storage_Core_T5.get(64),
                Materials.Silver.getNanite(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUV, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                Materials.UUMatter.getFluid(1000 * 512),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 4096),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(144 * 8192) },
            GTCMItemList.PhysicalFormSwitcher.get(1),
            20 * 180,
            (int) RECIPE_UHV);

        // Magnetic Mixer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16),
                GregtechItemList.Industrial_Mixer.get(64),

                ItemList.MixerUV.get(64),
                ItemList.Electric_Motor_UV.get(64),
                ItemList.Field_Generator_UV.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8))
            .fluidInputs(Materials.Iridium.getMolten(144 * 64))
            .itemOutputs(GTCMItemList.MagneticMixer.get(1))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 384)
            .addTo(assemblerRecipes);

        // Infinity Air Intake Hatch
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GregtechItemList.Hatch_Air_Intake_Extreme.get(4),
                ItemList.Electric_Pump_UHV.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 16))
            .fluidInputs(Materials.CosmicNeutronium.getMolten(144 * 16))
            .itemOutputs(GTCMItemList.InfiniteAirHatch.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 30)
            .addTo(assemblerRecipes);

        // Magnetic Domain Constructor
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Machine_Multi_IndustrialElectromagneticSeparator.get(1),
            4_096_000,
            512,
            (int) RECIPE_UEV,
            16,
            new Object[] {
                ItemList.Machine_Multi_IndustrialElectromagneticSeparator.get(64),
                ItemList.ElectromagneticSeparatorUHV.get(16),
                ItemList.PolarizerUHV.get(16),
                ItemList.Electromagnet_Tengam.get(8),

                ItemList.Robot_Arm_UEV.get(16),
                ItemList.Conveyor_Module_UEV.get(8),
                ItemList.Field_Generator_UEV.get(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 16),

                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.BlackPlutonium, 8),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 8) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                GGMaterial.tairitsu.getMolten(144 * 256),
                Materials.TengamAttuned.getMolten(144 * 1024),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(144 * 4096) },
            GTCMItemList.MagneticDomainConstructor.get(1),
            20 * 600,
            (int) RECIPE_UEV);

        // Silksong
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_WireFactory.get(64),
                ItemList.WiremillUV.get(8),

                HighEnergyFlowCircuit.get(8),
                ItemList.Robot_Arm_ZPM.get(16),
                ItemList.Conveyor_Module_ZPM.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 16))
            .fluidInputs(Materials.Iridium.getMolten(144 * 48))
            .itemOutputs(GTCMItemList.Silksong.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20 * 600)
            .addTo(assemblerRecipes);

        // Holy Separator
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Industrial_CuttingFactoryController.get(1),
            1_024_000,
            512,
            (int) RECIPE_UHV,
            16,
            new Object[] {
                GregtechItemList.Industrial_CuttingFactoryController.get(64),
                ItemList.CuttingMachineUHV.get(32),
                ItemList.SlicingMachineUHV.get(32),
                CustomItemList.eM_Power.get(16),

                ItemList.Field_Generator_UHV.get(16), ItemList.Emitter_UHV.get(64), ItemList.Emitter_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(8),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16), ItemList.Circuit_Chip_PPIC.get(64),
                ItemList.Circuit_Chip_PPIC.get(64),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CallistoIce, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Ledox, 64),
                copyAmount(64, Ic2Items.iridiumPlate) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.Naquadria.getMolten(144 * 256), Materials.SuperCoolant.getFluid(1000 * 512),
                Materials.Lubricant.getFluid(1000 * 1024) },
            GTCMItemList.HolySeparator.get(1),
            20 * 1200,
            (int) RECIPE_UHV);

        // Space Scaler
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.SpaceWarper.get(1),
            4_096_000,
            2048,
            (int) RECIPE_UIV,
            16,
            new Object[] {
                ItemList.Machine_Multi_HIPCompressor.get(64),
                ItemList.CompressorUHV.get(64),
                ItemList.Machine_Multi_IndustrialExtractor.get(64),
                ItemList.ExtractorUHV.get(64),

                ItemList.Field_Generator_UEV.get(32),
                ItemList.Field_Generator_UHV.get(64),
                ItemList.Field_Generator_UHV.get(64),
                new ItemStack(EternalSingularityItem.instance,4),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),
                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Oriharukon, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 64) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                Materials.UUMatter.getFluid(1000 * 256),
                Materials.SuperCoolant.getFluid(1000 * 256),
                Materials.NaquadahAlloy.getMolten(144 * 256) },
            GTCMItemList.SpaceScaler.get(1),
            20 * 1200,
            (int) RECIPE_UIV);

        // Molecule Deconstructor
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Materials.Neutronium.getNanite(1),
            1_024_000,
            512,
            (int) RECIPE_UV,
            16,
            new Object[] {
                GregtechItemList.Industrial_Electrolyzer.get(64),
                GregtechItemList.Industrial_Centrifuge.get(64),
                ItemList.ElectrolyzerUV.get(64),
                ItemList.CentrifugeUV.get(64),

                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                Materials.Silver.getNanite(16),
                ItemList.Robot_Arm_UV.get(64),
                ItemList.Electric_Pump_UV.get(32),

                ItemList.Emitter_UV.get(16),
                ItemList.Field_Generator_UV.get(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 64),

                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 64) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144 * 256),
                Materials.UUMatter.getFluid(1000 * 64),
                Materials.SuperCoolant.getFluid(1000 * 128) },
            GTCMItemList.MoleculeDeconstructor.get(1),
            20 * 600,
            (int) RECIPE_UHV);

        // Crystalline Infinitier
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.NuclearStar.get(1),
            2_048_000,
            512,
            (int) RECIPE_UEV,
            16,
            new Object[] {
                ItemList.Machine_Multi_Autoclave.get(64),
                ItemList.AutoclaveUEV.get(64),
                ItemList.ChemicalBathUEV.get(64),
                CustomItemList.eM_Containment_Field.get(4),

                ItemList.Field_Generator_UEV.get(32),
                ItemList.Electric_Pump_UEV.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),

                ItemList.EnergisedTesseract.get(64),
                ItemList.NuclearStar.get(64),
                ItemList.Circuit_Wafer_PPIC.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 64) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                GGMaterial.tairitsu.getMolten(144 * 512),
                Materials.StableBaryonicMatter.getFluid(1000 * 64),
                Materials.UUMatter.getFluid(1000 * 128) },
            GTCMItemList.CrystallineInfinitier.get(1),
            20 * 1800,
            (int) RECIPE_UEV);

        // Miracle Door
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.CriticalPhoton.get(1),
            32_768_000,
            8192,
            (int) RECIPE_UMV,
            64,
            new Object[] {
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),

                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),

                ItemList.Emitter_UMV.get(64),
                ItemList.EnergisedTesseract.get(64),
                ItemList.EnergisedTesseract.get(64),
                ItemList.Emitter_UMV.get(64),

                CustomItemList.eM_Teleportation.get(64),
                GTCMItemList.GravitationalLens.get(64),
                GTCMItemList.GravitationalLens.get(64),
                CustomItemList.eM_Teleportation.get(64)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 65536),
                MaterialsTST.NeutroniumAlloy.getMolten(144 * 65536),
                MaterialsUEVplus.PhononMedium.getFluid(1000 * 16384),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000 * 8192)},
            GTCMItemList.MiracleDoor.get(1),
            20 * 3600,
            (int) RECIPE_UXV);

        // Infinite Dynamo Hatch
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_dynamoTunnel5_UEV.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_dynamoTunnel5_UEV.get(1),
                ItemRefer.Compact_Fusion_Coil_T0.get(1),
                ItemList.Casing_Coil_Superconductor.get(1),
                CustomItemList.Machine_Multi_Transformer.get(1),

                ItemList.Casing_Dim_Injector.get(2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 2),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 4),

                ItemList.EnergisedTesseract.get(4),
                ItemList.Field_Generator_UEV.get(2),
                GTCMItemList.GravitationalLens.get(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 36),
                Materials.UUMatter.getFluid(1000 * 8),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 64))
            .itemOutputs(GTCMItemList.InfiniteWirelessDynamoHatch.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 20)
            .addTo(AssemblyLine);

        // Ore Processing Factory
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Ore_Processor.get(1),
            8_192_000,
            2048,
            (int) RECIPE_UIV,
            16,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                ItemList.Ore_Processor.get(64),
                GTCMItemList.MegaMacerator.get(64),
                ItemList.Electric_Motor_UIV.get(64),

                ItemList.Electric_Piston_UIV.get(16),
                ItemList.Electric_Pump_UIV.get(32),
                ItemList.Conveyor_Module_UIV.get(16),
                ItemList.Robot_Arm_UIV.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.rotor, MaterialsTST.NeutroniumAlloy, 64),

                Materials.Neutronium.getNanite(16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Iridium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.StainlessSteel, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TungstenSteel, 64)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 512),
                Materials.InfinityCatalyst.getMolten(144 * 1024),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(144 * 1024),
                Materials.Bedrockium.getMolten(144 * 1024)},
            GTCMItemList.OreProcessingFactory.get(1),
            20 * 1800,
            (int) RECIPE_UIV);

        // Circuit Converter
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                ItemList.Casing_Processor.get(3),
                ItemList.Machine_IV_Boxinator.get(1),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),

                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 8))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(GTCMItemList.CircuitConverter.get(1))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20 * 30)
            .addTo(assemblerRecipes);

        // Large Industrial Coking Factory
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 16),
                GregtechItemList.Industrial_CokeOven.get(64),

                ItemList.PyrolyseOven.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),
                HighEnergyFlowCircuit.get(16),

                ItemList.Electric_Pump_UV.get(6),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.BlackSteel, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 8))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 96))
            .itemOutputs(GTCMItemList.LargeIndustrialCokingFactory.get(1))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 128)
            .addTo(assemblerRecipes);

        // Mega Bricked Blast Furnace
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Machine_Bricked_BlastFurnace.get(64),
                ItemList.Machine_Bricked_BlastFurnace.get(64),

                ItemList.Machine_Bricked_BlastFurnace.get(64),
                ItemList.Machine_Bricked_BlastFurnace.get(64),
                ItemList.Casing_Firebricks.get(64),

                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 16))
            .itemOutputs(GTCMItemList.MegaBrickedBlastFurnace.get(1))
            .noOptimize()
            .eut(RECIPE_LV)
            .duration(514)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                ItemList.Machine_Bricked_BlastFurnace.get(64),
                ItemList.Machine_Bricked_BlastFurnace.get(64),

                ItemList.Casing_Firebricks.get(64),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Steel, 1),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 2),

                GTOreDictUnificator.get(OrePrefixes.rod, Materials.Steel, 4),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Steel, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4))
            .itemOutputs(GTCMItemList.MegaBrickedBlastFurnace.get(1))
            .noOptimize()
            .eut(RECIPE_LV)
            .duration(114)
            .addTo(assemblerRecipes);

        // Scavenger
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_Sifter.get(64),
                ItemList.SiftingMachineZPM.get(16),

                GregtechItemList.Casing_SifterGrate.get(64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 9),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 16),

                copyAmount(36, Ic2Items.iridiumPlate),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 8))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(GTCMItemList.Scavenger.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20 * 60)
            .addTo(assemblerRecipes);

        // Biosphere III
        ItemStack bioVat = GTModHandler
            .getModItem("gregtech", "gt.blockmahines", 1, 12600 + GTValues.VN.length * 7, ItemList.BreweryUHV.get(1));
        // todo ConfigHandler.IDOffset has been removed

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            bioVat.copy(),
            2_048_000,
            1024,
            (int) RECIPE_UHV,
            16,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 64),
                copyAmount(64, bioVat),
                ItemList.Machine_Multi_IndustrialBrewery.get(64),
                ItemList.FermenterUHV.get(8),

                copyAmount(64, BioItemList.getPetriDish(null)),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Infinity, 3),

                ItemList.Electric_Pump_UHV.get(16),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polybenzimidazole, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 8),
                HighEnergyFlowCircuit.get(16),

                ItemList.Circuit_Silicon_Wafer7.get(64),
                ItemList.Circuit_Parts_Chip_Bioware.get(64),
                copyAmount(64, Ic2Items.iridiumPlate),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.UV, 16)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                Materials.Naquadria.getMolten(144 * 256),
                Materials.CosmicNeutronium.getMolten(144 * 64),
                GGMaterial.metastableOganesson.getMolten(144 * 32)},
            GTCMItemList.BiosphereIII.get(1),
            20 * 900,
            (int) RECIPE_UEV);

        // region Mega Egg Generator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MagicEnergyAbsorber_LV.get(4),
                ItemList.MagicEnergyAbsorber_MV.get(4),
                ItemList.MagicEnergyAbsorber_HV.get(4),
                ItemList.MagicEnergyAbsorber_EV.get(4),
                ItemList.MagicEnergyConverter_LV.get(4),
                ItemList.MagicEnergyConverter_MV.get(4),
                ItemList.MagicEnergyConverter_HV.get(4),
                Materials.Thaumium.getPlates(16),
                GTCMItemList.VoidPollen.get(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(16 * 1000))
            .itemOutputs(GTCMItemList.MegaEggGenerator.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(120 * 20)
            .addTo(assemblerRecipes);

        // Advanced Oil Cracker
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                megaMachines[4],
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 16),
                ItemList.Electric_Pump_IV.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))
            .itemOutputs(GTCMItemList.AdvancedMegaOilCracker.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 3600)
            .addTo(assemblerRecipes);

        // Indistinct Tentacle
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GGItemList.AdvAssLine.get(1),
            2_000_000_000,
            65536,
            (int) RECIPE_UXV,
            64,
            new Object[] {
                GGItemList.AdvAssLine.get(64),
                ItemRefer.Component_Assembly_Line.get(64),
                ItemList.AssemblingMachineUMV.get(64),
                ItemRefer.Precise_Assembler.get(64),

                CustomItemList.eM_Dimensional.get(64),
                new ItemStack(lscLapotronicEnergyUnit, 64, 10),
                GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                GTCMItemList.StabilisationFieldGeneratorUMV.get(64),

                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 17),
                GTCMItemList.DysonSphereFrameComponent.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UXV,64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.MagMatter,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsTST.Axonium,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.Eternity,64)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8388608),
                MaterialsUEVplus.Universium.getMolten(144 * 524288),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(1000 * 1048576),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000 * 2097152)
            },
            GTCMItemList.IndistinctTentacle.get(1),
            20 * 115200,
            (int) RECIPE_UXV);

        // Thermal Energy Devourer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            megaMachines[1],
            1_024_000,
            512,
            (int) RECIPE_UHV,
            8,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 64),
                copyAmount(64, megaMachines[1]),
                ItemList.Field_Generator_UHV.get(16),
                ItemList.Electric_Pump_UHV.get(64),

                MaterialType.Singularity.stack(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 16),

                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.DraconiumAwakened, 16),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getRotor(16),

                copyAmount(64, CoolingCore.getLeft()),
                copyAmount(64, CoolingCore.getLeft()),
                copyAmount(64, CoolingCore.getLeft()),
                copyAmount(64, CoolingCore.getLeft())
            },
            new FluidStack[] {
                new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 64),
                Materials.NaquadahAlloy.getMolten(144 * 256),
                Materials.UUMatter.getFluid(1000 * 64),
                Materials.SuperCoolant.getFluid(1000 * 512)
            },
            GTCMItemList.ThermalEnergyDevourer.get(1),
            20 * 900,
            (int) RECIPE_UEV);

        // Debug uncertainty hatch
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                CustomItemList.UncertaintyX_Hatch.get(1),
                CustomItemList.hatch_CreativeMaintenance.get(1),
                ItemList.Tool_DataOrb.get(1))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128))
            .itemOutputs(GTCMItemList.DebugUncertaintyHatch.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 120)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                CustomItemList.UncertaintyX_Hatch.get(1),
                CustomItemList.hatch_CreativeMaintenance.get(1),
                ItemList.Tool_DataOrb.get(1),
                ItemList.Timepiece.get(1))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128))
            .itemOutputs(GTCMItemList.DebugUncertaintyHatch.get(16))
            .eut(RECIPE_UXV)
            .duration(20 * 120)
            .addTo(assemblerRecipes);

        // Laser Smart Node
        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.LASERpipe.get(64),
                Laser_Lens_Special.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1),
                ItemList.Hatch_Energy_UHV.get(1))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144))
            .itemOutputs(GTCMItemList.LaserSmartNode.get(1))
            .eut(RECIPE_UHV)
            .duration(20 * 5)
            .addTo(assemblerRecipes);

        // VacuumFilterExtractor
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.DistilleryUHV.get(1),
            1_024_000,
            512,
            (int) RECIPE_UHV,
            8,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 64),
                copyAmount(64, megaMachines[2]),
                GregtechItemList.Machine_Adv_DistillationTower.get(64),
                Materials.Carbon.getNanite(64),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),
                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 16),
                ItemList.Field_Generator_UHV.get(8),

                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.BlackPlutonium, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Polybenzimidazole, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.BlackSteel, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 16)
            },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                Materials.RadoxPolymer.getMolten(144 * 32),
                MaterialsTST.NeutroniumAlloy.getMolten(144 * 64),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(144 * 128)
            },
            GTCMItemList.VacuumFilterExtractor.get(1),
            20 * 900,
            (int) RECIPE_UEV);

        // Mega Macerator
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                Industrial_MacerationStack.get(64),
                ItemList.MaceratorZPM.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 16),
                GregtechItemList.Maceration_Upgrade_Chip.get(64),
                WerkstoffLoader.AdemicSteel.get(OrePrefixes.gearGt, 16),

                ItemList.Electric_Motor_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 8),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.plateDense, 16))
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 64))
            .itemOutputs(GTCMItemList.MegaMacerator.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20 * 300)
            .addTo(assemblerRecipes);

        // Hephaestus' Atelier
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.ElectricFurnaceUHV.get(1),
            1_024_000,
            512,
            (int) RECIPE_UHV,
            8,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenCarbide, 64),
                ItemList.Machine_Multi_Furnace.get(64),
                ItemList.Casing_HeatProof.get(64),
                CustomItemList.eM_Hollow.get(64),
                new ItemStack[] { ItemList.Casing_Coil_AwakenedDraconium.get(64), ItemList.Casing_Coil_Infinity.get(16),
                    ItemList.Casing_Coil_Hypogen.get(4), ItemList.Casing_Coil_Eternal.get(1), },

                ItemList.Field_Generator_UHV.get(16),
                ItemList.Emitter_UHV.get(32),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Conveyor_Module_UHV.get(64),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 32),
                HighEnergyFlowCircuit.get(64),
                HighEnergyFlowCircuit.get(64),
                MaterialType.Cell16384kPart.stack(64),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16),
                GGMaterial.incoloy903.get(OrePrefixes.gearGt,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Indium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Invar, 64)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                MaterialsAlloy.ABYSSAL.getFluidStack(144 * 96),
                Materials.UUMatter.getFluid(1000 * 64),
                Materials.SuperCoolant.getFluid(1000 * 64)},
            GTCMItemList.HephaestusAtelier.get(1),
            20 * 600,
            (int) RECIPE_UEV);

        // Deployed Nano Core
        if (Config.Enable_DeployedNanoCore) {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.NanoForge.get(1),
                2_000_000_000,
                65536,
                (int) RECIPE_UXV,
                64,
                new Object[] {
                    ItemList.NanoForge.get(64),
                    ItemList.NanoForge.get(64),
                    ItemList.NanoForge.get(64),
                    ItemList.NanoForge.get(64),

                    GTCMItemList.SpaceScaler.get(64),
                    new ItemStack(lscLapotronicEnergyUnit, 64, 9),
                    GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                    GTCMItemList.StabilisationFieldGeneratorUIV.get(64),

                    GTCMItemList.MatterRecombinator.get(64),
                    MaterialsTST.Axonium.getNanite(64),
                    MaterialsUEVplus.MagMatter.getNanite(64),
                    MaterialsUEVplus.Eternity.getNanite(64),

                    GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UXV,64),
                    GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.Universium,64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.SpaceTime,64)
                },
                new FluidStack[] {
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 256),
                    MaterialsUEVplus.PhononMedium.getFluid(1000 * 288),
                    MaterialsUEVplus.Eternity.getMolten(144 * 8192),
                    MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000 * 2048)
                },
                GTCMItemList.DeployedNanoCore.get(1),
                20 * 172800,
                (int) RECIPE_MAX);
        }

        // Core Device of Human Power Generation Facility
        if (Config.Enable_CoreDeviceOfHumanPowerGenerationFacility) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    ItemList.FluidHeaterUV.get(64),
                    ItemList.Electric_Pump_UV.get(64),

                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 32),
                    HighEnergyFlowCircuit.get(32),

                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 64))
                .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64))
                .itemOutputs(GTCMItemList.CoreDeviceOfHumanPowerGenerationFacility.get(1))
                .eut(RECIPE_UV)
                .duration(20 * 900)
                .addTo(assemblerRecipes);
        }

        // Starcore Miner
        if (Config.Enable_StarcoreMiner) {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Infinity, 1),
                4_096_000,
                2048,
                (int) RECIPE_UEV,
                16,
                new Object[] {
                    copyAmount(64, voidminer[2]),
                    copyAmount(64, voidminer[2]),
                    copyAmount(64, voidminer[2]),
                    copyAmount(64, voidminer[2]),

                    ItemList.EnergisedTesseract.get(64),
                    new ItemStack(IGItems.MiningDrones, 18, ItemMiningDrones.DroneTiers.UEV.ordinal()),
                    GTCMItemList.SpaceWarper.get(18),
                    GTCMItemList.PerfectEngravedLaptronChip.get(8),

                    ItemList.Field_Generator_UEV.get(32),
                    ItemList.Sensor_UEV.get(48),
                    ItemList.Electric_Motor_UEV.get(64),
                    ItemList.Conveyor_Module_UEV.get(64),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bedrockium, 64)},
                new FluidStack[] {
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 1024),
                    Materials.Quantium.getMolten(144 * 1024),
                    GGMaterial.metastableOganesson.getMolten(144 * 512),
                    Materials.UUMatter.getFluid(1000 * 2048)},
                GTCMItemList.StarcoreMiner.get(1),
                20 * 7200,
                (int) RECIPE_UIV);
        }

        // TST Disassembler
        if (Config.Enable_Disassembler) {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Machine_LV_Assembler.get(1),
                512_000,
                256,
                (int) RECIPE_UV,
                4,
                new Object[] {
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                    CustomItemList.eM_Containment.get(64),
                    ItemList.AssemblingMachineUV.get(16),
                    ItemList.Machine_UV_CircuitAssembler.get(16),

                    ItemList.Robot_Arm_UV.get(64),
                    ItemList.Robot_Arm_UV.get(64),
                    ItemList.Robot_Arm_UV.get(64),
                    ItemList.Robot_Arm_UV.get(64),

                    ItemList.Electric_Pump_UV.get(64),
                    ItemList.Electric_Pump_UV.get(64),
                    ItemList.Conveyor_Module_UV.get(64),
                    ItemList.Conveyor_Module_UV.get(64),

                    Materials.Carbon.getNanite(64),
                    Materials.Carbon.getNanite(64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 64)},
                new FluidStack[] {
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                    Materials.Infinity.getMolten(144 * 4),
                    Materials.Praseodymium.getMolten(144 * 128),
                    Materials.UUMatter.getFluid(1000 * 128)},
                GTCMItemList.Disassembler.get(1),
                20 * 1800,
                (int) RECIPE_UHV);
        }

        // Space Apiary Module Recipes
        if (Config.EnableSpaceApiaryModule) {

            // t1
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ExtremeIndustrialApiary.get(16),
                    HighEnergyFlowCircuit.get(16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),

                    ItemList.Field_Generator_UHV.get(16),
                    ItemList.Conveyor_Module_UHV.get(16),
                    ItemList.Robot_Arm_UHV.get(16),
                    ItemList.Electric_Pump_UHV.get(16),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                    Materials.Naquadah.getMolten(144 * 64),
                    Materials.UUMatter.getFluid(1000 * 128),
                    Materials.Honey.getFluid(1000 * 256))
                .itemOutputs(GTCMItemList.SpaceApiaryT1.get(1))
                .specialValue(1)
                .eut(RECIPE_UHV)
                .duration(20 * 300)
                .addTo(spaceAssemblerRecipes);

            // t2
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    GTCMItemList.SpaceApiaryT1.get(16),
                    HighEnergyFlowCircuit.get(16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 16),

                    ItemList.Field_Generator_UEV.get(16),
                    ItemList.Conveyor_Module_UEV.get(16),
                    ItemList.Robot_Arm_UEV.get(16),
                    ItemList.Electric_Pump_UEV.get(16),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                    Materials.EnrichedNaquadria.getMolten(144 * 64),
                    Materials.UUMatter.getFluid(1000 * 256),
                    Materials.Honey.getFluid(1000 * 512))
                .itemOutputs(GTCMItemList.SpaceApiaryT2.get(1))
                .specialValue(1)
                .eut(RECIPE_UEV)
                .duration(20 * 600)
                .addTo(spaceAssemblerRecipes);

            // t3
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    GTCMItemList.SpaceApiaryT2.get(16),
                    HighEnergyFlowCircuit.get(16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 16),

                    ItemList.Field_Generator_UIV.get(16),
                    ItemList.Conveyor_Module_UIV.get(16),
                    ItemList.Robot_Arm_UIV.get(16),
                    ItemList.Electric_Pump_UIV.get(16),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                    Materials.Naquadria.getMolten(144 * 64),
                    Materials.UUMatter.getFluid(1000 * 512),
                    Materials.Honey.getFluid(1000 * 1024))
                .itemOutputs(GTCMItemList.SpaceApiaryT3.get(1))
                .specialValue(2)
                .eut(RECIPE_UIV)
                .duration(20 * 1200)
                .addTo(spaceAssemblerRecipes);

            // t4
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    GTCMItemList.SpaceApiaryT3.get(16),
                    HighEnergyFlowCircuit.get(16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 16),

                    ItemList.Field_Generator_UMV.get(16),
                    ItemList.Conveyor_Module_UMV.get(16),
                    ItemList.Robot_Arm_UMV.get(16),
                    ItemList.Electric_Pump_UMV.get(16),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64))
                .fluidInputs(
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                    Materials.Naquadria.getMolten(144 * 64),
                    Materials.UUMatter.getFluid(1000 * 512),
                    Materials.Honey.getFluid(1000 * 1024))
                .itemOutputs(GTCMItemList.SpaceApiaryT4.get(1))
                .specialValue(2)
                .eut(RECIPE_UMV)
                .duration(20 * 2400)
                .addTo(spaceAssemblerRecipes);

        }

        // Bee Engineer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 64),
                GTModHandler.getModItem(Forestry.ID, "alveary", 64, 0),

                ItemList.Field_Generator_LuV.get(4),
                ItemList.Electric_Pump_LuV.get(16),
                ItemList.Conveyor_Module_LuV.get(16),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Plutonium241, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16))
            .fluidInputs(Materials.Honey.getFluid(1000 * 256))
            .itemOutputs(GTCMItemList.BeeEngineer.get(1))
            .eut(RECIPE_ZPM)
            .duration(20 * 300)
            .addTo(assemblerRecipes);

        // Large Canner
        if (Config.Enable_LargeCanner) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 16),
                    ItemList.FluidCannerZPM.get(16),

                    ItemList.CanningMachineZPM.get(16),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),
                    ItemList.Electric_Pump_ZPM.get(16),

                    ItemList.Conveyor_Module_ZPM.get(8),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 16),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 16))
                .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 64))
                .itemOutputs(GTCMItemList.LargeCanner.get(1))
                .eut(RECIPE_UV)
                .duration(20 * 300)
                .addTo(assemblerRecipes);
        }

        // Extreme Craft Center
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockExtremeAutoCrafter.instance),
            8_192_000,
            16384,
            (int) RECIPE_UIV,
            16,
            new Object[] {
                AEApi.instance()
                    .definitions()
                    .blocks()
                    .molecularAssembler()
                    .maybeStack(64)
                    .orNull(),
                AEApi.instance()
                    .definitions()
                    .blocks()
                    .molecularAssembler()
                    .maybeStack(64)
                    .orNull(),
                AEApi.instance()
                    .definitions()
                    .blocks()
                    .molecularAssembler()
                    .maybeStack(64)
                    .orNull(),
                AEApi.instance()
                    .definitions()
                    .blocks()
                    .molecularAssembler()
                    .maybeStack(64)
                    .orNull(),

                new ItemStack(BlockExtremeAutoCrafter.instance,64),
                new ItemStack(BlockExtremeAutoCrafter.instance,64),
                new ItemStack(BlockExtremeAutoCrafter.instance,64),
                new ItemStack(BlockExtremeAutoCrafter.instance,64),

                GregtechItemList.Controller_MolecularTransformer.get(4),
                ItemRefer.HiC_T5.get(16),
                ItemList.Field_Generator_UIV.get(4),
                GTCMItemList.WirelessUpdateItem.get(4)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 64),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(40),
                Materials.Hydrogen.getPlasma(1000 * 4000)},
            GTCMItemList.ExtremeCraftCenter.get(1),
            20 * 1800,
            (int) RECIPE_UMV);

        // Lightning Spire
        if (Config.Enable_LightningSpire) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    CI.getTieredGTPPMachineCasing(5, 1),
                    CI.getEnergyCore(4, 4),

                    CI.getTransmissionComponent(5, 2),
                    ItemList.Field_Generator_HV.get(4),
                    MaterialsAlloy.NITINOL_60.getGear(4),

                    MaterialsElements.getInstance().GERMANIUM.getBolt(16),
                    MaterialsAlloy.NICHROME.getFineWire(16),
                    MaterialsAlloy.NICHROME.getCable16(1))
                .fluidInputs(Materials.Silicone.getMolten(2304))
                .itemOutputs(GTCMItemList.LightningSpire.get(1))
                .eut(RECIPE_IV)
                .duration(20 * 120)
                .addTo(assemblerRecipes);
            }

        // Mega Stone Breaker
        ItemStack ExtraUtilitiesNodeUpgrade2 = Mods.ExtraUtilities.isModLoaded()
            ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "nodeUpgrade", 64, 2)
            : new ItemStack(Items.iron_pickaxe);
        ItemStack CompressCobblestone8 = Mods.ExtraUtilities.isModLoaded()
            ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 64, 7)
            : new ItemStack(Blocks.cobblestone);

        if (Config.Enable_MegaStoneBreaker) {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                Controller_IndustrialRockBreaker.get(1),
                512_000,
                256,
                (int) RECIPE_UV,
                4,
                new Object[] {
                    ItemList.Hull_UEV.get(4),
                    ItemList.RockBreakerZPM.get(16),
                    Controller_IndustrialRockBreaker.get(64),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 64),

                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bedrockium, 1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1),

                    ExtraUtilitiesNodeUpgrade2,
                    ExtraUtilitiesNodeUpgrade2,
                    ExtraUtilitiesNodeUpgrade2,
                    ExtraUtilitiesNodeUpgrade2,

                    CompressCobblestone8,
                    CompressCobblestone8,
                    CompressCobblestone8,
                    CompressCobblestone8},
                new FluidStack[] {
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                    GGMaterial.adamantiumAlloy.getMolten(144 * 64),
                    Materials.Water.getFluid(2_000_000_000),
                    Materials.Lava.getFluid(2_000_000_000)},
                GTCMItemList.MegaStoneBreaker.get(1),
                20 * 300,
                (int) RECIPE_UV);
        }
    }
    // spotless:on
}
