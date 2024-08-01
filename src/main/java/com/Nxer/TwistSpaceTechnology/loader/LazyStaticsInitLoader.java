package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MicroSpaceTimeFabricatorio;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.machines.TST_CircuitConverter;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_ArtificialStar;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_DSPLauncher;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_DSPReceiver;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_StrangeMatterAggregator;

public class LazyStaticsInitLoader {

    public void initStaticsOnCompleteInit() {
        TST_StrangeMatterAggregator.initStatics();
        TST_ArtificialStar.initStatics();
        TST_DSPReceiver.initStatics();
        TST_DSPLauncher.initStatics();
        TST_MicroSpaceTimeFabricatorio.initStatics();
        TST_CircuitConverter.initStatics();
    }
}
