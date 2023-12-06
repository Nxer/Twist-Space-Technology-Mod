package com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.dreammaster.gthandler.CustomItemList;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

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
        CircuitULV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Primitive));
        CircuitLV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Basic));
        CircuitMV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Good));
        CircuitHV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Advanced));
        CircuitEV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Data));
        CircuitIV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Elite));
        CircuitLuV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Master));
        CircuitZPM = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Ultimate));
        CircuitUV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.SuperconductorUHV));
        CircuitUHV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Infinite));
        CircuitUEV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Bio));
        CircuitUIV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Optical));
        CircuitUMV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Exotic));
        CircuitUMV.add(CustomItemList.PikoCircuit.get(1));
        CircuitUXV = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Cosmic));
        CircuitUXV.add(CustomItemList.QuantumCircuit.get(1));
        CircuitMAX = GT_OreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Transcendent));
    }
}
