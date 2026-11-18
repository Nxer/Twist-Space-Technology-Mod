package com.Nxer.TwistSpaceTechnology.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMath;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMeteorExcavation;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMeteorField;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMeteorFormation;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMeteorGrowth;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMiningCycle;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStarContainment;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.blocks.GTBlockOre;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

/** Cached unit-block faces of a virtual Blood Magic meteor, including GT's tinted ore overlays. */
final class StarMeteorMesh {

    private static final float[] SHADE = { 0.5f, 1, 0.8f, 0.8f, 0.6f, 0.6f };
    private static final float[][] NORMALS = { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 }, { -1, 0, 0 },
        { 1, 0, 0 } };
    private static final int BUILD_CHUNK = 128;
    private static final int TEXTURE_LAYERS = 3;
    private static final int GROWTH_STEPS = StarMeteorFormation.STEPS_PER_BLOCK;
    private final int radius;
    private final int miningTier;
    private final int style;
    private final long seed;
    private final int[] materials, metadata;
    private final double[] weights;
    private final boolean[] fillers;
    private final StarMeteorField field;
    private final StarMeteorExcavation excavation;
    private final int[] cells;
    private final Appearance[] palette;
    private final int[] batchX = new int[BUILD_CHUNK], batchY = new int[BUILD_CHUNK], batchZ = new int[BUILD_CHUNK],
        batchFaces = new int[BUILD_CHUNK], batchMaterials = new int[BUILD_CHUNK];
    private final RenderBlocks blocks = new RenderBlocks();
    private final Appearance core;
    private final Part exterior;
    private Part cap, pendingCap;
    private GrowthPart growth, pendingGrowth;
    private StarMeteorGrowth growthModel;
    private long visibleRemoved, requestedRemoved;
    private int growthStage;
    private boolean showGrowth;
    private boolean showCore;
    private double coreScale;

    StarMeteorMesh(TileStarContainment star) {
        radius = star.radius;
        miningTier = star.miningTier;
        style = star.style;
        seed = star.seed;
        materials = star.materials.clone();
        metadata = star.metadata.clone();
        weights = star.weights.clone();
        fillers = star.fillers.clone();
        field = new StarMeteorField(radius, seed, weights, style == 1 ? fillers : null);
        excavation = new StarMeteorExcavation(radius);
        cells = field.surfaceCells();
        exterior = new Part(-1);
        palette = new Appearance[materials.length];
        Item compressedStone = GameRegistry.findItem(Mods.ExtraUtilities.ID, "cobblestone_compressed");
        core = new Appearance(
            Item.getIdFromItem(compressedStone == null ? Item.getItemFromBlock(Blocks.cobblestone) : compressedStone),
            compressedStone == null ? 0 : 7);
        blocks.setRenderBounds(0, 0, 0, 1, 1, 1);
        blocks.enableAO = false;
    }

    boolean matches(TileStarContainment star) {
        return radius == star.radius && miningTier == star.miningTier
            && seed == star.seed
            && style == star.style
            && Arrays.equals(materials, star.materials)
            && Arrays.equals(metadata, star.metadata)
            && Arrays.equals(weights, star.weights)
            && Arrays.equals(fillers, star.fillers);
    }

    /** Keep only the current and next growth band/cut. Preparation shares the renderer's bounded frame budget. */
    long build(long budgetNanos, double progress, int totalDuration, int formationTicks, int beamWarmupTicks,
        double miningProgress, boolean animate) {
        int miningStart = formationTicks + beamWarmupTicks;
        int miningDuration = Math.max(1, totalDuration - miningStart);
        long volume = StarMath.volume(radius);
        long desired = animate ? StarMiningCycle
            .removed(excavation, miningTier, StarMiningCycle.completed(miningProgress, miningDuration), miningDuration)
            : 0;
        // A corrected/restarted snapshot can move backwards, even before a pending cut becomes visible.
        // Drop only excavation state; retain the expensive, unchanged exterior mesh.
        // A display-only toggle keeps the same ore layout and exterior cache, including when changed mid-batch.
        if (!animate || desired < requestedRemoved) disposeCuts();
        requestedRemoved = desired;
        showCore = animate && formationTicks > 0 && progress < StarMeteorFormation.NUCLEATION_TICKS;
        coreScale = StarMeteorFormation.coreSize(progress);
        growthStage = StarMeteorFormation.stageAt(radius, progress, formationTicks);
        if ((growth != null && growth.band.first > growthStage)
            || (growth == null && pendingGrowth != null && pendingGrowth.band.first > growthStage)) disposeGrowth();
        boolean forming = formationTicks > 0 && progress < formationTicks;
        showGrowth = animate && desired == 0 && (forming || growth != null || pendingGrowth != null);
        if (!showGrowth) disposeGrowth();
        if (desired == volume) {
            visibleRemoved = desired;
            return 0;
        }
        if (budgetNanos <= 0) return 0;
        long start = System.nanoTime();
        // Never reveal a temporary random palette while the final clustered layout is still being planned.
        if (!field.prepare(budgetNanos)) return System.nanoTime() - start;
        if (showGrowth) {
            if (growthModel == null) growthModel = new StarMeteorGrowth(field, seed);
            promoteGrowth();
            int target = Math.min(radius - 1, growthStage / GROWTH_STEPS);
            if (pendingGrowth != null && pendingGrowth.band.index + 1 < target) {
                pendingGrowth.dispose();
                pendingGrowth = null;
            }
            if (pendingGrowth == null) {
                int next = growth == null || growth.band.index < target ? target : growth.band.index + 1;
                if (next < radius) pendingGrowth = new GrowthPart(growthModel.band(next));
            }
            if (pendingGrowth != null) {
                long growthBudget = exterior.ready() ? budgetNanos : budgetNanos * 3 / 4;
                pendingGrowth.build(Math.min(growthBudget, budgetNanos - (System.nanoTime() - start)));
            }
            promoteGrowth();
            exterior.build(budgetNanos - (System.nanoTime() - start));
            // The last substep already has precisely the final block faces. This is a cache swap, not a shape jump.
            if (growthStage == growthModel.stages && growth != null
                && growth.band.last == growthModel.stages
                && exterior.ready()) {
                disposeGrowth();
                showGrowth = false;
            }
            return System.nanoTime() - start;
        }
        // Skip obsolete checkpoints instead of rebuilding every intervening cut after a slow frame.
        if (pendingCap != null && pendingCap.cut.removed < desired) {
            pendingCap.dispose();
            pendingCap = null;
        }
        if (animate && pendingCap == null) {
            long next = desired > visibleRemoved ? desired
                : StarMiningCycle.removed(
                    excavation,
                    miningTier,
                    StarMiningCycle.end(miningProgress, miningDuration),
                    miningDuration);
            if (next > visibleRemoved && next < volume) pendingCap = new Part(excavation.at(next));
        }
        boolean preparingCut = pendingCap != null && !pendingCap.ready();
        long exteriorBudget = preparingCut ? budgetNanos * 2 / 3 : budgetNanos;
        exterior.build(Math.min(exteriorBudget, budgetNanos - (System.nanoTime() - start)));
        if (pendingCap != null) pendingCap.build(budgetNanos - (System.nanoTime() - start));
        if (exterior.ready()) {
            // Promote in this frame, including a cut that has just finished using its reserved budget.
            if (pendingCap != null && pendingCap.ready() && pendingCap.cut.removed <= desired) {
                if (cap != null) cap.dispose();
                cap = pendingCap;
                visibleRemoved = cap.cut.removed;
                pendingCap = null;
            }
        }
        return System.nanoTime() - start;
    }

    void draw() {
        if (visibleRemoved >= StarMath.volume(radius)) return;
        if (showCore) {
            drawCore();
            return;
        }
        if (showGrowth ? growth == null : !exterior.ready()) return;
        GL11.glPushAttrib(
            GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_POLYGON_BIT);
        try {
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glDisable(GL11.GL_CLIP_PLANE0);
            // Finish the entire rock surface before drawing its coplanar ore/highlight decals.
            // Both decals test against rock depth without writing it, so their order stays stable during rotation.
            GL11.glPolygonOffset(-1, -1);
            for (int textureLayer = 0; textureLayer < TEXTURE_LAYERS; textureLayer++) {
                GL11.glDepthMask(textureLayer == 0);
                if (textureLayer == 0) GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                else GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                if (showGrowth) growth.draw(textureLayer);
                else {
                    // Omit mined block layers entirely: their bottom faces can lie exactly on the cut plane.
                    exterior.draw(textureLayer);
                    if (cap != null) cap.draw(textureLayer);
                }
            }
        } finally {
            GL11.glPopAttrib();
        }
    }

    private void drawCore() {
        if (coreScale <= 0) return;
        GL11.glPushMatrix();
        try {
            GL11.glScaled(coreScale, coreScale, coreScale);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setBrightness(0xF000F0);
            for (int side = 0; side < 6; side++) {
                drawFace(core.block, core.base[side], core.baseColor, side, 0, 0, 0, false);
            }
            tessellator.draw();
        } finally {
            GL11.glPopMatrix();
        }
    }

    void dispose() {
        exterior.dispose();
        disposeCuts();
        disposeGrowth();
    }

    private void disposeCuts() {
        if (cap != null) cap.dispose();
        if (pendingCap != null) pendingCap.dispose();
        cap = pendingCap = null;
        visibleRemoved = requestedRemoved = 0;
    }

    private void promoteGrowth() {
        if (pendingGrowth == null || !pendingGrowth.ready() || pendingGrowth.band.first > growthStage) return;
        if (growth != null) growth.dispose();
        growth = pendingGrowth;
        pendingGrowth = null;
    }

    private void disposeGrowth() {
        if (growth != null) growth.dispose();
        if (pendingGrowth != null) pendingGrowth.dispose();
        growth = pendingGrowth = null;
        growthModel = null;
    }

    private final class Part {

        final int layer;
        final StarMeteorExcavation.Cut cut;
        private final List<Integer> lists = new ArrayList<>(), listLayers = new ArrayList<>();
        private int cursor, drawList, drawFromLayer = -2;

        Part(int layer) {
            this.layer = layer;
            cut = null;
        }

        Part(StarMeteorExcavation.Cut cut) {
            this.cut = cut;
            layer = cut.layer;
        }

        int cellCount() {
            return cut == null ? cells.length : 2 * field.diameter * field.diameter;
        }

        boolean ready() {
            return cursor == cellCount();
        }

        void build(long budgetNanos) {
            if (budgetNanos <= 0) return;
            if (ready()) {
                cacheDrawList();
                return;
            }
            long start = System.nanoTime();
            int count = cellCount();
            do {
                int list = GL11.glGenLists(TEXTURE_LAYERS);
                if (list == 0) return;
                int sourceLayer = layer < 0 ? radius - field.y(cells[cursor]) : -1;
                int batchSize = collectCells(count, sourceLayer, start, budgetNanos);
                for (int textureLayer = 0; textureLayer < TEXTURE_LAYERS; textureLayer++) {
                    compileLayer(list + textureLayer, textureLayer, batchSize);
                }
                lists.add(list);
                listLayers.add(sourceLayer);
            } while (!ready() && System.nanoTime() - start < budgetNanos);
            if (ready()) cacheDrawList();
        }

        private int collectCells(int count, int sourceLayer, long start, long budgetNanos) {
            int batchSize = 0;
            while (cursor < count && batchSize < BUILD_CHUNK) {
                if ((cursor & 127) == 0 && System.nanoTime() - start >= budgetNanos) break;
                int cell = layer < 0 ? cells[cursor] : cursor;
                // A cached chunk must never straddle two mining layers.
                if (sourceLayer >= 0 && radius - field.y(cell) != sourceLayer) break;
                cursor++;
                int plane = field.diameter * field.diameter;
                int x = layer < 0 ? field.x(cell) : cell % plane / field.diameter - radius;
                int y = layer < 0 ? field.y(cell) : cut.y - cell / plane;
                int z = layer < 0 ? field.z(cell) : cell % field.diameter - radius;
                if (cut != null && (!field.contains(x, y, z) || !cut.remains(x, y, z))) continue;
                int faces = cut == null ? field.exposedFaces(x, y, z) : cut.replacementFaces(field, x, y, z);
                if (faces == 0) continue;
                int material = field.materialAt(x, y, z);
                if (palette[material] == null)
                    palette[material] = new Appearance(materials[material], metadata[material]);
                batchX[batchSize] = x;
                batchY[batchSize] = y;
                batchZ[batchSize] = z;
                batchFaces[batchSize] = faces;
                batchMaterials[batchSize++] = material;
            }
            return batchSize;
        }

        private int firstVisibleLayer() {
            return layer < 0 ? (cap == null ? 0 : cap.layer + 1) : -1;
        }

        private void cacheDrawList() {
            int firstLayer = firstVisibleLayer();
            if (drawList != 0 && drawFromLayer == firstLayer) return;
            if (drawList == 0) drawList = GL11.glGenLists(TEXTURE_LAYERS);
            if (drawList == 0) return;
            for (int textureLayer = 0; textureLayer < TEXTURE_LAYERS; textureLayer++) {
                GL11.glNewList(drawList + textureLayer, GL11.GL_COMPILE);
                try {
                    for (int i = 0; i < lists.size(); i++) {
                        if (listLayers.get(i) >= firstLayer) GL11.glCallList(lists.get(i) + textureLayer);
                    }
                } finally {
                    GL11.glEndList();
                }
            }
            drawFromLayer = firstLayer;
        }

        void draw(int textureLayer) {
            int firstLayer = firstVisibleLayer();
            if (drawList != 0 && drawFromLayer == firstLayer) GL11.glCallList(drawList + textureLayer);
            else for (int i = 0; i < lists.size(); i++) {
                // A cap may become ready after the body's build budget was used. Select the correct layers now;
                // rebuild only this small list of cached calls when the next frame has budget, never the vertices.
                if (listLayers.get(i) >= firstLayer) GL11.glCallList(lists.get(i) + textureLayer);
            }
        }

        void dispose() {
            if (drawList != 0) GL11.glDeleteLists(drawList, TEXTURE_LAYERS);
            for (int list : lists) GL11.glDeleteLists(list, TEXTURE_LAYERS);
            lists.clear();
            listLayers.clear();
            drawList = 0;
            drawFromLayer = -2;
        }
    }

    /** One frontier scan prepares all four substeps; faces are shared by their visible time interval. */
    private final class GrowthPart {

        private static final int STRIDE = GROWTH_STEPS + 2;
        final StarMeteorGrowth.Band band;
        private final GrowthBucket[] buckets = new GrowthBucket[(GROWTH_STEPS + 1) * STRIDE];
        private final int[] facesByInterval = new int[buckets.length], drawLists = new int[GROWTH_STEPS + 1];
        private int fullBuffers, flushCursor, preparedPhases;
        private boolean scanned;

        GrowthPart(StarMeteorGrowth.Band band) {
            this.band = band;
        }

        boolean ready() {
            return preparedPhases == drawLists.length;
        }

        void build(long budgetNanos) {
            if (budgetNanos <= 0 || ready()) return;
            long start = System.nanoTime();
            do {
                if (fullBuffers != 0) {
                    int bucket = Integer.numberOfTrailingZeros(fullBuffers);
                    if (!buckets[bucket].flush()) return;
                    fullBuffers &= ~(1 << bucket);
                } else if (!scanned) {
                    int cell = band.next();
                    if (cell < 0) scanned = true;
                    else collect(cell);
                } else if (flushCursor < buckets.length) {
                    if (buckets[flushCursor] != null && !buckets[flushCursor].flush()) return;
                    flushCursor++;
                } else if (!cachePhase()) return;
            } while (!ready() && System.nanoTime() - start < budgetNanos);
        }

        private void collect(int cell) {
            int x = field.x(cell), y = field.y(cell), z = field.z(cell);
            int first = Math.max(band.first, growthModel.birth(x, y, z));
            if (first > band.last) return;
            Arrays.fill(facesByInterval, 0);
            boolean visible = false;
            for (int side = 0; side < NORMALS.length; side++) {
                float[] d = NORMALS[side];
                int end = Math.min(band.last + 1, growthModel.birth(x + (int) d[0], y + (int) d[1], z + (int) d[2]));
                if (end <= first) continue;
                facesByInterval[(first - band.first) * STRIDE + end - band.first] |= 1 << side;
                visible = true;
            }
            if (!visible) return;
            int material = field.materialAt(x, y, z);
            if (palette[material] == null) palette[material] = new Appearance(materials[material], metadata[material]);
            for (int i = 0; i < buckets.length; i++) if (facesByInterval[i] != 0) {
                if (buckets[i] == null) buckets[i] = new GrowthBucket();
                GrowthBucket bucket = buckets[i];
                int at = bucket.count++;
                bucket.x[at] = x;
                bucket.y[at] = y;
                bucket.z[at] = z;
                bucket.faces[at] = facesByInterval[i];
                bucket.materials[at] = material;
                if (bucket.count == BUILD_CHUNK) fullBuffers |= 1 << i;
            }
        }

        private boolean cachePhase() {
            int list = GL11.glGenLists(TEXTURE_LAYERS);
            if (list == 0) return false;
            for (int layer = 0; layer < TEXTURE_LAYERS; layer++) {
                GL11.glNewList(list + layer, GL11.GL_COMPILE);
                try {
                    for (int i = 0; i < buckets.length; i++) {
                        if (buckets[i] == null || preparedPhases < i / STRIDE || preparedPhases >= i % STRIDE) continue;
                        for (int part : buckets[i].lists) GL11.glCallList(part + layer);
                    }
                } finally {
                    GL11.glEndList();
                }
            }
            drawLists[preparedPhases++] = list;
            return true;
        }

        void draw(int layer) {
            int phase = Math.max(0, Math.min(GROWTH_STEPS, growthStage - band.first));
            GL11.glCallList(drawLists[phase] + layer);
        }

        void dispose() {
            for (int list : drawLists) if (list != 0) GL11.glDeleteLists(list, TEXTURE_LAYERS);
            for (GrowthBucket bucket : buckets)
                if (bucket != null) for (int list : bucket.lists) GL11.glDeleteLists(list, TEXTURE_LAYERS);
        }

        private final class GrowthBucket {

            final int[] x = new int[BUILD_CHUNK], y = new int[BUILD_CHUNK], z = new int[BUILD_CHUNK],
                faces = new int[BUILD_CHUNK], materials = new int[BUILD_CHUNK];
            final List<Integer> lists = new ArrayList<>();
            int count;

            boolean flush() {
                if (count == 0) return true;
                int list = GL11.glGenLists(TEXTURE_LAYERS);
                if (list == 0) return false;
                System.arraycopy(x, 0, batchX, 0, count);
                System.arraycopy(y, 0, batchY, 0, count);
                System.arraycopy(z, 0, batchZ, 0, count);
                System.arraycopy(faces, 0, batchFaces, 0, count);
                System.arraycopy(materials, 0, batchMaterials, 0, count);
                for (int layer = 0; layer < TEXTURE_LAYERS; layer++) compileLayer(list + layer, layer, count);
                lists.add(list);
                count = 0;
                return true;
            }
        }
    }

    private void compileLayer(int list, int textureLayer, int batchSize) {
        GL11.glNewList(list, GL11.GL_COMPILE);
        try {
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setBrightness(0xF000F0);
            for (int i = 0; i < batchSize; i++) {
                Appearance appearance = palette[batchMaterials[i]];
                int color = textureLayer == 0 ? appearance.baseColor
                    : textureLayer == 1 ? appearance.oreColor : 0xFFFFFF;
                for (int side = 0; side < 6; side++) {
                    if ((batchFaces[i] & (1 << side)) == 0) continue;
                    IIcon icon = textureLayer == 0 ? appearance.base[side]
                        : textureLayer == 1 ? appearance.ore : appearance.overlay;
                    drawFace(
                        appearance.block,
                        icon,
                        color,
                        side,
                        batchX[i],
                        batchY[i],
                        batchZ[i],
                        textureLayer != 0 && appearance.glow);
                }
            }
            t.draw();
        } finally {
            GL11.glEndList();
        }
    }

    private void drawFace(Block block, IIcon icon, int color, int side, int x, int y, int z, boolean glow) {
        if (icon == null) return;
        float shade = glow ? 1 : SHADE[side];
        Tessellator t = Tessellator.instance;
        t.setNormal(NORMALS[side][0], NORMALS[side][1], NORMALS[side][2]);
        t.setColorOpaque_F(
            (color >> 16 & 255) / 255f * shade,
            (color >> 8 & 255) / 255f * shade,
            (color & 255) / 255f * shade);
        double xx = x - 0.5, yy = y - 0.5, zz = z - 0.5;
        switch (side) {
            case 0 -> blocks.renderFaceYNeg(block, xx, yy, zz, icon);
            case 1 -> blocks.renderFaceYPos(block, xx, yy, zz, icon);
            case 2 -> blocks.renderFaceZNeg(block, xx, yy, zz, icon);
            case 3 -> blocks.renderFaceZPos(block, xx, yy, zz, icon);
            case 4 -> blocks.renderFaceXNeg(block, xx, yy, zz, icon);
            case 5 -> blocks.renderFaceXPos(block, xx, yy, zz, icon);
        }
    }

    private static final class Appearance {

        Block block = Blocks.stone;
        final IIcon[] base = new IIcon[6];
        IIcon ore, overlay;
        int baseColor = 0xFFFFFF, oreColor = 0xFFFFFF;
        boolean glow;

        Appearance(int itemId, int meta) {
            Item item = Item.getItemById(itemId);
            IIcon missing = Minecraft.getMinecraft()
                .getTextureMapBlocks()
                .getAtlasSprite("missingno");
            if (!(item instanceof ItemBlock itemBlock)) {
                Arrays.fill(base, missing);
                return;
            }
            block = itemBlock.field_150939_a;
            baseColor = block.getRenderColor(meta);
            for (int side = 0; side < 6; side++) {
                IIcon icon = block.getIcon(side, meta);
                base[side] = icon == null ? missing : icon;
            }
            // Vanilla ores already have a complete face texture. GT/BW/GT++ ores need their native
            // rock background, material overlay and colour, not ItemStack.getIconIndex().
            if (!(block instanceof IBlockWithTextures)) return;
            try (OreInfo<?> info = OreManager.getOreInfo(new ItemStack(item, 1, meta))) {
                if (info == null || info.material == null || info.stoneType == null) return;
                for (int side = 0; side < 6; side++) base[side] = info.stoneType.getIcon(side);
                baseColor = 0xFFFFFF;
                IIconContainer texture = block instanceof GTBlockOre ? info.material.getTextureSet()
                    .getOreTexture(info.stoneType instanceof StoneType stone ? stone : StoneType.Stone, info.isSmall)
                    : info.material.getTextureSet().mTextures[(info.isSmall ? OrePrefixes.oreSmall : OrePrefixes.ore)
                        .getTextureIndex()];
                ore = texture.getIcon();
                overlay = texture.getOverlayIcon();
                short[] rgba = info.material.getRGBA();
                oreColor = (rgba[0] & 255) << 16 | (rgba[1] & 255) << 8 | (rgba[2] & 255);
                glow = info.material.getGTMaterial() != null && info.material.getGTMaterial()
                    .hasGlowingOre();
            }
        }
    }
}
