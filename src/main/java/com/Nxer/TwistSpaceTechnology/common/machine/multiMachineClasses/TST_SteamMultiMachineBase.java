package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses;

import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.GregTechAPI.sBlockCasings2;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import gregtech.api.logic.ProcessingLogic;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.OverclockCalculator;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;

public abstract class TST_SteamMultiMachineBase<T extends TST_SteamMultiMachineBase<T>> extends MTESteamMultiBase<T> {

    public TST_SteamMultiMachineBase(String aName) {
        super(aName);
    }

    public TST_SteamMultiMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    // region Processing Logic
    public static final List<Pair<Block, Integer>> STEAM_CASING_LIST = ImmutableList
        .of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0));

    public static int checkSteamCasingTier(Block block, int meta) {
        if (null == block || ((block != sBlockCasings1) && (block != sBlockCasings2))) return 0;
        if (block == sBlockCasings1 && meta == 10) return 1;
        if (block == sBlockCasings2 && meta == 0) return 2;
        return 0;
    }

    public static int getCasingTextureID(int steamCasingTier) {
        if (steamCasingTier == 2) return 16;
        return 10;
    }

    public void repairMachine() {
        mHardHammer = true;
        mSoftMallet = true;
        mScrewdriver = true;
        mCrowbar = true;
        mSolderingTool = true;
        mWrench = true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.33F);
            }

        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 16;
    }

    // endregion

}
