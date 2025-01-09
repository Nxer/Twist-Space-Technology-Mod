package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.multiuse;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GTModHandler;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocksAbstract;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingItems;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler3;

public class BlockMultiUseCore extends GregtechMetaCasingBlocksAbstract {

    /**
     * Keys are tiers in {@link gregtech.api.enums.VoltageIndex}.
     */
    private static final Multimap<Integer, Pair<Block, Integer>> Representatives = LinkedHashMultimap.create();

    /**
     * The drop lists map for this block.
     * If the drop list is missing, the block drops like default.
     */
    private static final Map<Integer, IItemContainer[]> BlockBreakDropTable = Maps.newHashMap();

    public static class BlockItemMultiUseCore extends GregtechMetaCasingItems {

        public BlockItemMultiUseCore(Block par1) {
            super(par1);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            aList.add(GTValues.TIER_COLORS[aStack.getItemDamage()] + GTValues.VN[aStack.getItemDamage()]);

            // #tr tst.blockcasings.multi.tooltip
            // # Break with Wrench to retrieve the machines
            // #zh_CN 使用扳手破坏返还机器
            aList.add(EnumChatFormatting.GREEN + TextEnums.tr("tst.blockcasings.multi.tooltip"));
        }
    }

    public BlockMultiUseCore() {
        super(BlockItemMultiUseCore.class, "tst.blockcasings.multi", MaterialCasings.INSTANCE);
        // #tr tst.blockcasings.multi.5.name
        // # Advanced MultiUse Core IV
        // #zh_CN 高级多功能核心 IV
        GTCMItemList.MultiUseCore_IV.set(setup(5));
        // #tr tst.blockcasings.multi.6.name
        // # Advanced MultiUse Core LuV
        // #zh_CN 高级多功能核心 LuV
        GTCMItemList.MultiUseCore_LuV.set(setup(6));
        // #tr tst.blockcasings.multi.7.name
        // # Advanced MultiUse Core ZPM
        // #zh_CN 高级多功能核心 ZPM
        GTCMItemList.MultiUseCore_ZPM.set(setup(7));
        // #tr tst.blockcasings.multi.8.name
        // # Advanced MultiUse Core UV
        // #zh_CN 高级多功能核心 UV
        GTCMItemList.MultiUseCore_UV.set(setup(8));
    }

    private ItemStack setup(int tier) {
        var stack = new ItemStack(this, 1, tier);
        Representatives.put(tier, Pair.of(this, tier));
        return stack;
    }

    /**
     * For internal use only. The drops MUST be an array of {@link IItemContainer}.
     * A workaround for ArrayStoreException.
     *
     * @param stack the stack of MultiUse Core
     * @param drops the IItemContainers of dropping items
     */
    private static void setBlockBreakDropTable(ItemStack stack, IItemContainer[] drops) {
        // check
        if (Block.getBlockFromItem(stack.getItem()) != BasicBlocks.multiUseCore) {
            throw new IllegalArgumentException("Not MultiUse Core Block");
        }

        BlockBreakDropTable.put(stack.getItemDamage(), drops);
    }

    public static void setupMultiUseCoreRecipe(ItemStack result, IItemContainer[] items) {
        // Machines -> Core
        // copy the items into an Object array, to fix the ArrayStoreException at GTModHandler.java:1344
        Object[] itemsObjectArray = new Object[items.length];
        System.arraycopy(items, 0, itemsObjectArray, 0, items.length);
        GTModHandler.addShapelessCraftingRecipe(result, itemsObjectArray);
        // Core -> Machines
        setBlockBreakDropTable(result, items);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        aList.add(GTCMItemList.MultiUseCore_IV.get(1));
        aList.add(GTCMItemList.MultiUseCore_LuV.get(1));
        aList.add(GTCMItemList.MultiUseCore_ZPM.get(1));
        aList.add(GTCMItemList.MultiUseCore_UV.get(1));
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        if (BlockBreakDropTable.containsKey(metadata)) {
            return Arrays.stream(BlockBreakDropTable.get(metadata))
                .map(item -> item.get(1))
                .collect(Collectors.toCollection(ArrayList::new));
        }
        return super.getDrops(world, x, y, z, metadata, fortune);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        // FIXME: change this
        return CasingTextureHandler3.getIcon(side, 2);
    }

    public static boolean hasCoreInTier(int tier) {
        return Representatives.containsKey(tier);
    }

    public static List<Pair<Block, Integer>> getRepresentatives() {
        return Representatives.entries()
            .stream()
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }

    public static Integer getTier(Block block, int i) {
        var other = Pair.of(block, i);
        return Representatives.entries()
            .stream()
            .filter(
                x -> x.getValue()
                    .equals(other))
            .findFirst()
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public static <T> IStructureElement<T> ofMultiUseCore(int initialValue, BiConsumer<T, Integer> setter,
        Function<T, Integer> getter) {
        return lazy(
            t -> ofBlocksTiered(BlockMultiUseCore::getTier, getRepresentatives(), initialValue, setter, getter));
    }

}
