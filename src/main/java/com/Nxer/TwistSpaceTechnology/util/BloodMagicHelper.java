package com.Nxer.TwistSpaceTechnology.util;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.common.items.ActivationCrystal;
import WayofTime.alchemicalWizardry.common.items.BloodShard;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BloodMagicHelper {

    /**
     * @param stack the ItemStack of Blood Orb
     * @return the owner name of the orb. {@code null} if the item is not valid or nbt tag is not valid.
     */
    @Nullable
    public static String getOrbOwnerName(@Nullable ItemStack stack) {
        if(stack == null || !(stack.getItem() instanceof IBloodOrb)) {
            return null;
        }

        var nbtTag = stack.getTagCompound();
        if(nbtTag == null) return null;

        var ownerName = nbtTag.getString("ownerName");
        return ownerName.isEmpty() ? null : ownerName;
    }

    /**
     *
     * @param stack the blood orb stack
     * @return the tier of the blood orb (from 1 to 6)
     */
    public static int getBloodOrbTier(@Nullable ItemStack stack) {
        if(stack != null && stack.getItem() instanceof IBloodOrb orb) {
            return orb.getOrbLevel();
        } else {
            return 0;
        }
    }

    /**
     * @param stack the shard stack
     * @return the tier of the shard (weak: 1; demon: 2)
     */
    public static int getBloodShardTier(@Nullable ItemStack stack) {
        if(stack != null && stack.getItem() instanceof BloodShard shard) {
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
        if(stack != null && stack.getItem() instanceof ActivationCrystal crystal) {
            return crystal.getCrystalLevel(stack);
        } else {
            return 0;
        }
    }

}
