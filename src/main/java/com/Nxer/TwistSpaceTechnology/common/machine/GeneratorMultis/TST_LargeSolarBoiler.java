package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.SimpleResultWithText;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings2;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.GregTechAPI.sBlockCasings2;
import static gregtech.api.GregTechAPI.sBlockCasings3;
import static gregtech.api.GregTechAPI.sBlockFrames;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsBA0;

public class TST_LargeSolarBoiler extends GTCM_MultiMachineBase<TST_LargeSolarBoiler> {

    // region Class Constructor
    public TST_LargeSolarBoiler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_LargeSolarBoiler(String aName) {
        super(aName);
    }

    // endregion

    private final double heatIncreaseSpeed = 0.01; //1 % per second
    private final double heatDecreaseSpeed = 0.005; //0.5 % per second

    private final double calcificationDelayTicks = 20*20; // 20 seconds
    private final double calcificationIncreaseSpeed = 0.005; //0.5 % per second
    private final int calcificationFactor = 3; // max calcification level will reduce steam production by 3 times

    private final int steamProductionBronze = 4800;
    private final int steamProductionSteel = 14400;

    private final double heatThresholdToExplode = 0.5;

    private final long explosionPower = V[1];

    private double heat = 0; // min - 0, max - 1
    private double calcification = 0; // min - 0, max - 1
    private long runningTicks = 0;
    private boolean shouldExplode = false;
    private int machineTier = 1;

    private int tierFrameCasing = -1;
    private int tierGearBoxCasing = -1;
    private int tierPipeCasing = -1;
    private int tierFireBoxCasing = -1;
    private int tierMachineCasing = -1;

    // region Processing Logic
    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        runningTicks+=20;

