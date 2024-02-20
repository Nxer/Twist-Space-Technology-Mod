package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class TST_BeeEngineer extends GTCM_MultiMachineBase<TST_BeeEngineer> {

    public TST_BeeEngineer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_BeeEngineer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BeeEngineer(mName);
    }

    // region Process
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

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    private ArrayList<ItemStack> outputStacks = new ArrayList<>();
    private int processSize = 0;
    private final double pChance = ValueEnum.BE_pChance;
    private final double pChanceEnhanced = ValueEnum.BE_pChanceEnhanced;
    private final int pHoneyCost = ValueEnum.BE_pHoneyCost;
    private final int pUUMCost = ValueEnum.BE_pUUMCost;
    private final int pEachProcessTime = ValueEnum.BE_pEachProcessTime;

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        mEfficiencyIncrease = 10000;
        processSize = 0;
        outputStacks = new ArrayList<>();
        ArrayList<ItemStack> inputStacks = getStoredInputs();
        if (inputStacks.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        for (ItemStack stack : inputStacks) {
            if (beeRoot.getType(stack) == EnumBeeType.DRONE) {
                while (stack.stackSize > 0) {
                    if (!consumeHoney()) {
                        break;
                    }
                    if (calculateSuccess(consumeUUM())) {
                        IBee bee = beeRoot.getMember(stack);
                        ItemStack princess = beeRoot.getMemberStack(bee.copy(), EnumBeeType.PRINCESS.ordinal());
                        outputStacks.add(princess);
                    }
                    stack.stackSize--;
                    processSize++;
                }
            }
        }
        calculateTime(processSize);
        updateSlots();
        if (!outputStacks.isEmpty()) {
            mOutputItems = outputStacks.toArray(new ItemStack[0]);
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    private boolean consumeHoney() {
        if (getStoredFluids() == null || getStoredFluids().isEmpty()) return false;
        int cost = pHoneyCost;
        for (FluidStack fluid : getStoredFluids()) {
            if (fluid.getFluid() == Materials.Honey.mFluid) {
                if (fluid.amount >= cost) {
                    fluid.amount -= cost;
                    return true;
                } else {
                    cost -= fluid.amount;
                    fluid.amount = 0;
                }
            }
        }
        return true;
    }

    private boolean consumeUUM() {
        if (getStoredFluids() == null || getStoredFluids().isEmpty()) return false;
        int cost = pUUMCost;
        for (FluidStack fluid : getStoredFluids()) {
            if (fluid.getFluid() == Materials.UUMatter.mFluid) {
                if (fluid.amount >= cost) {
                    fluid.amount -= cost;
                    return true;
                } else {
                    cost -= fluid.amount;
                    fluid.amount = 0;
                }
            }
        }
        return true;
    }

    private boolean calculateSuccess(boolean enhance) {
        double r = Math.random();
        return r <= (enhance ? pChance : pChanceEnhanced);
    }

    private void calculateTime(int size) {
        if (size <= 0) {
            mMaxProgresstime = 20;
        } else {
            mMaxProgresstime = size * pEachProcessTime;
        }
    }

    // endregion

    // region Structure
    private final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN_BE";
    private final int hOffset = 1, vOffset = 1, dOffset = 0;
    private final int sCasingIndex = 10;
    private IStructureDefinition<TST_BeeEngineer> structureDef = null;

    // spotless:off
    protected final String[][] STRUCTURE = new String[][]{
        {"CCC","CCC","CCC"},
        {"C~C","C C","CCC"},
        {"CCC","CCC","CCC"}
    };
    // spotless:on

    @Override
    public IStructureDefinition<TST_BeeEngineer> getStructureDefinition() {
        if (structureDef == null) {
            structureDef = StructureDefinition.<TST_BeeEngineer>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(STRUCTURE))
                .addElement(
                    'C',
                    GT_HatchElementBuilder.<TST_BeeEngineer>builder()
                        .atLeast(InputBus, InputHatch, OutputBus)
                        .adder(TST_BeeEngineer::addToMachineList)
                        .casingIndex(sCasingIndex)
                        .dot(1)
                        .buildAndChain(GregTech_API.sBlockCasings1, 10))
                .build();
        }
        return structureDef;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, hOffset, vOffset, dOffset);
    }

    // endregion
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_BeeEngineer_Type)
            .addInfo(TextLocalization.Tooltip_BeeEngineer_Controller)
            .addInfo(TextLocalization.Tooltip_BeeEngineer_01)
            .addInfo(TextLocalization.Tooltip_BeeEngineer_02)
            .addInfo(TextLocalization.Tooltip_BeeEngineer_03)
            .addInfo(TextLocalization.Tooltip_BeeEngineer_04)
            .addInfo(TextLocalization.Tooltip_BeeEngineer_05)
            .addInfo(TextLocalization.Tooltip_BeeEngineer_06)
            .addInfo(TextLocalization.Tooltip_BeeEngineer_07)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addInputBus(TextLocalization.BLUE_PRINT_INFO)
            .addInputHatch(TextLocalization.BLUE_PRINT_INFO)
            .addOutputBus(TextLocalization.BLUE_PRINT_INFO)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        return checkPiece(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(sCasingIndex),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(sCasingIndex), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(sCasingIndex) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagCompound outputList = new NBTTagCompound();
        outputList.setInteger("outputBELength", outputStacks.size());
        int index = 0;
        for (ItemStack itemStack : outputStacks) {
            outputList.setTag("outputBEItem" + index, itemStack.writeToNBT(new NBTTagCompound()));
            index++;
        }
        aNBT.setTag("outputBE", outputList);
        aNBT.setInteger("processSize", processSize);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        NBTTagCompound tempTag = aNBT.getCompoundTag("outputBE");
        for (int index = 0; index < tempTag.getInteger("outputBELength"); index++) {
            outputStacks.add(ItemStack.loadItemStackFromNBT(tempTag.getCompoundTag("outputBEItem" + index)));
        }
        processSize = aNBT.getInteger("processSize");
        super.loadNBTData(aNBT);
    }
}
