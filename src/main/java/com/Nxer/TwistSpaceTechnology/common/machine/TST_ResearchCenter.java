package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.util.CommonValues.VN;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Holder;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Created by danie_000 on 17.12.2016.
 */
@SuppressWarnings("unchecked")
public class TST_ResearchCenter extends GT_MetaTileEntity_MultiblockBase_EM implements ISurvivalConstructable {

    public static final String machine = "EM Machinery";
    public static final String crafter = "EM Crafting";
    // region variables
    private final ArrayList<GT_MetaTileEntity_Hatch_Holder> eHolders = new ArrayList<>();
    private boolean Training;
    private static final String assembly = "Assembly line";
    private ItemStack holdItem;
    private long computationRemaining, computationRequired;

    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.0"), // 1 - Classic/Data Hatches or
        // Computer casing
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.1"), // 2 - Holder Hatch
    };

    private String clientLocale = "en_US";
    // endregion

    // region structure
    private static final IStructureDefinition<TST_ResearchCenter> STRUCTURE_DEFINITION = IStructureDefinition
        .<TST_ResearchCenter>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "   ", " A ", " A ", "AAA", "AAA", "AAA", "AAA" },
                    { "AAA", "ACA", "ACA", "ACA", "BCB", "BCB", "BBB" },
                    { "   ", " C ", "   ", "   ", "ACA", "CCC", "DDD" },
                    { "   ", " E ", "   ", "   ", "A~A", "CCC", "DDD" },
                    { "   ", " C ", "   ", "   ", "ACA", "CCC", "DDD" },
                    { "AAA", "ACA", "ACA", "ACA", "BCB", "BCB", "BBB" },
                    { "   ", " A ", " A ", "AAA", "AAA", "AAA", "AAA" } }))
        .addElement('A', ofBlock(sBlockCasingsTT, 1))
        .addElement('B', ofBlock(sBlockCasingsTT, 2))
        .addElement('C', ofBlock(sBlockCasingsTT, 3))
        .addElement(
            'D',
            buildHatchAdder(TST_ResearchCenter.class)
                .atLeast(Energy.or(HatchElement.EnergyMulti), Maintenance, HatchElement.InputData)
                .casingIndex(textureOffset + 1)
                .dot(1)
                .buildAndChain(ofBlock(sBlockCasingsTT, 1)))
        .addElement('E', HolderHatchElement.INSTANCE.newAny(textureOffset + 3, 2))
        .build();
    // endregion

    public TST_ResearchCenter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_ResearchCenter(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_ResearchCenter(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Holder rack : filterValidMTEs(eHolders)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
        eHolders.clear();

        if (!structureCheck_EM("main", 1, 3, 4)) {
            return false;
        }

        for (GT_MetaTileEntity_Hatch_Holder rack : filterValidMTEs(eHolders)) {
            rack.getBaseMetaTileEntity()
                .setActive(iGregTechTileEntity.isActive());
        }
        return eHolders.size() == 1;
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        ItemStack controllerStack = getControllerSlot();

        if (!eHolders.isEmpty() && eHolders.get(0).mInventory[0] != null) {
            holdItem = eHolders.get(0).mInventory[0].copy();
            if (ItemList.Tool_DataOrb.isStackEqual(controllerStack, false, true)) {
                computationRequired = computationRemaining = 10_000_000 * 20L;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                eRequiredData = 1919810;// new Random(114514).nextInt(1000000, Integer.MAX_VALUE);
                eAmpereFlow = (short) (eRequiredData & 0xFFFF);
                lEUt = -(2 << 24);
                eHolders.get(0)
                    .getBaseMetaTileEntity()
                    .setActive(true);
                return SimpleCheckRecipeResult.ofSuccess("Training");
            } else {
                return CheckRecipeResultRegistry.NO_DATA_STICKS;
            }
        }
        holdItem = null;
        computationRequired = computationRemaining = 0;
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
            r.getBaseMetaTileEntity()
                .setActive(false);
        }
        return SimpleCheckRecipeResult.ofFailure("no_research_item");
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.research.name")) // Machine Type: Research
            // Station
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.0")) // Controller block of
            // the Research Station
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.1")) // Used to scan Data
            // Sticks for
            // Assembling Line Recipes
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.2")) // Needs to be fed with
            // computation to work
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.3")) // Does not consume the
            // item until
            // the Data Stick is written
            .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
            .addSeparator()
            .beginStructureBlock(3, 7, 7, false)
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.holder.tier.09.name"),
                translateToLocal("tt.keyword.Structure.CenterPillar"),
                2) // Object Holder: Center of the front pillar
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataConnector"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"),
                1) // Optical Connector: Any Computer Casing on the backside of the main body
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"), 1) // Energy Hatch:
            // Any Computer
            // Casing on the
            // backside of
            // the main body
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"), 1) // Maintenance
            // Hatch:
            // Any
            // Computer
            // Casing on
            // the
            // backside
            // of the
            // main body
            .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : filterValidMTEs(eEnergyMulti)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] { translateToLocalFormatted("tt.keyphrase.Energy_Hatches", clientLocale) + ":",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            (mEUt <= 0 ? translateToLocalFormatted("tt.keyphrase.Probably_uses", clientLocale) + ": "
                : translateToLocalFormatted("tt.keyphrase.Probably_makes", clientLocale) + ": ")
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(Math.abs(mEUt))
                + EnumChatFormatting.RESET
                + " EU/t "
                + translateToLocalFormatted("tt.keyword.at", clientLocale)
                + " "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(eAmpereFlow)
                + EnumChatFormatting.RESET
                + " A",
            translateToLocalFormatted("tt.keyphrase.Tier_Rating", clientLocale) + ": "
                + EnumChatFormatting.YELLOW
                + VN[getMaxEnergyInputTier_EM()]
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.GREEN
                + VN[getMinEnergyInputTier_EM()]
                + EnumChatFormatting.RESET
                + " "
                + translateToLocalFormatted("tt.keyphrase.Amp_Rating", clientLocale)
                + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(eMaxAmpereFlow)
                + EnumChatFormatting.RESET
                + " A",
            translateToLocalFormatted("tt.keyword.Problems", clientLocale) + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + translateToLocalFormatted("tt.keyword.Efficiency", clientLocale)
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            translateToLocalFormatted("tt.keyword.PowerPass", clientLocale) + ": "
                + EnumChatFormatting.BLUE
                + ePowerPass
                + EnumChatFormatting.RESET
                + " "
                + translateToLocalFormatted("tt.keyword.SafeVoid", clientLocale)
                + ": "
                + EnumChatFormatting.BLUE
                + eSafeVoid,
            translateToLocalFormatted("tt.keyphrase.Computation_Available", clientLocale) + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(eAvailableData)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(eRequiredData)
                + EnumChatFormatting.RESET,
            translateToLocalFormatted("tt.keyphrase.Computation_Remaining", clientLocale) + ":",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(computationRemaining / 20L)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(computationRequired / 20L) };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3],
                new TT_RenderedExtendedFacingTexture(
                    aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                        : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3] };
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
            r.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    @Override
    protected void extraExplosions_EM() {
        for (MetaTileEntity tTileEntity : eHolders) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(V[9]);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("eComputationRemaining", computationRemaining);
        aNBT.setLong("eComputationRequired", computationRequired);
        if (holdItem != null) {
            aNBT.setTag("eHold", holdItem.writeToNBT(new NBTTagCompound()));
        } else {
            aNBT.removeTag("eHold");
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        computationRemaining = aNBT.getLong("eComputationRemaining");
        computationRequired = aNBT.getLong("eComputationRequired");
        if (aNBT.hasKey("eHold")) {
            holdItem = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("eHold"));
        } else {
            holdItem = null;
        }
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
            r.getBaseMetaTileEntity()
                .setActive(false);
        }
        computationRequired = computationRemaining = 0;
        holdItem = null;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (computationRemaining > 0) {
                if (holdItem != null) {
                    if (ItemList.Tool_DataOrb.isStackEqual(mInventory[1], false, true)) {
                        Training = true;

                    }
                }
                if (!Training) {
                    holdItem = null;
                    computationRequired = computationRemaining = 0;
                    mMaxProgresstime = 0;
                    mEfficiencyIncrease = 0;
                    for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
                        r.getBaseMetaTileEntity()
                            .setActive(false);
                    }
                }
            } else Training = false;
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (computationRemaining <= 0) {
            computationRemaining = 0;
            mProgresstime = mMaxProgresstime;
            Training = false;
            return true;
        } else {
            computationRemaining -= eAvailableData;
            mProgresstime = 1;
            return super.onRunningTick(aStack);
        }
    }

    public final boolean addHolderToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Holder) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eHolders.add((GT_MetaTileEntity_Hatch_Holder) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        super.onRightclick(aBaseMetaTileEntity, aPlayer);

        if (!aBaseMetaTileEntity.isClientSide() && aPlayer instanceof EntityPlayerMP) {
            try {
                EntityPlayerMP player = (EntityPlayerMP) aPlayer;
                clientLocale = (String) FieldUtils.readField(player, "translator", true);
            } catch (Exception e) {
                clientLocale = "en_US";
            }
        } else {
            return true;
        }
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("hasProblems", (getIdealStatus() - getRepairStatus()) > 0);
        tag.setFloat("efficiency", mEfficiency / 100.0F);
        tag.setBoolean("incompleteStructure", (getBaseMetaTileEntity().getErrorDisplayID() & 64) != 0);
        tag.setLong("computation", (computationRequired - computationRemaining) / 20L);
        tag.setLong("computationRequired", computationRequired / 20L);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("incompleteStructure")) {
            currentTip.add(RED + "** INCOMPLETE STRUCTURE **" + RESET);
        }
        currentTip.add(
            (tag.getBoolean("hasProblems") ? (RED + "** HAS PROBLEMS **") : GREEN + "Running Fine") + RESET
                + "  Efficiency: "
                + tag.getFloat("efficiency")
                + "%");

        currentTip.add(
            String.format(
                "Computation: %,d / %,d",
                tag.getInteger("computation"),
                tag.getInteger("computationRequired")));
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 3, 4, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stackSize, 1, 3, 4, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<TST_ResearchCenter> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    private enum HolderHatchElement implements IHatchElement<TST_ResearchCenter> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_MetaTileEntity_Hatch_Holder.class);
        }

        @Override
        public IGT_HatchAdder<? super TST_ResearchCenter> adder() {
            return TST_ResearchCenter::addHolderToMachineList;
        }

        @Override
        public long count(TST_ResearchCenter t) {
            return t.eHolders.size();
        }
    }
}
