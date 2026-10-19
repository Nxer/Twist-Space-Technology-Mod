package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.MachineParameters;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.github.bsideup.jabel.Desugar;

import goodgenerator.blocks.tileEntity.MTENeutronActivator;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;
import kubatech.loaders.DEFCRecipes;

/** Startup machine directory and per-controller pool inventory; no runtime registry scans. */
public final class BottledChaosMachineConfiguration implements MachineParameters {

    public static final int MAX_POOLS = Long.SIZE;
    public static final int MACHINE_LIMIT = 4096;
    private static RecipeMap<?>[] pools = new RecipeMap<?>[0];
    private static final Map<RecipeMap<?>, Integer> poolIndexes = new IdentityHashMap<>();
    private static long[] machinePools = new long[0];
    private static int purificationPlantSlot = -1;
    private static long purificationPools;

    // This pool identifier is also the NBT key for installed plant modules.
    private static final String PURIFICATION_PLANT_KEY = "item.tst.bottled_chaos.machine_module.purification_plant";

    private final double[] machineSpeeds = new double[MAX_POOLS];
    private final BottledChaosStructureCatalog.Structure[] machineStructures = new BottledChaosStructureCatalog.Structure[MAX_POOLS];
    private final long[] machineQuantities = new long[MAX_POOLS];
    private long supportedPools;
    private long debugPools;
    private NBTTagCompound pendingMachines;
    private long requiredPools;
    private final ItemStackHandler machines;
    private final ItemStackHandler retired;

    public BottledChaosMachineConfiguration(Runnable changed) {
        Arrays.fill(machineSpeeds, 1);
        machines = new ItemStackHandler(MAX_POOLS) {

            @Override
            public int getSlotLimit(int slot) {
                return MACHINE_LIMIT;
            }

            @Override
            protected int getStackLimit(int slot, ItemStack stack) {
                return stack == null ? 0 : ItemBottledChaosMachineModule.isModule(stack) ? 1 : MACHINE_LIMIT;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return isPoolRequired(slot) && supportsPool(slot, stack);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
            }

            @Override
            protected void onContentsChanged(int slot) {
                ItemStack stack = getStackInSlot(slot);
                // Paid structure validation belongs to inventory changes, never the controller's tick loop.
                machineStructures[slot] = ItemBottledChaosMachineModule.isModule(stack)
                    ? ItemBottledChaosMachineModule.structure(stack)
                    : null;
                machineQuantities[slot] = quantity(stack);
                machineSpeeds[slot] = ItemBottledChaosMachineModule.quantitySpeed(machineQuantities[slot]);
                long bit = 1L << slot;
                boolean debug = GTCMItemList.TestItem0.equal(stack);
                debugPools = debug ? debugPools | bit : debugPools & ~bit;
                boolean supported = stack != null && stack.stackSize > 0
                    && (debug || machineStructures[slot] != null && machineQuantities[slot] > 0
                        && (machineStructures[slot].poolMask() & bit) != 0);
                supportedPools = supported ? supportedPools | bit : supportedPools & ~bit;
                changed.run();
            }
        };
        retired = new ItemStackHandler(MAX_POOLS) {

            @Override
            public int getSlotLimit(int slot) {
                return MACHINE_LIMIT;
            }

            @Override
            protected int getStackLimit(int slot, ItemStack stack) {
                return stack == null ? 0 : MACHINE_LIMIT;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return false;
            }

            @Override
            protected void onContentsChanged(int slot) {
                changed.run();
            }
        };
    }

