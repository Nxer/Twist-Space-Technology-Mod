package com.Nxer.TwistSpaceTechnology.common.egs;

import static kubatech.api.utils.ItemUtils.readItemStackFromNBT;
import static kubatech.api.utils.ItemUtils.writeItemStackToNBT;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.IPlantable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.mixin.interfaces.accessors.IBlockStemAccessor;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.Ic2Items;
import ic2.core.crop.IC2Crops;
import ic2.core.crop.TileEntityCrop;
import kubatech.api.eig.EIGDropTable;

public class EGSBucket {

    public static final IEGSBucketFactory factory = new EGSBucket.Factory();
    public static final String NBT_IDENTIFIER = "GREENHOUSE";
    private static final int NUMBER_OF_DROPS_TO_SIMULATE = 1000;

    public static class Factory implements IEGSBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EGSBucket tryCreateBucket(TST_MegaTreeFarm greenhouse) {
            return new EGSBucket(greenhouse);
        }

        @Override
        public EGSBucket restore(NBTTagCompound nbt) {
            return new EGSBucket(nbt);
        }
    }

    protected @Nullable ItemStack seed = null;
    protected int seedCount = 0;
    protected boolean isValid = false;
    protected EIGDropTable drops = new EIGDropTable();

    public EGSBucket(@NotNull TST_MegaTreeFarm greenhouse) {
        UpdateBucket(greenhouse);
    }

    public EGSBucket(@NotNull NBTTagCompound nbt) {
        this.seed = readItemStackFromNBT(nbt.getCompoundTag("seed"));
        this.seedCount = nbt.getInteger("count");
    }

    /**
     * Creates a persistent save of the bucket's current data.
     *
     * @return The nbt data for this bucket.
     */
    public NBTTagCompound save() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", this.getNBTIdentifier());
        if (this.seed != null) {
            nbt.setTag("seed", writeItemStackToNBT(this.seed));
        }
        nbt.setInteger("count", this.seedCount);
        return nbt;
    }

    /**
     * Gets an item stack representing the seeds in this bucket
     *
     * @return an item stack representing the seeds in this bucket.
     */
    public ItemStack getSeedStack() {
        if (this.seed != null) {
            ItemStack copied = seed.copy();
            copied.stackSize = seedCount;
            return copied;
        }
        return null;
    }

    /**
     * Gets the number of seeds in this bucket
     *
     * @return gets the number of seeds in this bucket.
     */
    public int getSeedCount() {
        return seedCount;
    }

    /**
     * Gets the display name of the seed in this bucket
     *
     * @return The display name of the seed.
     */
    public String getDisplayName() {
        if (seed != null) {
            return seed.getDisplayName();
        }
        return "No valid seed found";
    }

    public @NotNull String getInfoData() {
        StringBuilder sb = new StringBuilder();
        // display invalid buckets, we don't want people to think they lost their seeds or something.
        sb.append(this.isValid() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED);
        sb.append("x");
        sb.append(this.getSeedCount());
        sb.append(" ");
        sb.append(this.getDisplayName());
        this.getAdditionalInfoData(sb);
        sb.append(EnumChatFormatting.RESET);
        return sb.toString();
    }

    protected void getAdditionalInfoData(StringBuilder sb) {}

    /**
     * Updates the bucket to the latest seeds
     */
    public void UpdateBucket(@NotNull TST_MegaTreeFarm greenhouse) {
        // Abort is input if empty
        if (greenhouse.getControllerSlot() == null || greenhouse.getControllerSlot().stackSize <= 0) return;

        seed = greenhouse.getControllerSlot()
            .copy();

        // no support items, remove seed reference
        if (!isSeedSupported()) {
            seed = null;
            seedCount = 0;
            return;
        }

        // When we have a valid seed make more
        createMoreSeeds(greenhouse);

        seed = greenhouse.getControllerSlot()
            .copy();
        seedCount = greenhouse.getControllerSlot().stackSize;

        revalidate(greenhouse);
    }

    private void createMoreSeeds(@NotNull TST_MegaTreeFarm greenhouse) {
        greenhouse.getControllerSlot().stackSize = 64;

        if (ItemList.IC2_Crop_Seeds.isStackEqual(greenhouse.getControllerSlot(), true, true)) {
            if (greenhouse.getControllerSlot()
                .hasTagCompound()) {
                NBTTagCompound nbt = greenhouse.getControllerSlot()
                    .getTagCompound();
                if (nbt.hasKey("growth") && nbt.hasKey("gain") && nbt.hasKey("resistance")) {
                    nbt.setInteger("growth", 31);
                    nbt.setInteger("gain", 31);
                    nbt.setInteger("resistance", 1);
                }
            }
        }
    }

    /**
     * Returns true if the bucket can output items.
     *
     * @return true if the bucket is valid.
     */
    public boolean isValid() {
        return this.seed != null && this.seedCount > 0 && this.isValid;
    }

    /**
     * Gets the identifier used to identify this class during reconstruction
     *
     * @return the identifier for this bucket type.
     */
    protected String getNBTIdentifier() {
        return NBT_IDENTIFIER;
    }

    /**
     * Adds item drops to the item tracker.
     *
     * @param multiplier A multiplier to apply to the output.
     * @param tracker    The item drop tracker
     */
    public void addProgress(double multiplier, EIGDropTable tracker) {
        if (!isValid()) return;
        drops.addTo(tracker, multiplier * this.seedCount);
    }

    /**
     * Attempts to revalidate a seed bucket. If it returns false, attempt to seed and support items and delete the
     * bucket.
     *
     * @param greenhouse The greenhouse that contains the bucket.
     * @return True if the bucket was successfully validated. {@link EGSBucket#isValid()} should also return true.
     */
    public boolean revalidate(TST_MegaTreeFarm greenhouse) {
        recalculateDrops(greenhouse);
        return isValid();
    }

    public void recalculateDrops(TST_MegaTreeFarm greenhouse) {
        this.isValid = false;

        if (seed == null) {
            return;
        }

        EIGDropTable drops = new EIGDropTable();

        Item item = seed.getItem();

        // Ic2 check
        if (ItemList.IC2_Crop_Seeds.isStackEqual(seed, true, true)) {
            if (seed.hasTagCompound()) {
                NBTTagCompound nbt = seed.getTagCompound();
                if (nbt.hasKey("growth") && nbt.hasKey("gain") && nbt.hasKey("resistance")) {
                    CropCard cropCard = IC2Crops.instance.getCropCard(seed);
                    if (cropCard != null) {
                        FakeTileEntityCrop crop;

                        int[] xyz = new int[] { 0, 0, 0 };

                        xyz[0] += greenhouse.getBaseMetaTileEntity()
                            .getXCoord();
                        xyz[1] += greenhouse.getBaseMetaTileEntity()
                            .getYCoord();
                        xyz[2] += greenhouse.getBaseMetaTileEntity()
                            .getZCoord();

                        try {
                            crop = new EGSBucket.FakeTileEntityCrop(this, greenhouse, xyz);

                            crop.setSize((byte) cropCard.maxSize());
                            // check if the crop can be harvested at its max size
                            // Eg: the Eating plant cannot be harvested at its max size of 6, only 4 or 5 can
                            if (!cropCard.canBeHarvested(crop)) {
                                crop.setSize((byte) (cropCard.maxSize() - 2));
                                if (!cropCard.canBeHarvested(crop)) {
                                    return; // not supported
                                }
                            }

                            // Multiply drop sizes by the average number drop rounds per harvest.
                            double avgDropRounds = getRealAverageDropRounds(crop, cropCard);
                            double avgStackIncrease = getRealAverageDropIncrease(crop, cropCard);

                            for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
                                // try generating some loot drop
                                ItemStack drop = cropCard.getGain(crop);
                                if (drop == null || drop.stackSize <= 0) continue;

                                // Merge the new drop with the current loot table.
                                double avgAmount = (drop.stackSize + avgStackIncrease) * avgDropRounds;
                                drops.addDrop(drop, avgAmount / NUMBER_OF_DROPS_TO_SIMULATE);
                            }

                            if (drops.isEmpty()) {
                                return;
                            }

                            this.drops = drops;
                            this.isValid = true;
                        } catch (Exception e) {
                            e.printStackTrace(System.err);
                        }
                    }
                }
            }
        }

        if (item instanceof IPlantable plantable) {
            Block plantBlock = null;

            if (plantable instanceof ItemSeeds itemSeeds) {
                plantBlock = itemSeeds.getPlant(
                    greenhouse.getBaseMetaTileEntity()
                        .getWorld(),
                    0,
                    0,
                    0);
            } else if (plantable instanceof ItemSeedFood itemSeedFood) {
                plantBlock = itemSeedFood.getPlant(
                    greenhouse.getBaseMetaTileEntity()
                        .getWorld(),
                    0,
                    0,
                    0);
            } else {
                plantBlock = plantable.getPlant(
                    greenhouse.getBaseMetaTileEntity()
                        .getWorld(),
                    0,
                    0,
                    0);
            }

            // if we know some crops needs a specific metadata, remap here
            int metadata = -1;

            if (plantBlock instanceof BlockStem blockStem) {
                Block cropBlock = ((IBlockStemAccessor) blockStem).gt5u$getCropBlock();
                metadata = 0;
                if (cropBlock != null && cropBlock != Blocks.air) {
                    // Simulate drops
                    for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
                        // simulate 1 round of drops
                        ArrayList<ItemStack> blockDrops = cropBlock.getDrops(
                            greenhouse.getBaseMetaTileEntity()
                                .getWorld(),
                            greenhouse.getBaseMetaTileEntity()
                                .getXCoord(),
                            greenhouse.getBaseMetaTileEntity()
                                .getYCoord(),
                            greenhouse.getBaseMetaTileEntity()
                                .getZCoord(),
                            metadata,
                            0);
                        if (blockDrops == null || blockDrops.isEmpty()) continue;
                        // if the droped item is a block that places itself, assume this is the only possible drop
                        // eg: pumpkin, redlon
                        if (i == 0 && blockDrops.size() == 1) {
                            ItemStack drop = blockDrops.get(0);
                            if (drop != null && drop.stackSize >= 1
                                && drop.getItem() == Item.getItemFromBlock(cropBlock)) {
                                drops.addDrop(drop, drop.stackSize);
                                break;
                            }
                        }
                        // else append all the drops
                        for (ItemStack drop : blockDrops) {
                            drops.addDrop(drop, drop.stackSize / (double) NUMBER_OF_DROPS_TO_SIMULATE);
                        }
                    }
                }
            } else if (plantBlock != null) {
                metadata = 7;

                // Natura crops have an optimal harvest stage of 8.
                GameRegistry.UniqueIdentifier u = GameRegistry.findUniqueIdentifierFor(item);
                if (u != null && Objects.equals(u.modId, "Natura")) metadata = 8;

                // Pre-Generate drops.
                for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
                    ArrayList<ItemStack> plantDrops = plantBlock.getDrops(
                        greenhouse.getBaseMetaTileEntity()
                            .getWorld(),
                        greenhouse.getBaseMetaTileEntity()
                            .getXCoord(),
                        greenhouse.getBaseMetaTileEntity()
                            .getYCoord(),
                        greenhouse.getBaseMetaTileEntity()
                            .getZCoord(),
                        metadata,
                        0);
                    for (ItemStack drop : plantDrops) {
                        drops.addDrop(drop, drop.stackSize);
                    }
                }

                // reduce the number of drops to account for the seeds
                // make a safe copy of the seed just in case
                ItemStack seedSafe = seed.copy();
                seedSafe.stackSize = 1;
                // first check if we dropped an item identical to our seed item.
                int inputSeedDropCountAfterRemoval = (int) Math.round(drops.getItemAmount(seedSafe))
                    - NUMBER_OF_DROPS_TO_SIMULATE;
                if (inputSeedDropCountAfterRemoval > 0) {
                    drops.setItemAmount(seedSafe, inputSeedDropCountAfterRemoval);
                } else {
                    drops.removeItem(seedSafe);
                }

                // reduce drop count to account for the number of simulations
                drops.entrySet()
                    .forEach(x -> x.setValue(x.getValue() / NUMBER_OF_DROPS_TO_SIMULATE));
            }
        }

        // make sure we actually got a drop
        if (drops.isEmpty()) return;

        // and we are good, see ya.
        this.drops = drops;
        this.isValid = true;
    }

    public boolean isSeedSupported() {
        if (seed == null) {
            return false;
        }

        // Ic2 check
        if (ItemList.IC2_Crop_Seeds.isStackEqual(seed, true, true)) {
            if (seed.hasTagCompound()) {
                NBTTagCompound nbt = seed.getTagCompound();
                if (nbt.hasKey("growth") && nbt.hasKey("gain") && nbt.hasKey("resistance")) {
                    CropCard cropCard = IC2Crops.instance.getCropCard(seed);
                    if (cropCard != null) {
                        return true;
                    }
                }
            }
        }

        Item item = seed.getItem();

        if (item == Items.reeds) {
            return true;
        }

        if (item instanceof IPlantable) {
            return true;
        }

        Block block = Block.getBlockFromItem(item);

        if (block == Blocks.cactus) {
            return true;
        }

        if (block instanceof BlockFlower) {
            return true;
        }

        if (block instanceof BlockStem) {
            return true;
        }

        // Cannot support rainbow cactus atm
        // if (block instanceof BlockRainbowCactus)
        // {
        // return true;
        // }

        return false;
    }

    /**
     * Calculates the average number of separate item drops to be rolled per harvest using information obtained by
     * decompiling IC2.
     *
     * @see TileEntityCrop#harvest_automated(boolean)
     * @param te The {@link TileEntityCrop} holding the crop
     * @param cc The {@link CropCard} of the seed
     * @return The average number of drops to computer per harvest
     */
    private static double getRealAverageDropRounds(TileEntityCrop te, CropCard cc) {
        // this should be ~99.995% accurate
        double chance = (double) cc.dropGainChance() * powInt(1.03, te.getGain());
        // this is essentially just performing an integration using the composite trapezoidal rule.
        double min = -10, max = 10;
        int steps = 10000;
        double stepSize = (max - min) / steps;
        double sum = 0;
        for (int k = 1; k <= steps - 1; k++) {
            sum += getWeightedDropChance(min + k * stepSize, chance);
        }
        double minVal = getWeightedDropChance(min, chance);
        double maxVal = getWeightedDropChance(max, chance);
        return stepSize * ((minVal + maxVal) / 2 + sum);
    }

    /**
     * Computes base raised to the power of an integer exponent.
     * Typically faster than {@link java.lang.Math#pow(double, double)} when {@code exp} is an integer.
     */
    private static double powInt(double base, int exp) {
        if (exp > 0) return powBySquaring(base, exp);
        if (exp < 0) return 1.0 / powBySquaring(base, -exp);
        return 1.0;
    }

    /**
     * Computes base raised to non-negative integer exponent.
     */
    private static double powBySquaring(double base, int exp) {
        if (base == 2) return 1 << exp;
        if (base == 4) return 1 << 2 * exp;
        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) result *= base;
            base *= base;
            exp >>= 1;
        }
        return result;
    }

    /**
     * Evaluates the value of y for a standard normal distribution
     *
     * @param x The value of x to evaluate
     * @return The value of y
     */
    private static double stdNormDistr(double x) {
        return Math.exp(-0.5 * (x * x)) / SQRT2PI;
    }

    private static final double SQRT2PI = Math.sqrt(2.0d * Math.PI);

    /**
     * Calculates the weighted drop chance using
     *
     * @param x      The value rolled by nextGaussian
     * @param chance the base drop chance
     * @return the weighted drop chance
     */
    private static double getWeightedDropChance(double x, double chance) {
        return Math.max(0L, Math.round(x * chance * 0.6827d + chance)) * stdNormDistr(x);
    }

    /**
     * Calculates the average drop of the stack size caused by seed's gain using information obtained by
     * decompiling IC2.
     *
     * @see TileEntityCrop#harvest_automated(boolean)
     * @param te The {@link TileEntityCrop} holding the crop
     * @param cc The {@link CropCard} of the seed
     * @return The average number of drops to computer per harvest
     */
    private static double getRealAverageDropIncrease(TileEntityCrop te, CropCard cc) {
        // yes gain has the amazing ability to sometimes add 1 to your stack size!
        return (te.getGain() + 1) / 100.0d;
    }

    private static class FakeTileEntityCrop extends TileEntityCrop {

        public boolean isValid = false;
        private int lightLevel = 0;

        public FakeTileEntityCrop(EGSBucket bucket, TST_MegaTreeFarm greenhouse, int[] xyz) {
            super();
            this.isValid = false;
            this.ticker = 1;

            if (bucket.seed == null) {
                return;
            }

            // put seed in crop stick
            CropCard cc = Crops.instance.getCropCard(bucket.seed);
            this.setCrop(cc);
            NBTTagCompound nbt = bucket.seed.getTagCompound();
            this.setGrowth(nbt.getByte("growth"));
            this.setGain(nbt.getByte("gain"));
            this.setResistance(nbt.getByte("resistance"));
            this.setWorldObj(
                greenhouse.getBaseMetaTileEntity()
                    .getWorld());

            this.xCoord = xyz[0];
            this.yCoord = xyz[1];
            this.zCoord = xyz[2];
            this.blockType = Block.getBlockFromItem(Ic2Items.crop.getItem());
            this.blockMetadata = 0;

            this.isValid = canMature();
        }

        public boolean canMature() {
            CropCard cc = this.getCrop();
            size = cc.maxSize() - 1;
            // try with a high light level
            lightLevel = this.getLightLevel();
            if (cc.canGrow(this)) return true;
            // and then with a low light level.
            lightLevel = 9;
            return cc.canGrow(this);
        }

        @Override
        public boolean isBlockBelow(Block reqBlock) {
            return true;
        }

        @Override
        public boolean isBlockBelow(String oreDictionaryName) {
            return true;
        }

        // region environment simulation

        @Override
        public int getLightLevel() {
            // 9 should allow most light dependent crops to grow
            // the only exception I know of the eating plant which checks
            return lightLevel;
        }

        @Override
        public byte getHumidity() {
            return 127;
        }

        @Override
        public byte updateHumidity() {
            return 127;
        }

        @Override
        public byte getNutrients() {
            return 127;
        }

        @Override
        public byte updateNutrients() {
            return 127;
        }

        @Override
        public byte getAirQuality() {
            return 127;
        }

        @Override
        public byte updateAirQuality() {
            return 127;
        }

        @Override
        public int getWeedExStorage() {
            return 200;
        }

        @Override
        public void setWeedExStorage(int weedExStorage) {}

        @Override
        public int getHydrationStorage() {
            return 200;
        }

        @Override
        public void setHydrationStorage(int hydrationStorage) {}

        @Override
        public int getNutrientStorage() {
            return 200;
        }

        @Override
        public void setNutrientStorage(int weedExStorage) {}

        // endregion environment simulation
    }
}
