package com.Nxer.TwistSpaceTechnology.common.recipeMap;

import static com.Nxer.TwistSpaceTechnology.config.Config.Debug_DisplayAdvCircuitAssemblyLineCurrentRecipe;
import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.MachineTexture.UITextures;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters.ArtificialStar_SpecialValueFormatter;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters.DSP_Receiver_SpecialValueFormatter;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_AquaticZoneSimulatorFronted;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_ArtificialGreenHouseFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_DirectedMobClonerFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_GeneralFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_IndustrialMagicMatrixFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_MiracleTopFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_RapidCoolingDownFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_RapidHeatExchangeFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_StellarForgeFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_StrangeMatterAggregatorFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_SuperWaterPurifierFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_TreeGrowthSimulatorFrontend;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

import goodgenerator.client.GUI.GGUITextures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeCategorySetting;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.maps.AssemblyLineFrontend;
import gregtech.api.util.GTUtility;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;

public class GTCMRecipe {

    // #tr tst.common.recipe.IntensifyChemicalDistorterRecipeMap.name
    // # Intense Chemical Distorter
    // #zh_CN 深度化学扭曲
    public static final RecipeMap<TST_RecipeMapBackend> IntensifyChemicalDistorterRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.IntensifyChemicalDistorterRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 16)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.IntensifyChemicalDistorter.get(1))
                .setMultipleWidgetsAllowed(false))
        .build();

    // #tr tst.common.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipeMap.name
    // # Precise High-Energy Photonic Quantum Manipulator
    // #zh_CN 光子掌控者
    public static final RecipeMap<TST_RecipeMapBackend> PreciseHighEnergyPhotonicQuantumMasterRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 16)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.get(1))
                .setMultipleWidgetsAllowed(false))
        .build();

    // #tr tst.common.recipe.MiracleTopRecipeMap.name
    // # Miracle Top
    // #zh_CN 奇迹顶点
    public static final RecipeMap<TST_RecipeMapBackend> MiracleTopRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.MiracleTopRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(24, 4, 24, 4)
        .dontUseProgressBar()
        .addSpecialTexture(83, 116, 10, 16, UITexture.fullImage(GTPlusPlus.ID, "gui/picture/arrow_white_down"))
        .neiTransferRect(78, 114, 20, 20)
        .frontend(TST_MiracleTopFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1))
                .setMultipleWidgetsAllowed(false))
        .build();

    // #tr tst.common.recipe.MiracleTopNACRecipeCategory.name
    // # Miracle Top - NAC Recipes
    // #zh_CN 奇迹顶点 - NAC配方
    public static final RecipeCategory MiracleTopNACRecipeCategory = new RecipeCategory(
        "tst.common.recipe.MiracleTopNACRecipeCategory.name",
        MiracleTopRecipeMap,
        RecipeCategorySetting::getDefault,
        builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1))
            .setMultipleWidgetsAllowed(false));

    // #tr tst.common.recipe.QuantumInversionRecipeMap.name
    // # Quantum Inversion
    // #zh_CN 量子逆变
    public static final RecipeMap<RecipeMapBackend> QuantumInversionRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.QuantumInversionRecipeMap.name")
        .maxIO(4, 4, 2, 2)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1)))
        .build();

    // #tr tst.common.recipe.CrystallineInfinitierRecipeMap.name
    // # Crystalline Infinitier
    // #zh_CN 晶胞铸造器
    public static final RecipeMap<TST_RecipeMapBackend> CrystallineInfinitierRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.CrystallineInfinitierRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 4, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.CrystallineInfinitier.get(1)))
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("GT5U.nei.tier"))
        .build();

    // #tr tst.dyson.recipe.DSP_LauncherRecipeMap.name
    // # Dyson Sphere Module Launch Site
    // #zh_CN 戴森球模块发射
    public static final RecipeMap<RecipeMapBackend> DSP_LauncherRecipeMap = RecipeMapBuilder
        .of("tst.dyson.recipe.DSP_LauncherRecipeMap.name")
        .maxIO(1, 1, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();

    // #tr tst.dyson.recipe.DSP_ReceiverRecipeMap.name
    // # Dyson Sphere Ray Receiver
    // #zh_CN 戴森球射线接收
    public static final RecipeMap<RecipeMapBackend> DSP_ReceiverRecipeMap = RecipeMapBuilder
        .of("tst.dyson.recipe.DSP_ReceiverRecipeMap.name")
        .maxIO(0, 1, 0, 0)
        .neiSpecialInfoFormatter(DSP_Receiver_SpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.DSPReceiver.get(1)))
        .build();

    // #tr tst.common.recipe.ElvenWorkshopRecipeMap.name
    // # Mana Infuser
    // #zh_CN 魔力灌注
    public static final RecipeMap<RecipeMapBackend> ElvenWorkshopRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.ElvenWorkshopRecipeMap.name")
        .maxIO(4, 4, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ElvenWorkshop.get(1)))
        .build();

    // #tr tst.common.recipe.RuneEngraverRecipeMap.name
    // # Rune Engraver
    // #zh_CN 符文雕刻
    public static final RecipeMap<RecipeMapBackend> RuneEngraverRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.RuneEngraverRecipeMap.name")
        .maxIO(6, 1, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ElvenWorkshop.get(1)))
        .build();

    // #tr tst.dyson.recipe.ArtificialStarGeneratingRecipeMap.name
    // # Artificial Star
    // #zh_CN 人造恒星
    public static final RecipeMap<RecipeMapBackend> ArtificialStarGeneratingRecipeMap = RecipeMapBuilder
        .of("tst.dyson.recipe.ArtificialStarGeneratingRecipeMap.name")
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(ArtificialStar_SpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ArtificialStar.get(1)))
        .build();

    // #tr tst.common.recipe.megaUniversalSpaceStationRecipeMap.name
    // # §cThe §bMega §6Universal §9Space §8Station
    // #zh_CN §c寰 §b宇 §6空 §9间 §8站
    public static final RecipeMap<RecipeMapBackend> megaUniversalSpaceStationRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.megaUniversalSpaceStationRecipeMap.name")
        .maxIO(16, 4, 16, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1)))
        .build();

    // #tr tst.common.recipe.OreProcessingVisualRecipeMap.name
    // # Ore Processing Recipes
    // #zh_CN 集成矿物处理
    public static final RecipeMap<RecipeMapBackend> OreProcessingVisualRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.OreProcessingVisualRecipeMap.name")
        .maxIO(1, 9, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.OreProcessingFactory.get(1)))
        .build();

    // #tr tst.common.recipe.CokingFactoryRecipeMap.name
    // # Coking Factory Recipes
    // #zh_CN 工业炼焦
    public static final RecipeMap<RecipeMapBackend> CokingFactoryRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.CokingFactoryRecipeMap.name")
        .maxIO(2, 2, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();

    // #tr tst.common.recipe.StellarForgeRecipeMap.name
    // # Stellar Forge
    // #zh_CN 恒星锻炉
    public static final RecipeMap<RecipeMapBackend> StellarForgeRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.StellarForgeRecipeMap.name")
        .maxIO(6, 6, 1, 2)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .frontend(TST_StellarForgeFrontend::new)
        .useSpecialSlot()
        .build();

    // #tr tst.common.recipe.StellarForgeAlloySmelterRecipeMap.name
    // # Stellar Forge: Alloy Smelter
    // #zh_CN 恒星锻炉: 合金冶炼
    public static final RecipeMap<RecipeMapBackend> StellarForgeAlloySmelterRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.StellarForgeAlloySmelterRecipeMap.name")
        .maxIO(9, 9, 3, 3)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .frontend(TST_StellarForgeFrontend::new)
        .useSpecialSlot()
        .build();

    // #tr tst.common.recipe.HyperSpacetimeTransformerRecipeMap.name
    // # Hyper Spacetime Transformer
    // #zh_CN 极限时空转换仪
    public static final RecipeMap<RecipeMapBackend> HyperSpacetimeTransformerRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.HyperSpacetimeTransformerRecipeMap.name")
        .maxIO(4, 4, 4, 4)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.HyperSpacetimeTransformer.get(1)))
        .build();

    // #tr tst.common.recipe.AssemblyLineWithoutResearchRecipeMap.name
    // # Mega Assembly Line
    // #zh_CN 巨型装配线
    public static final RecipeMap<TST_RecipeMapBackend> AssemblyLineWithoutResearchRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.AssemblyLineWithoutResearchRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(16, 1, 4, 0)
        .minInputs(1, 0)
        .useSpecialSlot()
        .neiTransferRect(88, 8, 18, 72)
        .neiTransferRect(124, 8, 18, 72)
        .neiTransferRect(142, 26, 18, 18)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndistinctTentacle.get(1)))
        .frontend(AssemblyLineFrontend::new)
        .build();

    // #tr tst.common.recipe.TombOfTheDragonRecipeMap.name
    // # Mega Assembly Line
    // #zh_CN 巨型装配线
    public static final RecipeMap<TST_RecipeMapBackend> TombOfTheDragonRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.TombOfTheDragonRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(1, 1, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .build();

    // #tr tst.common.recipe.StarKernelGeneratorRecipeMap.name
    // # Star Kernel Generator
    // #zh_CN 星核发生器
    public static final RecipeMap<TST_RecipeMapBackend> StarKernelGeneratorRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.StarKernelGeneratorRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 4, 4)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.BallLightning.get(1))
                .setMultipleWidgetsAllowed(false))
        .build();

    // #tr tst.common.recipe.IndustrialMagicMatrixRecipeMap.name
    // # Industrial Infusion Matrix Recipes
    // #zh_CN §0工业注魔矩阵配方
    public static final RecipeMap<RecipeMapBackend> IndustrialMagicMatrixRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.IndustrialMagicMatrixRecipeMap.name")
        .maxIO(25, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndustrialMagicMatrix.get(1)))
        .neiTransferRect(100, 45, 18, 72)
        .useSpecialSlot()
        .frontend(TST_IndustrialMagicMatrixFrontend::new)
        .build();

    // #tr tst.common.recipe.NeutronActivatorWithEURecipeMap.name
    // # Neutron Oscillator - Electric Neutron Activator
    // #zh_CN 中子振荡器 - 电力版中子活化器
    public static final RecipeMap<TST_RecipeMapBackend> NeutronActivatorWithEURecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.NeutronActivatorWithEURecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(9, 9, 1, 1)
        .dontUseProgressBar()
        .addSpecialTexture(73, 22, 31, 21, GGUITextures.PICTURE_NEUTRON_ACTIVATOR)
        .build();

    // #tr tst.common.recipe.MassFabricatorGenesisRecipeMap.name
    // # Mass Fabricator: Genesis
    // #zh_CN 质量发生器: 创世纪
    public static final RecipeMap<TST_RecipeMapBackend> MassFabricatorGenesisRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.MassFabricatorGenesisRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(1, 0, 0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MassFabricatorGenesis.get(1)))
        .build();

    // #tr tst.ecosphere.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes.name
    // # Eco-Sphere: Arboreal Genesis
    // #zh_CN 生态圈: 原木拟生
    public static final RecipeMap<TST_RecipeMapBackend> TreeGrowthSimulatorWithoutToolFakeRecipes = RecipeMapBuilder
        .of("tst.ecosphere.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes.name", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 1, 0)
        .minInputs(1, 1)
        .useSpecialSlot()
        .specialSlotSensitive()
        .frontend(TST_TreeGrowthSimulatorFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.EcoSphereSimulator.get(1)))
        .build();

    // #tr tst.ecosphere.recipe.AquaticZoneSimulatorFakeRecipes.name
    // # Eco-Sphere: Aquatic Simulation
    // #zh_CN 生态圈: 水域模拟
    public static final RecipeMap<TST_RecipeMapBackend> AquaticZoneSimulatorFakeRecipes = RecipeMapBuilder
        .of("tst.ecosphere.recipe.AquaticZoneSimulatorFakeRecipes.name", TST_RecipeMapBackend::new)
        .maxIO(1, 1, 1, 0)
        .minInputs(0, 1)
        .frontend(TST_AquaticZoneSimulatorFronted::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.EcoSphereSimulator.get(1)))
        .build();

    // #tr tst.ecosphere.recipe.ArtificialGreenHouseFakeRecipes.name
    // # Eco-Sphere: Artificial Greenhouse
    // #zh_CN 生态圈: 人工温室
    public static final RecipeMap<TST_RecipeMapBackend> ArtificialGreenHouseFakeRecipes = RecipeMapBuilder
        .of("tst.ecosphere.recipe.ArtificialGreenHouseFakeRecipes.name", TST_RecipeMapBackend::new)
        .maxIO(1, 9, 1, 0)
        .minInputs(1, 1)
        .frontend(TST_ArtificialGreenHouseFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.EcoSphereSimulator.get(1)))
        .build();

    // #tr tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.name
    // # Eco-Sphere: Directed Mob Cloning
    // #zh_CN 生态圈: 定向克隆
    public static final RecipeMap<TST_RecipeMapBackend> DirectedMobClonerFakeRecipes = RecipeMapBuilder
        .of("tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.name", TST_RecipeMapBackend::new)
        .maxIO(1, 2, 1, 1)
        .minInputs(0, 0)
        .frontend(TST_DirectedMobClonerFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.EcoSphereSimulator.get(1)))
        .build();

    // #tr tst.dyson.recipe.StrangeMatterAggregatorRecipeMap.name
    // # Strange Matter Aggregation
    // #zh_CN 奇异物质聚合
    public static final RecipeMap<TST_RecipeMapBackend> StrangeMatterAggregatorRecipeMap = RecipeMapBuilder
        .of("tst.dyson.recipe.StrangeMatterAggregatorRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(4, 2, 2, 2)
        .progressBar(GTUITextures.PROGRESSBAR_COMPRESS)
        .neiHandlerInfo(b -> b.setDisplayStack(GTCMItemList.StrangeMatterAggregator.get(1)))
        .useSpecialSlot()
        .frontend(TST_StrangeMatterAggregatorFrontend::new)
        .build();

    // #tr tst.common.recipe.MicroSpaceTimeFabricatorioRecipeMap.name
    // # Micro SpaceTime Fabricatorio Recipes
    // #zh_CN 微型时空发生器
    public static final RecipeMap<TST_RecipeMapBackend> MicroSpaceTimeFabricatorioRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.MicroSpaceTimeFabricatorioRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(6, 2, 3, 1)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MicroSpaceTimeFabricatorio.get(1)))
        .build();

    private static final Comparator<ItemStack> COMPARE_ITEMSTACK_BY_DAMAGE = Comparator
        .comparingInt(ItemStack::getItemDamage);

    // #tr tst.common.recipe.BloodyHellRecipeMap.name
    // # Bloody Hell Altar Recipes
    // #zh_CN 血狱祭坛
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.BloodyHellRecipeMap.name", TST_RecipeMapBackend::new)
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

    // #tr tst.common.recipe.BloodyHellAlchemicRecipeMap.name
    // # Bloody Hell Alchemic Chemistry Recipes
    // #zh_CN 血狱炼金
    @Deprecated
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellAlchemicRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.BloodyHellAlchemicRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(6, 1, 1, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.BloodyHell.get(1)))
        .build();

    // #tr tst.common.recipe.BloodyHellBindingRecipeMap.name
    // # Bloody Hell Binding Ritual Recipes
    // #zh_CN 血狱绑定仪式
    @Deprecated
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellBindingRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.BloodyHellBindingRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(2, 1, 1, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.BloodyHell.get(1)))
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CIRCUIT : null)
        .build();

    // #tr tst.common.recipe.MegaStoneBreakerRecipeMap.name
    // # Mega Stone Breaker
    // #zh_CN 巨型碎石机
    public static final RecipeMap<TST_RecipeMapBackend> MegaStoneBreakerRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.MegaStoneBreakerRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(3, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaStoneBreaker.get(1)))
        .build();

    // #tr tst.common.recipe.VisualExtremeCraftRecipeMap.name
    // # Extreme Craft Recipe Transform
    // #zh_CN 梦魇工作台配方转换
    public static final RecipeMap<RecipeMapBackend> VisualExtremeCraftRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.VisualExtremeCraftRecipeMap.name")
        .maxIO(16, 1, 0, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.ExtremeCraftCenter.get(1))
                .setMaxRecipesPerPage(1))
        .build();

    // #tr tst.common.recipe.IndustrialAlchemyTowerRecipeMap.name
    // # Industrial Alchemy Tower
    // #zh_CN 工业炼金塔
    public static final RecipeMap<RecipeMapBackend> IndustrialAlchemyTowerRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.IndustrialAlchemyTowerRecipeMap.name")
        .maxIO(2, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndustrialAlchemyTower.get(1)))
        .useSpecialSlot()
        .build();

    // #tr tst.common.recipe.AdvCircuitAssemblyLineRecipeMap.name
    // # Adv Circuit Assembly Line (Actual recipe pool at runtime)
    // #zh_CN 进阶电路装配线(运行时实际配方池)
    public static final RecipeMap<RecipeMapBackend> AdvCircuitAssemblyLineRecipeMap = Debug_DisplayAdvCircuitAssemblyLineCurrentRecipe
        ? RecipeMapBuilder.of("tst.common.recipe.AdvCircuitAssemblyLineRecipeMap.name")
            .maxIO(6, 1, 1, 0)
            .minInputs(1, 1)
            .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.AdvCircuitAssemblyLine.get(1)))
            .progressBar(GTUITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER)
            .build()
        : RecipeMapBuilder.of("tst.common.recipe.AdvCircuitAssemblyLineRecipeMap.name")
            .maxIO(6, 1, 1, 0)
            .minInputs(1, 1)
            .disableRegisterNEI()
            .progressBar(GTUITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER)
            .build();

    // #tr tst.common.recipe.RapidHeatExchangeRecipeMap.name
    // # Rapid Heat Exchange
    // #zh_CN 快速热交换
    public static final RecipeMap<TST_RecipeMapBackend> RapidHeatExchangeRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.RapidHeatExchangeRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(0, 0, 2, 2)
        .dontUseProgressBar()
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.HyperThermalConvector.get(1)))
        .addSpecialTexture(47, 13, 78, 59, GGUITextures.PICTURE_EXTREME_HEAT_EXCHANGER)
        .frontend(TST_RapidHeatExchangeFrontend::new)
        .build();

    // #tr tst.common.recipe.RapidCoolingDownRecipeMap.name
    // # Rapid Cooling Down
    // #zh_CN 快速冷却
    public static final RecipeMap<TST_RecipeMapBackend> RapidCoolingDownRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.RapidCoolingDownRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(0, 0, 1, 1)
        .dontUseProgressBar()
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.HyperThermalConvector.get(1)))
        .addSpecialTexture(47, 13, 78, 59, UITextures.HESTTD_NEIPic_MUI1)
        .frontend(TST_RapidCoolingDownFrontend::new)
        .build();

    // #tr tst.common.recipe.DeployedNanoCoreRecipeMap.name
    // # Nano Core
    // #zh_CN 纳米核心
    public static final RecipeMap<TST_RecipeMapBackend> DeployedNanoCoreRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.DeployedNanoCoreRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(6, 2, 3, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.DeployedNanoCore.get(1)))
        .progressBar(GTUITextures.PROGRESSBAR_ASSEMBLE)
        .build();

    // #tr tst.common.recipe.NetherInterfaceVisualRecipeMap.name
    // # Nether Interface
    // #zh_CN 地狱接口
    public static final RecipeMap<TST_RecipeMapBackend> NetherInterfaceVisualRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.NetherInterfaceVisualRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(0, 6, 1, 2)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.NetherInterface.get(1)))
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();

    // #tr tst.common.recipe.SuperWaterPurifierVisualRecipeMap.name
    // # Super Water Purifier
    // #zh_CN 超净水生成器
    public static final RecipeMap<TST_RecipeMapBackend> SuperWaterPurifierVisualRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.SuperWaterPurifierVisualRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(0, 0, 1, 12)
        .frontend(TST_SuperWaterPurifierFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.SuperWaterPurifier.get(1)))
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();

    // #tr tst.common.recipe.SkypiercerTowerRecipeMap.name
    // # Skypiercer Tower
    // #zh_CN 穿云尖塔
    public static final RecipeMap<TST_RecipeMapBackend> SkypiercerTowerRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.SkypiercerTowerRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(2, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.SkypiercerTower.get(1)))
        .build();

    // #tr tst.common.recipe.MegaSolarPanelFactoryRecipeMap.name
    // # Mega Solar Panel Factory
    // #zh_CN 巨型太阳能板工厂
    public static final RecipeMap<TST_RecipeMapBackend> MegaSolarPanelFactoryRecipeMap = RecipeMapBuilder
        .of("tst.common.recipe.MegaSolarPanelFactoryRecipeMap.name", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 0)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaSolarPanelFactory.get(1)))
        .build();
}
