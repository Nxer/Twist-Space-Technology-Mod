package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AnnihilationConstrainer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Antimatter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ParticleTrapTimeSpaceShield;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SmallLaunchVehicle;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SolarSail;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUEveryAntimatter;
import static com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Values.EUEveryAntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.CustomItemList;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_RecipeAdder;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class DSPRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map DSPLauncher = GTCMRecipe.instance.DSP_LauncherRecipes;
        final GT_Recipe.GT_Recipe_Map SpaceAssembler = IG_RecipeAdder.instance.sSpaceAssemblerRecipes;
        final Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        // launcher
        GT_Values.RA.stdBuilder()
            .itemInputs(SolarSail.get(1))
            .noFluidInputs()
            .itemOutputs(SolarSail.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(DSPLauncher);

        GT_Values.RA.stdBuilder()
            .itemInputs(SmallLaunchVehicle.get(1))
            .noFluidInputs()
            .itemOutputs(
                SmallLaunchVehicle.get(1)
                    .setStackDisplayName(
                        texter("99%% Return an Empty Small Launch Vehicle.", "NEI.EmptySmallLaunchVehicleRecipe.0")))
            .noFluidOutputs()
            .eut(RECIPE_UMV)
            .duration(20 * 900)
            .addTo(DSPLauncher);

        // receiver fake recipe
        GT_Values.RA.stdBuilder()
            .noItemInputs()
            .itemOutputs(CriticalPhoton.get(1))
            .noFluidInputs()
            .noFluidOutputs()
            .specialValue(Integer.MAX_VALUE)
            .eut(0)
            .duration(0)
            .addTo(GTCMRecipe.instance.DSP_ReceiverRecipes);

        // inversion
        GT_Values.RA.stdBuilder()
            .itemInputs(CriticalPhoton.get(2))
            .noFluidInputs()
            .itemOutputs(Antimatter.get(1))
            .fluidOutputs(Materials.Hydrogen.getPlasma(1000))
            .eut(16000)
            .duration(20 * 64 * 8)
            .addTo(GTCMRecipe.instance.QuantumInversionRecipes);

        // Artificial Star Generating
        GT_Values.RA.stdBuilder()
            .itemInputs(AntimatterFuelRod.get(1))
            .noFluidInputs()
            .noItemOutputs()
            .noFluidOutputs()
            .specialValue((int) (EUEveryAntimatterFuelRod / Integer.MAX_VALUE))
            .eut(0)
            .duration(0)
            .addTo(GTCMRecipe.instance.ArtificialStarGeneratingRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Antimatter.get(1))
            .noFluidInputs()
            .noItemOutputs()
            .noFluidOutputs()
            .specialValue((int) (EUEveryAntimatter / Integer.MAX_VALUE))
            .eut(0)
            .duration(0)
            .addTo(GTCMRecipe.instance.ArtificialStarGeneratingRecipes);

        // Annihilation Constrainer
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(21),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1),
                SpaceWarper.get(3),
                ParticleTrapTimeSpaceShield.get(8),

                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                CustomItemList.QuantumCircuit.get(1),
                ItemList.EnergisedTesseract.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.TranscendentMetal, 16))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 16), MaterialsUEVplus.TranscendentMetal.getMolten(144 * 16))
            .itemOutputs(AnnihilationConstrainer.get(1))
            .noFluidOutputs()
            .specialValue(2)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1),
                SpaceWarper.get(8),
                ParticleTrapTimeSpaceShield.get(8),

                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                CustomItemList.QuantumCircuit.get(1),
                ItemList.EnergisedTesseract.get(1),
                MyMaterial.shirabon.get(OrePrefixes.foil, 64))
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 16),
                MaterialsUEVplus.RawStarMatter.getFluid(144 * 16),
                MaterialsUEVplus.WhiteDwarfMatter.getMolten(144 * 8),
                MaterialsUEVplus.BlackDwarfMatter.getMolten(144 * 8))
            .itemOutputs(AnnihilationConstrainer.get(8))
            .noFluidOutputs()
            .specialValue(2)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(23),
                GT_OreDictUnificator
                    .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1),
                SpaceWarper.get(4),
                ParticleTrapTimeSpaceShield.get(8),

                GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 4),
                CustomItemList.QuantumCircuit.get(1),
                ItemList.EnergisedTesseract.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 16),

                MaterialsUEVplus.Universium.getNanite(1))
            .fluidInputs(
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 16),
                MaterialsUEVplus.Eternity.getMolten(144 * 16))
            .itemOutputs(AnnihilationConstrainer.get(64))
            .noFluidOutputs()
            .specialValue(3)
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 32)
            .addTo(SpaceAssembler);

    }
}
