package com.Nxer.TwistSpaceTechnology.common.item.items;

import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdder01.initItem01;
import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdderIzumik.initItemIzumik;
import static com.Nxer.TwistSpaceTechnology.common.item.itemAdders.ItemAdderRune.initItemRune;
import static com.Nxer.TwistSpaceTechnology.common.item.items.BasicItems.MultiStructuresLinkTool;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.RiseOfDarkFog;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
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
        Item[] itemsToReg = { BasicItems.MetaItem01, BasicItems.ProofOfHeroes, BasicItems.ProofOfGods,
            MultiStructuresLinkTool,

            BasicItems.MetaItemRune, BasicItems.MetaItemIzumik, BasicItems.PowerChair, BasicItems.HatchUpdateTool,
            BasicItems.Yamato };

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
        GTCMItemList.PurpleMagnoliaPetal.set(initItem01("Purple Magnolia Petal", 19, new String[]{TextHandler.texter("Petals falling from Alfheim...", "tooltips.PurpleMagnoliaPetal.line1")}));
        GTCMItemList.PurpleMagnoliaSapling.set(initItem01("Purple Magnolia Sapling", 20, new String[]{TextHandler.texter("Not plantable. Need to be on ic2 crop sticks.", "tooltips.PurpleMagnoliaSapling.line1")}));
        GTCMItemList.VoidPollen.set(initItem01("Void Pollen", 21, new String[]{TextHandler.texter("Pollen yet to be arisen.", "tooltips.VoidPollen.line1")}));
        GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.set(initItem01("Primitive Man's SpaceTime Distortion Device", 22, new String[]{TextHandler.texter("Anyway...", "tooltips.PrimitiveMansSpaceTimeDistortionDevice.line1")}));
        GTCMItemList.WirelessUpdateItem.set(initItem01("Wireless Computation update circuit", 23, new String[]{}));
        GTCMItemList.BallLightningUpgradeChip.set(initItem01("Ball Lightning Upgrade Chip", 24, new String[]{TextHandler.texter("Power, give me, more power!", "tooltips.ItemBallLightningUpgradeChip.line1")}));

        // #tr EnergyShard.tooltips.01
        // # A piece of pure energy, from dark...
        // #zh_CN 一片纯净的能量, 来自黑暗的...
        GTCMItemList.EnergyShard.set(initItem01("Energy Shard", 25, new String[]{ tr("EnergyShard.tooltips.01"), DSPName, RiseOfDarkFog.getText() }));

        // #tr SiliconBasedNeuron.tooltips.01
        // # Very... uh, natural.
        // #zh_CN 非常的... 呃, 自然.
        GTCMItemList.SiliconBasedNeuron.set(initItem01("Silicon-based Neuron", 26, new String[]{ tr("SiliconBasedNeuron.tooltips.01"), DSPName, RiseOfDarkFog.getText() }));

        // #tr MatterRecombinator.tooltips.01
        // # The fundamental unit of material manipulation at the scale of elementary particles.
        // #zh_CN 基本粒子尺度上物质操作的基本单元.
        GTCMItemList.MatterRecombinator.set(initItem01("Matter Recombinator", 27, new String[]{ tr("MatterRecombinator.tooltips.01"), DSPName, RiseOfDarkFog.getText() }));

        // #tr CoreElement.tooltips.01
        // # Adding core elements to the singularization reaction of strange matter
        // #zh_CN 在奇异物质的奇异化反应中加入核心素,
        // #tr CoreElement.tooltips.02
        // # can slow down the singularization reaction
        // #zh_CN 可以慢化奇异化反应,
        // #tr CoreElement.tooltips.03
        // # and make it proceed in an orderly manner.
        // #zh_CN 使其有序进行.
        GTCMItemList.CoreElement.set(initItem01("Core Element", 28, new String[]{ tr("CoreElement.tooltips.01"), tr("CoreElement.tooltips.02"), tr("CoreElement.tooltips.03"), DSPName, RiseOfDarkFog.getText() }));

        // #tr StrangeAnnihilationFuelRod.tooltips.01
        // # Using strange matter as the main annihilation reaction raw material,
        // #zh_CN 使用了奇异物质为主要的湮灭反应原料,
        // #tr StrangeAnnihilationFuelRod.tooltips.02
        // # the energy density and output power are greatly improved.
        // #zh_CN 能量密度和输出功率均大幅提高.
        GTCMItemList.StrangeAnnihilationFuelRod.set(initItem01("Strange Annihilation Fuel Rod", 29, new String[]{ tr("StrangeAnnihilationFuelRod.tooltips.01"), tr("StrangeAnnihilationFuelRod.tooltips.02"), DSPName, RiseOfDarkFog.getText() }));

        GTCMItemList.SpaceTimeSuperconductingInlaidMotherboard.set(initItem01("SpaceTime Superconducting Inlaid Motherboard", 30));
        GTCMItemList.PacketInformationTranslationArray.set(initItem01("Packet Information Translation Array", 31));
        GTCMItemList.InformationHorizonInterventionShell.set(initItem01("Information Horizon Intervention Shell", 32));
        GTCMItemList.EnergyFluctuationSelfHarmonizer.set(initItem01("Energy Fluctuation Self-Harmonizer", 33));
        GTCMItemList.EncapsulatedMicroSpaceTimeUnit.set(initItem01("Encapsulated Micro SpaceTime Unit", 34));
        GTCMItemList.SeedsSpaceTime.set(initItem01("Seeds of Space and Time", 35));
        GTCMItemList.WhiteDwarfMold_Ingot.set(initItem01("White Dwarf Mold(Ingot)", 36));
        // #tr MetaItem01.36.name
        // # White Dwarf Mold(Ingot)
        // #zh_CN 白矮星模具(锭)
        if (Config.activateMegaSpaceStation) {
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
            // GTCMItemList.StarCore.set(initItem01("Star Core", 321));
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
            GTCMItemList.AlienStarCore.set(initItem01("Alien Star Core", 401));
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

        // Endgame Challenge content
        GTCMItemList.LvFlask.set(initItem01("LV FLASK", 420));
        GTCMItemList.MvFlask.set(initItem01("MV FLASK", 421));
        GTCMItemList.HvFlask.set(initItem01("HV FLASK", 422));
        GTCMItemList.EvFlask.set(initItem01("EV FLASK", 423));
        GTCMItemList.IvFlask.set(initItem01("IV FLASK", 424));
        GTCMItemList.LuvFlask.set(initItem01("LUV FLASK", 425));
        GTCMItemList.ZpmFlask.set(initItem01("ZPM FLASK", 426));
        GTCMItemList.UvFlask.set(initItem01("UV FLASK", 427));
        GTCMItemList.UhvFlask.set(initItem01("UHV FLASK", 428));
        GTCMItemList.UevFlask.set(initItem01("UEV FLASK", 429));
        GTCMItemList.UivFlask.set(initItem01("UIV FLASK", 430));
        GTCMItemList.UmvFlask.set(initItem01("UMV FLASK", 431));
        GTCMItemList.UxvFlask.set(initItem01("UXV FLASK", 432));

        GTCMItemList.Rune_of_Vigilance.set(initItemRune("Rune of Vigilance", 0, new String[]{TextHandler.texter("Vigilance.", "tooltips.Rune_of_Vigilance.line1")}));
        GTCMItemList.Rune_of_Erelong.set(initItemRune("Rune of Erelong", 1, new String[]{TextHandler.texter("Erelong.", "tooltips.Rune_of_Erelong.line1")}));
        GTCMItemList.Rune_of_Ether.set(initItemRune("Rune of Ether", 2, new String[]{TextHandler.texter("Ether.", "tooltips.Rune_of_Ether.line1")}));
        GTCMItemList.Rune_of_Perdition.set(initItemRune("Rune of Perdition", 3, new String[]{TextHandler.texter("Perdition.", "tooltips.Rune_of_Perdition.line1")}));

        GTCMItemList.FountOfEcology.set(initItemIzumik(
            "Fount Of Ecology"
            ,0,
            // #tr MetaItemIzumik.0.name
            // # {\BLUE}{\BOLD}Fount Of Ecology
            // #zh_CN {\BLUE}{\BOLD}生态泉源
            new String[]{tr("FountOfEcology.tooltips.01"), tr("FountOfEcology.tooltips.02")},
            // #tr FountOfEcology.tooltips.01
            // # {\AQUA}A unique looking jellyfish
            // #zh_CN {\AQUA}一只长相奇特的水母
            // #tr FountOfEcology.tooltips.02
            // # {\AQUA}Well......
            // #zh_CN {\AQUA}等下......
            new String[]{tr("FountOfEcology.tooltips.03"), tr("FountOfEcology.tooltips.04")}
            // #tr FountOfEcology.tooltips.03
            // # {\GOLD}A perfect creature close to the singularity of evolution, The counselor and lear of The Many.
            // #zh_CN {\GOLD}临近进化奇点的完美生物 大群的建言者与引航者
            // #tr FountOfEcology.tooltips.04
            // # {\GOLD}"The Afterborn Firstborn", Seaborn
            // #zh_CN {\GOLD}"后生的出初生" 海嗣
        ));

        GTCMItemList.OffSpring.set(initItemIzumik(
            "OffSpring"
            ,1,
            // #tr MetaItemIzumik.1.name
            // # {\DARK_AQUA}"Offspring"
            // #zh_CN {\DARK_AQUA}"子代"
            new String[]{tr("Offspring.tooltips.01"), tr("Offspring.tooltips.02")},
            // #tr Offspring.tooltips.01
            // # {\AQUA}A weak little jellyfish
            // #zh_CN {\AQUA}一只弱不禁风的小水母
            // #tr Offspring.tooltips.02
            // # {\AQUA}Seems to be containing additional information
            // #zh_CN {\AQUA}似乎包含着额外的信息
            new String[]{tr("Offspring.tooltips.03"), tr("Offspring.tooltips.04")}
            // #tr Offspring.tooltips.03
            // # {\LIGHT_PURPLE}The offspring derived from Izu'mik's evolutionary branches
            // #zh_CN {\LIGHT_PURPLE}伊祖米克进化分支中衍生出的子代
            // #tr Offspring.tooltips.04
            // # {\LIGHT_PURPLE}are returning to their parent with the genetic information collected from all of The Overworld
            // #zh_CN {\LIGHT_PURPLE}正携带着从主世界各地收集到的遗传信息返回母体
        ));
        GTCMItemList.ProofOfGods.set(new ItemStack(BasicItems.ProofOfGods, 1));
        GTCMItemList.ProofOfHeroes.set(new ItemStack(BasicItems.ProofOfHeroes, 2));
        GTCMItemList.PowerChair.set(new ItemStack(BasicItems.PowerChair, 1));

        GTCMItemList.HatchUpdateTool.set(new ItemStack(BasicItems.HatchUpdateTool, 1));

        GTCMItemList.Yamato.set(new ItemStack(BasicItems.Yamato,1));

    }

       // spotless:on
    public static void registry() {
        registryItems();
        registryItemContainers();
    }
}
