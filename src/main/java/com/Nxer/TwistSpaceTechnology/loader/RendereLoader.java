package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.BlockPowerChair;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers.PowerChairRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers.YamatoRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.MeteorMinerRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.TileEntityRenderer;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;

public class RendereLoader {

    public static IModelCustom PowerChairModel = null;
    public static IModelCustom YamatoModel = null;

    public static void init() {
        PowerChairModel = AdvancedModelLoader.loadModel(new ResourceLocation("gtnhcommunitymod:model/PowerChair.obj"));
        YamatoModel = AdvancedModelLoader.loadModel(new ResourceLocation("gtnhcommunitymod:model/Yamato.obj"));
        RendereLoader.registerItemRenderers();
        RendereLoader.registerTileEntityRenderers();
    }

    public static void registerItemRenderers() {
        MinecraftForgeClient.registerItemRenderer(
            com.Nxer.TwistSpaceTechnology.common.block.BlockPowerChair.ItemBlockPowerChair
                .getItemFromBlock(BlockPowerChair),
            new PowerChairRenderer(PowerChairModel, new ResourceLocation("gtnhcommunitymod", "model/PowerChair.png")));
        MinecraftForgeClient.registerItemRenderer(
            TstItems.Yamato,
            new YamatoRenderer(YamatoModel, new ResourceLocation("gtnhcommunitymod", "model/Yamato.png")));
    }

    public static void registerTileEntityRenderers() {
        new TileEntityRenderer(PowerChairModel, new ResourceLocation("gtnhcommunitymod", "model/PowerChair.png"));
        new MeteorMinerRenderer();
    }
}
