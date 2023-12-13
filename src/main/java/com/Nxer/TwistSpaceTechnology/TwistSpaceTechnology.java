package com.Nxer.TwistSpaceTechnology;

import java.util.Collection;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Nxer.TwistSpaceTechnology.common.crop.CropLoader;
import com.Nxer.TwistSpaceTechnology.devTools.PathHelper;
import com.Nxer.TwistSpaceTechnology.loader.MachineLoader;
import com.Nxer.TwistSpaceTechnology.loader.MaterialLoader;
import com.Nxer.TwistSpaceTechnology.loader.RecipeLoader;
import com.Nxer.TwistSpaceTechnology.nei.NEIHandler;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.StellarForgeRecipePool;
import com.Nxer.TwistSpaceTechnology.util.TextHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import gregtech.api.util.GT_Recipe;

@Mod(
    modid = Tags.MODID,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    dependencies = "required-before:IC2; " + "required-before:gregtech; "
        + "required-before:bartworks; "
        + "required-before:tectech; "
        + "before:miscutils; "
        + "before:dreamcraft;",
    acceptedMinecraftVersions = "[1.7.10]")
public class TwistSpaceTechnology {

    /**
     * <li>The signal of whether in Development Mode.
     * <li>Keep care to set 'false' when dev complete.
     */
    public static final boolean isInDevMode = false;

    /**
     * The absolute Path of your workspace/resources folder.
     * It will be replaced by {@link PathHelper#initResourceAbsolutePath}.
     * If it not work correctly, please operate it manually and disable
     * the{@link PathHelper#initResourceAbsolutePath}.
     */
    public static String DevResource = "";
    /**
     * <p>
     * Set false when auto generation get problems and set DevResource manually.
     * <p>
     * Mind to reset these changes when dev complete.
     */
    public static final boolean useAutoGeneratingDevResourcePath = true;

    public static final String MODID = Tags.MODID;
    public static final String MOD_ID = Tags.MODID;
    public static final String MOD_NAME = Tags.MODNAME;
    public static final String VERSION = Tags.VERSION;

    /**
     * If you need send a message to the Log, call this.
     */
    public static final Logger LOG = LogManager.getLogger(Tags.MODID);

    @Mod.Instance
    public static TwistSpaceTechnology instance;

    @SidedProxy(
        clientSide = "com.Nxer.TwistSpaceTechnology.ClientProxy",
        serverSide = "com.Nxer.TwistSpaceTechnology.CommonProxy")
    public static CommonProxy proxy;

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // process path
        if (isInDevMode && useAutoGeneratingDevResourcePath) {
            DevResource = PathHelper.initResourceAbsolutePath();
        }
        TextHandler.initLangMap(isInDevMode);

        proxy.preInit(event);
        MaterialLoader.load();// Load MaterialPool
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
        RecipeLoader.loadRecipesPostInit();// To init GTCM Recipemap

        TextHandler.serializeLangMap(isInDevMode);

        CropLoader.register();
        CropLoader.registerBaseSeed();
        // TwistSpaceTechnology.LOG.info("test GT.getResourcePath : " + GregTech.getResourcePath("testing"));
    }

    public static Collection<GT_Recipe> temp = new HashSet<>();

    @Mod.EventHandler
    public void completeInit(FMLLoadCompleteEvent event) {
        TwistSpaceTechnology.LOG.info("Start Complete Init.");
        RecipeLoader.loadRecipes();// Load Recipes
        // reflect

        //

    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
        TwistSpaceTechnology.LOG.info("serverStarting");
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
        new StellarForgeRecipePool().loadRecipes();

    }

}
