package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.proxy;
import static com.Nxer.TwistSpaceTechnology.system.RecipePattern.CustomCraftRecipe.areStacksEqual;
import static com.Nxer.TwistSpaceTechnology.system.RecipePattern.ExtremeCraftRecipe.extremeCraftRecipes;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
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
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_StructureUtility;
import gtPlusPlus.core.block.ModBlocks;

public class TST_MegaCraftingCenter extends GT_MetaTileEntity_MultiblockBase_EM
    implements ICraftingProvider, IActionHost, IGridProxyable, ISurvivalConstructable {

    @NotNull
    private ArrayList<ICraftingPatternDetails> cachedPatternDetails = new ArrayList<>();
    private final HashMap<ICraftingPatternDetails, Long> cachedOutput = new HashMap<>();

    @Nullable
    private BaseActionSource requestSource;
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

    private BaseActionSource getRequest() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
        // GT_MetaTileEntity_Hatch_CraftingInput_ME
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
        // GT_MetaTileEntity_Hatch_Input_ME
        // if (mOutputItems != null) {
        // return SimpleCheckRecipeResult.ofFailure("sadly, there's still something in your machine, what is that?");
        // }
        // long value = 0;
        // ArrayList<ItemStack> additionalOutput = new ArrayList<>();
        // var set = cachedOutput.entrySet();
        // for (var itemstack : set) {
        // ItemStack stack = itemstack.getKey()
        // .getOutputs()[0].getItemStack()
        // .copy();
        // long p=stack.stackSize * itemstack.getValue();
        // value +=stack.stackSize * itemstack.getValue();
        //
        // stack.stackSize = p;
        // additionalOutput.add(stack);
        //
        //
        // }
        // if (additionalOutput.isEmpty()) return SimpleCheckRecipeResult.ofFailure("Empty! Nothing is working here!");
        // mOutputItems = additionalOutput.toArray(new ItemStack[0]);
        // lEUt = RECIPE_ZPM * (Integer.MAX_VALUE - maxP);
        // mProgresstime = (int) (20 * Math.log10(Integer.MAX_VALUE - maxP));
        // return SimpleCheckRecipeResult.ofSuccess(
        // "You create " + (Integer.MAX_VALUE - maxP)
        // + " items! goooood!");
         return SimpleCheckRecipeResult.ofSuccess("This machine process each valid request with in 0 tick!\n" +
         "so you will never see it work!");
    }

    private static IStructureDefinition<TST_MegaCraftingCenter> STRUCTURE_DEFINITION;

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(
                    (IGridProxyable) getBaseMetaTileEntity(),
                    "proxy",
                    GTCMItemList.ExtremeCraftCenter.get(1),
                    true);
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
        if (structureCheck_EM("MAIN", 3, 3, 0)) {
            return !mInputBusses.isEmpty() && !mOutputBusses.isEmpty()
                && (!mEnergyHatches.isEmpty() || !eEnergyMulti.isEmpty())
                && !mMaintenanceHatches.isEmpty();
        }
        return false;
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
        LOG.info("do we pass to here?");
        // if (!getBaseMetaTileEntity().isActive()) return;
        if (proxy != null && proxy.isReady()) {
            for (var details : cachedPatternDetails) {

                craftingTracker.addCraftingOption(this, details);
            }
        }

    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        Long pre = cachedOutput.get(patternDetails);
        return Objects.equals(cachedOutput.put(patternDetails, pre + 1), pre);
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 20 == 0 && aBaseMetaTileEntity.isServerSide()) {
            updatePatterns();
            var proxy = getProxy();
            try {
                var grid = proxy.getCrafting();
                // LOG.info();
            } catch (GridAccessException e) {
                throw new RuntimeException(e);
            }
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    public void updatePatterns() {
        LOG.info("proxy is:" + getProxy());
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
                        LOG.info(
                            "extremeCraftRecipes have " + extremeCraftRecipes.getAllRecipes()
                                .size());
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
                                LOG.info("details added:" + details);
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

    public static ItemStack[] convertAEToMC(IAEItemStack[] STACK) {
        return Arrays.stream(STACK)
            .filter(Objects::nonNull)
            .map(IAEItemStack::getItemStack)
            .toArray(ItemStack[]::new);
    }
}
