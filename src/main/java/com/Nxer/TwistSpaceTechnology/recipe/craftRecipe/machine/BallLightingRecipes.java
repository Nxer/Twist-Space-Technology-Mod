package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AdvancedHighPowerCoilBlock;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AnnihilationConstrainer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Antimatter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BallLightning;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BallLightningUpgradeChip;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.HighPowerRadiationProofCasing;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.LaserSmartNode;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PerfectLapotronCrystal;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.NitronicSingularity;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MiracleTopRecipes;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static com.dreammaster.gthandler.CustomItemList.PicoWafer;
import static gregtech.api.enums.ItemList.ArcFurnaceUEV;
import static gregtech.api.enums.ItemList.PlasmaArcFurnaceUEV;
import static gregtech.api.enums.Materials.RadoxPolymer;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.core.item.chemistry.RocketFuels.Liquid_Hydrogen;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Casing_Industrial_Arc_Furnace;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.GTPP_Casing_UHV;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Arc_Furnace;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TransmissionComponent_UV;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import galaxyspace.core.register.GSItems;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.thing.CustomItemList;

public class BallLightingRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        if (Config.Enable_BallLightning) {
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, Industrial_Arc_Furnace.get(1))
                .metadata(RESEARCH_TIME, 8 * HOURS)
                .itemInputs(
                    HighPowerRadiationProofCasing.get(64),
                    Industrial_Arc_Furnace.get(64),
                    ArcFurnaceUEV.get(16),
                    PlasmaArcFurnaceUEV.get(16),

                    GTPP_Casing_UHV.get(64),
                    new ItemStack(ItemRegistry.bw_realglas, 48, 14),
                    ItemRefer.Field_Restriction_Coil_T2.get(32),
                    ItemList.Field_Generator_UEV.get(16),

                    ItemList.Robot_Arm_UEV.get(32),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 16),

                    HighEnergyFlowCircuit.get(64),
                    HighEnergyFlowCircuit.get(64),
                    HighEnergyFlowCircuit.get(64),
                    HighEnergyFlowCircuit.get(64))
                .fluidInputs(
                    WerkstoffLoader.Oganesson.getFluidOrGas(2000 * 1000),
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 64 * 64),
                    RadoxPolymer.getMolten(144 * 64 * 16),
                    MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 16))
                .itemOutputs(BallLightning.get(1))
                .eut(RECIPE_UEV)
                .duration(20 * 1800)
                .addTo(AssemblyLine);
        }
        // Casing
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Casing_Industrial_Arc_Furnace.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                Casing_Industrial_Arc_Furnace.get(8),
                CustomItemList.eM_Power.get(8),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                new ItemStack(GSItems.DysonSwarmItems, 64, 3),

                TransmissionComponent_UV.get(16),
                ItemList.Electric_Piston_UHV.get(8),
                HighEnergyFlowCircuit.get(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 4),

                PicoWafer.get(8),
                GGMaterial.incoloy903.get(OrePrefixes.pipeHuge, 64),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFineWire(64))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 40),
                Materials.UUMatter.getFluid(1000 * 8),
                MaterialsAlloy.ABYSSAL.getFluidStack(144 * 28))
            .itemOutputs(HighPowerRadiationProofCasing.get(4))
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(AssemblyLine);

        // Chip
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64),
                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),
                CriticalPhoton.get(64),
                Laser_Lens_Special.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),
                Antimatter.get(64),
                SpaceWarper.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),

                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                ItemList.ZPM.get(1),
                ItemList.ZPM5.get(1),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64))
            .fluidInputs(
                new FluidStack(Liquid_Hydrogen, 1_800_000),
                new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 1_800_000),
                Materials.Nitrogen.getPlasma(1_800_000),
                new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 1_800_000),
                new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1_800_000),
                Materials.Bismuth.getPlasma(1_800_000),
                Materials.Boron.getPlasma(1_800_000),
                FluidRegistry.getFluidStack("cryotheum", 1_800_000))
            .itemOutputs(BallLightningUpgradeChip.get(1))
            .eut(RECIPE_UIV)
            .duration(630_720_000)
            .addTo(MiracleTopRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64),
                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),
                CriticalPhoton.get(64),
                Laser_Lens_Special.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),
                Antimatter.get(64),
                SpaceWarper.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),

                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                ItemUtils.getSimpleStack(ModItems.itemChargePack_High_4, 1),
                ItemList.ZPM5.get(1),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64))
            .fluidInputs(
                new FluidStack(Liquid_Hydrogen, 1_800_000),
                new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 1_800_000),
                Materials.Nitrogen.getPlasma(1_800_000),
                new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 1_800_000),
                new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1_800_000),
                Materials.Bismuth.getPlasma(1_800_000),
                Materials.Boron.getPlasma(1_800_000),
                FluidRegistry.getFluidStack("cryotheum", 1_800_000))
            .itemOutputs(BallLightningUpgradeChip.get(1))
            .eut(RECIPE_UIV)
            .duration(630_720_000)
            .addTo(MiracleTopRecipes);

        // Coil
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Casing_Coil_QuantumForceTransformer.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Longasssuperconductornameforuhvwire, 8),
                GregtechItemList.Casing_Coil_QuantumForceTransformer.get(4),
                ItemRefer.Compact_Fusion_Coil_T4.get(16),
                LaserSmartNode.get(16),

                ItemList.Emitter_UIV.get(64),
                ItemList.Sensor_UIV.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),

                ItemRefer.HiC_T5.get(64),
                GravitationalLens.get(64),
                PerfectLapotronCrystal.get(64),
                NitronicSingularity.getLeft(),

                AnnihilationConstrainer.get(1),
                ItemList.ZPM2.get(1),
                GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore10", 1),
                Laser_Lens_Special.get(4))
            .fluidInputs(
                Materials.Hydrogen.getPlasma(1000 * 4096),
                MaterialsElements.getInstance().CALIFORNIUM.getFluidStack(144 * 256),
                MaterialsAlloy.QUANTUM.getFluidStack(144 * 256),
                MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(144 * 256))
            .itemOutputs(AdvancedHighPowerCoilBlock.get(1))
            .eut(RECIPE_UIV)
            .duration(20 * 240)
            .addTo(AssemblyLine);
    }
}
