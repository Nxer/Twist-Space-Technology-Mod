package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WhiteDwarfMold_Ingot;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MiracleTopRecipes;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gtPlusPlus.core.item.chemistry.RocketFuels.Liquid_Hydrogen;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class SingleItemRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        // 流汗黄豆
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(17),
                new ItemStack(Items.golden_apple, 1, 1),
                ItemList.Emitter_LV.get(64),
                ItemList.Field_Generator_LV.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 64))
            .itemOutputs(GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.get(1))
            .eut(RECIPE_LV)
            .duration(20 * 114)
            .addTo(assemblerRecipes);

        // Ball Lightning Upgrade Chip
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                GTCMItemList.CriticalPhoton.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                ItemList.ZPM.get(1),
                ItemList.ZPM5.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),

                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
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
            .itemOutputs(GTCMItemList.BallLightningUpgradeChip.get(1))
            .eut(RECIPE_UIV)
            .duration(630_720_000)
            .addTo(MiracleTopRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                GTCMItemList.CriticalPhoton.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                ItemUtils.getSimpleStack(ModItems.itemChargePack_High_4, 1),
                ItemList.ZPM5.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),

                GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 64),
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
            .itemOutputs(GTCMItemList.BallLightningUpgradeChip.get(1))
            .eut(RECIPE_UIV)
            .duration(630_720_000)
            .addTo(MiracleTopRecipes);
        // End Region

        // White Dwarf Mold(Ingot)
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Neutronium.getIngots(1))
            .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(576))
            .itemOutputs(WhiteDwarfMold_Ingot.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 10)
            .addTo(RecipeMaps.fluidSolidifierRecipes);
    }
}
