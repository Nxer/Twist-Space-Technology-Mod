package com.Nxer.TwistSpaceTechnology.common.machine.treefarm;

import static com.Nxer.TwistSpaceTechnology.util.TstUtils.setStackSize;
import static com.gtnewhorizon.cropsnh.tileentity.TileEntityCropSticks.BASE_GROWTH_SPEED;
import static com.gtnewhorizon.cropsnh.tileentity.multi.MTEIndustrialFarm.CYCLE_TICK_RATE_SCALAR;
import static net.minecraft.item.ItemStack.areItemStacksEqual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.gtnewhorizon.cropsnh.api.ISeedData;
import com.gtnewhorizon.cropsnh.tileentity.TileEntityCropSticks;
import com.gtnewhorizon.cropsnh.utility.CropsNHUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.mixin.interfaces.accessors.IBlockStemAccessor;

public class CropsNHFarm {

    public static final int MEGA_TREE_FARM_MULTIPLIER = 16384;

    public ItemStack lastCache = null;
    public ISeedData seedData = null;

    public Pair<ItemStack, Double>[] output = new Pair[0];

    public ItemStack[] getOutputStacks(double multiplier) {
        if (output == null || output.length == 0) {
            return new ItemStack[0];
        }

        List<ItemStack> o = new ArrayList<>();

        for (Pair<ItemStack, Double> p : output) {
            if (p == null) continue;
            ItemStack op = p.getKey();
            if (op == null) continue;
            // get actual amount of this stack to output
            long a = (long) Math.ceil(p.getValue() * multiplier);
            if (a < 1) continue;

            if (a <= Integer.MAX_VALUE) {
                o.add(setStackSize(op.copy(), (int) a));
            } else {
                while (a > Integer.MAX_VALUE) {
                    o.add(setStackSize(op.copy(), Integer.MAX_VALUE));
                    a -= Integer.MAX_VALUE;
                }
                if (a > 0) {
                    o.add(setStackSize(op.copy(), (int) a));
                }
            }
        }

        if (o.isEmpty()) return new ItemStack[0];

        return o.toArray(new ItemStack[0]);

    }

    public boolean isValid() {
        return output != null && output.length > 0;
    }

    public boolean createCropCache(ItemStack seed) {
        if (isCached(seed)) return true;
        return createCrop(seed) || createVanillaCrop(seed);
    }

    public boolean isCached(ItemStack seed) {
        if (lastCache == null || seed == null) return false;
        return areItemStacksEqual(lastCache, seed);
    }

    public boolean createCrop(ItemStack seedStack) {
        ISeedData seed = CropsNHUtils.getAnalyzedSeedData(seedStack);
        if (seed == null) return false;

        // calculate how much progress is done each cycle
        double tProgressPerCycle = getGrowthProgressPerCycle(seed);
        if (tProgressPerCycle <= 0) {
            tProgressPerCycle = 1;
            TwistSpaceTechnology.LOG
                .info("Creating Crop but get a invalid num tProgressPerCycle={} in seed {}", tProgressPerCycle, seed);
        } ;

        // calc avg drop stack size increase
        double avgDropIncrease = TileEntityCropSticks.getAvgDropCountIncrease(
            seed.getStats()
                .getGain());

        // calc average number of created drops per harvest
        double avgDropCount = TileEntityCropSticks.getAvgDropRounds(
            seed.getCrop(),
            seed.getStats()
                .getGain());

        List<Pair<ItemStack, Double>> toCache = new ArrayList<>();

        for (Map.Entry<ItemStack, Integer> entry : seed.getCrop()
            .getDropTable()
            .entrySet()) {

            ItemStack stack = entry.getKey();
            double chance = entry.getValue() / 10_000d;

            // scale by chance and progress completed each cycle.
            double basicAmount = (stack.stackSize + avgDropIncrease) * chance * avgDropCount * tProgressPerCycle;

            double amount = basicAmount * MEGA_TREE_FARM_MULTIPLIER;
            toCache.add(Pair.of(stack.copy(), amount));

        }

        if (toCache.isEmpty()) return false;

        output = toCache.toArray(new Pair[0]);
        lastCache = seedStack.copy();
        seedData = seed;
        return true;

    }

    private static final int NUMBER_OF_DROPS_TO_SIMULATE = 10;

