package com.Nxer.TwistSpaceTechnology.system.OreProcess.machines;

import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.LubricantCost;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeDuration;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeEUt;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.moveUnprocessedItemsToOutputs;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.ticksOfPerFluidConsuming;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_02;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_03;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_04;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_05;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_06;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_Controller;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_MachineType;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltips_JoinWirelessNetWithoutEnergyHatch;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_OreProcessingFactory extends GTCM_MultiMachineBase<TST_OreProcessingFactory> {

    // region Class Constructor
    public TST_OreProcessingFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_OreProcessingFactory(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_OreProcessingFactory(this.mName);
    }

    // endregion

    // region Processing Logic
    private String ownerName;
    private UUID ownerUUID;
    private boolean isWirelessMode = false;
    private long EUtCanUse = 0;
    private short runTicks = 0;
    private long usingEU = 0;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("isWirelessMode", isWirelessMode);
        aNBT.setLong("EUtCanUse", EUtCanUse);
        aNBT.setShort("runTicks", runTicks);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isWirelessMode = aNBT.getBoolean("isWirelessMode");
        EUtCanUse = aNBT.getLong("EUtCanUse");
        runTicks = aNBT.getShort("runTicks");
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("isWirelessMode")) {
            // #tr Waila.TST_OreProcessingFactory.1
            // # In Wireless mode
            // #zh_CN 无线EU电网模式
            currentTip.add(EnumChatFormatting.AQUA + TextEnums.tr("Waila.TST_OreProcessingFactory.1"));
        }
        if (tag.getBoolean("isActive") && tag.getBoolean("isWirelessMode")) {
            currentTip.add(
                // #tr Waila.TST_OreProcessingFactory.2
                // # Current Using EU:
                // #zh_CN 当前消耗EU:
                EnumChatFormatting.AQUA + TextEnums.tr("Waila.TST_OreProcessingFactory.2")
                    + EnumChatFormatting.GOLD
                    + GTUtility.formatNumbers(tag.getLong("usingEU"))
                    + EnumChatFormatting.RESET
                    + " EU");
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("isWirelessMode", isWirelessMode);
            if (tileEntity.isActive()) {
                tag.setLong("usingEU", usingEU);
            }
        }
    }

    public CheckRecipeResult OP_Process_Wireless() {
        RecipeMap<?> recipeMap = getRecipeMap();
        ArrayList<ItemStack> inputs = getStoredInputs();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        long EUt = 0;
        // check every inputs
        for (ItemStack items : inputs) {
            boolean hasNotFound = true;
            for (GTRecipe recipe : recipeMap.getAllRecipes()) {
                if (recipe.mInputs == null || recipe.mInputs.length < 1) continue;
                if (GTUtility.areStacksEqual(recipe.mInputs[0], items)
                    && items.stackSize >= recipe.mInputs[0].stackSize) {
                    // found the recipe
                    hasNotFound = false;
                    ItemStack recipeInput = recipe.mInputs[0];
                    int parallel = items.stackSize / recipeInput.stackSize;

                    // decrease the input stack amount
                    items.stackSize -= parallel * recipeInput.stackSize;

                    // add EU cost
                    EUt += (long) recipe.mEUt * parallel;

                    // process output stacks
                    for (ItemStack recipeOutput : recipe.mOutputs) {
                        if (Integer.MAX_VALUE / parallel >= recipeOutput.stackSize) {
                            // direct output
                            outputs.add(GTUtility.copyAmountUnsafe(recipeOutput.stackSize * parallel, recipeOutput));
                        } else {
                            // separate to any integer max stack
                            long outputAmount = (long) parallel * recipeOutput.stackSize;
                            while (outputAmount > 0) {
                                if (outputAmount >= Integer.MAX_VALUE) {
                                    outputs.add(GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, recipeOutput));
                                    outputAmount -= Integer.MAX_VALUE;
                                } else {
                                    outputs.add(GTUtility.copyAmountUnsafe((int) outputAmount, recipeOutput));
                                    outputAmount = 0;
                                }
                            }
                        }
                    }
                }
            }
            // If is gt ore but not in recipe map
            // Handle it specially
            if (hasNotFound) {
                if (Objects.equals(items.getUnlocalizedName(), "gt.blockores")) {
                    TwistSpaceTechnology.LOG.info("OP system recipe has not write this material's: " + items);
                    outputs.add(items.copy());
                    items.stackSize = 0;
                } else if (moveUnprocessedItemsToOutputs) {
                    outputs.add(items.copy());
                    items.stackSize = 0;
                }
            }
        }
        if (outputs.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        usingEU = EUt * OreProcessRecipeDuration;
        if (!addEUToGlobalEnergyMap(ownerUUID, -usingEU)) {
            return CheckRecipeResultRegistry.insufficientPower(usingEU);
        }
        // set these to machine outputs
        mOutputItems = outputs.toArray(new ItemStack[0]);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public void OP_Process_Normal() {
        RecipeMap<?> recipeMap = getRecipeMap();
        ArrayList<ItemStack> inputs = getStoredInputs();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        long EUtCanUseNow = EUtCanUse;
        boolean canContinueCheckRecipe = EUtCanUse >= OreProcessRecipeEUt;

        // check every inputs
        for (ItemStack items : inputs) {
            if (!canContinueCheckRecipe) break;

            boolean hasNotFound = true;
            for (GTRecipe recipe : recipeMap.getAllRecipes()) {
                if (GTUtility.areStacksEqual(recipe.mInputs[0], items)
                    && items.stackSize >= recipe.mInputs[0].stackSize) {
                    // found the recipe
                    hasNotFound = false;
                    ItemStack recipeInput = recipe.mInputs[0];
                    // check parallel value
                    long EUtParallel = EUtCanUseNow / recipe.mEUt;
                    int InputParallel = items.stackSize / recipeInput.stackSize;
                    int parallel;
                    if (InputParallel >= EUtParallel) {
                        // if parallel is limited by EUt, set the flag to stop recipe checking.
                        canContinueCheckRecipe = false;
                        parallel = (int) Math.min(EUtParallel, Integer.MAX_VALUE);
                    } else {
                        parallel = InputParallel;
                    }

                    // decrease the input stack amount
                    items.stackSize -= parallel * recipeInput.stackSize;

                    // flush EUtCanUseNow
                    EUtCanUseNow -= (long) parallel * recipe.mEUt;

                    // process output stacks
                    for (ItemStack recipeOutput : recipe.mOutputs) {
                        if (Integer.MAX_VALUE / parallel >= recipeOutput.stackSize) {
                            // direct output
                            outputs.add(GTUtility.copyAmountUnsafe(recipeOutput.stackSize * parallel, recipeOutput));
                        } else {
                            // separate to any integer max stack
                            long outputAmount = (long) parallel * recipeOutput.stackSize;
                            while (outputAmount > 0) {
                                if (outputAmount >= Integer.MAX_VALUE) {
                                    outputs.add(GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, recipeOutput));
                                    outputAmount -= Integer.MAX_VALUE;
                                } else {
                                    outputs.add(GTUtility.copyAmountUnsafe((int) outputAmount, recipeOutput));
                                    outputAmount = 0;
                                }
                            }
                        }
                    }
                }
            }
            // If is gt ore but not in recipe map
            // Handle it specially
            if (hasNotFound) {
                if (Objects.equals(items.getUnlocalizedName(), "gt.blockores")) {
                    TwistSpaceTechnology.LOG.info("OP system recipe has not write this material's: " + items);
                    outputs.add(items.copy());
                    items.stackSize = 0;
                } else if (moveUnprocessedItemsToOutputs) {
                    outputs.add(items.copy());
                    items.stackSize = 0;
                }
            }
        }
        // set these to machine outputs
        mOutputItems = outputs.toArray(new ItemStack[0]);

        // set EUt
        lEUt = EUtCanUseNow - EUtCanUse;

    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        return isWirelessMode ? checkProcessing_wirelessMode() : checkProcessing_normalMode();
    }

    protected CheckRecipeResult checkProcessing_wirelessMode() {

        CheckRecipeResult result = OP_Process_Wireless();
        if (!result.wasSuccessful()) return result;
        boolean noRecipe = mOutputItems == null || mOutputItems.length < 1;
        updateSlots();
        if (noRecipe) return CheckRecipeResultRegistry.NO_RECIPE;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = OreProcessRecipeDuration;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    protected CheckRecipeResult checkProcessing_normalMode() {

        if (EUtCanUse < OreProcessRecipeEUt) return CheckRecipeResultRegistry.insufficientPower(OreProcessRecipeEUt);

        OP_Process_Normal();
        boolean noRecipe = mOutputItems == null || mOutputItems.length < 1;
        updateSlots();
        if (noRecipe) return CheckRecipeResultRegistry.NO_RECIPE;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = OreProcessRecipeDuration;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(EUtCanUse);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe);
            }

        }.setMaxParallel(Integer.MAX_VALUE);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.OreProcessingRecipes;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            this.ownerName = aBaseMetaTileEntity.getOwnerName();
            this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) return false;
        // check Lubricant input
        runTicks++;
        if (this.runTicks >= ticksOfPerFluidConsuming) {
            runTicks = 0;
            startRecipeProcessing();
            if (!consumeFluids()) {
                endRecipeProcessing();
                criticalStopMachine();
                return false;
            }
            endRecipeProcessing();
        }
        return true;
    }

    private boolean consumeFluids() {
        ArrayList<FluidStack> storedFluids = getStoredFluids();
        if (storedFluids == null || storedFluids.isEmpty()) return false;

        int amount = LubricantCost;
        for (FluidStack fluid : storedFluids) {
            if (fluid.getFluid() == Materials.Lubricant.mFluid) {
                if (fluid.amount >= amount) {
                    fluid.amount -= amount;
                    // succeed to consume
                    return true;
                } else {
                    amount -= fluid.amount;
                    fluid.amount = 0;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.mExoticEnergyHatches.clear();
        this.mEnergyHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        isWirelessMode = this.mEnergyHatches.isEmpty() && this.mExoticEnergyHatches.isEmpty();
        if (isWirelessMode) {
            EUtCanUse = 0;
        } else if (this.mExoticEnergyHatches.isEmpty() && this.mEnergyHatches.size() == 1) {
            // 1/16 Power losing region with single normal energy hatch
            EUtCanUse = this.mEnergyHatches.get(0)
                .maxEUInput() * 15 / 16;
        } else {
            // 1/32 Power losing region with multi energy hatch
            EUtCanUse = getMaxInputEu() * 31 / 32;
        }
        return true;
    }

    // endregion

    // region Structure
    // spotless:off
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(
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
    private static final String STRUCTURE_PIECE_MAIN = "mainOreProcessingFactory";
    private final int horizontalOffSet = 30;
    private final int verticalOffSet = 11;
    private final int depthOffSet = 0;
    @Override
    public IStructureDefinition<TST_OreProcessingFactory> getStructureDefinition() {
        return IStructureDefinition
                   .<TST_OreProcessingFactory>builder()
                   .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                   .addElement('A', GTStructureUtility.chainAllGlasses())
                   .addElement('B', ofBlock(GregTechAPI.sBlockCasings2,4))
                   .addElement('C', ofBlock(GregTechAPI.sBlockCasings2,6))
                   .addElement('D', ofBlock(GregTechAPI.sBlockCasings2,15))
                   .addElement('E', ofBlock(GregTechAPI.sBlockCasings4,0))
                   .addElement('F', ofBlock(GregTechAPI.sBlockCasings4,1))
                   .addElement('G', ofBlock(GregTechAPI.sBlockCasings8,7))
                   .addElement('H', ofBlock(sBlockCasingsTT,0))
                   .addElement('I', ofBlock(sBlockCasingsTT,7))
                   .addElement('J',
                               HatchElementBuilder
                                   .<TST_OreProcessingFactory>builder()
                                   .atLeast(InputHatch)
                                   .adder(TST_OreProcessingFactory::addFluidInputToMachineList)
                                   .dot(1)
                                   .casingIndex(48)
                                   .buildAndChain(GregTechAPI.sBlockCasings4,0))
                   .addElement('K',
                               HatchElementBuilder
                                   .<TST_OreProcessingFactory>builder()
                                   .atLeast(Energy.or(ExoticEnergy))
                                   .adder(TST_OreProcessingFactory::addEnergyHatchOrExoticEnergyHatchToMachineList)
                                   .dot(2)
                                   .casingIndex(1024)
                                   .buildAndChain(sBlockCasingsTT,0))
                   .addElement('L',
                               HatchElementBuilder
                                   .<TST_OreProcessingFactory>builder()
                                   .atLeast(InputBus, OutputBus)
                                   .adder(TST_OreProcessingFactory::addInputBusOrOutputBusToMachineList)
                                   .dot(3)
                                   .casingIndex(48)
                                   .buildAndChain(GregTechAPI.sBlockCasings4,0))
                   .addElement('M', ofFrame(Materials.TungstenSteel))
                   .build();
    }
    /*
    Blocks:
A -> ofBlock...(blockAlloyGlass, 0, ...);
B -> ofBlock...(gt.blockcasings2, 4, ...);
C -> ofBlock...(gt.blockcasings2, 6, ...);
D -> ofBlock...(gt.blockcasings2, 15, ...);
E -> ofBlock...(gt.blockcasings4, 0, ...);
F -> ofBlock...(gt.blockcasings4, 1, ...);
G -> ofBlock...(gt.blockcasings8, 7, ...);
H -> ofBlock...(gt.blockcasingsTT, 0, ...);
I -> ofBlock...(gt.blockcasingsTT, 7, ...);
J -> ofBlock...(gt.blockcasings4, 0, ...); // input hatches
K -> ofBlock...(gt.blockcasingsTT, 0, ...); // energy hatches
L -> ofBlock...(gt.blockcasings8, 7, ...); // input output buses
M -> ofFrame...(Materials.TungstenSteel, 0, ...);

     */
    private final String[][] shapeMain = new String[][]{
        {"                                ","                             LLL","                             LLL","                             LLL","                             LLL","                             LLL","                             LLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL","LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL","                                ","                                ","                                ","                                "},
        {"                                ","                                ","                             MDM","                              D ","                              D ","                              D ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","FHHFFHHFFHHFFHHFFHHFFHHFFHHFFHHF","FDDFFDDFFDDFFDDFFDDFFDDFFDDFFDDF","FDDFFDDFFDDFFDDFFDDFFDDFFDDFFDDF","FHHFFHHFFHHFFHHFFHHFFHHFFHHFFHHF","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","                                ","                                ","                                "},
        {"                                ","                                ","                             MDM","                                ","                                ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GCC  CC  CC  CC  CC  CC  CC  CCG","FCC  CC  CC  CC  CC  CC  CC  CCF","A                              A","A                              A","FCC  CC  CC  CC  CC  CC  CC  CCF","GCC  CC  CC  CC  CC  CC  CC  CCG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","                                ","                                "},
        {"                                ","                                ","                             MDM","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GCC  CC  CC  CC  CC  CC  CC  CCG","FCC  CC  CC  CC  CC  CC  CC  CCF","A                              A","A                              A","FCC  CC  CC  CC  CC  CC  CC  CCF","GCC  CC  CC  CC  CC  CC  CC  CCG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"},
        {"                                ","                                ","                             MDM"," HH  HH  HH  HH  HH  HH  HH  HH ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GDD  DD  DD  DD  DD  DD  DD  DDG","GBB  BB  BB  BB  BB  BB  BB  BBG","FBB  BB  BB  BB  BB  BB  BB  BBF","A                              A","A                              A","FBB  BB  BB  BB  BB  BB  BB  BBF","GBB  BB  BB  BB  BB  BB  BB  BBG","GDD  DD  DD  DD  DD  DD  DD  DDG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," HH  HH  HH  HH  HH  HH  HH  HH "},
        {"                                ","                                ","                             MDM"," HH  HH  HH  HH  HH  HH  HH  HH ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","EDD  DD  DD  DD  DD  DD  DD  DDE","GBB  BB  BB  BB  BB  BB  BB  BBG","FBB  BB  BB  BB  BB  BB  BB  BBF","A                              A","A                              A","FBB  BB  BB  BB  BB  BB  BB  BBF","GBB  BB  BB  BB  BB  BB  BB  BBG","EDD  DD  DD  DD  DD  DD  DD  DDE","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," HH  HH  HH  HH  HH  HH  HH  HH "},
        {"                                ","                                ","                             MDM"," HH  HH  HH  HH  HH  HH  HH  HH ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","EDD  DD  DD  DD  DD  DD  DD  DDE","GBB  BB  BB  BB  BB  BB  BB  BBG","FBB  BB  BB  BB  BB  BB  BB  BBF","A                              A","A                              A","FBB  BB  BB  BB  BB  BB  BB  BBF","GBB  BB  BB  BB  BB  BB  BB  BBG","EDD  DD  DD  DD  DD  DD  DD  DDE","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," HH  HH  HH  HH  HH  HH  HH  HH "},
        {"                             JJJ","                             EEE","                             MDM"," HH  HH  HH  HH  HH  HH  HH  HH ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","EDD  DD  DD  DD  DD  DD  DD  DDE","GBB  BB  BB  BB  BB  BB  BB  BBG","FBB  BB  BB  BB  BB  BB  BB  BBF","A                              A","A                              A","FBB  BB  BB  BB  BB  BB  BB  BBF","GBB  BB  BB  BB  BB  BB  BB  BBG","EDD  DD  DD  DD  DD  DD  DD  DDE","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," HH  HH  HH  HH  HH  HH  HH  HH "},
        {"                             MJM","                             MDM","                             MDM"," HH  HH  HH  HH  HH  HH  HH  HH ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","EDD  DD  DD  DD  DD  DD  DD  DDE","GBB  BB  BB  BB  BB  BB  BB  BBG","FBB  BB  BB  BB  BB  BB  BB  BBF","A                              A","A                              A","FBB  BB  BB  BB  BB  BB  BB  BBF","GBB  BB  BB  BB  BB  BB  BB  BBG","EDD  DD  DD  DD  DD  DD  DD  DDE","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," HH  HH  HH  HH  HH  HH  HH  HH "},
        {"                             JJJ","                             EEE","                             EEE"," HH  HH  HH  HH  HH  HH  HH  HH ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","EDD  DD  DD  DD  DD  DD  DD  DDE","GBB  BB  BB  BB  BB  BB  BB  BBG","FBB  BB  BB  BB  BB  BB  BB  BBF","A                              A","A                              A","FBB  BB  BB  BB  BB  BB  BB  BBF","GBB  BB  BB  BB  BB  BB  BB  BBG","EDD  DD  DD  DD  DD  DD  DD  DDE","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," HH  HH  HH  HH  HH  HH  HH  HH "},
        {"                             GGG","                             KKK","                             GGG"," HH  HH  HH  HH  HH  HH  HH  HH ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","EDD  DD  DD  DD  DD  DD  DD  DDE","GBB  BB  BB  BB  BB  BB  BB  BBG","FBB  BB  BB  BB  BB  BB  BB  BBF","A                              A","A                              A","FBB  BB  BB  BB  BB  BB  BB  BBF","GBB  BB  BB  BB  BB  BB  BB  BBG","EDD  DD  DD  DD  DD  DD  DD  DDE","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," HH  HH  HH  HH  HH  HH  HH  HH "},
        {"                             G~G","                             KIK","                             GHG"," HH  HH  HH  HH  HH  HH  HH  HH ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","EDD  DD  DD  DD  DD  DD  DD  DDE","GBB  BB  BB  BB  BB  BB  BB  BBG","FBB  BB  BB  BB  BB  BB  BB  BBF","A                              A","A                              A","FBB  BB  BB  BB  BB  BB  BB  BBF","GBB  BB  BB  BB  BB  BB  BB  BBG","EDD  DD  DD  DD  DD  DD  DD  DDE","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," HH  HH  HH  HH  HH  HH  HH  HH "},
        {"                             GGG","                             KKK","                             GGG"," GGGGGGGGGGGGGGGGGGGGGGGGGGGGGG ","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"," GGGGGGGGGGGGGGGGGGGGGGGGGGGGGG "}
    };

    // spotless:on
    // endregion

    // region Info
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(Tooltip_OreProcessingFactory_MachineType)
            .addInfo(Tooltip_OreProcessingFactory_Controller)
            .addInfo(Tooltip_OreProcessingFactory_01)
            .addInfo(Tooltip_OreProcessingFactory_02)
            .addInfo(Tooltip_OreProcessingFactory_03)
            .addInfo(Tooltip_OreProcessingFactory_04)
            .addInfo(Tooltip_OreProcessingFactory_05)
            .addInfo(Tooltips_JoinWirelessNetWithoutEnergyHatch)
            .addInfo(Tooltip_OreProcessingFactory_06)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 3)
            .addOutputBus(TextLocalization.textUseBlueprint, 3)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 2)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(183) };
    }
}
