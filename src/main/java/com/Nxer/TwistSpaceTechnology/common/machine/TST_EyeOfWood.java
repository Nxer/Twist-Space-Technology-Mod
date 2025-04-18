package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import bwcrossmod.galacticgreg.VoidMinerUtility;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;

public class TST_EyeOfWood extends GTCM_MultiMachineBase<TST_EyeOfWood> {

    // region Class Constructor
    public TST_EyeOfWood(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_EyeOfWood(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_EyeOfWood(this.mName);
    }
    // endregion

    // region Processing Logic
    protected static VoidMinerUtility.DropMap dropMap = null;
    protected static VoidMinerUtility.DropMap extraDropMap = null;
    protected static float totalWeight = 0;
    private static boolean preGenerated = false;
    private static Fluid WATER;
    private static Fluid LAVA;
    private static final int STANDARD_WATER_BUCKET = ValueEnum.StandardWaterNeed_EyeOfWood;
    private static final int STANDARD_WATER_AMOUNT = STANDARD_WATER_BUCKET * 1000;
    private static final int STANDARD_LAVA_BUCKET = ValueEnum.StandardLavaNeed_EyeOfWood;
    private static final int STANDARD_LAVA_AMOUNT = STANDARD_LAVA_BUCKET * 1000;
    private static final int STANDARD_DIMENSION_ID = ValueEnum.StandardDimensionID_EyeOfWood;
    private static final double STANDARD_SUBSTRATE = Math
        .pow(2_000_000_000d, 1d / Math.max(STANDARD_WATER_BUCKET, STANDARD_LAVA_BUCKET));
    private int storedWater = 0;
    private int storedLava = 0;

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        // #tr getInfoData.StoredWater
        // # Stored Water
        // #zh_CN 已存储水
        ret[origin.length] = TextEnums.tr("getInfoData.StoredWater") + " : "
            + EnumChatFormatting.BLUE
            + storedWater
            + EnumChatFormatting.RESET
            + "L / "
            + STANDARD_WATER_AMOUNT
            + "L";
        // #tr getInfoData.StoredLava
        // # Stored Lava
        // #zh_CN 已存储岩浆
        ret[origin.length + 1] = TextEnums.tr("getInfoData.StoredLava") + " : "
            + EnumChatFormatting.RED
            + storedLava
            + EnumChatFormatting.RESET
            + "L / "
            + STANDARD_LAVA_AMOUNT
            + "L";
        // #tr getInfoData.SuccessChance
        // # Success Chance
        // #zh_CN 成功几率
        ret[origin.length + 2] = TextEnums.tr("getInfoData.SuccessChance") + " : "
            + EnumChatFormatting.GOLD
            + getSuccessChance()
            + " / 10000";
        return ret;
    }

    private void resetStored() {
        storedWater = 0;
        storedLava = 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("storedWater", storedWater);
        aNBT.setInteger("storedLava", storedLava);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        storedWater = aNBT.getInteger("storedWater");
        storedLava = aNBT.getInteger("storedLava");
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        if (dropMap == null || totalWeight == 0) {
            TwistSpaceTechnology.LOG.info("WARNING! Eye of Wood dropmap = null when checkProcessing !");
            return CheckRecipeResultRegistry.INTERNAL_ERROR;
        }

        int successChance = getSuccessChance();
        // TwistSpaceTechnology.LOG.info("success chance : " + successChance);
        resetStored();

        if (successChance > XSTR.XSTR_INSTANCE.nextInt(10000)) {
            // success
            mOutputItems = getItemOutputs();

        } else {
            // if fail
            // max 1200 * 30 * 7500 L Steam / 1200tick -> 112_500 EU/t in 100% Efficient , final multiply 25% fail
            // chance
            // min 1200 * 30 * 1 L Steam / 1200tick -> 15 EU/t in 100% Efficient , final multiply 99.99% fail chance
            mOutputFluids = new FluidStack[] { Materials.Water.getGas(36000L * successChance) };

        }

        updateSlots();
        mMaxProgresstime = ValueEnum.TicksPerProcessing_EyeOfWood;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private ItemStack[] getItemOutputs() {
        List<ItemStack> outputs = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ItemData oreData = GTOreDictUnificator.getItemData(generateOneStackOre());
            if (oreData == null) {
                TwistSpaceTechnology.LOG.info("EOW getItemOutputs error: oreData is null");
                return new ItemStack[0];
            }

            outputs.addAll(getOutputs(oreData.mMaterial.mMaterial, (int) oreData.mMaterial.mAmount));
        }

        if (outputs.isEmpty()) return new ItemStack[0];

        return outputs.toArray(new ItemStack[0]);
    }

