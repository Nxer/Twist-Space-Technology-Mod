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
    protected List<SpawnListEntry> undergroundMonsterList;

    //Northern Biomes
    public static final BiomeGenBase erosion_lake = null;
    public static final BiomeGenBase high_land =null;
    public static final BiomeGenBase glacier =null;
    public static final BiomeGenBase snowy_meadow =null;

    public static final BiomeGenBase inferno_abyss =null;

    //Sorthern Biomes
    public static final BiomeGenBase clover_patch =null;
    public static final BiomeGenBase fungal_jungle =null;
    public static final BiomeGenBase blossom_spring=null;

    public static final BiomeGenBase eclipse_garden=null;

    //Westren Biomes
    public static final BiomeGenBase fairy_wilderness=null;
    public static final BiomeGenBase flower_meadow=null;
    public static final BiomeGenBase primitive_valley=null;
    
    public static final BiomeGenBase temporal_field=null;

    //Eastern Biomes
    public static final BiomeGenBase reed_field=null;
    public static final BiomeGenBase prismatic_lake=null;
    public static final BiomeGenBase pasture=null;

    public static final BiomeGenBase eternal_spring=null;
    public static void registerWithBiomeDictionary() {
        BiomeDictionary.registerBiomeType(erosion_lake, Type.OCEAN, Type.SNOWY);
    }
    public List<SpawnListEntry> getUndergroundSpawnableList() {
    	return this.undergroundMonsterList;
    }
}
