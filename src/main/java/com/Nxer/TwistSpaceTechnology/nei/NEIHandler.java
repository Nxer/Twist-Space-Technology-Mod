package com.Nxer.TwistSpaceTechnology.nei;

import static gregtech.api.enums.Mods.NotEnoughItems;

import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.Tags;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;

/**
 * Handle NEI tags.
 * <li>Rewrite {@link RecipeMapWorkable#getAvailableRecipeMaps()} to auto handle GT machine's NEI tag if you are working
 * on a new GT machine which can process many recipe maps.
 */
public class NEIHandler {

    public static void IMCSender() {
        API.addRecipeCatalyst(GTCMItemList.HephaestusAtelier.get(1), "smelting");
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol1.get(1),
            "tst.ecosphere.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes.name",
            -10);
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol2.get(1),
            "tst.ecosphere.recipe.TreeGrowthSimulatorWithoutToolFakeRecipes.name",
            -10);
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol3.get(1),
            "tst.ecosphere.recipe.AquaticZoneSimulatorFakeRecipes.name",
            -10);
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol4.get(1),
            "tst.ecosphere.recipe.AquaticZoneSimulatorFakeRecipes.name",
            -10);
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol5.get(1),
            "tst.ecosphere.recipe.ArtificialGreenHouseFakeRecipes.name",
            -10);
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol6.get(1),
            "tst.ecosphere.recipe.ArtificialGreenHouseFakeRecipes.name",
            -10);
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol7.get(1),
            "tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.name",
            -10);
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol8.get(1),
            "tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.name",
            -10);
        API.addRecipeCatalyst(
            GTCMItemList.EcoSphereExecutionProtocol9.get(1),
            "tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.name",
            -10);
    }

    private static void sendHandler(String aName, String aBlock) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aName);
        aNBT.setString("modName", Tags.MODNAME);
        aNBT.setString("modId", Tags.MODID);
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", 1);
        aNBT.setInteger("yShift", 6);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", aNBT);
    }

    private static void sendCatalyst(String aName, String aStack) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", aName);
        aNBT.setString("itemName", aStack);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", aNBT);
    }

    private static void sendCatalyst(String aName, String aStack, int aPriority) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", aName);
        aNBT.setString("itemName", aStack);
        aNBT.setInteger("priority", aPriority);
        FMLInterModComms.sendMessage(NotEnoughItems.ID, "registerCatalystInfo", aNBT);
    }
}
