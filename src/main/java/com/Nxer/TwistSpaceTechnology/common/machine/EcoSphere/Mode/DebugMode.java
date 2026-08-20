package com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode;

import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeSupport.addSplitStack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereModeResult;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereSpecialUpgrade;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerRecipeCache;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerWeaponHandler;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerWeaponHandler.WeaponEffects;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.AquaticZoneSimulatorFakeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes.TreeGrowthSimulatorWithoutToolFakeRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTModHandler;
import gregtech.common.tileentities.machines.multi.MTETreeFarm.Mode;

public final class DebugMode {

    private static final long DEBUG_PARALLEL = 100_000;
    private static final Map<DebugProfile, List<DebugOutput>> OUTPUT_CACHE = new HashMap<>();
    private static final Map<TST_EcoSphereSimulator, DebugState> MACHINE_STATES = new WeakHashMap<>();

    private DebugMode() {}

    public static EcoSphereModeResult process(TST_EcoSphereSimulator machine, int mode, int beaconTier) {
        int tier = beaconTier >= 2 ? 2 : 1;
        boolean autoPulverize = machine.hasSpecialUpgrade(EcoSphereSpecialUpgrade.AUTO_PULVERIZE_EQUIPMENT);
        WeaponEffects weaponEffects = DirectedMobClonerWeaponHandler
            .process(mode == 3 ? machine.getCloningWeapons() : new ItemStack[0]);
        DebugProfile outputProfile = new DebugProfile(mode, tier, autoPulverize, weaponEffects);
        List<DebugOutput> cachedOutputs = OUTPUT_CACHE
            .computeIfAbsent(outputProfile, ignored -> collectOutputs(mode, tier, autoPulverize, weaponEffects));
        DebugState state = MACHINE_STATES.get(machine);
        if (state == null || !state.outputProfile.equals(outputProfile)) {
            if (state != null) state.close();
            state = createDebugState(machine, outputProfile, cachedOutputs);
            MACHINE_STATES.put(machine, state);
        }
        if (state.lineIndex >= state.lines.size()) {
            state.close();
            return EcoSphereModeResult.failure(CheckRecipeResultRegistry.NO_RECIPE);
        }

        DebugLine line = state.lines.get(state.lineIndex++);
        state.writeLine(line.text());
        List<ItemStack> outputs = new ArrayList<>();
        DebugOutput output = line.output();
        if (output != null) {
            addSplitStack(
                outputs,
                output.item()
                    .getItemStackWithNBT(),
                output.amount());
        }
        if (state.lineIndex >= state.lines.size()) state.close();
        return new EcoSphereModeResult(
            SimpleResultWithText.ofSuccessText("debugRUN"),
            outputs.toArray(new ItemStack[0]),
            new FluidStack[0],
            0,
            1);
    }

    public static void reset(TST_EcoSphereSimulator machine) {
        DebugState state = MACHINE_STATES.remove(machine);
        if (state != null) state.close();
    }

    private static DebugState createDebugState(TST_EcoSphereSimulator machine, DebugProfile outputProfile,
        List<DebugOutput> outputs) {
        Set<String> inputs = new LinkedHashSet<>();
        addItemId(inputs, machine.getControllerSlot());
        for (ItemStack input : machine.getModeInputs()) addItemId(inputs, input);

        List<DebugLine> lines = new ArrayList<>(inputs.size() + outputs.size() + 4);
        lines.add(new DebugLine("TST Version: " + TwistSpaceTechnology.VERSION, null));
        lines.add(
            new DebugLine(
                "System Time: " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                null));
        lines.add(new DebugLine("Inputs", null));
        for (String input : inputs) lines.add(new DebugLine(input, null));
        lines.add(new DebugLine("Outputs", null));
        for (DebugOutput output : outputs) {
            String itemId = getItemId(
                output.item()
                    .getItemStackWithNBT());
            if (itemId != null) lines.add(new DebugLine(itemId, output));
        }

        File gameRoot = Loader.instance()
            .getConfigDir()
            .getParentFile();
        try {
            BufferedWriter writer = Files.newBufferedWriter(
                new File(gameRoot, "EcoSpheraDebugRunResult").toPath(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
            return new DebugState(outputProfile, lines, writer);
        } catch (IOException exception) {
            TwistSpaceTechnology.LOG.error("Failed to write EcoSphera debug run result", exception);
            return new DebugState(outputProfile, lines, null);
        }
    }

    private static void addItemId(Set<String> itemIds, ItemStack stack) {
        String itemId = getItemId(stack);
        if (itemId != null) itemIds.add(itemId);
    }

    private static String getItemId(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (identifier == null) return null;
        return identifier.modId + ':' + identifier.name + ':' + stack.getItemDamage();
    }

    private static List<DebugOutput> collectOutputs(int mode, int beaconTier, boolean autoPulverize,
        WeaponEffects weaponEffects) {
        // Merge identical outputs before applying the fixed debug parallel count.
        Map<TST_ItemID, Long> outputAmounts = new LinkedHashMap<>();
        switch (mode) {
            case 0 -> collectTreeOutputs(outputAmounts, beaconTier);
            case 1 -> collectAquaticOutputs(outputAmounts, beaconTier);
            case 2 -> collectGreenhouseOutputs(outputAmounts, beaconTier);
            case 3 -> collectClonerOutputs(outputAmounts, beaconTier, autoPulverize, weaponEffects);
            default -> {}
        }

        List<DebugOutput> outputs = new ArrayList<>(outputAmounts.size());
        for (Map.Entry<TST_ItemID, Long> output : outputAmounts.entrySet()) {
            outputs.add(new DebugOutput(output.getKey(), output.getValue()));
        }
        return outputs;
    }

    private static void collectTreeOutputs(Map<TST_ItemID, Long> outputs, int beaconTier) {
        ItemStack[][] registeredProducts = TreeGrowthSimulatorWithoutToolFakeRecipe.allProducts;
        if (registeredProducts != null) {
            for (ItemStack[] products : registeredProducts) collect(outputs, products);
        }
        if (beaconTier < 2) return;

        ItemStack timeTree = GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5);
        if (timeTree != null) collectTreeRecipe(outputs, TreeGrowthSimulatorMode.queryTimeTreeProduct(timeTree));
        ItemStack taintedTree = GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintSapling", 1, 0);
        if (taintedTree != null) collectTreeRecipe(outputs, TreeGrowthSimulatorMode.queryTreeProduct(taintedTree));
        ItemStack barnardaCTree = GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 1, 0);
        if (barnardaCTree != null) collectTreeRecipe(outputs, TreeGrowthSimulatorMode.queryTreeProduct(barnardaCTree));
    }

