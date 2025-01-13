package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.adders.ITileAdder;

@SuppressWarnings("unused")
public class TSTStructureUtility {

    private TSTStructureUtility() {}

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> StructureDefinition.Builder<T> StructureDefinitionBuilder(Class<T> typeToken) {
        return StructureDefinition.builder();
    }

    // region Block
    @NotNull
    public static <T> IStructureElement<T> ofBlockStrict(Block block, int meta) {
        return ofBlockStrict(block, meta, block, meta);
    }

    @NotNull
    public static <T> IStructureElement<T> ofBlockStrict(Block block, int meta, Block hintBlock, int hintMeta) {
        if (block == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                // Gets the meta value of a block in the real world
                return meta == world.getBlockMetadata(x, y, z) && block == world.getBlock(x, y, z);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, block, meta, 2);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(block, meta);
            }
        };
    }

    @NotNull
    public static <T> IStructureElement<T> ofBlockStrictExt(Block block, int meta, Block specialBlock,
        int specialItemMeta) {
        return ofBlockStrictExt(block, meta, new ItemStack(specialBlock, 1, specialItemMeta), block, meta);
    }

    @NotNull
    public static <T> IStructureElement<T> ofBlockStrictExt(Block block, int meta, ItemStack specialItemStack,
        Block hintBlock, int hintMeta) {
        if (block == null || hintBlock == null || specialItemStack == null) throw new IllegalArgumentException();
        if (specialItemStack.stackSize != 1) throw new IllegalArgumentException("Wrong size of ItemStack.");

        return new IStructureElement<>() {

            private final ItemStack blockStack = new ItemStack(block, 1, meta);

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                // Gets the meta value of a block in the real world
                return meta == world.getBlockMetadata(x, y, z) && block == world.getBlock(x, y, z);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, block, meta, 2);
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {

                if (check(t, world, x, y, z)) return PlaceResult.SKIP;

                var actor = env.getActor();
                var source = env.getSource();
                var chatter = env.getChatter();

                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, actor)) return PlaceResult.REJECT;
                if (!source.takeOne(specialItemStack, true) && chatter != null) {
                    chatter.accept(
                        new ChatComponentTranslation(
                            "structurelib.autoplace.error.no_item_stack",
                            specialItemStack.func_151000_E()));
                    return PlaceResult.REJECT;
                }

                if (!(blockStack.copy()
                    .tryPlaceItemIntoWorld(actor, world, x, y, z, ForgeDirection.UP.ordinal(), 0.5f, 0.5f, 0.5f)))
                    return PlaceResult.REJECT;
                if (!check(t, world, x, y, z)) return PlaceResult.REJECT;
                if (!source.takeOne(specialItemStack, false)) {
                    world.setBlockToAir(x, y, z);
                    TwistSpaceTechnology.LOG.info(
                        "An error occurred with a placed block, specialItemStack: {}",
                        specialItemStack.toString());
                }
                return PlaceResult.ACCEPT;
            }
        };
    }
    // endregion

    // region TileAdder
    @NotNull
    public static <T> IStructureElement<T> ofAccurateTileAdder(ITileAdder<T> iTileAdder, Block tileBlock,
        int tileBlockMeta) {
        return ofAccurateTileAdder(iTileAdder, tileBlock, tileBlockMeta, tileBlock, tileBlockMeta);
    }

    @NotNull
    public static <T, E> IStructureElement<T> ofAccurateTile(Class<E> tileClass, Block tileBlock, int tileBlockMeta) {
        return ofAccurateTile(tileClass, tileBlock, tileBlockMeta, tileBlock, tileBlockMeta);
    }

    @NotNull
    public static <T, E> IStructureElement<T> ofAccurateTile(Class<E> tileClass, Block tileBlock, int tileBlockMeta,
        Block hintBlock, int hintMeta) {
        if (tileClass == null) throw new IllegalArgumentException();
        return ofAccurateTileAdder((a, b) -> tileClass.isInstance(b), tileBlock, tileBlockMeta, hintBlock, hintMeta);
    }

    @NotNull
    public static <T> IStructureElement<T> ofAccurateTileAdder(ITileAdder<T> iTileAdder, Block tileBlock,
        int tileBlockMeta, Block hintBlock, int hintMeta) {
        if (iTileAdder == null || tileBlock == null || hintBlock == null) {
            throw new IllegalArgumentException();
        }
        if (!(Item.getItemFromBlock(tileBlock) instanceof ItemBlock))
            throw new IllegalArgumentException("WrongType of tileBlock, must be an itemBlock.");

        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return iTileAdder.apply(t, tileEntity);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, tileBlock, tileBlockMeta, 2);
                return check(t, world, x, y, z); // also to update tile...
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(tileBlock, tileBlockMeta);
            }
        };
    }

    @NotNull
    public static <T, E> IStructureElement<T> ofAccurateTileExt(Class<E> tileClass, Block tileBlock, int tileBlockMeta,
        Item specialItem, int specialItemMeta) {
        if (tileClass == null) throw new IllegalArgumentException();
        return ofAccurateTileExt(tileClass, tileBlock, tileBlockMeta, new ItemStack(specialItem, 1, specialItemMeta));
    }

    @NotNull
    public static <T, E> IStructureElement<T> ofAccurateTileExt(Class<E> tileClass, Block tileBlock, int tileBlockMeta,
        ItemStack specialItemStack) {
        if (tileClass == null) throw new IllegalArgumentException();
        return ofAccurateTileAdderExt(
            (a, b) -> tileClass.isInstance(b),
            tileBlock,
            tileBlockMeta,
            specialItemStack,
            tileBlock,
            tileBlockMeta);
    }

    @NotNull
    public static <T> IStructureElement<T> ofAccurateTileAdderExt(ITileAdder<T> iTileAdder, Block tileBlock,
        int tileBlockMeta, ItemStack specialItemStack, Block hintBlock, int hintMeta) {
        // emm.....
        if (iTileAdder == null || hintBlock == null) throw new IllegalArgumentException();
        if (tileBlock == null || specialItemStack == null) throw new NullPointerException();
        if (specialItemStack.stackSize != 1) throw new IllegalArgumentException("Wrong size of ItemStack.");
        if (!(Item.getItemFromBlock(tileBlock) instanceof ItemBlock))
            throw new IllegalArgumentException("WrongType of tileBlock, must be an itemBlock.");

        return new IStructureElement<>() {

            private final ItemStack blockStack = new ItemStack(tileBlock, 1, tileBlockMeta);

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return iTileAdder.apply(t, tileEntity);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, tileBlock, tileBlockMeta, 2);
                return check(t, world, x, y, z); // also to update tile...
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {

                if (check(t, world, x, y, z)) return PlaceResult.SKIP;

                var actor = env.getActor();
                var source = env.getSource();
                var chatter = env.getChatter();

                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, actor)) return PlaceResult.REJECT;
                if (!source.takeOne(specialItemStack, true) && chatter != null) {
                    chatter.accept(
                        new ChatComponentTranslation(
                            "structurelib.autoplace.error.no_item_stack",
                            specialItemStack.func_151000_E()));
                    return PlaceResult.REJECT;
                }

                if (!(blockStack.copy()
                    .tryPlaceItemIntoWorld(actor, world, x, y, z, ForgeDirection.UP.ordinal(), 0.5f, 0.5f, 0.5f)))
                    return PlaceResult.REJECT;
                if (!check(t, world, x, y, z)) return PlaceResult.REJECT;
                if (!source.takeOne(specialItemStack, false)) {
                    world.setBlockToAir(x, y, z);
                    TwistSpaceTechnology.LOG.info(
                        "An error occurred with a placed tileBlock, specialItemStack: {}",
                        specialItemStack.toString());
                }
                return PlaceResult.ACCEPT;
            }
        };
    }
    // endregion

}
