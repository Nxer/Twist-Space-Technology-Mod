package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.system.RecipePattern.ExtremeCraftRecipe.extremeCraftRecipes;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TT_MultiMachineBase_EM;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.security.IActionHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.DimensionalCoord;
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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;

public class TST_MegaCraftingCenter extends TT_MultiMachineBase_EM
    implements ICraftingProvider, IActionHost, IGridProxyable, ISurvivalConstructable {

    // region Class Constructor

    public TST_MegaCraftingCenter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected TST_MegaCraftingCenter(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_MegaCraftingCenter(mName);
    }

    // endregion

    // region Statics

    public static ItemStack[] convertAEToMC(IAEItemStack... STACK) {
        // TODO use normal code style instead `Stream`
        return Arrays.stream(STACK)
            .filter(Objects::nonNull)
            .map(IAEItemStack::getItemStack)
            .toArray(ItemStack[]::new);
    }

    public static @Nullable ICraftingPatternDetails checkPattern(ItemStack pattern) {
        if (pattern == null || pattern.stackSize < 1) return null;
        if (pattern.getItem() instanceof ICraftingPatternItem patternItem) {
            ICraftingPatternDetails d = patternItem.getPatternForItem(pattern, null);
            if (d == null) return null;
            if (d.isCraftable()) {
                return d;
            } else {
                IAEItemStack[] dOutputs = d.getOutputs();
                if (dOutputs == null || dOutputs.length != 1 || dOutputs[0] == null) return null;

                ItemStack[] dInputs = convertAEToMC(d.getInputs());
                if (!Utils.isValid(dInputs)) return null;

                // fine all available extreme recipes of this pattern
                GT_Recipe[] validResult = extremeCraftRecipes.findRecipeQuery()
                    .items(dInputs)
                    .findAll()
                    .toArray(GT_Recipe[]::new);

                if (validResult.length < 1) return null;

                @NotNull
                ItemStack pItems = dOutputs[0].getItemStack();
                if (pItems == null || pItems.getItem() == null || pItems.stackSize < 1) return null;

                if (validResult.length == 1) {
                    if (validResult[0] == null) return null;

                    @NotNull
                    ItemStack rItems = validResult[0].mOutputs[0];
                    if (Utils.metaItemEqual(pItems, rItems)) {
                        if (pItems.stackSize < rItems.stackSize || (pItems.stackSize & rItems.stackSize) != 0)
                            return null;
                        // allow to double pattern
                        int multiple = pItems.stackSize / rItems.stackSize;
                        if (multiple < 1) return null;

                        ItemStack[] rInputs = validResult[0].mInputs;
                        Map<TST_ItemID, Long> rInputMap = new HashMap<>();
                        for (int t = 0; t < rInputs.length; t++) {
                            rInputMap.merge(
                                TST_ItemID.createNoNBT(GT_OreDictUnificator.get_nocopy(rInputs[t])),
                                (long) rInputs[t].stackSize,
                                Long::sum);
                        }

                        Map<TST_ItemID, Long> pInputMap = new HashMap<>();
                        for (int t = 0; t < dInputs.length; t++) {
                            pInputMap.merge(
                                TST_ItemID.createNoNBT(GT_OreDictUnificator.get_nocopy(dInputs[t])),
                                (long) dInputs[t].stackSize,
                                Long::sum);
                        }

                        if (rInputMap.size() != pInputMap.size()) return null;

                        for (Map.Entry<TST_ItemID, Long> rEntry : rInputMap.entrySet()) {
                            Long pAmount = pInputMap.get(rEntry.getKey());
                            if (pAmount == null || !pAmount.equals(rEntry.getValue() * multiple)) return null;
                        }

                        return d;
                    } else {
                        return null;
                    }

                } else {
                    recipesLoop: for (int i = 0; i < validResult.length; i++) {
                        @NotNull
                        ItemStack rItems = validResult[i].mOutputs[0];
                        if (Utils.metaItemEqual(pItems, rItems)) {
                            if (pItems.stackSize < rItems.stackSize || (pItems.stackSize & rItems.stackSize) != 0)
                                continue;
                            // allow to double pattern
                            int multiple = pItems.stackSize / rItems.stackSize;
                            if (multiple < 1) continue;

                            ItemStack[] rInputs = validResult[i].mInputs;
                            Map<TST_ItemID, Long> rInputMap = new HashMap<>();
                            for (int t = 0; t < rInputs.length; t++) {
                                rInputMap.merge(
                                    TST_ItemID.createNoNBT(GT_OreDictUnificator.get_nocopy(rInputs[t])),
                                    (long) rInputs[t].stackSize,
                                    Long::sum);
                            }

                            Map<TST_ItemID, Long> pInputMap = new HashMap<>();
                            for (int t = 0; t < dInputs.length; t++) {
                                pInputMap.merge(
                                    TST_ItemID.createNoNBT(GT_OreDictUnificator.get_nocopy(dInputs[t])),
                                    (long) dInputs[t].stackSize,
                                    Long::sum);
                            }

                            if (rInputMap.size() != pInputMap.size()) continue;
                            for (Map.Entry<TST_ItemID, Long> rEntry : rInputMap.entrySet()) {
                                Long pAmount = pInputMap.get(rEntry.getKey());
                                if (pAmount == null || !pAmount.equals(rEntry.getValue() * multiple))
                                    continue recipesLoop;
                            }

                            return d;
                        }
                    }
                }
            }
        }
        return null;
    }

    // endregion

    // region Logic

    /**
     * An internal inventory to hold valid patterns' item stacks.
     */
    protected Collection<ItemStack> internalPatterns = new ArrayList<>();

    protected Collection<ICraftingPatternDetails> patternDetails = new HashSet<>();

    private final HashMap<ICraftingPatternDetails, Long> cachedOutput = new HashMap<>();

    @Nullable
    private AENetworkProxy gridProxy;

    protected boolean toReturnPatterns = false;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setBoolean("toReturnPatterns", toReturnPatterns);

        // save internal patterns
        if (!internalPatterns.isEmpty()) {
            NBTTagList l = new NBTTagList();
            for (ItemStack i : internalPatterns) {
                l.appendTag(i.writeToNBT(new NBTTagCompound()));
            }
            aNBT.setTag("internalPatterns", l);
        }

    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        toReturnPatterns = aNBT.getBoolean("toReturnPatterns");

        // load internal patterns
        NBTTagList l = aNBT.getTagList("internalPatterns", TAG_COMPOUND);
        if (l != null && l.tagCount() > 0) {
            internalPatterns.clear();
            patternDetails.clear();

            for (int i = 0; i < l.tagCount(); i++) {
                ItemStack pattern = ItemStack.loadItemStackFromNBT(l.getCompoundTagAt(i));
                ICraftingPatternDetails d = checkPattern(pattern);
                if (d != null) {
                    if (!patternDetails.contains(d)) {
                        patternDetails.add(d);
                        internalPatterns.add(pattern);
                    }
                }
            }
        }
    }

    /**
     * Check input items, move valid patterns to internal inventory.
     *
     * @return A list of invalid patterns and items.
     */
    protected ArrayList<ItemStack> checkPatternInput() {
        ArrayList<ItemStack> l = new ArrayList<>();
        ArrayList<ItemStack> inputs = getStoredInputs();
        if (inputs.isEmpty()) return l;
        boolean addNew = false;
        for (ItemStack in : inputs) {
            ICraftingPatternDetails d = checkPattern(in);
            if (d != null && !patternDetails.contains(d)) {
                patternDetails.add(d);

                if (in.stackSize > 1) {
                    l.add(Utils.setStackSize(in.copy(), in.stackSize - 1));
                    in.stackSize = 1;
                }

                internalPatterns.add(in.copy());
                in.stackSize = 0;
                addNew = true;
            } else {
                l.add(in.copy());
                in.stackSize = 0;
            }
        }
        updateSlots();

        if (addNew) {
            flush();
        }
        return l;
    }

    protected void flush() {
        if (getProxy().isActive()) {
            try {
                getProxy().getGrid()
                    .postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
            } catch (GridAccessException ignored) {}
        }
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {

        if (toReturnPatterns) {
            toReturnPatterns = false;
            mOutputItems = internalPatterns.toArray(new ItemStack[0]);
            internalPatterns.clear();
            patternDetails.clear();
            flush();
            mMaxProgresstime = 20;

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        ArrayList<ItemStack> additionalOutput = checkPatternInput();

        for (Map.Entry<ICraftingPatternDetails, Long> itemstack : cachedOutput.entrySet()) {
            ItemStack stack = itemstack.getKey()
                .getOutputs()[0].getItemStack()
                    .copy();
            long p = stack.stackSize * itemstack.getValue();
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
        mMaxProgresstime = 128;
        mProgresstime = 0;
        cachedOutput.clear();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            if (mMaxProgresstime > 0) {
                // #tr MegaCraftingCenter.onScrewdriverRightClick.failed
                // # The encoded patterns can only be returned when there is no recipe running.
                // #zh_CN 仅可在未运行配方状态下退回样板.
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("MegaCraftingCenter.onScrewdriverRightClick.failed"));
                return;
            }

            // return encoded patterns
            toReturnPatterns = true;

            // #tr MegaCraftingCenter.onScrewdriverRightClick.success
            // # Preparing to returning encoded patterns.
            // #zh_CN 正在准备退回样板.
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("MegaCraftingCenter.onScrewdriverRightClick.success"));

        }
    }

    @Override
    public void provideCrafting(@NotNull ICraftingProviderHelper craftingTracker) {
        AENetworkProxy proxy = this.getProxy();
        if (proxy != null && proxy.isReady()) {
            for (var details : patternDetails) {
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
    public RecipeMap<?> getRecipeMap() {
        return extremeCraftRecipes;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick_EM(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            IGregTechTileEntity mte = getBaseMetaTileEntity();
            if (mte instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(this, "proxy", GTCMItemList.ExtremeCraftCenter.get(1), true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                // updateValidGridProxySides();
                if (mte.getWorld() != null) {
                    gridProxy.setOwner(
                        mte.getWorld()
                            .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
                }
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
            return !mOutputBusses.isEmpty();
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
    public boolean isBusy() {
        return false;
    }

    // endregion

    // region Structure
    protected static IStructureDefinition<TST_MegaCraftingCenter> STRUCTURE_DEFINITION;

    @Override
    public IStructureDefinition<TST_MegaCraftingCenter> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_MegaCraftingCenter>builder()
                .addShape(
                    "MAIN",
                    transpose(
                        // spotless:off
                        new String[][] {
                            { "BBBBBBB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BBBBBBB" },
                            { "BEEEEEB", "E     E", "E  D  E", "E DAD E", "E  D  E", "E     E", "BEEEEEB" },
                            { "BEEEEEB", "E  D  E", "E D D E", "ED A DE", "E D D E", "E  D  E", "BEEEEEB" },
                            { "BEE~EEB", "E DAD E", "ED A DE", "EAACAAE", "ED A DE", "E DAD E", "BEEEEEB" },
                            { "BEEEEEB", "E  D  E", "E D D E", "ED A DE", "E D D E", "E  D  E", "BEEEEEB" },
                            { "BEEEEEB", "E     E", "E  D  E", "E DAD E", "E  D  E", "E     E", "BEEEEEB" },
                            { "BBBBBBB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BBBBBBB" } }))
                // spotless:on
                .addElement('A', ofBlock(GregTech_API.sBlockCasings1, 14))
                .addElement(
                    'B',
                    GT_HatchElementBuilder.<TST_MegaCraftingCenter>builder()
                        .atLeast(InputBus, OutputBus)
                        .adder(TST_MegaCraftingCenter::addToMachineList)
                        .casingIndex(textureOffset + 12)
                        .dot(1)
                        .buildAndChain(ofBlock(TT_Container_Casings.sBlockCasingsTT, 12)))
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
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }

    // endregion

    // region General

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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        // spotless:off
        tt.addMachineType(TextEnums.tr("tst.megacraftingcenter.machinetype"))
            // #tr tst.megacraftingcenter.desc.0
            // # Do not use power.
            // #zh_CN 不需要耗电.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.0"))
            // #tr tst.megacraftingcenter.desc.1
            // # Time consumption is fixed at 6.4 second, output items in output buses.
            // #zh_CN 固定耗时 6.4 秒, 在输出总线产出产物.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.1"))
            // #tr tst.megacraftingcenter.desc.2
            // # Input encoded patterns into input bus, the valid will be moved to internal, the invalid will be moved to output bus.
            // #zh_CN 在输入总线内放入编码样板, 正确的样板将被转移到内部, 错误的样板将被转移到输出总线.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.2"))
            // #tr tst.megacraftingcenter.desc.3
            // # Support crafting table pattern and Dire Crafting process pattern.
            // #zh_CN 支持工作台样板和梦魇工作台处理样板.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.3"))
            // #tr tst.megacraftingcenter.desc.4
            // # Allow to double the pattern.
            // #zh_CN 允许倍增样板.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.4"))
            // #tr tst.megacraftingcenter.desc.5
            // # Use a screwdriver right click controller to move internal patterns to output bus.
            // #zh_CN 使用螺丝刀右键主机将内部样板转移至输出总线.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.5"))
            .addInfo(Text_SeparatingLine)
            .toolTipFinisher(ModName);
        // spotless:on
        return tt;
    }

}
