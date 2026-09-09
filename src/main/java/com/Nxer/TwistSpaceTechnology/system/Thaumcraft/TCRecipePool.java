package com.Nxer.TwistSpaceTechnology.system.Thaumcraft;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.BloodArsenal;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.common.api.ThaumcraftRecipeHandler.addInfusionCraftingRecipeAspectNotNull;
import static com.Nxer.TwistSpaceTechnology.common.api.ThaumcraftRecipeHandler.getAspect;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic.EVOLUTION;
import static com.Nxer.TwistSpaceTechnology.util.TstUtils.newItemStackWithNBT;
import static com.glodblock.github.loader.ItemAndBlockHolder.INTERFACE;
import static fox.spiteful.avaritia.compat.thaumcraft.Lucrum.ULTRA_DEATH;
import static goodgenerator.loader.Loaders.huiCircuit;
import static gregtech.api.enums.ItemList.Automation_ChestBuffer_IV;
import static gregtech.api.enums.ItemList.Machine_IV_Assembler;
import static gregtech.api.enums.ItemList.TreeGrowSimulator;
import static gregtech.api.enums.TCAspects.ELECTRUM;
import static gregtech.api.enums.TCAspects.RADIO;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_AlloyBlastSmelter;
import static kubatech.api.enums.ItemList.ExtremeEntityCrusher;
import static kubatech.api.enums.ItemList.ExtremeIndustrialGreenhouse;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.config.ConfigBlocks.blockStoneDevice;
import static thaumcraft.common.config.ConfigItems.itemZombieBrain;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BlockEssentiaDiscretizer;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.dreammaster.item.NHItemList;
import com.gtnewhorizon.cropsnh.api.CropsNHItemList;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class TCRecipePool {

    public static InfusionRecipe infusionRecipeElvenWorkshop;
    public static InfusionRecipe infusionRecipeIndustrialMagicMatrix;
    public static InfusionRecipe infusionRecipeEcoSphereSimulator;
    public static InfusionRecipe infusionRecipeEcoSphereInputInterface;
    public static InfusionRecipe infusionRecipeEcoSphereUpgradeInterface;
    public static InfusionRecipe infusionRecipeEcoSphereModeBeacon1;
    public static InfusionRecipe infusionRecipeEcoSphereModeBeacon2;
    public static InfusionRecipe infusionRecipeEcoSphereModeBeacon3;
    public static InfusionRecipe infusionRecipeEcoSphereModeBeacon4;
    public static InfusionRecipe infusionRecipeEcoSphereModeBeacon5;
    public static InfusionRecipe infusionRecipeEcoSphereModeBeacon6;
    public static InfusionRecipe infusionRecipeEcoSphereModeBeacon7;
    public static InfusionRecipe infusionRecipeEcoSphereModeBeacon8;
    public static InfusionRecipe infusionRecipeEcoSphereFluidEfficiencyUpgrade;
    public static InfusionRecipe infusionRecipeEcoSphereOutputBoostUpgrade;
    public static InfusionRecipe infusionRecipeEcoSphereSpeedUpgrade;
    public static InfusionRecipe infusionRecipeEcoSphereCapacityUpgrade;
    public static InfusionRecipe infusionRecipeEcoSphereAutoPulverizeUpgrade;
    public static InfusionRecipe infusionRecipeFontOfEcology;
    public static InfusionRecipe infusionRecipeBloodyHell;
    public static InfusionRecipe infusionRecipeCoagulatedBloodCasing;
    public static InfusionRecipe infusionRecipeBloodHatch;
    public static InfusionRecipe infusionRecipeTimeBendingSpeedRune;
    public static InfusionRecipe infusionRecipeIndustrialAlchemyTower;
    public static InfusionRecipe infusionRecipePrimordialDisjunctus;
    public static InfusionRecipe infusionRecipeSkypiercerTower;
    public static InfusionRecipe infusionRecipeInfusionMaterialDispenser;
    public static InfusionRecipe infusionRecipeEssentiaDiscretizer;
    public static CrucibleRecipe crucibleRecipeArcaneHole;

    public static void loadRecipes() {
        // spotless:off

        /* Elven Workshop */
        infusionRecipeElvenWorkshop = addInfusionCraftingRecipeAspectNotNull(
            "BH_ELVEN_WORKSHOP",
            GTCMItemList.ElvenWorkshop.get(1, 0),
            10,
            (new AspectList()).merge(Aspect.LIFE, 64)
                .merge(Aspect.EARTH, 64)
                .merge(Aspect.MAGIC, 64)
                .merge(Aspect.MECHANISM, 64),
            new ItemStack(ModBlocks.terraPlate),
            new ItemStack[] { ItemList.Field_Generator_EV.get(1), ItemList.Casing_IV.get(1),
                Materials.Steeleaf.getPlates(1), new ItemStack(ModItems.spawnerMover, 1),
                ItemList.Field_Generator_EV.get(1), ItemList.Casing_IV.get(1), Materials.Steeleaf.getPlates(1),
                new ItemStack(ModItems.spawnerMover, 1) });

        /* INDUSTRIAL_MAGIC_MATRIX */
        if (Config.Enable_IndustrialMagicMatrix) {
            infusionRecipeIndustrialMagicMatrix = addInfusionCraftingRecipeAspectNotNull(
                "INDUSTRIAL_MAGIC_MATRIX",
                GTCMItemList.IndustrialMagicMatrix.get(1, 0),
                25,
                (new AspectList()).merge(Aspect.LIFE, 128)
                    .merge(Aspect.EARTH, 128)
                    .merge(Aspect.MAGIC, 128)
                    .merge(Aspect.MECHANISM, 128)
                    .merge(Aspect.AIR, 128)
                    .merge(Aspect.EARTH, 128)
                    .merge(Aspect.FIRE, 128)
                    .merge(Aspect.WATER, 128)
                    .merge(Aspect.ORDER, 128)
                    .merge(Aspect.ENTROPY, 128),
                ItemList.Machine_Multi_Assemblyline.get(1, 0),
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12) });

            /* ECO_SPHERE_SIMULATOR */
            if (Config.Enable_EcoSphereSimulator) {
                infusionRecipeEcoSphereSimulator = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.EcoSphereSimulator.get(1),
                    50,
                    (new AspectList()).merge((Aspect) ELECTRUM.mAspect, 8192)
                        .merge(Aspect.MECHANISM, 8192)
                        .merge(Aspect.TREE, 4096)
                        .merge(Aspect.HARVEST, 2048)
                        .merge(Aspect.WATER, 4096)
                        .merge(Aspect.LIFE, 2048)
                        .merge(Aspect.PLANT, 4096)
                        .merge(Aspect.CROP, 2048)
                        .merge(Aspect.FLESH, 4096)
                        .merge(Aspect.WEAPON, 2048)
                        .merge(Aspect.ENERGY, 1024)
                        .merge(Aspect.LIGHT, 1024)
                        .merge(Aspect.AURA, 256),
                    new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
                    new ItemStack[] {
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Ichorium, 1L),
                        NHItemList.CircuitUV.get(1),
                        MaterialsAlloy.TITANSTEEL.getPlateDense(1),
                        ItemList.Robot_Arm_UV.get(1),
                        ItemList.Conveyor_Module_UV.get(1),

                        getModItem(Mods.Gadomancy.ID, "ItemAuraCore", 1, 1),
                        getModItem(Mods.Gadomancy.ID, "ItemAuraCore", 1, 3),
                        getModItem(Mods.Gadomancy.ID, "ItemAuraCore", 1, 4),

                        ItemList.Conveyor_Module_UV.get(1),
                        ItemList.Robot_Arm_UV.get(1),
                        MaterialsAlloy.TITANSTEEL.getPlateDense(1),
                        NHItemList.CircuitUV.get(1),
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Ichorium, 1L),
                        NHItemList.CircuitUV.get(1),
                        MaterialsAlloy.TITANSTEEL.getPlateDense(1),
                        ItemList.Robot_Arm_UV.get(1),
                        ItemList.Conveyor_Module_UV.get(1),

                        getModItem(Mods.Thaumcraft.ID, "FocusPortableHole", 1, 0),
                        getModItem(Mods.TaintedMagic.ID, "ItemFocusMeteorology", 1, 0),
                        getModItem(Mods.ThaumicHorizons.ID, "focusIllumination", 1, 11),

                        ItemList.Conveyor_Module_UV.get(1),
                        ItemList.Robot_Arm_UV.get(1),
                        MaterialsAlloy.TITANSTEEL.getPlateDense(1),
                        NHItemList.CircuitUV.get(1)});

                infusionRecipeEcoSphereInputInterface = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.EcoSphereInputInterface.get(1),
                    30,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 1024)
                        .merge(Aspect.MECHANISM, 1024)
                        .merge(Aspect.CRAFT, 512)
                        .merge(Aspect.SENSES, 512)
                        .merge(getAspect("desidia", 256))
                        .merge(Aspect.ENTROPY, 64),
                    ItemList.Hatch_Input_Bus_UV.get(1),
                    new ItemStack[] {
                        new ItemStack(ConfigBlocks.blockJar, 1, 1),
                        getModItem(Mods.ThaumicHorizons.ID, "focusAnimation", 1, 0),
                        getModItem(Mods.Automagy.ID, "avaricePearl", 1, 0),
                        getModItem(Mods.AppliedEnergistics2.ID, "tile.BlockCraftingUnit", 1, 3),
                        getModItem(Mods.OpenComputers.ID, "item", 1, 39),
                        NHItemList.CircuitUHV.get(1) });

                infusionRecipeEcoSphereUpgradeInterface = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.EcoSphereUpgradeInterface.get(1),
                    30,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 1024)
                        .merge(Aspect.MECHANISM, 1024)
                        .merge(Aspect.TOOL, 512)
                        .merge(Aspect.SENSES, 512)
                        .merge(getAspect("gula", 256))
                        .merge(Aspect.ORDER, 64),
                    ItemList.Hatch_Input_Bus_UV.get(1),
                    new ItemStack[] {
                        new ItemStack(ConfigBlocks.blockJar, 1, 1),
                        ItemList.Tool_DataOrb.get(1),
                        new ItemStack(ConfigItems.itemResource, 1, 12),
                        getModItem(Mods.AppliedEnergistics2.ID, "tile.BlockCraftingUnit", 1, 3),
                        getModItem(Mods.OpenComputers.ID, "item", 1, 39),
                        NHItemList.CircuitUHV.get(1) });

                infusionRecipeEcoSphereModeBeacon1 = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_MODE_BEACON_1",
                    GTCMItemList.EcoSphereModeBeacon1.get(1),
                    40,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(Aspect.PLANT, 128)
                        .merge(Aspect.TREE, 128)
                        .merge(Aspect.ORDER, 64),
                    ItemList.Circuit_Board_Bio.get(1),
                    new ItemStack[] {
                        TreeGrowSimulator.get(1),
                        new ItemStack(ConfigItems.itemAxeElemental, 1, 0),
                        new ItemStack(ModBlocks.pylon, 1, 1),
                        GregtechItemList.Compost.get(1),
                        new ItemStack(ModItems.manaResource, 1, 5),
                        NHItemList.CircuitUV.get(1),
                    });

                infusionRecipeEcoSphereModeBeacon2 = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_MODE_BEACON_2",
                    GTCMItemList.EcoSphereModeBeacon2.get(1),
                    40,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(getAspect("permutatio", 128))
                        .merge(getAspect("vitium", 128))
                        .merge(Aspect.ENTROPY, 64),
                    GTCMItemList.EcoSphereModeBeacon1.get(1),
                    new ItemStack[] {
                        getModItem("gendustry", "MutatronAdv", 1, 0),
                        getModItem("ThaumicTinkerer", "ichorAxeGem", 1, 0),
                        getModItem("TwilightForest", "tile.TFSapling", 1, 6),
                        getModItem("TConstruct", "CraftedSoil", 1, 3),
                        getModItem("EMT", "EMTItems", 1, 15),
                        NHItemList.CircuitUHV.get(1)
                    });

                infusionRecipeEcoSphereModeBeacon3 = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_MODE_BEACON_3",
                    GTCMItemList.EcoSphereModeBeacon3.get(1),
                    40,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(Aspect.LIFE, 128)
                        .merge(Aspect.SLIME, 128)
                        .merge(Aspect.ORDER, 64),
                    ItemList.Circuit_Board_Bio.get(1),
                    new ItemStack[] {
                        ItemList.FishingPort.get(1),
                        getModItem(Mods.Forestry.ID, "craftingMaterial", 1, 1),
                        getModItem(Mods.WarpTheory.ID, "item.warptheory.cleanser", 1, 0),
                        getModItem(Mods.ThaumicHorizons.ID, "planarConduit", 1, 0),
                        new ItemStack(ModItems.manaResource, 1, 5),
                        NHItemList.CircuitUHV.get(1)
                    });

                infusionRecipeEcoSphereModeBeacon4 = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_MODE_BEACON_4",
                    GTCMItemList.EcoSphereModeBeacon4.get(1),
                    40,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(getAspect("luxuria", 128))
                        .merge(getAspect("alienis", 128))
                        .merge(Aspect.ENTROPY, 64),
                    GTCMItemList.EcoSphereModeBeacon3.get(1),
                    new ItemStack[] {
                        getModItem(Mods.NewHorizonsCoreMod.ID, "TCetiESeaweedExtract", 1, 0),
                        new ItemStack(ConfigBlocks.blockCustomPlant, 1, 4),
                        getModItem(Mods.WarpTheory.ID, "item.warptheory.cleanserminor", 1, 0),
                        getModItem(Mods.Thaumcraft.ID, "FocusFrost", 1, 0),
                        getModItem("computronics", "computronics.partsForestry", 1, 1),
                        NHItemList.CircuitUEV.get(1)
                    });

                infusionRecipeEcoSphereModeBeacon5 = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_MODE_BEACON_5",
                    GTCMItemList.EcoSphereModeBeacon5.get(1),
                    40,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(Aspect.CROP, 128)
                        .merge(Aspect.PLANT, 128)
                        .merge(Aspect.ORDER, 64),
                    ItemList.Circuit_Board_Bio.get(1),
                    new ItemStack[] {
                        ExtremeIndustrialGreenhouse.get(1),
                        new ItemStack(ConfigItems.itemHoeElemental, 1, 0),
                        new ItemStack(ModBlocks.pylon, 1, 1),
                        newItemStackWithNBT(ModBlocks.specialFlower, 1, 0, "type", "agricarnation"),
                        new ItemStack(ModItems.manaResource, 1, 5),
                        NHItemList.CircuitUHV.get(1)
                    });

                infusionRecipeEcoSphereModeBeacon6 = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_MODE_BEACON_6",
                    GTCMItemList.EcoSphereModeBeacon6.get(1),
                    40,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(Aspect.POISON, 128)
                        .merge(getAspect("invidia", 128))
                        .merge(Aspect.ENTROPY, 64),
                    GTCMItemList.EcoSphereModeBeacon5.get(1),
                    new ItemStack[] {
                        CropsNHItemList.CropManager_UEV.get(1),
                        CropsNHItemList.goldfish.get(1),
                        CropsNHItemList.SeedBed_UEV.get(1),
                        new ItemStack(ModItems.obedienceStick, 1),
                        new ItemStack(ModBlocks.enchantedSoil, 1),
                        NHItemList.CircuitUEV.get(1)
                    });

                infusionRecipeEcoSphereModeBeacon7 = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_MODE_BEACON_7",
                    GTCMItemList.EcoSphereModeBeacon7.get(1),
                    40,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(Aspect.HUNGER, 128)
                        .merge(Aspect.FLESH, 128)
                        .merge(Aspect.ORDER, 64),
                    ItemList.Circuit_Board_Bio.get(1),
                    new ItemStack[] {
                        ExtremeEntityCrusher.get(1),
                        newItemStackWithNBT(getModItem(Mods.BloodArsenal.ID, "compacted_mrs", 1, 0), "ritualName", "AW013Suffering"),
                        GTCMItemList.BloodyCasing2.get(1),
                        ItemList.NameRemover.get(1),
                        getModItem(Mods.ExtraUtilities.ID, "ethericsword", 1, 0),
                        getModItem(Mods.BloodArsenal.ID, "sigil_of_divinity", 1, 0),
                        new ItemStack(ModItems.manaResource, 1, 5),
                        NHItemList.CircuitUEV.get(1)
                    });

                infusionRecipeEcoSphereModeBeacon8 = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_MODE_BEACON_8",
                    GTCMItemList.EcoSphereModeBeacon8.get(1),
                    40,
                    new AspectList().merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(getAspect("superbia", 128))
                        .merge(getAspect("ira", 128))
                        .merge(Aspect.ENTROPY, 64),
                    GTCMItemList.EcoSphereModeBeacon7.get(1),
                    new ItemStack[] {
                        getModItem(Mods.Thaumcraft.ID, "ItemGolemPlacer", 1, 4),
                        getModItem(Mods.ExtraUtilities.ID, "mini-soul", 1, 0),
                        getModItem(Mods.TaintedMagic.ID, "ItemFocusEldritch", 1, 0),
                        getModItem(Mods.BloodMagic.ID, "bloodMagicBaseItems", 1, 28),
                        getModItem(Mods.Avaritia.ID, "Infinity_Sword", 1, 0),
                        getModItem(Mods.BloodMagic.ID, "bloodMagicBaseItems", 1, 29),
                        new ItemStack(ConfigItems.itemFocusWarding, 1),
                        NHItemList.CircuitUIV.get(1)
                    });

                infusionRecipeEcoSphereFluidEfficiencyUpgrade = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.EcoSphereFluidEfficiencyUpgrade.get(1),
                    40,
                    new AspectList().merge(getAspect("nebrisum", 256))
                        .merge(getAspect("perfodio", 256))
                        .merge(getAspect("vacuos", 128))
                        .merge(getAspect("sano", 128))
                        .merge(getAspect("aqua", 64)),
                    getModItem(Mods.DraconicEvolution.ID, "draconiumEnergyCore", 1, 0),
                    new ItemStack[] {
                        getModItem(Mods.ThaumicHorizons.ID, "lensWater", 1, 0),
                        getModItem("ae2fc", "fluid_part", 1, 7),
                        getModItem(Mods.Thaumcraft.ID, "ItemGolemUpgrade", 1, 3),
                        ItemRefer.Fluid_Storage_Core_T7.get(1),
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CallistoIce, 1),
                        ItemList.FluidRegulator_UV.get(1) });

                infusionRecipeEcoSphereOutputBoostUpgrade = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.EcoSphereOutputBoostUpgrade.get(1),
                    40,
                    new AspectList().merge(getAspect("nebrisum", 256))
                        .merge(getAspect("perfodio", 256))
                        .merge(getAspect("meto", 128))
                        .merge(getAspect("iter", 128))
                        .merge(getAspect("terra", 64)),
                    getModItem(Mods.DraconicEvolution.ID, "draconiumEnergyCore", 1, 0),
                    new ItemStack[] {
                        getModItem("Automagy", "blockMirrorAlt", 1, 0),
                        getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 60),
                        getModItem(Mods.Thaumcraft.ID, "ItemGolemUpgrade", 1, 1),
                        ItemList.Quantum_Chest_EV.get(1),
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 1),
                        ItemList.Conveyor_Module_UHV.get(1) });

                infusionRecipeEcoSphereSpeedUpgrade = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.EcoSphereSpeedUpgrade.get(1),
                    40,
                    new AspectList().merge(getAspect("lucrum", 256))
                        .merge(getAspect("nebrisum", 256))
                        .merge(getAspect("tempus", 128))
                        .merge(getAspect("vinculum", 128))
                        .merge(getAspect("aer", 64)),
                    getModItem(Mods.DraconicEvolution.ID, "draconiumEnergyCore", 1, 0),
                    new ItemStack[] {
                        getModItem(Mods.TwilightForest.ID, "tile.TFMagicLogSpecial", 1, 0),
                        ItemList.Field_Generator_UEV.get(1),
                        getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 56),
                        ItemList.AcceleratorUV.get(1),
                        getEnchantedCapacitor(),
                        getModItem(Mods.TaintedMagic.ID, "ItemFocusTime", 1, 0) });

                infusionRecipeEcoSphereCapacityUpgrade = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.EcoSphereCapacityUpgrade.get(1),
                    40,
                    new AspectList().merge(getAspect("lucrum", 256))
                        .merge(getAspect("nebrisum", 256))
                        .merge(getAspect("caelum", 128))
                        .merge(getAspect("humanus", 128))
                        .merge(getAspect("motus", 64)),
                    getModItem(Mods.DraconicEvolution.ID, "draconiumEnergyCore", 1, 0),
                    new ItemStack[] {
                        getModItem(Mods.AppliedEnergistics2.ID, "item.ItemExtremeStorageCell.Quantum", 1, 0),
                        ItemList.Robot_Arm_UEV.get(1),
                        GregtechItemList.Laser_Lens_Special.get(1),
                        getModItem("ae2fc", "super_stock_replenisher", 1, 0),
                        getModItem(Mods.StorageDrawers.ID, "upgradeDowngrade", 1, 0),
                        ItemRefer.HiC_T5.get(1) });

                infusionRecipeEcoSphereAutoPulverizeUpgrade = addInfusionCraftingRecipeAspectNotNull(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.EcoSphereAutoPulverizeUpgrade.get(1),
                    40,
                    new AspectList().merge(getAspect("instrumentum", 256))
                        .merge(getAspect("custom2", 256))
                        .merge(getAspect("strontio", 128))
                        .merge(getAspect("mortuus", 128))
                        .merge(getAspect("perditio", 64)),
                    getModItem(Mods.DraconicEvolution.ID, "draconiumEnergyCore", 1, 1),
                    new ItemStack[] {
                        GTCMItemList.MegaMacerator.get(1),
                        ItemList.Component_Grinder_Tungsten.get(1),
                        getModItem(Mods.Thaumcraft.ID, "ItemEldritchObject", 1, 0),
                        ItemList.Automation_SuperBuffer_MAX.get(1),
                        ItemList.Sensor_UEV.get(1),
                        ItemList.T3Sawblade.get(1) });

                infusionRecipeFontOfEcology = addInfusionCraftingRecipeAspectNotNull(
                    "FONT_OF_ECOLOGY",
                    GTCMItemList.FountOfEcology.get(1),
                    200,
                    (new AspectList()).merge(EVOLUTION, 1024)
                        .merge(Aspect.WATER, 65536)
                        .merge(Aspect.LIFE, 16384)
                        .merge(Aspect.FLESH, 4096)
                        .merge(ULTRA_DEATH, 256),
                    getModItem(Mods.Witchery.ID, "infinityegg", 1, 0, new ItemStack(Blocks.dragon_egg, 1)),
                    new ItemStack[] { GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1) });
            }

            if (Config.Enable_BloodHell) {
                infusionRecipeBloodyHell = addInfusionCraftingRecipeAspectNotNull(
                    "BLOODY_HELL",
                    GTCMItemList.BloodyHell.get(1, 0),
                    25,
                    new AspectList().merge(Aspect.LIFE, 128)
                        .merge(Aspect.HEAL, 128)
                        .merge(Aspect.MAGIC, 128)
                        .merge(Aspect.MAN, 64)
                        .merge(Aspect.DEATH, 64)
                        .merge(Aspect.UNDEAD, 64)
                        .merge(Aspect.MECHANISM, 16),
                    new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.blockMasterStone),
                    new ItemStack[] { new ItemStack(WayofTime.alchemicalWizardry.ModItems.activationCrystal),
                        BloodArsenal.AmorphicCatalyst.get(1),
                        new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.blockAltar),
                        BloodArsenal.AmorphicCatalyst.get(1),
                        new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.blockWritingTable),
                        BloodArsenal.AmorphicCatalyst.get(1) });

                infusionRecipeCoagulatedBloodCasing = addInfusionCraftingRecipeAspectNotNull(
                    "BLOODY_HELL",
                    GTCMItemList.BloodyCasing1.get(1),
                    13,
                    new AspectList().merge(Aspect.LIFE, 13)
                        .merge(Aspect.HEAL, 13)
                        .merge(Aspect.MECHANISM, 26),
                    getModItem(Mods.BloodArsenal.ID, "blood_stone", 1, 1, new ItemStack(Blocks.stone, 1)),
                    new ItemStack[] { new ItemStack(WayofTime.alchemicalWizardry.ModItems.waterSigil, 1),
                        new ItemStack(WayofTime.alchemicalWizardry.ModItems.sigilOfTheFastMiner, 1),
                        new ItemStack(WayofTime.alchemicalWizardry.ModItems.itemSeerSigil, 1) });

                if (Config.Enable_BloodHatch) {
                    infusionRecipeBloodHatch = addInfusionCraftingRecipeAspectNotNull(
                        "BLOOD_HATCH",
                        GTCMItemList.BloodOrbHatch.get(1, 0),
                        5,
                        new AspectList().merge(Aspect.LIFE, 16)
                            .merge(Aspect.MECHANISM, 16)
                            .merge(Aspect.TOOL, 12),
                        ItemList.Hatch_Input_IV.get(1),
                        new ItemStack[] { new ItemStack(WayofTime.alchemicalWizardry.ModItems.weakBloodOrb),
                            new ItemStack(WayofTime.alchemicalWizardry.ModItems.sacrificialDagger),
                            new ItemStack(itemZombieBrain), new ItemStack(itemZombieBrain), });
                }
                infusionRecipeTimeBendingSpeedRune = addInfusionCraftingRecipeAspectNotNull(
                    "TIME_BENDING_SPEED_RUNE",
                    new ItemStack(TstBlocks.TimeBendingSpeedRune),
                    10,
                    new AspectList().merge(Aspect.LIFE, 64)
                        .merge(Aspect.MOTION, 256)
                        .merge(getAspect(Mods.MagicBees.isModLoaded() ? "tempus" : "air", 64)),
                    Materials.SpaceTime.getBlocks(1),
                    new ItemStack[] { ItemList.AcceleratorZPM.get(1), ItemList.AcceleratorZPM.get(1),
                        new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.bloodRune, 1, 5), // Rune of Acceleration
                        new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.bloodRune, 1, 5), });
            }
            if (Config.Enable_IndustrialAlchemyTower) {
                infusionRecipeIndustrialAlchemyTower = addInfusionCraftingRecipeAspectNotNull(
                    "INDUSTRIAL_ALCHEMY_TOWER",
                    GTCMItemList.IndustrialAlchemyTower.get(1),
                    16,
                    new AspectList().merge(Aspect.AIR, 64)
                        .merge(Aspect.FIRE, 64)
                        .merge(Aspect.ORDER, 64)
                        .merge(Aspect.ENTROPY, 64)
                        .merge(Aspect.EXCHANGE, 64)
                        .merge(Aspect.MAGIC, 128),
                    Industrial_AlloyBlastSmelter.get(1),
                    new ItemStack[] { new ItemStack(blockMetalDevice, 1, 9),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                        BloodArsenal.AmorphicCatalyst.get(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                        new ItemStack(blockMetalDevice, 1, 9),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                        BloodArsenal.AmorphicCatalyst.get(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L) });
            }
            crucibleRecipeArcaneHole = ThaumcraftApi.addCrucibleRecipe(
                "TST_ARCANE_HOLE",
                new ItemStack(TstBlocks.BlockArcaneHole, 1),
                new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2),
                new AspectList().merge(Aspect.VOID, 16)
                    .merge(Aspect.DARKNESS, 8)
                    .merge(Aspect.SENSES, 8));
            if (Config.Enable_PrimordialDisjunctus) {
                infusionRecipePrimordialDisjunctus = addInfusionCraftingRecipeAspectNotNull(
                    "PRIMORDIAL_DISJUNCTUS",
                    GTCMItemList.PrimordialDisjunctus.get(1),
                    12,
                    new AspectList().merge(Aspect.MAGIC, 128)
                        .merge((Aspect) ELECTRUM.mAspect, 128)
                        .merge((Aspect) RADIO.mAspect, 128)
                        .merge(Aspect.EXCHANGE, 128),
                    new ItemStack(ConfigBlocks.blockMetalDevice, 1, 0),
                    new ItemStack[] { new ItemStack(blockMetalDevice, 1, 9),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                        new ItemStack(ConfigBlocks.blockMetalDevice, 1, 1),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                        new ItemStack(blockMetalDevice, 1, 9),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                        new ItemStack(ConfigBlocks.blockCrystal, 1, 1),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L) });
            }
            if (Config.Enable_SkypiercerTower) {
                infusionRecipeSkypiercerTower = addInfusionCraftingRecipeAspectNotNull(
                    "SKYPIERCER_TOWER",
                    GTCMItemList.SkypiercerTower.get(1),
                    16,
                    new AspectList().merge(Aspect.MAGIC, 256)
                        .merge((Aspect) ELECTRUM.mAspect, 256)
                        .merge(Aspect.MECHANISM, 256)
                        .merge(Aspect.CLOTH, 256),
                    Machine_IV_Assembler.get(1),
                    new ItemStack[] { new ItemStack(huiCircuit, 1, 0),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L), new ItemStack(huiCircuit, 1, 0),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L), new ItemStack(huiCircuit, 1, 0),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L), new ItemStack(huiCircuit, 1, 0),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L) });
            }
            if (Config.Enable_InfusionMaterialDispenser) {
                infusionRecipeInfusionMaterialDispenser = addInfusionCraftingRecipeAspectNotNull(
                    "INFUSION_MATERIAL_DISPENSER",
                    GTCMItemList.InfusionMaterialDispenser.get(1),
                    8,
                    new AspectList().merge(Aspect.MOTION, 32)
                        .merge(Aspect.MECHANISM, 32)
                        .merge(Aspect.EXCHANGE, 32)
                        .merge(Aspect.MAN, 32),
                    Automation_ChestBuffer_IV.get(1),
                    new ItemStack[] { new ItemStack(ConfigItems.itemWandCasting, 1), ItemList.Conveyor_Module_IV.get(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L) });
            }
            if (Config.Enable_EssentiaDiscretizer) {
                infusionRecipeEssentiaDiscretizer = ThaumcraftApi.addInfusionCraftingRecipe(
                    "ESSENTIA_DISCRETIZER",
                    BlockEssentiaDiscretizer.stack(),
                    6,
                    new AspectList().merge(Aspect.MECHANISM, 32)
                        .merge(Aspect.MAN, 32)
                        .merge(Aspect.MAGIC, 32)
                        .merge(Aspect.SOUL, 32),
                    INTERFACE.stack(),
                    new ItemStack[] { new ItemStack(itemZombieBrain, 1), new ItemStack(blockMetalDevice, 1, 9),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L) });
            }
        }
        //spotless:on
    }

    private static ItemStack getEnchantedCapacitor() {
        ItemStack capacitor = getModItem(Mods.EnderIO.ID, "itemBasicCapacitor", 1, 6);
        NBTTagCompound enchantment = new NBTTagCompound();
        enchantment.setShort("id", (short) 32);
        enchantment.setShort("lvl", (short) 5);
        NBTTagList enchantments = new NBTTagList();
        enchantments.appendTag(enchantment);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("ench", enchantments);
        capacitor.setTagCompound(tag);
        return capacitor;
    }
}
