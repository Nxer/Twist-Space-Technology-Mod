package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.dreammaster.gthandler.CustomItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

// spotless:off
public class CrystallineInfinitierRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap AC = RecipeMaps.autoclaveRecipes;
        final IRecipeMap LE = RecipeMaps.laserEngraverRecipes;
        final IRecipeMap HM = RecipeMaps.hammerRecipes;
        final IRecipeMap CI = GTCMRecipe.CrystallineInfinitierRecipes;

        // region Lapotron circuit
        // Shard
        GT_Values.RA.stdBuilder()
            .itemInputs(GTCMItemList.PerfectLapotronCrystal.get(1))
            .itemOutputs(GTCMItemList.LapotronShard.get(64))
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(HM);

        // Growth
        GT_Values.RA.stdBuilder()
            .itemInputs(GTCMItemList.LapotronShard.get(1), MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))
            .fluidInputs(Materials.VibrantAlloy.getMolten(144 * 2))
            .itemOutputs(GTCMItemList.PerfectLapotronCrystal.get(1))
            .eut(RECIPE_UHV)
            .duration(20 * 30)
            .addTo(CI);

        // The first piece
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.RawLapotronCrystal.get(16),
                CustomItemList.LapotronDust.get(64),
                CustomItemList.LapotronDust.get(64),
                CustomItemList.LapotronDust.get(64))
            .fluidInputs(MaterialPool.HolmiumGarnet.getMolten(144 * 256))
            .itemOutputs(GTCMItemList.LapotronShard.get(1))

            .outputChances(999)
            .eut(RECIPE_UHV)
            .duration(20 * 3000)
            .addTo(CI);

        // Holmium Garnet Dust
        // gt mixer
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(4), Materials.Holmium.getDust(3), Materials.Sapphire.getDust(5))

            .itemOutputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(RecipeMaps.mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Holmium.getDust(3),
                Materials.GreenSapphire.getDust(10))

            .itemOutputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(RecipeMaps.mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Holmium.getDust(60),
                Materials.Sapphire.getDust(64),
                Materials.Sapphire.getDust(36))

            .itemOutputs(
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 32))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 30)
            .addTo(RecipeMaps.mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Holmium.getDust(60),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(8))

            .itemOutputs(
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 32))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 60)
            .addTo(RecipeMaps.mixerRecipes);

        // gt++ mixer
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(4), Materials.Holmium.getDust(3), Materials.Sapphire.getDust(5))

            .itemOutputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Holmium.getDust(3),
                Materials.GreenSapphire.getDust(10))

            .itemOutputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 8))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 5)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Holmium.getDust(60),
                Materials.Sapphire.getDust(64),
                Materials.Sapphire.getDust(36))

            .itemOutputs(
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 32))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 30)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Holmium.getDust(60),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(8))

            .itemOutputs(
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 64),
                MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 32))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 60)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        // Chip
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensBlue")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(Utils.copyAmount(0, itemStack), GTCMItemList.PerfectLapotronCrystal.get(1))

                .itemOutputs(
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(64))

                .noOptimize()
                .eut(RECIPE_UHV)
                .duration(20 * 15)
                .addTo(LE);
        }

        // endregion

        // region Energy crystal
        // Shard
        GT_Values.RA.stdBuilder()
            .itemInputs(GTCMItemList.PerfectEnergyCrystal.get(1))

            .itemOutputs(GTCMItemList.EnergyCrystalShard.get(64))

            .eut(RECIPE_ZPM)
            .duration(20 * 5)
            .addTo(HM);

        // Growth
        GT_Values.RA.stdBuilder()
            .itemInputs(GTCMItemList.EnergyCrystalShard.get(1), ItemList.IC2_Energium_Dust.get(64))
            .fluidInputs(Materials.EnergeticAlloy.getMolten(144 * 2))
            .itemOutputs(GTCMItemList.PerfectEnergyCrystal.get(1))

            .eut(RECIPE_ZPM)
            .duration(20 * 30)
            .addTo(CI);

        // The first piece
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(64), ItemList.IC2_Energium_Dust.get(64))
            .fluidInputs(Materials.Redstone.getMolten(144 * 1024))
            .itemOutputs(GTCMItemList.EnergyCrystalShard.get(1))

            .outputChances(1111)
            .eut(RECIPE_ZPM)
            .duration(20 * 3000)
            .addTo(AC);

        // Chip
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensRed")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(Utils.copyAmount(0, itemStack), GTCMItemList.PerfectEnergyCrystal.get(1))

                .itemOutputs(CustomItemList.EngravedEnergyChip.get(64), CustomItemList.EngravedEnergyChip.get(64))

                .noOptimize()
                .eut(RECIPE_UV)
                .duration(20 * 15)
                .addTo(LE);
        }

        // endregion

        // region LuAG
        GT_Values.RA.stdBuilder()
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
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                Materials.GalliumArsenide.getDust(1))
            .fluidInputs(Materials.SiliconSG.getMolten(144*64))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(4))

            .eut(RECIPE_MV)
            .duration(20*450)
            .addTo(CI);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                Materials.GalliumArsenide.getDust(1))
            .fluidInputs(Materials.Silicon.getMolten(144*64))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(2))

            .eut(RECIPE_MV)
            .duration(20*450)
            .addTo(CI);

        // Phosphorus doped Monocrystalline Silicon Boule
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(2),
                Materials.Phosphorus.getDust(16))
            .fluidInputs(Materials.SiliconSG.getMolten(144*128))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(4))

            .eut(RECIPE_HV)
            .duration(20*150)
            .addTo(CI);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                Materials.GalliumArsenide.getDust(2),
                Materials.Phosphorus.getDust(16))
            .fluidInputs(Materials.Silicon.getMolten(144*128))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(2))

            .eut(RECIPE_HV)
            .duration(20*150)
            .addTo(CI);

        // Naquadah doped Monocrystalline Silicon Boule
        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GalliumArsenide.getDust(4),
                        Materials.Naquadah.getDust(2))
                    .fluidInputs(Materials.SiliconSG.getMolten(144*256))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(4))

                    .eut(RECIPE_EV)
                    .duration(20*256)
                    .addTo(CI);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GalliumArsenide.getDust(4),
                        Materials.Naquadah.getDust(2))
                    .fluidInputs(Materials.Silicon.getMolten(144*256))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(2))

                    .eut(RECIPE_EV)
                    .duration(20*256)
                    .addTo(CI);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GalliumArsenide.getDust(4),
                        MyMaterial.naquadahine.get(OrePrefixes.dust, 6))
                    .fluidInputs(Materials.SiliconSG.getMolten(144*256))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(4))

                    .eut(RECIPE_IV)
                    .duration(20*16)
                    .addTo(CI);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GalliumArsenide.getDust(4),
                        MyMaterial.naquadahine.get(OrePrefixes.dust, 6))
                    .fluidInputs(Materials.Silicon.getMolten(144*256))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(2))

                    .eut(RECIPE_IV)
                    .duration(20*16)
                    .addTo(CI);

        // Europium doped Monocrystalline Silicon Boule
        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GalliumArsenide.getDust(8),
                        Materials.Europium.getDust(4))
                    .fluidInputs(Materials.SiliconSG.getMolten(144*512))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(4))

                    .eut(RECIPE_IV)
                    .duration(20*336)
                    .addTo(CI);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GalliumArsenide.getDust(8),
                        Materials.Europium.getDust(4))
                    .fluidInputs(Materials.Silicon.getMolten(144*512))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(2))

                    .eut(RECIPE_IV)
                    .duration(20*336)
                    .addTo(CI);

        // Americium doped Monocrystalline Silicon Boule
        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GalliumArsenide.getDust(16),
                        Materials.Americium.getDust(4))
                    .fluidInputs(Materials.SiliconSG.getMolten(144*1024))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(4))

                    .eut(RECIPE_LuV)
                    .duration(20*450)
                    .addTo(CI);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GalliumArsenide.getDust(16),
                        Materials.Americium.getDust(4))
                    .fluidInputs(Materials.Silicon.getMolten(144*1024))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(2))

                    .eut(RECIPE_LuV)
                    .duration(20*450)
                    .addTo(CI);

        // Optical Enriched Crystalline Silicon Boule
        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(3),
                        ItemList.Field_Generator_UIV.get(0),
                        Materials.GalliumArsenide.getDust(16),
                        Materials.Americium.getDust(4))
                    .fluidInputs(
                        Materials.SiliconSG.getMolten(144*1024),
                        Materials.UUMatter.getFluid(1000*32))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot6.get(4))

                    .eut(RECIPE_UEV)
                    .duration(20*30)
                    .addTo(CI);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_Utility.getIntegratedCircuit(3),
                        ItemList.Field_Generator_UIV.get(0),
                        Materials.GalliumArsenide.getDust(16),
                        Materials.Americium.getDust(4))
                    .fluidInputs(
                        Materials.Silicon.getMolten(144*1024),
                        Materials.UUMatter.getFluid(1000*32))
                    .itemOutputs(ItemList.Circuit_Silicon_Ingot6.get(2))

                    .eut(RECIPE_UEV)
                    .duration(20*30)
                    .addTo(CI);

        // endregion

        // region AE Quartz
        // Nether Quartz
        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.NetherQuartz.getDust(64))
                    .fluidInputs(Materials.Water.getFluid(1000))
                    .itemOutputs(
                        GT_ModHandler.getModItem("appliedenergistics2","item.ItemMultiMaterial",64,11),
                        GT_ModHandler.getModItem("appliedenergistics2","item.ItemMultiMaterial",64,11))

                    .eut(RECIPE_IV)
                    .duration(20*8)
                    .addTo(CI);

        // Certus Quartz
        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.CertusQuartz.getDust(64))
                    .fluidInputs(Materials.Water.getFluid(1000))
                    .itemOutputs(
                        GT_ModHandler.getModItem("appliedenergistics2","item.ItemMultiMaterial",64,10),
                        GT_ModHandler.getModItem("appliedenergistics2","item.ItemMultiMaterial",64,10))

                    .eut(RECIPE_IV)
                    .duration(20*8)
                    .addTo(CI);

        // Fluix Quartz
        GT_Values.RA.stdBuilder()
                    .itemInputs(Materials.Fluix.getDust(64))
                    .fluidInputs(Materials.Water.getFluid(1000))
                    .itemOutputs(
                        GT_ModHandler.getModItem("appliedenergistics2","item.ItemMultiMaterial",64,12),
                        GT_ModHandler.getModItem("appliedenergistics2","item.ItemMultiMaterial",64,12))

                    .eut(RECIPE_IV)
                    .duration(20*8)
                    .addTo(CI);


    }
}
// spotless:on
