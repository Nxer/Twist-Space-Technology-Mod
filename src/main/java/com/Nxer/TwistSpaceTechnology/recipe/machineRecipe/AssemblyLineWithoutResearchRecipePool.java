package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LuV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static com.github.technus.tectech.thing.CustomItemList.EOH_Reinforced_Spatial_Casing;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier0;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier1;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier2;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier3;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier4;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier5;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier6;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier7;
import static com.github.technus.tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier0;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier1;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier2;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier3;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier4;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier5;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier6;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier7;
import static com.github.technus.tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier0;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier1;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier2;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier3;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier4;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier5;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier6;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier7;
import static com.github.technus.tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8;
import static com.google.common.math.LongMath.pow;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gtPlusPlus.core.material.ALLOY.INDALLOY_140;
import static gtPlusPlus.core.material.MISC_MATERIALS.MUTATED_LIVING_SOLDER;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.items.MyMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import wanion.avaritiaddons.block.chest.infinity.BlockInfinityChest;

public class AssemblyLineWithoutResearchRecipePool implements IRecipePool {

    public ItemStack transToWildCircuit(ItemStack items) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(items);

        if (tPrefixMaterial == null || !tPrefixMaterial.hasValidPrefixMaterialData()) return null;
        if (tPrefixMaterial.mPrefix == OrePrefixes.circuit) {
            return GT_OreDictUnificator.get(false, items, true);
        }
        return null;
    }

    public static List<ItemStack[]> generateAllItemInput(ItemStack[] baseStack, ItemStack[][] wildCard) {
        List<ItemStack[]> result = new ArrayList<>();
        result.add(Utils.copyItemStackArray(baseStack));
        int len = baseStack.length;
        for (int i = 0; i < len; i++) {
            if (wildCard[i] == null) continue;
            for (int j = 1; j < wildCard[i].length; j++) {
                if (wildCard[i][j] == null) continue;
                ItemStack wildCardCopy = wildCard[i][j].copy();
                int resultSize = result.size();
                for (int k = 0; k < resultSize; k++) {
                    ItemStack[] inputList = Utils.copyItemStackArray(result.get(k));
                    inputList[i] = wildCardCopy;
                    result.add(inputList);
                }
            }
        }
        return result;
    }

    @Override
    public void loadRecipes() {
        TwistSpaceTechnology.LOG.info("Loading Mega Assembly Line Recipes.");

        // skip these recipes
        ItemStack[] skipRecipeOutputs = new ItemStack[] { ItemList.Circuit_Wetwaremainframe.get(1),
            ItemList.Circuit_Biowaresupercomputer.get(1), ItemList.Circuit_Biomainframe.get(1),
            ItemList.Circuit_OpticalAssembly.get(1), ItemList.Circuit_OpticalComputer.get(1),
            ItemList.Circuit_OpticalMainframe.get(1), SpacetimeCompressionFieldGeneratorTier0.get(1),
            SpacetimeCompressionFieldGeneratorTier1.get(1), SpacetimeCompressionFieldGeneratorTier2.get(1),
            SpacetimeCompressionFieldGeneratorTier3.get(1), SpacetimeCompressionFieldGeneratorTier4.get(1),
            SpacetimeCompressionFieldGeneratorTier5.get(1), SpacetimeCompressionFieldGeneratorTier6.get(1),
            SpacetimeCompressionFieldGeneratorTier7.get(1), SpacetimeCompressionFieldGeneratorTier8.get(1),
            TimeAccelerationFieldGeneratorTier0.get(1), TimeAccelerationFieldGeneratorTier1.get(1),
            TimeAccelerationFieldGeneratorTier2.get(1), TimeAccelerationFieldGeneratorTier3.get(1),
            TimeAccelerationFieldGeneratorTier4.get(1), TimeAccelerationFieldGeneratorTier5.get(1),
            TimeAccelerationFieldGeneratorTier6.get(1), TimeAccelerationFieldGeneratorTier7.get(1),
            TimeAccelerationFieldGeneratorTier8.get(1), StabilisationFieldGeneratorTier0.get(1),
            StabilisationFieldGeneratorTier1.get(1), StabilisationFieldGeneratorTier2.get(1),
            StabilisationFieldGeneratorTier3.get(1), StabilisationFieldGeneratorTier4.get(1),
            StabilisationFieldGeneratorTier5.get(1), StabilisationFieldGeneratorTier6.get(1),
            StabilisationFieldGeneratorTier7.get(1), StabilisationFieldGeneratorTier8.get(1),
            ItemList.Hatch_Energy_LuV.get(1), ItemList.Hatch_Energy_ZPM.get(1), ItemList.Hatch_Energy_UV.get(1),
            ItemList.Hatch_Energy_MAX.get(1), ItemList.Hatch_Dynamo_LuV.get(1), ItemList.Hatch_Dynamo_ZPM.get(1),
            ItemList.Hatch_Dynamo_UV.get(1), ItemList.Hatch_Dynamo_MAX.get(1), ItemList.Casing_Dim_Injector.get(1),
            ItemList.Casing_Dim_Trans.get(1), ItemRefer.Advanced_Radiation_Protection_Plate.get(1) };

        // start check assembly line recipes
        checkRecipe: for (var recipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes) {
            // debugLogInfo("Recipe output: " + recipe.mOutput.getDisplayName());

            for (ItemStack skip : skipRecipeOutputs) {
                // skip recipes need skip
                if (Utils.metaItemEqual(recipe.mOutput, skip)) {
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
                GT_RecipeBuilder ra = GT_Values.RA.stdBuilder();
                ra.itemInputs(Utils.sortNoNullArray(inputItems))
                    .itemOutputs(recipe.mOutput);
                if (recipe.mFluidInputs != null) {
                    ra.fluidInputs(Utils.sortNoNullArray(recipe.mFluidInputs));
                }
                ra.noOptimize()
                    .eut(recipe.mEUt)
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

                    GT_RecipeBuilder ra = GT_Values.RA.stdBuilder();
                    ra.itemInputs(Utils.sortNoNullArray(inputs))
                        .itemOutputs(recipe.mOutput);
                    if (recipe.mFluidInputs != null) {
                        ra.fluidInputs(Utils.sortNoNullArray(recipe.mFluidInputs));
                    }
                    ra.noOptimize()
                        .eut(recipe.mEUt)
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

    public void loadSpecialRecipes() {
        final RecipeMap<?> MASL = GTCMRecipe.AssemblyLineWithoutResearchRecipe;
        final Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");
        final Fluid solderIndalloy = INDALLOY_140.getFluid();
        final Fluid ic2coolant = FluidRegistry.getFluid("ic2coolant");

        {
            // adv radiation proof plate
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(24),
                    Materials.Neutronium.getNanite(1),
                    setStackSize(Materials.Lanthanum.getPlates(1), 4096),
                    setStackSize(Materials.NaquadahAlloy.getPlates(1), 6144),
                    ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 0, 1))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 1024),
                    Materials.Lead.getMolten(144 * 16 * 1024),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 8),
                    Materials.UUMatter.getFluid(1000 * 16))
                .itemOutputs(setStackSize(ItemRefer.Advanced_Radiation_Protection_Plate.get(1), 2048))
                .eut(RECIPE_UXV)
                .duration(20 * 10)
                .addTo(MASL);
        }

        {
            // ultimate battery

            // UMV
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                    setStackSize(ItemList.Circuit_Parts_Crystal_Chip_Master.get(1), 6144),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 16),
                    ItemList.Field_Generator_UEV.get(8),

                    ItemList.Circuit_Wafer_PPIC.get(64),
                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64))
                .fluidInputs(
                    new FluidStack(solderUEV, 144 * 64),
                    ELEMENT.STANDALONE.HYPOGEN.getFluidStack(144 * 64),
                    Materials.UUMatter.getFluid(1000 * 64))
                .itemOutputs(ItemList.ZPM3.get(1))
                .eut(32_000_000)
                .duration(20 * 128)
                .addTo(MASL);

            // UXV
            TST_RecipeBuilder.builder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 64),
                    setStackSize(ItemList.Circuit_Parts_Crystal_Chip_Master.get(1), 48912),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Optical, 16),
                    ItemList.Field_Generator_UIV.get(8),

                    ItemList.Circuit_Wafer_QPIC.get(64),
                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 64))
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
                    ELEMENT.STANDALONE.HYPOGEN.getPlateDense(64),
                    setStackSize(ItemList.Circuit_Parts_Crystal_Chip_Master.get(1), 391296),
                    // TODO piko circuit
                    CustomItemList.PikoCircuit.get(16),
                    ItemList.Field_Generator_UMV.get(8),

                    setStackSize(ItemList.Circuit_Wafer_QPIC.get(1), 2048),
                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 64))
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
                    ELEMENT.STANDALONE.DRAGON_METAL.getBlock(64),
                    setStackSize(ItemList.Circuit_Parts_Crystal_Chip_Master.get(1), 3130368),
                    // TODO piko circuit
                    CustomItemList.QuantumCircuit.get(16),
                    ItemList.Field_Generator_UXV.get(2),

                    ItemList.EnergisedTesseract.get(64),
                    CustomItemList.HighEnergyFlowCircuit.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64))
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Ledox, 1),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CallistoIce, 1),
                    ItemList.Reactor_Coolant_Sp_6.get(1L),
                    getModItem(GTPlusPlus.ID, "itemScrewLaurenium", 12, 0),
                    new Object[] { OrePrefixes.circuit.get(Materials.Elite), 2L },
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 2),
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6),
                    getModItem(GTPlusPlus.ID, "itemScrewLaurenium", 12, 0),
                    ItemList.Reactor_Coolant_Sp_6.get(1L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1))
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2),
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 2),
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 2),
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2L),
                    ItemList.Circuit_Chip_QPIC.get(2L),
                    new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L },
                    ItemList.UHV_Coil.get(2L),
                    ItemList.Electric_Pump_UHV.get(1L))
                .itemOutputs(ItemList.Hatch_Energy_MAX.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 16000), new FluidStack(solderIndalloy, 40 * 144))
                .duration(50 * 20)
                .eut(RECIPE_UHV)
                .addTo(MASL);

            // LuV dynamo hatch
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    GT_OreDictUnificator.get(
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 4),
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuvwire, 4),
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
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1L),
                    GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuhvwire, 8L),
                    ItemList.Circuit_Chip_QPIC.get(2L),
                    new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L },
                    ItemList.UHV_Coil.get(2L),
                    ItemList.Electric_Pump_UHV.get(1L))
                .itemOutputs(ItemList.Hatch_Dynamo_MAX.get(1))
                .fluidInputs(new FluidStack(ic2coolant, 16000), new FluidStack(solderIndalloy, 40 * 144))
                .duration(50 * 20)
                .eut(RECIPE_UHV)
                .addTo(MASL);

        }

        // EOH Blocks
        {
            // ME Digital singularity.
            final ItemStack ME_Singularity = getModItem(
                "appliedenergistics2",
                "item.ItemExtremeStorageCell.Singularity",
                1);

            final ItemStack[] boltList = new ItemStack[] {
                // Dense Shirabon plate.
                GT_OreDictUnificator.get("boltShirabon", 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
                GT_OreDictUnificator
                    .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                GT_OreDictUnificator
                    .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8) };
            // spacetime 1
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(1),
                    ItemList.Quantum_Tank_IV.get(4),
                    new ItemStack(BlockInfinityChest.instance, 1),

                    GregtechItemList.CosmicFabricManipulator.get(1),
                    Utils.copyAmount(1, ME_Singularity),
                    MyMaterial.shirabon.get(OrePrefixes.bolt, 2),
                    CustomItemList.QuantumCircuit.get(1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 20),
                    MaterialsUEVplus.Space.getMolten(144 * 10),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier0.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000)
                .addTo(MASL);

            // spacetime 2
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 2),
                    new ItemStack(BlockInfinityChest.instance, 2),

                    GregtechItemList.CosmicFabricManipulator.get(2),
                    Utils.copyAmount(2, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
                    CustomItemList.QuantumCircuit.get(1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 40),
                    MaterialsUEVplus.Space.getMolten(144 * 20),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier1.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 2)
                .addTo(MASL);

            // spacetime 3
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T7.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 3),
                    new ItemStack(BlockInfinityChest.instance, 3),

                    GregtechItemList.CosmicFabricManipulator.get(3),
                    Utils.copyAmount(3, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                    CustomItemList.QuantumCircuit.get(1))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 80),
                    MaterialsUEVplus.Space.getMolten(144 * 30),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier2.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 3)
                .addTo(MASL);

            // spacetime 4
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(4),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(1),
                    ItemList.Quantum_Tank_IV.get(4 * 4),
                    new ItemStack(BlockInfinityChest.instance, 4),

                    GregtechItemList.InfinityInfusedManipulator.get(1),
                    Utils.copyAmount(4, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
                    CustomItemList.QuantumCircuit.get(2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 160),
                    MaterialsUEVplus.Space.getMolten(144 * 40),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier3.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 4)
                .addTo(MASL);

            // spacetime 5
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(5),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 5),
                    new ItemStack(BlockInfinityChest.instance, 5),

                    GregtechItemList.InfinityInfusedManipulator.get(2),
                    Utils.copyAmount(5, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
                    CustomItemList.QuantumCircuit.get(2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 320),
                    MaterialsUEVplus.Space.getMolten(144 * 50),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier4.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 5)
                .addTo(MASL);

            // spacetime 6
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(6),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T8.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 6),
                    new ItemStack(BlockInfinityChest.instance, 6),

                    GregtechItemList.InfinityInfusedManipulator.get(3),
                    Utils.copyAmount(6, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
                    CustomItemList.QuantumCircuit.get(2))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 640),
                    MaterialsUEVplus.Space.getMolten(144 * 60),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier5.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 6)
                .addTo(MASL);

            // spacetime 7
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(7),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(1),
                    ItemList.Quantum_Tank_IV.get(4 * 7),
                    new ItemStack(BlockInfinityChest.instance, 7),

                    GregtechItemList.SpaceTimeContinuumRipper.get(1),
                    Utils.copyAmount(7, ME_Singularity),
                    GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
                    CustomItemList.QuantumCircuit.get(3))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 1280),
                    MaterialsUEVplus.Space.getMolten(144 * 70),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier6.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 7)
                .addTo(MASL);

            // spacetime 8
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(8),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(2),
                    ItemList.Quantum_Tank_IV.get(4 * 8),
                    new ItemStack(BlockInfinityChest.instance, 8),

                    GregtechItemList.SpaceTimeContinuumRipper.get(2),
                    Utils.copyAmount(8, ME_Singularity),
                    GT_OreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                    CustomItemList.QuantumCircuit.get(3))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(144 * 2560),
                    MaterialsUEVplus.Space.getMolten(144 * 80),
                    MaterialsUEVplus.SpaceTime.getMolten(144 * 10))
                .itemOutputs(SpacetimeCompressionFieldGeneratorTier7.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 4000 * 8)
                .addTo(MASL);

            // spacetime 9
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(9),

                    EOH_Reinforced_Spatial_Casing.get(1),
                    ItemRefer.YOTTank_Cell_T9.get(3),
                    ItemList.Quantum_Tank_IV.get(4 * 9),
                    new ItemStack(BlockInfinityChest.instance, 9),

                    GregtechItemList.SpaceTimeContinuumRipper.get(3),
                    Utils.copyAmount(9, ME_Singularity),
                    GT_OreDictUnificator
                        .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8),
                    CustomItemList.QuantumCircuit.get(3))
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
                final ItemStack baseCasing = com.github.technus.tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing
                    .get(1);

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

                for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_Utility.getIntegratedCircuit(absoluteTier + 1),

                            baseCasing,
                            fusionReactors[absoluteTier],
                            fusionCoils[absoluteTier],
                            getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", absoluteTier + 1, 0),

                            getItemContainer("QuantumCircuit").get(absoluteTier + 1),
                            getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                            getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                            getModItem(SuperSolarPanels.ID, "bluecomponent", 64),

                            boltList[absoluteTier],
                            getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 2),
                            getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 1),
                            getModItem(GregTech.ID, "gt.blockmachines", (absoluteTier + 1) * 4, 11107),

                            ItemList.Energy_Module.get(absoluteTier + 1),
                            GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, (absoluteTier + 1) * 4))
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
                final ItemStack baseCasing = com.github.technus.tectech.thing.CustomItemList.EOH_Infinite_Energy_Casing
                    .get(1);

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

                final ItemStack[] researchStuff = new ItemStack[] { baseCasing, StabilisationFieldGeneratorTier0.get(1),
                    StabilisationFieldGeneratorTier1.get(1), StabilisationFieldGeneratorTier2.get(1),
                    StabilisationFieldGeneratorTier3.get(1), StabilisationFieldGeneratorTier4.get(1),
                    StabilisationFieldGeneratorTier5.get(1), StabilisationFieldGeneratorTier6.get(1),
                    StabilisationFieldGeneratorTier7.get(1), StabilisationFieldGeneratorTier8.get(1) };

                final ItemStack[] timeCasings = new ItemStack[] { TimeAccelerationFieldGeneratorTier0.get(1),
                    TimeAccelerationFieldGeneratorTier1.get(1), TimeAccelerationFieldGeneratorTier2.get(1),
                    TimeAccelerationFieldGeneratorTier3.get(1), TimeAccelerationFieldGeneratorTier4.get(1),
                    TimeAccelerationFieldGeneratorTier5.get(1), TimeAccelerationFieldGeneratorTier6.get(1),
                    TimeAccelerationFieldGeneratorTier7.get(1), TimeAccelerationFieldGeneratorTier8.get(1) };

                final ItemStack[] spatialCasings = new ItemStack[] { SpacetimeCompressionFieldGeneratorTier0.get(1),
                    SpacetimeCompressionFieldGeneratorTier1.get(1), SpacetimeCompressionFieldGeneratorTier2.get(1),
                    SpacetimeCompressionFieldGeneratorTier3.get(1), SpacetimeCompressionFieldGeneratorTier4.get(1),
                    SpacetimeCompressionFieldGeneratorTier5.get(1), SpacetimeCompressionFieldGeneratorTier6.get(1),
                    SpacetimeCompressionFieldGeneratorTier7.get(1), SpacetimeCompressionFieldGeneratorTier8.get(1) };

                for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            timeCasings[absoluteTier],
                            spatialCasings[absoluteTier],
                            baseCasing,
                            // Dyson Swarm Module.
                            getModItem(GalaxySpace.ID, "item.DysonSwarmParts", 4 * (absoluteTier + 1), 0),

                            GT_OreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4 * (absoluteTier + 1)),
                            GT_OreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4 * (absoluteTier + 1)),
                            GT_OreDictUnificator
                                .get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4 * (absoluteTier + 1)),
                            GT_OreDictUnificator.get(
                                OrePrefixes.frameGt,
                                Materials.Longasssuperconductornameforuhvwire,
                                4 * (absoluteTier + 1)),

                            // Gravitation Engine
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                            getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),

                            boltList[absoluteTier],
                            getItemContainer("QuantumCircuit").get(2 * (absoluteTier + 1)),
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, absoluteTier + 1),
                            GT_OreDictUnificator
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
