package com.Nxer.TwistSpaceTechnology.system.CircuitConverter.machines;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_CircuitConverter_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_CircuitConverter_2_01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_CircuitConverter_Controller;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_CircuitConverter_MachineType;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedEnergyHatch;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Tooltip_DoNotNeedMaintenance;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.textAnyCasing;
import static com.Nxer.TwistSpaceTechnology.util.Utils.isStackInvalid;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;

public class TST_CircuitConverter extends GTCM_MultiMachineBase<TST_CircuitConverter> {

    // region Class Constructor
    public TST_CircuitConverter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_CircuitConverter(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_CircuitConverter(this.mName);
    }

    // endregion

    // region Processing Logic

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // check input buses
        ArrayList<ItemStack> inputs = getStoredInputs();
        if (inputs.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        Set<ItemStack> outputs = new HashSet<>();
        // check every input
        for (ItemStack item : inputs) {
            if (isStackInvalid(item)) continue;
            ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(item);

            if (tPrefixMaterial == null || !tPrefixMaterial.hasValidPrefixMaterialData()) continue;
            if (tPrefixMaterial.mPrefix == OrePrefixes.circuit) {
                outputs.add(GT_OreDictUnificator.get(true, item, true));
                item.stackSize = 0;
            }
        }
        // inputs are consumed at this point
        updateSlots();

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;

        mOutputItems = outputs.toArray(new ItemStack[0]);

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        return mInputBusses.size() + mOutputBusses.size() <= 8;
    }
    // endregion

    // region Structure
    private static final String STRUCTURE_PIECE_MAIN = "mainCircuitConverter";
    private final int horizontalOffSet = 1;
    private final int verticalOffSet = 1;
    private final int depthOffSet = 0;

    public void construct(ItemStack stackSize, boolean hintsOnly) {
        repairMachine();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
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

    @Override
    public IStructureDefinition<TST_CircuitConverter> getStructureDefinition() {
        return STRUCTURE;
    }

    private final String[][] shapeMain = new String[][] { { "AAA", "AAA", "AAA" }, { "A~A", "A A", "AAA" },
        { "AAA", "AAA", "AAA" } };

    private final IStructureDefinition<TST_CircuitConverter> STRUCTURE = IStructureDefinition
        .<TST_CircuitConverter>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
        .addElement(
            'A',
            GT_HatchElementBuilder.<TST_CircuitConverter>builder()
                .atLeast(InputBus, OutputBus)
                .adder(TST_CircuitConverter::addInputBusOrOutputBusToMachineList)
                .dot(1)
                .casingIndex(16)
                .buildAndChain(GregTech_API.sBlockCasings2, 6))
        .build();

    // endregion

    // region General
    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(Tooltip_CircuitConverter_MachineType)
            .addInfo(Tooltip_CircuitConverter_Controller)
            .addInfo(Tooltip_CircuitConverter_01)
            .addSeparator()
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addStructureInfo(Tooltip_CircuitConverter_2_01)
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .addStructureInfo(Tooltip_DoNotNeedEnergyHatch)
            .addInputBus(textAnyCasing, 1)
            .addOutputBus(textAnyCasing, 1)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
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
}
