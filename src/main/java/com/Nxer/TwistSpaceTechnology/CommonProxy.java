package com.Nxer.TwistSpaceTechnology;

import static com.Nxer.TwistSpaceTechnology.loader.RecipeLoader.loadRecipesServerStarted;

import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.combat.DamageEventHandler;
import com.Nxer.TwistSpaceTechnology.combat.PlayerEventHandler;
import com.Nxer.TwistSpaceTechnology.combat.items.ItemRegister;
import com.Nxer.TwistSpaceTechnology.command.CombatRework_Command;
import com.Nxer.TwistSpaceTechnology.command.TST_AdminCommand;
import com.Nxer.TwistSpaceTechnology.command.TST_Command;
import com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler;
import com.Nxer.TwistSpaceTechnology.common.entity.EntityMountableBlock;
import com.Nxer.TwistSpaceTechnology.common.ic2Crop.CropInfo;
import com.Nxer.TwistSpaceTechnology.common.item.ItemYamato;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_RackComputationMonitor;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialFix;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeResult.ResultInsufficientTier;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.event.ServerEvent;
import com.Nxer.TwistSpaceTechnology.event.StartServerEvent;
import com.Nxer.TwistSpaceTechnology.event.TickingEvent;
import com.Nxer.TwistSpaceTechnology.loader.LazyStaticsInitLoader;
import com.Nxer.TwistSpaceTechnology.loader.MachineLoader;
import com.Nxer.TwistSpaceTechnology.loader.MaterialLoader;
import com.Nxer.TwistSpaceTechnology.loader.RecipeLoader;
import com.Nxer.TwistSpaceTechnology.loader.TCLoader;
import com.Nxer.TwistSpaceTechnology.nei.NEIHandler;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.system.DimensionSystem.DimensionSystemInit;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_WorldSavedData;
import com.Nxer.TwistSpaceTechnology.system.ProcessingArrayBackend.PAHelper;
import com.Nxer.TwistSpaceTechnology.util.LanguageManager;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;

import WayofTime.alchemicalWizardry.ModBlocks;
import bartworks.API.SideReference;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.common.render.GTTextureBuilder;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        if (Config.activateCombatStats) {
            MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        }
        TwistSpaceTechnology.LOG.info(Tags.MODNAME + " at version " + Tags.VERSION);

        MaterialLoader.load();

        if (Config.activateCombatStats) {
            ItemRegister.registry();
        }

        TCLoader.preInit();

        LanguageManager.init();
    }

    public void init(FMLInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new DSP_WorldSavedData());

        if (Config.activateCombatStats) {
            MinecraftForge.EVENT_BUS.register(DamageEventHandler.instance);
        }

        ServerEvent serverEvent = new ServerEvent();
        if (SideReference.Side.Server) {
            MinecraftForge.EVENT_BUS.register(serverEvent);
        }
        FMLCommonHandler.instance()
            .bus()
            .register(serverEvent);

        FMLCommonHandler.instance()
            .bus()
            .register(new TickingEvent());

        CheckRecipeResultRegistry.register(new ResultInsufficientTier(0, 0));

        TstUtils.registerTexture(
            31,
            0,
            new GTTextureBuilder().setFromBlock(ModBlocks.bloodRune, 0)
                .build());

        LazyStaticsInitLoader.initStaticsOnInit();
        MachineLoader.loadMachines();
        GT_Hatch_RackComputationMonitor.run();
        EntityRegistry.registerModEntity(
            EntityMountableBlock.class,
            "TST:EntityMountableBlock",
            1,
            TwistSpaceTechnology.instance,
            256,
            20,
            false);

        ModBlocksHandler.initStatics();
    }

    public void postInit(FMLPostInitializationEvent event) {
        TST_Network.registryNetwork();
        MaterialFix.load();
        TST_BigBroArray.registerUUForArray();
        TST_BigBroArray.getGeneratorsForArray();
        TST_BigBroArray.initializeMaterials();
        TST_BigBroArray.initializeStructure();
        TST_BigBroArray.addRecipes();
        DimensionSystemInit.init();

        if (Config.Enable_ProcessingArray) {
            PAHelper.initStatics();
        }

        if (Config.RewriteEIOTravelStaffConfig) {
            ItemYamato.rewriteEIOTravelStaffConfig();
        }

        MachineLoader.loadMachinePostInit();
        RecipeLoader.loadRecipesPostInit();

        CropInfo.registerAllCropInfo();

        TCLoader.postInit();
        NEIHandler.IMCSender();
    }

    public void complete(FMLLoadCompleteEvent event) {
        RecipeLoader.loadRecipes();

        LazyStaticsInitLoader.initStaticsOnCompleteInit();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        TwistSpaceTechnology.LOG.info("Ok, " + Tags.MODNAME + " at version " + Tags.VERSION + " load success .");
        event.registerServerCommand(new TST_Command());
        event.registerServerCommand(new TST_AdminCommand());
        if (Config.activateCombatStats) {
            event.registerServerCommand(new CombatRework_Command());
        }
    }

    public void serverStarted(FMLServerStartedEvent event) {
        TwistSpaceTechnology.LOG.info("Init DSP Event.");
        StartServerEvent.INSTANCE.onLoading(event);

        loadRecipesServerStarted();
    }
}
