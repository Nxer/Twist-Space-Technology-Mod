package com.Nxer.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

import com.Nxer.world.biomes.BiomeBaseAlfheim;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;

public class WorldProviderAlfheim extends WorldProviderSurface {

    @Override
    public Vec3 getFogColor(float f, float f1) {
        float bright = MathHelper.cos(f * 3.141593F * 2.0F) * 2.0F + 0.5F;
        if (bright < 0.0F) {
            bright = 0.0F;
        }
        if (bright > 1.0F) {
            bright = 1.0F;
        }
        float red = TestUseOnly.skystats[0];// 0.7529412F;
        float green = TestUseOnly.skystats[1];// 1.0F;
        float blue = TestUseOnly.skystats[2];// 0.8470588F;
        red *= bright * TestUseOnly.skystats[3] + (1.0F - TestUseOnly.skystats[3]);// 0.94F + 0.06F;
        green *= bright * TestUseOnly.skystats[4] + (1.0F - TestUseOnly.skystats[3]);// 0.94F + 0.06F;
        blue *= bright * TestUseOnly.skystats[5] + (1.0F - TestUseOnly.skystats[3]);// 0.91F + 0.09F;
        return Vec3.createVectorHelper(red, green, blue);
    }

    public float calculateCelestialAngle(long par1, float par3) {
        return super.calculateCelestialAngle(par1, par3);
    }

    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerAlfheim(worldObj);// new TFWorldChunkManager(worldObj);
        this.dimensionId = WorldStats.dimensionID;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderAlfheim(
            worldObj,
            worldObj.getSeed(),
            worldObj.getWorldInfo()
                .isMapFeaturesEnabled());
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    @Override
    public boolean canRespawnHere() {
        // lie about this until the world is initialized
        // otherwise the server will try to generate enough terrain for a spawn point and that's annoying
        return worldObj.getWorldInfo()
            .isInitialized();
    }

    @Override
    public String getWelcomeMessage() {
        return "";
    }

    @Override
    public String getDepartMessage() {
        return "";
    }

    @Override
    public String getDimensionName() {
        return "Alfheim";
    }

    /**
     * Sleep anytime!
     */
    @Override
    public boolean isDaytime() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        return Vec3.createVectorHelper(43 / 256.0, 46 / 256.0, 99 / 256.0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1) {
        return 1.0F;
    }

    @Override
    public double getHorizon() {
        return 32.0D;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        BiomeGenBase biome = super.getBiomeGenForCoords(x, z);
        if (biome == null) {
            biome = BiomeBaseAlfheim.erosion_lake;
        }
        return biome;
    }

    /**
     * We're just going to check here for chunks with the relight flag set and KILL THEM!
     */
    @Override
    public void updateWeather() {
        super.updateWeather();
    }

    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer() {
        if (super.getSkyRenderer() == null) {
            this.setSkyRenderer(new TFSkyRenderer());
        }

        return super.getSkyRenderer();
    }

    @SideOnly(Side.CLIENT)
    public IRenderHandler getWeatherRenderer() {
        if (super.getWeatherRenderer() == null) {
            this.setWeatherRenderer(new TFWeatherRenderer());
        }

        return super.getWeatherRenderer();
    }

    /**
     * the y level at which clouds are rendered.
     */
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 161.0F;
    }
}