        final ArrayList<FluidStack> storedFluids = super.getStoredFluids();
        for (FluidStack hatchFluid : storedFluids) {
            FluidStack waterFluid = FluidUtils.getWater(1);
            FluidStack distilledWaterFluid = FluidUtils.getDistilledWater(1);

            boolean hasWater = hatchFluid.isFluidEqual(waterFluid) && waterFluid != null;
            boolean hasDistilledWater = hatchFluid.isFluidEqual(distilledWaterFluid) && distilledWaterFluid != null;

            int amountOfFluidInHatch = 0;

            if(hasWater || hasDistilledWater){
                amountOfFluidInHatch = hatchFluid.amount;
            }

            if(runningTicks > calcificationDelayTicks && hasWater) {
                calcification+=calcificationIncreaseSpeed;
                if (calcification > 1) {
                    calcification = 1;
                }
            }

            if (amountOfFluidInHatch > 0) {
                if(heat < heatThresholdToExplode){
                    shouldExplode = false;
                }

                if(shouldExplode){

                    for (MTEHatchInput hatch : mInputHatches){
                        hatch.doExplosion(explosionPower);
                    }

                    return CheckRecipeResultRegistry.NONE;
                }

                int consumedWater = (int)(Math.min(amountOfFluidInHatch, getSteamProduction() / GTValues.STEAM_PER_WATER) * heat / ((calcification * (calcificationFactor - 1)) + 1));

                FluidStack liquidToDeplete = null;
                if(hasWater){
                    liquidToDeplete = FluidUtils.getWater(consumedWater);
                }
                else{
                    liquidToDeplete = FluidUtils.getDistilledWater(consumedWater);
                }

                if (super.depleteInput(liquidToDeplete)) {
                    super.mOutputFluids = new FluidStack[]{FluidUtils.getSteam(consumedWater * GTValues.STEAM_PER_WATER)};
                    super.mMaxProgresstime = 20;
                    super.mEfficiency = getMaxEfficiency(null);

                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
            }
        }

        shouldExplode = heat >= heatThresholdToExplode;

        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        World world = aBaseMetaTileEntity.getWorld();
        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == 0) {
            if(mMachine){
                boolean isClearWeather = !world.isRaining() && !world.isThundering()
                    || aBaseMetaTileEntity.getBiome().rainfall == 0.0F;
                boolean isSeeSky = aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP);
                boolean isDay = world.isDaytime();
                if((!isClearWeather || !isSeeSky || !isDay)){
                    heat -= heatDecreaseSpeed;
                    if (heat < 0){
                        heat = 0;
                    }
                }
                else{
                    heat += heatIncreaseSpeed;
                    if (heat > 1){
                        heat = 1;
                    }
                }
            }
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tierFrameCasing = -1;
        tierGearBoxCasing = -1;
        tierPipeCasing = -1;
        tierFireBoxCasing = -1;
        tierMachineCasing = -1;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            TwistSpaceTechnology.LOG.info(tierMachineCasing);
            return false;
        }

        if (tierGearBoxCasing == 1 &&
            tierPipeCasing == 1 &&
            tierFireBoxCasing == 1 &&
            tierMachineCasing == 1 &&
            tierFrameCasing == 1
        ) {
            updateHatchTexture();
            machineTier = 1;
            return true;
        }
        if (tierGearBoxCasing == 2 &&
            tierPipeCasing == 2 &&
            tierFireBoxCasing == 2 &&
            tierMachineCasing == 2 &&
            tierFrameCasing == 2
        ) {
            updateHatchTexture();
            machineTier = 2;
            return true;
        }

        return false;
    }

    private void updateHatchTexture() {
        for (MTEHatchInput hatch : mInputHatches) hatch.updateTexture(getCasingTextureID());
        for (MTEHatchOutput hatch : mOutputHatches) hatch.updateTexture(getCasingTextureID());
    }

    private int getCasingTextureID() {
        if (machineTier == 2)
            return((BlockCasings2) sBlockCasings2).getTextureIndex(0);
        return ((BlockCasings1) sBlockCasings1).getTextureIndex(10);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        machineTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) machineTier;
    }

    private int getSteamProduction(){
        if(machineTier == 2)
            return steamProductionSteel;
        return steamProductionBronze;
    }

    // endregion

    // region Structure
    // spotless:off
    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainLargeSolarBoiler";
    private final int horizontalOffSet = 11;
    private final int verticalOffSet = 2;
    private final int depthOffSet = 0;

    private static IStructureDefinition<TST_LargeSolarBoiler> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_LargeSolarBoiler> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_LargeSolarBoiler>builder()
                .addShape(STRUCTURE_PIECE_MAIN,
                    transpose(shapeMain))
                .addElement('A', Glasses.chainAllGlasses())
                .addElement('B',
                    ofChain(
                        buildHatchAdder(TST_LargeSolarBoiler.class)
                            .atLeast(InputHatch)
                            .casingIndex(getCasingTextureID())
                            .dot(1)
                            .build(),
                        ofBlocksTiered(
                            TST_LargeSolarBoiler::getMachineCasingTier,
                            ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                            -1,
                            (t, m) -> t.tierMachineCasing = m,
                            t -> t.tierMachineCasing
                        )
                    )
                )
                .addElement('C',
                    ofChain(
                        buildHatchAdder(TST_LargeSolarBoiler.class)
                            .atLeast(OutputHatch)
                            .casingIndex(getCasingTextureID())
                            .dot(2)
                            .build(),
                        ofBlocksTiered(
                            TST_LargeSolarBoiler::getMachineCasingTier,
                            ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                            -1,
                            (t, m) -> t.tierMachineCasing = m,
                            t -> t.tierMachineCasing
                        )
                    )
                )
                .addElement('D',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getMachineCasingTier,
                        ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                        -1,
                        (t, m) -> t.tierMachineCasing = m,
                        t -> t.tierMachineCasing
                    )
                )
                .addElement('E',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getGearBoxCasingTier,
                        ImmutableList.of(Pair.of(sBlockCasings2, 2), Pair.of(sBlockCasings2, 3)),
                        -1,
                        (t, m) -> t.tierGearBoxCasing = m,
                        t -> t.tierGearBoxCasing
                    )
                )
                .addElement('F',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getPipeCasingTier,
                        ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                        -1,
                        (t, m) -> t.tierPipeCasing = m,
                        t -> t.tierPipeCasing
                    )
                )
                .addElement('G',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getFireBoxCasingTier,
                        ImmutableList.of(Pair.of(sBlockCasings3, 13), Pair.of(sBlockCasings3, 14)),
                        -1,
                        (t, m) -> t.tierFireBoxCasing = m,
                        t -> t.tierFireBoxCasing
                    )
                )
                .addElement('H',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getFrameCasingTier,
                        ImmutableList.of(Pair.of(sBlockFrames, 300), Pair.of(sBlockFrames, 305)),
                        -1,
                        (t, m) -> t.tierFrameCasing = m,
                        t -> t.tierFrameCasing
                    )
                )
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    /*
    Blocks:
    A -> ofBlock...(blockAlloyGlass, 0, ...);
    B -> ofBlock...(gt.blockcasings, 0, ...);
    C -> ofBlock...(gt.blockcasings, 1, ...);
    D -> ofBlock...(gt.blockcasings, 10, ...);
    E -> ofBlock...(gt.blockcasings2, 2, ...);
    F -> ofBlock...(gt.blockcasings2, 12, ...);
    G -> ofBlock...(gt.blockcasings3, 13, ...);
    H -> ofBlock...(gt.blockframes, 300, ...);

    */
    private final String[][] shapeMain = new String[][]{
        {"                  "," D                ","DDD             D "," D             DDD","                D ","                  "},
        {"       HHHH       ","DAD   HEEEEH      ","A A   HEEEEH   DAD","DAD   HEEEEH   A A","      HEEEEH   DAD","       HHHH       "},
        {"      DGGGG~      ","HDH   DDDDDD      ","BDDFF DDDDDDFF HDH","HDH FFDDDDDD FFDDC","      DDDDDD   HDH","      DGGGGD      "}
    };

    public static Integer getFrameCasingTier(Block block, int meta) {
        if (block == sBlockFrames && meta == 300) {
            return 1;
        }
        if (block == sBlockFrames && meta == 305) {
            return 2;
        }
        return null;
    }

    public static Integer getGearBoxCasingTier(Block block, int meta) {
        if (block == sBlockCasings2 && meta == 2) {
            return 1;
        }
        if (block == sBlockCasings2 && meta == 3) {
            return 2;
        }
        return null;
    }

    public static Integer getPipeCasingTier(Block block, int meta) {
        if (block == sBlockCasings2 && meta == 12) {
            return 1;
        }
        if (block == sBlockCasings2 && meta == 13) {
            return 2;
        }
        return null;
    }

    public static Integer getFireBoxCasingTier(Block block, int meta) {
        if (block == sBlockCasings3 && meta == 13) {
            return 1;
        }
        if (block == sBlockCasings3 && meta == 14) {
            return 2;
        }
        return null;
    }

    public static Integer getMachineCasingTier(Block block, int meta) {
        if (block == sBlockCasings1 && meta == 10) {
            return 1;
        }
        if (block == sBlockCasings2 && meta == 0) {
            return 2;
        }
        return null;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }
    // spotless:on
    // endregion

    // region Overrides
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(
            // #tr TST_LargeSolarBoiler.machineType
            // # Solar Boiler
            TextEnums.tr("TST_LargeSolarBoiler.machineType")
        )
        .addInfo(
            // #tr TST_LargeSolarBoiler.tooltip.01
            // # TODO
            TextEnums.tr("TST_LargeSolarBoiler.tooltip.01")
        )
        .addInfo(TextEnums.Author_Faotik.getText())
        .toolTipFinisher(TextLocalization.ModName);
        return tt;
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
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_LargeSolarBoiler(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()) };
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.WHITE
                            // #tr TST_LargeSolarBoiler.gui.02
                            // # Heat:
                            + TextEnums.tr("TST_LargeSolarBoiler.gui.02")
                            + " "
                            + EnumChatFormatting.GOLD
                            + numberFormat.format((int)(heat*100))
                            + "% "
                            + EnumChatFormatting.RESET
                    )
            )
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.WHITE
                            // #tr TST_LargeSolarBoiler.gui.03
                            // # Calcification Level:
                            + TextEnums.tr("TST_LargeSolarBoiler.gui.03")
                            + " "
                            + EnumChatFormatting.GOLD
                            + numberFormat.format((int)(calcification*100))
                            + "% "
                            + EnumChatFormatting.RESET
                    )
            )
            .widget(
                new FakeSyncWidget.DoubleSyncer(() -> heat, val -> heat = val)
            )
            .widget(
                new FakeSyncWidget.DoubleSyncer(() -> calcification, val -> calcification = val)
            );;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);

        builder
            .widget(
                new ButtonWidget()
                    .setOnClick((clickData, widget) -> {})
                    .setPlayClickSound(true)
                    .setBackground(() -> new IDrawable[] { GTUITextures.BUTTON_STANDARD, GTUITextures.SLOT_MAINTENANCE })
                    .addTooltip(
                        EnumChatFormatting.WHITE
                        // #tr TST_LargeSolarBoiler.gui.01
                        // # Press to clear the machine
                        + TextEnums.tr("TST_LargeSolarBoiler.gui.01")
                        + EnumChatFormatting.RESET
                    )
                    .setTooltipShowUpDelay(TOOLTIP_DELAY)
                    .setPos(new Pos2d(174, 91))
                    .setSize(16, 16)
            );
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setDouble("heat", heat);
        aNBT.setDouble("calcification", calcification);
        aNBT.setBoolean("shouldExplode", shouldExplode);
        aNBT.setLong("runningTicks", runningTicks);
        aNBT.setInteger("machineTier", machineTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        heat = aNBT.getDouble("heat");
        calcification = aNBT.getDouble("calcification");
        shouldExplode = aNBT.getBoolean("shouldExplode");
        runningTicks = aNBT.getLong("runningTicks");
        machineTier = aNBT.getInteger("machineTier");
    }

    // endregion
}
