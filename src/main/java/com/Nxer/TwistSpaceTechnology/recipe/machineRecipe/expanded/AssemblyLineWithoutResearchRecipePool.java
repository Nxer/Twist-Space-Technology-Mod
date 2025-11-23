package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.util.TstUtils.setStackSize;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LuV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static com.google.common.math.LongMath.pow;
import static gregtech.api.enums.ItemList.DysonSwarmModule;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.core.material.MaterialMisc.MUTATED_LIVING_SOLDER;
import static tectech.thing.CustomItemList.EOH_Reinforced_Spatial_Casing;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier0;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier1;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier2;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier3;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier4;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier5;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier6;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier7;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier0;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier1;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier2;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier3;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier4;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier5;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier6;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier7;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier0;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier1;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier2;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier3;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier4;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier5;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier6;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier7;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.LanthItemList;
import wanion.avaritiaddons.block.chest.infinity.BlockInfinityChest;

public class AssemblyLineWithoutResearchRecipePool {

    public static ItemStack transToWildCircuit(ItemStack items) {
        ItemData tPrefixMaterial = GTOreDictUnificator.getAssociation(items);

        if (tPrefixMaterial == null || !tPrefixMaterial.hasValidPrefixMaterialData()) return null;
        if (tPrefixMaterial.mPrefix == OrePrefixes.circuit) {
            return GTOreDictUnificator.get(false, items, true);
        }
        return null;
    }

    public static List<ItemStack[]> generateAllItemInput(ItemStack[] baseStack, ItemStack[][] wildCard) {
        List<ItemStack[]> result = new ArrayList<>();
        result.add(GTUtility.copyItemArray(baseStack));
        int len = baseStack.length;
        for (int i = 0; i < len; i++) {
            if (wildCard[i] == null) continue;
            for (int j = 1; j < wildCard[i].length; j++) {
                if (wildCard[i][j] == null) continue;
                ItemStack wildCardCopy = wildCard[i][j].copy();
                int resultSize = result.size();
                for (int k = 0; k < resultSize; k++) {
                    ItemStack[] inputList = GTUtility.copyItemArray(result.get(k));
                    inputList[i] = wildCardCopy;
                    result.add(inputList);
                }
            }
        }
        return result;
    }

