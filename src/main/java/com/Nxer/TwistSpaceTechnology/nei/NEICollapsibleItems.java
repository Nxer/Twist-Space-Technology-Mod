package com.Nxer.TwistSpaceTechnology.nei;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.IOUtils;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;

import codechicken.nei.NEIClientConfig;
import cpw.mods.fml.common.registry.GameData;
import gregtech.api.GregTechAPI;

/** Called by the client-only NEI plugin before NEI loads its collapsible item filters. */
final class NEICollapsibleItems {

    static void load() {
        Map<String, String> groups = new LinkedHashMap<>();
        groups.put("Cardigans", itemFilter(TstItems.Cardian));
        groups.put("Runes", itemFilter(TstItems.MetaItemRune));
        groups.put("FLASK", itemFilter(TstItems.MetaItemFlask));
        groups.put("Multi-use Cores", blockFilter(TstBlocks.MultiUseCore));
        groups.put("Meta Casings", blockFilter(TstBlocks.MetaBlockCasing01, TstBlocks.MetaBlockCasing02));
        groups.put("Photonic Intensifiers", blockFilter(TstBlocks.PhotonControllerUpgrade));
        groups.put("Space-time Oscillators", blockFilter(TstBlocks.SpaceTimeOscillator));
        groups.put("Space-time Constraintors", blockFilter(TstBlocks.SpaceTimeConstraintor));
        groups.put("Space-time Mergers", blockFilter(TstBlocks.SpaceTimeMerger));
        groups.put("Stabilisation Field Generators", blockFilter(TstBlocks.StabilisationFieldGenerator));
        groups.put("Energy Sustainment Matrices", blockFilter(TstBlocks.EnergySustainmentMatrix));
        groups.put("Material Blocks", blockFilter(TstBlocks.MetalBlock));
        groups.put("Eco-sphere Execution Protocols", itemFilter(TstItems.EcoSphereExecutionProtocol));
        groups.put("Eco-sphere Upgrades", itemFilter(TstItems.EcoSphereUpgrade));
        groups.put("Solidifier Hatches", prefixFilter("SolidifyHatch_"));
        groups.put("Dual Input Buffers", prefixFilter("DualInputBuffer_"));
        groups.put("Dynamic Parallel Controllers", prefixFilter("DynamicParallelControllerT"));
        groups.put("Static Parallel Controllers", prefixFilter("StaticParallelControllerT"));
        groups.put("Dynamic Speed Controllers", prefixFilter("DynamicSpeedControllerT"));
        groups.put("Static Speed Controllers", prefixFilter("StaticSpeedControllerT"));
        groups.put("Power Consumption Controllers", prefixFilter("StaticPowerConsumptionControllerT"));
        groups.put(
            "Overclock Controllers",
            String.join(
                " | ",
                selectors(
                    GTCMItemList.LowSpeedPerfectOverclockController,
                    GTCMItemList.PerfectOverclockController,
                    GTCMItemList.SingularityPerfectOverclockController)));

        Map<String, List<String>> mergedGroups = new LinkedHashMap<>();
        List<String> energy = prefixSelectors("BufferedEnergyHatch");
        energy.addAll(selectors(GTCMItemList.LegendaryWirelessEnergyHatch, GTCMItemList.HarmoniousWirelessEnergyHatch));
        mergedGroups.put("#gregtech Energy Hatch", energy);
        mergedGroups.put("#gregtech Dynamo Hatch", selectors(GTCMItemList.InfiniteWirelessDynamoHatch));
        mergedGroups.put("#gregtech Input Hatch", selectors(GTCMItemList.AEStorageCellInputHatch));
        mergedGroups.put("#gregtech Input Bus", selectors(GTCMItemList.AEStorageCellInputBus));

        // GTNH supplies these groups in its config; vanilla NEI's bundled defaults do not.
        String machines = blockFilter(GregTechAPI.sBlockMachines);
        Map<String, String> fallbackGroups = new LinkedHashMap<>();
        fallbackGroups.put(
            "#gregtech Energy Hatch",
            machines + " 40-49,206-209,216-219,227,229,266-269,286,11300-11304,15065-15097,15100-15129,15500-15506");
        fallbackGroups
            .put("#gregtech Dynamo Hatch", machines + " 30-39,899-908,11290-11293,15200-15229,15239,16030-16035");
        fallbackGroups.put(
            "#gregtech Input Hatch",
            machines + " 50-59,200,710-719,2712,2717,10503,11306,12097-12100,12102,12799,969");
        fallbackGroups.put("#gregtech Input Bus", machines + " 70-79,2711,2715,2718,10509,11305,13533,31046");

        Path config = NEIClientConfig.configDir.toPath()
            .resolve("collapsibleitems.cfg");
        // NEI reads this file with FileReader, so use the same charset for existing user text.
        Charset charset = Charset.defaultCharset();
        try (InputStream resource = NEIClientConfig.class.getResourceAsStream("/assets/nei/cfg/collapsibleitems.cfg")) {
            if (resource == null) throw new IOException("Missing NEI default collapsibleitems.cfg");
            String defaults = new String(IOUtils.toByteArray(resource), charset);
            String original = Files.exists(config) ? new String(Files.readAllBytes(config), charset) : defaults;
            String updated = NEICollapsibleConfig.update(original, fallbackGroups, groups, mergedGroups);
            if (Files.exists(config) && original.equals(updated)) return;
            Files.createDirectories(config.getParent());
            Path temporary = Files.createTempFile(config.getParent(), "collapsibleitems-tst-", ".tmp");
            try {
                Files.write(temporary, updated.getBytes(charset));
                try {
                    Files.move(temporary, config, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                } catch (AtomicMoveNotSupportedException e) {
                    Files.move(temporary, config, StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                Files.deleteIfExists(temporary);
            }
            TwistSpaceTechnology.LOG.info("Updated TST NEI collapsible item groups.");
        } catch (IOException | IllegalArgumentException e) {
            TwistSpaceTechnology.LOG.error("Failed to update TST NEI collapsible item groups", e);
        }
    }

    private static String blockFilter(Block... blocks) {
        return itemFilter(
            Arrays.stream(blocks)
                .filter(block -> block != null)
                .map(Item::getItemFromBlock)
                .toArray(Item[]::new));
    }

    private static String itemFilter(Item... items) {
        Set<String> filters = new LinkedHashSet<>();
        for (Item item : items) {
            if (item == null) continue;
            String name = GameData.getItemRegistry()
                .getNameForObject(item);
            if (name != null) filters.add("r/^" + Pattern.quote(name) + "$/");
        }
        return String.join(" | ", filters);
    }

    private static String prefixFilter(String prefix) {
        return String.join(" | ", prefixSelectors(prefix));
    }

    private static List<String> prefixSelectors(String prefix) {
        return selectors(
            Arrays.stream(GTCMItemList.values())
                .filter(
                    entry -> entry.name()
                        .startsWith(prefix))
                .toArray(GTCMItemList[]::new));
    }

    private static List<String> selectors(GTCMItemList... entries) {
        Set<String> filters = new LinkedHashSet<>();
        for (GTCMItemList entry : entries) {
            if (!entry.hasBeenSet()) continue;
            ItemStack stack = entry.getInternalStack_unsafe();
            if (stack == null || stack.getItem() == null) continue;
            String name = GameData.getItemRegistry()
                .getNameForObject(stack.getItem());
            if (name != null) filters.add("<" + name + ":" + stack.getItemDamage() + ">");
        }
        return new ArrayList<>(filters);
    }
}
