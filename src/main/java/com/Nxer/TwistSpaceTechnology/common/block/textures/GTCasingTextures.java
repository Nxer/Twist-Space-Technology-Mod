package com.Nxer.TwistSpaceTechnology.common.block.textures;

import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic.TST_ID;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;

public class GTCasingTextures {

    public static class TSTIcon implements IIconContainer, Runnable {

        protected IIcon mIcon;
        protected String mIconName;
        protected String mModID;

        public TSTIcon(final String aIconName) {
            this(TST_ID, aIconName);
        }

        public TSTIcon(final String aModID, final String aIconName) {
            this.mIconName = aIconName;
            this.mModID = aModID;
            GregTechAPI.sGTBlockIconload.add(this);
        }

        @Override
        public IIcon getIcon() {
            return this.mIcon;
        }

        @Override
        public IIcon getOverlayIcon() {
            return null;
        }

        @Override
        public void run() {
            this.mIcon = GregTechAPI.sBlockIcons.registerIcon(this.mModID + ":" + this.mIconName);
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }

    // Bloody Hell Casing
    public static final TSTIcon CasingBloodyHell = new TSTIcon("MetaBlockCasing02/0");
}
