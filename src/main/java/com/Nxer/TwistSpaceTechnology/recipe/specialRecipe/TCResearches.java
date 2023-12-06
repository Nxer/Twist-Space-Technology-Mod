package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static gregtech.api.enums.Mods.Thaumcraft;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
public class TCResearches {
    public void loadResearches()
    {
        ResearchItem ElvenWorkshopResearch = new ResearchItem("BH_ELVEN_WORKSHOP","botanichorizons",new AspectList(),6,-12,3,GTCMItemList.ElvenWorkshop.get(1, 0));
        ElvenWorkshopResearch.setParents("BH_GAIA_PYLON");
        ElvenWorkshopResearch.setPages(new ResearchPage("1"));
        ElvenWorkshopResearch.registerResearchItem();
    }
}
