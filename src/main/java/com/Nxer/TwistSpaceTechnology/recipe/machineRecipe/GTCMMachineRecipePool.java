package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.*;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
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
import static com.dreammaster.gthandler.CustomItemList.AutoclaveUHV;
import static com.dreammaster.gthandler.CustomItemList.Casing_UEV;
import static com.dreammaster.gthandler.CustomItemList.CentrifugeUV;
import static com.dreammaster.gthandler.CustomItemList.CompressorUHV;
import static com.dreammaster.gthandler.CustomItemList.CuttingMachineUHV;
import static com.dreammaster.gthandler.CustomItemList.ElectrolyzerUV;
import static com.dreammaster.gthandler.CustomItemList.ElectromagneticSeparatorUHV;
import static com.dreammaster.gthandler.CustomItemList.ExtractorUHV;
import static com.dreammaster.gthandler.CustomItemList.FluidExtractorUV;
import static com.dreammaster.gthandler.CustomItemList.FluidSolidifierUV;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static com.dreammaster.gthandler.CustomItemList.MixerUV;
import static com.dreammaster.gthandler.CustomItemList.PikoCircuit;
import static com.dreammaster.gthandler.CustomItemList.PolarizerUHV;
import static com.dreammaster.gthandler.CustomItemList.QuantumCircuit;
import static com.dreammaster.gthandler.CustomItemList.SlicingMachineUHV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_MAX_UXV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UIV_UEV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV;
import static com.dreammaster.gthandler.CustomItemList.Transformer_UXV_UMV;
import static com.dreammaster.gthandler.CustomItemList.WiremillUV;
import static com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry.megaMachines;
import static com.github.technus.tectech.thing.CustomItemList.EOH_Infinite_Energy_Casing;
import static com.github.technus.tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing;
import static com.github.technus.tectech.thing.CustomItemList.LASERpipe;
import static com.github.technus.tectech.thing.CustomItemList.Machine_Multi_Transformer;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8;
import static com.github.technus.tectech.thing.CustomItemList.UncertaintyX_Hatch;
import static com.github.technus.tectech.thing.CustomItemList.eM_Coil;
import static com.github.technus.tectech.thing.CustomItemList.eM_Containment_Field;
import static com.github.technus.tectech.thing.CustomItemList.eM_Hollow;
import static com.github.technus.tectech.thing.CustomItemList.eM_Spacetime;
import static com.github.technus.tectech.thing.CustomItemList.eM_Teleportation;
import static com.github.technus.tectech.thing.CustomItemList.eM_Ultimate_Containment;
import static com.github.technus.tectech.thing.CustomItemList.eM_Ultimate_Containment_Advanced;
import static com.github.technus.tectech.thing.CustomItemList.eM_Ultimate_Containment_Field;
import static com.github.technus.tectech.thing.CustomItemList.hatch_CreativeMaintenance;
import static goodgenerator.util.ItemRefer.Component_Assembly_Line;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Hatch_Air_Intake_Extreme;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Extruder;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_PlatePress;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Mega_AlloyBlastSmelter;

import net.glease.ggfab.GGItemList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.loader.MachineLoader;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.system.material.*;

import appeng.items.materials.MaterialType;
import goodgenerator.items.MyMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;

public class GTCMMachineRecipePool implements IRecipePool {

