package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static com.github.technus.tectech.thing.CustomItemList.DATApipe;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_UXV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.SpaceTimeBendingCore;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.dreammaster.gthandler.CustomItemList;
import com.dreammaster.gthandler.GT_CoreModSupport;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

// spotless:off
public class MiracleTopRecipePool implements IRecipePool {
    static final GT_Recipe.GT_Recipe_Map MT = GTCMRecipe.instance.MiracleTopRecipes;

    @Override
    public void loadRecipes() {

        TwistSpaceTechnology.LOG.info("MiracleTopRecipePool loading recipes.");

        Fluid solderIndAlloy = FluidRegistry.getFluid("molten.indalloy140");
        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");


        final ItemStack Wrapped_Circuit_Chip_Ram = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32735);
        final ItemStack Wrapped_Circuit_Chip_NanoCPU = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32720);

        final ItemStack Wrapped_Circuit_Parts_CapacitorASMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32737);
        final ItemStack Wrapped_Circuit_Parts_TransistorASMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32738);
        final ItemStack Wrapped_Circuit_Parts_DiodeASMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32739);
        final ItemStack Wrapped_Circuit_Parts_ResistorASMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32740);
        final ItemStack Wrapped_Circuit_Parts_InductorASMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32707);

        final ItemStack Wrapped_Circuit_Parts_CapacitorXSMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32708);
        final ItemStack Wrapped_Circuit_Parts_TransistorXSMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32709);
        final ItemStack Wrapped_Circuit_Parts_DiodeXSMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32710);
        final ItemStack Wrapped_Circuit_Parts_ResistorXSMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32711);

        final ItemStack Wrapped_Circuit_Board_Optical = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32704);
        final ItemStack Wrapped_Optically_Perfected_CPU = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32703);
        final ItemStack Wrapped_Optically_Compatible_Memory = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32701);
        final ItemStack Wrapped_Circuit_Parts_InductorXSMD = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32706);

        final ItemStack Wrapped_Circuit_Board_Bio_Ultra = GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32746);

        // region Quantum Circuit and Piko Circuit
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("dreamcraft", "item.PikoCircuit", 2),
                ItemList.Circuit_Parts_CapacitorXSMD.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                ItemList.Circuit_Parts_TransistorXSMD.get(64),
                ItemList.Circuit_Parts_ResistorXSMD.get(64),
                ItemList.Circuit_Chip_QPIC.get(64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 3744),
                Materials.UUMatter.getFluid(1000 * 24),
                Materials.Osmium.getMolten(144 * 16),
                Materials.Neutronium.getMolten(144 * 8),
                MyMaterial.shirabon.getMolten(144 * 8),
                Materials.Indium.getMolten(144 * 8),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 4),
                Materials.Lanthanum.getMolten(144 * 2)
            )
            .itemOutputs(GT_ModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 1))
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 1000)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(1L),
                getItemContainer("PicoWafer").get(4L),
                ItemList.Circuit_OpticalMainframe.get(2),
                ItemList.Circuit_Parts_TransistorXSMD.get(48L),
                ItemList.Circuit_Parts_ResistorXSMD.get(48L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(48L),
                ItemList.Circuit_Parts_DiodeXSMD.get(48L),
                ItemList.Circuit_Chip_PPIC.get(64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 3744),
                Materials.UUMatter.getFluid(1000 * 8),
                Materials.Osmium.getMolten(144 * 8),
                GT_CoreModSupport.RadoxPolymer.getMolten(144 * 4),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 4),
                Materials.Neutronium.getMolten(144 * 2),
                Materials.Lanthanum.getMolten(144 * 8)
            )
            .itemOutputs(GT_ModHandler.getModItem("dreamcraft", "item.PikoCircuit", 1))
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 500)
            .addTo(MT);
        // endregion

        // region Optical Circuit

        // Optical SoC frame
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(21),
                SpaceTimeBendingCore.get(0),
                ItemList.Optical_Cpu_Containment_Housing.get(2),
                Materials.Glowstone.getNanite(4)
            )
            .fluidInputs(
                MaterialsUEVplus.Space.getMolten(144),
                MaterialsUEVplus.Time.getMolten(144),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 2)
            )
            .itemOutputs(
                GTCMItemList.ParticleTrapTimeSpaceShield.get(1)
            )
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(2500))
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);


        // Optical Frame
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.Silicone.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 500)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.StyreneButadieneRubber.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 500)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 32L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.Silicone.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 500)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 32L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.StyreneButadieneRubber.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 500)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 16L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.Silicone.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 500)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 16L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 3744),
                Materials.Radon.getPlasma(5760),
                Materials.SuperCoolant.getFluid(1000 * 40),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 2),
                Materials.Tritanium.getMolten(144 * 16),
                Materials.StyreneButadieneRubber.getMolten(144 * 32),
                Materials.Polybenzimidazole.getMolten(144 * 32)
            )
            .itemOutputs(ItemList.Circuit_OpticalMainframe.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        // Optical Computer
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(2L),
                ItemList.Circuit_OpticalAssembly.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(24L),
                ItemList.Circuit_Parts_ResistorXSMD.get(24L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(24L),
                ItemList.Circuit_Parts_DiodeXSMD.get(24L),
                ItemList.Circuit_Chip_NOR.get(64L),
                ItemList.Circuit_Chip_SoC2.get(32L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 20),
                Materials.Radon.getPlasma(144 * 20),
                Materials.SuperCoolant.getFluid(1000L * 20),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000),
                MyMaterial.lumiium.getMolten(144 * 4),
                Materials.Silicone.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalComputer.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 200)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(2L),
                ItemList.Circuit_OpticalAssembly.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(24L),
                ItemList.Circuit_Parts_ResistorXSMD.get(24L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(24L),
                ItemList.Circuit_Parts_DiodeXSMD.get(24L),
                ItemList.Circuit_Chip_NOR.get(64L),
                ItemList.Circuit_Chip_SoC2.get(32L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 20),
                Materials.Radon.getPlasma(144 * 20),
                Materials.SuperCoolant.getFluid(1000L * 20),
                WerkstoffLoader.Oganesson.getFluidOrGas(1000),
                MyMaterial.lumiium.getMolten(144 * 4),
                Materials.StyreneButadieneRubber.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalComputer.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 200)
            .addTo(MT);

        // Optical Assembly
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(1L),
                ItemList.Circuit_OpticalProcessor.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(16L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(20L),
                ItemList.Circuit_Parts_ResistorXSMD.get(20L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1440),
                Materials.Radon.getPlasma(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                WerkstoffLoader.Oganesson.getFluidOrGas(500),
                MyMaterial.lumiium.getMolten(144 * 3),
                Materials.Silicone.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalAssembly.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Optical.get(1L),
                ItemList.Circuit_OpticalProcessor.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(16L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(20L),
                ItemList.Circuit_Parts_ResistorXSMD.get(20L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1440),
                Materials.Radon.getPlasma(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                WerkstoffLoader.Oganesson.getFluidOrGas(500),
                MyMaterial.lumiium.getMolten(144 * 3),
                Materials.StyreneButadieneRubber.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalAssembly.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(MT);

        // Optical Processor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Optically_Perfected_CPU),
                Utils.copyAmount(2, Wrapped_Optically_Compatible_Memory),
                Utils.copyAmount(16, Wrapped_Circuit_Parts_CapacitorXSMD),
                Utils.copyAmount(16, Wrapped_Circuit_Parts_DiodeXSMD),
                DATApipe.get(64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 2),
                Materials.EnrichedHolmium.getMolten(144 * 8)
            )
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 240)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Optical),
                GTCMItemList.OpticalSOC.get(1),
                DATApipe.get(16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 2),
                Materials.EnrichedHolmium.getMolten(144 * 2)
            )
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))
            .noFluidOutputs()
            .eut(9830400)
            .duration(20 * 10)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                Utils.copyAmount(16, Wrapped_Circuit_Board_Optical),
                GTCMItemList.OpticalSOC.get(16),
                DATApipe.get(64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 32),
                Materials.EnrichedHolmium.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_OpticalProcessor.get(64),
                ItemList.Circuit_OpticalProcessor.get(64),
                ItemList.Circuit_OpticalProcessor.get(64),
                ItemList.Circuit_OpticalProcessor.get(64))
            .noFluidOutputs()
            .eut(9830400 * 4)
            .duration(20 * 10 * 4)
            .addTo(MT);

        // endregion

        // region Bio Circuit

        // Bio Mainframe
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorASMD.get(24L),
                ItemList.Circuit_Parts_TransistorASMD.get(24L),
                ItemList.Circuit_Parts_ResistorASMD.get(24L),
                ItemList.Circuit_Parts_CapacitorASMD.get(24L),
                ItemList.Circuit_Parts_DiodeASMD.get(24L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
                }
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 2880),
                Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L),
                Materials.Tritanium.getMolten(144 * 8),
                Materials.Silicone.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biomainframe.get(1L)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 300)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(6L),
                ItemList.Circuit_Parts_TransistorXSMD.get(6L),
                ItemList.Circuit_Parts_ResistorXSMD.get(6L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(6L),
                ItemList.Circuit_Parts_DiodeXSMD.get(6L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
                }
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 2880),
                Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L),
                Materials.Tritanium.getMolten(144 * 8),
                Materials.Silicone.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biomainframe.get(1L)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 150)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorASMD.get(24L),
                ItemList.Circuit_Parts_TransistorASMD.get(24L),
                ItemList.Circuit_Parts_ResistorASMD.get(24L),
                ItemList.Circuit_Parts_CapacitorASMD.get(24L),
                ItemList.Circuit_Parts_DiodeASMD.get(24L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
                }
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 2880),
                Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L),
                Materials.Tritanium.getMolten(144 * 8),
                Materials.StyreneButadieneRubber.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biomainframe.get(1L)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 300)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(6L),
                ItemList.Circuit_Parts_TransistorXSMD.get(6L),
                ItemList.Circuit_Parts_ResistorXSMD.get(6L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(6L),
                ItemList.Circuit_Parts_DiodeXSMD.get(6L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
                }
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 2880),
                Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L),
                Materials.Tritanium.getMolten(144 * 8),
                Materials.StyreneButadieneRubber.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biomainframe.get(1L)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 150)
            .addTo(MT);

        // Bio SuperComputer
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Bio_Ultra.get(2L),
                ItemList.Circuit_Biowarecomputer.get(2L),
                ItemList.Circuit_Parts_TransistorASMD.get(16L),
                ItemList.Circuit_Parts_ResistorASMD.get(16L),
                ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                ItemList.Circuit_Parts_DiodeASMD.get(16L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1440),
                Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                Materials.NiobiumTitanium.getMolten(144 * 4),
                Materials.Silicone.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biowaresupercomputer.get(1L)
            )
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20 * 200)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Bio_Ultra.get(2L),
                ItemList.Circuit_Biowarecomputer.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(4L),
                ItemList.Circuit_Parts_ResistorXSMD.get(4L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(4L),
                ItemList.Circuit_Parts_DiodeXSMD.get(4L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1440),
                Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                Materials.NiobiumTitanium.getMolten(144 * 4),
                Materials.Silicone.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biowaresupercomputer.get(1L)
            )
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20 * 100)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Bio_Ultra.get(2L),
                ItemList.Circuit_Biowarecomputer.get(2L),
                ItemList.Circuit_Parts_TransistorASMD.get(16L),
                ItemList.Circuit_Parts_ResistorASMD.get(16L),
                ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                ItemList.Circuit_Parts_DiodeASMD.get(16L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1440),
                Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                Materials.NiobiumTitanium.getMolten(144 * 4),
                Materials.StyreneButadieneRubber.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biowaresupercomputer.get(1L)
            )
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20 * 200)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Board_Bio_Ultra.get(2L),
                ItemList.Circuit_Biowarecomputer.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(4L),
                ItemList.Circuit_Parts_ResistorXSMD.get(4L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(4L),
                ItemList.Circuit_Parts_DiodeXSMD.get(4L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1440),
                Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L),
                Materials.NiobiumTitanium.getMolten(144 * 4),
                Materials.StyreneButadieneRubber.getMolten(144 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_Biowaresupercomputer.get(1L)
            )
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20 * 100)
            .addTo(MT);

        // Bio Assembly
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Bio_Ultra),
                ItemList.Circuit_Bioprocessor.get(32),
                Utils.copyAmount(12, Wrapped_Circuit_Parts_InductorASMD),
                Utils.copyAmount(16, Wrapped_Circuit_Parts_CapacitorASMD),
                Utils.copyAmount(32, Wrapped_Circuit_Chip_Ram)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                Materials.NiobiumTitanium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Biowarecomputer.get(16)
            )
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20 * 240)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Bio_Ultra),
                ItemList.Circuit_Bioprocessor.get(32),
                Utils.copyAmount(3, Wrapped_Circuit_Parts_InductorXSMD),
                Utils.copyAmount(4, Wrapped_Circuit_Parts_CapacitorXSMD),
                Utils.copyAmount(32, Wrapped_Circuit_Chip_Ram)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                Materials.NiobiumTitanium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Biowarecomputer.get(16)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 30)
            .addTo(MT);

        // Bio Processor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32714),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32716),
                Utils.copyAmount(2, Wrapped_Circuit_Chip_NanoCPU),
                Utils.copyAmount(12, Wrapped_Circuit_Parts_CapacitorASMD),
                Utils.copyAmount(12, Wrapped_Circuit_Parts_TransistorASMD)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.NiobiumTitanium.getMolten(144 * 32)
            )
            .itemOutputs(
                ItemList.Circuit_Bioprocessor.get(16)
            )
            .noFluidOutputs()
            .eut(RECIPE_UV)
            .duration(20 * 180)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32714),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32716),
                Utils.copyAmount(2, Wrapped_Circuit_Chip_NanoCPU),
                Utils.copyAmount(3, Wrapped_Circuit_Parts_CapacitorXSMD),
                Utils.copyAmount(3, Wrapped_Circuit_Parts_TransistorXSMD)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.NiobiumTitanium.getMolten(144 * 32)
            )
            .itemOutputs(
                ItemList.Circuit_Bioprocessor.get(16)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(444)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Bio_Ultra),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32699)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                new FluidStack(FluidRegistry.getFluid("molten.chromaticglass"), 144 * 8),
                Materials.NiobiumTitanium.getMolten(144 * 32)
            )
            .itemOutputs(
                ItemList.Circuit_Bioprocessor.get(16)
            )
            .noFluidOutputs()
            .eut(RECIPE_UEV)
            .duration(450)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                Utils.copyAmount(12, Wrapped_Circuit_Board_Bio_Ultra),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32699)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144 * 12),
                new FluidStack(FluidRegistry.getFluid("molten.chromaticglass"), 144 * 8 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 32 * 12)
            )
            .itemOutputs(
                ItemList.Circuit_Bioprocessor.get(64),
                ItemList.Circuit_Bioprocessor.get(64),
                ItemList.Circuit_Bioprocessor.get(64),
                ItemList.Circuit_Bioprocessor.get(64)
            )
            .noFluidOutputs()
            .eut(RECIPE_UIV)
            .duration(450 * 3)
            .addTo(MT);

        // endregion

        // region Wetware Processor SoC
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32750),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32700)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                Materials.YttriumBariumCuprate.getMolten(144 * 16),
                Materials.CosmicNeutronium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Neuroprocessor.get(16)
            )
            .noFluidOutputs()
            .eut(614400)
            .duration(20 * 15)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32750),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32700)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144 * 12),
                Materials.YttriumBariumCuprate.getMolten(144 * 16 * 12),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 12)
            )
            .itemOutputs(
                ItemList.Circuit_Neuroprocessor.get(64),
                ItemList.Circuit_Neuroprocessor.get(64),
                ItemList.Circuit_Neuroprocessor.get(64),
                ItemList.Circuit_Neuroprocessor.get(64)
            )
            .noFluidOutputs()
            .eut(614400 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Crystal Processor SoC
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32753),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32717)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.NiobiumTitanium.getMolten(144 * 16),
                Materials.YttriumBariumCuprate.getMolten(144 * 8))
            .itemOutputs(
                ItemList.Circuit_Crystalprocessor.get(16)
            )
            .noFluidOutputs()
            .eut(153600)
            .duration(20 * 15)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32753),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32717)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 16 * 12),
                Materials.YttriumBariumCuprate.getMolten(144 * 8 * 12))
            .itemOutputs(
                ItemList.Circuit_Crystalprocessor.get(64),
                ItemList.Circuit_Crystalprocessor.get(64),
                ItemList.Circuit_Crystalprocessor.get(64),
                ItemList.Circuit_Crystalprocessor.get(64)
            )
            .noFluidOutputs()
            .eut(153600 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Quantum Processor SoC
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32754),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.Platinum.getMolten(144 * 32),
                Materials.NiobiumTitanium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Quantumprocessor.get(16)
            )
            .noFluidOutputs()
            .eut(38400)
            .duration(20 * 15)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32754),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.Platinum.getMolten(144 * 32 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 8 * 12)
            )
            .itemOutputs(
                ItemList.Circuit_Quantumprocessor.get(64),
                ItemList.Circuit_Quantumprocessor.get(64),
                ItemList.Circuit_Quantumprocessor.get(64),
                ItemList.Circuit_Quantumprocessor.get(64)
            )
            .noFluidOutputs()
            .eut(38400 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Nano Processor SoC
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32756),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.Electrum.getMolten(144 * 16),
                Materials.Platinum.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Nanoprocessor.get(16)
            )
            .noFluidOutputs()
            .eut(9600)
            .duration(20 * 15)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32756),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.Electrum.getMolten(144 * 16 * 12),
                Materials.Platinum.getMolten(144 * 8 * 12)
            )
            .itemOutputs(
                ItemList.Circuit_Nanoprocessor.get(64),
                ItemList.Circuit_Nanoprocessor.get(64),
                ItemList.Circuit_Nanoprocessor.get(64),
                ItemList.Circuit_Nanoprocessor.get(64)
            )
            .noFluidOutputs()
            .eut(9600 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region High Energy Flow Circuit
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32753),
                GT_ModHandler.getModItem("GoodGenerator", "circuitWrap", 2, 7),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 8, 32721)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 288),
                Materials.Infinity.getMolten(144)
            )
            .itemOutputs(
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 16)
            )
            .noFluidOutputs()
            .eut(RECIPE_IV)
            .duration(20 * 720)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32753),
                GT_ModHandler.getModItem("GoodGenerator", "circuitWrap", 24, 7),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 48, 32721)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 288 * 12),
                Materials.Infinity.getMolten(144 * 12)
            )
            .itemOutputs(
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 64),
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 64),
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 64),
                GT_ModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 64)

            )
            .noFluidOutputs()
            .eut(RECIPE_LuV)
            .duration(20 * 720 * 3)
            .addTo(MT);

