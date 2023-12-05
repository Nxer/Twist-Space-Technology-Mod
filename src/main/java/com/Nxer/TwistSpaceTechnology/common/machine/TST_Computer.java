package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_NEUTRAL;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.util.CommonValues.MULTI_CHECK_AT;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.mechanics.dataTransport.QuantumDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_switch;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.IGT_HatchAdder;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class TST_Computer extends GT_MetaTileEntity_MultiblockBase_EM implements ISurvivalConstructable {

    // region variables
    private final ArrayList<GT_MetaTileEntity_Hatch_Rack> eRacks = new ArrayList<>();

    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    // endregion

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.computer.hint.0"), // 1 - Classic/Data Hatches or
        // Computer casing
        translateToLocal("gt.blockmachines.multimachine.em.computer.hint.1"), // 2 - Rack Hatches or Advanced
        // computer casing
    };

    private static final IStructureDefinition<TST_Computer> STRUCTURE_DEFINITION = IStructureDefinition
        .<TST_Computer>builder()
        .addShape("front", transpose(new String[][] { { " AA" }, { " AA" }, { " ~A" }, { " AA" } }))
        .addShape("cap", transpose(new String[][] { { "-CB" }, { " DD" }, { " DD" }, { "-CB" } }))
        .addShape("slice", transpose(new String[][] { { "-CB" }, { " ED" }, { " ED" }, { "-CB" } }))
        .addShape("back", transpose(new String[][] { { " AA" }, { " AA" }, { " AA" }, { " AA" } }))
        .addElement('B', ofBlock(sBlockCasingsTT, 1))
        .addElement('C', ofBlock(sBlockCasingsTT, 2))
        .addElement('D', ofBlock(sBlockCasingsTT, 3))
        .addElement(
            'A',
            buildHatchAdder(TST_Computer.class)
                .atLeast(
                    Energy.or(HatchElement.EnergyMulti),
                    InputBus,
                    OutputBus,
                    InputHatch,
                    OutputHatch,
                    HatchElement.OutputData)
                .casingIndex(textureOffset + 1)
                .dot(1)
                .buildAndChain(ofBlock(sBlockCasingsTT, 1)))
        .addElement('E', ofChain(RackHatchElement.INSTANCE.newAny(textureOffset + 3, 2), ofBlock(sBlockCasingsTT, 3)))
        .build();
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn overclock, overvolt;
    protected Parameters.Group.ParameterOut maxCurrentTemp, availableData;

    private static final INameFunction<TST_Computer> OC_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.0"); // Overclock ratio
    private static final INameFunction<TST_Computer> OV_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.1"); // Overvoltage ratio
    private static final INameFunction<TST_Computer> MAX_TEMP_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.0"); // Current max. heat
    private static final INameFunction<TST_Computer> COMPUTE_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.1"); // Produced computation
    private static final IStatusFunction<TST_Computer> OC_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 1, 3);
    private static final IStatusFunction<TST_Computer> OV_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), .7, .8, 1.2, 2);
    private static final IStatusFunction<TST_Computer> MAX_TEMP_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), -10000, 0, 0, 5000);
    private static final IStatusFunction<TST_Computer> COMPUTE_STATUS = (base, p) -> {
        if (base.eAvailableData < 0) {
            return STATUS_TOO_LOW;
        }
        if (base.eAvailableData == 0) {
            return STATUS_NEUTRAL;
        }
        return STATUS_OK;
    };
    // endregion

    public TST_Computer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eCertainMode = 5;
        eCertainStatus = -128; // no-brain value
    }

    public TST_Computer(String aName) {
        super(aName);
        eCertainMode = 5;
        eCertainStatus = -128; // no-brain value
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_Computer(mName);
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0);
        overclock = hatch_0.makeInParameter(0, 1, OC_NAME, OC_STATUS);
        overvolt = hatch_0.makeInParameter(1, 1, OV_NAME, OV_STATUS);
        maxCurrentTemp = hatch_0.makeOutParameter(0, 0, MAX_TEMP_NAME, MAX_TEMP_STATUS);
        availableData = hatch_0.makeOutParameter(1, 0, COMPUTE_NAME, COMPUTE_STATUS);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
        eRacks.clear();
        if (!structureCheck_EM("front", 1, 2, 0)) {
            return false;
        }
        if (!structureCheck_EM("cap", 1, 2, -1)) {
            return false;
        }
        byte offset = -2, totalLen = 4;
        while (offset > -16) {
            if (!structureCheck_EM("slice", 1, 2, offset)) {
                break;
            }
            totalLen++;
            offset--;
        }
        if (totalLen > 17) {
            return false;
        }
        if (!structureCheck_EM("cap", 1, 2, ++offset)) {
            return false;
        }
        if (!structureCheck_EM("back", 1, 2, --offset)) {
            return false;
        }
        eCertainMode = (byte) Math.min(totalLen / 3, 5);
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(iGregTechTileEntity.isActive());
        }
        return eUncertainHatches.size() == 1;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setDouble("computation", availableData.get());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (availableData != null) {
            availableData.set(aNBT.getDouble("computation"));
            eAvailableData = (long) availableData.get();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && mMachine
            && !aBaseMetaTileEntity.isActive()
            && aTick % 20 == MULTI_CHECK_AT) {
            double maxTemp = 0;
            for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
                if (rack.heat > maxTemp) {
                    maxTemp = rack.heat;
                }
            }
            maxCurrentTemp.set(maxTemp);
        }
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        parametrization.setToDefaults(false, true);
        eAvailableData = 0;
        double maxTemp = 0;
        double overClockRatio = overclock.get();
        double overVoltageRatio = overvolt.get();
        if (Double.isNaN(overClockRatio) || Double.isNaN(overVoltageRatio)) {
            return SimpleCheckRecipeResult.ofFailure("no_computing");
        }
        if (overclock.getStatus(true).isOk && overvolt.getStatus(true).isOk) {
            float eut = V[8] * (float) overVoltageRatio * (float) overClockRatio;
            if (eut < Integer.MAX_VALUE - 7) {
                mEUt = -(int) eut;
            } else {
                mEUt = -(int) V[8];
                return CheckRecipeResultRegistry.POWER_OVERFLOW;
            }
            short thingsActive = 0;
            int rackComputation;

            for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
                if (rack.heat > maxTemp) {
                    maxTemp = rack.heat;
                }
                rackComputation = rack.tickComponents((float) overClockRatio, (float) overVoltageRatio);
                if (rackComputation > 0) {
                    eAvailableData += rackComputation;
                    thingsActive += 4;
                }
                rack.getBaseMetaTileEntity()
                    .setActive(true);
            }

            for (GT_MetaTileEntity_Hatch_InputData di : eInputData) {
                if (di.q != null) // ok for power losses
                {
                    thingsActive++;
                }
            }

            if (thingsActive > 0 && eCertainStatus == 0) {
                thingsActive += eOutputData.size();
                eAmpereFlow = 1 + (thingsActive >> 2);
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return SimpleCheckRecipeResult.ofSuccess("computing");
            } else {
                eAvailableData = 0;
                mEUt = -(int) V[8];
                eAmpereFlow = 1;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return SimpleCheckRecipeResult.ofSuccess("no_computing");
            }
        }
        return SimpleCheckRecipeResult.ofFailure("no_computing");
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eOutputData.isEmpty()) {
            Vec3Impl pos = new Vec3Impl(
                getBaseMetaTileEntity().getXCoord(),
                getBaseMetaTileEntity().getYCoord(),
                getBaseMetaTileEntity().getZCoord());

            QuantumDataPacket pack = new QuantumDataPacket(eAvailableData / eOutputData.size()).unifyTraceWith(pos);
            if (pack == null) {
                return;
            }
            for (GT_MetaTileEntity_Hatch_InputData hatch : eInputData) {
                if (hatch.q == null || hatch.q.contains(pos)) {
                    continue;
                }
                pack = pack.unifyPacketWith(hatch.q);
                if (pack == null) {
                    return;
                }
            }

            for (GT_MetaTileEntity_Hatch_OutputData o : eOutputData) {
                o.q = pack;
            }
        }
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.computer.name")) // Machine Type: Quantum
            // Computer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.0")) // Controller block of
            // the Quantum Computer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.1")) // Used to generate
            // computation (and heat)
            .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
            .addSeparator()
            .beginVariableStructureBlock(2, 2, 4, 4, 5, 16, false)
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.certain.tier.07.name"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"),
                1) // Uncertainty Resolver: Any Computer Casing on first or last slice
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataConnector"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"),
                1) // Optical Connector: Any Computer Casing on first or last slice
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.rack.tier.08.name"),
                translateToLocal("tt.keyword.Structure.AnyAdvComputerCasingExceptOuter"),
                2) // Computer Rack: Any Advanced Computer Casing, except the outer ones
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.param.tier.05.name"),
                translateToLocal("tt.keyword.Structure.Optional") + " "
                    + translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"),
                2) // Parametrizer: (optional) Any Computer Casing on first or last slice
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"), 1) // Energy
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingFirstOrLastSlice"), 1) // Maintenance
            .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3],
                new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3] };
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return GT_MetaTileEntity_EM_switch.activitySound;
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    @Override
    protected void extraExplosions_EM() {
        for (MetaTileEntity tTileEntity : eRacks) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(V[9]);
        }
    }

    @Override
    protected long getAvailableData_EM() {
        return eAvailableData;
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        eAvailableData = 0;
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    @Override
    protected void afterRecipeCheckFailed() {
        super.afterRecipeCheckFailed();
        for (GT_MetaTileEntity_Hatch_Rack rack : filterValidMTEs(eRacks)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    public final boolean addRackToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Rack) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eRacks.add((GT_MetaTileEntity_Hatch_Rack) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("front", 1, 2, 0, stackSize, hintsOnly);
        structureBuild_EM("cap", 1, 2, -1, stackSize, hintsOnly);

        byte offset = -2;
        for (int rackSlices = Math.min(stackSize.stackSize, 12); rackSlices > 0; rackSlices--) {
            structureBuild_EM("slice", 1, 2, offset--, stackSize, hintsOnly);
        }

        structureBuild_EM("cap", 1, 2, offset--, stackSize, hintsOnly);
        structureBuild_EM("back", 1, 2, offset, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int built;
        built = survivialBuildPiece("front", stackSize, 1, 2, 0, elementBudget, source, actor, false, true);
        if (built >= 0) return built;
        built = survivialBuildPiece("cap", stackSize, 1, 2, -1, elementBudget, source, actor, false, true);
        if (built >= 0) return built;

        byte offset = -2;
        for (int rackSlices = Math.min(stackSize.stackSize, 12); rackSlices > 0; rackSlices--) {
            built = survivialBuildPiece("slice", stackSize, 1, 2, offset--, elementBudget, source, actor, false, true);
            if (built >= 0) return built;
        }
        built = survivialBuildPiece("cap", stackSize, 1, 2, offset--, elementBudget, source, actor, false, true);
        if (built >= 0) return built;
        return survivialBuildPiece("back", stackSize, 1, 2, offset, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<TST_Computer> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    private enum RackHatchElement implements IHatchElement<TST_Computer> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_MetaTileEntity_Hatch_Rack.class);
        }

        @Override
        public IGT_HatchAdder<? super TST_Computer> adder() {
            return TST_Computer::addRackToMachineList;
        }

        @Override
        public long count(TST_Computer t) {
            return t.eRacks.size();
        }
    }
}
