package com.Nxer.TwistSpaceTechnology.common.tile;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_StarThatNeverWas;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarBatch;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarBatch.SurfaceMaterial;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarBeamSequence;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarFieldTransition;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarMath;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarRingStorage;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarStructure;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/** The receiver is the only active render anchor. Other star cores remain ordinary structural cubes. */
public final class TileStarContainment extends TileEntity {

    private static final int MAX_PALETTE = 1024;
    public boolean formed, powered, running, advancing;
    public boolean miningRenderEnabled = true, meteorRenderEnabled = true, ringsVisible;
    public boolean meteorAnimationEnabled = true;
    public int ringRotation;
    public int radius, style;
    public int progress, duration, formationTicks, beamWarmupTicks;
    public int miningTier = 1;
    public final StarFieldTransition field = new StarFieldTransition();
    public final StarBeamSequence beam = new StarBeamSequence();
    public double beamSourceDepth;
    public long seed;
    public int[] materials = new int[0];
    public int[] metadata = new int[0];
    public double[] weights = new double[0];
    public boolean[] fillers = new boolean[0];
    private int hostX, hostY, hostZ;
    private StarBatch paletteBatch;
    private long progressReceivedTick;
    private final StarRingStorage rings = new StarRingStorage();

    public void configure(IGregTechTileEntity host, boolean formed, boolean powered, boolean running, boolean advancing,
        StarBatch batch, int style, boolean ringRenderEnabled, boolean miningRenderEnabled, boolean meteorRenderEnabled,
        boolean meteorAnimationEnabled, int rotation, StarFieldTransition field, StarBeamSequence beam,
        double beamSourceDepth) {
        if (worldObj == null || worldObj.isRemote) return;
        int radius = batch == null ? 0 : StarMath.renderRadius(batch.radius);
        long seed = batch == null ? 0 : batch.seed;
        int progress = batch == null ? 0 : batch.progress;
        int duration = batch == null ? 0 : batch.duration;
        int formationTicks = batch == null ? 0 : batch.formationTicks;
        int beamWarmupTicks = batch == null ? 0 : batch.beamWarmupTicks;
        int miningTier = batch == null ? this.miningTier : batch.miningTier;
        boolean changed = this.formed != formed || this.powered != powered
            || this.running != running
            || this.advancing != advancing
            || this.radius != radius
            || this.seed != seed
            || this.style != style
            || this.miningRenderEnabled != miningRenderEnabled
            || this.meteorRenderEnabled != meteorRenderEnabled
            || this.meteorAnimationEnabled != meteorAnimationEnabled
            || this.progress != progress
            || this.duration != duration
            || this.formationTicks != formationTicks
            || this.beamWarmupTicks != beamWarmupTicks
            || this.miningTier != miningTier
            || !this.field.matches(field)
            || !this.beam.matches(beam)
            || this.beamSourceDepth != beamSourceDepth
            || paletteBatch != batch;
        this.field.copyFrom(field);
        this.beam.copyFrom(beam);
        this.beamSourceDepth = beamSourceDepth;
        hostX = host.getXCoord();
        hostY = host.getYCoord();
        hostZ = host.getZCoord();
        this.formed = formed;
        this.powered = powered;
        this.running = running;
        this.advancing = advancing;
        this.style = style;
        this.miningRenderEnabled = miningRenderEnabled;
        this.meteorRenderEnabled = meteorRenderEnabled;
        this.meteorAnimationEnabled = meteorAnimationEnabled;
        if (formed && ringRenderEnabled) {
            if (rings.hasBlocks() && !rings.complete()) rings.restore(this, false);
            rings.collect(this, rotation);
        } else rings.restore(this, false);
        boolean visible = formed && ringRenderEnabled && rings.complete();
        changed |= ringsVisible != visible || ringRotation != rings.rotation();
        ringsVisible = visible;
        ringRotation = rings.rotation();
        if (this.seed != seed || this.radius != radius || paletteBatch != batch) samplePalette(batch);
        paletteBatch = batch;
        this.seed = seed;
        this.radius = radius;
        this.progress = progress;
        this.duration = duration;
        this.formationTicks = formationTicks;
        this.beamWarmupTicks = beamWarmupTicks;
        this.miningTier = miningTier;
        if (changed) {
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public boolean ownsRingPosition(int x, int y, int z) {
        return rings.owns(this, x, y, z);
    }

    public boolean hasStoredRings() {
        return rings.hasBlocks();
    }

    public void restoreRings(boolean removingAnchor) {
        if (worldObj == null || worldObj.isRemote) return;
        rings.restore(this, removingAnchor);
        ringsVisible = false;
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void samplePalette(StarBatch batch) {
        if (batch == null || batch.surface.isEmpty()) {
            materials = metadata = new int[0];
            weights = new double[0];
            fillers = new boolean[0];
            return;
        }
        int size = Math.min(MAX_PALETTE, batch.surface.size());
        materials = new int[size];
        metadata = new int[size];
        weights = new double[size];
        fillers = new boolean[size];
        // Preserve rare ores and the original pre-Fortune distribution in normal meteor recipes.
        if (batch.surface.size() <= MAX_PALETTE) {
            for (int i = 0; i < size; i++) {
                SurfaceMaterial material = batch.surface.get(i);
                setPaletteSlot(i, material, material.weight());
            }
            return;
        }
        // Only extraordinarily large scripted registries use quantiles, keeping this packet below 32 KiB.
        double total = batch.surface.stream()
            .mapToDouble(SurfaceMaterial::weight)
            .sum();
        int index = 0;
        double cumulative = batch.surface.get(0)
            .weight();
        for (int i = 0; i < materials.length; i++) {
            double target = (i + 0.5) * total / materials.length;
            while (index + 1 < batch.surface.size() && cumulative < target) cumulative += batch.surface.get(++index)
                .weight();
            setPaletteSlot(i, batch.surface.get(index), 1);
        }
    }

    private void setPaletteSlot(int index, SurfaceMaterial material, double weight) {
        ItemStack stack = material.stack();
        materials[index] = Item.getIdFromItem(stack.getItem());
        metadata[index] = stack.getItemDamage();
        weights[index] = weight;
        fillers[index] = material.filler();
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote || (!formed && !rings.hasBlocks()) || worldObj.getTotalWorldTime() % 20 != 0) return;
        if (!worldObj.blockExists(hostX, hostY, hostZ)) return;
        TileEntity host = worldObj.getTileEntity(hostX, hostY, hostZ);
        if (!(host instanceof IGregTechTileEntity base)
            || !(base.getMetaTileEntity() instanceof TST_StarThatNeverWas star)
            || !star.mMachine) {
            formed = powered = running = advancing = false;
            field.sample(worldObj.getTotalWorldTime(), false);
            beam.clear();
            restoreRings(false);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(
            xCoord - 85,
            yCoord - Math.max(StarStructure.RECEIVER_Y - StarStructure.MINER_FLOOR_Y, beamSourceDepth + 1),
            zCoord - 85,
            xCoord + 86,
            yCoord + 153,
            zCoord + 86);
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        writeVisual(tag);
        rings.write(tag);
        tag.setInteger("hostX", hostX);
        tag.setInteger("hostY", hostY);
        tag.setInteger("hostZ", hostZ);
    }

    private void writeVisual(NBTTagCompound tag) {
        tag.setBoolean("formed", formed);
        tag.setBoolean("powered", powered);
        tag.setBoolean("running", running);
        tag.setBoolean("advancing", advancing);
        tag.setBoolean("miningRenderEnabled", miningRenderEnabled);
        tag.setBoolean("meteorRenderEnabled", meteorRenderEnabled);
        tag.setBoolean("meteorAnimationEnabled", meteorAnimationEnabled);
        tag.setBoolean("ringsVisible", ringsVisible);
        tag.setInteger("ringRotation", ringRotation);
        tag.setInteger("radius", radius);
        tag.setInteger("progress", progress);
        tag.setInteger("duration", duration);
        tag.setInteger("formationTicks", formationTicks);
        tag.setInteger("beamWarmupTicks", beamWarmupTicks);
        tag.setInteger("miningTier", miningTier);
        field.write(tag);
        beam.write(tag);
        tag.setDouble("beamSourceDepth", beamSourceDepth);
        tag.setInteger("style", style);
        tag.setLong("seed", seed);
        tag.setIntArray("materials", materials);
        tag.setIntArray("metadata", metadata);
        byte[] fillerFlags = new byte[fillers.length];
        for (int i = 0; i < fillers.length; i++) fillerFlags[i] = (byte) (fillers[i] ? 1 : 0);
        tag.setByteArray("fillers", fillerFlags);
        NBTTagList distribution = new NBTTagList();
        for (double weight : weights) distribution.appendTag(new NBTTagDouble(weight));
        tag.setTag("weights", distribution);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        readVisual(tag);
        rings.read(tag);
        ringRotation = rings.rotation();
        paletteBatch = null;
        hostX = tag.getInteger("hostX");
        hostY = tag.getInteger("hostY");
        hostZ = tag.getInteger("hostZ");
    }

    private void readVisual(NBTTagCompound tag) {
        formed = tag.getBoolean("formed");
        powered = tag.getBoolean("powered");
        running = tag.getBoolean("running");
        advancing = tag.getBoolean("advancing");
        miningRenderEnabled = tag.getBoolean("miningRenderEnabled");
        meteorRenderEnabled = tag.getBoolean("meteorRenderEnabled");
        meteorAnimationEnabled = tag.getBoolean("meteorAnimationEnabled");
        ringsVisible = tag.getBoolean("ringsVisible");
        ringRotation = Math.floorMod(tag.getInteger("ringRotation"), 4);
        radius = Math.max(0, Math.min(StarMath.MAX_RENDER_RADIUS, tag.getInteger("radius")));
        duration = Math.max(0, tag.getInteger("duration"));
        formationTicks = Math.max(0, Math.min(duration - 1, tag.getInteger("formationTicks")));
        beamWarmupTicks = Math.max(0, Math.min(duration - formationTicks - 1, tag.getInteger("beamWarmupTicks")));
        miningTier = Math.max(1, Math.min(3, tag.getInteger("miningTier")));
        field.read(tag);
        beam.read(tag);
        beamSourceDepth = Math.max(0, Math.min(64, tag.getDouble("beamSourceDepth")));
        progress = Math.max(0, Math.min(duration, tag.getInteger("progress")));
        progressReceivedTick = worldObj == null ? 0 : worldObj.getTotalWorldTime();
        style = tag.getInteger("style") == 1 ? 1 : 0;
        seed = tag.getLong("seed");
        materials = tag.getIntArray("materials");
        metadata = tag.getIntArray("metadata");
        byte[] fillerFlags = tag.getByteArray("fillers");
        fillers = new boolean[fillerFlags.length];
        for (int i = 0; i < fillerFlags.length; i++) fillers[i] = fillerFlags[i] != 0;
        NBTTagList distribution = tag.getTagList("weights", 6);
        weights = new double[distribution.tagCount()];
        double total = 0;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = distribution.func_150309_d(i);
            total += weights[i];
            if (!Double.isFinite(weights[i]) || weights[i] < 0) total = Double.NaN;
        }
        if (materials.length != metadata.length || materials.length != weights.length
            || materials.length != fillers.length
            || materials.length > MAX_PALETTE
            || !Double.isFinite(total)
            || total <= 0) {
            materials = metadata = new int[0];
            weights = new double[0];
            fillers = new boolean[0];
        }
    }

    /** A paid, unfinished meteor remains present independently of this tick's processing or the miner's state. */
    public boolean hasMeteor() {
        return formed && radius > 0 && duration > 0 && progress < duration;
    }

    /** One server checkpoint per second; rendering interpolates locally and never runs ahead indefinitely. */
    public double interpolatedProgress(double clock) {
        return Math.min(
            duration,
            progress + (running && advancing ? Math.max(0, Math.min(20, clock - progressReceivedTick)) : 0));
    }

    public double interpolatedMiningProgress(double clock) {
        int miningStart = formationTicks + beamWarmupTicks;
        // Excavation follows the paid job even while an unrelated beam wave is draining.
        if (progress < miningStart || duration <= miningStart) return 0;
        return Math.max(0, interpolatedProgress(clock) - miningStart);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        // Clients need the render snapshot, not the server's ring escrow or host inventory ownership.
        writeVisual(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readVisual(packet.func_148857_g());
    }
}
