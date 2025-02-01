package com.Nxer.TwistSpaceTechnology.system.ProcessingArrayBackend;

import static gregtech.api.GregTechAPI.MAXIMUM_METATILE_IDS;
import static gregtech.api.GregTechAPI.METATILEENTITIES;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class PAHelper {

    public static final Map<RecipeMap<?>, RecipeMap<?>> CONVERT_TO_NO_CELL = new HashMap<>();
    public static final Map<MTEBasicMachineWithRecipe, SoundResource> SOUND_RESOURCE = new HashMap<>();

    public static void initStatics() {
        CONVERT_TO_NO_CELL.put(RecipeMaps.mixerRecipes, GTPPRecipeMaps.mixerNonCellRecipes);
        CONVERT_TO_NO_CELL.put(RecipeMaps.centrifugeRecipes, GTPPRecipeMaps.centrifugeNonCellRecipes);
        CONVERT_TO_NO_CELL.put(RecipeMaps.electrolyzerRecipes, GTPPRecipeMaps.electrolyzerNonCellRecipes);

        try {
            for (IMetaTileEntity im : METATILEENTITIES) {
                if (im instanceof MTEBasicMachineWithRecipe mte) {
                    Field fSound = mte.getClass()
                        .getField("mSoundResourceLocation");
                    fSound.setAccessible(true);
                    ResourceLocation sound = (ResourceLocation) fSound.get(mte);
                    if (sound != null) {
                        SoundResource soundResource = SoundResource.get(sound.toString());
                        if (soundResource != null) {
                            SOUND_RESOURCE.put(mte, soundResource);
                        }
                    }
                }
            }
        } catch (Exception toLog) {
            TwistSpaceTechnology.LOG.info(toLog);
        }
    }

    public static @Nullable MTEBasicMachineWithRecipe getMTE(ItemStack itemStack) {
        if (null == itemStack || itemStack.stackSize < 1) return null;
        if (itemStack.getItem() != Item.getItemFromBlock(GregTechAPI.sBlockMachines)) return null;
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= MAXIMUM_METATILE_IDS) return null;
        IMetaTileEntity mte = METATILEENTITIES[meta];
        if (mte instanceof MTEBasicMachineWithRecipe m) {
            return m;
        } else {
            return null;
        }
    }

    public static @Nullable RecipeMap<?> getRecipeMapFromMTE(@Nullable MTEBasicMachineWithRecipe mte) {
        if (null == mte) return null;
        RecipeMap<?> r = mte.getRecipeMap();
        RecipeMap<?> t = CONVERT_TO_NO_CELL.get(r);
        return t != null ? t : r;
    }

    public static long getVoltageFromMTE(@Nullable MTEBasicMachineWithRecipe mte) {
        if (null == mte) return 0;
        return GTValues.V[mte.mTier] * mte.mAmperage;
    }

    public static @NotNull SoundResource getSoundFromMTE(@Nullable MTEBasicMachineWithRecipe mte) {
        if (null == mte) return SoundResource.NONE;
        SoundResource soundResource = SOUND_RESOURCE.get(mte);
        return soundResource == null ? SoundResource.NONE : soundResource;
    }

}
