package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

import com.Nxer.TwistSpaceTechnology.common.block.meta.casing.TstMetaBlockGlowCasing;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarRings;

import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;

/** Snapshots block atlas icons on the render thread. The mesh worker only sees numeric UVs and colours. */
final class StarGuidePalette {

    private static final float[] SHADE = { .60f, 1f, .80f, .80f, .70f, .70f };
    private final StarGuideGeometry.Material[] materials = new StarGuideGeometry.Material[62];

    static StarGuideGeometry.Material[] snapshot() {
        StarGuidePalette palette = new StarGuidePalette();
        for (StarRings.Material material : StarRings.Material.values()) {
            palette.add(material.id, material.block(), material.meta);
        }
        palette.add(0, TstBlocks.MetaBlockCasing01, 9);
        palette.add(12, "bartworks", "BW_TieredGlass", 7);
        palette.add(13, "bartworks", "BW_TieredGlass", 6);
        palette.add(14, "chisel", "arcane", 1);
        palette.add(16, TstBlocks.CondensedStarCore, 0);
        palette.add(19, "chisel", "arcane", 5);
        palette.add(23, "dreamcraft", "BloodyIchorium", 0);
        palette.add(25, "chisel", "arcane", 0);
        palette.add(27, "AWWayofTime", "largeBloodStoneBrick", 0);
        palette.add(28, "AWWayofTime", "ritualStone", 2);
        palette.add(29, TstBlocks.MetaBlockCasing02, 0);
        palette.add(30, "AWWayofTime", "ritualStone", 5);
        palette.frame(32, Materials.PrismaticNaquadah);
        palette.add(33, TstBlocks.MetaBlockGlow01, 0);
        for (int id = 34; id <= 40; id++) palette.add(id, "thaumicbases", "crystalBlock", id == 40 ? 7 : id - 34);
        palette.add(41, "Thaumcraft", "blockCosmeticSolid", 8);
        palette.add(42, "Thaumcraft", "blockCosmeticSolid", 11);
        // The material view shows the authored shell at optional service ports.
        for (int id : new int[] { 44, 45, 46, 47, 49 }) palette.add(id, GregTechAPI.sBlockCasings8, 10);
        palette.add(50, "Thaumcraft", "blockCosmeticOpaque", 2);
        palette.add(51, "dreamcraft", "BloodyThaumium", 0);
        palette.add(53, Loaders.magicCasing, 0);
        palette.add(55, "BloodArsenal", "blood_infused_diamond_block", 0);
        palette.add(56, "thaumicbases", "blockSalisMundus", 0);
        palette.add(57, "thaumicbases", "thauminiteBlock", 0);
        palette.add(58, GregTechAPI.sBlockCasings8, 3);
        palette.add(59, TstBlocks.MetaBlockCasing02, 0);
        palette.frame(60, Materials.AstralSilver);
        palette.add(61, "AWWayofTime", "ritualStone", 4);
        return palette.materials;
    }

    private void frame(int id, Materials material) {
        short[] rgb = material.mRGBa;
        add(id, GregTechAPI.sBlockFrames, material.mMetaItemSubID, (rgb[0] << 16) | (rgb[1] << 8) | rgb[2]);
    }

    private void add(int id, String mod, String name, int meta) {
        add(id, GameRegistry.findBlock(mod, name), meta);
    }

    private void add(int id, Block block, int meta) {
        add(id, block, meta, block == null ? 0xFFFFFF : block.getRenderColor(meta));
    }

    private void add(int id, Block block, int meta, int tint) {
        var atlas = Minecraft.getMinecraft()
            .getTextureMapBlocks();
        IIcon fallback = atlas.getAtlasSprite("missingno");
        if (block == null) block = Blocks.air;
        StarGuideGeometry.Face[] faces = new StarGuideGeometry.Face[6];
        StarGuideGeometry.Face[] overlays = new StarGuideGeometry.Face[6];
        int mask = 0;
        for (int side = 0; side < faces.length; side++) {
            IIcon icon = block.getIcon(side, meta), overlay = null;
            if (block instanceof TstMetaBlockGlowCasing casing) {
                icon = casing.getBaseIcon(side, meta);
                overlay = casing.getGlowIcon(side, meta);
            } else if (id == 45 && side == 3) {
                overlay = atlas.getAtlasSprite("gtnhcommunitymod:iconSets/OVERLAY_FRONT_STAR_FRAME");
            }
            float shade = id == 24 || id == 31 ? 1 : SHADE[side];
            faces[side] = face(icon == null ? fallback : icon, tint, shade);
            if (overlay != null) {
                overlays[side] = face(overlay, 0xFFFFFF, id == 45 ? shade : 1);
                mask |= 1 << side;
            }
        }
        materials[id] = new StarGuideGeometry.Material(
            block.isOpaqueCube(),
            block.getRenderBlockPass() == 1,
            faces,
            overlays,
            mask);
    }

    private static StarGuideGeometry.Face face(IIcon icon, int tint, float shade) {
        int r = (int) ((tint >> 16 & 255) * shade);
        int g = (int) ((tint >> 8 & 255) * shade);
        int b = (int) ((tint & 255) * shade);
        return new StarGuideGeometry.Face(
            icon.getMinU(),
            icon.getMaxU(),
            icon.getMinV(),
            icon.getMaxV(),
            r << 16 | g << 8 | b);
    }
}
