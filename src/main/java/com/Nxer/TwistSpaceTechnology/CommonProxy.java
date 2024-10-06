package com.Nxer.TwistSpaceTechnology;

import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.combat.DamageEventHandler;
import com.Nxer.TwistSpaceTechnology.combat.PlayerEventHandler;
import com.Nxer.TwistSpaceTechnology.command.CombatRework_Command;
import com.Nxer.TwistSpaceTechnology.command.TST_AdminCommand;
import com.Nxer.TwistSpaceTechnology.command.TST_Command;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeResult.ResultInsufficientTier;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.event.StartServerEvent;
import com.Nxer.TwistSpaceTechnology.event.TickingEvent;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_WorldSavedData;
import com.Nxer.TwistSpaceTechnology.util.TextureUtils;

import WayofTime.alchemicalWizardry.ModBlocks;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.common.render.GTTextureBuilder;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        if (Config.activateCombatStats) {
            MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        }
        TwistSpaceTechnology.LOG.info(Tags.MODNAME + " at version " + Tags.VERSION);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new DSP_WorldSavedData());
        if (Config.activateCombatStats) {
            MinecraftForge.EVENT_BUS.register(DamageEventHandler.instance);
        }
        FMLCommonHandler.instance()
            .bus()
            .register(new TickingEvent());

        CheckRecipeResultRegistry.register(new ResultInsufficientTier(0, 0));

        TextureUtils.registerTexture(
            31,
            0,
            new GTTextureBuilder().setFromBlock(ModBlocks.bloodRune, 0)
                .build());
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        TST_Network.tst.registerMessage(
            TST_BigBroArray.PackRequestMachineType.class,
            TST_BigBroArray.PackRequestMachineType.class,
            0,
            Side.SERVER);
        TST_Network.tst.registerMessage(
            TST_BigBroArray.PackSyncMachineType.class,
            TST_BigBroArray.PackSyncMachineType.class,
            1,
            Side.CLIENT);
        TST_BigBroArray.registerUUForArray();
        TST_BigBroArray.getGeneratorsForArray();
        TST_BigBroArray.initializeMaterials();
        TST_BigBroArray.initializeStructure();
        TST_BigBroArray.addRecipes();

        GTCMRecipe.prepareBloodyHellRecipes();
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        TwistSpaceTechnology.LOG.info("Ok, " + Tags.MODNAME + " at version " + Tags.VERSION + " load success .");
        event.registerServerCommand(new TST_Command());
        event.registerServerCommand(new TST_AdminCommand());
        if (Config.activateCombatStats) {
            event.registerServerCommand(new CombatRework_Command());
        }
        // test
        // *Unfinished */ event.registerServerCommand(new alftest_Command());
    }

    public void serverStarted(FMLServerStartedEvent event) {
        TwistSpaceTechnology.LOG.info("Init DSP Event.");
        StartServerEvent.INSTANCE.onLoading(event);
    }

}