    /** Uses the same public interface as GT's NEI machine catalyst registration. */
    public static void initialize(RecipeMap<?>[] selectedPools) {
        pools = selectedPools.clone();
        Arrays.sort(pools, Comparator.comparing(map -> map.unlocalizedName));
        purificationPools = 0;
        for (int slot = 0; slot < pools.length; slot++)
            if (isPurificationPool(pools[slot])) purificationPools |= 1L << slot;
        purificationPlantSlot = purificationPools == 0 ? -1 : pools.length;
        if (poolCount() > MAX_POOLS) throw new IllegalStateException("Too many Bottled Chaos machine slots");
        poolIndexes.clear();
        for (int slot = 0; slot < pools.length; slot++) poolIndexes.put(pools[slot], slot);
        machinePools = new long[GregTechAPI.METATILEENTITIES.length];
        for (int id = 0; id < machinePools.length; id++) {
            IMetaTileEntity machine = GregTechAPI.METATILEENTITIES[id];
            if (machine instanceof MTEPurificationPlant) {
                machinePools[id] = purificationPlantMask();
                continue;
            }
            if (!(machine instanceof RecipeMapWorkable workable)) continue;
            try {
                for (RecipeMap<?> map : workable.getAvailableRecipeMaps()) machinePools[id] |= mask(map);
                // Both activators consume the same converted source recipes; the ordinary machine keeps its own speed.
                if (machine instanceof MTENeutronActivator)
                    machinePools[id] |= mask(GTCMRecipe.NeutronActivatorWithEURecipeMap);
            } catch (RuntimeException exception) {
                TwistSpaceTechnology.LOG.warn("Cannot inspect Bottled Chaos machine catalyst {}.", id, exception);
            }
        }
        BottledChaosStructureCatalog.initialize(machinePools);
        for (int slot = 0; slot < pools.length; slot++) {
            final long bit = 1L << slot;
            if (Arrays.stream(machinePools)
                .noneMatch(mask -> (mask & bit) != 0))
                TwistSpaceTechnology.LOG.warn(
                    "No GT machine catalyst is registered for Bottled Chaos pool {}.",
                    pools[slot].unlocalizedName);
        }
    }

    public static int poolCount() {
        return pools.length + (purificationPlantSlot < 0 ? 0 : 1);
    }

    public static String poolKey(int slot) {
        return isPurificationPlantSlot(slot) ? PURIFICATION_PLANT_KEY : pools[slot].unlocalizedName;
    }

    /** Auxiliary plant slots have no recipe map or processing parameters. */
    public static RecipeMap<?> pool(int slot) {
        return isPurificationPlantSlot(slot) ? null : pools[slot];
    }

    public static String poolName(int slot) {
        return poolName(poolKey(slot));
    }

    public static String poolName(RecipeMap<?> map) {
        return poolName(map.unlocalizedName);
    }

    public static String poolName(String key) {
        if (key.equals(PURIFICATION_PLANT_KEY)) {
            // #tr tst.bottledchaos.machine_module.purification_plant
            // # Main Water Purification Plant
            // #zh_CN 主净水厂
            return StatCollector.translateToLocal("tst.bottledchaos.machine_module.purification_plant");
        }
        if (key.equals(GTCMRecipe.NeutronActivatorWithEURecipeMap.unlocalizedName)
            || key.equals("gg.recipe.neutron_activator")) {
            // #tr tst.bottledchaos.machine_module.neutron_pool
            // # Neutron Activator
            // #zh_CN 中子活化器
            return StatCollector.translateToLocal("tst.bottledchaos.machine_module.neutron_pool");
        }
        String name = StatCollector.translateToLocal(key);
        if (key.startsWith("gt.recipe.purificationplant")) {
            // #tr tst.bottledchaos.machine_module.purification_pool
            // # Purified Water-%s
            // #zh_CN 净化水-%s
            return StatCollector.translateToLocalFormatted("tst.bottledchaos.machine_module.purification_pool", name);
        }
        return name.replaceFirst("^(大型|工业|巨型|Large |Industrial |Mega )", "");
    }

    public static boolean isPurificationPool(RecipeMap<?> map) {
        return map == RecipeMaps.purificationClarifierRecipes || map == RecipeMaps.purificationOzonationRecipes
            || map == RecipeMaps.purificationFlocculationRecipes
            || map == RecipeMaps.purificationPhAdjustmentRecipes
            || map == RecipeMaps.purificationPlasmaHeatingRecipes
            || map == RecipeMaps.purificationUVTreatmentRecipes
            || map == RecipeMaps.purificationDegasifierRecipes
            || map == RecipeMaps.purificationParticleExtractionRecipes;
    }

