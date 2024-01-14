package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.FluidCapacity;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.HatchTier;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_FluidGenerator;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import vazkii.botania.common.block.tile.mana.TilePool;

public class GT_MetaTileEntity_Hatch_Mana extends GT_MetaTileEntity_Hatch_FluidGenerator {

    int mode = 0;
    int maxtrans = 50;

    public GT_MetaTileEntity_Hatch_Mana(final int aID, final String aName, final String aNameRegional,
        final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_Mana(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public int getTankPressure() {
        return -100;
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Mana(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public synchronized String[] getDescription() {
        mDescriptionArray[1] = FluidCapacity + GT_Utility.formatNumbers(getCapacity()) + "L";
        final String[] hatchTierString = new String[] { HatchTier + GT_Utility.getColoredTierNameFromTier(mTier) };

        String[] aCustomTips = getCustomTooltip();
        final String[] desc = new String[mDescriptionArray.length + aCustomTips.length + 2];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        System.arraycopy(hatchTierString, 0, desc, mDescriptionArray.length, 1);
        System.arraycopy(aCustomTips, 0, desc, mDescriptionArray.length + 1, aCustomTips.length);
        desc[mDescriptionArray.length + aCustomTips.length] = ModNameDesc;
        return desc;
    }

    @Override
    public String[] getCustomTooltip() {
        String[] aTooltip = new String[5];
        aTooltip[0] = texter("Mana transform hatch", "GT_MetaTileEntity_Hatch_Mana.Tooltip0");
        aTooltip[1] = texter(
            "Transform mana up to 10000 mana or 1000L every second",
            "GT_MetaTileEntity_Hatch_Mana.Tooltip1");
        aTooltip[2] = texter("Need to facing at a mana pool to work.", "GT_MetaTileEntity_Hatch_Mana.Tooltip2");
        aTooltip[3] = TextLocalization.textScrewdriverChangeMode;
        return aTooltip;
    }

    @Override
    public Fluid getFluidToGenerate() {
        return MaterialPool.LiquidMana.getFluidOrGas(0)
            .getFluid();
    }

    @Override
    public int getAmountOfFluidToGenerate() {
        return 0;
    }

    @Override
    public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
        return aFluid == getFluidToGenerate();
    }

    @Override
    public int getMaxTickTime() {
        return 1;
    }

    @Override
    public int getCapacity() {
        return 100_000;
    }

    @Override
    public boolean doesHatchMeetConditionsToGenerate() {
        FluidStack fluid = this.getFluid();
        boolean temp = false;
        if (fluid == null) temp = true;
        else temp = fluid.getFluid() == getFluidToGenerate();
        return this.getBaseMetaTileEntity()
            .getTileEntityAtSideAndDistance(
                this.getBaseMetaTileEntity()
                    .getFrontFacing(),
                1) instanceof TilePool
            && temp;
    }

    @Override
    public void generateParticles(World aWorld, String name) {}

    @Override
    public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Muffler_Adv) };
    }

    @Override
    public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Muffler_Adv) };
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.mode = (this.mode + 1) % 2;
        GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("Mana_Hatch.modeMsg." + this.mode));
    }

    @Override
    public boolean addFluidToHatch(long aTick) {
        if (!this.doesHatchMeetConditionsToGenerate()) {
            return false;
        } else {
            TilePool pool = ((TilePool) this.getBaseMetaTileEntity()
                .getTileEntityAtSideAndDistance(
                    this.getBaseMetaTileEntity()
                        .getFrontFacing(),
                    1));
            int mana = pool.getCurrentMana();
            int aFillAmount = 0;
            if (mode == 0) {
                aFillAmount = super.fill(
                    FluidUtils.getFluidStack(
                        this.getFluidToGenerate(),
                        (int) Math.min(Math.min(mana / 10, maxtrans), getCapacity() - this.getFluidAmount())),
                    true);

                pool.recieveMana(-aFillAmount * 10);
            } else {
                if (this.getFluidAmount() != 0) {
                    aFillAmount = super.drain(
                        (int) Math.min(Math.min(pool.getAvailableSpaceForMana() / 10, maxtrans), this.getFluidAmount()),
                        true).amount;
                    pool.recieveMana(aFillAmount * 10);
                }
            }
            return aFillAmount != 0;
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isAllowedToWork()) {
            aBaseMetaTileEntity.setActive(false);
            this.mProgresstime = 0;
            this.mMaxProgresstime = 0;
        } else {
            aBaseMetaTileEntity.setActive(true);
            this.mMaxProgresstime = this.getMaxTickTime();
            if (++this.mProgresstime >= this.mMaxProgresstime) {
                if (this.canTankBeFilled()) {
                    this.addFluidToHatch(aTick);
                }

                this.mProgresstime = 0;
            }
        }

    }
}
