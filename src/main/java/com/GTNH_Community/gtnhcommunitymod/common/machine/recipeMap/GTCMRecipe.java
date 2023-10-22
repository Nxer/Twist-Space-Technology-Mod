package com.GTNH_Community.gtnhcommunitymod.common.machine.recipeMap;

import java.util.Collection;
import java.util.HashSet;

import gregtech.api.util.GT_Recipe;

public class GTCMRecipe {

    public static final GTCMRecipe instance = new GTCMRecipe();

    public static class GTCMRecipeMap extends GT_Recipe.GT_Recipe_Map_LargeNEI {

        /**
         * Initialises a new type of Recipe Handler.
         *
         * @param aRecipeList                a List you specify as Recipe List. Usually just an ArrayList with a
         *                                   pre-initialised Size.
         * @param aUnlocalizedName           the unlocalised Name of this Recipe Handler, used mainly for NEI.
         * @param aLocalName                 @deprecated the displayed Name inside the NEI Recipe GUI for optionally
         *                                   registering aUnlocalizedName
         *                                   with the language manager
         * @param aNEIName
         * @param aNEIGUIPath                the displayed GUI Texture, usually just a Machine GUI. Auto-Attaches ".png"
         *                                   if forgotten.
         * @param aUsualInputCount           the usual amount of Input Slots this Recipe Class has.
         * @param aUsualOutputCount          the usual amount of Output Slots this Recipe Class has.
         * @param aMinimalInputItems
         * @param aMinimalInputFluids
         * @param aAmperage
         * @param aNEISpecialValuePre        the String in front of the Special Value in NEI.
         * @param aNEISpecialValueMultiplier the Value the Special Value is getting Multiplied with before displaying
         * @param aNEISpecialValuePost       the String after the Special Value. Usually for a Unit or something.
         * @param aShowVoltageAmperageInNEI
         * @param aNEIAllowed                if NEI is allowed to display this Recipe Handler in general.
         */
        public GTCMRecipeMap(Collection<gregtech.api.util.GT_Recipe> aRecipeList, String aUnlocalizedName,
            String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
            int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
            int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
            boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

    }

    public final GTCMRecipeMap IntensifyChemicalDistorterRecipes = new GTCMRecipeMap(
        new HashSet<>(),
        "gtcm.recipe.IntensifyChemicalDistorterRecipes",
        "Intensify Chemical Distorter",
        null,
        "gregtech:textures/gui/basicmachines/LCRNEI",
        6,
        6,
        0,
        0,
        1,
        "Heat Capacity: ",
        1,
        " K",
        false,
        true);

}
