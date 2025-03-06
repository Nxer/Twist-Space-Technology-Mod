package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.system.RecipePattern.ExtremeCraftRecipeHandler.extremeCraftRecipes;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;
import static tectech.thing.casing.BlockGTCasingsTT.textureOffset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemActualPattern;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TT_MultiMachineBase_EM;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.TST_PatternAccessHatch;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

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
import appeng.util.item.AEItemStack;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.block.BlockQuantumGlass;
import tectech.thing.casing.TTCasingsContainer;

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

    /**
     * Generating visual pattern to handle actual crafting.
     */
    public static class ActualPattern implements ICraftingPatternDetails {

        protected ItemStack patternItem = new ItemStack(Blocks.fire);
        protected IAEItemStack[] inputs = new IAEItemStack[0];
        protected IAEItemStack[] outputs = new IAEItemStack[0];
        protected boolean canSubstitute = false;
        protected int priority = 0;
        public ICraftingPatternDetails originPattern;
        public int multiplier;

        private ActualPattern() {}

        /**
         * Generate a visual ICraftingPatternDetails with real pattern, and make input/output amount multiplied by
         * variable.
         *
         * @param origin     The origin pattern.
         * @param multiplier This pattern details are the origin pattern's multiplied by this parameter.
         */
        public ActualPattern(ICraftingPatternDetails origin, int multiplier) {
            this.originPattern = origin;
            this.multiplier = multiplier;
            this.patternItem = origin.getPattern();
            this.canSubstitute = origin.canSubstitute();
            this.priority = origin.getPriority();

            // regenerate items' amount
            ItemStack[] originInputs = convertAEToMC(origin.getCondensedInputs());
            ItemStack[] originOutputs = convertAEToMC(origin.getCondensedOutputs());

            Map<TST_ItemID, Long> inputs = new HashMap<>();
            Map<TST_ItemID, Long> outputs = new HashMap<>();

            for (ItemStack i : originInputs) {
                inputs.merge(TST_ItemID.create(i), (long) i.stackSize, Long::sum);
            }

            for (ItemStack i : originOutputs) {
                outputs.merge(TST_ItemID.create(i), (long) i.stackSize, Long::sum);
            }

            List<IAEItemStack> actualInputs = new ArrayList<>();
            List<IAEItemStack> actualOutputs = new ArrayList<>();

            for (Map.Entry<TST_ItemID, Long> i : inputs.entrySet()) {
                long amount = i.getValue() * multiplier;
                if (amount <= Integer.MAX_VALUE) {
                    // in int limitation
                    actualInputs.add(
                        AEItemStack.create(
                            i.getKey()
                                .getItemStack((int) amount)));
                } else {
                    // over int limitation
                    TST_ItemID itemID = i.getKey();
                    for (;;) {
                        if (amount > Integer.MAX_VALUE) {
                            actualInputs.add(AEItemStack.create(itemID.getItemStack(Integer.MAX_VALUE)));
                            amount -= Integer.MAX_VALUE;
                        } else if (amount < 1) {
                            break;
                        } else {
                            actualInputs.add(AEItemStack.create(itemID.getItemStack((int) amount)));
                            break;
                        }
                    }
                }
            }

            for (Map.Entry<TST_ItemID, Long> i : outputs.entrySet()) {
                long amount = i.getValue() * multiplier;
                if (amount <= Integer.MAX_VALUE) {
                    // in int limitation
                    actualOutputs.add(
                        AEItemStack.create(
                            i.getKey()
                                .getItemStack((int) amount)));
                } else {
                    // over int limitation
                    TST_ItemID itemID = i.getKey();
                    for (;;) {
                        if (amount > Integer.MAX_VALUE) {
                            actualOutputs.add(AEItemStack.create(itemID.getItemStack(Integer.MAX_VALUE)));
                            amount -= Integer.MAX_VALUE;
                        } else if (amount < 1) {
                            break;
                        } else {
                            actualOutputs.add(AEItemStack.create(itemID.getItemStack((int) amount)));
                            break;
                        }
                    }
                }
            }

            this.inputs = actualInputs.toArray(new IAEItemStack[0]);
            this.outputs = actualOutputs.toArray(new IAEItemStack[0]);

        }

        @Override
        public ItemStack getPattern() {
            return ItemActualPattern.getStackFromPattern(this);
        }

        @Override
        public IAEItemStack[] getInputs() {
            return inputs;
        }

        @Override
        public IAEItemStack[] getCondensedInputs() {
            return inputs;
        }

        @Override
        public IAEItemStack[] getCondensedOutputs() {
            return outputs;
        }

        @Override
        public IAEItemStack[] getOutputs() {
            return outputs;
        }

        @Override
        public boolean canSubstitute() {
            return canSubstitute;
        }

        @Override
        public int getPriority() {
            return priority;
        }

        @Override
        public void setPriority(int priority) {
            this.priority = priority;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }

            final ActualPattern other = (ActualPattern) obj;

            if (this.originPattern != null && other.originPattern != null) {
                return this.originPattern.equals(other.originPattern) && this.multiplier == other.multiplier;
            }
            return false;
        }

        @Override
        public int hashCode() {
            if (originPattern == null) return 0;
            return originPattern.hashCode() * 31 + multiplier;
        }
        // region unused

        @Override
        public boolean isCraftable() {
            return false;
        }

        @Override
        public ItemStack getOutput(InventoryCrafting craftingInv, World world) {
            return null;
        }

        @Override
        public boolean isValidItemForSlot(int slotIndex, ItemStack itemStack, World world) {
            return false;
        }

        // endregion
    }

    public static ItemStack[] convertAEToMC(IAEItemStack... STACK) {
        // TODO use normal code style instead `Stream`
        return Arrays.stream(STACK)
            .filter(Objects::nonNull)
            .map(IAEItemStack::getItemStack)
            .toArray(ItemStack[]::new);
    }

    /**
     *
     * @param r   The find recipe of this pattern.
     * @param in  The pattern input items.
     * @param out The pattern output item.
     * @return If this pattern is valid.
     */
    protected static boolean checkPatternRecipe(GTRecipe r, ItemStack[] in, ItemStack out) {
        ItemStack rOut = r.mOutputs[0];
        if (out == null || out.getItem() == null || out.stackSize < 1) return false;
        if (!GTUtility.areStacksEqual(out, rOut)) return false;
        // check amount
        if (out.stackSize < rOut.stackSize || out.stackSize % rOut.stackSize != 0) return false;
        // the multiple of pattern
        int multiple = out.stackSize / rOut.stackSize;

        Map<TST_ItemID, Long> rItemMap = new HashMap<>();
        Map<Item, Long> rWildcardMap = new HashMap<>();
        for (ItemStack i : r.mInputs) {
            if (i == null || i.getItem() == null || i.stackSize < 1) continue;
            ItemData iData = GTOreDictUnificator.getAssociation(i);
            if (iData != null) {
                rItemMap.merge(TST_ItemID.createNoNBT(iData.mUnificationTarget), (long) i.stackSize, Long::sum);
            } else if (i.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                rWildcardMap.merge(i.getItem(), (long) i.stackSize, Long::sum);
            } else {
                rItemMap.merge(TST_ItemID.createNoNBT(i), (long) i.stackSize, Long::sum);
            }
        }

        Map<TST_ItemID, Long> pItemMap = new HashMap<>();
        Map<Item, Long> pWildcardMap = new HashMap<>();
        for (ItemStack i : in) {
            if (i == null || i.getItem() == null || i.stackSize < 1) continue;
            ItemData iData = GTOreDictUnificator.getAssociation(i);
            if (iData != null) {
                pItemMap.merge(TST_ItemID.createNoNBT(iData.mUnificationTarget), (long) i.stackSize, Long::sum);
            } else {
                Item ii = i.getItem();
                if (rWildcardMap.containsKey(ii)) {
                    pWildcardMap.merge(ii, (long) i.stackSize, Long::sum);
                } else {
                    pItemMap.merge(TST_ItemID.createNoNBT(i), (long) i.stackSize, Long::sum);
                }

            }
        }

        if (rItemMap.size() != pItemMap.size() || rWildcardMap.size() != pWildcardMap.size()) return false;

        for (Map.Entry<TST_ItemID, Long> rItemEntry : rItemMap.entrySet()) {
            Long pAmount = pItemMap.get(rItemEntry.getKey());
            if (pAmount == null || pAmount == 0 || !pAmount.equals(rItemEntry.getValue() * multiple)) return false;
        }

        for (Map.Entry<Item, Long> rWildcardEntry : rWildcardMap.entrySet()) {
            Long pAmount = pWildcardMap.get(rWildcardEntry.getKey());
            if (pAmount == null || pAmount == 0 || !pAmount.equals(rWildcardEntry.getValue() * multiple)) return false;
        }

        return true;

    }

    /**
     * Check input item stack whether is a valid crafting pattern for crafting table recipe or Extreme Crafting table
     * recipe.
     *
     * @param pattern The item stack inputting.
     * @return If pattern is valid, return its ICraftingPatternDetails. Return null if it is invalid.
     */
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
                if (!TstUtils.areItemsValid(dInputs)) return null;

                // fine all available extreme recipes of this pattern
                GTRecipe[] validResult = extremeCraftRecipes.findRecipeQuery()
                    .items(dInputs)
                    .findAll()
                    .toArray(GTRecipe[]::new);

                if (validResult.length < 1) return null;

                @NotNull
                ItemStack pItems = dOutputs[0].getItemStack();
                if (pItems == null || pItems.getItem() == null || pItems.stackSize < 1) return null;

                if (validResult.length == 1) {
                    if (validResult[0] == null) return null;

                    if (checkPatternRecipe(validResult[0], dInputs, pItems)) {
                        return d;
                    } else {
                        return null;
                    }

                } else {
                    for (int i = 0; i < validResult.length; i++) {
                        if (checkPatternRecipe(validResult[i], dInputs, pItems)) {
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
    protected Collection<ICraftingPatternDetails> actualPatternDetails = new HashSet<>();
    private final HashMap<ICraftingPatternDetails, Long> cachedOutput = new HashMap<>();

    /**
     * Finally the pattern actual IO numbers will be multiplied by this number, also include the crafting table recipe
     * patterns.
     */
    protected int magnification = 1;

    @Nullable
    private AENetworkProxy gridProxy;

    protected boolean toReturnPatterns = false;

    /**
     * Re-calculate the patterns, flush the actual pattern pool.
     */
    public void recalculatePatterns() {
        actualPatternDetails.clear();
        for (ICraftingPatternDetails origin : patternDetails) {
            actualPatternDetails.add(new ActualPattern(origin, magnification));
        }
    }

    /**
     * Return a read-only pattern list.
     */
    public Collection<ItemStack> getInternalPatterns() {
        return ImmutableList.copyOf(internalPatterns);
    }

    public ItemStack extractPattern(@Nonnull AEItemStack request) {
        if (mMaxProgresstime > 0) return null;
        if (request.getStackSize() > 1) return null;
        if (internalPatterns.removeIf(request::isSameType)) {
            ItemStack stack = request.getItemStack();
            if (stack.getItem() instanceof ICraftingPatternItem pattern) {
                patternDetails.remove(
                    pattern.getPatternForItem(
                        stack,
                        this.getBaseMetaTileEntity()
                            .getWorld()));
            }
            flush();
            return request.getItemStack();
        }

        return null;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setInteger("magnification", magnification);
        tag.setInteger("patternAmount", patternDetails.size());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        // #tr MegaCraftingCenter.waila.ForceRunningMagnification
        // # Force running magnification
        // #zh_CN 强制运行倍率
        currentTip.add(
            TextEnums.tr("MegaCraftingCenter.waila.ForceRunningMagnification") + " : "
                + tag.getInteger("magnification"));

        // #tr MegaCraftingCenter.waila.PatternAmount
        // # Internal Pattern Amount
        // #zh_CN 已载入样板数量
        currentTip
            .add(TextEnums.tr("MegaCraftingCenter.waila.PatternAmount") + " : " + tag.getInteger("patternAmount"));
    }

    @Override
    public String[] getInfoData() {
        if (patternDetails.isEmpty()) {
            return super.getInfoData();
        }

        ArrayList<String> items = new ArrayList<>();
        for (ICraftingPatternDetails d : patternDetails) {
            items.add(
                d.getOutputs()[0].getItemStack()
                    .getDisplayName());
        }

        // every 4 item in a row
        int rows = (int) Math.ceil((double) items.size() / 4);

        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + rows + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);

        // #tr MegaCraftingCenter.info.InternalPatterns
        // # Internal patterns
        // #zh_CN 已载入样板
        ret[origin.length] = TextEnums.tr("MegaCraftingCenter.info.InternalPatterns");
        StringBuilder t = new StringBuilder();
        int signal = 0;
        int row = origin.length + 1;
        for (String s : items) {
            t.append(s)
                .append("; ");
            signal++;
            if (signal == 4) {
                signal = 0;
                ret[row] = t.toString();
                t = new StringBuilder();
                row++;
            }
        }
        if (signal != 0) {
            ret[row] = t.toString();
        }

        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setBoolean("toReturnPatterns", toReturnPatterns);
        aNBT.setInteger("magnification", magnification);

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
        magnification = aNBT.getInteger("magnification");
        if (magnification < 1) magnification = 1;

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

        flush();
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
        for (ItemStack in : inputs) {
            ItemStack refund = tryInjectPattern(in);
            if (refund != null) l.add(refund);
        }
        updateSlots();
        return l;
    }

    /**
     * Try to add pattern to internal inventory.
     *
     * @return Return null if all accepted, or unwanted item otherwise.
     */
    @Nullable
    public ItemStack tryInjectPattern(ItemStack in) {
        ItemStack refund = null;
        ICraftingPatternDetails d = checkPattern(in);
        if (d != null && !patternDetails.contains(d)) {
            patternDetails.add(d);

            if (in.stackSize > 1) {
                refund = GTUtility.copyAmountUnsafe(in.stackSize - 1, in);
                in.stackSize = 1;
            }
            internalPatterns.add(in.copy());
            flush();
            in.stackSize = 0;
            return refund;
        } else {
            refund = (in.copy());
            in.stackSize = 0;
            return refund;
        }
    }

    /**
     * Call ME net flush itself. Always call this when there is new things inputted.
     */
    protected void flush() {
        recalculatePatterns();
        if (getProxy().isActive()) {
            try {
                getProxy().getGrid()
                    .postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
            } catch (GridAccessException ignored) {}
        }
        notifyAcessHatch();
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {

        if (toReturnPatterns) {
            toReturnPatterns = false;
            mOutputItems = internalPatterns.toArray(new ItemStack[0]);
            internalPatterns.clear();
            patternDetails.clear();
            flush();
            mMaxProgresstime = Config.TickEveryProcess_MegaCraftingCenter;

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
        mMaxProgresstime = Config.TickEveryProcess_MegaCraftingCenter;
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
                GTUtility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("MegaCraftingCenter.onScrewdriverRightClick.failed"));
                return;
            }

            // return encoded patterns
            toReturnPatterns = true;

            // #tr MegaCraftingCenter.onScrewdriverRightClick.success
            // # Preparing to returning encoded patterns.
            // #zh_CN 正在准备退回样板.
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("MegaCraftingCenter.onScrewdriverRightClick.success"));

        }
    }

    /**
     * Commit all patterns this machine handled to ME net.
     *
     * @param craftingTracker crafting helper
     */
    @Override
    public void provideCrafting(@NotNull ICraftingProviderHelper craftingTracker) {
        AENetworkProxy proxy = this.getProxy();
        if (proxy != null && proxy.isReady()) {
            for (var details : actualPatternDetails) {
                craftingTracker.addCraftingOption(this, details);
            }
        }
    }

    /**
     * Receive pattern task from ME net, in this machine we just collect these task and turn to sign the output items.
     */
    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        return cachedOutput.merge(patternDetails, 1L, Long::sum) >= 1;
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
            // MTEHatchCraftingInputME
        }
        return this.gridProxy;
    }

    @Nullable
    public IGridNode getGridNode(@Nullable ForgeDirection dir) {
        // MTEHatchInput_ME
        AENetworkProxy proxy = this.getProxy();
        return proxy != null ? proxy.getNode() : null;
    }

    public void securityBreak() {
        this.getBaseMetaTileEntity()
            .disableWorking();
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        mPatternAccessHatch.clear();
        maintenance_EM();
        if (structureCheck_EM("MAIN", 3, 3, 0)) {
            if (mPatternAccessHatch.size() > 1) {
                return false;
            }
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
                            { "BEEEEEB", "E     E", "E     E", "E     E", "E     E", "E     E", "BEEEEEB" },
                            { "BEEEEEB", "E     E", "E     E", "E     E", "E     E", "E     E", "BEEEEEB" },
                            { "BEE~EEB", "E     E", "E     E", "E     E", "E     E", "E     E", "BEEEEEB" },
                            { "BEEEEEB", "E     E", "E     E", "E     E", "E     E", "E     E", "BEEEEEB" },
                            { "BEEEEEB", "E     E", "E     E", "E     E", "E     E", "E     E", "BEEEEEB" },
                            { "BBBBBBB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BEEEEEB", "BBBBBBB" } }))
                // spotless:on
                .addElement(
                    'B',
                    HatchElementBuilder.<TST_MegaCraftingCenter>builder()
                        .atLeast(InputBus, OutputBus, AccessHatchAdder)
                        .adder(TST_MegaCraftingCenter::superAddToMachineList)
                        .casingIndex(textureOffset + 12)
                        .dot(1)
                        .buildAndChain(ofBlock(TTCasingsContainer.sBlockCasingsTT, 4)))
                .addElement('E', ofBlock(BlockQuantumGlass.INSTANCE, 0))
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

    private static IHatchElement<TST_MegaCraftingCenter> AccessHatchAdder = new IHatchElement<TST_MegaCraftingCenter>() {

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {

            return ImmutableList.of(TST_PatternAccessHatch.class);
        }

        @Override
        public IGTHatchAdder<? super TST_MegaCraftingCenter> adder() {

            return TST_MegaCraftingCenter::addAccessHatchToMachineList;
        }

        @Override
        public String name() {

            return "PatternAccessHatch";
        }

        @Override
        public long count(TST_MegaCraftingCenter t) {

            return t.mPatternAccessHatch.size();
        }
    };
    public ArrayList<TST_PatternAccessHatch> mPatternAccessHatch = new ArrayList<TST_PatternAccessHatch>();

    public final boolean superAddToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (addAccessHatchToMachineList(aTileEntity, aBaseCasingIndex)) return true;
        return super.addToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public final boolean addAccessHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof TST_PatternAccessHatch pa) {
            mPatternAccessHatch.add(pa);
            pa.bind(this);

            pa.updateTexture(aBaseCasingIndex);
            return true;
        }
        return false;
    }

    // endregion

    // region General

    protected static final int SYNC_WINDOW_MAGNIFICATION_ID = 10_114;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(SYNC_WINDOW_MAGNIFICATION_ID, this::createMagnificationConfigurationWindow);
        builder.widget(
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(SYNC_WINDOW_MAGNIFICATION_ID);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(GTUITextures.OVERLAY_BUTTON_CYCLIC);
                    return ret.toArray(new IDrawable[0]);
                })
                // #tr MegaCraftingCenter.UI.MagnificationInfoMenuButton.name
                // # Pattern Magnification Configuration Menu
                // #zh_CN 样板倍率配置菜单
                .addTooltip(TextEnums.tr("MegaCraftingCenter.UI.MagnificationInfoMenuButton.name"))
                .setPos(174, 97));
    }

    protected ModularWindow createMagnificationConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(240, 80);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());

        builder.widget(
            // spotless:off
                   // #tr MegaCraftingCenter.UI.Magnification.ConfigurationDescription.text
                   // # Set actual pattern magnification, actual input/output numbers of patterns will be multiplied by this number.
                   // #zh_CN 设置样板实际运行倍率, 实际合成输入输出等于样板数值乘以此参数.
                   // spotless:on
            TextWidget.localised("MegaCraftingCenter.UI.Magnification.ConfigurationDescription.text")
                .setPos(20, 10)
                .setSize(200, 14))
            .widget(new TextFieldWidget().setSetterInt(val -> {
                magnification = val;
                flush();
            })
                .setGetterInt(() -> magnification)
                .setNumbers(1, Config.MaxMagnification_MegaCraftingCenter)
                .setOnScrollNumbers(1, 64, 2048)
                .setTextAlignment(Alignment.Center)
                .setTextColor(Color.WHITE.normal)
                .setSize(60, 18)
                .setPos(100, 36)
                .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD));

        return builder.build();
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
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(TextEnums.tr("tst.megacraftingcenter.machinetype"))
            // #tr tst.megacraftingcenter.desc.firstWords
            // # {\AQUA}{\ITALIC}{\BOLD}Goodbye, all crafting lags.{\RESET}{\GRAY}
            // #zh_CN {\AQUA}{\ITALIC}{\BOLD}再见了, 所有的合成卡顿.{\RESET}{\GRAY}
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.firstWords"))
            // #tr tst.megacraftingcenter.desc.0
            // # Do not use power. Need to connect the controller to ME net.
            // #zh_CN 不需要耗电. 需要将主机连接至ME网络.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.0"))
            // #tr tst.megacraftingcenter.desc.1
            // # Time consumption is fixed at 1 second, output items in output buses.
            // #zh_CN 固定耗时 1 秒, 在输出总线产出产物.
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
            // # Set the pattern Magnification parameters in the controller GUI.
            // #zh_CN 在主方块GUI内设置样板倍率参数.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.5"))
            // #tr tst.megacraftingcenter.desc.6
            // # The internal pattern input and output quantity will be multiplied by the magnification parameter as the actual pattern information in running.
            // #zh_CN 内部样板输入输出数量将乘以倍率参数作为运行时的实际样板信息.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.6"))
            // #tr tst.megacraftingcenter.desc.7
            // # Include crafting pattern (crafting table recipes).
            // #zh_CN 包括合成样板 (工作台配方).
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.7"))
            // #tr tst.megacraftingcenter.desc.onScrewDriverRightClick
            // # Use a screwdriver right click controller to move internal patterns to output bus.
            // #zh_CN 使用螺丝刀右键主机将内部样板转移至输出总线.
            .addInfo(TextEnums.tr("tst.megacraftingcenter.desc.onScrewDriverRightClick"))
            .addInfo(Text_SeparatingLine)
            .addInfo(TextEnums.MoreInfoCheckingInScanner.getText())
            .toolTipFinisher(ModName);
        // spotless:on
        return tt;
    }

    public void notifyAcessHatch() {
        mPatternAccessHatch.forEach(TST_PatternAccessHatch::onChange);

    }
}