    public List<ItemStack> getOutputs(Materials material, int multiplier) {
        List<ItemStack> outputs = new ArrayList<>();

        // check byproduct
        if (!material.mOreByProducts.isEmpty()) {
            // the basic output the material
            outputs.add(getDustStack(material, 64));
            if (material.mOreByProducts.size() == 1) {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null) continue;
                    outputs.add(getDustStack(byproduct, 48));
                }
            } else {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null || byproduct == Materials.Netherrack
                        || byproduct == Materials.Endstone
                        || byproduct == Materials.Stone) continue;

                    outputs.add(getDustStack(byproduct, 32));
                }
            }

        } else {
            outputs.add(getDustStack(material, 128));
        }

        // check gem style
        if (GTOreDictUnificator.get(OrePrefixes.gem, material, 1) != null) {
            if (GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1) != null) {
                // has gem style
                outputs.add(GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 16));
                outputs.add(GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 32));
                outputs.add(GTOreDictUnificator.get(OrePrefixes.gem, material, 32));

            } else {
                // just normal gem
                outputs.add(GTOreDictUnificator.get(OrePrefixes.gem, material, 64));
            }
        }

        if (multiplier > 1) {
            for (ItemStack out : outputs) {
                out.stackSize *= multiplier;
            }
        }

        return outputs;
    }

    public ItemStack getDustStack(Materials material, int amount) {
        return GTUtility.copyAmountUnsafe(amount, GTOreDictUnificator.get(OrePrefixes.dust, material, 1));
    }

    /**
     * 成功率 = {7500 - 7499 * [1 - 1/(S^dW * S^dL)]} / 10000
     * <P>
     * S ≈ 1.087
     * </P>
     * <P>
     * dW, dL 分别为已存储的水量与需求量(256,000L)的差值除以1000和已存储的岩浆量和需求量的差值除以1000, 并向下取整.
     * </P>
     */
    private int getSuccessChance() {
        if (storedWater == STANDARD_WATER_AMOUNT && storedLava == STANDARD_LAVA_AMOUNT) {
            return 7500;
        }

        int waterBucketDifference = Math.abs(storedWater - STANDARD_WATER_AMOUNT) / 1000;
        int lavaBucketDifference = Math.abs(storedLava - STANDARD_LAVA_AMOUNT) / 1000;
        if (waterBucketDifference >= STANDARD_WATER_BUCKET || lavaBucketDifference >= STANDARD_LAVA_BUCKET) {
            return 1;
        }

        double waterMultiplier = 1d / Math.pow(STANDARD_SUBSTRATE, waterBucketDifference);
        double lavaMultiplier = 1d / Math.pow(STANDARD_SUBSTRATE, lavaBucketDifference);

        return (int) (7500 - 7499 * (1d - waterMultiplier * lavaMultiplier));
    }

    private ItemStack generateOneStackOre() {
        float currentWeight = 0.f;
        while (true) {
            float randomNumber = XSTR.XSTR_INSTANCE.nextFloat() * totalWeight;
            for (Map.Entry<GTUtility.ItemId, Float> entry : dropMap.getInternalMap()
                .entrySet()) {
                currentWeight += entry.getValue();
                if (randomNumber < currentWeight) return entry.getKey()
                    .getItemStack();
            }
            for (Map.Entry<GTUtility.ItemId, Float> entry : extraDropMap.getInternalMap()
                .entrySet()) {
                currentWeight += entry.getValue();
                if (randomNumber < currentWeight) return entry.getKey()
                    .getItemStack();
            }
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        if (aBaseMetaTileEntity.isServerSide()) {
            World world = aBaseMetaTileEntity.getWorld();
            int dimID = world.provider.dimensionId;

            if (dimID != STANDARD_DIMENSION_ID) {
                explodeMultiblock();
                return;
            }

            if (!preGenerated) {
                initDropMap();
                WATER = Materials.Water.mFluid;
                LAVA = Materials.Lava.mFluid;
                preGenerated = true;
            }
        }

    }

    protected void initDropMap() {
        dropMap = new VoidMinerUtility.DropMap();
        extraDropMap = new VoidMinerUtility.DropMap();
        int id = this.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId;
        handleModDimDef(id);
        handleExtraDrops(id);
        totalWeight = dropMap.getTotalWeight() + extraDropMap.getTotalWeight();
    }

    /**
     * Gets the DropMap of the dim for the specified dim id
     *
     * @param id the dim number
     */
    private void handleModDimDef(int id) {
        if (VoidMinerUtility.dropMapsByDimId.containsKey(id)) {
            dropMap = VoidMinerUtility.dropMapsByDimId.get(id);
        } else {
            String chunkProviderName = ((ChunkProviderServer) this.getBaseMetaTileEntity()
                .getWorld()
                .getChunkProvider()).currentChunkProvider.getClass()
                    .getName();

            if (VoidMinerUtility.dropMapsByChunkProviderName.containsKey(chunkProviderName)) {
                dropMap = VoidMinerUtility.dropMapsByChunkProviderName.get(chunkProviderName);
            }
        }
    }

    /**
     * Handles the ores added manually with {@link VoidMinerUtility#addMaterialToDimensionList}
     *
     * @param id the specified dim id
     */
    private void handleExtraDrops(int id) {
        if (VoidMinerUtility.extraDropsDimMap.containsKey(id)) {
            extraDropMap = VoidMinerUtility.extraDropsDimMap.get(id);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == 0) {
            startRecipeProcessing();
            ArrayList<FluidStack> fluidInputs = getStoredFluids();
            if (fluidInputs != null && !fluidInputs.isEmpty()) {
                for (FluidStack fluidStack : fluidInputs) {
                    Fluid fluid = fluidStack.getFluid();
                    if (fluid == WATER) {
                        long t = (long) storedWater + fluidStack.amount;
                        storedWater = t > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) t;
                        fluidStack.amount = 0;
                    } else if (fluid == LAVA) {
                        long t = (long) storedLava + fluidStack.amount;
                        storedLava = t > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) t;
                        fluidStack.amount = 0;
                    }
                }
                updateSlots();
            }
            endRecipeProcessing();
        }
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    // endregion

    // region Structure
    // spotless:off
    private static final String STRUCTURE_PIECE_MAIN = "mainEyeOfWood";
    private final int horizontalOffSet = 16;
    private final int verticalOffSet = 16;
    private final int depthOffSet = 0;
    private static IStructureDefinition<TST_EyeOfWood> STRUCTURE_DEFINITION = null;
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }
/*
A -> ofBlock...(gt.blockcasings, 10, ...); // hatches
B -> ofBlock...(tile.blockLapis, 0, ...);
C -> ofBlock...(tile.bookshelf, 0, ...);
D -> ofBlock...(tile.brick, 0, ...);
E -> ofBlock...(tile.stonebricksmooth, 3, ...);
F -> ofBlock...(tile.wood, 0, ...);
 */
    @Override
    public IStructureDefinition<TST_EyeOfWood> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION =
                IStructureDefinition
                    .<TST_EyeOfWood>builder()
                    .addShape(
                        STRUCTURE_PIECE_MAIN,
                        transpose(new String[][]{
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","               D D               ","            DDDDDDDDD            ","               D D               ","            DDDDDDDDD            ","               D D               ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","               D D               ","               D D               ","              FFFFF              ","             FFDFDFF             ","         DDDDFDDFDDFDDDD         ","             FFFFFFF             ","         DDDDFDDFDDFDDDD         ","             FFDFDFF             ","              FFFFF              ","               D D               ","               D D               ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","               D D               ","                F                ","                F                ","             FFFFFFF             ","            FF     FF            ","            F  BBB  F            ","       DDD  F BCCCB F  DDD       ","          FFF BCCCB FFF          ","       DDD  F BCCCB F  DDD       ","            F  BBB  F            ","            FF     FF            ","             FFFFFFF             ","                F                ","                F                ","               D D               ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","                F                ","                F                ","                                 ","                                 ","                                 ","                                 ","                                 ","      DD                 DD      ","        FF             FF        ","      DD                 DD      ","                                 ","                                 ","                                 ","                                 ","                                 ","                F                ","                F                ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","              DDDDD              ","                F                ","                C                ","                C                ","                                 ","                                 ","                                 ","                                 ","      D                   D      ","     DD                   DD     ","      DFCC             CCFD      ","     DD                   DD     ","      D                   D      ","                                 ","                                 ","                                 ","                                 ","                C                ","                C                ","                F                ","              DDDDD              ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","                F                ","             EBBCBBE             ","                                 ","                                 ","                                 ","                                 ","                                 ","       E                 E       ","       B                 B       ","    DD B                 B DD    ","      FC                 CF      ","    DD B                 B DD    ","       B                 B       ","       E                 E       ","                                 ","                                 ","                                 ","                                 ","                                 ","             EBBCBBE             ","                F                ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","               D D               ","              DDDDD              ","                F                ","                C                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","    D                       D    ","   DD                       DD   ","    DFC                   CFD    ","   DD                       DD   ","    D                       D    ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                C                ","                F                ","              DDDDD              ","               D D               ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","               D D               ","               D D               ","                F                ","             EBBCBBE             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","     E                     E     ","     B                     B     ","  DD B                     B DD  ","    FC                     CF    ","  DD B                     B DD  ","     B                     B     ","     E                     E     ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             EBBCBBE             ","                F                ","               D D               ","               D D               ","                                 ","                                 "},
                            {"                                 ","                                 ","               D D               ","                F                ","                C                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  D                           D  ","   FC                       CF   ","  D                           D  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                C                ","                F                ","               D D               ","                                 ","                                 "},
                            {"                                 ","               D D               ","               D D               ","                F                ","                C                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," DD                           DD ","   FC                       CF   "," DD                           DD ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                C                ","                F                ","               D D               ","               D D               ","                                 "},
                            {"                                 ","               D D               ","                F                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," D                             D ","  F                           F  "," D                             D ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                F                ","               D D               ","                                 "},
                            {"                                 ","               D D               ","                F                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," D                             D ","  F                           F  "," D                             D ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                F                ","               D D               ","                                 "},
                            {"             DDDDDDD             ","               D D               ","             FFFFFFF             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  F                           F  ","  F                           F  ","DDF                           FDD","  F                           F  ","DDF                           FDD","  F                           F  ","  F                           F  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             FFFFFFF             ","               D D               ","               D D               "},
                            {"            DDAAAAADD            ","              FFFFF              ","            FF     FF            ","                                 ","                                 ","       E                 E       ","                                 ","     E                     E     ","                                 ","                                 ","                                 ","                                 ","  F                           F  ","  F                           F  "," F                             F ","DF                             FD"," F                             F ","DF                             FD"," F                             F ","  F                           F  ","  F                           F  ","                                 ","                                 ","                                 ","                                 ","     E                     E     ","                                 ","       E                 E       ","                                 ","                                 ","            FF     FF            ","              FFFFF              ","               D D               "},
                            {"            DAAAAAAAD            ","             FFDFDFF             ","            F  BBB  F            ","                                 ","      D                   D      ","       B                 B       ","    D                       D    ","     B                     B     ","                                 ","                                 ","                                 ","                                 ","  F                           F  "," F                             F "," F                             F ","DDB                           BDD"," FB                           BF ","DDB                           BDD"," F                             F "," F                             F ","  F                           F  ","                                 ","                                 ","                                 ","                                 ","     B                     B     ","    D                       D    ","       B                 B       ","      D                   D      ","                                 ","            F  BBB  F            ","             FFDFDFF             ","               D D               "},
                            {"            DAADDDAAD            ","         DDDDFDDFDDFDDDD         ","       DDD  F BCCCB F  DDD       ","      DD                 DD      ","     DD                   DD     ","    DD B                 B DD    ","   DD                       DD   ","  DD B                     B DD  ","  D                           D  "," DD                           DD "," D                             D "," D                             D ","DDF                           FDD","DF                             FD","DDB                           BDD","DDC                           CDD","DFC                           CFD","DDC                           CDD","DDB                           BDD","DF                             FD","DDF                           FDD"," D                             D "," D                             D "," DD                           DD ","  D                           D  ","  DD B                     B DD  ","   DD                       DD   ","    DD B                 B DD    ","     DD                   DD     ","      DD                 DD      ","       DDD  F BCCCB F  DDD       ","         DDDDFDDFDDFDDDD         ","            DDDDDDDDD            "},
                            {"            DAAD~DAAD            ","             FFFFFFF             ","          FFF BCCCB FFF          ","        FF             FF        ","      DFCC             CCFD      ","      FC                 CF      ","    DFC                   CFD    ","    FC                     CF    ","   FC                       CF   ","   FC                       CF   ","  F                           F  ","  F                           F  ","  F                           F  "," F                             F "," FB                           BF ","DFC                           CFD"," FC                           CF ","DFC                           CFD"," FB                           BF "," F                             F ","  F                           F  ","  F                           F  ","  F                           F  ","   FC                       CF   ","   FC                       CF   ","    FC                     CF    ","    DFC                   CFD    ","      FC                 CF      ","      DFCC             CCFD      ","        FF             FF        ","          FFF BCCCB FFF          ","             FFFFFFF             ","               D D               "},
                            {"            DAADDDAAD            ","         DDDDFDDFDDFDDDD         ","       DDD  F BCCCB F  DDD       ","      DD                 DD      ","     DD                   DD     ","    DD B                 B DD    ","   DD                       DD   ","  DD B                     B DD  ","  D                           D  "," DD                           DD "," D                             D "," D                             D ","DDF                           FDD","DF                             FD","DDB                           BDD","DDC                           CDD","DFC                           CFD","DDC                           CDD","DDB                           BDD","DF                             FD","DDF                           FDD"," D                             D "," D                             D "," DD                           DD ","  D                           D  ","  DD B                     B DD  ","   DD                       DD   ","    DD B                 B DD    ","     DD                   DD     ","      DD                 DD      ","       DDD  F BCCCB F  DDD       ","         DDDDFDDFDDFDDDD         ","            DDDDDDDDD            "},
                            {"            DAAAAAAAD            ","             FFDFDFF             ","            F  BBB  F            ","                                 ","      D                   D      ","       B                 B       ","    D                       D    ","     B                     B     ","                                 ","                                 ","                                 ","                                 ","  F                           F  "," F                             F "," F                             F ","DDB                           BDD"," FB                           BF ","DDB                           BDD"," F                             F "," F                             F ","  F                           F  ","                                 ","                                 ","                                 ","                                 ","     B                     B     ","    D                       D    ","       B                 B       ","      D                   D      ","                                 ","            F  BBB  F            ","             FFDFDFF             ","               D D               "},
                            {"            DDAAAAADD            ","              FFFFF              ","            FF     FF            ","                                 ","                                 ","       E                 E       ","                                 ","     E                     E     ","                                 ","                                 ","                                 ","                                 ","  F                           F  ","  F                           F  "," F                             F ","DF                             FD"," F                             F ","DF                             FD"," F                             F ","  F                           F  ","  F                           F  ","                                 ","                                 ","                                 ","                                 ","     E                     E     ","                                 ","       E                 E       ","                                 ","                                 ","            FF     FF            ","              FFFFF              ","               D D               "},
                            {"             DDDDDDD             ","               D D               ","             FFFFFFF             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  F                           F  ","  F                           F  ","DDF                           FDD","  F                           F  ","DDF                           FDD","  F                           F  ","  F                           F  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             FFFFFFF             ","               D D               ","               D D               "},
                            {"                                 ","               D D               ","                F                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," D                             D ","  F                           F  "," D                             D ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                F                ","               D D               ","                                 "},
                            {"                                 ","               D D               ","                F                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," D                             D ","  F                           F  "," D                             D ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                F                ","               D D               ","                                 "},
                            {"                                 ","               D D               ","               D D               ","                F                ","                C                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "," DD                           DD ","   FC                       CF   "," DD                           DD ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                C                ","                F                ","               D D               ","               D D               ","                                 "},
                            {"                                 ","                                 ","               D D               ","                F                ","                C                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","  D                           D  ","   FC                       CF   ","  D                           D  ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                C                ","                F                ","               D D               ","                                 ","                                 "},
                            {"                                 ","                                 ","               D D               ","               D D               ","                F                ","             EBBCBBE             ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","     E                     E     ","     B                     B     ","  DD B                     B DD  ","    FC                     CF    ","  DD B                     B DD  ","     B                     B     ","     E                     E     ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","             EBBCBBE             ","                F                ","               D D               ","               D D               ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","               D D               ","              DDDDD              ","                F                ","                C                ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","    D                       D    ","   DD                       DD   ","    DFC                   CFD    ","   DD                       DD   ","    D                       D    ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                C                ","                F                ","              DDDDD              ","               D D               ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","                F                ","             EBBCBBE             ","                                 ","                                 ","                                 ","                                 ","                                 ","       E                 E       ","       B                 B       ","    DD B                 B DD    ","      FC                 CF      ","    DD B                 B DD    ","       B                 B       ","       E                 E       ","                                 ","                                 ","                                 ","                                 ","                                 ","             EBBCBBE             ","                F                ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","              DDDDD              ","                F                ","                C                ","                C                ","                                 ","                                 ","                                 ","                                 ","      D                   D      ","     DD                   DD     ","      DFCC             CCFD      ","     DD                   DD     ","      D                   D      ","                                 ","                                 ","                                 ","                                 ","                C                ","                C                ","                F                ","              DDDDD              ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","                F                ","                F                ","                                 ","                                 ","                                 ","                                 ","                                 ","      DD                 DD      ","        FF             FF        ","      DD                 DD      ","                                 ","                                 ","                                 ","                                 ","                                 ","                F                ","                F                ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","               D D               ","                F                ","                F                ","             FFFFFFF             ","            FF     FF            ","            F  BBB  F            ","       DDD  F BCCCB F  DDD       ","          FFF BCCCB FFF          ","       DDD  F BCCCB F  DDD       ","            F  BBB  F            ","            FF     FF            ","             FFFFFFF             ","                F                ","                F                ","               D D               ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","               D D               ","               D D               ","              FFFFF              ","             FFDFDFF             ","         DDDDFDDFDDFDDDD         ","             FFFFFFF             ","         DDDDFDDFDDFDDDD         ","             FFDFDFF             ","              FFFFF              ","               D D               ","               D D               ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "},
                            {"                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","               D D               ","               D D               ","               D D               ","            DDDDDDDDD            ","               D D               ","            DDDDDDDDD            ","               D D               ","               D D               ","               D D               ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 ","                                 "}
                        })
                    )
                    .addElement(
                        'A',
                        HatchElementBuilder
                            .<TST_EyeOfWood>builder()
                            .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                            .adder(TST_EyeOfWood::addToMachineList)
                            .dot(1)
                            .casingIndex(10)
                            .buildAndChain(GregTechAPI.sBlockCasings1, 10))
                    .addElement('B', ofBlock(Blocks.lapis_block, 0))
                    .addElement('C', ofBlock(Blocks.bookshelf, 0))
                    .addElement('D', ofBlock(Blocks.brick_block, 0))
                    .addElement('E', ofBlock(Blocks.stonebrick, 3))
                    .addElement('F', ofBlockAnyMeta(Blocks.planks))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }
    // spotless:on
    // endregion

    // region General

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    private static MultiblockTooltipBuilder tt = null;

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        // spotless:off
        if (tt == null) {
            tt = new MultiblockTooltipBuilder();
            tt.addMachineType(TextLocalization.Tooltip_EyeOfWood_MachineType)
                .addInfo(TextLocalization.Tooltip_EyeOfWood_Controller)
                .addInfo(TextLocalization.Tooltip_EyeOfWood_01)
                // #tr Tooltip_EyeOfWood_02
                // # Can only be deployed in the Overworld, otherwise it will have a festive effect.
                // #zh_CN 仅可部署在主世界, 否则将产生节庆效果.
                .addInfo(TextEnums.tr("Tooltip_EyeOfWood_02"))
                .addInfo(TextLocalization.StructureTooComplex)
                .addInfo(TextLocalization.BLUE_PRINT_INFO)
                .addSeparator()
                // #tr Tooltip_EyeOfWood_2_01
                // # This machine will constantly consume {\BLUE}Water {\GRAY}and {\RED}Lava {\GRAY}in Input Hatches and store it inside the machine, like the Eye of Harmony.
                // #zh_CN 机器会将输入仓中输入的{\BLUE}水{\GRAY}和{\RED}岩浆{\GRAY}存储于机器内部, 就像鸿蒙之眼一样.
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_01"))
                // #tr Tooltip_EyeOfWood_2_02
                // # The success rate of processing depends on the amount of water and magma that has been stored inside machine.
                // #zh_CN 机器运行的成功率取决于已存储的水和岩浆的数量.
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_02"))
                // #tr Tooltip_EyeOfWood_2_03
                // # Maximum success rate : 75%% when the amount of stored water and lava are both equal to 256,000L.
                // #zh_CN 当已存储的水和岩浆数量都等于256,000L时达到最高成功率: 75%%
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_03"))
                .addStructureInfo(TextLocalization.Text_SeparatingLine)
                // #tr Tooltip_EyeOfWood_2_04
                // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\AQUA}Success Rate{\GRAY} = {\WHITE}{7500 - 7499 * [1 - 1/({\GOLD}S{\WHITE}^{\BLUE}dW{\WHITE} * {\GOLD}S{\WHITE}^{\RED}dL{\WHITE}) ] } / 10000
                // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\AQUA}成功率{\GRAY} = {\WHITE}{7500 - 7499 * [1 - 1/({\GOLD}S{\WHITE}^{\BLUE}dW{\WHITE} * {\GOLD}S{\WHITE}^{\RED}dL{\WHITE}) ] } / 10000
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_04"))
                // #tr Tooltip_EyeOfWood_2_05
                // # Of Which :
                // #zh_CN 其中 :
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_05"))
                // #tr Tooltip_EyeOfWood_2_06
                // # Value-specific base {\GOLD}S{\WHITE} ≈ 1.087
                // #zh_CN 特定值底数 {\GOLD}S{\WHITE} ≈ 1.087
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_06"))
                // #tr Tooltip_EyeOfWood_2_07
                // # {\BLUE}dW{\GRAY} is the difference between stored water and demand (256,000L) divided by 1000, rounded down, unit L : {\BLUE}dW{\WHITE} = floor( |Stored Water - Demand | / 1000 )
                // #zh_CN {\BLUE}dW{\GRAY} 为已存储水量和需求量(256,000L)的差值再除以1000向下取整, 单位L : {\BLUE}dW{\WHITE} = floor( |已存储水量 - 需求量| / 1000 )
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_07"))
                // #tr Tooltip_EyeOfWood_2_08
                // # {\RED}dL{\GRAY} is the difference between stored lava and demand (256,000L) divided by 1000, rounded down, unit L : {\RED}dL{\WHITE} = floor( |Stored Lava - Demand | / 1000 )
                // #zh_CN {\RED}dL{\GRAY} 为已存储岩浆量和需求量(256,000L)的差值再除以1000向下取整, 单位L : {\RED}dL{\WHITE} = floor( |已存储岩浆量 - 需求量| / 1000 )
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_08"))
                .addStructureInfo(TextLocalization.Text_SeparatingLine)
                // #tr Tooltip_EyeOfWood_2_09
                // # The machine takes a constant 60 seconds per run.
                // #zh_CN 机器每次运行耗时恒定为60秒.
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_09"))
                // #tr Tooltip_EyeOfWood_2_10
                // # If processing succeed, machine will output lots of ore resource of Overworld.
                // #zh_CN 如果机器运行成功, 将产出大量主世界矿资源.
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_10"))
                // #tr Tooltip_EyeOfWood_2_11
                // # If processing fail, machine will output lots of Steam.
                // #zh_CN 如果机器运行失败, 则产出大量蒸汽.
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_11"))
                // #tr Tooltip_EyeOfWood_2_12
                // # Amount of steam produced (in L) = Success Rate * 360,000,000L (max. 270,000,000L)
                // #zh_CN 产出蒸汽数量(单位L) = 成功几率 * 360,000,000L (最多270,000,000L)
                .addStructureInfo(TextEnums.tr("Tooltip_EyeOfWood_2_12"))
                /*
                 * 成功率 = {7500 - 7499 * [1 - 1/(S^dW * S^dL)]} / 10000
                 * <P>S ≈ 1.087</P>
                 * <P>dW, dL 分别为已存储的水量与需求量(256,000L)的差值除以1000和已存储的岩浆量和需求量的差值除以1000, 并向下取整.</P>
                 */
                .addStructureInfo(TextLocalization.Text_SeparatingLine)
                .beginStructureBlock(33, 33, 33, false)
                .addController(TextLocalization.textFrontCenter)
                .addInputBus(TextLocalization.textAnyCasing, 2)
                .addOutputBus(TextLocalization.textAnyCasing, 2)
                .toolTipFinisher(TextLocalization.ModName);
        }
        // spotless:on

        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(10), TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_ON)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(10), TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }

        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(10) };
    }
}
