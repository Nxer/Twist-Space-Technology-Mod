package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.compatibility.GTNH260.common.machine.TST_StarcoreMiner_260;
import com.Nxer.TwistSpaceTechnology.compatibility.GTNH261.common.machine.TST_StarcoreMiner_261;
import com.Nxer.TwistSpaceTechnology.compatibility.GTNHVersion;

import gregtech.api.interfaces.IGlobalWirelessEnergy;

public abstract class TST_StarcoreMiner extends GTCM_MultiMachineBase<TST_StarcoreMiner>
    implements IGlobalWirelessEnergy {

    public static TST_StarcoreMiner getInstance(int aID, String aName, String aNameRegional) {
        return switch (GTNHVersion.version) {
            case GTNH261 -> new TST_StarcoreMiner_261(aID, aName, aNameRegional);
            case GTNH260 -> new TST_StarcoreMiner_260(aID, aName, aNameRegional);
            case Unknown -> null;
        };
    }

    protected TST_StarcoreMiner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected TST_StarcoreMiner(String aName) {
        super(aName);
    }
}
