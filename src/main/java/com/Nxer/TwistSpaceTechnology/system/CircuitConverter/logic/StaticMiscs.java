package com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.dreammaster.gthandler.CustomItemList;

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
        CircuitULV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Primitive));
        CircuitLV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Basic));
        CircuitMV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Good));
        CircuitHV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Advanced));
        CircuitEV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Data));
        CircuitIV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Elite));
        CircuitLuV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Master));
        CircuitZPM = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Ultimate));
        CircuitUV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.SuperconductorUHV));
        CircuitUHV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Infinite));
        CircuitUEV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Bio));
        CircuitUIV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Optical));
        CircuitUMV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Exotic));
        CircuitUMV.add(CustomItemList.PikoCircuit.get(1));
        CircuitUXV = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Cosmic));
        CircuitUXV.add(CustomItemList.QuantumCircuit.get(1));
        CircuitMAX = GTOreDictUnificator.getOres(OrePrefixes.circuit.get(Materials.Transcendent));
    }
}
