package com.Nxer.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

public abstract class GenLayerAlfheim extends GenLayer{

    public GenLayerAlfheim(long p_i2125_1_) {
        super(p_i2125_1_);
        //TODO Auto-generated constructor stub
    }
    public static GenLayer[] makeTheWorld(long par1) {
        GenLayer biomes=null;
        GenLayer genlayervoronoizoom = new GenLayerVoronoiZoom(10L, biomes);
        
		biomes.initWorldGenSeed(par1);
		genlayervoronoizoom.initWorldGenSeed(par1);

        
		return (new GenLayer[] { biomes, genlayervoronoizoom });
    }
}
