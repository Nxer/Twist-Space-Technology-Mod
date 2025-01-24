package com.Nxer.TwistSpaceTechnology.common.api;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public class ModItemsHandler {

    // region Blood Arsenal
    public static Pair<ItemStack, Integer> AmorphicCatalyst;
    public static Pair<ItemStack, Integer> InfinityEgg;

    // end region

    // region Eternal Singularity
    public static Pair<ItemStack, Integer> NitronicSingularity;
    public static Pair<ItemStack, Integer> PsychoticSingularity;
    public static Pair<ItemStack, Integer> SphaghetticSingularity;
    public static Pair<ItemStack, Integer> PneumaticSingularity;
    public static Pair<ItemStack, Integer> CrypticSingularity;
    public static Pair<ItemStack, Integer> HistoricSingularity;
    public static Pair<ItemStack, Integer> MeteoricSingularity;

    // end region

    // region Amun-Ra
    public static Pair<ItemStack, Integer> LightWeightPlate;
    public static Pair<ItemStack, Integer> ShuttleNoseCone;
    public static Pair<ItemStack, Integer> IonThrusterJet;
    // end region

    // region Graviation Suite
    public static Pair<ItemStack, Integer> CoolingCore;
    public static Pair<ItemStack, Integer> GravitationEngine;
    // end region

    public void initStatics() {
        if (Mods.BloodArsenal.isModLoaded()) {
            AmorphicCatalyst = Pair.of(GTModHandler.getModItem(Mods.BloodArsenal.ID, "amorphic_catalyst", 1), 0);
            InfinityEgg = Pair.of(GTModHandler.getModItem(Mods.BloodArsenal.ID, "infinityegg", 1), 0);
        } else {
            AmorphicCatalyst = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName(Mods.BloodArsenal.ID + ":Amorphic Catalyst"),
                0);
            InfinityEgg = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName(Mods.BloodArsenal.ID + ":Infinity Egg"),
                0);
        }

        if (Mods.EternalSingularity.isModLoaded()) {
            NitronicSingularity = Pair
                .of(GTModHandler.getModItem(Mods.EternalSingularity.ID, "combined_singularity", 1, 0), 0);
            PsychoticSingularity = Pair
                .of(GTModHandler.getModItem(Mods.EternalSingularity.ID, "combined_singularity", 1, 1), 0);
            SphaghetticSingularity = Pair
                .of(GTModHandler.getModItem(Mods.EternalSingularity.ID, "combined_singularity", 1, 2), 0);
            PneumaticSingularity = Pair
                .of(GTModHandler.getModItem(Mods.EternalSingularity.ID, "combined_singularity", 1, 3), 0);
            CrypticSingularity = Pair
                .of(GTModHandler.getModItem(Mods.EternalSingularity.ID, "combined_singularity", 1, 4), 0);
            HistoricSingularity = Pair
                .of(GTModHandler.getModItem(Mods.EternalSingularity.ID, "combined_singularity", 1, 5), 0);
            MeteoricSingularity = Pair
                .of(GTModHandler.getModItem(Mods.EternalSingularity.ID, "combined_singularity", 1, 6), 0);
        }
        if (NitronicSingularity.getLeft() == null) NitronicSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName(Mods.EternalSingularity.ID + ":Nitronic Singularity"),
            0);
        if (PsychoticSingularity.getLeft() == null) PsychoticSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName(Mods.EternalSingularity.ID + ":Psychotic Singularity"),
            0);

        if (SphaghetticSingularity.getLeft() == null) SphaghetticSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName(Mods.EternalSingularity.ID + ":Sphaghettic Singularity"),
            0);

        if (PneumaticSingularity.getLeft() == null) PneumaticSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName(Mods.EternalSingularity.ID + ":Pneumatic Singularity"),
            0);

        if (CrypticSingularity.getLeft() == null) CrypticSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName(Mods.EternalSingularity.ID + ":Cryptic Singularity"),
            0);

        if (HistoricSingularity.getLeft() == null) HistoricSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName(Mods.EternalSingularity.ID + ":Historic Singularity"),
            0);

        if (MeteoricSingularity.getLeft() == null) MeteoricSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName(Mods.EternalSingularity.ID + ":Meteoric Singularity"),
            0);

        if (Mods.GalacticraftAmunRa.isModLoaded()) {
            LightWeightPlate = Pair.of(GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "item.baseItem", 1, 15), 0);
            ShuttleNoseCone = Pair.of(GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "item.baseItem", 1, 16), 0);
            IonThrusterJet = Pair.of(GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.machines2", 1, 1), 0);
        } else {
            LightWeightPlate = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName(Mods.GalacticraftAmunRa.ID + ":Light Weight Plate"),
                0);
            ShuttleNoseCone = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName(Mods.GalacticraftAmunRa.ID + ":Shuttle Nose Cone"),
                0);
            IonThrusterJet = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName(Mods.GalacticraftAmunRa.ID + ":Ion Thruster Jet"),
                0);
        }

        if (Mods.GraviSuite.isModLoaded()) {
            CoolingCore = Pair.of(GTModHandler.getModItem(Mods.GraviSuite.ID, "itemSimpleItem>", 64, 2), 0);
            GravitationEngine = Pair.of(GTModHandler.getModItem(Mods.GraviSuite.ID, "itemSimpleItem>", 64, 3), 0);
        } else {
            CoolingCore = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName(Mods.GraviSuite.ID + ":Cooling Core"),
                0);
            GravitationEngine = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName(Mods.GraviSuite.ID + ":Gravitation Engine"),
                0);
        }
    }
}
