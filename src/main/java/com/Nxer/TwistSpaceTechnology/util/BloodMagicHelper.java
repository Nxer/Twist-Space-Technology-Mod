package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.config.Config;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.items.ActivationCrystal;
import WayofTime.alchemicalWizardry.common.items.BloodShard;
import fox.spiteful.avaritia.items.LudicrousItems;

public class BloodMagicHelper {

    public static boolean isBloodOrb(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() instanceof IBloodOrb;
    }

    public static boolean isCreativeOrb(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() == LudicrousItems.armok_orb
            && Config.Enable_BloodHatch_Armok_InfiniteDrain;
    }

    /**
     * @param stack the ItemStack of Blood Orb
     * @return the owner name of the orb. {@code null} if the item is not valid or nbt tag is not valid.
     */
    @Nullable
    public static String getOrbOwnerName(@Nullable ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof IBloodOrb)) {
            return null;
        }

        var nbtTag = stack.getTagCompound();
        if (nbtTag == null) return null;

        var ownerName = nbtTag.getString("ownerName");
        return ownerName.isEmpty() ? null : ownerName;
    }

    /**
     * @param stack the ItemStack of Blood Orb
     * @return the maximum capacity of the orb, or -1 if null
     */
    public static int getOrbCapacity(@Nullable ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IBloodOrb orb) {
            return SoulNetworkHandler.getMaximumForOrbTier(orb.getOrbLevel());
        } else {
            return -1;
        }
    }

    /**
     * @param stack the blood orb stack
     * @return the tier of the blood orb (from 1 to 6)
     */
    public static int getBloodOrbTier(@Nullable ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IBloodOrb orb) {
            return orb.getOrbLevel();
        } else {
            return 0;
        }
    }

    public static int getOrbOwnerLpAmount(@Nullable ItemStack stack) {
        var ownerName = getOrbOwnerName(stack);
        if (ownerName != null) {
            return SoulNetworkHandler.getCurrentEssence(ownerName);
        } else {
            return 0;
        }
    }

    public static int getOrbOwnerRemainingLpCapacity(@Nullable ItemStack stack) {
        var ownerName = getOrbOwnerName(stack);
        if (ownerName == null) return 0;
        long remaining = (long) SoulNetworkHandler.getMaxEssence(ownerName)
            - Math.max(0, SoulNetworkHandler.getCurrentEssence(ownerName));
        return (int) Math.max(0, Math.min(Integer.MAX_VALUE, remaining));
    }

    /**
     * Pays LP from the blood orb owner's network.
     *
     * @return the amount paid; a configured creative orb reports payment without changing the network
     */
    public static int drainBloodFromNetwork(@Nullable ItemStack orbStack, int lpAmount) {
        if (lpAmount <= 0 || !isBloodOrb(orbStack)) return 0;
        if (isCreativeOrb(orbStack)) return lpAmount;
        if (getOrbOwnerName(orbStack) == null) return 0;
        int amountToDrain = Math.min(lpAmount, Math.max(0, getOrbOwnerLpAmount(orbStack)));
        return amountToDrain <= 0 ? 0 : SoulNetworkHandler.syphonFromNetwork(orbStack, amountToDrain);
    }

    /**
     * @param stack the shard stack
     * @return the tier of the shard (weak: 1; demon: 2)
     */
    public static int getBloodShardTier(@Nullable ItemStack stack) {
        if (stack != null && stack.getItem() instanceof BloodShard shard) {
            return shard.getBloodShardLevel();
        } else {
            return 0;
        }
    }

    /**
     * @param stack the activation crystal stack
     * @return the tier of the activation crystal (weak: 1; awakened: 2; creative: 3)
     */
    public static int getActivationCrystalTier(@Nullable ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ActivationCrystal crystal) {
            return crystal.getCrystalLevel(stack);
        } else {
            return 0;
        }
    }

    /**
     * @param amount the amount of life essence to get
     * @return a FluidStack of life essence with the given amount, or null if the fluid is not registered.
     */
    @Nullable
    public static FluidStack getLifeEssence(int amount) {
        return FluidRegistry.getFluidStack("lifeessence", amount);
    }

    /**
     * Adds blood to the owner's network.
     *
     * @param ownerName the network owner name
     * @param lpAmount  the amount of blood
     * @return the amount of blood added to the network
     */
    public static int addBloodToNetwork(String ownerName, int lpAmount) {
        return SoulNetworkHandler
            .addCurrentEssenceToMaximum(ownerName, lpAmount, SoulNetworkHandler.getMaxEssence(ownerName));
    }

    /**
     * Adds blood to the network of blood orb's owner.
     *
     * @param orbStack the blood orb
     * @param lpAmount the amount of blood
     * @return the amount of blood added to the network
     */
    public static int addBloodToNetwork(ItemStack orbStack, int lpAmount) {
        var ownerName = getOrbOwnerName(orbStack);
        if (ownerName != null) {
            return addBloodToNetwork(ownerName, lpAmount);
        } else {
            return 0;
        }
    }
}
