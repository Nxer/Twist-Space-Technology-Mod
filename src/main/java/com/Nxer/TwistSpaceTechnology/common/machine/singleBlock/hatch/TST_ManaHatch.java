package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.FluidCapacity;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.HatchTier;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import vazkii.botania.common.block.tile.mana.TilePool;

public class TST_ManaHatch extends MTEHatchInput {

    private boolean isLiquidizerMode;
    private static FluidStack fluidMana;
    private static final int MAX_TRANS_RATE = 50;
    public static final ITexture TEXTURE = TextureFactory.of(TexturesGtBlock.Overlay_Hatch_Muffler_Adv);

    public TST_ManaHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public TST_ManaHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ManaHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TEXTURE };
    }

    @Override
    public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TEXTURE };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (fluidMana == null) fluidMana = MaterialPool.LiquidMana.getFluidOrGas(0);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide()) return;
        var pool = getManaPool();
        if (pool == null) return;
        int fluidAmount = getFluidAmount();
        int currentMana = pool.getCurrentMana();
        if (isLiquidizerMode) {
            if (fluidAmount != 0) {
                pool.recieveMana(
                    drain(
                        Math.min(Math.min(pool.getAvailableSpaceForMana() / 10, MAX_TRANS_RATE), fluidAmount),
                        true).amount * 10);
            }
        } else {
            if (currentMana != 0) {
                pool.recieveMana(
                    -fill(
                        createFluidStack(
                            Math.min(Math.min(currentMana / 10, MAX_TRANS_RATE), getCapacity() - fluidAmount)),
                        true) * 10);
            }
        }
    }

    private static FluidStack createFluidStack(int amount) {
        var c = fluidMana.copy();
        c.amount = amount;
        return c;
    }

    @Nullable
    private TilePool getManaPool() {
        var ma = getBaseMetaTileEntity();
        TileEntity te = ma.getTileEntityAtSideAndDistance(ma.getFrontFacing(), 1);
        if (te instanceof TilePool pool) {
            return pool;
        }
        return null;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
        isLiquidizerMode = !isLiquidizerMode;
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal("Mana_Hatch.modeMsg." + (isLiquidizerMode ? 0 : 1)));
    }

    @Override
    public synchronized String[] getDescription() {
        mDescriptionArray[1] = FluidCapacity + GTUtility.formatNumbers(getCapacity()) + "L";
        final String[] hatchTierString = new String[] { HatchTier + GTUtility.getColoredTierNameFromTier(mTier) };

        String[] aCustomTips = getCustomTooltip();
        final String[] desc = new String[mDescriptionArray.length + aCustomTips.length + 2];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        System.arraycopy(hatchTierString, 0, desc, mDescriptionArray.length, 1);
        System.arraycopy(aCustomTips, 0, desc, mDescriptionArray.length + 1, aCustomTips.length);
        desc[mDescriptionArray.length + aCustomTips.length] = ModNameDesc;
        return desc;
    }

    public String[] getCustomTooltip() {
        String[] aTooltip = new String[5];

        // #tr GT_MetaTileEntity_Hatch_Mana.Tooltip0
        // # Mana transform hatch
        // #zh_CN 魔力转换阀
        aTooltip[0] = TextEnums.tr("GT_MetaTileEntity_Hatch_Mana.Tooltip0");

        // #tr GT_MetaTileEntity_Hatch_Mana.Tooltip1
        // # Transform mana up to 10000 mana or 1000L every second
        // #zh_CN 每秒至多转换1000L液态魔力/10000mana.
        aTooltip[1] = TextEnums.tr("GT_MetaTileEntity_Hatch_Mana.Tooltip1");

        // #tr GT_MetaTileEntity_Hatch_Mana.Tooltip2
        // # Need to facing at a mana pool to work.
        // #zh_CN 需要正面朝向魔力池来工作.
        aTooltip[2] = TextEnums.tr("GT_MetaTileEntity_Hatch_Mana.Tooltip2");

        aTooltip[3] = TextLocalization.textScrewdriverChangeMode;

        return aTooltip;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("isLiquidizerMode", isLiquidizerMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isLiquidizerMode = aNBT.getBoolean("isLiquidizerMode");
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return aFluid.getFluid()
            .equals(fluidMana.getFluid());
    }

    @Override
    public int getCapacity() {
        return 100_000;
    }
}