//ULV LV and MV circuit
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32756),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 48, 32728)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(18 * 4),
                Materials.AnnealedCopper.getMolten(144 * 2 * 4),
                Materials.RedAlloy.getMolten(144 * 8)
            )
            .itemOutputs(
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64),
                CustomItemList.NandChipBoard.get(64)
            )
            .noFluidOutputs()
            .eut(RECIPE_EV)
            .duration(20 * 90 * 4)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32748),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32731)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(72 * 12),
                Materials.Copper.getMolten(144 * 96)
            )
            .itemOutputs(
                ItemList.Circuit_Microprocessor.get(64),
                ItemList.Circuit_Microprocessor.get(64),
                ItemList.Circuit_Microprocessor.get(64),
                ItemList.Circuit_Microprocessor.get(64),
                ItemList.Circuit_Microprocessor.get(64),
                ItemList.Circuit_Microprocessor.get(64),
                ItemList.Circuit_Microprocessor.get(64),
                ItemList.Circuit_Microprocessor.get(64)
            )
            .noFluidOutputs()
            .eut(600)
            .duration(20 * 30 * 4)
            .addTo(MT);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(16),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32748),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32731)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(72 * 12),
                Materials.AnnealedCopper.getMolten(144 * 8 * 12),
                Materials.RedAlloy.getMolten(144 * 24)
            )
            .itemOutputs(
                GT_ModHandler.getModItem("gregtech", "gt.metaitem.03", 64, 32080),
                GT_ModHandler.getModItem("gregtech", "gt.metaitem.03", 64, 32080),
                GT_ModHandler.getModItem("gregtech", "gt.metaitem.03", 64, 32080),
                GT_ModHandler.getModItem("gregtech", "gt.metaitem.03", 64, 32080)
            )
            .noFluidOutputs()
            .eut(RECIPE_EV)
            .duration(20 * 90 * 4)
            .addTo(MT);

        // endregion

        // region Neuro Processing Unit and Bio Processing Unit
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32750),
                ItemList.Circuit_Chip_Stemcell.get(64),
                ItemList.Circuit_Chip_Stemcell.get(64),
                ItemList.Circuit_Chip_Stemcell.get(64),
                ItemList.Circuit_Chip_Stemcell.get(64)
            )
            .fluidInputs(
                Materials.ReinforceGlass.getMolten(16 * 16 * 288),
                Materials.Polybenzimidazole.getMolten(16 * 8 * 72),
                Materials.NaquadahEnriched.getMolten(16 * 4 * 72),
                Materials.Silicone.getMolten(16 * 16 * 144),
                Materials.TungstenSteel.getMolten(16 * 32 * 18),
                Materials.GrowthMediumSterilized.getFluid(16 * 250),
                Materials.UUMatter.getFluid(16 * 250),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000 * 16)
            )
            .itemOutputs(
                GT_ModHandler.getModItem("gregtech", "gt.metaitem.03", 16, 32072)
            )
            .noFluidOutputs()
            .eut(RECIPE_ZPM)
            .duration(20 * 30 * 12)
            .addTo(MT);

        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32746),
                ItemList.Circuit_Chip_Biocell.get(64),
                ItemList.Circuit_Chip_Biocell.get(64),
                ItemList.Circuit_Chip_Biocell.get(64),
                ItemList.Circuit_Chip_Biocell.get(64)
            )
            .fluidInputs(
                Materials.ReinforceGlass.getMolten(16 * 16 * 288),
                Materials.Polybenzimidazole.getMolten(16 * 16 * 72),
                Materials.ElectrumFlux.getMolten(16 * 16 * 72),
                Materials.Silicone.getMolten(16 * 16 * 144),
                Materials.HSSS.getMolten(16 * 32 * 18),
                Materials.BioMediumSterilized.getFluid(16 * 500),
                Materials.UUMatter.getFluid(16 * 500),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000 * 16)
            )
            .itemOutputs(
                GT_ModHandler.getModItem("gregtech", "gt.metaitem.03", 16, 32077)
            )
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 30 * 12)
            .addTo(MT);
        // endregion

        // region Proof Of Heroes
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.SpaceWarper.get(64),
                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 64),
                GT_ModHandler.getModItem("eternalsingularity", "combined_singularity", 64, 15),
                ItemList.Timepiece.get(64),
                ItemList.GigaChad.get(64),
                com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                GT_ModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 15),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 16),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 20),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 21),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 17),
                ItemList.ZPM6.get(64),
                GTCMItemList.ArtificialStar.get(64)
            )
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(1000 * 114514),
                MaterialsUEVplus.Space.getMolten(1000 * 114514),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1000 * 114514),
                MyMaterial.shirabon.getMolten(1000 * 114514),
                MaterialsUEVplus.Universium.getMolten(1000 * 114514),
                MaterialsUEVplus.Eternity.getMolten(1000 * 114514),
                MaterialsUEVplus.PrimordialMatter.getFluid(1000 * 114514)
            )
            .itemOutputs(GTCMItemList.ProofOfHeroes.get(1))
            .noFluidOutputs()
            .noOptimize()
            .specialValue(13500)
            .eut(RECIPE_MAX)
            .duration(20 * 1919810)
            .addTo(MT);


        // endregion
        loadMaxRecipe();
    }

    public void loadMaxRecipe() {
        ItemStack[] inStack = new ItemStack[]{
            ItemList.Circuit_Parts_ResistorXSMD.get(16),
            ItemList.Circuit_Parts_DiodeXSMD.get(16),
            ItemList.Circuit_Parts_TransistorXSMD.get(16),
            ItemList.Circuit_Parts_CapacitorXSMD.get(16),
            ItemList.Circuit_Parts_InductorXSMD.get(16)
        };
        ItemStack[] outStack = new ItemStack[]{
            GTCMItemList.HighDimensionalResistor.get(64),
            GTCMItemList.HighDimensionalDiode.get(64),
            GTCMItemList.HighDimensionalTransistor.get(64),
            GTCMItemList.HighDimensionalCapacitor.get(64),
            GTCMItemList.HighDimensionalInterface.get(64),
        };
        for (int i = 0; i < 5; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(12),
                    GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.TranscendentMetal, 4),
                    GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 2),
                    inStack[i],
                    GTCMItemList.HighDimensionalExtend.get(1)
                )
                .fluidInputs(
                    MaterialsUEVplus.Time.getMolten(144)
                )
                .itemOutputs(
                    outStack[i]
                )
                .noFluidOutputs()
                .eut(RECIPE_UEV)
                .duration(20)
                .addTo(MT);
        }


