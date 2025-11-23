package com.Nxer.TwistSpaceTechnology.common.tile;

import java.util.LinkedHashMap;
import java.util.Map;

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

        if (inv == this.pattern && slot == 1) {
            final ItemStack is = inv.getStackInSlot(1);
            if (is != null && is.getItem() instanceof final ICraftingPatternItem craftingPatternItem) {
                final ICraftingPatternDetails details =
                    craftingPatternItem.getPatternForItem(is, this.getHost().getTile().getWorldObj());

                if (details != null) {

                    // Obtain the original input (provided by AE2)
                    final IAEItemStack[] inItems = details.getInputs();

                    //// Scan the inputs and convert them into crystallized Essentia when generating the pattern
                    ItemStack[] converted = convertEssentiaInputs(inItems);


                    // Clear the crafting slots and write in the converted items
                    for (int i = 0; i < this.crafting.getSizeInventory(); i++) {
                        this.crafting.setInventorySlotContents(i, null);
                    }

                    for (int i = 0; i < converted.length && i < this.crafting.getSizeInventory(); i++) {
                        if (converted[i] != null) {
                            this.crafting.setInventorySlotContents(i, converted[i]);
                        }
                    }
                    // Original logic: output slots and various flags, including inverted / combine / substitute
                    // Note: In practice, all infusion recipes cannot contain fluid substitutions
                    final IAEItemStack[] outItems = details.getOutputs();
                    int inputsCount = 0;
                    int outputCount = 0;

                    for (IAEItemStack in : inItems) if (in != null) inputsCount++;
                    for (IAEItemStack out : outItems) if (out != null) outputCount++;

                    this.setSubstitution(details.canSubstitute());
                    if (newStack != null) {
                        NBTTagCompound data = newStack.getTagCompound();
                        this.setCombineMode(data.getInteger("combine") == 1);
                        this.setBeSubstitute(details.canBeSubstitute());
                    }

                    this.setInverted(inputsCount <= 8 && outputCount > 8);
                    this.setActivePage(0);

                    for (int i = 0; i < this.output.getSizeInventory(); i++) {
                        this.output.setInventorySlotContents(i, null);
                    }

                    if (inverted) {
                        for (int i = 0; i < this.output.getSizeInventory() && i < outItems.length; i++) {
                            final IAEItemStack item = outItems[i];
                            if (item != null) {
                                if (item.getItem() instanceof ItemFluidDrop) {
                                    ItemStack packet = ItemFluidPacket
                                        .newStack(ItemFluidDrop.getFluidStack(item.getItemStack()));
                                    this.output.setInventorySlotContents(i, packet);
                                } else {
                                    this.output.setInventorySlotContents(i, item.getItemStack());
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < outItems.length && i < 8; i++) {
                            final IAEItemStack item = outItems[i];
                            if (item != null) {
                                ItemStack put = (item.getItem() instanceof ItemFluidDrop)
                                    ? ItemFluidPacket.newStack(ItemFluidDrop.getFluidStack(item.getItemStack()))
                                    : item.getItemStack();
                                this.output.setInventorySlotContents(i >= 4 ? 12 + i : i, put);
                            }
                        }
                    }
                }
            }
        }
        this.getHost().markForSave();
    }

    /**
     * Converts AE2 input items into crystallized essentia for crafting patterns.
     * - Preserves the original order of non-essentia items at the beginning.
     * - Accumulates identical aspects from multiple items to avoid duplicate crystal entries.
     * - Ensures that the total number of output ItemStacks does not exceed the original input array size.
     */
    private ItemStack[] convertEssentiaInputs(IAEItemStack[] inputItems) {
        ItemStack[] result = new ItemStack[inputItems.length];
        Map<Aspect, Integer> tempCrystals = new LinkedHashMap<>();
        int insertPos = 0;
        for (int i = 0; i < inputItems.length; i++) {
            IAEItemStack ae = inputItems[i];
            if (ae == null) continue;

            ItemStack stack = ae.getItemStack();
            if (stack == null) continue;

            AspectList aspects = extractAspects(stack);

            if (aspects == null || aspects.size() == 0) {
                result[insertPos++] = stack;
                continue;
            }
            for (Aspect asp : aspects.getAspects()) {
                int amt = aspects.getAmount(asp);
                tempCrystals.put(asp, tempCrystals.getOrDefault(asp, 0) + amt);
            }
        }

        for (Map.Entry<Aspect, Integer> entry : tempCrystals.entrySet()) {
            if (insertPos >= result.length) break;
            result[insertPos++] = ItemEssentiaHelper.createCrystal(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private AspectList extractAspects(ItemStack stack) {
        if (stack == null) return null;

        AspectList result = new AspectList();

        // Supports TST's ItemEssence, which may contain multiple Aspects
        if (stack.getItem() instanceof ItemEssence) {
            try {
                AspectList al = ((ItemEssence) stack.getItem()).getAspects(stack);
                if (al != null && al.size() > 0) {
                    for (Aspect asp : al.getAspects()) {
                        int amt = al.getAmount(asp);
                        result.add(asp, amt);
                    }
                }
            } catch (Throwable ignored) {}
        }

        // Supports thaumcraft-nei-plugin's ItemAspect (This mod is actually not open source.)
        try {
            Class<?> itemAspectClass = Class.forName("com.djgiannuzz.thaumcraftneiplugin.items.ItemAspect");
            if (itemAspectClass.isInstance(stack.getItem())) {
                java.lang.reflect.Method method = itemAspectClass.getMethod("getAspects", ItemStack.class);
                method.setAccessible(true);
                AspectList al = (AspectList) method.invoke(null, stack);
                if (al != null && al.size() > 0) {
                    int stackSize = stack.stackSize;
                    for (Aspect asp : al.getAspects()) {
                        result.add(asp, stackSize);
                    }
                }
            }
        } catch (Throwable ignored) {}

        return result.size() > 0 ? result : null;
    }

    @Override
    public void setCraftingRecipe(final boolean craftingMode) {
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
