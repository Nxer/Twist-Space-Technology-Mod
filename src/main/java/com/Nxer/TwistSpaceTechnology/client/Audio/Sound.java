package com.Nxer.TwistSpaceTechnology.client.Audio;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Sound extends PositionedSoundRecord {

    public Sound(ResourceLocation soundResource, float volume, float pitch, boolean repeat, float xPosition,
        float yPosition, float zPosition) {
        super(soundResource, volume, pitch, xPosition, yPosition, zPosition);
        this.repeat = repeat;
    }
}