    // spotless:off
    @Override
    public void loadRecipes() {
        TwistSpaceTechnology.LOG.info("GTCMMachineRecipePool loading recipes.");

        Fluid solderIndAlloy = FluidRegistry.getFluid("molten.indalloy140");

        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        Fluid celestialTungsten = FluidRegistry.getFluid("molten.celestialtungsten");

        IItemContainer eM_Power = com.github.technus.tectech.thing.CustomItemList.eM_Power;

        final IRecipeMap assemblyLine = GT_RecipeConstants.AssemblyLine;
        final IRecipeMap assembler = RecipeMaps.assemblerRecipes;

        // test machine recipe
        /*
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
            // GTNH Version 2.4.1+ don't need call this method , BUT!
            .specialValue(11451) // set special value, like HeatCapacity is the special value of EBF recipes
            .noOptimize() // disable the auto optimize of GT Machine recipes
            .eut(1919810)
            .duration(114514 * 20)
            .addTo(GTCMRecipe.instance.IntensifyChemicalDistorterRecipes);

         */
        /*
         * 2.4.0 and earlier need call these methods:
         * noItemInputs(); noItemOutputs(); noFluidInputs(); noFluidOutputs();
         * So had better call.
         */

        // Intensify Chemical Distorter
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                copyAmount(1, megaMachines[3]),
                Materials.Carbon.getNanite(16),
                ItemList.Emitter_UV.get(16),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV),16})
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))

            .itemOutputs(IntensifyChemicalDistorter.get(1))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(assembler);

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
                copyAmount(64, Ic2Items.iridiumPlate),
                GT_Utility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 128))
            .itemOutputs(copyAmount(1, MachineLoader.PreciseHighEnergyPhotonicQuantumMaster))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(assembler);

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
            .itemOutputs(PhotonControllerUpgradeLV.get(1))
            .eut(RECIPE_LV)
            .duration(20 * 10)
            .addTo(assembler);

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
            .itemOutputs(PhotonControllerUpgradeMV.get(1))
            .eut(RECIPE_MV)
            .duration(20 * 20)
            .addTo(assembler);

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
            .itemOutputs(PhotonControllerUpgradeHV.get(1))
            .eut(RECIPE_HV)
            .duration(20 * 40)
            .addTo(assembler);

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
            .itemOutputs(PhotonControllerUpgradeEV.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 80)
            .addTo(assembler);

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
            .itemOutputs(PhotonControllerUpgradeIV.get(1))

            .eut(RECIPE_IV)
            .duration(20 * 160)
            .addTo(assembler);

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
            .itemOutputs(PhotonControllerUpgradeLuV.get(1))
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
            .itemOutputs(PhotonControllerUpgradeZPM.get(1))

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
            .itemOutputs(PhotonControllerUpgradeUV.get(1))
            .eut(RECIPE_UV)
            .duration(20 * 80)
            .addTo(AssemblyLine);

        // Upgrade UHV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, copyAmount(1, MachineLoader.PreciseHighEnergyPhotonicQuantumMaster))
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
                PhotonControllerUpgradeUHV.get(1)
            )
            .eut(RECIPE_UHV)
            .duration(20*160)
            .addTo(AssemblyLine);

        // Upgrade UEV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUHV.get(1))
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
                PhotonControllerUpgradeUEV.get(1)
            )

            .eut(RECIPE_UEV)
            .duration(20 * 320)
            .addTo(AssemblyLine);

        // Upgrade UIV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUEV.get(1))
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
                PhotonControllerUpgradeUIV.get(1)
            )

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

                EOH_Reinforced_Temporal_Casing.get(4)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 256 * 144),
                Materials.SuperconductorUMVBase.getMolten(64 * 144),
                MaterialsUEVplus.Space.getMolten(144 * 64 * 8),
                MaterialsUEVplus.Time.getMolten(144 * 64 * 8)
            )
            .itemOutputs(
                PhotonControllerUpgradeUMV.get(1)
            )

            .eut(RECIPE_UMV)
            .duration(20 * 1280)
            .addTo(AssemblyLine);

        // Upgrade UXV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                PhotonControllerUpgradeUMV.get(1),
                GT_ModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 64),
                EOH_Infinite_Energy_Casing.get(16),
                Transformer_MAX_UXV.get(1),
                SpaceWarper.get(16),
                ItemList.Tesseract.get(64),
                ItemList.Emitter_UXV.get(16),
                ItemList.Field_Generator_UXV.get(8),
                SpacetimeCompressionFieldGeneratorTier8.get(1),
                TimeAccelerationFieldGeneratorTier8.get(1),
                StabilisationFieldGeneratorTier8.get(1),
                MaterialsUEVplus.Universium.getNanite(16),
                EOH_Reinforced_Temporal_Casing.get(16))
            .itemOutputs(PhotonControllerUpgradeUXV.get(1))
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(144 * 64),
                MaterialsUEVplus.Space.getMolten(144 * 64),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64),
                Materials.SuperconductorUMVBase.getMolten(128 * 144),
                Materials.SuperconductorUIVBase.getMolten(256 * 144),
                Materials.SuperconductorUEVBase.getMolten(512 * 144),
                MyMaterial.shirabon.getMolten(9 * 144 * 144))

            .eut(RECIPE_UXV)
            .duration(2560 * 20)
            .addTo(GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // Upgrade MAX
        GT_Values.RA.stdBuilder()
            .itemInputs(
                PhotonControllerUpgradeUXV.get(1),
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
            .itemOutputs(PhotonControllerUpgradeMAX.get(1))
            .fluidInputs(
                MaterialsUEVplus.Universium.getMolten(144 * 64),
                MaterialsUEVplus.Eternity.getMolten(144 * 64),
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 64),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64 * 8),
                MaterialsUEVplus.Time.getMolten(144 * 64 * 8),
                MaterialsUEVplus.Space.getMolten(144 * 64 * 8))

            .eut(RECIPE_MAX)
            .duration(114514 * 20)
            .addTo(GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipes);

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

                OpticalSOC.get(64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                OpticalSOC.get(64),

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
                copyAmount(1, MachineLoader.MiracleTop)
            )

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
                Materials.Osmiridium.getMolten(144 * 256),
                Materials.SuperconductorUEVBase.getMolten(32 * 144)
            )
            .itemOutputs(
                eM_Spacetime.get(1)
            )

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

            .eut(RECIPE_UV)
            .duration(20*180)
            .addTo(assembler);

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

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20*30)
            .addTo(assembler);

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

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20*384)
            .addTo(assembler);

        // endregion

        // region Infinity Air Intake Hatch
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(10),
                Hatch_Air_Intake_Extreme.get(4),
                ItemList.Electric_Pump_UHV.get(16),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite),8},
                GT_OreDictUnificator.get(OrePrefixes.plate,Materials.DraconiumAwakened,16))
            .fluidInputs(Materials.CosmicNeutronium.getMolten(144*16))
            .itemOutputs(InfiniteAirHatch.get(1))

            .eut(RECIPE_UHV)
            .duration(20*30)
            .addTo(assembler);
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

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20*320)
            .addTo(assembler);

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

            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20*600)
            .addTo(assembler);

        // endregion

        // region HolySeparator
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CuttingMachineUHV.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                CuttingMachineUHV.get(32),
                SlicingMachineUHV.get(32),
                eM_Power.get(16),

                ItemList.Field_Generator_UHV.get(16),
                ItemList.Emitter_UHV.get(64),
                ItemList.Emitter_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(8),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio),16},
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite),16},
                copyAmount(64, Ic2Items.iridiumPlate),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 16),

                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*128),
                Materials.Naquadria.getMolten(144*64),
                Materials.SuperCoolant.getFluid(1000*128)
            )
            .itemOutputs(HolySeparator.get(1))

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

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20*60)
            .addTo(assembler);

        // endregion

        // region SpaceScaler
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CompressorUHV.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                CompressorUHV.get(64),
                ExtractorUHV.get(64),
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

            .eut(RECIPE_UIV)
            .duration(20*180)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfinityCatalyst, 64))

            .itemOutputs(GT_ModHandler.getModItem("Avaritia", "Resource", 1, 5))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(10)
            .addTo(RecipeMaps.compressorRecipes);

        // endregion

        // region Molecule Deconstructor
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ElectrolyzerUV.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                ElectrolyzerUV.get(64),
                CentrifugeUV.get(64),
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

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20*600)
            .addTo(AssemblyLine);

        // endregion

        // region CrystallineInfinitier
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, AutoclaveUHV.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 16),
                eM_Containment_Field.get(4),
                AutoclaveUHV.get(64),
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

            .eut(RECIPE_UEV)
            .duration(20*1800)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .itemInputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 1))
            .fluidOutputs(MaterialPool.HolmiumGarnet.getMolten(144))
            .noOptimize()
            .eut(96)
            .duration(72)
            .addTo(RecipeMaps.fluidExtractionRecipes);

        // endregion

        // region Miracle Door
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, eM_Teleportation.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),

                StellarConstructionFrameMaterial.get(64),
                ItemList.Emitter_UMV.get(64),
                ItemList.Field_Generator_UMV.get(64),
                eM_Teleportation.get(64),

                HighEnergyFlowCircuit.get(64),
                QuantumCircuit.get(64),
                QuantumCircuit.get(64),
                HighEnergyFlowCircuit.get(64),

                SpaceWarper.get(64),
                GravitationalLens.get(64),
                ParticleTrapTimeSpaceShield.get(64),
                ItemList.EnergisedTesseract.get(64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*65536),
                ALLOY.PIKYONIUM.getFluidStack(144*65536),
                Materials.UUMatter.getFluid(1000*65536),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000*16384)
            )
            .itemOutputs(MiracleDoor.get(1))
            .eut(RECIPE_UXV)
            .duration(20*3600)
            .addTo(AssemblyLine);

        // eM_Teleportation blockCasingsTT 10
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_PlasmaForge.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 16),
                eM_Ultimate_Containment.get(4),
                ItemList.Casing_Dim_Bridge.get(4),
                ItemList.Casing_Dim_Injector.get(4),

                StellarConstructionFrameMaterial.get(4),
                ItemList.Field_Generator_UMV.get(3),
                ItemList.Emitter_UMV.get(6),
                PikoCircuit.get(1),

                SpaceWarper.get(24),
                ParticleTrapTimeSpaceShield.get(32),
                GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 16),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 16),

                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 8)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*128),
                ALLOY.PIKYONIUM.getFluidStack(144*64),
                Materials.UUMatter.getFluid(1000*64),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000*16)
            )
            .itemOutputs(eM_Teleportation.get(1))
            .eut(RECIPE_UMV)
            .duration(20*120)
            .addTo(AssemblyLine);

        // endregion

        // region Infinite Dynamo Hatch
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, com.github.technus.tectech.thing.CustomItemList.eM_dynamoMulti64_UMV.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                AnnihilationConstrainer.get(1),
                ItemRefer.Compact_Fusion_Coil_T0.get(1),
                ItemRefer.Compact_Fusion_Coil_T4.get(1),
                Machine_Multi_Transformer.get(1),

                eM_Power.get(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 16),
                ItemList.Field_Generator_UMV.get(2),
                QuantumCircuit.get(4),

                PikoCircuit.get(4),
                ItemList.EnergisedTesseract.get(4),
                ItemList.Tesseract.get(4),
                GravitationalLens.get(8)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*36),
                Materials.UUMatter.getFluid(1000*8),
                MaterialsUEVplus.ExcitedDTSC.getFluid(1000*4)
            )
            .itemOutputs(InfiniteWirelessDynamoHatch.get(1))
            .eut(RECIPE_UXV)
            .duration(20*20)
            .addTo(AssemblyLine);
        // endregion

        // region Ore Processing Factory
        GT_Values.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Ore_Processor.get(1))
            .metadata(RESEARCH_TIME, 16 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                ItemList.Ore_Processor.get(64),
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 64 },
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 64),

                ItemList.Electric_Motor_UEV.get(64),
                ItemList.Electric_Pump_UEV.get(16),
                ItemList.Conveyor_Module_UEV.get(16),
                ItemList.Robot_Arm_UEV.get(16),

                Materials.Neutronium.getNanite(16),
                GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Infinity, 16),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 64),

                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StainlessSteel, 64),
                eM_Power.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*512),
                Materials.TungstenSteel.getMolten(144*1024),
                Materials.Neutronium.getMolten(144*1024),
                Materials.Osmiridium.getMolten(144*1024)
            )
            .itemOutputs(OreProcessingFactory.get(1))

            .eut(RECIPE_UEV)
            .duration(20*1800)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(15),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sphalerite, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(250))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.germanium"), 36)
            )
            .noOptimize()
            .eut(RECIPE_IV)
            .duration((int) (20*37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(20),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(625))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.rhenium"), 36)
            )
            .noOptimize()
            .eut(RECIPE_IV)
            .duration((int) (20*37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(20),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Scheelite, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(1250))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.rhenium"), 18)
            )
            .noOptimize()
            .eut(RECIPE_IV)
            .duration((int) (20*37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(20),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenite, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 8)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(1875))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.rhenium"), 36)
            )
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20*75)
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(21),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Pyrite, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 16)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(1250))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.thallium"), 288)
            )
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20*75)
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        // endregion

        // region Circuit Converter
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                ItemList.Casing_Processor.get(3),
                ItemList.Machine_IV_Boxinator.get(1),

                new Object[]{OrePrefixes.circuit.get(Materials.Elite),1},
                new Object[]{OrePrefixes.circuit.get(Materials.Data),2},
                new Object[]{OrePrefixes.circuit.get(Materials.Advanced),4},

                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 8))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144*32))
            .itemOutputs(CircuitConverter.get(1))

            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20*30)
            .addTo(assembler);

        // endregion

        // region Large Industrial Coking Factory
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_CokeOven.get(64),
                ItemList.PyrolyseOven.get(64),

                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 16),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV),16},
                HighEnergyFlowCircuit.get(16),

                ItemList.Electric_Pump_UV.get(6),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.BlackSteel, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 8)
            )
            .fluidInputs(Materials.SolderingAlloy.getMolten(144*96))
            .itemOutputs(LargeIndustrialCokingFactory.get(1))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20*128)
            .addTo(assembler);
        // endregion

        // region Mega Bricked Blast Furnace
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Casing_Firebricks.get(64),
                ItemList.Machine_Bricked_BlastFurnace.get(64),
                ItemList.Casing_Firebricks.get(64),

                ItemList.Machine_Bricked_BlastFurnace.get(64),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 16),
                ItemList.Machine_Bricked_BlastFurnace.get(64)
            )

            .itemOutputs(MegaBrickedBlastFurnace.get(1))

            .noOptimize()
            .eut(RECIPE_LV)
            .duration(20*114)
            .addTo(assembler);
        // endregion

        // region Dual Input Buffer
        // IV
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1),
                ItemList.Hatch_Input_Multi_2x2_IV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Master),1},
                Materials.TungstenSteel.getPlates(4)
            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144*4))
            .itemOutputs(DualInputBuffer_IV.get(1))
            .eut(RECIPE_IV)
            .duration(20*15)
            .addTo(assembler);

        // LuV
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Hatch_Input_Multi_2x2_LuV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate),1},
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 4)
            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144*8))
            .itemOutputs(DualInputBuffer_LuV.get(1))
            .eut(RECIPE_LuV)
            .duration(20*15)
            .addTo(assembler);

        // ZPM
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                ItemList.Hatch_Input_Multi_2x2_ZPM.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV),1},
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4)

            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144*16))
            .itemOutputs(DualInputBuffer_ZPM.get(1))
            .eut(RECIPE_ZPM)
            .duration(20*15)
            .addTo(assembler);

        // UV
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_UV.get(1),
                ItemList.Hatch_Input_Multi_2x2_UV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite),1},
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)
            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144*32))
            .itemOutputs(DualInputBuffer_UV.get(1))
            .eut(RECIPE_UV)
            .duration(20*15)
            .addTo(assembler);

        // endregion

        // region Scavenger
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_Sifter.get(64),
                CustomItemList.SiftingMachineZPM.get(16),

                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 16},
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 9),
                copyAmount(36,Ic2Items.iridiumPlate),

                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 8)
            )
            .fluidInputs(Materials.SolderingAlloy.getMolten(144*32))
            .itemOutputs(Scavenger.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20*60)
            .addTo(assembler);
        // endregion

        // region BiosphereIII
        ItemStack bioVat = GT_ModHandler.getModItem("gregtech","gt.blockmahines",1,ConfigHandler.IDOffset + GT_Values.VN.length * 7, CustomItemList.BreweryUHV.get(1));

        GT_Values.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, bioVat.copy())
            .metadata(RESEARCH_TIME, 16 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 64),
                copyAmount(64, bioVat),
                copyAmount(64, BioItemList.getPetriDish(null)),
                GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Infinity, 3),

                ItemList.Electric_Pump_UHV.get(16),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 16),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 16),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polybenzimidazole, 64),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 8},
                ItemList.Circuit_Silicon_Wafer7.get(64),
                ItemList.Circuit_Parts_Chip_Bioware.get(64),
                HighEnergyFlowCircuit.get(16),

                copyAmount(64,Ic2Items.iridiumPlate),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*64),
                Materials.Naquadria.getMolten(144*64),
                Materials.CosmicNeutronium.getMolten(144*64),
                MyMaterial.metastableOganesson.getMolten(144*32)
            )
            .itemOutputs(BiosphereIII.get(1))
            .eut(RECIPE_UEV)
            .duration(20*900)
            .addTo(AssemblyLine);
        // endregion

        // region Advanced Oil Cracker
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                megaMachines[4],
                new Object[]{OrePrefixes.circuit.get(Materials.Elite), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Data), 16},
                ItemList.Electric_Pump_IV.get(4)
            )
            .fluidInputs(Materials.SolderingAlloy.getMolten(144*16))
            .itemOutputs(AdvancedMegaOilCracker.get(1))
            .eut(RECIPE_EV)
            .duration(20*3600)
            .addTo(assembler);
        // endregion

        // region Indistinct Tentacle
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GGItemList.AdvAssLine.get(64),
                ItemRefer.Component_Assembly_Line.get(64),
                CustomItemList.AssemblingMachineUMV.get(64),
                ItemRefer.Precise_Assembler.get(64),

                setStackSize(GTCMItemList.StellarConstructionFrameMaterial.get(64),128),
                setStackSize(GTCMItemList.AnnihilationConstrainer.get(64),128),
                setStackSize(GTCMItemList.DysonSphereFrameComponent.get(64),128),
                setStackSize(GTCMItemList.SpaceWarper.get(64),128),

                setStackSize(GTCMItemList.GravitationalLens.get(64),128),
                setStackSize(CustomItemList.QuantumCircuit.get(1),128),
                setStackSize(eM_Ultimate_Containment_Advanced.get(64),128)
            )
            .fluidInputs(
                MaterialsUEVplus.SpaceTime.getMolten(144*524288),
                MaterialsUEVplus.Space.getMolten(144*524288),
                MaterialsUEVplus.Time.getMolten(144*524288),
                MaterialsUEVplus.Eternity.getMolten(144*524288),

                MaterialsUEVplus.WhiteDwarfMatter.getMolten(144*524288),
                MaterialsUEVplus.BlackDwarfMatter.getMolten(144*524288),
                MaterialsUEVplus.Universium.getMolten(144*524288),
                MaterialsUEVplus.RawStarMatter.getFluid(1000*524288),

                MyMaterial.metastableOganesson.getMolten(144*524288),
                MyMaterial.shirabon.getMolten(144*524288),
                Materials.UUMatter.getFluid(1000*2097152),
                new FluidStack(solderPlasma, 144*2097152)
            )
            .itemOutputs(GTCMItemList.IndistinctTentacle.get(1))
            .eut(TierEU.RECIPE_MAX)
            .duration(20 * 14400)
            .addTo(GTCMRecipe.MiracleTopRecipes);

        // endregion

        // region MEG
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.MagicEnergyAbsorber_LV.get(4),
                ItemList.MagicEnergyAbsorber_MV.get(4),
                ItemList.MagicEnergyAbsorber_HV.get(4),
                ItemList.MagicEnergyAbsorber_EV.get(4),
                ItemList.MagicEnergyConverter_LV.get(4),
                ItemList.MagicEnergyConverter_MV.get(4),
                ItemList.MagicEnergyConverter_HV.get(4),
                Materials.Thaumium.getPlates(16),
                GTCMItemList.VoidPollen.get(4)
            )
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(16 * 1000))
            .itemOutputs(GTCMItemList.MegaEggGenerator.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(120 * 20)
            .addTo(assembler);
        // endregion

        // region ThermalEnergyDevourer
        GT_Values.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, megaMachines[1])
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 64),
                copyAmount(64, megaMachines[1]),
                ItemList.Field_Generator_UHV.get(16),
                ItemList.Electric_Pump_UHV.get(64),

                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 16),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 16),
                MaterialType.Singularity.stack(16),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 16},

                ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144*64),
                Materials.NaquadahAlloy.getMolten(144*64),
                Materials.UUMatter.getFluid(1000*64))
            .itemOutputs(ThermalEnergyDevourer.get(1))
            .eut(RECIPE_UEV)
            .duration(20*900)
            .addTo(AssemblyLine);
        // endregion

        // region Debug uncertainty hatch
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                UncertaintyX_Hatch.get(1),
                hatch_CreativeMaintenance.get(1),
                ItemList.Tool_DataOrb.get(1)
            )
            .fluidInputs(MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144*128))
            .itemOutputs(DebugUncertaintyHatch.get(1))
            .eut(RECIPE_UEV)
            .duration(20*120)
            .addTo(assembler);
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(6),
                UncertaintyX_Hatch.get(1),
                hatch_CreativeMaintenance.get(1),
                ItemList.Tool_DataOrb.get(1),
                ItemList.Timepiece.get(1)
            )
            .fluidInputs(MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144*128))
            .itemOutputs(DebugUncertaintyHatch.get(16))
            .eut(RECIPE_UXV)
            .duration(20*120)
            .addTo(assembler);
        // endregion

        // region LaserSmartPipe
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                LASERpipe.get(32),
                Laser_Lens_Special.get(0),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 1},
                Casing_UEV.get(1)
            )
            .fluidInputs(MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144))
            .itemOutputs(LaserSmartNode.get(1))
            .eut(RECIPE_UHV)
            .duration(20*5)
            .addTo(assembler);
        // endregion

        // region VacuumFilterExtractor
        GT_Values.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.DistilleryUV.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 64),
                copyAmount(64,megaMachines[2]),
                Materials.Carbon.getNanite(64),
                ItemList.Field_Generator_UHV.get(8),

                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),

                Laser_Lens_Special.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 16},
                HighEnergyFlowCircuit.get(64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.BlackPlutonium, 64),

                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144*128),
                Materials.Neutronium.getMolten(144*64),
                Materials.BlackPlutonium.getMolten(144*64))
            .itemOutputs(VacuumFilterExtractor.get(1))
            .eut(RECIPE_UEV)
            .duration(20*900)
            .addTo(AssemblyLine);
        // endregion

        // region Eye of Wood
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(17),
                new ItemStack(Items.golden_apple, 1, 1),
                ItemList.Emitter_LV.get(64),
                ItemList.Field_Generator_LV.get(64),
                new Object[]{OrePrefixes.circuit.get(Materials.Basic), 64}
            )
            .itemOutputs(GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.get(1))
            .eut(RECIPE_LV)
            .duration(20*114)
            .addTo(assembler);

        addCraftingRecipe(
            GTCMItemList.EyeOfWood.get(1),
            new Object[] { "ABA", "BCB", "ABA",
                'A', new ItemStack(Blocks.brick_block),
                'B', "plankWood",
                'C', GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.get(1)
            });

        // endregion

        // region Mega Macerator
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_MacerationStack.get(64),
                CustomItemList.MaceratorZPM.get(16),

                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 16},
                WerkstoffLoader.HDCS.get(OrePrefixes.gearGt, 16),
                WerkstoffLoader.AdemicSteel.get(OrePrefixes.gearGt, 16),

                ItemList.Electric_Motor_UV.get(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 8),
                MyMaterial.adamantiumAlloy.get(OrePrefixes.plateDense, 16)
            )
            .fluidInputs(new FluidStack(solderIndAlloy,144*64))
            .itemOutputs(MegaMacerator.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20*300)
            .addTo(assembler);
        // endregion
    }
    // spotless:on
}
