package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.compatibility.GTNH260.common.machine.TST_EyeOfWood_260;
import com.Nxer.TwistSpaceTechnology.compatibility.GTNH261.common.machine.TST_EyeOfWood_261;
import com.Nxer.TwistSpaceTechnology.compatibility.GTNHVersion;

public abstract class TST_EyeOfWood extends GTCM_MultiMachineBase<TST_EyeOfWood> {

    public static TST_EyeOfWood getInstance(int aID, String aName, String aNameRegional) {
        return switch (GTNHVersion.version) {
            case GTNH261 -> new TST_EyeOfWood_261(aID, aName, aNameRegional);
            case GTNH260 -> new TST_EyeOfWood_260(aID, aName, aNameRegional);
            case Unknown -> null;
        };
    }

    // region Class Constructor
    protected TST_EyeOfWood(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected TST_EyeOfWood(String aName) {
        super(aName);
    }

}
