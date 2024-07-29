package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.config.Config.Parallel_MicroSpaceTimeFabricatorio;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.StabilisationFieldGenerators;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTech_API.sBlockCasings1;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;

public class TST_MicroSpaceTimeFabricatorio extends GTCM_MultiMachineBase<TST_MicroSpaceTimeFabricatorio> {

    // region Class Constructor
    public TST_MicroSpaceTimeFabricatorio(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_MicroSpaceTimeFabricatorio(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MicroSpaceTimeFabricatorio(this.mName);
    }

    // endregion

    // region Processing Logic

    public static TST_ItemID SeedSpaceTime;

    public static void initStatics() {
        SeedSpaceTime = TST_ItemID.createNoNBT(GTCMItemList.SeedsSpaceTime.get(1));
    }

    protected int transcendentCasingTier = 0;
    protected int injectionCasingTier = 0;
    protected int bridgeCasingTier = 0;
    protected int fieldTier = 0;
    protected GT_MetaTileEntity_Hatch_InputBus specialInputBus;
    protected boolean perfectOverclock = false;
    protected float speedBonus = 1;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("transcendentCasingTier", transcendentCasingTier);
        aNBT.setInteger("injectionCasingTier", injectionCasingTier);
        aNBT.setInteger("bridgeCasingTier", bridgeCasingTier);
        aNBT.setInteger("fieldTier", fieldTier);
        aNBT.setBoolean("perfectOverclock", perfectOverclock);
        aNBT.setFloat("speedBonus", speedBonus);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        transcendentCasingTier = aNBT.getInteger("transcendentCasingTier");
        injectionCasingTier = aNBT.getInteger("injectionCasingTier");
        bridgeCasingTier = aNBT.getInteger("bridgeCasingTier");
        fieldTier = aNBT.getInteger("fieldTier");
        perfectOverclock = aNBT.getBoolean("perfectOverclock");
        speedBonus = aNBT.getFloat("speedBonus");
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.MicroSpaceTimeFabricatorioRecipes;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processingLogic.getDuration();
        setEnergyUsage(processingLogic);

        mOutputItems = processingLogic.getOutputItems();
        mOutputFluids = processingLogic.getOutputFluids();

        if (tryConsumeSpaceTimeSeed()) {
            if (mOutputItems != null && mOutputItems.length > 0) {
                List<ItemStack> o = new ArrayList<>();
                for (ItemStack i : mOutputItems) {
                    o.add(i.copy());
                    o.add(i);
                }
                mOutputItems = o.toArray(new ItemStack[0]);
            }

            if (mOutputFluids != null && mOutputFluids.length > 0) {
                List<FluidStack> o = new ArrayList<>();
                for (FluidStack f : mOutputFluids) {
                    o.add(f.copy());
                    o.add(f);
                }
                mOutputFluids = o.toArray(new FluidStack[0]);
            }
        }

        return result;
    }

