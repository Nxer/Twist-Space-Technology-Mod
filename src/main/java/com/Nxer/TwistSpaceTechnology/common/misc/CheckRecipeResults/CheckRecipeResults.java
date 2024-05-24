package com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public final class CheckRecipeResults {

    static {
        CheckRecipeResultRegistry.register(new SimpleResultWithText(false, "", false));
    }

    // #tr CheckRecipeResult.NoIdleExecutionCore
    // # No idle execution core.
    // #zh_CN 没有空闲的执行核心
    /**
     *
     */
    public static final CheckRecipeResult NoIdleExecutionCore = SimpleResultWithText
        .ofFailure("CheckRecipeResult.NoIdleExecutionCore");
}
