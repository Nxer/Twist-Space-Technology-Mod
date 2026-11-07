package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosCrafting;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialRoutes;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.github.bsideup.jabel.Desugar;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemVolumetricFlask;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import tectech.thing.CustomItemList;
import tectech.thing.block.BlockGodforgeGlass;

/** One ingredient definition drives the injection workbench and its NEI recipes. */
public final class BottledChaosCredentialRecipes {

    private static final List<InjectionRecipe> RECIPES = new ArrayList<>();

    public static void registerRecipes() {
        RECIPES.clear();
        // Ingredient tiers follow the family and material progression; processing voltage is registered separately.
        // Finished line products are preferred; selected intermediates can supplement them.
        // spotless:off
        if (CredentialModel.indexForTag("honeycomb") >= 0) {
            register("honeycomb", Stage.UV, ItemList.CentrifugeUV.get(4), ItemList.Casing_CleanStainlessSteel.get(8), ItemList.Component_Filter.get(8), GTUtility.copyAmountUnsafe(16,
                CredentialRoutes.get(CredentialModel.indexForTag("honeycomb")).rootStartStack()));
        }
        register("titanium", Stage.ZPM, ItemList.Casing_Coil_NaquadahAlloy.get(8), GregtechItemList.Casing_Vacuum_Furnace.get(8), ItemList.UHTResistantMesh.get(8), Materials.Titanium.getIngots(64));
        register("tungsten", Stage.ZPM, ItemList.Casing_Coil_NaquadahAlloy.get(8), GregtechItemList.Casing_Adv_BlastFurnace.get(8), ItemList.UHTResistantMesh.get(8), Materials.Tungsten.getIngots(64));
        register("indium", Stage.ZPM, ItemList.ChemicalBathZPM.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), ItemList.ActivatedCarbonFilterMesh.get(8), Materials.Indium.getIngots(64));

