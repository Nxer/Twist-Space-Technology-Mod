package com.Nxer.TwistSpaceTechnology.common.block.meta.casing;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.RESOURCE_ROOT_ID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * UnlocalizedName: {@code MetaBlockCasing01} for 0-15, {@code MetaBlockCasing02} for 16+
 */
public class TstMetaBlockCasing extends AbstractTstMetaBlockCasing {

    public final byte texturePageIndex;
    public final byte textureIdOffsite;
    private final Set<Integer> capVariants = new HashSet<>();
    private final Map<Integer, IIcon> capIcons = new HashMap<>();

    public TstMetaBlockCasing(String unlocalizedName, byte textureIdOffsite) {
        this(unlocalizedName, textureIdOffsite, TEXTURE_PAGE_INDEX);
    }

    public TstMetaBlockCasing(String unlocalizedName, byte textureIdOffsite, byte texturePageIndex) {
        super(unlocalizedName);
        if (textureIdOffsite > 112) throw new IllegalArgumentException(
            "Texture ID will overflow. Create a new GT Texture Page and manually solve this problem.");
        this.texturePageIndex = texturePageIndex;
        this.textureIdOffsite = textureIdOffsite;
    }

    /** Optional smooth caps for a normal casing variant; existing six-identical-face variants are unchanged. */
    public ItemStack registerVariantWithTopTexture(int meta) {
        ItemStack variant = registerVariant(meta);
        capVariants.add(meta);
        return variant;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        capIcons.clear();
        for (int meta : capVariants) {
            capIcons.put(meta, register.registerIcon(RESOURCE_ROOT_ID + ":" + unlocalizedName + "/" + meta + "_top"));
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        IIcon cap = side < 2 ? capIcons.get(meta) : null;
        return cap == null ? super.getIcon(side, meta) : cap;
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
