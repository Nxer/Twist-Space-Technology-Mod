package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static gregtech.api.enums.Mods.Thaumcraft;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.crafting.InfusionRecipe;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
public class TCResearches {
    public void loadResearches()
    {
        ResearchItem ElvenWorkshopResearch = new ResearchItem("BH_ELVEN_WORKSHOP","botanichorizons",new AspectList(),6,-12,3,GTCMItemList.ElvenWorkshop.get(1, 0));
        ElvenWorkshopResearch.setParents("BH_GAIA_PYLON");
        ElvenWorkshopResearch.setPages(new ResearchPage("tc.research_text.BH_ELVEN_WORKSHOP.1"),
        new ResearchPage(
            new InfusionRecipe("BH_ELVEN_WORKSHOP",
            GTCMItemList.ElvenWorkshop.get(1, 0),
            10, 
            (new AspectList()).merge(Aspect.LIFE, 64).merge(Aspect.EARTH, 64).merge(Aspect.MAGIC, 64).merge(Aspect.MECHANISM, 64),
            new ItemStack(ModBlocks.terraPlate)
            ,new ItemStack[]{ItemList.Field_Generator_EV.get(1, null),ItemList.Casing_IV.get(1,null),Materials.Steeleaf.getPlates(1),new ItemStack(ModItems.spawnerMover,1),ItemList.Field_Generator_EV.get(1, null),ItemList.Casing_IV.get(1,null),Materials.Steeleaf.getPlates(1),new ItemStack(ModItems.spawnerMover,1)})));
        ElvenWorkshopResearch.registerResearchItem();
    }
}
