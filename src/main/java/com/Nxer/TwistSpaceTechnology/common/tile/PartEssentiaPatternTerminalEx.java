package com.Nxer.TwistSpaceTechnology.common.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper;
import com.glodblock.github.client.textures.FCPartsTexture;
import com.glodblock.github.common.item.ItemFluidDrop;
import com.glodblock.github.common.item.ItemFluidPacket;
import com.glodblock.github.common.parts.PartFluidPatternTerminalEx;
import com.glodblock.github.inventory.gui.GuiType;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import appeng.tile.inventory.BiggerAppEngInventory;
import appeng.tile.inventory.InvOperation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.ItemEssence;

public class PartEssentiaPatternTerminalEx extends PartFluidPatternTerminalEx {

    private static final FCPartsTexture FRONT_BRIGHT_ICON = FCPartsTexture.PartFluidPatternTerminal_Bright;
    private static final FCPartsTexture FRONT_DARK_ICON = FCPartsTexture.PartFluidPatternTerminal_Colored;
    private static final FCPartsTexture FRONT_COLORED_ICON = FCPartsTexture.PartFluidPatternTerminal_Dark;

    // Cache reflection result to avoid performance overhead from repeated Class.forName calls
    private static Class<?> NEI_ITEM_ASPECT_CLASS;
    static {
        try {
            NEI_ITEM_ASPECT_CLASS = Class.forName("com.djgiannuzz.thaumcraftneiplugin.items.ItemAspect");
        } catch (ClassNotFoundException ignored) {}
    }

    // Guard flag to prevent infinite recursion during inventory updates
    private boolean isUpdating = false;

    public PartEssentiaPatternTerminalEx(ItemStack is) {
        super(is);
        this.crafting = new BiggerAppEngInventory(this, 32);
        this.output = new BiggerAppEngInventory(this, 32);
        this.craftingMode = false;
    }

    @Override
    public GuiType getGui() {
        return GuiType.FLUID_PATTERN_TERMINAL_EX;
    }

    @Override
    public void onChangeInventory(final IInventory inv, final int slot, final InvOperation mc,
        final ItemStack removedStack, final ItemStack newStack) {
        if (isUpdating) return;

        // CORE FIX: Detect changes in the crafting grid (e.g., NEI recipe ghost-fill)
        // and immediately convert virtual aspect items into crystallized essentia.
        if (inv == this.crafting) {
            if (newStack != null) {
                checkAndConvertSlot(slot);
            }
        }

        // Handle pattern encoding/decoding logic when a pattern is placed in the slot
        if (inv == this.pattern && slot == 1) {
            final ItemStack is = inv.getStackInSlot(1);
            if (is != null && is.getItem() instanceof ICraftingPatternItem) {
                ICraftingPatternItem craftingPatternItem = (ICraftingPatternItem) is.getItem();
                final ICraftingPatternDetails details = craftingPatternItem.getPatternForItem(
                    is,
                    this.getHost()
                        .getTile()
                        .getWorldObj());

                if (details != null) {
                    this.isUpdating = true;
                    try {
                        updateGridFromPattern(details, is);
                    } finally {
                        this.isUpdating = false;
                    }
                }
            }
        }
        this.getHost()
            .markForSave();
    }

    /**
     * Inspects a specific slot and converts any detected essentia-providing items
     * into actual Crystallized Essentia items.
     */
    private void checkAndConvertSlot(int slot) {
        if (slot < 0 || slot >= this.crafting.getSizeInventory()) return;

        ItemStack stack = this.crafting.getStackInSlot(slot);
        AspectList aspects = extractAspects(stack);

        if (aspects != null && aspects.size() > 0) {
            this.isUpdating = true; // Lock to prevent setInventorySlotContents from re-triggering this method
            try {
                // NEI aspect items usually represent a single aspect type
                Aspect asp = aspects.getAspects()[0];
                int amount = aspects.getAmount(asp) * stack.stackSize;
                this.crafting.setInventorySlotContents(slot, ItemEssentiaHelper.createCrystal(asp, amount));
            } finally {
                this.isUpdating = false;
            }
        }
    }