    public static void loadRecipes() {
        TwistSpaceTechnology.LOG.info("Loading Mega Assembly Line Recipes.");

        // skip these recipes
        ItemStack[] skipRecipeOutputs = new ItemStack[] { ItemList.Circuit_Wetwaremainframe.get(1),
            ItemList.Circuit_Biowaresupercomputer.get(1), ItemList.Circuit_Biomainframe.get(1),
            SpacetimeCompressionFieldGeneratorTier0.get(1), SpacetimeCompressionFieldGeneratorTier1.get(1),
            SpacetimeCompressionFieldGeneratorTier2.get(1), SpacetimeCompressionFieldGeneratorTier3.get(1),
            SpacetimeCompressionFieldGeneratorTier4.get(1), SpacetimeCompressionFieldGeneratorTier5.get(1),
            SpacetimeCompressionFieldGeneratorTier6.get(1), SpacetimeCompressionFieldGeneratorTier7.get(1),
            SpacetimeCompressionFieldGeneratorTier8.get(1), TimeAccelerationFieldGeneratorTier0.get(1),
            TimeAccelerationFieldGeneratorTier1.get(1), TimeAccelerationFieldGeneratorTier2.get(1),
            TimeAccelerationFieldGeneratorTier3.get(1), TimeAccelerationFieldGeneratorTier4.get(1),
            TimeAccelerationFieldGeneratorTier5.get(1), TimeAccelerationFieldGeneratorTier6.get(1),
            TimeAccelerationFieldGeneratorTier7.get(1), TimeAccelerationFieldGeneratorTier8.get(1),
            StabilisationFieldGeneratorTier0.get(1), StabilisationFieldGeneratorTier1.get(1),
            StabilisationFieldGeneratorTier2.get(1), StabilisationFieldGeneratorTier3.get(1),
            StabilisationFieldGeneratorTier4.get(1), StabilisationFieldGeneratorTier5.get(1),
            StabilisationFieldGeneratorTier6.get(1), StabilisationFieldGeneratorTier7.get(1),
            StabilisationFieldGeneratorTier8.get(1), ItemList.Hatch_Energy_LuV.get(1), ItemList.Hatch_Energy_ZPM.get(1),
            ItemList.Hatch_Energy_UV.get(1), ItemList.Hatch_Energy_UHV.get(1), ItemList.Hatch_Dynamo_LuV.get(1),
            ItemList.Hatch_Dynamo_ZPM.get(1), ItemList.Hatch_Dynamo_UV.get(1), ItemList.Hatch_Dynamo_UHV.get(1),
            ItemList.Casing_Dim_Injector.get(1), ItemList.Casing_Dim_Trans.get(1),
            ItemRefer.Advanced_Radiation_Protection_Plate.get(1),
            tectech.thing.CustomItemList.eM_energyTunnel8_UXV.get(1),
            tectech.thing.CustomItemList.eM_dynamoTunnel8_UXV.get(1),
            tectech.thing.CustomItemList.eM_energyTunnel9_UXV.get(1),
            tectech.thing.CustomItemList.eM_dynamoTunnel9_UXV.get(1),
            new ItemStack(LanthItemList.FOCUS_MANIPULATION_CASING),
            new ItemStack(LanthItemList.TARGET_RECEPTACLE_CASING) };

        // start check assembly line recipes
        checkRecipe: for (var recipe : GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes) {
            // debugLogInfo("Recipe output: " + recipe.mOutput.getDisplayName());

            for (ItemStack skip : skipRecipeOutputs) {
                // skip recipes need skip
                if (GTUtility.areStacksEqual(recipe.mOutput, skip)) {
                    // debugLogInfo("Skip recipe.");
                    continue checkRecipe;
                }
            }

            ItemStack[] inputItems = new ItemStack[recipe.mInputs.length];
            ItemStack[][] inputWildcards = new ItemStack[recipe.mInputs.length][];
            boolean hasCustomWildcardItemList = false;

            if (recipe.mOreDictAlt != null && recipe.mOreDictAlt.length > 0) {
                // wildcards recipe
                for (int i = 0; i < recipe.mOreDictAlt.length; i++) {
                    if (recipe.mOreDictAlt[i] != null && recipe.mOreDictAlt[i].length > 0) {
                        ItemStack circuitStack = transToWildCircuit(recipe.mOreDictAlt[i][0]);
                        if (circuitStack != null) {
                            // this wildcard is a circuit stack
                            // replace it by dreamcraft:anyCircuit then the recipe will check this stack by any circuit
                            inputItems[i] = circuitStack;
                        } else {
                            // this wildcard is a custom list
                            hasCustomWildcardItemList = true;
                            inputWildcards[i] = recipe.mOreDictAlt[i];
                        }
                    } else {
                        // this stack is normal
                        inputItems[i] = recipe.mInputs[i];
                    }
                }
            } else {
                // no wildcards recipe
                inputItems = recipe.mInputs;
            }

            if (!hasCustomWildcardItemList) {
                // debugLogInfo("Normal recipe generating.");
                GTRecipeBuilder ra = GTValues.RA.stdBuilder();
                ra.itemInputs(TstUtils.toNonNullItemStackArray(inputItems))
                    .itemOutputs(recipe.mOutput);
                if (recipe.mFluidInputs != null) {
                    ra.fluidInputs(TstUtils.toNonNullFluidStackArray(recipe.mFluidInputs));
                }
                ra.eut(recipe.mEUt)
                    .duration(recipe.mDuration)
                    .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);

            } else {
                // debugLogInfo("Wildcard recipe generating.");
                for (int i = 0; i < inputItems.length; i++) {
                    if (inputItems[i] == null) {
                        if (inputWildcards[i] != null && inputWildcards[i].length > 0) {
                            inputItems[i] = inputWildcards[i][0];
                        }
                    }
                }
                List<ItemStack[]> inputCombine = generateAllItemInput(inputItems, inputWildcards);
                // debugLogInfo("inputCombine.size " + inputCombine.size());
                // int loopFlag = 1;
                for (ItemStack[] inputs : inputCombine) {
                    // debugLogInfo("generate " + loopFlag);
                    // debugLogInfo("Input item list: " + Arrays.toString(inputs));
                    // loopFlag++;

                    GTRecipeBuilder ra = GTValues.RA.stdBuilder();
                    ra.itemInputs(TstUtils.toNonNullItemStackArray(inputs))
                        .itemOutputs(recipe.mOutput);
                    if (recipe.mFluidInputs != null) {
                        ra.fluidInputs(TstUtils.toNonNullFluidStackArray(recipe.mFluidInputs));
                    }
                    ra.eut(recipe.mEUt)
                        .duration(recipe.mDuration)
                        .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
                }
            }
        }

