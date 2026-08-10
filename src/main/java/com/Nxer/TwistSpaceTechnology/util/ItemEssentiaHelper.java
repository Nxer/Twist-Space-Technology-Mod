package com.Nxer.TwistSpaceTechnology.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.gtnewhorizons.aspectrecipeindex.ModItems;
import com.gtnewhorizons.aspectrecipeindex.common.items.ItemAspect;

import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumicenergistics.common.storage.AEEssentiaStack;

public class ItemEssentiaHelper {

    // ========================================================
    // ItemAspect Creation (NEI display)
    // ========================================================

    /**
     * Create an {@link ItemAspect} carrying the given {@link Aspect}.<br>
     * Used for NEI display of essentia requirements in recipe maps.
     *
     * @param aspect The aspect to encode.
     * @param amount The stack size.
     * @return The ItemAspect ItemStack, or null if aspect is null or amount <= 0.
     */
    @Nullable
    public static ItemStack createAspectItem(Aspect aspect, int amount) {
        if (aspect == null || amount <= 0) return null;
        ItemStack stack = new ItemStack(ModItems.itemAspect, amount);
        ItemAspect.setAspect(stack, aspect);
        return stack;
    }

    /**
     * Convenience: create an array of {@link ItemAspect} from an {@link AspectList}.<br>
     * One entry per aspect in the list.
     *
     * @param aspectList The aspect list to convert.
     * @return Array of ItemAspect ItemStacks, or an empty array if null or empty.
     */
    public static ItemStack[] createAspectItems(AspectList aspectList) {
        if (aspectList == null || aspectList.size() == 0) return new ItemStack[0];
        ItemStack[] result = new ItemStack[aspectList.size()];
        int i = 0;
        for (Aspect asp : aspectList.getAspects()) {
            result[i++] = createAspectItem(asp, aspectList.getAmount(asp));
        }
        return result;
    }

    // ========================================================
    // CrystalEssence Creation (MC item stack)
    // ========================================================

    /**
     * Create a single {@code ItemCrystalEssence} carrying the given {@link Aspect}.
     *
     * @param asp The aspect to put on the crystal.
     * @return The CrystalEssence ItemStack.
     */
    public static ItemStack createCrystal(Aspect asp) {
        ItemStack stack = new ItemStack(thaumcraft.common.config.ConfigItems.itemCrystalEssence, 1);
        AspectList aspects = new AspectList().add(asp, 1);
        NBTTagCompound tag = new NBTTagCompound();
        aspects.writeToNBT(tag);
        stack.setTagCompound(tag);
        return stack;
    }

    /**
     * Create {@code count} {@code ItemCrystalEssence} carrying the given {@link Aspect}.
     *
     * @param asp   The aspect to put on the crystal.
     * @param count The stack size.
     * @return The CrystalEssence ItemStack.
     */
    public static ItemStack createCrystal(Aspect asp, int count) {
        ItemStack stack = createCrystal(asp);
        stack.stackSize = count;
        return stack;
    }

    // ========================================================
    // Aspect Reading from ItemStacks
    // ========================================================

    /**
     * Read the <b>first</b> {@link Aspect} from a {@code ItemCrystalEssence}
     * or any custom tagged ItemStack.
     *
     * @param stack The item to read from.
     * @return The first aspect found, or null if none.
     */
    @Nullable
    public static Aspect readAspectFromCrystal(@Nullable ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) return null;

        // ItemAspect-style single-key format
        if (stack.hasTagCompound() && stack.getTagCompound()
            .hasKey("aspect")) {
            Aspect aspect = Aspect.getAspect(
                stack.getTagCompound()
                    .getString("aspect"));
            if (aspect != null) return aspect;
        }

