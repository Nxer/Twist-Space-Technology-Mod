package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.BLUE_PRINT_INFO;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textFrontCenter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.loader.Loaders.magicCasing;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.mojang.authlib.GameProfile;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TilePedestal;

public class TST_InfusionMaterialDispenser extends GTCM_MultiMachineBase<TST_InfusionMaterialDispenser> {

    public TST_InfusionMaterialDispenser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_InfusionMaterialDispenser(String aName) {
        super(aName);
    }

    private TileInfusionMatrix targetMatrix;
    private TilePedestal mainPedestal; //
    private ArrayList<TilePedestal> subPedestals = new ArrayList<>();

    public EntityPlayerMP fakePlayer = null;
    public String playerName = null;// The names of the items inside the controller should be named after the real
                                    // players' names.

    public static final int STATE_IDLE = 0;
    public static final int STATE_INFUSING = 1;
    private boolean outputProcessed = false;
    private IStructureDefinition<TST_InfusionMaterialDispenser> multiDefinition = null;
    public int infusionState = STATE_IDLE;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("infusionState", this.infusionState);
        aNBT.setBoolean("outputProcessed", this.outputProcessed);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.infusionState = aNBT.getInteger("infusionState");
        this.outputProcessed = aNBT.getBoolean("outputProcessed");
        this.subPedestals.clear();
        super.loadNBTData(aNBT);
    }

    // spotless:off
    //In the logical section, code formatting is prohibited to avoid affecting readability.
    @Override
    public @NotNull CheckRecipeResult checkProcessing()  {
        ArrayList<ItemStack> tItemsList = getStoredInputs();
        //Set some basic parameters
        setElectricityStats();

        //Actually, it's not necessary to check every time, but who cares about this little expense?
        World WORLD = this.getBaseMetaTileEntity().getWorld();
        int x = this.getBaseMetaTileEntity().getXCoord();
        int y = this.getBaseMetaTileEntity().getYCoord();
        int z = this.getBaseMetaTileEntity().getZCoord();

        TileEntity tempTile = null;
         tempTile = WORLD.getTileEntity(x, y-1, z );
         //Check if there is an infusion matrix beneath the main unit.
         if (!(tempTile instanceof TileInfusionMatrix)) {
             return SimpleCheckRecipeResult.ofFailure("Can't find infusion matrix");
         }
         targetMatrix = (TileInfusionMatrix) tempTile;

         //Check if there is an Unactivated infusion matrix .
        if(!targetMatrix.active){
            return SimpleCheckRecipeResult.ofFailure("Unactivated infusion matrix");
        }

        //Obtain the entities of the pedestal.If targetMatrix is activated, then the mainPedestal must exist.
        mainPedestal = (TilePedestal) WORLD.getTileEntity(x, y-3, z );
        if(subPedestals.isEmpty()){AddSubPedestals();}

        if (this.fakePlayer == null) {
            //This code is the core of Gadomancy's Infusion Claw. Thank you for the open source.
            //Otherwise, I might get stuck by this thing for a very long time.
            if(getControllerSlot()==null)
                return SimpleCheckRecipeResult.ofFailure("The controller should contain a piece of paper with the player's name on it.");
            playerName = getControllerSlot().getDisplayName();
            this.fakePlayer = new EntityPlayerMP(MinecraftServer.getServer(),
                (WorldServer)WORLD,
                new GameProfile(UUID.randomUUID(), "[InfusionCore]"),
                new ItemInWorldManager(WORLD));
            Thaumcraft.proxy.getCompletedResearch()
                .put(this.fakePlayer.getCommandSenderName(), ResearchManager.getResearchForPlayerSafe(playerName));
        }
        //The two states can be mutually transferred.
        //Since the machine should immediately re-run this function once it successfully operates, I believe there is no significant loss of time.
        if(infusionState == STATE_IDLE){
            if(tItemsList.isEmpty() && isAllPedestalsEmpty() && mainPedestal.getStackInSlot(0)==null){
                return SimpleCheckRecipeResult.ofFailure("waiting for infusion");
            }
            if(isAllPedestalsEmpty() && mainPedestal.getStackInSlot(0)==null){
                if(isPedestalSpaceSufficient(tItemsList, subPedestals)>0)
                    return SimpleCheckRecipeResult.ofFailure( isPedestalSpaceSufficient(tItemsList, subPedestals)+"pedestals are missing");
                else{
                    insertItemsIntoPedestals(tItemsList,mainPedestal,subPedestals);
                    //Deletion maybe lazy deletion, so it is necessary to manually clear the input bus.
                    //If not, it will lead to multiple reads of the same item.
                    for (MTEHatchInputBus bus : validMTEList(mInputBusses)) {
                        if (!(bus instanceof MTEHatchCraftingInputME)) {
                            IGregTechTileEntity tile = bus.getBaseMetaTileEntity();
                            for (int i = 0; i < tile.getSizeInventory(); i++) {
                                tile.setInventorySlotContents(i, null);
                            }
                        }
                    }
                    //This might not work because the research has not been finished.
                    targetMatrix.craftingStart(fakePlayer);
                    infusionState = STATE_INFUSING;
                    return SimpleCheckRecipeResult.ofSuccess("infusioning");
                }
            }
        }

        if(infusionState == STATE_INFUSING) {
            if(targetMatrix.crafting) {
                return SimpleCheckRecipeResult.ofSuccess("infusioning");
            } else {
                if (!outputProcessed) {
                    //If the infusion is not crafted at all, all the items will be recycled. This wasn't in my plan, but the performance was good.
                    collectAndOutputResults();
                    outputProcessed = true;
                    return SimpleCheckRecipeResult.ofSuccess("infusion complete");
                }
                infusionState = STATE_IDLE;
                outputProcessed = false;
            }
        }
        return SimpleCheckRecipeResult.ofFailure("unknown infusion state");
    }
    //Make the fake player be null, so that it will indirectly affect the progress of the research during the inspection.
    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.fakePlayer = null;
    }

    @Override
    public void onDisableWorking() {
        this.mOutputItems = new ItemStack[0];
        this.outputProcessed = false;
        super.onDisableWorking();
    }

    protected void setElectricityStats() {
        this.mOutputItems = new ItemStack[0];

        this.mEfficiency = 10000;
        this.mEfficiencyIncrease = 10000;
        //The processing time must be set; otherwise, the items will not be popped out.
        this.mMaxProgresstime = 1;
        OverclockCalculator calculator = new OverclockCalculator().setEUt(0)
            .setAmperage(0)
            .setDuration(1)
            .enablePerfectOC();
        calculator.calculate();
    }
    //Check whether the number of pedestal is sufficient
    public int isPedestalSpaceSufficient(ArrayList<ItemStack> tItemsList, ArrayList<TilePedestal> subPedestals){
        int totalItems = 0;
        for (ItemStack stack : tItemsList) {
            if (stack != null && stack.stackSize > 0) {
                totalItems += stack.stackSize;
            }
        }
        int totalPedestals = subPedestals.size() + 1;
            return totalItems-totalPedestals;
    }

    public void insertItemsIntoPedestals(ArrayList<ItemStack> ItemsList, TilePedestal mainPedestal, ArrayList<TilePedestal> subPedestals) {
        if (mainPedestal.getStackInSlot(0) == null && !ItemsList.isEmpty()) {
            mainPedestal.setInventorySlotContents(0, ItemsList.get(ItemsList.size()-1).copy());
            ItemsList.get(ItemsList.size()-1).stackSize--;
            if (ItemsList.get(ItemsList.size()-1).stackSize <= 0) {
                ItemsList.remove(ItemsList.size()-1);
            }
        }
        for (int i = ItemsList.size()-1; i >= 0; i--) {
            while (ItemsList.get(i).stackSize > 0) {
                TilePedestal tp = null;
                for (TilePedestal pedestal : subPedestals) {
                    if (pedestal.getStackInSlot(0) == null) {
                        tp = pedestal;
                        break;
                    }
                }
                if (tp == null) break;
                tp.setInventorySlotContents(0, ItemsList.get(i).copy());
                ItemsList.get(i).stackSize--;

                if (ItemsList.get(i).stackSize <= 0) {
                    ItemsList.remove(i);
                    break;
                }
            }
        }
    }
