package com.Nxer.TwistSpaceTechnology.system.Disassembler;

import static com.Nxer.TwistSpaceTechnology.config.Config.CostTicksPerItemDisassembling_Disassembler;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

public class TST_Disassembler extends GTCM_MultiMachineBase<TST_Disassembler> {

    // region Class Constructor
    public TST_Disassembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_Disassembler(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_Disassembler(this.mName);
    }

    // endregion

    // region Structure
    // spotless:off
    private static final int horizontalOffSet = 13;
    private static final int verticalOffSet = 21;
    private static final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainTSTDisassembler";
    private static IStructureDefinition<TST_Disassembler> STRUCTURE_DEFINITION = null;
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<TST_Disassembler> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION =
                StructureDefinition
                    .<TST_Disassembler>builder()
                    .addShape(
                        STRUCTURE_PIECE_MAIN,
                        transpose(new String[][]{
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","           D   D           ","           D   D           ","           D   D           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","           D   D           ","           D   D           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","           D   D           ","           D G D           ","            GGG            ","             G             ","                           ","                           ","                           ","                           ","                           ","                           "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","            EEE            ","                           ","                           ","                           ","                           ","                           ","            GGG            ","           DGGGD           ","            GGG            ","             G             ","                           ","                           ","                           ","                           ","                           "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","            FFF            ","          EEBBBEE          ","            FFF            ","                           ","                           ","                           ","                           ","                           ","            GGG            ","           DGGGD           ","            GGG            ","             G             ","                           ","                           ","                           ","                           "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","          FF   FF          ","         EBBABABBE         ","          FF   FF          ","                           ","                           ","                           ","                           ","                           ","                           ","           D G D           ","            GGG            ","            GGG            ","             G             ","                           ","                           ","                           "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","         F                 ","        EBAA B AA          ","         F                 ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","           D G D           ","            GGG            ","            GGG            ","             G             ","                           ","                           "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","        F                  ","       EBA   B             ","        F                  ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","           D   D           ","             G             ","            GGG            ","            GGG            ","             G             ","                           "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","        F                  ","       EBA                 ","        F                  ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","           D   D           ","             G             ","            GGG            ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","            FFF            ","          FF   FF          ","                 F         ","                  F        ","                  F        ","       F           F       ","     GEBA          FEG     ","       F           F       ","                  F        ","                  F        ","                 F         ","          FF   FF          ","            FFF            ","                           ","                           ","                           ","           D   D           ","                           ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","           D   D           ","                           ","                           ","            EEE            ","          EEBBBEE          ","         EBBABABBE         ","          AA B AABE        ","             B   ABE       ","                 ABE       ","     GEF          ABEG     ","     GBBBBB  H  BBBBBG     ","     GEF          ABEG     ","                 ABE       ","             B   ABE       ","          AA B AABE        ","         EBBABABBE         ","          EEBBBEE          ","            EEE            ","                           ","                           ","           D   D           ","                           ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","           D   D           ","                           ","                           ","                           ","            FFF            ","          FF   FF          ","                 F         ","                  F        ","                  F        ","       F           F       ","     GEBA          FEG     ","       F           F       ","                  F        ","                  F        ","                 F         ","          FF   FF          ","            FFF            ","                           ","                           ","                           ","                           ","                           ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","           D   D           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","     J  F            J     ","     G EBA           G     ","     J  F            J     ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","                           ","           D   D           ","                           ","                           ","                           ","                           ","                           ","                           ","     J               J     ","     G  F            G     ","     G EBA   B       G     ","     G  F            G     ","     J               J     ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","                           ","           D   D           ","                           ","                           ","                           ","                           ","                           ","     J               J     ","     G               G     ","         F                 ","     G  EBAA B AA    G     ","         F                 ","     G               G     ","     J               J     ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","                           ","                           ","           D   D           ","                           ","                           ","                           ","     J               J     ","     G               G     ","                           ","          FF   FF          ","     G   EBBABABBE   G     ","          FF   FF          ","                           ","     G               G     ","     J               J     ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","                           ","                           ","           D   D           ","                           ","             H             ","     J               J     ","     G               G     ","                           ","                           ","            FFF            ","     G    EEBBBEE    G     ","            FFF            ","                           ","                           ","     G               G     ","     J               J     ","                           ","                           ","                           ","                           ","                           ","                           ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","                           ","                           ","                           ","           DGHGD           ","     J      GGG      J     ","     G               G     ","                           ","                           ","                           ","                           ","     G      EEE      G     ","                           ","                           ","                           ","                           ","     G               G     ","     J               J     ","                           ","                           ","                           ","                           ","                           ","            JGJ            ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","                           ","                           ","            GHG            ","     J      JGJ      J     ","     G     D   D     G     ","           D   D           ","                           ","                           ","                           ","                           ","    JG               GJ    ","                           ","                           ","                           ","                           ","                           ","     G               G     ","     J               J     ","                           ","                           ","                           ","            J J            ","             G             ","            GGG            ","             G             "},
                            {"                           ","                           ","                           ","                           ","            GCG            ","     J       G       J     ","     G      J J      G     ","                           ","                           ","           D   D           ","           D   D           ","                           ","                           ","   JGGJ             JGGJ   ","                           ","                           ","                           ","                           ","                           ","                           ","     G               G     ","     J               J     ","                           ","            J J            ","                           ","             G             ","            GGG            ","             G             "},
                            {"            III            ","            GCG            ","            GCG            ","            GCG            ","     J      JGJ      J     ","     G               G     ","            J J            ","                           ","                           ","                           ","                           ","           D   D           ","           D   D           ","  JG GGJ   D   D   JGG GJ  ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","     G               G     ","     J      J J      J     ","                           ","             G             ","             G             ","            GGG            ","             G             "},
                            {"            I~I            ","             G             ","            JGJ            ","     J       G       J     ","     G      J J      G     ","                           ","            J J            ","                           ","                           ","                           ","                           ","                           ","                           "," JG  G GJ         JG G  GJ ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","            J J            ","     G               G     ","     J       G       J     ","             G             ","             G             ","            GGG            ","             G             "},
                            {"            III            ","             G             ","     G      J J      G     ","     G               G     ","     G      J J      G     ","     G               G     ","     G      J J      G     ","     G               G     ","     G               G     ","     G               G     ","     G               G     ","     G               G     ","     G               G     ","GGGGGGGGGGGGGGGGGGGGGGGGGGG","     G               G     ","     G               G     ","     G               G     ","     G               G     ","     G               G     ","     G               G     ","     G      GGG      G     ","     G      GGG      G     ","     G      GGG      G     ","     G      GGG      G     ","     G      GGG      G     ","            GGG            ","            GGG            ","            GGG            "}
                        }))
                    .addElement('A', BorosilicateGlass.ofBoroGlassAnyTier())
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
                                (t, meta) -> t.tierComponentCasing = meta,
                                t -> t.tierComponentCasing))
                    )
                    .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 5))
                    .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 8))
                    .addElement('E', ofBlock(GregTechAPI.sBlockCasings2, 9))
                    .addElement('F', ofBlock(GregTechAPI.sBlockCasings9, 1))
                    .addElement('G', ofBlock(sBlockCasingsTT, 4))
                    .addElement('H', ofBlock(sBlockCasingsTT, 8))
                    .addElement(
                        'I',
                        HatchElementBuilder
                            .<TST_Disassembler>builder()
                            .atLeast(InputBus, OutputBus, OutputHatch)
                            .adder(TST_Disassembler::addToMachineList)
                            .dot(1)
                            .casingIndex(1028)
                            .buildAndChain(sBlockCasingsTT, 4)
                    )
                    .addElement('J', ofFrame(Materials.CosmicNeutronium))
                    /*
                    A -> ofBlock...(BW_GlasBlocks, 0, ...); // any glass
                    B -> ofBlock...(componentAssemblyLineCasing, 13, ...); // tiered component casing
                    C -> ofBlock...(gt.blockcasings2, 5, ...);
                    D -> ofBlock...(gt.blockcasings2, 8, ...);
                    E -> ofBlock...(gt.blockcasings2, 9, ...);
                    F -> ofBlock...(gt.blockcasings9, 1, ...);
                    G -> ofBlock...(gt.blockcasingsTT, 4, ...);
                    H -> ofBlock...(gt.blockcasingsTT, 8, ...);
                    I -> ofBlock...(gt.blockcasingsTT, 4, ...); // hatches
                    J -> ofFrame...(Materials.CosmicNeutron);
                     */
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        tierComponentCasing = -2;
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    // spotless:on
    // endregion

    // region Processing Logic
    public int tierComponentCasing = -2;
    private static Set<GTRecipe> allRecipes = null;

    public Set<GTRecipe> getAllRecipes() {
        if (allRecipes == null) {
            allRecipes = new HashSet<>();
            allRecipes.addAll(GoodGeneratorRecipeMaps.componentAssemblyLineRecipes.getAllRecipes());
            allRecipes.addAll(GTCMRecipe.MiracleTopRecipes.getAllRecipes());
            allRecipes.addAll(RecipeMaps.assemblylineVisualRecipes.getAllRecipes());
            allRecipes.addAll(RecipeMaps.assemblerRecipes.getAllRecipes());
            allRecipes.addAll(GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipes.getAllRecipes());
        }
        return allRecipes;
    }

    private byte getRealTierComponentCasing() {
        return (byte) (tierComponentCasing + 1);
    }

    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + TextEnums.tr("MachineInfoData.ComponentBlockTier")
            + ": "
            + EnumChatFormatting.GOLD
            + getRealTierComponentCasing();

        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("tierComponentCasing", tierComponentCasing);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tierComponentCasing = aNBT.getInteger("tierComponentCasing");
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        List<ItemStack> inputItems = getStoredInputsNoSeparation();
        if (inputItems.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        final byte tier = getRealTierComponentCasing();
        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();
        long processed = 0;

        for (ItemStack items : inputItems) {
            if (items.stackSize <= 0) continue;

            TST_SimpleDisassemblyRecipe disassemblyRecipe = TST_DisassemblerRecipeHandler.DisassemblerRecipeMap
                .get(TST_ItemID.createNoNBT(items));

            if (disassemblyRecipe == null) continue;

            if (tier < 14) {
                byte recipeTier = disassemblyRecipe.getTier();
                if (recipeTier > tier + 1) {
                    if (processed == 0) {
                        return CheckRecipeResultRegistry.insufficientMachineTier(recipeTier - 1);
                    } else continue;
                }
            }

            int recipeNeed = disassemblyRecipe.getItemAmount();
            if (items.stackSize < recipeNeed) continue;

            // Found the recipe
            int outputMultiplier = items.stackSize / recipeNeed;
            items.stackSize -= outputMultiplier * recipeNeed;

            // Handle progress time cost
            processed += outputMultiplier;

            if (disassemblyRecipe.hasOutputItems()) {
                outputItems.addAll(disassemblyRecipe.getOutputItems(outputMultiplier));
            }

            if (disassemblyRecipe.hasOutputFluids()) {
                outputFluids.addAll(disassemblyRecipe.getOutputFluids(outputMultiplier));
            }

        }

        if (processed == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        long tempProgressTime = Math.max(1, processed / (4L * tier)) * CostTicksPerItemDisassembling_Disassembler;
        mMaxProgresstime = tempProgressTime > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) tempProgressTime;

        if (!outputItems.isEmpty()) {
            mOutputItems = outputItems.toArray(new ItemStack[0]);
        }

        if (!outputFluids.isEmpty()) {
            mOutputFluids = outputFluids.toArray(new FluidStack[0]);
        }

        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        TST_DisassemblerRecipeHandler.initDisassemblerRecipes();
    }

    @NotNull
    // @Override
    public CheckRecipeResult checkProcessingOld() {
        ArrayList<ItemStack> inputItems = getStoredInputs();
        if (inputItems.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        // TODO Optimize the recipe finding algorithm
        final byte tier = getRealTierComponentCasing();
        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();
        long processed = 0;
        base: for (ItemStack items : inputItems) {
            if (items.stackSize <= 0) continue;
            for (GTRecipe recipe : getAllRecipes()) {
                // Item things are not correct state.
                if (items.stackSize <= 0) continue base;
                // Recipe is not just output 1 item.
                if (recipe.mInputs == null || recipe.mOutputs.length > 1
                    || (recipe.mFluidOutputs != null && recipe.mFluidOutputs.length > 0)) continue;
                // Not this recipe.
                if (!Utils.metaItemEqual(items, recipe.mOutputs[0])) continue;
                // Amount is not enough.
                if (items.stackSize < recipe.mOutputs[0].stackSize) continue base;
                // Tier block limit.
                if (tier < 14) {
                    byte recipeTier = GTUtility.getTier(recipe.mEUt);
                    if (recipeTier > tier + 1) {
                        if (processed == 0) {
                            return CheckRecipeResultRegistry.insufficientMachineTier(recipeTier - 1);
                        }
                        continue base;
                    }
                }

                // Found the recipe
                int outputMultiplier = items.stackSize / recipe.mOutputs[0].stackSize;
                items.stackSize -= outputMultiplier * recipe.mOutputs[0].stackSize;

                // Handle progress time cost
                processed += outputMultiplier;

                // Handle output items
                if (recipe.mInputs != null) {
                    for (ItemStack outs : recipe.mInputs) {
                        if (outs.stackSize <= 0) continue;
                        long amount = (long) outs.stackSize * outputMultiplier;
                        if (amount <= Integer.MAX_VALUE) {
                            outputItems.add(Utils.setStackSize(outs.copy(), (int) amount));
                        } else {
                            while (amount > 0) {
                                if (amount > Integer.MAX_VALUE) {
                                    outputItems.add(Utils.setStackSize(outs.copy(), Integer.MAX_VALUE));
                                    amount -= Integer.MAX_VALUE;
                                } else {
                                    outputItems.add(Utils.setStackSize(outs.copy(), (int) amount));
                                    amount = 0;
                                }
                            }
                        }
                    }
                }

                // Handle output fluids
                if (recipe.mFluidInputs != null) {
                    for (FluidStack outs : recipe.mFluidInputs) {
                        if (outs.amount <= 0) continue;
                        long amount = (long) outs.amount * outputMultiplier;
                        if (amount <= Integer.MAX_VALUE) {
                            outputFluids.add(Utils.setStackSize(outs.copy(), (int) amount));
                        } else {
                            while (amount > 0) {
                                if (amount > Integer.MAX_VALUE) {
                                    outputFluids.add(Utils.setStackSize(outs.copy(), Integer.MAX_VALUE));
                                    amount -= Integer.MAX_VALUE;
                                } else {
                                    outputFluids.add(Utils.setStackSize(outs.copy(), (int) amount));
                                    amount = 0;
                                }
                            }
                        }
                    }
                }
            }

            // check crafting table recipes here
            // or just ignore crafting recipes

        }

        if (processed == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        long tempProgressTime = Math.max(1, processed / (4L * tier)) * CostTicksPerItemDisassembling_Disassembler;
        mMaxProgresstime = tempProgressTime > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) tempProgressTime;

        if (!outputItems.isEmpty()) {
            mOutputItems = outputItems.toArray(new ItemStack[0]);
        }

        if (!outputFluids.isEmpty()) {
            mOutputFluids = outputFluids.toArray(new FluidStack[0]);
        }

        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;

    }

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
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    // endregion

    // region General
    private static MultiblockTooltipBuilder tooltip;

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        if (tooltip == null) {
            // spotless:off
            tooltip = new MultiblockTooltipBuilder();
            // #tr Tooltip_TSTDisassembler_MachineType
            // # Disassembler
            // #zh_CN 拆解机
            tooltip.addMachineType(TextEnums.tr("Tooltip_TSTDisassembler_MachineType"))
                // #tr Tooltip_TSTDisassembler_Controller
                // # Controller block for the TST Disassembler
                // #zh_CN TST大型拆解机的控制器方块
                .addInfo(TextEnums.tr("Tooltip_TSTDisassembler_Controller"))
                // #tr Tooltip_TSTDisassembler_01
                // # {\BLUE}The incomplete becomes complete; the crooked becomes straight; the empty becomes full; the worn out becomes new.
                // #zh_CN {\BLUE}曲则全，枉则直，洼则盈，敝则新。
                .addInfo(TextEnums.tr("Tooltip_TSTDisassembler_01"))
                // #tr Tooltip_TSTDisassembler_02
                // # How your other machine assembles the item, this one disassembles it back.
                // #zh_CN 你的其他机器怎样组装物品, 这个机器就怎样把它拆回去.
                .addInfo(TextEnums.tr("Tooltip_TSTDisassembler_02"))
                // #tr Tooltip_TSTDisassembler_03
                // # Note: The component assembly line casing level limit recipe the machine can perform.
                // #zh_CN 注意: 结构中的部件装配线外壳等级限制可拆卸的配方等级.
                .addInfo(TextEnums.tr("Tooltip_TSTDisassembler_03"))
                // #tr Tooltip_TSTDisassembler_04
                // # No energy consumption.
                // #zh_CN 不消耗能源.
                .addInfo(TextEnums.tr("Tooltip_TSTDisassembler_04"))
                .addSeparator()
                .addInfo(TextLocalization.StructureTooComplex)
                .addInfo(TextLocalization.BLUE_PRINT_INFO)
                .addStructureInfo(TextLocalization.Tooltip_Details)
                // #tr Tooltip_TSTDisassembler_2_01
                // # Supported:
                // #zh_CN 支持:
                .addStructureInfo(TextEnums.tr("Tooltip_TSTDisassembler_2_01"))
                // #tr Tooltip_TSTDisassembler_2_02
                // # {\SPACE}- {\AQUA}Component Assembly Line Recipes
                // #zh_CN {\SPACE}- {\AQUA}部件装配线配方
                .addStructureInfo(TextEnums.tr("Tooltip_TSTDisassembler_2_02"))
                // #tr Tooltip_TSTDisassembler_2_03
                // # {\SPACE}- {\AQUA}Miracle Top Recipes
                // #zh_CN {\SPACE}- {\AQUA}奇迹顶点配方
                .addStructureInfo(TextEnums.tr("Tooltip_TSTDisassembler_2_03"))
                // #tr Tooltip_TSTDisassembler_2_04
                // # {\SPACE}- {\AQUA}Assembly Line Recipes
                // #zh_CN {\SPACE}- {\AQUA}装配线配方
                .addStructureInfo(TextEnums.tr("Tooltip_TSTDisassembler_2_04"))
                // #tr Tooltip_TSTDisassembler_2_05
                // # {\SPACE}- {\AQUA}Assembler Recipes
                // #zh_CN {\SPACE}- {\AQUA}组装机配方
                .addStructureInfo(TextEnums.tr("Tooltip_TSTDisassembler_2_05"))
                // #tr Tooltip_TSTDisassembler_2_06
                // # {\SPACE}- {\AQUA}Photon Controller Recipes
                // #zh_CN {\SPACE}- {\AQUA}光子掌控者配方
                .addStructureInfo(TextEnums.tr("Tooltip_TSTDisassembler_2_06"))
                // #tr Tooltip_TSTDisassembler_2_07
                // # {\RED}NOT SUPPORTED CRAFTING TABLE RECIPES !
                // #zh_CN {\RED}不兼容工作台配方!
                .addStructureInfo(TextEnums.tr("Tooltip_TSTDisassembler_2_07"))
                .beginStructureBlock(27, 23, 28, false)
                .addInputBus(TextLocalization.textUseBlueprint, 1)
                .addOutputBus(TextLocalization.textUseBlueprint, 1)
                .addOutputHatch(TextLocalization.textUseBlueprint, 1)
                .toolTipFinisher(TextLocalization.ModName);
            // spotless:on
        }
        return tooltip;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {

            if (aActive) {
                return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_ON)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }

        return new ITexture[] { casingTexturePages[0][12] };
    }
}
