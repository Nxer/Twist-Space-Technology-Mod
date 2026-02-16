package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing02;
import static com.Nxer.TwistSpaceTechnology.util.TextEnums.tr;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static java.lang.Integer.MAX_VALUE;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.TstSharedLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_MegaSolarPanelFactory extends GTCM_MultiMachineBase<TST_MegaSolarPanelFactory> {

    // region Class Constructor

    public TST_MegaSolarPanelFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_MegaSolarPanelFactory(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaSolarPanelFactory(mName);
    }

    // endregion

    // region Processing Logic
    private int casingTier = -2;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return super.createProcessingLogic();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.MegaSolarPanelFactoryRecpies;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    public int getMaxParallelRecipes() {
        return MAX_VALUE;
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = TstSharedLocalization.MachineInfo.componentTier(this.casingTier + 1);

        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("casingTier", casingTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        casingTier = aNBT.getInteger("casingTier");
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("batchMode")) {
            currentTip.add(EnumChatFormatting.GREEN + TextEnums.tr("Waila.TST_MegaSolarPanelFactory.1"));
            // #tr Waila.TST_MegaSolarPanelFactory.1
            // # {\GREEN}Batch mode is ON
            // #zh_CN {\GREEN}批处理已开启
        }
        float speedBonus = tag.getFloat("speedBonus");
        if (speedBonus > -1) {
            currentTip.add(
                EnumChatFormatting.GREEN + TextEnums.tr("Waila.TST_MegaSolarPanelFactory.2")
                    + "="
                    + EnumChatFormatting.GOLD
                    + GTUtility.formatNumbers(tag.getFloat("speedBonus"))
                    + "%");
            // #tr Waila.TST_MegaSolarPanelFactory.2
            // # {\GREEN}Current Speed Bonus
            // #zh_CN {\GREEN}当前速度加成
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("batchMode", batchMode);
            tag.setFloat("speedBonus", casingTier * 100);
        }
    }

    // endregion

    // region Structure

    protected static final int horizontalOffSet = 6;
    protected static final int verticalOffSet = 14;
    protected static final int depthOffSet = 0;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static IStructureDefinition<TST_MegaSolarPanelFactory> STRUCTURE_DEFINITION;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.casingTier = -2;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        this.speedBonus = 1F / (casingTier + 1);
        return true;

    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(
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
    public IStructureDefinition<TST_MegaSolarPanelFactory> getStructureDefinition() {
        if (null == STRUCTURE_DEFINITION) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaSolarPanelFactory>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                // A -> ofBlock...(BW_GlasBlocks, 0, ...);
                // B -> ofBlock...(componentAssemblyLineCasing, 0, ...);
                // C -> ofBlock...(gt.blockcasings10, 11, ...);
                // D -> ofBlock...(gt.blockcasingsTT, 9, ...);
                // E -> ofBlock...(gt.godforgecasing, 0, ...);
                // F -> ofBlock...(tile.MetaBlockCasing02, 2, ...);

                .addElement('A', chainAllGlasses())
                .addElement(
                    'B',
                    withChannel(
                        "component",
                        ofBlocksTiered(
                            (block, meta) -> block == Loaders.componentAssemblylineCasing ? meta : -1,
                            IntStream.range(0, 14)
                                .mapToObj(i -> Pair.of(Loaders.componentAssemblylineCasing, i))
                                .collect(Collectors.toList()),
                            -2,
                            (t, meta) -> t.casingTier = meta,
                            t -> t.casingTier)))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 11))
                .addElement('D', ofBlock(sBlockCasingsTT, 9))
                .addElement('E', ofBlock(GodforgeCasings, 0))
                .addElement(
                    'F',
                    HatchElementBuilder.<TST_MegaSolarPanelFactory>builder()
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_MegaSolarPanelFactory::addToMachineList)
                        .dot(1)
                        .casingIndex(TstBlocks.MetaBlockCasing02.getTextureIndex(2))
                        .buildAndChain(MetaBlockCasing02, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // spotless:off
    protected static final String[][] shape = new String[][]{
        {"     CFC     ","  CCCCCCCCC  "," CCCCCCCCCCC "," CCCCCCCCCCC "," CCCCCCCCCCC ","CCCCCEEECCCCC","FCCCCEEECCCCF","CCCCCEEECCCCC"," CCCCCCCCCCC "," CCCCCCCCCCC "," CCCCCCCCCCC ","  CCCCCCCCC  ","     CFC     "},
        {"   CCCFCCC   ","  CBBC CBBC  "," CB       BC ","CB F     F BC","CB   ACA   BC","CC  A   A  CC","F   C D C   F","CC  A   A  CC","CB   ACA   BC","CB F     F BC"," CB       BC ","  CBBC CBBC  ","   CCCFCCC   "},
        {"     CFC     ","  C       C  "," C         C ","   F     F   ","     ACA     ","C   A   A   C","F   C D C   F","C   A   A   C","     ACA     ","   F     F   "," C         C ","  C       C  ","     CFC     "},
        {"     C C     ","             ","             ","   F     F   ","     ACA     ","C   A   A   C","    C D C    ","C   A   A   C","     ACA     ","   F     F   ","             ","             ","     C C     "},
        {"             ","             ","             ","   F     F   ","     ACA     ","    A   A    ","    C D C    ","    A   A    ","     ACA     ","   F     F   ","             ","             ","             "},
        {"             ","             ","             ","             ","     CCC     ","    C   C    ","    C D C    ","    C   C    ","     CCC     ","             ","             ","             ","             "},
        {"             ","             ","             ","             ","             ","             ","      D      ","             ","             ","             ","             ","             ","             "},
        {"             ","             ","             ","             ","     FFF     ","    F   F    ","    F   F    ","    F   F    ","     FFF     ","             ","             ","             ","             "},
        {"             ","             ","             ","             ","     FFF     ","    F   F    ","    F   F    ","    F   F    ","     FFF     ","             ","             ","             ","             "},
        {"             ","             ","             ","             ","             ","             ","      D      ","             ","             ","             ","             ","             ","             "},
        {"             ","             ","             ","             ","     CCC     ","    C   C    ","    C D C    ","    C   C    ","     CCC     ","             ","             ","             ","             "},
        {"             ","             ","             ","   F     F   ","     ACA     ","    A   A    ","    C D C    ","    A   A    ","     ACA     ","   F     F   ","             ","             ","             "},
        {"     C C     ","             ","             ","   F     F   ","     ACA     ","C   A   A   C","    C D C    ","C   A   A   C","     ACA     ","   F     F   ","             ","             ","     C C     "},
        {"     CFC     ","  C       C  "," C         C ","   F     F   ","     ACA     ","C   A   A   C","F   C D C   F","C   A   A   C","     ACA     ","   F     F   "," C         C ","  C       C  ","     CFC     "},
        {"   CCC~CCC   ","  CBBC CBBC  "," CB       BC ","CB F     F BC","CB   ACA   BC","CC  A   A  CC","F   C D C   F","CC  A   A  CC","CB   ACA   BC","CB F     F BC"," CB       BC ","  CBBC CBBC  ","   CCCFCCC   "},
        {"     CFC     ","  CCCCCCCCC  "," CCCCCCCCCCC "," CCCCCCCCCCC "," CCCCCCCCCCC ","CCCCCEEECCCCC","FCCCCEEECCCCF","CCCCCEEECCCCC"," CCCCCCCCCCC "," CCCCCCCCCCC "," CCCCCCCCCCC ","  CCCCCCCCC  ","     CFC     "}
    };


    // spotless:on

    // region General

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side,
        ForgeDirection forgeDirection, int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == forgeDirection) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(181),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(181), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(181) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tttt = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_MegaSolarPanelFactory
        // # Solar Factory
        // #zh_CN 太阳能板制造厂
        tttt.addMachineType(tr("Tooltip_MegaSolarPanelFactory_MachineType"))
            // #tr Tooltip_MegaSolarPanelFactory_1_00
            // # {\BOLD}bigger is better!
            // #zh_CN {\BOLD}大就是好！
            .addInfo(tr("Tooltip_MegaSolarPanelFactory_1_00"))
            // #tr Tooltip_MegaSolarPanelFactory_1_01
            // # {\LIGHT_PURPLE}Perfect overclocking{\GRAY}.
            // #zh_CN 执行{\LIGHT_PURPLE}无损超频{\GRAY}.
            .addInfo(tr("Tooltip_MegaSolarPanelFactory_1_01"))
            // #tr Tooltip_MegaSolarPanelFactory_1_02
            // # Recipe Time Multiplier = 100% / Component Casing Tier.
            // #zh_CN 耗时倍率 = 100% / 部件装配线外壳等级.
            .addInfo(tr("Tooltip_MegaSolarPanelFactory_1_02"))
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(13, 16, 13, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        // spotless:on
        return tttt;
    }

    // endregion

}
