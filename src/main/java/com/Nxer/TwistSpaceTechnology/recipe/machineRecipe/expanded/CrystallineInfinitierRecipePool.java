package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UV;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class CrystallineInfinitierRecipePool {

    public static void loadRecipes() {

        final IRecipeMap AC = RecipeMaps.autoclaveRecipes;
        final IRecipeMap LE = RecipeMaps.laserEngraverRecipes;
        final IRecipeMap HM = RecipeMaps.hammerRecipes;
        final IRecipeMap CI = GTCMRecipe.CrystallineInfinitierRecipes;

        // region Adv Nether Stars
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.nether_star))
            .fluidInputs(Materials.Neutronium.getMolten(144 * 2))
            .itemOutputs(ItemList.Gravistar.get(1))
            .eut(RECIPE_IV)
            .duration(20 * 16)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.nether_star))
            .fluidInputs(Materials.Infinity.getMolten(144 * 2))
            .itemOutputs(ItemList.NuclearStar.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 16)
            .addTo(CI);

        // endregion

        // region Cubic Zirconia
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 10))
            .fluidInputs(Materials.Oxygen.getGas(1000 * 20))
            .itemOutputs(WerkstoffLoader.CubicZirconia.get(OrePrefixes.gem, 30))
            .eut(RECIPE_HV)
            .duration(20 * 144)
            .addTo(CI);
        // endregion

        // Holmium Garnet Dust
        // gt mixer
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4), Materials.Holmium.getDust(3), Materials.Sapphire.getDust(5))
            .itemOutputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(RecipeMaps.mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                Materials.Holmium.getDust(3),
                Materials.GreenSapphire.getDust(10))
            .itemOutputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(RecipeMaps.mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                Materials.Holmium.getDust(60),
                Materials.Sapphire.getDust(64),
                Materials.Sapphire.getDust(36))
            .itemOutputs(
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 32))
            .eut(RECIPE_UV)
            .duration(20 * 30)
            .addTo(RecipeMaps.mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                Materials.Holmium.getDust(60),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(8))
            .itemOutputs(
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 32))
            .eut(RECIPE_UV)
            .duration(20 * 60)
            .addTo(RecipeMaps.mixerRecipes);

        // gt++ mixer
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4), Materials.Holmium.getDust(3), Materials.Sapphire.getDust(5))
            .itemOutputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                Materials.Holmium.getDust(3),
                Materials.GreenSapphire.getDust(10))
            .itemOutputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                Materials.Holmium.getDust(60),
                Materials.Sapphire.getDust(64),
                Materials.Sapphire.getDust(36))
            .itemOutputs(
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 32))
            .eut(RECIPE_UV)
            .duration(20 * 30)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                Materials.Holmium.getDust(60),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(8))
            .itemOutputs(
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 32))
            .eut(RECIPE_UV)
            .duration(20 * 60)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        // region LuAG
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dust, 0))
            .fluidInputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumOxygenBlend.getMolten(144 * 64))
            .itemOutputs(
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64))
            .eut(RECIPE_UHV)
            .duration(20 * 10)
            .addTo(CI);

        // endregion

        // region Boule

        // Monocrystalline Silicon Boule
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2), Materials.GalliumArsenide.getDust(1))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 64))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(4))
            .eut(RECIPE_MV)
            .duration(20 * 450)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2), Materials.GalliumArsenide.getDust(1))
            .fluidInputs(Materials.Silicon.getMolten(144 * 64))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(2))
            .eut(RECIPE_MV)
            .duration(20 * 450)
            .addTo(CI);

        // Phosphorus doped Monocrystalline Silicon Boule
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(2),
                Materials.Phosphorus.getDust(16))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 128))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(4))
            .eut(RECIPE_HV)
            .duration(20 * 150)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(2),
                Materials.Phosphorus.getDust(16))
            .fluidInputs(Materials.Silicon.getMolten(144 * 128))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(2))
            .eut(RECIPE_HV)
            .duration(20 * 150)
            .addTo(CI);

        // Naquadah doped Monocrystalline Silicon Boule
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(4),
                Materials.Naquadah.getDust(2))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 256))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(4))
            .eut(RECIPE_EV)
            .duration(20 * 256)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(4),
                Materials.Naquadah.getDust(2))
            .fluidInputs(Materials.Silicon.getMolten(144 * 256))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(2))
            .eut(RECIPE_EV)
            .duration(20 * 256)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(4),
                GGMaterial.naquadahine.get(OrePrefixes.dust, 6))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 256))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(4))
            .eut(RECIPE_IV)
            .duration(20 * 16)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(4),
                GGMaterial.naquadahine.get(OrePrefixes.dust, 6))
            .fluidInputs(Materials.Silicon.getMolten(144 * 256))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(2))
            .eut(RECIPE_IV)
            .duration(20 * 16)
            .addTo(CI);

        // Europium doped Monocrystalline Silicon Boule
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(8),
                Materials.Europium.getDust(4))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 512))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(4))
            .eut(RECIPE_IV)
            .duration(20 * 336)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(8),
                Materials.Europium.getDust(4))
            .fluidInputs(Materials.Silicon.getMolten(144 * 512))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(2))
            .eut(RECIPE_IV)
            .duration(20 * 336)
            .addTo(CI);

        // Americium doped Monocrystalline Silicon Boule
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(16),
                Materials.Americium.getDust(4))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 1024))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(4))
            .eut(RECIPE_LuV)
            .duration(20 * 450)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(16),
                Materials.Americium.getDust(4))
            .fluidInputs(Materials.Silicon.getMolten(144 * 1024))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(2))
            .eut(RECIPE_LuV)
            .duration(20 * 450)
            .addTo(CI);

        // Optical Enriched Crystalline Silicon Boule
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                ItemList.Field_Generator_UIV.get(0),
                Materials.GalliumArsenide.getDust(16),
                Materials.Americium.getDust(4))
            .fluidInputs(Materials.SiliconSG.getMolten(144 * 1024), Materials.UUMatter.getFluid(1000 * 32))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot6.get(4))
            .eut(RECIPE_UEV)
            .duration(20 * 30)
            .addTo(CI);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                ItemList.Field_Generator_UIV.get(0),
                Materials.GalliumArsenide.getDust(16),
                Materials.Americium.getDust(4))
            .fluidInputs(Materials.Silicon.getMolten(144 * 1024), Materials.UUMatter.getFluid(1000 * 32))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot6.get(2))
            .eut(RECIPE_UEV)
            .duration(20 * 30)
            .addTo(CI);

        // endregion

        // region AE Quartz
        // Nether Quartz
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.NetherQuartz.getDust(64))
            .fluidInputs(Materials.Water.getFluid(1000))
            .itemOutputs(
                GTModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 64, 11),
                GTModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 64, 11))
            .eut(RECIPE_IV)
            .duration(20 * 8)
            .addTo(CI);

        // Certus Quartz
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CertusQuartz.getDust(64))
            .fluidInputs(Materials.Water.getFluid(1000))
            .itemOutputs(
                GTModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 64, 10),
                GTModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 64, 10))
            .eut(RECIPE_IV)
            .duration(20 * 8)
            .addTo(CI);

        // Fluix Quartz
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Fluix.getDust(64))
            .fluidInputs(Materials.Water.getFluid(1000))
            .itemOutputs(
                GTModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 64, 12),
                GTModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 64, 12))
            .eut(RECIPE_IV)
            .duration(20 * 8)
            .addTo(CI);

    }
}
