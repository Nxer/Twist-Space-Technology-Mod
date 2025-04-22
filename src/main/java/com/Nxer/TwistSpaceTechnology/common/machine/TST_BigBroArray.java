package com.Nxer.TwistSpaceTechnology.common.machine;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TT_MultiMachineBase_EM;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.util.MathUtils;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.dreammaster.gthandler.CustomItemList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.supsolpans.MainSSP;

import advsolar.common.AdvancedSolarPanel;
import advsolar.common.tiles.TileEntitySolarPanel;
import bartworks.API.BorosilicateGlass;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emt.EMT;
import emt.block.BlockSolars;
import emt.tile.solar.Solars;
import emt.tile.solar.TileEntitySolarBase;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.recipe.Scanning;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.basic.MTEMassfabricator;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import io.netty.buffer.ByteBuf;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class TST_BigBroArray extends TT_MultiMachineBase_EM implements ISurvivalConstructable {

    private ItemStack machines;

    private long maxParallelism = 256;

    private long actualParallelism = 256;
    private int machineCountForMaxParallelism = 256;
    private String machineType = null;

    private int machineTier = -1;

    // affects energy hatch
    private int glassTier = -2;

    // affects machines tier that can be put into array
    private int frameTier = -1;

    // bonus processing speed and reduce power cost
    private HeatingCoilLevel coilTier = HeatingCoilLevel.None;

    // affects maxparallelism
    private int parallelismTier = 0;

    // affects dynamo
    private int casingTier = -1;

    // affects max parallelism
    private int addonCount = 0;
    private String mode;

    private static final String MODE_GENERATOR = "generator";

    private static final String MODE_PROCESSOR = "processor";

    private TileEntity generatorTE;

    private UUID ownerUUID;

    private boolean isWirelessMode = false;

    private BigInteger output = BigInteger.valueOf(0);
    private BigInteger outEUt = BigInteger.valueOf(0);

    private int casingMultiplier;

    private static String[] tierNames = new String[] { "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV", "UEV",
        "UIV", "UMV", "UXV", "MAX" };

    // UHV tier is called 'MAX' in legacy GT
    private static String[] tierNamesCasing = new String[] { "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "MAX",
        "UEV", "UIV", "UMV", "UXV" };

    @SideOnly(Side.CLIENT)
    public static ITexture[] DEFAULT_FRONT_ACTIVE;

    @SideOnly(Side.CLIENT)
    public static ITexture[] DEFAULT_FRONT_IDLE;

    @SideOnly(Side.CLIENT)
    private static ITexture[] DEFAULT_CASING_TEXTURE;

    // spotless:off
    public static final String[][] PROCESSING_MACHINE_LIST = new String[][]{
        // OP
        {"Macerator", "Macerator"}, {"OreWasher", "OreWashingPlant"}, {"ChemicalBath", "ChemicalBath"},
        {"ThermalCentrifuge", "ThermalCentrifuge"},
        // Processing
        {"E_Furnace", "ElectricFurnace"}, {"ArcFurnace", "ArcFurnace"}, {"Bender", "BendingMachine"},
        {"Wiremill", "Wiremill"}, {"Lathe", "Lathe"}, {"Hammer", "ForgeHammer"}, {"Extruder", "Extruder"},
        {"FluidExtractor", "FluidExtractor"}, {"Compressor", "Compressor"}, {"Press", "FormingPress"},
        {"FluidSolidifier", "FluidSolidifier"}, {"Extractor", "Extractor"},
        {"LaserEngraver", "PrecisionLaserEngraver"}, {"Autoclave", "Autoclave"}, {"Mixer", "Mixer"},
        {"AlloySmelter", "AlloySmelter"}, {"Electrolyzer", "Electrolyzer"}, {"Sifter", "SiftingMachine"},
        {"ChemicalReactor", "ChemicalReactor"}, {"ElectromagneticSeparator", "ElectromagneticSeparator"},
        {"Recycler", "Recycler"}, {"Massfab", "MassFabricator"}, {"Centrifuge", "Centrifuge"},
        {"Cutter", "CuttingMachine"}, {"Assembler", "AssemblingMachine"}, {"CircuitAssembler", "CircuitAssembler"}
        // TODO: bartworks bio lab
    };

    private static final DecimalFormat Out_Format = new DecimalFormat("#,###");

    // spotless:off
    private static final String[][] PATTERN_CORE = new String[][]{
        {
            "   AAAAA   ",
            "  AAAAAAA  ",
            " AAAAAAAAA ",
            "AAAAAAAAAAA",
            "AAAAAAAAAAA",
            "AAAAAAAAAAA",
            "AAAAAAAAAAA",
            "AAAAAAAAAAA",
            " AAAAAAAAA ",
            "  AAAAAAA  ",
            "   AAAAA   "
        }, {
        "   CCCCC   ",
        "  CCCCCCC  ",
        " CCCCCCCCC ",
        "CCCCCCCCCCC",
        "CCCCCCCCCCC",
        "CCCCCCCCCCC",
        "CCCCCCCCCCC",
        "CCCCCCCCCCC",
        " CCCCCCCCC ",
        "  CCCCCCC  ",
        "   CCCCC   "
    }, {
        "           ",
        "           ",
        "   B   B   ",
        "  B     B  ",
        "           ",
        "           ",
        "           ",
        "  B     B  ",
        "   B   B   ",
        "           ",
        "           "
    }, {
        "           ",
        "           ",
        "   B   B   ",
        "  B     B  ",
        "           ",
        "           ",
        "           ",
        "  B     B  ",
        "   B   B   ",
        "           ",
        "           "
    }, {
        "           ",
        "           ",
        "   B   B   ",
        "  B     B  ",
        "    DDD    ",
        "    DDD    ",
        "    DDD    ",
        "  B     B  ",
        "   B   B   ",
        "           ",
        "           "
    }, {
        "           ",
        "           ",
        "   B   B   ",
        "  B     B  ",
        "    D~D    ",
        "    D D    ",
        "    DDD    ",
        "  B     B  ",
        "   B   B   ",
        "           ",
        "           "
    }, {
        "           ",
        "           ",
        "   B   B   ",
        "  B     B  ",
        "    DDD    ",
        "    DDD    ",
        "    DDD    ",
        "  B     B  ",
        "   B   B   ",
        "           ",
        "           "
    }, {
        "     F     ",
        "    EEE    ",
        "   EEEEE   ",
        "  EEEEEEE  ",
        " EEEEEEEEE ",
        "FEEEEEEEEEF",
        " EEEEEEEEE ",
        "  EEEEEEE  ",
        "   EEEEE   ",
        "    EEE    ",
        "     F     "
    }};

    private static final String[][] PATTERN_ADDON = new String[][]{{
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "        EEE      ",
        "       EEEEE     ",
        "       EEEEE     ",
        "       EEEEE     ",
        "        EEE      ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "       EEEEE     ",
        "      EEEEEEE    ",
        "     EEE A EEE   ",
        "     EE  A  EE   ",
        "     EEAAAAAEE   ",
        "     EE  A  EE   ",
        "     EEE A EEE   ",
        "      EEEEEEE    ",
        "       EEEEE     ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "       EEEEE     ",
        "      E  A  E    ",
        "     E   A   E   ",
        "    E         E  ",
        "    E         E  ",
        "    EAA     AAE  ",
        "    E         E  ",
        "    E         E  ",
        "     E   A   E   ",
        "      E  A  E    ",
        "       EEEEE     ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "       EEEEE     ",
        "      E  A  E    ",
        "     E       E   ",
        "    E         E  ",
        "   E           E ",
        "   E           E ",
        "   EA         AE ",
        "   E           E ",
        "   E           E ",
        "    E         E  ",
        "     E       E   ",
        "      E  A  E    ",
        "       EEEEE     ",
        "                 "
    }, {
        "                 ",
        "      EEEEEEE    ",
        "     E   A   E   ",
        "    E         E  ",
        "   E           E ",
        "   E           E ",
        "   E           E ",
        "   EA         AE ",
        "   E           E ",
        "   E           E ",
        "   E           E ",
        "    E         E  ",
        "     E   A   E   ",
        "      EEEEEEE    ",
        "                 "
    }, {
        "        EEE      ",
        "     EEEAAAEEE   ",
        "    E         E  ",
        "   E           E ",
        "   E           E ",
        "   E           E ",
        "  EA           AE",
        "  EA           AE",
        "  EA           AE",
        "   E           E ",
        "   E           E ",
        "   E           E ",
        "    E         E  ",
        "     EEEAAAEEE   ",
        "        EEE      "
    }, {
        "       EEEEE     ",
        "     EEA A AEE   ",
        "    E         E  ",
        "   E           E ",
        "   E           E ",
        "  EA           AE",
        "  E             E",
        "  EA           AE",
        "  E             E",
        "  EA           AE",
        "   E           E ",
        "   E           E ",
        "    E         E  ",
        "     EEA A AEE   ",
        "       EEEEE     "
    }, {
        "       EEEEE     ",
        "     EEAAAAAEE   ",
        "    EAA     AAE  ",
        "   EA         AE ",
        "   EA         AE ",
        "  EA           AE",
        "  EA           AE",
        "  EA           AE",
        "  EA           AE",
        "  EA           AE",
        "   EA         AE ",
        "   EA         AE ",
        "    EAA     AAE  ",
        "     EEAAAAAEE   ",
        "       EEEEE     "
    }, {
        "       EEEEE     ",
        "     EEA A AEE   ",
        "    E         E  ",
        "   E           E ",
        "   E           E ",
        "  EA           AE",
        "  E             E",
        "  EA           AE",
        "  E             E",
        "  EA           AE",
        "   E           E ",
        "   E           E ",
        "    E         E  ",
        "     EEA A AEE   ",
        "       EEEEE     "
    }, {
        "        EEE      ",
        "     EEEAAAEEE   ",
        "    E         E  ",
        "   E           E ",
        "   E           E ",
        "   E           E ",
        "  EA           AE",
        "  EA           AE",
        "  EA           AE",
        "   E           E ",
        "   E           E ",
        "   E           E ",
        "    E         E  ",
        "     EEEAAAEEE   ",
        "        EEE      "
    }, {
        "                 ",
        "      EEEEEEE    ",
        "     E   A   E   ",
        "    E         E  ",
        "   E           E ",
        "   E           E ",
        "   E           E ",
        "   EA         AE ",
        "   E           E ",
        "   E           E ",
        "   E           E ",
        "    E         E  ",
        "     E   A   E   ",
        "      EEEEEEE    ",
        "                 "
    }, {
        "                 ",
        "       EEEEE     ",
        "      E  A  E    ",
        "     E       E   ",
        "    E         E  ",
        "   E           E ",
        "   E           E ",
        "   EA         AE ",
        "   E           E ",
        "   E           E ",
        "    E         E  ",
        "     E       E   ",
        "      E  A  E    ",
        "       EEEEE     ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "       EEEEE     ",
        "      E  A  E    ",
        "     E   A   E   ",
        "    E         E  ",
        "    E         E  ",
        "    EAA     AAE  ",
        "    E         E  ",
        "    E         E  ",
        "     E   A   E   ",
        "      E  A  E    ",
        "       EEEEE     ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "       EEEEE     ",
        "      EEEEEEE    ",
        "     EEE A EEE   ",
        "     EE  A  EE   ",
        "     EEAAAAAEE   ",
        "     EE  A  EE   ",
        "     EEE A EEE   ",
        "      EEEEEEE    ",
        "       EEEEE     ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "         BB      ",
        "                 ",
        "        EEE      ",
        "       EEEEE     ",
        "       EEEEE     ",
        "       EEEEE     ",
        "        EEE      ",
        "                 ",
        "        BB       ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "           B     ",
        "            B    ",
        "                 ",
        "        DDD      ",
        "        DCD      ",
        "        DDD      ",
        "                 ",
        "      B          ",
        "       B         ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "             B   ",
        "        DDD  B   ",
        "        DCD      ",
        "     B  DDD      ",
        "     B           ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "     B  DDD      ",
        "     B  DCD  B   ",
        "        DDD  B   ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "      B          ",
        "     B           ",
        "        DDD      ",
        "        DCD      ",
        "        DDD      ",
        "             B   ",
        "            B    ",
        "                 ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "       BB        ",
        "                 ",
        "                 ",
        "        DDD      ",
        "        DCD      ",
        "        DDD      ",
        "                 ",
        "                 ",
        "          BB     ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "         BB      ",
        "                 ",
        "                 ",
        "        DDD      ",
        "        DCD      ",
        "        DDD      ",
        "                 ",
        "                 ",
        "        BB       ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "           B     ",
        "            B    ",
        "                 ",
        "        DDD      ",
        "        DCD      ",
        "        DDD      ",
        "                 ",
        "      B          ",
        "       B         ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "             B   ",
        "        DDD  B   ",
        "        DCD      ",
        "     B  DDD      ",
        "     B           ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "     B  CCC      ",
        "     B  CCC  B   ",
        "        CCC  B   ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "                 ",
        "      B          ",
        "     B CCCCC     ",
        "       CCCCC     ",
        "       CCCCC     ",
        "       CCCCC     ",
        "       CCCCC B   ",
        "            B    ",
        "                 ",
        "                 ",
        "                 ",
        "                 "
    }, {
        "                 ",
        "                 ",
        "                 ",
        "     FFFFFFFFF   ",
        "     FFFFFFFFF   ",
        "     FFFFFFFFF   ",
        "     FFFFFFFFF   ",
        "FFFFFFFFFFFFFF   ",
        "     FFFFFFFFF   ",
        "     FFFFFFFFF   ",
        "     FFFFFFFFF   ",
        "     FFFFFFFFF   ",
        "                 ",
        "                 ",
        "                 "
    }};


    // spotless:on

    private static final String[][] PATTERN_ADDON_90_CW = new String[PATTERN_ADDON.length][PATTERN_ADDON[0][0]
        .length()];

    private static final String[][] PATTERN_ADDON_90_CCW = new String[PATTERN_ADDON.length][PATTERN_ADDON[0][0]
        .length()];

    private static final String[][] PATTERN_ADDON_180 = new String[PATTERN_ADDON.length][PATTERN_ADDON[0].length];

    public static final Map<String, String> overlayMapping = new HashMap<>() {

        {
            put("Macerator", "macerator");
            put("OreWasher", "ore_washer");
            put("ChemicalBath", "chemical_bath");
            put("ThermalCentrifuge", "thermal_centrifuge");
            put("E_Furnace", "electric_furnace");
            put("ArcFurnace", "arc_furnace");
            put("Bender", "bender");
            put("Wiremill", "wiremill");
            put("Lathe", "lathe");
            put("Hammer", "hammer");
            put("Extruder", "extruder");
            put("FluidExtractor", "fluid_extractor");
            put("Compressor", "compressor");
            put("Press", "press");
            put("FluidSolidifier", "fluid_solidifier");
            put("Extractor", "extractor");
            put("LaserEngraver", "laser_engraver");
            put("Autoclave", "autoclave");
            put("Mixer", "mixer");
            put("AlloySmelter", "alloy_smelter");
            put("Electrolyzer", "electrolyzer");
            put("ElectromagneticSeparator", "electromagnetic_separator");
            put("Recycler", "recycler");
            put("Massfab", "amplifab");
            put("Centrifuge", "centrifuge");
            put("Cutter", "cutter");
            put("Assembler", "assembler");
            put("CircuitAssembler", "circuitassembler");
        }
    };

    public static final Map<String, Field> recipeBackendRefMapping = new HashMap<>() {

        {
            try {
                put("Macerator", RecipeMaps.class.getDeclaredField("maceratorRecipes"));
                put("OreWasher", RecipeMaps.class.getDeclaredField("oreWasherRecipes"));
                put("ChemicalBath", RecipeMaps.class.getDeclaredField("chemicalBathRecipes"));
                put("ThermalCentrifuge", RecipeMaps.class.getDeclaredField("thermalCentrifugeRecipes"));
                put("E_Furnace", RecipeMaps.class.getDeclaredField("furnaceRecipes"));
                put("ArcFurnace", RecipeMaps.class.getDeclaredField("arcFurnaceRecipes"));
                put("Bender", RecipeMaps.class.getDeclaredField("benderRecipes"));
                put("Wiremill", RecipeMaps.class.getDeclaredField("wiremillRecipes"));
                put("Lathe", RecipeMaps.class.getDeclaredField("latheRecipes"));
                put("Hammer", RecipeMaps.class.getDeclaredField("hammerRecipes"));
                put("Extruder", RecipeMaps.class.getDeclaredField("extruderRecipes"));
                put("FluidExtractor", RecipeMaps.class.getDeclaredField("fluidExtractionRecipes"));
                put("Compressor", RecipeMaps.class.getDeclaredField("compressorRecipes"));
                put("Press", RecipeMaps.class.getDeclaredField("formingPressRecipes"));
                put("FluidSolidifier", RecipeMaps.class.getDeclaredField("fluidSolidifierRecipes"));
                put("Extractor", RecipeMaps.class.getDeclaredField("extractorRecipes"));
                put("LaserEngraver", RecipeMaps.class.getDeclaredField("laserEngraverRecipes"));
                put("Autoclave", RecipeMaps.class.getDeclaredField("autoclaveRecipes"));
                put("Mixer", RecipeMaps.class.getDeclaredField("mixerRecipes"));
                put("AlloySmelter", RecipeMaps.class.getDeclaredField("alloySmelterRecipes"));
                put("Electrolyzer", RecipeMaps.class.getDeclaredField("electrolyzerRecipes"));
                put("ElectromagneticSeparator", RecipeMaps.class.getDeclaredField("electroMagneticSeparatorRecipes"));
                put("Recycler", RecipeMaps.class.getDeclaredField("recyclerRecipes"));
                put("Massfab", RecipeMaps.class.getDeclaredField("massFabFakeRecipes"));
                put("Centrifuge", RecipeMaps.class.getDeclaredField("centrifugeRecipes"));
                put("Cutter", RecipeMaps.class.getDeclaredField("cutterRecipes"));
                put("Assembler", RecipeMaps.class.getDeclaredField("assemblerRecipes"));
                put("CircuitAssembler", RecipeMaps.class.getDeclaredField("circuitAssemblerRecipes"));
                put("Diesel", RecipeMaps.class.getDeclaredField("dieselFuels"));
                put("Gas_Turbine", RecipeMaps.class.getDeclaredField("gasTurbineFuels"));
                put("Naquadah_1", RecipeMaps.class.getDeclaredField("smallNaquadahReactorFuels"));
                put("Naquadah_2", RecipeMaps.class.getDeclaredField("largeNaquadahReactorFuels"));
                put("Naquadah_3", RecipeMaps.class.getDeclaredField("hugeNaquadahReactorFuels"));
                put("Naquadah_4", RecipeMaps.class.getDeclaredField("extremeNaquadahReactorFuels"));
                put("Naquadah_5", RecipeMaps.class.getDeclaredField("ultraHugeNaquadahReactorFuels"));
                put("Semi_Fluid", GTPPRecipeMaps.class.getDeclaredField("semiFluidFuels"));
                // no recipe map for steam, 2MB steam for 1EU, and 1/80mb distilled water, 1 mb sc for 100EU, and 1mb
                // steam
            } catch (Exception e) {

            }
        }
    };

    // added by getDieselGeneratorsForArray in postInit phase
    public static final Map<String, ItemStack[]> GENERATORS = new HashMap<>();

    public static final String[] GENERATOR_TYPES = new String[] { "ASP_Solar", "EMT_Solar", "Diesel", "Naquadah",
        "Semi_Fluid", "Gas_Turbine", "Steam_Turbine" };

    private static final Map<String, float[]> GENERATOR_EFFICIENCY = new HashMap<>() {

        {
            put("Diesel", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Gas_Turbine", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Steam_Turbine", new float[] { 1f, 1f, 1f });
            put("Semi_Fluid", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Naquadah", new float[] { 0.8f, 1.0f, 1.5f, 2.0f, 2.5f });
        }
    };

    private static List<Pair<Block, Integer>> FRAMES;

    private static List<Pair<Block, Integer>> PARALLELISM_CASINGS;

    private static List<Pair<Block, Integer>> MACHINE_CASINGS;
    /*
     * core Structure:
     * Blocks:
     * A -> ofBlock...(BW_GlasBlocks, 5, ...); --channel that restricts energy hatch
     * B -> ofBlock...(block.Pikyonium64B.frame, 0, ...); --channel that restricts machine level that array can accept
     * C -> ofBlock...(gt.blockcasings, 6, ...); -- (Machine casing)casing that restricts dynamo hatch
     * D -> ofBlock...(gt.blockcasings4, 0, ...); --robust tungstensteel
     * E -> ofBlock...(gt.blockcasings4, 10, ...); --stainless steel, cheap
     * Tiles:
     * Special Tiles:
     * F -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...); Laser vacuum pipe casing
     * Structure:
     * Blocks:
     * A -> ofBlock...(BW_GlasBlocks, 5, ...); --channel that restricts energy hatch
     * B -> ofBlock...(MetaBlockCasing01, 3, ...); -- casing that gives additional parallelism
     * C -> ofBlock...(block.Pikyonium64B.frame, 0, ...); --channel that restricts machine level that array can accept
     * D -> ofBlock...(gt.blockcasings4, 10, ...); -- stainless steel, cheap
     * E -> ofBlock...(gt.blockcasings5, 0, ...); --coil that gives bonus
     * Tiles:
     * Special Tiles:
     * D -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...); // You will probably want to change
     * it to something else
     */

    private static IStructureDefinition<TST_BigBroArray> STRUCTURE_DEFINITION;

    @SideOnly(Side.CLIENT)
    private ITexture[] activeTextures;

    @SideOnly(Side.CLIENT)
    private ITexture[] idleTextures;

    public static void initializeMaterials() {
        MACHINE_CASINGS = IntStream.range(0, tierNamesCasing.length)
            .mapToObj(i -> Pair.of(i, tierNamesCasing[i]))
            .map(pair -> {
                String name = "Casing_" + pair.getValue();
                try {

                    ItemStack itemStack = ItemList.valueOf(name)
                        .get(1);
                    int level = itemStack.getItemDamage();
                    return Pair.of(Block.getBlockFromItem(itemStack.getItem()), level);
                } catch (Exception ex) {
                    ItemStack itemStack = CustomItemList.valueOf(name)
                        .get(1);
                    int level = itemStack.getItemDamage();
                    return Pair.of(Block.getBlockFromItem(itemStack.getItem()), level);
                } catch (IllegalAccessError error) {
                    // todo
                    TwistSpaceTechnology.LOG.warn("TST BigBroArray An error has occurred: {}", error.getMessage());
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        FRAMES = Arrays.asList(
            Pair.of(
                Block.getBlockFromItem(
                    MaterialsAlloy.ARCANITE.getFrameBox(1)
                        .getItem()),
                0), // IV
            Pair.of(
                Block.getBlockFromItem(
                    MaterialsAlloy.ZERON_100.getFrameBox(1)
                        .getItem()),
                0), // LuV
            Pair.of(
                Block.getBlockFromItem(
                    MaterialsAlloy.PIKYONIUM.getFrameBox(1)
                        .getItem()),
                0), // ZPM
            Pair.of(
                Block.getBlockFromItem(
                    MaterialsAlloy.BOTMIUM.getFrameBox(1)
                        .getItem()),
                0), // UV
            Pair.of(
                Block.getBlockFromItem(
                    MaterialsAlloy.ABYSSAL.getFrameBox(1)
                        .getItem()),
                0), // UHV
            Pair.of(
                Block.getBlockFromItem(
                    MaterialsAlloy.QUANTUM.getFrameBox(1)
                        .getItem()),
                0)); // UEV - MAX
        PARALLELISM_CASINGS = Arrays.asList(
            Pair.of(
                Block.getBlockFromItem(
                    GTCMItemList.ParallelismCasing0.get(1)
                        .getItem()),
                3),
            Pair.of(
                Block.getBlockFromItem(
                    GTCMItemList.ParallelismCasing1.get(1)
                        .getItem()),
                4),
            Pair.of(
                Block.getBlockFromItem(
                    GTCMItemList.ParallelismCasing2.get(1)
                        .getItem()),
                5),
            Pair.of(
                Block.getBlockFromItem(
                    GTCMItemList.ParallelismCasing3.get(1)
                        .getItem()),
                6),
            Pair.of(
                Block.getBlockFromItem(
                    GTCMItemList.ParallelismCasing4.get(1)
                        .getItem()),
                7));
    }

    public static void registerUUForArray() {
        // some workaround on UU
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidOutputs(Materials.UUMatter.getFluid(1))
            .eut(MTEMassfabricator.BASE_EUT)
            .duration(MTEMassfabricator.sDurationMultiplier)
            .addTo(RecipeMaps.massFabFakeRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(23))
            .fluidInputs(Materials.UUAmplifier.getFluid(MTEMassfabricator.sUUAperUUM))
            .fluidOutputs(Materials.UUMatter.getFluid(1))
            .eut(MTEMassfabricator.BASE_EUT)
            .duration(MTEMassfabricator.sDurationMultiplier / MTEMassfabricator.sUUASpeedBonus)
            .addTo(RecipeMaps.massFabFakeRecipes);
    }

    public static void getGeneratorsForArray() {
        TST_BigBroArray.GENERATORS.put(
            "Diesel",
            new ItemStack[] { ItemList.Generator_Diesel_LV.get(1), ItemList.Generator_Diesel_MV.get(1),
                ItemList.Generator_Diesel_HV.get(1), Loaders.Generator_Diesel[0], Loaders.Generator_Diesel[1] });
        TST_BigBroArray.GENERATORS.put(
            "Steam_Turbine",
            new ItemStack[] { ItemList.Generator_Steam_Turbine_LV.get(1), ItemList.Generator_Steam_Turbine_MV.get(1),
                ItemList.Generator_Steam_Turbine_HV.get(1), });
        TST_BigBroArray.GENERATORS.put(
            "Gas_Turbine",
            new ItemStack[] { ItemList.Generator_Gas_Turbine_LV.get(1), ItemList.Generator_Gas_Turbine_MV.get(1),
                ItemList.Generator_Gas_Turbine_HV.get(1), ItemList.Generator_Gas_Turbine_EV.get(1),
                ItemList.Generator_Gas_Turbine_IV.get(1), });

        TST_BigBroArray.GENERATORS.put(
            "Semi_Fluid",
            new ItemStack[] { GregtechItemList.Generator_SemiFluid_LV.get(1),
                GregtechItemList.Generator_SemiFluid_MV.get(1), GregtechItemList.Generator_SemiFluid_HV.get(1),
                GregtechItemList.Generator_SemiFluid_EV.get(1), GregtechItemList.Generator_SemiFluid_IV.get(1) });

        TST_BigBroArray.GENERATORS.put(
            "Naquadah",
            new ItemStack[] { ItemList.Generator_Naquadah_Mark_I.get(1), ItemList.Generator_Naquadah_Mark_II.get(1),
                ItemList.Generator_Naquadah_Mark_III.get(1), ItemList.Generator_Naquadah_Mark_IV.get(1),
                ItemList.Generator_Naquadah_Mark_V.get(1), });

        TST_BigBroArray.GENERATORS.put(
            "ASP_Solar",
            new ItemStack[] { new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1), // LV
                new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1, 1), // MV
                new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1, 2), // HV
                new ItemStack(ItemBlock.getItemFromBlock(AdvancedSolarPanel.blockAdvSolarPanel), 1, 3), // EV
                new ItemStack(MainSSP.BlockSpectralSP, 1), // IV
                new ItemStack(MainSSP.BlockSingularSP, 1), // LuV
                new ItemStack(MainSSP.BlockAdminSP, 1), // ZPM
                new ItemStack(MainSSP.BlockPhotonSP, 1) // UV
            });

        List<ItemStack> EMTSolars = new ArrayList<>();
        for (int i = 0; i < Solars.getCountOfInstances(); i++) {
            BlockSolars emtSolars;
            if (i == 0) {
                emtSolars = (BlockSolars) GameRegistry.findBlock(EMT.MOD_ID, "EMTSolars");

            } else {
                emtSolars = (BlockSolars) GameRegistry.findBlock(EMT.MOD_ID, "EMTSolars" + (i + 1));
            }
            for (int meta = 0; meta < emtSolars.countOfMetas; meta++) {
                EMTSolars.add(new ItemStack(emtSolars, 1, meta));
            }
        }
        TST_BigBroArray.GENERATORS.put("EMT_Solar", EMTSolars.toArray(new ItemStack[0]));

    }

    public static int getFrameTier(Block block, int meta) {
        for (int i = 0; i < FRAMES.size(); i++) {
            if (block == FRAMES.get(i)
                .getKey()) {
                return i + 1;
            }
        }
        return 0;
    }

    public static Integer getParallelismCasingTier(Block block, int meta) {
        if (block == GTCMItemList.ParallelismCasing0.getBlock()) {
            return meta - 2;
        }
        return null;
    }

    public void setCoilTier(HeatingCoilLevel level) {
        this.coilTier = coilTier != HeatingCoilLevel.None ? (level.getTier() < coilTier.getTier() ? level : coilTier)
            : level;
    }

    public HeatingCoilLevel getCoilTier() {
        return coilTier;
    }

    public void setCasingTier(int casingTier) {
        this.casingTier = this.casingTier >= 0 ? Math.min(casingTier, this.casingTier) : casingTier;
    }

    public int getCasingTier() {
        return casingTier;
    }

    public static void initializeStructure() {

        for (int i = 0; i < PATTERN_ADDON.length; i++) {
            for (int j = 0; j < PATTERN_ADDON[i].length; j++) {
                PATTERN_ADDON[i][j] = PATTERN_ADDON[i][j].replace('A', 'G')
                    .replace('B', 'H')
                    .replace('C', 'I')
                    .replace('D', 'J')
                    .replace('E', 'K')
                    .replace('F', 'L');
            }
        }

        for (int i = 0; i < PATTERN_ADDON.length; i++) {
            for (int j = 0; j < PATTERN_ADDON[0].length; j++) {
                // cw 180 addon
                PATTERN_ADDON_180[i][j] = StringUtils.reverse(PATTERN_ADDON[i][j]);
            }
        }
        for (int i = 0; i < PATTERN_ADDON.length; i++) {
            for (int k = 0; k < PATTERN_ADDON[0][0].length(); k++) {
                String rotated = "";
                for (int j = 0; j < PATTERN_ADDON[0].length; j++) {
                    // cw 90 addon
                    rotated += PATTERN_ADDON[i][j].charAt(k);
                }
                PATTERN_ADDON_90_CW[i][k] = rotated;
            }
        }

        for (int i = 0; i < PATTERN_ADDON_90_CW.length; i++) {
            for (int j = 0; j <= PATTERN_ADDON_90_CW.length / 2; j++) {
                PATTERN_ADDON_90_CCW[i][j] = PATTERN_ADDON_90_CW[i][PATTERN_ADDON_90_CW[0].length - 1 - j];
                PATTERN_ADDON_90_CCW[i][PATTERN_ADDON_90_CW[0].length - 1 - j] = PATTERN_ADDON_90_CW[i][j];
            }
        }

        StructureDefinition.Builder<TST_BigBroArray> builder = StructureDefinition.<TST_BigBroArray>builder()
            .addShape("core", StructureUtility.transpose(PATTERN_CORE))
            .addElement(
                'D',
                HatchElementBuilder.<TST_BigBroArray>builder()
                    .atLeast(
                        gregtech.api.enums.HatchElement.Maintenance,
                        gregtech.api.enums.HatchElement.InputBus.or(gregtech.api.enums.HatchElement.InputHatch),
                        gregtech.api.enums.HatchElement.OutputBus.or(gregtech.api.enums.HatchElement.OutputHatch),
                        gregtech.api.enums.HatchElement.Muffler)
                    .adder(TST_BigBroArray::addToMachineList)
                    .dot(1)
                    .casingIndex(((BlockCasingsAbstract) GregTechAPI.sBlockCasings4).getTextureIndex(0))
                    .buildAndChain(GregTechAPI.sBlockCasings4, 0))
            .addElement(
                'A',
                StructureUtility.withChannel(
                    "glass",
                    BorosilicateGlass.ofBoroGlass(
                        (byte) -2,
                        (te, tier) -> te.glassTier = te.glassTier >= 0 ? Math.min(tier, te.glassTier) : tier,
                        (te) -> (byte) te.glassTier)))
            .addElement(
                'B',
                StructureUtility.withChannel(
                    "frame",
                    StructureUtility.ofBlocksTiered(
                        TST_BigBroArray::getFrameTier,
                        FRAMES,
                        -1,
                        (te, tier) -> te.frameTier = te.frameTier >= 0 ? Math.min(tier, te.frameTier) : tier,
                        (te) -> te.frameTier)))
            .addElement('E', StructureUtility.ofBlock(GregTechAPI.sBlockCasings4, 1))
            .addElement(
                'F',
                HatchElementBuilder.<TST_BigBroArray>builder()
                    .atLeast(
                        HatchElement.DynamoMulti.or(gregtech.api.enums.HatchElement.ExoticEnergy)
                            .or(gregtech.api.enums.HatchElement.Dynamo))
                    .adder(TST_BigBroArray::addToMachineList)
                    .dot(2)
                    .casingIndex(((BlockCasingsAbstract) GregTechAPI.sBlockCasings4).getTextureIndex(1))
                    .buildAndChain(GregTechAPI.sBlockCasings4, 1))
            .addElement(
                'C',
                StructureUtility.withChannel(
                    "casing",
                    StructureUtility.ofBlocksTiered(
                        (block, meta) -> meta,
                        MACHINE_CASINGS,
                        -1,
                        TST_BigBroArray::setCasingTier,
                        TST_BigBroArray::getCasingTier)));
        List<String[][]> strings = Arrays
            .asList(PATTERN_ADDON, PATTERN_ADDON_90_CW, PATTERN_ADDON_180, PATTERN_ADDON_90_CCW);
        for (int i = 0; i < strings.size(); i++) {
            String[][] pattern = strings.get(i);
            builder = builder.addShape("addon" + i, StructureUtility.transpose(pattern))
                .addElement(
                    'I',
                    StructureUtility.withChannel(
                        "coil",
                        GTStructureUtility.ofCoil(TST_BigBroArray::setCoilTier, TST_BigBroArray::getCoilTier)))
                .addElement(
                    'H',
                    StructureUtility.withChannel(
                        "frame",
                        StructureUtility.ofBlocksTiered(
                            TST_BigBroArray::getFrameTier,
                            FRAMES,
                            -1,
                            (te, tier) -> te.frameTier = te.frameTier >= 0 ? Math.min(tier, te.frameTier) : tier,
                            (te) -> te.frameTier)))
                .addElement(
                    'K',
                    StructureUtility.withChannel(
                        "glass",
                        BorosilicateGlass.ofBoroGlass(
                            (byte) -1,
                            (te, tier) -> te.glassTier = te.glassTier >= 0 ? Math.min(tier, te.glassTier) : tier,
                            (te) -> (byte) te.glassTier)))
                .addElement(
                    'G',
                    StructureUtility.withChannel(
                        "parallelism",
                        StructureUtility.ofBlocksTiered(
                            TST_BigBroArray::getParallelismCasingTier,
                            PARALLELISM_CASINGS,
                            0,
                            (te, tier) -> { te.parallelismTier = Math.max(tier, te.parallelismTier); },
                            (te) -> te.parallelismTier)))
                .addElement('L', StructureUtility.ofBlock(GregTechAPI.sBlockCasings4, 1))
                .addElement('J', StructureUtility.ofBlock(GregTechAPI.sBlockCasings2, 5));
        }
        STRUCTURE_DEFINITION = builder.build();
    }

    @SideOnly(Side.CLIENT)
    public static void initializeDefaultTextures() {
        DEFAULT_FRONT_ACTIVE = new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 0)),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                .extFacing()
                .glow()
                .build(), };

        DEFAULT_FRONT_IDLE = new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 0)),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                .extFacing()
                .glow()
                .build(), };

        DEFAULT_CASING_TEXTURE = new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 0)) };

    }

    @SideOnly(Side.CLIENT)
    private ITexture[] getActiveTextures(String machineType) {
        if (StringUtils.isEmpty(machineType)) {
            return DEFAULT_FRONT_ACTIVE;
        }
        String overlay = overlayMapping.get(machineType);
        if (overlay == null) {
            return DEFAULT_FRONT_ACTIVE;
        }
        String front = String.format("basicmachines/%s/OVERLAY_FRONT_ACTIVE", overlay);
        String frontGlow = String.format("basicmachines/%s/OVERLAY_FRONT_ACTIVE_GLOW", overlay);
        Textures.BlockIcons.CustomIcon frontIcon = new Textures.BlockIcons.CustomIcon(front);
        frontIcon.run();
        Textures.BlockIcons.CustomIcon frontGlowIcon = new Textures.BlockIcons.CustomIcon(frontGlow);
        frontGlowIcon.run();
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ROBUST_TUNGSTENSTEEL)
            .build(),
            TextureFactory.builder()
                .addIcon(frontIcon)
                .build(),
            TextureFactory.builder()
                .addIcon(frontGlowIcon)
                .glow()
                .build() };
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick_EM(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @SideOnly(Side.CLIENT)
    private ITexture[] getIdleTextures(String machineType) {
        if (StringUtils.isEmpty(machineType)) {
            return DEFAULT_FRONT_IDLE;
        }
        String overlay = overlayMapping.get(machineType);
        if (overlay == null) {
            return DEFAULT_FRONT_IDLE;
        }
        String front = String.format("basicmachines/%s/OVERLAY_FRONT", overlay);
        String frontGlow = String.format("basicmachines/%s/OVERLAY_FRONT_GLOW", overlay);
        Textures.BlockIcons.CustomIcon frontIcon = new Textures.BlockIcons.CustomIcon(front);
        frontIcon.run();
        Textures.BlockIcons.CustomIcon frontGlowIcon = new Textures.BlockIcons.CustomIcon(frontGlow);
        frontGlowIcon.run();
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ROBUST_TUNGSTENSTEEL)
            .build(),
            TextureFactory.builder()
                .addIcon(frontIcon)
                .build(),
            TextureFactory.builder()
                .addIcon(frontGlowIcon)
                .glow()
                .build() };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (machineType != null) {
            aNBT.setString("machineType", machineType);
        }
        aNBT.setInteger("tier", machineTier);
        aNBT.setLong("maxParallelism", maxParallelism);
        aNBT.setLong("actualParallelism", actualParallelism);
        if (mode != null) aNBT.setString("mode", mode);

        if (generatorTE != null) {
            NBTTagCompound compound = new NBTTagCompound();
            generatorTE.writeToNBT(compound);
            aNBT.setTag("solarTE", compound);
        }
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        if (machines != null) {
            nbtTagCompound.setShort("id", (short) Item.getIdFromItem(machines.getItem()));
            nbtTagCompound.setInteger("Count", machines.stackSize);
            nbtTagCompound.setShort("Damage", (short) machines.getItemDamage());

            if (machines.getTagCompound() != null) {
                nbtTagCompound.setTag("tag", machines.getTagCompound());
            }
            /*
             * machines.writeToNBT(nbtTagCompound);
             */
        }
        if (ownerUUID != null) {
            aNBT.setString("owner", ownerUUID.toString());
        }
        aNBT.setTag("machines", nbtTagCompound);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineType = aNBT.getString("machineType");
        mode = aNBT.getString("mode");
        machineTier = aNBT.getInteger("tier");
        maxParallelism = aNBT.getLong("maxParallelism");
        actualParallelism = aNBT.getLong("actualParallelism");
        if (aNBT.hasKey("machines")) {
            // ItemStack.loadItemStackFromNBT()
            NBTTagCompound compound = aNBT.getCompoundTag("machines");
            ItemStack itemStack = new ItemStack(Item.getItemById(compound.getShort("id")));
            itemStack.stackSize = compound.getInteger("Count");
            itemStack.setItemDamage(compound.getShort("Damage"));
            if (compound.hasKey("tag")) {
                itemStack.setTagCompound(compound.getCompoundTag("tag"));
            }
            machines = itemStack;
        }
        if (aNBT.hasKey("solarTE")) {
            NBTTagCompound compound = aNBT.getCompoundTag("solarTE");
            generatorTE = Block.getBlockFromItem(machines.getItem())
                .createTileEntity(null, machines.getItemDamage());
            generatorTE.readFromNBT(compound);
        }
        if (aNBT.hasKey("owner")) {
            ownerUUID = UUID.fromString(aNBT.getString("owner"));
        }
    }

    public TST_BigBroArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.useLongPower = true;
    }

    public TST_BigBroArray(String aName) {
        super(aName);
        this.useLongPower = true;
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        GTCM_ProcessingLogic gtcm_processingLogic = new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                return recipe.mEUt <= 8 * Math.pow(4, machineTier) ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientMachineTier((int) (Math.log(recipe.mEUt) / Math.log(4)));
            }
        };
        gtcm_processingLogic.setMaxParallelSupplier(
            () -> machines != null ? (int) Math.min(machines.stackSize * Math.pow(2, parallelismTier), maxParallelism)
                : 1);
        return gtcm_processingLogic;
    }

    private long getDieselFuelValue(FluidStack stack) {
        RecipeMap<?> tRecipes = getRecipeMap();
        if (stack == null || !(tRecipes.getBackend() instanceof FuelBackend tFuels)) return 0;
        GTRecipe tFuel = tFuels.findFuel(stack);
        if (tFuel == null) return 0;
        return tFuel.mSpecialValue;
    }

    private GTRecipe getNaquadahFuelRecipe(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack) || getRecipeMap() == null) return null;

        GTRecipe tFuel = getRecipeMap().findRecipeQuery()
            .items(stack)
            .fluids()
            .specialSlot(null)
            .voltage(Long.MAX_VALUE)
            .cachedRecipe(null)
            .notUnificated(false)
            .dontCheckStackSizes(false)
            .find();
        return tFuel;
    }

    void consumeFuel(String machineType, FluidStack storedFluid, long liquidFuelValue) {
        float effiency = GENERATOR_EFFICIENCY.get(machineType)[machineTier - 1];
        long expectedGeneration = (8L << (machineTier * 2L)) * actualParallelism * 20;
        long energyInFuel = (long) (storedFluid.amount * liquidFuelValue * effiency);
        long fuelConsumption = (long) (expectedGeneration / liquidFuelValue / effiency);
        output = BigInteger.valueOf(Math.min(energyInFuel, expectedGeneration));
        storedFluid.amount = (int) Math.max(0, storedFluid.amount - fuelConsumption);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing_EM() {
        startRecipeProcessing();
        CheckRecipeResult result;
        if (MODE_PROCESSOR.equals(mode)) {
            result = super.checkProcessing_EM();
        } else if (MODE_GENERATOR.equals(mode)) {
            mMaxProgresstime = 20;
            if ("ASP_Solar".equals(machineType) || "EMT_Solar".equals(machineType)) {
                int xCoord = getBaseMetaTileEntity().getXCoord();
                int yCoord = getBaseMetaTileEntity().getYCoord() + 4;
                int zCoord = getBaseMetaTileEntity().getZCoord();
                generatorTE.xCoord = xCoord;
                generatorTE.yCoord = yCoord;
                generatorTE.zCoord = zCoord;
                if (!generatorTE.hasWorldObj()) {
                    generatorTE.setWorldObj(getBaseMetaTileEntity().getWorld());
                }
                double parallelismBlockBoost = Math.pow(1.5, parallelismTier);
                double coilBoost = Math.pow(1.1, coilTier.getTier());
                BigDecimal eut;
                if (generatorTE instanceof TileEntitySolarPanel te) {
                    te.updateEntity();
                    eut = BigDecimal.valueOf(te.storage);
                    te.storage = 0;
                } else if (generatorTE instanceof TileEntitySolarBase te) {
                    te.checkConditions();
                    te.updateEntity();
                    eut = BigDecimal.valueOf(te.generating);
                    te.energySource.drawEnergy(te.energySource.getEnergyStored());
                } else {
                    endRecipeProcessing();
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                output = eut.multiply(BigDecimal.valueOf(parallelismBlockBoost))
                    .multiply(BigDecimal.valueOf(coilBoost))
                    .multiply(BigDecimal.valueOf(actualParallelism))
                    .multiply(BigDecimal.valueOf(20))
                    .toBigInteger();
                result = CheckRecipeResultRegistry.SUCCESSFUL;
            } else if ("Diesel".equals(machineType) || "Semi_Fluid".equals(machineType)
                || "Gas_Turbine".equals(machineType)) {
                    result = CheckRecipeResultRegistry.NO_RECIPE;
                    for (FluidStack storedFluid : getStoredFluids()) {
                        long liquidFuelValue = getDieselFuelValue(storedFluid);
                        if (liquidFuelValue > 0) {
                            consumeFuel(machineType, storedFluid, liquidFuelValue);
                            result = CheckRecipeResultRegistry.SUCCESSFUL;
                            break;
                        }
                    }
                } else if ("Steam_Turbine".equals(machineType)) {
                    result = CheckRecipeResultRegistry.NO_RECIPE;
                    for (FluidStack storedFluid : getStoredFluids()) {
                        if (GTModHandler.isAnySteam(storedFluid)) {
                            long liquidFuelValue = 3;
                            consumeFuel(machineType, storedFluid, liquidFuelValue);
                            result = CheckRecipeResultRegistry.SUCCESSFUL;
                            break;
                        }
                    }
                } else if ("Naquadah".equals(machineType)) {
                    result = CheckRecipeResultRegistry.NO_RECIPE;
                    List<ItemStack> outputItems = new ArrayList<>();
                    for (ItemStack storedInput : getStoredInputs()) {
                        GTRecipe recipe = getNaquadahFuelRecipe(storedInput);
                        if (recipe != null) {
                            long fuelValue = recipe.mSpecialValue * 1000;
                            for (ItemStack out : recipe.mOutputs) {
                                if (out != null) {
                                    ItemStack stack = ItemStack.copyItemStack(out);
                                    // 暂时不知道怎么处理，捏妈的
                                    // 如果是Long.MAX个2333
                                    // 谁知道什么时候会有人丢那么多个螺栓呢
                                    stack.stackSize = (int) Math
                                        .min(Math.min(storedInput.stackSize, actualParallelism), 2147483647L);
                                    outputItems.add(stack);
                                }
                            }
                            float effiency = GENERATOR_EFFICIENCY.get("Naquadah")[machineTier - 4];
                            long machineEUt = (8L << (machineTier * 2L)) * actualParallelism;
                            long expectedGeneration = (long) (fuelValue * effiency * actualParallelism);
                            long energyInFuel = (long) (storedInput.stackSize * fuelValue * effiency);
                            long fuelConsumption = (long) (expectedGeneration / fuelValue / effiency);
                            output = BigInteger.valueOf(Math.min(energyInFuel, expectedGeneration));
                            mMaxProgresstime = MathUtils.bigToInt(output.divide(BigInteger.valueOf(machineEUt)));
                            storedInput.stackSize = (int) Math.max(0, storedInput.stackSize - fuelConsumption);
                            result = CheckRecipeResultRegistry.SUCCESSFUL;
                            break;
                        }
                    }
                    mOutputItems = outputItems.toArray(new ItemStack[0]);
                } else {
                    result = CheckRecipeResultRegistry.NO_RECIPE;
                }
            if (result == CheckRecipeResultRegistry.SUCCESSFUL) {
                outEUt = output.divide(BigInteger.valueOf(mMaxProgresstime));
                if (isWirelessMode) {
                    if (ownerUUID == null) {
                        result = CheckRecipeResultRegistry.INTERNAL_ERROR;
                    } else {
                        lEUt = MathUtils.bigToLong(outEUt);
                        addEUToGlobalEnergyMap(ownerUUID, output);
                    }
                } else {
                    long out = MathUtils.bigToLong(output);
                    lEUt = out / mMaxProgresstime;
                    fillAllDynamos(out);
                }
            }
        } else {
            result = CheckRecipeResultRegistry.NO_RECIPE;
        }
        endRecipeProcessing();
        return result;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget(String.format("Generating: %sEU/t", Out_Format.format(outEUt)))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(
                        widget -> getErrorDisplayID() == 0 && getBaseMetaTileEntity().isActive()
                            && MODE_GENERATOR.equals(mode)))
            .widget(new FakeSyncWidget.StringSyncer(() -> mode, (mode) -> TST_BigBroArray.this.mode = mode))
            .widget(new FakeSyncWidget.StringSyncer(() -> outEUt.toString(), (l) -> outEUt = new BigInteger(l)))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> mMaxProgresstime, (i) -> mMaxProgresstime = i));

        screenElements.widget(
            new TextWidget("Machine state: " + mode).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()));

        screenElements.widget(
            new TextWidget("Machine type:" + machineType).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.StringSyncer(() -> machineType, (type) -> machineType = type));;

        screenElements.widget(
            new TextWidget("Machine tier:" + machineTier).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> machineTier, (tier) -> machineTier = tier));
        screenElements.widget(
            new TextWidget("Provided parallelism:" + actualParallelism).setDefaultColor(0xFFEF00)
                .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.LongSyncer(() -> actualParallelism, (p) -> this.actualParallelism = p));

        screenElements
            .widget(
                new TextWidget("Speed boost:" + Math.pow(1.5, parallelismTier)).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> parallelismTier, (tier) -> parallelismTier = tier));

        screenElements.widget(
            new TextWidget("Addons attached:" + addonCount).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> addonCount, (ct) -> addonCount = ct));
    }

    private long fillAllDynamos(long energy) {
        for (MTEHatchDynamo dynamo : mDynamoHatches) {
            long drain = Math.min(energy, dynamo.maxEUStore() - dynamo.getEUVar());
            energy -= drain;
            dynamo.setEUVar(dynamo.getEUVar() + drain);
        }
        for (MTEHatchDynamoMulti dynamo : eDynamoMulti) {
            long drain = Math.min(energy, dynamo.maxEUStore() - dynamo.getEUVar());
            energy -= drain;
            dynamo.setEUVar(dynamo.getEUVar() + drain);
        }
        return energy;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return super.getStructureDescription(stackSize);
    }

    @Override
    protected MultiblockTooltipBuilder getTooltip() {
        MultiblockTooltipBuilder gt_multiblock_tooltip_builder = new MultiblockTooltipBuilder()
            .addMachineType(TextEnums.BigBroArrayType.toString())
            .addInfo(TextEnums.BigBroArrayName.toString())
            .addInfo(TextEnums.BigBroArrayDesc1.toString())
            .addInfo(TextEnums.BigBroArrayDesc2.toString())
            .addInfo(TextEnums.BigBroArrayDesc3.toString())
            .addInfo(TextEnums.BigBroArrayDesc4.toString())
            .addInfo(TextEnums.BigBroArrayDesc5.toString())
            .addInfo(TextEnums.BigBroArrayDesc6.toString())
            .addInfo(TextEnums.BigBroArrayDesc7.toString())
            .addInfo(TextEnums.BigBroArrayDesc8.toString())
            .addInfo(TextEnums.BigBroArrayDesc9.toString())
            .addInfo(TextEnums.BigBroArrayDesc10.toString())
            .addInfo(TextEnums.BigBroArrayDesc11.toString())
            .addInfo(TextEnums.BigBroArrayDesc12.toString())
            .addInfo(TextEnums.StructureTooComplex.toString())
            .addInfo(TextLocalization.BLUE_PRINT_INFO);
        gt_multiblock_tooltip_builder.toolTipFinisher(TextLocalization.ModName);
        return gt_multiblock_tooltip_builder;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (machines != null) {
            try {
                String key = "Naquadah".equals(machineType) ? machineType + "_" + (machineTier - 3) : machineType;
                Field field = recipeBackendRefMapping.get(key);
                if (field == null) {
                    return null;
                }
                RecipeMap<?> o = (RecipeMap<?>) field.get(null);
                return o;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece("core", itemStack, hintsOnly, 5, 5, 4);
        int addonCount = Math.min(itemStack.stackSize - 1, 4);
        if (addonCount == 4) {
            buildPiece("addon3", itemStack, hintsOnly, 7, 23, 21);
            addonCount--;
        }
        if (addonCount == 3) {
            buildPiece("addon2", itemStack, hintsOnly, 22, 23, 6);
            addonCount--;
        }
        if (addonCount == 2) {
            buildPiece("addon1", itemStack, hintsOnly, 7, 23, -7);
            addonCount--;
        }
        if (addonCount == 1) {
            buildPiece("addon0", itemStack, hintsOnly, -6, 23, 6);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int blockPlacedCount = 0;
        if (!mMachine) {
            blockPlacedCount += survivialBuildPiece("core", stackSize, 5, 5, 4, elementBudget, env, true);
        } else {
            switch (this.addonCount) {
                case 0:
                    blockPlacedCount += survivialBuildPiece("addon0", stackSize, -6, 23, 6, elementBudget, env, true);
                    break;
                case 1:
                    blockPlacedCount += survivialBuildPiece("addon1", stackSize, 7, 23, -7, elementBudget, env, true);
                    break;
                case 2:
                    blockPlacedCount += survivialBuildPiece("addon2", stackSize, 22, 23, 6, elementBudget, env, true);
                    break;
                case 3:
                    blockPlacedCount += survivialBuildPiece("addon2", stackSize, 7, 23, 21, elementBudget, env, true);
                    break;
                default:
                    break;
            }
        }
        return blockPlacedCount;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return getTooltip();
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        repairMachine();

        this.casingTier = -1;
        this.glassTier = -2;
        this.coilTier = HeatingCoilLevel.None;
        this.frameTier = -1;
        boolean checkPiece = checkPiece("core", 5, 5, 4);
        if (!checkPiece) return false;
        // dynamo hatch level follows casing level
        if (mDynamoHatches.isEmpty() && eDynamoMulti.isEmpty() && casingTier >= 12) {
            isWirelessMode = true;
        } else {
            for (MTEHatchDynamo mDynamoHatch : mDynamoHatches) {
                if (casingTier >= 12) {
                    continue;
                }
                if (mDynamoHatch.mTier > casingTier) {
                    return false;
                }
            }
            for (MTEHatchDynamoMulti gt_metaTileEntity_hatch_dynamoMulti : eDynamoMulti) {
                if (casingTier >= 12) {
                    continue;
                }
                if (gt_metaTileEntity_hatch_dynamoMulti.mTier > casingTier
                    || (gt_metaTileEntity_hatch_dynamoMulti instanceof MTEHatchDynamoTunnel && casingTier < 8)) {
                    return false;
                }
            }
        }
        // energy hatch level follows glass level
        for (MTEHatchEnergy mEnergyHatch : mEnergyHatches) {
            if (mEnergyHatch.mTier > glassTier) {
                return false;
            }
        }
        for (MTEHatchEnergyMulti gt_metaTileEntity_hatch_energyMulti : eEnergyMulti) {
            if (Math.min(gt_metaTileEntity_hatch_energyMulti.mTier, 12) > glassTier
                || (gt_metaTileEntity_hatch_energyMulti instanceof MTEHatchEnergyTunnel && glassTier < 6)) {
                return false;
            }
        }

        this.addonCount = 0;
        int p = 5;
        this.parallelismTier = 0;
        if (checkPiece("addon0", -6, 23, 6)) {
            this.addonCount += 1;
            p = Math.min(this.parallelismTier, p);
        }
        this.parallelismTier = 0;
        if (checkPiece("addon1", 7, 23, -7)) {
            this.addonCount += 1;
            p = Math.min(this.parallelismTier, p);
        }
        this.parallelismTier = 0;
        if (checkPiece("addon2", 22, 23, 6)) {
            this.addonCount += 1;
            p = Math.min(this.parallelismTier, p);
        }
        this.parallelismTier = 0;
        if (checkPiece("addon3", 7, 23, 21)) {
            this.addonCount += 1;
            p = Math.min(this.parallelismTier, p);
        }
        if (mMufflerHatches.size() <= 0) {
            return false;
        }
        if (this.addonCount > 0) {
            this.parallelismTier = p;
        } else {
            this.parallelismTier = 0;
        }
        // 5 is place holder, max tier is 4
        this.maxParallelism = calculateMaxParallelismByAddonTier();
        this.casingMultiplier = parallelismTier > 3 ? (parallelismTier + 6) : parallelismTier;
        this.machineCountForMaxParallelism = (int) (this.maxParallelism >> casingMultiplier);
        this.actualParallelism = calculateParallelismByAddonTier();
        processingLogic.setSpeedBonus((float) Math.pow(0.66, parallelismTier));
        processingLogic.setEuModifier((float) Math.pow(0.9, Math.max(0, coilTier.getTier())));
        return checkPiece;
    }

    private long calculateMaxParallelismByAddonTier() {
        if (addonCount > 0) {
            long infinity = 2147483647L << casingMultiplier;
            return parallelismTier >= 5 ? (infinity / 5) * (1 + this.addonCount)
                : (64L << ((parallelismTier) * 2)) * (1 + this.addonCount);
        } else {
            return 64;
        }
    }

    private long calculateParallelismByAddonTier() {
        long stackSize = (machines != null ? machines.stackSize : 0);
        int casingMultiplier = parallelismTier > 3 ? (parallelismTier + 6) : parallelismTier;
        long p = stackSize << casingMultiplier;
        long m = calculateMaxParallelismByAddonTier();
        return Math.min(p, m);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BigBroArray(mName);
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return (int) Math.min(actualParallelism, 10000);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aStack) {
        machineType = null;
        startRecipeProcessing();
        int maxTier = (frameTier >= 6 || addonCount == 0) ? 16 : frameTier + 4;
        MachineHintMessages hint = null;
        if (machines == null) {
            for (ItemStack storedInput : getStoredInputs()) {
                for (String[] machineType : PROCESSING_MACHINE_LIST) {
                    for (int i = 0; i < tierNames.length; i++) {
                        String tierName = tierNames[i];
                        String name = String.format("Machine_%s_%s", tierName, machineType[0]);
                        ItemStack machineTypeToBeUsed = null;
                        try {
                            ItemList itemList = ItemList.valueOf(name);
                            machineTypeToBeUsed = itemList.get(1);
                        } catch (IllegalArgumentException ex) {
                            name = String.format("%s%s", machineType[1], tierName);
                            try {
                                ItemList itemList = ItemList.valueOf(name);
                                machineTypeToBeUsed = itemList.get(1);
                            } catch (IllegalArgumentException e) {

                            }
                        }
                        if (machineTypeToBeUsed != null) {
                            if (GTUtility.areStacksEqual(machineTypeToBeUsed, storedInput, true)) {
                                // supports all machines when there's no additional strucutures or frame level >= 6
                                if (i + 1 <= maxTier) {
                                    if (machines != null) {
                                        if (GTUtility.areStacksEqual(machines, storedInput)) {
                                            int d = Math
                                                // every parallelism tier provides 4x parallelism each machine
                                                .min(
                                                    machineCountForMaxParallelism - machines.stackSize,
                                                    storedInput.stackSize);
                                            machines.stackSize += d;
                                            storedInput.stackSize -= d;
                                        }
                                    } else if (storedInput.stackSize > 0) { // or parallelism will be 0
                                        machines = storedInput.copy();
                                        machines.stackSize = Math
                                            .min(machineCountForMaxParallelism, storedInput.stackSize);
                                        storedInput.stackSize -= machines.stackSize;
                                        this.machineType = machineType[0];
                                        machineTier = i + 1;
                                        mode = MODE_PROCESSOR;
                                    }
                                } else {
                                    hint = MachineHintMessages.FAIL_TIER_MISMATCH;
                                }
                            }
                        }
                    }
                }

                for (String machineType : GENERATOR_TYPES) {
                    for (int i = 0; i < GENERATORS.get(machineType).length; i++) {
                        ItemStack machineTypeToBeUsed = GENERATORS.get(machineType)[i];
                        if (GTUtility.areStacksEqual(storedInput, machineTypeToBeUsed, true)) {
                            // create dummy TE for solar generation
                            generatorTE = Block.getBlockFromItem(machineTypeToBeUsed.getItem())
                                .createTileEntity(aPlayer.worldObj, machineTypeToBeUsed.getItemDamage());
                            int tier;
                            if ("Naquadah".equals(machineType)) {
                                tier = i + 4;
                            } else if ("EMT_Solar".equals(machineType)) {
                                tier = (int) Math
                                    .floor(Math.log(((TileEntitySolarBase) generatorTE).output / 8) / Math.log(4));
                            } else {
                                tier = i + 1;
                            }
                            // calculate tier with log
                            // (int)log(output / 8, 4) = LV(1), MV(2), HV(3), EV(4), IV(5), .......
                            // supports all machines when there's no additional strucutures or frame level >= 6

                            if (tier <= maxTier) {
                                if (machines != null) {
                                    if (GTUtility.areStacksEqual(machines, storedInput)) {
                                        int d = Math.min(
                                            machineCountForMaxParallelism - machines.stackSize,
                                            storedInput.stackSize);
                                        machines.stackSize += d;
                                        storedInput.stackSize -= d;
                                    }
                                } else if (storedInput.stackSize > 0) { // or parallelism will be 0
                                    mode = MODE_GENERATOR;
                                    machines = storedInput.copy();
                                    machines.stackSize = Math.min(machineCountForMaxParallelism, machines.stackSize);
                                    storedInput.stackSize -= machines.stackSize;
                                    this.machineType = machineType;
                                    if (generatorTE != null) generatorTE.setWorldObj(aPlayer.worldObj);
                                    machineTier = tier;
                                }
                            } else {
                                hint = MachineHintMessages.FAIL_TIER_MISMATCH;
                            }
                        }
                    }
                }
            }
            if (machineType != null) {
                hint = MachineHintMessages.SUCCESS;
                this.actualParallelism = calculateParallelismByAddonTier();
                int xCoord = getBaseMetaTileEntity().getXCoord();
                int yCoord = getBaseMetaTileEntity().getYCoord();
                int zCoord = getBaseMetaTileEntity().getZCoord();
                TST_Network.tst.sendToAll(new PackSyncMachineType(xCoord, yCoord, zCoord, machineType));
            } else {
                if (hint == null) hint = MachineHintMessages.FAIL_NON_ACCEPT;
            }

        } else {
            hint = MachineHintMessages.MACHINE_TO_OUTPUT;
            // clear
            addOutput(machines);
            machines = null;
            generatorTE = null;
            mode = null;
            actualParallelism = 0;
            int xCoord = getBaseMetaTileEntity().getXCoord();
            int yCoord = getBaseMetaTileEntity().getYCoord();
            int zCoord = getBaseMetaTileEntity().getZCoord();
            TST_Network.tst.sendToAll(new PackSyncMachineType(xCoord, yCoord, zCoord, machineType));
        }
        String message;
        switch (hint) {
            case FAIL_NON_ACCEPT: {
                message = String.format(hint.message, recipeBackendRefMapping.keySet());
                break;
            }
            case FAIL_TIER_MISMATCH: {
                message = String.format(hint.message, 8L << (2 * maxTier));
                break;
            }
            case SUCCESS: {
                message = String.format(hint.message, machines.getDisplayName(), actualParallelism);
                break;
            }
            default: {
                message = hint.message;
                break;
            }
        }
        GTUtility.sendChatToPlayer(aPlayer, message);
        endRecipeProcessing();
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        Map<GTUtility.ItemId, ItemStack> inputsFromME = new HashMap<>();
        for (MTEHatchInputBus tHatch : GTUtility.filterValidMTEs(mInputBusses)) {
            if (tHatch instanceof MTEHatchCraftingInputME) {
                continue;
            }
            tHatch.mRecipeMap = getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            boolean isMEBus = tHatch instanceof MTEHatchInputBusME;
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isMEBus && i >= 16) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GTUtility.ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        rList.add(itemStack);
                    }
                }
            }
        }

        if (getStackInSlot(1) != null && getStackInSlot(1).getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) rList.add(getStackInSlot(1));
        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        return rList;
    }

    public static void addRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.ResearchOnAncientPA.get(16),
                ItemList.Robot_Arm_IV.get(32),
                ItemList.Emitter_IV.get(32),
                ItemList.Field_Generator_IV.get(32),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 64))
            .fluidInputs(MaterialsAlloy.NITINOL_60.getFluidStack(24576))
            .itemOutputs(GTCMItemList.BigBroArray.get(1))
            .eut(TierEU.RECIPE_IV)
            .duration(20 * 1200)
            .addTo(RecipeMaps.assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Field_Generator_IV.get(2),
                ItemList.Casing_RobustTungstenSteel.get(1),
                ItemList.Robot_Arm_IV.get(16),
                ItemList.Emitter_IV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .itemOutputs(GTCMItemList.ParallelismCasing0.get(1))
            .eut(6400)
            .duration(20 * 150)
            .addTo(RecipeMaps.assemblerRecipes);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.ParallelismCasing0.get(1))
            .metadata(SCANNING, new Scanning(4 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Field_Generator_ZPM.get(2),
                ItemList.Casing_StableTitanium.get(1),
                GTCMItemList.ParallelismCasing0.get(4),
                ItemList.Robot_Arm_ZPM.get(16),
                ItemList.Emitter_ZPM.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 8),
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 })
            .itemOutputs(GTCMItemList.ParallelismCasing1.get(1))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(9216),
                new FluidStack(MaterialsAlloy.HELICOPTER.getFluid(), 24576))
            .duration(600 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.ParallelismCasing1.get(1))
            .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Field_Generator_UHV.get(4),
                ItemList.Casing_CleanStainlessSteel.get(1),
                GTCMItemList.ParallelismCasing1.get(4),
                ItemList.Robot_Arm_UHV.get(16),
                ItemList.Emitter_UHV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 8),
                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 })
            .itemOutputs(GTCMItemList.ParallelismCasing2.get(1))
            .fluidInputs(
                new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 9216),
                new FluidStack(MaterialsAlloy.HELICOPTER.getFluid(), 14400),
                new FluidStack(MaterialsAlloy.PIKYONIUM.getFluid(), 24576))
            .duration(1200 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.ParallelismCasing2.get(1),
            20000000,
            2000,
            31457280,
            1,
            new Object[] { ItemList.Casing_Dim_Bridge.get(64), ItemList.Casing_Dim_Injector.get(64),
                ItemList.Casing_Dim_Trans.get(64),
                new ItemStack(ItemBlock.getItemFromBlock(TTCasingsContainer.sBlockCasingsTT), 2, 14),
                GTCMItemList.SolarSail.get(64), GTCMItemList.SolarSail.get(64), GTCMItemList.SolarSail.get(64),
                GTCMItemList.SolarSail.get(64), ItemList.Casing_FrostProof.get(1),
                GTCMItemList.ParallelismCasing2.get(16), ItemList.Robot_Arm_UIV.get(16), ItemList.Emitter_UIV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 64),
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 8 } },
            new FluidStack[] { new FluidStack(solderUEV, 9216) },
            GTCMItemList.ParallelismCasing3.get(1),
            20 * 1200,
            31457280);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.ParallelismCasing3.get(1),
            200000000,
            20000,
            503316480,
            1,
            new Object[] { GTCMItemList.SolarSail.get(64), GTCMItemList.SolarSail.get(64),
                GTCMItemList.SolarSail.get(64), GTCMItemList.SolarSail.get(64), GTCMItemList.SolarSail.get(64),
                GTCMItemList.SolarSail.get(64), ItemList.Field_Generator_UXV.get(8), ItemList.Casing_SolidSteel.get(1),
                GTCMItemList.ParallelismCasing3.get(16),
                tectech.thing.CustomItemList.StabilisationFieldGeneratorTier2.get(4),
                tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(4),
                tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier2.get(4), ItemList.Robot_Arm_UXV.get(16),
                ItemList.Emitter_UXV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8),
                com.dreammaster.item.NHItemList.QuantumCircuit.getIS(4) },
            new FluidStack[] { MaterialsUEVplus.SpaceTime.getMolten(9216) },
            GTCMItemList.ParallelismCasing4.get(1),
            20 * 1200,
            503316480);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (activeTextures == null) {
            activeTextures = getActiveTextures(machineType);
        }
        if (idleTextures == null) {
            idleTextures = getIdleTextures(machineType);
        }
        String machineType = ((TST_BigBroArray) baseMetaTileEntity.getMetaTileEntity()).machineType;
        if (machineType == null && baseMetaTileEntity.getWorld() != null) {
            TST_Network.tst.sendToServer(
                new PackRequestMachineType(
                    baseMetaTileEntity.getWorld().provider.dimensionId,
                    baseMetaTileEntity.getXCoord(),
                    baseMetaTileEntity.getYCoord(),
                    baseMetaTileEntity.getZCoord()));
        }
        if (side == facing) {
            if (active) {
                return activeTextures;
            }
            return idleTextures;
        }
        return DEFAULT_CASING_TEXTURE;
    }

    enum MachineHintMessages {

        SUCCESS("Machine [%s] is set, parallelism is %s"),
        FAIL_NON_ACCEPT("Machines are not acceptable, acceptable types are %s"),
        FAIL_TIER_MISMATCH("Tier of machine is beyond the max tier that structure can hold. Max Tier is %s EU/t. "
            + "Machines will always be accepted when there's no addons. "
            + "When there's addon attached, arcanite frames unlock IV machines, quantum alloy frames unlock UEV and higher tier machines."),

        MACHINE_TO_OUTPUT("Machines are sent to output bus");

        private String message;

        private MachineHintMessages(String message) {
            this.message = message;
        }
    }

    public static class PackRequestMachineType
        implements IMessageHandler<PackRequestMachineType, PackSyncMachineType>, IMessage {

        int worldId;

        int x;

        int y;

        int z;

        public PackRequestMachineType() {}

        public PackRequestMachineType(int worldId, int x, int y, int z) {
            this.worldId = worldId;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.worldId = buf.readInt();
            this.x = buf.readInt();
            this.y = buf.readInt();
            this.z = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(worldId);
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }

        @Override
        public PackSyncMachineType onMessage(PackRequestMachineType message, MessageContext ctx) {
            WorldServer world = DimensionManager.getWorld(message.worldId);
            if (world != null) {
                TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);
                if (tileEntity instanceof BaseMetaTileEntity) {
                    TST_BigBroArray array = (TST_BigBroArray) ((BaseMetaTileEntity) tileEntity).getMetaTileEntity();
                    return new PackSyncMachineType(message.x, message.y, message.z, array.machineType);
                }
            }
            return null;
        }
    }

    public static class PackSyncMachineType
        implements IMessageHandler<PackSyncMachineType, PackSyncMachineType>, IMessage {

        int x;
        int y;
        int z;
        String machineType;

        public PackSyncMachineType() {}

        public PackSyncMachineType(int x, int y, int z, String machineType) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.machineType = machineType;
        }

        @Override
        public PackSyncMachineType onMessage(PackSyncMachineType message, MessageContext ctx) {

            TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
            if (tileEntity instanceof BaseMetaTileEntity) {
                IMetaTileEntity metaTileEntity = ((BaseMetaTileEntity) tileEntity).getMetaTileEntity();
                if (metaTileEntity instanceof TST_BigBroArray) {
                    TST_BigBroArray bigbro = (TST_BigBroArray) metaTileEntity;
                    bigbro.idleTextures = bigbro.getIdleTextures(message.machineType);
                    bigbro.activeTextures = bigbro.getActiveTextures(message.machineType);
                    bigbro.machineType = message.machineType;
                }
            }
            return null;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.x = buf.readInt();
            this.y = buf.readInt();
            this.z = buf.readInt();
            byte[] bytes = new byte[buf.readShort()];
            buf.readBytes(bytes);
            this.machineType = new String(bytes, StandardCharsets.UTF_8);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
            byte[] bytes = machineType != null ? machineType.getBytes(StandardCharsets.UTF_8) : new byte[0];
            buf.writeShort(bytes.length);
            buf.writeBytes(bytes);
        }
    }
}
