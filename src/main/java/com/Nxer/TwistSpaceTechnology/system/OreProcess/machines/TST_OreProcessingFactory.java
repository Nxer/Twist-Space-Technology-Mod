package com.Nxer.TwistSpaceTechnology.system.OreProcess.machines;

import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.LubricantCost;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeDuration;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreRecipesInputs;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.moveUnprocessedItemsToOutputs;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.ticksOfPerFluidConsuming;
import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_02;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_03;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_04;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_05;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_06;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_Controller;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_OreProcessingFactory_MachineType;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltips_JoinWirelessNetWithoutEnergyHatch;
import static com.Nxer.TwistSpaceTechnology.util.Utils.itemStackArrayEqualFuzzy;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_OreProcessingFactory extends GTCM_MultiMachineBase<TST_OreProcessingFactory>
    implements IGlobalWirelessEnergy {

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
            currentTip.add(EnumChatFormatting.AQUA + texter("In Wireless mode", "Waila.TST_OreProcessingFactory.1"));
        }
        if (tag.getBoolean("isActive") && tag.getBoolean("isWirelessMode")) {
            currentTip.add(
                EnumChatFormatting.AQUA + texter("Current Using EU: ", "Waila.TST_OreProcessingFactory.2")
                    + EnumChatFormatting.GOLD
                    + tag.getLong("usingEU")
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

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        return isWirelessMode ? checkProcessing_wirelessMode() : checkProcessing_normalMode();
    }

    protected CheckRecipeResult checkProcessing_wirelessMode() {
        // If no inputs
        if (getStoredInputsWithoutDualInputHatch().isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (getStoredFluids().isEmpty()) {
            return SimpleCheckRecipeResult.ofFailure("No_Lubricant_Inputting");
        }

        setupProcessingLogic(processingLogic);
        processingLogic.setAvailableVoltage(Long.MAX_VALUE);

        Set<ItemStack> outputItems = new HashSet<>();
        long useEUt = 0;

        CheckRecipeResult result = doCheckRecipe();
        ItemStack[] sanityCheckFlag = new ItemStack[] {};
        while (result.wasSuccessful()) {
            if (itemStackArrayEqualFuzzy(sanityCheckFlag, processingLogic.getOutputItems())) {
                TwistSpaceTechnology.LOG
                    .info("Ore Processing Factory TST may cause an error, broke the CheckRecipeResult cycle.");
                break;
            }
            sanityCheckFlag = processingLogic.getOutputItems();
            outputItems.addAll(Arrays.asList(sanityCheckFlag));
            useEUt += processingLogic.getCalculatedEut();
            result = doCheckRecipe();
        }
        if (outputItems.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        if (!addEUToGlobalEnergyMap(ownerUUID, -useEUt * OreProcessRecipeDuration)) {
            return CheckRecipeResultRegistry.insufficientPower(useEUt * OreProcessRecipeDuration);
        }

        // inputs with no recipe will be moved into output
        if (moveUnprocessedItemsToOutputs) {
            for (ItemStack items : getStoredInputsWithoutDualInputHatch()) {
                if (OreRecipesInputs.contains(items.getUnlocalizedName() + items.getItemDamage())) continue;
                outputItems.add(items.copy());
                items.stackSize = 0;
            }
        }

        updateSlots();

        usingEU = useEUt * OreProcessRecipeDuration;
        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;

        mMaxProgresstime = OreProcessRecipeDuration;

        mOutputItems = outputItems.toArray(new ItemStack[] {});

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    protected CheckRecipeResult checkProcessing_normalMode() {
        // If no inputs
        if (getStoredInputsWithoutDualInputHatch().isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (getStoredFluids().isEmpty()) {
            return SimpleCheckRecipeResult.ofFailure("No_Lubricant_Inputting");
        }

        setupProcessingLogic(processingLogic);

        Set<ItemStack> outputItems = new HashSet<>();

        long voltageCanUse = EUtCanUse;
        processingLogic.setAvailableVoltage(voltageCanUse);
        CheckRecipeResult result = doCheckRecipe();
        while (result.wasSuccessful()) {
            outputItems.addAll(Arrays.asList(processingLogic.getOutputItems()));
            voltageCanUse -= processingLogic.getCalculatedEut();
            processingLogic.setAvailableVoltage(voltageCanUse);
            result = doCheckRecipe();
        }
        if (outputItems.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        // inputs with no recipe will be moved into output
        if (moveUnprocessedItemsToOutputs) {
            for (ItemStack items : getStoredInputsWithoutDualInputHatch()) {
                if (OreRecipesInputs.contains(items.getUnlocalizedName() + items.getItemDamage())) continue;
                outputItems.add(items.copy());
                items.stackSize = 0;
            }
        }

        // inputs are consumed at this point
        updateSlots();

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;

        mMaxProgresstime = OreProcessRecipeDuration;

        // set EUt
        lEUt = voltageCanUse - EUtCanUse;

        mOutputItems = outputItems.toArray(new ItemStack[] {});

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
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe);
            }
        }.setMaxParallel(Integer.MAX_VALUE);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTCMRecipe.instance.OreProcessingRecipes;
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
            if (!consumeFluids()) {
                criticalStopMachine();
                return false;
            }
        }
        return true;
    }

    private boolean consumeFluids() {
        if (getStoredFluids() == null || getStoredFluids().isEmpty()) return false;

        int amount = LubricantCost;
        for (FluidStack fluid : getStoredFluids()) {
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
    protected int getMaxParallelRecipes() {
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
        EUtCanUse = getMaxInputEu();
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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
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
                   .addElement('A', Glasses.chainAllGlasses())
                   .addElement('B', ofBlock(GregTech_API.sBlockCasings2,4))
                   .addElement('C', ofBlock(GregTech_API.sBlockCasings2,6))
                   .addElement('D', ofBlock(GregTech_API.sBlockCasings2,15))
                   .addElement('E', ofBlock(GregTech_API.sBlockCasings4,0))
                   .addElement('F', ofBlock(GregTech_API.sBlockCasings4,1))
                   .addElement('G', ofBlock(GregTech_API.sBlockCasings8,7))
                   .addElement('H', ofBlock(sBlockCasingsTT,0))
                   .addElement('I', ofBlock(sBlockCasingsTT,7))
                   .addElement('J',
                               GT_HatchElementBuilder
                                   .<TST_OreProcessingFactory>builder()
                                   .atLeast(InputHatch)
                                   .adder(TST_OreProcessingFactory::addInputHatchOrOutputHatchToMachineList)
                                   .dot(1)
                                   .casingIndex(48)
                                   .buildAndChain(GregTech_API.sBlockCasings4,0))
                   .addElement('K',
                               GT_HatchElementBuilder
                                   .<TST_OreProcessingFactory>builder()
                                   .atLeast(Energy.or(ExoticEnergy))
                                   .adder(TST_OreProcessingFactory::addEnergyHatchOrExoticEnergyHatchToMachineList)
                                   .dot(2)
                                   .casingIndex(1024)
                                   .buildAndChain(sBlockCasingsTT,0))
                   .addElement('L',
                               GT_HatchElementBuilder
                                   .<TST_OreProcessingFactory>builder()
                                   .atLeast(InputBus, OutputBus)
                                   .adder(TST_OreProcessingFactory::addInputBusOrOutputBusToMachineList)
                                   .dot(3)
                                   .casingIndex(48)
                                   .buildAndChain(GregTech_API.sBlockCasings4,0))
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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