    public static boolean isPurificationPlantSlot(int slot) {
        return slot >= 0 && slot == purificationPlantSlot;
    }

    public static long purificationPlantMask() {
        return purificationPlantSlot < 0 ? 0 : 1L << purificationPlantSlot;
    }

    public static long withDependencies(long mask) {
        return (mask & purificationPools) == 0 ? mask : mask | purificationPlantMask();
    }

    public static String poolKeys(long mask) {
        StringJoiner keys = new StringJoiner(";");
        for (int slot = 0; slot < poolCount(); slot++) if ((mask & (1L << slot)) != 0) keys.add(poolKey(slot));
        return keys.toString();
    }

    public static long maskFromKeys(String keys) {
        long mask = 0;
        for (String key : keys.split(";"))
            for (int slot = 0; slot < poolCount(); slot++) if (poolKey(slot).equals(key)) mask |= 1L << slot;
        return withDependencies(mask);
    }

    public static long mask(RecipeMap<?> map) {
        Integer slot = poolIndexes.get(map);
        return slot == null ? 0 : 1L << slot;
    }

    public static boolean supportsPool(int slot, ItemStack stack) {
        if (slot < 0 || slot >= poolCount() || stack == null || stack.stackSize <= 0) return false;
        if (GTCMItemList.TestItem0.equal(stack)) return true;
        if (ItemBottledChaosMachineModule.isModule(stack)) {
            var structure = ItemBottledChaosMachineModule.structure(stack);
            return structure != null && ItemBottledChaosMachineModule.quantity(stack) > 0
                && (structure.poolMask() & (1L << slot)) != 0;
        }
        return false;
    }

    public ItemStackHandler machines() {
        loadPendingMachines();
        return machines;
    }

    public ItemStackHandler retired() {
        return retired;
    }

    public long requiredPoolMask() {
        return requiredPools;
    }

    public void setRequiredPoolMask(long mask) {
        requiredPools = withDependencies(mask);
    }

    public boolean isPoolRequired(int slot) {
        return (requiredPools & (1L << slot)) != 0;
    }

    /** A full retired inventory leaves the remainder in its original, extraction-only slot. */
    public void updateRequiredPools(long mask) {
        loadPendingMachines();
        requiredPools = withDependencies(mask);
        for (int slot = 0; slot < MAX_POOLS; slot++) {
            if (isPoolRequired(slot)) continue;
            ItemStack original = machines.getStackInSlot(slot);
            if (original == null) continue;
            ItemStack remainder = original.copy();
            for (int target = 0; target < retired.getSlots() && remainder != null; target++)
                remainder = retired.insertItem(target, remainder, false);
            if (remainder == null || remainder.stackSize != original.stackSize)
                machines.setStackInSlot(slot, remainder);
        }
    }

    public long installedPoolMask() {
        loadPendingMachines();
        long mask = requiredPools & supportedPools;
        long plantQuantity = purificationPlantQuantity();
        for (int slot = 0; slot < poolCount(); slot++) {
            long bit = 1L << slot;
            if ((purificationPools & bit) != 0 && machineQuantities[slot] > plantQuantity) mask &= ~bit;
        }
        return mask;
    }

    private long purificationPlantQuantity() {
        if (purificationPlantSlot < 0) return 0;
        return (supportedPools & purificationPlantMask()) != 0 ? machineQuantities[purificationPlantSlot] : 0;
    }

    private static long quantity(ItemStack stack) {
        return ItemBottledChaosMachineModule.isModule(stack) ? ItemBottledChaosMachineModule.quantity(stack)
            : stack == null ? 0 : stack.stackSize;
    }

    public static int quantitySpeed(int count) {
        return ItemBottledChaosMachineModule.quantitySpeed(count);
    }

    @Override
    public double speedMultiplier(RecipeMap<?> map) {
        Integer slot = poolIndexes.get(map);
        loadPendingMachines();
        return slot == null ? 1 : machineSpeeds[slot];
    }

    @Override
    public OverclockType overclock(RecipeMap<?> map) {
        return MachineParameters.super.overclock(map);
    }

