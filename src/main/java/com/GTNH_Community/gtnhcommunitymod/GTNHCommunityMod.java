package com.GTNH_Community.gtnhcommunitymod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.GTNH_Community.gtnhcommunitymod.loader.MachineLoader;
import com.GTNH_Community.gtnhcommunitymod.loader.MaterialLoader;
import com.GTNH_Community.gtnhcommunitymod.loader.RecipeLoader;
import com.GTNH_Community.gtnhcommunitymod.nei.NEIHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = Tags.MODID,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    dependencies = "required-after:IC2; " + "required-after:gregtech; "
        + "required-after:bartworks; "
        + "before:miscutils; ",
    acceptedMinecraftVersions = "[1.7.10]")
public class GTNHCommunityMod {

    public static final Logger LOG = LogManager.getLogger(Tags.MODID);

    @Mod.Instance
    public static GTNHCommunityMod instance;

    @SidedProxy(
        clientSide = "com.GTNH_Community.gtnhcommunitymod.ClientProxy",
        serverSide = "com.GTNH_Community.gtnhcommunitymod.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        MaterialLoader.loadMaterial();// Load MaterialPool

    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        MachineLoader.loadMachines();// Load Machines
        NEIHandler.IMCSender();// NEI reg

    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        RecipeLoader.loadRecipes();// Load Recipes
    }

    @Mod.EventHandler
    public void completeInit(FMLLoadCompleteEvent event) {
        // reflect

    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