        register("platinum_metallic_powder", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), GGMaterial.preciousMetalAlloy.getPart(OrePrefixes.gearGt, 8), Materials.TPV.getPart(OrePrefixes.plateDense, 8), Materials.Platinum.getDust(64));
        register("palladium_metallic_powder", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), GGMaterial.preciousMetalAlloy.getPart(OrePrefixes.gearGt, 8), WerkstoffLoader.RhodiumPlatedPalladium.getPart(OrePrefixes.plateDense, 8), Materials.Palladium.getDust(64));
        register("platinum_residue", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), GGMaterial.preciousMetalAlloy.getPart(OrePrefixes.gearGt, 8), WerkstoffLoader.Ruridit.getPart(OrePrefixes.plateDense, 8), BottledChaosRecipeResources.RhodiumDust.getItemStack(64), BottledChaosRecipeResources.RutheniumDust.getItemStack(64));
        register("crude_rhodium_metal", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), GGMaterial.preciousMetalAlloy.getPart(OrePrefixes.gearGt, 8), WerkstoffLoader.RhodiumPlatedPalladium.getPart(OrePrefixes.plateDense, 8), BottledChaosRecipeResources.RhodiumDust.getItemStack(64));
        register("leach_residue", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), GGMaterial.preciousMetalAlloy.getPart(OrePrefixes.gearGt, 8), WerkstoffLoader.Ruridit.getPart(OrePrefixes.plateDense, 8), BottledChaosRecipeResources.RutheniumDust.getItemStack(64));
        register("rarest_metal_residue", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), GGMaterial.preciousMetalAlloy.getPart(OrePrefixes.gearGt, 8), Materials.Osmiridium.getPart(OrePrefixes.plateDense, 8), Materials.Osmium.getDust(64));
        register("iridium_metal_residue", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), GGMaterial.preciousMetalAlloy.getPart(OrePrefixes.gearGt, 8), Materials.Osmiridium.getPart(OrePrefixes.plateDense, 8), Materials.Iridium.getDust(64));

        register("monazite", Stage.UV, ItemList.SiftingMachineUV.get(4), GregtechItemList.Casing_Centrifuge1.get(8), ItemList.ActivatedCarbonFilterMesh.get(8), Materials.Samarium.getIngots(64), Materials.Gadolinium.getIngots(64), Materials.Europium.getIngots(64));
        register("bastnasite", Stage.UV, ItemList.CentrifugeUV.get(4), GregtechItemList.Casing_Centrifuge1.get(8), ItemList.ActivatedCarbonFilterMesh.get(8), Materials.Lanthanum.getIngots(64), Materials.Neodymium.getIngots(64), Materials.Holmium.getIngots(64));
        register("cerium", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), ItemList.ActivatedCarbonFilterMesh.get(8), Materials.Cerium.getIngots(64));
        register("samarium", Stage.UV, ItemList.ElectrolyzerUV.get(4), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), ItemList.ActivatedCarbonFilterMesh.get(8), Materials.Samarium.getIngots(64));
        register("nano_extraction", Stage.UHV, ItemList.Casing_Pipe_Polybenzimidazole.get(8), ItemList.Field_Generator_UHV.get(2), filledContainer(WerkstoffMaterialPool.PraseodymiumExtractingNanoResin.getFluidOrGas(128_000), ItemList.Large_Fluid_Cell_Titanium.get(1)), filledContainer(WerkstoffMaterialPool.HolmiumExtractingNanoResin.getFluidOrGas(128_000), ItemList.Large_Fluid_Cell_Titanium.get(1)), Materials.Praseodymium.getIngots(64), Materials.Holmium.getIngots(64));

        register("naquadah", Stage.UV, ItemRefer.Radiation_Proof_Steel_Frame_Box.get(8), ItemList.Superconducting_Magnet_Solenoid_UV.get(8), filledContainer(GGMaterial.P507.getFluidOrGas(32_768_000), ItemList.Large_Fluid_Cell_Osmium.get(1)), Materials.Naquadah.getIngots(64), ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(8));
        register("enriched_naquadah", Stage.UHV, ItemRefer.Radiation_Proof_Steel_Frame_Box.get(8), ItemList.Superconducting_Magnet_Solenoid_UHV.get(8), filledContainer(GGMaterial.P507.getFluidOrGas(131_072_000), ItemList.Large_Fluid_Cell_Neutronium.get(1)), Materials.NaquadahEnriched.getIngots(64));
        register("naquadria", Stage.UEV, ItemRefer.Radiation_Proof_Steel_Frame_Box.get(8), ItemList.Superconducting_Magnet_Solenoid_UEV.get(8), filledContainer(GGMaterial.P507.getFluidOrGas(524_288_000), GregtechItemList.KLEIN_BOTTLE.get(1)), Materials.Naquadria.getIngots(64), ItemList.Neutron_Reflector.get(8));
        register("naquadah_fuel_preparation", Stage.UEV, ItemRefer.Naquadah_Fuel_Refinery_Casing.get(8), ItemRefer.Field_Restriction_Glass.get(8), ItemList.Neutron_Reflector.get(8), cells(GGMaterial.heavyNaquadahFuel.getFluidOrGas(1000), 64), cells(GGMaterial.lightNaquadahFuel.getFluidOrGas(1000), 64));
        register("naquadah_fuel_3", Stage.UEV, ItemRefer.Naquadah_Fuel_Refinery_Casing.get(8), ItemRefer.Field_Restriction_Coil_T1.get(8), ItemList.Electric_Pump_UEV.get(2), ItemList.Neutron_Reflector.get(8), cells(GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1000), 64));
        register("naquadah_fuel_4", Stage.UIV, ItemRefer.Naquadah_Fuel_Refinery_Casing.get(8), ItemRefer.Field_Restriction_Coil_T2.get(8), ItemList.Electric_Pump_UIV.get(2), ItemList.Neutron_Reflector.get(8), cells(GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(1000), 64));
        register("naquadah_fuel_5", Stage.UMV, ItemRefer.Naquadah_Fuel_Refinery_Casing.get(8), ItemRefer.Field_Restriction_Coil_T3.get(8), ItemList.Electric_Pump_UMV.get(2), ItemList.Neutron_Reflector.get(8), cells(GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(1000), 64));
        register("naquadah_fuel_6", Stage.UXV, ItemRefer.Naquadah_Fuel_Refinery_Casing.get(8), ItemRefer.Field_Restriction_Coil_T4.get(8), ItemList.Electric_Pump_UXV.get(2), ItemList.Neutron_Reflector.get(8), cells(GGMaterial.naquadahBasedFuelMkVI.getFluidOrGas(1000), 64));
        register("fusion", Stage.UIV, CustomItemList.Machine_Multi_Infuser.get(1), ItemRefer.Compact_Fusion_Coil_T4.get(8), Materials.Duranium.getIngots(64), MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getIngot(64), MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getIngot(64), MaterialsElements.STANDALONE.ADVANCED_NITINOL.getIngot(64));
        if (CredentialModel.indexForTag("ancient_debris") >= 0) {
            register("ancient_debris", Stage.UV, ItemList.SiftingMachineUV.get(4), ItemList.Casing_RobustTungstenSteel.get(8), BottledChaosRecipeResources.IntenselyBondedNetheriteNanoparticles.getItemStack(64), Materials.Netherite.getPart(OrePrefixes.ring, 64));
        }
        register("isamill", Stage.ZPM, ItemList.MaceratorZPM.get(4), GregtechItemList.Casing_IsaMill_Pipe.get(8), Materials.Indium.getDust(64), Materials.Lutetium.getDust(64), cells(new FluidStack(BottledChaosRecipeResources.RedMud, 1000), 64));
        register("prismatic_acid", Stage.UHV, ItemList.BlockExtremeCorrosionResistantCasing.get(8), ItemList.Casing_Pipe_Polybenzimidazole.get(8), GregtechItemList.SolidAcidCatalyst.get(8), cells(Materials.PrismaticAcid.getFluid(1000), 64));

        register("purified_water_1", Stage.ZPM, ItemList.BlockSterileWaterPlantCasing.get(8), ItemList.Casing_Pipe_Polytetrafluoroethylene.get(8), ItemList.ActivatedCarbonFilterMesh.get(8), cells(Materials.Grade1PurifiedWater.getFluid(1000), 64));
        register("purified_water_2", Stage.ZPM, ItemList.BlockHighPressureResistantCasing.get(8), ItemList.BlockOzoneCasing.get(8), cells(Materials.Ozone.getGas(1000), 8), cells(Materials.Grade2PurifiedWater.getFluid(1000), 64));
        register("purified_water_3", Stage.UV, ItemList.BlockFlocculationCasing.get(8), ItemList.BlockNaquadahReinforcedWaterPlantCasing.get(8), cells(Materials.PolyAluminiumChloride.getFluid(1000), 8), cells(Materials.Grade3PurifiedWater.getFluid(1000), 64));
        register("purified_water_4", Stage.UV, ItemList.BlockExtremeCorrosionResistantCasing.get(8), ItemList.GlassPHResistant.get(8), Materials.SodiumHydroxide.getDust(8), cells(Materials.Grade4PurifiedWater.getFluid(1000), 64));
        register("purified_water_5", Stage.UHV, ItemList.BlockPlasmaHeatingCasing.get(8), ItemList.neutroniumHeatCapacitor.get(8), ItemList.TaHfCNanofibers.get(8), cells(Materials.Grade5PurifiedWater.getFluid(1000), 64));
        register("purified_water_6", Stage.UHV, ItemList.BlockUltraVioletLaserEmitter.get(8), ItemList.GlassUVResistant.get(8), ItemList.Emitter_UHV.get(2), cells(Materials.Grade6PurifiedWater.getFluid(1000), 64));
        register("purified_water_7", Stage.UEV, ItemList.BlockNaquadriaReinforcedWaterPlantCasing.get(8), ItemList.GlassOmniPurposeInfinityFused.get(8), ItemList.Field_Generator_UEV.get(2), cells(Materials.Grade7PurifiedWater.getFluid(1000), 64));
        register("purified_water_8", Stage.UIV, ItemList.BlockQuarkContainmentCasing.get(8), ItemList.GlassQuarkContainment.get(8), ItemList.Quark_Catalyst_Housing.get(8), cells(Materials.Grade8PurifiedWater.getFluid(1000), 64), cells(Materials.StableBaryonicMatter.getFluid(1000), 64));

        register("kevlar", Stage.UHV, ItemList.Casing_Autoclave.get(16), ItemList.Casing_Pipe_Polybenzimidazole.get(16), GregtechItemList.SolidAcidCatalyst.get(16), Materials.Infinity.getPart(OrePrefixes.wireFine, 4), Materials.Carbon.getDust(64), Materials.Polybenzimidazole.getPlates(64));
        register("super_heavy_radox", Stage.UEV, ItemList.Casing_Coil_NaquadahAlloy.get(8), ItemList.Casing_Pipe_Polybenzimidazole.get(8), cells(Materials.RadoxSuperHeavy.getFluid(1000), 64), Materials.RadoxPolymer.getPart(OrePrefixes.plateDense, 64));
        register("heavy_radox", Stage.UEV, ItemList.Casing_Coil_NaquadahAlloy.get(8), ItemList.Casing_Pipe_Polybenzimidazole.get(8), cells(Materials.RadoxHeavy.getFluid(1000), 64), Materials.RadoxPolymer.getPart(OrePrefixes.plateDouble, 64));
        register("light_radox", Stage.UEV, ItemList.Casing_Coil_NaquadahAlloy.get(8), ItemList.Casing_Pipe_Polybenzimidazole.get(8), cells(Materials.RadoxLight.getGas(1000), 64), Materials.RadoxPolymer.getPlates(64));
        register("super_light_radox", Stage.UEV, ItemList.Casing_Coil_NaquadahAlloy.get(8), ItemList.Casing_Pipe_Polybenzimidazole.get(8), cells(Materials.RadoxSuperLight.getGas(1000), 64), Materials.RadoxPolymer.getPart(OrePrefixes.foil, 64));
        register("xenoxene", Stage.UEV, ItemList.CasingNaquadahReinforcedDistillation.get(8), GregtechItemList.Casing_CokeOven_Coil2.get(8), cells(Materials.Xenoxene.getFluid(1000), 64), cells(new FluidStack(BottledChaosRecipeResources.Mutagen, 1000), 64));

        register("growth_medium_sterilized", Stage.UEV, ItemList.BlockSterileWaterPlantCasing.get(8), ItemList.Casing_Autoclave.get(8), ItemList.Circuit_Parts_PetriDish.get(8), ItemList.PlasmaMembrane.get(8), cells(Materials.GrowthMediumSterilized.getFluid(1000), 64));
        register("stem_cells", Stage.UEV, ItemList.ChemicalReactorUHV.get(4), ItemList.BlockSterileWaterPlantCasing.get(8), ItemList.Circuit_Board_Wetware_Extreme.get(8), ItemList.Circuit_Chip_Stemcell.get(64));
        register("seaweed_broth", Stage.UEV, ItemList.BlockSterileWaterPlantCasing.get(8), ItemList.Casing_Chemically_Inert.get(8), BottledChaosRecipeResources.TauCetiESeaweed.getItemStack(64), cells(new FluidStack(BottledChaosRecipeResources.SeaweedBroth, 1000), 64));
        register("tau_ceti_e_seaweed_extract", Stage.UEV, ItemList.ExtractorUHV.get(4), ItemList.BlockSterileWaterPlantCasing.get(8), ItemList.PlasmaMembrane.get(8), BottledChaosRecipeResources.TauCetiESeaweedExtract.getItemStack(64));
        register("bio_medium_sterilized", Stage.UEV, ItemList.Casing_Autoclave.get(8), ItemList.GlassUVResistant.get(8), cells(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1000), 8), cells(Materials.BioMediumSterilized.getFluid(1000), 64));
        register("bio_cells", Stage.UEV, ItemList.ChemicalReactorUHV.get(4), ItemList.GlassUVResistant.get(8), ItemList.Circuit_Board_Bio_Ultra.get(8), ItemList.Circuit_Wafer_Bioware.get(8), ItemList.Circuit_Chip_Biocell.get(64));

        if (CredentialModel.indexForTag("crystal_cycle") >= 0) {
            register("crystal_cycle", Stage.LuV, ItemList.AutoclaveLuV.get(4), ItemList.Casing_Autoclave.get(8), ItemList.QuantumEye.get(8), ItemList.Circuit_Parts_RawCrystalChip.get(64));
            register("cerium_lutetium_garnet", Stage.UHV, ItemList.PrecisionLaserEngraverUHV.get(4), ItemList.GlassUVResistant.get(8), GregtechItemList.Laser_Lens_WoodsGlass.get(8), ItemList.Emitter_UHV.get(2),
                BottledChaosRecipeResources.ExquisiteCeriumLutetiumAluminiumGarnet.getItemStack(64));
            register("tesseract", Stage.UIV, ItemList.Casing_Coil_Infinity.get(8), Materials.TranscendentMetal.getPart(OrePrefixes.frameGt, 8), ItemList.Field_Generator_UIV.get(2), ItemList.NuclearStar.get(8), ItemList.EnergisedTesseract.get(64), ItemList.Tesseract.get(64));
            if (CredentialModel.indexForTag("eternity") >= 0) {
                register("eternity", Stage.UMV, GregtechItemList.Casing_Coil_QuantumForceTransformer.get(8), GregtechItemList.CosmicFabricManipulator.get(8), cells(Materials.PrimordialMatter.getFluid(1000), 8), ItemList.Field_Generator_UMV.get(2), Materials.Eternity.getIngots(64));
            }
            register("phonon_medium", Stage.UXV, CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(8), ItemList.Thermal_Superconductor.get(8), ItemList.Relativistic_Heat_Capacitor.get(8), ItemList.Naquarite_Universal_Insulator_Foil.get(8), cells(Materials.PhononMedium.getFluid(1000), 64));
        }
        register("magmatter", Stage.UXV, CustomItemList.Godforge_StellarEnergySiphonCasing.get(8), Materials.MHDCSM.getPart(OrePrefixes.frameGt, 8), cells(Materials.Time.getMolten(1000), 8), cells(Materials.Space.getMolten(1000), 8), ItemList.PseudoStar.get(8), Materials.MagMatter.getDust(64));
        register("quark_gluon_plasma", Stage.UXV, new ItemStack(BlockGodforgeGlass.INSTANCE, 8), GregtechItemList.CosmicFabricManipulator.get(8), ItemList.Field_Generator_UXV.get(2), ItemList.PseudoStar.get(8), cells(Materials.QuarkGluonPlasma.getFluid(1000), 64));
        // spotless:on
        RECIPES.sort(Comparator.comparingInt(InjectionRecipe::tagIndex));
        if (RECIPES.size() != CredentialModel.ROUTE_COUNT)
            throw new IllegalStateException("Incomplete Bottled Chaos credential injection recipes");
    }

    public static List<InjectionRecipe> recipes() {
        return Collections.unmodifiableList(RECIPES);
    }

    /** Called on the workbench's detached preview inventory; merge/split and EasySkip remain in its normal path. */
    public static BottledChaosCrafting.Result inject(ItemStackHandler inventory, ItemStack target) {
        if (ItemBottledChaosCredential.getRouteMask(target) != 0) return null;
        ItemStack[] materials = new ItemStack[BottledChaosCrafting.MATERIAL_SLOT_COUNT];
        for (int slot = 0; slot < materials.length; slot++)
            materials[slot] = inventory.getStackInSlot(BottledChaosCrafting.MATERIAL_SLOT_START + slot);
        for (InjectionRecipe injection : RECIPES) {
            GTRecipe recipe = injection.ingredients();
            // Extra, unrelated inputs must not silently select one credential from several possible recipes.
            if (Arrays.stream(materials)
                .filter(Objects::nonNull)
                .anyMatch(
                    stack -> Arrays.stream(recipe.getCachedCombinedItemInputs())
                        .noneMatch(input -> input.matchesType(stack))))
                continue;
            if (!recipe.isRecipeInputEqual(false, null, materials)) continue;
            long mask = 1L << injection.tagIndex();
            int weight = ItemBottledChaosCredential.getRequiredWeight(mask);
            if (weight > ItemBottledChaosCredential.getCapacity(target)) return BottledChaosCrafting.Result
                .failure("tst.bottledchaos.gui.credential_injection.capacity_exceeded");
            ItemStack result = target.copy();
            if (!ItemBottledChaosCredential.setBinding(result, mask, weight))
                return BottledChaosCrafting.Result.failure("tst.bottledchaos.gui.credential_injection.invalid_source");
            recipe.isRecipeInputEqual(true, null, materials);
            for (int slot = 0; slot < materials.length; slot++) {
                ItemStack remaining = materials[slot];
                inventory.setStackInSlot(
                    BottledChaosCrafting.MATERIAL_SLOT_START + slot,
                    remaining == null || remaining.stackSize <= 0 ? null : remaining);
            }
            inventory.setStackInSlot(BottledChaosCrafting.CARRIER_SLOT, null);
            inventory.setStackInSlot(BottledChaosCrafting.OUTPUT_SLOT, result);
            return BottledChaosCrafting.Result.success("tst.bottledchaos.gui.credential_injection.success", result);
        }
        return null;
    }

    private static void register(String tag, Stage stage, ItemStack... materials) {
        int index = CredentialModel.indexForTag(tag);
        if (index < 0) return;
        Object[] inputs = new Object[materials.length + 2];
        if (inputs.length > BottledChaosCrafting.MATERIAL_SLOT_COUNT)
            throw new IllegalStateException("Too many credential ingredients for " + tag);
        inputs[0] = new Object[] { OrePrefixes.circuit.get(stage.circuit), 4 };
        inputs[1] = stage.sensor.get(2);
        System.arraycopy(materials, 0, inputs, 2, materials.length);
        for (Object input : inputs) {
            if (input == null || input instanceof ItemStack stack && (stack.getItem() == null || stack.stackSize < 0))
                throw new IllegalStateException("Missing credential ingredient for " + tag);
        }
        RECIPES.add(
            new InjectionRecipe(
                index,
                GTValues.RA.stdBuilder()
                    .itemInputs(inputs)
                    .nbtSensitive()
                    .duration(1)
                    .eut(0)
                    .buildWithAlt()
                    .orElseThrow(() -> new IllegalStateException("Invalid credential ingredients for " + tag))));
    }

    /** Universal cells keep all recipe inputs in the existing eight item slots. */
    private static ItemStack cells(FluidStack fluid, int amount) {
        return GTUtility.copyAmountUnsafe(amount, filledContainer(fluid, ItemList.Cell_Universal_Fluid.get(1)));
    }

    private static ItemStack filledContainer(FluidStack fluid, ItemStack container) {
        if (container.getItem() instanceof ItemVolumetricFlask flask) {
            flask.setCapacity(container, fluid.amount);
        }
        return Objects.requireNonNull(
            GTUtility.fillFluidContainer(fluid, container, false, true),
            "Missing credential fluid container");
    }

    @Desugar
    public record InjectionRecipe(int tagIndex, GTRecipe.GTRecipe_WithAlt ingredients) {}

    private enum Stage {

        // spotless:off
        LuV(Materials.LuV, ItemList.Sensor_LuV),
        ZPM(Materials.ZPM, ItemList.Sensor_ZPM),
        UV(Materials.UV, ItemList.Sensor_UV),
        UHV(Materials.UHV, ItemList.Sensor_UHV),
        UEV(Materials.UEV, ItemList.Sensor_UEV),
        UIV(Materials.UIV, ItemList.Sensor_UIV),
        UMV(Materials.UMV, ItemList.Sensor_UMV),
        UXV(Materials.UXV, ItemList.Sensor_UXV);
        // spotless:on

        private final Materials circuit;
        private final ItemList sensor;

        Stage(Materials circuit, ItemList sensor) {
            this.circuit = circuit;
            this.sensor = sensor;
        }
    }
}
