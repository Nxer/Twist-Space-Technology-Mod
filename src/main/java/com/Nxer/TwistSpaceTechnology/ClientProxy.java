package com.Nxer.TwistSpaceTechnology;

import com.Nxer.TwistSpaceTechnology.client.render.ArtificialStarRender;
import com.Nxer.TwistSpaceTechnology.client.render.ItemRenderer;
import com.Nxer.TwistSpaceTechnology.client.render.TileEntityRenderer;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockPowerChair;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.system.ItemCooldown.CooldownEventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.MinecraftForge;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.BlockPowerChair;

public class ClientProxy extends CommonProxy {

    public static IModelCustom PowerChairModel = null;

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        new ArtificialStarRender();
        MinecraftForge.EVENT_BUS.register(new CooldownEventHandler());// load cooldown HUD
        TST_BigBroArray.initializeDefaultTextures();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        PowerChairModel = AdvancedModelLoader.loadModel(new ResourceLocation("gtnhcommunitymod:model/PowerChair.obj"));
        ClientProxy.registerItemRenderers();
        ClientProxy.registerTileEntityRenderers();
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
