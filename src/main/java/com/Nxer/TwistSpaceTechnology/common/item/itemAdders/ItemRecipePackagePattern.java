// package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;
//
// import appeng.api.networking.crafting.ICraftingPatternDetails;
// import com.Nxer.TwistSpaceTechnology.common.machine.ultimateMachines.PatternRecipePackage;
// import com.Nxer.TwistSpaceTechnology.common.machine.ultimateMachines.TST_UltimateMachineBase;
// import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
// import net.minecraft.entity.player.EntityPlayer;
// import net.minecraft.item.Item;
// import net.minecraft.item.ItemStack;
// import net.minecraft.world.World;
//
// public class ItemRecipePackagePattern extends Item {
// public PatternRecipePackage pattern;
// public ICraftingPatternDetails details;
//
// public TST_UltimateMachineBase<?> machine;
//
// ItemRecipePackagePattern() {
// setMaxStackSize(1);
// }
//
// public void initPattern(TST_UltimateMachineBase<?> machine, PatternRecipePackage pattern) {
// this.pattern = pattern;
// this.machine = machine;
//
// }
//
// public void clearPattern() {
// pattern = null;
// details = null;
// machine = null;
// }
//
// @Override
// public final Item setMaxStackSize(int size) {
// return super.setMaxStackSize(size);
// }
//
// public boolean isValidForCrafting(TST_UltimateMachineBase<?> machine) {
// return this.machine != null && this.machine == machine && machine.isValid() && pattern != null && pattern.isValid;
// }
//
// public boolean isValidForInsert(TST_UltimateMachineBase<?> machine) {
// return this.machine.getBaseMetaTileEntity().getMetaTileID() == machine.getBaseMetaTileEntity().getMetaTileID();
// }
//
// @Override
// public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
// if (player.isSneaking()) {
// clearPattern();
// }
// return super.onItemRightClick(itemStackIn, worldIn, player);
// }
// }
