package com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic;

import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

public class StaticMiscs {

    public static List<ItemStack> CircuitULV;
    public static List<ItemStack> CircuitLV;
    public static List<ItemStack> CircuitMV;
    public static List<ItemStack> CircuitHV;
    public static List<ItemStack> CircuitEV;
    public static List<ItemStack> CircuitIV;
    public static List<ItemStack> CircuitLuV;
    public static List<ItemStack> CircuitZPM;
    public static List<ItemStack> CircuitUV;
    public static List<ItemStack> CircuitUHV;
    public static List<ItemStack> CircuitUEV;
    public static List<ItemStack> CircuitUIV;
    public static List<ItemStack> CircuitUMV;
    public static List<ItemStack> CircuitUXV;
    public static List<ItemStack> CircuitMAX;

    public static void init() {
        CircuitULV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.ULV));
        CircuitLV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.LV));
        CircuitMV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.MV));
        CircuitHV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.HV));
        CircuitEV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.EV));
        CircuitIV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.IV));
        CircuitLuV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.LuV));
        CircuitZPM = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.ZPM));
        CircuitUV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.UHV));
        CircuitUHV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.UHV));
        CircuitUEV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.UEV));
        CircuitUIV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.UIV));
        CircuitUMV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.UMV));
        CircuitUXV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.UXV));
        CircuitMAX = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.MAX));
    }
}
