package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure.spaceStationModular;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.GT_TileEntity_MultiStructureMachine;
import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.MTELargeChemicalReactor;

public class TST_GeneralProcessor extends GT_TileEntity_MultiStructureMachine<TST_GeneralProcessor> {

    // region Class Constructor
    protected TST_GeneralProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_GeneralProcessor(String mName) {
        super(mName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_GeneralProcessor(this.mName);
    }
    // endregion

    // region Structure
    public static IStructureDefinition<TST_GeneralProcessor> structureDefinition;

    @Override
    public IStructureDefinition<TST_GeneralProcessor> getStructureDefinition() {
        return null;
    }
    // endregion

    // region Processing Logic
    public MTEEnhancedMultiBlockBase<?> monitor = null;
    public Integer structureCount = 0;
    public static HashMap<Item, MTEEnhancedMultiBlockBase<?>> machineSupport = new HashMap<>();

    @Override
    public RecipeMap<?> getRecipeMap() {
        return monitor.getRecipeMap();
    }

    @Override
    public UITexture[] getMachineModeIcons() {
        return new UITexture[0];
    }

    @Override
    public int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected float getSpeedBonus() {
        return 1000;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                return super.process();
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        return monitor.checkProcessing();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 100 == 0) {
            var machine = getStackInSlot(0).getItem();
            monitor = machineSupport.getOrDefault(machine, null);
            if (monitor == null) structureCount = 0;
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    public static void loadMachineSupport() {
        machineSupport
            .put(ItemList.Machine_Multi_LargeChemicalReactor.getItem(), new MTELargeChemicalReactor("monitor"));
    }

    // endregion

    // region Textures

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }

    // endregion

    // region Tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return null;
    }

    // endregion

}
