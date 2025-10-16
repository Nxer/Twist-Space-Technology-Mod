package com.Nxer.TwistSpaceTechnology.common.machine.GeneratorMultis;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.GregTechAPI.sBlockCasings2;
import static gregtech.api.GregTechAPI.sBlockCasings3;
import static gregtech.api.GregTechAPI.sBlockFrames;
import static gregtech.api.GregTechAPI.sBlockMetal6;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.tile.TileLargeSolarBoilerRender;
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
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings2;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class TST_LargeSolarBoiler extends GTCM_MultiMachineBase<TST_LargeSolarBoiler> {

    // region Class Constructor
    public TST_LargeSolarBoiler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_LargeSolarBoiler(String aName) {
        super(aName);
    }

    // endregion

    private static final double heatIncreaseSpeed = 0.001; // 0.1% per second
    private static final double heatDecreaseSpeed = 0.0001; // 0.01% per second

    private static final long calcificationDelayTicks = 20 * 60 * 60 * 24; // 24 hours
    private static final long calcificationTimeSeconds = 60 * 60 * 36; // 36 hours
    private static final int calcificationFactor = 3; // max calcification level will reduce steam production by 3 times

    private static final int steamProductionBronze = 4800;
    private static final int steamProductionSteel = 14400;

    private static final double heatThresholdToExplode = 0.5;

    private static final long explosionPower = V[1];

    private static final FluidStack waterFluid = FluidUtils.getWater(1);
    private static final FluidStack distilledWaterFluid = FluidUtils.getDistilledWater(1);

    private double heat = 0; // min - 0, max - 1
    private double calcification = 0; // min - 0, max - 1
    private long runningTicks = 0;
    private boolean shouldExplode = false;
    private int machineTier = 1;
    private boolean isRendering = false;

    private int tierFrameCasing = -1;
    private int tierGearBoxCasing = -1;
    private int tierPipeCasing = -1;
    private int tierFireBoxCasing = -1;
    private int tierMachineCasing = -1;

    // region Processing Logic
    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        if (!isRendering) {
            createRenderBlock();
        }

        final ArrayList<FluidStack> storedFluids = super.getStoredFluids();
        for (FluidStack hatchFluid : storedFluids) {

            boolean hasWater = hatchFluid.isFluidEqual(waterFluid);
            boolean hasDistilledWater = hatchFluid.isFluidEqual(distilledWaterFluid);

            int amountOfFluidInHatch = 0;

            if (hasWater || hasDistilledWater) {
                amountOfFluidInHatch = hatchFluid.amount;
            }

            boolean shouldIncreaseCalcification = (runningTicks / 20) % (calcificationTimeSeconds / 100) == 0;
            if (runningTicks > calcificationDelayTicks && shouldIncreaseCalcification && hasWater) {
                calcification += 0.01;
                if (calcification > 1) {
                    calcification = 1;
                }
            }

            if (amountOfFluidInHatch > 0) {
                if (heat < heatThresholdToExplode) {
                    shouldExplode = false;
                }

                if (shouldExplode) {

                    for (MTEHatchInput hatch : mInputHatches) {
                        hatch.doExplosion(explosionPower);
                    }

                    return CheckRecipeResultRegistry.NONE;
                }

                int consumedWater = (int) (Math
                    .min(amountOfFluidInHatch, getSteamProduction() / GTValues.STEAM_PER_WATER) * heat
                    / ((calcification * (calcificationFactor - 1)) + 1));

                FluidStack liquidToDeplete;
                if (hasWater) {
                    liquidToDeplete = FluidUtils.getWater(consumedWater);
                } else {
                    liquidToDeplete = FluidUtils.getDistilledWater(consumedWater);
                }

                if (super.depleteInput(liquidToDeplete)) {
                    super.mOutputFluids = new FluidStack[] {
                        FluidUtils.getSteam(consumedWater * GTValues.STEAM_PER_WATER) };
                    super.mMaxProgresstime = 20;
                    super.mEfficiency = getMaxEfficiency(null);
                    runningTicks += 20;

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

        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == 0) {
            if (mMachine) {
                World world = aBaseMetaTileEntity.getWorld();
                boolean isClearWeather = !world.isRaining() && !world.isThundering()
                    || aBaseMetaTileEntity.getBiome().rainfall == 0.0F;
                boolean isSeeSky = aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP);
                boolean isDay = world.isDaytime();
                if ((!isClearWeather || !isSeeSky || !isDay)) {
                    heat -= heatDecreaseSpeed;
                    if (heat < 0) {
                        heat = 0;
                    }
                } else {
                    heat += heatIncreaseSpeed;
                    if (heat > 1) {
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
            if (isRendering) {
                destroyRenderBlock();
            }
            return false;
        }

        if (tierGearBoxCasing == 1 && tierPipeCasing == 1
            && tierFireBoxCasing == 1
            && tierMachineCasing == 1
            && tierFrameCasing == 1) {
            updateHatchTexture();
            machineTier = 1;
            return true;
        }
        if (tierGearBoxCasing == 2 && tierPipeCasing == 2
            && tierFireBoxCasing == 2
            && tierMachineCasing == 2
            && tierFrameCasing == 2) {
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
        if (machineTier == 2) return ((BlockCasings2) sBlockCasings2).getTextureIndex(0);
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

    private int getSteamProduction() {
        if (machineTier == 2) return steamProductionSteel;
        return steamProductionBronze;
    }

    public void createRenderBlock() {
        int x = getBaseMetaTileEntity().getXCoord();
        int y = getBaseMetaTileEntity().getYCoord();
        int z = getBaseMetaTileEntity().getZCoord();

        int xBackOffset = getExtendedFacing().getRelativeBackInWorld().offsetX;
        int zBackOffset = getExtendedFacing().getRelativeBackInWorld().offsetZ;
        int xRightOffset = getExtendedFacing().getRelativeLeftInWorld().offsetX;
        int zRightOffset = getExtendedFacing().getRelativeLeftInWorld().offsetZ;

        if (this.getBaseMetaTileEntity()
            .getWorld()
            .getBlock((x + xBackOffset * 2), (y - 1), (z + zBackOffset * 2))
            .equals(Blocks.air)) {
            this.getBaseMetaTileEntity()
                .getWorld()
                .setBlock((x + xBackOffset * 2), (y - 1), (z + zBackOffset * 2), TstBlocks.BlockLargeSolarBoilerRender);

            if (this.getBaseMetaTileEntity()
                .getWorld()
                .getTileEntity(
                    (x + xBackOffset * 2),
                    (y - 1),
                    (z + zBackOffset * 2)) instanceof TileLargeSolarBoilerRender tileRenderer) {
                tileRenderer.zBackOffset = zBackOffset;
                tileRenderer.xRightOffset = xRightOffset;
                tileRenderer.zRightOffset = zRightOffset;
                tileRenderer.updateToClient();
            }
        }

        isRendering = true;
    }

    private void destroyRenderBlock() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        int xBackOffset = getExtendedFacing().getRelativeBackInWorld().offsetX;
        int zBackOffset = getExtendedFacing().getRelativeBackInWorld().offsetZ;;

        if (this.getBaseMetaTileEntity()
            .getWorld()
            .getBlock((x + xBackOffset * 2), (y - 1), (z + zBackOffset * 2))
            .equals(TstBlocks.BlockLargeSolarBoilerRender)) {

            this.getBaseMetaTileEntity()
                .getWorld()
                .setBlock((x + xBackOffset * 2), (y - 1), (z + zBackOffset * 2), Blocks.air);
        }

        isRendering = false;
    }

    // endregion

    // region Structure
    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
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

    private static final String STRUCTURE_PIECE_MAIN = "mainLargeSolarBoiler";
    private final int horizontalOffSet = 6;
    private final int verticalOffSet = 0;
    private final int depthOffSet = 0;

    private static IStructureDefinition<TST_LargeSolarBoiler> STRUCTURE_DEFINITION = null;

    // spotless:off
    @Override
    public IStructureDefinition<TST_LargeSolarBoiler> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_LargeSolarBoiler>builder()
                .addShape(STRUCTURE_PIECE_MAIN,
                    transpose(shapeMain))
                .addElement('A', chainAllGlasses())
                .addElement('J',
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
                .addElement('K',
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
                .addElement('C',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getMachineCasingTier,
                        ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                        -1,
                        (t, m) -> t.tierMachineCasing = m,
                        t -> t.tierMachineCasing
                    )
                )
                .addElement('D',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getGearBoxCasingTier,
                        ImmutableList.of(Pair.of(sBlockCasings2, 2), Pair.of(sBlockCasings2, 3)),
                        -1,
                        (t, m) -> t.tierGearBoxCasing = m,
                        t -> t.tierGearBoxCasing
                    )
                )
                .addElement('E',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getPipeCasingTier,
                        ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                        -1,
                        (t, m) -> t.tierPipeCasing = m,
                        t -> t.tierPipeCasing
                    )
                )
                .addElement('F',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getFireBoxCasingTier,
                        ImmutableList.of(Pair.of(sBlockCasings3, 13), Pair.of(sBlockCasings3, 14)),
                        -1,
                        (t, m) -> t.tierFireBoxCasing = m,
                        t -> t.tierFireBoxCasing
                    )
                )
                .addElement('G',
                    ofBlocksTiered(
                        TST_LargeSolarBoiler::getFrameCasingTier,
                        ImmutableList.of(Pair.of(sBlockFrames, 300), Pair.of(sBlockFrames, 305)),
                        -1,
                        (t, m) -> t.tierFrameCasing = m,
                        t -> t.tierFrameCasing
                    )
                )
                .addElement('H',
                    ofBlock(sBlockMetal6, 10)
                )
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    /*
    Blocks:
    A -> ofBlock...(blockAlloyGlass, 0, ...);
    C -> ofBlock...(gt.blockcasings, 10, ...);
    D -> ofBlock...(gt.blockcasings2, 2, ...);
    E -> ofBlock...(gt.blockcasings2, 12, ...);
    F -> ofBlock...(gt.blockcasings3, 13, ...);
    G -> ofBlock...(gt.blockframes, 300, ...);
    H -> ofBlock...(gt.blockmetal6, 10, ...);
    */

    private final String[][] shapeMain = new String[][]{
        {"     C~C     "," C  CAAAC  C ","CCC CAAAC CCC"," C  CAAAC  C ","     CCC     "},
        {"     DDD     ","CAC D   D CAC","A-A D   D A-A","CAC D   D CAC","     DDD     "},
        {"     FFF     ","GJG FHHHF GKG","JCCEFHHHFECCK","GJG FHHHF GKG","     FFF     "}
    };

    // spotless:on
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
    // endregion

    // region Overrides
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(
            // #tr TST_LargeSolarBoiler.machineType
            // # Solar Boiler
            // #zh_CN 太阳能锅炉
            TextEnums.tr("TST_LargeSolarBoiler.machineType"))
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.01
                // # Steam Power by the Sun.
                // #zh_CN 蒸汽版太阳神之力.
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.01"))
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.02
                // # Works similarly to the singleblock version.
                // #zh_CN 与单方块版本运行模式类似.
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.02"))
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.03
                // # Has §6Heat§7 and §6Calcification§7 mechanics.
                // #zh_CN 有§6热量§7和§6钙化§7机制.
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.03"))
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.04
                // # On a clear day, it will quickly
                // #zh_CN 在白天晴天时, 机器将快速升温,
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.04") + " (" + heatIncreaseSpeed * 100 + "%/s) "
                // #tr TST_LargeSolarBoiler.tooltip.05
                // # increase its temperature until it reaches its maximum.
                // #zh_CN , 直到达到最大温度.
                    + TextEnums.tr("TST_LargeSolarBoiler.tooltip.05"))
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.06
                // # At night or in bad weather, it will cool down slowly
                // #zh_CN 在夜晚或非晴天时, 机器将缓慢降温
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.06") + " (" + heatDecreaseSpeed * 100 + "%/s).")
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.07
                // # The controller needs a clear view of the sky to heat up.
                // #zh_CN 控制器上方不允许遮挡.
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.07"))
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.08
                // # After
                // #zh_CN 运行
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.08") + " "
                    + EnumChatFormatting.GREEN
                    + String.format("%.1f ", calcificationDelayTicks / 20.0 / 60.0 / 60.0)
                    + EnumChatFormatting.GRAY
                    // #tr TST_LargeSolarBoiler.tooltip.09
                    // # hours will start to calcify during its work, at max level reducing steam output to:
                    // #zh_CN 小时之后机器将逐渐发生钙化, 直到将蒸汽产出率降低至原产出的:
                    + TextEnums.tr("TST_LargeSolarBoiler.tooltip.09")
                    + " "
                    + EnumChatFormatting.GREEN
                    + String.format("%.2f%%", 100.0 / calcificationFactor)
                    + EnumChatFormatting.GRAY
                    + ".")
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.10
                // # It will take
                // #zh_CN 大约需要
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.10") + " "
                    + EnumChatFormatting.GREEN
                    + String.format("%.1f ", calcificationTimeSeconds / 60.0 / 60.0)
                    + EnumChatFormatting.GRAY
                    // #tr TST_LargeSolarBoiler.tooltip.11
                    // # hours to reach max level of calcification. Use button in GUI to clear the machine.
                    // #zh_CN 小时达到最大钙化程度. 在GUI内手动点击按钮清除机器的钙化.
                    + TextEnums.tr("TST_LargeSolarBoiler.tooltip.11"))
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.17
                // # Use §bdistilled water§7 to prevent calcification.
                // #zh_CN 使用§b蒸馏水§7不会提高钙化程度.
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.17"))
            .addSeparator()
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.16
                // # Will §cExplode§7 if water is added when heat is equal or above 50%
                // #zh_CN 当温度达到50%后再加水会引起§c爆炸§7.
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.16"))
            .addSeparator()
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.12
                // # Has two tiers: §6Bronze§7 and §8Steel§7
                // #zh_CN 拥有两个等级: §6青铜§7 | §8钢§7
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.12"))
            .addInfo(
                // #tr TST_LargeSolarBoiler.tooltip.13
                // # Max steam production:
                // #zh_CN 最大蒸汽产速:
                TextEnums.tr("TST_LargeSolarBoiler.tooltip.13"))
            .addInfo(
                "  - "
                    // #tr TST_LargeSolarBoiler.tooltip.14
                    // # §6Bronze§7:
                    // #zh_CN §6青铜§7:
                    + TextEnums.tr("TST_LargeSolarBoiler.tooltip.14")
                    + " "
                    + EnumChatFormatting.WHITE
                    + steamProductionBronze
                    + " L/s"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "  - "
                    // #tr TST_LargeSolarBoiler.tooltip.15
                    // # §8Steel§7:
                    // #zh_CN §8钢§7:
                    + TextEnums.tr("TST_LargeSolarBoiler.tooltip.15")
                    + " "
                    + EnumChatFormatting.WHITE
                    + steamProductionSteel
                    + " L/s"
                    + EnumChatFormatting.GRAY)
            .addSeparator()
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
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()) };
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements.widget(
            new TextWidget().setStringSupplier(
                () -> EnumChatFormatting.WHITE
                    // #tr TST_LargeSolarBoiler.gui.02
                    // # Heat:
                    // #zh_CN 热量:
                    + TextEnums.tr("TST_LargeSolarBoiler.gui.02")
                    + " "
                    + EnumChatFormatting.GOLD
                    + numberFormat.format((int) (heat * 100))
                    + "% "
                    + EnumChatFormatting.RESET))
            .widget(
                new TextWidget().setStringSupplier(
                    () -> EnumChatFormatting.WHITE
                        // #tr TST_LargeSolarBoiler.gui.03
                        // # Calcification Level:
                        // #zh_CN 钙化程度:
                        + TextEnums.tr("TST_LargeSolarBoiler.gui.03")
                        + " "
                        + EnumChatFormatting.GOLD
                        + numberFormat.format((int) (calcification * 100))
                        + "% "
                        + EnumChatFormatting.RESET))
            .widget(new FakeSyncWidget.DoubleSyncer(() -> heat, val -> heat = val))
            .widget(new FakeSyncWidget.DoubleSyncer(() -> calcification, val -> calcification = val));;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);

        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (clickData.mouseButton == 0) {
                calcification = 0;
                runningTicks = 0;
            }
        })
            .setPlayClickSound(true)
            .setBackground(
                () -> new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                    GTUITextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT })
            .addTooltip(
                EnumChatFormatting.WHITE
                    // #tr TST_LargeSolarBoiler.gui.01
                    // # Press to clear the machine
                    // #zh_CN 点击以清洁机器的钙化
                    + TextEnums.tr("TST_LargeSolarBoiler.gui.01")
                    + EnumChatFormatting.RESET)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(new Pos2d(174, 91))
            .setSize(16, 16));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setDouble("heat", heat);
        aNBT.setDouble("calcification", calcification);
        aNBT.setBoolean("shouldExplode", shouldExplode);
        aNBT.setLong("runningTicks", runningTicks);
        aNBT.setInteger("machineTier", machineTier);
        aNBT.setBoolean("isRendering", isRendering);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        heat = aNBT.getDouble("heat");
        calcification = aNBT.getDouble("calcification");
        shouldExplode = aNBT.getBoolean("shouldExplode");
        runningTicks = aNBT.getLong("runningTicks");
        machineTier = aNBT.getInteger("machineTier");
        isRendering = aNBT.getBoolean("isRendering");
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        destroyRenderBlock();
        super.stopMachine(reason);
    }

    @Override
    public void onBlockDestroyed() {
        destroyRenderBlock();
        super.onBlockDestroyed();
    }

    // endregion
}
