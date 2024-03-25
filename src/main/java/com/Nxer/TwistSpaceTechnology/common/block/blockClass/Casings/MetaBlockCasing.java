package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings;

public class MetaBlockCasing extends MetaBlockCasingBase {

    public final byte texturePageIndex;
    public final byte textureIdOffsite;

    public MetaBlockCasing(String unlocalizedName, byte textureIdOffsite) {
        this(unlocalizedName, textureIdOffsite, TEXTURE_PAGE_INDEX);
    }

    public MetaBlockCasing(String unlocalizedName, byte textureIdOffsite, byte texturePageIndex) {
        super(unlocalizedName);
        if (textureIdOffsite > 112) throw new IllegalArgumentException(
            "Texture ID will overflow. Create a new GT Texture Page and manually solve this problem.");
        this.texturePageIndex = texturePageIndex;
        this.textureIdOffsite = textureIdOffsite;
    }

    /**
     * Used to provide
     * 
     * <pre>
     * {@code casingIndex}
     * </pre>
     * 
     * at Structure defining.
     */
    @Override
    public int getTextureIndex(int aMeta) {
        return super.getTextureIndex(aMeta);
    }

    @Override
    public byte getTexturePageIndex() {
        return texturePageIndex;
    }

    @Override
    public byte getTextureIndexInPage(int meta) {
        return (byte) (textureIdOffsite + meta);
    }

}
