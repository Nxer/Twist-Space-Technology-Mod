package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
import com.Nxer.TwistSpaceTechnology.util.MathUtils;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchFluidGenerator;

public class TST_BloodOrbHatch extends MTEHatchFluidGenerator {

    private static final String TEXTURE_NAME_OVERLAY_ACTIVE = "gtnhcommunitymod:iconSets/overlay_blood_hatch";
    private static final String TEXTURE_NAME_OVERLAY_INACTIVE = "gtnhcommunitymod:iconSets/overlay_blood_hatch_inactive";

    public static Textures.BlockIcons.CustomIcon OVERLAY_ACTIVE = new Textures.BlockIcons.CustomIcon(
        TEXTURE_NAME_OVERLAY_ACTIVE);
    public static Textures.BlockIcons.CustomIcon OVERLAY_INACTIVE = new Textures.BlockIcons.CustomIcon(
        TEXTURE_NAME_OVERLAY_INACTIVE);

    // TODO: add Chinese localized text

    private static final String[] DESC = new String[] {
        // #tr Tooltip_BloodOrbHatch_1
        // # Life Essence Input for Multiblocks
        // #zh_CN 多方块结构的生命本质输入仓
        TextEnums.tr("Tooltip_BloodOrbHatch_1"),
        // #tr Tooltip_BloodOrbHatch_2
        // # Capacity is equal to the Blood Orb capacity inserted.
        // #zh_CN 容量等于插入的气血宝珠的容量
        TextEnums.tr("Tooltip_BloodOrbHatch_2"),
        // #tr Tooltip_BloodOrbHatch_3
        // # A hatch drain Life Essence from the Blood Orb.
        // #zh_CN 可以从气血宝珠中提取生命本质
        TextEnums.tr("Tooltip_BloodOrbHatch_3"),
        // #tr Tooltip_BloodOrbHatch_4
        // # Drains LP as much as possible from the Soul Network.
        // #zh_CN 尽可能多地从灵魂网络中抽取LP
        TextEnums.tr("Tooltip_BloodOrbHatch_4"),
        // #tr Tooltip_BloodOrbHatch_5
        // # Deactivating the Hatch will refund the Life Essence back to the Blood Orb.
        // #zh_CN 关闭血液仓将会把生命本质输回气血宝珠
        TextEnums.tr("Tooltip_BloodOrbHatch_5"), TextEnums.Author_Taskeren.getText(),
        TextEnums.Mod_TwistSpaceTechnology.getText() };

    private static final String[] TOOLTIP = new String[] { TextEnums.tr("Tooltip_BloodOrbHatch_3"),
        TextEnums.tr("Tooltip_BloodOrbHatch_4"), TextEnums.tr("Tooltip_BloodOrbHatch_5"), };

    public TST_BloodOrbHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public TST_BloodOrbHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BloodOrbHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Nullable
    public ItemStack getOrbItemStack() {
        return mInventory[0];
    }

    private int getCapacityFromOrb() {
        var orb = getOrbItemStack();
        if (orb != null) {
            var capacity = BloodMagicHelper.getOrbCapacity(orb);
            if (capacity != -1) {
                return capacity;
            }
        }
        return 0;
    }

    private int getMaxCanDrainFromOrb() {
        var orb = getOrbItemStack();
        if (orb != null) {
            if (BloodMagicHelper.isCreativeOrb(orb)) {
                return getCapacity() - getFluidAmount(); // the unfilled amount
            }
            return BloodMagicHelper.getOrbOwnerLpAmount(orb);
        }
        return 0;
    }

    private int drainFromOrb(int lpAmount) {
        var orb = getOrbItemStack();
        if (orb != null) {
            if (BloodMagicHelper.isCreativeOrb(orb)) { // creative orb, drain them all!
                return lpAmount;
            }
            return SoulNetworkHandler.syphonFromNetwork(orb, lpAmount);
        }
        return 0;
    }

    @Override
    public synchronized String[] getDescription() {
        return DESC;
    }

    @Override
    public String[] getCustomTooltip() {
        return TOOLTIP;
    }

    @Override
    public Fluid getFluidToGenerate() {
        return FluidUtils.getFluidStack(AlchemicalWizardry.lifeEssenceFluid, 1)
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
        return getCapacityFromOrb();
    }

    @Override
    public boolean doesHatchMeetConditionsToGenerate() {
        return BloodMagicHelper.getOrbOwnerName(getOrbItemStack()) != null
            || BloodMagicHelper.isCreativeOrb(getOrbItemStack());
    }

    @Override
    public void generateParticles(World aWorld, String name) {}

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_INACTIVE) };
    }

    @Override
    public boolean addFluidToHatch(long aTick) {
        if (!getBaseMetaTileEntity().isServerSide()) { // I don't know where to check if remote. :(
            return false;
        }

        if (!this.doesHatchMeetConditionsToGenerate()) {
            return false;
        }

        // the orb is swapped to one with smaller capacity.
        // update the amount to the capacity, and void the exceeding bloods.
        if (getFluid() != null && getFluidAmount() > getCapacity()) {
            getFluid().amount = getCapacity();
            return false;
        }

        int maxDrainAmount = MathUtils.clamp(getCapacity() - getFluidAmount(), 0, getMaxCanDrainFromOrb());
        int drainedAmount = drainFromOrb(maxDrainAmount);
        if (drainedAmount > 0) {
            super.fill(FluidUtils.getFluidStack(getFluidToGenerate(), drainedAmount), true);
            return true;
        }

        return false;
    }

    /**
     * Refund the remaining blood back to the network of orb's owner.
     */
    public void refundBlood() {
        if (!getBaseMetaTileEntity().isServerSide()) {
            return;
        }

        // return the bloods back to the network
        if (getFluid() != null && getFluidAmount() > 0) {
            var added = BloodMagicHelper.addBloodToNetwork(getOrbItemStack(), getFluidAmount());
            mFluid.amount -= added;
            if (mFluid.amount <= 0) {
                mFluid = null;
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (getBaseMetaTileEntity().isAllowedToWork()) {
            getBaseMetaTileEntity().setActive(true);
            this.mMaxProgresstime = getMaxTickTime();
            if (++this.mProgresstime >= this.mMaxProgresstime) {
                if (this.getOrbItemStack() == null) { // remove the remaining bloods if the orb is not present
                    this.mFluid = null;
                } else { // canTankBeFilled() will always be true, ignored
                    this.addFluidToHatch(aTick);
                }
                this.mProgresstime = 0;
            }
        } else {
            getBaseMetaTileEntity().setActive(false);
            refundBlood();
            this.mProgresstime = 0;
            this.mMaxProgresstime = 0;
        }
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return true;
    }

    @Override
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        return true;
    }
}
