package com.Nxer.TwistSpaceTechnology.common.machine.NuclearReactor;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.NuclearReactorBlock;
import static com.Nxer.TwistSpaceTechnology.common.machine.NuclearReactor.TST_NuclearReactorCalculate.influenceExplosion;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.items.GT_IntegratedCircuit_Item;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.reactor.IReactorComponent;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.IC2DamageSource;
import ic2.core.Ic2Items;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import ic2.core.init.MainConfig;
import ic2.core.item.reactor.ItemReactorHeatStorage;
import ic2.core.util.ConfigUtil;
import ic2.core.util.LogCategory;
import ic2.core.util.Util;

public class TST_NuclearReactor extends GTCM_MultiMachineBase<TST_NuclearReactor> {

    private static final int x = 15;
    private static final int y = 15;
    private static final int z = 15;

    ItemStack[][][] reactorCells = new ItemStack[15][15][15];
    private double heat = 0;
    private double maxHeat = 0;

    private double emitHeat = 0;
    private boolean preActivate;
    private float output;
    private int EmitHeatbuffer;
    private float hem;
    private static final double huOutputModifier = 2.0F
        * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/FluidReactor/outputModifier");

    // Structure:
    //
    // Blocks:
    // A -> ofBlock...(blockAlloy, 0, ...);
    // B -> ofBlock...(blockAlloyGlass, 0, ...);
    // C -> ofBlock...(blockreactorvessel, 0, ...);
    // D -> ofBlock...(fluidCoolant, 0, ...);
    private static final int offsetX = 7, offsetY = 0, offsetZ = 7;

