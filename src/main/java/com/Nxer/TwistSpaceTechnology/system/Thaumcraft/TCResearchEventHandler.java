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
    private static final String FOUNT_OF_ECOLOGY_RESEARCH = "FOUNT_OF_ECOLOGY";
    private static final String AQUATIC_EXECUTION_PROTOCOL_RESEARCH = "ECO_SPHERE_EXECUTION_PROTOCOL_3";
    private static final String ECO_SPHERE_EXECUTION_PROTOCOL_8_RESEARCH = "ECO_SPHERE_EXECUTION_PROTOCOL_8";
    private static final String ECO_SPHERE_EXECUTION_PROTOCOL_7_RESEARCH = "ECO_SPHERE_EXECUTION_PROTOCOL_7";
    private static final String ECO_SPHERE_TIER_TWO_RESEARCH = "ECO_SPHERE_TIER_TWO";

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.worldObj.isRemote
            || event.player.ticksExisted % 20 != 0
            || !(event.player instanceof EntityPlayerMP player)) return;

        String playerName = player.getCommandSenderName();
        unlockEvolutionResearch(player, playerName);
        unlockOffspringResearch(player, playerName);
        revealFountOfEcologyResearch(player, playerName);
        revealDirectedCloningResearch(player, playerName, ECO_SPHERE_EXECUTION_PROTOCOL_8_RESEARCH);
    }

    private static void unlockEvolutionResearch(EntityPlayerMP player, String playerName) {
        if (ResearchManager.isResearchComplete(playerName, EVOLUTION_RESEARCH) || !Thaumcraft.proxy.getPlayerKnowledge()
            .hasDiscoveredAspect(playerName, EVOLUTION)) return;

        completeResearch(player, "@" + EVOLUTION_RESEARCH);
        completeResearch(player, EVOLUTION_RESEARCH);
    }

    private static void unlockOffspringResearch(EntityPlayerMP player, String playerName) {
        if (ResearchManager.isResearchComplete(playerName, OFFSPRING_RESEARCH)
            || !ResearchManager.isResearchComplete(playerName, AQUATIC_EXECUTION_PROTOCOL_RESEARCH)
            || !hasOffspring(player)) return;

        completeResearch(player, OFFSPRING_RESEARCH);
    }

    private static void revealFountOfEcologyResearch(EntityPlayerMP player, String playerName) {
        if (!ResearchManager.isResearchComplete(playerName, OFFSPRING_RESEARCH)
            || ResearchManager.isResearchComplete(playerName, FOUNT_OF_ECOLOGY_RESEARCH)
            || ResearchManager.isResearchComplete(playerName, "@" + FOUNT_OF_ECOLOGY_RESEARCH)
            || !hasScannedOffspring(playerName)) return;

        completeResearch(player, "@" + FOUNT_OF_ECOLOGY_RESEARCH);
    }

    private static void revealDirectedCloningResearch(EntityPlayerMP player, String playerName, String researchKey) {
        if (ResearchManager.isResearchComplete(playerName, researchKey)
            || ResearchManager.isResearchComplete(playerName, "@" + researchKey)
            || (!ResearchManager.isResearchComplete(playerName, ECO_SPHERE_EXECUTION_PROTOCOL_7_RESEARCH)
                && !ResearchManager.isResearchComplete(playerName, ECO_SPHERE_TIER_TWO_RESEARCH)))
            return;

        completeResearch(player, "@" + researchKey);
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
