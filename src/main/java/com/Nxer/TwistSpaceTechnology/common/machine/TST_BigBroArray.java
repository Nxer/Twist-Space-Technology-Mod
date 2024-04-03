package com.Nxer.TwistSpaceTechnology.common.machine;

import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Textures.BlockIcons.*;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.logging.log4j.core.helpers.Strings;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.dreammaster.gthandler.CustomItemList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import io.netty.buffer.ByteBuf;

public class TST_BigBroArray extends GTCM_MultiMachineBase<TST_BigBroArray> {

    private ItemStack machines;

    private int maxParallelism = 256;

    private String machineType = null;

    private int tier = 0;

    private static String[] tierNames = new String[] { "LV", "MV", "HV", "EV", "IV", "LUV", "ZPM", "UV", "UHV", "UEV",
        "UIV", "UMV", "UXV", "MAX" };

    private static String[] romeDigits = new String[] { "I", "II", "III", "IV", "V" };

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
        { "Assembler", "AssemblingMachine" }, { "CircuitAssembler", "CircuitAssembler" }
        // TODO: bartworks bio lab
    };

    public static final Map<String, String> overlayMapping = new HashMap<>() {
        {
            put("Macerator", "macerator");
            put("OreWasher", "ore_washer");
            put("ChemicalBath","chemical_bath");
            put("ThermalCentrifuge", "thermal_centrifuge");
            put("E_Furnace", "electric_furnace");
            put("ArcFurnace", "arc_furnace");
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
            } catch (Exception e) {

            }
        }
    };

    public static final String[] GT_GENERATOR_MACHINE_LIST = new String[] { "Diesel", "Gas_Turbine", "Steam_Turbine", };

    public static final String[] interModGenerators = new String[] {
        // ASP

        // Electro-magic-tools compact solar
    };

    public static final String GENERATOR_NQ = "Generator_Naquadah";

    @SideOnly(Side.CLIENT)
    private ITexture[] activeTextures = new ITexture[0];

    @SideOnly(Side.CLIENT)
    private ITexture[] idleTextures = new ITexture[0];

    public static final Map<String, List<ItemList>> processingMachinesMapping = new HashMap<>() {

        {

            // material processing
            put("extractor", Arrays.asList());
            put("laser_engraver", Arrays.asList());
            put("autoclave", Arrays.asList());
            put("electrolyzer", Arrays.asList());
            put("sifter", Arrays.asList());
            put("chemical_plant", Arrays.asList());
            put("ElectromagneticSeparator", Arrays.asList());
            put("recycler", Arrays.asList());
            put("fabricator", Arrays.asList());
            put("centrifuge", Arrays.asList());

        }
    };

    private ITexture[] getActiveTextures(String machineType) {
        String front = String.format("basicmachines/%s/OVERLAY_FRONT_ACTIVE", overlayMapping.get(machineType));
        String frontGlow = String
            .format("basicmachines/%s/OVERLAY_FRONT_ACTIVE_GLOW", overlayMapping.get(machineType));
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
        String front = String.format("basicmachines/%s/OVERLAY_FRONT", overlayMapping.get(machineType));
        String frontGlow = String.format("basicmachines/%s/OVERLAY_FRONT_GLOW", overlayMapping.get(machineType));
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
        tier = aNBT.getInteger("tier");
        machines = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("machines"));
    }

    private List<ItemList> macerators = Arrays.asList(
        ItemList.Machine_LV_Macerator,
        ItemList.Machine_MV_Macerator,
        ItemList.Machine_HV_Macerator,
        ItemList.Machine_EV_Macerator,
        ItemList.Machine_IV_Macerator);

    public TST_BigBroArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_BigBroArray(String aName) {
        super(aName);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        GTCM_ProcessingLogic gtcm_processingLogic = new GTCM_ProcessingLogic();
        gtcm_processingLogic.setMaxParallelSupplier(() -> machines != null ? machines.stackSize : 1);
        return gtcm_processingLogic;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return super.getStructureDescription(stackSize);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder getTooltip() {
        GT_Multiblock_Tooltip_Builder gt_multiblock_tooltip_builder = new GT_Multiblock_Tooltip_Builder()
            .addMachineType("Processing Array")
            .addInfo("Big brother array")
            .addInfo("Big brother is watching you!")
            .addInfo("Provides 256x/1024x/4096x/Integer.MAX_VALUEx parallelism")
            .addInfo("Supports all types of single block machines, including single block generator")
            .addStructureHint("7x7 cube")
            .addStructureHint("Controller is on front center")
            .addStructureHint("Maintaince, energy, dynamo, input bus/hatch, output bus/hatch replace any block");
        gt_multiblock_tooltip_builder.toolTipFinisher("Twist Space Technology");
        return gt_multiblock_tooltip_builder;
    }

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
                    GT_HatchElement.Energy.or(GT_HatchElement.ExoticEnergy)
                        .or(GT_HatchElement.Dynamo),
                    GT_HatchElement.InputBus.or(GT_HatchElement.InputHatch),
                    GT_HatchElement.OutputBus.or(GT_HatchElement.OutputHatch))
                .adder(TST_BigBroArray::addToMachineList)
                .dot(1)
                .casingIndex(((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings4).getTextureIndex(0))
                .buildAndChain(GregTech_API.sBlockCasings4, 0))
        .build();

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (machines != null) {
            try {
                Field field = recipeBackendRefMapping.get(machineType);
                if (field == null) {
                    return null;
                }
                return (RecipeMap<?>) field.get(null);
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1.0f;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return machines != null ? machines.stackSize : 0;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addDynamoToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece("box", itemStack, hintsOnly, 3, 3, 0);
    }

    @Override
    public IStructureDefinition<TST_BigBroArray> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return getTooltip();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece("box", 3, 3, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BigBroArray(mName);
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
                                    break;
                                }
                            } catch (IllegalArgumentException e) {

                            }
                        }
                    }
                }
            }
            if (machineType != null) {
                int xCoord = getBaseMetaTileEntity().getXCoord();
                int yCoord = getBaseMetaTileEntity().getYCoord();
                int zCoord = getBaseMetaTileEntity().getZCoord();
                TST_Network.tst.sendToAll(new PackSyncMachineType(xCoord, yCoord, zCoord, machineType));
            }
        } else {
            machineType = null;
            addOutput(machines);
            machines = null;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
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
                if (Strings.isEmpty(machineType) || activeTextures.length == 0) {
                    return new ITexture[] {
                        Textures.BlockIcons
                            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 2)),
                        TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                            .extFacing()
                            .build(),
                        TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build() };
                }
                return activeTextures;
            }
            if (machineType == null || idleTextures.length == 0) {
                return new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                return idleTextures;
            }
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)) };
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
            TileEntity tileEntity = DimensionManager.getWorld(message.worldId)
                .getTileEntity(message.x, message.y, message.z);
            if (tileEntity instanceof BaseMetaTileEntity) {
                TST_BigBroArray array = (TST_BigBroArray) ((BaseMetaTileEntity) tileEntity).getMetaTileEntity();
                return new PackSyncMachineType(message.x, message.y, message.z, array.machineType);
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
