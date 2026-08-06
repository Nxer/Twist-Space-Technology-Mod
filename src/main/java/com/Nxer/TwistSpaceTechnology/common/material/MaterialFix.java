package com.Nxer.TwistSpaceTechnology.common.material;

import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_MULTISTEP;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_TIER;

import java.lang.reflect.Method;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.recipe.TecTechRecipeMaps;

public class MaterialFix {

    // spotless:off
    public static void load() {

        // Holmium Garnet

        GTValues.RA.stdBuilder()
                   .itemInputs(MaterialPool.HolmiumGarnet.get(OrePrefixes.dust, 1))
                   .fluidOutputs(MaterialPool.HolmiumGarnet.getMolten(144))
                   .eut(96)
                   .duration(72)
                   .addTo(RecipeMaps.fluidExtractionRecipes);

        // spotless:off
        // Neutronium Alloy

                addBlastRecipe(MaterialsTST.NeutroniumAlloy, (int) TierEU.RECIPE_UIV, 54 * 20, 12500, true,true);
                addVacuumFreezerRecipe(MaterialsTST.NeutroniumAlloy,(int)TierEU.RECIPE_UEV,18 * 20);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        Materials.Neutronium.getDust(7),
                        Materials.Duranium.getDust(2),
                        Materials.Flerovium.getDust(1),
        //                MaterialsElements.STANDALONE.WHITE_METAL.getDust(1),
                        Materials.DarkIron.getDust(1),
                        GTUtility.getIntegratedCircuit(2)
                    )
                    .fluidInputs(Materials.Hydrogen.getPlasma(1000 * 14))
                    .itemOutputs(MaterialsTST.NeutroniumAlloy.getDust(12))
                    .eut(TierEU.RECIPE_UHV)
                    .duration(12 * 20)
                    .addTo(RecipeMaps.mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(6),
                        Materials.Neutronium.getDust(7),
                        Materials.Duranium.getDust(2),
                        Materials.Flerovium.getDust(1),
        //                MaterialsElements.STANDALONE.WHITE_METAL.getDust(1),
                        Materials.DarkIron.getDust(1))
                    .fluidInputs(Materials.Hydrogen.getGas(1000 * 14))
                    .fluidOutputs(MaterialsTST.NeutroniumAlloy.getMolten(16 * 144))
                    .eut(TierEU.RECIPE_UIV)
                    .duration(660 * 20)
                    .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

                // Axonis Alloy

