package com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public final class CheckRecipeResults {

    public static void initStatics() {
        CheckRecipeResultRegistry.register(new SimpleResultWithText(false, "", false));
    }

    // #tr CheckRecipeResult.NoIdleExecutionCore
    // # No idle execution core.
    // #zh_CN 没有空闲的执行核心
    public static final CheckRecipeResult NoIdleExecutionCore = SimpleResultWithText
        .ofFailure("CheckRecipeResult.NoIdleExecutionCore");

    // #tr CheckRecipeResult.SetProcessingFailed
    // # Set processing failed. Check your power system or wireless EU net.
    // #zh_CN 设置处理任务失败，检查你的能源系统或无线EU网络。
    public static final CheckRecipeResult SetProcessingFailed = SimpleResultWithText
        .ofFailurePersistOnShutdown("CheckRecipeResult.SetProcessingFailed");

    // #tr CheckRecipeResult.NoSpaceTimeMaintenanceFluidInput
    // # No Space Time Maintenance Fluid Input
    // #zh_CN 没有时空维护流体输入
    public static final CheckRecipeResult NoSpaceTimeMaintenanceFluidInput = SimpleResultWithText
        .ofFailure("CheckRecipeResult.NoSpaceTimeMaintenanceFluidInput");

    // #tr CheckRecipeResult.NoAnnihilationConstrainerInput
    // # No Annihilation Constrainers Input
    // #zh_CN 没有湮灭约束器输入
    public static final CheckRecipeResult NoAnnihilationConstrainerInput = SimpleResultWithText
        .ofFailure("CheckRecipeResult.NoAnnihilationConstrainerInput");

    // #tr CheckRecipeResult.NoCorrectFluidInput
    // # No Correct Fluid Input
    // #zh_CN 没有正确的流体输入
    public static final CheckRecipeResult NoCorrectFluidInput = SimpleResultWithText
        .ofFailurePersistOnShutdown("CheckRecipeResult.NoCorrectFluidInput");

    // #tr CheckRecipeResult.RapidHeating
    // # Rapid Thermal Boosting
    // #zh_CN 快速升温中
    public static final CheckRecipeResult RapidHeating = SimpleResultWithText
        .ofSuccess("CheckRecipeResult.RapidHeating");

    // #tr CheckRecipeResult.RapidHeatFinish
    // # Heating Complete: Thermal Retention Activated
    // #zh_CN 升温完成：已自动执行热保持操作
    public static final CheckRecipeResult RapidHeatFinish = SimpleResultWithText
        .ofSuccess("CheckRecipeResult.RapidHeatFinish");

    // #tr CheckRecipeResult.NoSeedInController
    // # Missing seed input
    // #zh_CN 缺失种子输入
    public static final CheckRecipeResult NoSeedInController = SimpleResultWithText
        .ofFailure("CheckRecipeResult.NoSeedInController");

    // #tr CheckRecipeResult.NotEnoughWater
    // # Not enough water has been input.
    // #zh_CN 没有输入足够的水
    public static final CheckRecipeResult NotEnoughWater = SimpleResultWithText
        .ofFailure("CheckRecipeResult.NotEnoughWater");

    // #tr CheckRecipeResult.EcoSphereSimulator.MissingTreeOutputSelection
    // # Missing circuit input
    // #zh_CN 缺失输入电路板
    public static final CheckRecipeResult MissingTreeOutputSelection = SimpleResultWithText
        .ofFailure("CheckRecipeResult.EcoSphereSimulator.MissingTreeOutputSelection");

    // #tr CheckRecipeResult.EcoSphereSimulator.MissingSaplingInput
    // # Missing sapling input
    // #zh_CN 缺失树苗输入
    public static final CheckRecipeResult MissingSaplingInput = SimpleResultWithText
        .ofFailure("CheckRecipeResult.EcoSphereSimulator.MissingSaplingInput");

    // #tr CheckRecipeResult.EcoSphereSimulator.ModeBeaconInputMismatch
    // # Requires a higher-tier mode beacon
    // #zh_CN 需要更高等级的模式信标
    public static final CheckRecipeResult ModeBeaconInputMismatch = SimpleResultWithText
        .ofFailure("CheckRecipeResult.EcoSphereSimulator.ModeBeaconInputMismatch");

}