    /** One candidate's immutable inventory snapshot; source requirements never leak into another credential. */
    public RuntimeParameters runtime(long inputVoltage, long inputPower, ItemStack[] items, FluidStack[] supplyFluids,
        double masterTime, double masterEU, long masterParallel) {
        loadPendingMachines();
        return new RuntimeParameters(
            inputVoltage,
            inputPower,
            items,
            supplyFluids,
            masterTime,
            masterEU,
            masterParallel);
    }

    public final class RuntimeParameters implements MachineParameters {

        private final long inputVoltage;
        private final long inputPower;
        private final ItemStack[] items;
        private final FluidStack[] fluids;
        private final double masterTime;
        private final double masterEU;
        private final long masterParallel;
        private final long purificationPlantQuantity;
        private final ItemStack[] controllers = new ItemStack[MAX_POOLS];
        private final BottledChaosStructureCatalog.Structure[] structures = new BottledChaosStructureCatalog.Structure[MAX_POOLS];
        private final long[] quantities = new long[MAX_POOLS];
        private final double[] quantitySpeeds = machineSpeeds.clone();
        private final boolean[] debug = new boolean[MAX_POOLS];
        private final boolean[] configured = new boolean[MAX_POOLS];
        private final Map<SourceKey, BottledChaosMachineParameters.Profile> profiles = new HashMap<>();
        private final List<BottledChaosUpkeep.Demand> sourceUsage = new ArrayList<>();
        private final BottledChaosMachineParameters.Profile[] currentProfiles = new BottledChaosMachineParameters.Profile[MAX_POOLS];
        private final CheckRecipeResult[] currentFailures = new CheckRecipeResult[MAX_POOLS];
        private CheckRecipeResult sourceFailure = CheckRecipeResultRegistry.NO_RECIPE;

        private RuntimeParameters(long inputVoltage, long inputPower, ItemStack[] items, FluidStack[] fluids,
            double masterTime, double masterEU, long masterParallel) {
            this.inputVoltage = inputVoltage;
            this.inputPower = inputPower;
            this.items = items;
            this.fluids = fluids;
            this.masterTime = masterTime;
            this.masterEU = masterEU;
            this.masterParallel = masterParallel;
            purificationPlantQuantity = purificationPlantQuantity();
            for (int slot = 0; slot < pools.length; slot++) {
                ItemStack stack = machines.getStackInSlot(slot);
                if (stack == null) continue;
                debug[slot] = (debugPools & (1L << slot)) != 0;
                structures[slot] = machineStructures[slot];
                controllers[slot] = structures[slot] != null ? structures[slot].controller()
                    : ItemBottledChaosMachineModule.isModule(stack) ? null : stack;
                quantities[slot] = machineQuantities[slot];
            }
        }

        @Override
        public void configureSource(RecipeMap<?> map, long mergedRecipeVoltage, int requiredHeat, int requiredGlassTier,
            ItemStack requiredSpecialInput, boolean heavyCentrifuge) {
            Integer slot = poolIndexes.get(map);
            if (slot == null || debug[slot] || controllers[slot] == null) return;
            configured[slot] = true;
            SourceKey key = new SourceKey(
                slot,
                mergedRecipeVoltage,
                requiredHeat,
                requiredGlassTier,
                requiredSpecialInput == null ? null : GTUtility.ItemId.createNoCopy(requiredSpecialInput),
                heavyCentrifuge);
            currentFailures[slot] = null;
            currentProfiles[slot] = profiles.computeIfAbsent(
                key,
                ignored -> BottledChaosMachineParameters.evaluate(
                    controllers[slot],
                    structures[slot],
                    map,
                    mergedRecipeVoltage,
                    inputVoltage,
                    inputPower,
                    items,
                    fluids,
                    masterTime,
                    masterEU,
                    masterParallel,
                    requiredHeat,
                    requiredGlassTier,
                    requiredSpecialInput,
                    heavyCentrifuge,
                    failure -> currentFailures[slot] = failure));
        }

