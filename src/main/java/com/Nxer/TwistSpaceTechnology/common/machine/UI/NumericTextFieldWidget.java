package com.Nxer.TwistSpaceTechnology.common.machine.UI;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

/** Keeps numeric keypad input consistent with the main keyboard in legacy ModularUI numeric fields. */
public class NumericTextFieldWidget extends TextFieldWidget {

    @Override
    public boolean onKeyPressed(char character, int keyCode) {
        if (character == Character.MIN_VALUE) {
            character = switch (keyCode) {
                case Keyboard.KEY_NUMPAD0 -> '0';
                case Keyboard.KEY_NUMPAD1 -> '1';
                case Keyboard.KEY_NUMPAD2 -> '2';
                case Keyboard.KEY_NUMPAD3 -> '3';
                case Keyboard.KEY_NUMPAD4 -> '4';
                case Keyboard.KEY_NUMPAD5 -> '5';
                case Keyboard.KEY_NUMPAD6 -> '6';
                case Keyboard.KEY_NUMPAD7 -> '7';
                case Keyboard.KEY_NUMPAD8 -> '8';
                case Keyboard.KEY_NUMPAD9 -> '9';
                default -> Character.MIN_VALUE;
            };
        }
        return super.onKeyPressed(character, keyCode);
    }
}
