package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CrystallineInfinitier;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.HolySeparator;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.InfiniteAirHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticDomainConstructor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticDrivePressureFormer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticMixer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhysicalFormSwitcher;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Silksong;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceScaler;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.dreammaster.gthandler.CustomItemList.ElectromagneticSeparatorUHV;
import static com.dreammaster.gthandler.CustomItemList.FluidExtractorUV;
import static com.dreammaster.gthandler.CustomItemList.FluidSolidifierUV;
import static com.dreammaster.gthandler.CustomItemList.MixerUV;
import static com.dreammaster.gthandler.CustomItemList.PolarizerUHV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_MAX_UXV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UIV_UEV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UXV_UMV;
import static com.dreammaster.gthandler.CustomItemList.WiremillUV;
import static com.github.technus.tectech.thing.CustomItemList.eM_Coil;
import static com.github.technus.tectech.thing.CustomItemList.eM_Containment_Field;
import static com.github.technus.tectech.thing.CustomItemList.eM_Hollow;
import static com.github.technus.tectech.thing.CustomItemList.eM_Spacetime;
import static com.github.technus.tectech.thing.CustomItemList.eM_Ultimate_Containment_Field;
import static goodgenerator.util.ItemRefer.Component_Assembly_Line;
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
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Hatch_Air_Intake_Extreme;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Extruder;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_PlatePress;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.DistortionSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.loader.MachineLoader;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.CustomItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.items.MyMaterial;
import goodgenerator.util.ItemRefer;
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

public class GTCMMachineRecipePool implements IRecipePool {

