package com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe;

import static com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList.OpticalSOC;
import static com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList.SpaceWarper;
import static com.dreammaster.gthandler.CustomItemList.Transformer_MAX_UXV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UIV_UEV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UXV_UMV;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_UXV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.GTNH_Community.gtnhcommunitymod.GTNHCommunityMod;
import com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList;
import com.GTNH_Community.gtnhcommunitymod.common.machine.recipeMap.GTCMRecipe;
import com.GTNH_Community.gtnhcommunitymod.common.material.MaterialPool;
import com.GTNH_Community.gtnhcommunitymod.loader.MachineLoader;
import com.GTNH_Community.gtnhcommunitymod.recipe.RecipePool;
import com.dreammaster.gthandler.CustomItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.technus.tectech.recipe.TT_recipeAdder;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;

public class GTCMMachineRecipePool implements RecipePool {

    public GTCMMachineRecipePool() {}

    @Override
    public void loadRecipes() {
        GTNHCommunityMod.LOG.info("GTCMMachineRecipePool.loadRecipes");

        Fluid solderIndAlloy = FluidRegistry.getFluid("molten.indalloy140");

        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        IItemContainer eM_Power = com.github.technus.tectech.thing.CustomItemList.eM_Power;

        // test machine recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000),
                Materials.Helium.getGas(1000),
                WerkstoffLoader.Neon.getFluidOrGas(1000),
                Materials.Argon.getGas(1000),
                WerkstoffLoader.Krypton.getFluidOrGas(1000),
                WerkstoffLoader.Xenon.getFluidOrGas(1000),
                Materials.Radon.getGas(1000))
            .fluidOutputs(MaterialPool.TestingMaterial.getMolten(144))
            .noItemOutputs()// GTNH Version 2.4.1+ don't need call this method , BUT!
            .specialValue(11451) // set special value, like HeatCapacity is the special value of EBF recipes
            .noOptimize() // disable the auto optimize of GT Machine recipes
            .eut(1919810)
            .duration(114514 * 20)
            .addTo(GTCMRecipe.instance.IntensifyChemicalDistorterRecipes);
        /*
         * 2.4.0 and earlier need call these methods:
         * noItemInputs(); noItemOutputs(); noFluidInputs(); noFluidOutputs();
         * So had better call.
         */