        private BottledChaosMachineParameters.Profile profile(RecipeMap<?> map) {
            Integer slot = poolIndexes.get(map);
            return slot == null ? null : currentProfiles[slot];
        }

        @Override
        public boolean canProcess(RecipeMap<?> map, long mergedRecipeVoltage, int requiredHeat, int requiredGlassTier,
            int requiredCasingTier, int requiredCompressionTier, boolean heavyCentrifuge, int requiredPlasmaTier,
            boolean multiStepPlasma) {
            Integer slot = poolIndexes.get(map);
            if (slot != null && (purificationPools & (1L << slot)) != 0
                && (quantities[slot] <= 0 || quantities[slot] > purificationPlantQuantity)) {
                return reject(slot, "purification_capacity", purificationPlantQuantity, Math.max(1, quantities[slot]));
            }
            var profile = profile(map);
            // Missing pools are reported by ProcessingLogic; TestItem deliberately supplies unrestricted defaults.
            if (profile == null) {
                boolean allowed = slot == null || !configured[slot];
                // #tr tst.bottledchaos.result.invalid_source_configuration
                // # Source structure configuration is not valid for this recipe pool.
                // #zh_CN 来源结构配置不适用于此配方池.
                if (!allowed) sourceFailure = currentFailures[slot] != null ? currentFailures[slot]
                    : BottledChaosPoolFailure.of(
                        poolKey(slot),
                        SimpleResultWithText.ofFailure("tst.bottledchaos.result.invalid_source_configuration"));
                return allowed;
            }
            if (profile.maximumRecipeTier() >= 0
                && mergedRecipeVoltage > gregtech.api.enums.GTValues.V[profile.maximumRecipeTier()])
                return reject(
                    slot,
                    "voltage",
                    gregtech.api.enums.GTValues.V[profile.maximumRecipeTier()],
                    mergedRecipeVoltage);
            if (requiredHeat > profile.heat()) return reject(slot, "heat", profile.heat(), requiredHeat);
            if (requiredGlassTier > profile.glassTier())
                return reject(slot, "glass", profile.glassTier(), requiredGlassTier);
            if (requiredCasingTier > 0 && requiredCasingTier > profile.casingTier())
                return reject(slot, "casing", profile.casingTier(), requiredCasingTier);
            if (requiredCompressionTier > 0 && requiredCompressionTier > profile.compressionTier())
                return reject(slot, "compression", profile.compressionTier(), requiredCompressionTier);
            if (heavyCentrifuge && !profile.heavyCentrifuge()) return reject(slot, "heavy_centrifuge", 0, 1);
            if (requiredPlasmaTier > profile.plasmaTier())
                return reject(slot, "plasma", profile.plasmaTier(), requiredPlasmaTier);
            if (multiStepPlasma && !profile.multiStepPlasma()) return reject(slot, "multi_step_plasma", 0, 1);
            return true;
        }

        private boolean reject(int slot, String parameter, long available, long required) {
            sourceFailure = BottledChaosPoolFailure.parameter(poolKey(slot), parameter, available, required);
            return false;
        }

        public CheckRecipeResult sourceFailure() {
            return sourceFailure;
        }

        @Override
        public long parallel(RecipeMap<?> map) {
            var profile = profile(map);
            return profile == null ? 1 : profile.parallel();
        }

        @Override
        public double speedMultiplier(RecipeMap<?> map) {
            var profile = profile(map);
            return profile == null ? 1 : 1 / profile.timeMultiplier();
        }

        @Override
        public double quantitySpeedMultiplier(RecipeMap<?> map) {
            Integer slot = poolIndexes.get(map);
            return slot == null ? 1 : quantitySpeeds[slot];
        }

        @Override
        public double euMultiplier(RecipeMap<?> map, int requiredHeat) {
            var profile = profile(map);
            return profile == null ? 1 : profile.euMultiplier();
        }

        @Override
        public double timeDivisor(RecipeMap<?> map, int requiredHeat) {
            var profile = profile(map);
            return profile == null ? MachineParameters.super.timeDivisor(map, requiredHeat) : profile.timeDivisor();
        }

