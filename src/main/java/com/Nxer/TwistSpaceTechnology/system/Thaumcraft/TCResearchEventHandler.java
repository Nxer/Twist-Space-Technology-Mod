package com.Nxer.TwistSpaceTechnology.system.Thaumcraft;

import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic.EVOLUTION;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;

public final class TCResearchEventHandler {

    private static final String EVOLUTION_RESEARCH = "EVOLUTIO";
    private static final String OFFSPRING_RESEARCH = "OFFSPRING";
    private static final String FONT_OF_ECOLOGY_RESEARCH = "FONT_OF_ECOLOGY";
    private static final String AQUATIC_MODE_RESEARCH = "ECO_SPHERE_MODE_SYMBOL_2";

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.worldObj.isRemote
            || event.player.ticksExisted % 20 != 0
            || !(event.player instanceof EntityPlayerMP player)) return;

        String playerName = player.getCommandSenderName();
        unlockEvolutionResearch(player, playerName);
        unlockOffspringResearch(player, playerName);
        revealFontOfEcologyResearch(player, playerName);
    }

    private static void unlockEvolutionResearch(EntityPlayerMP player, String playerName) {
        if (ResearchManager.isResearchComplete(playerName, EVOLUTION_RESEARCH) || !Thaumcraft.proxy.getPlayerKnowledge()
            .hasDiscoveredAspect(playerName, EVOLUTION)) return;

        completeResearch(player, "@" + EVOLUTION_RESEARCH);
        completeResearch(player, EVOLUTION_RESEARCH);
    }

    private static void unlockOffspringResearch(EntityPlayerMP player, String playerName) {
        if (ResearchManager.isResearchComplete(playerName, OFFSPRING_RESEARCH)
            || !ResearchManager.isResearchComplete(playerName, AQUATIC_MODE_RESEARCH)
            || !hasOffspring(player)) return;

        completeResearch(player, OFFSPRING_RESEARCH);
    }

    private static void revealFontOfEcologyResearch(EntityPlayerMP player, String playerName) {
        if (!ResearchManager.isResearchComplete(playerName, OFFSPRING_RESEARCH)
            || ResearchManager.isResearchComplete(playerName, FONT_OF_ECOLOGY_RESEARCH)
            || ResearchManager.isResearchComplete(playerName, "@" + FONT_OF_ECOLOGY_RESEARCH)
            || !hasScannedOffspring(playerName)) return;

        completeResearch(player, "@" + FONT_OF_ECOLOGY_RESEARCH);
    }

    private static boolean hasScannedOffspring(String playerName) {
        ItemStack offspring = GTCMItemList.OffSpring.get(1);
        int itemHash = ScanManager.generateItemHash(offspring.getItem(), offspring.getItemDamage());
        List<String> scannedObjects = Thaumcraft.proxy.getScannedObjects()
            .get(playerName);
        return scannedObjects != null
            && (scannedObjects.contains("@" + itemHash) || scannedObjects.contains("#" + itemHash));
    }

    private static void completeResearch(EntityPlayerMP player, String researchKey) {
        PacketHandler.INSTANCE.sendTo(new PacketResearchComplete(researchKey), player);
        Thaumcraft.proxy.getResearchManager()
            .completeResearch(player, researchKey);
    }

    private static boolean hasOffspring(EntityPlayerMP player) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (GTCMItemList.OffSpring.equal(stack)) return true;
        }
        return false;
    }
}
