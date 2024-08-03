package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockPowerChair;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers.YamatoRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.TileEntitySpecialRenderer.MachineRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.TileEntitySpecialRenderer.PowerChairRenderer;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;

public class RendereLoader {

    public static IModelCustom PowerChairModel = null;
    public static IModelCustom YamatoModel = null;

    public RendereLoader() {
        PowerChairModel = AdvancedModelLoader.loadModel(new ResourceLocation("gtnhcommunitymod:model/PowerChair.obj"));
        YamatoModel = AdvancedModelLoader.loadModel(new ResourceLocation("gtnhcommunitymod:model/Yamato.obj"));
        RendereLoader.registerItemRenderers();
        RendereLoader.registerTileEntityRenderers();
    }

    public static void registerItemRenderers() {
        MinecraftForgeClient.registerItemRenderer(
            ItemBlockPowerChair.getItemFromBlock(BlockPowerChair),
            new com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers.PowerChairRenderer(
                PowerChairModel,
                new ResourceLocation("gtnhcommunitymod", "model/PowerChair.png")));
        MinecraftForgeClient.registerItemRenderer(
            BasicItems.Yamato,
            new YamatoRenderer(YamatoModel, new ResourceLocation("gtnhcommunitymod", "model/Yamato.png")));
    }

    public static void registerTileEntityRenderers() {
        new MachineRenderer();
        new PowerChairRenderer(PowerChairModel, new ResourceLocation("gtnhcommunitymod", "model/PowerChair.png"));
    }
}