        @Override
        public double powerMultiplier(RecipeMap<?> map, int requiredHeat) {
            var profile = profile(map);
            return profile == null ? MachineParameters.super.powerMultiplier(map, requiredHeat)
                : profile.powerMultiplier();
        }

        @Override
        public int maximumOverclocks(RecipeMap<?> map) {
            var profile = profile(map);
            return profile == null ? -1 : profile.maximumOverclocks();
        }

        @Override
        public int[] overclockLimitAlternatives(RecipeMap<?> map) {
            Integer slot = poolIndexes.get(map);
            var profile = profile(map);
            if (slot == null || profile == null || !BottledChaosComplexStructures.fourHeliocasts(structures[slot]))
                return null;
            int base = profile.maximumOverclocks();
            return new int[] { base, base + 1, base + 2, base + 3 };
        }

        @Override
        public int heatOverclocks(RecipeMap<?> map, int requiredHeat, int requiredCasingTier) {
            var profile = profile(map);
            if (profile != null && map == DEFCRecipes.fusionCraftingRecipes)
                return Math.max(0, profile.casingTier() - requiredCasingTier);
            return profile == null ? 0 : profile.heatOverclocks();
        }

        @Override
        public double minimumSourceDuration(RecipeMap<?> map) {
            var profile = profile(map);
            return profile == null ? 0 : profile.minimumSourceDuration();
        }

        @Override
        public void resetSourceUsage() {
            sourceUsage.clear();
        }

        /** Record only executed steps; rejected binary-search candidates cannot overwrite the chosen plan's fees. */
        @Override
        public void recordSourceUsage(RecipeMap<?> map) {
            Integer slot = poolIndexes.get(map);
            var profile = profile(map);
            if (slot == null || profile == null) return;
            for (FluidStack fluid : profile.upkeep()) {
                if (fluid == null || fluid.amount <= 0) continue;
                sourceUsage.add(
                    new BottledChaosUpkeep.Demand(
                        poolKey(slot),
                        fluid,
                        BigInteger.valueOf(quantities[slot])
                            .multiply(BigInteger.valueOf(fluid.amount)),
                        profile.startupReserveSeconds()));
            }
        }

        @Override
        public List<BottledChaosUpkeep.Demand> upkeepDemands() {
            return Collections.unmodifiableList(new ArrayList<>(sourceUsage));
        }
    }

    @Desugar
    private record SourceKey(int pool, long voltage, int heat, int glass, GTUtility.ItemId special,
        boolean heavyCentrifuge) {}

    public void save(NBTTagCompound nbt) {
        loadPendingMachines();
        NBTTagCompound active = pendingMachines == null ? new NBTTagCompound()
            : (NBTTagCompound) pendingMachines.copy();
        for (int slot = 0; slot < poolCount(); slot++) {
            ItemStack stack = machines.getStackInSlot(slot);
            if (stack == null) continue;
            NBTTagCompound item = stack.writeToNBT(new NBTTagCompound());
            item.setInteger("Count", stack.stackSize);
            active.setTag(poolKey(slot), item);
        }
        nbt.setTag("bottledChaosPoolMachines", active);
        nbt.setTag("bottledChaosRetiredMachines", retired.serializeNBT());
    }

    public void load(NBTTagCompound nbt) {
        pendingMachines = (NBTTagCompound) nbt.getCompoundTag("bottledChaosPoolMachines")
            .copy();
        loadPendingMachines();
        retired.deserializeNBT(nbt.getCompoundTag("bottledChaosRetiredMachines"));
    }

    private void loadPendingMachines() {
        if (pendingMachines == null || pools.length == 0) return;
        NBTTagCompound active = pendingMachines;
        pendingMachines = null;
        for (int slot = 0; slot < poolCount(); slot++) {
            NBTTagCompound item = active.getCompoundTag(poolKey(slot));
            ItemStack stack = ItemStack.loadItemStackFromNBT(item);
            if (stack != null) stack.stackSize = Math.max(0, item.getInteger("Count"));
            machines.setStackInSlot(slot, stack);
        }
    }
}
