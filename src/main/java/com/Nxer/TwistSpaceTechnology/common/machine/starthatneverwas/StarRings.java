package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import net.minecraft.block.Block;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/** The authored ring blocks, shared by construction, reversible storage and the client mesh. */
public final class StarRings {

    public static final int CENTER_Y = 100;
    private static final List<Cell> CELLS = new ArrayList<>();
    private static final List<Cell> READ_ONLY_CELLS = Collections.unmodifiableList(CELLS);
    private static final List<List<Cell>> RINGS = new ArrayList<>();
    private static final Int2IntOpenHashMap INDICES = new Int2IntOpenHashMap();
    // Tilts are baked into both structure resources; the client only spins around these plane normals.
    private static final double[] AZIMUTH = { 0, 65, 130 }, TILT = { 18, 12, 54 };
    private static final double[] SPEED = { .11, -.16, .21 };

    private static final Material[] MATERIALS = new Material[256];

    static {
        for (Material material : Material.values()) MATERIALS[material.id] = material;
        INDICES.defaultReturnValue(-1);
        String path = "/assets/gtnhcommunitymod/structure/star_rings.bin.gz";
        InputStream resource = StarRings.class.getResourceAsStream(path);
        if (resource == null) throw new IllegalStateException("Missing star rings");
        try (DataInputStream input = new DataInputStream(new GZIPInputStream(resource))) {
            if (input.readInt() != 0x53545247 || input.readInt() != 1) throw new IOException("Invalid ring format");
            for (int ring = 0; ring < 3; ring++) {
                int count = input.readInt();
                if (count < 1 || count > 16384) throw new IOException("Invalid ring size");
                List<Cell> cells = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    Cell cell = new Cell(
                        input.readShort(),
                        input.readShort(),
                        input.readShort(),
                        material(input.readUnsignedByte()));
                    if (Math.abs(cell.x) > 84 || Math.abs(cell.z) > 84
                        || cell.y < 16
                        || cell.y > 184
                        || cell.material == null) {
                        throw new IOException("Invalid ring block");
                    }
                    if (INDICES.put(key(cell.x, cell.y, cell.z), CELLS.size()) != -1)
                        throw new IOException("Overlapping rings");
                    cells.add(cell);
                    CELLS.add(cell);
                }
                RINGS.add(Collections.unmodifiableList(cells));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read star rings", e);
        }
    }

    public static List<Cell> cells() {
        return READ_ONLY_CELLS;
    }

    public static List<Cell> ring(int index) {
        return RINGS.get(index);
    }

    public static int indexAt(int x, int y, int z) {
        if (x < -84 || x > 84 || y < 16 || y > 184 || z < -84 || z > 84) return -1;
        return INDICES.get(key(x, y, z));
    }

    public static int key(int x, int y, int z) {
        return (x + 128) << 16 | y << 8 | (z + 128);
    }

    /** Normal to each authored ring plane: the spin axis, not a shared vertical axis. */
    public static double[] axis(int ring) {
        double a = Math.toRadians(AZIMUTH[ring]), t = Math.toRadians(TILT[ring]);
        return new double[] { -Math.sin(a) * Math.cos(t), -Math.sin(t), Math.cos(a) * Math.cos(t) };
    }

    public static double rotationDegrees(int ring, double ticks) {
        return (ticks * SPEED[ring]) % 360;
    }

    static Material material(int id) {
        return MATERIALS[id];
    }

    public enum Material {

        ARCANE_STONE(1, "Thaumcraft", "blockCosmeticSolid", 6),
        REINFORCED_OBSIDIAN(2, () -> TstBlocks.MetaBlockCasing02, 5),
        ASTRAL_BAND(3, () -> TstBlocks.MetaBlockCasing02, 9),
        GORE_CASING(5, () -> TstBlocks.MetaBlockCasing02, 0),
        RADIANT_NAQUADAH(18, () -> GregTechAPI.sBlockCasings8, 10),
        ARCANE_TILE_3(20, "chisel", "arcane", 3),
        ARCANE_TILE_8(21, "chisel", "arcane", 8),
        ARCANE_TILE_4(22, "chisel", "arcane", 4),
        GREENSCREEN(24, "ExtraUtilities", "greenscreen", 14),
        TRANSLUCENT(31, "Automagy", "blockTranslucent", 0),
        ICHOR_DRACONIC(43, () -> TstBlocks.MetaBlockCasing02, 1),
        BLOODY_VOID(52, "dreamcraft", "BloodyVoid", 0);

        public final int id;
        final String mod, name;
        public final int meta;
        private final Supplier<Block> source;
        private Block block;

        Material(int id, String mod, String name, int meta) {
            this(id, mod, name, meta, () -> GameRegistry.findBlock(mod, name));
        }

        Material(int id, Supplier<Block> source, int meta) {
            this(id, null, null, meta, source);
        }

        Material(int id, String mod, String name, int meta, Supplier<Block> source) {
            this.id = id;
            this.mod = mod;
            this.name = name;
            this.meta = meta;
            this.source = source;
        }

        public Block block() {
            if (block == null) block = source.get();
            return block;
        }
    }

    public static final class Cell {

        public final int x, y, z;
        public final Material material;

        private Cell(int x, int y, int z, Material material) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.material = material;
        }
    }
}
