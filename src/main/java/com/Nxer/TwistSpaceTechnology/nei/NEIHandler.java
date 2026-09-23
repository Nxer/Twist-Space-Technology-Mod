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

        /*
         * sendCatalyst("mc.recipe.furnace", "gregtech:gt.blockmachines:19037", -10);
         * sendHandler("tst.common.recipe.IntensifyChemicalDistorterRecipeMap.name", "gregtech:gt.blockmachines:19001");
         * sendHandler("tst.common.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipeMap.name",
         * "gregtech:gt.blockmachines:19002");
         * sendHandler("tst.common.recipe.MiracleTopRecipeMap.name", "gregtech:gt.blockmachines:19003");
         * sendHandler("tst.common.recipe.QuantumInversionRecipeMap.name", "gregtech:gt.blockmachines:19003");
         * sendHandler("tst.common.recipe.CrystallineInfinitierRecipeMap.name", "gregtech:gt.blockmachines:19012");
         * sendHandler("tst.dyson.recipe.DSP_LauncherRecipeMap.name", "gregtech:gt.blockmachines:19013");
         * sendHandler("tst.dyson.recipe.DSP_ReceiverRecipeMap.name", "gregtech:gt.blockmachines:19014");
         * sendHandler("tst.dyson.recipe.ArtificialStarGeneratingRecipeMap.name", "gregtech:gt.blockmachines:19015");
         * sendHandler("tst.common.recipe.OreProcessingVisualRecipeMap.name", "gregtech:gt.blockmachines:19017");
         * sendHandler("tst.common.recipe.megaUniversalSpaceStationRecipeMap.name", "gregtech:gt.blockmachines:19018");
         * sendHandler("tst.common.recipe.ElvenWorkshopRecipeMap.name", "gregtech:gt.blockmachines:19500");
         * sendHandler("tst.common.recipe.RuneEngraverRecipeMap.name", "gregtech:gt.blockmachines:19500");
         * sendHandler("tst.common.recipe.CokingFactoryRecipeMap.name", "gregtech:gt.blockmachines:19021");
         * sendHandler("tst.common.recipe.StellarForgeRecipeMap.name", "gregtech:gt.blockmachines:19016");
         * sendHandler("tst.common.recipe.HyperSpacetimeTransformerRecipeMap.name", "gregtech:gt.blockmachines:19501");
         * sendCatalyst("tst.common.recipe.IntensifyChemicalDistorterRecipeMap.name",
         * "gregtech:gt.blockmachines:19001");
         * sendCatalyst("tst.common.recipe.PreciseHighEnergyPhotonicQuantumMasterRecipeMap.name",
         * "gregtech:gt.blockmachines:19002");
         * sendCatalyst("tst.common.recipe.MiracleTopRecipeMap.name", "gregtech:gt.blockmachines:19003");
         * sendCatalyst("tst.common.recipe.QuantumInversionRecipeMap.name", "gregtech:gt.blockmachines:19003");
         * sendCatalyst("gt.recipe.largechemicalreactor", "gregtech:gt.blockmachines:19001", -20);
         * sendCatalyst("gt.recipe.laserengraver", "gregtech:gt.blockmachines:19002", -10);
         * sendCatalyst("gt.recipe.metalbender", "gregtech:gt.blockmachines:19004", -10);
         * sendCatalyst("gt.recipe.press", "gregtech:gt.blockmachines:19004", -10);
         * sendCatalyst("gt.recipe.hammer", "gregtech:gt.blockmachines:19004", -10);
         * sendCatalyst("gt.recipe.extruder", "gregtech:gt.blockmachines:19004", -10);
         * sendCatalyst("gt.recipe.fluidextractor", "gregtech:gt.blockmachines:19005", -10);
         * sendCatalyst("gt.recipe.fluidsolidifier", "gregtech:gt.blockmachines:19005", -10);
         * sendCatalyst("gtpp.recipe.multimixer", "gregtech:gt.blockmachines:19006", -10);
         * sendCatalyst("gt.recipe.polarizer", "gregtech:gt.blockmachines:19007", -10);
         * sendCatalyst("gt.recipe.electromagneticseparator", "gregtech:gt.blockmachines:19007", -10);
         * sendCatalyst("gt.recipe.wiremill", "gregtech:gt.blockmachines:19008", -10);
         * sendCatalyst("gt.recipe.slicer", "gregtech:gt.blockmachines:19009", -10);
         * sendCatalyst("gt.recipe.cuttingsaw", "gregtech:gt.blockmachines:19009", -10);
         * sendCatalyst("gt.recipe.lathe", "gregtech:gt.blockmachines:19009", -10);
         * sendCatalyst("gt.recipe.extractor", "gregtech:gt.blockmachines:19010", -10);
         * sendCatalyst("gt.recipe.compressor", "gregtech:gt.blockmachines:19010", -10);
         * sendCatalyst("gtpp.recipe.cyclotron", "gregtech:gt.blockmachines:19010", -10);
         * sendCatalyst("gtpp.recipe.multicentrifuge", "gregtech:gt.blockmachines:19011", -10);
         * sendCatalyst("gtpp.recipe.multielectro", "gregtech:gt.blockmachines:19011", -10);;
         * sendCatalyst("gtpp.recipe.moleculartransformer", "gregtech:gt.blockmachines:19501", -10);
         * sendCatalyst("gt.recipe.autoclave", "gregtech:gt.blockmachines:19012", -10);
         * sendCatalyst("tst.common.recipe.CrystallineInfinitierRecipeMap.name", "gregtech:gt.blockmachines:19012");
         * sendCatalyst("tst.dyson.recipe.DSP_LauncherRecipeMap.name", "gregtech:gt.blockmachines:19013");
         * sendCatalyst("tst.dyson.recipe.DSP_ReceiverRecipeMap.name", "gregtech:gt.blockmachines:19014");
         * sendCatalyst("tst.dyson.recipe.ArtificialStarGeneratingRecipeMap.name", "gregtech:gt.blockmachines:19015");
         * sendCatalyst("gtpp.recipe.alloyblastsmelter", "gregtech:gt.blockmachines:19016");
         * sendCatalyst("tst.common.recipe.OreProcessingVisualRecipeMap.name", "gregtech:gt.blockmachines:19017");
         * sendCatalyst("tst.common.recipe.megaUniversalSpaceStationRecipeMap.name", "gregtech:gt.blockmachines:19018");
         * sendCatalyst("tst.common.recipe.ElvenWorkshopRecipeMap.name", "gregtech:gt.blockmachines:19500");
         * sendCatalyst("tst.common.recipe.RuneEngraverRecipeMap.name", "gregtech:gt.blockmachines:19500");
         * sendCatalyst("tst.common.recipe.CokingFactoryRecipeMap.name", "gregtech:gt.blockmachines:19021");
         * sendCatalyst("tst.common.recipe.StellarForgeRecipeMap.name", "gregtech:gt.blockmachines:19016");
         * sendCatalyst("gt.recipe.sifter", "gregtech:gt.blockmachines:19023");
         * sendCatalyst("tst.common.recipe.HyperSpacetimeTransformerRecipeMap.name", "gregtech:gt.blockmachines:19501");
         * sendCatalyst("tst.common.recipe.AssemblyLineWithoutResearchRecipeMap.name",
         * "gregtech:gt.blockmachines:19028");
         * sendCatalyst("gg.recipe.componentassemblyline", "gregtech:gt.blockmachines:19028");
         * sendCatalyst("gt.recipe.assembler", "gregtech:gt.blockmachines:19028");
         * sendCatalyst("gg.recipe.precise_assembler", "gregtech:gt.blockmachines:19028");
         * sendCatalyst("gt.recipe.distillery", "gregtech:gt.blockmachines:19031");
         * sendCatalyst("gt.recipe.distillationtower", "gregtech:gt.blockmachines:19031");
         * sendCatalyst("gt.recipe.arcfurnace", "gregtech:gt.blockmachines:19046");
         * sendCatalyst("gt.recipe.plasmaarcfurnace", "gregtech:gt.blockmachines:19046");
         * sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:19046");
         * sendCatalyst("tst.common.recipe.StarKernelGeneratorRecipeMap.name", "gregtech:gt.blockmachines:19046");
         * sendCatalyst("gt.recipe.electromagneticseparator", "gregtech:gt.blockmachines:19050");
         */
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
