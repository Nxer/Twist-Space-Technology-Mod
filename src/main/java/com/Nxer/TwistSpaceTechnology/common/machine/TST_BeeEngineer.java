package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.AUTHOR;
import static com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit.Role.MAINTAINER;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
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
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.text.TextLocalization;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

@SkipGenerateDescription
public class TST_BeeEngineer extends GTCM_MultiMachineBase<TST_BeeEngineer> {

    // region Class Constructor
    public TST_BeeEngineer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(AUTHOR, ID.RH_NU, MAINTAINER, ID.NXER);
    }

    public TST_BeeEngineer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BeeEngineer(mName);
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "STRUCTURE_PIECE_MAIN_BE";
    private final int hOffset = 1, vOffset = 1, dOffset = 0;
    private static final int CASING_INDEX = 10;
    private static IStructureDefinition<TST_BeeEngineer> STRUCTURE_DEF = null;

    // spotless:off
    protected final String[][] STRUCTURE = new String[][]{
        {"CCC", "CCC", "CCC"},
        {"C~C", "C C", "CCC"},
        {"CCC", "CCC", "CCC"}
    };
    // spotless:on

    @Override
    public IStructureDefinition<TST_BeeEngineer> getStructureDefinition() {
        if (STRUCTURE_DEF == null) {
            STRUCTURE_DEF = StructureDefinition.<TST_BeeEngineer>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(STRUCTURE))
                .addElement(
                    'C',
                    HatchElementBuilder.<TST_BeeEngineer>builder()
                        .atLeast(InputBus, InputHatch, OutputBus)
                        .adder(TST_BeeEngineer::addToMachineList)
                        .hint(1)
                        .casingIndex(CASING_INDEX)
                        .buildAndChain(GregTechAPI.sBlockCasings1, 10))
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
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hOffset,
            vOffset,
            dOffset,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        repairMachine();
        checkPiece(STRUCTURE_PIECE_MAIN, hOffset, vOffset, dOffset, errors);
    }
    // endregion

    // region Processing Logic
    private static FluidStack HONEY;
    private static FluidStack UUM;

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
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
                if (fluidStack == null || fluidStack.amount < 1) continue;
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

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (HONEY == null) {
            HONEY = Materials.Honey.getFluid(1);
        }
        if (UUM == null) {
            UUM = Materials.UUMatter.getFluid(1);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MEGA_INDUSTRIAL_APIARY_LOOP;
    }

    // endregion

    // region Textures

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

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_BeeEngineer_Type
        // # Bee Engineer
        // #zh_CN 蜜蜂操纵者
        tt.addMachineType(TextEnums.tr("Tooltip_BeeEngineer_Type"))
            // #tr Tooltip_BeeEngineer_Controller
            // # Controller of the Bee Engineer
            // #zh_CN 蜜蜂操纵者的控制器
            .addInfo(TextEnums.tr("Tooltip_BeeEngineer_Controller"))
            // #tr Tooltip_BeeEngineer_01
            // # Still in test.
            // #zh_CN 其实还在测试中.
            .addInfo(TextEnums.tr("Tooltip_BeeEngineer_01"))
            // #tr Tooltip_BeeEngineer_02
            // # Transforming drones into princesses.
            // #zh_CN 将雄蜂转化为公主蜂.
            .addInfo(TextEnums.tr("Tooltip_BeeEngineer_02"))
            // #tr Tooltip_BeeEngineer_03
            // # Who knows how many drones became stepping stones for the Last Queen?
            // #zh_CN 谁知道有多少雄蜂成为了蜂后的垫脚石？
            .addInfo(TextEnums.tr("Tooltip_BeeEngineer_03"))
            // #tr Tooltip_BeeEngineer_04
            // # Cost {\GOLD}128kL{\GRAY} Honey to transform a drone into princess, but with {\RED}40%{\GRAY} failing chance.
            // #zh_CN 消耗{\GOLD}128k{\GRAY}L蜂蜜, 将雄蜂转化为公主蜂，但失败几率为{\RED}40%{\GRAY}.
            .addInfo(TextEnums.tr("Tooltip_BeeEngineer_04"))
            // #tr Tooltip_BeeEngineer_05
            // # Will try to consume {\GOLD}32kL{\GRAY} UUM (if exists) to increase success rate to {\RED}80%{\GRAY}.
            // #zh_CN 将尝试消耗{\GOLD}32kL{\GRAY}的UU物质(如果存在)以将成功率提高到{\RED}80%{\GRAY}.
            .addInfo(TextEnums.tr("Tooltip_BeeEngineer_05"))
            // #tr Tooltip_BeeEngineer_06
            // # In case of failure, all consumed ingredients will not be returned.
            // #zh_CN 在失败的情况下, 所有投入的原料都不会返还.
            .addInfo(TextEnums.tr("Tooltip_BeeEngineer_06"))
            // #tr Tooltip_BeeEngineer_07
            // # Don't put too many drones in at once, that will result in a long run time!
            // #zh_CN 不要一次性放入太多雄蜂, 那会导致运行时间过长!
            // .addInfo(TextEnums.tr("Tooltip_BeeEngineer_07"))
            .addInputBus(TextLocalization.textUseBlueprint)
            .addInputHatch(TextLocalization.textUseBlueprint)
            .addOutputBus(TextLocalization.textUseBlueprint)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    // endregion

}
