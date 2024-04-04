package com.Nxer.TwistSpaceTechnology.common.machine;

import static gregtech.api.enums.Textures.BlockIcons.*;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.dreammaster.gthandler.CustomItemList;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import advsolar.common.tiles.TileEntitySolarPanel;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emt.tile.solar.TileEntitySolarBase;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import io.netty.buffer.ByteBuf;

public class TST_BigBroArray extends GT_MetaTileEntity_MultiblockBase_EM {

    private ItemStack machines;

    private int maxParallelism = 256;

    private String machineType = null;

    private int tier = 0;

    private String mode;

    private static final String MODE_GENERATOR = "generator";

    private static final String MODE_PROCESSOR = "processor";

    private TileEntity solarTE;

    private static String[] tierNames = new String[] { "LV", "MV", "HV", "EV", "IV", "LUV", "ZPM", "UV", "UHV", "UEV",
        "UIV", "UMV", "UXV", "MAX" };

    @SideOnly(Side.CLIENT)
    public static ITexture[] DEFAULT_FRONT_ACTIVE = null;

    @SideOnly(Side.CLIENT)
    public static ITexture[] DEFAULT_FRONT_IDLE = null;

    @SideOnly(Side.CLIENT)
    private static ITexture[] DEFAULT_CASING_TEXTURE = null;

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

    private static final Map<String, float[]> generatorEfficiency = new HashMap<>() {

        {
            put("Diesel", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Gas_Turbine", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Steam_Turbine", new float[] { 1f, 1f, 1f });
            put("Semi_Fluid", new float[] { 0.95f, 0.9f, 0.85f, 0.8f, 0.75f });
            put("Naquadah", new float[] { 0.8f, 1f, 1.5f, 2f, 2.5f });
        }
    };

    public static final String GENERATOR_NQ = "Generator_Naquadah";

    private static final IStructureDefinition<TST_BigBroArray> STRUCTURE_DEFINITION = StructureDefinition
        .<TST_BigBroArray>builder()
        .addShape(
            "box",
            StructureUtility.transpose(
                new String[][] { { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
                    { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
                    { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
                    { "AAA~AAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
                    { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
                    { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
                    { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" } }))
        .addElement(
            'A',
            GT_HatchElementBuilder.<TST_BigBroArray>builder()
                .atLeast(
                    GT_HatchElement.Maintenance,
                    GT_HatchElement.Energy.or(GT_HatchElement.ExoticEnergy),
                    GT_HatchElement.Dynamo.or(GT_MetaTileEntity_MultiblockBase_EM.HatchElement.DynamoMulti),
                    GT_HatchElement.InputBus.or(GT_HatchElement.InputHatch),
                    GT_HatchElement.OutputBus.or(GT_HatchElement.OutputHatch)
                        .or(GT_HatchElement.Muffler))
                .adder(TST_BigBroArray::addToMachineList)
                .dot(1)
                .casingIndex(((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings4).getTextureIndex(0))
                .buildAndChain(GregTech_API.sBlockCasings4, 0))
        .build();

    @SideOnly(Side.CLIENT)
    private ITexture[] activeTextures = DEFAULT_FRONT_ACTIVE;

    @SideOnly(Side.CLIENT)
    private ITexture[] idleTextures = DEFAULT_FRONT_IDLE;

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
        aNBT.setInteger("tier", tier);
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
        tier = aNBT.getInteger("tier");
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
        gtcm_processingLogic.setMaxParallelSupplier(() -> machines != null ? machines.stackSize : 1);
        gtcm_processingLogic.setSpeedBonus(1.1f);
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
                if (!solarTE.hasWorldObj()) {
                    solarTE.setWorldObj(getBaseMetaTileEntity().getWorld());
                }
                solarTE.updateEntity();
                if (solarTE instanceof TileEntitySolarPanel te) {
                    solarTE.updateEntity();
                    long energyOutput = ((long) te.storage) * machines.stackSize * 20;
                    fillAllDynamos(energyOutput);
                    te.storage = 0;
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                } else if (solarTE instanceof TileEntitySolarBase te) {
                    te.checkConditions();
                    long energyOutput = te.energySource.getEnergyStored();
                    fillAllDynamos(energyOutput * machines.stackSize * 20);
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

    private void fillAllDynamos(long energy) {
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
        buildPiece("box", itemStack, hintsOnly, 3, 3, 0);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return getTooltip();
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return checkPiece("box", 3, 3, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BigBroArray(mName);
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return machines != null ? machines.stackSize * 250 : 0;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (machines == null) {
            for (ItemStack storedInput : getStoredInputs()) {
                for (String[] machineType : PROCESSING_MACHINE_LIST) {
                    for (int i = 0; i < tierNames.length; i++) {
                        String tierName = tierNames[i];
                        String name = String.format("Machine_%s_%s", tierName, machineType[0]);
                        try {
                            ItemList itemList = ItemList.valueOf(name);
                            if (GT_Utility.areStacksEqual(itemList.get(1), storedInput)) {
                                machines = storedInput.copy();
                                machines.stackSize = Math.min(maxParallelism, machines.stackSize);
                                storedInput.stackSize -= machines.stackSize;
                                this.machineType = machineType[0];
                                tier = i;
                                mode = MODE_PROCESSOR;
                                break;
                            }
                        } catch (IllegalArgumentException ex) {
                            name = String.format("%s%s", machineType[1], tierName);
                            try {
                                CustomItemList customItemList = CustomItemList.valueOf(name);
                                if (GT_Utility.areStacksEqual(customItemList.get(1), storedInput)) {
                                    machines = storedInput.copy();
                                    machines.stackSize = Math.min(maxParallelism, machines.stackSize);
                                    storedInput.stackSize -= machines.stackSize;
                                    this.machineType = machineType[0];
                                    tier = i;
                                    mode = MODE_PROCESSOR;
                                    break;
                                }
                            } catch (IllegalArgumentException e) {

                            }
                        }
                    }
                }

                for (String machineType : INTER_MOD_GENERATORS) {
                    for (ItemStack generator : GENERATORS.get(machineType)) {
                        if (GT_Utility.areStacksEqual(storedInput, generator)) {
                            mode = MODE_GENERATOR;
                            machines = storedInput.copy();
                            machines.stackSize = Math.min(maxParallelism, machines.stackSize);
                            storedInput.stackSize -= machines.stackSize;
                            this.machineType = machineType;

                            // create dummy TE for solar generation
                            solarTE = Block.getBlockFromItem(generator.getItem())
                                .createTileEntity(aPlayer.worldObj, generator.getItemDamage());
                            solarTE.setWorldObj(aPlayer.worldObj);
                            int xCoord = getBaseMetaTileEntity().getXCoord();
                            int yCoord = getBaseMetaTileEntity().getYCoord() + 3;
                            int zCoord = getBaseMetaTileEntity().getZCoord();
                            solarTE.xCoord = xCoord;
                            solarTE.yCoord = yCoord;
                            solarTE.zCoord = zCoord;
                            break;
                        }
                    }
                }

            }
            if (machineType != null) {
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    String.format(
                        "Machine [%s] is set, parallelism is %s",
                        machines.getDisplayName(),
                        machines.stackSize));
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
            int xCoord = getBaseMetaTileEntity().getXCoord();
            int yCoord = getBaseMetaTileEntity().getYCoord();
            int zCoord = getBaseMetaTileEntity().getZCoord();
            TST_Network.tst.sendToAll(new PackSyncMachineType(xCoord, yCoord, zCoord, machineType));
        }
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
