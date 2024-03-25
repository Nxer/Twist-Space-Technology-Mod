package com.Nxer.TwistSpaceTechnology.common.block.blockClass;

import gregtech.api.interfaces.IHasIndexedTexture;
import gregtech.api.util.GT_Utility;

public interface ICasing extends IHasIndexedTexture {

    byte getTexturePageIndex();

    byte getTextureIndexInPage(int meta);

    default int getTextureId(byte page, byte index) {
        return GT_Utility.getTextureId(page, index);
    }
}
