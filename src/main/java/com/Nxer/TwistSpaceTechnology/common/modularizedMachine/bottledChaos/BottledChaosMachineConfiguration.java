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
import com.Nxer.TwistSpaceTechnology.common.machine.MiscHelper;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_MiracleDoor;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosSourceParameters.Evaluation;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.github.bsideup.jabel.Desugar;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.blocks.tileEntity.MTENeutronActivator;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;
import kubatech.loaders.DEFCRecipes;

/** Startup machine directory and per-controller pool inventory; no runtime registry scans. */
public final class BottledChaosMachineConfiguration {

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
    private final BottledChaosMachineParameters.InstalledSource[] machineSources = new BottledChaosMachineParameters.InstalledSource[MAX_POOLS];
    private final long[] machineQuantities = new long[MAX_POOLS];
    private ConfigurationSnapshot configurationSnapshot;
    private long supportedPools;
    private long debugPools;
    private NBTTagCompound pendingMachines;
    private long requiredPools;
    private final ItemStackHandler machines;

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
                ItemStack controller = machineStructures[slot] != null ? machineStructures[slot].controller()
                    : ItemBottledChaosMachineModule.isModule(stack) ? null : stack;
                machineSources[slot] = !debug && controller != null
                    ? new BottledChaosMachineParameters.InstalledSource(controller, machineStructures[slot])
                    : null;
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
                if (machine instanceof TST_MiracleDoor) machinePools[id] |= mask(RecipeMaps.alloyBlastSmelterRecipes);
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

    public long requiredPoolMask() {
        return requiredPools;
    }

    public void setRequiredPoolMask(long mask) {
        requiredPools = withDependencies(mask);
    }

    public boolean isPoolRequired(int slot) {
        return (requiredPools & (1L << slot)) != 0;
    }

    /** Unused modules stay in their named pool slots until the player takes them out. */
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

    /** One candidate's immutable inventory snapshot; source requirements never leak into another credential. */
    public RuntimeParameters runtime(long inputVoltage, long inputPower, ItemStack[] items, FluidStack[] supplyFluids,
        double masterTime, double masterEU, long masterParallel) {
        loadPendingMachines();
        if (configurationSnapshot == null) configurationSnapshot = new ConfigurationSnapshot();
        return new RuntimeParameters(
            configurationSnapshot,
            inputVoltage,
            inputPower,
            items,
            supplyFluids,
            masterTime,
            masterEU,
            masterParallel);
    }

    /** Stable until a module changes; each planning attempt shares these read-only arrays. */
    private final class ConfigurationSnapshot {

        private final long purificationPlantQuantity = purificationPlantQuantity();
        private final long[] quantities = machineQuantities.clone();
        private final boolean[] miracleDoor = new boolean[MAX_POOLS];
        private final long debugMask = debugPools;
        private final BottledChaosMachineParameters.InstalledSource[] sources = machineSources.clone();
        private final Evaluation[] defaults = new Evaluation[MAX_POOLS];

        private ConfigurationSnapshot() {
            for (int slot = 0; slot < pools.length; slot++) {
                defaults[slot] = Evaluation.defaults(pools[slot], machineSpeeds[slot]);
                miracleDoor[slot] = sources[slot] != null && sources[slot].original instanceof TST_MiracleDoor;
            }
        }
    }

    public final class RuntimeParameters implements BottledChaosSourceParameters {

        private final ConfigurationSnapshot installed;
        private final BottledChaosMachineParameters.Context context;
        private final Map<SourceKey, Evaluation> evaluations = new HashMap<>();
        private final List<BottledChaosUpkeep.Demand> sourceUsage = new ArrayList<>();
        private long roundItemPools;
        private CheckRecipeResult sourceFailure = CheckRecipeResultRegistry.NO_RECIPE;

        private RuntimeParameters(ConfigurationSnapshot installed, long inputVoltage, long inputPower,
            ItemStack[] items, FluidStack[] fluids, double masterTime, double masterEU, long masterParallel) {
            this.installed = installed;
            context = new BottledChaosMachineParameters.Context(
                inputVoltage,
                inputPower,
                items,
                fluids,
                masterTime,
                masterEU,
                masterParallel);
        }

        @Override
        public Evaluation evaluateSource(RecipeMap<?> map, long recipeVoltage,
            BottledChaosSourceRequirements requirements) {
            Integer slot = poolIndexes.get(map);
            if (slot == null) return Evaluation.defaults(map, 1);
            Evaluation result;
            if ((purificationPools & (1L << slot)) != 0 && (installed.quantities[slot] <= 0
                || installed.quantities[slot] > installed.purificationPlantQuantity)) {
                result = reject(
                    slot,
                    "purification_capacity",
                    installed.purificationPlantQuantity,
                    Math.max(1, installed.quantities[slot]));
            } else if (installed.sources[slot] == null) {
                // Missing pools are reported by ProcessingLogic; TestItem supplies unrestricted defaults.
                return installed.defaults[slot];
            } else {
                SourceKey key = new SourceKey(slot, recipeVoltage, requirements);
                result = evaluations.get(key);
                if (result == null) {
                    result = evaluate(slot, map, recipeVoltage, requirements);
                    evaluations.put(key, result);
                }
            }
            if (!result.successful()) sourceFailure = result.failure();
            return result;
        }

        private Evaluation evaluate(int slot, RecipeMap<?> map, long recipeVoltage,
            BottledChaosSourceRequirements requirements) {
            var result = BottledChaosMachineParameters
                .evaluate(installed.sources[slot], map, recipeVoltage, context, requirements);
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
                installed.defaults[slot].quantitySpeedMultiplier(),
                installed.miracleDoor[slot],
                maximum,
                perfect,
                installed.sources[slot].fourHeliocasts ? new int[] { base, base + 1, base + 2, base + 3 } : null,
                null);
        }

        private Evaluation reject(int slot, String parameter, long available, long required) {
            return Evaluation
                .rejected(BottledChaosPoolFailure.parameter(poolKey(slot), parameter, available, required));
        }

        @Override
        public boolean isSourceAvailable(RecipeMap<?> map) {
            Integer slot = poolIndexes.get(map);
            return slot != null && installed.quantities[slot] > 0
                && ((installed.debugMask & (1L << slot)) != 0 || installed.sources[slot] != null);
        }

        @Override
        public CheckRecipeResult sourceFailure() {
            return sourceFailure;
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
            if (slot == null || profile == null) return;
            if (source.energyOnly()) roundItemPools |= 1L << slot;
            for (FluidStack fluid : profile.upkeep()) {
                if (fluid == null || fluid.amount <= 0) continue;
                sourceUsage.add(
                    new BottledChaosUpkeep.Demand(
                        poolKey(slot),
                        fluid,
                        BigInteger.valueOf(installed.quantities[slot])
                            .multiply(BigInteger.valueOf(fluid.amount)),
                        profile.startupReserveSeconds()));
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
            for (int slot = 0; slot < pools.length; slot++) {
                if ((roundItemPools & (1L << slot)) != 0) demands.add(
                    new BottledChaosUpkeep.ItemDemand(
                        poolKey(slot),
                        MiscHelper.CRITICAL_PHOTON,
                        installed.quantities[slot]));
            }
            return demands;
        }
    }

    @Desugar
    private record SourceKey(int pool, long voltage, BottledChaosSourceRequirements requirements) {}

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
    }

    public void load(NBTTagCompound nbt) {
        pendingMachines = (NBTTagCompound) nbt.getCompoundTag("bottledChaosPoolMachines")
            .copy();
        loadPendingMachines();
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
