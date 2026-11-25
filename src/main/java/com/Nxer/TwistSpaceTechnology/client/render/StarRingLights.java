package com.Nxer.TwistSpaceTechnology.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarRings;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStarContainment;
import com.gtnewhorizons.angelica.dynamiclights.DynamicLights;
import com.gtnewhorizons.angelica.dynamiclights.IDynamicLightSource;
import com.gtnewhorizons.angelica.dynamiclights.IDynamicLightWorldRenderer;

/** Optional client lighting: six representative amber lamps per ring, never one light source per block. */
final class StarRingLights {

    private static final Map<TileStarContainment, List<Lamp>> SOURCES = new HashMap<>();

    static void track(TileStarContainment star) {
        if (!DynamicLights.isEnabled() || SOURCES.containsKey(star)) return;
        List<Lamp> lamps = new ArrayList<>(18);
        for (int ring = 0; ring < 3; ring++) {
            List<StarRings.Cell> candidates = new ArrayList<>();
            for (StarRings.Cell cell : StarRings.ring(ring))
                if (cell.material == StarRings.Material.TRANSLUCENT) candidates.add(cell);
            List<StarRings.Cell> selected = new ArrayList<>(6);
            while (!candidates.isEmpty() && selected.size() < 6) {
                StarRings.Cell best = null;
                double distance = -1;
                for (StarRings.Cell cell : candidates) {
                    double nearest = Double.MAX_VALUE;
                    for (StarRings.Cell previous : selected) {
                        double dx = cell.x - previous.x, dy = cell.y - previous.y, dz = cell.z - previous.z;
                        nearest = Math.min(nearest, dx * dx + dy * dy + dz * dz);
                    }
                    if (nearest > distance) {
                        distance = nearest;
                        best = cell;
                    }
                }
                if (distance < 256) break;
                selected.add(best);
                candidates.remove(best);
                lamps.add(new Lamp(star, ring, best));
            }
        }
        SOURCES.put(star, lamps);
    }

    static void tick() {
        if (!DynamicLights.isEnabled()) {
            clear();
            return;
        }
        Iterator<Map.Entry<TileStarContainment, List<Lamp>>> iterator = SOURCES.entrySet()
            .iterator();
        while (iterator.hasNext()) {
            Map.Entry<TileStarContainment, List<Lamp>> entry = iterator.next();
            TileStarContainment star = entry.getKey();
            if (star.isInvalid() || star.getWorldObj() != Minecraft.getMinecraft().theWorld
                || !star.formed
                || !star.ringsVisible
                || !star.getWorldObj()
                    .blockExists(star.xCoord, star.yCoord, star.zCoord)
                || star.getWorldObj()
                    .getTileEntity(star.xCoord, star.yCoord, star.zCoord) != star) {
                for (Lamp lamp : entry.getValue()) DynamicLights.get()
                    .removeLightSource(lamp);
                iterator.remove();
                continue;
            }
            for (Lamp lamp : entry.getValue()) {
                lamp.angelica$dynamicLightTick();
                if (!DynamicLights.get()
                    .containsLightSource(lamp))
                    DynamicLights.get()
                        .addLightSource(lamp);
            }
        }
    }

    static void clear() {
        for (List<Lamp> lamps : SOURCES.values()) for (Lamp lamp : lamps) DynamicLights.get()
            .removeLightSource(lamp);
        SOURCES.clear();
    }

    private static final class Lamp implements IDynamicLightSource {

        private final TileStarContainment star;
        private final int ring;
        private final double[] axis;
        private final double vx, vy, vz;
        private double x, y, z, previousX = Double.NaN, previousY, previousZ;
        private long lastUpdate = Long.MIN_VALUE;

        Lamp(TileStarContainment star, int ring, StarRings.Cell cell) {
            this.star = star;
            this.ring = ring;
            axis = StarRings.axis(ring);
            vx = cell.x;
            vy = cell.y - StarRings.CENTER_Y;
            vz = cell.z;
            angelica$dynamicLightTick();
        }

        @Override
        public void angelica$dynamicLightTick() {
            double angle = Math.toRadians(
                StarRings.rotationDegrees(
                    ring,
                    star.getWorldObj()
                        .getTotalWorldTime()));
            double c = Math.cos(angle), s = Math.sin(angle), dot = axis[0] * vx + axis[1] * vy + axis[2] * vz;
            double rx = vx * c + (axis[1] * vz - axis[2] * vy) * s + axis[0] * dot * (1 - c);
            double ry = vy * c + (axis[2] * vx - axis[0] * vz) * s + axis[1] * dot * (1 - c);
            double rz = vz * c + (axis[0] * vy - axis[1] * vx) * s + axis[2] * dot * (1 - c);
            double yaw = star.ringRotation * Math.PI / 2;
            x = star.xCoord + .5 + rx * Math.cos(yaw) + rz * Math.sin(yaw);
            y = star.yCoord + .5 + StarRings.CENTER_Y - 32 + ry;
            z = star.zCoord + .5 - rx * Math.sin(yaw) + rz * Math.cos(yaw);
        }

        @Override
        public double angelica$getDynamicLightX() {
            return x;
        }

        @Override
        public double angelica$getDynamicLightY() {
            return y;
        }

        @Override
        public double angelica$getDynamicLightZ() {
            return z;
        }

        @Override
        public int angelica$getLuminance() {
            return star.formed && star.ringsVisible ? 15 : 0;
        }

        @Override
        public void angelica$resetDynamicLight() {
            lastUpdate = Long.MIN_VALUE;
        }

        @Override
        public boolean angelica$updateDynamicLight(IDynamicLightWorldRenderer renderer) {
            long tick = star.getWorldObj()
                .getTotalWorldTime();
            if (lastUpdate != Long.MIN_VALUE && tick - lastUpdate < 4) return false;
            lastUpdate = tick;
            if (!Double.isNaN(previousX) && Math.abs(x - previousX) < .3
                && Math.abs(y - previousY) < .3
                && Math.abs(z - previousZ) < .3) return false;
            angelica$scheduleTrackedChunksRebuild(renderer);
            previousX = x;
            previousY = y;
            previousZ = z;
            rebuild(renderer, x, y, z, false);
            return true;
        }

        @Override
        public void angelica$scheduleTrackedChunksRebuild(IDynamicLightWorldRenderer renderer) {
            if (!Double.isNaN(previousX)) rebuild(renderer, previousX, previousY, previousZ, true);
        }

        private static void rebuild(IDynamicLightWorldRenderer renderer, double x, double y, double z,
            boolean removal) {
            // Angelica's 7.75-block light radius intersects at most eight render sections.
            for (int cx = (int) Math.floor((x - 7.75) / 16); cx <= (int) Math.floor((x + 7.75) / 16); cx++)
                for (int cy = (int) Math.floor((y - 7.75) / 16); cy <= (int) Math.floor((y + 7.75) / 16); cy++)
                    for (int cz = (int) Math.floor((z - 7.75) / 16); cz <= (int) Math.floor((z + 7.75) / 16); cz++) {
                        if (removal) DynamicLights.scheduleChunkRebuildForRemoval(renderer, cx, cy, cz);
                        else DynamicLights.scheduleChunkRebuild(renderer, cx, cy, cz);
                    }
        }
    }
}
