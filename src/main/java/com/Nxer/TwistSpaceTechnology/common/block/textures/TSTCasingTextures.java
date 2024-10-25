package com.Nxer.TwistSpaceTechnology.common.block.textures;

import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic.TST_ID;

import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.IIconContainer;

public class TSTCasingTextures {

    public static class TSTIcon extends CustomIcon {

        public TSTIcon(String aIconName) {
            super(TST_ID + ":" + aIconName);
        }

    }

    // Bloody Hell
    public static final TSTIcon BH_1_Active = new TSTIcon("iconSets/BloodHellActive_1");
    public static final TSTIcon BH_1 = new TSTIcon("iconSets/BloodHell_1");
    public static final TSTIcon BH_2_Active = new TSTIcon("iconSets/BloodHellActive_2");
    public static final TSTIcon BH_2 = new TSTIcon("iconSets/BloodHell_2");
    public static final TSTIcon BH_3_Active = new TSTIcon("iconSets/BloodHellActive_3");
    public static final TSTIcon BH_3 = new TSTIcon("iconSets/BloodHell_3");
    public static final TSTIcon BH_4_Active = new TSTIcon("iconSets/BloodHellActive_4");
    public static final TSTIcon BH_4 = new TSTIcon("iconSets/BloodHell_4");
    public static final TSTIcon BH_5_Active = new TSTIcon("iconSets/BloodHellActive_5");
    public static final TSTIcon BH_5 = new TSTIcon("iconSets/BloodHell_5");
    public static final TSTIcon BH_6_Active = new TSTIcon("iconSets/BloodHellActive_6");
    public static final TSTIcon BH_6 = new TSTIcon("iconSets/BloodHell_6");
    public static final TSTIcon BH_7_Active = new TSTIcon("iconSets/BloodHellActive_7");
    public static final TSTIcon BH_7 = new TSTIcon("iconSets/BloodHell_7");
    public static final TSTIcon BH_8_Active = new TSTIcon("iconSets/BloodHellActive_8");
    public static final TSTIcon BH_8 = new TSTIcon("iconSets/BloodHell_8");
    public static final TSTIcon BH_9_Active = new TSTIcon("iconSets/BloodHellActive_9");
    public static final TSTIcon BH_9 = new TSTIcon("iconSets/BloodHell_9");

    public static IIconContainer[] BloodyHellIcons = { BH_1, BH_2, BH_3, BH_4, BH_5, BH_6, BH_7, BH_8, BH_9 };
    public static IIconContainer[] BloodyHellIconsActive = { BH_1_Active, BH_2_Active, BH_3_Active, BH_4_Active,
        BH_5_Active, BH_6_Active, BH_7_Active, BH_8_Active, BH_9_Active };
}
