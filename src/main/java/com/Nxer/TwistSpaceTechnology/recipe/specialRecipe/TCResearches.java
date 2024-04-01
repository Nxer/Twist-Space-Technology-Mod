package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class TCResearches {

    public void loadResearches() {
        new ResearchItem(
            "BH_ELVEN_WORKSHOP",
            "botanichorizons",
            (new AspectList()).merge(Aspect.EARTH, 1)
                .merge(Aspect.MECHANISM, 1)
                .merge(Aspect.MAGIC, 1),
            4,
            -9,
            3,
            GTCMItemList.ElvenWorkshop.get(1, 0)).setParents("BH_GAIA_PYLON")
                .setPages(
                    new ResearchPage("tc.research_text.BH_ELVEN_WORKSHOP.1"),
                    new ResearchPage("tc.research_text.BH_ELVEN_WORKSHOP.2"),
                    new ResearchPage(
                        new InfusionRecipe(
                            "BH_ELVEN_WORKSHOP",
                            GTCMItemList.ElvenWorkshop.get(1, 0),
                            10,
                            (new AspectList()).merge(Aspect.LIFE, 64)
                                .merge(Aspect.EARTH, 64)
                                .merge(Aspect.MAGIC, 64)
                                .merge(Aspect.MECHANISM, 64),
                            new ItemStack(ModBlocks.terraPlate),
                            new ItemStack[] { ItemList.Field_Generator_EV.get(1), ItemList.Casing_IV.get(1),
                                Materials.Steeleaf.getPlates(1), new ItemStack(ModItems.spawnerMover, 1),
                                ItemList.Field_Generator_EV.get(1), ItemList.Casing_IV.get(1),
                                Materials.Steeleaf.getPlates(1), new ItemStack(ModItems.spawnerMover, 1) })))
                .registerResearchItem();
        new ResearchItem(
            "INDUSTRIAL_MAGIC_MATRIX",
            "BASICS",
            (new AspectList()).merge(Aspect.EARTH, 1)
                .merge(Aspect.MECHANISM, 1)
                .merge(Aspect.MAGIC, 1),
            4,
            -9,
            5,
            GTCMItemList.IndustrialMagicMatrix.get(1, 0)).setParents("ICHORIUM")
                .setPages(
                    new ResearchPage("tc.research_text.INDUSTRIAL_MAGIC_MATRIX.1"),
                    new ResearchPage(
                        new InfusionRecipe(
                            "INDUSTRIAL_MAGIC_MATRIX",
                            GTCMItemList.IndustrialMagicMatrix.get(1, 0),
                            25,
                            (new AspectList()).merge(Aspect.LIFE, 128)
                                .merge(Aspect.EARTH, 128)
                                .merge(Aspect.MAGIC, 128)
                                .merge(Aspect.MECHANISM, 128)
                                .merge(Aspect.AIR, 128)
                                .merge(Aspect.EARTH, 128)
                                .merge(Aspect.FIRE, 128)
                                .merge(Aspect.WATER, 128)
                                .merge(Aspect.ORDER, 128)
                                .merge(Aspect.ENTROPY, 128),
                            ItemList.Machine_Multi_Assemblyline.get(1, 0),
                            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L) })))
                .registerResearchItem();
    }
}
