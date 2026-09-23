package com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public final class CheckRecipeResults {

    public static void initStatics() {
        CheckRecipeResultRegistry.register(new SimpleResultWithText(false, "", false));
    }

    // #tr tst.common.shared.result.no_idle_execution_core
    // # No idle execution core.
    // #zh_CN 没有空闲的执行核心
    public static final CheckRecipeResult NoIdleExecutionCore = SimpleResultWithText
        .ofFailure("tst.common.shared.result.no_idle_execution_core");

    // #tr tst.common.shared.result.set_processing_failed
    // # Set processing failed. Check your power system or wireless EU net.
    // #zh_CN 设置处理任务失败，检查你的能源系统或无线EU网络。
    public static final CheckRecipeResult SetProcessingFailed = SimpleResultWithText
        .ofFailurePersistOnShutdown("tst.common.shared.result.set_processing_failed");

    // #tr tst.common.shared.result.no_space_time_maintenance_fluid_input
    // # No Space Time Maintenance Fluid Input
    // #zh_CN 没有时空维护流体输入
    public static final CheckRecipeResult NoSpaceTimeMaintenanceFluidInput = SimpleResultWithText
        .ofFailure("tst.common.shared.result.no_space_time_maintenance_fluid_input");

    // #tr tst.common.shared.result.no_annihilation_constrainer_input
    // # No Annihilation Constrainers Input
    // #zh_CN 没有湮灭约束器输入
    public static final CheckRecipeResult NoAnnihilationConstrainerInput = SimpleResultWithText
        .ofFailure("tst.common.shared.result.no_annihilation_constrainer_input");

    // #tr tst.common.shared.result.no_correct_fluid_input
    // # No Correct Fluid Input
    // #zh_CN 没有正确的流体输入
    public static final CheckRecipeResult NoCorrectFluidInput = SimpleResultWithText
        .ofFailurePersistOnShutdown("tst.common.shared.result.no_correct_fluid_input");

    // #tr tst.common.machine.SwelegfyrBlastFurnace.result.rapid_heating
    // # Rapid Thermal Boosting
    // #zh_CN 快速升温中
    public static final CheckRecipeResult RapidHeating = SimpleResultWithText
        .ofSuccess("tst.common.machine.SwelegfyrBlastFurnace.result.rapid_heating");

    // #tr tst.common.machine.SwelegfyrBlastFurnace.result.rapid_heat_finish
    // # Heating Complete: Thermal Retention Activated
    // #zh_CN 升温完成：已自动执行热保持操作
    public static final CheckRecipeResult RapidHeatFinish = SimpleResultWithText
        .ofSuccess("tst.common.machine.SwelegfyrBlastFurnace.result.rapid_heat_finish");

    // #tr tst.common.shared.result.no_seed_in_controller
    // # No valid seed input detected
    // #zh_CN 未检测到有效种子输入
    public static final CheckRecipeResult NoSeedInController = SimpleResultWithText
        .ofFailure("tst.common.shared.result.no_seed_in_controller");

    // #tr tst.common.shared.result.not_enough_water
    // # Insufficient water supply
    // #zh_CN 供水量不足
    public static final CheckRecipeResult NotEnoughWater = SimpleResultWithText
        .ofFailure("tst.common.shared.result.not_enough_water");

    // #tr tst.ecosphere.machine.EcoSphereSimulator.result.missing_tree_output_selection
    // # No arboreal output category selected
    // #zh_CN 未指定林木输出类别
    public static final CheckRecipeResult MissingTreeOutputSelection = SimpleResultWithText
        .ofFailure("tst.ecosphere.machine.EcoSphereSimulator.result.missing_tree_output_selection");

    // #tr tst.ecosphere.machine.EcoSphereSimulator.result.missing_sapling_input
    // # No valid sapling input detected
    // #zh_CN 未检测到有效树苗输入
    public static final CheckRecipeResult MissingSaplingInput = SimpleResultWithText
        .ofFailure("tst.ecosphere.machine.EcoSphereSimulator.result.missing_sapling_input");

    // #tr tst.ecosphere.machine.EcoSphereSimulator.result.execution_protocol_input_mismatch
    // # Authorization level insufficient
    // #zh_CN 授权等级不足
    public static final CheckRecipeResult ExecutionProtocolInputMismatch = SimpleResultWithText
        .ofFailure("tst.ecosphere.machine.EcoSphereSimulator.result.execution_protocol_input_mismatch");

}