//我看你东西没加完，我找不到的东西暂时先不加，等你加完再说，先注释掉
        //以下这三个主机的配方，除了贴片以外的输入数量全部除以2就可以写作下一级的配方，例如：复制粘贴，然后把超级电脑移动到输出，输入的超级电脑改为天顶星处理器集群，同时输入数量和配方耗时除以二，以此类推
        //如果存在soc，则使用soc的配方在最基础那一级去掉除了纳米蜂群以外的所有输入，耗电提高四倍，时长提高四倍，这很符合GTNH的SOC法
        //天顶星 UMV主机
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(16),
                GTCMItemList.HighDimensionalResistor.get(16),
                GTCMItemList.HighDimensionalDiode.get(16),
                GTCMItemList.HighDimensionalTransistor.get(16),
                GTCMItemList.HighDimensionalCapacitor.get(16),
                GTCMItemList.HighDimensionalInterface.get(16),
                GTCMItemList.CosmicCircuitBoard.get(16),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(16),
                GTCMItemList.EventHorizonNanoSwarm.get(8),
                ItemList.Circuit_ExoticComputer.get(2)
            )
            .fluidInputs(
                MaterialsUEVplus.SpaceTime.getMolten(144 * 8 * 16),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 8 * 16),
                Materials.Neutronium.getMolten(144 * 8 * 16 * 144),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 16 * 144)
            )
            .itemOutputs(
                ItemList.Circuit_ExoticMainframe.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 1000)
            .addTo(MT);

        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(12),
                GTCMItemList.HighDimensionalResistor.get(12),
                GTCMItemList.HighDimensionalDiode.get(12),
                GTCMItemList.HighDimensionalTransistor.get(12),
                GTCMItemList.HighDimensionalCapacitor.get(12),
                GTCMItemList.HighDimensionalInterface.get(12),
                GTCMItemList.CosmicCircuitBoard.get(12),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(12),
                GTCMItemList.EventHorizonNanoSwarm.get(6),
                ItemList.Circuit_CosmicAssembly.get(2)
            )
            .fluidInputs(
                MaterialsUEVplus.SpaceTime.getMolten(144 * 8 * 12),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 8 * 12),
                Materials.Neutronium.getMolten(144 * 8 * 12 * 144),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 12 * 144)
            )
            .itemOutputs(
                ItemList.Circuit_CosmicComputer.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UXV)
            .duration(20 * 1000)
            .addTo(MT);

        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UEV.get(8),
                GTCMItemList.HighDimensionalResistor.get(8),
                GTCMItemList.HighDimensionalDiode.get(8),
                GTCMItemList.HighDimensionalTransistor.get(8),
                GTCMItemList.HighDimensionalCapacitor.get(8),
                GTCMItemList.HighDimensionalInterface.get(8),
                GTCMItemList.CosmicCircuitBoard.get(8),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(8),
                GTCMItemList.EventHorizonNanoSwarm.get(4),
                ItemList.Circuit_TranscendentProcessor.get(2)
            )
            .fluidInputs(
                MaterialsUEVplus.SpaceTime.getMolten(144 * 8 * 12),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 8 * 12),
                Materials.Neutronium.getMolten(144 * 8 * 12 * 144),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 12 * 144)
            )
            .itemOutputs(
                ItemList.Circuit_TranscendentAssembly.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UXV)
            .duration(20 * 1000)
            .addTo(MT);

        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(24),
                ItemList.Field_Generator_UMV.get(1),
                GTCMItemList.HighDimensionalResistor.get(2),
                GTCMItemList.HighDimensionalDiode.get(2),
                GTCMItemList.HighDimensionalTransistor.get(2),
                GTCMItemList.HighDimensionalCapacitor.get(2),
                GTCMItemList.HighDimensionalInterface.get(2),
                GTCMItemList.CosmicCircuitBoard.get(2),
                GTCMItemList.IntelligentImitationNeutronStarCore.get(2),
                GTCMItemList.RealSingularityNanoSwarm.get(4),
                GTCMItemList.CoreOfT800.get(2)
            )
            .fluidInputs(
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144),
                MaterialPool.entropyReductionProcess.getMolten(1440),
                MaterialPool.eventHorizonDiffusers.getMolten(1440),
                MaterialPool.realSingularity.getMolten(144)
            )
            .itemOutputs(
                ItemList.Circuit_TranscendentProcessor.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_UXV)
            .duration(20 * 1000)
            .addTo(MT);

        //寰宇 UXV主机
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Field_Generator_UIV.get(16),
                GTCMItemList.HighDimensionalResistor.get(32),
                GTCMItemList.HighDimensionalDiode.get(32),
                GTCMItemList.HighDimensionalTransistor.get(32),
                GTCMItemList.HighDimensionalCapacitor.get(32),
                GTCMItemList.HighDimensionalInterface.get(32),
                GTCMItemList.ExoticCircuitBoard.get(16),
                GTCMItemList.MicroDimensionOutput.get(16),
                GTCMItemList.EntropyReductionMaterialNanoswarm.get(16),
                ItemList.Circuit_CosmicComputer.get(2)
            )
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(1000 * 16),
                MaterialsUEVplus.Space.getMolten(1000 * 16)
            )
            .itemOutputs(
                ItemList.Circuit_CosmicMainframe.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_MAX)
            .duration(20 * 1000)
            .addTo(MT);

