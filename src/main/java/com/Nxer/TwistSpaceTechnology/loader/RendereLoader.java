package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.client.render.ItemRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.TileEntityRenderer;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockPowerChair;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockPowerChair;

public class RendereLoader {

    public static IModelCustom PowerChairModel = null;

    public RendereLoader() {
        PowerChairModel = AdvancedModelLoader.loadModel(new ResourceLocation("gtnhcommunitymod:model/PowerChair.obj"));
        RendereLoader.registerItemRenderers();
        RendereLoader.registerTileEntityRenderers();
    }

    public static void registerItemRenderers() {
        MinecraftForgeClient.registerItemRenderer(
            ItemBlockPowerChair.getItemFromBlock(BlockPowerChair),
            new ItemRenderer(PowerChairModel, new ResourceLocation("gtnhcommunitymod", "model/PowerChair.png")));
    }

    public static void registerTileEntityRenderers() {
        new TileEntityRenderer(PowerChairModel, new ResourceLocation("gtnhcommunitymod", "model/PowerChair.png"));
    }
}
