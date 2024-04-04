package com.Nxer.TwistSpaceTechnology;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemBlock;
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
import com.supsolpans.MainSSP;

import advsolar.common.AdvancedSolarPanel;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import emt.EMT;
import emt.block.BlockSolars;
import emt.tile.solar.Solars;
import goodgenerator.loader.Loaders;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Massfabricator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

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
        registerUUForArray();
        getGeneratorsForArray();
    }

    private void registerUUForArray() {
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
        RecipeMaps.massFabFakeRecipes.addRecipe(
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

    private void getGeneratorsForArray() {
        TST_BigBroArray.GENERATORS.put(
            "Diesel",
            new ItemStack[] { ItemList.Generator_Diesel_LV.get(1), ItemList.Generator_Diesel_MV.get(1),
                ItemList.Generator_Diesel_HV.get(1), Loaders.Generator_Diesel[0], Loaders.Generator_Diesel[1] });
        TST_BigBroArray.GENERATORS.put(
            "Steam_Turbine",
            new ItemStack[] { ItemList.Generator_Steam_Turbine_LV.get(1), ItemList.Generator_Steam_Turbine_MV.get(1),
                ItemList.Generator_Steam_Turbine_HV.get(1), });
        TST_BigBroArray.GENERATORS.put(
            "Gas_Turbine",
            new ItemStack[] { ItemList.Generator_Gas_Turbine_LV.get(1), ItemList.Generator_Gas_Turbine_MV.get(1),
                ItemList.Generator_Gas_Turbine_HV.get(1), ItemList.Generator_Gas_Turbine_EV.get(1),
                ItemList.Generator_Gas_Turbine_IV.get(1), });

        TST_BigBroArray.GENERATORS.put(
            "SemiFluid",
            new ItemStack[] { GregtechItemList.Generator_SemiFluid_LV.get(1),
                GregtechItemList.Generator_SemiFluid_MV.get(1), GregtechItemList.Generator_SemiFluid_HV.get(1),
                GregtechItemList.Generator_SemiFluid_EV.get(1), GregtechItemList.Generator_SemiFluid_IV.get(1) });

        TST_BigBroArray.GENERATORS.put(
            "Naquadah",
            new ItemStack[] { ItemList.Generator_Naquadah_Mark_I.get(1), ItemList.Generator_Naquadah_Mark_II.get(1),
                ItemList.Generator_Naquadah_Mark_III.get(1), ItemList.Generator_Naquadah_Mark_IV.get(1),
                ItemList.Generator_Naquadah_Mark_V.get(1), });

        TST_BigBroArray.GENERATORS.put(
            "ASP_Solar",
            new ItemStack[] { new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1), // LV
                new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1, 1), // MV
                new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1, 2), // HV
                new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1, 3), // EV
                new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1, 4), // IV
                new ItemStack(MainSSP.BlockSingularSP, 1), // LuV
                new ItemStack(MainSSP.BlockAdminSP, 1), // ZPM
                new ItemStack(MainSSP.BlockPhotonSP, 1) // UV
            });

        List<ItemStack> EMTSolars = new ArrayList<>();
        for (int i = 0; i < Solars.getCountOfInstances(); i++) {
            BlockSolars emtSolars;
            if (i == 0) {
                emtSolars = (BlockSolars) GameRegistry.findBlock(EMT.MOD_ID, "EMTSolars");

            } else {
                emtSolars = (BlockSolars) GameRegistry.findBlock(EMT.MOD_ID, "EMTSolars" + (i + 1));
            }
            for (int meta = 0; meta < emtSolars.countOfMetas; meta++) {
                EMTSolars.add(new ItemStack(emtSolars, 1, meta));
            }
        }
        TST_BigBroArray.GENERATORS.put("EMT_Solar", EMTSolars.toArray(new ItemStack[0]));

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
