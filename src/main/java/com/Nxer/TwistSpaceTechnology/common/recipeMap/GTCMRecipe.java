package com.Nxer.TwistSpaceTechnology.common.recipeMap;

import static com.Nxer.TwistSpaceTechnology.config.Config.Debug_DisplayAdvCircuitAssemblyLineCurrentRecipe;
import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosRecipeMetadata;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureDisplay;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters.ArtificialStar_SpecialValueFormatter;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters.DSP_Receiver_SpecialValueFormatter;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_AquaticZoneSimulatorFronted;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_ArtificialGreenHouseFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_AssemblyLineFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosCredentialFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosExpandedFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosStructureFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_DirectedMobClonerFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_GeneralFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_IndustrialMagicMatrixFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_MiracleTopFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_RapidCoolingDownFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_RapidHeatExchangeFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_RecipeMapFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_StellarForgeFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_StrangeMatterAggregatorFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_SuperWaterPurifierFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_TreeGrowthSimulatorFrontend;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

import goodgenerator.client.GUI.GGUITextures;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeCategorySetting;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;
import gtnhlanth.common.register.LanthItemList;

public class GTCMRecipe {

    // #tr tst.recipe.IntensifyChemicalDistorterRecipeMap.name
    // # Intense Chemical Distorter
    // #zh_CN 深度化学扭曲
    public static final RecipeMap<TST_RecipeMapBackend> IntensifyChemicalDistorterRecipeMap = RecipeMapBuilder
        .of("tst.recipe.IntensifyChemicalDistorterRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 16)
        .neiSpecialInfoFormatter(
            recipeInfo -> Collections.singletonList(
                StatCollector.translateToLocalFormatted(
                    "GT5U.nei.heat_capacity.material",
                    HeatingCoilLevel.getDisplayNameFromHeat(recipeInfo.recipe.mSpecialValue, false))))
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.IntensifyChemicalDistorter.get(1))
                .setMultipleWidgetsAllowed(false))
        .build();

    // #tr tst.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipeMap.name
    // # Precise High-Energy Photonic Quantum Manipulator
    // #zh_CN 光子掌控者
    public static final RecipeMap<TST_RecipeMapBackend> PreciseHighEnergyPhotonicQuantumMasterRecipeMap = RecipeMapBuilder
        .of("tst.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 16)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.get(1))
                .setMultipleWidgetsAllowed(false))
        .build();

    // #tr tst.recipe.MiracleTopRecipeMap.name
    // # Miracle Top
    // #zh_CN 奇迹顶点
    public static final RecipeMap<TST_RecipeMapBackend> MiracleTopRecipeMap = RecipeMapBuilder
        .of("tst.recipe.MiracleTopRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(24, 4, 24, 4)
        .dontUseProgressBar()
        .addSpecialTexture(83, 116, 10, 16, UITexture.fullImage(GTPlusPlus.ID, "gui/picture/arrow_white_down"))
        .neiTransferRect(78, 114, 20, 20)
        .frontend(TST_MiracleTopFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1))
                .setMultipleWidgetsAllowed(false))
        .build();

    // #tr tst.recipe.MiracleTopNACRecipeCategory.name
    // # Miracle Top - NAC Recipes
    // #zh_CN 奇迹顶点 - NAC配方
    public static final RecipeCategory MiracleTopNACRecipeCategory = new RecipeCategory(
        "tst.recipe.MiracleTopNACRecipeCategory.name",
        MiracleTopRecipeMap,
        RecipeCategorySetting::getDefault,
        builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1))
            .setMultipleWidgetsAllowed(false));

    // #tr tst.recipe.QuantumInversionRecipeMap.name
    // # Quantum Inversion
    // #zh_CN 量子逆变
    public static final RecipeMap<RecipeMapBackend> QuantumInversionRecipeMap = RecipeMapBuilder
        .of("tst.recipe.QuantumInversionRecipeMap.name")
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(4, 4, 2, 2)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1)))
        .build();

    // #tr tst.recipe.CrystallineInfinitierRecipeMap.name
    // # Crystalline Infinitier
    // #zh_CN 晶胞铸造器
    public static final RecipeMap<TST_RecipeMapBackend> CrystallineInfinitierRecipeMap = RecipeMapBuilder
        .of("tst.recipe.CrystallineInfinitierRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 4, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.CrystallineInfinitier.get(1)))
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("GT5U.nei.tier"))
        .build();

    // #tr tst.recipe.DSP_LauncherRecipeMap.name
    // # Dyson Sphere Module Launch Site
    // #zh_CN 戴森球模块发射
    public static final RecipeMap<RecipeMapBackend> DSP_LauncherRecipeMap = RecipeMapBuilder
        .of("tst.recipe.DSP_LauncherRecipeMap.name")
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(1, 1, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();

    // #tr tst.recipe.DSP_ReceiverRecipeMap.name
    // # Dyson Sphere Ray Receiver
    // #zh_CN 戴森球射线接收
    public static final RecipeMap<RecipeMapBackend> DSP_ReceiverRecipeMap = RecipeMapBuilder
        .of("tst.recipe.DSP_ReceiverRecipeMap.name")
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(0, 1, 0, 0)
        .neiSpecialInfoFormatter(DSP_Receiver_SpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.DSPReceiver.get(1)))
        .build();

    // #tr tst.recipe.ElvenWorkshopRecipeMap.name
    // # Mana Infuser
    // #zh_CN 魔力灌注
    public static final RecipeMap<RecipeMapBackend> ElvenWorkshopRecipeMap = RecipeMapBuilder
        .of("tst.recipe.ElvenWorkshopRecipeMap.name")
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(4, 4, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ElvenWorkshop.get(1)))
        .build();

    // #tr tst.recipe.RuneEngraverRecipeMap.name
    // # Rune Engraver
    // #zh_CN 符文雕刻
    public static final RecipeMap<RecipeMapBackend> RuneEngraverRecipeMap = RecipeMapBuilder
        .of("tst.recipe.RuneEngraverRecipeMap.name")
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(6, 1, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ElvenWorkshop.get(1)))
        .build();

    // #tr tst.recipe.ArtificialStarGeneratingRecipeMap.name
    // # Artificial Star
    // #zh_CN 人造恒星
    public static final RecipeMap<RecipeMapBackend> ArtificialStarGeneratingRecipeMap = RecipeMapBuilder
        .of("tst.recipe.ArtificialStarGeneratingRecipeMap.name")
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(ArtificialStar_SpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ArtificialStar.get(1)))
        .build();

    // #tr tst.recipe.megaUniversalSpaceStationRecipeMap.name
    // # §cThe §bMega §6Universal §9Space §8Station
    // #zh_CN §c寰 §b宇 §6空 §9间 §8站
    public static final RecipeMap<RecipeMapBackend> megaUniversalSpaceStationRecipeMap = RecipeMapBuilder
        .of("tst.recipe.megaUniversalSpaceStationRecipeMap.name")
        .maxIO(16, 4, 16, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1)))
        .build();

    // #tr tst.recipe.OreProcessingVisualRecipeMap.name
    // # Ore Processing Recipes
    // #zh_CN 集成矿物处理
    public static final RecipeMap<RecipeMapBackend> OreProcessingVisualRecipeMap = RecipeMapBuilder
        .of("tst.recipe.OreProcessingVisualRecipeMap.name")
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(1, 9, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.OreProcessingFactory.get(1)))
        .build();

    // #tr tst.recipe.CokingFactoryRecipeMap.name
    // # Coking Factory Recipes
    // #zh_CN 工业炼焦
    public static final RecipeMap<RecipeMapBackend> CokingFactoryRecipeMap = RecipeMapBuilder
        .of("tst.recipe.CokingFactoryRecipeMap.name")
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(2, 2, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();

    // #tr tst.recipe.StellarForgeRecipeMap.name
    // # Stellar Forge
    // #zh_CN 恒星锻炉
    public static final RecipeMap<RecipeMapBackend> StellarForgeRecipeMap = RecipeMapBuilder
        .of("tst.recipe.StellarForgeRecipeMap.name")
        .maxIO(6, 6, 1, 2)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .frontend(TST_StellarForgeFrontend::new)
        .useSpecialSlot()
        .build();

    // #tr tst.recipe.StellarForgeAlloySmelterRecipeMap.name
    // # Stellar Forge: Alloy Smelter
    // #zh_CN 恒星锻炉: 合金冶炼
    public static final RecipeMap<RecipeMapBackend> StellarForgeAlloySmelterRecipeMap = RecipeMapBuilder
        .of("tst.recipe.StellarForgeAlloySmelterRecipeMap.name")
        .maxIO(9, 9, 3, 3)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .frontend(TST_StellarForgeFrontend::new)
        .useSpecialSlot()
        .build();

    // #tr tst.recipe.HyperSpacetimeTransformerRecipeMap.name
    // # Hyper Spacetime Transformer
    // #zh_CN 极限时空转换仪
    public static final RecipeMap<RecipeMapBackend> HyperSpacetimeTransformerRecipeMap = RecipeMapBuilder
        .of("tst.recipe.HyperSpacetimeTransformerRecipeMap.name")
        .maxIO(4, 4, 4, 4)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.HyperSpacetimeTransformer.get(1)))
        .build();

    // #tr tst.recipe.AssemblyLineWithoutResearchRecipeMap.name
    // # Mega Assembly Line
    // #zh_CN 巨型装配线
    public static final RecipeMap<TST_RecipeMapBackend> AssemblyLineWithoutResearchRecipeMap = RecipeMapBuilder
        .of("tst.recipe.AssemblyLineWithoutResearchRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(16, 1, 4, 0)
        .minInputs(1, 0)
        .useSpecialSlot()
        .neiTransferRect(88, 8, 18, 72)
        .neiTransferRect(124, 8, 18, 72)
        .neiTransferRect(142, 26, 18, 18)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndistinctTentacle.get(1)))
        .frontend(TST_AssemblyLineFrontend::new)
        .build();

    // #tr tst.recipe.TombOfTheDragonRecipeMap.name
    // # Mega Assembly Line
    // #zh_CN 巨型装配线
    public static final RecipeMap<TST_RecipeMapBackend> TombOfTheDragonRecipeMap = RecipeMapBuilder
        .of("tst.recipe.TombOfTheDragonRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(1, 1, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .build();

    // #tr tst.recipe.StarKernelGeneratorRecipeMap.name
    // # Star Kernel Generator
    // #zh_CN 星核发生器
    public static final RecipeMap<TST_RecipeMapBackend> StarKernelGeneratorRecipeMap = RecipeMapBuilder
        .of("tst.recipe.StarKernelGeneratorRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 4, 4)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.BallLightning.get(1))
                .setMultipleWidgetsAllowed(false))
        .build();

    // #tr tst.recipe.IndustrialMagicMatrixRecipeMap.name
    // # Industrial Infusion Matrix Recipes
    // #zh_CN §0工业注魔矩阵配方
    public static final RecipeMap<RecipeMapBackend> IndustrialMagicMatrixRecipeMap = RecipeMapBuilder
        .of("tst.recipe.IndustrialMagicMatrixRecipeMap.name")
        .maxIO(25, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndustrialMagicMatrix.get(1)))
        .neiTransferRect(100, 45, 18, 72)
        .useSpecialSlot()
        .frontend(TST_IndustrialMagicMatrixFrontend::new)
        .build();

    // #tr tst.recipe.NeutronActivatorWithEURecipeMap.name
    // # Neutron Oscillator - Electric Neutron Activator
    // #zh_CN 中子振荡器 - 电力版中子活化器
    public static final RecipeMap<TST_RecipeMapBackend> NeutronActivatorWithEURecipeMap = RecipeMapBuilder
        .of("tst.recipe.NeutronActivatorWithEURecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(9, 9, 1, 1)
        .dontUseProgressBar()
        .addSpecialTexture(73, 22, 31, 21, GGUITextures.PICTURE_NEUTRON_ACTIVATOR)
        .build();

    // #tr tst.recipe.MassFabricatorGenesisRecipeMap.name
    // # Mass Fabricator: Genesis
    // #zh_CN 质量发生器: 创世纪
    public static final RecipeMap<TST_RecipeMapBackend> MassFabricatorGenesisRecipeMap = RecipeMapBuilder
        .of("tst.recipe.MassFabricatorGenesisRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(1, 0, 0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MassFabricatorGenesis.get(1)))
        .build();

    private static final Comparator<GTRecipe> BOTTLED_CHAOS_RECIPE_ORDER = Comparator.comparingInt(recipe -> {
        var display = recipe.getMetadata(BottledChaosRecipeMetadata.DISPLAY_BATCH);
        return display == null ? Integer.MAX_VALUE : display.displayOrder();
    });

    // #tr tst.bottledchaos.recipe.name
    // # Bottled Chaos
    // #zh_CN 瓶中混沌
    public static final RecipeMap<TST_RecipeMapBackend> BottledChaosRecipes = RecipeMapBuilder
        .of("tst.bottledchaos.recipe.name", TST_RecipeMapBackend::new)
        .maxIO(72, 18, 18, 18)
        .disableRegisterNEI()
        .frontend(TST_BottledChaosExpandedFrontend::new)
        .neiRecipeComparator(BOTTLED_CHAOS_RECIPE_ORDER)
        .useCustomFilterForNEI()
        .useSpecialSlot()
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> {
            int maxMaps = BottledChaosRecipeProcessor.maxDisplaySourcePools();
            if (GTCMItemList.BottledChaos.hasBeenSet()) builder.setDisplayStack(GTCMItemList.BottledChaos.get(1));
            return builder.setHeight(350 + 10 * maxMaps)
                .setMultipleWidgetsAllowed(true);
        })
        .build();

    // #tr tst.bottledchaos.recipe.category.titanium.name
    // # Titanium
    // #zh_CN 钛
    public static final RecipeCategory BottledChaosTitaniumCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.titanium.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.Rutile.getDust(1)));

    // #tr tst.bottledchaos.recipe.category.tungsten.name
    // # Tungsten
    // #zh_CN 钨
    public static final RecipeCategory BottledChaosTungstenCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.tungsten.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(BotWerkstoffMaterialPool.TungstenTrioxide.get(OrePrefixes.dust, 1)));

    // #tr tst.bottledchaos.recipe.category.indium.name
    // # Indium
    // #zh_CN 铟
    public static final RecipeCategory BottledChaosIndiumCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.indium.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.Indium.getDust(1)));

    // #tr tst.bottledchaos.recipe.category.platinum.name
    // # Platinum Group
    // #zh_CN 铂系
    public static final RecipeCategory BottledChaosPlatinumCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.platinum.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.Platinum.getDust(1)));

    // #tr tst.bottledchaos.recipe.category.lanthanide.name
    // # Lanthanides
    // #zh_CN 镧系
    public static final RecipeCategory BottledChaosLanthanideCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.lanthanide.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.Lanthanum.getDust(1)));

    // #tr tst.bottledchaos.recipe.category.nano_extraction.name
    // # Nano Resin Extraction
    // #zh_CN 纳米萃取
    public static final RecipeCategory BottledChaosNanoExtractionCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.nano_extraction.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(LanthItemList.DIGESTER.copy()));

    // #tr tst.bottledchaos.recipe.category.naquadah.name
    // # Naquadah Processing
    // #zh_CN 硅岩处理
    public static final RecipeCategory BottledChaosNaquadahCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.naquadah.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.Naquadria.getDust(1)));

    // #tr tst.bottledchaos.recipe.category.naquadah_fuel.name
    // # Naquadah Fuel
    // #zh_CN 硅岩燃料
    public static final RecipeCategory BottledChaosNaquadahFuelCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.naquadah_fuel.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 1)));

    // #tr tst.bottledchaos.recipe.category.fusion.name
    // # Fusion
    // #zh_CN 聚变
    public static final RecipeCategory BottledChaosFusionCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.fusion.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(ItemList.FusionComputer_LuV.get(1)));

    // #tr tst.bottledchaos.recipe.category.netherite.name
    // # Netherite
    // #zh_CN 下界合金
    public static final RecipeCategory BottledChaosNetheriteCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.netherite.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1)));

    // #tr tst.bottledchaos.recipe.category.isamill.name
    // # IsaMill Processing
    // #zh_CN 艾萨矿物处理
    public static final RecipeCategory BottledChaosIsaMillCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.isamill.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(GregtechItemList.Controller_IsaMill.get(1)));

    // #tr tst.bottledchaos.recipe.category.prismatic.name
    // # Prismatic Acid
    // #zh_CN 海晶酸
    public static final RecipeCategory BottledChaosPrismaticCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.prismatic.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.PrismaticAcid.getCells(1)));

    // #tr tst.bottledchaos.recipe.category.purified_water.name
    // # Water Purification
    // #zh_CN 净化水
    public static final RecipeCategory BottledChaosPurifiedWaterCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.purified_water.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.Grade8PurifiedWater.getCells(1)));

    // #tr tst.bottledchaos.recipe.category.kevlar.name
    // # Kevlar
    // #zh_CN 凯芙拉
    public static final RecipeCategory BottledChaosKevlarCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.kevlar.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.Kevlar.getPlates(1)));

    // #tr tst.bottledchaos.recipe.category.radox.name
    // # Radox
    // #zh_CN 拉多X
    public static final RecipeCategory BottledChaosRadoxCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.radox.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(Materials.RadoxPolymer.getPlates(1)));

    // #tr tst.bottledchaos.recipe.category.biological.name
    // # Biological Processing
    // #zh_CN 生物线
    public static final RecipeCategory BottledChaosBiologicalCategory = new RecipeCategory(
        "tst.bottledchaos.recipe.category.biological.name",
        BottledChaosRecipes,
        RecipeCategorySetting::getDefault,
        builder -> BottledChaosRecipes.getDefaultRecipeCategory().handlerInfoCreator.apply(builder)
            .setDisplayStack(ItemList.Circuit_Chip_Biocell.get(1)));

    // #tr tst.bottledchaos.recipe.credential.name
    // # Bottled Chaos - Credential Injection
    // #zh_CN 瓶中混沌 - 凭证注入
    public static final RecipeMap<TST_RecipeMapBackend> BottledChaosCredentialRecipes = RecipeMapBuilder
        .of("tst.bottledchaos.recipe.credential.name", TST_RecipeMapBackend::new)
        .maxIO(9, 2, 0, 0)
        .frontend(TST_BottledChaosCredentialFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiRecipeComparator((r1, r2) -> 0)
        .neiHandlerInfo(builder -> {
            if (GTCMItemList.BottledChaos.hasBeenSet()) builder.setDisplayStack(GTCMItemList.BottledChaos.get(1));
            return builder.setHeight(64)
                .setMultipleWidgetsAllowed(true);
        })
        .build();

    // #tr tst.bottledchaos.recipe.structure.name
    // # Bottled Chaos - Machine Structure Packaging
    // #zh_CN 瓶中混沌 - 机器结构封装
    public static final RecipeMap<TST_RecipeMapBackend> BottledChaosStructureRecipes = RecipeMapBuilder
        .of("tst.bottledchaos.recipe.structure.name", TST_RecipeMapBackend::new)
        .maxIO(TST_BottledChaosStructureFrontend.INPUT_SLOTS, 2, 0, 0)
        .disableRegisterNEI()
        .frontend(TST_BottledChaosStructureFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiRecipeComparator(Comparator.comparingInt(recipe -> {
            var display = recipe.getMetadata(BottledChaosStructureDisplay.DISPLAY);
            return display == null ? Integer.MAX_VALUE : display.poolOrder();
        }))
        .neiHandlerInfo(builder -> {
            if (GTCMItemList.BottledChaos.hasBeenSet()) builder.setDisplayStack(GTCMItemList.BottledChaos.get(1));
            return builder.setHeight(TST_BottledChaosStructureFrontend.PAGE_HEIGHT)
                .setMultipleWidgetsAllowed(true);
        })
        .build();

    // #tr tst.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes.name
    // # Eco-Sphere: Arboreal Genesis
    // #zh_CN 生态圈: 原木拟生
    public static final RecipeMap<TST_RecipeMapBackend> TreeGrowthSimulatorWithoutToolFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes.name", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 1, 0)
        .minInputs(1, 1)
        .useSpecialSlot()
        .specialSlotSensitive()
        .frontend(TST_TreeGrowthSimulatorFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.EcoSphereSimulator.get(1)))
        .build();

    // #tr tst.recipe.AquaticZoneSimulatorFakeRecipes.name
    // # Eco-Sphere: Aquatic Simulation
    // #zh_CN 生态圈: 水域模拟
    public static final RecipeMap<TST_RecipeMapBackend> AquaticZoneSimulatorFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.AquaticZoneSimulatorFakeRecipes.name", TST_RecipeMapBackend::new)
        .maxIO(1, 1, 1, 0)
        .minInputs(0, 1)
        .frontend(TST_AquaticZoneSimulatorFronted::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.EcoSphereSimulator.get(1)))
        .build();

    // #tr tst.recipe.ArtificialGreenHouseFakeRecipes.name
    // # Eco-Sphere: Artificial Greenhouse
    // #zh_CN 生态圈: 人工温室
    public static final RecipeMap<TST_RecipeMapBackend> ArtificialGreenHouseFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.ArtificialGreenHouseFakeRecipes.name", TST_RecipeMapBackend::new)
        .maxIO(1, 9, 1, 0)
        .minInputs(1, 1)
        .frontend(TST_ArtificialGreenHouseFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.EcoSphereSimulator.get(1)))
        .build();

    // #tr tst.recipe.DirectedMobClonerFakeRecipes.name
    // # Eco-Sphere: Directed Mob Cloning
    // #zh_CN 生态圈: 定向克隆
    public static final RecipeMap<TST_RecipeMapBackend> DirectedMobClonerFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.DirectedMobClonerFakeRecipes.name", TST_RecipeMapBackend::new)
        .maxIO(1, 2, 1, 1)
        .minInputs(0, 0)
        .frontend(TST_DirectedMobClonerFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.EcoSphereSimulator.get(1)))
        .build();

    // #tr tst.recipe.StrangeMatterAggregatorRecipeMap.name
    // # Strange Matter Aggregation
    // #zh_CN 奇异物质聚合
    public static final RecipeMap<TST_RecipeMapBackend> StrangeMatterAggregatorRecipeMap = RecipeMapBuilder
        .of("tst.recipe.StrangeMatterAggregatorRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(4, 2, 2, 2)
        .progressBar(GTUITextures.PROGRESSBAR_COMPRESS)
        .neiHandlerInfo(b -> b.setDisplayStack(GTCMItemList.StrangeMatterAggregator.get(1)))
        .useSpecialSlot()
        .frontend(TST_StrangeMatterAggregatorFrontend::new)
        .build();

    // #tr tst.recipe.MicroSpaceTimeFabricatorioRecipeMap.name
    // # Micro SpaceTime Fabricatorio Recipes
    // #zh_CN 微型时空发生器
    public static final RecipeMap<TST_RecipeMapBackend> MicroSpaceTimeFabricatorioRecipeMap = RecipeMapBuilder
        .of("tst.recipe.MicroSpaceTimeFabricatorioRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(6, 2, 3, 1)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MicroSpaceTimeFabricatorio.get(1)))
        .build();

    private static final Comparator<ItemStack> COMPARE_ITEMSTACK_BY_DAMAGE = Comparator
        .comparingInt(ItemStack::getItemDamage);

    // #tr tst.recipe.BloodyHellRecipeMap.name
    // # Bloody Hell Altar Recipes
    // #zh_CN 血狱祭坛
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellRecipeMap = RecipeMapBuilder
        .of("tst.recipe.BloodyHellRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(6, 1, 1, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.BloodyHell.get(1)))
        .neiRecipeComparator((r1, r2) -> {
            Optional<ItemStack> circuit1 = Arrays.stream(r1.mInputs)
                .filter(GTUtility::isAnyIntegratedCircuit)
                .findFirst();
            Optional<ItemStack> circuit2 = Arrays.stream(r2.mInputs)
                .filter(GTUtility::isAnyIntegratedCircuit)
                .findFirst();

            return circuit1.map(
                c1 -> circuit2.map(c2 -> COMPARE_ITEMSTACK_BY_DAMAGE.compare(c1, c2))
                    .orElse(-1))
                .orElse(1);
        })
        .build();

    // #tr tst.recipe.BloodyHellAlchemicRecipeMap.name
    // # Bloody Hell Alchemic Chemistry Recipes
    // #zh_CN 血狱炼金
    @Deprecated
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellAlchemicRecipeMap = RecipeMapBuilder
        .of("tst.recipe.BloodyHellAlchemicRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(6, 1, 1, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.BloodyHell.get(1)))
        .build();

    // #tr tst.recipe.BloodyHellBindingRecipeMap.name
    // # Bloody Hell Binding Ritual Recipes
    // #zh_CN 血狱绑定仪式
    @Deprecated
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellBindingRecipeMap = RecipeMapBuilder
        .of("tst.recipe.BloodyHellBindingRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(2, 1, 1, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.BloodyHell.get(1)))
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CIRCUIT : null)
        .build();

    // #tr tst.recipe.MegaStoneBreakerRecipeMap.name
    // # Mega Stone Breaker
    // #zh_CN 巨型碎石机
    public static final RecipeMap<TST_RecipeMapBackend> MegaStoneBreakerRecipeMap = RecipeMapBuilder
        .of("tst.recipe.MegaStoneBreakerRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(3, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaStoneBreaker.get(1)))
        .build();

    // #tr tst.recipe.VisualExtremeCraftRecipeMap.name
    // # Extreme Craft Recipe Transform
    // #zh_CN 梦魇工作台配方转换
    public static final RecipeMap<RecipeMapBackend> VisualExtremeCraftRecipeMap = RecipeMapBuilder
        .of("tst.recipe.VisualExtremeCraftRecipeMap.name")
        .maxIO(16, 1, 0, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.ExtremeCraftCenter.get(1))
                .setMaxRecipesPerPage(1))
        .build();

    // #tr tst.recipe.IndustrialAlchemyTowerRecipeMap.name
    // # Industrial Alchemy Tower
    // #zh_CN 工业炼金塔
    public static final RecipeMap<RecipeMapBackend> IndustrialAlchemyTowerRecipeMap = RecipeMapBuilder
        .of("tst.recipe.IndustrialAlchemyTowerRecipeMap.name")
        .maxIO(2, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndustrialAlchemyTower.get(1)))
        .useSpecialSlot()
        .build();

    // #tr tst.recipe.AdvCircuitAssemblyLineRecipeMap.name
    // # Adv Circuit Assembly Line (Actual recipe pool at runtime)
    // #zh_CN 进阶电路装配线(运行时实际配方池)
    public static final RecipeMap<RecipeMapBackend> AdvCircuitAssemblyLineRecipeMap = Debug_DisplayAdvCircuitAssemblyLineCurrentRecipe
        ? RecipeMapBuilder.of("tst.recipe.AdvCircuitAssemblyLineRecipeMap.name")
            .frontend(TST_RecipeMapFrontend::new)
            .maxIO(6, 1, 1, 0)
            .minInputs(1, 1)
            .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.AdvCircuitAssemblyLine.get(1)))
            .progressBar(GTUITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER)
            .build()
        : RecipeMapBuilder.of("tst.recipe.AdvCircuitAssemblyLineRecipeMap.name")
            .frontend(TST_RecipeMapFrontend::new)
            .maxIO(6, 1, 1, 0)
            .minInputs(1, 1)
            .disableRegisterNEI()
            .progressBar(GTUITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER)
            .build();

    // #tr tst.recipe.RapidHeatExchangeRecipeMap.name
    // # Rapid Heat Exchange
    // #zh_CN 快速热交换
    public static final RecipeMap<TST_RecipeMapBackend> RapidHeatExchangeRecipeMap = RecipeMapBuilder
        .of("tst.recipe.RapidHeatExchangeRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(0, 0, 2, 2)
        .dontUseProgressBar()
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.HyperThermalConvector.get(1)))
        .addSpecialTexture(47, 13, 78, 59, GGUITextures.PICTURE_EXTREME_HEAT_EXCHANGER)
        .frontend(TST_RapidHeatExchangeFrontend::new)
        .build();

    // #tr tst.recipe.RapidCoolingDownRecipeMap.name
    // # Rapid Cooling Down
    // #zh_CN 快速冷却
    public static final RecipeMap<TST_RecipeMapBackend> RapidCoolingDownRecipeMap = RecipeMapBuilder
        .of("tst.recipe.RapidCoolingDownRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(0, 0, 1, 1)
        .dontUseProgressBar()
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.HyperThermalConvector.get(1)))
        .addSpecialTexture(47, 13, 78, 59, UITextures.HESTTD_NEIPic_MUI1)
        .frontend(TST_RapidCoolingDownFrontend::new)
        .build();

    // #tr tst.recipe.DeployedNanoCoreRecipeMap.name
    // # Nano Core
    // #zh_CN 纳米核心
    public static final RecipeMap<TST_RecipeMapBackend> DeployedNanoCoreRecipeMap = RecipeMapBuilder
        .of("tst.recipe.DeployedNanoCoreRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(6, 2, 3, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.DeployedNanoCore.get(1)))
        .progressBar(GTUITextures.PROGRESSBAR_ASSEMBLE)
        .build();

    // #tr tst.recipe.NetherInterfaceVisualRecipeMap.name
    // # Nether Interface
    // #zh_CN 地狱接口
    public static final RecipeMap<TST_RecipeMapBackend> NetherInterfaceVisualRecipeMap = RecipeMapBuilder
        .of("tst.recipe.NetherInterfaceVisualRecipeMap.name", TST_RecipeMapBackend::new)
        .frontend(TST_RecipeMapFrontend::new)
        .maxIO(0, 6, 1, 2)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.NetherInterface.get(1)))
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();

    // #tr tst.recipe.SuperWaterPurifierVisualRecipeMap.name
    // # Super Water Purifier
    // #zh_CN 超净水生成器
    public static final RecipeMap<TST_RecipeMapBackend> SuperWaterPurifierVisualRecipeMap = RecipeMapBuilder
        .of("tst.recipe.SuperWaterPurifierVisualRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(0, 0, 1, 12)
        .frontend(TST_SuperWaterPurifierFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.SuperWaterPurifier.get(1)))
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();

    // #tr tst.recipe.SkypiercerTowerRecipeMap.name
    // # Skypiercer Tower
    // #zh_CN 穿云尖塔
    public static final RecipeMap<TST_RecipeMapBackend> SkypiercerTowerRecipeMap = RecipeMapBuilder
        .of("tst.recipe.SkypiercerTowerRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(2, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.SkypiercerTower.get(1)))
        .build();

    // #tr tst.recipe.MegaSolarPanelFactoryRecipeMap.name
    // # Mega Solar Panel Factory
    // #zh_CN 巨型太阳能板工厂
    public static final RecipeMap<TST_RecipeMapBackend> MegaSolarPanelFactoryRecipeMap = RecipeMapBuilder
        .of("tst.recipe.MegaSolarPanelFactoryRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(24, 4, 8, 0)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaSolarPanelFactory.get(1)))
        .build();
}
