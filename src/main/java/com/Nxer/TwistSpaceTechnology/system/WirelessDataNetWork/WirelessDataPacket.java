package com.Nxer.TwistSpaceTechnology.system.WirelessDataNetWork;

import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic_t.TeamManager;
import com.github.technus.tectech.mechanics.dataTransport.QuantumDataPacket;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import java.util.HashMap;

public class WirelessDataPacket {

    public static HashMap<String, WirelessDataPacket> TeamWirelessData = new HashMap<>();
    public boolean wirelessEnabled = false;

    public long latestActivatedAstralArray = -1000000;
    public long[] latestUpload = new long[20];
    public long[] latestDownload = new long[20];

    public final long[] previewUploaded = new long[20];

    public final long[] previewDownloaded = new long[20];

    public Vec3Impl controllerPosition = null;
    public int loopTags = 0;

    private QuantumDataPacket download(long dataIn) {
        long time = System.currentTimeMillis();
        if (!wirelessEnabled || Math.abs(time - latestActivatedAstralArray) > 10000 || controllerPosition == null)
            return new QuantumDataPacket(0L);
        latestDownload[loopTags] += dataIn;
        double totalRequired = 1, totalUploaded = 1;
        for (int i = 0; i < 20; i++) {
            totalRequired += latestDownload[i];
            totalUploaded += latestUpload[i];
        }
        long result = (long) (Math.min(1.0, totalUploaded / totalRequired) * dataIn);
        // LOG.info("someone required:" + dataIn + " and get " + result);
        return new QuantumDataPacket(result).unifyTraceWith(controllerPosition);
    }

    private void update(IGregTechTileEntity entity, long aTick) {
        // if (loopTags == aTick % 20) return;
        latestActivatedAstralArray = System.currentTimeMillis();
        loopTags = (loopTags + 1) % 20;
        // LOG.info("wireless maintainance start in tick:" + loopTags);
        // LOG.info("Team now has " + latestUpload[loopTags] + " " + latestDownload[loopTags] + " " +
        // previewUploaded[loopTags] + " " + previewDownloaded[loopTags]);
        latestUpload[loopTags] -= previewUploaded[loopTags];
        latestDownload[loopTags] -= previewDownloaded[loopTags];
        previewUploaded[loopTags] = latestUpload[loopTags];
        previewDownloaded[loopTags] = latestDownload[loopTags];
        double totalRequired = 1, totalUploaded = 1;
        for (int i = 0; i < 20; i++) {
            totalRequired += latestDownload[i];
            totalUploaded += latestUpload[i];
        }
        // LOG.info(" and total " + totalRequired + " " + totalUploaded);

    }

    private void setWirelessEnabled(boolean wirelessEnabled) {
        this.wirelessEnabled = wirelessEnabled;
    }

    private void upload(long dataOut) {
        long time = System.currentTimeMillis();
        if (!wirelessEnabled || Math.abs(time - latestActivatedAstralArray) > 10000) return;
        // LOG.info("someone uploaded " + dataOut);
        latestUpload[loopTags] += dataOut;
    }

    public static QuantumDataPacket downloadData(String userId, long dataIn) {
        return getPacketByUserId(userId).download(dataIn);
    }

    public static void uploadData(String userId, long dataOut) {
        getPacketByUserId(userId).upload(dataOut);
    }

    public static void updatePacket(IGregTechTileEntity entity, long aTick) {
        getPacketByUserId(entity.getOwnerName()).update(entity, aTick);
    }

    public static boolean enableWirelessNetWork(IGregTechTileEntity entity) {
        var packet = getPacketByUserId(entity.getOwnerName());
        Vec3Impl pos = new Vec3Impl(entity.getXCoord(), entity.getYCoord(), entity.getZCoord());
        if (packet.wirelessEnabled && packet.controllerPosition != null
            && pos.compareTo(packet.controllerPosition) != 0) return false;
        getPacketByUserId(entity.getOwnerName()).setWirelessEnabled(true);
        if (packet.controllerPosition == null) {
            packet.controllerPosition = new Vec3Impl(entity.getXCoord(), entity.getYCoord(), entity.getZCoord());
        }
        return true;
    }

    public static void disableWirelessNetWork(IGregTechTileEntity entity) {
        var packet = getPacketByUserId(entity.getOwnerName());
        Vec3Impl pos = new Vec3Impl(entity.getXCoord(), entity.getYCoord(), entity.getZCoord());
        if (packet.controllerPosition != null && packet.controllerPosition.compareTo(pos) != 0) return;
        getPacketByUserId(entity.getOwnerName()).setWirelessEnabled(false);
    }

    public static WirelessDataPacket getPacketByUserId(String userId) {
        String team = TeamManager.getOrCreatTeam(userId);
        if (TeamWirelessData.get(team) == null) {
            TeamWirelessData.put(team, new WirelessDataPacket());
        }
        return TeamWirelessData.get(team);
    }

}
