package com.Nxer.TwistSpaceTechnology.client.render;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

/** One GPU buffer shared by every guide view. Only texture lookup and bounded uploads run on the render thread. */
final class StarGuideModel {

    private static final String SOURCE = "/assets/gtnhcommunitymod/structure/star_that_never_was.bin.gz";
    private static final int UPLOAD_BYTES = StarGuideGeometry.STRIDE * 4 * 8192;
    private final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
    private final Comparator<StarGuideGeometry.Range> byDepth = Comparator.comparingDouble(this::depth);
    private CompletableFuture<StarGuideGeometry.Mesh> build;
    private ByteBuffer pending;
    private StarGuideGeometry.Range[] transparent;
    private int vbo, uploaded, opaqueCount;
    private float centerX, centerY, centerZ, extent;
    private float cameraX, cameraY, cameraZ;
    private boolean failed;
    private boolean droneStructure;

    void render(double x, double y, double z, boolean includeDrones) {
        if (droneStructure != includeDrones) {
            dispose();
            droneStructure = includeDrones;
        }
        if (failed) return;
        if (vbo == 0 && build == null) {
            StarGuideGeometry.Material[] palette;
            try {
                palette = StarGuidePalette.snapshot();
            } catch (RuntimeException e) {
                failed = true;
                TwistSpaceTechnology.LOG.error("Cannot resolve the star guide block atlas", e);
                return;
            }
            build = CompletableFuture.supplyAsync(() -> {
                try (InputStream in = StarGuideModel.class.getResourceAsStream(SOURCE)) {
                    if (in == null) throw new IOException("Missing " + SOURCE);
                    return StarGuideGeometry.bake(in, palette, includeDrones);
                } catch (IOException e) {
                    throw new CompletionException(e);
                }
            });
        }
        if (build != null && !build.isDone()) return;
        int oldBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
        GL11.glPushMatrix();
        try {
            if (build != null) {
                var mesh = build.join();
                build = null;
                centerX = mesh.x();
                centerY = mesh.y();
                centerZ = mesh.z();
                extent = mesh.extent();
                opaqueCount = mesh.opaqueCount();
                transparent = mesh.translucent();
                pending = mesh.vertices();
                vbo = GL15.glGenBuffers();
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (long) pending.capacity(), GL15.GL_STATIC_DRAW);
            } else {
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            }
            if (pending != null) {
                int end = Math.min(uploaded + UPLOAD_BYTES, pending.capacity());
                pending.limit(end)
                    .position(uploaded);
                GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, (long) uploaded, pending);
                uploaded = end;
                if (uploaded == pending.capacity()) pending = null;
            }

            GL11.glTranslated(x + .5, y + .5, z + .5);
            double scale = 8d / extent;
            GL11.glScaled(scale, scale, scale);
            GL11.glTranslatef(-centerX, -centerY, -centerZ);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glAlphaFunc(GL11.GL_GREATER, .1f);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            GL11.glVertexPointer(3, GL11.GL_SHORT, StarGuideGeometry.STRIDE, 0L);
            GL11.glTexCoordPointer(2, GL11.GL_FLOAT, StarGuideGeometry.STRIDE, 8L);
            GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, StarGuideGeometry.STRIDE, 16L);
            GL11.glDrawArrays(GL11.GL_QUADS, 0, Math.min(opaqueCount, uploaded / StarGuideGeometry.STRIDE));
            if (pending == null && transparent.length > 0) drawTransparent();
        } catch (CompletionException e) {
            dispose();
            failed = true;
            TwistSpaceTechnology.LOG.error("Cannot bake the full star guide model", e.getCause());
        } finally {
            GL11.glPopMatrix();
            GL11.glPopClientAttrib();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, oldBuffer);
            GL11.glPopAttrib();
        }
    }

    private void drawTransparent() {
        modelView.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
        float x = modelView.get(2), y = modelView.get(6), z = modelView.get(10);
        if (x != cameraX || y != cameraY || z != cameraZ) {
            cameraX = x;
            cameraY = y;
            cameraZ = z;
            Arrays.sort(transparent, byDepth);
        }
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, .01f);
        GL11.glDepthMask(false);
        for (var region : transparent) GL11.glDrawArrays(GL11.GL_QUADS, region.start(), region.count());
    }

    private double depth(StarGuideGeometry.Range region) {
        return cameraX * region.x() + cameraY * region.y() + cameraZ * region.z();
    }

    void dispose() {
        if (vbo != 0) GL15.glDeleteBuffers(vbo);
        if (build != null) build.cancel(false);
        vbo = uploaded = opaqueCount = 0;
        build = null;
        pending = null;
        transparent = null;
        cameraX = cameraY = cameraZ = 0;
        failed = false;
    }
}
