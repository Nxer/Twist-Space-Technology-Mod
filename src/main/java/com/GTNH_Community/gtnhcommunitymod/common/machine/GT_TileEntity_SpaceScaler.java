package com.GTNH_Community.gtnhcommunitymod.common.machine;

import static com.GTNH_Community.gtnhcommunitymod.common.machine.ValueEnum.MAX_PARALLEL_LIMIT;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.BLUE_PRINT_INFO;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.ModName;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.StructureTooComplex;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_SpaceScaler_00;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_SpaceScaler_01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_SpaceScaler_02;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_SpaceScaler_03;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_SpaceScaler_04;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_SpaceScaler_05;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_SpaceScaler_06;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_SpaceScaler_MachineType;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textScrewdriverChangeMode;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textUseBlueprint;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.StabilisationFieldGenerators;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.GTNH_Community.gtnhcommunitymod.DistortionSpaceTechnology;
import com.GTNH_Community.gtnhcommunitymod.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_TileEntity_SpaceScaler extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_SpaceScaler>
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
    private byte mode = 0;
    private int fieldGeneratorTier = 0;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        /*
         * ProcessingLogic normalProcessingLogic = new GTCM_ProcessingLogic() {
         * @NotNull
         * @Override
         * public CheckRecipeResult process() {
         * setSpeedBonus(getSpeedBonus());
         * setOverclock(fieldGeneratorTier > 1 ? 2 : 1, 2);
         * return super.process();
         * }
         * }.setMaxParallelSupplier(this::getMaxParallelRecipes);
         */

        ProcessingLogic ParticalProcessingLogic = new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclock(fieldGeneratorTier > 1 ? 2 : 1, 2);
                return super.process();
            }

            protected CheckRecipeResult applyRecipe(@NotNull GT_Recipe recipe, GT_ParallelHelper helper,
                GT_OverclockCalculator calculator, CheckRecipeResult result) {
                if (!helper.getResult()
                    .wasSuccessful()) {
                    return helper.getResult();
                }

                if (recipe.mCanBeBuffered) {
                    lastRecipe = recipe;
                } else {
                    lastRecipe = null;
                }
                calculatedParallels = helper.getCurrentParallel();

                if (calculator.getConsumption() == Long.MAX_VALUE) {
                    return CheckRecipeResultRegistry.POWER_OVERFLOW;
                }
                if (calculator.getDuration() == Integer.MAX_VALUE) {
                    return CheckRecipeResultRegistry.DURATION_OVERFLOW;
                }

                calculatedEut = calculator.getConsumption();

                double finalDuration = calculateDuration(recipe, helper, calculator);
                if (finalDuration >= Integer.MAX_VALUE) {
                    return CheckRecipeResultRegistry.DURATION_OVERFLOW;
                }
                duration = (int) finalDuration;

                // normal mode
                if (mode != 2) {
                    outputItems = helper.getItemOutputs();
                    outputFluids = helper.getFluidOutputs();

                    return result;
                }

                // add outputs
                final int multiplier = (int) Math.pow(2, fieldGeneratorTier - 3);
                DistortionSpaceTechnology.LOG.info("test multiplier: " + multiplier);

                outputItems = new ItemStack[helper.getItemOutputs().length];
                DistortionSpaceTechnology.LOG.info("test before outputItems: " + outputItems);
                for (int i = 0; i < helper.getItemOutputs().length; i++) {
                    ItemStack itemStack = helper.getItemOutputs()[i];
                    itemStack.stackSize = (int) Math.min(Integer.MAX_VALUE, (long) multiplier * itemStack.stackSize);
                    outputItems[i] = itemStack;
                }
                DistortionSpaceTechnology.LOG.info("test after outputItems: " + outputItems);

                outputFluids = new FluidStack[helper.getFluidOutputs().length];
                for (int i = 0; i < helper.getFluidOutputs().length; i++) {
                    FluidStack fluidStack = helper.getFluidOutputs()[i];
                    fluidStack.amount = (int) Math.min(Integer.MAX_VALUE, (long) multiplier * fluidStack.amount);
                    outputFluids[i] = fluidStack;
                }

                return result;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);

        // return mode == 3 ? ParticalProcessingLogic : normalProcessingLogic;
        return ParticalProcessingLogic;
    }

    public int getMaxParallelRecipes() {
        return (int) Math.min(MAX_PARALLEL_LIMIT, Math.pow(2, GT_Utility.getTier(this.getMaxInputVoltage())));
    }

    public float getSpeedBonus() {
        return 1.0F;
        // return (float) (1 / (this.fieldGeneratorTier < 2 ? 1 : 16));
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        switch (mode) {
            case 1:
                return GT_Recipe.GT_Recipe_Map.sExtractorRecipes;
            case 2:
                return GTPP_Recipe.GTPP_Recipe_Map.sCyclotronRecipes;
            default:
                return GT_Recipe.GT_Recipe_Map.sCompressorRecipes;
        }
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = fieldGeneratorTier < 3 ? (byte) ((this.mode + 1) % 2) : (byte) ((this.mode + 1) % 3);
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("SpaceScaler.modeMsg." + this.mode));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.fieldGeneratorTier = 0;
        boolean sign = checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
        if (this.fieldGeneratorTier == 0) {
            return false;
        }
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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
            false,
            true);
    }

    private static final String STRUCTURE_PIECE_MAIN = "mainSpaceScaler";
    private final int horizontalOffSet = 15;
    private final int verticalOffSet = 15;
    private final int depthOffSet = 0;
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
        return StructureDefinition.<GT_TileEntity_SpaceScaler>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
            .addElement(
                'A',
                GT_HatchElementBuilder.<GT_TileEntity_SpaceScaler>builder()
                    .atLeast(Energy.or(ExoticEnergy))
                    .adder(GT_TileEntity_SpaceScaler::addToMachineList)
                    .dot(1)
                    .casingIndex(1024)
                    .buildAndChain(sBlockCasingsTT, 0))
            .addElement('B', ofBlock(sBlockCasingsTT, 4))
            .addElement('C', ofBlock(sBlockCasingsTT, 6))
            .addElement('D', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement(
                'E',
                GT_HatchElementBuilder.<GT_TileEntity_SpaceScaler>builder()
                    .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Maintenance)
                    .adder(GT_TileEntity_SpaceScaler::addToMachineList)
                    .dot(2)
                    .casingIndex(1028)
                    .buildAndChain(sBlockCasingsTT, 4))
            .addElement(
                'G',
                withChannel("fieldGeneratorTier",
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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(Tooltip_SpaceScaler_MachineType)
            .addInfo(Tooltip_SpaceScaler_00)
            .addInfo(Tooltip_SpaceScaler_01)
            .addInfo(Tooltip_SpaceScaler_02)
            .addInfo(Tooltip_SpaceScaler_03)
            .addInfo(Tooltip_SpaceScaler_04)
            .addInfo(Tooltip_SpaceScaler_05)
            .addInfo(Tooltip_SpaceScaler_06)
            .addInfo(textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(31, 31, 32, false)
            .addInputHatch(textUseBlueprint, 1)
            .addOutputHatch(textUseBlueprint, 1)
            .addInputBus(textUseBlueprint, 1)
            .addOutputBus(textUseBlueprint, 1)
            .addMaintenanceHatch(textUseBlueprint, 1)
            .addEnergyHatch(textUseBlueprint, 2)
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setByte("mode", mode);
        aNBT.setInteger("fieldGeneratorTier", fieldGeneratorTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mode = aNBT.getByte("mode");
        fieldGeneratorTier = aNBT.getInteger("fieldGeneratorTier");
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length - 2] = EnumChatFormatting.AQUA + "Mode: " + EnumChatFormatting.GOLD + this.mode;
        ret[origin.length - 1] = EnumChatFormatting.AQUA + "fieldGeneratorTier: "
            + EnumChatFormatting.GOLD
            + this.fieldGeneratorTier;
        ret[origin.length] = EnumChatFormatting.AQUA + "Parallel: "
            + EnumChatFormatting.GOLD
            + this.getMaxParallelRecipes();
        return ret;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
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
