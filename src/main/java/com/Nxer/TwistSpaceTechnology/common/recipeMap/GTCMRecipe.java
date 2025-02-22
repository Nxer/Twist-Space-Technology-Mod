package com.Nxer.TwistSpaceTechnology.common.recipeMap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters.ArtificialStar_SpecialValueFormatter;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters.DSP_Receiver_SpecialValueFormatter;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_AquaticZoneSimulatorFronted;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_GeneralFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_IndustrialMagicMatrixFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_StellarForgeFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_StrangeMatterAggregatorFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_TreeGrowthSimulatorFrontend;

import goodgenerator.client.GUI.GGUITextures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.maps.AssemblyLineFrontend;
import gregtech.api.util.GTUtility;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;

public class GTCMRecipe {

    public static final RecipeMap<TST_RecipeMapBackend> IntensifyChemicalDistorterRecipes = RecipeMapBuilder
        // At the same time , the localization key of the NEI Name
        // of this page.
        .of("gtcm.recipe.IntensifyChemicalDistorterRecipes", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 16)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.IntensifyChemicalDistorter.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> PreciseHighEnergyPhotonicQuantumMasterRecipes = RecipeMapBuilder
        .of("gtcm.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipes", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 16)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> MiracleTopRecipes = RecipeMapBuilder
        .of("gtcm.recipe.MiracleTopRecipes", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 4)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> QuantumInversionRecipes = RecipeMapBuilder
        .of("gtcm.recipe.QuantumInversionRecipes")
        .maxIO(4, 4, 2, 2)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> CrystallineInfinitierRecipes = RecipeMapBuilder
        .of("gtcm.recipe.CrystallineInfinitierRecipes", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 4, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.CrystallineInfinitier.get(1)))
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("GT5U.nei.tier"))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> DSP_LauncherRecipes = RecipeMapBuilder
        .of("gtcm.recipe.DSPLauncherRecipes")
        .maxIO(1, 1, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> DSP_ReceiverRecipes = RecipeMapBuilder
        .of("gtcm.recipe.DSPReceiverRecipes")
        .maxIO(0, 1, 0, 0)
        .neiSpecialInfoFormatter(DSP_Receiver_SpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.DSPReceiver.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> ElvenWorkshopRecipes = RecipeMapBuilder
        .of("gtcm.recipe.ElvenWorkshopRecipes")
        .maxIO(4, 4, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ElvenWorkshop.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> RuneEngraverRecipes = RecipeMapBuilder
        .of("gtcm.recipe.RuneEngraverRecipes")
        .maxIO(6, 1, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ElvenWorkshop.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> ArtificialStarGeneratingRecipes = RecipeMapBuilder
        .of("gtcm.recipe.ArtificialStarGeneratingRecipes")
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(ArtificialStar_SpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ArtificialStar.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> megaUniversalSpaceStationRecipePool = RecipeMapBuilder
        .of("gtcm.recipe.megaUniversalSpaceStationRecipePool")
        .maxIO(16, 4, 16, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> OreProcessingRecipes = RecipeMapBuilder
        .of("tst.recipe.OreProcessingRecipes")
        .maxIO(1, 9, 1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.OreProcessingFactory.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> CokingFactoryRecipes = RecipeMapBuilder
        .of("tst.recipe.CokingFactoryRecipes")
        .maxIO(2, 2, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .build();

    // #tr tst.recipe.StellarForgeRecipes
    // # Stellar Forge
    // #zh_CN 恒星锻炉
    public static final RecipeMap<RecipeMapBackend> StellarForgeRecipes = RecipeMapBuilder
        .of("tst.recipe.StellarForgeRecipes")
        .maxIO(6, 6, 1, 2)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .frontend(TST_StellarForgeFrontend::new)
        .useSpecialSlot()
        .disableOptimize()
        .build();

    // #tr tst.recipe.StellarForgeAlloySmelterRecipes
    // # Stellar Forge : Alloy Smelter
    // #zh_CN 恒星锻炉:合金冶炼
    public static final RecipeMap<RecipeMapBackend> StellarForgeAlloySmelterRecipes = RecipeMapBuilder
        .of("tst.recipe.StellarForgeAlloySmelterRecipes")
        .maxIO(9, 9, 3, 3)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .frontend(TST_StellarForgeFrontend::new)
        .useSpecialSlot()
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> HyperSpacetimeTransformerRecipe = RecipeMapBuilder
        .of("tst.recipe.HyperSpacetimeTransformerRecipe")
        .maxIO(4, 4, 4, 4)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.HyperSpacetimeTransformer.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> AssemblyLineWithoutResearchRecipe = RecipeMapBuilder
        .of("tst.recipe.AssemblyLineWithoutResearchRecipe", TST_RecipeMapBackend::new)
        .maxIO(16, 1, 4, 0)
        .minInputs(1, 0)
        .useSpecialSlot()
        .disableOptimize()
        .neiTransferRect(88, 8, 18, 72)
        .neiTransferRect(124, 8, 18, 72)
        .neiTransferRect(142, 26, 18, 18)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndistinctTentacle.get(1)))
        .frontend(AssemblyLineFrontend::new)
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> TombOfTheDragonRecipe = RecipeMapBuilder
        .of("tst.recipe.TombOfTheDragonRecipe", TST_RecipeMapBackend::new)
        .maxIO(1, 1, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> BallLightningRecipes = RecipeMapBuilder
        .of("tst.recipe.BallLightningRecipes", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 4, 4)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.BallLightning.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();
    public static final RecipeMap<RecipeMapBackend> IndustrialMagicMatrixRecipe = RecipeMapBuilder
        .of("tst.recipe.IndustrialMagicMatrixRecipe")
        .maxIO(25, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndustrialMagicMatrix.get(1)))
        .neiTransferRect(100, 45, 18, 72)
        .useSpecialSlot()
        .frontend(TST_IndustrialMagicMatrixFrontend::new)
        .disableOptimize()
        .build();

    // #tr tst.recipe.neutronActivatorRecipesWithEU
    // # Neutron Oscillator - Electric Neutron Activator
    // #zh_CN 中子振荡器 - 电力版中子活化器
    public static final RecipeMap<TST_RecipeMapBackend> NeutronActivatorRecipesWithEU = RecipeMapBuilder
        .of("tst.recipe.neutronActivatorRecipesWithEU", TST_RecipeMapBackend::new)
        .maxIO(9, 9, 1, 1)
        .dontUseProgressBar()
        .addSpecialTexture(73, 22, 31, 21, GGUITextures.PICTURE_NEUTRON_ACTIVATOR)
        .disableOptimize()
        .build();

    // #tr tst.recipe.MassFabricatorGenesis
    // # Mass Fabricator : Genesis
    // #zh_CN 质量发生器 : 创世纪
    public static final RecipeMap<TST_RecipeMapBackend> MassFabricatorGenesis = RecipeMapBuilder
        .of("tst.recipe.MassFabricatorGenesis", TST_RecipeMapBackend::new)
        .maxIO(1, 0, 0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MassFabricatorGenesis.get(1)))
        .disableOptimize()
        .build();

    // #tr tst.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes
    // # Eco-Sphere : Tree Growth Simulator
    // #zh_CN 生态圈 : 原木拟生
    public static final RecipeMap<TST_RecipeMapBackend> TreeGrowthSimulatorWithoutToolFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 1, 0)
        .minInputs(1, 1)
        .useSpecialSlot()
        .specialSlotSensitive()
        .frontend(TST_TreeGrowthSimulatorFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaTreeFarm.get(1)))
        .disableOptimize()
        .build();

    // #tr tst.recipe.AquaticZoneSimulatorFakeRecipes
    // # Eco-Sphere : Aquatic Zone Simulator
    // #zh_CN 生态圈 : 水域模拟
    public static final RecipeMap<TST_RecipeMapBackend> AquaticZoneSimulatorFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.AquaticZoneSimulatorFakeRecipes", TST_RecipeMapBackend::new)
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 1)
        .frontend(TST_AquaticZoneSimulatorFronted::new)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaTreeFarm.get(1)))
        .disableOptimize()
        .build();

    // #tr tst.recipe.ArtificialGreenHouseFakeRecipes
    // # Eco-Sphere : Artificial Green House
    // #zh_CN 生态圈 : 人工温室
    public static final RecipeMap<TST_RecipeMapBackend> ArtificialGreenHouseFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.ArtificialGreenHouseFakeRecipes", TST_RecipeMapBackend::new)
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaTreeFarm.get(1)))
        .disableOptimize()
        .build();

    // #tr tst.recipe.DirectedMobClonerFakeRecipes
    // # Eco-Sphere : Directed Mob Cloner
    // #zh_CN 生态圈 : 定向克隆
    public static final RecipeMap<TST_RecipeMapBackend> DirectedMobClonerFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.DirectedMobClonerFakeRecipes", TST_RecipeMapBackend::new)
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaTreeFarm.get(1)))
        .disableOptimize()
        .build();

    // #tr tst.recipe.StrangeMatterAggregatorRecipes
    // # Strange Matter Aggregation
    // #zh_CN 奇异物质聚合
    public static final RecipeMap<TST_RecipeMapBackend> StrangeMatterAggregatorRecipes = RecipeMapBuilder
        .of("tst.recipe.StrangeMatterAggregatorRecipes", TST_RecipeMapBackend::new)
        .maxIO(4, 2, 2, 2)
        .progressBar(GTUITextures.PROGRESSBAR_COMPRESS)
        .neiHandlerInfo(b -> b.setDisplayStack(GTCMItemList.StrangeMatterAggregator.get(1)))
        .useSpecialSlot()
        .frontend(TST_StrangeMatterAggregatorFrontend::new)
        .disableOptimize()
        .build();

    // #tr tst.recipe.MicroSpaceTimeFabricatorioRecipes
    // # Micro SpaceTime Fabricatorio Recipes
    // #zh_CN 微型时空发生器
    public static final RecipeMap<TST_RecipeMapBackend> MicroSpaceTimeFabricatorioRecipes = RecipeMapBuilder
        .of("tst.recipe.MicroSpaceTimeFabricatorioRecipes", TST_RecipeMapBackend::new)
        .maxIO(6, 2, 3, 1)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MicroSpaceTimeFabricatorio.get(1)))
        .disableOptimize()
        .build();

    private static final Comparator<ItemStack> COMPARE_ITEMSTACK_BY_DAMAGE = Comparator
        .comparingInt(ItemStack::getItemDamage);

    // #tr tst.recipe.BloodyHellRecipes
    // # Bloody Hell Altar Recipes
    // #zh_CN 血狱祭坛
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellRecipes = RecipeMapBuilder
        .of("tst.recipe.BloodyHellRecipes", TST_RecipeMapBackend::new)
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
        .disableOptimize()
        .build();

    // #tr tst.recipe.BloodyHellAlchemicRecipes
    // # Bloody Hell Alchemic Chemistry Recipes
    // #zh_CN 血狱炼金
    @Deprecated
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellRecipe_Alchemic = RecipeMapBuilder
        .of("tst.recipe.BloodyHellAlchemicRecipes", TST_RecipeMapBackend::new)
        .maxIO(6, 1, 1, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.BloodyHell.get(1)))
        .build();

    // #tr tst.recipe.BloodyHellBindingRecipes
    // # Bloody Hell Binding Ritual Recipes
    // #zh_CN 血狱绑定仪式
    @Deprecated
    public static final RecipeMap<TST_RecipeMapBackend> BloodyHellRecipe_Binding = RecipeMapBuilder
        .of("tst.recipe.BloodyHellBindingRecipes", TST_RecipeMapBackend::new)
        .maxIO(2, 1, 1, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.BloodyHell.get(1)))
        .disableOptimize()
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CIRCUIT : null)
        .build();

    // #tr tst.recipe.MegaStoneBreakerRecipes
    // # Mega Stone Breaker
    // #zh_CN 巨型碎石机
    public static final RecipeMap<TST_RecipeMapBackend> MegaStoneBreakerRecipes = RecipeMapBuilder
        .of("tst.recipe.MegaStoneBreakerRecipes", TST_RecipeMapBackend::new)
        .maxIO(2, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaStoneBreaker.get(1)))
        .disableOptimize()
        .build();

    // #tr tst.recipe.IndustrialAlchemyTowerRecipe
    // # Industrial Alchemy Tower
    // #zh_CN 工业炼金塔
    public static final RecipeMap<RecipeMapBackend> IndustrialAlchemyTowerRecipe = RecipeMapBuilder
        .of("tst.recipe.IndustrialAlchemyTowerRecipe")
        .maxIO(2, 1, 0, 0)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.IndustrialAlchemyTower.get(1)))
        .useSpecialSlot()
        .disableOptimize()
        .build();

    // #tr tst.recipe.advCircuitAssemblyLineTrueRecipes
    // # Adv Circuit Assembly Line (Hide)
    // #zh_CN 进阶电路装配线(隐藏)
    public static final RecipeMap<RecipeMapBackend> advCircuitAssemblyLineRecipes = RecipeMapBuilder
        .of("tst.recipe.advCircuitAssemblyLineRecipes")
        .maxIO(6, 1, 1, 0)
        .minInputs(1, 1)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.AdvCircuitAssemblyLine.get(1)))
        .progressBar(GTUITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER)
        .disableRegisterNEI()
        .useSpecialSlot()
        .disableOptimize()
        .build();
}
