package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TstUtils.setStackSize;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings10;
import static gregtech.api.GregTechAPI.sBlockCasings8;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.api.random.RandomPackageFactory;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.common.blocks.BlockCasings10;
import tectech.thing.block.BlockGodforgeGlass;

public class TST_SuperWaterPurifier extends GTCM_MultiMachineBase<TST_SuperWaterPurifier> {

    // region Class Constructor
    public TST_SuperWaterPurifier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_SuperWaterPurifier(String aName) {
        super(aName);
    }

    // endregion

    // region Processing Logic
    private int mCasing = 0;
    private static RandomPackageFactory<FluidStack> fluidRandomGetter;
    private final static int amountFluidOutput = 1_000;
    private static FluidStack UUM;
    private static FluidStack UUMC;

    public static void initStatics() {
        fluidRandomGetter = RandomPackageFactory.<FluidStack>builder()
            .add(Materials.Grade1PurifiedWater.getFluid(amountFluidOutput), 12d)
            .add(Materials.Grade2PurifiedWater.getFluid(amountFluidOutput), 12d)
            .add(Materials.Grade3PurifiedWater.getFluid(amountFluidOutput), 12d)
            .add(Materials.Grade4PurifiedWater.getFluid(amountFluidOutput), 12d)
            .add(Materials.Grade5PurifiedWater.getFluid(amountFluidOutput), 12d)
            .add(Materials.Grade6PurifiedWater.getFluid(amountFluidOutput), 12d)
            .add(Materials.Grade7PurifiedWater.getFluid(amountFluidOutput), 12d)
            .add(Materials.Grade8PurifiedWater.getFluid(amountFluidOutput), 12d)
            .add(Materials.StableBaryonicMatter.getFluid(amountFluidOutput), 4d)
            .build(new FluidStack[0]);

        if (UUM == null) UUM = Materials.UUMatter.getFluid(1_000);
        if (UUMC == null) UUMC = MaterialPool.ConcentratedUUMatter.getFluidOrGas(1_000);
    }

    @Override
    public ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                FluidStack findFluid = null;
                long inputEut = getMaxInputEu();
                long fluidAmount = 0;
                int basicFluidUsage = 1_000;
                int parallel = (int) Math.min(getTrueParallel(), inputEut / TierEU.RECIPE_UMV);
                boolean useUUMC = false;
                if (inputEut < TierEU.RECIPE_UMV) {
                    return CheckRecipeResultRegistry.insufficientPower(TierEU.RECIPE_UMV);
                }

                // Inputs
                ArrayList<FluidStack> inputFluids = getStoredFluids();
                if (inputFluids.isEmpty()) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                // Find needed fluid
                for (FluidStack fluid : inputFluids) {
                    if (UUM.isFluidEqual(fluid)) {
                        findFluid = UUM.copy();
                        break;
                    }

                    if (UUMC.isFluidEqual(fluid)) {
                        findFluid = UUMC.copy();
                        basicFluidUsage /= 1000;
                        useUUMC = true;
                        break;
                    }
                }
                if (findFluid == null) return CheckRecipeResultRegistry.NO_RECIPE;

                // Get fluid amount
                for (FluidStack fluid : inputFluids) {
                    if (findFluid.isFluidEqual(fluid)) {
                        fluidAmount += fluid.amount;
                    }
                }

                parallel = (int) Math.min(parallel, fluidAmount / basicFluidUsage);

                if (parallel <= 0) return CheckRecipeResultRegistry.NO_RECIPE;

                findFluid.amount = parallel * basicFluidUsage;
                if (!depleteInput(findFluid, true)) return CheckRecipeResultRegistry.NO_RECIPE;

                // Outputs
                List<FluidStack> outputs = new ArrayList<>();
                int outputStack = useUUMC ? 6 : 3;
                Map<Fluid, Long> cacheOutputs = new HashMap<>();
                for (int i = 0; i < outputStack; i++) {
                    FluidStack t = fluidRandomGetter.getOne()
                        .copy();
                    cacheOutputs.merge(t.getFluid(), (long) parallel * t.amount, Long::sum);
                    t.amount *= parallel;
                }

                // safe over 2.1G output
                for (Map.Entry<Fluid, Long> o : cacheOutputs.entrySet()) {
                    FluidStack f = new FluidStack(o.getKey(), 1);
                    long amount = o.getValue();
                    if (amount <= Integer.MAX_VALUE) {
                        f.amount = (int) amount;
                        outputs.add(f);
                    } else {
                        while (amount > Integer.MAX_VALUE) {
                            outputs.add(setStackSize(f.copy(), Integer.MAX_VALUE));
                            amount -= Integer.MAX_VALUE;
                        }
                        if (amount > 0) {
                            outputs.add(setStackSize(f.copy(), (int) amount));
                        }
                    }
                }

