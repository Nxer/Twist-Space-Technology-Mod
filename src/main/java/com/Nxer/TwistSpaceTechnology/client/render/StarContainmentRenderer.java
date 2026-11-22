package com.Nxer.TwistSpaceTechnology.client.render;

import java.nio.DoubleBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMeteorFormation;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarRings;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStarContainment;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/** Cached virtual meteor blocks and a separate containment shell; neither modifies the world. */
public final class StarContainmentRenderer extends TileEntitySpecialRenderer {

    private static final int LAT = 24, LON = 48;
    private static final double[][] VERTICES = vertices();
    private final Map<TileStarContainment, StarMeteorMesh> surfaces = new LinkedHashMap<>(8, 0.75f, true);
    private final DoubleBuffer clipPlane = BufferUtils.createDoubleBuffer(4);
    private long buildBudgetNanos = 2_000_000;
    private final StarRingMesh[] rings = new StarRingMesh[3];
    private final StarGuideModel guideModel = new StarGuideModel();
    private static final double[][] RING_AXES = { StarRings.axis(0), StarRings.axis(1), StarRings.axis(2) };

    public StarContainmentRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileStarContainment.class, this);
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partial) {
        TileStarContainment star = (TileStarContainment) tile;
        if (star.isGuidePreview()) {
            guideModel.render(x, y, z, star.includesGuidePreviewDrones());
            return;
        }
        if (!star.hasMeteor() || !star.meteorRenderEnabled) {
            StarMeteorMesh previous = surfaces.remove(star);
            if (previous != null) previous.dispose();
        }
        if (!star.ringsVisible && !star.miningRenderEnabled && !star.meteorRenderEnabled) return;
        double clock = tile.getWorldObj()
            .getTotalWorldTime() + partial;
        double enclosure = star.miningRenderEnabled ? star.field.value(clock) : 0;
        if (!star.formed && enclosure <= 0) return;
        int pass = MinecraftForgeClient.getRenderPass();
        // Opaque meshes must finish before the transparent containment and both linked beam segments.
        // A direct preview call outside the world passes (-1) still draws both parts.
        boolean opaque = pass != 1;
        float lightX = OpenGlHelper.lastBrightnessX, lightY = OpenGlHelper.lastBrightnessY;
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        try {
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if (opaque && star.formed && star.ringsVisible) {
                drawRings(star, clock);
                if (Loader.isModLoaded("angelica")) StarRingLights.track(star);
            }
            if (opaque && star.meteorRenderEnabled && star.hasMeteor()) {
                GL11.glPushMatrix();
                GL11.glTranslated(0, 68, 0);
                GL11.glRotated(clock * 0.06, 0, 1, 0);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                bindTexture(TextureMap.locationBlocksTexture);
                drawSurface(star, clock);
                GL11.glPopMatrix();
            }
            if (pass == 0) return;
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            if (star.formed && star.miningRenderEnabled) StarBeamRenderer.floorSeal(star.powered);
            if (star.formed && star.miningRenderEnabled && star.beam.visible(clock)) {
                // Keep the emitter's last position through shutdown, even if the physical miner was removed.
                GL11.glPushMatrix();
                GL11.glTranslated(0, -star.beamSourceDepth, 0);
                StarBeamRenderer.miner(star.beam, star.beamSourceDepth, clock);
                GL11.glPopMatrix();
                StarBeamRenderer.receiver(star, clock);
            }
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (star.meteorRenderEnabled && star.meteorAnimationEnabled && star.hasMeteor()) {
                double progress = star.interpolatedProgress(clock);
                if (progress < star.formationTicks) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(0, 68, 0);
                    drawFormationGlow(star, progress);
                    GL11.glPopMatrix();
                }
            }
            if (enclosure > 0) {
                GL11.glTranslated(0, 68, 0);
                drawEnclosure(enclosure);
                if (star.formed && star.beamSourceDepth > 0 && star.beam.visible(clock))
                    StarBeamRenderer.ripple(star, clock);
            }
        } finally {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightX, lightY);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    private void drawRings(TileStarContainment star, double clock) {
        boolean ready = true;
        for (int i = 0; i < rings.length; i++) {
            if (rings[i] == null) {
                if (buildBudgetNanos <= 0) {
                    ready = false;
                    continue;
                }
                long start = System.nanoTime();
                rings[i] = new StarRingMesh(i);
                buildBudgetNanos = Math.max(0, buildBudgetNanos - (System.nanoTime() - start));
            }
            buildBudgetNanos = Math.max(0, buildBudgetNanos - rings[i].build(buildBudgetNanos));
            ready &= rings[i].ready();
        }
        if (!ready) return;
        bindTexture(TextureMap.locationBlocksTexture);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, .1f);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDepthMask(true);
        int light = star.getWorldObj()
            .getLightBrightnessForSkyBlocks(star.xCoord, star.yCoord + 68, star.zCoord, 0);
        GL11.glPushMatrix();
        GL11.glTranslated(0, StarRings.CENTER_Y - 32, 0);
        GL11.glRotatef(star.ringRotation * 90, 0, 1, 0);
        for (int i = 0; i < rings.length; i++) {
            GL11.glPushMatrix();
            double[] axis = RING_AXES[i];
            GL11.glRotated(StarRings.rotationDegrees(i, clock), axis[0], axis[1], axis[2]);
            rings[i].draw(light);
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    private void drawSurface(TileStarContainment star, double clock) {
        if (star.materials.length == 0 || star.materials.length != star.weights.length) return;
        long start = System.nanoTime();
        StarMeteorMesh surface = surfaces.get(star);
        if (surface == null || !surface.matches(star)) {
            if (surface != null) surface.dispose();
            surface = new StarMeteorMesh(star);
            surfaces.put(star, surface);
            if (surfaces.size() > 8) {
                Iterator<StarMeteorMesh> oldest = surfaces.values()
                    .iterator();
                oldest.next()
                    .dispose();
                oldest.remove();
            }
        }
        buildBudgetNanos = Math.max(0, buildBudgetNanos - (System.nanoTime() - start));
        buildBudgetNanos = Math.max(
            0,
            buildBudgetNanos - surface.build(
                buildBudgetNanos,
                star.interpolatedProgress(clock),
                star.duration,
                star.formationTicks,
                star.beamWarmupTicks,
                star.interpolatedMiningProgress(clock),
                star.meteorAnimationEnabled));
        surface.draw();
    }

    private void drawEnclosure(double amount) {
        double height = 58 * Math.sin(amount * Math.PI / 2);
        setClip(GL11.GL_CLIP_PLANE0, 1, height);
        setClip(GL11.GL_CLIP_PLANE1, -1, height);
        GL11.glEnable(GL11.GL_CLIP_PLANE0);
        GL11.glEnable(GL11.GL_CLIP_PLANE1);
        // Deeper blue separates the shell from daylight sky while keeping the meteor visible through it.
        drawSphere(58, 0.26f, 0.46f, 0.66f, 0.14f);
        GL11.glDisable(GL11.GL_CLIP_PLANE0);
        GL11.glDisable(GL11.GL_CLIP_PLANE1);
        GL11.glLineWidth(1.5f);
        for (int ring = -2; ring <= 2; ring++) {
            double yy = ring * 20;
            if (Math.abs(yy) > height) continue;
            float alpha = (float) (0.42 * Math.min(1, (height - Math.abs(yy)) / 3));
            GL11.glColor4f(0.38f, 0.66f, 0.82f, alpha);
            circle(Math.sqrt(58 * 58 - yy * yy), yy);
        }
        if (amount < 1) {
            double radius = Math.sqrt(Math.max(0, 58 * 58 - height * height));
            GL11.glLineWidth(2);
            GL11.glColor4f(0.38f, 0.78f, 0.96f, (float) (0.8 * Math.min(1, (1 - amount) * 8)));
            circle(radius, height);
            circle(radius, -height);
        }
    }

    private static void drawFormationGlow(TileStarContainment star, double progress) {
        double fade = StarMeteorFormation.glow(progress, star.formationTicks);
        if (fade <= 0) return;
        double size = StarMeteorFormation.size(progress, star.formationTicks);
        double radius = Math.max(.5 * StarMeteorFormation.coreSize(progress), star.radius * size) + 1.5 * fade;
        GL11.glRotated(progress * .6, 0, 1, 0);
        GL11.glLineWidth(1.5f);
        GL11.glColor4f(.78f, .12f, .16f, (float) (.4 * fade));
        circle(radius, 0);
        GL11.glPushMatrix();
        GL11.glRotated(65, 1, 0, 1);
        GL11.glColor4f(1, .67f, .27f, (float) (.3 * fade));
        circle(radius, 0);
        GL11.glPopMatrix();
        // Fixed-count sparks in one draw call; no particle/entity allocation proportional to meteor volume.
        GL11.glPointSize(3);
        GL11.glBegin(GL11.GL_POINTS);
        for (int i = 0; i < 24; i++) {
            double yy = 1 - (i + .5) / 12;
            double a = i * 2.399963229728653 + progress * .025;
            double rr = Math.sqrt(1 - yy * yy) * radius;
            if ((i & 3) == 0) GL11.glColor4f(1, .76f, .36f, (float) (.85 * fade));
            else GL11.glColor4f(.9f, .18f, .22f, (float) (.7 * fade));
            GL11.glVertex3d(Math.cos(a) * rr, yy * radius, Math.sin(a) * rr);
        }
        GL11.glEnd();
    }

    private void setClip(int plane, double y, double offset) {
        setClip(plane, 0, y, 0, offset);
    }

    private void setClip(int plane, double x, double y, double z, double offset) {
        clipPlane.clear();
        clipPlane.put(x)
            .put(y)
            .put(z)
            .put(offset)
            .flip();
        GL11.glClipPlane(plane, clipPlane);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        buildBudgetNanos = 2_000_000;
        Iterator<Map.Entry<TileStarContainment, StarMeteorMesh>> iterator = surfaces.entrySet()
            .iterator();
        while (iterator.hasNext()) {
            Map.Entry<TileStarContainment, StarMeteorMesh> entry = iterator.next();
            TileStarContainment tile = entry.getKey();
            if (tile.isInvalid() || tile.getWorldObj() != Minecraft.getMinecraft().theWorld) {
                entry.getValue()
                    .dispose();
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Loader.isModLoaded("angelica")) StarRingLights.tick();
    }

    @SubscribeEvent
    public void onTextureReload(TextureStitchEvent.Post event) {
        if (event.map.getTextureType() == 0) clearSurfaces();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) clearSurfaces();
    }

    private void clearSurfaces() {
        guideModel.dispose();
        if (Loader.isModLoaded("angelica")) StarRingLights.clear();
        for (StarMeteorMesh surface : surfaces.values()) surface.dispose();
        surfaces.clear();
        for (int i = 0; i < rings.length; i++) {
            if (rings[i] != null) rings[i].dispose();
            rings[i] = null;
        }
    }

    private static void drawSphere(double radius, float r, float g, float b, float alpha) {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setColorRGBA_F(r, g, b, alpha);
        for (int lat = 0; lat < LAT; lat++) for (int lon = 0; lon < LON; lon++) {
            plainVertex(t, lat, lon, radius);
            plainVertex(t, lat + 1, lon, radius);
            plainVertex(t, lat + 1, lon + 1, radius);
            plainVertex(t, lat, lon + 1, radius);
        }
        t.draw();
    }

    private static void plainVertex(Tessellator t, int lat, int lon, double radius) {
        double[] p = VERTICES[lat * (LON + 1) + lon];
        t.addVertex(p[0] * radius, p[1] * radius, p[2] * radius);
    }

    private static void circle(double radius, double y) {
        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i < 96; i++) {
            double a = i * Math.PI / 48;
            GL11.glVertex3d(Math.cos(a) * radius, y, Math.sin(a) * radius);
        }
        GL11.glEnd();
    }

    private static double[][] vertices() {
        double[][] vertices = new double[(LAT + 1) * (LON + 1)][3];
        for (int lat = 0; lat <= LAT; lat++) for (int lon = 0; lon <= LON; lon++) {
            double theta = lat * Math.PI / LAT, phi = lon * Math.PI * 2 / LON;
            vertices[lat * (LON + 1) + lon] = new double[] { Math.sin(theta) * Math.cos(phi), Math.cos(theta),
                Math.sin(theta) * Math.sin(phi) };
        }
        return vertices;
    }

}
