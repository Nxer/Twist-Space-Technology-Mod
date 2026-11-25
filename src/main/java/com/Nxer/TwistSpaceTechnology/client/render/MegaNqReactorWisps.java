package com.Nxer.TwistSpaceTechnology.client.render;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vazkii.botania.client.fx.FXWisp;

@SideOnly(Side.CLIENT)
public final class MegaNqReactorWisps {

    private static final int COUNT = 171;
    private static final int LOOP_TICKS = 72;
    private static final double RADIUS = 6;
    private static final MegaNqReactorWisps[] WISPS = new MegaNqReactorWisps[COUNT];

    static {
        Random random = new Random(161);
        for (int i = 0; i < COUNT; i++) {
            WISPS[i] = new MegaNqReactorWisps(random);
        }
    }

    private final double height;
    private final double horizontal;
    private final double angle;
    private final double phase;

    private MegaNqReactorWisps(Random random) {
        height = 2 * random.nextDouble() - 1;
        horizontal = Math.sqrt(1 - height * height);
        angle = 2 * Math.PI * random.nextDouble();
        phase = random.nextDouble();
    }

    public static void render(double x, double y, double z, long tick, float partialTick) {
        double time = (tick % LOOP_TICKS + partialTick) / LOOP_TICKS;
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        try {
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(FXWisp.particles);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glDepthMask(false);

            Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            tess.setBrightness(0xF000F0);
            for (int i = 0; i < COUNT; i++) {
                MegaNqReactorWisps wisp = WISPS[i];
                double age = (wisp.phase + time * (1 + i % 2)) % 1;
                double radius = RADIUS * (1 - age);
                double theta = wisp.angle + 0.4 * age;
                boolean orange = i % 3 == 2;
                tess.setColorRGBA(255, orange ? 158 : 46, orange ? 26 : 34, 255);
                drawWisp(
                    tess,
                    x + radius * wisp.horizontal * Math.cos(theta),
                    y + radius * wisp.height,
                    z + radius * wisp.horizontal * Math.sin(theta),
                    0.57 * (1 - Math.abs(2 * age - 1)));
            }
            tess.draw();
        } finally {
            GL11.glPopAttrib();
        }
    }

    private static void drawWisp(Tessellator tess, double x, double y, double z, double size) {
        double rightX = ActiveRenderInfo.rotationX * size;
        double rightZ = ActiveRenderInfo.rotationZ * size;
        double upX = ActiveRenderInfo.rotationYZ * size;
        double upY = ActiveRenderInfo.rotationXZ * size;
        double upZ = ActiveRenderInfo.rotationXY * size;
        tess.addVertexWithUV(x - rightX - upX, y - upY, z - rightZ - upZ, 0, 1);
        tess.addVertexWithUV(x - rightX + upX, y + upY, z - rightZ + upZ, 0, 0);
        tess.addVertexWithUV(x + rightX + upX, y + upY, z + rightZ + upZ, 1, 0);
        tess.addVertexWithUV(x + rightX - upX, y - upY, z + rightZ - upZ, 1, 1);
    }
}
