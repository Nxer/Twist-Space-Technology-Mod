package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.dreammaster.block.BlockList.BloodyThaumium;
import static com.dreammaster.block.BlockList.BloodyVoid;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.loader.Loaders.magicCasing;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static thaumcraft.common.config.ConfigBlocks.*;
import static thaumcraft.common.config.ConfigItems.itemEldritchObject;
import static thaumcraft.common.lib.research.ResearchManager.getResearchForPlayer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModBlocksHandler;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import fox.spiteful.avaritia.items.LudicrousItems;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import scala.Int;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.common.tiles.TileNodeEnergized;
import thaumicenergistics.common.storage.EnumEssentiaStorageTypes;
import thaumicenergistics.common.tiles.TileInfusionProvider;

public class GT_TileEntity_IndustrialMagicMatrix extends GTCM_MultiMachineBase<GT_TileEntity_IndustrialMagicMatrix>
    implements ISidedInventory {

    // region default value

    private int mParallel;
    private int ExtraTime;
    private double mSpeedBonus;
    private double Mean;
    private double Variance;
    private final ItemStack EssentiaCell_Creative = EnumEssentiaStorageTypes.Type_Creative.getCell();
    private final ItemStack ProofOfHeroes = GTCMItemList.ProofOfHeroes.get(1, 0);
    protected ArrayList<TileInfusionProvider> mTileInfusionProvider = new ArrayList<>();
    protected ArrayList<TileNodeEnergized> mNodeEnergized = new ArrayList<>();
    protected ArrayList<String> Research = new ArrayList<>();
    public static final CheckRecipeResult Essentia_InsentiaL = SimpleCheckRecipeResult
        .ofFailurePersistOnShutdown("Essentiainsentia");
    public static final CheckRecipeResult Research_not_completed = SimpleCheckRecipeResult
        .ofFailurePersistOnShutdown("Research_not_completed");

    // end region

    // region Class Constructor

    public GT_TileEntity_IndustrialMagicMatrix(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_IndustrialMagicMatrix(String aName) {
        super(aName);
    }

    // end region

    // region Processing Logic

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclock(isEnablePerfectOverclock() ? 2 : 1, 2);
                // setOverclock(isEnablePerfectOverclock() ? 2 : 1, isEnablePerfectOverclock() ? 2 : 3);
                return super.process();
            }

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
                int Para = createParallelHelper(recipe).setConsumption(false)
                    .build()
                    .getCurrentParallel();
                for (TCRecipeTools.InfusionCraftingRecipe recipe1 : TCRecipeTools.ICR) {
                    if (recipe1.getOutput()
                        .isItemEqual(recipe.mOutputs[0])) {
                        if (!(isResearchComplete(recipe1.getResearch()))) {
                            return Research_not_completed;
                        }
                        if (!(getControllerSlot() == null)) {
                            if (getControllerSlot().isItemEqual(EssentiaCell_Creative)
                                || getControllerSlot().isItemEqual(ProofOfHeroes)) {
                                return CheckRecipeResultRegistry.SUCCESSFUL;
                            }
                        }
                        for (Aspect aspect : recipe1.getInputAspects()
                            .getAspects()) {
                            if (mTileInfusionProvider.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;
                            for (TileInfusionProvider hatch : mTileInfusionProvider) {

                                if (hatch.takeFromContainer(aspect, recipe1.getAspectAmount(aspect) * Para)) {
                                    return CheckRecipeResultRegistry.SUCCESSFUL;
                                } else return Essentia_InsentiaL;
                            }

                        }
                    }
                }
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public boolean isResearchComplete(String key) {
        if (!key.startsWith("@") && ResearchCategories.getResearch(key) == null) {
            return false;
        } else {
            return this.Research != null && !this.Research.isEmpty() && this.Research.contains(key);
        }
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        // If no logic is found, try legacy checkRecipe
        if (processingLogic == null) {
            return checkRecipe(mInventory[1]) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        }

        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        if (getControllerSlot() == null) {
            mMaxProgresstime = processingLogic.getDuration() + ExtraTime;
        } else if (getControllerSlot().isItemEqual(ProofOfHeroes)) {
            mMaxProgresstime = 1;
        } else mMaxProgresstime = processingLogic.getDuration() + ExtraTime;
        setEnergyUsage(processingLogic);
        ItemStack PrimordialPearl = new ItemStack(itemEldritchObject, 1, 3);
        int size = 0;
        for (ItemStack itemStack : processingLogic.getOutputItems()) {
            if (!(itemStack.isItemEqual(new ItemStack(LudicrousItems.bigPearl)))) {
                InfusionRecipe Recipe = ThaumcraftApi.getInfusionRecipe(itemStack);
                for (ItemStack itemStack1 : Recipe.getComponents()) {
                    if (itemStack1.isItemEqual(PrimordialPearl)) {
                        size++;
                    }
                }
            } else {
                mOutputItems = processingLogic.getOutputItems();
                mOutputFluids = processingLogic.getOutputFluids();
                return result;
            }
        }
        if (size != 0) {
            int index = 0;
            ItemStack[] OutputItems = new ItemStack[processingLogic.getOutputItems().length + 1];
            for (ItemStack itemStack : processingLogic.getOutputItems()) {
                OutputItems[index] = itemStack;
            }
            OutputItems[OutputItems.length - 1] = new ItemStack(itemEldritchObject, size, 3);
            mOutputItems = OutputItems;
            mOutputFluids = processingLogic.getOutputFluids();
        } else {
            mOutputItems = processingLogic.getOutputItems();
            mOutputFluids = processingLogic.getOutputFluids();
        }

        return result;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.IndustrialMagicMatrixRecipe;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return mSpeedBonus == (double) 1 / 11.4514;
    }

    @Override
    protected float getSpeedBonus() {
        mSpeedBonus = 0.0;
        ExtraTime = 0;
        countSpeedBonus();
        return (float) mSpeedBonus;
    }

    @Override
    protected int getLimitedMaxParallel() {
        return getMaxParallelRecipes();
    }

    private void countSpeedBonus() {
        int penalize = 0;
        ArrayList<Double> MaxAmount = new ArrayList<>();
        AspectList aspectList = new AspectList();
        AspectList auraBase = (new AspectList()).add(Aspect.AIR, 20)
            .add(Aspect.FIRE, 20)
            .add(Aspect.EARTH, 20)
            .add(Aspect.WATER, 20)
            .add(Aspect.ORDER, 20)
            .add(Aspect.ENTROPY, 20);
        if (mNodeEnergized.isEmpty()) {
            mSpeedBonus = 1;
            return;
        } else if (mNodeEnergized.size() < 6) {
            mSpeedBonus = (6 - mNodeEnergized.size()) * 1.75;
            return;
        }
        for (TileNodeEnergized tileNodeEnergized : mNodeEnergized) {
            aspectList.add(
                getMaxAspect(tileNodeEnergized.getAspects()),
                tileNodeEnergized.getAspects()
                    .getAmount(getMaxAspect(tileNodeEnergized.getAspects())));
        }
        for (Aspect aspect : auraBase.getAspects()) {
            if (!(aspectList.aspects.containsKey(aspect))) {
                penalize++;
            }
        }
        if (penalize > 0) {
            ExtraTime = penalize * 20;
            mSpeedBonus = 1;
            return;
        }
        for (Aspect aspect : aspectList.getAspects()) {
            MaxAmount.add((double) aspectList.getAmount(aspect));
        }
        Mean = calculateMean(MaxAmount);
        Variance = calculateVariance(MaxAmount);
        double e = Math.E;
        double ln1 = Math.log(1 + Math.pow(e, -Variance));
        double ln2 = Math.log(2);
        double SpeedBonus = 1 + ((0.4 + Math.pow(0.45 * e, -0.005 * Variance) + 0.15 * (ln1 / ln2)) * Mean / 500);
        mSpeedBonus = Math.max(1 / SpeedBonus, 1 / 11.4514);
    }

    public static double calculateVariance(List<Double> data) {
        int n = data.size();
        double mean = calculateMean(data);
        double sum = 0.0;

        for (double value : data) {
            double diff = value - mean;
            sum += diff * diff;
        }
        return sum / n;
    }

    private static double calculateMean(List<Double> data) {
        int n = data.size();
        double sum = 0.0;

        for (double value : data) {
            sum += value;
        }

        return sum / n;
    }

    private Aspect getMaxAspect(@Nonnull AspectList aspectList) {
        Aspect maxAspect = null;
        int max = 0;
        for (Aspect aspect : aspectList.getAspects()) {
            max = Math.max(aspectList.getAmount(aspect), max);
            maxAspect = aspect;
        }
        return maxAspect;
    }

    @Override
    protected int getMaxParallelRecipes() {
        if (getControllerSlot() == null) {
            return mParallel;
        } else if (getControllerSlot().isItemEqual(ProofOfHeroes)) {
            return Int.MaxValue();
        } else return mParallel;
    }

    // end region

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 2];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + "Mean: " + EnumChatFormatting.GOLD + this.Mean;
        ret[origin.length + 1] = EnumChatFormatting.AQUA + "Variance: " + EnumChatFormatting.GOLD + this.Variance;
        return ret;
    }

    protected String getPlayName() {
        return this.getBaseMetaTileEntity()
            .getOwnerName();
    }

    protected void onEssentiaCellFound(int tier) {
        this.mParallel = (int) (tier * 8L);
    }

    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    // region Structure

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final int horizontalOffSet = 22;
    private final int verticalOffSet = 35;
    private final int depthOffSet = 20;

    // spotless:off

    private static final String[][] shape = new String[][] {
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "             D                 D             ",
                    "            DZD               DZD            ", "             D                 D             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "    D                                   D    ",
                    "   DZD                                 DZD   ", "    D                                   D    ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "             D                 D             ",
                    "            DZD               DZD            ", "             D                 D             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            DDD               DDD            ",
                    "            D D               D D            ", "            DDD               DDD            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   DDD                                 DDD   ",
                    "   D D                                 D D   ", "   DDD                                 DDD   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            DDD               DDD            ",
                    "            D D               D D            ", "            DDD               DDD            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "             D                 D             ", "            DDD               DDD            ",
                    "           DD DD             DD DD           ", "            DDD               DDD            ",
                    "             D                 D             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "    D                                   D    ", "   DDD                                 DDD   ",
                    "  DD DD                               DD DD  ", "   DDD                                 DDD   ",
                    "    D                                   D    ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "             D                 D             ", "            DDD               DDD            ",
                    "           DD DD             DD DD           ", "            DDD               DDD            ",
                    "             D                 D             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "            DDD               DDD            ", "           D   D             D   D           ",
                    "           D   D             D   D           ", "           D   D             D   D           ",
                    "            DDD               DDD            ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "   DDD                                 DDD   ", "  D   D                               D   D  ",
                    "  D   D                               D   D  ", "  D   D                               D   D  ",
                    "   DDD                                 DDD   ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "            DDD               DDD            ", "           D   D             D   D           ",
                    "           D   D             D   D           ", "           D   D             D   D           ",
                    "            DDD               DDD            ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "             D                 D             ",
                    "            DDD               DDD            ", "           D   D             D   D           ",
                    "          DD   DD           DD   DD          ", "           D   D             D   D           ",
                    "            DDD               DDD            ", "             D                 D             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "    D                                   D    ",
                    "   DDD               ZZZ               DDD   ", "  D   D             ZZZZZ             D   D  ",
                    " DD   DD            ZZZZZ            DD   DD ", "  D   D             ZZZZZ             D   D  ",
                    "   DDD               ZZZ               DDD   ", "    D                                   D    ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "             D                 D             ",
                    "            DDD               DDD            ", "           D   D             D   D           ",
                    "          DD   DD           DD   DD          ", "           D   D             D   D           ",
                    "            DDD               DDD            ", "             D                 D             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            DDD               DDD            ",
                    "           D   D             D   D           ", "          D     D           D     D          ",
                    "          D     D           D     D          ", "          D     D           D     D          ",
                    "           D   D             D   D           ", "            DDD               DDD            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   DDD              ZZZZZ              DDD   ",
                    "  D   D            ZZ   ZZ            D   D  ", " D     D           Z     Z           D     D ",
                    " D     D           Z     Z           D     D ", " D     D           Z     Z           D     D ",
                    "  D   D            ZZ   ZZ            D   D  ", "   DDD              ZZZZZ              DDD   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            DDD               DDD            ",
                    "           D   D             D   D           ", "          D     D           D     D          ",
                    "          D     D           D     D          ", "          D     D           D     D          ",
                    "           D   D             D   D           ", "            DDD               DDD            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "           DDDDD             DDDDD           ",
                    "          DD   DD           DD   DD          ", "          D     D           D     D          ",
                    "          D     D           D     D          ", "          D     D           D     D          ",
                    "          DD   DD           DD   DD          ", "           DDDDD             DDDDD           ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                    ZZZZZ                    ", "  DDDDD            Z     Z            DDDDD  ",
                    " DD   DD          Z       Z          DD   DD ", " D     D          Z  WWW  Z          D     D ",
                    " D     D          Z  WWW  Z          D     D ", " D     D          Z  WWW  Z          D     D ",
                    " DD   DD          Z       Z          DD   DD ", "  DDDDD            Z     Z            DDDDD  ",
                    "                    ZZZZZ                    ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "           DDDDD             DDDDD           ",
                    "          DD   DD           DD   DD          ", "          D     D           D     D          ",
                    "          D     D           D     D          ", "          D     D           D     D          ",
                    "          DD   DD           DD   DD          ", "           DDDDD             DDDDD           ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "           DDDDD             DDDDD           ", "          DDDDDDD           DDDDDDD          ",
                    "         DDD   DDD         DDD   DDD         ", "         DD     DD         DD     DD         ",
                    "         DD     DD         DD     DD         ", "         DD     DD         DD     DD         ",
                    "         DDD   DDD         DDD   DDD         ", "          DDDDDDD           DDDDDDD          ",
                    "           DDDDD             DDDDD           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                     ZZZ                     ",
                    "  DDDDD            ZZ   ZZ            DDDDD  ", " DDDDDDD          Z       Z          DDDDDDD ",
                    "DDD   DDD         Z WWWWW Z         DDD   DDD", "DD     DD        Z  W   W  Z        DD     DD",
                    "DD     DD        Z  W   W  Z        DD     DD", "DD     DD        Z  W   W  Z        DD     DD",
                    "DDD   DDD         Z WWWWW Z         DDD   DDD", " DDDDDDD          Z       Z          DDDDDDD ",
                    "  DDDDD            ZZ   ZZ            DDDDD  ", "                     ZZZ                     ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           DDDDD             DDDDD           ", "          DDDDDDD           DDDDDDD          ",
                    "         DDD   DDD         DDD   DDD         ", "         DD     DD         DD     DD         ",
                    "         DD     DD         DD     DD         ", "         DD     DD         DD     DD         ",
                    "         DDD   DDD         DDD   DDD         ", "          DDDDDDD           DDDDDDD          ",
                    "           DDDDD             DDDDD           ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BBB               BBB            ",
                    "           BDDDB             BDDDB           ", "          BDDDDDB           BDDDDDB          ",
                    "          BDDZDDB           BDDZDDB          ", "          BDDDDDB           BDDDDDB          ",
                    "           BDDDB             BDDDB           ", "            BBB               BBB            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                    ZZZZZ                    ",
                    "                   Z     Z                   ", "   BBB            Z  WWW  Z            BBB   ",
                    "  BDDDB          Z  W   W  Z          BDDDB  ", " BDDDDDB         Z W     W Z         BDDDDDB ",
                    " BDDZDDB         Z W     W Z         BDDZDDB ", " BDDDDDB         Z W     W Z         BDDDDDB ",
                    "  BDDDB          Z  W   W  Z          BDDDB  ", "   BBB            Z  WWW  Z            BBB   ",
                    "                   Z     Z                   ", "                    ZZZZZ                    ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            BBB               BBB            ",
                    "           BDDDB             BDDDB           ", "          BDDDDDB           BDDDDDB          ",
                    "          BDDZDDB           BDDZDDB          ", "          BDDDDDB           BDDDDDB          ",
                    "           BDDDB             BDDDB           ", "            BBB               BBB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           B D B             B D B           ", "                                             ",
                    "           D   D             D   D           ", "                                             ",
                    "           B D B             B D B           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                    ZZZZZ                    ",
                    "                   Z     Z                   ", "                  Z  WGW  Z                  ",
                    "  B D B          Z  W   W  Z          B D B  ", "                 Z W     W Z                 ",
                    "  D   D          Z G     G Z          D   D  ", "                 Z W     W Z                 ",
                    "  B D B          Z  W   W  Z          B D B  ", "                  Z  WGW  Z                  ",
                    "                   Z     Z                   ", "                    ZZZZZ                    ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           B D B             B D B           ", "                                             ",
                    "           D   D             D   D           ", "                                             ",
                    "           B D B             B D B           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           B   B             B   B           ", "                                             ",
                    "                                             ", "                                             ",
                    "           B   B             B   B           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                    ZZZZZ                    ",
                    "                   Z     Z                   ", "                  Z  WWW  Z                  ",
                    "  B   B          Z  W   W  Z          B   B  ", "                 Z W     W Z                 ",
                    "                 Z W     W Z                 ", "                 Z W     W Z                 ",
                    "  B   B          Z  W   W  Z          B   B  ", "                  Z  WWW  Z                  ",
                    "                   Z     Z                   ", "                    ZZZZZ                    ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           B   B             B   B           ", "                                             ",
                    "                                             ", "                                             ",
                    "           B   B             B   B           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                     ZZZ                     ",
                    "                   ZZ   ZZ                   ", "                  Z       Z                  ",
                    "                  Z WWWWW Z                  ", "                 Z  W   W  Z                 ",
                    "                 Z  W   W  Z                 ", "                 Z  W   W  Z                 ",
                    "                  Z WWWWW Z                  ", "                  Z       Z                  ",
                    "                   ZZ   ZZ                   ", "                     ZZZ                     ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            DDD               DDD            ",
                    "            DZD               DZD            ", "            DDD               DDD            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                  JFZZZZZFJ                  ", "                  FZ     ZF                  ",
                    "                  Z       Z                  ", "   DDD           JZ  WWW  ZF           DDD   ",
                    "   DZD           JZ  WWW  ZJ           DZD   ", "   DDD           JZ  WWW  ZF           DDD   ",
                    "                  Z       Z                  ", "                  FZ     ZF                  ",
                    "                  JFZZZZZFJ                  ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            DDD               DDD            ",
                    "            DZD               DZD            ", "            DDD               DDD            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "            DDD               DDD            ", "           DDDDD             DDDDD           ",
                    "           DD DD             DD DD           ", "           DDDDD             DDDDD           ",
                    "            DDD               DDD            ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                  11FJJJF11                  ", "                  1JZZZZZJ1                  ",
                    "   DDD            FZZ   ZZF            DDD   ", "  DDDDD           FZ     ZF           DDDDD  ",
                    "  DD DD         11JZ     ZJ11         DD DD  ", "  DDDDD           FZ     ZF           DDDDD  ",
                    "   DDD            FZZ   ZZF            DDD   ", "                  1JZZZZZJ1                  ",
                    "                  11FJJJF11                  ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "            DDD               DDD            ", "           DDDDD             DDDDD           ",
                    "           DD DD             DD DD           ", "           DDDDD             DDDDD           ",
                    "            DDD               DDD            ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BBB               BBB            ",
                    "           BDDDB             BDDDB           ", "          BDDDDDB           BDDDDDB          ",
                    "          BDD DDB           BDD DDB          ", "          BDDDDDB           BDDDDDB          ",
                    "           BDDDB             BDDDB           ", "            BBB               BBB            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                  11     11                  ", "   BBB            1JJJJJJJ1            BBB   ",
                    "  BDDDB            J ZZZ J            BDDDB  ", " BDDDDDB           JZZZZZJ           BDDDDDB ",
                    " BDD DDB        11 JZZZZZJ 11        BDD DDB ", " BDDDDDB           JZZZZZJ           BDDDDDB ",
                    "  BDDDB            J ZZZ J            BDDDB  ", "   BBB            1JJJJJJJ1            BBB   ",
                    "                  11     11                  ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            BBB               BBB            ",
                    "           BDDDB             BDDDB           ", "          BDDDDDB           BDDDDDB          ",
                    "          BDD DDB           BDD DDB          ", "          BDDDDDB           BDDDDDB          ",
                    "           BDDDB             BDDDB           ", "            BBB               BBB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AMFMA             AMFMA           ", "            BFB               BFB            ",
                    "             1                 1             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                  11     11                  ", "   BZB            1       1            BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E EFF1       11         11       1FFE E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB            1       1            BZB   ",
                    "                  11     11                  ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "             1                 1             ", "            BFB               BFB            ",
                    "           AMFMA             AMFMA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AMFMA             AMFMA           ", "            BFB               BFB            ",
                    "             1                 1             ", "              1               1              ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                 1         1                 ",
                    "                  11     11                  ", "   BZB            1       1            BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E EFF11     11           11     11FFE E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB            1       1            BZB   ",
                    "                  11     11                  ", "                 1         1                 ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "              1               1              ",
                    "             1                 1             ", "            BFB               BFB            ",
                    "           AMFMA             AMFMA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AMFMA             AMFMA           ", "            BFB               BFB            ",
                    "             1                 1             ", "              1               1              ",
                    "              1               1              ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                 1         1                 ",
                    "                  1       1                  ", "   BZB                                 BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E EFF111    11           11    111FFE E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB                                 BZB   ",
                    "                  1       1                  ", "                 1         1                 ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "              1               1              ", "              1               1              ",
                    "             1                 1             ", "            BFB               BFB            ",
                    "           AMFMA             AMFMA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AMDMA             AMDMA           ", "            BBB               BBB            ",
                    "                                             ", "              1               1              ",
                    "              1               1              ", "               1             1               ",
                    "                                             ", "                                             ",
                    "                1           1                ", "                 1         1                 ",
                    "                                             ", "   BZB                                 BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E EDB 111  11             11  111 BDE E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB                                 BZB   ",
                    "                                             ", "                 1         1                 ",
                    "                1           1                ", "                                             ",
                    "                                             ", "               1             1               ",
                    "              1               1              ", "              1               1              ",
                    "                                             ", "            BBB               BBB            ",
                    "           AMDMA             AMDMA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "              1               1              ", "               1             1               ",
                    "               1             1               ", "                1           1                ",
                    "                1           1                ", "                 1         1                 ",
                    "                                             ", "   BZB                                 BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E E Z  111111             111111  Z E E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB                                 BZB   ",
                    "                                             ", "                 1         1                 ",
                    "                1           1                ", "                1           1                ",
                    "               1             1               ", "               1             1               ",
                    "              1               1              ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             ", "               1             1               ",
                    "               1             1               ", "                1           1                ",
                    "                1           1                ", "                                             ",
                    "                                             ", "   BZB                                 BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E E Z   1111               1111   Z E E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB                                 BZB   ",
                    "                                             ", "                                             ",
                    "                1           1                ", "                1           1                ",
                    "               1             1               ", "               1             1               ",
                    "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   BZB                                 BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E E Z                             Z E E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB                                 BZB   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   BZB                                 BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E E Z                             Z E E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB                                 BZB   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   BZB                                 BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E E Z                             Z E E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB                                 BZB   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   BZB                                 BZB   ",
                    "  AM MA                               AM MA  ", " BM E MB                             BM E MB ",
                    " Z E E Z                             Z E E Z ", " BM E MB                             BM E MB ",
                    "  AM MA                               AM MA  ", "   BZB                                 BZB   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            BZB               BZB            ",
                    "           AM MA             AM MA           ", "          BM E MB           BM E MB          ",
                    "          Z E E Z           Z E E Z          ", "          BM E MB           BM E MB          ",
                    "           AM MA             AM MA           ", "            BZB               BZB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            BBB               BBB            ",
                    "           BDDDB             BDDDB           ", "          BDDDDDB           BDDDDDB          ",
                    "          BDD DDB           BDD DDB          ", "          BDDDDDB           BDDDDDB          ",
                    "           BDDDB             BDDDB           ", "            BBB               BBB            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   BBB                                 BBB   ",
                    "  BDDDB                               BDDDB  ", " BDDDDDB                             BDDDDDB ",
                    " BDD DDB                             BDD DDB ", " BDDDDDB                             BDDDDDB ",
                    "  BDDDB                               BDDDB  ", "   BBB                                 BBB   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            BBB               BBB            ",
                    "           BDDDB             BDDDB           ", "          BDDDDDB           BDDDDDB          ",
                    "          BDD DDB           BDD DDB          ", "          BDDDDDB           BDDDDDB          ",
                    "           BDDDB             BDDDB           ", "            BBB               BBB            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "            DDD               DDD            ", "           DDDDD             DDDDD           ",
                    "           DD DD             DD DD           ", "           DDDDD             DDDDD           ",
                    "            DDD               DDD            ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "   DDD                                 DDD   ", "  DDDDD                               DDDDD  ",
                    "  DD DD                               DD DD  ", "  DDDDD                               DDDDD  ",
                    "   DDD                                 DDD   ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "            DDD               DDD            ", "           DDDDD             DDDDD           ",
                    "           DD DD             DD DD           ", "           DDDDD             DDDDD           ",
                    "            DDD               DDD            ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            DDD               DDD            ",
                    "            DZD               DZD            ", "            DDD               DDD            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   DDD                                 DDD   ",
                    "   DZD                                 DZD   ", "   DDD                                 DDD   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            DDD               DDD            ",
                    "            DZD               DZD            ", "            DDD               DDD            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "             5                 5             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "    5                                   5    ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "             5                 5             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           B   B             B   B           ", "                                             ",
                    "             Y                 Y             ", "                                             ",
                    "           B   B             B   B           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "  B   B                               B   B  ", "                                             ",
                    "    Y                                   Y    ", "                                             ",
                    "  B   B                               B   B  ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           B   B             B   B           ", "                                             ",
                    "             Y                 Y             ", "                                             ",
                    "           B   B             B   B           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            B B               B B            ",
                    "           B   B             B   B           ", "          B     B           B     B          ",
                    "             3                 3             ", "          B     B           B     B          ",
                    "           B   B             B   B           ", "            B B               B B            ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "   B B                                 B B   ",
                    "  B   B                               B   B  ", " B     B                             B     B ",
                    "    3                 0                 3    ", " B     B                             B     B ",
                    "  B   B                               B   B  ", "   B B                                 B B   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "            B B               B B            ",
                    "           B   B             B   B           ", "          B     B           B     B          ",
                    "             3                 3             ", "          B     B           B     B          ",
                    "           B   B             B   B           ", "            B B               B B            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "            DDD               DDD            ", "           DBBBD             DBBBD           ",
                    "          DBDDDBD           DBDDDBD          ", "         DBDDDDDBD         DBDDDDDBD         ",
                    "         DBDD8DDBD         DBDD8DDBD         ", "         DBDDDDDBD         DBDDDDDBD         ",
                    "          DBDDDBD           DBDDDBD          ", "           DBBBD             DBBBD           ",
                    "            DDD               DDD            ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "   DDD                                 DDD   ", "  DBBBD                               DBBBD  ",
                    " DBDDDBD                             DBDDDBD ", "DBDDDDDBD            N N            DBDDDDDBD",
                    "DBDD8DDBD                           DBDD8DDBD", "DBDDDDDBD            N N            DBDDDDDBD",
                    " DBDDDBD                             DBDDDBD ", "  DBBBD                               DBBBD  ",
                    "   DDD                                 DDD   ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "            DDD               DDD            ", "           DBBBD             DBBBD           ",
                    "          DBDDDBD           DBDDDBD          ", "         DBDDDDDBD         DBDDDDDBD         ",
                    "         DBDD8DDBD         DBDD8DDBD         ", "         DBDDDDDBD         DBDDDDDBD         ",
                    "          DBDDDBD           DBDDDBD          ", "           DBBBD             DBBBD           ",
                    "            DDD               DDD            ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "           DDDDD             DDDDD           ", "          DDDDDDD           DDDDDDD          ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "         DDDCCCDDD         DDDCCCDDD         ",
                    "         DDDCCCDDD         DDDCCCDDD         ", "         DDDCCCDDD         DDDCCCDDD         ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "          DDDDDDD           DDDDDDD          ",
                    "           DDDDD             DDDDD           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "  DDDDD                               DDDDD  ", " DDDDDDD                             DDDDDDD ",
                    "DDDDDDDDD                           DDDDDDDDD", "DDDCCCDDD            6 6            DDDCCCDDD",
                    "DDDCCCDDD             7             DDDCCCDDD", "DDDCCCDDD            6 6            DDDCCCDDD",
                    "DDDDDDDDD                           DDDDDDDDD", " DDDDDDD                             DDDDDDD ",
                    "  DDDDD                               DDDDD  ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           DDDDD             DDDDD           ", "          DDDDDDD           DDDDDDD          ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "         DDDCCCDDD         DDDCCCDDD         ",
                    "         DDDCCCDDD         DDDCCCDDD         ", "         DDDCCCDDD         DDDCCCDDD         ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "          DDDDDDD           DDDDDDD          ",
                    "           DDDDD             DDDDD           ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "           DDDDD             DDDDD           ", "          DDDDDDD           DDDDDDD          ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "         DDDDDDDDD         DDDDDDDDD         ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "         DDDDDDDDD         DDDDDDDDD         ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "          DDDDDDD           DDDDDDD          ",
                    "           DDDDD             DDDDD           ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "  DDDDD                               DDDDD  ", " DDDDDDD                             DDDDDDD ",
                    "DDDDDDDDD            K~K            DDDDDDDDD", "DDDDDDDDD           KKKKK           DDDDDDDDD",
                    "DDDDDDDDD           KKKKK           DDDDDDDDD", "DDDDDDDDD           KKKKK           DDDDDDDDD",
                    "DDDDDDDDD            KKK            DDDDDDDDD", " DDDDDDD                             DDDDDDD ",
                    "  DDDDD                               DDDDD  ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "           DDDDD             DDDDD           ", "          DDDDDDD           DDDDDDD          ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "         DDDDDDDDD         DDDDDDDDD         ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "         DDDDDDDDD         DDDDDDDDD         ",
                    "         DDDDDDDDD         DDDDDDDDD         ", "          DDDDDDD           DDDDDDD          ",
                    "           DDDDD             DDDDD           ", "                                             ",
                    "                                             " },
            { "                  JJJJJJJJJ                  ", "              JJJJ    O    JJJJ              ",
                    "           JJJJJ     O O     JJJJJ           ", "          JJJJJJJ    O O    JJJJJJJ          ",
                    "         JJJJJJJJJ  O   O  JJJJJJJJJ         ", "        JJJJJJJJJJ  O   O  JJJJJJJJJJ        ",
                    "       J JJJJJJJJJ O     O JJJJJJJJJ J       ", "      J  JJJJJJJJJ O     O JJJJJJJJJ  J      ",
                    "     J   JJJJJJJJJO       OJJJJJJJJJ   J     ", "    J     JJJJJJJO OOOOOOO OJJJJJJJ     J    ",
                    "   J       JJJJJOOOIIIIIIIOOOJJJJJ       J   ", "   JOOOOOOOOOOOOOIIZZZZZZZIIOOOOOOOOOOOOOJ   ",
                    "  J  O       OOIIZZZZZZZZZZZIIOO       O  J  ", "  J  O      O IZIIIZZZZZZZIIIZI O      O  J  ",
                    " J    O     OIZZIZZIIZZZIIZZIZZIO     O    J ", " J    O    OIZZZIZZZZIIIZZZZIZZZIO    O    J ",
                    " J     O  O IZZZIZZZIIVIIZZZIZZZI O  O     J ", " J     O  OIZZZZIZZIKKKKKIZZIZZZZIO  O     J ",
                    "J JJJJJ O OIZZZZIIIK     KIIIZZZZIO O JJJJJ J", "JJJJJJJJOOIZZZZIIVK       KVIIZZZZIOOJJJJJJJJ",
                    "JJJJJJJJJOIZZIIZIK         KIZIIZZIOJJJJJJJJJ", "JJJJJJJJJOIIIZZZIK   KKK   KIZZZIIIOJJJJJJJJJ",
                    "JJJJJJJJJOIZZZZZVK   KKK   KVZZZZZIOJJJJJJJJJ", "JJJJJJJJJOIIIZZZIK   KKK   KIZZZIIIOJJJJJJJJJ",
                    "JJJJJJJJJOIZZIIZIK         KIZIIZZIOJJJJJJJJJ", "JJJJJJJJOOIZZZZIIVK       KVIIZZZZIOOJJJJJJJJ",
                    "J JJJJJ O OIZZZZIIIK     KIIIZZZZIO O JJJJJ J", " J     O  OIZZZZIZZIKKKKKIZZIZZZZIO  O     J ",
                    " J     O  O IZZZIZZZIIVIIZZZIZZZI O  O     J ", " J    O    OIZZZIZZZZIIIZZZZIZZZIO    O    J ",
                    " J    O     OIZZIZZIIZZZIIZZIZZIO     O    J ", "  J  O      O IZIIIZZZZZZZIIIZI O      O  J  ",
                    "  J  O       OOIIZZZZZZZZZZZIIOO       O  J  ", "   JOOOOOOOOOOOOOIIZZZZZZZIIOOOOOOOOOOOOOJ   ",
                    "   J       JJJJJOOOIIIIIIIOOOJJJJJ       J   ", "    J     JJJJJJJO OOOOOOO OJJJJJJJ     J    ",
                    "     J   JJJJJJJJJO       OJJJJJJJJJ   J     ", "      J  JJJJJJJJJ O     O JJJJJJJJJ  J      ",
                    "       J JJJJJJJJJ O     O JJJJJJJJJ J       ", "        JJJJJJJJJJ  O   O  JJJJJJJJJJ        ",
                    "         JJJJJJJJJ  O   O  JJJJJJJJJ         ", "          JJJJJJJ    O O    JJJJJJJ          ",
                    "           JJJJJ     O O     JJJJJ           ", "              JJJJ    O    JJJJ              ",
                    "                  JJJJJJJJJ                  " },
            { "                                             ", "                                             ",
                    "           KKKKK             KKKKK           ", "          KKXXXKK           KKXXXKK          ",
                    "         KKXXXXXKK         KKXXXXXKK         ", "         KXXXXXXXK         KXXXXXXXK         ",
                    "         KXXXXXXXK         KXXXXXXXK         ", "         KXXXXXXXK         KXXXXXXXK         ",
                    "         KKXXXXXKK         KKXXXXXKK         ", "          KKXXXKK           KKXXXKK          ",
                    "           KKKKK   IIIIIII   KKKKK           ", "                 IIHHHHHHHII                 ",
                    "               IIHHHHHHHHHHHII               ", "              IHIIIHHHHHHHIIIHI              ",
                    "             IHHIQQIIHHHIIPPIHHI             ", "            IHHHIQQQQIIIPPPPIHHHI            ",
                    "            IHHHIQQQIIKIIPPPIHHHI            ", "           IHHHHIQQI     IPPIHHHHI           ",
                    "  KKKKK    IHHHHIII       IIIHHHHI    KKKKK  ", " KKXXXKK  IHHHHIIK         KIIHHHHI  KKXXXKK ",
                    "KKXXXXXKK IHHIITI           IUIIHHI KKXXXXXKK", "KXXXXXXXK IIITTTI           IUUUIII KXXXXXXXK",
                    "KXXXXXXXK ITTTTTI           IUUUUUI KXXXXXXXK", "KXXXXXXXK IIITTTI           IUUUIII KXXXXXXXK",
                    "KKXXXXXKK IHHIITI           IUIIHHI KKXXXXXKK", " KKXXXKK  IHHHHIIK         KIIHHHHI  KKXXXKK ",
                    "  KKKKK    IHHHHIII       IIIHHHHI    KKKKK  ", "           IHHHHISSI     IRRIHHHHI           ",
                    "            IHHHISSSIIKIIRRRIHHHI            ", "            IHHHISSSSIIIRRRRIHHHI            ",
                    "             IHHISSIIHHHIIRRIHHI             ", "              IHIIIHHHHHHHIIIHI              ",
                    "               IIHHHHHHHHHHHII               ", "                 IIHHHHHHHII                 ",
                    "            KKKK   IIIIIII   KKKKK           ", "          KKXXXKK           KKXXXKK          ",
                    "         KKXXXXXKK         KKXXXXXKK         ", "         KXXXXXXXK         KXXXXXXXK         ",
                    "         KXXXXXXXK         KXXXXXXXK         ", "         KXXXXXXXK         KXXXXXXXK         ",
                    "         KKXXXXXKK         KKXXXXXKK         ", "          KKXXXKK           KKXXXKK          ",
                    "           KKKKK             KKKKK           ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "            KKK               KKK            ",
                    "           KKKKK             KKKKK           ", "          KKKKKKK           KKKKKKK          ",
                    "          KKKKKKK           KKKKKKK          ", "          KKKKKKK           KKKKKKK          ",
                    "           KKKKK             KKKKK           ", "            KKK               KKK            ",
                    "                   IIIIIII                   ", "                 IIZZZZZZZII                 ",
                    "               IIZZZZZZZZZZZII               ", "              IZIIIZZZZZZZIIIZI              ",
                    "             IZZIZZIIZZZIIZZIZZI             ", "            IZZZIZZZZIIIZZZZIZZZI            ",
                    "            IZZZIZZZII IIZZZIZZZI            ", "           IZZZZIZZI     IZZIZZZZI           ",
                    "           IZZZZIII       IIIZZZZI           ", "   KKK    IZZZZII           IIZZZZI    KKK   ",
                    "  KKKKK   IZZIIZI           IZIIZZI   KKKKK  ", " KKKKKKK  IIIZZZI           IZZZIII  KKKKKKK ",
                    " KKKKKKK  IZZZZZI           IZZZZZI  KKKKKKK ", " KKKKKKK  IIIZZZI           IZZZIII  KKKKKKK ",
                    "  KKKKK   IZZIIZI           IZIIZZI   KKKKK  ", "   KKK    IZZZZII           IIZZZZI    KKK   ",
                    "           IZZZZIII       IIIZZZZI           ", "           IZZZZIZZI     IZZIZZZZI           ",
                    "            IZZZIZZZII IIZZZIZZZI            ", "            IZZZIZZZZIIIZZZZIZZZI            ",
                    "             IZZIZZIIZZZIIZZIZZI             ", "              IZIIIZZZZZZZIIIZI              ",
                    "               IIZZZZZZZZZZZII               ", "                 IIZZZZZZZII                 ",
                    "                   IIIIIII                   ", "            KKK               KKK            ",
                    "           KKKKK             KKKKK           ", "          KKKKKKK           KKKKKKK          ",
                    "          KKKKKKK           KKKKKKK          ", "          KKKKKKK           KKKKKKK          ",
                    "           KKKKK             KKKKK           ", "            KKK               KKK            ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                    OOOOO                    ", "                  OO     OO                  ",
                    "                OO         OO                ", "                O           O                ",
                    "               O             O               ", "               O             O               ",
                    "              O               O              ", "              O               O              ",
                    "              O               O              ", "              O               O              ",
                    "              O               O              ", "               O             O               ",
                    "               O             O               ", "                O           O                ",
                    "                OO         OO                ", "                  OO     OO                  ",
                    "                    OOOOO                    ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                  OOOOOOOOO                  ", "               OOO         OOO               ",
                    "             OO               OO             ", "           OO                   OO           ",
                    "          O  O                 O  O          ", "         O   OOO             OOO   O         ",
                    "        O    O  OO         OO  O    O        ", "       O     O    O       O    O     O       ",
                    "      O      O     OO   OO     O      O      ", "     O       O       OOO       O       O     ",
                    "     O       O       O O       O       O     ", "    O        O     OO   OO     O        O    ",
                    "    O        O   OO       OO   O        O    ", "   O         O OO           OO O         O   ",
                    "   O         OO     OOOOO     OO         O   ", "   O        OO    OO     OO    OO        O   ",
                    "  O       OO O   O         O   O OO       O  ", "  O      O   O   O         O   O   O      O  ",
                    "  O    OO    O  O           O  O    OO    O  ", "  O  OO      O  O           O  O      OO  O  ",
                    "  O O        O  O           O  O        O O  ", "  O  OO      O  O           O  O      OO  O  ",
                    "  O    OO    O  O           O  O    OO    O  ", "  O      O   O   O         O   O   O      O  ",
                    "  O       OO O   O         O   O OO       O  ", "   O        OO    OO     OO    OO        O   ",
                    "   O         OO     OOOOO     OO         O   ", "   O         O OO           OO O         O   ",
                    "    O        O   OO       OO   O        O    ", "    O        O     OO   OO     O        O    ",
                    "     O       O       O O       O       O     ", "     O       O       OOO       O       O     ",
                    "      O      O     OO   OO     O      O      ", "       O     O    O       O    O     O       ",
                    "        O    O  OO         OO  O    O        ", "         O   OOO             OOO   O         ",
                    "          O  O                 O  O          ", "           OO                   OO           ",
                    "             OO               OO             ", "               OOO         OOO               ",
                    "                  OOOOOOOOO                  ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                   OOOOOOO                   ",
                    "                 OO       OO                 ", "                O           O                ",
                    "               O             O               ", "              O               O              ",
                    "             O                 O             ", "             O                 O             ",
                    "            O                   O            ", "            O                   O            ",
                    "            O                   O            ", "            O                   O            ",
                    "            O                   O            ", "            O                   O            ",
                    "            O                   O            ", "             O                 O             ",
                    "             O                 O             ", "              O               O              ",
                    "               O             O               ", "                O           O                ",
                    "                 OO       OO                 ", "                   OOOOOOO                   ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                     DDD                     ",
                    "                     DZD                     ", "                     DDD                     ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                   JJJJJJJ                   ", "                JJJKKKKKKKJJJ                ",
                    "              JJKKKKKKKKKKKKKJJ              ", "             JKKKKKLLLLLLLKKKKKJ             ",
                    "            JKKKKLL       LLKKKKJ            ", "           JKKKKL           LKKKKJ           ",
                    "          JKKKKL             LKKKKJ          ", "          JKKKL               LKKKJ          ",
                    "         JKKKL                 LKKKJ         ", "         JKKL                   LKKJ         ",
                    "         JKKL                   LKKJ         ", "        JKKL                     LKKJ        ",
                    "        JKKL         DDD         LKKJ        ", "        JKKL        DDDDD        LKKJ        ",
                    "        JKKL        DD DD        LKKJ        ", "        JKKL        DDDDD        LKKJ        ",
                    "        JKKL         DDD         LKKJ        ", "        JKKL                     LKKJ        ",
                    "         JKKL                   LKKJ         ", "         JKKL                   LKKJ         ",
                    "         JKKKL                 LKKKJ         ", "          JKKKL               LKKKJ          ",
                    "          JKKKKL             LKKKKJ          ", "           JKKKKL           LKKKKJ           ",
                    "            JKKKKLL       LLKKKKJ            ", "             JKKKKKLLLLLLLKKKKKJ             ",
                    "              JJKKKKKKKKKKKKKJJ              ", "                JJJKKKKKKKJJJ                ",
                    "                   JJJJJJJ                   ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                   JJJJJJJ                   ", "                 JJKKKKKKKJJ                 ",
                    "                JKKKKKKKKKKKJ                ", "               JKKKKLLLLLKKKKJ               ",
                    "              JKKKLLKKKKKLLKKKJ              ", "             JKKKLKKKKKKKKKLKKKJ             ",
                    "             JKKLKKKKKKKKKKKLKKJ             ", "            JKKKLKKKKBBBKKKKLKKKJ            ",
                    "            JKKLKKKKBDDDBKKKKLKKJ            ", "            JKKLKKKBDDDDDBKKKLKKJ            ",
                    "            JKKLKKKBDD DDBKKKLKKJ            ", "            JKKLKKKBDDDDDBKKKLKKJ            ",
                    "            JKKLKKKKBDDDBKKKKLKKJ            ", "            JKKKLKKKKBBBKKKKLKKKJ            ",
                    "             JKKLKKKKKKKKKKKLKKJ             ", "             JKKKLKKKKKKKKKLKKKJ             ",
                    "              JKKKLLKKKKKLLKKKJ              ", "               JKKKKLLLLLKKKKJ               ",
                    "                JKKKKKKKKKKKJ                ", "                 JJKKKKKKKJJ                 ",
                    "                   JJJJJJJ                   ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                    JJJJJ                    ", "                  JJKKKKKJJ                  ",
                    "                 JKKKKKKKKKJ                 ", "                 JKKKLLLKKKJ                 ",
                    "                JKKKLKKKLKKKJ                ", "                JKKLKDDDKLKKJ                ",
                    "                JKKLKD DKLKKJ                ", "                JKKLKDDDKLKKJ                ",
                    "                JKKKLKKKLKKKJ                ", "                 JKKKLLLKKKJ                 ",
                    "                 JKKKKKKKKKJ                 ", "                  JJKKKKKJJ                  ",
                    "                    JJJJJ                    ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                     JJJ                     ", "                    JDDDJ                    ",
                    "                    JDDDJ                    ", "                    JDDDJ                    ",
                    "                     JJJ                     ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " },
            { "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             ", "                                             ",
                    "                                             " } };

    // spotless:on

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            source,
            actor,
            false,
            true);
    }

    private static IStructureDefinition<GT_TileEntity_IndustrialMagicMatrix> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GT_TileEntity_IndustrialMagicMatrix> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_IndustrialMagicMatrix>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    ofChain(
                        onElementPass(x -> x.onEssentiaCellFound(1), ofBlock(Loaders.essentiaCell, 0)),
                        onElementPass(x -> x.onEssentiaCellFound(2), ofBlock(Loaders.essentiaCell, 1)),
                        onElementPass(x -> x.onEssentiaCellFound(3), ofBlock(Loaders.essentiaCell, 2)),
                        onElementPass(x -> x.onEssentiaCellFound(4), ofBlock(Loaders.essentiaCell, 3))))
                .addElement('B', ofBlock(GregTech_API.sBlockCasings8, 8))
                .addElement('C', ofBlock(GregTech_API.sBlockMetal4, 10))
                .addElement(
                    'D',
                    ofChain(
                        GT_HatchElementBuilder.<GT_TileEntity_IndustrialMagicMatrix>builder()
                            .atLeast(InputBus, OutputBus, Energy)
                            .adder(GT_TileEntity_IndustrialMagicMatrix::addToMachineList)
                            .casingIndex(1536)
                            .dot(1)
                            .build(),
                        ofBlock(magicCasing, 0),
                        ofTileAdder(GT_TileEntity_IndustrialMagicMatrix::addInfusionProvider, magicCasing, 0)))
                .addElement('E', ofBlock(blockMetalDevice, 9))
                .addElement('F', ofBlock(BloodyThaumium.getBlock(), 0))
                .addElement('G', ofBlock(BloodyVoid.getBlock(), 0))
                .addElement(
                    'H',
                    ofBlock(ModBlocksHandler.BlockCrystalDeep.getLeft(), ModBlocksHandler.BlockCrystalDeep.getRight()))
                .addElement('I', ofBlock(blockCosmeticSolid, 0))
                .addElement('J', ofBlock(blockCosmeticSolid, 4))
                .addElement('K', ofBlock(blockCosmeticSolid, 6))
                .addElement('L', ofBlock(blockCosmeticSolid, 7))
                .addElement('M', ofBlock(blockMetalDevice, 3))
                .addElement('N', ofChain(ofBlock(blockCosmeticSolid, 6), ofBlock(blockStoneDevice, 6)))
                .addElement(
                    'O',
                    ofBlock(ModBlocksHandler.BlockTranslucent.getLeft(), ModBlocksHandler.BlockTranslucent.getRight()))
                .addElement(
                    'P',
                    ofBlock(ModBlocksHandler.AirCrystalBlock.getLeft(), ModBlocksHandler.AirCrystalBlock.getRight()))
                .addElement(
                    'Q',
                    ofBlock(ModBlocksHandler.FireCrystalBlock.getLeft(), ModBlocksHandler.FireCrystalBlock.getRight()))
                .addElement(
                    'R',
                    ofBlock(
                        ModBlocksHandler.WaterCrystalBlock.getLeft(),
                        ModBlocksHandler.WaterCrystalBlock.getRight()))
                .addElement(
                    'S',
                    ofBlock(
                        ModBlocksHandler.EarthCrystalBlock.getLeft(),
                        ModBlocksHandler.EarthCrystalBlock.getRight()))
                .addElement(
                    'T',
                    ofBlock(
                        ModBlocksHandler.OrderCrystalBlock.getLeft(),
                        ModBlocksHandler.OrderCrystalBlock.getRight()))
                .addElement(
                    'U',
                    ofBlock(
                        ModBlocksHandler.EntropyCrystalBlock.getLeft(),
                        ModBlocksHandler.EntropyCrystalBlock.getRight()))
                .addElement('V', ofBlock(ModBlocksHandler.DustBlock.getLeft(), ModBlocksHandler.DustBlock.getRight()))
                .addElement('W', ofBlock(ModBlocksHandler.VoidBlock.getLeft(), ModBlocksHandler.VoidBlock.getRight()))
                .addElement(
                    'X',
                    ofBlock(ModBlocksHandler.ThauminiteBlock.getLeft(), ModBlocksHandler.ThauminiteBlock.getRight()))
                .addElement(
                    'Y',
                    ofChain(
                        ofTileAdder(GT_TileEntity_IndustrialMagicMatrix::addNodeEnergized, Blocks.air, 0),
                        ofBlock(Blocks.air, 0)))
                .addElement('Z', ofBlock(blockCosmeticOpaque, 2))
                .addElement('0', ofBlock(blockStoneDevice, 2))
                .addElement('1', ofFrame(Materials.Thaumium))
                .addElement('3', ofBlock(blockStoneDevice, 10))
                .addElement('5', ofBlock(blockStoneDevice, 11))
                .addElement('6', ofChain(ofBlock(blockCosmeticSolid, 7), ofBlock(blockStoneDevice, 7)))
                .addElement('7', ofBlock(blockStoneDevice, 1))
                .addElement('8', ofBlockAnyMeta(Blocks.beacon, 1))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    public final boolean addInfusionProvider(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileInfusionProvider) {
            return this.mTileInfusionProvider.add((TileInfusionProvider) aTileEntity);
        }
        return false;
    }

    public final boolean addNodeEnergized(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileNodeEnergized) {
            if (!(mNodeEnergized.size() == 6)) {
                return this.mNodeEnergized.add((TileNodeEnergized) aTileEntity);
            } else return true;
        }
        return false;
    }

    // spotless:off

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(
                // #tr Tooltip_IndustrialMagicMatrix_MachineType
                // # Controller block for the Industrial Magic Matrix
                // #zh_CN §0工业注魔矩阵
                TextEnums.tr("Tooltip_IndustrialMagicMatrix_MachineType"))
                // #tr Tooltip_IndustrialMagicMatrix_Controller
                // # Magic Matrix
                // #zh_CN 工业注魔矩阵的控制器方块
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_Controller"))
                // #tr Tooltip_IndustrialMagicMatrix_00
                // # Please use the Infusion Supplier to supply Essence!
                // #zh_CN 请使用注魔供应器供给源质！
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_00"))
                // #tr Tooltip_IndustrialMagicMatrix_01
                // # When you stare into the void, the void stares at you.
                // #zh_CN §c§n当你凝视虚空时，虚空也在凝视你。
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_01"))
                // #tr Tooltip_IndustrialMagicMatrix_02
                // # Some say it's a miracle of a mystical envoy,
                // #zh_CN 有人说这是神秘使的神迹，
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_02"))
                // #tr Tooltip_IndustrialMagicMatrix_03
                // # Others say that it is a cult creature of a sorcerer.
                // #zh_CN 也有人说这是邪术使的§c§0邪祟造物。
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_03"))
                // #tr Tooltip_IndustrialMagicMatrix_04
                // # But who cares?!
                // #zh_CN 不过谁在意呢？！
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_04"))
                // #tr Tooltip_IndustrialMagicMatrix_05
                // # Needless to say, its incredible principle is fascinating...
                // #zh_CN §k毋庸置疑的是它那不可思议的原理令人十分入迷...
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_05"))
                // #tr Tooltip_IndustrialMagicMatrix_06
                // # Because of the pollution of technology,
                // #zh_CN 由于被科技所污染，
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_06"))
                // #tr Tooltip_IndustrialMagicMatrix_07
                // # It is unable to perform active infusions.
                // #zh_CN 它无法进行具有活性的注魔。
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_07"))
                // #tr Tooltip_IndustrialMagicMatrix_08
                // # Parallelism depends on the level of the structure block.
                // #zh_CN 并行取决于结构方块的等级。
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_08"))
                // #tr Tooltip_IndustrialMagicMatrix_09
                // # Do an 4/2 overclock.Turn on lossless overclocking after reaching the maximum acceleration rate.
                // #zh_CN 进行4/2超频。达到最高加速倍率后开启无损超频.
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_09"))
                // #tr Tooltip_IndustrialMagicMatrix_10
                // # Use Charged Nodes to get acceleration rewards,
                // #zh_CN §b使用充能节点以获得加速奖励§7，
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_10"))
                // #tr Tooltip_IndustrialMagicMatrix_11
                // # However, when the number of nodes is less than six,
                // #zh_CN 但节点数量不足六个时,
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_11"))
                // #tr Tooltip_IndustrialMagicMatrix_12
                // # the processing time will be carried out (recipe time * number of missing nodes * 1.75).
                // #zh_CN 将会额外计算(配方时间*节点缺失数量*1.75)的加工时长，
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_12"))
                // #tr Tooltip_IndustrialMagicMatrix_13
                // # The matrix will take the largest element of each of the six nodes and calculate the average.
                // #zh_CN 矩阵将会取这六个节点中每个最大的要素并计算平均值，
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_13"))
                // #tr Tooltip_IndustrialMagicMatrix_14
                // # The closer the quantities between the six primitive elements are, the higher the multiplication factor will be.
                // #zh_CN 六种原始要素之间的数量越接近倍率系数就会越高。
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_14"))
                // #tr Tooltip_IndustrialMagicMatrix_15
                // # The actual acceleration magnification is ((0.4+0.45exp(-0.05Variance)+
                // #zh_CN 实际加速倍率为{\SPACE}{\AQUA}((0.4+0.45exp(-0.05Variance) +
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_15"))
                // #tr Tooltip_IndustrialMagicMatrix_16
                // # 0.15(ln(1+exp(-Variance)/ln2)) * (Mean / 500).
                // #zh_CN {\SPACE}{\SPACE}{\AQUA}0.15(ln(1+exp(-Variance)/ln2)) * (Mean / 500)
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_16"))
                // #tr Tooltip_IndustrialMagicMatrix_17
                // # Variance is the variance of the largest element in the six nodes,
                // #zh_CN Variance为六个节点里最大要素的方差，
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_17"))
                // #tr Tooltip_IndustrialMagicMatrix_18
                // # Mean is the average.
                // #zh_CN Mean则为平均数。
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_18"))
                // #tr Tooltip_IndustrialMagicMatrix_19
                // # When the type of the six elements is not the six original elements,
                // #zh_CN 当六个要素的种类不为六种原始要素时,
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_19"))
                // #tr Tooltip_IndustrialMagicMatrix_20
                // # Each missing one adds a fixed 1 second to the time.
                // #zh_CN 每缺少一种就固定增加1秒耗时。
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_20"))
                // #tr Tooltip_IndustrialMagicMatrix_21
                // # Gain up to 1145.14%% acceleration multiplier.
                // #zh_CN 最高获得 1145.14%% 的加速倍数。
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_21"))
                // #tr Tooltip_IndustrialMagicMatrix_22
                // # Putting EssentiaCell_Creative in the controller GUI doesn't cost essentia, but if it's a hero's proof,maybe a little bit of an incredible change...
                // #zh_CN 在控制器GUI放入魔导源质元件则无需消耗源质，但如果是某位英雄的证明或许会发生一点不可思议的变化...
                .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_22"))
                .addSeparator()
                .addInfo(StructureTooComplex)
                .addInfo(BLUE_PRINT_INFO)
                // #tr Tooltip_IndustrialMagicMatrix_23
                // # Infusion Provider
                // #zh_CN 注魔供应器
                // #tr Tooltip_IndustrialMagicMatrix_23.1
                // # §bAny magic mechanical block
                // #zh_CN §b任意魔法机械方块
                .addOtherStructurePart(TextEnums.tr("Tooltip_IndustrialMagicMatrix_23"),
                        TextEnums.tr("Tooltip_IndustrialMagicMatrix_23.1"))
                // #tr Tooltip_IndustrialMagicMatrix_24
                // # §bAny magic mechanical block
                // #zh_CN §b任意魔法机械方块
                .addInputBus(TextEnums.tr("Tooltip_IndustrialMagicMatrix_24"))
                .addOutputBus(TextEnums.tr("Tooltip_IndustrialMagicMatrix_24"))
                .addEnergyHatch(TextEnums.tr("Tooltip_IndustrialMagicMatrix_24"))
                // #tr Tooltip_IndustrialMagicMatrix_25
                // # Essentia diffusion unit
                // #zh_CN 源质扩散单元
                // #tr Tooltip_IndustrialMagicMatrix_25.1
                // # Each level provides 4^tier parallel
                // #zh_CN §b每级提供4^tier的并行
                .addOtherStructurePart(TextEnums.tr("Tooltip_IndustrialMagicMatrix_25"),
                        TextEnums.tr("Tooltip_IndustrialMagicMatrix_25.1"))
                .toolTipFinisher(ModName);
        return tt;
    }
    // spotless:on
    // end region

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagList list = new NBTTagList();
        for (String string : Research) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ResearchName", string);
            list.appendTag(tag);
        }
        aNBT.setInteger("mParallel", this.mParallel);
        aNBT.setDouble("mSpeedBonus", this.mSpeedBonus);
        aNBT.setTag("Research", list);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.Research.clear();
        for (int i = 0; i < aNBT.getTagList("Research", 10)
            .tagCount(); i++) {
            if (aNBT.getTagList("Research", 10)
                .getCompoundTagAt(i)
                .hasKey("ResearchName")) {
                this.Research.add(
                    aNBT.getTagList("Research", 10)
                        .getCompoundTagAt(i)
                        .getString("ResearchName"));
            }
        }
        this.mParallel = aNBT.getInteger("mParallel");
        this.mSpeedBonus = aNBT.getDouble("mSpeedBonus");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 100 == 0) {
            super.onPreTick(aBaseMetaTileEntity, aTick);
            if (aBaseMetaTileEntity.isServerSide()) {
                ArrayList<String> list = getResearchForPlayer(getPlayName());
                if ((this.Research == null && list != null)
                    || (list != null && !list.isEmpty() && this.Research.size() != list.size())) {
                    this.Research = list;
                }
            }
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_IndustrialMagicMatrix(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { TextureFactory.of(blockStoneDevice, 2), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { TextureFactory.of(blockStoneDevice, 2), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { TextureFactory.of(blockStoneDevice, 2) };
    }
}
