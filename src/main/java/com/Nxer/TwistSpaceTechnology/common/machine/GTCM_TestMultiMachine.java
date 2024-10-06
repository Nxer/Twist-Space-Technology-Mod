package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

public class GTCM_TestMultiMachine extends GTCM_MultiMachineBase<GTCM_TestMultiMachine> {

    public GTCM_TestMultiMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTCM_TestMultiMachine(String aName) {
        super(aName);
    }
    // region Processing Logic

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    protected float getSpeedBonus() {
        return 1.0F / 16;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    // endregion

    protected int mode = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final String[][] shape = new String[][] { { "AAA", "AAA", "AAA" }, { "A~A", "AAA", "AAA" },
        { "AAA", "AAA", "AAA" } };
    private static final int horizontalOffSet = 1;
    private static final int verticalOffSet = 1;
    private static final int depthOffSet = 0;

    @Override
    public IStructureDefinition<GTCM_TestMultiMachine> getStructureDefinition() {
        return StructureDefinition.<GTCM_TestMultiMachine>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            .addElement(
                'A',
                HatchElementBuilder.<GTCM_TestMultiMachine>builder()
                    .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy))
                    .adder(GTCM_TestMultiMachine::addToMachineList)
                    .casingIndex(BasicBlocks.MetaBlockCasing01.getTextureIndex(0))
                    .dot(1)
                    .buildAndChain(BasicBlocks.MetaBlockCasing01, 0))
            .build();
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
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
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % 2;
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("IntensifyChemicalDistorter.mode." + this.mode));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // this.casingAmountActual = 0; // re-init counter
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mode = aNBT.getInteger("mode");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_TestMultiMachine(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    // Tooltips
    private static MultiblockTooltipBuilder tooltip;

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        if (tooltip == null) {
            tooltip = new MultiblockTooltipBuilder();
            tooltip.addMachineType("test")
                .addInfo("testing")
                .addSeparator()
                .addInfo(TextLocalization.StructureTooComplex)
                .addInfo(TextLocalization.BLUE_PRINT_INFO)
                .beginStructureBlock(3, 3, 3, false)
                .addInputHatch(TextLocalization.textUseBlueprint, 1)
                .addOutputHatch(TextLocalization.textUseBlueprint, 1)
                .addInputBus(TextLocalization.textUseBlueprint, 2)
                .addOutputBus(TextLocalization.textUseBlueprint, 2)
                .addEnergyHatch(TextLocalization.textUseBlueprint, 3)
                .toolTipFinisher(TextLocalization.ModName);

        }
        return tooltip;
    }

    // // read only unless you are making computation generator - read computer class
    // protected long data = 0; // data being available
    // private int module = 0;
    // public static final double destroyModuleBase_chance = 0.066d;
    // public static final double destroyModule_a = 5.0E-5; // = 0.00005
    // public static final double destroyModule_b = 3.0E-5; // = 0.00003
    // public static final double destroyModuleMaxCPS = 100000.0;
    //
    // private static Consumer<GTCM_TestMultiMachine> moduleDestroyer = (t) -> {
    // // 每小时执行一次此运算
    // t.module =
    // (int) ((double) t.module -
    // (double) t.module * 2.0 * destroyModuleBase_chance /
    // ( Math.exp( -destroyModule_a * (double) (t.module - 1))
    // + Math.exp(destroyModule_b * (double) Math.min(t.data, (long) destroyModuleMaxCPS)))
    // );
    // if (t.module < 0) {
    // t.module = 0;
    // }
    //
    // // 拆解一下
    //
    // // 每次损毁的模块数量部分为
    // double moduleDestroy = (double) t.module * 2.0 * destroyModuleBase_chance /
    // ( Math.exp( -destroyModule_a * (double) (t.module - 1))
    // + Math.exp(destroyModule_b * (double) Math.min(t.data, (long) destroyModuleMaxCPS)));
    //
    // // 其中
    // double 分母 = (double) t.module * 2 * destroyModuleBase_chance;
    // double 分子 = Math.exp( -destroyModule_a * (double) (t.module - 1))
    // + Math.exp( destroyModule_b * (double) Math.min(t.data, (long) destroyModuleMaxCPS));
    //
    // // 代入默认值
    // // destroyModuleBase_chance = 0.066d;
    // // destroyModule_a = 5.0E-5; ( = 0.00005 )
    // // destroyModule_b = 3.0E-5; ( = 0.00003 )
    // // destroyModuleMaxCPS = 100000.0;
    // 分母 = (double) t.module * 2 * 0.066d;
    // 分子 = Math.exp( -0.00005 * (double) (t.module - 1))
    // + Math.exp( 0.00003 * (double) Math.min(t.data, (long) 100000.0));
    //
    // // 可见算力上限为 100,000
    // // 汇总后
    // // y = 损耗量 ; m = 当前模块数量 ; x = 提供算力
    // // y = (m * 0.132) / {e^[-0.00005 * (m-1)] + e^(0.00003 * x)}
    //
    // // 取值上限情况
    // // m = 10,000 ; x = 100,000
    // // y = (10000 * 0.132) / (e^(-0.00005 * (10000-1)) + e^(0.00003 * 100000))
    // // y ≈ 63.7924683001747
    //
    // };

}
