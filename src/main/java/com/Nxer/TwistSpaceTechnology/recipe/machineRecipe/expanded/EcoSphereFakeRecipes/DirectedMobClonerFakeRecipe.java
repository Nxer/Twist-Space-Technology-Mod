package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator.MODE_RECIPE_DURATION;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerOutputInfoKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerRecipeNumberKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorBeaconRequirementKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorTierRequirementKey;

import crazypants.enderio.EnderIO;
import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;

public final class DirectedMobClonerFakeRecipe {

    public static final int LIFE_ESSENCE_PER_PARALLEL = 100;
    public static final int FALLBACK_BLOOD_PER_PARALLEL = 100;
    public static final int FALLBACK_LIFE_ESSENCE_OUTPUT_PER_PARALLEL = 100;

    private DirectedMobClonerFakeRecipe() {}

    public static void rebuildFakeRecipes(Map<Integer, MobRecipeDisplay> recipesById) {
        GTCMRecipe.DirectedMobClonerFakeRecipes.getBackend()
            .clearRecipes();
        registerFallbackRecipe();
        for (Map.Entry<Integer, MobRecipeDisplay> entry : recipesById.entrySet()) {
            ItemStack circuit = GTUtility.getIntegratedCircuit(0);
            FluidStack lifeEssence = FluidRegistry.getFluidStack("lifeessence", LIFE_ESSENCE_PER_PARALLEL);
            if (lifeEssence == null) continue;
            GTValues.RA.stdBuilder()
                .itemInputs(circuit)
                .itemOutputs(createDisplayOutputs(entry.getValue()))
                .fluidInputs(lifeEssence)
                .metadata(EcoSphereSimulatorTierRequirementKey.INSTANCE, 2)
                .metadata(
                    EcoSphereSimulatorBeaconRequirementKey.INSTANCE,
                    entry.getValue()
                        .boss() ? 2 : 1)
                .metadata(DirectedMobClonerRecipeNumberKey.INSTANCE, entry.getKey())
                .metadata(DirectedMobClonerOutputInfoKey.INSTANCE, true)
                .duration(MODE_RECIPE_DURATION)
                .eut(0)
                .fake()
                .addTo(GTCMRecipe.DirectedMobClonerFakeRecipes);
        }
    }

    private static void registerFallbackRecipe() {
        FluidStack bloodInput = FluidRegistry.getFluidStack("blood", FALLBACK_BLOOD_PER_PARALLEL);
        FluidStack lifeEssence = FluidRegistry.getFluidStack("lifeessence", FALLBACK_LIFE_ESSENCE_OUTPUT_PER_PARALLEL);
        if (bloodInput == null || lifeEssence == null) return;
        GTValues.RA.stdBuilder()
            .fluidInputs(bloodInput)
            .fluidOutputs(lifeEssence)
            .metadata(EcoSphereSimulatorTierRequirementKey.INSTANCE, 1)
            .metadata(EcoSphereSimulatorBeaconRequirementKey.INSTANCE, 1)
            .metadata(DirectedMobClonerRecipeNumberKey.INSTANCE, 0)
            .metadata(DirectedMobClonerOutputInfoKey.INSTANCE, false)
            .duration(MODE_RECIPE_DURATION)
            .eut(0)
            .fake()
            .addTo(GTCMRecipe.DirectedMobClonerFakeRecipes);
    }

    private static ItemStack[] createDisplayOutputs(MobRecipeDisplay display) {
        ItemStack soulVial = EnderIO.itemSoulVessel.createVesselWithEntityStub(display.mobName());
        ItemStack firstSelfDrop = display.firstSelfDrop();
        return firstSelfDrop == null ? new ItemStack[] { soulVial } : new ItemStack[] { soulVial, firstSelfDrop };
    }

    public static final class MobRecipeDisplay {

        private final String mobName;
        private final boolean boss;
        private final ItemStack firstSelfDrop;

        public MobRecipeDisplay(String mobName, boolean boss, ItemStack firstSelfDrop) {
            this.mobName = mobName;
            this.boss = boss;
            this.firstSelfDrop = firstSelfDrop == null ? null : firstSelfDrop.copy();
        }

        public String mobName() {
            return mobName;
        }

        public boolean boss() {
            return boss;
        }

        public ItemStack firstSelfDrop() {
            return firstSelfDrop == null ? null : firstSelfDrop.copy();
        }
    }
}