    private static final IStructureDefinition<TST_NuclearReactor> structureDefinition = StructureDefinition
        .<TST_NuclearReactor>builder()
        .addShape(
            "main",
            new String[][] {
                { "AAAAAAAAAAAAAAA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA",
                    "ABBBBBBCBBBBBBA", "ABBBBBCCCBBBBBA", "ACCCCCCCCCCCCCA", "ABBBBBCCCBBBBBA", "ABBBBBBCBBBBBBA",
                    "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "AAAAAAAAAAAAAAA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB",
                    "BDDDDDDDDDDDDDB", "BDDDDDCCCDDDDDB", "CDDDDDC CDDDDDC", "BDDDDDCCCDDDDDB", "BDDDDDDDDDDDDDB",
                    "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB",
                    "BDDDDDDDDDDDDDB", "BDDDDDCCCDDDDDB", "CDDDDDC CDDDDDC", "BDDDDDCCCDDDDDB", "BDDDDDDDDDDDDDB",
                    "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "CDDEEEEEEEEEDDC", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "CDDEEEEEEEEEDDC", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "CDDEEEEEEEEEDDC", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBCCCBBBBBA", "BDDDDDCCCDDDDDB", "BDDDDDCCCDDDDDB", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "CCCEEEEEEEEECCC", "CCCEEEEEEEEECCC", "CCCEEEEEEEEECCC", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "BDDDDDCCCDDDDDB", "BDDDDDCCCDDDDDB", "ABBBBBCCCBBBBBA" },
                { "ACCCCCC~CCCCCCA", "CDDDDDC CDDDDDC", "CDDDDDC CDDDDDC", "CDDEEEEEEEEEDDC", "CDDEEEEEEEEEDDC",
                    "CDDEEEEEEEEEDDC", "CCCEEEEEEEEECCC", "C  EEEEEEEEE  C", "CCCEEEEEEEEECCC", "CDDEEEEEEEEEDDC",
                    "CDDEEEEEEEEEDDC", "CDDEEEEEEEEEDDC", "CDDDDDC CDDDDDC", "CDDDDDC CDDDDDC", "ACCCCCCCCCCCCCA" },
                { "ABBBBBCCCBBBBBA", "BDDDDDCCCDDDDDB", "BDDDDDCCCDDDDDB", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "CCCEEEEEEEEECCC", "CCCEEEEEEEEECCC", "CCCEEEEEEEEECCC", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "BDDDDDCCCDDDDDB", "BDDDDDCCCDDDDDB", "ABBBBBCCCBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "CDDEEEEEEEEEDDC", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "CDDEEEEEEEEEDDC", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "CDDEEEEEEEEEDDC", "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB",
                    "BDDEEEEEEEEEDDB", "BDDEEEEEEEEEDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB",
                    "BDDDDDDDDDDDDDB", "BDDDDDCCCDDDDDB", "CDDDDDC CDDDDDC", "BDDDDDCCCDDDDDB", "BDDDDDDDDDDDDDB",
                    "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "ABBBBBBCBBBBBBA", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB",
                    "BDDDDDDDDDDDDDB", "BDDDDDCCCDDDDDB", "CDDDDDC CDDDDDC", "BDDDDDCCCDDDDDB", "BDDDDDDDDDDDDDB",
                    "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "BDDDDDDDDDDDDDB", "ABBBBBBCBBBBBBA" },
                { "AAAAAAAAAAAAAAA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA",
                    "ABBBBBBCBBBBBBA", "ABBBBBCCCBBBBBA", "ACCCCCCCCCCCCCA", "ABBBBBCCCBBBBBA", "ABBBBBBCBBBBBBA",
                    "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "ABBBBBBCBBBBBBA", "AAAAAAAAAAAAAAA" } })
        .addElement('A', ofBlock(NuclearReactorBlock, 0))// A -> ofBlock...(blockAlloy, 0, ...);
        .addElement('B', ofBlock(NuclearReactorBlock, 1))// B -> ofBlock...(blockAlloyGlass, 0, ...);
        .addElement('C', ofBlock(NuclearReactorBlock, 2))// C -> ofBlock...(blockreactorvessel, 0, ...);
        .addElement('D', ofBlock(BlocksItems.getFluidBlock(InternalName.fluidCoolant), 0))
        .addElement('E', ofBlock(NuclearReactorBlock, 3))// D -> centerBlock
        .build();
    private IIcon blockIcon;

    public TST_NuclearReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        blockIcon = NuclearReactorBlock.getIcon(0, 4);
    }

    public TST_NuclearReactor(String aName) {
        super(aName);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 0;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 0;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        lEUt = (long) output;
        output = 0;
        return SimpleCheckRecipeResult.ofSuccess("running fine");
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % this.getTickRate() == 0 && mMachine) {
            if (!preActivate) updateBusList();
            updateItems();
            TileEntity te = (TileEntity) aBaseMetaTileEntity;
            if (!te.getWorldObj()
                .doChunksNearChunkExist(te.xCoord, te.yCoord, te.zCoord, 2)) {
                this.output = 0.0F;
            } else {
                if (IC2.platform.isSimulating()) {
                    MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) te));
                }
                this.dropAllUnfittingStuff();
                this.output = 0.0F;
                this.maxHeat = 10000;
                this.hem = 1.0F;
                this.processChambers();
                // this.processfluidsSlots();
                int huOtput = (int) (huOutputModifier * (float) this.EmitHeatbuffer);
                var input = mInputBusses.get(0);
                var output = mOutputBusses.get(0);
                int outputroom = input.getCapacity() - input.getFluidAmount();
                if (outputroom > 0) {
                    FluidStack draincoolant;
                    if (huOtput < outputroom) {
                        draincoolant = input.drain(huOtput, false);
                    } else {
                        draincoolant = input.drain(outputroom, false);
                    }
                    if (draincoolant != null) {
                        this.emitHeat = draincoolant.amount;
                        huOtput -= input.drain(draincoolant.amount, true).amount;
                        int amount = output.fill(
                            new FluidStack(BlocksItems.getFluid(InternalName.fluidHotCoolant), draincoolant.amount),
                            true);
                        huOtput += draincoolant.amount - amount;
                    } else {
                        this.emitHeat = 0;
                    }
                } else {
                    this.emitHeat = 0;
                }
                this.addHeat(huOtput / 2);
                this.EmitHeatbuffer = 0;
                if (this.calculateHeatEffects()) {
                    return;
                }
                // this.setActive(this.heat >= 1000 || this.output > 0.0F);
                this.markDirty();
            }
            (IC2.network.get()).updateTileEntityField(te, "output");
        } else {
            preActivate = mMachine;
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    public boolean calculateHeatEffects() {
        if (this.heat >= 4000 && IC2.platform.isSimulating()
            && !(ConfigUtil.getFloat(MainConfig.get(), "protection/reactorExplosionPowerLimit") <= 0.0F)) {
            float power = (float) this.heat / (float) this.maxHeat;
            if (power >= 1.0F) {
                this.explode();
                return true;
            } else {
                int[] coord;
                Block block;
                Material mat;
                if (power >= 0.85F && getWorld().rand.nextFloat() <= 0.2F * this.hem) {
                    coord = this.getRandCoord(2);
                    if (coord != null) {
                        block = getWorld().getBlock(coord[0], coord[1], coord[2]);
                        if (block.isAir(getWorld(), coord[0], coord[1], coord[2])) {
                            getWorld().setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
                        } else if (block.getBlockHardness(getWorld(), coord[0], coord[1], coord[2]) >= 0.0F
                            && getWorld().getTileEntity(coord[0], coord[1], coord[2]) == null) {
                                mat = block.getMaterial();
                                if (mat != Material.rock && mat != Material.iron
                                    && mat != Material.lava
                                    && mat != Material.ground
                                    && mat != Material.clay) {
                                    getWorld().setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
                                } else {
                                    getWorld().setBlock(coord[0], coord[1], coord[2], Blocks.flowing_lava, 15, 7);
                                }
                            }
                    }
                }

                if (power >= 0.7F) {
                    TileEntity te = (TileEntity) getBaseMetaTileEntity();
                    List list1 = getWorld().getEntitiesWithinAABB(
                        EntityLivingBase.class,
                        AxisAlignedBB.getBoundingBox(
                            (te.xCoord - 3),
                            (te.yCoord - 3),
                            (te.zCoord - 3),
                            (te.xCoord + 4),
                            (te.yCoord + 4),
                            (te.zCoord + 4)));

                    for (Object o : list1) {
                        Entity ent = (Entity) o;
                        ent.attackEntityFrom(
                            IC2DamageSource.radiation,
                            (float) ((int) ((float) getWorld().rand.nextInt(4) * this.hem)));
                    }
                }

                if (power >= 0.5F && getWorld().rand.nextFloat() <= this.hem) {
                    coord = this.getRandCoord(2);
                    if (coord != null) {
                        block = getWorld().getBlock(coord[0], coord[1], coord[2]);
                        if (block.getMaterial() == Material.water) {
                            getWorld().setBlockToAir(coord[0], coord[1], coord[2]);
                        }
                    }
                }

                if (power >= 0.4F && getWorld().rand.nextFloat() <= this.hem) {
                    coord = this.getRandCoord(2);
                    if (coord != null && getWorld().getTileEntity(coord[0], coord[1], coord[2]) == null) {
                        block = getWorld().getBlock(coord[0], coord[1], coord[2]);
                        mat = block.getMaterial();
                        if (mat == Material.wood || mat == Material.leaves || mat == Material.cloth) {
                            getWorld().setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
                        }
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    private long getTickRate() {
        return 20;
    }

    public void explode() {
        float boomPower = 10.0F;
        float boomMod = 1.0F;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    ItemStack stack = getItemAt(i, j, k);
                    if (stack != null && stack.getItem() instanceof IReactorComponent) {
                        float f = influenceExplosion(((IReactorComponent) stack.getItem()), this, stack);
                        if (f > 0.0F && f < 1.0F) {
                            boomMod *= f;
                        } else {
                            boomPower += f;
                        }
                    }
                    setItemAt(i, j, k, null);
                }
            }
        }
        TileEntity te = (TileEntity) getBaseMetaTileEntity();
        boomPower *= this.hem * boomMod;
        IC2.log.log(
            LogCategory.PlayerActivity,
            Level.INFO,
            "Nuclear Reactor at %s melted (raw explosion power %f)",
            Util.formatPosition(te),
            boomPower);
        boomPower = Math.min(boomPower, ConfigUtil.getFloat(MainConfig.get(), "protection/reactorExplosionPowerLimit"));
        getWorld().setBlockToAir(te.xCoord, te.yCoord, te.zCoord);
        ExplosionIC2 explosion = new ExplosionIC2(
            getWorld(),
            null,
            te.xCoord,
            te.yCoord,
            te.zCoord,
            boomPower,
            0.01F,
            ExplosionIC2.Type.Nuclear);
        explosion.doExplosion();
    }

    public void dropAllUnfittingStuff() {
        ItemStack stack;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    stack = getItemAt(i, j, k);
                    if (stack != null && !isUsefulItem(stack, false, true)) {
                        setItemAt(i, j, k, null);
                        eject(stack);
                    }
                }
            }
        }
    }

