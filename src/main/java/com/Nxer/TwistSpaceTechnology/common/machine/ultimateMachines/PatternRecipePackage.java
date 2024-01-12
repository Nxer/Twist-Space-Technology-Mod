// package com.Nxer.TwistSpaceTechnology.common.machine.ultimateMachines;
//
// import appeng.api.AEApi;
// import appeng.api.implementations.ICraftingPatternItem;
// import appeng.api.networking.crafting.ICraftingPatternDetails;
// import appeng.api.networking.security.BaseActionSource;
// import appeng.api.storage.IMEMonitor;
// import appeng.api.storage.data.IAEFluidStack;
// import appeng.api.storage.data.IAEItemStack;
// import appeng.me.GridAccessException;
// import appeng.me.helpers.AENetworkProxy;
// import appeng.util.Platform;
// import com.glodblock.github.common.item.ItemFluidPacket;
// import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
// import gregtech.api.recipe.RecipeMap;
// import gregtech.api.util.GT_Recipe;
// import gregtech.common.tileentities.machines.IDualInputInventory;
// import net.minecraft.inventory.InventoryCrafting;
// import net.minecraft.item.ItemStack;
// import net.minecraft.nbt.NBTTagCompound;
// import net.minecraft.world.World;
// import net.minecraftforge.fluids.FluidStack;
// import org.jetbrains.annotations.NotNull;
//
// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;
// import java.util.Objects;
//
// public class PatternRecipePackage implements ICraftingPatternDetails, Comparable<PatternRecipePackage>,
// IDualInputInventory {
// //private GT_MetaTileEntity_EnhancedMultiBlockBase<?> machine;
// GT_Recipe cachedRecipe;
// Long craftingCount;
// Long recipeCount;
// public Boolean isValid = false;
// IAEItemStack[] inputStacks;
// IAEItemStack[] outputStacks;
// ItemStack pattern;
//
// PatternRecipePackage(RecipeMap<?> recipeMap,
// ICraftingPatternDetails details, ItemStack pattern) {
// if (recipeMap == null) return;
// //this.machine = machine;
// IAEItemStack[] stacks = details.getInputs();
// ArrayList<ItemStack> inputItems = new ArrayList<>();
// ArrayList<FluidStack> inputFluids = new ArrayList<>();
// ArrayList<ItemStack> outItems = new ArrayList<>();
// ArrayList<FluidStack> outFluid = new ArrayList<>();
// for (var stack : stacks) {
// if (stack instanceof IAEFluidStack fluid) {
// inputFluids.add(fluid.getFluidStack());
// } else inputItems.add(stack.getItemStack());
// }
// stacks = details.getOutputs();
// for (var stack : stacks) {
// if (stack instanceof IAEFluidStack fluid) {
// outFluid.add(fluid.getFluidStack());
// } else outItems.add(stack.getItemStack());
// }
// cachedRecipe = checkRecipe(inputItems, inputFluids, outItems, outFluid, recipeMap);
// if (cachedRecipe != null) {
// this.inputStacks = details.getInputs();
// this.outputStacks = details.getOutputs();
// //this.pattern = details.getPattern();
// this.pattern = pattern;
// isValid = true;
// }
// }
//
// public GT_Recipe checkRecipe(ArrayList<ItemStack> inputItems,
// ArrayList<FluidStack> inputFluids,
// ArrayList<ItemStack> outItems,
// ArrayList<FluidStack> outFluid,
// RecipeMap<?> recipeMap) {
// var cmp = new Comparator<ItemStack>() {
// @Override
// public int compare(ItemStack o1, ItemStack o2) {
// if (o2 == null) return -1;
// else if (o1 == null) return 1;
// else return o1.getItem().hashCode() - o2.getItem().hashCode();
// }
// };
// var cmp2 = new Comparator<FluidStack>() {
// @Override
// public int compare(FluidStack o1, FluidStack o2) {
// if (o1 == null) return -1;
// else if (o2 == null) return 1;
// else return o1.getFluid().hashCode() - o2.getFluid().hashCode();
// }
// };
// inputItems.sort(cmp);
// outItems.sort(cmp);
// inputFluids.sort(cmp2);
// outFluid.sort(cmp2);
//
// for (var recipe : recipeMap.getAllRecipes()) {
// ArrayList<ItemStack> a = new ArrayList<>(List.of(recipe.mInputs));
// ArrayList<FluidStack> b = new ArrayList<>(List.of(recipe.mFluidInputs));
// ArrayList<ItemStack> c = new ArrayList<>(List.of(recipe.mOutputs));
// ArrayList<FluidStack> d = new ArrayList<>(List.of(recipe.mFluidOutputs));
// a.sort(cmp);
// b.sort(cmp2);
// c.sort(cmp);
// d.sort(cmp2);
// long cnt = 0;
// for (int i = 0; i < a.size(); i++) {
// if (a.get(i) == null) {
// if (inputItems.size() > i && inputItems.get(i) == null) break;
// else {
// cnt = 0;
// break;
// }
// } else if (inputItems.size() <= i) {
// cnt = 0;
// break;
// } else if (!Objects.equals(a.get(i).getItem(), inputItems.get(i).getItem())) {
// cnt = 0;
// break;
// } else {
// int mod = a.get(i).stackSize % inputItems.get(i).stackSize, div = a.get(i).stackSize / inputItems.get(i).stackSize;
// if (mod != 0 || (cnt != 0 && div != cnt)) {
// cnt = 0;
// break;
// } else cnt = div;
// }
// }
// if (cnt == 0) continue;
// for (int i = 0; i < c.size(); i++) {
// if (c.get(i) == null) {
// if (outItems.size() > i && outItems.get(i) == null) break;
// else {
// cnt = 0;
// break;
// }
// } else if (outItems.size() <= i) {
// cnt = 0;
// break;
// } else if (!Objects.equals(c.get(i).getItem(), outItems.get(i).getItem())) {
// cnt = 0;
// break;
// } else {
// int mod = c.get(i).stackSize % outItems.get(i).stackSize, div = c.get(i).stackSize / outItems.get(i).stackSize;
// if (mod != 0 || div != cnt) {
// cnt = 0;
// break;
// }
// }
// }
// if (cnt == 0) continue;
// for (int i = 0; i < b.size(); i++) {
// if (b.get(i) == null) {
// if (inputFluids.size() > i && inputFluids.get(i) == null) break;
// else {
// cnt = 0;
// break;
// }
// } else if (inputFluids.size() <= i) {
// cnt = 0;
// break;
// } else if (!Objects.equals(b.get(i).getFluid(), inputFluids.get(i).getFluid())) {
// cnt = 0;
// break;
// } else {
// int mod = b.get(i).amount % inputFluids.get(i).amount, div = b.get(i).amount / inputFluids.get(i).amount;
// if (mod != 0 || div != cnt) {
// cnt = 0;
// break;
// }
// }
// }
// if (cnt == 0) continue;
// for (int i = 0; i < d.size(); i++) {
// if (d.get(i) == null) {
// if (outFluid.size() > i && outFluid.get(i) == null) break;
// else {
// cnt = 0;
// break;
// }
// } else if (outFluid.size() <= i) {
// cnt = 0;
// break;
// } else if (!Objects.equals(d.get(i).getFluid(), outFluid.get(i).getFluid())) {
// cnt = 0;
// break;
// } else {
// int mod = d.get(i).amount % outFluid.get(i).amount, div = d.get(i).amount / outFluid.get(i).amount;
// if (mod != 0 || div != cnt) {
// cnt = 0;
// break;
// }
// }
// }
// if (cnt == 0) continue;
// craftingCount = cnt;
// return recipe;
// }
// return null;
// }
//
//
//// public void setMachine(GT_MetaTileEntity_EnhancedMultiBlockBase<?> machine) {
//// if (machine == null) return;
//// this.machine = machine;
//// }
//
// @Override
//
// public ItemStack getPattern() {
// return pattern;
// }
//
// @Override
// public boolean isValidItemForSlot(int slotIndex, ItemStack itemStack, World world) {
// return false;
// }
//
// @Override
// public boolean isCraftable() {
// return isValid;
// }
//
// @Override
// public IAEItemStack[] getInputs() {
// return inputStacks;
// }
//
// @Override
// public IAEItemStack[] getCondensedInputs() {
// return new IAEItemStack[0];
// }
//
// @Override
// public IAEItemStack[] getCondensedOutputs() {
// return new IAEItemStack[0];
// }
//
// @Override
// public IAEItemStack[] getOutputs() {
// return new IAEItemStack[0];
// }
//
// @Override
// public boolean canSubstitute() {
// return isValid;
// }
//
// @Override
// public boolean canBeSubstitute() {
// return isValid && ICraftingPatternDetails.super.canBeSubstitute();
// }
//
// @Override
// public ItemStack getOutput(InventoryCrafting craftingInv, World world) {
// return null;
// }
//
// @Override
// public int getPriority() {
// return Integer.MAX_VALUE;
// }
//
// @Override
// public void setPriority(int priority) {
//
// }
//
// @Override
// public int compareTo(@NotNull PatternRecipePackage o) {
// return 0;
// }
//
// @Override
// public ItemStack[] getItemInputs() {
// return null;
// }
//
// @Override
// public FluidStack[] getFluidInputs() {
// return null;
// }
//
// public boolean hasChanged(ItemStack newPattern, World world) {
// return newPattern == null
// || (!ItemStack.areItemStacksEqual(pattern, newPattern) && !this.equals(
// ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem()))
// .getPatternForItem(pattern, world)));
// }
//
// public ICraftingPatternDetails getPatternDetails() {
// return this;
// }
//
// public void refund(AENetworkProxy proxy, BaseActionSource src) throws GridAccessException {
// IMEMonitor<IAEItemStack> sg = proxy.getStorage()
// .getItemInventory();
// for (ItemStack itemStack : cachedRecipe.mInputs) {
// if (itemStack == null || itemStack.stackSize == 0) continue;
// long stackSize = recipeCount * itemStack.stackSize;
// while (true) {
// int push = (int) Math.min(stackSize, Integer.MAX_VALUE);
// IAEItemStack rest = Platform.poweredInsert(
// proxy.getEnergy(),
// sg,
// AEApi.instance()
// .storage()
// .createItemStack(new ItemStack(itemStack.getItem(), push)),
// src);
// int back = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
// stackSize -= (push - back);
// if (back > 0 || stackSize == 0) break;
// }
//
// }
// IMEMonitor<IAEFluidStack> fsg = proxy.getStorage()
// .getFluidInventory();
// for (FluidStack fluidStack : cachedRecipe.mFluidInputs) {
// if (fluidStack == null || fluidStack.amount == 0) continue;
// fluidStack.amount *= recipeCount;
// IAEFluidStack rest = Platform.poweredInsert(
// proxy.getEnergy(),
// fsg,
// AEApi.instance()
// .storage()
// .createFluidStack(fluidStack),
// src);
// fluidStack.amount = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
// }
// recipeCount = 0L;
// }
//
// public boolean insertItemsAndFluids(InventoryCrafting inventoryCrafting) {
// if (inventoryCrafting == null || inventoryCrafting.getStackInSlot(0) == null) return false;
// int a = inventoryCrafting.getStackInSlot(0).stackSize;
// int b = inputStacks[0].getItemStack().stackSize;
// if (a % b != 0) return false;
// recipeCount += (long) (a / b);
// return true;
// }
//
// public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
// nbt.setTag("pattern", pattern.writeToNBT(new NBTTagCompound()));
//
// return nbt;
// }
// }
