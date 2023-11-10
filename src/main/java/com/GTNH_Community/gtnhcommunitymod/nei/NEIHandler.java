package com.GTNH_Community.gtnhcommunitymod.nei;

import static gregtech.api.enums.Mods.NotEnoughItems;

import net.minecraft.nbt.NBTTagCompound;

import com.GTNH_Community.gtnhcommunitymod.Tags;

import cpw.mods.fml.common.event.FMLInterModComms;

public class NEIHandler {

    public static void IMCSender() {

        sendHandler("gtcm.recipe.IntensifyChemicalDistorterRecipes", "gregtech:gt.blockmachines:19001");
        sendHandler("gtcm.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipes", "gregtech:gt.blockmachines:19002");
        sendHandler("gtcm.recipe.MiracleTopRecipes", "gregtech:gt.blockmachines:19003");

        sendCatalyst("gtcm.recipe.IntensifyChemicalDistorterRecipes", "gregtech:gt.blockmachines:19001");
        sendCatalyst("gtcm.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipes", "gregtech:gt.blockmachines:19002");
        sendCatalyst("gtcm.recipe.MiracleTopRecipes", "gregtech:gt.blockmachines:19003");
        sendCatalyst("gt.recipe.largechemicalreactor", "gregtech:gt.blockmachines:19001", -20);
        sendCatalyst("gt.recipe.laserengraver", "gregtech:gt.blockmachines:19002", -10);
        sendCatalyst("gt.recipe.metalbender", "gregtech:gt.blockmachines:19004", -10);
        sendCatalyst("gt.recipe.press", "gregtech:gt.blockmachines:19004", -10);
        sendCatalyst("gt.recipe.hammer", "gregtech:gt.blockmachines:19004", -10);
        sendCatalyst("gt.recipe.extruder", "gregtech:gt.blockmachines:19004", -10);
        sendCatalyst("gt.recipe.fluidextractor", "gregtech:gt.blockmachines:19005", -10);
        sendCatalyst("gt.recipe.fluidsolidifier", "gregtech:gt.blockmachines:19005", -10);
        sendCatalyst("gtpp.recipe.multimixer", "gregtech:gt.blockmachines:19006", -10);
        sendCatalyst("gt.recipe.polarizer", "gregtech:gt.blockmachines:19007", -10);
        sendCatalyst("gt.recipe.electromagneticseparator", "gregtech:gt.blockmachines:19007", -10);
        sendCatalyst("gt.recipe.wiremill", "gregtech:gt.blockmachines:19008", -10);
        sendCatalyst("gt.recipe.slicer", "gregtech:gt.blockmachines:19009", -10);
        sendCatalyst("gt.recipe.cuttingsaw", "gregtech:gt.blockmachines:19009", -10);
        sendCatalyst("gt.recipe.lathe", "gregtech:gt.blockmachines:19009", -10);
        sendCatalyst("gt.recipe.extractor", "gregtech:gt.blockmachines:19010", -10);
        sendCatalyst("gt.recipe.compressor", "gregtech:gt.blockmachines:19010", -10);
        sendCatalyst("gtpp.recipe.cyclotron", "gregtech:gt.blockmachines:19010", -10);
        sendCatalyst("gtpp.recipe.multicentrifuge", "gregtech:gt.blockmachines:19011", -10);
        sendCatalyst("gtpp.recipe.multielectro", "gregtech:gt.blockmachines:19011", -10);

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
