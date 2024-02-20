package com.Nxer.TwistSpaceTechnology.common.item.items;

import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdder01.initItem01;
import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdderRune.initItemRune;
import static com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems.MultiStructuresLinkTool;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.DSPName;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextHandler;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegister {

    public static void registryItems() {
        Item[] itemsToReg = { BasicItems.MetaItem01, BasicItems.ProofOfHeroes, MultiStructuresLinkTool,
            BasicItems.MetaItemRune, BasicItems.PowerChair };

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
        GTCMItemList.PurpleMagnoliaPetal.set(initItem01("Purple Magnolia Petal", 19, new String[]{ TextHandler.texter("Petals falling from Alfheim...", "tooltips.PurpleMagnoliaPetal.line1")}));
        GTCMItemList.PurpleMagnoliaSapling.set(initItem01("Purple Magnolia Sapling", 20, new String[]{ TextHandler.texter("Not plantable. Need to be on ic2 crop sticks.", "tooltips.PurpleMagnoliaSapling.line1")}));
        GTCMItemList.VoidPollen.set(initItem01("Void Pollen", 21, new String[]{ TextHandler.texter("Pollen yet to be arisen.", "tooltips.VoidPollen.line1")}));
        GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.set(initItem01("Primitive Man's SpaceTime Distortion Device", 22, new String[]{ TextHandler.texter("Anyway...", "tooltips.PrimitiveMansSpaceTimeDistortionDevice.line1")}));

        if(Config.activateMegaSpaceStation) {
            GTCMItemList.HighDimensionalExtend.set(initItem01("High-dimensional extend", 176));
            GTCMItemList.HighDimensionalCircuitDoard.set(initItem01("High-dimensional circuit board", 177));
            GTCMItemList.HighDimensionalCapacitor.set(initItem01("High-dimensional capacitor", 178));
            GTCMItemList.HighDimensionalInterface.set(initItem01("High-dimensional interface", 179));
            GTCMItemList.HighDimensionalDiode.set(initItem01("High-dimensional SMD diode", 180));
            GTCMItemList.HighDimensionalResistor.set(initItem01("High-dimensional Resistor", 181));
            GTCMItemList.HighDimensionalTransistor.set(initItem01("High-dimensional Transistor", 182));

            GTCMItemList.CosmicCircuitBoard.set(initItem01("Cosmic Circuit Board", 300));
            GTCMItemList.IntelligentImitationNeutronStarCore.set(initItem01("Intelligent imitation neutron star core", 301));
            GTCMItemList.EventHorizonNanoSwarm.set(initItem01("Event Horizon Nano Swarm", 302));
            GTCMItemList.MicroDimensionOutput.set(initItem01("Micro dimension output", 303));
            GTCMItemList.EntropyReductionMaterialNanoswarm.set(initItem01("Entropy reduction material nanoswarm", 304));
            GTCMItemList.TranscendentCircuitBoard.set(initItem01("Transcendent Circuit Board", 305));
            GTCMItemList.NarrativeLayerOverwritingDevice.set(initItem01("Narrative layer overwriting device", 306));
            GTCMItemList.HyperspaceNarrativeLayerAdaptiveSpecialSRA.set(initItem01("Hyperspace Narrative Layer Adaptive Special SRA", 307));
            GTCMItemList.RealSingularityNanoSwarm.set(initItem01("Real Singularity Nano Swarm", 308));
            GTCMItemList.ParadoxEngine.set(initItem01("paradox engine", 309));
            GTCMItemList.QuasarSoc.set(initItem01("quasar soc", 310));
            GTCMItemList.MiniatureGalaxy.set(initItem01("miniature galaxy", 311));
            GTCMItemList.Self_adaptiveAI1.set(initItem01("self-adaptive AI Ⅰ", 312));
            GTCMItemList.Self_adaptiveAI2.set(initItem01("self-adaptive AI Ⅱ", 313));
            GTCMItemList.Self_adaptiveAI3.set(initItem01("self-adaptive AI Ⅲ", 314));
            GTCMItemList.Self_adaptiveAI4.set(initItem01("self-adaptive AI Ⅳ", 315));
            GTCMItemList.Self_adaptiveAI5.set(initItem01("self-adaptive AI Ⅴ", 316));
            GTCMItemList.CoreOfT800.set(initItem01("core of T800", 317));
            GTCMItemList.ExoticCircuitBoard.set(initItem01("Exotic Circuit Board", 318));
            GTCMItemList.spaceStationConstructingMaterialMax.set(initItem01("very good item", 319));
            GTCMItemList.LightQuantumMatrix.set(initItem01("Light Quantum Matrix", 320));
            //GTCMItemList.StarCore.set(initItem01("Star Core", 321));
            GTCMItemList.CasimirQuantumFiber.set(initItem01("Casimir Quantum Fiber", 322));
            GTCMItemList.SuperstringStructure.set(initItem01("Superstring structure", 323));
            GTCMItemList.DynamicParadoxBody.set(initItem01("Dynamic Paradox Body", 324));
            GTCMItemList.VoidPrism.set(initItem01("Void Prism", 325));
            GTCMItemList.PulsarCore.set(initItem01("Pulsar core", 326));
            GTCMItemList.StarCrystalI.set(initItem01("Star Crystal I", 327));
            GTCMItemList.SuperDimensionalRing.set(initItem01("Super Dimensional Ring", 328));
            GTCMItemList.HyperdimensionalExpansion.set(initItem01("Hyperdimensional expansion", 329));
            GTCMItemList.OpticalLayer.set(initItem01("optical layer", 330));
            GTCMItemList.MagneticSpinI.set(initItem01("Magnetic Spin I", 331));
            GTCMItemList.Staraxis.set(initItem01("star axis", 332));
            GTCMItemList.BoltzmannBrain.set(initItem01("Boltzmann Brain", 333));
            GTCMItemList.RemnantsOfTheBigBang.set(initItem01("Remnants of the Big Bang", 334));
            GTCMItemList.StrangeFilm.set(initItem01("Strange Film", 335));
            GTCMItemList.PulseManganese.set(initItem01("Pulse Manganese", 336));
            GTCMItemList.SuperdimensionalWeb.set(initItem01("Superdimensional Web", 337));
            GTCMItemList.PinoanStructure.set(initItem01("Pinoan Structure", 338));
            GTCMItemList.QuantumChain.set(initItem01("Quantum Chain", 339));
            GTCMItemList.StarBelt.set(initItem01("Star Belt", 340));
            GTCMItemList.Nanoflow.set(initItem01("Nanoflow", 341));
            GTCMItemList.Space_TimeLayer.set(initItem01("Space-time layer", 342));
            GTCMItemList.SuperconductingRing.set(initItem01("Superconducting ring", 343));
            GTCMItemList.QuantizedSuperstringStructure.set(initItem01("Quantized Superstring Structure", 344));
            GTCMItemList.ThezeroPointOfVacuumCanManifestObjects.set(initItem01("The zero point of vacuum can manifest objects", 345));
            GTCMItemList.StarCore.set(initItem01("Star Core", 346));
            GTCMItemList.QuasarRemnant.set(initItem01("quasar remnant", 347));
            GTCMItemList.InfiniteDivineMachine.set(initItem01("Infinite Divine Machine", 348));
            GTCMItemList.OriginalSoup.set(initItem01("Original Soup", 349));
            GTCMItemList.GravityBelt.set(initItem01("Gravity Belt", 350));
            GTCMItemList.anti_GravityEngine.set(initItem01("anti-gravity engine", 351));
            GTCMItemList.CondensedDarkMatterPolymer.set(initItem01("Condensed Dark Matter Polymer", 352));
            GTCMItemList.LowDensityDarkMatterPolymer.set(initItem01("Low Density Dark Matter Polymer", 353));
            GTCMItemList.InfiniteRecursion.set(initItem01("infinite recursion", 354));
            GTCMItemList.SuperdimensionalSpiral.set(initItem01("Superdimensional Spiral", 355));
            GTCMItemList.InfiniteDivineMachineI.set(initItem01("Infinite Divine Machine I", 356));
            GTCMItemList.NuclearaxisFluctuation.set(initItem01("nuclear axis fluctuation", 357));
            GTCMItemList.StrangeFluctuations.set(initItem01("Strange fluctuations", 358));
            GTCMItemList.PulseCopper.set(initItem01("Pulse Copper", 359));
            GTCMItemList.DarkMatterCrystallization.set(initItem01("Dark Matter Crystallization", 360));
            GTCMItemList.QuantumCore.set(initItem01("Quantum Core", 361));
            GTCMItemList.PhotonFlow.set(initItem01("Photon flow", 362));
            GTCMItemList.NuclearBelt.set(initItem01("nuclear belt", 363));
            GTCMItemList.LifeGuide.set(initItem01("Life Guide", 364));
            GTCMItemList.QuantizedSuperstringStructureI.set(initItem01("Quantized Superstring Structure I", 365));
            GTCMItemList.EmptyHeart.set(initItem01("empty heart", 366));
            GTCMItemList.StarCoreBelt.set(initItem01("Star Core Belt", 367));
            GTCMItemList.Space_TimeSpiral.set(initItem01("Space-time Spiral", 368));
            GTCMItemList.MagneticSpinIV.set(initItem01("Magnetic Spin IV", 369));
            GTCMItemList.NuclearFluctuation.set(initItem01("Nuclear Fluctuation", 370));
            GTCMItemList.CelestialResonanceCrystal.set(initItem01("Celestial Resonance Crystal", 371));
            GTCMItemList.LowDensityDarkMatterPolymerI.set(initItem01("Low Density Dark Matter Polymer I", 372));
            GTCMItemList.QuantumCore.set(initItem01("Quantum Core", 373));
            GTCMItemList.SuperdimensionalLife.set(initItem01("Superdimensional life", 374));
            GTCMItemList.GravityFluctuation.set(initItem01("Gravity Fluctuation", 375));
            GTCMItemList.LightSpiral.set(initItem01("Light Spiral", 376));
            GTCMItemList.NuclearaxisBelt.set(initItem01("nuclear axis belt", 377));
            GTCMItemList.SuperconductingNetwork.set(initItem01("Superconducting Network", 378));
            GTCMItemList.Nanolife.set(initItem01("Nanolife", 379));
            GTCMItemList.CoreOfAncientCreation.set(initItem01("Core of Ancient Creation", 380));
            GTCMItemList.QuantumHeart.set(initItem01("Quantum Heart", 381));
            GTCMItemList.FluctuatingLife.set(initItem01("fluctuating life", 382));
            GTCMItemList.PioneerRemains.set(initItem01("Pioneer Remains", 383));
            GTCMItemList.LifeIsEmpty.set(initItem01("Life is empty", 384));
            GTCMItemList.SuperstringStructureV.set(initItem01("Superstring structure V", 385));
            GTCMItemList.SuperdimensionalFluctuations.set(initItem01("Superdimensional fluctuations", 386));
            GTCMItemList.CreationsFromTheOuterUniverse.set(initItem01("Creations from the Outer Universe", 387));
            GTCMItemList.MagneticVortex.set(initItem01("Magnetic Vortex", 388));
            GTCMItemList.Space_TimeCore.set(initItem01("Space-time core", 389));
            GTCMItemList.SubspaceHeart.set(initItem01("Subspace Heart", 390));
            GTCMItemList.CosmicExpansionEffectFluctuations.set(initItem01("cosmic expansion effect fluctuations", 391));
            GTCMItemList.StarCrystalIV.set(initItem01("Star Crystal IV", 392));
            GTCMItemList.InfiniteRecursiveNet.set(initItem01("Infinite Recursive Net", 393));
            GTCMItemList.SuperconductingLifeWaves.set(initItem01("Superconducting Life Waves", 394));
            GTCMItemList.GravitationalHeart.set(initItem01("gravitational heart", 395));
            GTCMItemList.CelestialResonanceCrystalSpiral.set(initItem01("Celestial Resonance Crystal Spiral", 396));
            GTCMItemList.NuclearaxisCore.set(initItem01("nuclear axis core", 397));
            GTCMItemList.VoidFluctuation.set(initItem01("Void Fluctuation", 398));
            GTCMItemList.AncientCreationFluctuation.set(initItem01("Ancient Creation Fluctuation", 399));
            GTCMItemList.InfiniteRecursiveHeart.set(initItem01("Infinite recursive heart", 400));
            //GTCMItemList.AlienStarCore.set(initItem01("Alien Star Core", 401));
            GTCMItemList.SpiralSpiral.set(initItem01("spiral spiral", 402));
            GTCMItemList.MagneticSpinLife.set(initItem01("Magnetic Spin Life", 403));
            GTCMItemList.LightWaves.set(initItem01("Light Waves", 404));
            GTCMItemList.NuclearSpiral.set(initItem01("nuclear spiral", 405));
            GTCMItemList.CosmicExpansionEffectCore.set(initItem01("Cosmic expansion effect core", 406));
            GTCMItemList.GravityLife.set(initItem01("Gravity Life", 407));
            GTCMItemList.CelestialResonanceCrystalNetwork.set(initItem01("Celestial Resonance Crystal Network", 408));
            GTCMItemList.LifeInTimeandSpace.set(initItem01("Life in Time and Space", 409));
            GTCMItemList.CoreAxisLifeHeart.set(initItem01("Core Axis Life Heart", 410));
            GTCMItemList.AlienStarCore.set(initItem01("Alien Star Core", 411));
            GTCMItemList.NanoLifeHeart.set(initItem01("Nano Life Heart", 412));
            GTCMItemList.AncientCreationLife.set(initItem01("Ancient Creation Life", 413));
            GTCMItemList.InfiniteRecursiveKernel.set(initItem01("infinite recursive kernel", 414));
            GTCMItemList.SuperconductingLifeZone.set(initItem01("Superconducting Life Zone", 415));
            GTCMItemList.GravityLifeFluctuation.set(initItem01("Gravity Life Fluctuation", 416));
            GTCMItemList.PioneerRemainsLifeCore.set(initItem01("Pioneer Remains Life Core", 417));
            GTCMItemList.SubspaceFluctuation.set(initItem01("Subspace Fluctuation", 418));
            GTCMItemList.Space_TimeLifeCore.set(initItem01("Space-time life core", 419));
        }

        GTCMItemList.Rune_of_Vigilance.set(initItemRune("Rune of Vigilance", 0, new String[]{ TextHandler.texter("Vigilance.", "tooltips.Rune_of_Vigilance.line1")}));
        GTCMItemList.Rune_of_Erelong.set(initItemRune("Rune of Erelong", 1, new String[]{ TextHandler.texter("Erelong.", "tooltips.Rune_of_Erelong.line1")}));
        GTCMItemList.Rune_of_Ether.set(initItemRune("Rune of Ether", 2, new String[]{ TextHandler.texter("Ether.", "tooltips.Rune_of_Ether.line1")}));
        GTCMItemList.Rune_of_Perdition.set(initItemRune("Rune of Perdition", 3, new String[]{ TextHandler.texter("Perdition.", "tooltips.Rune_of_Perdition.line1")}));

        GTCMItemList.ProofOfHeroes.set(new ItemStack(BasicItems.ProofOfHeroes, 1));
        GTCMItemList.PowerChair.set(new ItemStack(BasicItems.PowerChair, 1));
    }

    // spotless:on
    public static void registry() {
        registryItems();
        registryItemContainers();
    }
}
