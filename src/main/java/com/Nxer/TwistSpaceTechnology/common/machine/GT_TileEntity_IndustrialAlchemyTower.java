package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static goodgenerator.loader.Loaders.magicCasing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static thaumcraft.common.config.ConfigBlocks.blockMetalDevice;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCRecipeTools;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
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
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumicenergistics.common.storage.EnumEssentiaStorageTypes;
import thaumicenergistics.common.tiles.TileInfusionProvider;

public class GT_TileEntity_IndustrialAlchemyTower extends GTCM_MultiMachineBase<GT_TileEntity_IndustrialAlchemyTower>
    implements ISidedInventory {

    private final ItemStack EssentiaCell_Creative = EnumEssentiaStorageTypes.Type_Creative.getCell();
    private final ItemStack ProofOfHeroes = GTCMItemList.ProofOfHeroes.get(1, 0);
    protected ArrayList<TileInfusionProvider> mTileInfusionProvider = new ArrayList<>();
    public static final CheckRecipeResult Essentia_InsentiaL = SimpleCheckRecipeResult
        .ofFailurePersistOnShutdown("Essentiainsentia");
    public static final CheckRecipeResult Research_not_completed = SimpleCheckRecipeResult
        .ofFailurePersistOnShutdown("Research_not_completed");

    private int mParallel;
    private double mSpeedBonus;

    public GT_TileEntity_IndustrialAlchemyTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_IndustrialAlchemyTower(String aName) {
        super(aName);
    }

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
                if (!ThaumcraftApiHelper.isResearchComplete(getUsername(), recipe1.getResearch()))
                    return Research_not_completed;
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
        };
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

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final int horizontalOffSet = 1;
    private final int verticalOffSet = 1;
    private final int depthOffSet = 0;
    // spotless:off
    private static final String[][] shape = new String[][]{{
        "AAA",
        "A~A",
        "AAA"
    },{
        "AAA",
        "A A",
        "AAA"
    },{
        "AAA",
        "AAA",
        "AAA"
    }};
    // spotless:on
    private static IStructureDefinition<GT_TileEntity_IndustrialAlchemyTower> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GT_TileEntity_IndustrialAlchemyTower> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_TileEntity_IndustrialAlchemyTower>builder()
                .addShape(STRUCTURE_PIECE_MAIN, shape)
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

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("CCR")
            .addInfo("test")
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo("Tc_traveler")
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mParallel", this.mParallel);
        aNBT.setDouble("mSpeedBonus", this.mSpeedBonus);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mParallel = aNBT.getInteger("mParallel");
        this.mSpeedBonus = aNBT.getDouble("mSpeedBonus");
        super.loadNBTData(aNBT);
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
