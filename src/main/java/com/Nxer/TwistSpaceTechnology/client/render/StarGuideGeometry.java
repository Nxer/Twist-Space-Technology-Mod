package com.Nxer.TwistSpaceTechnology.client.render;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.github.bsideup.jabel.Desugar;

/** Builds an atlas-textured surface from the same sparse cells that the machine validates. No world or GL access. */
final class StarGuideGeometry {

    static final int STRIDE = 20;
    // Four corners per outward-facing quad, in vanilla side order. Each corner packs x + 2*y + 4*z.
    private static final int[][] CORNERS = { { 0, 1, 5, 4 }, { 6, 7, 3, 2 }, { 1, 0, 2, 3 }, { 4, 5, 7, 6 },
        { 0, 4, 6, 2 }, { 5, 1, 3, 7 } };

    @Desugar
    record Face(float minU, float maxU, float minV, float maxV, int rgb) {}

    @Desugar
    record Material(boolean opaque, boolean blended, Face[] faces, Face[] overlays, int overlayMask) {

        Material(boolean opaque, boolean blended, Face[] faces) {
            this(opaque, blended, faces, null, 0);
        }
    }

    @Desugar
    record Range(int start, int count, float x, float y, float z) {}

    @Desugar
    record Mesh(ByteBuffer vertices, int opaqueCount, Range[] translucent, float x, float y, float z, float extent) {}

    private static int coord(byte[] cells, int offset) {
        return (short) ((cells[offset] << 8) | (cells[offset + 1] & 255));
    }

