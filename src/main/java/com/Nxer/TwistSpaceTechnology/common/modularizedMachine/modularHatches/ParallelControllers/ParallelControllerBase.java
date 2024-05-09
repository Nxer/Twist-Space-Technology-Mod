package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DATA_ACCESS;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;

import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;

public abstract class ParallelControllerBase extends GT_MetaTileEntity_Hatch implements IModularHatch {

    // region Class Constructor
    public ParallelControllerBase(int aID, String aName, String aNameRegional, int aTier, String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, 0, aDescription);
    }

    public ParallelControllerBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    // endregion

    // region Logic
    public ModularHatchTypes getType() {
        return ModularHatchTypes.PARALLEL_CONTROLLER;
    }

    public abstract int getParallel();

    public void onChecking(ModularizedMachineBase<?> machine) {
        if (machine instanceof IModularizedMachine.ISupportParallelController parallelSupporter) {
            int p = parallelSupporter.getParallelParameterValue();
            if (p == Integer.MAX_VALUE) return;
            int tp = getParallel();
            if (p >= Integer.MAX_VALUE - tp) {
                parallelSupporter.setParallelParameter(Integer.MAX_VALUE);
            } else {
                parallelSupporter.setParallelParameter(p + tp);
            }
        }
    }

    // endregion

    // region Texture
    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_DATA_ACCESS) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_DATA_ACCESS) };
    }
    // endregion

}
