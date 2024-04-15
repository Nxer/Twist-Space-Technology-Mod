package com.Nxer.TwistSpaceTechnology.system.RecipePattern;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static gregtech.api.enums.GT_Values.W;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.glodblock.github.common.item.ItemFluidPacket;

import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.helpers.PatternHelper;
import appeng.util.item.AEItemStack;

public class CustomCraftRecipe implements IRecipeProvider {

    private static final Random random = new Random();
    // private boolean enabled = false;
    private final double[] itemPriority;
    private final double[] fluidPriority;
    private int rounds = 0;
    private String provider = NONEPROVIDER;
    private static final String NONEPROVIDER = "none";
    public IAEItemStack[] inputs = null, outputs = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomCraftRecipe that = (CustomCraftRecipe) o;
        return Arrays.equals(getItemPriority(), that.getItemPriority())
            && Arrays.equals(getFluidPriority(), that.getFluidPriority())
            && Objects.equals(getProvider(), that.getProvider())
            && Arrays.equals(getInputs(), that.getInputs())
            && Arrays.equals(getOutputs(), that.getOutputs());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getProvider());
        // result = 31 * result + Arrays.hashCode(getItemPriority());
        // result = 31 * result + Arrays.hashCode(getFluidPriority());
        result = 31 * result + rehashAEItemstacks(getInputs());
        // result = 31 * result + rehashAEItemstacks(getOutputs());
        return result;
    }

    public static int rehashAEItemstacks(IAEItemStack[] stack) {
        int result = 31;
        if (stack != null) {
            for (var item : stack) {
                result = 31 * result + rehashAEItemstack(item);
            }
        }
        return result;

    }

    public static int rehashAEItemstack(IAEItemStack itemstack) {
        return (int) (31 * 31
            * itemstack.getItemStack()
                .getUnlocalizedName()
                .hashCode()
            + 31 * itemstack.getStackSize()
            + itemstack.getItem()
                .getDamage(itemstack.getItemStack()));
    }

    public CustomCraftRecipe(ItemStack[] inputItem, FluidStack[] inputFluid, ItemStack[] outputItem,
        FluidStack[] outputFluid, double[] itemPriority, double[] fluidPriority, int rounds, String provider) {
        // this.enabled = enabled;
        this.provider = provider;
        if (inputItem == null) inputItem = new ItemStack[0];
        if (inputFluid == null) inputFluid = new FluidStack[0];
        if (outputItem == null) outputItem = new ItemStack[0];
        if (outputFluid == null) outputFluid = new FluidStack[0];
        setInputs(combineAnyStackList(inputFluid, inputItem));
        setOutputs(combineAnyStackList(outputFluid, outputItem));
        this.itemPriority = itemPriority;
        this.fluidPriority = fluidPriority;
        this.rounds = rounds;
        if (RecipeMatcher.DEBUG) {
            DEBUG_PRINT();
        }
    }

    public CustomCraftRecipe(IAEItemStack[] in, IAEItemStack[] out, double[] itemPriority, double[] fluidPriority,
        int rounds, String provider) {
        // this.enabled = enabled;
        setInputs(in);
        setOutputs(out);
        this.itemPriority = itemPriority;
        this.fluidPriority = fluidPriority;
        this.rounds = rounds;
        this.provider = provider;
    }

    public CustomCraftRecipe copy() {
        return new CustomCraftRecipe(inputs, outputs, itemPriority, fluidPriority, 0, provider);
    }

    public CustomCraftRecipe setRounds(int rounds) {
        this.rounds = rounds;
        return this;
    }

    private void DEBUG_PRINT() {
        LOG.info("--------------------------------------------");
        LOG.info("REGISTER CUSTOM RECIPE:");
        LOG.info("PROVIDER: " + provider);
        LOG.info("INPUT ITEMS:");
        if (inputs == null) LOG.info("NULL");
        else {
            for (var items : inputs) {
                LOG.info(
                    items.getItem()
                        .getUnlocalizedName() + "."
                        + items.getItem()
                            .getDamage(items.getItemStack())
                        + " "
                        + items.getStackSize());
            }
        }
        LOG.info("OUTPUT ITEMS:");
        if (outputs == null) LOG.info("NULL");
        else {
            for (var items : outputs) {
                LOG.info(
                    items.getItem()
                        .getUnlocalizedName() + "."
                        + items.getItem()
                            .getDamage(items.getItemStack())
                        + " "
                        + items.getStackSize());
            }
        }
        LOG.info("--------------------------------------------");
    }

    @Override
    public String provider() {
        return provider;
    }

    public static IAEItemStack[] combineAnyStackList(IAEItemStack[] a, IAEItemStack[] b) {
        if (a == null) a = new IAEItemStack[0];
        if (b == null) b = new IAEItemStack[0];
        IAEItemStack[] list = new IAEItemStack[a.length + b.length];
        int i = 0;
        for (var stack : a) {
            list[i] = stack;
            i++;
        }
        for (var stack : b) {
            list[i] = stack;
            i++;
        }
        return list;
    }

    public static ArrayList<ItemStack> mergeItemstacks(ItemStack[] stack) {
        ArrayList<ItemStack> result = new ArrayList<>();
        for (var item : stack) {
            if (item == null || item.getItem() == null || item.stackSize == 0) continue;
            boolean flag = false;
            for (int i = 0; i < result.size(); i++) {
                ItemStack inv = result.get(i)
                    .copy();
                if (areStacksEqual(inv, item, true)) {
                    inv.stackSize += item.stackSize;
                    result.set(i, inv);
                    flag = true;
                }
            }
            if (!flag) result.add(item);
        }
        return result;
    }

    public static Object[] mergeItemstacks(Object[] stack) {
        ArrayList<Object> result = new ArrayList<>();
        for (var it : stack) {
            if (it instanceof ItemStack item) {
                if (item.getItem() == null || item.stackSize == 0) continue;
                boolean flag = false;
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i) instanceof ItemStack inv) {
                        if (areStacksEqual(inv, item, true)) {
                            inv.stackSize += item.stackSize;
                            result.set(i, inv);
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) result.add(item.copy());
            } else if (it instanceof ArrayList<?>itemStacks) {
                LOG.info("GET ORE DICTIONARY INPUT: " + itemStacks.get(0));
                ItemStack[] warped = new ItemStack[itemStacks.size()];
                for (int i = 0; i < itemStacks.size(); i++) {
                    warped[i] = (ItemStack) itemStacks.get(i);
                }
                // ItemStack[] oredicted = itemStacks.stream()
                // .map((vv) -> (ItemStack) vv)
                // .toArray(ItemStack[]::new);
                // if (oredicted.length == 0 || oredicted[0] == null) continue;

                result.add(warped);
            } else if (it != null) result.add(it);
        }
        if (result.isEmpty()) return null;
        return result.stream()
            .filter(Objects::nonNull)
            .toArray();
    }

    public static boolean areStacksEqual(ItemStack aStack1, ItemStack aStack2, boolean aIgnoreNBT) {
        return aStack1 != null && aStack2 != null
            && aStack1.getItem() == aStack2.getItem()
            && (aIgnoreNBT || (((aStack1.getTagCompound() == null) == (aStack2.getTagCompound() == null))
                && (aStack1.getTagCompound() == null || aStack1.getTagCompound()
                    .equals(aStack2.getTagCompound()))))
            && (Items.feather.getDamage(aStack1) == Items.feather.getDamage(aStack2)
                || Items.feather.getDamage(aStack1) == W
                || Items.feather.getDamage(aStack2) == W);
    }

    public static IAEItemStack[] combineAnyStackList(IAEItemStack[] a, ItemStack[] b) {
        return combineAnyStackList(
            a,
            Arrays.stream(b)
                .map(AEItemStack::create)
                .toArray(IAEItemStack[]::new));
    }

    public static IAEItemStack[] combineAnyStackList(IAEItemStack[] a, FluidStack[] b) {
        return combineAnyStackList(
            a,
            Arrays.stream(b)
                .map((stack) -> AEItemStack.create(ItemFluidPacket.newStack(stack)))
                .toArray(IAEItemStack[]::new));
    }

    public static IAEItemStack[] combineAnyStackList(ItemStack[] a, ItemStack[] b) {
        return combineAnyStackList(
            Arrays.stream(a)
                .map(AEItemStack::create)
                .toArray(IAEItemStack[]::new),
            Arrays.stream(b)
                .map(AEItemStack::create)
                .toArray(IAEItemStack[]::new));
    }

    public static IAEItemStack[] combineAnyStackList(FluidStack[] a, FluidStack[] b) {
        return combineAnyStackList(
            Arrays.stream(a)
                .map((stack) -> AEItemStack.create(ItemFluidPacket.newStack(stack)))
                .toArray(IAEItemStack[]::new),
            Arrays.stream(b)
                .map((stack) -> AEItemStack.create(ItemFluidPacket.newStack(stack)))
                .toArray(IAEItemStack[]::new));
    }

    public static IAEItemStack[] combineAnyStackList(ItemStack[] a, FluidStack[] b) {
        return combineAnyStackList(
            Arrays.stream(a)
                .map(AEItemStack::create)
                .toArray(IAEItemStack[]::new),
            Arrays.stream(b)
                .map((stack) -> AEItemStack.create(ItemFluidPacket.newStack(stack)))
                .toArray(IAEItemStack[]::new));
    }

    public static IAEItemStack[] combineAnyStackList(FluidStack[] a, ItemStack[] b) {
        return combineAnyStackList(b, a);
    }

    public static FluidStack convertAEItemToFluid(IAEItemStack stack) {
        ItemStack fluidPacket = stack.getItemStack();
        return FluidStack.loadFluidStackFromNBT(fluidPacket.getTagCompound());
    }

    public boolean setInputs(IAEItemStack[] stack) {
        if (stack.length == 0) {
            return false;
        }
        this.inputs = Arrays.stream(stack)
            .filter(Objects::nonNull)
            .sorted(RecipeMatcher::compareAEItemStack)
            .toArray(IAEItemStack[]::new);
        return true;
    }

    public boolean setOutputs(IAEItemStack[] stack) {
        IAEItemStack[] condensed = PatternHelper.convertToCondensedList(stack);
        if (condensed.length == 0) {
            return false;
        }
        this.outputs = Arrays.stream(stack)
            .filter(Objects::nonNull)
            .sorted(RecipeMatcher::compareAEItemStack)
            .toArray(IAEItemStack[]::new);
        return true;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public ItemStack[] getItemInput() {
        return Arrays.stream(inputs)
            .filter(IAEStack::isItem)
            .map(IAEItemStack::getItemStack)
            .toArray(ItemStack[]::new);
    }

    @Override
    public FluidStack[] getFluidInput() {
        return Arrays.stream(inputs)
            .filter(IAEStack::isFluid)
            .map(CustomCraftRecipe::convertAEItemToFluid)
            .toArray(FluidStack[]::new);
    }

    @Override
    public ItemStack[] getItemOutput() {
        return Arrays.stream(outputs)
            .filter(IAEStack::isItem)
            .map(IAEItemStack::getItemStack)
            .toArray(ItemStack[]::new);
    }

    @Override
    public FluidStack[] getFluidOutput() {
        return Arrays.stream(outputs)
            .filter(IAEStack::isFluid)
            .map(CustomCraftRecipe::convertAEItemToFluid)
            .toArray(FluidStack[]::new);
    }

    @Override
    public long getRecipeProcessRounds() {
        return rounds;
    }

    @Override
    public double[] getItemOutputPriority() {
        return itemPriority;
    }

    @Override
    public double[] getFluidOutputPriority() {
        return fluidPriority;
    }

    @Override
    public ItemStack[] getRealItemOutput() {
        ArrayList<ItemStack> result = new ArrayList<>();
        ItemStack[] OutputItem = getItemOutput();
        for (int i = 0; i < OutputItem.length; i++) {
            if (random.nextInt(100) / 100.0 < itemPriority[i]) {
                long outputs = (long) rounds * OutputItem[i].stackSize;
                while (outputs >= Integer.MAX_VALUE) {
                    result.add(new ItemStack(OutputItem[i].getItem(), Integer.MAX_VALUE));
                    outputs -= Integer.MAX_VALUE;
                }
                if (outputs != 0) {
                    result.add(new ItemStack(OutputItem[i].getItem(), (int) outputs));
                }
            }
        }
        return result.toArray(new ItemStack[] {});
    }

    @Override
    public FluidStack[] getRealFluidOutput() {
        ArrayList<FluidStack> result = new ArrayList<>();
        FluidStack[] OutputFluid = getFluidOutput();
        for (int i = 0; i < OutputFluid.length; i++) {
            if (random.nextInt(100) / 100.0 < itemPriority[i]) {
                long outputs = (long) rounds * OutputFluid[i].amount;
                while (outputs >= Integer.MAX_VALUE) {
                    result.add(new FluidStack(OutputFluid[i].getFluid(), Integer.MAX_VALUE));
                    outputs -= Integer.MAX_VALUE;
                }
                if (outputs != 0) {
                    result.add(new FluidStack(OutputFluid[i].getFluid(), (int) outputs));
                }
            }
        }
        return result.toArray(new FluidStack[] {});
    }

    public IAEItemStack[] getRealAEOutput() {
        ArrayList<IAEItemStack> result = new ArrayList<>();
        for (int i = 0; i < outputs.length; i++) {
            if (random.nextInt(100) / 100.0 < itemPriority[i]) {
                long output = (long) rounds * outputs[i].getStackSize();
                while (output >= Integer.MAX_VALUE) {
                    result.add(
                        outputs[i].copy()
                            .setStackSize(Integer.MAX_VALUE));
                    output -= Integer.MAX_VALUE;
                }
                if (output != 0) {
                    result.add(
                        outputs[i].copy()
                            .setStackSize(output));
                }
            }
        }
        return result.toArray(new IAEItemStack[] {});
    }

    @Override
    public boolean matchRecipe(Object o) {
        return false;
    }

    @Override
    public void initRecipe() {

    }

    public double[] getItemPriority() {
        return itemPriority;
    }

    public double[] getFluidPriority() {
        return fluidPriority;
    }

    public String getProvider() {
        return provider;
    }

    public IAEItemStack[] getInputs() {
        return inputs;
    }

    public IAEItemStack[] getOutputs() {
        return outputs;
    }

}
