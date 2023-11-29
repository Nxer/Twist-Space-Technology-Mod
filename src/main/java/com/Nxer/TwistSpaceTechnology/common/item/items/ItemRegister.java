package com.Nxer.TwistSpaceTechnology.common.item.items;

import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdder01.initItem01;
import static com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems.MultiStructuresLinkTool;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.DSPName;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegister {

    public static void registryItems() {
        Item[] itemsToReg = { BasicItems.MetaItem01, BasicItems.ProofOfHeroes, MultiStructuresLinkTool };

        for (Item item : itemsToReg) {
            GameRegistry.registerItem(item, item.getUnlocalizedName());
        }

    }

    // spotless:off
    public static void registryItemContainers() {
        GTCMItemList.TestItem0.set(initItem01("Test Item 0", 0, new String[]{texter("A test item, no use.", "tooltips.TestItem0.line1")}));
        GTCMItemList.SpaceWarper.set(initItem01("Space Warper", 1, new String[]{texter(EnumChatFormatting.DARK_BLUE + "Power of gravitation !", "tooltips.SpaceWarper.line1")}));
        GTCMItemList.OpticalSOC.set(initItem01("Gravitational Constraint Optical Quantum Crystal", 2, new String[]{texter("These Photons have their own mind.", "tooltips.OpticalSOC.line1")}));
        GTCMItemList.MoldSingularity.set(initItem01("Mold(Singularity)", 3, new String[]{texter("Mold for making Singularity", "tooltips.MoldSingularity.line1")}));
        GTCMItemList.ParticleTrapTimeSpaceShield.set(initItem01("Particle Trap - SpaceTime Shield", 4, new String[]{texter("Constrain the operator(the photon) to a miniature spacetime.", "tooltips.ParticleTrapTimeSpaceShield.line1")}));
        GTCMItemList.LapotronShard.set(initItem01("Lapotron Shard", 5, new String[]{texter("Even though it's just a shard, the energy fluctuations inside are also visible to the naked eye.", "tooltips.LapotronShard.line1")}));
        GTCMItemList.PerfectLapotronCrystal.set(initItem01("Perfect Lapotron Crystal", 6, new String[]{texter("Immaculate !", "tooltips.PerfectLapotronCrystal.line1")}));
        GTCMItemList.EnergyCrystalShard.set(initItem01("Energy Crystal Shard", 7, new String[]{texter("A red crystal shard, doesn't look like anything special.", "tooltips.EnergyCrystalShard.line1")}));
        GTCMItemList.PerfectEnergyCrystal.set(initItem01("Perfect Energy Crystal", 8, new String[]{texter("As it grew in size, it displayed incredible traits on energy control.", "tooltips.PerfectEnergyCrystal.line1")}));
        GTCMItemList.SolarSail.set(initItem01("Solar Sail", 9, new String[]{texter("Collect and concentrate light energy.", "tooltips.SolarSail.line1"), DSPName}));
        GTCMItemList.DysonSphereFrameComponent.set(initItem01("Dyson Sphere Frame Component", 10, new String[]{texter("Stellar gravity can't destroy these structures, even black hole.", "tooltips.DysonSphereFrameComponent.line1"), DSPName}));
        GTCMItemList.SmallLaunchVehicle.set(initItem01("Small Launch Vehicle", 11, new String[]{texter("Subtle and sophisticated.", "tooltips.SmallLaunchVehicle.line1"), DSPName}));
        GTCMItemList.EmptySmallLaunchVehicle.set(initItem01("Empty Small Launch Vehicle", 12, new String[]{texter("Subtle and sophisticated but Empty.", "tooltips.EmptySmallLaunchVehicle.line1"), DSPName}));
        GTCMItemList.CriticalPhoton.set(initItem01("Critical Photon", 13, new String[]{texter("The future has arrived.", "tooltips.CriticalPhoton.line1"), DSPName}));
        GTCMItemList.Antimatter.set(initItem01("Antimatter", 14, new String[]{texter("The Other Side of Matter.", "tooltips.Antimatter.line1"), DSPName}));
        GTCMItemList.AnnihilationConstrainer.set(initItem01("Annihilation Constrainer", 15, new String[]{texter("Encourage indirect operation.", "tooltips.AnnihilationConstrainer.line1"), DSPName}));
        GTCMItemList.AntimatterFuelRod.set(initItem01("Antimatter Fuel Rod", 16, new String[]{texter("More...", "tooltips.AntimatterFuelRod.line1"), DSPName}));
        GTCMItemList.StellarConstructionFrameMaterial.set(initItem01("Stellar Construction Frame Material", 17, new String[]{texter("Perfect and expensive.", "tooltips.StellarConstructionFrameMaterial.line1"), DSPName}));
        GTCMItemList.GravitationalLens.set(initItem01("Gravitational Lens", 18, new String[]{texter("Its twisted and powerful gravitational field is shielded in a container.", "tooltips.GravitationalLens.line1"), texter("It is usually utilized to work and alter spatial structures, ", "tooltips.GravitationalLens.line2"), texter(" but that doesn't stop some people from taking it and focusing sunlight to light fires for fun.", "tooltips.GravitationalLens.line3"), DSPName}));


        GTCMItemList.HighDimensionalExtend.set(initItem01("High-dimensional extend", 176));
        GTCMItemList.HighDimensionalCircuitDoard.set(initItem01("High-dimensional circuit board", 177));
        GTCMItemList.HighDimensionalCapacitor.set(initItem01("High-dimensional capacitor", 178));
        GTCMItemList.HighDimensionalInterface.set(initItem01("High-dimensional interface", 179));
        GTCMItemList.HighDimensionalDiode.set(initItem01("High-dimensional SMD diode", 180));
        GTCMItemList.HighDimensionalResistor.set(initItem01("High-dimensional Resistor", 181));
        GTCMItemList.HighDimensionalTransistor.set(initItem01("High-dimensional Transistor", 182));


        GTCMItemList.ProofOfHeroes.set(new ItemStack(BasicItems.ProofOfHeroes, 1));
    }

    // spotless:on
    public static void registry() {
        registryItems();
        registryItemContainers();
    }
}
