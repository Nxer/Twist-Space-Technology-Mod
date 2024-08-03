package com.Nxer.TwistSpaceTechnology.common.recipeMap;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters.ArtificialStar_SpecialValueFormatter;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.NEISpecialInfoFormatters.DSP_Receiver_SpecialValueFormatter;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_GeneralFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_IndustrialMagicMatrixFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_MegaTreeFarmFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_StrangeMatterAggregatorFrontend;

import goodgenerator.client.GUI.GG_UITextures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.maps.AssemblyLineFrontend;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;

public class GTCMRecipe {

    public static final RecipeMap<TST_RecipeMapBackend> IntensifyChemicalDistorterRecipes = RecipeMapBuilder
        // At the same time , the localization key of the NEI Name
        // of this page.
        .of("gtcm.recipe.IntensifyChemicalDistorterRecipes", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 16)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.IntensifyChemicalDistorter.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> PreciseHighEnergyPhotonicQuantumMasterRecipes = RecipeMapBuilder
        .of("gtcm.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipes", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 16)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> MiracleTopRecipes = RecipeMapBuilder
        .of("gtcm.recipe.MiracleTopRecipes", TST_RecipeMapBackend::new)
        .maxIO(16, 16, 16, 4)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> QuantumInversionRecipes = RecipeMapBuilder
        .of("gtcm.recipe.QuantumInversionRecipes")
        .maxIO(4, 4, 2, 2)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> CrystallineInfinitierRecipes = RecipeMapBuilder
        .of("gtcm.recipe.CrystallineInfinitierRecipes", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 4, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.CrystallineInfinitier.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> DSP_LauncherRecipes = RecipeMapBuilder
        .of("gtcm.recipe.DSPLauncherRecipes")
        .maxIO(1, 1, 1, 0)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> DSP_ReceiverRecipes = RecipeMapBuilder
        .of("gtcm.recipe.DSPReceiverRecipes")
        .maxIO(0, 1, 0, 0)
        .neiSpecialInfoFormatter(DSP_Receiver_SpecialValueFormatter.INSTANCE)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.DSPReceiver.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> ElvenWorkshopRecipes = RecipeMapBuilder
        .of("gtcm.recipe.ElvenWorkshopRecipes")
        .maxIO(4, 4, 1, 0)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ElvenWorkshop.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> RuneEngraverRecipes = RecipeMapBuilder
        .of("gtcm.recipe.RuneEngraverRecipes")
        .maxIO(6, 1, 1, 0)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ElvenWorkshop.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> ArtificialStarGeneratingRecipes = RecipeMapBuilder
        .of("gtcm.recipe.ArtificialStarGeneratingRecipes")
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(ArtificialStar_SpecialValueFormatter.INSTANCE)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.ArtificialStar.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> megaUniversalSpaceStationRecipePool = RecipeMapBuilder
        .of("gtcm.recipe.megaUniversalSpaceStationRecipePool")
        .maxIO(16, 4, 16, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleTop.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> OreProcessingRecipes = RecipeMapBuilder
        .of("tst.recipe.OreProcessingRecipes")
        .maxIO(1, 9, 1, 0)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.OreProcessingFactory.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> CokingFactoryRecipes = RecipeMapBuilder
        .of("tst.recipe.CokingFactoryRecipes")
        .maxIO(2, 2, 1, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> StellarForgeRecipes = RecipeMapBuilder
        .of("tst.recipe.StellarForgeRecipes")
        .maxIO(4, 4, 1, 2)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<RecipeMapBackend> HyperSpacetimeTransformerRecipe = RecipeMapBuilder
        .of("tst.recipe.HyperSpacetimeTransformerRecipe")
        .maxIO(4, 4, 4, 4)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
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
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MiracleDoor.get(1)))
        .disableOptimize()
        .build();

    public static final RecipeMap<TST_RecipeMapBackend> BallLightningRecipes = RecipeMapBuilder
        .of("tst.recipe.BallLightningRecipes", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 4, 4)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
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
        .addSpecialTexture(73, 22, 31, 21, GG_UITextures.PICTURE_NEUTRON_ACTIVATOR)
        .disableOptimize()
        .build();

    // #tr tst.recipe.MassFabricatorGenesis
    // # Mass Fabricator : Genesis
    // #zh_CN 质量发生器 : 创世纪
    public static final RecipeMap<TST_RecipeMapBackend> MassFabricatorGenesis = RecipeMapBuilder
        .of("tst.recipe.MassFabricatorGenesis", TST_RecipeMapBackend::new)
        .maxIO(1, 0, 0, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MassFabricatorGenesis.get(1)))
        .disableOptimize()
        .build();

    // #tr tst.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes
    // # BioSphere : Tree Growth Simulator
    // #zh_CN 生物圈 : 原木拟生
    public static final RecipeMap<TST_RecipeMapBackend> TreeGrowthSimulatorWithoutToolFakeRecipes = RecipeMapBuilder
        .of("tst.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes", TST_RecipeMapBackend::new)
        .maxIO(4, 4, 1, 0)
        .minInputs(1, 1)
        .useSpecialSlot()
        .specialSlotSensitive()
        // .neiSpecialInfoFormatter(TST_MegaTreeGrowthSimulatorFrontend.INSTANCE)
        .frontend(TST_MegaTreeFarmFrontend::new)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTCMItemList.MegaTreeFarm.get(1)))
        .disableOptimize()
        .build();

    // #tr tst.recipe.StrangeMatterAggregatorRecipes
    // # Strange Matter Aggregation
    // #zh_CN 奇异物质聚合
    public static final RecipeMap<TST_RecipeMapBackend> StrangeMatterAggregatorRecipes = RecipeMapBuilder
        .of("tst.recipe.StrangeMatterAggregatorRecipes", TST_RecipeMapBackend::new)
        .maxIO(4, 2, 2, 2)
        .progressBar(GT_UITextures.PROGRESSBAR_COMPRESS)
        .neiHandlerInfo(b -> b.setDisplayStack(GTCMItemList.StrangeMatterAggregator.get(1)))
        .useSpecialSlot()
        .frontend(TST_StrangeMatterAggregatorFrontend::new)
        .disableOptimize()
        .build();
}
