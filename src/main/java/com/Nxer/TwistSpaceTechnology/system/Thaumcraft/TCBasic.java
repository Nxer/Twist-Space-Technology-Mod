package com.Nxer.TwistSpaceTechnology.system.Thaumcraft;

import static gregtech.common.items.MetaGeneratedItem98.FluidCell.LIQUID_DNA;

import net.minecraft.util.ResourceLocation;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TCBasic {

    public static Aspect EVOLUTION;

    public static void registerAspect() {
        EVOLUTION = new Aspect(
            "evolutio",
            // #tr tc.aspect.evolutio
            // # Evolution
            // #zh_CN 进化
            0x6699FF,
            new Aspect[] { Aspect.LIFE, Aspect.EXCHANGE },
            new ResourceLocation("gtnhcommunitymod", "textures/icons/TCAspects/evolutio.png"),
            771);

        ThaumcraftApi.registerObjectTag(
            GTCMItemList.OffSpring.get(1),
            new AspectList().add(EVOLUTION, 2)
                .add(Aspect.LIFE, 8)
                .add(Aspect.ENTROPY, 12)
                .add(Aspect.WATER, 32));

        ThaumcraftApi.registerObjectTag(
            GTCMItemList.FountOfEcology.get(1),
            new AspectList().add(EVOLUTION, 4)
                .add(Aspect.ELDRITCH, 6)
                .add(Aspect.EXCHANGE, 8)
                .add(Aspect.LIFE, 16)
                .add(Aspect.ENTROPY, 24)
                .add(Aspect.WATER, 48));

        if (Mods.Gendustry.isModLoaded()) {
            ThaumcraftApi.registerObjectTag(
                GTModHandler.getModItem(Mods.Gendustry.ID, "LiquidDNABucket", 1),
                new AspectList().add(EVOLUTION, 1)
                    .add(Aspect.METAL, 1));
            ThaumcraftApi.registerObjectTag(
                LIQUID_DNA.get(1),
                new AspectList().add(EVOLUTION, 1)
                    .add(Aspect.METAL, 1));
        }
    }
}
