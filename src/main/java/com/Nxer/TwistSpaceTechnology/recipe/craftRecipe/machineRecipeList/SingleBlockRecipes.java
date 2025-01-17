package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ParticleTrapTimeSpaceShield;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
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
import gtPlusPlus.core.material.MaterialsElements;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class SingleBlockRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        BlockFromTecTech();
        BlockFormGregTech();
    }

    void BlockFromTecTech() {
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_Coil.get(1),
            1_024_000,
            512,
            (int) RECIPE_UEV,
            16,
            new Object[] { CustomItemList.eM_Hollow.get(4), CustomItemList.eM_Coil.get(8),
                ItemList.Casing_Assembler.get(24), ItemList.Casing_Gearbox_TungstenSteel.get(24),

                SpaceWarper.get(6), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 24),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 36), HighEnergyFlowCircuit.get(48),

                new Object[] { OrePrefixes.gearGt.get(MaterialsUEVplus.ProtoHalkonite), 6 },
                new Object[] { OrePrefixes.gearGtSmall.get(MaterialsUEVplus.ProtoHalkonite), 12 },
                new Object[] { OrePrefixes.screw.get(MaterialsUEVplus.ProtoHalkonite), 24 },
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 3, 14),

                ItemList.Field_Generator_UEV.get(12), ItemList.Robot_Arm_UEV.get(24),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 36),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 36) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1024 * 144),
                Materials.UUMatter.getFluid(1000 * 256), Materials.SuperconductorUEVBase.getMolten(32 * 144),
                GGMaterial.metastableOganesson.getMolten(16 * 144) },
            CustomItemList.eM_Spacetime.get(1),
            20 * 512,
            (int) RECIPE_UHV);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Hollow.get(4),
                ItemList.Field_Generator_UHV.get(8),
                ItemList.Field_Generator_UV.get(16),

                ItemList.Field_Generator_ZPM.get(64),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(16),
                CustomItemList.eM_Power.get(4),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 6),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 2),
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 32))
            .itemOutputs(CustomItemList.eM_Containment_Field.get(4))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 60)
            .addTo(assemblerRecipes);
    }

    void BlockFormGregTech() {
        // Containment Field casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                ItemList.Field_Generator_LuV.get(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 8))
            .fluidInputs(Materials.NaquadahAlloy.getMolten(144 * 4))
            .itemOutputs(GTModHandler.getModItem("gregtech", "gt.blockcasings2", 1, 8))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 30)
            .addTo(assemblerRecipes);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_Containment_Field.get(1),
            2_048_000,
            1024,
            (int) RECIPE_UIV,
            4,
            new Object[] { CustomItemList.eM_Containment_Field.get(4), ItemList.Field_Generator_UIV.get(16),
                ItemList.Field_Generator_UEV.get(64), SpaceWarper.get(4),

                ItemList.Tesseract.get(32), ItemList.EnergisedTesseract.get(32),
                GTOreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 32),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 256),
                Materials.Infinity.getMolten(144 * 32) },
            CustomItemList.eM_Ultimate_Containment_Field.get(1),
            20 * 180,
            (int) RECIPE_UIV);

        // CustomItemList.eM_Teleportation blockCasingsTT 10
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_PlasmaForge.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 16),
                CustomItemList.eM_Ultimate_Containment.get(4),
                ItemList.Casing_Dim_Bridge.get(4),
                ItemList.Casing_Dim_Injector.get(4),

                StellarConstructionFrameMaterial.get(4),
                ItemList.Field_Generator_UMV.get(3),
                ItemList.Emitter_UMV.get(6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 1),

                SpaceWarper.get(24),
                ParticleTrapTimeSpaceShield.get(32),
                GTOreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 16),
                GTOreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 16),

                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 8))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                MaterialsAlloy.PIKYONIUM.getFluidStack(144 * 64),
                Materials.UUMatter.getFluid(1000 * 64),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 16))
            .itemOutputs(CustomItemList.eM_Teleportation.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 120)
            .addTo(AssemblyLine);
    }
}
