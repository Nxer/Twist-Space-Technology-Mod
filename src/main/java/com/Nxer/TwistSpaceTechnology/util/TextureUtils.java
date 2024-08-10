package com.Nxer.TwistSpaceTechnology.util;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;

public class TextureUtils {

    public static void registerTexture(int page, int index, ITexture texture) {
        if (Textures.BlockIcons.casingTexturePages[page] == null) {
            Textures.BlockIcons.casingTexturePages[page] = new ITexture[128];
        }
        Textures.BlockIcons.setCasingTexture((byte) page, (byte) index, texture);
    }

    public static int getTextureIndex(int page, int index) {
        return 128 * page + index;
    }

}
