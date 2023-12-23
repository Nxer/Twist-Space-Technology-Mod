package com.Nxer.world.biomes;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.Nxer.world.WorldStats;

import twilightforest.biomes.TFBiomeTwilightForest;

public class BiomeBaseAlfheim {

    public static final BiomeGenBase erosion_lake = (new TFBiomeTwilightForest(WorldStats.biomeIDoffset + 0))
        .setColor(0x005500)
        .setBiomeName("Erosion Lake Test");

    public static void registerWithBiomeDictionary() {
        BiomeDictionary.registerBiomeType(erosion_lake, Type.OCEAN, Type.SNOWY);
    }
}
