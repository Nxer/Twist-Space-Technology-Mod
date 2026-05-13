package com.Nxer.TwistSpaceTechnology.client.effect;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MegaNqReactorParticle extends EntityFX {

    public static final int STATIONARY_TICKS = 40;
    public static final int INWARD_TICKS = 160;
    public static final int LIFETIME_TICKS = STATIONARY_TICKS + INWARD_TICKS;

    private static float START_SCALE = 2F;
    private static float END_SCALE = 0.5F;
    private static final float START_ALPHA = 0.92F;
    private static final double SWIRL_RADIUS = 1.55D;
    private static final int TEXTURE_INDEX = 48;

    private final double startX;
    private final double startY;
    private final double startZ;
    private final double centerX;
    private final double centerY;
    private final double centerZ;
    private final double tangentX;
    private final double tangentY;
    private final double tangentZ;
    private final double phase;

    public MegaNqReactorParticle(World world, double startX, double startY, double startZ, double centerX,
        double centerY, double centerZ, double tangentX, double tangentY, double tangentZ, double phase) {
        super(world, startX, startY, startZ, 0.0D, 0.0D, 0.0D);
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.tangentX = tangentX;
        this.tangentY = tangentY;
        this.tangentZ = tangentZ;
        this.phase = phase;
        this.particleMaxAge = LIFETIME_TICKS;
        this.particleScale = START_SCALE;
        this.particleAlpha = START_ALPHA;
        this.noClip = true;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        setParticleTextureIndex(TEXTURE_INDEX);
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge) {
            setDead();
            return;
        }

        if (particleAge <= STATIONARY_TICKS) {
            setPosition(startX, startY, startZ);
            return;
        }

        double progress = (particleAge - STATIONARY_TICKS) / (double) INWARD_TICKS;
        if (progress >= 1.0D) {
            setDead();
            return;
        }

        updateInwardMotion(progress);
    }

    private void updateInwardMotion(double progress) {
        double eased = easeInOut(progress);
        double reverse = 1.0D - eased;
        double turn = Math.sin(progress * Math.PI * 2.0D + phase);
        double swirl = SWIRL_RADIUS * reverse * turn;

        double x = centerX + (startX - centerX) * reverse + tangentX * swirl;
        double y = centerY + (startY - centerY) * reverse + tangentY * swirl;
        double z = centerZ + (startZ - centerZ) * reverse + tangentZ * swirl;
        setPosition(x, y, z);

        particleAlpha = (float) (START_ALPHA * reverse * reverse);
        particleScale = (float) (END_SCALE + (START_SCALE - END_SCALE) * reverse);
    }

    private static double easeInOut(double progress) {
        return progress * progress * (3.0D - 2.0D * progress);
    }
}
