package com.Nxer.TwistSpaceTechnology.client.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MegaNqReactorParticleBatch {

    public static final double RADIUS = 6.0D;
    /** 每批球面采样粒子数；与较高生成频率叠加时略增以减轻单薄感。 */
    public static final int PARTICLE_COUNT = 400;

    private static final double GOLDEN_ANGLE = Math.PI * (3.0D - Math.sqrt(5.0D));
    private static final double TWO_PI = Math.PI * 2.0D;

    private MegaNqReactorParticleBatch() {}

    public static void spawn(World world, double centerX, double centerY, double centerZ) {
        if (world == null || !world.isRemote) {
            return;
        }

        EffectRenderer renderer = Minecraft.getMinecraft().effectRenderer;
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            renderer.addEffect(createParticle(world, centerX, centerY, centerZ, i));
        }
    }

    private static MegaNqReactorParticle createParticle(World world, double centerX, double centerY, double centerZ,
        int index) {
        double y = 1.0D - (2.0D * index + 1.0D) / PARTICLE_COUNT;
        double ringRadius = Math.sqrt(Math.max(0.0D, 1.0D - y * y));
        double theta = GOLDEN_ANGLE * index;
        double normalX = Math.cos(theta) * ringRadius;
        double normalY = y;
        double normalZ = Math.sin(theta) * ringRadius;

        double startX = centerX + normalX * RADIUS;
        double startY = centerY + normalY * RADIUS;
        double startZ = centerZ + normalZ * RADIUS;
        Tangent tangent = createTangent(normalX, normalY, normalZ);
        double phase = (index * TWO_PI) / PARTICLE_COUNT;

        return new MegaNqReactorParticle(
            world,
            startX,
            startY,
            startZ,
            centerX,
            centerY,
            centerZ,
            tangent.x,
            tangent.y,
            tangent.z,
            phase);
    }

    private static Tangent createTangent(double normalX, double normalY, double normalZ) {
        double axisX = Math.abs(normalY) > 0.82D ? 1.0D : 0.0D;
        double axisY = Math.abs(normalY) > 0.82D ? 0.0D : 1.0D;

        double tangentX = -normalZ * axisY;
        double tangentY = normalZ * axisX;
        double tangentZ = normalX * axisY - normalY * axisX;
        double length = Math.sqrt(tangentX * tangentX + tangentY * tangentY + tangentZ * tangentZ);
        if (length <= 1.0E-6D) {
            return new Tangent(1.0D, 0.0D, 0.0D);
        }
        return new Tangent(tangentX / length, tangentY / length, tangentZ / length);
    }

    private static class Tangent {

        private final double x;
        private final double y;
        private final double z;

        private Tangent(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
