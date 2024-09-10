// package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.EcoSphereFakeRecipes;
//
// import atomicstryker.infernalmobs.common.InfernalMobsCore;
// import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
// import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
// import com.kuba6000.mobsinfo.api.IChanceModifier;
// import com.kuba6000.mobsinfo.api.MobDrop;
// import com.kuba6000.mobsinfo.api.MobRecipe;
// import com.kuba6000.mobsinfo.api.event.PostMobRegistrationEvent;
// import cpw.mods.fml.common.eventhandler.SubscribeEvent;
// import kubatech.config.Config;
// import kubatech.loaders.MobHandlerLoader;
// import net.minecraft.enchantment.EnchantmentHelper;
// import net.minecraft.entity.EntityLiving;
// import net.minecraft.item.ItemStack;
// import net.minecraftforge.common.MinecraftForge;
//
// import java.lang.reflect.InvocationTargetException;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Random;
// import java.util.stream.Collectors;
//
// import static gregtech.api.enums.Mods.InfernalMobs;
// import static kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeEntityCrusher.DIAMOND_SPIKES_DAMAGE;
// import static kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeEntityCrusher.MOB_SPAWN_INTERVAL;
//
// public class Mode3SimulatorFakeRecipe extends MobHandlerLoader implements IRecipePool{
// private static MobHandlerLoader instance = null;
// public static Map<String, MobEECRecipe> MobrecipeMap = new HashMap<>();
//
// @Override
// public void loadRecipes() {
// init();
// }
//
// public static void init() {
// instance = new Mode3SimulatorFakeRecipe();
// MinecraftForge.EVENT_BUS.register(instance);
// }
//
//
//
// public static class MobEECRecipe {
//
// public final List<MobDrop> mOutputs;
//
// public final MobRecipe recipe;
//
// public final int mEUt = 2000;
// public final int mDuration;
// public final EntityLiving entityCopy;
//
// public MobEECRecipe(List<MobDrop> transformedDrops, MobRecipe recipe) {
// this.mOutputs = transformedDrops;
// this.recipe = recipe;
// try {
// this.entityCopy = this.recipe.createEntityCopy();
// } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
// | IllegalAccessException e) {
// throw new RuntimeException(e);
// }
// mDuration = Math.max(MOB_SPAWN_INTERVAL, (int) ((recipe.maxEntityHealth / DIAMOND_SPIKES_DAMAGE) * 10d));
// }
//
// public ItemStack[] generateOutputs(Random rnd, TST_EcoSphereSimulator MTE, double attackDamage,
// int lootinglevel, boolean preferInfernalDrops, boolean voidAllDamagedAndEnchantedItems) {
// MTE.lEUt = mEUt;
// MTE.mMaxProgresstime = Math.max(MOB_SPAWN_INTERVAL, (int) ((recipe.maxEntityHealth / attackDamage) * 10d));
// ArrayList<ItemStack> stacks = new ArrayList<>(this.mOutputs.size());
// this.entityCopy.setPosition(
// MTE.getBaseMetaTileEntity()
// .getXCoord(),
// MTE.getBaseMetaTileEntity()
// .getYCoord(),
// MTE.getBaseMetaTileEntity()
// .getZCoord());
// for (MobDrop o : this.mOutputs) {
// if (voidAllDamagedAndEnchantedItems && (o.damages != null || o.enchantable != null)) continue;
// int chance = o.chance;
//
// double dChance = (double) chance / 100d;
// for (IChanceModifier chanceModifier : o.chanceModifiers) {
// dChance = chanceModifier.apply(
// dChance,
// MTE.getBaseMetaTileEntity()
// .getWorld(),
// stacks,
// MTE.ESSPlayer,
// this.entityCopy);
// }
//
// chance = (int) (dChance * 100d);
// if (chance == 0) continue;
//
// if (o.playerOnly) {
// chance = (int) ((double) chance * Config.MobHandler.playerOnlyDropsModifier);
// if (chance < 1) chance = 1;
// }
// int amount = o.stack.stackSize;
// if (o.lootable && lootinglevel > 0) {
// chance += lootinglevel * 5000;
// if (chance > 10000) {
// int div = (int) Math.ceil(chance / 10000d);
// amount *= div;
// chance /= div;
// }
// }
// if (chance == 10000 || rnd.nextInt(10000) < chance) {
// ItemStack s = o.stack.copy();
// s.stackSize = amount;
// if (o.enchantable != null) EnchantmentHelper.addRandomEnchantment(rnd, s, o.enchantable);
// if (o.damages != null) {
// int rChance = rnd.nextInt(recipe.mMaxDamageChance);
// int cChance = 0;
// for (Map.Entry<Integer, Integer> damage : o.damages.entrySet()) {
// cChance += damage.getValue();
// if (rChance <= cChance) {
// s.setItemDamage(damage.getKey());
// break;
// }
// }
// }
// stacks.add(s);
// }
// }
//
// if (InfernalMobs.isModLoaded()) {
// InfernalMobsCore infernalMobsCore = InfernalMobsCore.instance();
// if (recipe.infernalityAllowed && mEUt * 8 <= MTE.getMaxInputEu()
// && !infernalMobsCore.getDimensionBlackList()
// .contains(
// MTE.getBaseMetaTileEntity()
// .getWorld().provider.dimensionId)) {
// int p = 0;
// int mods = 0;
// if (recipe.alwaysinfernal
// || (preferInfernalDrops && rnd.nextInt(infernalMobsCore.getEliteRarity()) == 0)) {
// p = 1;
// if (rnd.nextInt(infernalMobsCore.getUltraRarity()) == 0) {
// p = 2;
// if (rnd.nextInt(infernalMobsCore.getInfernoRarity()) == 0) p = 3;
// }
// }
// ArrayList<ItemStack> infernalstacks = null;
// if (p > 0) if (p == 1) {
// infernalstacks = infernalMobsCore.getDropIdListElite();
// mods = infernalMobsCore.getMinEliteModifiers();
// } else if (p == 2) {
// infernalstacks = infernalMobsCore.getDropIdListUltra();
// mods = infernalMobsCore.getMinUltraModifiers();
// } else {
// infernalstacks = infernalMobsCore.getDropIdListInfernal();
// mods = infernalMobsCore.getMinInfernoModifiers();
// }
// if (infernalstacks != null) {
// ItemStack infernalstack = infernalstacks.get(rnd.nextInt(infernalstacks.size()))
// .copy();
// // noinspection ConstantConditions
// EnchantmentHelper.addRandomEnchantment(
// rnd,
// infernalstack,
// infernalstack.getItem()
// .getItemEnchantability());
// stacks.add(infernalstack);
// MTE.lEUt *= 8L;
// MTE.mMaxProgresstime *= (int) (mods * InfernalMobsCore.instance()
// .getMobModHealthFactor());
// }
// }
// }
//
// return stacks.toArray(new ItemStack[0]);
// }
// }
// void reWriteRecipes(){
// MobrecipeMap = recipeMap.entrySet().stream()
// .collect(Collectors.toMap(
// Map.Entry::getKey,
// entry -> new MobEECRecipe(entry.getValue()) // 这里假设 MobEECRecipe 有一个复制构造函数
// ));
// }
// }
