package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static gregtech.api.util.GTModHandler.addShapelessCraftingRecipe;
import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.dreammaster.item.NHItemList;

import bartworks.system.material.WerkstoffLoader;
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
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhintergalactic.recipe.IGRecipeMaps;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;

/** Crafting recipes for Bottled Chaos, its credential carriers and dedicated components. */
public final class BottledChaosCraftingRecipes {

    public static void loadRecipes() {
        if (Config.EnableModularizedMachineSystem) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Tool_DataStick.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1 },
                    ItemList.Circuit_Chip_Ram.get(4),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 2))
                .circuit(1)
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(144))
                .itemOutputs(GTCMItemList.BottledChaosEarlyAccess.get(1))
                .duration(20 * 30)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(RecipeMaps.assemblerRecipes);

            // Controller: UV assembly, reachable with two ZPM energy hatches.
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, getModItem(Mods.AppliedEnergistics2.ID, "tile.BlockCraftingMonitor", 1))
                .metadata(SCANNING, new Scanning(20 * 120, TierEU.RECIPE_ZPM))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 8),
                    ItemList.Machine_Multi_Assemblyline.get(1),
                    ItemList.Quantum_Chest_MV.get(4),
                    getModItem(Mods.AppliedEnergistics2.ID, "tile.BlockCraftingUnit", 8, 3), // 16x co-processors.

                    getModItem(Mods.AppliedEnergistics2.ID, "tile.BlockWirelessHub", 4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(16),
                    ItemList.Tool_DataOrb.get(64),

                    ItemList.Field_Generator_ZPM.get(2),
                    ItemList.Emitter_ZPM.get(4),
                    ItemList.Sensor_ZPM.get(4),
                    ItemList.Robot_Arm_ZPM.get(4),

                    ItemList.Conveyor_Module_ZPM.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 16),
                    MaterialsAlloy.TANTALLOY_61.getBolt(32),
                    MaterialsAlloy.ZERON_100.getPlateDense(8))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(9216),
                    Materials.Polybenzimidazole.getMolten(4608),
                    Materials.SuperCoolant.getFluid(32000),
                    Materials.UUMatter.getFluid(8000))
                .itemOutputs(GTCMItemList.BottledChaos.get(1))
                .duration(20 * 1800)
                .eut(TierEU.RECIPE_UV)
                .requireMods(Mods.AppliedEnergistics2)
                .addTo(GTRecipeConstants.AssemblyLine);

            // Credential hatch: data access, persistent storage and local input control.
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Hatch_DataAccess_LuV.get(1))
                .metadata(SCANNING, new Scanning(20 * 30, TierEU.RECIPE_ZPM))
                .itemInputs(
                    ItemList.Hatch_Input_Bus_ZPM.get(1),
                    ItemList.Hatch_DataAccess_LuV.get(1),
                    getModItem(Mods.AE2FluidCraft.ID, "fluid_interface", 4),
                    getModItem(Mods.AppliedEnergistics2.ID, "tile.BlockWirelessConnector", 1),

                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    getModItem(Mods.OpenComputers.ID, "item", 8, 72), // Component buses, tier 3.
                    getModItem(Mods.OpenComputers.ID, "item", 32, 28), // Control units.
                    getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiMaterial", 16, 58), // 1024k storage
                                                                                               // components.

                    ItemList.Tool_DataOrb.get(32),
                    ItemList.Circuit_Chip_Ram.get(64),
                    ItemList.Circuit_Chip_NAND.get(64),
                    ItemList.Circuit_Chip_NOR.get(64),

                    ItemList.Sensor_ZPM.get(2),
                    ItemList.Robot_Arm_ZPM.get(2),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmium, 64),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 16))
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(2304), Materials.RubberSilicone.getMolten(2304))
                .itemOutputs(GTCMItemList.BottledChaosRecipeTokenHatch.get(1))
                .duration(20 * 40)
                .eut(TierEU.RECIPE_ZPM)
                .requireMods(Mods.AppliedEnergistics2, Mods.AE2FluidCraft, Mods.OpenComputers)
                .addTo(GTRecipeConstants.AssemblyLine);
        }

        // Empty structure module: spatial storage with UV control circuits.
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, getModItem(Mods.AppliedEnergistics2.ID, "tile.BlockSpatialPylon", 1))
            .metadata(SCANNING, new Scanning(20 * 30, TierEU.RECIPE_ZPM))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4, 34), // 128^3 spatial components.
                ItemList.Emitter_ZPM.get(4),

                ItemList.Tool_DataOrb.get(64),
                ItemList.Circuit_Chip_Ram.get(64),
                MaterialsAlloy.LAURENIUM.getFoil(64),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 8))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(1152),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(64000),
                Materials.SuperCoolant.getFluid(16000))
            .itemOutputs(GTCMItemList.BottledChaosMachineModule.get(1))
            .duration(20 * 30)
            .eut(TierEU.RECIPE_UV)
            .requireMods(Mods.AppliedEnergistics2)
            .addTo(GTRecipeConstants.AssemblyLine);

        // Capacity block I: ZPM storage, 4 capacity per complete region.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiMaterial", 16, 37), // 16k storage components.

                ItemList.Field_Generator_LuV.get(1),
                ItemList.Circuit_Parts_Crystal_Chip_Elite.get(16),
                ItemList.Tool_DataOrb.get(16),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 64),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 16))
            .circuit(10)
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1152))
            .itemOutputs(GTCMItemList.BottledChaosCapacityTier1.get(16))
            .duration(20 * 5)
            .eut(TierEU.RECIPE_ZPM)
            .requireMods(Mods.AppliedEnergistics2)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity block II: UV neural control and holmium shielding, 16 capacity per region.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.BottledChaosCapacityTier1.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8, 59), // 4096k storage components.

                ItemList.Sensor_UV.get(2),
                ItemList.Circuit_Chip_NeuroCPU.get(16),
                ItemList.Gravistar.get(4),

                MaterialsAlloy.QUANTUM.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.EnrichedHolmium, 16))
            .circuit(10)
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(2304))
            .itemOutputs(GTCMItemList.BottledChaosCapacityTier2.get(8))
            .duration(20 * 5)
            .eut(TierEU.RECIPE_UV)
            .requireMods(Mods.AppliedEnergistics2)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity block III: UHV storage and field isolation, 64 capacity per region.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.BottledChaosCapacityTier2.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8 },
                ItemList.Field_Generator_UHV.get(1),

                getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4, 60), // 16384k storage components.
                ItemList.UHV_Coil.get(4),
                ItemList.Gravistar.get(16),

                MaterialsAlloy.ABYSSAL.getPlate(16),
                ItemList.Naquarite_Universal_Insulator_Foil.get(16))
            .circuit(10)
            .fluidInputs(Materials.RadoxPolymer.getMolten(1152))
            .itemOutputs(GTCMItemList.BottledChaosCapacityTier3.get(4))
            .duration(20 * 10)
            .eut(TierEU.RECIPE_UHV)
            .requireMods(Mods.AppliedEnergistics2)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity block IV: UIV resonance and spatial confinement, 256 capacity per region.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.ProtoHalkonite, 4),
                NHItemList.CircuitUIV.get(8),
                ItemList.MetaMaterial_ResonanceChamber1.get(8),
                ItemList.MetaMaterial_FieldManipulator1.get(8),

                GregtechItemList.CosmicFabricManipulator.get(2),
                ItemList.Field_Generator_UIV.get(2),
                ItemList.EnergisedTesseract.get(16),
                ItemList.StableBosonContainmentUnit.get(4),

                ItemList.ZPM2.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.SixPhasedCopper, 4),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Creon, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.RadoxPolymer, 16))
            .metadata(
                NANITE_TIERS,
                new NaniteTier[] { NaniteTier.Gold, NaniteTier.Transcendent, NaniteTier.Gold, NaniteTier.Transcendent,

                    NaniteTier.Transcendent, NaniteTier.Transcendent, NaniteTier.Gold, NaniteTier.Transcendent,

                    NaniteTier.Transcendent, NaniteTier.Gold, NaniteTier.Transcendent, NaniteTier.Gold })
            .fluidInputs(
                CondensateType.Bedrockium.getEntangled(9216),
                CondensateType.CelestialTungsten.getEntangled(4608),
                CondensateType.Neutronium.getEntangled(9216),
                CondensateType.CosmicNeutronium.getEntangled(9216))
            .itemOutputs(GTCMItemList.BottledChaosCapacityTier4.get(4))
            .duration(20 * 10)
            .eut(TierEU.RECIPE_UIV)
            .requireMods(Mods.NewHorizonsCoreMod)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);

        // Capacity block V: UXV compression and exotic-matter shielding, 1024 capacity per region.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 4),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Eternity, 8),
                NHItemList.CircuitMAX.get(8),
                ItemList.MetaMaterial_ResonanceChamber4.get(4),

                ItemList.MetaMaterial_Shielding3.get(8),
                ItemList.MetaMaterial_Waveguide3.get(8),
                ItemList.MetaMaterial_SensorArray4.get(4),
                ItemList.Field_Generator_UXV.get(2),

                ItemList.ZPM3.get(1),
                GregtechItemList.SpaceTimeContinuumRipper.get(1),
                ItemList.PseudoStar.get(4),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.MagMatter, 16))
            .metadata(
                NANITE_TIERS,
                new NaniteTier[] { NaniteTier.Eternity, NaniteTier.Eternity, NaniteTier.MagMatter,
                    NaniteTier.BlackDwarf,

                    NaniteTier.Eternity, NaniteTier.Eternity, NaniteTier.BlackDwarf, NaniteTier.Universium,

                    NaniteTier.Eternity, NaniteTier.Universium, NaniteTier.BlackDwarf, NaniteTier.MagMatter })
            .fluidInputs(
                CondensateType.BoundlessCosmicSolder.getEntangled(16000),
                CondensateType.SpaceTime.getEntangled(9216),
                CondensateType.Space.getEntangled(9216),
                CondensateType.QuarkGluonPlasma.getEntangled(32000))
            .itemOutputs(GTCMItemList.BottledChaosCapacityTier5.get(4))
            .duration(20 * 15)
            .eut(TierEU.RECIPE_UXV)
            .requireMods(Mods.NewHorizonsCoreMod)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);

        // Capacity 1: LuV.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polyethylene, 16),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                ItemList.Circuit_Chip_CPU.get(16),
                ItemList.Circuit_Chip_Ram.get(64),
                ItemList.Circuit_Chip_NOR.get(64),
                ItemList.Circuit_Chip_NAND.get(64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 64))
            .circuit(10)
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1152))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(0))
            .duration(20 * 30)
            .eut(TierEU.RECIPE_LuV)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity 4: ZPM.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemBottledChaosCredential.createEmptyFrame(0),
                ItemBottledChaosCredential.createEmptyFrame(0),
                ItemBottledChaosCredential.createEmptyFrame(0),
                ItemBottledChaosCredential.createEmptyFrame(0),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                ItemList.Circuit_Chip_Ram.get(64),
                ItemList.Circuit_Chip_NAND.get(64))
            .circuit(10)
            .nbtSensitive()
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1152))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(1))
            .duration(20 * 60)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity 16: UV.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemBottledChaosCredential.createEmptyFrame(1),
                ItemBottledChaosCredential.createEmptyFrame(1),
                ItemBottledChaosCredential.createEmptyFrame(1),
                ItemBottledChaosCredential.createEmptyFrame(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                ItemList.Circuit_Parts_Crystal_Chip_Master.get(16),
                ItemList.Circuit_Chip_UHPIC.get(16))
            .circuit(10)
            .nbtSensitive()
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1152))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(2))
            .duration(20 * 120)
            .eut(TierEU.RECIPE_UV)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity 64: UHV.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemBottledChaosCredential.createEmptyFrame(2),
                ItemBottledChaosCredential.createEmptyFrame(2),
                ItemBottledChaosCredential.createEmptyFrame(2),
                ItemBottledChaosCredential.createEmptyFrame(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                ItemList.Circuit_Chip_NeuroCPU.get(16),
                ItemList.Circuit_Chip_NPIC.get(16),
                getModItem(Mods.OpenComputers.ID, "item", 16, 101)) // APU tier 1.
            .circuit(10)
            .nbtSensitive()
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1152))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(3))
            .duration(20 * 240)
            .eut(TierEU.RECIPE_UHV)
            .requireMods(Mods.OpenComputers)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity 256: UEV.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemBottledChaosCredential.createEmptyFrame(3),
                ItemBottledChaosCredential.createEmptyFrame(3),
                ItemBottledChaosCredential.createEmptyFrame(3),
                ItemBottledChaosCredential.createEmptyFrame(3),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                ItemList.Circuit_Chip_Optical.get(16),
                ItemList.Optically_Compatible_Memory.get(16),
                getModItem(Mods.OpenComputers.ID, "item", 16, 102)) // APU tier 2.
            .circuit(10)
            .nbtSensitive()
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1152))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(4))
            .duration(20 * 480)
            .eut(TierEU.RECIPE_UEV)
            .requireMods(Mods.OpenComputers)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity 1024: UIV.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemBottledChaosCredential.createEmptyFrame(4),
                ItemBottledChaosCredential.createEmptyFrame(4),
                ItemBottledChaosCredential.createEmptyFrame(4),
                ItemBottledChaosCredential.createEmptyFrame(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4 },
                ItemList.Optically_Perfected_CPU.get(16),
                ItemList.Optically_Compatible_Memory.get(16),
                getModItem(Mods.OpenComputers.ID, "item", 16, 103)) // APU tier 3.
            .circuit(10)
            .nbtSensitive()
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1152))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(5))
            .duration(20 * 960)
            .eut(TierEU.RECIPE_UIV)
            .requireMods(Mods.OpenComputers)
            .addTo(RecipeMaps.assemblerRecipes);

        // Capacity 64: biological storage and control components.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Bio_Ultra.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 2 },
                ItemList.Circuit_Chip_NeuroCPU.get(4),
                getModItem(Mods.OpenComputers.ID, "item", 4, 101), // APU tier 1.

                ItemList.Tool_DataOrb.get(4),
                ItemList.Circuit_Chip_Ram.get(16),
                ItemList.Circuit_Chip_NOR.get(16),
                ItemList.Energy_Module.get(1),

                ItemList.Circuit_Parts_TransistorASMD.get(8),
                ItemList.Circuit_Parts_CapacitorASMD.get(8),
                getModItem(Mods.OpenComputers.ID, "item", 4, 72), // Component bus tier 3.
                CustomItemList.DATApipe.get(4),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 16),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Infinity, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 2))
            .nbtSensitive()
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1152),
                Materials.RadoxPolymer.getMolten(576),
                Materials.SuperconductorUEVBase.getMolten(288))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(3))
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .duration(20 * 10)
            .eut(TierEU.RECIPE_UHV)
            .requireMods(Mods.OpenComputers)
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

        // Capacity 256: optical processing and power control.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 2 },
                ItemList.Optically_Perfected_CPU.get(1),
                getModItem(Mods.OpenComputers.ID, "item", 4, 102), // APU tier 2.

                ItemList.Optically_Compatible_Memory.get(2),
                ItemList.ZPM2.get(1),
                ItemList.Circuit_Chip_QPIC.get(4),
                ItemList.Circuit_Parts_DiodeXSMD.get(8),

                ItemList.Circuit_Parts_TransistorXSMD.get(8),
                getModItem(Mods.OpenComputers.ID, "item", 4, 72), // Component bus tier 3.
                CustomItemList.DATApipe.get(4),
                MaterialsElements.STANDALONE.HYPOGEN.getFineWire(16),

                GGMaterial.metastableOganesson.get(OrePrefixes.foil, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 2))
            .nbtSensitive()
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1152),
                Materials.RadoxPolymer.getMolten(576),
                Materials.SuperconductorUMVBase.getMolten(288))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(4))
            .metadata(IGRecipeMaps.MODULE_TIER, 2)
            .duration(20 * 10)
            .eut(TierEU.RECIPE_UIV)
            .requireMods(Mods.OpenComputers)
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

        // Capacity 1024: optical processing with quantum power components.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.MAX), 2 },
                ItemList.Optically_Perfected_CPU.get(1),
                getModItem(Mods.OpenComputers.ID, "item", 4, 103), // APU tier 3.

                ItemList.Optically_Compatible_Memory.get(2),
                ItemList.ZPM3.get(1),
                ItemList.Circuit_Chip_QPIC.get(4),
                ItemList.Wrap_OpticalSMDDiodes.get(1),

                ItemList.Wrap_OpticalSMDCapacitors.get(1),
                getModItem(Mods.OpenComputers.ID, "item", 4, 72), // Component bus tier 3.
                CustomItemList.DATApipe.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 16),

                GTOreDictUnificator.get(OrePrefixes.foil, Materials.MagMatter, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 2))
            .nbtSensitive()
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1152),
                Materials.RadoxPolymer.getMolten(576),
                Materials.MHDCSM.getMolten(288))
            .itemOutputs(ItemBottledChaosCredential.createEmptyFrame(5))
            .metadata(IGRecipeMaps.MODULE_TIER, 3)
            .duration(20 * 10)
            .eut(TierEU.RECIPE_UXV)
            .requireMods(Mods.OpenComputers)
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

        // Recycle a structure module into a clean, empty carrier.
        addShapelessCraftingRecipe(
            GTCMItemList.BottledChaosMachineModule.get(1),
            new Object[] { GTCMItemList.BottledChaosMachineModule.get(1) });

        // Recycle any credential into a clean, same-capacity carrier for further assembly.
        addShapelessCraftingRecipe(
            ItemBottledChaosCredential.createEmptyFrame(0),
            new Object[] { ItemBottledChaosCredential.createEmptyFrame(0) });
        addShapelessCraftingRecipe(
            ItemBottledChaosCredential.createEmptyFrame(1),
            new Object[] { ItemBottledChaosCredential.createEmptyFrame(1) });
        addShapelessCraftingRecipe(
            ItemBottledChaosCredential.createEmptyFrame(2),
            new Object[] { ItemBottledChaosCredential.createEmptyFrame(2) });
        addShapelessCraftingRecipe(
            ItemBottledChaosCredential.createEmptyFrame(3),
            new Object[] { ItemBottledChaosCredential.createEmptyFrame(3) });
        addShapelessCraftingRecipe(
            ItemBottledChaosCredential.createEmptyFrame(4),
            new Object[] { ItemBottledChaosCredential.createEmptyFrame(4) });
        addShapelessCraftingRecipe(
            ItemBottledChaosCredential.createEmptyFrame(5),
            new Object[] { ItemBottledChaosCredential.createEmptyFrame(5) });
    }
}