    private static void collectTreeRecipe(Map<TST_ItemID, Long> outputs, EnumMap<Mode, ItemStack> products) {
        if (products == null) return;
        for (Map.Entry<Mode, ItemStack> product : products.entrySet()) {
            ItemStack stack = product.getValue();
            if (stack == null) continue;
            collect(
                outputs,
                stack,
                (long) stack.stackSize * TreeGrowthSimulatorMode.getModeMultiplier(product.getKey()));
        }
    }

    private static void collectAquaticOutputs(Map<TST_ItemID, Long> outputs, int beaconTier) {
        collect(outputs, AquaticZoneSimulatorFakeRecipe.WatersOutputs);
        if (beaconTier >= 2) collect(outputs, AquaticZoneSimulatorFakeRecipe.UnknownWaterOutputs);
    }

    private static void collectGreenhouseOutputs(Map<TST_ItemID, Long> outputs, int beaconTier) {
        for (ICropCard crop : CropRegistry.instance.getAllInRegistrationOrder()) {
            Collection<ItemStack> alternateSeeds = crop.getAlternateSeeds();
            // Tier I can only reach crops that have a normal seed registration.
            if (beaconTier < 2 && (alternateSeeds == null || alternateSeeds.isEmpty())) continue;
            Map<ItemStack, Integer> dropTable = crop.getDropTable();
            if (dropTable != null) collect(outputs, dropTable.keySet());
        }
    }

    private static void collectClonerOutputs(Map<TST_ItemID, Long> outputs, int beaconTier, boolean autoPulverize,
        WeaponEffects weaponEffects) {
        for (DirectedMobClonerRecipeCache.CachedRecipe recipe : DirectedMobClonerRecipeCache.getDebugRecipes()) {
            // The secondary cloning beacon adds boss recipes to the same numbered pool.
            if (recipe.boss() && beaconTier < 2) continue;
            for (int tableIndex = 0; tableIndex < 2; tableIndex++) {
                List<DirectedMobClonerRecipeCache.CachedOutput> outputTable = tableIndex == 0 ? recipe.ordinaryOutputs()
                    : recipe.equipmentOutputs(autoPulverize);
                for (DirectedMobClonerRecipeCache.CachedOutput output : outputTable) {
                    ItemStack stack = output.stack();
                    if (stack != null) collect(outputs, stack, stack.stackSize);
                }
            }
            for (DirectedMobClonerRecipeCache.CachedSpecialOutput output : recipe.specialOutputs()) {
                ItemStack stack = output.stack();
                if (stack != null && output.extraChance(weaponEffects) > 0d) collect(outputs, stack, stack.stackSize);
            }
        }
    }

    private static void collect(Map<TST_ItemID, Long> outputs, Iterable<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (stack != null) collect(outputs, stack, stack.stackSize);
        }
    }

    private static void collect(Map<TST_ItemID, Long> outputs, ItemStack[] stacks) {
        if (stacks == null) return;
        for (ItemStack stack : stacks) {
            if (stack != null) collect(outputs, stack, stack.stackSize);
        }
    }

    private static void collect(Map<TST_ItemID, Long> outputs, ItemStack stack, long baseAmount) {
        if (stack == null || stack.getItem() == null || baseAmount <= 0) return;
        long amount = baseAmount > Long.MAX_VALUE / DEBUG_PARALLEL ? Long.MAX_VALUE : baseAmount * DEBUG_PARALLEL;
        outputs.merge(TST_ItemID.create(stack), amount, DebugMode::addSaturated);
    }

    private static long addSaturated(long left, long right) {
        return left > Long.MAX_VALUE - right ? Long.MAX_VALUE : left + right;
    }

    @Desugar
    private record DebugOutput(TST_ItemID item, long amount) {}

    @Desugar
    private record DebugLine(String text, DebugOutput output) {}

    @Desugar
    private record DebugProfile(int mode, int beaconTier, boolean autoPulverize, WeaponEffects weaponEffects) {}

    private static final class DebugState {

        private final DebugProfile outputProfile;
        private final List<DebugLine> lines;
        private BufferedWriter writer;
        private int lineIndex;

        private DebugState(DebugProfile outputProfile, List<DebugLine> lines, BufferedWriter writer) {
            this.outputProfile = outputProfile;
            this.lines = lines;
            this.writer = writer;
        }

        private void writeLine(String line) {
            if (writer == null) return;
            try {
                writer.write(line);
                writer.newLine();
                writer.flush();
            } catch (IOException exception) {
                TwistSpaceTechnology.LOG.error("Failed to append EcoSphera debug run result", exception);
                close();
            }
        }

        private void close() {
            if (writer == null) return;
            try {
                writer.close();
            } catch (IOException exception) {
                TwistSpaceTechnology.LOG.error("Failed to close EcoSphera debug run result", exception);
            } finally {
                writer = null;
            }
        }
    }
}
