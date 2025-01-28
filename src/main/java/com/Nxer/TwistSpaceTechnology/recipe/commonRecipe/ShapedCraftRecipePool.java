package com.Nxer.TwistSpaceTechnology.recipe.commonRecipe;

import static gregtech.api.util.GTModHandler.addCraftingRecipe;
import static vazkii.botania.common.item.ModItems.rune;
import static vazkii.botania.common.item.ModItems.spark;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.multiuse.BlockMultiUseCore;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;

public class ShapedCraftRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // Large Steam Forge Hammer
        addCraftingRecipe(
            GTCMItemList.LargeSteamForgeHammer.get(1),
            new Object[] { "ABA", "CDC", "ABA", 'A', ItemList.Casing_BronzePlatedBricks, 'B',
                new ItemStack(Blocks.anvil, 1, 0), 'C', OreDictNames.craftingPiston, 'D',
                MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        // Large Steam MaterialsAlloy Smelter
        addCraftingRecipe(
            GTCMItemList.LargeSteamAlloySmelter.get(1),
            new Object[] { "ABA", "BCB", "ABA", 'A',
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Steel, 1), 'B',
                ItemList.Machine_HP_AlloySmelter.get(1), 'C', MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        // Mana Hatch
        addCraftingRecipe(
            GTCMItemList.ManaHatch.get(1),
            new Object[] { "ABA", "ACA", "ADA", 'A',
                GTOreDictUnificator.get(OrePrefixes.plate, MaterialsBotania.ElvenElementium, 1), 'B',
                new ItemStack(spark), 'C', ItemList.Hatch_Input_HV.get(1), 'D', new ItemStack(rune, 1, 0) });

        // Eye of Wood
        addCraftingRecipe(
            GTCMItemList.EyeOfWood.get(1),
            new Object[] { "ABA", "BCB", "ABA", 'A', new ItemStack(Blocks.brick_block), 'B', "plankWood", 'C',
                GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.get(1) });

        if (Config.Enable_IndustrialMagnetarSeparator) {
            // Industrial Magnetar Separator
            addCraftingRecipe(
                GTCMItemList.IndustrialMagnetarSeparator.get(1),
                new Object[] { "ABA", "CDC", "ABA", 'A', MaterialsAlloy.TALONITE.getPlate(1), 'B',
                    OrePrefixes.circuit.get(Materials.Elite), 'C', ItemList.IV_Coil.get(1), 'D',
                    ItemList.Machine_IV_ElectromagneticSeparator.get(1) });

            // Anti Magnetic Casing
            addCraftingRecipe(
                GTCMItemList.AntiMagneticCasing.get(1),
                new Object[] { "ABA", "CDC", "ABA", 'A', MaterialsAlloy.TALONITE.getPlate(1), 'B',
                    MaterialsAlloy.MARAGING300.getLongRod(1), 'C', MaterialsAlloy.MARAGING250.getRod(1), 'D',
                    MaterialsAlloy.MARAGING300.getFrameBox(1) });
        }

        // transform existing PA to PA Research
        addCraftingRecipe(
            GTCMItemList.ResearchOnAncientPA.get(1),
            new Object[] { "X", 'X', ItemList.Processing_Array.get(1) });

        // original PA recipe, changed output to PA Research
        // but the machine hull is changed from EV to IV, to prevent recipe dupe, just in case,
        // since it's just used for MegaArray, so it should not matter at all.
        addCraftingRecipe(
            GTCMItemList.ResearchOnAncientPA.get(1),
            new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_IV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.IV), 'F',
                ItemList.Robot_Arm_EV, 'T', ItemList.Energy_LapotronicOrb });

        // Manufacturing Center
        addCraftingRecipe(
            GTCMItemList.ManufacturingCenter.get(1),
            new Object[] { "AhA", "BHB", "CdC", 'A', MaterialsAlloy.STABALLOY.getPlate(1), 'B',
                Materials.StainlessSteel.getPlates(1), 'C', MaterialsAlloy.ZIRCONIUM_CARBIDE.getPlate(1), 'H',
                ItemList.Hull_IV, });

        // MultiUse Core

        BlockMultiUseCore.setupMultiUseCoreRecipe(
            GTCMItemList.MultiUseCore_IV.get(1),
            new IItemContainer[] { ItemList.Machine_IV_Compressor, ItemList.Machine_IV_Lathe,
                ItemList.Machine_IV_Polarizer, ItemList.Machine_IV_Fermenter, ItemList.Machine_IV_FluidExtractor,
                ItemList.Machine_IV_Extractor, ItemList.Machine_IV_LaserEngraver, ItemList.Machine_IV_Autoclave,
                ItemList.Machine_IV_FluidSolidifier, });

        BlockMultiUseCore.setupMultiUseCoreRecipe(
            GTCMItemList.MultiUseCore_LuV.get(1),
            new IItemContainer[] { ItemList.CompressorLuV, ItemList.LatheLuV, ItemList.PolarizerLuV,
                ItemList.FermenterLuV, ItemList.FluidExtractorLuV, ItemList.ExtractorLuV,
                ItemList.PrecisionLaserEngraverLuV, ItemList.AutoclaveLuV, ItemList.FluidSolidifierLuV, });

        BlockMultiUseCore.setupMultiUseCoreRecipe(
            GTCMItemList.MultiUseCore_ZPM.get(1),
            new IItemContainer[] { ItemList.CompressorZPM, ItemList.LatheZPM, ItemList.PolarizerZPM,
                ItemList.FermenterZPM, ItemList.FluidExtractorZPM, ItemList.ExtractorZPM,
                ItemList.PrecisionLaserEngraverZPM, ItemList.AutoclaveZPM, ItemList.FluidSolidifierZPM, });

        BlockMultiUseCore.setupMultiUseCoreRecipe(
            GTCMItemList.MultiUseCore_UV.get(1),
            new IItemContainer[] { ItemList.CompressorUV, ItemList.LatheUV, ItemList.PolarizerUV, ItemList.FermenterUV,
                ItemList.FluidExtractorUV, ItemList.ExtractorUV, ItemList.PrecisionLaserEngraverUV,
                ItemList.AutoclaveUV, ItemList.FluidSolidifierUV, });
        // end region
    }
}
