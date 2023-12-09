package com.Nxer.TwistSpaceTechnology.util.enums;

import java.math.BigInteger;
import java.util.Arrays;

public class TierEU {

    public static final long[] V = new long[] { 8L, 32L, 128L, 512L, 2048L, 8192L, 32_768L, 131_072L, 524_288L,
        2_097_152L, 8_388_608L, 33_554_432L, 134_217_728L, 536_870_912L, Integer.MAX_VALUE - 7,
        // Error tier to prevent out of bounds errors. Not really a real tier (for now).
        8_589_934_592L };

    /**
     * The Voltage Practical. These are recipe voltage you should use if you expect the recipe to use a full amp of that
     * tier. These leave a bit of headroom for cable and transformer losses, but not enough to make it a great gain.
     */
    // this will correctly map ULV to 7.
    public static final long[] VP = Arrays.stream(V)
        .map(
            i -> BigInteger.valueOf(i)
                .multiply(BigInteger.valueOf(30))
                .divide(BigInteger.valueOf(32))
                .longValueExact())
        .toArray();

    // Do NOT use these for crafting recipes as they are exactly 1A! Use RECIPE_ULV etc.
    public static final long ULV = V[0];
    public static final long LV = V[1];
    public static final long MV = V[2];
    public static final long HV = V[3];
    public static final long EV = V[4];
    public static final long IV = V[5];
    public static final long LuV = V[6];
    public static final long ZPM = V[7];
    public static final long UV = V[8];
    public static final long UHV = V[9];
    public static final long UEV = V[10];
    public static final long UIV = V[11];
    public static final long UMV = V[12];
    public static final long UXV = V[13];
    public static final long MAX = V[14];

    // Use me for recipes.
    public static final long RECIPE_ULV = VP[0];
    public static final long RECIPE_LV = VP[1];
    public static final long RECIPE_MV = VP[2];
    public static final long RECIPE_HV = VP[3];
    public static final long RECIPE_EV = VP[4];
    public static final long RECIPE_IV = VP[5];
    public static final long RECIPE_LuV = VP[6];
    public static final long RECIPE_ZPM = VP[7];
    public static final long RECIPE_UV = VP[8];
    public static final long RECIPE_UHV = VP[9];
    public static final long RECIPE_UEV = VP[10];
    public static final long RECIPE_UIV = VP[11];
    public static final long RECIPE_UMV = VP[12];
    public static final long RECIPE_UXV = VP[13];
    public static final long RECIPE_MAX = VP[14];
}
