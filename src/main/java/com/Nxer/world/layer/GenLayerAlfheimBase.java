package com.Nxer.world.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import com.Nxer.world.biomes.BiomeBaseAlfheim;

import twilightforest.biomes.TFBiomeBase;

public class GenLayerAlfheimBase extends GenLayerAlfheim {

    private static final int RARE_BIOME_CHANCE = 15;
    protected BiomeGenBase commonBiomes[] = (new BiomeGenBase[] { BiomeBaseAlfheim.erosion_lake });
    protected BiomeGenBase rareBiomes[] = (new BiomeGenBase[] { TFBiomeBase.tfLake, TFBiomeBase.deepMushrooms,
        TFBiomeBase.enchantedForest, TFBiomeBase.clearing });

    public GenLayerAlfheimBase(long l, GenLayer genlayer) {
        super(l);
        parent = genlayer;
    }

    public GenLayerAlfheimBase(long p_i2125_1_) {
        super(p_i2125_1_);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int[] getInts(int x, int z, int width, int depth) {
        int dest[] = IntCache.getIntCache(width * depth);
        for (int dz = 0; dz < depth; dz++) {
            for (int dx = 0; dx < width; dx++) {
                initChunkSeed(dx + x, dz + z);
                if (nextInt(RARE_BIOME_CHANCE) == 0) {
                    // make rare biome
                    dest[dx + dz * width] = rareBiomes[nextInt(rareBiomes.length)].biomeID;
                } else {
                    // make common biome
                    dest[dx + dz * width] = commonBiomes[nextInt(commonBiomes.length)].biomeID;
                }
            }

        }
        return dest;
    }
}
