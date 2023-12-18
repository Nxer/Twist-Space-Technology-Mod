package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.*;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_FluidGenerator;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

public class GT_MetaTileEntity_Hatch_Mana extends GT_MetaTileEntity_Hatch_FluidGenerator{

    public static final int MAX_MANA = 1000000;
    int mana;

    public GT_MetaTileEntity_Hatch_Mana(final int aID, final String aName, final String aNameRegional,
        final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_Mana(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
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
        String[] aTooltip = new String[3];
        aTooltip[0] = texter("Mana transform hatch", "GT_MetaTileEntity_Hatch_Mana.Tooltip0");
        aTooltip[1] = texter("Transform mana up to 10000 every second", "GT_MetaTileEntity_Hatch_Mana.Tooltip1");
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
        return true;
    }

    @Override
    public int getMaxTickTime() {
        return 20;
    }

    @Override
    public int getCapacity() {
        return 128_000;
    }

    @Override
    public boolean doesHatchMeetConditionsToGenerate() {
        return true;
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
    public boolean addFluidToHatch(long aTick) {
        if (!this.doesHatchMeetConditionsToGenerate()) {
            return false;
        } else {
            int aFillAmount = super.fill(
                FluidUtils.getFluidStack(
                    this.getFluidToGenerate(),
                    (int) (mana / 10 > getCapacity() - this.getFluidAmount()
                        ? mana / 10 > getCapacity() - this.getFluidAmount()
                        : mana / 10)),
                true);
            // if (aFillAmount > 0 && this.getBaseMetaTileEntity().isClientSide()) {
            // this.generateParticles(this.getBaseMetaTileEntity().getWorld(), "cloud");
            // }
            mana -= aFillAmount * 10;
            return aFillAmount > 0;
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
