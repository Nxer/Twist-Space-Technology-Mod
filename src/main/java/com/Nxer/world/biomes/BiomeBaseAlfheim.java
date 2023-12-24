package com.Nxer.world.biomes;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.List;

import com.Nxer.world.WorldStats;

import twilightforest.biomes.TFBiomeTwilightForest;

public abstract class BiomeBaseAlfheim extends BiomeGenBase{

    public BiomeBaseAlfheim(int p_i1971_1_) {
        super(p_i1971_1_);
        //TODO Auto-generated constructor stub
    }

    public static final BiomeGenBase erosion_lake = (new TFBiomeTwilightForest(WorldStats.biomeIDoffset + 0))
        .setColor(0x005500)
        .setBiomeName("Erosion Lake Test");

    public static void registerWithBiomeDictionary() {
        BiomeDictionary.registerBiomeType(erosion_lake, Type.OCEAN, Type.SNOWY);
    }

    public List<SpawnListEntry> getUndergroundSpawnableList() {
        return null;
    }
}
