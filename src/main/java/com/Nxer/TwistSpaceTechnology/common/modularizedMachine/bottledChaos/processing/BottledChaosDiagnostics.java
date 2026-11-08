package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

/** Optional observations for one processing attempt; never participates in planning or resource changes. */
public final class BottledChaosDiagnostics {

    // Source-only switch. Rebuild after changing; intentionally not exposed through config or GUI.
    public static final boolean ENABLED = false;
    private static final BottledChaosDiagnostics DISABLED = new BottledChaosDiagnostics();

    private long started, setup, prepareStart, prepared, fitted, recovered, checked;
    private long probeNs, sourceNs, matchNs, inputNs, targetPassNs;
    private int probes, sources, targetPasses, targetExtensions, shortProbes, shortExtensions;
    private double shortBefore, shortAfter;

    public static BottledChaosDiagnostics begin() {
        if (!ENABLED) return DISABLED;
        BottledChaosDiagnostics diagnostics = new BottledChaosDiagnostics();
        diagnostics.started = System.nanoTime();
        return diagnostics;
    }

    public long startTimer() {
        return ENABLED ? System.nanoTime() : 0;
    }

    public void setupComplete() {
        if (ENABLED) setup = System.nanoTime();
    }

    public void prepareStarted() {
        if (ENABLED) prepareStart = System.nanoTime();
    }

    public void prepared() {
        if (ENABLED) prepared = System.nanoTime();
    }

    public void fitted() {
        if (ENABLED) fitted = System.nanoTime();
    }

    public void recovered() {
        if (ENABLED) recovered = System.nanoTime();
    }

    public void outputsChecked() {
        if (ENABLED) checked = System.nanoTime();
    }

    public void sourceChecked(long start) {
        if (!ENABLED) return;
        sources++;
        sourceNs += System.nanoTime() - start;
    }

    public void inputsCollected(long start) {
        if (ENABLED) inputNs += System.nanoTime() - start;
    }

    public void inputsMatched(long start) {
        if (ENABLED) matchNs += System.nanoTime() - start;
    }

    public void probed(long start) {
        if (!ENABLED) return;
        probes++;
        probeNs += System.nanoTime() - start;
    }

    public void targetsChecked(long start) {
        if (!ENABLED) return;
        targetPasses++;
        targetPassNs += System.nanoTime() - start;
    }

    public void targetsExtended() {
        if (ENABLED) targetExtensions++;
    }

    public void shortCorrectionStarted(double share) {
        if (!ENABLED) return;
        shortBefore = share;
        shortAfter = share;
    }

    public void shortProbed() {
        if (ENABLED) shortProbes++;
    }

    public void shortExtended(double share) {
        if (!ENABLED) return;
        shortExtensions++;
        shortAfter = share;
    }

    public void finish(int entry, int ticks, long eut) {
        if (!ENABLED) return;
        long ended = System.nanoTime();
        TwistSpaceTechnology.LOG.info(
            "BC_ROUND entry=" + entry
                + " total="
                + (ended - started)
                + " setup="
                + (setup - started)
                + " prepare="
                + (prepared - prepareStart)
                + " fit="
                + (fitted - prepared)
                + " recover="
                + (recovered - fitted)
                + " output="
                + (checked - recovered)
                + " finish="
                + (ended - checked)
                + " probes="
                + probes
                + " probeNs="
                + probeNs
                + " targetPasses="
                + targetPasses
                + " targetExtensions="
                + targetExtensions
                + " targetPassNs="
                + targetPassNs
                + " shortProbes="
                + shortProbes
                + " shortExtensions="
                + shortExtensions
                + " shortBefore="
                + shortBefore
                + " shortAfter="
                + shortAfter
                + " sources="
                + sources
                + " sourceNs="
                + sourceNs
                + " inputNs="
                + inputNs
                + " matchNs="
                + matchNs
                + " ticks="
                + ticks
                + " eut="
                + eut);
    }
}