    static Mesh bake(InputStream compressedStructure, Material[] palette) throws IOException {
        int minX = Short.MAX_VALUE, minY = Short.MAX_VALUE, minZ = Short.MAX_VALUE;
        int maxX = Short.MIN_VALUE, maxY = Short.MIN_VALUE, maxZ = Short.MIN_VALUE;
        int total = 0;
        List<byte[]> partitions = new ArrayList<>();
        try (DataInputStream in = new DataInputStream(
            new BufferedInputStream(new GZIPInputStream(compressedStructure)))) {
            if (in.readInt() != 0x53544152) throw new IOException("Invalid star structure signature");
            int count = in.readInt();
            if (count < 1 || count > 256) throw new IOException("Invalid star partition count");
            for (int i = 0; i < count; i++) {
                minX = Math.min(minX, in.readShort());
                minY = Math.min(minY, in.readShort());
                minZ = Math.min(minZ, in.readShort());
                maxX = Math.max(maxX, in.readShort());
                maxY = Math.max(maxY, in.readShort());
                maxZ = Math.max(maxZ, in.readShort());
                int blocks = in.readInt();
                if (blocks < 1 || blocks > 4096) throw new IOException("Invalid star partition size");
                byte[] cells = new byte[blocks * 7];
                in.readFully(cells);
                partitions.add(cells);
                total += blocks;
            }
        }

        int width = maxX - minX + 3, height = maxY - minY + 3, depth = maxZ - minZ + 3;
        if (width < 3 || height < 3 || depth < 3 || (long) width * depth * height > 32_000_000) {
            throw new IOException("Invalid star bounds");
        }
        // One air cell of padding makes all six neighbour lookups valid, including the exterior.
        int layer = width * depth;
        byte[] grid = new byte[layer * height];
        int[] positions = new int[total];
        int index = 0;
        for (byte[] cells : partitions) {
            for (int at = 0; at < cells.length; at += 7) {
                int x = coord(cells, at) - minX + 1;
                int y = coord(cells, at + 2) - minY + 1;
                int z = coord(cells, at + 4) - minZ + 1;
                int material = cells[at + 6] & 255;
                if (x < 1 || y < 1
                    || z < 1
                    || x >= width - 1
                    || y >= height - 1
                    || z >= depth - 1
                    || material >= palette.length
                    || palette[material] == null) {
                    throw new IOException("Invalid star guide cell or material " + material);
                }
                int pos = y * layer + z * width + x;
                if (grid[pos] != 0) throw new IOException("Overlapping star guide cells");
                grid[pos] = (byte) (material + 1);
                positions[index++] = pos;
            }
        }

        int[] neighbours = { -layer, layer, -width, width, -1, 1 };
        int regionX = (width + 31) >> 5, regionZ = (depth + 31) >> 5;
        int[] counts = new int[1 + regionX * regionZ * ((height + 31) >> 5)];
        int[] groups = new int[total];
        byte[] masks = new byte[total];
        for (int i = 0; i < total; i++) {
            int pos = positions[i], material = grid[pos] & 255;
            int mask = 0;
            for (int side = 0; side < 6; side++) {
                int adjacent = grid[pos + neighbours[side]] & 255;
                if (adjacent != 0 && (adjacent == material || palette[adjacent - 1].opaque)) continue;
                mask |= 1 << side;
            }
            masks[i] = (byte) mask;
            if (palette[material - 1].blended) {
                int x = pos % width, y = pos / layer, z = (pos / width) % depth;
                groups[i] = 1 + (x >> 5) + regionX * ((z >> 5) + regionZ * (y >> 5));
            }
            counts[groups[i]] += (Integer.bitCount(mask) + Integer.bitCount(mask & palette[material - 1].overlayMask))
                * 4;
        }

        int vertices = 0;
        int[] cursor = new int[counts.length];
        List<Range> translucent = new ArrayList<>();
        for (int g = 0; g < counts.length; g++) {
            cursor[g] = vertices;
            if (g != 0 && counts[g] != 0) {
                int r = g - 1;
                translucent.add(
                    new Range(
                        vertices,
                        counts[g],
                        minX + (r % regionX) * 32 + 15,
                        minY + (r / (regionX * regionZ)) * 32 + 15,
                        minZ + (r / regionX % regionZ) * 32 + 15));
            }
            vertices += counts[g];
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(Math.multiplyExact(vertices, STRIDE))
            .order(ByteOrder.nativeOrder());
        for (int i = 0; i < total; i++) {
            if (masks[i] == 0) continue;
            int pos = positions[i];
            int x = pos % width + minX - 1, y = pos / layer + minY - 1, z = pos / width % depth + minZ - 1;
            Material material = palette[(grid[pos] & 255) - 1];
            int group = groups[i];
            buffer.position(cursor[group] * STRIDE);
            for (int side = 0; side < 6; side++) {
                if ((masks[i] & (1 << side)) == 0) continue;
                putFace(buffer, x, y, z, side, material.faces[side]);
                cursor[group] += 4;
                if ((material.overlayMask & (1 << side)) != 0) {
                    putFace(buffer, x, y, z, side, material.overlays[side]);
                    cursor[group] += 4;
                }
            }
        }
        buffer.clear();
        return new Mesh(
            buffer,
            counts[0],
            translucent.toArray(new Range[0]),
            (minX + maxX + 1) * .5f,
            (minY + maxY + 1) * .5f,
            (minZ + maxZ + 1) * .5f,
            Math.max(width, Math.max(height, depth)) - 2);
    }

    private static void putFace(ByteBuffer buffer, int x, int y, int z, int side, Face face) {
        for (int corner = 0; corner < 4; corner++) {
            int point = CORNERS[side][corner];
            buffer.putShort((short) (x + (point & 1)))
                .putShort((short) (y + ((point >> 1) & 1)))
                .putShort((short) (z + (point >> 2)))
                .putShort((short) 0);
            buffer.putFloat(corner == 0 || corner == 3 ? face.minU : face.maxU)
                .putFloat(corner < 2 ? face.maxV : face.minV);
            buffer.put((byte) (face.rgb >> 16))
                .put((byte) (face.rgb >> 8))
                .put((byte) face.rgb)
                .put((byte) 255);
        }
    }

    private StarGuideGeometry() {}
}