        TwistSpaceTechnology.LOG.info("load Special Recipes.");
        loadSpecialRecipes();
        TwistSpaceTechnology.LOG.info("Mega Assembly Line Recipes loading complete.");
        // debugLogInfo(
        // "Mega Assembly Line Recipe List size: " + GTCMRecipe.AssemblyLineWithoutResearchRecipe.getAllRecipes()
        // .size());
    }

    public static void loadSpecialRecipes() {
        final RecipeMap<?> MASL = GTCMRecipe.AssemblyLineWithoutResearchRecipe;
        final Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");
        final Fluid solderIndalloy = MaterialsAlloy.INDALLOY_140.getFluid();
        final Fluid ic2coolant = FluidRegistry.getFluid("ic2coolant");

        // GTNH Lanthanides Focus Manipulation Casing and Target Receptacle Casing
        {
            TST_RecipeBuilder.builder()
                .fluidInputs(
                    Materials.SolderingAlloy.getMolten(8000),
                    Materials.Gold.getMolten(2000),
                    Materials.Argon.getGas(1000))
                .itemInputs(
                    GTUtility.getIntegratedCircuit(1),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),
                    ItemList.Robot_Arm_LuV.get(4),
                    ItemList.Conveyor_Module_LuV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 2))
                .itemOutputs(new ItemStack(LanthItemList.FOCUS_MANIPULATION_CASING))
                .duration(60 * GTRecipeBuilder.SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(MASL);

            TST_RecipeBuilder.builder()
                .fluidInputs(
                    Materials.SolderingAlloy.getMolten(8000),
                    Materials.Gold.getMolten(2000),
                    Materials.Argon.getGas(1000))
                .itemInputs(
                    GTUtility.getIntegratedCircuit(2),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),
                    ItemList.Robot_Arm_LuV.get(4),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 2))
                .itemOutputs(new ItemStack(LanthItemList.TARGET_RECEPTACLE_CASING))
                .duration(60 * GTRecipeBuilder.SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(MASL);

        }

        {
            // Circuits

            // Wetware Mainframe
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 2),
                    ItemList.Circuit_Wetwaresupercomputer.get(2),
                    ItemList.Circuit_Parts_InductorXSMD.get(4),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(4),
                    ItemList.Circuit_Parts_ResistorXSMD.get(4),
                    ItemList.Circuit_Parts_TransistorXSMD.get(4),
                    ItemList.Circuit_Parts_DiodeXSMD.get(4),
                    ItemList.Circuit_Chip_Ram.get(48),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 16),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 64))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 20),
                    new FluidStack(ic2coolant, 10000),
                    Materials.Radon.getGas(2500))
                .itemOutputs(ItemList.Circuit_Wetwaremainframe.get(1))
                .eut(300000)
                .duration(20 * 100)
                .addTo(MASL);
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 2),
                    ItemList.Circuit_Wetwaresupercomputer.get(2),
                    ItemList.Circuit_Parts_InductorXSMD.get(4),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(4),
                    ItemList.Circuit_Parts_ResistorXSMD.get(4),
                    ItemList.Circuit_Parts_TransistorXSMD.get(4),
                    ItemList.Circuit_Parts_DiodeXSMD.get(4),
                    ItemList.Circuit_Chip_Ram.get(48),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 16),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.StyreneButadieneRubber, 64))
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 20),
                    new FluidStack(ic2coolant, 10000),
                    Materials.Radon.getGas(2500))
                .itemOutputs(ItemList.Circuit_Wetwaremainframe.get(1))
                .eut(300000)
                .duration(20 * 100)
                .addTo(MASL);

            // Bioware Supercomputer
            TST_RecipeBuilder.builder()
                .itemInputs(
                    ItemList.Circuit_Board_Bio_Ultra.get(2),
                    ItemList.Circuit_Biowarecomputer.get(2),
                    ItemList.Circuit_Parts_TransistorXSMD.get(4),
                    ItemList.Circuit_Parts_ResistorXSMD.get(4),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(4),
                    ItemList.Circuit_Parts_DiodeXSMD.get(4),
                    ItemList.Circuit_Chip_NOR.get(32),
                    ItemList.Circuit_Chip_Ram.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 32),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 64))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 10),
                    Materials.BioMediumSterilized.getFluid(144 * 10),
                    Materials.SuperCoolant.getFluid(10000))
                .itemOutputs(ItemList.Circuit_Biowaresupercomputer.get(1))
                .eut(491520)
                .duration(20 * 200)
                .addTo(MASL);
            TST_RecipeBuilder.builder()
                .itemInputs(
                    ItemList.Circuit_Board_Bio_Ultra.get(2),
                    ItemList.Circuit_Biowarecomputer.get(2),
                    ItemList.Circuit_Parts_TransistorXSMD.get(4),
                    ItemList.Circuit_Parts_ResistorXSMD.get(4),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(4),
                    ItemList.Circuit_Parts_DiodeXSMD.get(4),
                    ItemList.Circuit_Chip_NOR.get(32),
                    ItemList.Circuit_Chip_Ram.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 32),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.StyreneButadieneRubber, 64))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 10),
                    Materials.BioMediumSterilized.getFluid(144 * 10),
                    Materials.SuperCoolant.getFluid(10000))
                .itemOutputs(ItemList.Circuit_Biowaresupercomputer.get(1))
                .eut(491520)
                .duration(20 * 200)
                .addTo(MASL);

            // Bioware Mainframe
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 4),
                    ItemList.Circuit_Biowaresupercomputer.get(2),
                    ItemList.Circuit_Parts_InductorXSMD.get(6),
                    ItemList.Circuit_Parts_TransistorXSMD.get(6),
                    ItemList.Circuit_Parts_ResistorXSMD.get(6),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(6),
                    ItemList.Circuit_Parts_DiodeXSMD.get(6),
                    ItemList.Circuit_Chip_Ram.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 20),
                    Materials.BioMediumSterilized.getFluid(144 * 20),
                    Materials.SuperCoolant.getFluid(20000))
                .itemOutputs(ItemList.Circuit_Biomainframe.get(1))
                .eut(1966080)
                .duration(20 * 300)
                .addTo(MASL);
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 4),
                    ItemList.Circuit_Biowaresupercomputer.get(2),
                    ItemList.Circuit_Parts_InductorXSMD.get(6),
                    ItemList.Circuit_Parts_TransistorXSMD.get(6),
                    ItemList.Circuit_Parts_ResistorXSMD.get(6),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(6),
                    ItemList.Circuit_Parts_DiodeXSMD.get(6),
                    ItemList.Circuit_Chip_Ram.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.StyreneButadieneRubber, 64),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 20),
                    Materials.BioMediumSterilized.getFluid(144 * 20),
                    Materials.SuperCoolant.getFluid(20000))
                .itemOutputs(ItemList.Circuit_Biomainframe.get(1))
                .eut(1966080)
                .duration(20 * 300)
                .addTo(MASL);

        }

        {
            // Dyson Swarm Module
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTCMItemList.SolarSail.get(16),
                    GTCMItemList.StellarConstructionFrameMaterial.get(4),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 24),
                    ItemList.Circuit_Wafer_QPIC.get(32),
                    ItemList.Emitter_UMV.get(4),
                    ItemList.Sensor_UMV.get(4))
                .fluidInputs(new FluidStack(solderUEV, 144 * 256))
                .itemOutputs(setStackSize(DysonSwarmModule.get(1), 16384))
                .eut(100000000)
                .duration(20 * 50)
                .addTo(MASL);
        }

        {
            // adv radiation proof plate
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(24),
                    Materials.Neutronium.getNanite(1),
                    GTUtility.copyAmountUnsafe(4096, Materials.Lanthanum.getPlates(1)),
                    GTUtility.copyAmountUnsafe(6144, Materials.NaquadahAlloy.getPlates(1)),
                    new ItemStack(ModItems.itemStandarParticleBase, 1))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 1024),
                    Materials.Lead.getMolten(144 * 16 * 1024),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 8),
                    Materials.UUMatter.getFluid(1000 * 16))
                .itemOutputs(GTUtility.copyAmountUnsafe(2048, ItemRefer.Advanced_Radiation_Protection_Plate.get(1)))
                .eut(RECIPE_UXV)
                .duration(20 * 10)
                .addTo(MASL);
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    ItemRefer.Radiation_Protection_Plate.get(2),
                    Materials.ElectrumFlux.getPlates(4),
                    Materials.Trinium.getPlates(4),
                    Materials.NaquadahAlloy.getPlates(4),
                    Materials.Osmiridium.getPlates(4),
                    Materials.VibrantAlloy.getPlates(4),
                    ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(4))
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(8 * INGOTS))
                .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(1))
                .eut(RECIPE_ZPM / 2)
                .duration(50*SECONDS)
                .addTo(MASL);
        }

        {
            // ultimate battery

            // UMV
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                    GTUtility.copyAmountUnsafe(6144, ItemList.Circuit_Parts_Crystal_Chip_Master.get(1)),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 16),
                    ItemList.Field_Generator_UEV.get(8),

                    ItemList.Circuit_Wafer_PPIC.get(64),
                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 64),
                    MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144 * 64),
                    Materials.UUMatter.getFluid(1000 * 64))
                .itemOutputs(ItemList.ZPM3.get(1))
                .eut(32_000_000)
                .duration(20 * 128)
                .addTo(MASL);

            // UXV
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 64),
                    GTUtility.copyAmountUnsafe(48912, ItemList.Circuit_Parts_Crystal_Chip_Master.get(1)),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Optical, 16),
                    ItemList.Field_Generator_UIV.get(8),

                    ItemList.Circuit_Wafer_QPIC.get(64),
                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 64))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 128),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 128),
                    Materials.UUMatter.getFluid(1000 * 128))
                .itemOutputs(ItemList.ZPM4.get(1))
                .eut(128_000_000)
                .duration(20 * 256)
                .addTo(MASL);

            // MAX
            TST_RecipeBuilder.builder()
                .itemInputs(
                    MaterialsElements.STANDALONE.HYPOGEN.getPlateDense(64),
                    GTUtility.copyAmountUnsafe(391296, ItemList.Circuit_Parts_Crystal_Chip_Master.get(1)),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 16),
                    ItemList.Field_Generator_UMV.get(8),

                    GTUtility.copyAmountUnsafe(2048, ItemList.Circuit_Wafer_QPIC.get(1)),
                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 64))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 256),
                    MaterialsUEVplus.Eternity.getMolten(144 * 256),
                    Materials.UUMatter.getFluid(1000 * 256))
                .itemOutputs(ItemList.ZPM5.get(1))
                .eut(512_000_000)
                .duration(20 * 512)
                .addTo(MASL);

            // ERR
            TST_RecipeBuilder.builder()
                .itemInputs(
                    MaterialsElements.STANDALONE.DRAGON_METAL.getBlock(64),
                    GTUtility.copyAmountUnsafe(3130368, ItemList.Circuit_Parts_Crystal_Chip_Master.get(1)),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 16),
                    ItemList.Field_Generator_UXV.get(2),

                    ItemList.EnergisedTesseract.get(64),
                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 512),
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 128),
                    Materials.UUMatter.getFluid(1000 * 512))
                .itemOutputs(ItemList.ZPM6.get(1))
                .eut(2_048_000_000)
                .duration(20 * 1024)
                .addTo(MASL);

        }

        {

            // Dimensionally Injection Casing
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(2),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Ledox, 1),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CallistoIce, 1),
                    ItemList.Reactor_Coolant_Sp_6.get(1L),
                    getModItem(GTPlusPlus.ID, "itemScrewLaurenium", 12, 0),
                    new Object[] { OrePrefixes.circuit.get(Materials.Elite), 2L },
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 2),
                    ItemList.Super_Chest_IV.get(1),
                    ItemList.Super_Tank_IV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 1, 0))
                .fluidInputs(
                    WerkstoffLoader.Oganesson.getFluidOrGas(1000),
                    new FluidStack(solderUEV, 576),
                    Materials.NaquadahEnriched.getMolten(288L))
                .itemOutputs(ItemList.Casing_Dim_Injector.get(1))
                .eut(32_000_000)
                .duration(20 * 20)
                .addTo(MASL);

            // Dimensionally Transcendent Casing
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6),
                    getModItem(GTPlusPlus.ID, "itemScrewLaurenium", 12, 0),
                    ItemList.Reactor_Coolant_Sp_6.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1))
                .fluidInputs(
                    WerkstoffLoader.Oganesson.getFluidOrGas(500),
                    new FluidStack(solderUEV, 288),
                    Materials.NaquadahEnriched.getMolten(144L))
                .itemOutputs(ItemList.Casing_Dim_Trans.get(1))
                .eut(32_000_000)
                .duration(20 * 20)
                .addTo(MASL);
        }

        // Energy hatch and dynamo hatch of LuV - UHV
        {

            // LuV energy hatch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2),
                    ItemList.Circuit_Chip_UHPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 },
                    ItemList.LuV_Coil.get(2),
                    ItemList.Electric_Pump_LuV.get(1))
                .itemOutputs(ItemList.Hatch_Energy_LuV.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 2000), new FluidStack(solderIndalloy, 720))
                .duration(20 * 20)
                .eut(RECIPE_LuV)
                .addTo(MASL);
            // ZPM energy hatch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 2),
                    ItemList.Circuit_Chip_NPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 2 },
                    ItemList.ZPM_Coil.get(2),
                    ItemList.Electric_Pump_ZPM.get(1))
                .itemOutputs(ItemList.Hatch_Energy_ZPM.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 4000), new FluidStack(solderIndalloy, 1440))
                .duration(30 * 20)
                .eut(RECIPE_ZPM)
                .addTo(MASL);
            // UV energy hatch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 2),
                    ItemList.Circuit_Chip_PPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2 },
                    ItemList.UV_Coil.get(2),
                    ItemList.Electric_Pump_UV.get(1))
                .itemOutputs(ItemList.Hatch_Energy_UV.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 8000), new FluidStack(solderIndalloy, 2880))
                .duration(40 * 20)
                .eut(RECIPE_UV)
                .addTo(MASL);
            // UHV energy hatch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2L),
                    ItemList.Circuit_Chip_QPIC.get(2L),
                    new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L },
                    ItemList.UHV_Coil.get(2L),
                    ItemList.Electric_Pump_UHV.get(1L))
                .itemOutputs(ItemList.Hatch_Energy_UHV.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 16000), new FluidStack(solderIndalloy, 40 * 144))
                .duration(50 * 20)
                .eut(RECIPE_UHV)
                .addTo(MASL);

            // LuV dynamo hatch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GTOreDictUnificator.get(
                        OrePrefixes.spring,
                        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                        2),
                    ItemList.Circuit_Chip_UHPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 },
                    ItemList.LuV_Coil.get(2),
                    ItemList.Electric_Pump_LuV.get(1))
                .itemOutputs(ItemList.Hatch_Dynamo_LuV.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 2000), new FluidStack(solderIndalloy, 720))
                .duration(20 * 20)
                .eut(RECIPE_LuV)
                .addTo(MASL);
            // ZPM dynamo hatch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GTOreDictUnificator.get(OrePrefixes.spring, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 4),
                    ItemList.Circuit_Chip_NPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 2 },
                    ItemList.ZPM_Coil.get(2),
                    ItemList.Electric_Pump_ZPM.get(1))
                .itemOutputs(ItemList.Hatch_Dynamo_ZPM.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 4000), new FluidStack(solderIndalloy, 1440))
                .duration(30 * 20)
                .eut(RECIPE_ZPM)
                .addTo(MASL);
            // UV dynamo hatch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuvwire, 4),
                    ItemList.Circuit_Chip_PPIC.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2 },
                    ItemList.UV_Coil.get(2),
                    ItemList.Electric_Pump_UV.get(1))
                .itemOutputs(ItemList.Hatch_Dynamo_UV.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 8000), new FluidStack(solderIndalloy, 2880))
                .duration(40 * 20)
                .eut(RECIPE_UV)
                .addTo(MASL);
            // UHV dynamo hatch
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuhvwire, 8L),
                    ItemList.Circuit_Chip_QPIC.get(2L),
                    new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L },
                    ItemList.UHV_Coil.get(2L),
                    ItemList.Electric_Pump_UHV.get(1L))
                .itemOutputs(ItemList.Hatch_Dynamo_UHV.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 16000), new FluidStack(solderIndalloy, 40 * 144))
                .duration(50 * 20)
                .eut(RECIPE_UHV)
                .addTo(MASL);

        }

        // 4MA+ Lasers Target or Source
        {

            // 4M UXV Target
            GTValues.RA.stdBuilder()
                .itemInputsUnsafe(
                    GTUtility.getIntegratedCircuit(1),
                    ItemList.Hull_UXV.get(1),
                    GTUtility.copyAmountUnsafe(128, GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1)),
                    GTUtility.copyAmountUnsafe(128, ItemList.Sensor_UXV.get(1)),
                    GTUtility.copyAmountUnsafe(128, ItemList.Electric_Pump_UXV.get(1)),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 32))
                .itemOutputs(tectech.thing.CustomItemList.eM_energyTunnel8_UXV.get(1))
                .fluidInputs(new FluidStack(solderUEV, 1_296 * 64 * 4))
                .duration(106 * MINUTES + 40 * SECONDS)
                .eut(RECIPE_UXV)
                .addTo(MASL);

            // 4M UXV Source
            GTValues.RA.stdBuilder()
                .itemInputsUnsafe(
                    GTUtility.getIntegratedCircuit(1),
                    ItemList.Hull_UXV.get(1),
                    GTUtility.copyAmountUnsafe(128, GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1)),
                    GTUtility.copyAmountUnsafe(128, ItemList.Emitter_UXV.get(1)),
                    GTUtility.copyAmountUnsafe(128, ItemList.Electric_Pump_UXV.get(1)),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 32))
                .itemOutputs(tectech.thing.CustomItemList.eM_dynamoTunnel8_UXV.get(1))
                .fluidInputs(new FluidStack(solderUEV, 1_296 * 64 * 4))
                .duration(106 * MINUTES + 40 * SECONDS)
                .eut(RECIPE_UXV)
                .addTo(MASL);

            // 16M UXV Target
            GTValues.RA.stdBuilder()
                .itemInputsUnsafe(
                    GTUtility.getIntegratedCircuit(2),
                    ItemList.Hull_UXV.get(1),
                    GTUtility.copyAmountUnsafe(256, GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1)),
                    GTUtility.copyAmountUnsafe(256, ItemList.Sensor_UXV.get(1)),
                    GTUtility.copyAmountUnsafe(256, ItemList.Electric_Pump_UXV.get(1)),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 64))
                .itemOutputs(tectech.thing.CustomItemList.eM_energyTunnel9_UXV.get(1))
                .fluidInputs(new FluidStack(solderUEV, 1_296 * 128 * 4))
                .duration(213 * MINUTES + 20 * SECONDS)
                .eut(RECIPE_UXV)
                .addTo(MASL);

            // 16M UXV Source
            GTValues.RA.stdBuilder()
                .itemInputsUnsafe(
                    GTUtility.getIntegratedCircuit(2),
                    ItemList.Hull_UXV.get(1),
                    GTUtility.copyAmountUnsafe(256, GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1)),
                    GTUtility.copyAmountUnsafe(256, ItemList.Emitter_UXV.get(1)),
                    GTUtility.copyAmountUnsafe(256, ItemList.Electric_Pump_UXV.get(1)),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 64))
                .itemOutputs(tectech.thing.CustomItemList.eM_dynamoTunnel9_UXV.get(1))
                .fluidInputs(new FluidStack(solderUEV, 1_296 * 128 * 4))
                .duration(213 * MINUTES + 20 * SECONDS)
                .eut(RECIPE_UXV)
                .addTo(MASL);

        }

        // EOH Blocks
        {
            // ME Digital singularity.
            final ItemStack ME_Singularity = getModItem(
                "appliedenergistics2",
                "item.ItemExtremeStorageCell.Singularity",
                1);

            final ItemStack[] plateList = new ItemStack[] {
                // Dense Shirabon plate.
                GTOreDictUnificator.get("boltShirabon", 2),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
                GTOreDictUnificator
                    .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                GTOreDictUnificator
                    .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8) };

            final ItemStack[] boltList = new ItemStack[] {
                // Dense Shirabon plate.
                GTOreDictUnificator.get("boltShirabon", 2),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
                GTOreDictUnificator
                    .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                GTOreDictUnificator
                    .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8) };
            // spacetime 1
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(1),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(1),
                    ItemList.Quantum_Tank_IV.get(4),
                    new ItemStack(BlockInfinityChest.instance, 1),

                    GregtechItemList.CosmicFabricManipulator.get(1),
                    GTUtility.copyAmountUnsafe(1, ME_Singularity),
                    GGMaterial.shirabon.get(OrePrefixes.bolt, 2),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 20),
                    MaterialsUEVplus.Space.getMolten(144 * 10),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier0.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000)
                .addTo(MASL);

            // spacetime 2
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(2),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 2),
                    new ItemStack(BlockInfinityChest.instance, 2),

                    GregtechItemList.CosmicFabricManipulator.get(2),
                    GTUtility.copyAmountUnsafe(2, ME_Singularity),
                    GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 40),
                    MaterialsUEVplus.Space.getMolten(144 * 20),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier1.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 2)
                .addTo(MASL);

            // spacetime 3
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(3),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 3),
                    new ItemStack(BlockInfinityChest.instance, 3),

                    GregtechItemList.CosmicFabricManipulator.get(3),
                    GTUtility.copyAmountUnsafe(3, ME_Singularity),
                    GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 80),
                    MaterialsUEVplus.Space.getMolten(144 * 30),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier2.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 3)
                .addTo(MASL);

            // spacetime 4
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(4),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(1),
                    ItemList.Quantum_Tank_IV.get(4 * 4),
                    new ItemStack(BlockInfinityChest.instance, 4),

                    GregtechItemList.InfinityInfusedManipulator.get(1),
                    GTUtility.copyAmountUnsafe(4, ME_Singularity),
                    GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 160),
                    MaterialsUEVplus.Space.getMolten(144 * 40),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier3.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 4)
                .addTo(MASL);

            // spacetime 5
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(5),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 5),
                    new ItemStack(BlockInfinityChest.instance, 5),

                    GregtechItemList.InfinityInfusedManipulator.get(2),
                    GTUtility.copyAmountUnsafe(5, ME_Singularity),
                    GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 320),
                    MaterialsUEVplus.Space.getMolten(144 * 50),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier4.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 5)
                .addTo(MASL);

            // spacetime 6
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(6),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 6),
                    new ItemStack(BlockInfinityChest.instance, 6),

                    GregtechItemList.InfinityInfusedManipulator.get(3),
                    GTUtility.copyAmountUnsafe(6, ME_Singularity),
                    GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 640),
                    MaterialsUEVplus.Space.getMolten(144 * 60),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier5.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 6)
                .addTo(MASL);

            // spacetime 7
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(7),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(1),
                    ItemList.Quantum_Tank_IV.get(4 * 7),
                    new ItemStack(BlockInfinityChest.instance, 7),

                    GregtechItemList.SpaceTimeContinuumRipper.get(1),
                    GTUtility.copyAmountUnsafe(7, ME_Singularity),
                    GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 3))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 1280),
                    MaterialsUEVplus.Space.getMolten(144 * 70),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier6.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 7)
                .addTo(MASL);

            // spacetime 8
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(8),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 8),
                    new ItemStack(BlockInfinityChest.instance, 8),

                    GregtechItemList.SpaceTimeContinuumRipper.get(2),
                    GTUtility.copyAmountUnsafe(8, ME_Singularity),
                    GTOreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 3))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 2560),
                    MaterialsUEVplus.Space.getMolten(144 * 80),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier7.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 8)
                .addTo(MASL);

            // spacetime 9
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(9),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 9),
                    new ItemStack(BlockInfinityChest.instance, 9),

                    GregtechItemList.SpaceTimeContinuumRipper.get(3),
                    GTUtility.copyAmountUnsafe(9, ME_Singularity),
                    GTOreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 3))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 5120),
                    MaterialsUEVplus.Space.getMolten(144 * 90),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier8.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 9)
                .addTo(MASL);

            // EOH Time Dilation Field Generators.
            {
                final ItemStack baseCasing = tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing.get(1);

                // T0 - Shirabon
                // T1 - White Dwarf Matter
                // T2 - White Dwarf Matter
                // T3 - White Dwarf Matter
                // T4 - Black Dwarf Matter
                // T5 - Black Dwarf Matter
                // T6 - Black Dwarf Matter
                // T7 - Black Dwarf Matter
                // T8 - MHDCSM.

                final ItemStack[] fusionReactors = new ItemStack[] { ItemList.FusionComputer_ZPMV.get(1),
                    ItemList.FusionComputer_ZPMV.get(2), ItemList.FusionComputer_ZPMV.get(3),
                    ItemList.FusionComputer_UV.get(1), ItemList.FusionComputer_UV.get(2),
                    ItemList.FusionComputer_UV.get(3),
                    // MK4 Fusion Computer.
                    getModItem(GregTech.ID, "gt.blockmachines", 1, 965),
                    getModItem(GregTech.ID, "gt.blockmachines", 2, 965),
                    getModItem(GregTech.ID, "gt.blockmachines", 3, 965) };

                final ItemStack[] fusionCoils = new ItemStack[] {
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 1),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 2),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 3),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 2, 3),
                    getModItem(GoodGenerator.ID, "compactFusionCoil", 3, 3) };

                final ItemStack[] researchStuff = new ItemStack[] { baseCasing,
                    TimeAccelerationFieldGeneratorTier0.get(1), TimeAccelerationFieldGeneratorTier1.get(1),
                    TimeAccelerationFieldGeneratorTier2.get(1), TimeAccelerationFieldGeneratorTier3.get(1),
                    TimeAccelerationFieldGeneratorTier4.get(1), TimeAccelerationFieldGeneratorTier5.get(1),
                    TimeAccelerationFieldGeneratorTier6.get(1), TimeAccelerationFieldGeneratorTier7.get(1),
                    TimeAccelerationFieldGeneratorTier8.get(1) };

                // Spectral Components
                // Cycling should fix issues with conflicting recipes for T1-T2, T4-T5 & T7-T8
                final ItemStack[] spectralComponents = new ItemStack[] {
                    // Red Spectral Component
                    getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                    // Green Spectral Component
                    getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                    // Blue Spectral Component
                    getModItem(SuperSolarPanels.ID, "bluecomponent", 64) };

                for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTUtility.getIntegratedCircuit(absoluteTier + 1),

                            baseCasing,
                            fusionReactors[absoluteTier],
                            fusionCoils[absoluteTier],
                            // UV Solar panel
                            ItemList.Machine_UV_SolarPanel.get(absoluteTier + 1),

                            new Object[] { OrePrefixes.circuit.get(Materials.UXV), absoluteTier + 1 },
                            // Red Spectral Component
                            spectralComponents[absoluteTier % spectralComponents.length],
                            // Green Spectral Component
                            spectralComponents[(absoluteTier + 1) % spectralComponents.length],
                            // Blue Spectral Component
                            spectralComponents[(absoluteTier + 2) % spectralComponents.length],

                            plateList[absoluteTier],
                            ItemList.DysonSwarmDeploymentUnitCasing.get((absoluteTier + 1) * 4),
                            ItemList.DysonSwarmReceiverDish.get((absoluteTier + 1) * 4),
                            ItemList.AcceleratorUV.get((absoluteTier + 1) * 4),

                            ItemList.Energy_Module.get(absoluteTier + 1),
                            GTOreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, (absoluteTier + 1) * 4)

                        // baseCasing,
                        // fusionReactors[absoluteTier],
                        // fusionCoils[absoluteTier],
                        // getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", absoluteTier + 1, 0),
                        //
                        // GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, absoluteTier + 1),
                        // getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                        // getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                        // getModItem(SuperSolarPanels.ID, "bluecomponent", 64),
                        //
                        // boltList[absoluteTier],
                        // getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 2),
                        // getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 1),
                        // getModItem(GregTech.ID, "gt.blockmachines", (absoluteTier + 1) * 4, 11107),
                        //
                        // ItemList.Energy_Module.get(absoluteTier + 1),
                        // GTOreDictUnificator
                        // .get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, (absoluteTier + 1) * 4)
                        )
                        .fluidInputs(
                            MUTATED_LIVING_SOLDER.getFluidStack((int) (2_880 * pow(2, absoluteTier))),
                            MaterialsUEVplus.Time.getMolten(1_440 * (absoluteTier + 1)),
                            MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                        .itemOutputs(researchStuff[absoluteTier + 1])
                        .eut(RECIPE_UMV)
                        .duration((absoluteTier + 1) * 4_000 * 20)
                        .addTo(MASL);
                }

            }

            {
                final ItemStack baseCasing = tectech.thing.CustomItemList.EOH_Infinite_Energy_Casing.get(1);

                int baseCompPerSec = 16_384;

                // T0 - Shirabon
                // T1 - White Dwarf Matter
                // T2 - White Dwarf Matter
                // T3 - White Dwarf Matter
                // T4 - Black Dwarf Matter
                // T5 - Black Dwarf Matter
                // T6 - Black Dwarf Matter
                // T7 - Black Dwarf Matter
                // T8 - MHDCSM.

                final ItemStack[] researchStuff = new ItemStack[] { baseCasing,
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier0.get(1),
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier1.get(1),
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier2.get(1),
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier3.get(1),
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier4.get(1),
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier5.get(1),
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier6.get(1),
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier7.get(1),
                    tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8.get(1) };

                final ItemStack[] timeCasings = new ItemStack[] {
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier1.get(1),
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier2.get(1),
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier3.get(1),
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier4.get(1),
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier5.get(1),
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier6.get(1),
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier7.get(1),
                    tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1) };

                final ItemStack[] spatialCasings = new ItemStack[] {
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                    tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1) };

                for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            timeCasings[absoluteTier],
                            spatialCasings[absoluteTier],
                            baseCasing,
                            // Dyson Swarm Module.
                            ItemList.DysonSwarmModule.get(4 * (absoluteTier + 1)),

                            GTOreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4 * (absoluteTier + 1)),
                            GTOreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4 * (absoluteTier + 1)),
                            GTOreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4 * (absoluteTier + 1)),
                            GTOreDictUnificator.get(
                                OrePrefixes.frameGt,
                                Materials.Longasssuperconductornameforuhvwire,
                                4 * (absoluteTier + 1)),

                            // Gravitation Engine
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),

                            boltList[absoluteTier],
                            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2 * (absoluteTier + 1)),
                            GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, absoluteTier + 1),
                            GTOreDictUnificator
                                .get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, absoluteTier + 1))
                        .fluidInputs(
                            MUTATED_LIVING_SOLDER.getFluidStack((int) (2_880 * pow(2, absoluteTier))),
                            MaterialsUEVplus.Time.getMolten(1_440 * (absoluteTier + 1)),
                            MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                            MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                        .itemOutputs(researchStuff[absoluteTier + 1])
                        .eut(RECIPE_UMV)
                        .duration((absoluteTier + 1) * 4_000 * 20)
                        .addTo(MASL);
                }

            }

        }

    }

}
