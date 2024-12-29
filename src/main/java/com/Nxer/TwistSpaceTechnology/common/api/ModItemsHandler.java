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
    public void initStatics() {
        if (Mods.BloodArsenal.isModLoaded()) {
            AmorphicCatalyst = Pair.of(GTModHandler.getModItem(Mods.BloodArsenal.ID, "amorphic_catalyst", 1), 0);
            InfinityEgg = Pair.of(GTModHandler.getModItem(Mods.BloodArsenal.ID, "infinityegg", 1), 0);
        } else {
            AmorphicCatalyst = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName("Amorphic Catalyst"),
                0);
            InfinityEgg = Pair.of(
                GTCMItemList.TestItem0.get(1)
                    .setStackDisplayName("Infinity Egg"),
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
                .setStackDisplayName("Nitronic Singularity"),
            0);
        if (PsychoticSingularity.getLeft() == null) PsychoticSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName("Psychotic Singularity"),
            0);

        if (SphaghetticSingularity.getLeft() == null) SphaghetticSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName("Sphaghettic Singularity"),
            0);

        if (PneumaticSingularity.getLeft() == null) PneumaticSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName("Pneumatic Singularity"),
            0);

        if (CrypticSingularity.getLeft() == null) CrypticSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName("Cryptic Singularity"),
            0);

        if (HistoricSingularity.getLeft() == null) HistoricSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName("Historic Singularity"),
            0);

        if (MeteoricSingularity.getLeft() == null) MeteoricSingularity = Pair.of(
            GTCMItemList.TestItem0.get(1)
                .setStackDisplayName("Meteoric Singularity"),
            0);
    }
}
