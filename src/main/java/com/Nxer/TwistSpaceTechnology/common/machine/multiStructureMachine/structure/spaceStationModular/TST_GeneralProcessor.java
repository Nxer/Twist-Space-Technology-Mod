package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.structure.spaceStationModular;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.GT_TileEntity_MultiStructureMachine;
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

    public MTEEnhancedMultiBlockBase<?> monitor = null;

    public static IStructureDefinition<TST_GeneralProcessor> structureDefinition;

    public Integer structureCount = 0;
    public static HashMap<Item, MTEEnhancedMultiBlockBase<?>> machineSupport = new HashMap<>();

    protected TST_GeneralProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_GeneralProcessor(String mName) {
        super(mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return monitor.getRecipeMap();
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    protected float getSpeedBonus() {
        return 1000;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    public IStructureDefinition<TST_GeneralProcessor> getStructureDefinition() {
        return null;
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
    protected MultiblockTooltipBuilder createTooltip() {
        return null;
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

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_GeneralProcessor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }

    public static void loadMachineSupport() {
        machineSupport
            .put(ItemList.Machine_Multi_LargeChemicalReactor.getItem(), new MTELargeChemicalReactor("monitor"));
    }
}