                outputFluids = outputs.toArray(new FluidStack[0]);

                VoidProtectionHelper voidProtection = new VoidProtectionHelper().setMachine(machine)
                    .setFluidOutputs(outputFluids)
                    .build();

                if (voidProtection.isFluidFull()) {
                    return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
                }

                depleteInput(findFluid);
                duration = 1_200;
                calculatedEut = -TierEU.RECIPE_UMV * parallel;

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.SuperWaterPurifierVisualRecipeMap;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        maxParallel = 2_000_000;
        if (!checkPiece(STRUCTURE_PIECE, horizontalOffSet, verticalOffSet, depthOffSet)) return false;

        return this.mCasing >= 45;
    }

    // endregion

    // region Structure
    // spotless:off
    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.buildPiece(STRUCTURE_PIECE, itemStack, b, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(STRUCTURE_PIECE, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    private static final String STRUCTURE_PIECE = "main";

    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 15;
    private final int depthOffSet = 0;

    private final int mainTextureID = ((BlockCasings10) sBlockCasings10).getTextureIndex(9);
    private static IStructureDefinition<TST_SuperWaterPurifier> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_SuperWaterPurifier> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_SuperWaterPurifier>builder()
                .addShape(STRUCTURE_PIECE,
                    transpose(shape))
                .addElement(
                    'A',
                    ofChain(
                        buildHatchAdder(TST_SuperWaterPurifier.class)
                            .atLeast(InputHatch, InputBus, OutputBus, OutputHatch, Energy, ExoticEnergy)
                            .casingIndex(mainTextureID)
                            .dot(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(sBlockCasings10, 9))))
                .addElement('B',ofBlock(sBlockCasings8, 14))
                .addElement('C',ofBlock(sBlockCasingsTT,10))
                .addElement('D', ofFrame(MaterialsUEVplus.SixPhasedCopper))
                .addElement('E', ofBlock(GodforgeCasings, 3))
                .addElement('F',ofBlock(BlockGodforgeGlass.INSTANCE, 0))

                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private final String[][] shape = new String[][]{
        {"               ","               ","               ","               ","               ","               ","               ","               ","    DBBBBB     ","   BBBBBBBBB   ","  BBBBBBBBBBB  "," BBBBBBBBBBBBB "," BBBBBBBBBBBBBD","BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB","DBBBBBBBBBBBBB "," BBBBBBBBBBBBB ","  BBBBBBBBBBB  ","   BBBBBBBBB   ","     BBBBBD    "},
        {"               ","               ","               ","               ","               ","               ","               ","               ","               ","   D  FFF      ","    FF   FF    ","   F       F D ","  F         F  ","  F         F  "," F           F "," F     C     F "," F           F ","  F         F  ","  F         F  "," D F       F   ","    FF   FF    ","      FFF  D   ","               "},
        {"               ","               ","               ","               ","               ","               ","               ","               ","               ","       F       ","  D FFF FFF D  ","   F       F   ","  F         F  ","  F         F  ","  F         F  "," F     C     F ","  F         F  ","  F         F  ","  F         F  ","   F       F   ","  D FFF FFF D  ","       F       ","               "},
        {"               ","               ","               ","               ","               ","               ","               ","               ","               ","           D   ","     FFFFF     "," D FF     FF   ","   F       F   ","  F         F  ","  F         F  ","  F    C    F  ","  F         F  ","  F         F  ","   F       F   ","   FF     FF D ","     FFFFF     ","   D           ","               "},
        {"               ","               ","               ","               ","               ","               ","               ","               ","          D    ","               ","      FFF      ","    FF   FF    ","D  F       F   ","   F       F   ","  F         F  ","  F    C    F  ","  F         F  ","   F       F   ","   F       F  D","    FF   FF    ","      FFF      ","               ","    D          "},
        {"               ","               ","               ","               ","               ","               ","               ","               ","        DD     ","               ","               ","     FFFFF     ","    F     F    ","D  F       F   ","D  F       F   ","   F   C   F   ","   F       F  D","   F       F  D","    F     F    ","     FFFFF     ","               ","               ","     DD        "},
        {"               ","               ","               ","               ","               ","               ","               ","               ","       D       ","               ","               ","      FFF      ","     F   F     ","    F     F    ","   F       F   ","D  F   C   F  D","   F       F   ","    F     F    ","     F   F     ","      FFF      ","               ","               ","       D       "},
        {"               ","               ","               ","               ","               ","               ","               ","               ","     DD        ","               ","               ","               ","      FFF      ","     F   F    D","    F     F   D","    F  C  F    ","D   F     F    ","D    F   F     ","      FFF      ","               ","               ","               ","        DD     "},
        {"               ","               ","       D       ","               ","               ","               ","               ","               ","    D          ","               ","               ","               ","              D","      FFF      ","     F   F     ","     F C F     ","     F   F     ","      FFF      ","D              ","               ","               ","               ","          D    "},
        {"               ","               ","       D       ","               ","               ","               ","               ","               ","               ","   D           ","               ","             D ","      FFF      ","     F   F     ","    F     F    ","    F  C  F    ","    F     F    ","     F   F     ","      FFF      "," D             ","               ","           D   ","               "},
        {"               ","               ","       D       ","               ","               ","               ","               ","               ","               ","               ","  D         D  ","      FFF      ","     F   F     ","    F     F    ","   F       F   ","   F   C   F   ","   F       F   ","    F     F    ","     F   F     ","      FFF      ","  D         D  ","               ","               "},
        {"               ","      DAD      ","      AAA      ","      DAD      ","               ","               ","               ","               ","               ","           D   ","               "," D   FFFFF     ","    F     F    ","   F       F   ","   F       F   ","   F   C   F   ","   F       F   ","   F       F   ","    F     F    ","     FFFFF   D ","               ","   D           ","               "},
        {"               ","      DAD      ","      ACA      ","      DAD      ","               ","               ","               ","               ","          D    ","               ","      FFF      ","    FF   FF    ","D  F       F   ","   F       F   ","  F         F  ","  F    C    F  ","  F         F  ","   F       F   ","   F       F  D","    FF   FF    ","      FFF      ","               ","    D          "},
        {"               ","      DAD      ","      ACA      ","      DAD      ","               ","               ","               ","               ","        DD     ","               ","     FFFFF     ","   FF     FF   ","   F       F   ","D F         F  ","D F         F  ","  F    C    F  ","  F         F D","  F         F D","   F       F   ","   FF     FF   ","     FFFFF     ","               ","     DD        "},
        {"     DAAAD     ","     AAAAA     ","     AACAA     ","     AAAAA     ","     DAAAD     ","               ","               ","               ","       D       ","       F       ","    FFF FFF    ","   F       F   ","  F         F  ","  F         F  ","  F         F  ","DF     C     FD","  F         F  ","  F         F  ","  F         F  ","   F       F   ","    FFF FFF    ","       F       ","       D       "},
        {"     DA~AD     ","     AEEEA     ","     AECEA     ","     AEEEA     ","     DAAAD     ","               ","               ","               ","     DD        ","      FFF      ","    FF   FF    ","   F       F   ","  F         F  ","  F         F D"," F           FD"," F     C     F ","DF           F ","D F         F  ","  F         F  ","   F       F   ","    FF   FF    ","      FFF      ","        DD     "},
        {"     DAAAD     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     DAAAD     ","               ","               ","               ","   DDBBBBB     ","   BBBBBBBBB   ","  BBBBBBBBBBB  "," BBBBBBBBBBBBBD"," BBBBBBBBBBBBBD","BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB","BBBBBBBBBBBBBBB","DBBBBBBBBBBBBB ","DBBBBBBBBBBBBB ","  BBBBBBBBBBB  ","   BBBBBBBBB   ","     BBBBBDD   "}
    };

    	/*
	Blocks:
A -> ofBlock...(gt.blockcasings10, 9, ...);
B -> ofBlock...(gt.blockcasings8, 14, ...);
C -> ofBlock...(gt.blockcasingsTT, 10, ...);
D -> ofBlock...(gt.blockframes, 147, ...);
E -> ofBlock...(gt.godforgecasing, 3, ...);
F -> ofBlock...(tile.spatiallyTranscendentGravitationalLens, 0, ...);
	 */
    // spotless:on
    // endregion

    // region Overrides
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.Tooltip_SuperwaterPurifier_MachineType)
            .addInfo(TextLocalization.Tooltip_SuperWaterPurifier_00)
            .addInfo(TextLocalization.Tooltip_SuperWaterPurifier_01)
            .addInfo(TextLocalization.Tooltip_SuperWaterPurifier_02)
            .addInfo(TextLocalization.Tooltip_SuperWaterPurifier_03)
            .addInfo(TextLocalization.Tooltip_SuperWaterPurifier_04)
            .addInfo(TextEnums.Author_EvgenWarGold.getText())
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addDynamoHatch(TextLocalization.textUseBlueprint, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_SuperWaterPurifier(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(mainTextureID),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(mainTextureID), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(mainTextureID) };
    }
    // endregion

}