    public void eject(ItemStack drop) {
        if (IC2.platform.isSimulating() && drop != null) {
            float f = 0.7F;
            double d = (double) (getWorld().rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
            double d1 = (double) (getWorld().rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
            double d2 = (double) (getWorld().rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
            TileEntity te = (TileEntity) getBaseMetaTileEntity();
            EntityItem entityitem = new EntityItem(
                getWorld(),
                (double) te.xCoord + d,
                (double) te.yCoord + d1,
                (double) te.zCoord + d2,
                drop);
            entityitem.delayBeforeCanPickup = 10;
            getWorld().spawnEntityInWorld(entityitem);
        }
    }

    public void updateItems() {
        var realInput = mInputBusses.get(x);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    var item = getItemAt(i, j, k);
                    var mappedInput = mInputBusses.get(i);

                    if (item == null || !item.isItemEqual(mappedInput.getStackInSlot(j * z + z))) {
                        if (item != null && outputDepletedStack(item)) setItemAt(i, j, k, null);
                        item = getItemAt(i, j, k);
                        if (item == null) {
                            for (var stack : realInput.getRealInventory()) {
                                if (stack != null && stack.stackSize > 0
                                    && stack.isItemEqual(mappedInput.getStackInSlot(j * z + z))) {
                                    stack.stackSize--;
                                    setItemAt(i, j, k, new ItemStack(stack.getItem(), 1));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateBusList() {
        GT_MetaTileEntity_Hatch_InputBus[] tmp = new GT_MetaTileEntity_Hatch_InputBus[16];
        for (int i = 0; i <= x; i++) {
            var bus = mInputBusses.get(i);
            int slot = bus.getCircuitSlot();
            ItemStack item = bus.getStackInSlot(slot);
            if (item.getItem() instanceof GT_IntegratedCircuit_Item cr) {
                int realSlot = cr.getDamage(item);
                tmp[realSlot] = bus;
            }
        }
        for (int i = 0; i <= x; i++) {
            mInputBusses.set(i, tmp[i]);
        }
    }

    public boolean outputDepletedStack(ItemStack stack) {
        return mOutputBusses.get(0)
            .storeAll(stack);
    }

    @Override
    public IStructureDefinition<TST_NuclearReactor> getStructureDefinition() {
        return structureDefinition;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        var tt = new GT_Multiblock_Tooltip_Builder();
        tt.toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return mInputHatches.size() == 16 && mOutputBusses.size() == 1
            && mOutputHatches.size() == 1
            && mInputBusses.size() == 1
            && mMaintenanceHatches.size() == 1
            && mEnergyHatches.size() == 0
            && mDynamoHatches.size() > 0
            && checkPiece("main", 10, 10, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_NuclearReactor(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }

    public static boolean isUsefulItem(ItemStack stack, boolean forInsertion, boolean fluidCoolReactor) {
        Item item = stack.getItem();
        if (item == null) {
            return false;
        } else if (forInsertion && fluidCoolReactor
            && item.getClass() == ItemReactorHeatStorage.class
            && ((ItemReactorHeatStorage) item).getCustomDamage(stack) > 0) {
                return false;
            } else if (item instanceof IReactorComponent) {
                return true;
            } else {
                return item == Ic2Items.TritiumCell.getItem() || item == Ic2Items.reactorDepletedUraniumSimple.getItem()
                    || item == Ic2Items.reactorDepletedUraniumDual.getItem()
                    || item == Ic2Items.reactorDepletedUraniumQuad.getItem()
                    || item == Ic2Items.reactorDepletedMOXSimple.getItem()
                    || item == Ic2Items.reactorDepletedMOXDual.getItem()
                    || item == Ic2Items.reactorDepletedMOXQuad.getItem();
            }
    }

    public void processChambers() {
        for (int pass = 0; pass < 2; ++pass) {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        ItemStack stack = reactorCells[i][j][k];
                        if (stack != null && stack.getItem() instanceof IReactorComponent comp) {
                            TST_NuclearReactorCalculate.processChamber(comp, this, stack, i, j, k, pass == 0);
                        }
                    }
                }
            }
        }
    }

    public int[] getRandCoord(int radius) {
        TileEntity te = (TileEntity) getBaseMetaTileEntity();
        if (radius <= 0) {
            return null;
        } else {
            int[] c = new int[] { te.xCoord + te.getWorldObj().rand.nextInt(2 * radius + 1) - radius,
                te.yCoord + te.getWorldObj().rand.nextInt(2 * radius + 1) - radius,
                te.zCoord + te.getWorldObj().rand.nextInt(2 * radius + 1) - radius };
            return c[0] == te.xCoord && c[1] == te.yCoord && c[2] == te.zCoord ? null : c;
        }
    }

    public ItemStack getItemAt(int x, int y, int z) {
        return reactorCells[x][y][z];
    }

    public double getHeat() {
        return this.heat;
    }

    public double getMaxHeat() {
        return this.maxHeat;
    }

    public void setHeat(double v) {
        this.heat = v;
    }

    public boolean produceEnergy() {
        return mMachine;
    }

    public void addHeat(int heat) {
        this.heat += heat;
    }

    public void setItemAt(int x, int y, int z, ItemStack item) {
        reactorCells[x][y][z] = item;
    }

    public void setMaxHeat(double maxHeat) {
        this.maxHeat = maxHeat;
    }

    public void addEmitHeat(int heat) {
        this.EmitHeatbuffer += heat;
    }

    public float getHeatEffectModifier() {
        return this.hem;
    }

    public void setHeatEffectModifier(float newHEM) {
        this.hem = newHEM;
    }

    public void addOutput(float energy) {
        this.output += energy;
    }

    public World getWorld() {
        return getBaseMetaTileEntity().getWorld();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece("main", stackSize, hintsOnly, offsetX, offsetY, offsetZ);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this
            .survivialBuildPiece("main", stackSize, offsetX, offsetY, offsetZ, realBudget, source, actor, false, true);
    }
}
