package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

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
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.machine.MiscHelper;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MiracleDoor;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosSourceParameters.Evaluation;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing.BottledChaosUpkeep;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.result.BottledChaosPoolFailure;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.github.bsideup.jabel.Desugar;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import kubatech.loaders.DEFCRecipes;

/** Startup machine directory and independent installed modules; no runtime registry scans. */
public final class BottledChaosMachineConfiguration {

    public static final int MAX_POOLS = Long.SIZE;
    public static final int MACHINE_LIMIT = 4096;
    public static final int MODULE_SLOTS = 24;
    private static RecipeMap<?>[] pools = new RecipeMap<?>[0];
    private static final Map<RecipeMap<?>, Integer> poolIndexes = new IdentityHashMap<>();
    private static int[] displayPools = new int[0];
    private static long[] displaySources = new long[0];
    private static long[] machinePools = new long[0];
    private static int purificationPlantSlot = -1;
    private static long purificationPools;

    // Auxiliary dependency identifier; installation slots are independent of pool identifiers.
    private static final String PURIFICATION_PLANT_KEY = "item.tst.bottled_chaos.machine_module.purification_plant";

    private final double[] machineSpeeds = new double[MODULE_SLOTS];
    private final BottledChaosStructureCatalog.Structure[] machineStructures = new BottledChaosStructureCatalog.Structure[MODULE_SLOTS];
    private final BottledChaosMachineParameters.InstalledSource[] machineSources = new BottledChaosMachineParameters.InstalledSource[MODULE_SLOTS];
    private final long[] machineQuantities = new long[MODULE_SLOTS];
    private ConfigurationSnapshot configurationSnapshot;
    private long supportedPools;
    private long debugPools;
    private NBTTagCompound pendingMachines;
    private long requiredPools;
    private final ItemStackHandler machines;