    public boolean createVanillaCrop(ItemStack seedStack) {

        if (seedStack == null || seedStack.stackSize < 1) return false;
        Item seedItem = seedStack.getItem();
        Map<TST_ItemID, Double> simulator = new HashMap<>();

        if (seedItem instanceof IPlantable plantable) {
            Block plantBlock;

            if (plantable instanceof ItemSeeds itemSeeds) {
                plantBlock = itemSeeds.getPlant(null, 0, 0, 0);
            } else if (plantable instanceof ItemSeedFood itemSeedFood) {
                plantBlock = itemSeedFood.getPlant(null, 0, 0, 0);
            } else {
                plantBlock = plantable.getPlant(null, 0, 0, 0);
            }

            // if we know some crops needs a specific metadata, remap here
            int metadata = -1;
            MinecraftServer server = MinecraftServer.getServer();
            World world = server.worldServerForDimension(0);

            if (plantBlock instanceof BlockStem blockStem) {
                Block cropBlock = ((IBlockStemAccessor) blockStem).gt5u$getCropBlock();
                metadata = 0;
                if (cropBlock != null && cropBlock != Blocks.air) {
                    // Simulate drops
                    for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
                        // simulate 1 round of drops
                        ArrayList<ItemStack> blockDrops = cropBlock.getDrops(world, 0, 0, 0, metadata, 0);
                        if (blockDrops == null || blockDrops.isEmpty()) continue;
                        // if the droped item is a block that places itself, assume this is the only possible drop
                        // eg: pumpkin, redlon
                        if (i == 0 && blockDrops.size() == 1) {
                            ItemStack drop = blockDrops.get(0);
                            if (drop != null && drop.stackSize >= 1
                                && drop.getItem() == Item.getItemFromBlock(cropBlock)) {
                                simulator.merge(TST_ItemID.create(drop), (double) drop.stackSize, Double::sum);
                                break;
                            }
                        }
                        // else append all the drops
                        for (ItemStack drop : blockDrops) {
                            simulator.merge(
                                TST_ItemID.create(drop),
                                drop.stackSize / (double) NUMBER_OF_DROPS_TO_SIMULATE,
                                Double::sum);
                        }
                    }
                }
            } else if (plantBlock != null) {
                metadata = 7;

                // Natura crops have an optimal harvest stage of 8.
                GameRegistry.UniqueIdentifier u = GameRegistry.findUniqueIdentifierFor(seedItem);
                if (u != null && Objects.equals(u.modId, "Natura")) metadata = 8;

                // Pre-Generate drops.
                for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
                    ArrayList<ItemStack> plantDrops = plantBlock.getDrops(world, 0, 0, 0, metadata, 0);
                    for (ItemStack drop : plantDrops) {
                        simulator.merge(TST_ItemID.create(drop), (double) drop.stackSize, Double::sum);
                    }
                }

                // reduce the number of drops to account for the seeds
                // make a safe copy of the seed just in case
                ItemStack seedSafe = seedStack.copy();
                seedSafe.stackSize = 1;
                // first check if we dropped an item identical to our seed item.
                int inputSeedDropCountAfterRemoval = (int) Math.round(simulator.get(TST_ItemID.create(seedSafe)))
                    - NUMBER_OF_DROPS_TO_SIMULATE;
                if (inputSeedDropCountAfterRemoval > 0) {
                    simulator.put(TST_ItemID.create(seedSafe), (double) inputSeedDropCountAfterRemoval);
                } else {
                    simulator.remove(TST_ItemID.create(seedSafe));
                }

                // reduce drop count to account for the number of simulations
                simulator.entrySet()
                    .forEach(x -> x.setValue(x.getValue() / NUMBER_OF_DROPS_TO_SIMULATE));
            }
        }

        if (simulator.isEmpty()) return false;
        List<Pair<ItemStack, Double>> toCache = new ArrayList<>();
        simulator.entrySet()
            .forEach(
                e -> {
                    toCache.add(
                        Pair.of(
                            e.getKey()
                                .getItemStackWithNBT(),
                            Math.ceil(e.getValue())));
                });

        output = toCache.toArray(new Pair[0]);
        lastCache = seedStack;
        seedData = null;
        return true;

    }

    public static double getGrowthProgressPerCycle(ISeedData aCrop) {
        // calc unscaled growth speed of crop.
        int tUnscaledGrowthSpeed = aCrop.getStats()
            .getGrowth() + BASE_GROWTH_SPEED;
        if (tUnscaledGrowthSpeed <= 0) return -1;
        // calculate percentage grown each tick up to 100% since growth
        // don't carry over if you wait to harvest in world crops.
        // this is intentional balancing and shouldn't be included in the future mega multi.
        int cropGrowthDuration = aCrop.getCrop()
            .getGrowthDuration();
        int growthTicksPerHarvest = (cropGrowthDuration / tUnscaledGrowthSpeed)
            + (cropGrowthDuration % tUnscaledGrowthSpeed == 0 ? 0 : 1);
        // calculate percent progress per growth tick
        double growthPercentPerGrowthTick = 1.0d / growthTicksPerHarvest;
        // scale it to the cycle's rate and apply growth speed multipliers
        return growthPercentPerGrowthTick * CYCLE_TICK_RATE_SCALAR;
    }
}
