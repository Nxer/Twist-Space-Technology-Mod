package com.Nxer.TwistSpaceTechnology.util;

import org.jetbrains.annotations.NotNull;

public class BitUtils {

    /**
     * Encode booleans into a byte, decoding index low to high.
     */
    public static byte encodeBooleansToByte(boolean @NotNull... booleans) {
        if (booleans.length > 8) {
            throw new IllegalArgumentException("Max 8 input parameters. You put " + booleans.length + " here.");
        }

        byte result = 0;
        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                result |= (1 << i); // 设置对应位
            }
        }
        return result;
    }

    /**
     * Decode all 8 bits of a byte, index low to high.
     */
    public static boolean[] decodeBooleansFromByte(byte b) {
        // 解码方法1：解析全部8个bit位
        boolean[] result = new boolean[8];
        int unsignedByte = b & 0xFF; // 转为无符号int处理

        for (int i = 0; i < 8; i++) {
            result[i] = (unsignedByte & (1 << i)) != 0;
        }
        return result;
    }

    /**
     * Get a boolean from the bit of index in inputting byte.
     * 提取byte中指定位置的boolean值
     * 
     * @param b        待解析的byte
     * @param bitIndex 位索引（0-7，0表示最低位）
     * @return 对应位的boolean值
     */
    public static boolean getBooleanDecodeFromByte(byte b, int bitIndex) {
        if (bitIndex < 0 || bitIndex > 7) {
            throw new IllegalArgumentException("Index should be in 0 - 7 , you input " + bitIndex + " here.");
        }
        int unsignedByte = b & 0xFF; // 转为无符号整数
        return (unsignedByte & (1 << bitIndex)) != 0;
    }

}
