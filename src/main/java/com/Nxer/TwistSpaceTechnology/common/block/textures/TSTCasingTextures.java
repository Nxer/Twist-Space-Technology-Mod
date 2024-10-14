package com.Nxer.TwistSpaceTechnology.common.block.textures;

import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic.TST_ID;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;

public class TSTCasingTextures {

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

    private static final TSTIcon BH_1_Active = new TSTIcon("iconsets/BloodHellActive_1");
    private static final TSTIcon BH_1 = new TSTIcon("iconsets/BloodHell_1");
    private static final TSTIcon BH_2_Active = new TSTIcon("iconsets/BloodHellActive_2");
    private static final TSTIcon BH_2 = new TSTIcon("iconsets/BloodHell_2");
    private static final TSTIcon BH_3_Active = new TSTIcon("iconsets/BloodHellActive_3");
    private static final TSTIcon BH_3 = new TSTIcon("iconsets/BloodHell_3");
    private static final TSTIcon BH_4_Active = new TSTIcon("iconsets/BloodHellActive_4");
    private static final TSTIcon BH_4 = new TSTIcon("iconsets/BloodHell_4");
    private static final TSTIcon BH_5_Active = new TSTIcon("iconsets/BloodHellActive_5");
    private static final TSTIcon BH_5 = new TSTIcon("iconsets/BloodHell_5");
    private static final TSTIcon BH_6_Active = new TSTIcon("iconsets/BloodHellActive_6");
    private static final TSTIcon BH_6 = new TSTIcon("iconsets/BloodHell_6");
    private static final TSTIcon BH_7_Active = new TSTIcon("iconsets/BloodHellActive_7");
    private static final TSTIcon BH_7 = new TSTIcon("iconsets/BloodHell_7");
    private static final TSTIcon BH_8_Active = new TSTIcon("iconsets/BloodHellActive_8");
    private static final TSTIcon BH_8 = new TSTIcon("iconsets/BloodHell_8");
    private static final TSTIcon BH_9_Active = new TSTIcon("iconsets/BloodHellActive_9");
    private static final TSTIcon BH_9 = new TSTIcon("iconsets/BloodHell_9");

    public static IIconContainer[] BloodyHellIcons = { BH_1, BH_2, BH_3, BH_4, BH_5, BH_6, BH_7, BH_8, BH_9 };
    public static IIconContainer[] BloodyHellIconsActive = { BH_1_Active, BH_2_Active, BH_3_Active, BH_4_Active,
        BH_5_Active, BH_6_Active, BH_7_Active, BH_8_Active, BH_9_Active };
}
