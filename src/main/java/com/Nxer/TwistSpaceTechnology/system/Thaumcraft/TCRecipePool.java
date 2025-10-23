package com.Nxer.TwistSpaceTechnology.system.Thaumcraft;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.BloodArsenal;
import static com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic.EVOLUTION;
import static com.glodblock.github.loader.ItemAndBlockHolder.DISCRETIZER;
import static fox.spiteful.avaritia.compat.thaumcraft.Lucrum.ULTRA_DEATH;
import static goodgenerator.loader.Loaders.huiCircuit;
import static gregtech.api.enums.ItemList.Automation_ChestBuffer_IV;
import static gregtech.api.enums.ItemList.Machine_IV_Assembler;
import static gregtech.api.enums.TCAspects.ELECTRUM;
import static gregtech.api.enums.TCAspects.RADIO;
import static gtPlusPlus.core.material.MaterialsAlloy.TITANSTEEL;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_AlloyBlastSmelter;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_FishingPond;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_TreeFarm;
import static kubatech.api.enums.ItemList.ExtremeEntityCrusher;
import static kubatech.api.enums.ItemList.ExtremeIndustrialGreenhouse;
import static net.minecraft.init.Items.diamond_sword;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.config.ConfigBlocks.blockStoneDevice;
import static thaumcraft.common.config.ConfigItems.itemZombieBrain;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BlockEssentiaDiscretizer;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.config.Config;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
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
        /* Elven Workshop */
        infusionRecipeElvenWorkshop = ThaumcraftApi.addInfusionCraftingRecipe(
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
            infusionRecipeIndustrialMagicMatrix = ThaumcraftApi.addInfusionCraftingRecipe(
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
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                    new ItemStack(blockStoneDevice, 1, 2), new ItemStack(blockMetalDevice, 1, 3),
                    new ItemStack(blockMetalDevice, 1, 12) });

            /* ECO_SPHERE_SIMULATOR */
            if (Config.Enable_MegaTreeFarm) {
                infusionRecipeEcoSphereSimulator = ThaumcraftApi.addInfusionCraftingRecipe(
                    "ECO_SPHERE_SIMULATOR",
                    GTCMItemList.MegaTreeFarm.get(1),
                    100,
                    (new AspectList()).merge(Aspect.MECHANISM, 256)
                        .merge(Aspect.TREE, 1024)
                        .merge(Aspect.HARVEST, 2048)
                        .merge(Aspect.WATER, 1024)
                        .merge(Aspect.LIFE, 2048)
                        .merge(Aspect.PLANT, 1024)
                        .merge(Aspect.CROP, 2048)
                        .merge(Aspect.FLESH, 1024)
                        .merge(Aspect.WEAPON, 2048)
                        .merge((Aspect) ELECTRUM.mAspect, 8192),

                    GTModHandler.getModItem(Mods.Botania.ID, "manaResource", 1, 5),
                    new ItemStack[] { Industrial_TreeFarm.get(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                        TITANSTEEL.getPlateDense(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),

                        Industrial_FishingPond.get(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                        TITANSTEEL.getPlateDense(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),

                        ExtremeIndustrialGreenhouse.get(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                        TITANSTEEL.getPlateDense(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),

                        (Mods.EnderIO.isModLoaded() && Mods.MobsInfo.isModLoaded()) ? ExtremeEntityCrusher.get(1)
                            : new ItemStack(diamond_sword, 1),
                        GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1),
                        TITANSTEEL.getPlateDense(1),
                        GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 1) });

                infusionRecipeFontOfEcology = ThaumcraftApi.addInfusionCraftingRecipe(
                    "FONT_OF_ECOLOGY",
                    GTCMItemList.FountOfEcology.get(1),
                    200,
                    (new AspectList()).merge(EVOLUTION, 1024)
                        .merge(Aspect.WATER, 65536)
                        .merge(Aspect.LIFE, 16384)
                        .merge(Aspect.FLESH, 4096)
                        .merge(ULTRA_DEATH, 256),
                    Mods.Witchery.isModLoaded() ? GTModHandler.getModItem(Mods.Witchery.ID, "infinityegg", 1)
                        : new ItemStack(Blocks.dragon_egg, 1),
                    new ItemStack[] { GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1),
                        GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1), GTCMItemList.OffSpring.get(1) });
            }

            if (Config.Enable_BloodHell) {
                infusionRecipeBloodyHell = ThaumcraftApi.addInfusionCraftingRecipe(
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

                infusionRecipeCoagulatedBloodCasing = ThaumcraftApi.addInfusionCraftingRecipe(
                    "BLOODY_HELL",
                    GTCMItemList.BloodyCasing1.get(1),
                    13,
                    new AspectList().merge(Aspect.LIFE, 13)
                        .merge(Aspect.HEAL, 13)
                        .merge(Aspect.MECHANISM, 26),
                    Mods.BloodArsenal.isModLoaded() ? GTModHandler.getModItem(Mods.BloodArsenal.ID, "blood_stone", 1, 1)
                        : new ItemStack(Blocks.stone, 1),
                    new ItemStack[] { new ItemStack(WayofTime.alchemicalWizardry.ModItems.waterSigil, 1),
                        new ItemStack(WayofTime.alchemicalWizardry.ModItems.sigilOfTheFastMiner, 1),
                        new ItemStack(WayofTime.alchemicalWizardry.ModItems.itemSeerSigil, 1) });

                if (Config.Enable_BloodHatch) {
                    infusionRecipeBloodHatch = ThaumcraftApi.addInfusionCraftingRecipe(
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
                infusionRecipeTimeBendingSpeedRune = ThaumcraftApi.addInfusionCraftingRecipe(
                    "TIME_BENDING_SPEED_RUNE",
                    new ItemStack(TstBlocks.TimeBendingSpeedRune),
                    10,
                    new AspectList().merge(Aspect.LIFE, 64)
                        .merge(Aspect.MOTION, 256)
                        .merge((Mods.MagicBees.isModLoaded() ? Aspect.getAspect("tempus") : Aspect.AIR), 64),
                    MaterialsUEVplus.SpaceTime.getBlocks(1),
                    new ItemStack[] { ItemList.AcceleratorZPM.get(1), ItemList.AcceleratorZPM.get(1),
                        new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.bloodRune, 1, 5), // Rune of Acceleration
                        new ItemStack(WayofTime.alchemicalWizardry.ModBlocks.bloodRune, 1, 5), });
            }
            if (Config.Enable_IndustrialAlchemyTower) {
                infusionRecipeIndustrialAlchemyTower = ThaumcraftApi.addInfusionCraftingRecipe(
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
                infusionRecipePrimordialDisjunctus = ThaumcraftApi.addInfusionCraftingRecipe(
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
                infusionRecipeSkypiercerTower = ThaumcraftApi.addInfusionCraftingRecipe(
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
                infusionRecipeInfusionMaterialDispenser = ThaumcraftApi.addInfusionCraftingRecipe(
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
                    DISCRETIZER.stack(),
                    new ItemStack[] { new ItemStack(itemZombieBrain, 1), new ItemStack(blockMetalDevice, 1, 9),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L) });
            }
        }
    }
}
