package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static gregtech.api.util.GTModHandler.addCraftingRecipe;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.thing.CustomItemList;

public class SingleBlockRecipes {

    // spotless:off
    public static void loadRecipes() {
        // Borophene Based Nanowire Composite Thermal Conductive Casing
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.InfinityCatalyst, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 4),
                GTCMItemList.BoropheneBasedNanowireCompositeThermalConductiveMedium.get(3),
                ItemList.neutroniumHeatCapacitor.get(1))
            .fluidInputs(
                MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144 * 8),
                FluidRegistry.getFluidStack("sodiumpotassium", 1000 * 6),
                Materials.Boron.getPlasma(144 * 16),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 12))
            .itemOutputs(GTCMItemList.BoropheneBasedNanowireCompositeThermalConductiveCasing.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(20 * 30)
            .specialValue(3)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        // Neutronium Pipe Casing
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(GTCMItemList.NeutroniumPipeCasing.get(1))
            .eut(TierEU.RECIPE_LV)
            .duration(20 * 5)
            .addTo(RecipeMaps.assemblerRecipes);

        addCraftingRecipe(
            GTCMItemList.NeutroniumPipeCasing.get(1),
            new Object[] { "ABA", "BCB", "ABA",
                'A', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 1),
                'B', GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 1),
                'C', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1)});

        // Cryogenic Cooling Casing
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                MaterialsAlloy.INCONEL_625.getFrameBox(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NiobiumTitanium, 8),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Osmiridium, 2),
                ItemList.Electric_Pump_ZPM.get(2))
            .fluidInputs(Materials.SuperCoolant.getFluid(16000))
            .itemOutputs(GTCMItemList.CryogenicCoolingCasing.get(4))
            .eut(TierEU.RECIPE_ZPM)
            .duration(20 * 30)
            .addTo(RecipeMaps.assemblerRecipes);

        // Neutronium Load-Bearing Casing
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bedrockium, 1),
                MaterialsAlloy.PIKYONIUM.getPlateDense(2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 16))
            .itemOutputs(GTCMItemList.NeutroniumSupportCasing.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(20 * 10)
            .addTo(RecipeMaps.formingPressRecipes);

        // Energy Conduction Glass: RGB optics, crystal control and a glass-fiber cladding.
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_dynamoTunnel2_ZPM.get(1))
            .metadata(SCANNING, new Scanning(20 * 60, TierEU.RECIPE_ZPM))
            .itemInputs(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 5),
                CustomItemList.eM_dynamoTunnel2_ZPM.get(1),
                CustomItemList.eM_energyTunnel2_ZPM.get(1),
                ItemList.Field_Generator_ZPM.get(1),

                ItemList.Emitter_ZPM.get(2),
                ItemList.Sensor_ZPM.get(2),
                ItemList.Circuit_Parts_Crystal_Chip_Master.get(4),
                ItemList.QuantumStar.get(4),

                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Ruby, 1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Emerald, 1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Sapphire, 1),
                ItemList.Circuit_Parts_GlassFiber.get(32))
            .fluidInputs(
                Materials.SuperconductorZPMBase.getMolten(144 * 16),
                MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 8))
            .itemOutputs(GTCMItemList.EnergyFluxContainmentGlass.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(20 * 10)
            .addTo(GTRecipeConstants.AssemblyLine);
    }
    // spotless:on
}
