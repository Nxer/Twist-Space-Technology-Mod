package com.GTNH_Community.gtnhcommunitymod.nei;

import static gregtech.api.enums.Mods.NotEnoughItems;

import net.minecraft.nbt.NBTTagCompound;

import com.GTNH_Community.gtnhcommunitymod.Tags;

import cpw.mods.fml.common.event.FMLInterModComms;

public class NEIHandler {

    public static void IMCSender() {

        sendHandler("gtcm.recipe.IntensifyChemicalDistorterRecipes", "gregtech:gt.blockmachines:19001");
        sendHandler("gtcm.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipes", "gregtech:gt.blockmachines:19002");

        sendCatalyst("gtcm.recipe.IntensifyChemicalDistorterRecipes", "gregtech:gt.blockmachines:19001");
        sendCatalyst("gtcm.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipes", "gregtech:gt.blockmachines:19002");
        sendCatalyst("gt.recipe.largechemicalreactor", "gregtech:gt.blockmachines:19001", -20);
        sendCatalyst("gt.recipe.laserengraver", "gregtech:gt.blockmachines:19002", -10);

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
