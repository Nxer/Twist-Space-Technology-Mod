package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CompactCyclotronCoil;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DSPLauncher;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DenseCyclotronOuterCasing;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.IncompactCyclotron;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.LightWeightPlate;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.COMET_Cyclotron;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Casing_AdvancedVacuum;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Casing_Cyclotron_Coil;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Casing_Cyclotron_External;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.util.enums.TierEU;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import galaxyspace.core.register.GSBlocks;
import gregtech.api.enums.MaterialsUEVplus;
import gtPlusPlus.core.material.MaterialMisc;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraftforge.fluids.FluidStack;
import tectech.recipe.TTRecipeAdder;

public class IncompactCyclotronRecipes implements IRecipePool {
    // spotless:off
    @Override
    public void loadRecipes() {

        if (Config.Enable_IncompactCyclotron) {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GregtechItemList.COMET_Cyclotron.get(1),
                2_048_000,
                1024,
                (int) TierEU.RECIPE_UEV,
                4,
                new Object[] {
                    ItemList.Hull_UEV.get(64),
                    GregtechItemList.COMET_Cyclotron.get(64),
                    ItemList.Casing_Coil_Infinity.get(8),
                    GregtechItemList.Laser_Lens_Special.get(4),

                    ItemList.Field_Generator_UHV.get(16),
                    ItemRefer.HiC_T5.get(32),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 16),
                    GGMaterial.enrichedNaquadahAlloy.get(OrePrefixes.plateDense, 16),

                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.NaquadahAlloy, 16),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 64)},
                new FluidStack[] {
                    Materials.NaquadahAlloy.getMolten(144 * 256),
                    FluidRegistry.getFluidStack("cryotheum", 1_000_000),
                    MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144 * 2) },
                GTCMItemList.IncompactCyclotron.get(1),
                20 * 900,
                (int) TierEU.RECIPE_UEV);
        }

        // Dense Cyclotron Outer Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_Cyclotron_External.get(1),
            1_024_000,
            512,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] {
                Casing_Cyclotron_External.get(4),
                Casing_AdvancedVacuum.get(4),
                ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 3, 16),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(6),

                MaterialsAlloy.ABYSSAL.getLongRod(12),
                MaterialsAlloy.TITANSTEEL.getScrew(24),
                ItemList.Electric_Piston_UV.get(6)},
            new FluidStack[] {
                MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144 * 10),
                GGMaterial.enrichedNaquadahAlloy.getMolten(144 * 4)},
            GTCMItemList.DenseCyclotronOuterCasing.get(1),
            20 * 30,
            (int) TierEU.RECIPE_UHV);

        // Compact Cyclotron Coil
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_Cyclotron_Coil.get(1),
            1_024_000,
            512,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] {
                Casing_Cyclotron_Coil.get(16),
                ItemList.Casing_Coil_Superconductor.get(4),
                new ItemStack[] { GregtechItemList.Battery_Gem_2.get(1), ItemList.Energy_Module.get(2) },
                ItemList.UHV_Coil.get(64),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 2),
                ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 19, 16),
                ItemList.Field_Generator_UHV.get(1)},
            new FluidStack[] {
                Materials.UUMatter.getFluid(1000 * 64),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(1000 * 16),
                Materials.Longasssuperconductornameforuhvwire.getMolten(144 * 8),
                GGMaterial.enrichedNaquadahAlloy.getMolten(144 * 2)},
            GTCMItemList.DenseCyclotronOuterCasing.get(1),
            20 * 60,
            (int) TierEU.RECIPE_UHV);

    }
    // spotless:on
}
