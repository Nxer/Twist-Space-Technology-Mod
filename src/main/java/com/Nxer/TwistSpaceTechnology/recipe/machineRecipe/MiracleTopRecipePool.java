package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.glodblock.github.loader.ItemAndBlockHolder.SINGULARITY_CELL;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;
import static gregtech.api.util.GTModHandler.getModItem;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.InfinityInfusedShieldingCore;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.SpaceTimeBendingCore;
import static tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static tectech.thing.CustomItemList.DATApipe;
import static thaumcraft.common.config.ConfigBlocks.blockEssentiaReservoir;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;
import com.glodblock.github.common.storage.CellType;

import appeng.core.Api;
import appeng.items.materials.MaterialType;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import thaumicenergistics.implementaion.ThEAPIImplementation;

// spotless:off
public class MiracleTopRecipePool implements IRecipePool {
    final RecipeMap<?> MT = GTCMRecipe.MiracleTopRecipes;

    @Override
    public void loadRecipes() {

        TwistSpaceTechnology.LOG.info("MiracleTopRecipePool loading recipes.");

        Fluid solderIndAlloy = FluidRegistry.getFluid("molten.indalloy140");
        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");


        final ItemStack Wrapped_Circuit_Chip_Ram = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32735);
        final ItemStack Wrapped_Circuit_Chip_NanoCPU = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32720);

        final ItemStack Wrapped_Circuit_Parts_CapacitorASMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32737);
        final ItemStack Wrapped_Circuit_Parts_TransistorASMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32738);
        final ItemStack Wrapped_Circuit_Parts_DiodeASMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32739);
        final ItemStack Wrapped_Circuit_Parts_ResistorASMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32740);
        final ItemStack Wrapped_Circuit_Parts_InductorASMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32707);

        final ItemStack Wrapped_Circuit_Parts_CapacitorXSMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32708);
        final ItemStack Wrapped_Circuit_Parts_TransistorXSMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32709);
        final ItemStack Wrapped_Circuit_Parts_DiodeXSMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32710);
        final ItemStack Wrapped_Circuit_Parts_ResistorXSMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32711);

        final ItemStack Wrapped_Circuit_Board_Optical = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32704);
        final ItemStack Wrapped_Optically_Perfected_CPU = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32703);
        final ItemStack Wrapped_Optically_Compatible_Memory = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32701);
        final ItemStack Wrapped_Circuit_Parts_InductorXSMD = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32706);

        final ItemStack Wrapped_Circuit_Board_Bio_Ultra = GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32746);

        final ItemStack ringBlock = GTModHandler.getModItem("SGCraft", "stargateRing" , 1, 0);
        final ItemStack chevronBlock = GTModHandler.getModItem("SGCraft", "stargateRing", 1, 1);
        final ItemStack irisUpgrade = GTModHandler.getModItem("SGCraft", "sgIrisUpgrade" , 1, 0);





        // region ME Storage Component
        {
            final ItemStack integratedCircuit19 = GTUtility.getIntegratedCircuit(19);

            // Item
            TST_RecipeBuilder
                .builder()
                .itemInputs(
                    integratedCircuit19,
                    CustomItemList.EngineeringProcessorItemAdvEmeraldCore.get(48),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64)
                )
                .fluidInputs(
                    new FluidStack(solderPlasma, 72)
                )
                .itemOutputs(MaterialType.Cell16384kPart.stack(64))
                .eut(RECIPE_UV)
                .duration(200)
                .addTo(MT);

            // Fluid
            TST_RecipeBuilder
                .builder()
                .itemInputs(
                    integratedCircuit19,
                    ItemList.Electric_Pump_EV.get(48),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64)
                )
                .fluidInputs(
                    new FluidStack(solderPlasma, 72)
                )
                .itemOutputs(CellType.Cell16384kPart.stack(64))
                .eut(RECIPE_UV)
                .duration(200)
                .addTo(MT);

            // Essentia
            TST_RecipeBuilder
                .builder()
                .itemInputs(
                    integratedCircuit19,
                    CustomItemList.EngineeringProcessorEssentiaPulsatingCore.get(48),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 64)
                )
                .fluidInputs(
                    new FluidStack(solderPlasma, 72)
                )
                .itemOutputs(ThEAPIImplementation.instance().items().EssentiaStorageComponent_16384k.getStacks(64))
                .eut(RECIPE_UV)
                .duration(200)
                .addTo(MT);

            final ItemStack eternalSingularity = getModItem(EternalSingularity.ID, "eternal_singularity", 1);

            // Item Singularity
            TST_RecipeBuilder
                .builder()
                .itemInputs(
                    integratedCircuit19,
                    eternalSingularity,
                    MaterialType.Cell16384kPart.stack(9),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 32),

                    ItemList.Field_Generator_UV.get(6),
                    ItemList.Conveyor_Module_UEV.get(6),
                    setStackSize(Materials.InfinityCatalyst.getDust(1), 192)
                )
                .fluidInputs(
                    Materials.Infinity.getMolten(144*24),
                    Materials.CosmicNeutronium.getMolten(144*81),
                    Materials.Americium.getMolten(144*18)
                )
                .itemOutputs(Api.INSTANCE.definitions().items().cellSingularity().maybeStack(1).get())
                .eut(RECIPE_UXV)
                .duration(200)
                .addTo(MT);

            // Fluid Singularity
            TST_RecipeBuilder
                .builder()
                .itemInputs(
                    integratedCircuit19,
                    eternalSingularity,
                    CellType.Cell16384kPart.stack(6),
                    ItemList.Electric_Pump_UHV.get(24),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 32),
                    ItemList.Field_Generator_UV.get(6),
                    ItemList.Electric_Pump_UV.get(6),

                    new ItemStack(Items.nether_star, 144),
                    setStackSize(Materials.InfinityCatalyst.getDust(1), 192)
                )
                .fluidInputs(
                    Materials.DraconiumAwakened.getMolten(144*150),
                    Materials.Americium.getMolten(144*12),
                    Materials.Infinity.getMolten(144*123),
                    Materials.CosmicNeutronium.getMolten(144*177)
                )
                .itemOutputs(SINGULARITY_CELL.stack())
                .eut(RECIPE_UXV)
                .duration(200)
                .addTo(MT);

            // Essentia Singularity
            TST_RecipeBuilder
                .builder()
                .itemInputs(
                    integratedCircuit19,
                    eternalSingularity,
                    ThEAPIImplementation.instance().items().EssentiaStorageComponent_16384k.getStacks(9),
                    new ItemStack(blockEssentiaReservoir, 6),
                    setStackSize(Materials.InfinityCatalyst.getDust(1), 192)
                )
                .fluidInputs(
                    Materials.Infinity.getMolten(144*24),
                    Materials.CosmicNeutronium.getMolten(144*81)
                )
                .itemOutputs(ThEAPIImplementation.instance().items().EssentiaCell_Singularity.getStack())
                .eut(RECIPE_UXV)
                .duration(200)
                .addTo(MT);

        }
        // endregion


        // region Quantum Circuit and Piko Circuit
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("dreamcraft", "item.PikoCircuit", 2),
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
                GGMaterial.shirabon.getMolten(144 * 8),
                Materials.Indium.getMolten(144 * 8),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 4),
                Materials.Lanthanum.getMolten(144 * 2)
            )
            .itemOutputs(GTModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 1))
            .eut(RECIPE_UMV)
            .duration(20 * 1000)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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
                Materials.RadoxPolymer.getMolten(144 * 4),
                MaterialsUEVplus.TranscendentMetal.getMolten(144 * 4),
                Materials.Neutronium.getMolten(144 * 2),
                Materials.Lanthanum.getMolten(144 * 8)
            )
            .itemOutputs(GTModHandler.getModItem("dreamcraft", "item.PikoCircuit", 1))

            .eut(RECIPE_UMV)
            .duration(20 * 500)
            .addTo(MT);
        // endregion

        // region Optical Component

        // optical cpu
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                setStackSize(ItemList.Circuit_Chip_Optical.get(1), 192),
                setStackSize(ItemList.Optical_Cpu_Containment_Housing.get(1), 192),
                setStackSize(DATApipe.get(1), 192)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1152 * 12),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 24),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 24),
                Materials.Tritanium.getMolten(144 * 24),
                GGMaterial.enrichedNaquadahAlloy.getMolten(144 * 24),
                GGMaterial.shirabon.getMolten(144 * 24)
            )
            .itemOutputs(setStackSize(ItemList.Optically_Perfected_CPU.get(1), 256))
            .eut(RECIPE_UIV)
            .duration(20 * 20 * 12)
            .addTo(MT);

        // optical memory
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                setStackSize(ItemList.Circuit_Chip_Optical.get(1), 192),
                getModItem(SuperSolarPanels.ID, "solarsplitter", 192),
                setStackSize(DATApipe.get(1), 768),
                setStackSize(ItemList.Circuit_Chip_Ram.get(1), 192),
                setStackSize(ItemList.Circuit_Chip_SoC.get(1), 192),
                setStackSize(ItemList.Circuit_Chip_NAND.get(1), 192)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1152 * 12),
                Materials.VanadiumGallium.getMolten(144 * 48),
                Materials.Infinity.getMolten(144 * 48),
                Materials.SuperconductorUMVBase.getMolten(144 * 24),
                MaterialsUEVplus.SpaceTime.getMolten(144 * 7)
            )
            .itemOutputs(setStackSize(ItemList.Optically_Compatible_Memory.get(1), 512))
            .eut(RECIPE_UIV)
            .duration(20 * 20 * 12)
            .addTo(MT);

        // endregion

        // region Optical Circuit

        // Optical SoC frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                InfinityInfusedShieldingCore.get(0),
                ItemList.Optical_Cpu_Containment_Housing.get(1),
                Materials.Glowstone.getNanite(4)
            )
            .fluidInputs(
                MaterialsUEVplus.Space.getMolten(36),
                MaterialsUEVplus.Time.getMolten(36)
            )
            .itemOutputs(
                GTCMItemList.ParticleTrapTimeSpaceShield.get(1)
            )
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
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
                GTCMItemList.ParticleTrapTimeSpaceShield.get(16)
            )
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(2500))
            .eut(RECIPE_UMV)
            .duration(20 * 64)
            .addTo(MT);


        // Optical Frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64L)
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

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64L)
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

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 32L)
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

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 32L)
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

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 16L)
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

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_OpticalComputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 16L)
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

            .eut(RECIPE_UEV)
            .duration(20 * 500)
            .addTo(MT);

        // Optical Computer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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
                GGMaterial.lumiium.getMolten(144 * 4),
                Materials.Silicone.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalComputer.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 200)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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
                GGMaterial.lumiium.getMolten(144 * 4),
                Materials.StyreneButadieneRubber.getMolten(144 * 16),
                Materials.Polybenzimidazole.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalComputer.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 200)
            .addTo(MT);

        // Optical Assembly
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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
                GGMaterial.lumiium.getMolten(144 * 3),
                Materials.Silicone.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalAssembly.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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
                GGMaterial.lumiium.getMolten(144 * 3),
                Materials.StyreneButadieneRubber.getMolten(144 * 16)
            )
            .itemOutputs(ItemList.Circuit_OpticalAssembly.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(MT);

        // Optical Processor
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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

            .eut(RECIPE_UHV)
            .duration(20 * 240)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Optical),
                GTCMItemList.OpticalSOC.get(1),
                DATApipe.get(16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 2),
                Materials.EnrichedHolmium.getMolten(144 * 2)
            )
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))

            .eut(9830400)
            .duration(20 * 10)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                Utils.copyAmount(16, Wrapped_Circuit_Board_Optical),
                GTCMItemList.OpticalSOC.get(16),
                DATApipe.get(64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 32),
                Materials.EnrichedHolmium.getMolten(144 * 16)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_OpticalProcessor.get(64), 64*4))
            .eut(9830400 * 4)
            .duration(20 * 10 * 4)
            .addTo(MT);

        // endregion

        // region Bio Circuit

        // Bio Mainframe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorASMD.get(24L),
                ItemList.Circuit_Parts_TransistorASMD.get(24L),
                ItemList.Circuit_Parts_ResistorASMD.get(24L),
                ItemList.Circuit_Parts_CapacitorASMD.get(24L),
                ItemList.Circuit_Parts_DiodeASMD.get(24L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
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

            .eut(RECIPE_UHV)
            .duration(20 * 300)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(6L),
                ItemList.Circuit_Parts_TransistorXSMD.get(6L),
                ItemList.Circuit_Parts_ResistorXSMD.get(6L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(6L),
                ItemList.Circuit_Parts_DiodeXSMD.get(6L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
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

            .eut(RECIPE_UHV)
            .duration(20 * 150)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorASMD.get(24L),
                ItemList.Circuit_Parts_TransistorASMD.get(24L),
                ItemList.Circuit_Parts_ResistorASMD.get(24L),
                ItemList.Circuit_Parts_CapacitorASMD.get(24L),
                ItemList.Circuit_Parts_DiodeASMD.get(24L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
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

            .eut(RECIPE_UHV)
            .duration(20 * 300)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(6L),
                ItemList.Circuit_Parts_TransistorXSMD.get(6L),
                ItemList.Circuit_Parts_ResistorXSMD.get(6L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(6L),
                ItemList.Circuit_Parts_DiodeXSMD.get(6L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[]{
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L)
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

            .eut(RECIPE_UHV)
            .duration(20 * 150)
            .addTo(MT);

        // Bio SuperComputer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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

            .eut(RECIPE_UV)
            .duration(20 * 200)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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

            .eut(RECIPE_UV)
            .duration(20 * 100)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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

            .eut(RECIPE_UV)
            .duration(20 * 200)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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

            .eut(RECIPE_UV)
            .duration(20 * 100)
            .addTo(MT);

        // Bio Assembly
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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

            .eut(RECIPE_UV)
            .duration(20 * 240)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
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

            .eut(RECIPE_UHV)
            .duration(20 * 30)
            .addTo(MT);

        // Bio Processor
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32714),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32716),
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

            .eut(RECIPE_UV)
            .duration(20 * 180)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32714),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32716),
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

            .eut(RECIPE_UHV)
            .duration(444)
            .addTo(MT);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Utils.copyAmount(1, Wrapped_Circuit_Board_Bio_Ultra),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32699)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                new FluidStack(FluidRegistry.getFluid("molten.chromaticglass"), 144 * 8),
                Materials.NiobiumTitanium.getMolten(144 * 32)
            )
            .itemOutputs(
                ItemList.Circuit_Bioprocessor.get(16)
            )

            .eut(RECIPE_UEV)
            .duration(450)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                Utils.copyAmount(12, Wrapped_Circuit_Board_Bio_Ultra),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32699)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144 * 12),
                new FluidStack(FluidRegistry.getFluid("molten.chromaticglass"), 144 * 8 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 32 * 12)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Bioprocessor.get(64), 64*4))
            .eut(RECIPE_UIV)
            .duration(450 * 3)
            .addTo(MT);

        // endregion

        // region Wetware Processor SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32750),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32700)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144),
                Materials.YttriumBariumCuprate.getMolten(144 * 16),
                Materials.CosmicNeutronium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Neuroprocessor.get(16)
            )
            .eut(614400)
            .duration(20 * 15)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32750),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32700)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144 * 12),
                Materials.YttriumBariumCuprate.getMolten(144 * 16 * 12),
                Materials.CosmicNeutronium.getMolten(144 * 8 * 12)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Neuroprocessor.get(64), 64*4))
            .eut(614400 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Crystal Processor SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32753),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32717)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.NiobiumTitanium.getMolten(144 * 16),
                Materials.YttriumBariumCuprate.getMolten(144 * 8))
            .itemOutputs(
                ItemList.Circuit_Crystalprocessor.get(16)
            )

            .eut(153600)
            .duration(20 * 15)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32753),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32717)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 16 * 12),
                Materials.YttriumBariumCuprate.getMolten(144 * 8 * 12))
            .itemOutputs(setStackSize(ItemList.Circuit_Crystalprocessor.get(64), 64*4))
            .eut(153600 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Quantum Processor SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32754),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.Platinum.getMolten(144 * 32),
                Materials.NiobiumTitanium.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Quantumprocessor.get(16)
            )

            .eut(38400)
            .duration(20 * 15)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32754),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.Platinum.getMolten(144 * 32 * 12),
                Materials.NiobiumTitanium.getMolten(144 * 8 * 12)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Quantumprocessor.get(64), 64*4))
            .eut(38400 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region Nano Processor SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32756),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72),
                Materials.Electrum.getMolten(144 * 16),
                Materials.Platinum.getMolten(144 * 8)
            )
            .itemOutputs(
                ItemList.Circuit_Nanoprocessor.get(16)
            )
            .eut(9600)
            .duration(20 * 15)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32756),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32730)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 72 * 12),
                Materials.Electrum.getMolten(144 * 16 * 12),
                Materials.Platinum.getMolten(144 * 8 * 12)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Nanoprocessor.get(64), 64*4))
            .eut(9600 * 4)
            .duration(20 * 15 * 3)
            .addTo(MT);

        // endregion

        // region High Energy Flow Circuit
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32753),
                GTModHandler.getModItem("GoodGenerator", "circuitWrap", 2, 7),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 8, 32721)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 288),
                Materials.Infinity.getMolten(144)
            )
            .itemOutputs(
                GTModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 16)
            )

            .eut(RECIPE_IV)
            .duration(20 * 720)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32753),
                GTModHandler.getModItem("GoodGenerator", "circuitWrap", 24, 7),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 48, 32721)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 288 * 12),
                Materials.Infinity.getMolten(144 * 12)
            )
            .itemOutputs(setStackSize(GTModHandler.getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 64),64*4))
            .eut(RECIPE_LuV)
            .duration(20 * 720 * 3)
            .addTo(MT);

        //ULV LV and MV circuit
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32756),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 48, 32728)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(18 * 4),
                Materials.AnnealedCopper.getMolten(144 * 2 * 4),
                Materials.RedAlloy.getMolten(144 * 8)
            )
            .itemOutputs(setStackSize(CustomItemList.NandChipBoard.get(64), 64*16))
            .eut(RECIPE_EV)
            .duration(20 * 90 * 4)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32748),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32731)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(72 * 12),
                Materials.Copper.getMolten(144 * 96)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Microprocessor.get(64),64*8))
            .eut(600)
            .duration(20 * 30 * 4)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32748),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 12, 32731)
            )
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(72 * 12),
                Materials.AnnealedCopper.getMolten(144 * 8 * 12),
                Materials.RedAlloy.getMolten(144 * 24)
            )
            .itemOutputs(setStackSize(ItemList.Circuit_Processor.get(1), 64*4))
            .eut(RECIPE_EV)
            .duration(20 * 90 * 4)
            .addTo(MT);

        // endregion

        // region Neuro Processing Unit and Bio Processing Unit
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32750),
                setStackSize(ItemList.Circuit_Chip_Stemcell.get(64), 64*4)
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
            .itemOutputs(ItemList.Circuit_Chip_NeuroCPU.get(16))
            .eut(RECIPE_ZPM)
            .duration(20 * 30 * 12)
            .addTo(MT);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTModHandler.getModItem("bartworks", "gt.bwMetaGeneratedItem0", 1, 32746),
                setStackSize(ItemList.Circuit_Chip_Biocell.get(64), 64*4)
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
            .itemOutputs(ItemList.Circuit_Chip_BioCPU.get(16))
            .eut(RECIPE_UHV)
            .duration(20 * 30 * 12)
            .addTo(MT);
        // endregion

        // region Proof Of Heroes
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.SpaceWarper.get(64),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 64),
                GTModHandler.getModItem("eternalsingularity", "combined_singularity", 64, 15),
                ItemList.Timepiece.get(64),
                ItemList.GigaChad.get(64),
                tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                GTModHandler.getModItem("dreamcraft", "item.QuantumCircuit", 64),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 15),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 16),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 20),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 21),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 17),
                ItemList.ZPM6.get(64),
                GTCMItemList.IndistinctTentacle.get(64)
            )
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(1000 * 114514),
                MaterialsUEVplus.Space.getMolten(1000 * 114514),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1000 * 114514),
                GGMaterial.shirabon.getMolten(1000 * 114514),
                MaterialsUEVplus.Universium.getMolten(1000 * 114514),
                MaterialsUEVplus.Eternity.getMolten(1000 * 114514),
                MaterialsUEVplus.PrimordialMatter.getFluid(1000 * 114514)
            )
            .itemOutputs(GTCMItemList.ProofOfHeroes.get(1))
            .noOptimize()
            .specialValue(13500)
            .eut(RECIPE_MAX)
            .duration(20 * 1919810)
            .addTo(MT);


        // endregion

        // region Endgame Challenge content

        // Liquid Stargate
        GTValues.RA.stdBuilder()
            .itemInputs(
                Utils.copyAmount(1, ringBlock),
                Utils.copyAmount(1, chevronBlock),
                Utils.copyAmount(1, chevronBlock),
                Utils.copyAmount(1, ringBlock),

                Utils.copyAmount(1, chevronBlock),
                Utils.copyAmount(1, irisUpgrade),
                Utils.copyAmount(1, irisUpgrade),
                Utils.copyAmount(1, chevronBlock),

                Utils.copyAmount(1, ringBlock),
                Utils.copyAmount(1, irisUpgrade),
                Utils.copyAmount(1, irisUpgrade),
                Utils.copyAmount(1, ringBlock),

                Utils.copyAmount(1, chevronBlock),
                Utils.copyAmount(1, ringBlock),
                Utils.copyAmount(1, ringBlock),
                Utils.copyAmount(1, chevronBlock)
            )
            .fluidInputs(
                MaterialPool.StabiliseVoidMatter.getFluidOrGas(1_000)
            )
            .fluidOutputs(MaterialPool.LiquidStargate.getFluidOrGas(1000))
            .noOptimize()
            .specialValue(13500)
            .eut(RECIPE_MAX)
            .duration(20 *99_999_999)
            .addTo(MT);

        // StabiliseVoidMatter
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                setStackSize(Materials.CosmicNeutronium.getDust(1), 10_000_000),
                setStackSize(Materials.Bedrockium.getDust(1),10_000_000),
                setStackSize(Materials.Carbon.getDust(1),10_000_000),
                setStackSize(Materials.Oilsands.getDust(1),10_000_000),
                setStackSize(Materials.NiobiumTitanium.getDust(1),10_000_000),
                setStackSize(MaterialsElements.STANDALONE.BLACK_METAL.getDust(1),10_000_000),
                setStackSize(Materials.Naquadria.getDust(1),10_000_000),
                setStackSize(Materials.Obsidian.getDust(1),10_000_000),
                setStackSize(Materials.Coal.getDust(1),10_000_000),
                setStackSize(Materials.NaquadahAlloy.getDust(1),10_000_000),
                setStackSize(Materials.Tungsten.getDust(1),10_000_000),
                setStackSize(MaterialsUEVplus.TranscendentMetal.getDust(1),10_000_000),
                setStackSize(Materials.Perlite.getDust(1),10_000_000),
                setStackSize(Materials.DarkAsh.getDust(1),10_000_000),
                setStackSize(Materials.GraniticMineralSand.getDust(1),10_000_000),
                setStackSize(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1),10_000_000)
            )
            .fluidInputs(
                Materials.Polycaprolactam.getMolten(10_000_000),
                Materials.NickelZincFerrite.getMolten(10_000_000),
                Materials.DarkSteel.getMolten(10_000_000),
                Materials.Polybenzimidazole.getMolten(10_000_000),
                GGMaterial.tairitsu.getMolten(10_000_000),
                Materials.Tungsten.getMolten(10_000_000),
                GGMaterial.marM200.getMolten(10_000_000),
                Materials.Vanadium.getMolten(10_000_000),
                MaterialsElements.STANDALONE.BLACK_METAL.getFluidStack(10_000_000),
                Materials.ShadowIron.getMolten(10_000_000),
                Materials.NaquadahAlloy.getMolten(10_000_000),
                Materials.ShadowSteel.getMolten(10_000_000),
                Materials.Cadmium.getMolten(10_000_000),
                Materials.Desh.getMolten(10_000_000),
                Materials.BlackPlutonium.getMolten(10_000_000),
                Materials.BlackSteel.getMolten(10_000_000),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(10_000_000)
            )
            .fluidOutputs(MaterialPool.StabiliseVoidMatter.getFluidOrGas(1))
            .eut(RECIPE_MAX)
            .duration(20 * 99_999_999)
            .addTo(MT);

        // ProofOfGods
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTCMItemList.UxvFlask.get(1),
                GTCMItemList.ProofOfHeroes.get(64),
                setStackSize(Materials.Silver.getNanite(1), 1_000),
                setStackSize(Materials.Gold.getNanite(1), 1_000),
                setStackSize(Materials.Neutronium.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.Universium.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.Eternity.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.TranscendentMetal.getNanite(1), 1_000),
                setStackSize(Materials.Glowstone.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.WhiteDwarfMatter.getNanite(1), 1_000),
                setStackSize(MaterialsUEVplus.BlackDwarfMatter.getNanite(1), 1_000)
            )
            .fluidInputs(
                MaterialPool.LiquidStargate.getFluidOrGas(50_000),
                MaterialPool.StabiliseVoidMatter.getFluidOrGas(1_000)
            )
            .itemOutputs(
                GTCMItemList.ProofOfGods.get(1)
            )
            .eut(RECIPE_MAX)
            .duration(20 * 99_999_999)
            .addTo(MT);

        // FLASK
        loadFlaskRecipe();

        // endregion
        if (Config.activateMegaSpaceStation) {
            loadMaxRecipe();
        }
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
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(12),
                    GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.TranscendentMetal, 4),
                    GTOreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 2),
                    inStack[i],
                    GTCMItemList.HighDimensionalExtend.get(1)
                )
                .fluidInputs(
                    MaterialsUEVplus.Time.getMolten(144)
                )
                .itemOutputs(
                    outStack[i]
                )
                .eut(RECIPE_UEV)
                .duration(20)
                .addTo(MT);
        }


    }

    public void loadFlaskRecipe() {
        final int ITEMS_FLASK_COUNT = 100_000;

        // LV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                setStackSize(ItemList.Electric_Motor_LV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_LV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_LV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_LV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_LV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.LV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedstoneAlloy, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Iron.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.LvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(32 * 20)
            .addTo(MT);

        // MV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.LvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_MV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_MV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_MV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_MV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_MV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.MV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Copper.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.MvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(128 * 20)
            .addTo(MT);

        // HV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.MvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_HV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_HV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_HV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_HV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_HV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.HV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Nickel.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.HvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(512 * 20)
            .addTo(MT);

        // EV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.HvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_EV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_EV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_EV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_EV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_EV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.EV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Titanium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.EvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(2_048 * 20)
            .addTo(MT);

        // IV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.EvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_IV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_IV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_IV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_IV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_IV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.IV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Tungsten.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.IvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(8_192 * 20)
            .addTo(MT);

        // LUV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.IvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_LuV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_LuV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_LuV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_LuV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_LuV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.LuV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Osmium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.LuvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(32_768 * 20)
            .addTo(MT);

        // ZPM FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.LuvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_ZPM.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_ZPM.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_ZPM.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_ZPM.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_ZPM.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.ZPM,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Naquadah.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.ZpmFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(131_072 * 20)
            .addTo(MT);

        // UV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.ZpmFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Neutronium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(524_288 * 20)
            .addTo(MT);

        // UHV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UHV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UHV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UHV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UHV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UHV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UHV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Samarium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UhvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(2_097_152 * 20)
            .addTo(MT);

        // UEV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UhvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UEV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UEV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UEV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UEV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UEV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UEV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Americium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UevFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(8_388_608 * 20)
            .addTo(MT);

        // UIV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UevFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UIV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UIV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UIV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UIV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UIV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UIV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Thorium.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UivFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(33_554_432 * 20)
            .addTo(MT);

        // UMV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UivFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UMV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UMV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UMV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UMV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UMV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UMV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Plutonium241.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UmvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(100_000_000 * 20)
            .addTo(MT);

        // UXV FLASK
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.UmvFlask.get(1),
                setStackSize(ItemList.Electric_Motor_UXV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Piston_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Electric_Pump_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Field_Generator_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Conveyor_Module_UXV.get(1), ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Robot_Arm_UXV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Emitter_UXV.get(1),ITEMS_FLASK_COUNT),
                setStackSize(ItemList.Sensor_UXV.get(1),ITEMS_FLASK_COUNT),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UXV,ITEMS_FLASK_COUNT),
                setStackSize(GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Infinity, 1),ITEMS_FLASK_COUNT)
            )
            .fluidInputs(
                Materials.Radon.getPlasma(1_000_000_000)
            )
            .itemOutputs(GTCMItemList.UxvFlask.get(1))
            .noOptimize()
            .eut(RECIPE_MAX)
            .duration(100_000_000 * 20)
            .addTo(MT);
    }

//    HighDimensionalExtend,
//    HighDimensionalCircuitDoard,

    // pattern
    /*
    GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1)

            )
            .fluidInputs(

            )
            .itemOutputs(

            )

            .eut()
            .duration(20)
            .addTo(MT);
     */
}
// spotless:on
