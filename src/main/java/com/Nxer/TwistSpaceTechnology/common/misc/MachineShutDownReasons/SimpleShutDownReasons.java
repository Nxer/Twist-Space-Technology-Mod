package com.Nxer.TwistSpaceTechnology.common.misc.MachineShutDownReasons;

import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;

public final class SimpleShutDownReasons {

    // #tr GT5U.gui.text.SimpleShutDownReasons.NoSpaceTimeMaintenanceFluidInput
    // # No Space Time Maintenance Fluid Input
    // #zh_CN 没有时空维护流体输入
    public static final ShutDownReason NoSpaceTimeMaintenanceFluidInput = SimpleShutDownReason
        .ofCritical("SimpleShutDownReasons.NoSpaceTimeMaintenanceFluidInput");
}
