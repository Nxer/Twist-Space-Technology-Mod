package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static com.Nxer.TwistSpaceTechnology.system.RecipePattern.CustomCraftRecipe.areStacksEqual;
import static com.Nxer.TwistSpaceTechnology.system.RecipePattern.ExtremeCraftRecipe.extremeCraftRecipes;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TT_MultiMachineBase_EM;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.security.IActionHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.DimensionalCoord;
import appeng.items.misc.ItemEncodedPattern;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_StructureUtility;
import gtPlusPlus.core.block.ModBlocks;

public class TST_MegaCraftingCenter extends TT_MultiMachineBase_EM
    implements ICraftingProvider, IActionHost, IGridProxyable, ISurvivalConstructable {

    @NotNull
    private ArrayList<ICraftingPatternDetails> cachedPatternDetails = new ArrayList<>();
    private final HashMap<ICraftingPatternDetails, Long> cachedOutput = new HashMap<>();

    private ArrayList<ItemStack> cachedPattern;
    @Nullable
    private AENetworkProxy gridProxy;

    public TST_MegaCraftingCenter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected TST_MegaCraftingCenter(String aName) {
        super(aName);
        // TileMolecularAssembler
    }

    @Override
    public IStructureDefinition<TST_MegaCraftingCenter> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaCraftingCenter>builder()
                .addShape(
                    "MAIN",
                    transpose(
                        new String[][] {
                            { "BBBBBBB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BBBBBBB" },
                            { "BEEEEEB", "E     E", "E  D  E", "E DAD E", "E  D  E", "E     E", "BEEEEEB" },
                            { "BEEEEEB", "E  D  E", "E D D E", "ED A DE", "E D D E", "E  D  E", "BEEEEEB" },
                            { "BEE~EEB", "E DAD E", "ED A DE", "EAACAAE", "ED A DE", "E DAD E", "BEEEEEB" },
                            { "BEEEEEB", "E  D  E", "E D D E", "ED A DE", "E D D E", "E  D  E", "BEEEEEB" },
                            { "BEEEEEB", "E     E", "E  D  E", "E DAD E", "E  D  E", "E     E", "BEEEEEB" },
                            { "BBBBBBB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BBBBBBB" } }))
                .addElement('A', ofBlock(GregTech_API.sBlockCasings1, 14))
                .addElement(
                    'B',
                    StructureUtility.ofChain(
                        GT_StructureUtility
                            .ofHatchAdder(TST_MegaCraftingCenter::addToMachineList, textureOffset + 12, 1),
                        StructureUtility.ofBlock(TT_Container_Casings.sBlockCasingsTT, 12)))
                .addElement('C', ofBlock(TT_Container_Casings.sBlockCasingsTT, 10))
                .addElement('D', ofBlock(ModBlocks.blockCasings3Misc, 15))
                .addElement('E', ofBlock(QuantumGlassBlock.INSTANCE, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // Blocks:
    // A -> ofBlock...(gt.blockcasings, 14, ...);
    // B -> ofBlock...(gt.blockcasingsTT, 4, ...);
    // C -> ofBlock...(gt.blockcasingsTT, 10, ...);
    // D -> ofBlock...(gtplusplus.blockcasings.3, 15, ...);
    // E -> ofBlock...(tile.quantumGlass, 0, ...);
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("MAIN", stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece("MAIN", stackSize, 3, 3, 0, elementBudget, env, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) {
                return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_ON)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }

            return new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }

        return new ITexture[] { casingTexturePages[0][12] };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaCraftingCenter(mName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return extremeCraftRecipes;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick_EM(aBaseMetaTileEntity);
        getProxy().onReady();
        updatePatterns();
        // appeng.me.cache.CraftingGridCache.addNode(this);
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        if (mOutputItems != null) {
            return SimpleCheckRecipeResult.ofFailure("sadly, there's still something in your machine, what is that?");
        }
        long value = 0;
        ArrayList<ItemStack> additionalOutput = new ArrayList<>();
        var set = cachedOutput.entrySet();
        for (var itemstack : set) {
            ItemStack stack = itemstack.getKey()
                .getOutputs()[0].getItemStack()
                    .copy();
            long p = stack.stackSize * itemstack.getValue();
            value += stack.stackSize * itemstack.getValue();
            ItemStack newStack;
            while (p > Integer.MAX_VALUE) {
                newStack = stack.copy();
                newStack.stackSize = Integer.MAX_VALUE;
                additionalOutput.add(newStack.copy());
                p -= Integer.MAX_VALUE;
            }
            newStack = stack.copy();
            if (p > 0) newStack.stackSize = (int) p;
            else continue;
            additionalOutput.add(newStack.copy());
        }
        if (additionalOutput.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;
        mOutputItems = additionalOutput.toArray(new ItemStack[0]);
        // LOG.info(Arrays.toString(mOutputItems));
        lEUt = -RECIPE_ZPM * value;
        mMaxProgresstime = (int) (2 * (1 + Math.log10(1 + value)));
        mProgresstime = 0;
        cachedOutput.clear();
        return CheckRecipeResultRegistry.GENERATING;
        // return SimpleCheckRecipeResult.ofSuccess("This machine process each valid request with in 0 tick!\n" +
        // "so you will never see it work!");
    }

    private static IStructureDefinition<TST_MegaCraftingCenter> STRUCTURE_DEFINITION;

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(this, "proxy", GTCMItemList.ExtremeCraftCenter.get(1), true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                // updateValidGridProxySides();
                if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                    getBaseMetaTileEntity().getWorld()
                        .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
            }
            // GT_MetaTileEntity_Hatch_CraftingInput_ME
        }
        return this.gridProxy;
    }

    @Nullable
    public IGridNode getGridNode(@Nullable ForgeDirection dir) {
        // GT_MetaTileEntity_Hatch_Input_ME
        AENetworkProxy proxy = this.getProxy();
        return proxy != null ? proxy.getNode() : null;
    }

    public void securityBreak() {
        this.getBaseMetaTileEntity()
            .disableWorking();
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        maintenance_EM();
        if (structureCheck_EM("MAIN", 3, 3, 0)) {
            return !mInputBusses.isEmpty() && !mOutputBusses.isEmpty()
                && (!mEnergyHatches.isEmpty() || !eEnergyMulti.isEmpty());
        }
        return false;
    }

    protected void maintenance_EM() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    @NotNull
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(
            this.getBaseMetaTileEntity()
                .getWorld(),
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord());
    }

    @Nullable
    public IGridNode getActionableNode() {
        AENetworkProxy proxy = this.getProxy();
        return proxy != null ? proxy.getNode() : null;
    }

    @Override
    public void provideCrafting(@NotNull ICraftingProviderHelper craftingTracker) {
        AENetworkProxy proxy = this.getProxy();
        if (proxy != null && proxy.isReady()) {
            for (var details : cachedPatternDetails) {
                craftingTracker.addCraftingOption(this, details);
            }
        }

    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        if (cachedOutput.containsKey(patternDetails)) {
            Long pre = cachedOutput.get(patternDetails);
            return Objects.equals(cachedOutput.put(patternDetails, pre + 1), pre);
        }
        cachedOutput.put(patternDetails, 1L);
        return true;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 100 == 0 && aBaseMetaTileEntity.isServerSide()) {
            updatePatterns();
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    public void updatePatterns() {
        ArrayList<ICraftingPatternDetails> patternDetails = new ArrayList<>();
        ArrayList<ItemStack> patterns = getStoredInputs();
        if (!patterns.equals(cachedPattern)) {
            for (var pattern : patterns) {
                if (pattern.getItem() instanceof ItemEncodedPattern encodedPattern) {
                    var details = encodedPattern.getPatternForItem(pattern, getBaseMetaTileEntity().getWorld());
                    if (cachedPatternDetails.contains(details)) patternDetails.add(details);
                    else if (details.isCraftable()) {
                        patternDetails.add(details);
                        LOG.info("details added:" + details);
                    } else {
                        if (details.getOutputs().length != 1) continue;
                        // LOG.info(
                        // "extremeCraftRecipes have " + extremeCraftRecipes.getAllRecipes()
                        // .size());
                        var validResult = extremeCraftRecipes.findRecipeQuery()
                            .items(convertAEToMC(details.getInputs()))
                            .findAll()
                            .toArray(GT_Recipe[]::new);
                        // LOG.info("what do we find here?" + validResult.length + " recipes " +
                        // validResult[0].getOutput(0));
                        // for (var result : validResult) {
                        // LOG.info("they are:" + Arrays.toString(result.mInputs) + " to " +
                        // Arrays.toString(result.mOutputs));
                        // }
                        if (validResult.length >= 1) {
                            var recipe = validResult[0];
                            if (areStacksEqual(recipe.getOutput(0), details.getOutputs()[0].getItemStack(), true)
                                && details.getOutputs().length == 1) {
                                patternDetails.add(details);
                                // LOG.info("details added:" + details);
                            }
                        }
                    }
                }
            }
            cachedPatternDetails = patternDetails;
            cachedPattern = patterns;
            // getProxy().getGrid().
            if (!getProxy().isActive()) return;
            try {
                // LOG.info("we submit a MENetworkCraftingPatternChange");
                getProxy().getGrid()
                    .postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
            } catch (GridAccessException ignored) {
                // LOG.info(ignored);
            }
        }
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextEnums.tr("tst.megacraftingcenter.machinetype"))
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.0"))
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.1"))
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.2"))
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.3"))
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.4"))
            // #tr tst.megacraftingcenter.desc.5
            // # There is a performance issue when checking pattern. Please use with caution.
            // #zh_CN 当前检查样板功能存在性能问题. 谨慎使用.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.5"))
            .addInfo(Text_SeparatingLine)
            .toolTipFinisher(ModName);
        return tt;
    }

    public static ItemStack[] convertAEToMC(IAEItemStack[] STACK) {
        // TODO use normal code style instead `Stream`
        return Arrays.stream(STACK)
            .filter(Objects::nonNull)
            .map(IAEItemStack::getItemStack)
            .toArray(ItemStack[]::new);
    }

}
