package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withDescription;
import static goodgenerator.loader.Loaders.magicCasing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.TST_StarThatNeverWas;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.hfstudio.guidenh.guide.scene.level.GuidebookPreviewWorld;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.util.HatchElementBuilder;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.blocks.ItemMachines;
import thaumcraft.common.config.ConfigBlocks;

/** Cached occupied geometry; preview channels select complete architectural components. */
public final class StarStructure {

    public static final String CHANNEL = "megastructure";
    public static final int UPPER_PREVIEW_SECTION = 7;
    public static final int FULL_PREVIEW_SECTION = 8;
    public static final int BASE_TIER = 1;
    public static final int DRONE_TIER = 2;
    // The high bit of an encoded material marks a cell in the tier 2 drone structure.
    public static final int DRONE_CELL_FLAG = 0x80;
    public static final int TIERED_GLASS = 12;
    public static final int NEUTRONIUM_GLASS_PREVIEW = 13;
    // Shared front panel, directly above the tier 2/3 miner controller.
    public static final int CONTROLLER_Y = 8;
    public static final int CONTROLLER_Z = 6;
    public static final int MINER_FLOOR_Y = 0;
    public static final int RECEIVER_Y = 32;
    private static final int FLOOR_ENERGY_PORT = 59;
    private static final int ASTRAL_SILVER_FRAME = 60;
    private static final int RITUAL_STONE = 61;
    private static final int MAX_MATERIAL_ID = RITUAL_STONE;
    private static final List<Cell> PREVIEW_HATCHES = new ArrayList<>();
    private static final List<Part> PARTS = load();
    private static IStructureDefinition<TST_StarThatNeverWas> definition;

    public static List<Part> parts() {
        return PARTS;
    }

