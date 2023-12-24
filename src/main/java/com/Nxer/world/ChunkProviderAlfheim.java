package com.Nxer.world;

import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.world.MapGenTFHollowTree;
import twilightforest.world.MapGenTFMajorFeature;
import twilightforest.world.TFGenCaves;
import twilightforest.world.TFGenRavine;
import twilightforest.world.TFWorld;

import java.util.Random;

import com.Nxer.world.biomes.BiomeBaseAlfheim;

public class ChunkProviderAlfheim implements IChunkProvider{
private Random rand;
   private NoiseGeneratorOctaves noiseGen4;
   public NoiseGeneratorOctaves noiseGen5;
   public NoiseGeneratorOctaves noiseGen6;
   public NoiseGeneratorOctaves mobSpawnerNoise;
   private World worldObj;
   private double[] stoneNoise = new double[256];
   private TFGenCaves caveGenerator = new TFGenCaves();
   private TFGenRavine ravineGenerator = new TFGenRavine();
   private BiomeGenBase[] biomesForGeneration;
   double[] noise3;
   double[] noise1;
   double[] noise2;
   double[] noise5;
   double[] noise6;
   float[] squareTable;
   int[][] unusedIntArray32x32 = new int[32][32];
   private WorldType field_147435_p;
   private NoiseGeneratorOctaves field_147431_j;
   private NoiseGeneratorOctaves field_147432_k;
   private NoiseGeneratorOctaves field_147429_l;
   private NoiseGeneratorPerlin field_147430_m;
   private double[] terrainCalcs;
   private float[] parabolicField;
   double[] field_147427_d;
   double[] field_147428_e;
   double[] field_147425_f;
   double[] field_147426_g;
   int[][] field_73219_j = new int[32][32];
   private MapGenTFMajorFeature majorFeatureGenerator = new MapGenTFMajorFeature();
   private MapGenTFHollowTree hollowTreeGenerator = new MapGenTFHollowTree();

    public ChunkProviderAlfheim(World world, long l, boolean flag) {
      this.worldObj = world;
      this.rand = new Random(l);
      this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
      this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
      this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
      this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
      this.field_147435_p = world.getWorldInfo().getTerrainType();
      this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
      this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
      this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
      this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
      this.terrainCalcs = new double[825];
      this.parabolicField = new float[25];

      for(int j = -2; j <= 2; ++j) {
         for(int k = -2; k <= 2; ++k) {
            float f = 10.0F / MathHelper.sqrt_float((float)(j * j + k * k) + 0.2F);
            this.parabolicField[j + 2 + (k + 2) * 5] = f;
         }
      }

   }
    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    @Override
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'provideChunk'");
    }

    @Override
    public Chunk loadChunk(int i, int j) {
        return provideChunk(i, j);
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'populate'");
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveChunks'");
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "AlfheimLevelSource";
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, int mapX, int mapY, int mapZ) {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(mapX, mapZ);

        if (biome == null) {
            return null;
        } else
            if (mapY < TFWorld.SEALEVEL && creatureType == EnumCreatureType.monster) {
                // cave monsters!
                return ((BiomeBaseAlfheim) biome).getUndergroundSpawnableList();
            } else {
               return biome.getSpawnableList(creatureType);
            }
    }

    @Override
    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_,
            int p_147416_5_) {
            return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(int p_82695_1_, int p_82695_2_) {
    }

    @Override
    public void saveExtraData() {
    }
    
}
