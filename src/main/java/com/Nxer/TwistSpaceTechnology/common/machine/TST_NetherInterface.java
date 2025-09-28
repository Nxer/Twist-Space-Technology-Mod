package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.init.TstBlocks.MetaBlockCasing02;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.NetherInterfaceVisualRecipeMap;
import static com.Nxer.TwistSpaceTechnology.config.Config.GenerateStackEveryProcessing_NetherInterface;
import static com.Nxer.TwistSpaceTechnology.config.Config.OutputHellishMetalPercent_NetherInterface;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.api.random.RandomPackageFactory;
import com.Nxer.TwistSpaceTechnology.common.api.random.XSTR;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

public class TST_NetherInterface extends GTCM_MultiMachineBase<TST_NetherInterface> {

    // region Class Constructor
    public TST_NetherInterface(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_NetherInterface(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_NetherInterface(mName);
    }

    // endregion

    // region Logic
    public static RandomPackageFactory<ItemStack> ItemRandomGetter;

    public static void initStatics() {
        ItemRandomGetter = RandomPackageFactory.<ItemStack>builder()
            .add(
                getModItem(
                    EtFuturumRequiem.ID,
                    "ancient_debris",
                    1,
                    new ItemStack(Blocks.fire).setStackDisplayName("EtFuturumRequiem:ancient_debris")),
                1d)
            .add(ItemList.Heavy_Hellish_Mud.get(48), 49d)
            .add(ItemList.Brittle_Netherite_Scrap.get(64), 30d)
            .add(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(64), 10d)
            .add(
                getModItem(
                    ThaumicTinkerer.ID,
                    "kamiResource",
                    8,
                    6,
                    new ItemStack(Blocks.fire).setStackDisplayName("ThaumicTinkerer:kamiResource:6")),
                10d)
            .build(new ItemStack[0]);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        long inputEut = getMaxInputEu();
        if (inputEut < Config.BasicEnergyCost_NetherInterface * 3L) {
            return CheckRecipeResultRegistry.insufficientPower(Config.BasicEnergyCost_NetherInterface * 3L);
        }

        int parallel = (int) Math.min(getTrueParallel(), (inputEut / Config.BasicEnergyCost_NetherInterface) - 2);

        ArrayList<FluidStack> inputFluids = getStoredFluids();
        if (inputFluids.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        List<FluidStack> distilledWaters = new ArrayList<>();
        long waterAmount = 0;

        for (FluidStack f : inputFluids) {
            if (MiscHelper.distilledWater.isFluidEqual(f)) {
                distilledWaters.add(f);
                waterAmount += f.amount;
            }
        }

        parallel = (int) Math.min(parallel, waterAmount / Config.BasicDistilledWaterCost_NetherInterface);

        int outputFluidAmount = parallel * Config.BasicDistilledWaterCost_NetherInterface;

        int waterToCost = outputFluidAmount;
        for (FluidStack f : distilledWaters) {
            if (f.amount >= waterToCost) {
                f.amount -= (int) waterAmount;
                break;
            } else {
                waterToCost -= f.amount;
                f.amount = 0;
            }
        }

        mOutputItems = new ItemStack[GenerateStackEveryProcessing_NetherInterface];

        for (int i = 0; i < mOutputItems.length; i++) {
            ItemStack t = ItemRandomGetter.getOne()
                .copy();
            t.stackSize *= parallel;
            mOutputItems[i] = t;
        }

        if (XSTR.XSTR_INSTANCE.nextInt(10000) < OutputHellishMetalPercent_NetherInterface * 100) {
            mOutputFluids = new FluidStack[2];
            mOutputFluids[0] = Materials.HellishMetal.getMolten(288L * parallel);
            mOutputFluids[1] = Materials.PoorNetherWaste.getFluid(outputFluidAmount);
        } else {
            mOutputFluids = new FluidStack[] { Materials.PoorNetherWaste.getFluid(outputFluidAmount) };
        }

        lEUt = (long) -Config.BasicEnergyCost_NetherInterface * (2 + parallel);
        mMaxProgresstime = Config.CycleTime_NetherInterface;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return NetherInterfaceVisualRecipeMap;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        maxParallel = Config.MaxParallel_NetherInterface;
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    // endregion

    // region Structure
    // spotless:off
    private static final int horizontalOffSet = 7;
    private static final int verticalOffSet = 13;
    private static final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<TST_NetherInterface> STRUCTURE_DEFINITION = null;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        repairMachine();
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<TST_NetherInterface> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION =
                StructureDefinition
                    .<TST_NetherInterface>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                        {"               "," AAAAAAAAAAAAA "," AAAAAAAAAAAAA "," AAAAAAAAAAAAA "},
                        {"               "," A           A "," CCCCCCCCCCCCC "," A           A "},
                        {"               "," BCCCCCCCCCCCB "," CDDDDDDDDDDDC "," BCCCCCCCCCCCB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"               "," BC         CB "," CD         DC "," BC         CB "},
                        {"      A~A      "," BCCCCCCCCCCCB "," CDDDDDDDDDDDC "," BCCCCCCCCCCCB "},
                        {"      AAA      ","AAAAAAAAAAAAAAA","ACCCCCCCCCCCCCA","AAAAAAAAAAAAAAA"},
                        {"      AAA      ","AAAAAAAAAAAAAAA","AAAAAAAAAAAAAAA","AAAAAAAAAAAAAAA"}
                    }))
                    .addElement(
                        'A',
                        HatchElementBuilder
                            .<TST_NetherInterface>builder()
                            .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Energy.or(ExoticEnergy))
                            .adder(TST_NetherInterface::addToMachineList)
                            .dot(1)
                            .casingIndex(16)
                            .buildAndChain(ofBlock(GregTechAPI.sBlockCasings2, 0))
                    )
                    .addElement('B', ofFrame(Materials.Obsidian))
                    .addElement('C', ofBlock(MetaBlockCasing02, 5))
                    .addElement('D', ofBlock(Blocks.obsidian, 0))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // spotless:on
    // endregion

