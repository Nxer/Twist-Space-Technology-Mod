package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler;

import static com.Nxer.TwistSpaceTechnology.util.TstUtils.setStackSize;
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
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ISeedData;
import com.gtnewhorizon.cropsnh.farming.SeedData;
import com.gtnewhorizon.cropsnh.farming.SeedStats;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.tileentity.TileEntityCropSticks;
import com.gtnewhorizon.cropsnh.utility.CropsNHUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.common.GTDummyWorld;
import gregtech.mixin.interfaces.accessors.IBlockStemAccessor;

public final class CropsNHFarm {

    /**
     * Efficiency of every ordinary seed compared to an analyzed hybrid seed.
     * Registered alternate seeds and dynamically simulated plantable seeds both use this multiplier.
     */
    private static final double NORMAL_SEED_EFFICIENCY = 0.25d;
    // Every crop is simulated with all of its preferred biomes satisfied, without rewarding longer preference lists.
    private static final int SIMULATED_NUTRIENT_SCORE = 220;
    private static final int NORMAL_SEED_GROWTH_DURATION = 1200;
    private static final World SIMULATION_WORLD = new GTDummyWorld() {

        @Override
        public Block getBlock(int x, int y, int z) {
            return y == -1 ? Blocks.farmland : Blocks.air;
        }

        @Override
        public int getBlockMetadata(int x, int y, int z) {
            return y == 0 ? 7 : 0;
        }
    };

    private ItemStack lastCache = null;
    private ISeedData seedData = null;

    private Pair<ItemStack, Double>[] output = new Pair[0];

    private static ItemStack[] getOutputStacks(Pair<ItemStack, Double>[] cachedOutput, double multiplier) {
        if (cachedOutput == null || cachedOutput.length == 0) {
            return new ItemStack[0];
        }

        List<ItemStack> outputs = new ArrayList<>();

        for (Pair<ItemStack, Double> entry : cachedOutput) {
            if (entry == null) continue;
            ItemStack output = entry.getKey();
            if (output == null) continue;
            // get actual amount of this stack to output
            long amount = (long) Math.ceil(entry.getValue() * multiplier);
            if (amount < 1) continue;

            if (amount <= Integer.MAX_VALUE) {
                outputs.add(setStackSize(output.copy(), (int) amount));
            } else {
                while (amount > Integer.MAX_VALUE) {
                    outputs.add(setStackSize(output.copy(), Integer.MAX_VALUE));
                    amount -= Integer.MAX_VALUE;
                }
                if (amount > 0) {
                    outputs.add(setStackSize(output.copy(), (int) amount));
                }
            }
        }

        return outputs.toArray(new ItemStack[0]);

    }

    public CropCache getCropCache(ItemStack seed) {
        if (seed == null || seed.getItem() == null) return null;
        if (!isCached(seed) && !createCrop(seed)) {
            ICropCard crop = CropRegistry.instance.fromAlternateSeed(seed);
            if (crop != null) {
                if (!createAlternateSeedCrop(seed, crop)) return null;
            } else if (!createVanillaCrop(seed)) {
                return null;
            }
        }
        return new CropCache(seedData != null, output.clone(), 1);
    }

    private boolean createAlternateSeedCrop(ItemStack seedStack, ICropCard crop) {
        // Alternate seeds are raw crop items (e.g. the Gaia Wart item itself) that can be planted as a
        // stand-in for a hybrid seed. They carry no seed stats, so they are simulated with the default
        // analyzed stats (1/1/1) through the FULL hybrid chain: growth progress per cycle, drop chance
        // rounds and the avg drop count increase all apply. On top of that the whole result is multiplied
        // by NORMAL_SEED_EFFICIENCY so that properly bred hybrid seeds always strictly outperform
        // simply feeding the raw crop item (O(1) per slot vs a bred 1/1/1 seed).
        ISeedData seed = new SeedData(crop, SeedStats.DEFAULT_ANALYZED);

        // calculate how much progress is done each cycle (same chain as createCrop)
        double tProgressPerCycle = getGrowthProgressPerCycle(seed);
        if (tProgressPerCycle <= 0) {
            tProgressPerCycle = 1;
            TwistSpaceTechnology.LOG.info(
                "Creating Alternate Seed Crop but get a invalid num tProgressPerCycle={} in seed {}",
                tProgressPerCycle,
                seed);
        }

        // calc avg drop stack size increase
        double avgDropIncrease = TileEntityCropSticks.getAvgDropCountIncrease(
            seed.getStats()
                .getGain());

        // calc average number of created drops per harvest
        double avgDropCount = TileEntityCropSticks.getAvgDropRounds(
            crop,
            seed.getStats()
                .getGain());

        List<Pair<ItemStack, Double>> toCache = new ArrayList<>();
        for (Map.Entry<ItemStack, Integer> entry : crop.getDropTable()
            .entrySet()) {
            ItemStack stack = entry.getKey();
            if (stack == null || stack.getItem() == null) continue;
            double chance = entry.getValue() / 10_000d;

            // scale by chance, progress, drop rounds, and the alternate-seed efficiency penalty.
            double amount = (stack.stackSize + avgDropIncrease) * chance
                * avgDropCount
                * tProgressPerCycle
                * NORMAL_SEED_EFFICIENCY;
            if (amount > 0) toCache.add(Pair.of(stack.copy(), amount));
        }
        if (toCache.isEmpty()) return false;

        output = toCache.toArray(new Pair[0]);
        lastCache = seedStack.copy();
        seedData = null;
        return true;
    }

