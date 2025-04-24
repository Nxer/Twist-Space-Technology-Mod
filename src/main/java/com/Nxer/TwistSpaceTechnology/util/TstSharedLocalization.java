package com.Nxer.TwistSpaceTechnology.util;

import net.minecraft.util.ChatComponentTranslation;

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

    public static class MachineTooltip {

        public static String tooComplex() {
            return TstUtils.tr("GT5U.MBTT.Structure.Complex");
        }

    }

    public static class Command {

        public static ChatComponentTranslation invalidCommand() {
            // #tr TST_Command.InvalidCommand
            // # {\RED}Invalid command.
            // #zh_CN 无效指令.
            return new ChatComponentTranslation("TST_Command.InvalidCommand");
        }

        public static ChatComponentTranslation formatError() {
            // #tr TST_Command.InputFormatError
            // # {\RED}Input format error, please check your inputs.
            // #zh_CN 输入格式错误, 请检查你的输入参数.
            return new ChatComponentTranslation("TST_Command.FormatError");
        }
    }

}
