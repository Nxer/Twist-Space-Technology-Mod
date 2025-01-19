package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.TestItem0;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static com.dreammaster.item.ItemList.HeavyDutyPlateTier8;
import static com.dreammaster.item.ItemList.StonePlate;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellTierKey;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;

import bartworks.system.material.WerkstoffLoader;
import galaxyspace.core.register.GSBlocks;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import singulariteam.eternalsingularity.item.EternalSingularityItem;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class SingleBlockRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        BlockRecipesTT();
        BlockRecipesGT();
        BlockRecipesTST();
    }

    // spotless:off
    void BlockRecipesTT() {
        // Advanced Molecular Casing
        // blockCasingsTT 5
        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Containment.get(2),
                MaterialsAlloy.PIKYONIUM.getPlateDense(6),
                GTOreDictUnificator.get(OrePrefixes.foil,Materials.Americium,18),

                MaterialsAlloy.BOTMIUM.getScrew(36),
                MaterialsAlloy.BOTMIUM.getRing(36),
                ItemList.Field_Generator_UV.get(1),

                Materials.Neutronium.getNanite(1))
            .fluidInputs(WerkstoffLoader.Californium.getMolten(18 * 144))
            .itemOutputs(CustomItemList.eM_Containment_Advanced.get(1))

            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 80)
            .addTo(assemblerRecipes);

        // Containment Field Generator
        // blockCasingsTT 6
        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Hollow.get(4),
                ItemList.Field_Generator_UHV.get(8),
                ItemList.Field_Generator_UV.get(16),

                ItemList.Field_Generator_ZPM.get(64),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(16),
                CustomItemList.eM_Power.get(4),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 6),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2),
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 32))
            .itemOutputs(CustomItemList.eM_Containment_Field.get(4))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 60)
            .addTo(assemblerRecipes);

        // Spacetime Altering Casing
        // blockCasingsTT 9
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            MaterialsUEVplus.SpaceTime.getIngots(1),
            1_024_000,
            512,
            (int) RECIPE_UEV,
            16,
            new Object[] {
                CustomItemList.eM_Hollow.get(8),
                CustomItemList.eM_Coil.get(4),
                ItemList.Casing_Dim_Bridge.get(2),
                GTCMItemList.StabilisationFieldGeneratorUEV.get(1),

                ItemList.Field_Generator_UEV.get(12),
                GTCMItemList.GravitationalLens.get(9),
                GTCMItemList.SpaceWarper.get(6),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 3, 14),

                ItemRefer.HiC_T5.get(12),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 24),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 36),
                HighEnergyFlowCircuit.get(48),

                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 4),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getGear(9),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Thulium, 36),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsTST.NeutroniumAlloy, 36)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(512 * 144),
                MaterialsUEVplus.Protomatter.getFluid(1000 * 16),
                GGMaterial.metastableOganesson.getMolten(144 * 32),
                MaterialsUEVplus.MoltenProtoHalkoniteBase.getFluid(144 * 8)},
            CustomItemList.eM_Spacetime.get(1),
            20 * 512,
            (int) RECIPE_UHV);

        // Teleportation Casing
        // blockCasingsTT 10
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_Dimensional.get(1),
            65_536_000,
            8192,
            (int) RECIPE_UMV,
            16,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt,MaterialsUEVplus.Creon,16),
                ItemList.Casing_Dim_Bridge.get(4),
                GregtechItemList.SpaceTimeBendingCore.get(3),
                CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(1),

                GGMaterial.metastableOganesson.get(OrePrefixes.gearGt,32),
                GTCMItemList.SpaceWarper.get(16),
                GTCMItemList.ParticleTrapTimeSpaceShield.get(8),
                ItemList.Emitter_UMV.get(6),

                GTCMItemList.UltimateEnergyFlowCircuit.get(3),
                ItemList.Wireless_Dynamo_Energy_UMV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsTST.AxonisAlloy, 6),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.SpaceTime, 6),

                ItemList.EnergisedTesseract.get(64),
                ItemList.EnergisedTesseract.get(64),
                ItemList.EnergisedTesseract.get(64),
                ItemList.EnergisedTesseract.get(64)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.SuperconductorUIVBase.getMolten(144 * 256),
                new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 1000 * 16),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000)},
            CustomItemList.eM_Teleportation.get(1),
            20 * 120,
            (int) RECIPE_UMV);

        // Dimensional Bridge Generator
        // blockCasingsTT 11
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(GSBlocks.DysonSwarmBlocks,1,1),
            65_536_000,
            8192,
            (int) RECIPE_UMV,
            16,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt,MaterialsTST.NeutroniumAlloy,16),
                ItemList.Casing_Dim_Injector.get(4),
                GregtechItemList.SpaceTimeContinuumRipper.get(3),
                CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(1),

                GGMaterial.metastableOganesson.get(OrePrefixes.gearGt,32),
                GTCMItemList.EnergyFluctuationSelfHarmonizer.get(16),
                GTCMItemList.OpticalSOC.get(8),
                ItemList.Emitter_UMV.get(6),

                GTCMItemList.UltimateEnergyFlowCircuit.get(3),
                ItemList.Wireless_Hatch_Energy_UMV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsTST.AxonisAlloy, 6),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.SpaceTime, 6),

                ItemList.Tesseract.get(64),
                ItemList.Tesseract.get(64),
                ItemList.Tesseract.get(64),
                ItemList.Tesseract.get(64)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.SuperconductorUIVBase.getMolten(144 * 256),
                new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 1000 * 16),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000)},
            CustomItemList.eM_Dimensional.get(1),
            20 * 120,
            (int) RECIPE_UMV);

        // Ultimate Molecular Casing
        // blockCasingsTT 12
        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Containment_Advanced.get(4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 6),
                MaterialsElements.STANDALONE.HYPOGEN.getFoil(24),

                GTOreDictUnificator.get(OrePrefixes.screw, MaterialsTST.NeutroniumAlloy, 48),
                GTOreDictUnificator.get(OrePrefixes.ring, MaterialsTST.NeutroniumAlloy, 48),
                ItemList.Field_Generator_UEV.get(1),

                MaterialsUEVplus.TranscendentMetal.getNanite(1),
                GTCMItemList.StellarConstructionFrameMaterial.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 64))
            .itemOutputs(CustomItemList.eM_Ultimate_Containment.get(1))

            .noOptimize()
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * 160)
            .addTo(assemblerRecipes);

        // Ultimate Advanced Molecular Casing
        // blockCasingsTT 13
        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Ultimate_Containment.get(8),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.Eternity, 6),
                GTOreDictUnificator.get(OrePrefixes.foil, MaterialsTST.AxonisAlloy, 30),

                GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.BlackDwarfMatter, 60),
                GTOreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.WhiteDwarfMatter, 60),
                ItemList.Field_Generator_UMV.get(1),

                MaterialsUEVplus.SixPhasedCopper.getNanite(1),
                GTCMItemList.StellarConstructionFrameMaterial.get(1),
                ItemList.Timepiece.get(2))
            .fluidInputs(GGMaterial.metastableOganesson.getMolten(144 * 64))
            .itemOutputs(CustomItemList.eM_Ultimate_Containment_Advanced.get(1))

            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 320)
            .addTo(assemblerRecipes);

        // Ultimate Containment Field Generator
        // blockCasingsTT 14
        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Containment_Field.get(4),
                ItemList.Field_Generator_UIV.get(16),
                ItemList.Field_Generator_UEV.get(64),

                GTCMItemList.SpaceWarper.get(4),
                new ItemStack(EternalSingularityItem.instance,1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 6),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 16),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlateDense(32),
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256))
            .itemOutputs(CustomItemList.eM_Ultimate_Containment_Field.get(1))

            .noOptimize()
            .eut(RECIPE_UIV)
            .duration(20 * 180)
            .addTo(assemblerRecipes);
    }

    void BlockRecipesGT() {
        // Containment Field casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                ItemList.Field_Generator_LuV.get(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 8))
            .fluidInputs(Materials.NaquadahAlloy.getMolten(144 * 4))
            .itemOutputs(GTModHandler.getModItem("gregtech", "gt.blockcasings2", 1, 8))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 30)
            .addTo(assemblerRecipes);
    }

    void BlockRecipesTST() {

        ItemStack FarmGear;
        ItemStack FarmOutput;
        ItemStack FarmPump;
        ItemStack FarmController;
        if (Forestry.isModLoaded()) {
            FarmGear = GTModHandler.getModItem(Forestry.ID, "ffarm", 1, 2);
            FarmOutput = GTModHandler.getModItem(Forestry.ID, "ffarm", 1, 3);
            FarmPump = GTModHandler.getModItem(Forestry.ID, "ffarm", 1, 4);
            FarmController = GTModHandler.getModItem(Forestry.ID, "ffarm", 1, 5);
        } else {
            FarmGear = new ItemStack(Blocks.stonebrick, 1);
            FarmOutput = new ItemStack(Blocks.stonebrick, 1);
            FarmPump = new ItemStack(Blocks.stonebrick, 1);
            FarmController = new ItemStack(Blocks.stonebrick, 1);
        }

        // Casing Stone Brick
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Palladium, 1),
                StonePlate.getIS(6))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("concrete"), 36000))
            .itemOutputs(GTCMItemList.ReinforcedStoneBrickCasing.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 12)
            .addTo(assemblerRecipes);

        // Casing Stone Brick Advanced
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.ReinforcedStoneBrickCasing.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Adamantium, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 6))
            .fluidInputs(MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(1728))
            .itemOutputs(GTCMItemList.ReinforcedBedrockCasing.get(1))
            .eut(RECIPE_ZPM)
            .duration(20 * 30)
            .addTo(assemblerRecipes);

        // Casing Farm
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.ReinforcedStoneBrickCasing.get(1),
            1_000_000,
            512,
            2_000_000,
            4,
            new Object[] {
                GTCMItemList.ReinforcedStoneBrickCasing.get(1),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Polybenzimidazole, 4),
                GTOreDictUnificator.get(OrePrefixes.pipeRestrictiveHuge, Materials.BlackPlutonium, 4),
                ItemList.Casing_Vent.get(1),

                FarmGear,
                FarmOutput,
                FarmPump,
                FarmController,

                GGMaterial.marCeM200.get(OrePrefixes.gearGt, 4), ItemList.Electric_Piston_UV.get(2),
                ItemList.Electric_Pump_UV.get(2), ItemRefer.HiC_T3.get(4),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 18) },
            new FluidStack[] {
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 16000),
                MaterialsAlloy.ARCANITE.getFluidStack(864),
                MaterialsAlloy.TITANSTEEL.getFluidStack(144) },
            GTCMItemList.CompositeFarmCasing.get(1),
            20 * 60,
            (int) RECIPE_UV);

        // Casing Clean
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.get(1),
            2_000_000,
            512,
            2_000_000,
            16,
            new Object[] { GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.get(1),
                ItemList.Casing_Coil_Superconductor.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SterlingSilver, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 4),

                ItemList.Circuit_Parts_Chip_Bioware.get(8),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.plateDouble, 6),
                ItemList.neutroniumHeatCapacitor.get(1) },
            new FluidStack[] {
                Materials.Grade8PurifiedWater.getFluid(8000),
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 64000) },
            GTCMItemList.AsepticGreenhouseCasing.get(1),
            20 * 240,
            (int) RECIPE_UHV);

        // Gore Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BloodInfusedIron, 1),
                new ItemStack(WayofTime.alchemicalWizardry.ModItems.blankSlate, 6),
                GTUtility.getIntegratedCircuit(1))
            .fluidInputs(BloodMagicHelper.getLifeEssence(1000))
            .itemOutputs(GTCMItemList.BloodyCasing1.get(1))
            .eut(0)
            .duration(20 * 18)
            .metadata(BloodyHellTierKey.INSTANCE, 2)
            .addTo(GTCMRecipe.BloodyHellRecipes);

        // Ichor Draconic Block
        GTValues.RA.stdBuilder()
            .itemInputs(
                Mods.DraconicEvolution.isModLoaded()
                    ? GTModHandler.getModItem(Mods.DraconicEvolution.ID, "draconicBlock", 1)
                    : TestItem0.get(1),
                GTUtility.getIntegratedCircuit(1))
            .fluidInputs(BloodMagicHelper.getLifeEssence(100000))
            .itemOutputs(GTCMItemList.BloodyCasing2.get(1))
            .eut(0)
            .duration(20 * 300)
            .metadata(BloodyHellTierKey.INSTANCE, 5)
            .addTo(GTCMRecipe.BloodyHellRecipes);

        // Stabilisation Field Generator Framework
        ItemStack GravitationEngine = Mods.GraviSuite.isModLoaded() ?
            GTModHandler.getModItem(Mods.GraviSuite.ID, "itemSimpleItem>", 64,3):
            TestItem0.get(1).setStackDisplayName(Mods.GraviSuite.ID + ":Gravitation Engine");

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_Hollow.get(1),
            8_192_000,
            1024,
                (int) RECIPE_UHV,
            4,
            new Object[] {
                CustomItemList.eM_Hollow.get(32),
                ItemList.Casing_Coil_Superconductor.get(16),
                CustomItemList.eM_Containment_Field.get(8),
                GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore10", 4),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 32),
                ItemRefer.HiC_T5.get(16),
                Materials.Neutronium.getNanite(16),
                Materials.Silver.getNanite(4),

                HeavyDutyPlateTier8.getIS(64),
                HeavyDutyPlateTier8.getIS(64),
                HeavyDutyPlateTier8.getIS(64),
                HeavyDutyPlateTier8.getIS(64),

                GravitationEngine,
                GravitationEngine,
                GravitationEngine,
                GravitationEngine},
            new FluidStack[] {
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 64),
                Materials.SuperconductorUV.getMolten(144 * 512),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 256),
                Materials.StellarAlloy.getMolten(144 * 256)},
            GTCMItemList.StabilisationFieldGeneratorFramework.get(1),
            20 * 60,
            (int) RECIPE_UHV);

        // Stabilisation Field Generator UEV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.StabilisationFieldGeneratorFramework.get(1),
            32_768_000,
            2048,
            (int) RECIPE_UEV,
            8,
            new Object[] {
                GTCMItemList.StabilisationFieldGeneratorFramework.get(1),
                CustomItemList.eM_Containment.get(1),
                GTCMItemList.PhotonControllerUpgradeUEV.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16,Materials.SuperconductorUHV,64),

                GTCMItemList.SpaceWarper.get(1),
                GTCMItemList.StellarConstructionFrameMaterial.get(8),
                Laser_Lens_Special.get(4),
                new ItemStack(EternalSingularityItem.instance,4),

                ItemList.Field_Generator_UEV.get(8),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UEV,8),
                HighEnergyFlowCircuit.get(64),
                ItemList.ZPM3.get(1),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,Materials.DraconiumAwakened,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,Materials.Bedrockium,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,Materials.Neutronium,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,Materials.CosmicNeutronium,64),
            },
            new FluidStack[] {
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 128),
                MaterialsUEVplus.ExcitedDTPC.getFluid(1000 * 72),
                GGMaterial.tairitsu.getMolten(144 * 128),
                Materials.UUMatter.getFluid(1000 * 100)},
            GTCMItemList.StabilisationFieldGeneratorUEV.get(1),
            20 * 60 * 4,
            (int) RECIPE_UEV);

        // Stabilisation Field Generator UIV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.StabilisationFieldGeneratorUEV.get(1),
            131_072_000,
            4096,
            (int) RECIPE_UIV,
            16,
            new Object[] {
                GTCMItemList.StabilisationFieldGeneratorUEV.get(1),
                CustomItemList.eM_Containment_Advanced.get(1),
                GTCMItemList.PhotonControllerUpgradeUIV.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16,Materials.SuperconductorUEV,64),

                GTCMItemList.SpaceWarper.get(4),
                GTCMItemList.StellarConstructionFrameMaterial.get(16),
                GTCMItemList.OpticalSOC.get(4),
                GTCMItemList.CriticalPhoton.get(64),

                ItemList.Field_Generator_UIV.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UIV,16),
                GTCMItemList.UltimateEnergyFlowCircuit.get(16),
                ItemList.ZPM4.get(1),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.TranscendentMetal,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.Creon,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.Mellion,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,Materials.Infinity,64),
            },
            new FluidStack[] {
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 192),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(1000 * 144),
                MaterialsUEVplus.PhononMedium.getFluid(1000 * 64),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(200)},
            GTCMItemList.StabilisationFieldGeneratorUIV.get(1),
            20 * 60 * 16,
            (int) RECIPE_UIV);

        // Stabilisation Field Generator UMV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.StabilisationFieldGeneratorUIV.get(1),
            524_288_000,
            8192,
            (int) RECIPE_UMV,
            32,
            new Object[] {
                GTCMItemList.StabilisationFieldGeneratorUIV.get(1),
                CustomItemList.eM_Ultimate_Containment.get(1),
                GTCMItemList.PhotonControllerUpgradeUMV.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16,Materials.SuperconductorUIV,64),

                GTCMItemList.SpaceWarper.get(16),
                GTCMItemList.StellarConstructionFrameMaterial.get(32),
                ItemList.Transdimensional_Alignment_Matrix.get(4),
                GTCMItemList.EnergyFluctuationSelfHarmonizer.get(64),

                ItemList.Field_Generator_UMV.get(32),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UMV,32),
                GTCMItemList.UltimateEnergyFlowCircuit.get(32),
                ItemList.ZPM5.get(1),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.SixPhasedCopper,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.WhiteDwarfMatter,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.BlackDwarfMatter,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.ProtoHalkonite,64),
            },
            new FluidStack[] {
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 256),
                Materials.Infinity.getPlasma(1000 * 216),
                MaterialsUEVplus.PhononMedium.getFluid(1000 * 256),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(200000)},
            GTCMItemList.StabilisationFieldGeneratorUMV.get(1),
            20 * 60 * 64,
            (int) RECIPE_UMV);

        // Stabilisation Field Generator UXV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.StabilisationFieldGeneratorUMV.get(1),
            2_097_152_000,
            16384,
            (int) RECIPE_UXV,
            64,
            new Object[] {
                GTCMItemList.StabilisationFieldGeneratorUMV.get(1),
                CustomItemList.eM_Ultimate_Containment_Advanced.get(1),
                GTCMItemList.PhotonControllerUpgradeUXV.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16,Materials.SuperconductorUMV,64),

                GTCMItemList.SpaceWarper.get(64),
                GTCMItemList.StellarConstructionFrameMaterial.get(64),
                CustomItemList.astralArrayFabricator.get(4),
                GTCMItemList.AnnihilationConstrainer.get(64),

                ItemList.Field_Generator_UXV.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UXV,64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                ItemList.ZPM6.get(1),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.MagMatter,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsTST.Axonium,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsUEVplus.SpaceTime,64),
            },
            new FluidStack[] {
                new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 320),
                MaterialsUEVplus.QuarkGluonPlasma.getFluid(1000 * 288),
                MaterialsUEVplus.PhononMedium.getFluid(1000 * 1024),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(200000000)},
            GTCMItemList.StabilisationFieldGeneratorUXV.get(1),
            20 * 60 * 256,
            (int) RECIPE_UXV);
    }
    // spotless:on
}
