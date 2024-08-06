package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockPowerChair;

import com.Nxer.TwistSpaceTechnology.client.ModelEnum;
import com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers.ItemPowerChairRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.Nxer.TwistSpaceTechnology.client.render.ItemRenderers.YamatoRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.TileEntitySpecialRenderer.MachineRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.TileEntitySpecialRenderer.PowerChairRenderer;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems;

public class RendererLoader {


    public RendererLoader() {
        ModelEnum.PowerChair.set(new ResourceLocation("gtnhcommunitymod:model/PowerChair.obj"));
        ModelEnum.Yamato.set(new ResourceLocation("gtnhcommunitymod:model/Yamato.obj"));
        ModelEnum.STAR.set(new ResourceLocation("gtnhcommunitymod:model/Sol.obj"));
    }

    public void registerItemRenderers() {
        MinecraftForgeClient.registerItemRenderer(
            ItemBlockPowerChair.getItemFromBlock(BlockPowerChair),
            new ItemPowerChairRenderer(
                    ModelEnum.PowerChair.get(),
                new ResourceLocation("gtnhcommunitymod", "model/PowerChair.png")));
        MinecraftForgeClient.registerItemRenderer(
            BasicItems.Yamato,
            new YamatoRenderer(ModelEnum.Yamato.get(), new ResourceLocation("gtnhcommunitymod", "model/Yamato.png")));
    }

    public void registerTileEntityRenderers() {
        new MachineRenderer();
        new PowerChairRenderer(ModelEnum.PowerChair.get(), new ResourceLocation("gtnhcommunitymod", "model/PowerChair.png"));
    }
}
