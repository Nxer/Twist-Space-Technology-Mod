package com.Nxer.TwistSpaceTechnology.util;

/**
 * The commonly used localization texts.
 *
 * @since 0.6.4
 */
public class TstSharedLocalization {

    public static class MachineInfo {

        public static String glassTier(int glassTier) {
            // #tr tst.shared.machineInfo.glassTier
            // # {\AQUA}Glass Tier: {\GOLD}%s
            // #zh_CN {\AQUA}玻璃等级: {\GOLD}%s
            return TstUtils.tr("tst.shared.machineInfo.glassTier", glassTier);
        }

        public static String coilTier(int coilTier) {
            // #tr tst.shared.machineInfo.coilTier
            // # {\AQUA}Coil Tier: {\GOLD}%s
            // #zh_CN {\AQUA}线圈等级: {\GOLD}%s
            return TstUtils.tr("tst.shared.machineInfo.coilTier", coilTier);
        }

        public static String componentTier(int componentTier) {
            // #tr tst.shared.machineInfo.componentTier
            // # {\AQUA}Component Tier: {\GOLD}%s
            // #zh_CN {\AQUA}部件等级: {\GOLD}%s
            return TstUtils.tr("tst.shared.machineInfo.componentTier", componentTier);
        }
    }

}