        // Intensify Chemical Distorter
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(1, ItemRegistry.megaMachines[3]),
                Materials.Carbon.getNanite(1),
                ItemList.Emitter_UV.get(16),
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))
            .noFluidOutputs()
            .itemOutputs(GT_Utility.copyAmount(1, MachineLoader.IntensifyChemicalDistorter))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(sAssemblerRecipes);

        // test
        GT_Values.RA.stdBuilder()
            .itemInputs(
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                GT_Utility.getIntegratedCircuit(10))
            .noFluidInputs()
            .itemOutputs(MaterialPool.TestingMaterial.get(OrePrefixes.dust, 1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(256 * 20)
            .addTo(sMultiblockChemicalRecipes);

        // test
        GT_Values.RA.stdBuilder()
            .itemInputs(
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                GT_Utility.getIntegratedCircuit(10))
            .noFluidInputs()
            .itemOutputs(MaterialPool.TestingMaterial.get(OrePrefixes.dust, 1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(512 * 20)
            .addTo(GTCMRecipe.instance.IntensifyChemicalDistorterRecipes);

        // region PreciseHighEnergyPhotonicQuantumMaster

        // PreciseHighEnergyPhotonicQuantumMaster Controller
        GT_Values.RA.stdBuilder()
            .itemInputs(
                eM_Power.get(4),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 8, 10932),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 1L, 32105),
                ItemList.Emitter_UV.get(5),
                ItemList.Field_Generator_UV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4 },
                GT_Utility.copyAmount(64, Ic2Items.iridiumPlate),
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 128))
            .noFluidOutputs()
            .itemOutputs(GT_Utility.copyAmount(1, MachineLoader.PreciseHighEnergyPhotonicQuantumMaster))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(sAssemblerRecipes);

        // Upgrade LV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11100),
                ItemList.Transformer_MV_LV.get(1),
                ItemList.Emitter_LV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 3),
                ItemList.Field_Generator_LV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.Basic), 4 },
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 2))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeLV.get(1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 10)
            .addTo(sAssemblerRecipes);

        // Upgrade MV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11101),
                ItemList.Transformer_HV_MV.get(1),
                ItemList.Emitter_MV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 6),
                ItemList.Field_Generator_MV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.Good), 4 },
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 8))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeMV.get(1))
            .noFluidOutputs()
            .eut(RECIPE_MV)
            .duration(20 * 20)
            .addTo(sAssemblerRecipes);

        // Upgrade HV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11102),
                ItemList.Transformer_EV_HV.get(1),
                ItemList.Emitter_HV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 12),
                ItemList.Field_Generator_HV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.Advanced), 4 },
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeHV.get(1))
            .noFluidOutputs()
            .eut(RECIPE_HV)
            .duration(20 * 40)
            .addTo(sAssemblerRecipes);

        // Upgrade EV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11103),
                ItemList.Transformer_IV_EV.get(1),
                ItemList.Emitter_EV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 24),
                ItemList.Field_Generator_EV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.Data), 4 },
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 128))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeEV.get(1))
            .noFluidOutputs()
            .eut(RECIPE_EV)
            .duration(20 * 80)
            .addTo(sAssemblerRecipes);

        // Upgrade IV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11104),
                ItemList.Transformer_LuV_IV.get(1),
                ItemList.Emitter_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 48),
                ItemList.Field_Generator_IV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.Elite), 4 },
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 512))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeIV.get(1))
            .noFluidOutputs()
            .eut(RECIPE_IV)
            .duration(20 * 160)
            .addTo(sAssemblerRecipes);

        // Upgrade LuV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11105))
            .metadata(RESEARCH_TIME, 1 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11105),
                ItemList.Transformer_ZPM_LuV.get(1),
                eM_Power.get(4),
                ItemList.Emitter_LuV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 4),
                ItemList.Field_Generator_LuV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.Master), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.Elite), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 4))
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 4))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeLuV.get(1))
            .noFluidOutputs()
            .eut(RECIPE_LuV)
            .duration(20 * 20)
            .addTo(AssemblyLine);

        // Upgrade ZPM
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11106))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11106),
                ItemList.Transformer_UV_ZPM.get(1),
                eM_Power.get(4),
                ItemList.Emitter_ZPM.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 8),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 8),
                ItemList.Field_Generator_ZPM.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.Master), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 4))
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 8))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeZPM.get(1))
            .noFluidOutputs()
            .eut(RECIPE_ZPM)
            .duration(20 * 40)
            .addTo(AssemblyLine);

        // Upgrade UV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11107))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11107),
                ItemList.Transformer_MAX_UV.get(1),
                eM_Power.get(4),
                ItemList.Emitter_UV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 16),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 16),
                ItemList.Field_Generator_UV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 4))
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 16))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeUV.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20 * 80)
            .addTo(AssemblyLine);

        // Upgrade UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GT_Utility.copyAmount(1, MachineLoader.PreciseHighEnergyPhotonicQuantumMaster),
            100000,
            500,
            2000000,
            2,
            new Object[] { ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 8, 11107),
                CustomItemList.Transformer_UEV_UHV.get(1), eM_Power.get(4), ItemList.Emitter_UHV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 32),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 32),
                ItemList.Field_Generator_UHV.get(2), new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 4) },
            new FluidStack[] { new FluidStack(solderPlasma, 16 * 144) },
            GTCMItemList.PhotonControllerUpgradeUHV.get(1),
            20 * 160,
            (int) RECIPE_UHV);

        // Upgrade UEV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.PhotonControllerUpgradeUHV.get(1),
            1000000,
            2000,
            8000000,
            2,
            new Object[] { ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 11107), Transformer_UIV_UEV.get(1),
                eM_Power.get(4), ItemList.Emitter_UEV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 64),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UEV.get(2), new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUEV, 4) },
            new FluidStack[] { new FluidStack(solderPlasma, 64 * 144),
                Materials.SuperconductorUEVBase.getMolten(64 * 144) },
            GTCMItemList.PhotonControllerUpgradeUEV.get(1),
            20 * 320,
            (int) RECIPE_UEV);

        // Upgrade UIV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.PhotonControllerUpgradeUEV.get(1),
            10000000,
            8000,
            32000000,
            2,
            new Object[] { ItemList.Casing_Advanced_Iridium.get(1), SpaceWarper.get(1), Transformer_UMV_UIV.get(1),
                ItemList.Casing_Dim_Injector.get(1), ItemList.Emitter_UIV.get(4),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 4L, 32105),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UIV.get(2), new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUIV, 8) },
            new FluidStack[] { new FluidStack(solderPlasma, 256 * 144),
                Materials.SuperconductorUIVBase.getMolten(64 * 144) },
            GTCMItemList.PhotonControllerUpgradeUIV.get(1),
            20 * 640,
            (int) RECIPE_UIV);

        // Upgrade UMV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            SpaceWarper.get(1),
            100000000,
            32000,
            128000000,
            2,
            new Object[] { ItemList.Casing_Advanced_Iridium.get(1), SpaceWarper.get(4), Transformer_UXV_UMV.get(1),
                ItemList.Casing_Dim_Injector.get(4), ItemList.Emitter_UMV.get(8),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16L, 32105),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UMV.get(4), GT_ModHandler.getModItem("dreamcraft", "item.PikoCircuit", 8),
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 32 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUMV, 16),
                MaterialsUEVplus.TranscendentMetal.getNanite(16),
                com.github.technus.tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing.get(4) },
            new FluidStack[] { new FluidStack(solderPlasma, 256 * 144),
                Materials.SuperconductorUMVBase.getMolten(64 * 144), MaterialsUEVplus.Space.getFluid(144 * 64 * 8),
                MaterialsUEVplus.Time.getFluid(144 * 64 * 8) },
            GTCMItemList.PhotonControllerUpgradeUMV.get(1),
            20 * 1280,
            (int) RECIPE_UMV);
        // Upgrade UXV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.PhotonControllerUpgradeUMV.get(1),
                GT_ModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 64),
                com.github.technus.tectech.thing.CustomItemList.EOH_Infinite_Energy_Casing.get(16),
                Transformer_MAX_UXV.get(1),
                SpaceWarper.get(16),
                ItemList.Tesseract.get(64),
                ItemList.Emitter_UXV.get(16),
                ItemList.Field_Generator_UXV.get(8),
                com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1),
                com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1),
                com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(1),
                MaterialsUEVplus.Universium.getNanite(16),
                com.github.technus.tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing.get(16))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeUXV.get(1))
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(144 * 64),
                MaterialsUEVplus.Space.getMolten(144 * 64),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64),
                Materials.SuperconductorUMVBase.getMolten(128 * 144),
                Materials.SuperconductorUIVBase.getMolten(256 * 144),
                Materials.SuperconductorUEVBase.getMolten(512 * 144),
                MyMaterial.shirabon.getMolten(9 * 144 * 144))
            .noFluidOutputs()
            .eut(RECIPE_UXV)
            .duration(2560 * 20)
            .addTo(GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // Upgrade MAX
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.PhotonControllerUpgradeUXV.get(1),
                SpaceWarper.get(64),
                GT_ModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 15),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 16),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 20),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 21),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 17),
                MaterialsUEVplus.Eternity.getNanite(16),
                MaterialsUEVplus.Universium.getNanite(16),
                ItemList.Timepiece.get(64),
                ItemList.EnergisedTesseract.get(64),
                GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 3, 15))
            .itemOutputs(GTCMItemList.PhotonControllerUpgradeMAX.get(1))
            .fluidInputs(
                MaterialsUEVplus.Universium.getMolten(144 * 64),
                MaterialsUEVplus.Eternity.getMolten(144 * 64),
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 64),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64 * 8),
                MaterialsUEVplus.Time.getMolten(144 * 64 * 8),
                MaterialsUEVplus.Space.getMolten(144 * 64 * 8))
            .noFluidOutputs()
            .eut(RECIPE_MAX)
            .duration(114514 * 20)
            .addTo(GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // Space Wrapper

        // UEV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tesseract.get(16),
                ItemList.EnergisedTesseract.get(16),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1),
                ItemList.Field_Generator_UEV.get(8),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16L, 32105),
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 64), Materials.SuperconductorUEVBase.getMolten(16 * 144))
            .itemOutputs(SpaceWarper.get(1))
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(72))
            .eut(RECIPE_UIV)
            .duration(512 * 20)
            .addTo(GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // UIV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tesseract.get(12),
                ItemList.EnergisedTesseract.get(12),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1),
                ItemList.Field_Generator_UIV.get(4),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 8L, 32105),
                GT_Utility.getIntegratedCircuit(11))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 32), Materials.SuperconductorUIVBase.getMolten(8 * 144))
            .itemOutputs(SpaceWarper.get(2))
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(72))
            .eut(RECIPE_UMV)
            .duration(256 * 20)
            .addTo(GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // UMV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tesseract.get(8),
                ItemList.EnergisedTesseract.get(8),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1),
                ItemList.Field_Generator_UMV.get(2),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 4L, 32105),
                GT_Utility.getIntegratedCircuit(12))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 32), Materials.SuperconductorUMVBase.getMolten(4 * 144))
            .itemOutputs(SpaceWarper.get(4))
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(72))
            .eut(RECIPE_UXV)
            .duration(128 * 20)
            .addTo(GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // UXV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tesseract.get(4),
                ItemList.EnergisedTesseract.get(4),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1),
                ItemList.Field_Generator_UXV.get(1),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 2L, 32105),
                GT_Utility.getIntegratedCircuit(13))
            .fluidInputs(
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 16),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64),
                MaterialsUEVplus.Time.getMolten(144 * 32),
                MaterialsUEVplus.Space.getMolten(144 * 32))
            .itemOutputs(SpaceWarper.get(8))
            .fluidOutputs(Materials.Hydrogen.getPlasma(1000 * 128))
            .eut(RECIPE_MAX)
            .duration(64 * 20)
            .addTo(GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // endregion

        // Optical Soc Circuit Assembly Line
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(16),
                OpticalSOC.get(1),
                com.github.technus.tectech.thing.CustomItemList.DATApipe.get(32),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.EnrichedHolmium, 64))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 2))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))
            .noFluidOutputs()
            .eut(9830400)
            .duration(32 * 20)
            .addTo(GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes);

    }
}
