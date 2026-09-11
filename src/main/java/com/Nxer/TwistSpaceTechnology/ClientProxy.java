package com.Nxer.TwistSpaceTechnology;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;

import com.Nxer.TwistSpaceTechnology.client.render.ArtificialStarRender;
import com.Nxer.TwistSpaceTechnology.client.render.EyeOfWoodRender;
import com.Nxer.TwistSpaceTechnology.client.render.LargeSolarBoilerRender;
import com.Nxer.TwistSpaceTechnology.client.render.TileArcaneHoleRender;
import com.Nxer.TwistSpaceTechnology.client.sound.SoundLoader;
import com.Nxer.TwistSpaceTechnology.command.ContainerDumpMode;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_BigBroArray;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.loader.NEIConfigLoader;
import com.Nxer.TwistSpaceTechnology.loader.RendereLoader;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.network.packet.ContainerDumpTargetPacket;
import com.Nxer.TwistSpaceTechnology.system.ItemCooldown.CooldownEventHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        NEIConfigLoader.registerPreloadHook();
    }

    @Override
    public void copyToClipboard(String text) {
        GuiScreen.setClipboardString(text);
    }

    @Override
    public void sendContainerDumpTarget(ContainerDumpMode mode) {
        MovingObjectPosition target = Minecraft.getMinecraft().objectMouseOver;
        if (target == null || target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            TST_Network.tst.sendToServer(new ContainerDumpTargetPacket(0, -1, 0, mode));
            return;
        }
        TST_Network.tst.sendToServer(new ContainerDumpTargetPacket(target.blockX, target.blockY, target.blockZ, mode));
    }

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