//        超时空 MAX主机
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
//        这是个例外：超时空的soc也不能省去超立方体
                ItemList.EnergisedTesseract.get(64),
//        所有主机的力场也不能省去，但是放在你那个空间站可以节省
                ItemList.Field_Generator_UMV.get(16),
                GTCMItemList.HighDimensionalResistor.get(64),
                GTCMItemList.HighDimensionalDiode.get(64),
                GTCMItemList.HighDimensionalTransistor.get(64),
                GTCMItemList.HighDimensionalCapacitor.get(64),
                GTCMItemList.HighDimensionalInterface.get(64),
                GTCMItemList.TranscendentCircuitBoard.get(16),
                GTCMItemList.NarrativeLayerOverwritingDevice.get(16),
                GTCMItemList.HyperspaceNarrativeLayerAdaptiveSpecialSRA.get(16),
                GTCMItemList.RealSingularityNanoSwarm.get(8),
                ItemList.Circuit_TranscendentComputer.get(2)
            )
//        此处流体输入应有蓝框岩浆和你加的三种材料的熔融态
            .fluidInputs(
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(1440),
                MaterialPool.entropyReductionProcess.getMolten(144000),
                MaterialPool.eventHorizonDiffusers.getMolten(144000),
                MaterialPool.realSingularity.getMolten(1440)
            )
            .itemOutputs(
                ItemList.Circuit_TranscendentMainframe.get(1)
            )
            .noFluidOutputs()
            .eut(RECIPE_MAX * 16)
            .duration(20 * 1500)
            .addTo(MT);

        //MAX电动马达
        //物品输入：
        //充能超立方体 4
        //长realSingularity杆 16
        //realSingularity环 8
        //realSingularity滚珠 32
        //细 熵还原物质导线 64*8
        //16*时空导线 64
        //16*无尽导线 64
        //宇宙素纳米蜂群 4
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

        //MAX电动泵
        //物品输入：
        //充能超立方体 4
        //大型realSingularity流体管道 16
        //realSingularity板 4
        //realSingularity螺丝 16
        //拉多X聚合物环 64*4
        //熵还原物质转子 4
        //事件视界转子 4
        //16*时空导线 64
        //16*无尽导线 64
        //宇宙素纳米蜂群 4
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

        //MAX电动泵
        //物品输入：
        //充能超立方体 4
        //大型realSingularity流体管道 16
        //realSingularity板 4
        //realSingularity螺丝 16
        //拉多X聚合物环 64*4
        //熵还原物质转子 4
        //事件视界转子 4
        //16*时空导线 64
        //16*无尽导线 64
        //宇宙素纳米蜂群 4
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

        //MAX机械臂
        //物品输入：
        //充能超立方体 4
        //MAX电动马达 2
        //MAX电动活塞 2
        //长realSingularity杆 16
        //realSingularity齿轮 4
        //熵还原物质齿轮 4
        //小realSingularity齿轮 16
        //小熵还原物质齿轮 16
        //熵还原物质转子 4
        //事件视界转子 4
        //MAX电路板 4
        //UXV电路板 8
        //UMV电路板 16
        //16*时空导线 64
        //16*无尽导线 64
        //宇宙素纳米蜂群 4
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

        //MAX传送带
        //物品输入：
        //充能超立方体 4
        //MAX电动马达 2
        //致密realSingularity板 4
        //realSingularity环 16
        //realSingularity滚珠 64
        //16*时空导线 64
        //16*无尽导线 64
        //致密凯芙拉板 64+32
        //致密拉多X聚合物板 64+32
        //宇宙素纳米蜂群 4
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

        //MAX电动活塞
        //物品输入：
        //充能超立方体 4
        //MAX电动马达 1
        //致密realSingularity板 8
        //realSingularity环 16
        //realSingularity滚珠 64
        //realSingularity杆 16
        //realSingularity齿轮 4
        //熵还原物质齿轮 4
        //小型realSingularity齿轮 8
        //16*时空导线 64
        //16*无尽导线 64
        //宇宙素纳米蜂群 4
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

        //MAX发射器
        //物品输入：
        //充能超立方体 16
        //MAX电动马达 1
        //长realSingularity杆 64
        //扭曲时空之星 64
        //realSingularity箔 64
        //熵还原物质箔 64
        //事件视界物质箔 64
        //蓝框岩浆箔 64
        //MAX电路板 16
        //16*时空导线 64
        //16*无尽导线 64
        //宇宙素纳米蜂群 16
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

        //MAX传感器
        //物品输入：
        //充能超立方体 16
        //MAX电动马达 1
        //致密realSingularity板 32
        //扭曲时空之星 64
        //realSingularity箔 64
        //熵还原物质箔 64
        //事件视界物质箔 64
        //蓝框岩浆箔 64
        //MAX电路板 16
        //16*时空导线 64
        //16*无尽导线 64
        //宇宙素纳米蜂群 16
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

        //MAX力场发生器
        //物品输入：
        //充能超立方体 64
        //MAX发射器 16
        //致密realSingularity板 64
        //扭曲时空之星 64（576mB时空+核能之星，流体固化，UIV20S）
        //细realSingularity导线 64*2
        //细熵还原物质导线 64*2
        //细事件视界物质导线 64*2
        //细蓝框岩浆导线 64*2
        //MAX电路板 64
        //16*时空导线 64
        //16*无尽导线 64
        //宇宙素纳米蜂群 32
        //流体输入：
        //576mB 熵还原物质
        //576mB 事件视界
        //576mB realSingularity
        //2304mB 蓝框岩浆

//所有的MAX部件配方加工时间均为UXV 50S
//放进空间站的配方，只需要除了流体输入和合成表中输入的MAX部件以外，其余物品材料数量均除以2


    }

//    HighDimensionalExtend,
//    HighDimensionalCircuitDoard,

    // pattern
    /*
    GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1)

            )
            .fluidInputs(

            )
            .itemOutputs(

            )
            .noFluidOutputs()
            .eut()
            .duration(20)
            .addTo(MT);
     */
}
// spotless:on