    // region General

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][16] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        // #tr Tooltip_NetherInterface_MachineType
        // # Otherworld Teleporter
        // #zh_CN 异界传送器
        tt.addMachineType(TextEnums.tr("Tooltip_NetherInterface_MachineType"))
          // #tr Tooltip_NetherInterface_01
          // # {\BOLD}The imprisoned souls of an ancient civilization now serve you.
          // #zh_CN {\BOLD}古老文明被囚禁的灵魂现在为你效命.
          .addInfo(TextEnums.tr("Tooltip_NetherInterface_01"))
          // #tr Tooltip_NetherInterface_02
          // # Build portals and transport the dirty but useful resources of Hell back to you.
          // #zh_CN 构建传送门, 并将地狱那些肮脏但有用的资源传送回来.
          .addInfo(TextEnums.tr("Tooltip_NetherInterface_02"))
          // #tr Tooltip_NetherInterface_03
          // # Machine takes 2A IV to maintain the teleporter, and 1A IV per parallel.
          // #zh_CN 需要消耗 2A IV 维持传送器, 并且每个并行消耗1A IV.
          .addInfo(TextEnums.tr("Tooltip_NetherInterface_03"))
          .addSeparator()
          .addInfo(TextLocalization.StructureTooComplex)
          .addInfo(TextLocalization.BLUE_PRINT_INFO)
          .beginStructureBlock(15, 16, 3, false)
          .addInputHatch(TextLocalization.textUseBlueprint, 1)
          .addOutputHatch(TextLocalization.textUseBlueprint, 1)
          .addInputBus(TextLocalization.textUseBlueprint, 1)
          .addOutputBus(TextLocalization.textUseBlueprint, 1)
          .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
          .toolTipFinisher(TextLocalization.ModName);
        // spotless:on
        return tt;
    }
    // endregion

}
