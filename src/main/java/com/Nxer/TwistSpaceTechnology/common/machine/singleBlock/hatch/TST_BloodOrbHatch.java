package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
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

    private static final String TEXTURE_NAME_OVERLAY_ACTIVE = "gtnhcommunitymod:overlay_blood_hatch";
    private static final String TEXTURE_NAME_OVERLAY_INACTIVE = "gtnhcommunitymod:overlay_blood_hatch_inactive";

    public static Textures.BlockIcons.CustomIcon OVERLAY_ACTIVE = new Textures.BlockIcons.CustomIcon(
        TEXTURE_NAME_OVERLAY_ACTIVE);
    public static Textures.BlockIcons.CustomIcon OVERLAY_INACTIVE = new Textures.BlockIcons.CustomIcon(
        TEXTURE_NAME_OVERLAY_INACTIVE);

    // TODO: add Chinese localized text

    private static final String[] DESC = new String[] {
        // #tr Tooltip_BloodOrbHatch_1
        // # Life Essence Input for Multiblocks
        TextEnums.tr("Tooltip_BloodOrbHatch_1"),
        // #tr Tooltip_BloodOrbHatch_2
        // # Capacity is equal to the Blood Orb capacity inserted.
        TextEnums.tr("Tooltip_BloodOrbHatch_2"),
        // #tr Tooltip_BloodOrbHatch_3
        // # A hatch drain Life Essence from the Blood Orb.
        TextEnums.tr("Tooltip_BloodOrbHatch_3"),
        // #tr Tooltip_BloodOrbHatch_4
        // # Drains LP as much as possible from the Soul Network.
        TextEnums.tr("Tooltip_BloodOrbHatch_4"), TextEnums.Author_Taskeren.getText(), };

    private static final String[] TOOLTIP = new String[] { TextEnums.tr("Tooltip_BloodOrbHatch_3"),
        TextEnums.tr("Tooltip_BloodOrbHatch_4") };

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

        int maxDrainAmount = Math.min(getCapacity() - getFluidAmount(), getMaxCanDrainFromOrb());
        int drainedAmount = drainFromOrb(maxDrainAmount);
        if (drainedAmount > 0) {
            super.fill(FluidUtils.getFluidStack(getFluidToGenerate(), drainedAmount), true);
            return true;
        }

        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (getBaseMetaTileEntity().isAllowedToWork()) {
            getBaseMetaTileEntity().setActive(true);
            this.mMaxProgresstime = getMaxTickTime();
            if (++this.mProgresstime >= this.mMaxProgresstime) {
                if (this.canTankBeFilled()) {
                    this.addFluidToHatch(aTick);
                }
            }
        } else {
            getBaseMetaTileEntity().setActive(false);
            this.mProgresstime = 0;
            this.mMaxProgresstime = 0;
        }
    }
}
