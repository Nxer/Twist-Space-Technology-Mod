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
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
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
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 1;
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

    private static final FluidStack HONEY = Materials.Honey.getFluid(1);
    private static final FluidStack UUM = Materials.UUMatter.getFluid(1);

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        List<ItemStack> inputStacks = getStoredInputs();
        List<FluidStack> inputFluid = getStoredFluids();
        // check no input
        if (inputStacks.isEmpty() || inputFluid.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        // check honey liquid and uu matter liquid input, store fluids info
        List<FluidStack> honeyStacks = new ArrayList<>();
        boolean honeyEnough = false;
        List<FluidStack> uuStacks = new ArrayList<>();
        boolean uuProvided = false;
        {
            long honeyAmount = 0;
            long uuAmount = 0;
            for (FluidStack fluidStack : inputFluid) {
                if (fluidStack == null || fluidStack.amount > 1) continue;
                if (!honeyEnough && fluidStack.isFluidEqual(HONEY)) {
                    honeyStacks.add(fluidStack);
                    honeyAmount += fluidStack.amount;
                    if (honeyAmount >= ValueEnum.BE_pHoneyCost) honeyEnough = true;
                }
                if (!uuProvided && fluidStack.isFluidEqual(UUM)) {
                    uuStacks.add(fluidStack);
                    uuAmount += fluidStack.amount;
                    if (uuAmount >= ValueEnum.BE_pUUMCost) uuProvided = true;
                }
                if (honeyEnough && uuProvided) break;
            }

            // check no honey input
            if (!honeyEnough) {
                if (honeyAmount == 0) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                } else {
                    return CheckRecipeResultRegistry
                        .insufficientStartupPower((int) (ValueEnum.BE_pHoneyCost - honeyAmount));
                }
            }

        }

        // check bee input and processing
        for (ItemStack itemStack : inputStacks) {
            if (itemStack == null || itemStack.stackSize < 1 || beeRoot.getType(itemStack) != EnumBeeType.DRONE)
                continue;

            // process

            // consume honey
            {
                int needHoney = ValueEnum.BE_pHoneyCost;
                for (FluidStack honeyStack : honeyStacks) {
                    if (honeyStack.amount >= needHoney) {
                        honeyStack.amount -= needHoney;
                        needHoney = 0;
                        break;
                    } else if (honeyStack.amount > 0) {
                        needHoney -= honeyStack.amount;
                        honeyStack.amount = 0;
                    }
                }
                // honey should be enough in checking before here
                if (needHoney > 0) return CheckRecipeResultRegistry.INTERNAL_ERROR;
            }

            double successRate = ValueEnum.BE_pChance;
            // consume UU
            if (uuProvided) {
                int needUU = ValueEnum.BE_pUUMCost;
                for (FluidStack uuStack : uuStacks) {
                    if (uuStack.amount >= needUU) {
                        uuStack.amount -= needUU;
                        needUU = 0;
                        break;
                    } else if (uuStack.amount > 0) {
                        needUU -= uuStack.amount;
                        uuStack.amount = 0;
                    }
                }

                // UU should be enough here but still make a check
                if (needUU == 0) {
                    successRate = ValueEnum.BE_pChanceEnhanced;
                }
            }

            // consume the drone bee
            itemStack.stackSize -= 1;

            if (Math.random() <= successRate) {
                IBee bee = beeRoot.getMember(itemStack);
                mOutputItems = new ItemStack[] { beeRoot.getMemberStack(bee.copy(), EnumBeeType.PRINCESS.ordinal()) };
            }

            mEfficiencyIncrease = 10000;
            mMaxProgresstime = ValueEnum.BE_pEachProcessTime;

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        // check no bees
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN_BE";
    private final int hOffset = 1, vOffset = 1, dOffset = 0;
    private static final int CASING_INDEX = 10;
    private static IStructureDefinition<TST_BeeEngineer> STRUCTURE_DEF = null;

    // spotless:off
    protected final String[][] STRUCTURE = new String[][]{
        {"CCC","CCC","CCC"},
        {"C~C","C C","CCC"},
        {"CCC","CCC","CCC"}
    };
    // spotless:on

    @Override
    public IStructureDefinition<TST_BeeEngineer> getStructureDefinition() {
        if (STRUCTURE_DEF == null) {
            STRUCTURE_DEF = StructureDefinition.<TST_BeeEngineer>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(STRUCTURE))
                .addElement(
                    'C',
                    GT_HatchElementBuilder.<TST_BeeEngineer>builder()
                        .atLeast(InputBus, InputHatch, OutputBus)
                        .adder(TST_BeeEngineer::addToMachineList)
                        .dot(1)
                        .casingIndex(CASING_INDEX)
                        .buildAndChain(GregTech_API.sBlockCasings1, 10))
                .build();
        }
        return STRUCTURE_DEF;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, hOffset, vOffset, dOffset);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hOffset,
            vOffset,
            dOffset,
            realBudget,
            env,
            false,
            true);
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
            // .addInfo(TextLocalization.Tooltip_BeeEngineer_07)
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
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

}
