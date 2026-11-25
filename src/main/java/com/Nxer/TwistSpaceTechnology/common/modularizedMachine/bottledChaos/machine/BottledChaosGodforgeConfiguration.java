package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineParameters.InstalledSource;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosModuleStacking.Combination;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Structure;
import com.github.bsideup.jabel.Desugar;

/** Immutable fleet snapshot. Matching packages combine by configuration, never by inventory slot or recipe pool. */
public final class BottledChaosGodforgeConfiguration {

    @Desugar
    record Host(int installation, Structure structure, BigInteger quantity,
        BottledChaosComplexStructures.GodforgeProfile profile) {}

    @Desugar
    record Binding(Host host, InstalledSource source, List<Combination> combinations) {}

    final List<Host> hosts = new ArrayList<>();
    final List<List<Binding>> bindings = new ArrayList<>();
    final BigInteger[] quantities;
    final int[] canonical;
    final BigInteger totalModules;

    BottledChaosGodforgeConfiguration(InstalledSource[] sources, long[] counts, int limit) {
        quantities = new BigInteger[sources.length];
        canonical = new int[sources.length];
        BigInteger total = BigInteger.ZERO;
        for (int i = 0; i < sources.length; i++) {
            quantities[i] = BigInteger.ZERO;
            canonical[i] = i;
            bindings.add(new ArrayList<>());
            if (i >= limit || sources[i] == null || counts[i] <= 0) continue;
            Structure structure = sources[i].structure;
            if (BottledChaosComplexStructures.isSeparateGodforgeHost(structure)) {
                int matching = -1;
                var profile = BottledChaosComplexStructures.godforge(structure);
                for (int j = 0; j < hosts.size(); j++) {
                    Host candidate = hosts.get(j);
                    if (profile != null && candidate.profile() != null
                        && profile.fuelFactor() == candidate.profile()
                            .fuelFactor()
                        && structure.withCircuitParameter(BottledChaosCircuitParameter.UNSET)
                            .sameConfiguration(
                                candidate.structure()
                                    .withCircuitParameter(BottledChaosCircuitParameter.UNSET)))
                        matching = j;
                }
                if (matching < 0) hosts.add(new Host(i, structure, BigInteger.valueOf(counts[i]), profile));
                else {
                    Host old = hosts.get(matching);
                    hosts.set(
                        matching,
                        new Host(
                            old.installation(),
                            old.structure(),
                            old.quantity()
                                .add(BigInteger.valueOf(counts[i])),
                            old.profile()));
                    canonical[i] = old.installation();
                }
            } else if (BottledChaosComplexStructures.isSeparateGodforgeModule(structure)) {
                for (int j = 0; j < i; j++)
                    if (sources[j] != null && counts[j] > 0 && structure.sameConfiguration(sources[j].structure)) {
                        canonical[i] = canonical[j];
                        break;
                    }
                BigInteger count = BigInteger.valueOf(counts[i]);
                quantities[canonical[i]] = quantities[canonical[i]].add(count);
                total = total.add(count);
            }
        }
        totalModules = total;
        for (int i = 0; i < limit; i++) {
            if (quantities[i].signum() == 0) continue;
            for (Host host : hosts) {
                if (host.profile() == null || host.quantity()
                    .multiply(
                        BigInteger.valueOf(
                            host.profile()
                                .modules()))
                    .compareTo(totalModules) < 0) continue;
                var profile = BottledChaosComplexStructures.bindGodforge(host.structure(), sources[i].original);
                if (profile == null) continue;
                bindings.get(i)
                    .add(
                        new Binding(
                            host,
                            new InstalledSource(sources[i].structure.controller(), sources[i].structure, profile),
                            BottledChaosModuleStacking.combinations(
                                quantities[i],
                                host.profile()
                                    .modules(),
                                host.quantity())));
            }
        }
    }

    List<Binding> bindings(int installation) {
        return bindings.get(canonical[installation]);
    }

    BigInteger hostQuantity(int installation) {
        for (Host host : hosts) if (host.installation() == installation) return host.quantity();
        return null;
    }

    public static List<String> describe(Structure structure, NBTTagCompound runtime) {
        List<String> lines = new ArrayList<>();
        // #tr item.tst.bottledchaos.machine_module.info.godforge_capacity
        // # Module capacity per host: %s
        // #zh_CN 每台主机模块容量: %s

        // #tr item.tst.bottledchaos.machine_module.info.godforge_installed
        // # Installed modules: %s; host capacity: %s
        // #zh_CN 已安装模块: %s; 主机容量: %s

        // #tr item.tst.bottledchaos.machine_module.info.stacking
        // # Reference: %s at %sx + %s at %sx; pending: %s.
        // #zh_CN 参考组合: %s个%s倍速, %s个%s倍速; 待增效数量: %s.
        boolean host = BottledChaosComplexStructures.isSeparateGodforgeHost(structure);
        if (host) {
            var profile = BottledChaosComplexStructures.godforgePreset(structure);
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "item.tst.bottledchaos.machine_module.info.godforge_configuration",
                    profile.rings(),
                    BottledChaosComplexStructures.godforgeUpgradeName(profile)));
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "item.tst.bottledchaos.machine_module.info.godforge_capacity",
                    profile.modules()));
        }
        if (runtime != null) {
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "item.tst.bottledchaos.machine_module.info.godforge_installed",
                    runtime.getString("godforgeModules"),
                    runtime.getString("godforgeCapacity")));
            if (!host && !runtime.getBoolean("godforgeReady"))
                lines.add(StatCollector.translateToLocal("tst.machine.BottledChaos.result.godforge_host"));
            if (runtime.hasKey("godforgeLower")) {
                BigInteger speed = new BigInteger(runtime.getString("godforgeLower"));
                lines.add(
                    StatCollector.translateToLocalFormatted(
                        "item.tst.bottledchaos.machine_module.info.stacking",
                        runtime.getInteger("godforgeParallel") - runtime.getInteger("godforgeFaster"),
                        speed,
                        runtime.getInteger("godforgeFaster"),
                        speed.add(BigInteger.ONE),
                        runtime.getString("godforgeUnused")));
            }
        }
        return lines;
    }
}
