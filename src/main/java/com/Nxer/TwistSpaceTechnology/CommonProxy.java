package com.Nxer.TwistSpaceTechnology;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.combat.DamageEventHandler;
import com.Nxer.TwistSpaceTechnology.combat.PlayerEventHandler;
import com.Nxer.TwistSpaceTechnology.command.CombatRework_Command;
import com.Nxer.TwistSpaceTechnology.command.TST_Command;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.event.StartServerEvent;
import com.Nxer.TwistSpaceTechnology.event.TickingEvent;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_WorldSavedData;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Massfabricator;

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
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        TST_Network.tst.registerMessage(
            TST_BigBroArray.PackRequestMachineType.class,
            TST_BigBroArray.PackRequestMachineType.class,
            2,
            Side.SERVER);

        // some workaround on UU
        RecipeMaps.massFabFakeRecipes.addRecipe(
            false,
            new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
            null,
            null,
            null,
            new FluidStack[] { Materials.UUMatter.getFluid(1) },
            GT_MetaTileEntity_Massfabricator.sDurationMultiplier,
            GT_MetaTileEntity_Massfabricator.BASE_EUT,
            0);
        GT_MetaTileEntity_Massfabricator.uuaRecipe = RecipeMaps.massFabFakeRecipes.addRecipe(
            false,
            new ItemStack[] { GT_Utility.getIntegratedCircuit(23) },
            null,
            null,
            new FluidStack[] { Materials.UUAmplifier.getFluid(GT_MetaTileEntity_Massfabricator.sUUAperUUM) },
            new FluidStack[] { Materials.UUMatter.getFluid(1L) },
            GT_MetaTileEntity_Massfabricator.sDurationMultiplier / GT_MetaTileEntity_Massfabricator.sUUASpeedBonus,
            GT_MetaTileEntity_Massfabricator.BASE_EUT,
            0);
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        TwistSpaceTechnology.LOG.info("Ok, " + Tags.MODNAME + " at version " + Tags.VERSION + " load success .");
        event.registerServerCommand(new TST_Command());
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
