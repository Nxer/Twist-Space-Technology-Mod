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
        .ofFailurePersistOnShutdown("CheckRecipeResult.NoSpaceTimeMaintenanceFluidInput");

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

}
