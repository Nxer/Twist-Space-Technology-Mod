package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

/** Only immutable source requirements survive startup; radiation is satisfied by the packaged radio hatch. */
@Desugar
public record BottledChaosSourceRequirements(int heat, int glassTier, int casingTier, int compressionTier,
    boolean heavyCentrifuge, TST_ItemID specialInput, int plasmaTier, boolean multiStepPlasma,
    long requiredSourceVoltage, long fusionThreshold) {

    public static final BottledChaosSourceRequirements NONE = new BottledChaosSourceRequirements(
        0,
        0,
        0,
        0,
        false,
        null,
        0,
        false,
        0,
        0);
}
