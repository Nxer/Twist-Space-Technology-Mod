package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.Multiplier_ExtraOutputsPerFieldTier_SpaceScaler;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedMultiplier_BeyondTier2Block_SpaceScaler;
import static com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum.SpeedMultiplier_Tier1Block_SpaceScaler;
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
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static tectech.thing.casing.TTCasingsContainer.StabilisationFieldGenerators;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import tectech.thing.block.BlockQuantumGlass;

public class GT_TileEntity_SpaceScaler extends GTCM_MultiMachineBase<GT_TileEntity_SpaceScaler>
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_SpaceScaler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_SpaceScaler(String aName) {
        super(aName);
    }
    // endregion

    // region Processing Logic
    private int fieldGeneratorTier = 0;
    private int multiplier = 1;

    @Override
    public int totalMachineMode() {
        /*
         * 0 - Compressor
         * 1 - Extractor
         * 2 - Particle Collider
         * 3 - Electric Implosion Compressor
         * 4 - Neutronium Compressor
         */
        return 5;
    }

    @Override
    public int nextMachineMode() {
        if (fieldGeneratorTier >= 11) {
            return super.nextMachineMode();
        } else if (fieldGeneratorTier >= 3) {
            return super.nextMachineMode() % 3;
        } else {
            return super.nextMachineMode() % 2;
        }
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_COMPRESSING);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SEPARATOR);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SINGULARITY);
    }

    @Override
    public String getMachineModeName(int mode) {
        return StatCollector.translateToLocal("SpaceScaler.modeMsg." + mode);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setByte("mode", (byte) machineMode);
        aNBT.setInteger("fieldGeneratorTier", fieldGeneratorTier);
        aNBT.setInteger("multiplier", multiplier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        machineMode = aNBT.getByte("mode");
        fieldGeneratorTier = aNBT.getInteger("fieldGeneratorTier");
        multiplier = aNBT.getInteger("multiplier");
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        if (fieldGeneratorTier < 3 && machineMode > 1) return CheckRecipeResultRegistry.INTERNAL_ERROR;
        if (fieldGeneratorTier < 11 && machineMode > 2) return CheckRecipeResultRegistry.INTERNAL_ERROR;

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

        // if in this state , no extra settings is in need.
        if (fieldGeneratorTier < 4) {
            mOutputItems = processingLogic.getOutputItems();
            mOutputFluids = processingLogic.getOutputFluids();
            return result;
        }

        ItemStack[] outputItemStack = processingLogic.getOutputItems();
        FluidStack[] outputFluidStack = processingLogic.getOutputFluids();

        if (machineMode != 2) {
            // compressor mode and extractor mode
            mOutputItems = outputItemStack;
            mOutputFluids = outputFluidStack;
        } else {
            // check in particle mode
            if (fieldGeneratorTier < 3) return CheckRecipeResultRegistry.INTERNAL_ERROR;

            // process Items
            List<ItemStack> extraItems = new ArrayList<>();
            for (ItemStack items : outputItemStack) {
                if (items.stackSize <= Integer.MAX_VALUE / multiplier) {
                    // set amount directly if in integer area
                    items.stackSize *= multiplier;
                } else {
                    for (int i = 0; i < multiplier - 1; i++) {
                        extraItems.add(items.copy());
                    }
                }
            }

            if (extraItems.isEmpty()) {
                // no over integer amount
                mOutputItems = outputItemStack;
            } else {
                extraItems.addAll(Arrays.asList(outputItemStack));
                mOutputItems = extraItems.toArray(new ItemStack[] {});
            }

            // process Fluids
            List<FluidStack> extraFluids = new ArrayList<>();
            for (FluidStack fluids : outputFluidStack) {
                if (fluids.amount <= Integer.MAX_VALUE / multiplier) {
                    fluids.amount *= multiplier;
                } else {
                    for (int i = 0; i < multiplier - 1; i++) {
                        extraFluids.add(fluids.copy());
                    }
                }
            }

            if (extraFluids.isEmpty()) {
                mOutputFluids = outputFluidStack;
            } else {
                extraFluids.addAll(Arrays.asList(outputFluidStack));
                mOutputFluids = extraFluids.toArray(new FluidStack[] {});
            }

        }

        return result;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {

        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    fieldGeneratorTier >= 2 ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {

                if (fieldGeneratorTier >= 11 || machineMode > 0) return super.validateRecipe(recipe);

                int recipeReq = 1 + recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0);
                if (recipeReq > fieldGeneratorTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipeReq);
                }
                return super.validateRecipe(recipe);
            }

        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    public float getSpeedBonus() {
        return 1F / (this.fieldGeneratorTier < 2 ? SpeedMultiplier_Tier1Block_SpaceScaler
            : SpeedMultiplier_BeyondTier2Block_SpaceScaler);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (machineMode) {
            case 1:
                return RecipeMaps.extractorRecipes;
            case 2:
                return GTPPRecipeMaps.cyclotronRecipes;
            case 3:
                return BartWorksRecipeMaps.electricImplosionCompressorRecipes;
            case 4:
                return RecipeMaps.neutroniumCompressorRecipes;
            default:
                return RecipeMaps.compressorRecipes;
        }
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(
            RecipeMaps.extractorRecipes,
            GTPPRecipeMaps.cyclotronRecipes,
            RecipeMaps.compressorRecipes,
            BartWorksRecipeMaps.electricImplosionCompressorRecipes,
            RecipeMaps.neutroniumCompressorRecipes);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.fieldGeneratorTier = 0;
        boolean sign = checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
        if (this.fieldGeneratorTier < 1) {
            return false;
        }
        multiplier = 1 + Multiplier_ExtraOutputsPerFieldTier_SpaceScaler * Math.max(0, fieldGeneratorTier - 3);
        return sign;
    }

    // endregion

    // region Structure
    // spotless:off
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
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

    private final String STRUCTURE_PIECE_MAIN = "mainSpaceScaler";
    private final int horizontalOffSet = 15;
    private final int verticalOffSet = 15;
    private final int depthOffSet = 0;
    private static IStructureDefinition<GT_TileEntity_SpaceScaler> STRUCTURE_DEFINITION = null;
    public static int getBlockFieldGeneratorTier(Block block, int meta){
        if (block == sBlockCasingsTT){
            switch (meta) {
                case 6:
                    return 1;
                case 14:
                    return 2;
                default:
                    return 0;
            }
        }
        if (block == StabilisationFieldGenerators){
            return meta + 3;
        }
        return 0;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_SpaceScaler> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_SpaceScaler>builder()
                               .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                               .addElement(
                                   'A',
                                   HatchElementBuilder.<GT_TileEntity_SpaceScaler>builder()
                                                         .atLeast(Energy.or(ExoticEnergy))
                                                         .adder(GT_TileEntity_SpaceScaler::addToMachineList)
                                                         .dot(1)
                                                         .casingIndex(1024)
                                                         .buildAndChain(sBlockCasingsTT, 0))
                               .addElement('B', ofBlock(sBlockCasingsTT, 4))
                               .addElement('C', ofBlock(sBlockCasingsTT, 6))
                               .addElement('D', ofBlock(BlockQuantumGlass.INSTANCE, 0))
                               .addElement(
                                   'E',
                                   HatchElementBuilder.<GT_TileEntity_SpaceScaler>builder()
                                                         .atLeast(InputBus, InputHatch, OutputBus, OutputHatch)
                                                         .adder(GT_TileEntity_SpaceScaler::addToMachineList)
                                                         .dot(2)
                                                         .casingIndex(1028)
                                                         .buildAndChain(sBlockCasingsTT, 4))
                               .addElement(
                                   'G',
                                   withChannel("fieldgeneratortier",
                                               ofBlocksTiered(
                                                   GT_TileEntity_SpaceScaler::getBlockFieldGeneratorTier,
                                                   ImmutableList.of(
                                                       Pair.of(sBlockCasingsTT, 6),
                                                       Pair.of(sBlockCasingsTT, 14),
                                                       Pair.of(StabilisationFieldGenerators, 0),
                                                       Pair.of(StabilisationFieldGenerators, 1),
                                                       Pair.of(StabilisationFieldGenerators, 2),
                                                       Pair.of(StabilisationFieldGenerators, 3),
                                                       Pair.of(StabilisationFieldGenerators, 4),
                                                       Pair.of(StabilisationFieldGenerators, 5),
                                                       Pair.of(StabilisationFieldGenerators, 6),
                                                       Pair.of(StabilisationFieldGenerators, 7),
                                                       Pair.of(StabilisationFieldGenerators, 8)
                                                   ),
                                                   0,
                                                   (m, t) -> m.fieldGeneratorTier = t,
                                                   m -> m.fieldGeneratorTier))
                               )
                               .build();
        }
        return STRUCTURE_DEFINITION;
    }
    /*
     * Blocks:
     * A -> ofBlock...(gt.blockcasingsTT, 0, ...); // Energy
     * B -> ofBlock...(gt.blockcasingsTT, 4, ...);
     * C -> ofBlock...(gt.blockcasingsTT, 6, ...); // Field generator NORMAL
     * D -> ofBlock...(tile.quantumGlass, 0, ...);
     * E -> ofBlock...(gt.blockcasingsTT, 4, ...); // Hatches
     * G -> ofBlock...(gt.blockcasingsTT, 6, ...); // Field generator can upgrade
     */

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    private final String[][] shapeMain = new String[][] {
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "              AAA              ", "            AAEEEAA            ", "           AEEEEEEEA           ", "           AEECCCEEA           ", "          AEECEEECEEA          ", "          AEECEEECEEA          ", "          AEECEEECEEA          ", "           AEECCCEEA           ", "           AEEEEEEEA           ", "            AAEEEAA            ", "              AAA              ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "               A               ", "               A               ", "              AAA              ", "            AAAAAAA            ", "              AAA              ", "               A               ", "               A               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "            BBBBBBB            ", "          BB       BB          ", "         B           B         ", "        B             B        ", "       B               B       ", "       B               B       ", "      B                 B      ", "      B                 B      ", "      B        B        B      ", "      B       BGB       B      ", "      B        B        B      ", "      B                 B      ", "      B                 B      ", "       B               B       ", "       B               B       ", "        B             B        ", "         B           B         ", "          BB       BB          ", "            BBBBBBB            ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "            BBBBBBB            ", "          BB       BB          ", "         B           B         ", "        B             B        ", "       B               B       ", "      B                 B      ", "      B                 B      ", "     B                   B     ", "     B                   B     ", "     B                   B     ", "     B         G         B     ", "     B                   B     ", "     B                   B     ", "     B                   B     ", "      B                 B      ", "      B                 B      ", "       B               B       ", "        B             B        ", "         B           B         ", "          BB       BB          ", "            BBBBBBB            ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "               G               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "            BBBBBBB            ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "   B                       B   ", "   B                       B   ", "   B                       B   ", "   B           G           B   ", "   B                       B   ", "   B                       B   ", "   B                       B   ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "            BBBBBBB            ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "            BBBBBBB            ", "          BB       BB          ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "   B                       B   ", "   B                       B   ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "   B                       B   ", "   B                       B   ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "          BB       BB          ", "            BBBBBBB            ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "          BB       BB          ", "         B           B         ", "                               ", "                               ", "                               ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "                               ", "                               ", "                               ", "         B           B         ", "          BB       BB          ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "         B           B         ", "        B             B        ", "                               ", "                               ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "                               ", "                               ", "        B             B        ", "         B           B         ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "        B             B        ", "       B               B       ", "                               ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "                               ", "       B               B       ", "        B             B        ", "                               ", "                               " },
        { "                               ", "              AAA              ", "                               ", "       B               B       ", "      B                 B      ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "A                             A", "A                             A", "A                             A", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "      B                 B      ", "       B               B       ", "                               ", "              AAA              " },
        { "                               ", "            AAEEEAA            ", "                               ", "       B               B       ", "      B                 B      ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "A                             A", "A                             A", "E                             E", "E                             E", "E                             E", "A                             A", "A                             A", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "      B                 B      ", "       B               B       ", "                               ", "            AAEEEAA            " },
        { "                               ", "           AEEEEEEEA           ", "               A               ", "      B                 B      ", "     B                   B     ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "A                             A", "E                             E", "E                             E", "E                             E", "EA                           AE", "E                             E", "E                             E", "E                             E", "A                             A", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "     B                   B     ", "      B                 B      ", "               A               ", "           AEEEEEEEA           " },
        { "              AAA              ", "           AEECCCEEA           ", "               A               ", "      B                 B      ", "     B                   B     ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "A                             A", "E                             E", "E                             E", "C              D              C", "CA            DDD            AC", "C              D              C", "E                             E", "E                             E", "A                             A", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "     B                   B     ", "      B                 B      ", "               A               ", "           AEECCCEEA           " },
        { "             AEEEA             ", "          AEECEEECEEA          ", "              AAA              ", "      B        B        B      ", "     B                   B     ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "A                             A", "E                             E", "E                             E", "C              D              C", "EA            D D            AE", "EAB          D   D          BAE", "EA            D D            AE", "C              D              C", "E                             E", "E                             E", "A                             A", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "     B                   B     ", "      B        B        B      ", "              AAA              ", "          AEECEEECEEA          " },
        { "             AE~EA             ", "          AEECEEECEEA          ", "            AAAAAAA            ", "      B       BGB       B      ", "     B         G         B     ", "               G               ", "   B           G           B   ", "  B                         B  ", "                               ", "                               ", "                               ", "A                             A", "E                             E", "EA                           AE", "CA            DDD            AC", "EAB          D   D          BAE", "EAGGGG       D   D       GGGGAE", "EAB          D   D          BAE", "CA            DDD            AC", "EA                           AE", "E                             E", "A                             A", "                               ", "                               ", "                               ", "  B                         B  ", "   B           G           B   ", "               G               ", "     B         G         B     ", "      B       BGB       B      ", "            AAAAAAA            ", "          AEECEEECEEA          " },
        { "             AEEEA             ", "          AEECEEECEEA          ", "              AAA              ", "      B        B        B      ", "     B                   B     ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "A                             A", "E                             E", "E                             E", "C              D              C", "EA            D D            AE", "EAB          D   D          BAE", "EA            D D            AE", "C              D              C", "E                             E", "E                             E", "A                             A", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "     B                   B     ", "      B        B        B      ", "              AAA              ", "          AEECEEECEEA          " },
        { "              AAA              ", "           AEECCCEEA           ", "               A               ", "      B                 B      ", "     B                   B     ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "A                             A", "E                             E", "E                             E", "C              D              C", "CA            DDD            AC", "C              D              C", "E                             E", "E                             E", "A                             A", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "     B                   B     ", "      B                 B      ", "               A               ", "           AEECCCEEA           " },
        { "                               ", "           AEEEEEEEA           ", "               A               ", "      B                 B      ", "     B                   B     ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "A                             A", "E                             E", "E                             E", "E                             E", "EA                           AE", "E                             E", "E                             E", "E                             E", "A                             A", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "     B                   B     ", "      B                 B      ", "               A               ", "           AEEEEEEEA           " },
        { "                               ", "            AAEEEAA            ", "                               ", "       B               B       ", "      B                 B      ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "A                             A", "A                             A", "E                             E", "E                             E", "E                             E", "A                             A", "A                             A", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "      B                 B      ", "       B               B       ", "                               ", "            AAEEEAA            " },
        { "                               ", "              AAA              ", "                               ", "       B               B       ", "      B                 B      ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "A                             A", "A                             A", "A                             A", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "      B                 B      ", "       B               B       ", "                               ", "              AAA              " },
        { "                               ", "                               ", "                               ", "        B             B        ", "       B               B       ", "                               ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "                               ", "       B               B       ", "        B             B        ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "         B           B         ", "        B             B        ", "                               ", "                               ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "                               ", "                               ", "        B             B        ", "         B           B         ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "          BB       BB          ", "         B           B         ", "                               ", "                               ", "                               ", "                               ", "                               ", "   B                       B   ", "  B                         B  ", "  B                         B  ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "  B                         B  ", "  B                         B  ", "   B                       B   ", "                               ", "                               ", "                               ", "                               ", "                               ", "         B           B         ", "          BB       BB          ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "            BBBBBBB            ", "          BB       BB          ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "   B                       B   ", "   B                       B   ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "  B                         B  ", "   B                       B   ", "   B                       B   ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "          BB       BB          ", "            BBBBBBB            ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "            BBBBBBB            ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "   B                       B   ", "   B                       B   ", "   B                       B   ", "   B           G           B   ", "   B                       B   ", "   B                       B   ", "   B                       B   ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "            BBBBBBB            ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "               G               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "            BBBBBBB            ", "          BB       BB          ", "         B           B         ", "        B             B        ", "       B               B       ", "      B                 B      ", "      B                 B      ", "     B                   B     ", "     B                   B     ", "     B                   B     ", "     B         G         B     ", "     B                   B     ", "     B                   B     ", "     B                   B     ", "      B                 B      ", "      B                 B      ", "       B               B       ", "        B             B        ", "         B           B         ", "          BB       BB          ", "            BBBBBBB            ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "            BBBBBBB            ", "          BB       BB          ", "         B           B         ", "        B             B        ", "       B               B       ", "       B               B       ", "      B                 B      ", "      B                 B      ", "      B        B        B      ", "      B       BGB       B      ", "      B        B        B      ", "      B                 B      ", "      B                 B      ", "       B               B       ", "       B               B       ", "        B             B        ", "         B           B         ", "          BB       BB          ", "            BBBBBBB            ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "               A               ", "               A               ", "              AAA              ", "            AAAAAAA            ", "              AAA              ", "               A               ", "               A               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               " },
        { "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "              AAA              ", "            AAEEEAA            ", "           AEEEEEEEA           ", "           AEECCCEEA           ", "          AEECEEECEEA          ", "          AEECEEECEEA          ", "          AEECEEECEEA          ", "           AEECCCEEA           ", "           AEEEEEEEA           ", "            AAEEEAA            ", "              AAA              ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               ", "                               " } };


    // spotless:on
    // endregion

    // region Overrides

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        // spotless:off
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_SpaceScaler_MachineType)
            .addInfo(TextLocalization.Tooltip_SpaceScaler_00)
            .addInfo(TextLocalization.Tooltip_SpaceScaler_01)
            .addInfo(TextLocalization.Tooltip_SpaceScaler_02)
            .addInfo(TextLocalization.Tooltip_SpaceScaler_03)
            .addInfo(TextLocalization.Tooltip_SpaceScaler_04)
            .addInfo(TextLocalization.Tooltip_SpaceScaler_05)
            .addInfo(TextLocalization.Tooltip_SpaceScaler_06)
            // #tr Tooltip_SpaceScaler_07
            // # Gallifreyan Stabilisation Field Generator block enables Electric Implosion Compressor & Neutronium Compressor mode.
            // #zh_CN 鸿蒙稳定力场发生器等级允许使用电动聚爆压缩机模式和中子态素压缩机模式.
            .addInfo(TextEnums.tr("Tooltip_SpaceScaler_07"))
            .addInfo(TextLocalization.Tooltip_SpaceScaler_08)
            // #tr Tooltip_SpaceScaler_09
            // # T2 block unlock HIP Unit limitation, T3 block unlock Stabilized Black Hole limitation.
            // #zh_CN 2级方块解锁HIP单元限制, 3级解锁稳定黑洞限制.
            .addInfo(TextEnums.tr("Tooltip_SpaceScaler_09"))
            .addInfo(TextLocalization.textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(31, 31, 32, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 1)
            .addOutputBus(TextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 2)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
        // spotless:on
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Mode: " + EnumChatFormatting.GOLD + machineMode;
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "fieldGeneratorTier: "
            + EnumChatFormatting.GOLD
            + this.fieldGeneratorTier;
        return ret;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_SpaceScaler(this.mName);
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
    // endregion
}
