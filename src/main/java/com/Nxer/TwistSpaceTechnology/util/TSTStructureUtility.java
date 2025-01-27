package com.Nxer.TwistSpaceTechnology.util;

import static com.Nxer.TwistSpaceTechnology.util.Utils.createItemStack;
import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.structure.adders.IBlockAdder;
import com.gtnewhorizon.structurelib.structure.adders.ITileAdder;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;

public class TSTStructureUtility {

    private TSTStructureUtility() {}

    /**
     * A helper method to build {@code StructureDefinition} without force generic conversions.
     */
    @NotNull
    @SuppressWarnings("unused")
    @Contract(value = "_ -> new", pure = true)
    public static <T> StructureDefinition.Builder<T> StructureDefinitionBuilder(Class<T> typeToken) {
        return StructureDefinition.builder();
    }

    // region Block

    /**
     * Overload method, similar to {@link #ofBlockStrict(Block, int, Block, int)}.
     * The hitBlock used block instead, hitMeta used meta instead.
     */
    @NotNull
    public static <T> IStructureElement<T> ofBlockStrict(Block block, int meta) {
        return ofBlockStrict(block, meta, block, meta);
    }

    /**
     * Compares the exact meta value of a block as it exists in the real world instance.
     * This is like {@link com.gtnewhorizon.structurelib.structure.StructureUtility#ofBlock(Block, int, Block, int)},
     * but it checks the real meta value used in the world, which might be different from the default one.
     * It's especially useful for blocks such as {@code Slab} where the meta value can vary.
     */
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
                world.setBlock(x, y, z, block, meta, 2);
                if (check(t, world, x, y, z)) return true;
                else return world.setBlockToAir(x, y, z);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(block, meta);
            }
        };
    }

    /**
     * Overload method, similar to {@link #ofBlockStrictExt(Block, int, ItemStack, Block, int)}
     * The hitBlock used block instead, hitMeta used meta instead.
     */
    @NotNull
    public static <T> IStructureElement<T> ofBlockStrictExt(Block block, int meta, Block specialBlock,
        int specialItemMeta) {
        return ofBlockStrictExt(block, meta, createItemStack(specialBlock, specialItemMeta), block, meta);
    }

    /**
     * Places a block using the specified block & meta, but the drainer is specialItemStack.
     * Similar to {@link #ofBlockStrict(Block, int, Block, int)} placing block.
     * This method is intended for block that used specialItemStack to place in world .
     * Such as, in the mod Thaumcraft, the block tileNitor uses metaItem itemResource.
     */
    @NotNull
    public static <T> IStructureElement<T> ofBlockStrictExt(Block block, int meta, ItemStack specialItemStack,
        Block hintBlock, int hintMeta) {
        if (block == null || hintBlock == null || specialItemStack == null) throw new IllegalArgumentException();
        if (specialItemStack.stackSize != 1) throw new IllegalArgumentException("Wrong size of ItemStack.");

        return new IStructureElement<>() {

            private final ItemStack blockStack = createItemStack(block, meta);

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
                world.setBlock(x, y, z, block, meta, 2);
                if (check(t, world, x, y, z)) return true;
                else return world.setBlockToAir(x, y, z);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(block, meta);
            }

            @Override
            @SuppressWarnings("deprecation")
            public PlaceResult survivalPlaceBlock(T t, World w, int x, int y, int z, ItemStack trigger,
                IItemSource source, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                if (check(t, w, x, y, z)) return PlaceResult.SKIP;
                return survivalPlaceBlock(
                    t,
                    w,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(source, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World w, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (check(t, w, x, y, z)) return PlaceResult.SKIP;
                return survivalPlaceBlockWithSpecialItemStack(this, t, w, x, y, z, env, blockStack, specialItemStack);
            }
        };
    }

    /**
     * Accurately adds a block with the meta value to the structure.
     * This method is intended for block that don't cause other effect or issue effects upon addition.
     * Like {@link com.gtnewhorizon.structurelib.structure.StructureUtility#ofBlockAdder(IBlockAdder, Block, int)}
     * and {@link #ofBlockStrict(Block, int, Block, int)}.
     */
    @NotNull
    public static <T> IStructureElement<T> ofAccurateBlockAdder(IBlockAdder<T> iBlockAdder, Block block, int meta) {
        if (iBlockAdder == null || block == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                // Gets the meta value of a block in the real world
                return iBlockAdder.apply(t, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, block, meta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, block, meta, 2);
                if (check(t, world, x, y, z)) return true;
                else return world.setBlockToAir(x, y, z);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(block, meta);
            }
        };
    }

    /**
     * This method is mainly used for chisel blocks, which are automatically transformed when the channel is used to
     * determine whether to place specificBlock by variableStacks.
     *
     * @param channel It can be null, and auto-transform is used by default。
     */
    @NotNull
    public static <T> IStructureElement<T> ofVariableBlock(@Nullable String channel, Block specificBlock,
        int specificMeta, List<ItemStack> variableStacks) {
        if (specificBlock == null || variableStacks == null) throw new IllegalArgumentException();
        if (variableStacks.isEmpty()) throw new IllegalArgumentException();
        if (channel != null) {
            if (channel.isEmpty() || !channel.toLowerCase(Locale.ROOT)
                .equals(channel)) throw new IllegalArgumentException();
        }
        final ItemStack blockStack = createItemStack(specificBlock, specificMeta);
        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                // Gets the meta value of a block in the real world
                return specificMeta == world.getBlockMetadata(x, y, z) && specificBlock == world.getBlock(x, y, z);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, specificBlock, specificMeta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, specificBlock, specificMeta, 2);
                if (check(t, world, x, y, z)) return true;
                else return world.setBlockToAir(x, y, z);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                // only for nei preview the variable block array.
                return BlocksToPlace.create(variableStacks.toArray(new ItemStack[0]));
            }

            @Override
            @SuppressWarnings("deprecation")
            public PlaceResult survivalPlaceBlock(T t, World w, int x, int y, int z, ItemStack trigger,
                IItemSource source, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                if (check(t, w, x, y, z)) return PlaceResult.SKIP;
                return survivalPlaceBlock(
                    t,
                    w,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(source, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World w, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (check(t, w, x, y, z)) return PlaceResult.SKIP;
                var source = env.getSource();
                var chatter = env.getChatter();

                if (source.takeOne(blockStack, true)) {
                    return StructureUtility.survivalPlaceBlock(
                        blockStack,
                        ItemStackPredicate.NBTMode.EXACT,
                        null,
                        true,
                        w,
                        x,
                        y,
                        z,
                        source,
                        env.getActor(),
                        chatter);
                } else {
                    if ((channel == null) || (ChannelDataAccessor.hasSubChannel(trigger, channel))) {
                        for (ItemStack stack : variableStacks) {
                            if (!source.takeOne(stack, true)) continue;
                            return survivalPlaceBlockWithSpecialItemStack(this, t, w, x, y, z, env, blockStack, stack);
                        }
                    }
                }

                if (chatter != null) {
                    chatter.accept(
                        new ChatComponentTranslation(
                            "structurelib.autoplace.error.no_item_stack",
                            blockStack.func_151000_E()));
                }
                return PlaceResult.REJECT;
            }
        };
    }
    // endregion

    // region TileAdder
    /**
     * Overload method, similar to {@link #ofAccurateTile(Class, Block, int, Block, int)}
     * The hitBlock used block instead, hitMeta used meta instead.
     */
    @NotNull
    public static <T, E> IStructureElement<T> ofAccurateTile(Class<E> tileClass, Block tileBlock, int tileBlockMeta) {
        return ofAccurateTile(tileClass, tileBlock, tileBlockMeta, tileBlock, tileBlockMeta);
    }

    /**
     * Overload method, similar to {@link #ofAccurateTileAdder(ITileAdder, Block, int, Block, int)}
     * An easy way to check if it's an instance of this accurate tileEntity.
     */
    @NotNull
    public static <T, E> IStructureElement<T> ofAccurateTile(Class<E> tileClass, Block tileBlock, int tileBlockMeta,
        Block hintBlock, int hintMeta) {
        if (tileClass == null) throw new IllegalArgumentException();
        return ofAccurateTileAdder((a, b) -> tileClass.isInstance(b), tileBlock, tileBlockMeta, hintBlock, hintMeta);
    }

    /**
     * Overload method, similar to {@link #ofAccurateTileAdder(ITileAdder, Block, int, Block, int)}
     * The hitBlock used block instead, hitMeta used meta instead.
     */
    @NotNull
    public static <T> IStructureElement<T> ofAccurateTileAdder(ITileAdder<T> iTileAdder, Block tileBlock,
        int tileBlockMeta) {
        return ofAccurateTileAdder(iTileAdder, tileBlock, tileBlockMeta, tileBlock, tileBlockMeta);
    }

    /**
     * This method used to place accurate tileEntity that without other effect or issue,
     * aimed to auto place TE and preview.
     */
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

    /**
     * This method like {@link #ofBlockStrictExt(Block, int, ItemStack, Block, int)} and
     * {@link #ofAccurateTileAdder(ITileAdder, Block, int, Block, int)}, uses specialItemStack & accurate tileEntity,
     * but used to
     * accurate tileEntity that without other effect or issue, aimed to auto place TE and preview.
     */
    @NotNull
    public static <T, E> IStructureElement<T> ofAccurateTileExt(Class<E> tileClass, Block tileBlock, int tileBlockMeta,
        Item specialItem, int specialItemMeta) {
        if (tileClass == null) throw new IllegalArgumentException();
        return ofAccurateTileExt(tileClass, tileBlock, tileBlockMeta, createItemStack(specialItem, specialItemMeta));
    }

    /**
     * This method like {@link #ofBlockStrictExt(Block, int, ItemStack, Block, int)} and
     * {@link #ofAccurateTileAdder(ITileAdder, Block, int, Block, int)}, uses specialItemStack & accurate tileEntity,
     * but used to
     * accurate tileEntity that without other effect or issue, aimed to auto place TE and preview.
     */
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

    /**
     * This method like {@link #ofBlockStrictExt(Block, int, ItemStack, Block, int)} and
     * {@link #ofAccurateTileAdder(ITileAdder, Block, int, Block, int)}, uses specialItemStack & accurate tileEntity,
     * but used to
     * accurate tileEntity that without other effect or issue, aimed to auto place TE and preview.
     */
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
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(blockStack);
            }

            @Override
            @SuppressWarnings("deprecation")
            public PlaceResult survivalPlaceBlock(T t, World w, int x, int y, int z, ItemStack trigger,
                IItemSource source, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                if (check(t, w, x, y, z)) return PlaceResult.SKIP;
                return survivalPlaceBlock(
                    t,
                    w,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(source, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World w, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (check(t, w, x, y, z)) return PlaceResult.SKIP;
                return survivalPlaceBlockWithSpecialItemStack(this, t, w, x, y, z, env, blockStack, specialItemStack);
            }
        };
    }
    // endregion

    // region Util
    protected static <T> PlaceResult survivalPlaceBlockWithSpecialItemStack(IStructureElement<T> iStructureElement, T t,
        World world, int x, int y, int z, AutoPlaceEnvironment env, ItemStack blockStack, ItemStack specialItemStack) {

        var actor = env.getActor();
        var source = env.getSource();
        var chatter = env.getChatter();

        if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, actor)) return PlaceResult.REJECT;
        if (!source.takeOne(specialItemStack, true)) {
            if (chatter != null) chatter.accept(
                new ChatComponentTranslation(
                    "structurelib.autoplace.error.no_item_stack",
                    specialItemStack.func_151000_E()));
            return PlaceResult.REJECT;
        }

        if (!(blockStack.copy()
            .tryPlaceItemIntoWorld(actor, world, x, y, z, ForgeDirection.UP.ordinal(), 0.5f, 0.5f, 0.5f)))
            return PlaceResult.REJECT;
        if (!iStructureElement.check(t, world, x, y, z)) return PlaceResult.REJECT;
        if (!source.takeOne(specialItemStack, false)) {
            world.setBlockToAir(x, y, z);
            TwistSpaceTechnology.LOG
                .error("An error occurred with a placed block, specialItemStack: {}", specialItemStack);
            return PlaceResult.REJECT;
        }
        return PlaceResult.ACCEPT;
    }
    // endregion

}