    protected boolean tryConsumeSpaceTimeSeed() {
        if (null == specialInputBus || !specialInputBus.isValid()) return false;
        IGregTechTileEntity te = specialInputBus.getBaseMetaTileEntity();
        for (int i = te.getSizeInventory() - 1; i >= 0; i--) {
            ItemStack itemStack = te.getStackInSlot(i);
            if (itemStack != null && itemStack.stackSize >= 1) {
                if (SeedSpaceTime.equalItemStack(itemStack)) {
                    itemStack.stackSize--;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        if (null != specialInputBus && specialInputBus.isValid()) specialInputBus.updateSlots();
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return perfectOverclock;
    }

    @Override
    protected float getSpeedBonus() {
        return speedBonus;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return Parallel_MicroSpaceTimeFabricatorio;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        specialInputBus = null;
        transcendentCasingTier = 0;
        injectionCasingTier = 0;
        bridgeCasingTier = 0;
        fieldTier = 0;
        perfectOverclock = false;
        speedBonus = 1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (fieldTier < 1) return false;
        if (fieldTier > 1) {
            // using homo field block need use homo structure block at same time
            if (transcendentCasingTier < 2 || injectionCasingTier < 2 || bridgeCasingTier < 2) return false;
            // using homo field block enable perfect overclock
            perfectOverclock = true;
            if (fieldTier > 2) {
                // higher tier gives a stacked speed up parameter
                int t = fieldTier - 2;
                int speedUp = (t + 1) * t / 2;
                speedBonus = 1F / speedUp;
            }
        }

        return true;
    }

    // endregion

    // region Structure

    protected static final int horizontalOffSet = 9;
    protected static final int verticalOffSet = 20;
    protected static final int depthOffSet = 1;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static IStructureDefinition<TST_MicroSpaceTimeFabricatorio> STRUCTURE_DEFINITION = null;

    public static int getBlockFieldTier(Block block, int meta) {
        if (block == sBlockCasingsTT && meta == 14) {
            return 1;
        }
        if (block == StabilisationFieldGenerators && meta <= 8) {
            return meta + 2;
        }
        return 0;
    }

    public static int getBlockTranscendentCasingTier(Block block, int meta) {
        if (block == sBlockCasings1 && meta == 12) {
            return 1;
        }
        if (block == sBlockCasingsBA0 && meta == 11) {
            return 2;
        }
        return 0;
    }

    public static int getBlockInjectionCasingTier(Block block, int meta) {
        if (block == sBlockCasings1 && meta == 13) {
            return 1;
        }
        if (block == sBlockCasingsBA0 && meta == 12) {
            return 2;
        }
        return 0;
    }

    public static int getBlockBridgeCasingTier(Block block, int meta) {
        if (block == sBlockCasings1 && meta == 14) {
            return 1;
        }
        if (block == sBlockCasingsTT && meta == 10) {
            return 2;
        }
        return 0;
    }

    public boolean addSpecialInputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;

        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            hatch.mRecipeMap = getRecipeMap();
            specialInputBus = hatch;
            return true;
        }
        return false;
    }

    @Override
    protected void startRecipeProcessing() {
        super.startRecipeProcessing();
        if (null != specialInputBus && specialInputBus.isValid()) {
            if (specialInputBus instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
    }

    @Override
    protected void endRecipeProcessing() {
        super.endRecipeProcessing();
        if (null != specialInputBus && specialInputBus.isValid()) {
            if (specialInputBus instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(
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
    public IStructureDefinition<TST_MicroSpaceTimeFabricatorio> getStructureDefinition() {

        if (null == STRUCTURE_DEFINITION) {
            IStructureElement<TST_MicroSpaceTimeFabricatorio> tra = ofBlocksTiered(
                TST_MicroSpaceTimeFabricatorio::getBlockTranscendentCasingTier,
                ImmutableList.of(Pair.of(sBlockCasings1, 12), Pair.of(sBlockCasingsBA0, 11)),
                0,
                (m, t) -> m.transcendentCasingTier = t,
                m -> m.transcendentCasingTier);
            IStructureElement<TST_MicroSpaceTimeFabricatorio> inj = ofBlocksTiered(
                TST_MicroSpaceTimeFabricatorio::getBlockInjectionCasingTier,
                ImmutableList.of(Pair.of(sBlockCasings1, 13), Pair.of(sBlockCasingsBA0, 12)),
                0,
                (m, t) -> m.injectionCasingTier = t,
                m -> m.injectionCasingTier);
            IStructureElement<TST_MicroSpaceTimeFabricatorio> bri = ofBlocksTiered(
                TST_MicroSpaceTimeFabricatorio::getBlockBridgeCasingTier,
                ImmutableList.of(Pair.of(sBlockCasings1, 14), Pair.of(sBlockCasingsTT, 10)),
                0,
                (m, t) -> m.bridgeCasingTier = t,
                m -> m.bridgeCasingTier);

            STRUCTURE_DEFINITION = StructureDefinition.<TST_MicroSpaceTimeFabricatorio>builder()
                // spotless:off
                    .addShape(
                        STRUCTURE_PIECE_MAIN,
                        transpose(new String[][]{
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","             D     ","             D     ","             D     ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","            D      ","            D      ","                   ","                   ","                   ","            D      ","            D      ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","            D      ","            D      ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","            D      ","            D      ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","           D       ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","           D       ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","           D       ","                   ","                   ","                   ","                   "," D                 "," D                 "," D                 ","                   ","                   ","                   ","                   ","           D       ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","          D        ","                   ","   D               ","   D               ","  D                ","  D                ","                   ","                   ","                   ","  D                ","  D                ","   D               ","   D               ","                   ","          D        ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","          D        ","    DD             ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","    DD             ","          D        ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","         D         ","      DD           ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","      DD           ","         D         ","                   "},
                            {"                   ","                   ","                   ","         C         ","        DDD        ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","        DDD        ","         C         "},
                            {"                   ","                   ","                   ","                   ","         D         ","           DD      ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","           DD      ","         D         ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","        D          ","             DD    ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","             DD    ","        D          ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","        D          ","                   ","               D   ","               D   ","                D  ","                D  ","                   ","                   ","                   ","                D  ","                D  ","               D   ","               D   ","                   ","        D          ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","       D           ","                   ","                   ","                   ","                   ","                 D ","                 D ","                 D ","                   ","                   ","                   ","                   ","       D           ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","       D           ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","       D           ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","      D            ","      D            ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","      D            ","      D            ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","      D            ","      D            ","                   ","                   ","                   ","      D            ","      D            ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","     D             ","     D             ","     D             ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","        CCC        ","                   ","    CC       CC    ","    C         C    ","                   ","                   ","  C             C  ","  C             C  ","  C             C  ","                   ","                   ","    C         C    ","    CC       CC    ","                   ","        CCC        ","                   ","                   "},
                            {"                   ","         ~         ","                   ","                   ","        CCC        ","      CCBBBCC      ","    CCBBBBBBBCC    ","   CBBBBBBBBBBBC   ","   CBBBBBBBBBBBC   ","  CBBBBBBBBBBBBBC  ","  CBBBBBBBBBBBBBC  "," CBBBBBBBBBBBBBBBC "," CBBBBBBBBBBBBBBBC "," CBBBBBBBBBBBBBBBC ","  CBBBBBBBBBBBBBC  ","  CBBBBBBBBBBBBBC  ","   CBBBBBBBBBBBC   ","   CBBBBBBBBBBBC   ","    CCBBBBBBBCC    ","      CCBBBCC      ","        CCC        ","                   "},
                            {"       FAEAF       ","       FAAAF       ","       FAAAF       ","       AAAAA       ","      AAAAAAA      ","    AAAAAAAAAAA    ","   AAAAAAAAAAAAA   ","  AAAAAAAAAAAAAAA  ","  AAAAAAAAAAAAAAA  "," AAAAAAAAAAAAAAAAA "," AAAAAAAAAAAAAAAAA ","AAAAAAAAAAAAAAAAAAA","AAAAAAAAAAAAAAAAAAA","AAAAAAAAAAAAAAAAAAA"," AAAAAAAAAAAAAAAAA "," AAAAAAAAAAAAAAAAA ","  AAAAAAAAAAAAAAA  ","  AAAAAAAAAAAAAAA  ","   AAAAAAAAAAAAA   ","    AAAAAAAAAAA    ","      AAAAAAA      ","        AAA        "}
                        })
                    )
                    // spotless:on
                .addElement('A', tra)
                .addElement('B', inj)
                .addElement('C', bri)
                .addElement(
                    'D',
                    ofBlocksTiered(
                        TST_MicroSpaceTimeFabricatorio::getBlockFieldTier,
                        ImmutableList.of(
                            Pair.of(sBlockCasingsTT, 14),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 0),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 1),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 2),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 3),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 4),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 5),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 6),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 7),
                            Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 8)),
                        0,
                        (m, t) -> m.fieldTier = t,
                        m -> m.fieldTier))
                .addElement(
                    'E',
                    GT_HatchElementBuilder.<TST_MicroSpaceTimeFabricatorio>builder()
                        .atLeast(InputBus)
                        .adder(TST_MicroSpaceTimeFabricatorio::addSpecialInputBusToMachineList)
                        .dot(2)
                        .casingIndex(1024 + 12)
                        .buildAndChain(tra))
                .addElement(
                    'F',
                    GT_HatchElementBuilder.<TST_MicroSpaceTimeFabricatorio>builder()
                        .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_MicroSpaceTimeFabricatorio::addToMachineList)
                        .dot(1)
                        .casingIndex(1024)
                        .buildAndChain(inj))
                .build();
            /*
             * A -> ofBlock...(gt.blockcasings, 12, ...); tra
             * B -> ofBlock...(gt.blockcasings, 13, ...); inj
             * C -> ofBlock...(gt.blockcasings, 14, ...); bri
             * D -> ofBlock...(gt.blockcasingsTT, 14, ...); generator
             * E -> ofBlock...(tile.stonebricksmooth, 0, ...); spaceTime seed input bus
             * F -> ofBlock...(tile.wood, 0, ...); general hatches
             */
        }

        return STRUCTURE_DEFINITION;
    }

    // endregion

    // region General
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tooltip = new GT_Multiblock_Tooltip_Builder();
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
        return tooltip;
    }

    protected static Textures.BlockIcons.CustomIcon ActiveFace;
    protected static Textures.BlockIcons.CustomIcon InactiveFace;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ActiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Adv_on");
        InactiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Adv_off");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[8][12],
                new TT_RenderedExtendedFacingTexture(active ? ActiveFace : InactiveFace) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[8][12] };
    }
}
