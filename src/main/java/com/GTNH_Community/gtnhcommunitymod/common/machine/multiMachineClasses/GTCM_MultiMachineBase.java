package com.GTNH_Community.gtnhcommunitymod.common.machine.multiMachineClasses;

import static com.GTNH_Community.gtnhcommunitymod.common.machine.ValueEnum.MAX_PARALLEL_LIMIT;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import com.GTNH_Community.gtnhcommunitymod.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Recipe;

public abstract class GTCM_MultiMachineBase<T extends GTCM_MultiMachineBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GTCM_MultiMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_MultiMachineBase(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic

    /**
     * Need Rewrite in every machine class.
     * 
     * @return The recipe map this machine processing.
     */
    @ApiStatus.OverrideOnly
    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
    }

    /**
     * Creates logic to run recipe check based on recipemap. This runs only once, on class instantiation.
     * <p>
     * If this machine doesn't use recipemap or does some complex things, override {@link #checkProcessing()}.
     */
    @ApiStatus.OverrideOnly
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclock(isEnablePerfectOverclock() ? 2 : 1, 2);
                return super.process();
            }

        }.setMaxParallelSupplier(this::getLimitedMaxParallel);
    }

    /**
     * Proxy Perfect Overclock Supplier.
     * 
     * @return If true, enable Perfect Overclock.
     */
    @ApiStatus.OverrideOnly
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    /**
     * Proxy Standard Speed Multiplier Supplier.
     * 
     * @return The value (or a method to get the value) of Speed Multiplier (dynamically) .
     */
    @ApiStatus.OverrideOnly
    protected float getSpeedBonus() {
        return 1.0F;
    }

    /**
     * Proxy Standard Parallel Supplier.
     * 
     * @return The value (or a method to get the value) of Max Parallel (dynamically) .
     */
    @ApiStatus.OverrideOnly
    protected int getMaxParallelRecipes() {
        return 1;
    }

    /**
     * Limit the max parallel to prevent overflow.
     * 
     * @return Limited parallel.
     */
    protected int getLimitedMaxParallel() {
        return Math.min(MAX_PARALLEL_LIMIT, getMaxParallelRecipes());
    }

    /**
     * Checks recipe and setup machine if it's successful.
     * <p>
     * For generic machine working with recipemap, use {@link #createProcessingLogic()} to make use of shared codebase.
     */
    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        // If no logic is found, try legacy checkRecipe
        if (processingLogic == null) {
            return checkRecipe(mInventory[1]) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        }

        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processingLogic.getDuration();
        setEnergyUsage(processingLogic);

        mOutputItems = processingLogic.getOutputItems();
        mOutputFluids = processingLogic.getOutputFluids();

        return result;
    }

    // region Overrides
    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length - 1] = EnumChatFormatting.AQUA + "Parallels: "
            + EnumChatFormatting.GOLD
            + this.getLimitedMaxParallel();
        ret[origin.length] = EnumChatFormatting.AQUA + "Speed multiplier: "
            + EnumChatFormatting.GOLD
            + this.getSpeedBonus();
        return ret;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    // endregion
}
