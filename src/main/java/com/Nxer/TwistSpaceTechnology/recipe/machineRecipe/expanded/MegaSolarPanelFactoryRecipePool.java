package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.dreammaster.scripts.IScriptLoader.missing;
import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.TierEU.*;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.*;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.dreammaster.item.NHItemList;

import crazypants.enderio.material.Material;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class MegaSolarPanelFactoryRecipePool {

    static final IRecipeMap MSPF = GTCMRecipe.MegaSolarPanelFactoryRecpies;

    // spotless:off
    public static void loadRecipes() {

    // Irradiant Reinforced Plates
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.Aluminium.getPlates(2),
            Materials.Iron.getPlates(4),
            Materials.RedAlloy.getPlates(2),
            Materials.Diamond.getGems(1),
            Material.VIBRANT_CYSTAL.getStack(1),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 4))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144))
        .itemOutputs(NHItemList.IrradiantReinforcedAluminiumPlate.getIS(16))
        .eut(HV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.Titanium.getPlates(2),
            Materials.Iron.getPlates(4),
            Materials.SiliconSG.getPlates(4),
            Materials.MeteoricSteel.getPlates(1),
            Materials.Lapis.getPlates(2),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 16))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144*2),
            Materials.Sunnarium.getMolten(144))
        .itemOutputs(NHItemList.IrradiantReinforcedTitaniumPlate.getIS(16))
        .eut(EV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.Tungsten.getPlates(6),
            Materials.Iron.getPlates(4),
            Materials.Steeleaf.getPlates(2),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV,16))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144*4),
            Materials.Uranium.getMolten(144*9),
            Materials.Sunnarium.getMolten(144*9))
        .itemOutputs(NHItemList.IrradiantReinforcedTungstenPlate.getIS(16))
        .eut(IV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.TungstenSteel.getPlates(6),
            Materials.Iron.getPlates(4),
            Materials.Plutonium.getPlates(1),
            Materials.Knightmetal.getPlates(2),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 16))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144*8),
            Materials.Sunnarium.getMolten(144*4))
        .itemOutputs(NHItemList.IrradiantReinforcedTungstenSteelPlate.getIS(16))
        .eut(LuV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.Chrome.getPlates(2),
            Materials.Iron.getPlates(4),
            Materials.Diamond.getPlates(4),
            Materials.YttriumBariumCuprate.getPlates(1),
            Materials.FierySteel.getPlates(2),
            Materials.Diamond.getDust(32),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 12))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144*16),
            Materials.Iridium.getMolten(144*32),
            Materials.Sunnarium.getMolten(144*4))
        .itemOutputs(NHItemList.IrradiantReinforcedChromePlate.getIS(16))
        .eut(ZPM)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.Iridium.getPlates(28),
            Materials.Iron.getPlates(4),
            Materials.Osmium.getPlates(1),
            Materials.Diamond.getDust(48),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 8))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144*32),
            Materials.Uranium.getMolten(144*64),
            Materials.Iridium.getMolten(144*116),
            Materials.Sunnarium.getMolten(144*76))
        .itemOutputs(getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 16, 8, missing))
        .eut(UV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.Naquadria.getPlates(2),
            Materials.Iron.getPlates(4),
            Materials.MysteriousCrystal.getPlates(4),
            Materials.Quantium.getPlates(1),
            Materials.Osmiridium.getPlates(2),
            GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Naquadria, 1),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 4))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144*32),
            Materials.Uranium.getMolten(144*64),
            Materials.Iridium.getMolten(144*32),
            Materials.Sunnarium.getMolten(144*76))
        .itemOutputs(NHItemList.IrradiantReinforcedNaquadriaPlate.getIS(16))
        .eut(UHV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.Neutronium.getPlates(2),
            Materials.Iron.getPlates(4),
            Materials.BlackPlutonium.getPlates(4),
            Materials.MysteriousCrystal.getPlates(1),
            Materials.Infinity.getPlates(2),
            GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Naquadria, 1),
            GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 1),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 3))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144*32),
            Materials.Uranium.getMolten(144*64),
            Materials.Iridium.getMolten(144*32),
            Materials.Sunnarium.getMolten(144*76))
        .itemOutputs(NHItemList.IrradiantReinforcedNeutroniumPlate.getIS(16))
        .eut(UEV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            Materials.Bedrockium.getPlates(2),
            Materials.Iron.getPlates(4),
            Materials.DraconiumAwakened.getPlates(5),
            Materials.InfinityCatalyst.getPlates(2),
            GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Naquadria, 1),
            GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 1),
            GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 18),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 2))
        .fluidInputs(
            Materials.SolderingAlloy.getMolten(144*32),
            Materials.Uranium.getMolten(144*64),
            Materials.Iridium.getMolten(144*32),
            Materials.Sunnarium.getMolten(144*76))
        .itemOutputs(NHItemList.IrradiantReinforcedBedrockiumPlate.getIS(16))
        .eut(UIV)
        .duration(40*SECONDS)
        .addTo(MSPF);

    // Solar Panels
    GTValues.RA.stdBuilder()
        .itemInputs(
            NHItemList.IrradiantReinforcedTungstenPlate.getIS(4),
            ItemList.Circuit_Silicon_Wafer7.get(1),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 8),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 4))
        .fluidInputs(
            Materials.VibrantAlloy.getMolten(144*4),
            Materials.IndiumGalliumPhosphide.getMolten(144*4))
        .itemOutputs(GTUtility.copyAmountUnsafe(16384,ItemList.Cover_SolarPanel_LV.get(1)))
        .eut(UEV)
        .duration(10*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            NHItemList.IrradiantReinforcedTungstenSteelPlate.getIS(4),
            ItemList.Circuit_Silicon_Wafer7.get(1),
            GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 8),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 4))
        .fluidInputs(
            Materials.Barium.getMolten(144*2),
            Materials.MeteoricSteel.getMolten(144*4))
        .itemOutputs(GTUtility.copyAmountUnsafe(16384,ItemList.Cover_SolarPanel_MV.get(1)))
        .eut(UEV)
        .duration(20*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            NHItemList.IrradiantReinforcedChromePlate.getIS(4),
            ItemList.Circuit_Silicon_Wafer7.get(1),
            GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 8),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 4))
        .fluidInputs(
            Materials.Uranium.getMolten(144*4),
            Materials.Sunnarium.getMolten(144*8))
        .itemOutputs(GTUtility.copyAmountUnsafe(16384,ItemList.Cover_SolarPanel_HV.get(1)))
        .eut(UEV)
        .duration(30*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 2, 8, missing),
            ItemList.Circuit_Silicon_Wafer7.get(1),
            ItemList.Circuit_Wafer_QPIC.get(6),
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 8),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 4))
        .fluidInputs(
            Materials.Plutonium.getMolten(144*2),
            Materials.Knightmetal.getMolten(144*4),
            Materials.Ultimet.getMolten(144*8))
        .itemOutputs(GTUtility.copyAmountUnsafe(16384,ItemList.Cover_SolarPanel_EV.get(1)))
        .eut(UEV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            NHItemList.IrradiantReinforcedNaquadriaPlate.getIS(4),
            ItemList.Circuit_Silicon_Wafer7.get(1),
            ItemList.Circuit_Wafer_QPIC.get(32),
            GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUEV, 8),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 4))
        .fluidInputs(
            Materials.Chrome.getMolten(144*8),
            Materials.NaquadahAlloy.getMolten(144*4),
            Materials.FierySteel.getMolten(144*2))
        .itemOutputs(GTUtility.copyAmountUnsafe(16384,ItemList.Cover_SolarPanel_IV.get(1)))
        .eut(UEV)
        .duration(50*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            NHItemList.IrradiantReinforcedNeutroniumPlate.getIS(4),
            ItemList.Circuit_Silicon_Wafer7.get(2),
            ItemList.Circuit_Wafer_QPIC.get(32),
            GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Steeleaf, 2),
            GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 8),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 4))
        .fluidInputs(
            new FluidStack(FluidRegistry.getFluid("molten.indalloy140"), 8 * 144),
            Materials.FierySteel.getMolten(4 * 144),
            Materials.Knightmetal.getMolten(4 * 144))
        .itemOutputs(GTUtility.copyAmountUnsafe(16384,ItemList.Cover_SolarPanel_LuV.get(1)))
        .eut(UIV)
        .duration(30*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            NHItemList.IrradiantReinforcedNeutroniumPlate.getIS(12),
            ItemList.Circuit_Silicon_Wafer7.get(2),
            ItemList.Circuit_Wafer_QPIC.get(32),
            GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Steeleaf, 2),
            GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUEV, 8),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 4))
        .fluidInputs(
            Materials.Infinity.getMolten(2 * 144),
            MaterialsKevlar.Kevlar.getMolten(4 * 144))
        .itemOutputs(GTUtility.copyAmountUnsafe(16384,ItemList.Cover_SolarPanel_ZPM.get(1)))
        .eut(UIV)
        .duration(40*SECONDS)
        .addTo(MSPF);
    GTValues.RA.stdBuilder()
        .itemInputs(
            NHItemList.IrradiantReinforcedBedrockiumPlate.getIS(4),
            ItemList.Circuit_Silicon_Wafer7.get(4),
            GTModHandler.getModItem("dreamcraft", "item.PicoWafer", 32),
            ItemList.Circuit_Wafer_QPIC.get(64),
            GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Steeleaf, 2),
            GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 32),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 4))
        .fluidInputs(
            Materials.Infinity.getMolten(4 * 144),
            MaterialsKevlar.Kevlar.getMolten(8 * 144),
            Materials.Knightmetal.getMolten(2 * 144))
        .itemOutputs(GTUtility.copyAmountUnsafe(16384,ItemList.Cover_SolarPanel_UV.get(1)))
        .eut(UIV)
        .duration(60*SECONDS)
        .addTo(MSPF);
    }
    //spotless:on
}
