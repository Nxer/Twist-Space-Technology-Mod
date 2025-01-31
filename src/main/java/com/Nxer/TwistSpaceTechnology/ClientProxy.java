package com.Nxer.TwistSpaceTechnology;

import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.client.render.ArtificialStarRender;
import com.Nxer.TwistSpaceTechnology.client.render.TileArcaneHoleRender;
import com.Nxer.TwistSpaceTechnology.client.sound.SoundLoader;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.loader.RendereLoader;
import com.Nxer.TwistSpaceTechnology.system.ItemCooldown.CooldownEventHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        new ArtificialStarRender();
        new TileArcaneHoleRender();
        MinecraftForge.EVENT_BUS.register(new CooldownEventHandler());// load cooldown HUD
        TST_BigBroArray.initializeDefaultTextures();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        new RendereLoader();
        new SoundLoader();
    }

}