    private boolean isCached(ItemStack seed) {
        if (lastCache == null || seed == null) return false;
        return areItemStacksEqual(lastCache, seed);
    }

    private boolean createCrop(ItemStack seedStack) {
        ISeedData seed = CropsNHUtils.getAnalyzedSeedData(seedStack);
        if (seed == null) return false;

        // calculate how much progress is done each cycle
        double tProgressPerCycle = getGrowthProgressPerCycle(seed);
        if (tProgressPerCycle <= 0) {
            tProgressPerCycle = 1;
            TwistSpaceTechnology.LOG
                .info("Creating Crop but get a invalid num tProgressPerCycle={} in seed {}", tProgressPerCycle, seed);
        }

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

            toCache.add(Pair.of(stack.copy(), basicAmount));

        }

        if (toCache.isEmpty()) return false;

        output = toCache.toArray(new Pair[0]);
        lastCache = seedStack.copy();
        seedData = seed;
        return true;

    }

    private static final int NUMBER_OF_DROPS_TO_SIMULATE = 10;

    private boolean createVanillaCrop(ItemStack seedStack) {

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
            World world = SIMULATION_WORLD;

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
                double simulatedSeedDrops = simulator.getOrDefault(TST_ItemID.create(seedSafe), 0d);
                int inputSeedDropCountAfterRemoval = (int) Math.round(simulatedSeedDrops) - NUMBER_OF_DROPS_TO_SIMULATE;
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
        int growthRate = TileEntityCropSticks
            .getGrowthRate(SIMULATED_NUTRIENT_SCORE, 1, SeedStats.DEFAULT_ANALYZED.getGrowth());
        int growthTicksPerHarvest = (int) Math.ceil((double) NORMAL_SEED_GROWTH_DURATION / growthRate);
        double progressPerCycle = CYCLE_TICK_RATE_SCALAR / growthTicksPerHarvest * NORMAL_SEED_EFFICIENCY;
        List<Pair<ItemStack, Double>> toCache = new ArrayList<>();
        simulator.entrySet()
            .forEach(
                e -> {
                    toCache.add(
                        Pair.of(
                            e.getKey()
                                .getItemStackWithNBT(),
                            e.getValue() * progressPerCycle));
                });

        output = toCache.toArray(new Pair[0]);
        lastCache = seedStack.copy();
        seedData = null;
        return true;

    }

    private static double getGrowthProgressPerCycle(ISeedData aCrop) {
        // calc unscaled growth speed of crops.
        // All crops use the same amplified environmental score, so crops with more listed biome
        // preferences do not gain additional output merely because their preference list is longer.
        int tUnscaledGrowthSpeed = TileEntityCropSticks.getGrowthRate(
            SIMULATED_NUTRIENT_SCORE,
            aCrop.getCrop()
                .getTier(),
            aCrop.getStats()
                .getGrowth());
        if (tUnscaledGrowthSpeed <= 0) return -1;
        // calculate percentage grown each tick up to 100% since growth
        // don't carry over if you wait to harvest in world crops.
        // this is intentional balancing and shouldn't be included in the future mega multi.
        int cropGrowthDuration = aCrop.getCrop()
            .getGrowthDuration();
        int growthTicksPerHarvest = cropGrowthDuration / tUnscaledGrowthSpeed;
        if (cropGrowthDuration % tUnscaledGrowthSpeed != 0) growthTicksPerHarvest++;
        // calculate percent progress per growth tick
        double growthPercentPerGrowthTick = 1.0d / growthTicksPerHarvest;
        // scale it to the cycle's rate and apply growth speed multipliers
        return growthPercentPerGrowthTick * CYCLE_TICK_RATE_SCALAR;
    }

    @Desugar
    public record CropCache(boolean hybrid, Pair<ItemStack, Double>[] cachedOutput, int seedCount) {

        public ItemStack[] getOutputStacks(double multiplier) {
            return CropsNHFarm.getOutputStacks(cachedOutput, multiplier);
        }

        public CropCache withSeedCount(int count) {
            return new CropCache(hybrid, cachedOutput, Math.max(1, count));
        }
    }
}
