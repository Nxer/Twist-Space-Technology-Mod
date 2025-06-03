package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures.OVERLAY_FRONT_PROCESSING_ARRAY;
import static com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static com.Nxer.TwistSpaceTechnology.client.texture.TstMachineTextures.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.TstProcessingLogic;
import com.Nxer.TwistSpaceTechnology.system.ProcessingArrayBackend.PAHelper;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Recode the Processing Array.
 */
public class TST_ProcessingArray extends GTCM_MultiMachineBase<TST_ProcessingArray> {

    // region Class Constructors
    public TST_ProcessingArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_ProcessingArray(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ProcessingArray(mName);
    }

    // endregion

    // region Processing Logic
    public TST_ItemID internalMachine;
    public RecipeMap<?> recipeMap;
    public long voltage;
    public SoundResource sound = SoundResource.NONE;

    public void checkInternalMachine() {
        ItemStack controllerStack = getControllerSlot();

        if (controllerStack == null || controllerStack.stackSize < 1) {
            // set to no internal machine
            resetMachineInfo();
            return;
        }

        if (internalMachine == null || !internalMachine.equalItemStack(controllerStack)) {
            // check new machine input
            MTEBasicMachineWithRecipe mte = PAHelper.getMTE(controllerStack);
            if (mte == null) {
                // the item in controller is not a single block machine
                // set to no internal machine
                resetMachineInfo();
            } else {
                // set to new machine
                internalMachine = TST_ItemID.createNoNBT(controllerStack);
                recipeMap = PAHelper.getRecipeMapFromMTE(mte);
                maxParallel = controllerStack.stackSize;
                voltage = PAHelper.getVoltageFromMTE(mte);
                sound = PAHelper.getSoundFromMTE(mte);

                for (MTEHatchInputBus tInputBus : mInputBusses) {
                    tInputBus.mRecipeMap = recipeMap;
                }
                for (MTEHatchInput tInputHatch : mInputHatches) {
                    tInputHatch.mRecipeMap = recipeMap;
                }
            }
        }

        // check if machine amount has been changed
        maxParallel = controllerStack.stackSize;
    }

    public void resetMachineInfo() {
        if (internalMachine == null) return;
        internalMachine = null;
        maxParallel = 0;
        voltage = 0;
        recipeMap = null;
        sound = SoundResource.NONE;

        for (MTEHatchInputBus tInputBus : mInputBusses) {
            tInputBus.mRecipeMap = null;
        }
        for (MTEHatchInput tInputHatch : mInputHatches) {
            tInputHatch.mRecipeMap = null;
        }

    }

    @Override
    protected void sendStartMultiBlockSoundLoop() {
        if (sound != null && sound != SoundResource.NONE) {
            sendLoopStart((byte) sound.id);
        }
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        SoundResource sound = SoundResource.get(aIndex < 0 ? aIndex + 256 : 0);
        if (sound != null) {
            GTUtility.doSoundAtClient(sound, getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
        }
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        checkInternalMachine();
        if (internalMachine == null) {
            return SimpleCheckRecipeResult.ofFailure("no_machine");
        }

        return super.checkProcessing();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new TstProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0) > 0) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                if (recipe.mEUt > availableVoltage) return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(voltage);
        logic.setAvailableAmperage(getMaxParallelRecipes());
        logic.setAmperageOC(false);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return recipeMap;
    }

    @Override
    protected boolean canUseControllerSlotForRecipe() {
        return false;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Collections.emptyList();
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (recipeMap != null) {
            tag.setString("recipeMap", recipeMap.unlocalizedName);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("recipeMap")) {
            // #tr TST_ProcessingArray.Waila.Machine
            // # Machine
            // #zh_CN 机器类型
            currentTip.add(
                TextEnums.tr("TST_ProcessingArray.Waila.Machine") + " : "
                    + EnumChatFormatting.YELLOW
                    + TextEnums.tr(tag.getString("recipeMap")));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }
        checkInternalMachine();
        return true;
    }
    // endregion

    // region Structure

    protected static final int horizontalOffSet = 1;
    protected static final int verticalOffSet = 1;
    protected static final int depthOffSet = 0;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static IStructureDefinition<TST_ProcessingArray> STRUCTURE_DEFINITION = null;

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
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

    @Override
    public IStructureDefinition<TST_ProcessingArray> getStructureDefinition() {
        if (null == STRUCTURE_DEFINITION) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_ProcessingArray>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "hhh", "hhh", "hhh" }, { "h~h", "h h", "hhh" }, { "hhh", "hhh", "hhh" } }))
                .addElement(
                    'h',
                    HatchElementBuilder.<TST_ProcessingArray>builder()
                        .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_ProcessingArray::addToMachineList)
                        .dot(1)
                        .casingIndex(48)
                        .buildAndChain(GregTechAPI.sBlockCasings4, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][48] };
    }

    // endregion

    // region Generals

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tooltips = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr TST_ProcessingArray.tooltips.MachineType
        // # Processing Array
        // #zh_CN 处理阵列
        tooltips.addMachineType(TextEnums.tr("TST_ProcessingArray.tooltips.MachineType"))
            .addInfo(TextEnums.Machine_of_TwistSpaceTechnology.getText())
            // #tr TST_ProcessingArray.tooltips.01
            // # Runs supplied machines as if placed in the world
            // #zh_CN 让其中的机器如同放置在世界中一样运行
            .addInfo(TextEnums.tr("TST_ProcessingArray.tooltips.01"))
            // #tr TST_ProcessingArray.tooltips.02
            // # Place up to 64 singleblock GT machines into the controller
            // #zh_CN 在控制器GUI中放置至多64个GT单方块机器
            .addInfo(TextEnums.tr("TST_ProcessingArray.tooltips.02"))
            // #tr TST_ProcessingArray.tooltips.03
            // # Note that you still need to supply power to them all
            // #zh_CN 请保证电力充足
            .addInfo(TextEnums.tr("TST_ProcessingArray.tooltips.03"))
            // #tr TST_ProcessingArray.tooltips.04
            // # Do general overclock
            // #zh_CN 执行有损超频
            .addInfo(TextEnums.tr("TST_ProcessingArray.tooltips.04"))
            // #tr TST_ProcessingArray.tooltips.05
            // # Centrifuge, Electrolyzer, Mixer do their multiblock machine recipe
            // #zh_CN 离心机,电解机,搅拌机执行其对应多方块机器配方
            .addInfo(TextEnums.tr("TST_ProcessingArray.tooltips.05"))
            .beginStructureBlock(3, 3, 3, true)
            .addController(TextLocalization.textFrontCenter)
            .addEnergyHatch(TextLocalization.textAnyCasing, 1)
            .addInputBus(TextLocalization.textAnyCasing, 1)
            .addInputHatch(TextLocalization.textAnyCasing, 1)
            .addOutputBus(TextLocalization.textAnyCasing, 1)
            .addOutputHatch(TextLocalization.textAnyCasing, 1)
            .toolTipFinisher();
        // spotless:on
        return tooltips;
    }

}
