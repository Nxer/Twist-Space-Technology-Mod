package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.loader.Loaders.magicCasing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;
import static thaumcraft.common.lib.research.ResearchManager.getResearchForPlayer;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipeTools;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumicenergistics.common.storage.EnumEssentiaStorageTypes;
import thaumicenergistics.common.tiles.TileInfusionProvider;

public class GT_TileEntity_IndustrialAlchemyTower extends GTCM_MultiMachineBase<GT_TileEntity_IndustrialAlchemyTower>
    implements ISidedInventory {

    // region default value

    private int mParallel;
    private double mSpeedBonus;
    private final ItemStack EssentiaCell_Creative = EnumEssentiaStorageTypes.Type_Creative.getCell();
    private final ItemStack ProofOfHeroes = GTCMItemList.ProofOfHeroes.get(1, 0);
    protected ArrayList<TileInfusionProvider> mTileInfusionProvider = new ArrayList<>();
    protected ArrayList<String> Research = new ArrayList<>();
    public static final CheckRecipeResult Essentia_InsentiaL = SimpleCheckRecipeResult
        .ofFailurePersistOnShutdown("Essentiainsentia");
    public static final CheckRecipeResult Research_not_completed = SimpleCheckRecipeResult
        .ofFailurePersistOnShutdown("Research_not_completed");

    // endregion

    // region constructor

    public GT_TileEntity_IndustrialAlchemyTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_IndustrialAlchemyTower(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclockType(
                    isEnablePerfectOverclock() ? OverclockType.PerfectOverclock : OverclockType.NormalOverclock);
                return super.process();
            }

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                int Para = createParallelHelper(recipe).setConsumption(false)
                    .build()
                    .getCurrentParallel();
                ItemStack input1 = recipe.mInputs[0];
                ItemStack circuit = recipe.mInputs[1];
                String key = TCRecipeTools.toStringWithoutStackSize(input1);
                int circuitNum = circuit.getItemDamage();
                if (circuitNum <= 0) return CheckRecipeResultRegistry.NO_RECIPE;
                ArrayList<TCRecipeTools.CrucibleCraftingRecipe> recipes = TCRecipeTools.CCR.get(key);
                if (recipes.size() < circuitNum) return CheckRecipeResultRegistry.NO_RECIPE;
                TCRecipeTools.CrucibleCraftingRecipe recipe1 = recipes.get(circuitNum - 1);
                if (!isResearchComplete(recipe1.getResearch())) return Research_not_completed;
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

    // WIP
    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        return super.checkProcessing();
    }

    private String getUsername() {
        return this.getBaseMetaTileEntity()
            .getOwnerName();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.IndustrialAlchemyTowerRecipe;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1.0F;
    }

    @Override
    protected int getLimitedMaxParallel() {
        return getMaxParallelRecipes();
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 4;
    }

    // endregion

    @Override
    public String[] getInfoData() {
        return super.getInfoData();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    // region Structure

    // WIP
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final int horizontalOffSet = 1;
    private final int verticalOffSet = 1;
    private final int depthOffSet = 0;
    // spotless:off
    //WIP
    private static final String[][] shape = new String[][]{
        {"AAA","AAA","AAA"},
        {"A~A","A A","AAA"},
        {"AAA","AAA","AAA"}
    };
    // spotless:on
    private static IStructureDefinition<GT_TileEntity_IndustrialAlchemyTower> STRUCTURE_DEFINITION = null;

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
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

    // WIP
    @Override
    public IStructureDefinition<GT_TileEntity_IndustrialAlchemyTower> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_IndustrialAlchemyTower>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    ofChain(
                        HatchElementBuilder.<GT_TileEntity_IndustrialAlchemyTower>builder()
                            .atLeast(InputBus, OutputBus, Energy)
                            .adder(GT_TileEntity_IndustrialAlchemyTower::addToMachineList)
                            .casingIndex(1536)
                            .dot(1)
                            .build(),
                        ofBlock(magicCasing, 0),
                        ofTileAdder(GT_TileEntity_IndustrialAlchemyTower::addInfusionProvider, magicCasing, 0)))
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

    // WIP
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // #tr Tooltip_IndustrialAlchemyTower_MachineType
        // # Alchemy Tower
        // #zh_CN 工业炼金塔
        tt.addMachineType(TextEnums.tr("Tooltip_IndustrialAlchemyTower_MachineType"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .toolTipFinisher(ModName);
        return tt;
    }

    // endregion

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagList nbtTagList = new NBTTagList();
        for (String string : Research) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ResearchName", string);
            nbtTagList.appendTag(tag);
        }
        aNBT.setInteger("mParallel", this.mParallel);
        aNBT.setDouble("mSpeedBonus", this.mSpeedBonus);
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
                ArrayList<String> list = getResearchForPlayer(getUsername());
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
        return new GT_TileEntity_IndustrialAlchemyTower(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { TextureFactory.of(blockMetalDevice, 9), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { TextureFactory.of(blockMetalDevice, 9), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { TextureFactory.of(blockMetalDevice, 9) };
    }
}