    public BottledChaosMachineConfiguration(Runnable changed) {
        Arrays.fill(machineSpeeds, 1);
        machines = new ItemStackHandler(MODULE_SLOTS) {

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
                return stack != null && (GTCMItemList.TestItem0.equal(stack)
                    || ItemBottledChaosMachineModule.structure(stack) != null && quantity(stack) > 0);
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
                ItemStack controller = machineStructures[slot] == null ? null : machineStructures[slot].controller();
                machineSources[slot] = controller == null ? null
                    : new BottledChaosMachineParameters.InstalledSource(controller, machineStructures[slot]);
                if (machineSources[slot] != null) machineSpeeds[slot] /= machineSources[slot].poolSpeedDivisor();
                supportedPools = 0;
                for (int i = 0; i < MODULE_SLOTS; i++) {
                    if ((debugPools & (1L << i)) != 0) supportedPools = -1L;
                    else if (machineStructures[i] != null && machineQuantities[i] > 0)
                        supportedPools |= machineStructures[i].poolMask();
                }
                configurationSnapshot = null;
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
        displayPools = java.util.stream.IntStream.range(0, poolCount())
            .toArray();
        displaySources = new long[poolCount()];
        for (var machine : BottledChaosSupportedMachine.values()) for (var route : machine.replacements()) {
            Integer source = poolIndexes.get(route.pool());
            Integer target = poolIndexes.get(
                route.chain()
                    .get(0));
            if (source != null && target != null) displayPools[source] = target;
        }
        for (int slot = 0; slot < poolCount(); slot++) displaySources[displayPools[slot]] |= 1L << slot;
        machinePools = new long[GregTechAPI.METATILEENTITIES.length];
        for (int id = 0; id < machinePools.length; id++) {
            IMetaTileEntity machine = GregTechAPI.METATILEENTITIES[id];
            var supported = BottledChaosSupportedMachine.find(machine);
            if (supported == null) continue;
            try {
                machinePools[id] = supported.poolMask(machine);
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

    /** Query-page grouping only; native recipe identities and execution masks stay distinct. */
    public static int displayPoolSlot(int slot) {
        return displayPools[slot];
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
            // #zh_CN 主净化厂
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

    public int[] sourcesForPool(int pool) {
        loadPendingMachines();
        if (pool < 0 || pool >= poolCount()) return new int[0];
        return sourcesForMask(1L << pool);
    }

    public int[] displaySourcesForPool(int pool) {
        loadPendingMachines();
        if (pool < 0 || pool >= poolCount() || displayPools[pool] != pool) return new int[0];
        return sourcesForMask(displaySources[pool]);
    }

    private int[] sourcesForMask(long mask) {
        return java.util.stream.IntStream.range(0, MODULE_SLOTS)
            .filter(
                slot -> machineQuantities[slot] > 0 && ((debugPools & (1L << slot)) != 0
                    || machineStructures[slot] != null && (machineStructures[slot].poolMask() & mask) != 0))
            .toArray();
    }

    public ItemStack poolStack(int pool, int candidate) {
        int[] sources = displaySourcesForPool(pool);
        return sources.length == 0 ? null : machines.getStackInSlot(sources[Math.floorMod(candidate, sources.length)]);
    }

    public static String moduleName(ItemStack stack) {
        if (stack == null) return "";
        ItemStack controller = ItemBottledChaosMachineModule.controller(stack);
        return controller == null ? stack.getDisplayName() : controller.getDisplayName();
    }

    public ItemStackHandler machines() {
        loadPendingMachines();
        return machines;
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

    /** Required pools affect admission and display, never the physical module inventory. */
    public void updateRequiredPools(long mask) {
        loadPendingMachines();
        requiredPools = withDependencies(mask);
    }

    public long installedPoolMask() {
        loadPendingMachines();
        long mask = requiredPools & supportedPools;
        long plantQuantity = purificationPlantQuantity();
        for (int slot = 0; slot < poolCount(); slot++) {
            long bit = 1L << slot;
            if ((purificationPools & bit) != 0) {
                boolean available = false;
                for (int source : sourcesForPool(slot))
                    if (machineQuantities[source] <= plantQuantity) available = true;
                if (!available) mask &= ~bit;
            }
        }
        return mask;
    }

    private long purificationPlantQuantity() {
        if (purificationPlantSlot < 0) return 0;
        long quantity = 0;
        for (int source : sourcesForPool(purificationPlantSlot))
            quantity = Math.max(quantity, machineQuantities[source]);
        return quantity;
    }

    private static long quantity(ItemStack stack) {
        return ItemBottledChaosMachineModule.isModule(stack) ? ItemBottledChaosMachineModule.quantity(stack)
            : stack == null ? 0 : stack.stackSize;
    }

    /** Bounded GUI snapshot from cached installed sources; never validates packaged structures each tick. */
    public NBTTagCompound moduleParameters(int slot, int candidate, BottledChaosMachineParameters.Context context,
        List<BottledChaosUpkeep.Demand> activeDemands) {
        loadPendingMachines();
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("slot", slot);
        if (slot < 0 || slot >= poolCount()) return data;
        int[] candidates = displaySourcesForPool(slot);
        int sourceSlot = candidates.length == 0 ? -1 : candidates[Math.floorMod(candidate, candidates.length)];
        long quantity = sourceSlot < 0 ? 0 : machineQuantities[sourceSlot];
        data.setLong("quantity", quantity);
        if (isPurificationPlantSlot(slot)) {
            long needed = 0;
            for (int pool = 0; pool < pools.length; pool++)
                if (isPoolRequired(pool) && isPurificationPool(pools[pool])) for (int installed : sourcesForPool(pool))
                    needed = Math.max(needed, Math.max(1, machineQuantities[installed]));
            data.setLong("needed", needed);
            return data;
        }
        if (sourceSlot < 0) return data;
        var source = machineSources[sourceSlot];
        boolean debug = (debugPools & (1L << sourceSlot)) != 0;
        int nativeSlot = slot;
        if (!debug) for (int pool = 0; pool < pools.length; pool++) {
            if (displayPools[pool] == slot && (machineStructures[sourceSlot].poolMask() & (1L << pool)) != 0) {
                nativeSlot = pool;
                break;
            }
        }
        RecipeMap<?> nativePool = pools[nativeSlot];
        data.setInteger("sourcePool", nativeSlot);
        data.setDouble("speed", machineSpeeds[sourceSlot]);
        if ((!debug && source == null) || quantity <= 0) return data;
        var profile = debug ? Evaluation.defaults(nativePool, machineSpeeds[sourceSlot], context.sourceOverclock())
            .profile()
            : BottledChaosMachineParameters
                .evaluate(source, nativePool, 1, context, BottledChaosSourceRequirements.NONE)
                .profile();
        if (profile != null) {
            data.setBoolean("numeric", true);
            data.setLong("parallel", profile.parallel());
            data.setDouble("eu", profile.euMultiplier());
            data.setDouble("time", profile.timeMultiplier());
            data.setDouble("power", profile.powerMultiplier());
            data.setDouble("divisor", profile.timeDivisor());
            data.setInteger(
                "sourceOverclock",
                context.sourceOverclock()
                    .getID());
            data.setLong("heat", profile.heat());
            if (debug) {
                data.setBoolean("debug", true);
                return data;
            }
        } else {
            // A powerless host still has fixed structural upkeep. Do not invent recipe-dependent fuel rates.
            profile = BottledChaosMachineParameters
                .evaluate(
                    source,
                    nativePool,
                    1,
                    new BottledChaosMachineParameters.Context(
                        32,
                        32,
                        context.items(),
                        context.fluids(),
                        context.masterTime(),
                        context.masterEU(),
                        context.masterParallel(),
                        context.masterOverclock(),
                        context.wireless(),
                        context.hasOverclockController()),
                    BottledChaosSourceRequirements.NONE)
                .profile();
        }
        List<BottledChaosUpkeep.Demand> demands = new ArrayList<>();
        for (var demand : activeDemands) if (demand.installation() == sourceSlot) demands.add(demand);
        if (demands.isEmpty() && profile != null
            && source.machine != BottledChaosMachineParameters.Machine.SPINMATRON) {
            for (FluidStack fluid : profile.upkeep()) if (fluid != null && fluid.amount > 0) demands.add(
                new BottledChaosUpkeep.Demand(
                    poolKey(slot),
                    fluid,
                    BigInteger.valueOf(quantity)
                        .multiply(BigInteger.valueOf(fluid.amount)),
                    profile.startupReserveSeconds(),
                    profile.upkeepPeriodSeconds(),
                    sourceSlot));
        }
        // Multiple groups share a pool's upkeep; use its maximum rate for each fluid, as execution does.
        NBTTagList upkeep = new NBTTagList();
        for (int i = 0; i < demands.size(); i++) {
            var demand = demands.get(i);
            boolean duplicate = false;
            for (int previous = 0; previous < i; previous++) if (demands.get(previous)
                .fluid()
                .isFluidEqual(demand.fluid())) duplicate = true;
            if (duplicate) continue;
            var rate = demand.perSecond();
            for (var other : demands) if (other.fluid()
                .isFluidEqual(demand.fluid())) rate = rate.max(other.perSecond());
            if (rate.signum() <= 0) continue;
            NBTTagCompound entry = demand.fluid()
                .writeToNBT(new NBTTagCompound());
            entry.setString(
                "perSecond",
                rate.stripTrailingZeros()
                    .toPlainString());
            upkeep.appendTag(entry);
        }
        data.setTag("upkeep", upkeep);
        return data;
    }

    /** One candidate's immutable inventory snapshot; source requirements never leak into another credential. */
    public RuntimeParameters runtime(BottledChaosMachineParameters.Context context) {
        loadPendingMachines();
        if (configurationSnapshot == null) configurationSnapshot = new ConfigurationSnapshot();
        return new RuntimeParameters(configurationSnapshot, context);
    }

    /** Stable until a module changes; each planning attempt shares these read-only arrays. */
    private final class ConfigurationSnapshot {

        private final long purificationPlantQuantity = purificationPlantQuantity();
        private final long[] quantities = machineQuantities.clone();
        private final long debugMask = debugPools;
        private final double[] speeds = machineSpeeds.clone();
        private final BottledChaosMachineParameters.InstalledSource[] sources = machineSources.clone();
        private final int[][] candidates = new int[poolCount()][];

        private ConfigurationSnapshot() {
            for (int pool = 0; pool < poolCount(); pool++) candidates[pool] = sourcesForPool(pool);
        }
    }

    public final class RuntimeParameters implements BottledChaosSourceParameters {

        private final ConfigurationSnapshot installed;
        private final BottledChaosMachineParameters.Context context;
        private final Map<SourceKey, Evaluation> evaluations = new HashMap<>();
        private final List<BottledChaosUpkeep.Demand> sourceUsage = new ArrayList<>();
        private long roundItemPools;

        private RuntimeParameters(ConfigurationSnapshot installed, BottledChaosMachineParameters.Context context) {
            this.installed = installed;
            this.context = context;
        }

        @Override
        public long normalizationVoltage(long recipeVoltage) {
            return context.wireless() ? context.inputVoltage() : recipeVoltage;
        }

        @Override
        public boolean normalizeSourceEnergy() {
            return context.wireless();
        }

        @Override
        public List<Evaluation> evaluateCandidates(RecipeMap<?> map, long recipeVoltage,
            BottledChaosSourceRequirements requirements) {
            Integer pool = poolIndexes.get(map);
            if (pool == null) return Collections.singletonList(Evaluation.defaults(map, 1, context.sourceOverclock()));
            List<Evaluation> results = new ArrayList<>();
            for (int installation : installed.candidates[pool]) {
                SourceKey key = new SourceKey(pool, installation, recipeVoltage, requirements);
                results.add(
                    evaluations.computeIfAbsent(
                        key,
                        ignored -> evaluate(pool, installation, map, recipeVoltage, requirements)));
            }
            // Pool admission reports a missing installation; no uninstalled source can become a replacement.
            if (results.isEmpty()) results.add(Evaluation.defaults(map, 1, context.sourceOverclock()));
            return results;
        }

        private Evaluation evaluate(int slot, int installation, RecipeMap<?> map, long recipeVoltage,
            BottledChaosSourceRequirements requirements) {
            if ((purificationPools & (1L << slot)) != 0
                && installed.quantities[installation] > installed.purificationPlantQuantity)
                return reject(
                    slot,
                    "purification_capacity",
                    installed.purificationPlantQuantity,
                    installed.quantities[installation]);
            if ((installed.debugMask & (1L << installation)) != 0) {
                var defaults = Evaluation.defaults(map, installed.speeds[installation], context.sourceOverclock());
                return new Evaluation(
                    defaults.profile(),
                    defaults.quantitySpeedMultiplier(),
                    false,
                    -1,
                    0,
                    null,
                    null,
                    installation,
                    true);
            }
            var result = BottledChaosMachineParameters
                .evaluate(installed.sources[installation], map, recipeVoltage, context, requirements);
            var profile = result.profile();
            if (profile == null) {
                // #tr tst.bottledchaos.result.invalid_source_configuration
                // # Source structure configuration is not valid for this recipe pool.
                // #zh_CN 来源结构配置不适用于此配方池.
                return Evaluation.rejected(
                    result.failure() != null ? result.failure()
                        : BottledChaosPoolFailure.of(
                            poolKey(slot),
                            SimpleResultWithText.ofFailure("tst.bottledchaos.result.invalid_source_configuration")));
            }
            long requiredVoltage = requirements.requiredSourceVoltage() > 0 ? requirements.requiredSourceVoltage()
                : recipeVoltage;
            if (profile.maximumRecipeTier() >= 0
                && requiredVoltage > gregtech.api.enums.GTValues.V[profile.maximumRecipeTier()])
                return reject(
                    slot,
                    "voltage",
                    gregtech.api.enums.GTValues.V[profile.maximumRecipeTier()],
                    requiredVoltage);
            if (requirements.heat() > profile.heat()) return reject(slot, "heat", profile.heat(), requirements.heat());
            if (requirements.glassTier() > profile.glassTier())
                return reject(slot, "glass", profile.glassTier(), requirements.glassTier());
            if (requirements.casingTier() > 0 && requirements.casingTier() > profile.casingTier())
                return reject(slot, "casing", profile.casingTier(), requirements.casingTier());
            if (requirements.compressionTier() > 0 && requirements.compressionTier() > profile.compressionTier())
                return reject(slot, "compression", profile.compressionTier(), requirements.compressionTier());
            if (requirements.heavyCentrifuge() && !profile.heavyCentrifuge())
                return reject(slot, "heavy_centrifuge", 0, 1);
            if (requirements.plasmaTier() > profile.plasmaTier())
                return reject(slot, "plasma", profile.plasmaTier(), requirements.plasmaTier());
            if (requirements.multiStepPlasma() && !profile.multiStepPlasma())
                return reject(slot, "multi_step_plasma", 0, 1);
            int maximum = map == GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes
                ? Math.max(0, profile.casingTier() - requirements.casingTier())
                : profile.maximumOverclocks();
            int perfect = map == DEFCRecipes.fusionCraftingRecipes
                ? Math.max(0, profile.casingTier() - requirements.casingTier())
                : profile.perfectOverclocks();
            int base = profile.maximumOverclocks();
            return new Evaluation(
                profile,
                installed.speeds[installation],
                installed.sources[installation].original instanceof TST_MiracleDoor,
                maximum,
                perfect,
                installed.sources[installation].fourHeliocasts ? new int[] { base, base + 1, base + 2, base + 3 }
                    : null,
                null,
                installation,
                installed.sources[installation].upgrades.ingotMold);
        }

        private Evaluation reject(int slot, String parameter, long available, long required) {
            return Evaluation
                .rejected(BottledChaosPoolFailure.parameter(poolKey(slot), parameter, available, required));
        }

        @Override
        public boolean isSourceAvailable(RecipeMap<?> map) {
            Integer slot = poolIndexes.get(map);
            return slot != null && installed.candidates[slot].length > 0;
        }

        @Override
        public void resetSourceUsage() {
            sourceUsage.clear();
            roundItemPools = 0;
        }

        /** Record only executed steps; rejected binary-search candidates cannot overwrite the chosen plan's fees. */
        @Override
        public void recordSourceUsage(RecipeMap<?> map, Evaluation source) {
            Integer slot = poolIndexes.get(map);
            var profile = source.profile();
            int installation = source.installation();
            if (slot == null || profile == null || installation < 0) return;
            if (source.energyOnly()) roundItemPools |= 1L << installation;
            for (FluidStack fluid : profile.upkeep()) {
                if (fluid == null || fluid.amount <= 0) continue;
                sourceUsage.add(
                    new BottledChaosUpkeep.Demand(
                        poolKey(slot),
                        fluid,
                        BigInteger.valueOf(installed.quantities[installation])
                            .multiply(BigInteger.valueOf(fluid.amount)),
                        profile.startupReserveSeconds(),
                        profile.upkeepPeriodSeconds(),
                        installation));
            }
        }

        @Override
        public List<BottledChaosUpkeep.Demand> upkeepDemands() {
            return Collections.unmodifiableList(new ArrayList<>(sourceUsage));
        }

        @Override
        public List<BottledChaosUpkeep.ItemDemand> roundItemDemands() {
            if (roundItemPools == 0) return Collections.emptyList();
            List<BottledChaosUpkeep.ItemDemand> demands = new ArrayList<>();
            for (int slot = 0; slot < MODULE_SLOTS; slot++) {
                if ((roundItemPools & (1L << slot)) != 0) demands.add(
                    new BottledChaosUpkeep.ItemDemand(
                        poolKeys(installed.sources[slot].structure.poolMask()),
                        MiscHelper.CRITICAL_PHOTON,
                        installed.quantities[slot]));
            }
            return demands;
        }
    }

    @Desugar
    private record SourceKey(int pool, int installation, long voltage, BottledChaosSourceRequirements requirements) {}

    public void save(NBTTagCompound nbt) {
        loadPendingMachines();
        NBTTagList items = new NBTTagList();
        for (int slot = 0; slot < MODULE_SLOTS; slot++) {
            ItemStack stack = machines.getStackInSlot(slot);
            if (stack == null) continue;
            NBTTagCompound item = stack.writeToNBT(new NBTTagCompound());
            item.setInteger("Count", stack.stackSize);
            item.setInteger("slot", slot);
            items.appendTag(item);
        }
        nbt.setTag("bottledChaosModules", items);
    }

    public void load(NBTTagCompound nbt) {
        pendingMachines = (NBTTagCompound) nbt.copy();
        loadPendingMachines();
    }

    private void loadPendingMachines() {
        if (pendingMachines == null || pools.length == 0) return;
        NBTTagList items = pendingMachines.getTagList("bottledChaosModules", 10);
        pendingMachines = null;
        for (int slot = 0; slot < MODULE_SLOTS; slot++) machines.setStackInSlot(slot, null);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound item = items.getCompoundTagAt(i);
            int slot = item.getInteger("slot");
            if (slot < 0 || slot >= MODULE_SLOTS) continue;
            ItemStack stack = ItemStack.loadItemStackFromNBT(item);
            if (stack != null) stack.stackSize = Math.max(0, item.getInteger("Count"));
            machines.setStackInSlot(slot, stack);
        }
    }
}
