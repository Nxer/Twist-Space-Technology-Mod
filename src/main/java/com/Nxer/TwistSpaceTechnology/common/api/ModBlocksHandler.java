package com.Nxer.TwistSpaceTechnology.common.api;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.enums.Mods;

public final class ModBlocksHandler {

    // region Thaumic Bases
    public static Pair<Block, Integer> AirCrystalBlock;
    public static Pair<Block, Integer> FireCrystalBlock;
    public static Pair<Block, Integer> WaterCrystalBlock;
    public static Pair<Block, Integer> EarthCrystalBlock;
    public static Pair<Block, Integer> OrderCrystalBlock;
    public static Pair<Block, Integer> EntropyCrystalBlock;
    public static Pair<Block, Integer> MixedCrystalBlock;
    public static Pair<Block, Integer> TaintedCrystalBlock;
    public static Pair<Block, Integer> DustBlock;
    public static Pair<Block, Integer> VoidBlock;
    public static Pair<Block, Integer> ThauminiteBlock;

    // endregion

    // region Thaumic Horizons
    public static Pair<Block, Integer> BlockCrystalDeep;

    // endregion

    // region Automagy
    public static Pair<Block, Integer> BlockTranslucent;

    // endregion

    public void initStatics() {
        if (Mods.ThaumicBases.isModLoaded()) {
            Block crystalBlock = Block.getBlockFromName(Mods.ThaumicBases.ID + ":crystalBlock");
            AirCrystalBlock = Pair.of(crystalBlock, 0);
            FireCrystalBlock = Pair.of(crystalBlock, 1);
            WaterCrystalBlock = Pair.of(crystalBlock, 2);
            EarthCrystalBlock = Pair.of(crystalBlock, 3);
            OrderCrystalBlock = Pair.of(crystalBlock, 4);
            EntropyCrystalBlock = Pair.of(crystalBlock, 5);
            MixedCrystalBlock = Pair.of(crystalBlock, 6);
            TaintedCrystalBlock = Pair.of(crystalBlock, 7);
            DustBlock = Pair.of(Block.getBlockFromName(Mods.ThaumicBases.ID + ":blockSalisMundus"), 0);
            VoidBlock = Pair.of(Block.getBlockFromName(Mods.ThaumicBases.ID + ":voidBlock"), 0);
            ThauminiteBlock = Pair.of(Block.getBlockFromName(Mods.ThaumicBases.ID + ":thauminiteBlock"), 0);
        } else {
            AirCrystalBlock = Pair.of(Blocks.wool, 4);
            FireCrystalBlock = Pair.of(Blocks.wool, 14);
            WaterCrystalBlock = Pair.of(Blocks.wool, 3);
            EarthCrystalBlock = Pair.of(Blocks.wool, 5);
            OrderCrystalBlock = Pair.of(Blocks.wool, 0);
            EntropyCrystalBlock = Pair.of(Blocks.wool, 7);
            MixedCrystalBlock = Pair.of(Blocks.wool, 2);
            TaintedCrystalBlock = Pair.of(Blocks.wool, 10);
            DustBlock = Pair.of(Blocks.wool, 6);
            VoidBlock = Pair.of(Blocks.wool, 15);
            ThauminiteBlock = Pair.of(Blocks.wool, 11);
        }

        if (Mods.ThaumicHorizons.isModLoaded()) {
            BlockCrystalDeep = Pair.of(Block.getBlockFromName(Mods.ThaumicHorizons.ID + ":crystalDeep"), 0);
        } else {
            BlockCrystalDeep = Pair.of(Blocks.glowstone, 0);
        }

        if (Mods.Automagy.isModLoaded()) {
            BlockTranslucent = Pair.of(Block.getBlockFromName(Mods.Automagy.ID + ":blockTranslucent"), 0);
        } else {
            BlockTranslucent = Pair.of(Blocks.glowstone, 0);
        }

    }

}
