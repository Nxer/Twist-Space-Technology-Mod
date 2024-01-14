package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.ticksOfInfiniteAirHatchFillFull;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.*;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.common.block.tile.mana.TilePool;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_FluidGenerator;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_Hatch_InfMana extends GT_MetaTileEntity_Hatch_FluidGenerator {

    public GT_MetaTileEntity_Hatch_InfMana(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_InfMana(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InfMana(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
        aTooltip[0] = texter("Infinite mana supply hatch", "GT_MetaTileEntity_Hatch_InfMana.Tooltip0");
        aTooltip[1] = texter("Fills to max capacity every second", "GT_MetaTileEntity_Hatch_Air.Tooltip1");
        return aTooltip;
    }

    @Override
    public Fluid getFluidToGenerate() {
        return MaterialPool.LiquidMana.getFluidOrGas(0)
            .getFluid();
    }

    @Override
    public int getAmountOfFluidToGenerate() {
        return 2_000_000_000;
    }

    @Override
    public int getMaxTickTime() {
        return ticksOfInfiniteAirHatchFillFull;
    }

    @Override
    public int getCapacity() {
        return 2_000_000_000;
    }

    @Override
    public boolean doesHatchMeetConditionsToGenerate() {
        return true;
    }

    @Override
    public boolean addFluidToHatch(long tick)
    {
        TileEntity manate=this.getBaseMetaTileEntity()
            .getTileEntityAtSideAndDistance(
                this.getBaseMetaTileEntity()
                    .getFrontFacing(),
                1);
                if(manate==null)
                {
                    return super.addFluidToHatch(tick);
                }
        if(manate instanceof ISparkAttachable)
                {
                    ((ISparkAttachable)manate).recieveMana(((ISparkAttachable)manate).getAvailableSpaceForMana());
                }
        return super.addFluidToHatch(tick);
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
}