package com.Nxer.TwistSpaceTechnology.common.machine;

import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.*;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.dreammaster.gthandler.CustomItemList;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoTunnel;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
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
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.GT_Values;
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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Massfabricator;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import io.netty.buffer.ByteBuf;

public class TST_BigBroArray extends GT_MetaTileEntity_MultiblockBase_EM implements ISurvivalConstructable {

    private ItemStack machines;

    private int maxParallelism = 256;

    private int actualParallelism = 256;
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
    private int parallelismTier = -1;

    // affects dynamo
    private int casingTier = -1;

    // affects max parallelism
    private int addonCount = 0;
    private String mode;

    private static final String MODE_GENERATOR = "generator";

    private static final String MODE_PROCESSOR = "processor";

    private TileEntity solarTE;

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
    public static final String[][] PROCESSING_MACHINE_LIST = new String[][] {
        // OP
        { "Macerator", "Macerator" }, { "OreWasher", "OreWashingPlant" }, { "ChemicalBath", "ChemicalBath" },
        { "ThermalCentrifuge", "ThermalCentrifuge" },
        // Processing
        { "E_Furnace", "ElectricFurnace" }, { "ArcFurnace", "ArcFurnace" }, { "Bender", "BendingMachine" },
        { "Wiremill", "Wiremill" }, { "Lathe", "Lathe" }, { "Hammer", "ForgeHammer" }, { "Extruder", "Extruder" },
        { "FluidExtractor", "FluidExtractor" }, { "Compressor", "Compressor" }, { "Press", "FormingPress" },
        { "FluidSolidifier", "FluidSolidifier" }, { "Extractor", "Extractor" },
        { "LaserEngraver", "PrecisionLaserEngraver" }, { "Autoclave", "Autoclave" }, { "Mixer", "Mixer" },
        { "AlloySmelter", "AlloySmelter" }, { "Electrolyzer", "Electrolyzer" }, { "Sifter", "SiftingMachine" },
        { "ChemicalReactor", "ChemicalReactor" }, { "ElectromagneticSeparator", "ElectromagneticSeparator" },
        { "Recycler", "Recycler" }, { "Massfab", "MassFabricator" }, { "Centrifuge", "Centrifuge" },
        { "Cutter", "CuttingMachine" }, { "Assembler", "AssemblingMachine" }, { "CircuitAssembler", "CircuitAssembler" }
        // TODO: bartworks bio lab
    };

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
        },{
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
    },{
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
    },{
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
    },{
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
    },{
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
    },{
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
        "              ",
        "              ",
        "              ",
        "              ",
        "    A         ",
        "              ",
        "              ",
        "              ",
        "              "
    },{
        "              ",
        "              ",
        "              ",
        "   AAA        ",
        "   ABA        ",
        "   AAA        ",
        "              ",
        "              ",
        "              "
    },{
        "              ",
        "              ",
        "   AAA        ",
        "  ABBBA       ",
        "  ABBBA       ",
        "  ABBBA       ",
        "   AAA        ",
        "              ",
        "              "
    },{
        "              ",
        "   AAA        ",
        "  ABBBA       ",
        " ABBBBBA      ",
        " ABBBBBA      ",
        " ABBBBBA      ",
        "  ABBBA       ",
        "   AAA        ",
        "              "
    },{
        "  AAAAA       ",
        " AABBBAA      ",
        "AABBBBBAA     ",
        "ABBBBBBBA     ",
        "ABBBBBBBA     ",
        "ABBBBBBBA     ",
        "AABBBBBAA     ",
        " AABBBAA      ",
        "  AAAAA       "
    },{
        "  AAAAA       ",
        " AABBBAA      ",
        "AABBBBBAA     ",
        "ABBBBBBBA     ",
        "ABBBBBBBA     ",
        "ABBBBBBBA     ",
        "AABBBBBAA     ",
        " AABBBAA      ",
        "  AAAAA       "
    },{
        "              ",
        "   AAA        ",
        "  ABBBA       ",
        " ABBBBBA      ",
        " ABBBBBA      ",
        " ABBBBBA      ",
        "  ABBBA       ",
        "   AAA        ",
        "              "
    },{
        "              ",
        "   CCC        ",
        "  CAAAC       ",
        " CABBBAC      ",
        " CABBBAC      ",
        " CABBBAC      ",
        "  CAAAC       ",
        "   CCC        ",
        "              "
    },{
        "              ",
        "   CCC        ",
        "  C   C       ",
        " C AAA C      ",
        " C ABA C      ",
        " C AAA C      ",
        "  C   C       ",
        "   CCC        ",
        "              "
    },{
        "              ",
        "   CCC        ",
        "  C   C       ",
        " C     C      ",
        " C  A  C      ",
        " C     C      ",
        "  C   C       ",
        "   CCC        ",
        "              "
    },{
        "              ",
        "   CCC        ",
        "  C   C       ",
        " C     C      ",
        " C  E  C      ",
        " C     C      ",
        "  C   C       ",
        "   CCC        ",
        "              "
    },{
        "              ",
        "   CCC        ",
        "  C   C       ",
        " C     C      ",
        " C  E  C      ",
        " C     C      ",
        "  C   C       ",
        "   CCC        ",
        "              "
    },{
        "              ",
        "   CCC        ",
        "  C   C       ",
        " C     C      ",
        " C  E  C      ",
        " C     C      ",
        "  C   C       ",
        "   CCC        ",
        "              "
    },{
        "              ",
        "   CCC        ",
        "  C   C       ",
        " C EEE C      ",
        " C EEE C      ",
        " C EEE C      ",
        "  C   C       ",
        "   CCC        ",
        "              "
    },{
        "DDDDDDDDD     ",
        "DDDDDDDDD     ",
        "DDEEEEEDD     ",
        "DDEEEEEDD     ",
        "DDEEEEEDDDDDDD",
        "DDEEEEEDD     ",
        "DDEEEEEDD     ",
        "DDDDDDDDD     ",
        "DDDDDDDDD     "
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
                put("Mixer", RecipeMaps.class.getDeclaredField("mixerNonCellRecipes"));
                put("AlloySmelter", RecipeMaps.class.getDeclaredField("alloySmelterRecipes"));
                put("Electrolyzer", RecipeMaps.class.getDeclaredField("electrolyzerNonCellRecipes"));
                put("ElectromagneticSeparator", RecipeMaps.class.getDeclaredField("electroMagneticSeparatorRecipes"));
                put("Recycler", RecipeMaps.class.getDeclaredField("recyclerRecipes"));
                put("Massfab", RecipeMaps.class.getDeclaredField("massFabFakeRecipes"));
                put("Centrifuge", RecipeMaps.class.getDeclaredField("centrifugeNonCellRecipes"));
                put("Cutter", RecipeMaps.class.getDeclaredField("cutterRecipes"));
                put("Assembler", RecipeMaps.class.getDeclaredField("assemblerRecipes"));
                put("CircuitAssembler", RecipeMaps.class.getDeclaredField("circuitAssemblerRecipes"));
                put("Diesel", RecipeMaps.class.getDeclaredField("dieselFuels"));
                put("Gas_Turbine", RecipeMaps.class.getDeclaredField("gasTurbineFuels"));
                // no recipe map for steam, 2MB steam for 1EU, and 1/80mb distilled water, 1 mb sc for 100EU, and 1mb
                // steam
            } catch (Exception e) {

            }
        }
    };

    // added by getDieselGeneratorsForArray in postInit phase
    public static final Map<String, ItemStack[]> GENERATORS = new HashMap<>();
    public static final String[] GT_GENERATOR_MACHINE_LIST = new String[] { "Diesel", "Gas_Turbine", "Steam_Turbine",
        "SemiFluid", "Naquadah" };

    public static final String[] INTER_MOD_GENERATORS = new String[] { "ASP_Solar", "EMT_Solar" };

    private static final Map<String, float[]> GENERATOR_EFFICIENCY = new HashMap<>() {

        {
            put("Diesel", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Gas_Turbine", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Steam_Turbine", new float[] { 1f, 1f, 1f });
            put("Semi_Fluid", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Naquadah", new float[] { 0.8f, 1f, 1.5f, 2f, 2.5f });
        }
    };

    public static final String GENERATOR_NQ = "Generator_Naquadah";

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
                }
            })
            .collect(Collectors.toList());
        FRAMES = Arrays.asList(
            Pair.of(
                Block.getBlockFromItem(
                    ALLOY.ARCANITE.getFrameBox(1)
                        .getItem()),
                1), // IV
            Pair.of(
                Block.getBlockFromItem(
                    ALLOY.ZERON_100.getFrameBox(1)
                        .getItem()),
                2), // LuV
            Pair.of(
                Block.getBlockFromItem(
                    ALLOY.PIKYONIUM.getFrameBox(1)
                        .getItem()),
                3), // ZPM
            Pair.of(
                Block.getBlockFromItem(
                    ALLOY.BOTMIUM.getFrameBox(1)
                        .getItem()),
                4), // UV
            Pair.of(
                Block.getBlockFromItem(
                    ALLOY.ABYSSAL.getFrameBox(1)
                        .getItem()),
                5), // UHV
            Pair.of(
                Block.getBlockFromItem(
                    ALLOY.QUANTUM.getFrameBox(1)
                        .getItem()),
                6)); // UEV - MAX
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
        RecipeMaps.massFabFakeRecipes.addRecipe(
            false,
            new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
            null,
            null,
            null,
            new FluidStack[] { Materials.UUMatter.getFluid(1) },
            GT_MetaTileEntity_Massfabricator.sDurationMultiplier,
            GT_MetaTileEntity_Massfabricator.BASE_EUT,
            0);
        RecipeMaps.massFabFakeRecipes.addRecipe(
            false,
            new ItemStack[] { GT_Utility.getIntegratedCircuit(23) },
            null,
            null,
            new FluidStack[] { Materials.UUAmplifier.getFluid(GT_MetaTileEntity_Massfabricator.sUUAperUUM) },
            new FluidStack[] { Materials.UUMatter.getFluid(1L) },
            GT_MetaTileEntity_Massfabricator.sDurationMultiplier / GT_MetaTileEntity_Massfabricator.sUUASpeedBonus,
            GT_MetaTileEntity_Massfabricator.BASE_EUT,
            0);
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
            "SemiFluid",
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

    public static int getParallelismCasingTier(Block block, int meta) {
        if (block == GTCMItemList.ParallelismCasing0.getBlock()) {
            return meta - 2;
        }
        return 0;
    }

    public void setCoilTier(HeatingCoilLevel level) {
        this.coilTier = coilTier != HeatingCoilLevel.None ? (level.getTier() < coilTier.getLevel() ? level : coilTier)
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
                    .replace('E', 'K');
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
                GT_HatchElementBuilder.<TST_BigBroArray>builder()
                    .atLeast(
                        GT_HatchElement.Maintenance,
                        GT_HatchElement.InputBus.or(GT_HatchElement.InputHatch),
                        GT_HatchElement.OutputBus.or(GT_HatchElement.OutputHatch),
                        GT_HatchElement.Muffler)
                    .adder(TST_BigBroArray::addToMachineList)
                    .dot(1)
                    .casingIndex(((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings4).getTextureIndex(0))
                    .buildAndChain(GregTech_API.sBlockCasings4, 0))
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
            .addElement('E', StructureUtility.ofBlock(GregTech_API.sBlockCasings4, 1))
            .addElement(
                'F',
                GT_HatchElementBuilder.<TST_BigBroArray>builder()
                    .atLeast(
                        HatchElement.DynamoMulti.or(GT_HatchElement.ExoticEnergy)
                            .or(GT_HatchElement.Dynamo))
                    .adder(TST_BigBroArray::addToMachineList)
                    .dot(2)
                    .casingIndex(((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings4).getTextureIndex(1))
                    .buildAndChain(GregTech_API.sBlockCasings4, 1))
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
                    'K',
                    StructureUtility.withChannel(
                        "coil",
                        GT_StructureUtility.ofCoil(TST_BigBroArray::setCoilTier, TST_BigBroArray::getCoilTier)))
                .addElement(
                    'I',
                    StructureUtility.withChannel(
                        "frame",
                        StructureUtility.ofBlocksTiered(
                            TST_BigBroArray::getFrameTier,
                            FRAMES,
                            -1,
                            (te, tier) -> te.frameTier = te.frameTier >= 0 ? Math.min(tier, te.frameTier) : tier,
                            (te) -> te.frameTier)))
                .addElement(
                    'G',
                    StructureUtility.withChannel(
                        "glass",
                        BorosilicateGlass.ofBoroGlass(
                            (byte) -1,
                            (te, tier) -> te.glassTier = te.glassTier >= 0 ? Math.min(tier, te.glassTier) : tier,
                            (te) -> (byte) te.glassTier)))
                .addElement(
                    'H',
                    StructureUtility.withChannel(
                        "parallelism",
                        StructureUtility.ofBlocksTiered(
                            TST_BigBroArray::getParallelismCasingTier,
                            PARALLELISM_CASINGS,
                            -1,
                            (te, tier) -> {
                                if (te.parallelismTier >= 0) {
                                    te.parallelismTier = Math.min(tier, te.parallelismTier);
                                } else {
                                    te.parallelismTier = tier;
                                }
                            },
                            (te) -> te.parallelismTier)))
                .addElement('J', StructureUtility.ofBlock(GregTech_API.sBlockCasings4, 1));
        }

        STRUCTURE_DEFINITION = builder.build();
    }

    @SideOnly(Side.CLIENT)
    public static void initializeDefaultTextures() {
        DEFAULT_FRONT_ACTIVE = new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)),
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
            Textures.BlockIcons.getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                .extFacing()
                .glow()
                .build(), };

        DEFAULT_CASING_TEXTURE = new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)) };

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
        CustomIcon frontIcon = new CustomIcon(front);
        frontIcon.run();
        CustomIcon frontGlowIcon = new CustomIcon(frontGlow);
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
        CustomIcon frontIcon = new CustomIcon(front);
        frontIcon.run();
        CustomIcon frontGlowIcon = new CustomIcon(frontGlow);
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
        aNBT.setInteger("maxParallelism", maxParallelism);
        aNBT.setInteger("actualParallelism", actualParallelism);
        if (mode != null) aNBT.setString("mode", mode);

        if (solarTE != null) {
            NBTTagCompound compound = new NBTTagCompound();
            solarTE.writeToNBT(compound);
            aNBT.setTag("solarTE", compound);
        }
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        if (machines != null) {
            machines.writeToNBT(nbtTagCompound);
        }
        aNBT.setTag("machines", nbtTagCompound);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineType = aNBT.getString("machineType");
        mode = aNBT.getString("mode");
        machineTier = aNBT.getInteger("tier");
        maxParallelism = aNBT.getInteger("maxParallelism");
        actualParallelism = aNBT.getInteger("acutalParallelism");
        if (aNBT.hasKey("machines")) machines = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("machines"));
        if (aNBT.hasKey("solarTE")) {
            NBTTagCompound compound = aNBT.getCompoundTag("solarTE");
            solarTE = Block.getBlockFromItem(machines.getItem())
                .createTileEntity(null, machines.getItemDamage());
            solarTE.readFromNBT(compound);
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
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        GTCM_ProcessingLogic gtcm_processingLogic = new GTCM_ProcessingLogic();
        gtcm_processingLogic.setMaxParallelSupplier(
            () -> machines != null ? (int) Math.min(machines.stackSize * Math.pow(2, parallelismTier), maxParallelism)
                : 1);
        // gtcm_processingLogic.setEuModifier((float) Math.pow(0.9, coilTier.getTier()));
        return gtcm_processingLogic;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing_EM() {
        if (MODE_PROCESSOR.equals(mode)) {
            return super.checkProcessing_EM();
        } else if (MODE_GENERATOR.equals(mode)) {
            if ("ASP_Solar".equals(machineType) || "EMT_Solar".equals(machineType)) {
                mMaxProgresstime = 20;
                int xCoord = getBaseMetaTileEntity().getXCoord();
                int yCoord = getBaseMetaTileEntity().getYCoord() + 4;
                int zCoord = getBaseMetaTileEntity().getZCoord();
                solarTE.xCoord = xCoord;
                solarTE.yCoord = yCoord;
                solarTE.zCoord = zCoord;
                if (!solarTE.hasWorldObj()) {
                    solarTE.setWorldObj(getBaseMetaTileEntity().getWorld());
                }
                solarTE.updateEntity();
                if (solarTE instanceof TileEntitySolarPanel te) {
                    solarTE.updateEntity();
                    lEUt = ((long) (te.storage * Math.pow(1.5, parallelismTier))) * actualParallelism * 20;
                    fillAllDynamos(lEUt);
                    te.storage = 0;
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                } else if (solarTE instanceof TileEntitySolarBase te) {
                    te.checkConditions();
                    long energyOutput = te.energySource.getEnergyStored();
                    lEUt = (long) (Math.pow(1.5, parallelismTier) * energyOutput * actualParallelism * 20);
                    fillAllDynamos(lEUt);
                    te.energySource.drawEnergy(energyOutput);
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                return CheckRecipeResultRegistry.NO_RECIPE;
            } else {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
        } else {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget(String.format("发电中, 功率: %dEU/t", lEUt / 20)).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(
                        widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && getBaseMetaTileEntity().isActive()
                            && MODE_GENERATOR.equals(mode)))
            .widget(new FakeSyncWidget.StringSyncer(() -> mode, (mode) -> TST_BigBroArray.this.mode = mode))
            .widget(new FakeSyncWidget.LongSyncer(() -> lEUt, (l) -> lEUt = l));

        screenElements.widget(
            new TextWidget("Machine state: " + mode).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()));

        screenElements.widget(
            new TextWidget("Machine type:" + machineType).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.StringSyncer(() -> machineType, (type) -> machineType = type));;

        screenElements.widget(
            new TextWidget("Machine tier:" + machineTier).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> machineTier, (tier) -> machineTier = tier));
        screenElements
            .widget(
                new TextWidget("Provided parallelism:" + actualParallelism).setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> actualParallelism, (p) -> this.actualParallelism = p));

        screenElements.widget(
            new TextWidget("Speed boost:" + Math.pow(1.5, parallelismTier)).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> parallelismTier, (tier) -> parallelismTier = tier));

        screenElements.widget(
            new TextWidget("Addons attached:" + addonCount).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> addonCount, (ct) -> addonCount = ct));
    }

    private long fillAllDynamos(long energy) {
        for (GT_MetaTileEntity_Hatch_Dynamo dynamo : mDynamoHatches) {
            long drain = Math.min(energy, dynamo.maxEUStore() - dynamo.getEUVar());
            energy -= drain;
            dynamo.setEUVar(dynamo.getEUVar() + drain);
        }
        for (GT_MetaTileEntity_Hatch_DynamoMulti dynamo : eDynamoMulti) {
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
    protected GT_Multiblock_Tooltip_Builder getTooltip() {
        GT_Multiblock_Tooltip_Builder gt_multiblock_tooltip_builder = new GT_Multiblock_Tooltip_Builder()
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
            .addInfo(TextEnums.StructureTooComplex.toString())
            .addInfo(TextLocalization.BLUE_PRINT_INFO);
        gt_multiblock_tooltip_builder.toolTipFinisher(TextLocalization.ModName);
        return gt_multiblock_tooltip_builder;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (machines != null) {
            try {
                Field field = recipeBackendRefMapping.get(machineType);
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
            buildPiece("addon3", itemStack, hintsOnly, 4, 12, -7);
            addonCount--;
        }
        if (addonCount == 3) {
            buildPiece("addon2", itemStack, hintsOnly, -6, 12, 3);
            addonCount--;
        }
        if (addonCount == 2) {
            buildPiece("addon1", itemStack, hintsOnly, 4, 12, 18);
            addonCount--;
        }
        if (addonCount == 1) {
            buildPiece("addon0", itemStack, hintsOnly, 19, 12, 3);
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
                    blockPlacedCount += survivialBuildPiece("addon0", stackSize, 19, 12, 3, elementBudget, env, true);
                    break;
                case 1:
                    blockPlacedCount += survivialBuildPiece("addon1", stackSize, 4, 12, 18, elementBudget, env, true);
                    break;
                case 2:
                    blockPlacedCount += survivialBuildPiece("addon2", stackSize, -6, 12, 3, elementBudget, env, true);
                    break;
                case 3:
                    blockPlacedCount += survivialBuildPiece("addon2", stackSize, -4, 12, -7, elementBudget, env, true);
                    break;
                default:
                    break;
            }
        }
        return blockPlacedCount;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return getTooltip();
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.casingTier = -1;
        this.glassTier = -2;
        this.coilTier = HeatingCoilLevel.None;
        this.frameTier = -1;
        boolean checkPiece = checkPiece("core", 5, 5, 4);
        if (!checkPiece) return false;
        // dynamo hatch level follows casing level
        for (GT_MetaTileEntity_Hatch_Dynamo mDynamoHatch : mDynamoHatches) {
            if (mDynamoHatch.mTier > casingTier) {
                return false;
            }
        }
        for (GT_MetaTileEntity_Hatch_DynamoMulti gt_metaTileEntity_hatch_dynamoMulti : eDynamoMulti) {
            if (gt_metaTileEntity_hatch_dynamoMulti.mTier > casingTier
                || (gt_metaTileEntity_hatch_dynamoMulti instanceof GT_MetaTileEntity_Hatch_DynamoTunnel
                    && casingTier < 8)) {
                return false;
            }
        }
        // energy hatch level follows glass level
        for (GT_MetaTileEntity_Hatch_Energy mEnergyHatch : mEnergyHatches) {
            if (mEnergyHatch.mTier > glassTier) {
                return false;
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti gt_metaTileEntity_hatch_energyMulti : eEnergyMulti) {
            if (gt_metaTileEntity_hatch_energyMulti.mTier > glassTier
                || (gt_metaTileEntity_hatch_energyMulti instanceof GT_MetaTileEntity_Hatch_EnergyTunnel
                    && glassTier < 6)) {
                return false;
            }
        }

        this.addonCount = 0;
        if (checkPiece("addon0", 19, 12, 3)) {
            this.addonCount += 1;
        }
        if (checkPiece("addon1", 4, 12, 18)) {
            this.addonCount += 1;
        }
        if (checkPiece("addon2", -6, 12, 3)) {
            this.addonCount += 1;
        }
        if (checkPiece("addon3", 4, 12, -7)) {
            this.addonCount += 1;
        }
        // 5 is place holder, max tier is 4
        this.maxParallelism = calculateMaxParallelismByAddonTier();
        this.machineCountForMaxParallelism = (this.maxParallelism >> parallelismTier);
        this.actualParallelism = calculateParallelismByAddonTier();
        processingLogic.setSpeedBonus((float) Math.pow(1.5, parallelismTier));
        processingLogic.setEuModifier((float) Math.pow(0.9, coilTier.getTier()));
        return checkPiece;
    }

    private int calculateMaxParallelismByAddonTier() {
        if (addonCount > 0) {
            // 0-64
            // 1-256
            // 2-1024
            // 3-2048
            // 4-MAX/5
            return parallelismTier >= 5 ? (Integer.MAX_VALUE / 5) * (1 + this.addonCount)
                : (64 << ((parallelismTier) * 2)) * (1 + this.addonCount);
        } else {
            return 64;
        }
    }

    private int calculateParallelismByAddonTier() {
        int p = parallelismTier >= 5 ? (Integer.MAX_VALUE / 5) * (1 + this.addonCount)
            : ((machines != null ? machines.stackSize : 0) << parallelismTier);
        int m = calculateMaxParallelismByAddonTier();
        return Math.min(p, m);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BigBroArray(mName);
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return Math.min(actualParallelism, 10000);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
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
                                CustomItemList customItemList = CustomItemList.valueOf(name);
                                machineTypeToBeUsed = customItemList.get(1);
                            } catch (IllegalArgumentException e) {

                            }
                        }
                        if (machineTypeToBeUsed != null) {
                            if (GT_Utility.areStacksEqual(machineTypeToBeUsed, storedInput)) {
                                // supports all machines when there's no additional strucutures or frame level >= 6
                                if (i < frameTier + 5 || frameTier >= 6 || addonCount == 0) {
                                    if (machines != null) {
                                        if (GT_Utility.areStacksEqual(machines, storedInput)) {
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
                                        machineTier = i;
                                        mode = MODE_PROCESSOR;
                                    }
                                }
                            }
                        }
                    }
                }

                for (String machineType : INTER_MOD_GENERATORS) {
                    for (int i = 0; i < GENERATORS.get(machineType).length; i++) {
                        ItemStack machineTypeToBeUsed = GENERATORS.get(machineType)[i];
                        if (GT_Utility.areStacksEqual(storedInput, machineTypeToBeUsed)) {
                            // create dummy TE for solar generation
                            solarTE = Block.getBlockFromItem(machineTypeToBeUsed.getItem())
                                .createTileEntity(aPlayer.worldObj, machineTypeToBeUsed.getItemDamage());
                            if ("ASP_Solar".equals(machineType) && i >= (frameTier + 4) && addonCount > 0) continue;
                            // calculate tier with log
                            // (int)log(output / 8, 4) = LV(1), MV(2), HV(3), EV(4), IV(5), .......
                            // supports all machines when there's no additional strucutures or frame level >= 6
                            if ("EMT_Solar".equals(machineType)
                                && (int) Math.floor(Math.log(((TileEntitySolarBase) solarTE).output / 8) / Math.log(4))
                                    >= (frameTier + 4)
                                && frameTier < 6
                                && addonCount > 0) continue;
                            if (machines != null) {
                                if (GT_Utility.areStacksEqual(machines, storedInput)) {
                                    int d = Math
                                        .min(machineCountForMaxParallelism - machines.stackSize, storedInput.stackSize);
                                    machines.stackSize += d;
                                    storedInput.stackSize -= d;
                                }
                            } else if (storedInput.stackSize > 0) { // or parallelism will be 0
                                mode = MODE_GENERATOR;
                                machines = storedInput.copy();
                                machines.stackSize = Math.min(machineCountForMaxParallelism, machines.stackSize);
                                storedInput.stackSize -= machines.stackSize;
                                this.machineType = machineType;
                                solarTE.setWorldObj(aPlayer.worldObj);
                                machineTier = i;
                            }
                        }
                    }
                }
            }
            if (machineType != null) {
                this.actualParallelism = calculateParallelismByAddonTier();
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    String.format(
                        "Machine [%s] is set, parallelism is %s",
                        machines.getDisplayName(),
                        actualParallelism));
                int xCoord = getBaseMetaTileEntity().getXCoord();
                int yCoord = getBaseMetaTileEntity().getYCoord();
                int zCoord = getBaseMetaTileEntity().getZCoord();
                TST_Network.tst.sendToAll(new PackSyncMachineType(xCoord, yCoord, zCoord, machineType));
            }
        } else {
            GT_Utility.sendChatToPlayer(aPlayer, "Machines are sent to output bus");
            // clear
            machineType = null;
            addOutput(machines);
            machines = null;
            solarTE = null;
            mode = null;
            actualParallelism = 0;
            int xCoord = getBaseMetaTileEntity().getXCoord();
            int yCoord = getBaseMetaTileEntity().getYCoord();
            int zCoord = getBaseMetaTileEntity().getZCoord();
            TST_Network.tst.sendToAll(new PackSyncMachineType(xCoord, yCoord, zCoord, machineType));
        }
    }

    public static void addRecipes() {
        GT_Values.RA.addAssemblerRecipe(
            new ItemStack[] { ItemList.Processing_Array.get(16), ItemList.Robot_Arm_IV.get(32),
                ItemList.Emitter_IV.get(32), ItemList.Field_Generator_IV.get(32),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 64), },
            ALLOY.NITINOL_60.getFluidStack(24576),
            GTCMItemList.BigBroArray.get(1),
            20 * 1200,
            7680);
        GT_Values.RA.addAssemblerRecipe(
            new ItemStack[] { ItemList.Field_Generator_IV.get(2), ItemList.Casing_RobustTungstenSteel.get(1),
                ItemList.Robot_Arm_IV.get(16), ItemList.Emitter_IV.get(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Elite), 4) },
            Materials.SolderingAlloy.getMolten(9216),
            GTCMItemList.ParallelismCasing0.get(1),
            150 * 20,
            6400);

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.ParallelismCasing0.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                ItemList.Field_Generator_ZPM.get(2),
                ItemList.Casing_StableTitanium.get(1),
                GTCMItemList.ParallelismCasing0.get(4),
                ItemList.Robot_Arm_ZPM.get(16),
                ItemList.Emitter_ZPM.get(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Ultimate), 4))
            .itemOutputs(GTCMItemList.ParallelismCasing1.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .duration(600 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.ParallelismCasing1.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                ItemList.Field_Generator_UHV.get(4),
                ItemList.Casing_CleanStainlessSteel.get(1),
                GTCMItemList.ParallelismCasing1.get(4),
                ItemList.Robot_Arm_UHV.get(16),
                ItemList.Emitter_UHV.get(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 4))
            .itemOutputs(GTCMItemList.ParallelismCasing2.get(1))
            .fluidInputs(new FluidStack(ALLOY.INDALLOY_140.getFluid(), 9216))
            .duration(1200 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.ParallelismCasing2.get(1),
            20000000,
            2000,
            31457280,
            1,
            new Object[] { ItemList.Field_Generator_UIV.get(8), ItemList.Casing_FrostProof.get(1),
                GTCMItemList.ParallelismCasing2.get(4), ItemList.Robot_Arm_UIV.get(16), ItemList.Emitter_UIV.get(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Optical), 4) },
            new FluidStack[] { new FluidStack(solderUEV, 9216) },
            GTCMItemList.ParallelismCasing3.get(1),
            20 * 1200,
            31457280);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.ParallelismCasing2.get(1),
            200000000,
            20000,
            503316480,
            1,
            new Object[] { ItemList.Field_Generator_UXV.get(8), ItemList.Casing_SolidSteel.get(1),
                GTCMItemList.ParallelismCasing2.get(4), ItemList.Robot_Arm_UXV.get(16), ItemList.Emitter_UXV.get(16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8),
                com.dreammaster.item.ItemList.QuantumCircuit.getIS(4) },
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