    // spotless:off
    @Override
    public void loadRecipes() {
        DistortionSpaceTechnology.LOG.info("GTCMMachineRecipePool loading recipes.");

        Fluid solderIndAlloy = FluidRegistry.getFluid("molten.indalloy140");

        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        Fluid celestialTungsten = FluidRegistry.getFluid("molten.celestialtungsten");

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
                GT_Utility.getIntegratedCircuit(10),
                GT_Utility.copyAmount(1, ItemRegistry.megaMachines[3]),
                Materials.Carbon.getNanite(16),
                ItemList.Emitter_UV.get(16),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV),16})
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))
            .noFluidOutputs()
            .itemOutputs(IntensifyChemicalDistorter.get(1))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(sAssemblerRecipes);

        // test
        // GT_Values.RA.stdBuilder()
        // .itemInputs(
        // new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
        // GT_Utility.getIntegratedCircuit(10))
        // .noFluidInputs()
        // .itemOutputs(MaterialPool.TestingMaterial.get(OrePrefixes.dust, 1))
        // .noFluidOutputs()
        // .noOptimize()
        // .eut(RECIPE_UHV)
        // .duration(256 * 20)
        // .addTo(sMultiblockChemicalRecipes);
        //
        // // test
        // GT_Values.RA.stdBuilder()
        // .itemInputs(
        // new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
        // GT_Utility.getIntegratedCircuit(10))
        // .noFluidInputs()
        // .itemOutputs(MaterialPool.TestingMaterial.get(OrePrefixes.dust, 1))
        // .noFluidOutputs()
        // .noOptimize()
        // .eut(RECIPE_UHV)
        // .duration(512 * 20)
        // .addTo(GTCMRecipe.instance.IntensifyChemicalDistorterRecipes);

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
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GT_Utility.copyAmount(1, MachineLoader.PreciseHighEnergyPhotonicQuantumMaster))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 8, 11107),
                CustomItemList.Transformer_UEV_UHV.get(1),
                eM_Power.get(4),

                ItemList.Emitter_UHV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 32),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 32),
                ItemList.Field_Generator_UHV.get(2),

                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 4)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 16 * 144)
            )
            .itemOutputs(
                GTCMItemList.PhotonControllerUpgradeUHV.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20*160)
            .addTo(AssemblyLine);

        // Upgrade UEV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.PhotonControllerUpgradeUHV.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 11107),
                Transformer_UIV_UEV.get(1),
                eM_Power.get(4),

                ItemList.Emitter_UEV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 64),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UEV.get(2),

                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUEV, 4)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 64 * 144),
                Materials.SuperconductorUEVBase.getMolten(64 * 144)
            )
            .itemOutputs(
                GTCMItemList.PhotonControllerUpgradeUEV.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UEV)
            .duration(20 * 320)
            .addTo(AssemblyLine);

        // Upgrade UIV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.PhotonControllerUpgradeUEV.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                SpaceWarper.get(1),
                Transformer_UMV_UIV.get(1),
                ItemList.Casing_Dim_Injector.get(1),

                ItemList.Emitter_UIV.get(4),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 4L, 32105),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UIV.get(2),

                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUIV, 8)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 256 * 144),
                Materials.SuperconductorUIVBase.getMolten(64 * 144)
            )
            .itemOutputs(
                GTCMItemList.PhotonControllerUpgradeUIV.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UIV)
            .duration(20 * 640)
            .addTo(AssemblyLine);

        // Upgrade UMV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, SpaceWarper.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                SpaceWarper.get(4),
                Transformer_UXV_UMV.get(1),
                ItemList.Casing_Dim_Injector.get(4),

                ItemList.Emitter_UMV.get(8),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16L, 32105),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UMV.get(4),

                GT_ModHandler.getModItem("dreamcraft", "item.PikoCircuit", 8),
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 32 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUMV, 16),
                MaterialsUEVplus.TranscendentMetal.getNanite(16),

                com.github.technus.tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing.get(4)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 256 * 144),
                Materials.SuperconductorUMVBase.getMolten(64 * 144),
                MaterialsUEVplus.Space.getFluid(144 * 64 * 8),
                MaterialsUEVplus.Time.getFluid(144 * 64 * 8)
            )
            .itemOutputs(
                GTCMItemList.PhotonControllerUpgradeUMV.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 1280)
            .addTo(AssemblyLine);

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
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(36))
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
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(36))
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
            .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten(36))
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
            .itemOutputs(SpaceWarper.get(16))
            .fluidOutputs(Materials.Hydrogen.getPlasma(1000 * 128))
            .eut(RECIPE_MAX)
            .duration(64 * 20)
            .addTo(GTCMRecipe.instance.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // endregion

        // region Miracle Top

        // MiracleTop
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 1, 12735))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 12735),
                Component_Assembly_Line.get(64),
                SpaceWarper.get(64),
                MaterialsUEVplus.TranscendentMetal.getNanite(48),

                GT_ModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 4),
                GT_ModHandler.getModItem("dreamcraft", "item.PikoCircuit", 32),
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 64 },
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 64 },

                GTCMItemList.OpticalSOC.get(64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                GTCMItemList.OpticalSOC.get(64),

                eM_Spacetime.get(16),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 64),
                ItemList.Field_Generator_UIV.get(32),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Infinity, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1024 * 144),
                MaterialsUEVplus.SpaceTime.getMolten(16 * 144),
                Materials.SuperconductorUIVBase.getMolten(64 * 144),
                Materials.SuperconductorUEVBase.getMolten(512 * 144)
            )
            .itemOutputs(
                GT_Utility.copyAmount(1, MachineLoader.MiracleTop)
            )
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20*3600)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, eM_Coil.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                eM_Hollow.get(4),
                SpaceWarper.get(8),
                eM_Coil.get(8),
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 4),

                ItemList.Field_Generator_UEV.get(24),
                ItemList.Casing_Assembler.get(24),
                ItemList.Casing_Gearbox_TungstenSteel.get(24),
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 24 },

                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 2, 14),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 2, 14),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),

                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1024 * 144),
                Materials.UUMatter.getFluid(1000 * 64),
                Materials.OsmiumTetroxide.getMolten(144 * 256),
                Materials.SuperconductorUEVBase.getMolten(32 * 144)
            )
            .itemOutputs(
                eM_Spacetime.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20*512)
            .addTo(AssemblyLine);


        // endregion

        // region MagneticDrivePressureFormer
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Industrial_Extruder.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                ItemList.Casing_MiningOsmiridium.get(64),
                Industrial_Extruder.get(64),
                Industrial_PlatePress.get(64),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV),64},

                ItemList.UV_Coil.get(64),
                GT_OreDictUnificator.get(OrePrefixes.block,Materials.Neutronium,64),
                GT_OreDictUnificator.get(OrePrefixes.block,Materials.Neutronium,64),
                ItemList.UV_Coil.get(64),

                ItemList.Field_Generator_UV.get(2),
                ItemList.Robot_Arm_UV.get(16),
                ItemList.Conveyor_Module_UV.get(16),
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 32),

                GT_OreDictUnificator.get(OrePrefixes.plateDense,Materials.Neutronium,32),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02,Materials.SuperconductorUV,64)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy,144*256),
                Materials.Naquadria.getMolten(144*256),
                Materials.Lubricant.getFluid(1000*256),
                Materials.Samarium.getMolten(144*256)
            )
            .itemOutputs(MagneticDrivePressureFormer.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20*256)
            .addTo(AssemblyLine);

        // endregion

        // region PhysicalFormSwitcher
        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(10),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt,Materials.Naquadria,16),
                        FluidExtractorUV.get(64),

                        FluidSolidifierUV.get(64),
                        ItemList.Field_Generator_UV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense,Materials.NaquadahAlloy,16),

                        new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV),16},
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04,Materials.SuperconductorUV,8)
                    )
                    .fluidInputs(Materials.Iridium.getMolten(144*64))
                    .itemOutputs(PhysicalFormSwitcher.get(1))
                    .noFluidOutputs()
                    .eut(RECIPE_UV)
                    .duration(20*180)
                    .addTo(GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);

        // Containment Field casing
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(11),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                ItemList.Field_Generator_LuV.get(4),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate),8},
                GT_OreDictUnificator.get(OrePrefixes.cableGt01,Materials.Naquadah,4),
                GT_OreDictUnificator.get(OrePrefixes.plate,Materials.Steel,8))
            .fluidInputs(Materials.NaquadahAlloy.getMolten(144*4))
            .itemOutputs(GT_ModHandler.getModItem("gregtech","gt.blockcasings2",1,8))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20*30)
            .addTo(GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);

        // endregion

        // region MagneticMixer
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16),
                MixerUV.get(64),
                ItemList.Field_Generator_UV.get(16),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16},
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8)
            )
            .fluidInputs(Materials.Iridium.getMolten(144*64))
            .itemOutputs(MagneticMixer.get(1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20*384)
            .addTo(GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);

        // endregion

        // region Infinity Air Intake Hatch
        GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.getIntegratedCircuit(10),
                                Hatch_Air_Intake_Extreme.get(4),
                                ItemList.Electric_Pump_UHV.get(16),
                                new Object[]{OrePrefixes.circuit.get(Materials.Infinite),8},
                                GT_OreDictUnificator.get(OrePrefixes.plate,Materials.DraconiumAwakened,16))
                    .fluidInputs(Materials.Cosmic.getMolten(144*16))
                    .itemOutputs(InfiniteAirHatch.get(1))
                    .noFluidOutputs()
                    .eut(RECIPE_UHV)
                    .duration(20*30)
                    .addTo(GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);
        // endregion

        // region MagneticDomainConstructor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 16),
                ElectromagneticSeparatorUHV.get(16),

                PolarizerUHV.get(16),
                ItemList.Field_Generator_UV.get(3),
                ItemList.Robot_Arm_UHV.get(8),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio),16},
                GT_OreDictUnificator.get(OrePrefixes.plate,Materials.BlackPlutonium,64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04,Materials.SuperconductorUHV,16)
            )
            .fluidInputs(new FluidStack(solderPlasma,144*64))
            .itemOutputs(MagneticDomainConstructor.get(1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20*320)
            .addTo(GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);

        // endregion

        // region Silksong
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                WiremillUV.get(16),

                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 8),
                ItemList.Robot_Arm_ZPM.get(16),
                ItemList.Conveyor_Module_ZPM.get(16),

                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate),8},
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 16),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 16)
            )
            .fluidInputs(Materials.Iridium.getMolten(144*32))
            .itemOutputs(Silksong.get(1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20*600)
            .addTo(GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);

        // endregion

        // region HolySeparator
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.CuttingMachineUHV.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                CustomItemList.CuttingMachineUHV.get(32),
                CustomItemList.SlicingMachineUHV.get(32),
                eM_Power.get(16),

                ItemList.Field_Generator_UHV.get(16),
                ItemList.Emitter_UHV.get(64),
                ItemList.Emitter_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(8),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio),16},
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite),16},
                GT_Utility.copyAmount(64, Ic2Items.iridiumPlate),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 16),

                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*128),
                Materials.Naquadria.getMolten(144*64),
                Materials.SuperCoolant.getFluid(1000*128)
            )
            .itemOutputs(HolySeparator.get(1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20*1200)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),

                eM_Hollow.get(4),
                ItemList.Field_Generator_UHV.get(8),
                ItemList.Field_Generator_UV.get(16),

                ItemList.Field_Generator_ZPM.get(64),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(16),
                eM_Power.get(4),

                new Object[]{OrePrefixes.circuit.get(Materials.Infinite),6},
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2)
            )
            .fluidInputs(new FluidStack(solderPlasma, 144*32))
            .itemOutputs(eM_Containment_Field.get(4))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20*60)
            .addTo(sAssemblerRecipes);

        // endregion

        // region SpaceScaler
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.CompressorUHV.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                CustomItemList.CompressorUHV.get(64),
                CustomItemList.ExtractorUHV.get(64),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.CosmicNeutronium, 64),

                ItemList.Field_Generator_UEV.get(32),
                ItemList.Field_Generator_UHV.get(64),
                ItemList.Field_Generator_UHV.get(64),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 16),

                new Object[]{OrePrefixes.circuit.get(Materials.Optical), 32},
                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 32),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),

                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*256),
                Materials.UUMatter.getFluid(1000*256),
                Materials.SuperCoolant.getFluid(1000*256),
                Materials.NaquadahAlloy.getMolten(144*256)
            )
            .itemOutputs(SpaceScaler.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UEV)
            .duration(20*1800)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, eM_Containment_Field.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                eM_Containment_Field.get(4),
                ItemList.Field_Generator_UIV.get(16),
                ItemList.Field_Generator_UEV.get(64),
                SpaceWarper.get(4),

                ItemList.Tesseract.get(32),
                ItemList.EnergisedTesseract.get(32),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 32),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 32),

                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*256),
                new FluidStack(celestialTungsten, 144*256),
                Materials.Infinity.getMolten(144*32)
            )
            .itemOutputs(eM_Ultimate_Containment_Field.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UIV)
            .duration(20*180)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfinityCatalyst, 64))
            .noFluidInputs()
            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Resource", 1, 5))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(10)
            .addTo(GT_Recipe.GT_Recipe_Map.sCompressorRecipes);

        // endregion

        // region Molecule Deconstructor
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.ElectrolyzerUV.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                CustomItemList.ElectrolyzerUV.get(64),
                CustomItemList.CentrifugeUV.get(64),
                Materials.Carbon.getNanite(16),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                ItemList.Emitter_UV.get(16),
                ItemList.Field_Generator_UV.get(8),
                ItemList.Electric_Pump_UV.get(32),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 16},
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 64},
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*256),
                Materials.Osmiridium.getMolten(144*256),
                Materials.UUMatter.getFluid(1000*64),
                Materials.SuperCoolant.getFluid(1000*128)
            )
            .itemOutputs(MoleculeDeconstructor.get(1))
            .noFluidOutputs()
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20*600)
            .addTo(AssemblyLine);

        // endregion
        
        // region CrystallineInfinitier
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.AutoclaveUHV.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 16),
                eM_Containment_Field.get(4),
                CustomItemList.AutoclaveUHV.get(64),
                ItemList.Electric_Pump_UEV.get(16),
                
                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},
                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*256),
                Materials.NaquadahAlloy.getMolten(144*256),
                Materials.UUMatter.getFluid(1000*128)
            )
            .itemOutputs(CrystallineInfinitier.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UEV)
            .duration(20*1800)
            .addTo(AssemblyLine);
            
        
        // endregon

    }
    // spotless:on
}