//The lock was used. However, I don't understand why simply removing it would cause the same product to be generated in double amounts every two times.
    private void collectAndOutputResults() {

        ArrayList<ItemStack> outputBuffer = new ArrayList<>();

        synchronized (this) {
            if (mainPedestal.getStackInSlot(0) != null) {
                outputBuffer.add(mainPedestal.getStackInSlot(0).copy());
                mainPedestal.setInventorySlotContents(0, null);
            }

            for (TilePedestal pedestal : subPedestals) {
                if (pedestal != null && pedestal.getStackInSlot(0) != null) {
                    outputBuffer.add(pedestal.getStackInSlot(0).copy());
                    pedestal.setInventorySlotContents(0, null);
                }
            }
        }
        this.mOutputItems = outputBuffer.toArray(new ItemStack[0]);

    }

    //Check to see if all the pedestals are empty.
    public boolean isAllPedestalsEmpty() {
        if (subPedestals == null || subPedestals.isEmpty()) {
            return true;
        }
        for (TilePedestal pedestal : subPedestals) {
                ItemStack stack = pedestal.getStackInSlot(0);
                if (stack != null) {
                    return false;
            }
        }
        return true;
    }

//Migrate the original code for pedestal scanning
    public void AddSubPedestals() {
        this.subPedestals.clear();
        World world = this.getBaseMetaTileEntity().getWorld();
        int centerX = this.getBaseMetaTileEntity().getXCoord();
        int centerY = this.getBaseMetaTileEntity().getYCoord();
        centerY--;
        int centerZ = this.getBaseMetaTileEntity().getZCoord();

        // Scanning range: ±12 grids in X/Z directions, -5 to +10 grids in Y direction
        for (int xOffset = -12; xOffset <= 12; xOffset++) {
            for (int zOffset = -12; zOffset <= 12; zOffset++) {
                // Skip the center point (the main pedestals)
                if (xOffset == 0 && zOffset == 0) continue;
                for (int yOffset = -5; yOffset <= 10; yOffset++) {
                    int x = centerX + xOffset;
                    int y = centerY + yOffset;
                    int z = centerZ + zOffset;
                    TileEntity te = world.getTileEntity(x, y, z);
                    if (te instanceof TilePedestal) {
                        this.subPedestals.add((TilePedestal) te);
                        break; // For each X/Z column, only the first encountered pedestals is taken.
                    }
                }
            }
        }
    }
    // spotless:on

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.subPedestals.clear();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        this.subPedestals.clear();
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final int horizontalOffSet = 4;
    private final int verticalOffSet = 0;
    private final int depthOffSet = 4;

    private static final String[][] shape = new String[][] {
        { "         ", "         ", "         ", "         ", "    ~    ", "         ", "         ", "         ",
            "         " },
        { "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ",
            "         " },
        { "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ",
            "         " },
        { "    A    ", "         ", "         ", "         ", "A       A", "         ", "         ", "         ",
            "    A    " }, };

    @Override
    public IStructureDefinition<TST_InfusionMaterialDispenser> getStructureDefinition() {
        if (multiDefinition == null) {
            this.multiDefinition = StructureDefinition.<TST_InfusionMaterialDispenser>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    ofChain(
                        buildHatchAdder(TST_InfusionMaterialDispenser.class).atLeast(InputBus, OutputBus)
                            .adder(TST_InfusionMaterialDispenser::addToMachineList)
                            .casingIndex(1536)
                            .dot(1)
                            .buildAndChain(magicCasing, 0)))
                .build();
        }
        return multiDefinition;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_InfusionMaterialDispenser_MachineType
        // # Infusion Material Dispenser
        // #zh_CN 注魔原料分配器
        tt.addMachineType(TextEnums.tr("Tooltip_InfusionMaterialDispenser_MachineType"))
            // #tr Tooltip_InfusionMaterialDispenser_00
            // # automatically dispense? What? This is impossible!
            // #zh_CN 自动分配?什么?这不可能!
            .addInfo(TextEnums.tr("Tooltip_InfusionMaterialDispenser_00"))
            // #tr Tooltip_InfusionMaterialDispenser_01
            // # A paper with player's name should be in controller to enable the fakeplayer to obtain research.Otherwise, the machine will crash.
            // #zh_CN 需要在控制器内放入一张写有玩家名称的纸,使得假人获取研究进度,否则机器会崩溃.
            .addInfo(TextEnums.tr("Tooltip_InfusionMaterialDispenser_01"))
            // #tr Tooltip_InfusionMaterialDispenser_02
            // # By right-clicking controller with a screwdriver, the research progress can be actively refreshed.
            // #zh_CN 螺丝刀右键主机可以主动刷新研究进度.
            .addInfo(TextEnums.tr("Tooltip_InfusionMaterialDispenser_02"))
            // #tr Tooltip_InfusionMaterialDispenser_03
            // # For research, no management. If materials are directly recycled, it indicates that the research has not been unlocked and the infusion cannot be activated.
            // #zh_CN 对于研究不做管理,若材料被直接回收说明研究未解锁,无法开启注魔.
            .addInfo(TextEnums.tr("Tooltip_InfusionMaterialDispenser_03"))
            // #tr Tooltip_InfusionMaterialDispenser_04
            // # For essence, no management. If essences are insufficient, and the world accelerator is used...
            // #zh_CN 对于源质不做管理,若源质不足,并使用世界加速器的话...
            .addInfo(TextEnums.tr("Tooltip_InfusionMaterialDispenser_04"))
            // #tr Tooltip_InfusionMaterialDispenser_05
            // # The controller is located in the upper square of the infusion matrix.Also,remember to open InterfaceBlockingMode for the input bus.
            // #zh_CN 控制器在注魔矩阵的上面,另外输入总线记得开阻挡模式.
            .addInfo(TextEnums.tr("Tooltip_InfusionMaterialDispenser_05"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(11, 10, 23, true)
            .addController(textFrontCenter)
            // #tr Tooltip_InfusionMaterialDispenser_HatchBusInfo
            // # Replace Magic mechanical blocks in any cabin
            // #zh_CN 任何舱室替换魔法机械方块
            .addInputBus(TextEnums.tr("Tooltip_InfusionMaterialDispenser_HatchBusInfo"))
            .addOutputBus(TextEnums.tr("Tooltip_InfusionMaterialDispenser_HatchBusInfo"))
            .toolTipFinisher(ModName);
        return tt;
        // spotless:on
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_InfusionMaterialDispenser(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1536),
                TextureFactory.of(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .glow()
                    .build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1536),
                TextureFactory.of(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1536) };
    }

}
