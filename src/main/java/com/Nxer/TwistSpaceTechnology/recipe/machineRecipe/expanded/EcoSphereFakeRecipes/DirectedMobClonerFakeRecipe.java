package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerBossRequirementKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.MegaTreeFarmTierRequirementKey;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import crazypants.enderio.EnderIO;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public final class DirectedMobClonerFakeRecipe {

    private static final int DURATION = 20 * 5;

    private DirectedMobClonerFakeRecipe() {}

    public static void rebuildFakeRecipes(Map<Integer, MobRecipeDisplay> recipesById) {
        GTCMRecipe.DirectedMobClonerFakeRecipes.getBackend()
            .clearRecipes();
        registerLifeEssenceRecipe();
        for (Map.Entry<Integer, MobRecipeDisplay> entry : recipesById.entrySet()) {
            ItemStack circuit = GTUtility.getIntegratedCircuit(0);
            circuit.setStackDisplayName("Recipe Number: " + entry.getKey());
            FluidStack lifeEssence = FluidRegistry.getFluidStack("lifeessence", 100);
            if (lifeEssence == null) continue;
            GTValues.RA.stdBuilder()
                .itemInputs(circuit)
                .itemOutputs(createDisplayOutputs(entry.getValue()))
                .fluidInputs(lifeEssence)
                .metadata(MegaTreeFarmTierRequirementKey.INSTANCE, 2)
                .metadata(
                    DirectedMobClonerBossRequirementKey.INSTANCE,
                    entry.getValue()
                        .boss() ? new ItemStack(Items.diamond_sword) : null)
                .duration(DURATION)
                .eut(0)
                .fake()
                .addTo(GTCMRecipe.DirectedMobClonerFakeRecipes);
        }
    }

    private static void registerLifeEssenceRecipe() {
        FluidStack water = FluidRegistry.getFluidStack("water", 1000);
        FluidStack lifeEssence = FluidRegistry.getFluidStack("lifeessence", 20);
        ItemStack lifeBucket = GTModHandler.getModItem(Mods.BloodMagic.ID, "bucketLife", 1, 0);
        if (water == null || lifeEssence == null || lifeBucket == null) return;
        lifeBucket.setStackDisplayName(TextEnums.tr("MegaTreeFarm.nei.mobHealthLifeEssence"));
        ItemStack circuit = GTUtility.getIntegratedCircuit(0);
        circuit.stackSize = 0;
        circuit.setStackDisplayName(TextEnums.tr("MegaTreeFarm.nei.recipeNumberZero"));
        // #tr MegaTreeFarm.nei.recipeNumberZero
        // # Recipe Number: 0
        // #zh_CN 配方编号：0
        GTValues.RA.stdBuilder()
            .itemInputs(circuit)
            .itemOutputs(lifeBucket)
            .fluidInputs(water)
            .fluidOutputs(lifeEssence)
            .metadata(MegaTreeFarmTierRequirementKey.INSTANCE, 1)
            .duration(DURATION)
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