                addBlastRecipe(MaterialsTST.AxonisAlloy, (int) TierEU.RECIPE_UMV, 720, 13200, true,false);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(6),
                        MaterialsElements.STANDALONE.DRAGON_METAL.getDust(5),
                        MaterialsElements.STANDALONE.HYPOGEN.getDust(3),
                        Materials.Creon.getDust(2),
                        Materials.Ichorium.getDust(1),
                        Materials.Terbium.getDust(1),
                        GGMaterial.shirabon.get(OrePrefixes.dust,1))
                    .fluidOutputs(MaterialsTST.AxonisAlloy.getMolten(144 * 12))
                    .eut(TierEU.RECIPE_UMV)
                    .duration(720 * 20)
                    .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

                // Axonium

                makeWires(MaterialsTST.Axonium,20000,0L, 0L,1_000_000L, Integer.MAX_VALUE, false, true);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.getIntegratedCircuit(20))
                    .fluidInputs(
                        new FluidStack( MaterialsElements.STANDALONE.DRAGON_METAL.getPlasma(),5000),
                        new FluidStack( MaterialsElements.STANDALONE.HYPOGEN.getPlasma(),3000),
                        Materials.Creon.getPlasma(2000),
                        Materials.Ichorium.getPlasma(1000),
                        Materials.Terbium.getPlasma(1000),
                        GGMaterial.shirabon.getMolten(1000),
                        Materials.PrimordialMatter.getFluid(1000))
                    .fluidOutputs(MaterialsTST.Axonium.getPlasma(120000))
                    .eut(TierEU.RECIPE_MAX)
                    .duration(45 * 20)
                    .addTo(RecipeMaps.transcendentPlasmaMixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialsTST.Axonium.getDust(1))
                    .fluidOutputs(MaterialsTST.Axonium.getMolten(144))
                    .eut(TierEU.RECIPE_UHV)
                    .duration(240)
                    .addTo(RecipeMaps.fluidExtractionRecipes);

                addGodForgePlasmaRecipes(MaterialsTST.Axonium, 25 * 20, false, 2);
                addGodForgePlasmaRecipes(MaterialsTST.Dubnium, 7 * 20, true, 1);


            }

            public static void addBlastRecipe(Materials aMaterial, int EUt, int duration, int level, boolean gas,
                boolean isHot) {
                ItemStack input = aMaterial.getDust(1);
                ItemStack output = isHot ? GTOreDictUnificator.get(OrePrefixes.ingotHot, aMaterial, 1) : aMaterial.getIngots(1);
                if (gas) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(input, GTUtility.getIntegratedCircuit(11))
                        .fluidInputs(Materials.Helium.getGas(1000))
                        .itemOutputs(output)
                        .eut(EUt)
                        .duration(duration * TICKS)
                        .metadata(COIL_HEAT, level)
                        .addTo(RecipeMaps.blastFurnaceRecipes);
                } else {
                    GTValues.RA.stdBuilder()
                        .itemInputs(input, GTUtility.getIntegratedCircuit(1))
                        .itemOutputs(output)
                        .eut(EUt)
                        .duration(duration * TICKS)
                        .metadata(COIL_HEAT, level)
                        .addTo(RecipeMaps.blastFurnaceRecipes);
                }
            }

            public static void addVacuumFreezerRecipe(Materials aMaterial, FluidStack[] fluidIn, FluidStack[] fluidOut, int eut,
                int ticks) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, aMaterial, 1))
                    .fluidInputs(fluidIn)
                    .itemOutputs(aMaterial.getIngots(1))
                    .fluidOutputs(fluidOut)
                    .eut(eut)
                    .duration(ticks)
                    .addTo(RecipeMaps.vacuumFreezerRecipes);
            }

            public static void addVacuumFreezerRecipe(Materials aMaterial, int eut, int ticks) {
                addVacuumFreezerRecipe(aMaterial, new FluidStack[] {}, new FluidStack[] {}, eut, ticks);
            }

            public static void addGodForgePlasmaRecipes(Materials aMaterial, int duration, boolean isMultistep, int tier) {
                GTValues.RA.stdBuilder()
                    .itemInputs(aMaterial.getDust(1))
                    .fluidOutputs(aMaterial.getPlasma(144))
                    .duration(duration)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, isMultistep)
                    .metadata(FOG_PLASMA_TIER, tier)
                    .addTo(TecTechRecipeMaps.godforgePlasmaRecipes);

                GTValues.RA.stdBuilder()
                    .fluidInputs(aMaterial.getMolten(144))
                    .fluidOutputs(aMaterial.getPlasma(144))
                    .duration(duration)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, isMultistep)
                    .metadata(FOG_PLASMA_TIER, tier)
                    .addTo(TecTechRecipeMaps.godforgePlasmaRecipes);
            }

            public static void makeWires(Materials aMaterial, int aStartID, long aLossInsulated, long aLoss, long aAmperage,
                long aVoltage, boolean aInsulatable, boolean aAutoInsulated) {
                try {
                    Class<?> builderClass = Class
                        .forName("gregtech.loaders.preload.LoaderMetaPipeEntities$WireCableBuilder");
                    Object builder = call(builderClass, null, "builder");
                    builder = call(builderClass, builder, "material", new Class<?>[] { Materials.class }, aMaterial);
                    builder = call(builderClass, builder, "startId", new Class<?>[] { int.class }, aStartID);
                    builder = call(builderClass, builder, "lossWire", new Class<?>[] { int.class }, Math.toIntExact(aLoss));
                    builder = call(builderClass, builder, "amperage", new Class<?>[] { int.class }, Math.toIntExact(aAmperage));
                    builder = call(builderClass, builder, "voltage", new Class<?>[] { long.class }, aVoltage);
                    builder = aInsulatable
                        ? call(builderClass, builder, "lossCable", new Class<?>[] { int.class }, Math.toIntExact(aLossInsulated))
                        : call(builderClass, builder, "disableCable");
                    if (aAutoInsulated) builder = call(builderClass, builder, "disableElectricDamage");
                    call(builderClass, builder, "build");
                } catch (ReflectiveOperationException | ArithmeticException e) {
                    throw new IllegalStateException("Failed to register GT wires for " + aMaterial.mName, e);
                }
            }

            private static Object call(Class<?> owner, Object target, String name) throws ReflectiveOperationException {
                return call(owner, target, name, new Class<?>[0]);
            }

            private static Object call(Class<?> owner, Object target, String name, Class<?>[] types, Object... args)
                throws ReflectiveOperationException {
                Method method = owner.getDeclaredMethod(name, types);
                method.setAccessible(true);
                return method.invoke(target, args);
            }
        // spotless:on
}
