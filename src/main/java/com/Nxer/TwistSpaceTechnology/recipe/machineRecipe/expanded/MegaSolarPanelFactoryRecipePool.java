package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.dreammaster.scripts.IScriptLoader.missing;
import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.util.GTModHandler.getModItem;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.dreammaster.item.NHItemList;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;

// Generated from NewHorizonsCoreMod solar factory recipes. Do not edit by hand.
public class MegaSolarPanelFactoryRecipePool {

    private static final IRecipeMap MSPF = GTCMRecipe.MegaSolarPanelFactoryRecipeMap;

    public static void loadRecipes() {

        // Irradiant Aluminium Plate -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartCarbonPlate", 4, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIndustrialDiamond", 1, 0, missing),
                getModItem(EnderIO.ID, "itemMaterial", 1, 6, missing),
                ore(OrePrefixes.plate, Materials.Aluminium, 2),
                ore(OrePrefixes.plate, Materials.Iron, 4),
                ore(OrePrefixes.plate, Materials.RedAlloy, 2),
                ore(OrePrefixes.screw, Materials.RedAlloy, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .itemOutputs(NHItemList.IrradiantReinforcedAluminiumPlate.get(32))
            .eut(TierEU.RECIPE_HV)
            .duration(600)
            .addTo(MSPF);

        // Irradiant Titanium Plate -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                ore(OrePrefixes.plate, Materials.Iron, 4),
                ore(OrePrefixes.plate, Materials.Lapis, 2),
                ore(OrePrefixes.plate, Materials.MeteoricSteel, 1),
                ore(OrePrefixes.plate, Materials.SiliconSG, 4),
                ore(OrePrefixes.plate, Materials.Titanium, 2),
                ore(OrePrefixes.screw, Materials.RedAlloy, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288), Materials.Sunnarium.getMolten(144))
            .itemOutputs(NHItemList.IrradiantReinforcedTitaniumPlate.get(32))
            .eut(TierEU.RECIPE_EV)
            .duration(600)
            .addTo(MSPF);

        // Irradiant Tungsten Plate -> x32 [circuit 1]
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                ore(OrePrefixes.plate, Materials.Iron, 4),
                ore(OrePrefixes.plate, Materials.Steeleaf, 2),
                ore(OrePrefixes.plate, Materials.Tungsten, 6),
                ore(OrePrefixes.screw, Materials.RedAlloy, 4))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(576),
                Materials.Sunnarium.getMolten(1296),
                Materials.Uranium.getMolten(1296))
            .itemOutputs(NHItemList.IrradiantReinforcedTungstenPlate.get(32))
            .eut(TierEU.RECIPE_IV)
            .duration(600)
            .addTo(MSPF);

        // Irradiant TungstenSteel Plate -> x32 [circuit 1]
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                ore(OrePrefixes.plate, Materials.Iron, 4),
                ore(OrePrefixes.plate, Materials.Knightmetal, 2),
                ore(OrePrefixes.plate, Materials.Plutonium, 1),
                ore(OrePrefixes.plate, Materials.TungstenSteel, 6),
                ore(OrePrefixes.plateQuadruple, Materials.Sunnarium, 1),
                ore(OrePrefixes.screw, Materials.RedAlloy, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(1152))
            .itemOutputs(NHItemList.IrradiantReinforcedTungstenSteelPlate.get(32))
            .eut(TierEU.RECIPE_LuV)
            .duration(600)
            .addTo(MSPF);

        // Irradiant Chrome Plate -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 8, 0, missing),
                ore(OrePrefixes.plate, Materials.Chrome, 2),
                ore(OrePrefixes.plate, Materials.Diamond, 4),
                ore(OrePrefixes.plate, Materials.FierySteel, 2),
                ore(OrePrefixes.plate, Materials.Iron, 4),
                ore(OrePrefixes.plate, Materials.Sunnarium, 4),
                ore(OrePrefixes.plate, Materials.YttriumBariumCuprate, 1),
                ore(OrePrefixes.screw, Materials.RedAlloy, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(2304))
            .itemOutputs(NHItemList.IrradiantReinforcedChromePlate.get(32))
            .eut(TierEU.RECIPE_ZPM)
            .duration(600)
            .addTo(MSPF);

        // Irradiant Iridium Plate -> x32 [circuit 1]
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 12, 0, missing),
                ore(OrePrefixes.plate, Materials.Iridium, 2),
                ore(OrePrefixes.plate, Materials.Iron, 4))
            .fluidInputs(Materials.Sunnarium.getMolten(10944), Materials.Uranium.getMolten(9216))
            .itemOutputs(asp(32, 8))
            .eut(TierEU.RECIPE_UV)
            .duration(600)
            .addTo(MSPF);

        // Irradiant Naquadria Plate -> x32 [circuit 1]
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 8, 0, missing),
                ore(OrePrefixes.plate, Materials.Iron, 4),
                ore(OrePrefixes.plate, Materials.MysteriousCrystal, 4),
                ore(OrePrefixes.plate, Materials.Naquadria, 2),
                ore(OrePrefixes.plate, Materials.Osmiridium, 2),
                ore(OrePrefixes.plate, Materials.Quantium, 1),
                ore(OrePrefixes.plateDense, Materials.Naquadria, 1),
                ore(OrePrefixes.screw, Materials.RedAlloy, 4))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(9216),
                Materials.Sunnarium.getMolten(10944),
                Materials.Uranium.getMolten(9216))
            .itemOutputs(NHItemList.IrradiantReinforcedNaquadriaPlate.get(32))
            .eut(TierEU.RECIPE_UHV)
            .duration(600)
            .addTo(MSPF);

        // Irradiant Neutronium Plate -> x32 [circuit 1]
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 8, 0, missing),
                ore(OrePrefixes.plate, Materials.BlackPlutonium, 4),
                ore(OrePrefixes.plate, Materials.Infinity, 2),
                ore(OrePrefixes.plate, Materials.Iron, 4),
                ore(OrePrefixes.plate, Materials.MysteriousCrystal, 1),
                ore(OrePrefixes.plate, Materials.Neutronium, 2),
                ore(OrePrefixes.plateDense, Materials.Naquadria, 1),
                ore(OrePrefixes.plateDense, Materials.Neutronium, 1),
                ore(OrePrefixes.screw, Materials.RedAlloy, 4))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(18432),
                Materials.Sunnarium.getMolten(10944),
                Materials.Uranium.getMolten(9216))
            .itemOutputs(NHItemList.IrradiantReinforcedNeutroniumPlate.get(32))
            .eut(TierEU.RECIPE_UEV)
            .duration(600)
            .addTo(MSPF);

        // Irradiant Bedrockium Plate -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 4, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 8, 0, missing),
                ore(OrePrefixes.plate, Materials.Bedrockium, 20),
                ore(OrePrefixes.plate, Materials.DraconiumAwakened, 5),
                ore(OrePrefixes.plate, Materials.InfinityCatalyst, 2),
                ore(OrePrefixes.plate, Materials.Iron, 4),
                ore(OrePrefixes.plateDense, Materials.Naquadria, 1),
                ore(OrePrefixes.plateDense, Materials.Neutronium, 1),
                ore(OrePrefixes.screw, Materials.RedAlloy, 4))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(36864),
                Materials.Sunnarium.getMolten(10944),
                Materials.Uranium.getMolten(9216))
            .itemOutputs(NHItemList.IrradiantReinforcedBedrockiumPlate.get(32))
            .eut(TierEU.RECIPE_UIV)
            .duration(600)
            .addTo(MSPF);

        // Solar Panel LV (direct) -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Silicon_Wafer2.get(4),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 16, 0, missing),
                ore(OrePrefixes.plate, Materials.Iron, 16),
                ore(OrePrefixes.plate, Materials.StainlessSteel, 8),
                ore(OrePrefixes.plate, Materials.Steeleaf, 8),
                ore(OrePrefixes.plate, Materials.Tungsten, 24),
                ore(OrePrefixes.screw, Materials.RedAlloy, 16),
                ore(OrePrefixes.wireGt01, Materials.SuperconductorEV, 8),
                ore(OrePrefixes.circuit, Materials.IV, 4))
            .fluidInputs(
                Materials.IndiumGalliumPhosphide.getMolten(576),
                Materials.SolderingAlloy.getMolten(2304),
                Materials.Sunnarium.getMolten(5184),
                Materials.Uranium.getMolten(5184),
                Materials.VibrantAlloy.getMolten(576))
            .itemOutputs(ItemList.Cover_SolarPanel_LV.get(32))
            .eut(TierEU.RECIPE_EV)
            .duration(200)
            .addTo(MSPF);

        // Solar Panel MV (direct) -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Silicon_Wafer3.get(4),
                ItemList.Circuit_Wafer_PIC.get(4),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 16, 0, missing),
                ore(OrePrefixes.plate, Materials.HSSE, 8),
                ore(OrePrefixes.plate, Materials.Iron, 16),
                ore(OrePrefixes.plate, Materials.Knightmetal, 8),
                ore(OrePrefixes.plate, Materials.Plutonium, 4),
                ore(OrePrefixes.plate, Materials.TungstenSteel, 24),
                ore(OrePrefixes.plateQuadruple, Materials.Sunnarium, 4),
                ore(OrePrefixes.screw, Materials.RedAlloy, 16),
                ore(OrePrefixes.wireGt02, Materials.SuperconductorIV, 8),
                ore(OrePrefixes.circuit, Materials.LuV, 4))
            .fluidInputs(
                Materials.Barium.getMolten(288),
                Materials.MeteoricSteel.getMolten(576),
                Materials.SolderingAlloy.getMolten(4608))
            .itemOutputs(ItemList.Cover_SolarPanel_MV.get(32))
            .eut(TierEU.RECIPE_IV)
            .duration(200)
            .addTo(MSPF);

        // Solar Panel HV (direct) -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Silicon_Wafer3.get(8),
                ItemList.Circuit_Wafer_HPIC.get(4),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 16, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 32, 0, missing),
                ore(OrePrefixes.plate, Materials.Chrome, 8),
                ore(OrePrefixes.plate, Materials.Diamond, 16),
                ore(OrePrefixes.plate, Materials.FierySteel, 8),
                ore(OrePrefixes.plate, Materials.Iron, 16),
                ore(OrePrefixes.plate, Materials.Naquadah, 8),
                ore(OrePrefixes.plate, Materials.YttriumBariumCuprate, 4),
                ore(OrePrefixes.screw, Materials.RedAlloy, 16),
                ore(OrePrefixes.wireGt04, Materials.SuperconductorLuV, 8),
                ore(OrePrefixes.circuit, Materials.ZPM, 4))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(9216),
                Materials.Sunnarium.getMolten(3456),
                Materials.Uranium.getMolten(576))
            .itemOutputs(ItemList.Cover_SolarPanel_HV.get(32))
            .eut(TierEU.RECIPE_LuV)
            .duration(200)
            .addTo(MSPF);

        // Solar Panel EV (direct) -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_NPIC.get(4),
                ItemList.Circuit_Silicon_Wafer4.get(8),
                ItemList.Circuit_Wafer_UHPIC.get(4),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 8, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 24, 0, missing),
                ore(OrePrefixes.plate, Materials.Iridium, 4),
                ore(OrePrefixes.plate, Materials.Iron, 8),
                ore(OrePrefixes.plate, Materials.NaquadahAlloy, 16),
                ore(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 20),
                ore(OrePrefixes.circuit, Materials.UV, 4))
            .fluidInputs(
                Materials.Knightmetal.getMolten(576),
                Materials.Plutonium.getMolten(288),
                Materials.Sunnarium.getMolten(21888),
                Materials.Ultimet.getMolten(1152),
                Materials.Uranium.getMolten(18432))
            .itemOutputs(ItemList.Cover_SolarPanel_EV.get(32))
            .eut(TierEU.RECIPE_ZPM)
            .duration(200)
            .addTo(MSPF);

        // Solar Panel IV (direct) -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_NPIC.get(8),
                ItemList.Circuit_Silicon_Wafer5.get(8),
                ItemList.Circuit_Wafer_QPIC.get(4),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 8, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 16, 0, missing),
                ore(OrePrefixes.plate, Materials.Iron, 8),
                ore(OrePrefixes.plate, Materials.MysteriousCrystal, 8),
                ore(OrePrefixes.plate, Materials.Naquadria, 4),
                ore(OrePrefixes.plate, Materials.Osmiridium, 4),
                ore(OrePrefixes.plate, Materials.Quantium, 2),
                ore(OrePrefixes.plate, Materials.Tritanium, 8),
                ore(OrePrefixes.plateDense, Materials.Naquadria, 2),
                ore(OrePrefixes.screw, Materials.RedAlloy, 8),
                ore(OrePrefixes.wireGt01, Materials.SuperconductorUV, 16),
                ore(OrePrefixes.circuit, Materials.UHV, 2))
            .fluidInputs(
                Materials.Chrome.getMolten(1152),
                Materials.FierySteel.getMolten(288),
                Materials.NaquadahAlloy.getMolten(576),
                Materials.SolderingAlloy.getMolten(18432),
                Materials.Sunnarium.getMolten(21888),
                Materials.Uranium.getMolten(18432))
            .itemOutputs(ItemList.Cover_SolarPanel_IV.get(32))
            .eut(TierEU.RECIPE_UV)
            .duration(200)
            .addTo(MSPF);

        // Solar Panel LuV (direct) -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_PPIC.get(2),
                ItemList.Circuit_Chip_UHPIC.get(4),
                ItemList.Circuit_Silicon_Wafer5.get(8),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 16, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 32, 0, missing),
                getModItem(SuperSolarPanels.ID, "solarsplitter", 4, 0, missing),
                ore(OrePrefixes.plate, Materials.BlackPlutonium, 16),
                ore(OrePrefixes.plate, Materials.Infinity, 8),
                ore(OrePrefixes.plate, Materials.Iron, 16),
                ore(OrePrefixes.plate, Materials.MysteriousCrystal, 4),
                ore(OrePrefixes.plate, Materials.Neutronium, 8),
                ore(OrePrefixes.plateDense, Materials.Naquadria, 4),
                ore(OrePrefixes.plateDense, Materials.Neutronium, 4),
                ore(OrePrefixes.plateSuperdense, Materials.SiliconSG, 2),
                ore(OrePrefixes.plateSuperdense, Materials.Steeleaf, 1),
                ore(OrePrefixes.screw, Materials.RedAlloy, 16),
                ore(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 32),
                ore(OrePrefixes.circuit, Materials.UEV, 1))
            .fluidInputs(
                Materials.FierySteel.getMolten(576),
                Materials.Knightmetal.getMolten(576),
                Materials.SolderingAlloy.getMolten(73728),
                Materials.Sunnarium.getMolten(43776),
                Materials.Uranium.getMolten(36864),
                new FluidStack(FluidRegistry.getFluid("molten.indalloy140"), 1152))
            .itemOutputs(ItemList.Cover_SolarPanel_LuV.get(32))
            .eut(TierEU.RECIPE_UEV)
            .duration(200)
            .addTo(MSPF);

        // Solar Panel ZPM (direct) -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_PPIC.get(4),
                ItemList.Circuit_Chip_UHPIC.get(8),
                ItemList.Circuit_Silicon_Wafer5.get(8),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 24, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 48, 0, missing),
                ore(OrePrefixes.plate, Materials.BlackPlutonium, 24),
                ore(OrePrefixes.plate, Materials.Iron, 24),
                ore(OrePrefixes.plate, Materials.MysteriousCrystal, 6),
                ore(OrePrefixes.plate, Materials.Neutronium, 12),
                ore(OrePrefixes.plateDense, Materials.Naquadria, 6),
                ore(OrePrefixes.plateDense, Materials.Neutronium, 6),
                ore(OrePrefixes.plateSuperdense, Materials.Samarium, 1),
                ore(OrePrefixes.plateSuperdense, Materials.Steeleaf, 1),
                ore(OrePrefixes.screw, Materials.RedAlloy, 24),
                ore(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 32),
                ore(OrePrefixes.circuit, Materials.UEV, 4))
            .fluidInputs(
                Materials.Infinity.getMolten(2016),
                Materials.Kevlar.getMolten(576),
                Materials.SolderingAlloy.getMolten(110592),
                Materials.Sunnarium.getMolten(65664),
                Materials.Uranium.getMolten(55296))
            .itemOutputs(ItemList.Cover_SolarPanel_ZPM.get(32))
            .eut(TierEU.RECIPE_UIV)
            .duration(200)
            .addTo(MSPF);

        // Solar Panel UV (direct) -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_PPIC.get(8),
                ItemList.Circuit_Chip_UHPIC.get(16),
                ItemList.Circuit_Silicon_Wafer5.get(8),
                ItemList.Circuit_Wafer_FPIC.get(24),
                getModItem(IndustrialCraft2.ID, "itemPartAlloy", 32, 0, missing),
                getModItem(IndustrialCraft2.ID, "itemPartIridium", 64, 0, missing),
                ore(OrePrefixes.plate, Materials.BlackPlutonium, 32),
                ore(OrePrefixes.plate, Materials.Iron, 32),
                ore(OrePrefixes.plate, Materials.MysteriousCrystal, 8),
                ore(OrePrefixes.plate, Materials.Neutronium, 16),
                ore(OrePrefixes.plateDense, Materials.Naquadria, 8),
                ore(OrePrefixes.plateDense, Materials.Neutronium, 8),
                ore(OrePrefixes.plateSuperdense, Materials.ElectrumFlux, 2),
                ore(OrePrefixes.plateSuperdense, Materials.Steeleaf, 1),
                ore(OrePrefixes.screw, Materials.RedAlloy, 32),
                ore(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32),
                ore(OrePrefixes.circuit, Materials.UEV, 8))
            .fluidInputs(
                Materials.Infinity.getMolten(2880),
                Materials.Kevlar.getMolten(1152),
                Materials.Knightmetal.getMolten(288),
                Materials.SolderingAlloy.getMolten(147456),
                Materials.Sunnarium.getMolten(87552),
                Materials.Uranium.getMolten(73728))
            .itemOutputs(ItemList.Cover_SolarPanel_UV.get(32))
            .eut(TierEU.RECIPE_UIV)
            .duration(200)
            .addTo(MSPF);

        // Solar Splitter -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ReinforcedGlass.get(288), ore(OrePrefixes.plate, Materials.Glowstone, 72))
            .fluidInputs(
                Materials.Sunnarium.getMolten(41472),
                Materials.Uranium.getMolten(41472),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(2304))
            .itemOutputs(getModItem(SuperSolarPanels.ID, "solarsplitter", 256, 0, missing))
            .eut(TierEU.RECIPE_UEV)
            .duration(20)
            .addTo(MSPF);

        // Solar Sail -> x32
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Cover_SolarPanel_LuV.get(1),
                ItemList.Robot_Arm_LuV.get(1),
                ItemList.UHTResistantMesh.get(8),
                ore(OrePrefixes.plate, Materials.Kevlar, 4),
                ore(OrePrefixes.itemCasing, Materials.RadoxPolymer, 1),
                ore(OrePrefixes.nanite, Materials.Glowstone, 2))
            .fluidInputs(
                Materials.Grade8PurifiedWater.getFluid(100),
                new FluidStack(FluidRegistry.getFluid("molten.mutatedlivingsolder"), 720))
            .itemOutputs(ItemList.SolarSail.get(32))
            .eut(TierEU.RECIPE_UIV)
            .duration(200)
            .addTo(MSPF);
    }

    private static ItemStack ore(OrePrefixes prefix, Materials material, long amount) {
        return GTOreDictUnificator.get(prefix, material, amount);
    }

    private static ItemStack asp(long amount, int meta) {
        return getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", amount, meta, missing);
    }
}
