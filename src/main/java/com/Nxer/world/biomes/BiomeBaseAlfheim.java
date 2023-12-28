package com.Nxer.world.biomes;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import twilightforest.entity.EntityTFKobold;
import twilightforest.entity.passive.EntityTFMobileFirefly;

import java.util.List;

import java.util.ArrayList;
import com.Nxer.world.WorldStats;

import appeng.api.storage.ICellCacheRegistry.TYPE;

public abstract class BiomeBaseAlfheim extends BiomeGenBase{

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
    
    public static final BiomeGenBase eternal_spring=null;

    //Eastern Biomes
    public static final BiomeGenBase reed_field=null;
    public static final BiomeGenBase prismatic_lake=null;
    public static final BiomeGenBase pasture=null;

    public static final BiomeGenBase temporal_field=null;

    //
    protected WorldGenBigMushroom bigMushroomGen;
	protected WorldGenForest treeGen;

    public BiomeBaseAlfheim(int p_i1971_1_) {
        super(p_i1971_1_);
        bigMushroomGen = new WorldGenBigMushroom();
		treeGen = new WorldGenForest(false, false);
         spawnableMonsterList.clear();
        // remove squids
        spawnableWaterCreatureList.clear();
        // custom creature list.
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFBighorn.class, 12, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFBoar.class, 10, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.entity.passive.EntityChicken.class, 10, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFDeer.class, 15, 4, 5));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.entity.passive.EntityWolf.class, 5, 4, 4));
        
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFTinyBird.class, 15, 4, 8));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFSquirrel.class, 10, 2, 4));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFBunny.class, 10, 4, 5));
        spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFRaven.class, 10, 1, 2));
        
        undergroundMonsterList = new ArrayList<SpawnListEntry>();
        
        undergroundMonsterList.add(new SpawnListEntry(EntitySpider.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntityZombie.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntityCreeper.class, 1, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntitySlime.class, 10, 4, 4));
        undergroundMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 1, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityTFKobold.class, 10, 4, 8));

		this.spawnableCaveCreatureList.clear();
        this.spawnableCaveCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityBat.class, 10, 8, 8));
        this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFMobileFirefly.class, 10, 8, 8));
        
        theBiomeDecorator.treesPerChunk=10;
        theBiomeDecorator.grassPerChunk=2;
	}
    protected List<SpawnListEntry> undergroundMonsterList;

    public static void registerWithBiomeDictionary() {
        BiomeDictionary.registerBiomeType(erosion_lake, Type.WATER, Type.SNOWY,Type.COLD);
        BiomeDictionary.registerBiomeType(high_land, Type.SPARSE,Type.CONIFEROUS,Type.SNOWY,Type.MOUNTAIN,Type.COLD);
        BiomeDictionary.registerBiomeType(glacier, Type.SPARSE,Type.CONIFEROUS, Type.SNOWY,Type.HILLS,Type.COLD);
        BiomeDictionary.registerBiomeType(snowy_meadow , Type.SPARSE,Type.SNOWY,Type.COLD);
        BiomeDictionary.registerBiomeType(inferno_abyss, Type.SPARSE,Type.NETHER ,Type.MOUNTAIN,Type.HOT);
        BiomeDictionary.registerBiomeType(clover_patch, Type.DENSE,Type.LUSH,Type.PLAINS);
        BiomeDictionary.registerBiomeType(fungal_jungle, Type.DENSE,Type.JUNGLE,Type.MUSHROOM,Type.FOREST,Type.HOT);
        BiomeDictionary.registerBiomeType(blossom_spring, Type.DENSE,Type.WATER,Type.LUSH,Type.PLAINS);
        BiomeDictionary.registerBiomeType(eclipse_garden, Type.MAGICAL, Type.PLAINS);
        BiomeDictionary.registerBiomeType(fairy_wilderness,Type.LUSH ,Type.FOREST);
        BiomeDictionary.registerBiomeType(flower_meadow, Type.DENSE,Type.LUSH,Type.PLAINS);
        BiomeDictionary.registerBiomeType(primitive_valley, Type.HILLS);
        BiomeDictionary.registerBiomeType(eternal_spring, Type.MAGICAL,Type.PLAINS);
        BiomeDictionary.registerBiomeType(reed_field, Type.WATER,Type.SWAMP);
        BiomeDictionary.registerBiomeType(prismatic_lake, Type.WATER);
        BiomeDictionary.registerBiomeType(pasture, Type.DENSE,Type.LUSH,Type.PLAINS);
        BiomeDictionary.registerBiomeType(temporal_field, Type.MAGICAL, Type.PLAINS);
    }
    public List<SpawnListEntry> getUndergroundSpawnableList() {
    	return this.undergroundMonsterList;
    }
}