    /**
     * Synchronizes the GUI grids with the data stored inside an encoded pattern.
     */
    private void updateGridFromPattern(ICraftingPatternDetails details, ItemStack patternStack) {
        // Clear existing grid contents
        for (int i = 0; i < this.crafting.getSizeInventory(); i++) {
            this.crafting.setInventorySlotContents(i, null);
        }

        IAEItemStack[] inItems = details.getInputs();
        List<ItemStack> convertedInputs = new ArrayList<>();

        // Process inputs: Convert aspects and preserve regular items
        for (IAEItemStack ae : inItems) {
            if (ae == null) continue;
            ItemStack is = ae.getItemStack();
            AspectList al = extractAspects(is);
            if (al != null && al.size() > 0) {
                for (Aspect asp : al.getAspects()) {
                    convertedInputs.add(ItemEssentiaHelper.createCrystal(asp, al.getAmount(asp)));
                }
            } else {
                convertedInputs.add(is);
            }
        }

        // Populate crafting grid
        for (int i = 0; i < convertedInputs.size() && i < this.crafting.getSizeInventory(); i++) {
            this.crafting.setInventorySlotContents(i, convertedInputs.get(i));
        }

        // Process output logic and pattern flags (substitution, combine mode, etc.)
        IAEItemStack[] outItems = details.getOutputs();
        this.setSubstitution(details.canSubstitute());

        if (patternStack != null && patternStack.hasTagCompound()) {
            NBTTagCompound tag = patternStack.getTagCompound();
            this.setCombineMode(tag.getInteger("combine") == 1);
            this.setBeSubstitute(details.canBeSubstitute());
        }

        // Clear and populate output grid
        for (int i = 0; i < this.output.getSizeInventory(); i++) {
            this.output.setInventorySlotContents(i, null);
        }

        for (int i = 0; i < outItems.length && i < this.output.getSizeInventory(); i++) {
            IAEItemStack item = outItems[i];
            if (item != null) {
                ItemStack outStack = item.getItemStack();
                // Convert FluidDrops to FluidPackets for better UI representation
                if (outStack.getItem() instanceof ItemFluidDrop) {
                    outStack = ItemFluidPacket.newStack(ItemFluidDrop.getFluidStack(outStack));
                }
                this.output.setInventorySlotContents(i, outStack);
            }
        }
    }

    /**
     * Utility method to extract Thaumcraft Aspects from various item types (TST Essence or NEI Plugin).
     */
    private AspectList extractAspects(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;
        AspectList result = new AspectList();

        // 1. Support for TwistSpaceTechnology's ItemEssence
        if (stack.getItem() instanceof ItemEssence) {
            try {
                AspectList al = ((ItemEssence) stack.getItem()).getAspects(stack);
                if (al != null) {
                    for (Aspect asp : al.getAspects()) {
                        result.add(asp, al.getAmount(asp));
                    }
                }
            } catch (Exception ignored) {}
        }

        // 2. Support for thaumcraft-nei-plugin's ItemAspect via reflection
        if (NEI_ITEM_ASPECT_CLASS != null && NEI_ITEM_ASPECT_CLASS.isInstance(stack.getItem())) {
            try {
                java.lang.reflect.Method method = NEI_ITEM_ASPECT_CLASS.getMethod("getAspects", ItemStack.class);
                AspectList al = (AspectList) method.invoke(null, stack);
                if (al != null) {
                    for (Aspect asp : al.getAspects()) {
                        result.add(asp, stack.stackSize);
                    }
                }
            } catch (Exception ignored) {}
        }

        return result.size() > 0 ? result : null;
    }

    @Override
    public void setCraftingRecipe(boolean craftingMode) {
        this.craftingMode = false;
    }

    @Override
    public boolean isCraftingRecipe() {
        return false;
    }

    @Override
    public FCPartsTexture getFrontBright() {
        return FRONT_BRIGHT_ICON;
    }

    @Override
    public FCPartsTexture getFrontColored() {
        return FRONT_COLORED_ICON;
    }

    @Override
    public FCPartsTexture getFrontDark() {
        return FRONT_DARK_ICON;
    }

    @Override
    public boolean isLightSource() {
        return false;
    }
}
