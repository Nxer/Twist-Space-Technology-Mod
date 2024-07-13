package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import gregtech.api.logic.ProcessingLogic;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_SteamMultiBase;

public abstract class TST_SteamMultiMachineBase<T extends TST_SteamMultiMachineBase<T>>
    extends GregtechMeta_SteamMultiBase<T> {

    public TST_SteamMultiMachineBase(String aName) {
        super(aName);
    }

    public TST_SteamMultiMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    // region Processing Logic

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.33F)
                    .setSpeedBoost(1.5F);
            }

        }.setMaxParallel(getMaxParallelRecipes());
    }

    @Override
    public int getMaxParallelRecipes() {
        return 16;
    }

    // endregion

    public void repairMachine() {
        mHardHammer = true;
        mSoftHammer = true;
        mScrewdriver = true;
        mCrowbar = true;
        mSolderingTool = true;
        mWrench = true;
    }

}
