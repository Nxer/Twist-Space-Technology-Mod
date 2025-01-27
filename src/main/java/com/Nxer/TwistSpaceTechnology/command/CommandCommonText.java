package com.Nxer.TwistSpaceTechnology.command;

import net.minecraft.util.ChatComponentTranslation;

/**
 * Command commonly used localization texts.
 * <p>
 * This is package-private because we don't need them as public.
 */
class CommandCommonText {

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
