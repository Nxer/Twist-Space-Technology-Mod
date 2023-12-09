package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.common.material.MaterialPool.entropyReductionProcess;
import static com.Nxer.TwistSpaceTechnology.common.material.MaterialPool.eventHorizonDiffusers;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import cpw.mods.fml.common.Loader;
import galaxyspace.SolarSystem.SolarSystemPlanets;
import gregtech.api.enums.Materials;

public class StellarMaterialSiphonRecipePool implements IRecipePool {

    public static final Map<String, Map<Integer, FluidStack>> RECIPES = new HashMap<>();

    /**
     * Add all pumping recipes to the siphon
     */
    public static void addPumpingRecipes() {
        // T3 (-> 0.5 to 2A of EV)
        Map<Integer, FluidStack> jupiterRecipes = new HashMap<>();
        jupiterRecipes.put(1, Materials.Hydrogen.getGas(15000));
        jupiterRecipes.put(2, Materials.Helium.getGas(500));
        jupiterRecipes.put(3, Materials.Nitrogen.getGas(300));
        jupiterRecipes.put(4, Materials.Oxygen.getGas(200));

        // T5 (-> 0.5 to 2A of LuV)
        Map<Integer, FluidStack> saturnRecipes = new HashMap<>();
        saturnRecipes.put(1, Materials.Hydrogen.getGas(18000));
        saturnRecipes.put(2, Materials.Helium.getGas(800));
        saturnRecipes.put(3, Materials.Oxygen.getGas(500));
        saturnRecipes.put(4, Materials.LiquidOxygen.getGas(150));

        // T5 (-> 0.5 to 2A of LuV)
        Map<Integer, FluidStack> uranusRecipes = new HashMap<>();
        uranusRecipes.put(1, Materials.Deuterium.getGas(5000));
        uranusRecipes.put(3, Materials.Argon.getGas(250));

        // T6 (-> 0.5 to 2A of ZPM)
        Map<Integer, FluidStack> neptuneRecipes = new HashMap<>();
        neptuneRecipes.put(1, Materials.Tritium.getGas(3000));
        neptuneRecipes.put(2, Materials.Helium_3.getGas(500));
        neptuneRecipes.put(3, Materials.Ammonia.getGas(400));

        if (Loader.isModLoaded("bartworks")) {
            uranusRecipes.put(2, WerkstoffLoader.Neon.getFluidOrGas(450));
            uranusRecipes.put(4, WerkstoffLoader.Krypton.getFluidOrGas(100));
            neptuneRecipes.put(4, WerkstoffLoader.Xenon.getFluidOrGas(350));
        }
        Map<Integer, FluidStack> blackHoleRecipes = new HashMap<>();
        blackHoleRecipes.put(1, eventHorizonDiffusers.getMolten(144));
        blackHoleRecipes.put(1, entropyReductionProcess.getMolten(144));

        RECIPES.put(SolarSystemPlanets.planetJupiter.getUnlocalizedName(), jupiterRecipes);
        RECIPES.put(SolarSystemPlanets.planetSaturn.getUnlocalizedName(), saturnRecipes);
        RECIPES.put(SolarSystemPlanets.planetUranus.getUnlocalizedName(), uranusRecipes);
        RECIPES.put(SolarSystemPlanets.planetNeptune.getUnlocalizedName(), neptuneRecipes);
        RECIPES.put("blackHole", blackHoleRecipes);// should replace with world later
    }

    @Override
    public void loadRecipes() {
        addPumpingRecipes();
    }
}
