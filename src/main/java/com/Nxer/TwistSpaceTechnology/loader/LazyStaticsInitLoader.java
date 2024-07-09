package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines.TST_StrangeMatterAggregator;

public class LazyStaticsInitLoader {

    public void initStaticsOnCompleteInit() {
        TST_StrangeMatterAggregator.initStatics();
    }
}