    public static int casingTextureIndex() {
        return ((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(StarRings.Material.RADIANT_NAQUADAH.meta);
    }

    public static void finishPreviewHatches(TST_StarThatNeverWas host) {
        // A partial preview can finish without a successful machine check to apply the casing texture.
        for (Cell cell : PREVIEW_HATCHES) {
            if (!(host.tileAt(cell.x, cell.y, cell.z) instanceof IGregTechTileEntity tile)
                || !(tile.getMetaTileEntity() instanceof MTEHatch hatch)) continue;
            boolean floor = cell.symbol == symbol(FLOOR_ENERGY_PORT);
            ForgeDirection facing = floor ? ForgeDirection.DOWN
                : host.getBaseMetaTileEntity()
                    .getFrontFacing();
            if (tile.getFrontFacing() != facing) tile.setFrontFacing(facing);
            hatch.updateTexture(floor ? TstBlocks.MetaBlockCasing02.getTextureIndex(0) : casingTextureIndex());
        }
    }

    public static <T> IStructureElement<T> controllerCasing() {
        return ofBlock(GregTechAPI.sBlockCasings8, StarRings.Material.RADIANT_NAQUADAH.meta);
    }

    public static int selectedPreviewSection(ItemStack trigger) {
        if (!ChannelDataAccessor.hasSubChannel(trigger, CHANNEL)) return 1;
        return Math.max(1, Math.min(FULL_PREVIEW_SECTION, ChannelDataAccessor.getChannelData(trigger, CHANNEL)));
    }

    public static boolean includesDroneStructure(int masterTier) {
        return masterTier >= DRONE_TIER;
    }

    public static String meOutputBusNameKey() {
        return ItemList.Hatch_Output_Bus_ME.get(1)
            .getUnlocalizedName() + ".name";
    }

    public static boolean isGuidebookWorld(World world) {
        return world != null && world.isRemote && Loader.isModLoaded("guidenh") && isGuidebookPreviewWorld(world);
    }

    @Optional.Method(modid = "guidenh")
    private static boolean isGuidebookPreviewWorld(World world) {
        return world instanceof GuidebookPreviewWorld;
    }

    public static synchronized IStructureDefinition<TST_StarThatNeverWas> definition() {
        if (definition != null) return definition;
        var builder = StructureDefinition.<TST_StarThatNeverWas>builder()
            .addChannel(CHANNEL, 1, FULL_PREVIEW_SECTION);
        for (Part part : PARTS) builder.addShape(part.name, part.shape);
        for (int material = 0; material <= MAX_MATERIAL_ID; material++) {
            IStructureElement<TST_StarThatNeverWas> element = element(material);
            if (element != null) {
                builder.addElement(symbol(material), element);
                if (StarRings.material(material) != null)
                    builder.addElement((char) (256 + material), ringElement(element));
            }
        }
        builder.addElement(
            '@',
            HatchElementBuilder.<TST_StarThatNeverWas>builder()
                .atLeast(InputBus, InputHatch, OutputBus, Energy.or(ExoticEnergy))
                .adder(TST_StarThatNeverWas::addGeneralHatch)
                .descriptions(
                    () -> Arrays.asList(
                        InputBus.getDescriptionLangKey(),
                        InputHatch.getDescriptionLangKey(),
                        Energy.getDescriptionLangKey(),
                        ExoticEnergy.getDescriptionLangKey(),
                        meOutputBusNameKey()))
                .hatchItemFilterAnd(host -> hatchFilter(TST_StarThatNeverWas::acceptsGeneralHatch))
                .casingIndex(casingTextureIndex())
                .allowOnly(ForgeDirection.NORTH)
                .hint(1)
                .buildAndChain(controllerCasing()));
        builder.addElement(
            symbol(FLOOR_ENERGY_PORT),
            HatchElementBuilder.<TST_StarThatNeverWas>builder()
                .atLeast(Energy.or(ExoticEnergy))
                .casingIndex(TstBlocks.MetaBlockCasing02.getTextureIndex(0))
                .allowOnly(ForgeDirection.DOWN)
                .hint(2)
                .buildAndChain(TstBlocks.MetaBlockCasing02, 0));
        builder.addElement(
            symbol(46),
            HatchElementBuilder.<TST_StarThatNeverWas>builder()
                .atLeast(InputBus)
                .description(() -> "tst.star.ports.drone_input")
                .adder(TST_StarThatNeverWas::addDroneInput)
                .hint(3)
                .shouldReject(host -> false)
                .hatchItemFilter(host -> hatchFilter(StarDronePorts::acceptsItemInput))
                .shouldSkip((host, tile) -> tile != null && StarDronePorts.acceptsItemInput(tile.getMetaTileEntity()))
                .casingIndex(casingTextureIndex())
                .allowOnly(ForgeDirection.NORTH)
                .buildAndChain(controllerCasing()));
        builder.addElement(
            symbol(47),
            HatchElementBuilder.<TST_StarThatNeverWas>builder()
                .atLeast(OutputBus)
                .descriptions(() -> Arrays.asList(OutputBus.getDescriptionLangKey(), meOutputBusNameKey()))
                .adder(TST_StarThatNeverWas::addDroneOutput)
                .hint(4)
                .shouldReject(host -> false)
                .hatchItemFilter(host -> hatchFilter(StarDronePorts::acceptsDroneOutput))
                .shouldSkip((host, tile) -> tile != null && StarDronePorts.acceptsDroneOutput(tile.getMetaTileEntity()))
                .casingIndex(casingTextureIndex())
                .allowOnly(ForgeDirection.NORTH)
                .buildAndChain(controllerCasing()));
        builder.addElement(
            symbol(49),
            HatchElementBuilder.<TST_StarThatNeverWas>builder()
                .atLeast(InputHatch)
                .adder(TST_StarThatNeverWas::addDroneFluidInput)
                .hint(5)
                .shouldReject(host -> false)
                .hatchItemFilter(host -> hatchFilter(StarDronePorts::acceptsFluidInput))
                .shouldSkip((host, tile) -> tile != null && StarDronePorts.acceptsFluidInput(tile.getMetaTileEntity()))
                .casingIndex(casingTextureIndex())
                .allowOnly(ForgeDirection.NORTH)
                .buildAndChain(controllerCasing()));
        definition = builder.build();
        return definition;
    }

    private static Predicate<ItemStack> hatchFilter(Predicate<IMetaTileEntity> accepts) {
        return stack -> {
            if (stack == null || stack.getItemDamage() >= GregTechAPI.METATILEENTITIES.length) return false;
            IMetaTileEntity hatch = ItemMachines.getMetaTileEntity(stack);
            return hatch != null && accepts.test(hatch);
        };
    }

    private static IStructureElement<TST_StarThatNeverWas> element(int id) {
        StarRings.Material material = StarRings.material(id);
        if (material != null) return material.mod == null ? ofBlock(material.block(), material.meta)
            : external(material.mod, material.name, material.meta);
        return switch (id) {
            case 0 -> ofBlock(TstBlocks.MetaBlockCasing01, 9);
            case TIERED_GLASS, NEUTRONIUM_GLASS_PREVIEW -> lazy(() -> {
                var glass = GameRegistry.findBlock("bartworks", "BW_TieredGlass");
                return ofBlocksTiered((block, meta) -> {
                    if (block != glass) return null;
                    return switch (meta) {
                        case 6 -> BASE_TIER;
                        case 7 -> DRONE_TIER;
                        default -> null;
                    };
                },
                    Arrays.asList(Pair.of(glass, 6), Pair.of(glass, 7)),
                    0,
                    TST_StarThatNeverWas::setGlassTier,
                    TST_StarThatNeverWas::getGlassTier);
            });
            case 14 -> external("chisel", "arcane", 1);
            case 16 -> ofBlock(TstBlocks.CondensedStarCore, 0);
            case 19 -> external("chisel", "arcane", 5);
            case 23 -> external("dreamcraft", "BloodyIchorium", 0);
            case 25 -> external("chisel", "arcane", 0);
            case 27 -> external("AWWayofTime", "largeBloodStoneBrick", 0);
            case 28 -> external("AWWayofTime", "ritualStone", 2);
            case 29 -> ofBlock(TstBlocks.MetaBlockCasing02, 0);
            case 30 -> external("AWWayofTime", "ritualStone", 5);
            case 32 -> ofFrame(Materials.PrismaticNaquadah);
            case 33 -> ofBlock(TstBlocks.MetaBlockGlow01, 0);
            case 34, 35, 36, 37, 38, 39 -> external("thaumicbases", "crystalBlock", id - 34);
            case 40 -> external("thaumicbases", "crystalBlock", 7);
            case 41 -> external("Thaumcraft", "blockCosmeticSolid", 8);
            case 42 -> external("Thaumcraft", "blockCosmeticSolid", 11);
            case 50 -> ofChain(
                ofTileAdder(TST_StarThatNeverWas::addWardedGlass, ConfigBlocks.blockCosmeticOpaque, 2),
                external("Thaumcraft", "blockCosmeticOpaque", 2));
            case 51 -> external("dreamcraft", "BloodyThaumium", 0);
            case 53 -> ofBlock(magicCasing, 0);
            case 55 -> external("BloodArsenal", "blood_infused_diamond_block", 0);
            case 56 -> external("thaumicbases", "blockSalisMundus", 0);
            case 57 -> external("thaumicbases", "thauminiteBlock", 0);
            case 58 -> ofBlock(GregTechAPI.sBlockCasings8, 3);
            case ASTRAL_SILVER_FRAME -> ofFrame(Materials.AstralSilver);
            case RITUAL_STONE -> external("AWWayofTime", "ritualStone", 4);
            default -> null;
        };
    }

    private static IStructureElement<TST_StarThatNeverWas> external(String mod, String block, int meta) {
        // The registry-name helper does not supply diagnostic names. Resolve them lazily, after block registration.
        return lazy(() -> {
            var registered = GameRegistry.findBlock(mod, block);
            Item item = registered == null ? null : Item.getItemFromBlock(registered);
            String name = item == null ? mod + ":" + block + ":" + meta
                : new ItemStack(item, 1, meta).getUnlocalizedName() + ".name";
            return withDescription(Collections.singletonList(name), ofBlockUnlocalizedName(mod, block, meta, false));
        });
    }

    private static IStructureElement<TST_StarThatNeverWas> ringElement(
        IStructureElement<TST_StarThatNeverWas> physical) {
        return new IStructureElement<>() {

            @Override
            public boolean check(TST_StarThatNeverWas host, World world, int x, int y, int z) {
                return host.ownsRingPosition(x, y, z) ? world.isAirBlock(x, y, z)
                    : physical.check(host, world, x, y, z);
            }

            @Override
            public boolean spawnHint(TST_StarThatNeverWas host, World world, int x, int y, int z, ItemStack trigger) {
                return physical.spawnHint(host, world, x, y, z, trigger);
            }

            @Override
            public boolean placeBlock(TST_StarThatNeverWas host, World world, int x, int y, int z, ItemStack trigger) {
                return host.ownsRingPosition(x, y, z) || physical.placeBlock(host, world, x, y, z, trigger);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(TST_StarThatNeverWas host, World world, int x, int y, int z,
                ItemStack trigger, AutoPlaceEnvironment env) {
                return physical.getBlocksToPlace(host, world, x, y, z, trigger, env);
            }

            @Override
            public PlaceResult survivalPlaceBlock(TST_StarThatNeverWas host, World world, int x, int y, int z,
                ItemStack trigger, AutoPlaceEnvironment env) {
                return host.ownsRingPosition(x, y, z) ? PlaceResult.SKIP
                    : physical.survivalPlaceBlock(host, world, x, y, z, trigger, env);
            }

            @Override
            public List<String> getDescription(TST_StarThatNeverWas host) {
                return physical.getDescription(host);
            }
        };
    }

    private static char symbol(int material) {
        // Extended palette skips StructureLib's reserved ASCII controller marker and the separate ring range.
        if (material >= ASTRAL_SILVER_FRAME) return (char) (128 + material);
        return material == 45 ? '~' : material == 44 ? '@' : (char) ('A' + material);
    }

    private static int previewSection(int x, int y, int z, boolean ring) {
        double terraceRadius = Math.max(Math.max(Math.abs(x), Math.abs(z)), (Math.abs(x) + Math.abs(z)) / Math.sqrt(2));
        // Keep the entire miner cradle in section 1; the receiver, tall supports and authored rings belong to 7.
        if (ring || (y > 11 && (terraceRadius > 22 || y >= RECEIVER_Y - 2))) return UPPER_PREVIEW_SECTION;
        if (terraceRadius >= 88) return 6;
        // The well includes its lining, ports and complete innermost orbit, rather than four sliced shaft walls.
        if (x * x + z * z <= 28 * 28) return 1;
        // Diagonal seams follow the terrace gaps and pass between the large nodes and scattered elemental nodes.
        if (Math.abs(z) >= Math.abs(x)) return z < 0 ? 2 : 4;
        return x > 0 ? 3 : 5;
    }

    private static List<Part> load() {
        String path = "/assets/gtnhcommunitymod/structure/star_that_never_was.bin.gz";
        InputStream resource = StarStructure.class.getResourceAsStream(path);
        if (resource == null) throw new IllegalStateException("Missing star structure: " + path);
        try (DataInputStream input = new DataInputStream(new GZIPInputStream(resource))) {
            if (input.readInt() != 0x53544152) throw new IOException("Invalid star structure signature");
            int count = input.readInt();
            if (count < 1 || count > 256) throw new IOException("Invalid partition count");
            List<Part> parts = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                int minX = input.readShort(), minY = input.readShort(), minZ = input.readShort();
                int maxX = input.readShort(), maxY = input.readShort(), maxZ = input.readShort();
                int width = maxX - minX + 1, height = maxY - minY + 1, depth = maxZ - minZ + 1;
                if (width < 1 || height < 1 || depth < 1 || (long) width * height * depth > 2_000_000) {
                    throw new IOException("Invalid star partition bounds");
                }
                List<List<Cell>> sections = new ArrayList<>(UPPER_PREVIEW_SECTION * 2);
                for (int j = 0; j < UPPER_PREVIEW_SECTION * 2; j++) sections.add(new ArrayList<>());
                int blocks = input.readInt();
                if (blocks < 1 || blocks > 4096) throw new IOException("Unbounded preview partition");
                for (int n = 0; n < blocks; n++) {
                    int x = input.readShort(), y = input.readShort(), z = input.readShort();
                    int encodedMaterial = input.readUnsignedByte();
                    boolean droneStructure = (encodedMaterial & DRONE_CELL_FLAG) != 0;
                    int material = encodedMaterial & ~DRONE_CELL_FLAG;
                    if (material > MAX_MATERIAL_ID) throw new IOException("Invalid star material");
                    if (x < minX || x > maxX || y < minY || y > maxY || z < minZ || z > maxZ) {
                        throw new IOException("Star cell outside partition bounds");
                    }
                    int ringIndex = StarRings.indexAt(x, y, z);
                    boolean ring = ringIndex >= 0;
                    if (ring && StarRings.cells()
                        .get(ringIndex).material.id != material)
                        throw new IOException("Ring material does not match structure at " + x + "," + y + "," + z);
                    Cell cell = new Cell(x, y, z, ring ? (char) (256 + material) : symbol(material));
                    int section = previewSection(x, y, z, ring) - 1;
                    sections.get(section + (droneStructure ? UPPER_PREVIEW_SECTION : 0))
                        .add(cell);
                    if (material == 44 || material == 46
                        || material == 47
                        || material == 49
                        || material == FLOOR_ENERGY_PORT) PREVIEW_HATCHES.add(cell);
                }
                // Storage partitions stay bounded, but a channel assembles every part of the selected component.
                for (int j = 0; j < sections.size(); j++) {
                    List<Cell> cells = sections.get(j);
                    if (cells.isEmpty()) continue;
                    int section = j % UPPER_PREVIEW_SECTION + 1;
                    boolean droneStructure = j >= UPPER_PREVIEW_SECTION;
                    String name = "star_" + i + "_" + section + (droneStructure ? "_drones" : "");
                    parts.add(createPart(name, section, droneStructure, cells));
                }
            }
            return Collections.unmodifiableList(parts);
        } catch (IOException | IndexOutOfBoundsException e) {
            throw new IllegalStateException("Cannot read star structure", e);
        }
    }

    private static Part createPart(String name, int section, boolean droneStructure, List<Cell> points) {
        Cell first = points.get(0);
        int minX = first.x, minY = first.y, minZ = first.z;
        int maxX = minX, maxY = minY, maxZ = minZ;
        for (Cell cell : points) {
            minX = Math.min(minX, cell.x);
            minY = Math.min(minY, cell.y);
            minZ = Math.min(minZ, cell.z);
            maxX = Math.max(maxX, cell.x);
            maxY = Math.max(maxY, cell.y);
            maxZ = Math.max(maxZ, cell.z);
        }
        int width = maxX - minX + 1, height = maxY - minY + 1, depth = maxZ - minZ + 1;
        char[][][] cells = new char[depth][height][width];
        for (char[][] slice : cells) for (char[] row : slice) Arrays.fill(row, ' ');
        for (Cell cell : points) cells[maxZ - cell.z][maxY - cell.y][cell.x - minX] = cell.symbol;
        String[][] shape = new String[depth][height];
        for (int z = 0; z < depth; z++) for (int y = 0; y < height; y++) shape[z][y] = new String(cells[z][y]);
        return new Part(name, section, -minX, maxY - CONTROLLER_Y, maxZ - CONTROLLER_Z, shape, droneStructure);
    }

    @Desugar
    private record Cell(int x, int y, int z, char symbol) {}

    public static final class Part {

        public final String name;
        public final int a, b, c;
        public final boolean droneStructure;
        private final int previewSection;
        private final String[][] shape;

        private Part(String name, int previewSection, int a, int b, int c, String[][] shape, boolean droneStructure) {
            this.name = name;
            this.previewSection = previewSection;
            this.a = a;
            this.b = b;
            this.c = c;
            this.shape = shape;
            this.droneStructure = droneStructure;
        }

        public boolean isInPreviewSection(int selected) {
            return selected == FULL_PREVIEW_SECTION || selected == previewSection;
        }
    }
}
