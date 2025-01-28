package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.HarmoniousWirelessEnergyHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeEV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeHV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeIV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeLV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeLuV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeMAX;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeMV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUEV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUHV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUIV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUMV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUXV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeZPM;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_HV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_IV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LuV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static gregtech.api.enums.ItemList.ZPM6;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.loader.MachineLoader;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import ic2.core.Ic2Items;
import tectech.thing.CustomItemList;

public class PreciseHighEnergyPhotonicQuantumMasterRecipes implements IRecipePool {

    // spotless:off
    @Override
    public void loadRecipes() {

        // Controller
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CustomItemList.eM_Power.get(6),
                CustomItemList.Machine_Multi_Transformer.get(2),
                ItemList.Machine_Multi_IndustrialLaserEngraver.get(64),

                GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 3L, 32105),
                ItemList.Emitter_UV.get(16),
                ItemList.Field_Generator_UV.get(8),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 8),
                copyAmount(64, Ic2Items.iridiumPlate))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 128))
            .itemOutputs(copyAmount(1, MachineLoader.PreciseHighEnergyPhotonicQuantumMaster))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(assemblerRecipes);

        // Upgrade LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11100),

                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 900),
                ItemList.Emitter_LV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 3),

                ItemList.Field_Generator_LV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 2))
            .itemOutputs(PhotonControllerUpgradeLV.get(1))
            .eut(RECIPE_LV)
            .duration(20 * 10)
            .addTo(assemblerRecipes);

        // Upgrade MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11101),

                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 901),
                ItemList.Emitter_MV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 6),

                ItemList.Field_Generator_MV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 8))
            .itemOutputs(PhotonControllerUpgradeMV.get(1))
            .eut(RECIPE_MV)
            .duration(20 * 20)
            .addTo(assemblerRecipes);

        // Upgrade HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11102),

                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 902),
                ItemList.Emitter_HV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 12),

                ItemList.Field_Generator_HV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(PhotonControllerUpgradeHV.get(1))
            .eut(RECIPE_HV)
            .duration(20 * 40)
            .addTo(assemblerRecipes);

        // Upgrade EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11103),

                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 15229),
                ItemList.Emitter_EV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 24),

                ItemList.Field_Generator_EV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 128))
            .itemOutputs(PhotonControllerUpgradeEV.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 80)
            .addTo(assemblerRecipes);

        // Upgrade IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11104),

                CustomItemList.eM_energyTunnel1_IV.get(1),
                ItemList.Emitter_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 48),

                ItemList.Field_Generator_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 512))
            .itemOutputs(PhotonControllerUpgradeIV.get(1))
            .eut(RECIPE_IV)
            .duration(20 * 160)
            .addTo(assemblerRecipes);

        // Upgrade LuV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11105))
            .metadata(RESEARCH_TIME, 1 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11105),
                CustomItemList.eM_energyTunnel2_LuV.get(1),
                CustomItemList.eM_Power.get(4),

                ItemList.Emitter_LuV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 4),
                ItemList.Field_Generator_LuV.get(2),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 4))
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 4))
            .itemOutputs(PhotonControllerUpgradeLuV.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 20)
            .addTo(AssemblyLine);

        // Upgrade ZPM
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11106))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11106),
                CustomItemList.eM_energyTunnel3_ZPM.get(1),
                CustomItemList.eM_Power.get(4),

                ItemList.Emitter_ZPM.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 8),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 8),
                ItemList.Field_Generator_ZPM.get(2),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 4))
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 8))
            .itemOutputs(PhotonControllerUpgradeZPM.get(1))

            .eut(RECIPE_ZPM)
            .duration(20 * 40)
            .addTo(AssemblyLine);

        // Upgrade UV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11107))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11107),
                CustomItemList.eM_energyTunnel4_UV.get(1),
                CustomItemList.eM_Power.get(4),

                ItemList.Emitter_UV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 16),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 16),
                ItemList.Field_Generator_UV.get(2),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 4))
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 16))
            .itemOutputs(PhotonControllerUpgradeUV.get(1))
            .eut(RECIPE_UV)
            .duration(20 * 80)
            .addTo(AssemblyLine);

        // Upgrade UHV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUV.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 8, 11107),
                CustomItemList.eM_energyTunnel5_UHV.get(1),
                ItemList.Casing_Dim_Injector.get(4),

                ItemList.Emitter_UHV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 32),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 32),
                ItemList.Field_Generator_UHV.get(2),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 4))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * 144))
            .itemOutputs(PhotonControllerUpgradeUHV.get(1))
            .eut(RECIPE_UHV)
            .duration(20 * 160)
            .addTo(AssemblyLine);

        // Upgrade UEV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUHV.get(1))
            .metadata(RESEARCH_TIME, 16 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 64, 11107),
                CustomItemList.eM_energyTunnel6_UEV.get(1),
                CustomItemList.eM_Power.get(4),

                ItemList.Emitter_UEV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 64),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UEV.get(2),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 4),
                Materials.Silver.getNanite(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(64 * 144),
                Materials.SuperconductorUEVBase.getMolten(64 * 144))
            .itemOutputs(PhotonControllerUpgradeUEV.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 320)
            .addTo(AssemblyLine);

        // Upgrade UIV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUEV.get(1))
            .metadata(RESEARCH_TIME, 32 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                SpaceWarper.get(1),
                CustomItemList.eM_energyTunnel7_UIV.get(1),
                ItemList.Casing_Dim_Injector.get(16),

                ItemList.Emitter_UIV.get(8),
                GregtechItemList.Laser_Lens_Special.get(4),
                ItemList.EnergisedTesseract.get(4),
                ItemList.Field_Generator_UIV.get(4),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 8),
                Materials.Gold.getNanite(32))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(256 * 144),
                Materials.SuperconductorUIVBase.getMolten(64 * 144),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(256 * 1000))
            .itemOutputs(PhotonControllerUpgradeUIV.get(1))
            .eut(RECIPE_UIV)
            .duration(20 * 640)
            .addTo(AssemblyLine);

        // Upgrade UMV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUIV.get(1))
            .metadata(RESEARCH_TIME, 64 * HOURS)
            .itemInputs(
                PhotonControllerUpgradeUIV.get(1),
                SpaceWarper.get(4),
                CustomItemList.eM_energyTunnel8_UMV.get(1),
                ItemList.Casing_Dim_Injector.get(64),

                ItemList.Emitter_UMV.get(16),
                GregtechItemList.Laser_Lens_Special.get(16),
                ItemList.EnergisedTesseract.get(16),
                ItemList.Field_Generator_UMV.get(8),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 16),
                MaterialsUEVplus.SixPhasedCopper.getNanite(16),

                CustomItemList.EOH_Reinforced_Temporal_Casing.get(4))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1024 * 144),
                Materials.SuperconductorUMVBase.getMolten(64 * 144),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(1024 * 1000),
                MaterialsUEVplus.Time.getMolten(144 * 64 * 8))
            .itemOutputs(PhotonControllerUpgradeUMV.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 1280)
            .addTo(AssemblyLine);

        // Upgrade UXV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUMV.get(1))
            .metadata(RESEARCH_TIME, 128 * HOURS)
            .itemInputs(
                PhotonControllerUpgradeUMV.get(1),
                SpaceWarper.get(16),
                CustomItemList.eM_energyTunnel9_UXV.get(1),
                CustomItemList.EOH_Infinite_Energy_Casing.get(16),

                ItemList.Emitter_UXV.get(32),
                GregtechItemList.Laser_Lens_Special.get(64),
                ItemList.EnergisedTesseract.get(64),
                ItemList.Field_Generator_UXV.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialsUEVplus.SpaceTime, 64),
                ItemList.Timepiece.get(64),

                MaterialsTST.Axonium.getNanite(64),
                MaterialsUEVplus.Eternity.getNanite(48),
                MaterialsUEVplus.Universium.getNanite(32),
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4096 * 144),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64), // SuperconductorUXVBase
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(4096 * 1000),
                MaterialsUEVplus.Time.getMolten(144 * 64 * 32))
            .itemOutputs(PhotonControllerUpgradeUXV.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 2560)
            .addTo(AssemblyLine);

        // Upgrade MAX -- Temporarily, be revised in the next version
        GTValues.RA.stdBuilder()
            .itemInputs(
                PhotonControllerUpgradeUXV.get(1),
                SpaceWarper.get(64),
                HarmoniousWirelessEnergyHatch.get(2),
                CustomItemList.EOH_Infinite_Energy_Casing.get(64),

                ItemList.Emitter_MAX.get(64),
                GTCMItemList.GravitationalLens.get(64),
                ItemList.EnergisedTesseract.get(64),
                ItemList.Field_Generator_MAX.get(32),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MAX, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialsTST.Axonium, 64),

                MaterialsUEVplus.MagMatter.getNanite(64),
                ZPM6.get(4),
                ItemList.Timepiece.get(4),
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(64))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(65536 * 144),
                MaterialsUEVplus.QuarkGluonPlasma.getFluid(4096 * 1000),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(65536 * 1000),
                MaterialsUEVplus.Time.getMolten(144 * 64 * 512),

                MaterialsUEVplus.Universium.getMolten(144 * 8192),
                MaterialsUEVplus.Eternity.getMolten(144 * 8192),
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 8192),
                Materials.Tritanium.getPlasma(144 * 8192))
            .itemOutputs(PhotonControllerUpgradeMAX.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 20480)
            .addTo(GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipes);

    }
    // spotless:on
}