        // TC CrystalEssence / glass ampoule: parse the full list and take the first
        AspectList aspects = readAllAspectsFromCrystal(stack);
        if (aspects != null && aspects.size() > 0) {
            return aspects.getAspects()[0];
        }
        return null;
    }

    /**
     * Read <b>all</b> {@link Aspect}s from a {@code ItemCrystalEssence} or glass ampoule.
     * <p>
     * Quantities are scaled by {@code stack.stackSize}:<br>
     * 1 bottle with {@code aer:2, ignis:3} → 2 bottles return {@code aer:4, ignis:6}.
     *
     * @param stack The item to read from.
     * @return The full AspectList with quantities scaled, or null if none.
     */
    @Nullable
    public static AspectList readAllAspectsFromCrystal(@Nullable ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) return null;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return null;

        AspectList raw = new AspectList();
        raw.readFromNBT(tag);
        if (raw.size() == 0) return null;

        AspectList scaled = new AspectList();
        for (Aspect asp : raw.getAspects()) {
            int perUnit = raw.getAmount(asp);
            long total = (long) perUnit * stack.stackSize;
            scaled.add(asp, (int) Math.min(total, Integer.MAX_VALUE));
        }
        return scaled;
    }

    // ========================================================
    // AE Network Conversion (CrystalEssence <-> AEEssentiaStack)
    // ========================================================

    /**
     * Convert a {@code ItemCrystalEssence} (or glass ampoule) to a native
     * {@link AEEssentiaStack} for ME network storage.
     *
     * @param stack The item to convert.
     * @return An AEEssentiaStack, or null if the item has no aspect.
     */
    @Nullable
    public static AEEssentiaStack getAeEssentiaStack(@Nullable ItemStack stack) {
        Aspect asp = readAspectFromCrystal(stack);
        if (asp == null) return null;
        long amount = stack.stackSize;
        if (amount <= 0) return null;
        return new AEEssentiaStack(asp, amount);
    }

    /**
     * Overload accepting an {@link IAEItemStack} wrapping a {@code ItemCrystalEssence}.
     *
     * @param stack The AE item stack to convert.
     * @return An AEEssentiaStack, or null if the item has no aspect.
     */
    @Nullable
    public static AEEssentiaStack getAeEssentiaStack(@Nullable IAEItemStack stack) {
        if (stack == null) return null;
        return getAeEssentiaStack(stack.getItemStack());
    }

    /**
     * Convert a native {@link AEEssentiaStack} back to a {@code ItemCrystalEssence}
     * wrapped in an {@link IAEItemStack}.
     *
     * @param essentia The AE essentia stack to convert.
     * @return An IAEItemStack wrapping a CrystalEssence, or null if essentia is null.
     */
    @Nullable
    public static IAEItemStack newAeItemStack(@Nullable AEEssentiaStack essentia) {
        if (essentia == null || essentia.getAspect() == null) return null;

        ItemStack crystal = createCrystal(essentia.getAspect());

        long stackSize = essentia.getStackSize();
        if (stackSize <= 0) stackSize = 1;
        if (stackSize > Integer.MAX_VALUE) stackSize = Integer.MAX_VALUE;

        IAEItemStack aeStack = AEItemStack.create(crystal);
        aeStack.setStackSize(stackSize);
        return aeStack;
    }

    // ========================================================
    // Glass Ampoule / CrystalEssence Detection
    // ========================================================

    /**
     * Check whether the given {@link ItemStack} is a glass ampoule
     * ({@code ItemEssence} damage=1).<br>
     * Ampoules carry {@link Aspect} data in NBT and are converted by default.
     *
     * @param stack The item to check.
     * @return True if the item is a glass ampoule.
     */
    public static boolean isGlassAmpoule(ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) return false;
        return stack.getItem() instanceof thaumcraft.common.items.ItemEssence && stack.getItemDamage() == 1;
    }

    /**
     * Check whether the given {@link ItemStack} is a {@code ItemCrystalEssence}.<br>
     * Crystal essences also carry {@link Aspect} data, but their conversion is optional
     * (off by default) because they are actual items rather than empty containers.
     *
     * @param stack The item to check.
     * @return True if the item is a crystal essence.
     */
    public static boolean isCrystalEssence(ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) return false;
        return stack.getItem() instanceof thaumcraft.common.items.ItemCrystalEssence;
    }

    // ========================================================
    // Glass Ampoule -> ItemAspect Conversion
    // ========================================================

    /**
     * Convert a glass ampoule or {@code ItemCrystalEssence} into an array of
     * {@link ItemAspect} items, one per aspect type.
     * <p>
     * A multi-aspect bottle (e.g. {@code aer:2 + ignis:3}) yields multiple
     * {@code ItemAspect} entries.
     *
     * @param stack The item to convert.
     * @return An array of ItemAspect ItemStacks, or an empty array if conversion fails.
     */
    public static ItemStack[] convertGlassAmpouleToAspectItems(ItemStack stack) {
        AspectList aspects = readAllAspectsFromCrystal(stack);
        if (aspects == null || aspects.size() == 0) return new ItemStack[0];

        ItemStack[] result = new ItemStack[aspects.size()];
        int i = 0;
        for (Aspect asp : aspects.getAspects()) {
            result[i++] = createAspectItem(asp, aspects.getAmount(asp));
        }
        return result;
    }

    /**
     * Convenience: convert a glass ampoule or {@code ItemCrystalEssence} into
     * a single {@link ItemAspect} (first aspect only).
     * <p>
     * For multi-aspect bottles, prefer {@link #convertGlassAmpouleToAspectItems}.
     *
     * @param stack The item to convert.
     * @return An ItemAspect ItemStack, or null if conversion fails.
     */
    @Nullable
    public static ItemStack convertGlassAmpouleToAspectItem(ItemStack stack) {
        ItemStack[] items = convertGlassAmpouleToAspectItems(stack);
        return items.length > 0 ? items[0] : null;
    }

    // ========================================================
    // Pattern NBT Conversion (for Mixin)
    // ========================================================

    /**
     * Convert a plain {@link ItemStack} into AE2's generic IAEStack NBT format
     * ({@code StackType}, {@code Cnt}, {@code Req}, {@code Craft}).
     * <p>
     * This is necessary because AE2's internal NBT format differs from Minecraft's
     * standard {@code ItemStack.writeToNBT()} format.
     */
    private static NBTTagCompound toAeGenericNbt(ItemStack stack) {
        IAEItemStack aeStack = AEItemStack.create(stack);
        aeStack.setStackSize(stack.stackSize);
        NBTTagCompound tag = new NBTTagCompound();
        aeStack.writeToNBT(tag);
        return tag;
    }

    /**
     * Scan a pattern's NBT and replace glass ampoules and/or {@code ItemCrystalEssence}
     * entries in both {@code "in"} and {@code "out"} lists with {@link ItemAspect}.
     * The two conversions are independent and gated separately.
     * <p>
     * This is the public entry point called by the Mixin.
     *
     * @param tag                   The pattern's NBTTagCompound.
     * @param convertGlassAmpoule   Whether to convert glass ampoules.
     * @param convertCrystalEssence Whether to convert crystal essences.
     * @return True if any replacement was made.
     */
    public static boolean convertPatternNBT(NBTTagCompound tag, boolean convertGlassAmpoule,
        boolean convertCrystalEssence) {
        boolean modified = false;
        if (convertPatternNBTList(tag, "in", convertGlassAmpoule, convertCrystalEssence)) modified = true;
        if (convertPatternNBTList(tag, "out", convertGlassAmpoule, convertCrystalEssence)) modified = true;
        return modified;
    }

    /**
     * Two-pass scan of a single NBT list ({@code "in"} or {@code "out"}):
     * <ol>
     * <li>Identify glass ampoules / crystal essences, split into {@link ItemAspect}s.
     * The first {@code ItemAspect} occupies the original slot; extras go to overflow.</li>
     * <li>Fill empty slots ({@code {}}) with overflow entries.
     * If slots run out, remaining entries are appended to the list end
     * (AE2's {@code getInputs()} iterates the full list, no length limit).</li>
     * </ol>
     *
     * @param tag                   The parent NBTTagCompound.
     * @param key                   The list key, either {@code "in"} or {@code "out"}.
     * @param convertGlassAmpoule   Whether to convert glass ampoules.
     * @param convertCrystalEssence Whether to convert crystal essences.
     * @return True if any replacement was made.
     */
    private static boolean convertPatternNBTList(NBTTagCompound tag, String key, boolean convertGlassAmpoule,
        boolean convertCrystalEssence) {
        if (!tag.hasKey(key)) return false;

        NBTTagList list = tag.getTagList(key, net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        int size = list.tagCount();
        if (size == 0) return false;

        // null = empty slot, available for overflow
        NBTTagCompound[] slots = new NBTTagCompound[size];
        java.util.List<NBTTagCompound> overflow = new java.util.ArrayList<>();
        boolean modified = false;

        // Pass 1: identify and split
        for (int i = 0; i < size; i++) {
            NBTTagCompound itemTag = list.getCompoundTagAt(i);
            if (itemTag.hasNoTags()) {
                slots[i] = null; // empty slot
                continue;
            }

            ItemStack mcStack = ItemStack.loadItemStackFromNBT(itemTag);
            if (mcStack == null || mcStack.getItem() == null) {
                slots[i] = itemTag;
                continue;
            }

            // AE2 IAEStack: Count is always 0b, real count is in Cnt (Long)
            long realCount = itemTag.hasKey("Cnt") ? itemTag.getLong("Cnt") : 1L;
            mcStack.stackSize = (int) Math.max(1L, Math.min(realCount, Integer.MAX_VALUE));

            if (!((isGlassAmpoule(mcStack) && convertGlassAmpoule)
                || (isCrystalEssence(mcStack) && convertCrystalEssence))) {
                slots[i] = itemTag;
                continue;
            }

            ItemStack[] aspectItems = convertGlassAmpouleToAspectItems(mcStack);
            if (aspectItems.length == 0) {
                slots[i] = itemTag;
                continue;
            }

            // First aspect occupies the original slot
            slots[i] = toAeGenericNbt(aspectItems[0]);
            modified = true;

            // Remaining aspects need extra slots
            for (int k = 1; k < aspectItems.length; k++) {
                overflow.add(toAeGenericNbt(aspectItems[k]));
            }
        }

        // Pass 2: fill empty slots with overflow
        int overflowPlaced = 0;
        for (int i = 0; i < size && overflowPlaced < overflow.size(); i++) {
            if (slots[i] == null) {
                slots[i] = overflow.get(overflowPlaced++);
            }
        }

        if (!modified) return false;

        NBTTagList newList = new NBTTagList();
        for (int i = 0; i < size; i++) {
            newList.appendTag(slots[i] != null ? slots[i] : new NBTTagCompound());
        }
        // Overflow entries beyond empty slots: append to end (AE2 iterates full list, no length limit)
        for (int k = overflowPlaced; k < overflow.size(); k++) {
            newList.appendTag(overflow.get(k));
        }
        tag.setTag(key, newList);
        return true;
    }

    // ========================================================
    // Aspect Combination
    // ========================================================

    /**
     * Given two input {@link Aspect}s, return the compound Aspect synthesised from
     * them (if any). For example: {@code Aer + Ignis = Potentia}.
     *
     * @param aspectA First input aspect.
     * @param aspectB Second input aspect.
     * @return The combined aspect, or null if no combination exists.
     */
    @Nullable
    public static Aspect findCombinedAspect(Aspect aspectA, Aspect aspectB) {
        for (Aspect candidate : Aspect.aspects.values()) {
            Aspect[] comps = candidate.getComponents();
            if (comps != null && comps.length == 2) {
                if ((comps[0] == aspectA && comps[1] == aspectB) || (comps[0] == aspectB && comps[1] == aspectA)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    private static final Map<Aspect, Map<Aspect, Aspect>> COMBO_CACHE = new HashMap<>();

    /**
     * Cached version of {@link #findCombinedAspect}. Results are stored in a
     * bidirectional map for O(1) lookups on repeated calls.
     *
     * @param a First input aspect.
     * @param b Second input aspect.
     * @return The combined aspect, or null if no combination exists.
     */
    @Nullable
    public static Aspect findCombinedAspectCached(Aspect a, Aspect b) {
        Map<Aspect, Aspect> inner = COMBO_CACHE.get(a);
        if (inner != null && inner.containsKey(b)) {
            return inner.get(b);
        }
        inner = COMBO_CACHE.get(b);
        if (inner != null && inner.containsKey(a)) {
            return inner.get(a);
        }
        Aspect result = findCombinedAspect(a, b);
        if (result != null) {
            COMBO_CACHE.computeIfAbsent(a, k -> new HashMap<>())
                .put(b, result);
            COMBO_CACHE.computeIfAbsent(b, k -> new HashMap<>())
                .put(a, result);
        }
        return result;
    }
}
