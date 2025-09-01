package com.Nxer.TwistSpaceTechnology;

import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.client.render.ArtificialStarRender;
import com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender;
import com.Nxer.TwistSpaceTechnology.client.render.TileArcaneHoleRender;
import com.Nxer.TwistSpaceTechnology.client.render.LargeSolarBoilerRender;
import com.Nxer.TwistSpaceTechnology.client.sound.SoundLoader;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.loader.RendereLoader;
import com.Nxer.TwistSpaceTechnology.system.ItemCooldown.CooldownEventHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MaterialsTST.initClient();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        new EyeOfWoodRender();
        new ArtificialStarRender();
        new TileArcaneHoleRender();
        new LargeSolarBoilerRender();
        MinecraftForge.EVENT_BUS.register(new CooldownEventHandler());// load cooldown HUD
        TST_BigBroArray.initializeDefaultTextures();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        RendereLoader.init();
        SoundLoader.init();
    }

}
