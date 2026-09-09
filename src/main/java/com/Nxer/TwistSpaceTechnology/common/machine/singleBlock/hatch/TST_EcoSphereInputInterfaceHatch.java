package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.util.GTUtility.dropItemToBlockPos;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ISeedData;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.tileentity.TileEntityCropSticks;
import com.gtnewhorizon.cropsnh.utility.CropsNHUtils;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.shapes.Rectangle;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.GTMod;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.machines.multi.MTETreeFarm.Mode;
import lombok.Getter;

@SkipGenerateDescription
public final class TST_EcoSphereInputInterfaceHatch extends MTEHatch implements IAddUIWidgets {

    private static final int TREE_INPUT_SLOT = 0;
    private static final int AQUATIC_INPUT_START = 1;
    private static final int AQUATIC_MAX_SLOTS = 4;
    private static final int GREENHOUSE_INPUT_START = AQUATIC_INPUT_START + AQUATIC_MAX_SLOTS;
    private static final int GREENHOUSE_MAX_SLOTS = 4;
    private static final int CLONING_WEAPON_START = GREENHOUSE_INPUT_START + GREENHOUSE_MAX_SLOTS;
    private static final int CLONING_MAX_WEAPON_SLOTS = 4;
    private static final int MAX_INPUT_SLOTS = CLONING_WEAPON_START + CLONING_MAX_WEAPON_SLOTS;
    // Capacity upgrades still add one slot per upgrade; they additionally multiply the per-slot stack limit by 4.
    // Cloning keeps 1 stack per slot regardless of upgrades.
    private static final InputSlotLayout EMPTY_INPUT_LAYOUT = new InputSlotLayout(0, 0, 0, 0);
    private static final InputSlotLayout[] MODE_INPUT_LAYOUTS = { new InputSlotLayout(TREE_INPUT_SLOT, 1, 4, 1),
        new InputSlotLayout(AQUATIC_INPUT_START, 1, AQUATIC_MAX_SLOTS, 1),
        new InputSlotLayout(GREENHOUSE_INPUT_START, 1, GREENHOUSE_MAX_SLOTS, 1),
        new InputSlotLayout(CLONING_WEAPON_START, 0, CLONING_MAX_WEAPON_SLOTS, 1) };

    private final boolean[] selectedTreeOutputs = new boolean[Mode.values().length];
    private int machineMode = -1;
    private int capacityUpgrades = 0;
    @Getter
    private int cloningRecipeId = 0;

    public TST_EcoSphereInputInterfaceHatch(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, MAX_INPUT_SLOTS, new String[] { ModNameDesc });
    }

    private TST_EcoSphereInputInterfaceHatch(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, MAX_INPUT_SLOTS, description, textures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new TST_EcoSphereInputInterfaceHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return getInputBusTextures(baseTexture);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return getInputBusTextures(baseTexture);
    }

    private ITexture[] getInputBusTextures(ITexture baseTexture) {
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { baseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) }
            : new ITexture[] { baseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isValidSlot(int index) {
        return index >= 0 && index < MAX_INPUT_SLOTS;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player) {
        openGui(player);
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity baseMetaTileEntity, int index, ForgeDirection side,
        ItemStack stack) {
        return side == baseMetaTileEntity.getFrontFacing() && isInputSlotActive(index);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity baseMetaTileEntity, int index, ForgeDirection side,
        ItemStack stack) {
        return side == baseMetaTileEntity.getFrontFacing() && isInputSlotActive(index) && isInputValid(index, stack);
    }

    public void setMachineState(int mode, int installedCapacityUpgrades) {
        int newCapacityUpgrades = Math.max(0, Math.min(4, installedCapacityUpgrades));
        if (mode == machineMode && newCapacityUpgrades < capacityUpgrades) {
            // Eject excess stacks when the per-slot stack limit shrinks (capacity upgrades removed).
            // Switching the mode beacon must NOT eject any stored items; they stay in their slots.
            int firstSlot = getFirstInputSlot(mode);
            int activeSlots = getActiveInputSlots(mode, newCapacityUpgrades);
            int slotLimit = getSlotStackLimit(mode, newCapacityUpgrades);
            for (int slot = firstSlot; slot < firstSlot + activeSlots; slot++) {
                ItemStack stack = mInventory[slot];
                if (stack == null || stack.stackSize <= slotLimit) continue;
                ItemStack excess = stack.copy();
                excess.stackSize = stack.stackSize - slotLimit;
                stack.stackSize = slotLimit;
                IGregTechTileEntity base = getBaseMetaTileEntity();
                if (base != null && base.isServerSide() && base.getWorld() != null) {
                    dropItemToBlockPos(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord(), excess);
                }
            }
        }
        machineMode = mode;
        capacityUpgrades = newCapacityUpgrades;
    }

    /**
     * Maximum stack size per slot for the given mode and capacity upgrade count.
     * Tree/aquatic/greenhouse: 1 base, x4 per capacity upgrade, capped at 64. Cloning stays at 1.
     */
    public static int getSlotStackLimit(int mode, int capacityUpgrades) {
        if (mode == 3) return 1;
        int limit = 1;
        for (int i = 0; i < capacityUpgrades; i++) {
            limit *= 4;
        }
        return Math.min(64, limit);
    }

    public ItemStack[] getModeInputs() {
        int slots = getActiveInputSlots();
        int firstSlot = getFirstInputSlot();
        List<ItemStack> inputs = new ArrayList<>(slots);
        // For greenhouse mode: deduplicate by crop IDENTITY and keep only the highest-rated seed stack.
        // key -> the current best stack; score map is kept separately because ItemStack equality is NBT-aware.
        Map<String, ItemStack> bestStacks = machineMode == 2 ? new LinkedHashMap<>() : null;
        Map<String, Double> bestScores = machineMode == 2 ? new HashMap<>() : null;
        ItemStack aquaticTarget = null;
        for (int i = firstSlot; i < firstSlot + slots; i++) {
            ItemStack stack = mInventory[i];
            if (stack == null || stack.stackSize <= 0) continue;
            if (machineMode == 1) {
                // Only one target kind may be focused; skip slots holding different items.
                if (aquaticTarget != null && !aquaticTarget.isItemEqual(stack)) continue;
                aquaticTarget = stack;
            }
            if (bestStacks != null) {
                // A hybrid seed and its alternate seed of the same crop share one identity; keep the
                // best-scoring stack so identical slots never stack their output twice.
                String key = getGreenhouseSeedIdentity(stack);
                double score = getGreenhouseSeedOutputScore(stack);
                ItemStack existing = bestStacks.get(key);
                if (existing == null || score > bestScores.getOrDefault(key, Double.NEGATIVE_INFINITY)) {
                    bestStacks.put(key, stack.copy());
                    bestScores.put(key, score);
                }
            } else {
                inputs.add(stack);
            }
        }
        if (bestStacks != null) inputs.addAll(bestStacks.values());
        return inputs.toArray(new ItemStack[0]);
    }

    /**
     * Resolves the greenhouse deduplication identity of a seed stack.
     * Hybrid seeds (ItemGenericSeed with crop nbt) and alternate seeds (raw crop items) that map to the
     * same CropsNH crop share the same "cropsnh:{id}" key, so they cannot be stacked twice. Stacks that
     * have no CropsNH identity (e.g. vanilla plantable crops) fall back to their raw item id.
     */
    private static String getGreenhouseSeedIdentity(ItemStack stack) {
        ISeedData seedData = CropsNHUtils.getAnalyzedSeedData(stack);
        if (seedData != null) return "cropsnh:" + seedData.getCrop()
            .getId();
        ICropCard crop = CropRegistry.instance.fromAlternateSeed(stack);
        if (crop != null) return "cropsnh:" + crop.getId();
        return "minecraft:" + stack.getItem()
            .getUnlocalizedName();
    }

    /**
     * Approximates the expected output per cycle of a greenhouse seed stack, mirroring the CropsNH
     * farm chain: harvest = (1 + avgDropIncrease) * chance * avgDropRounds, progress = per-tick growth
     * fraction * CYCLE_TICK_RATE_SCALAR. Alternate seeds (no stats) are scored with the default 1/1/1
     * chain times the ALTERNATE_SEED_EFFICIENCY penalty, so a bred hybrid seed always outranks feeding
     * the raw crop item. Only the ranking matters here, absolute values are irrelevant.
     */
    private static double getGreenhouseSeedOutputScore(ItemStack stack) {
        ISeedData seedData = CropsNHUtils.getAnalyzedSeedData(stack);
        if (seedData != null) {
            ICropCard crop = seedData.getCrop();
            int gain = seedData.getStats()
                .getGain();
            double avgDropRounds = TileEntityCropSticks.getAvgDropRounds(crop, gain);
            double avgDropIncrease = TileEntityCropSticks.getAvgDropCountIncrease(gain);
            int growthRate = TileEntityCropSticks.getGrowthRate(
                TileEntityCropSticks.MAX_NUTRIENT_SCORE,
                crop.getTier(),
                seedData.getStats()
                    .getGrowth());
            if (growthRate <= 0) return -1;
            int growthTicks = crop.getGrowthDuration() / growthRate;
            if (crop.getGrowthDuration() % growthRate != 0) growthTicks++;
            double progress = (1.0d / growthTicks) * (100.0d / 256.0d);
            return avgDropIncrease * avgDropRounds * progress;
        }
        ICropCard crop = CropRegistry.instance.fromAlternateSeed(stack);
        if (crop != null) {
            // Score as default 1/1/1 stats through the full chain times the alternate-seed penalty.
            int growthRate = TileEntityCropSticks
                .getGrowthRate(TileEntityCropSticks.MAX_NUTRIENT_SCORE, crop.getTier(), 1);
            if (growthRate <= 0) return -1;
            int growthTicks = crop.getGrowthDuration() / growthRate;
            if (crop.getGrowthDuration() % growthRate != 0) growthTicks++;
            double progress = (1.0d / growthTicks) * (100.0d / 256.0d);
            double avgDropRounds = TileEntityCropSticks.getAvgDropRounds(crop, 1);
            double avgDropIncrease = TileEntityCropSticks.getAvgDropCountIncrease(1);
            return avgDropIncrease * avgDropRounds * progress * ALTERNATE_SEED_EFFICIENCY_SCORE;
        }
        // Non-CropsNH stacks (vanilla plantable) can't be ranked; keep them as-is.
        return 0;
    }

    private static final double ALTERNATE_SEED_EFFICIENCY_SCORE = 0.25d;

    public EnumSet<Mode> getSelectedTreeOutputs() {
        EnumSet<Mode> selected = EnumSet.noneOf(Mode.class);
        for (Mode mode : Mode.values()) {
            if (selectedTreeOutputs[mode.ordinal()]) selected.add(mode);
        }
        return selected;
    }

    public ItemStack[] getCloningWeapons() {
        return getModeInputs();
    }

    public int getAquaticFocusWeight() {
        ItemStack[] targets = getModeInputs();
        if (targets.length == 0) return 0;
        int itemCount = 0;
        for (ItemStack target : targets) itemCount += target.stackSize;
        // Only one target kind is allowed (same-kind slot check), so the first target defines the type.
        // Offspring (the only rare target) weighs x41 per item, other targets x1 per item.
        return GTCMItemList.OffSpring.equal(targets[0]) ? itemCount * 41 : itemCount;
    }

    private int getActiveInputSlots() {
        return getActiveInputSlots(machineMode, capacityUpgrades);
    }

    private static int getActiveInputSlots(int mode, int capacityUpgrades) {
        InputSlotLayout layout = getInputSlotLayout(mode);
        return Math.min(layout.maximumSlots(), layout.baseSlots() + capacityUpgrades * layout.slotsPerUpgrade());
    }

    private boolean isInputSlotActive(int index) {
        int firstSlot = getFirstInputSlot();
        return index >= firstSlot && index < firstSlot + getActiveInputSlots();
    }

    private int getFirstInputSlot() {
        return getFirstInputSlot(machineMode);
    }

    private static int getFirstInputSlot(int mode) {
        return getInputSlotLayout(mode).firstSlot();
    }

    private static InputSlotLayout getInputSlotLayout(int mode) {
        if (mode < 0 || mode >= MODE_INPUT_LAYOUTS.length) return EMPTY_INPUT_LAYOUT;
        return MODE_INPUT_LAYOUTS[mode];
    }

    private boolean isInputValid(int index, ItemStack stack) {
        if (stack == null || stack.getItem() == null) return false;
        // Offspring only stacks by 1 (item-owned limit).
        if (machineMode != 1) return true;
        // Only one target kind may be focused: reject items differing from already placed targets.
        int firstSlot = getFirstInputSlot();
        for (int i = firstSlot; i < firstSlot + getActiveInputSlots(); i++) {
            if (i == index || mInventory[i] == null) continue;
            if (!mInventory[i].isItemEqual(stack)) return false;
        }
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setInteger("machineMode", machineMode);
        nbt.setInteger("capacityUpgrades", capacityUpgrades);
        nbt.setInteger("cloningRecipeId", cloningRecipeId);
        for (Mode mode : Mode.values()) {
            nbt.setBoolean("treeOutput" + mode.ordinal(), selectedTreeOutputs[mode.ordinal()]);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        machineMode = nbt.hasKey("machineMode") ? nbt.getInteger("machineMode") : -1;
        capacityUpgrades = nbt.getInteger("capacityUpgrades");
        cloningRecipeId = Math.max(0, nbt.getInteger("cloningRecipeId"));
        for (Mode mode : Mode.values()) {
            selectedTreeOutputs[mode.ordinal()] = nbt.getBoolean("treeOutput" + mode.ordinal());
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> machineMode, value -> machineMode = value))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> capacityUpgrades, value -> capacityUpgrades = value))
            .widget(createWaitingText())
            .widget(
                new ButtonWidget().setOnClick(
                    (clickData, widget) -> { if (clickData.mouseButton == 0) dropInventoryRange(0, MAX_INPUT_SLOTS); })
                    .setPlayClickSound(true)
                    .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_EXPORT)
                    // #tr EcoSphereInputInterface.gui.dropAllItems
                    // # Drop all stored items
                    // #zh_CN 清空所有物品
                    .addTooltip(StatCollector.translateToLocal("EcoSphereInputInterface.gui.dropAllItems"))
                    .setPos(7, 63)
                    .setSize(16, 16));

        addTreeInputSlots(builder);
        addTreeOutputButtons(builder);
        addAquaticSlots(builder);
        addGreenhouseSlots(builder);
        addCloningRecipeInput(builder);
        addCloningWeaponSlots(builder);
    }

    private void dropInventoryRange(int firstSlot, int endSlot) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null || !base.isServerSide() || base.getWorld() == null) return;

        boolean changed = false;
        for (int slot = Math.max(0, firstSlot); slot < Math.min(endSlot, mInventory.length); slot++) {
            ItemStack stack = mInventory[slot];
            if (stack == null || stack.stackSize <= 0) continue;
            dropItemToBlockPos(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord(), stack);
            mInventory[slot] = null;
            changed = true;
        }
        if (changed) base.markDirty();
    }

    private Widget createWaitingText() {
        return TextWidget
            .dynamicString(
                () -> StatCollector
                    .translateToLocal("GT5U.gui.text.recipe_result.eco_sphere_simulator_waiting_for_mode_beacon"))
            .setTextAlignment(Alignment.Center)
            .setDefaultColor(Color.WHITE.normal)
            .setPos(20, 35)
            .setSize(136, 28)
            .setEnabled(widget -> machineMode < 0);
    }

    @Desugar
    private record InputSlotLayout(int firstSlot, int baseSlots, int maximumSlots, int slotsPerUpgrade) {}

    private void addTreeOutputButtons(ModularWindow.Builder builder) {
        for (Mode mode : Mode.values()) {
            int index = mode.ordinal();
            int x = 101 + index % 2 * 20;
            int y = 26 + index / 2 * 20;
            builder.widget(
                new ButtonWidget()
                    .setOnClick((clickData, widget) -> selectedTreeOutputs[index] = !selectedTreeOutputs[index])
                    .setBackground(() -> getTreeButtonBackground(mode, selectedTreeOutputs[index]))
                    .attachSyncer(
                        new FakeSyncWidget.BooleanSyncer(
                            () -> selectedTreeOutputs[index],
                            value -> selectedTreeOutputs[index] = value),
                        builder)
                    .setPos(x, y)
                    .setSize(18, 18)
                    .setEnabled(widget -> machineMode == 0));
        }
    }

    private IDrawable[] getTreeButtonBackground(Mode mode, boolean selected) {
        ItemStack display = switch (mode) {
            case LOG -> new ItemStack(Blocks.log);
            case SAPLING -> new ItemStack(Blocks.sapling);
            case LEAVES -> new ItemStack(Blocks.leaves);
            case FRUIT -> new ItemStack(Items.apple);
        };
        if (selected) return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED, new ItemDrawable(display) };
        // Fade inactive products so the selected full-color buttons are easy to scan.
        return new IDrawable[] { GTUITextures.BUTTON_STANDARD, new ItemDrawable(display),
            new Rectangle().setColor(0x98707070) };
    }

    private void addAquaticSlots(ModularWindow.Builder builder) {
        // 2x2 grid centered in the window (176px wide).
        for (int index = 0; index < AQUATIC_MAX_SLOTS; index++) {
            builder.widget(createInputSlot(AQUATIC_INPUT_START + index, 70 + index % 2 * 18, 26 + index / 2 * 18, 1));
        }
    }

    private void addGreenhouseSlots(ModularWindow.Builder builder) {
        // 2x2 grid centered in the window.
        for (int index = 0; index < GREENHOUSE_MAX_SLOTS; index++) {
            builder
                .widget(createInputSlot(GREENHOUSE_INPUT_START + index, 70 + index % 2 * 18, 26 + index / 2 * 18, 2));
        }
    }

    private void addTreeInputSlots(ModularWindow.Builder builder) {
        // 2x2 grid centered in the area left of the tree output buttons.
        for (int index = 0; index < 4; index++) {
            builder.widget(createInputSlot(TREE_INPUT_SLOT + index, 34 + index % 2 * 18, 26 + index / 2 * 18, 0));
        }
    }

    private SlotWidget createInputSlot(int index, int x, int y, int requiredMode) {
        BaseSlot slot = new BaseSlot(inventoryHandler, index) {

            @Override
            public int getSlotStackLimit() {
                return TST_EcoSphereInputInterfaceHatch.getSlotStackLimit(requiredMode, capacityUpgrades);
            }

            @Override
            public boolean isEnabled() {
                return machineMode == requiredMode && isInputSlotActive(index);
            }

            @Override
            public boolean isItemValidPhantom(ItemStack stack) {
                return isEnabled() && isInputValid(index, stack) && super.isItemValidPhantom(stack);
            }
        };
        SlotWidget widget = new SlotWidget(slot);
        widget.setBackground(
            () -> isInputSlotActive(index) ? new IDrawable[] { getGUITextureSet().getItemSlot() }
                : new IDrawable[] { getGUITextureSet().getItemSlot(), new Rectangle().setColor(0xB0000000) });
        widget.setPos(x, y);
        widget.setEnabled(value -> machineMode == requiredMode);
        return widget;
    }

    private void addCloningRecipeInput(ModularWindow.Builder builder) {
        builder.widget(
            // #tr EcoSphereInputInterface.gui.recipeNumber
            // # Recipe Number
            // #zh_CN 配方编号
            TextWidget.localised("EcoSphereInputInterface.gui.recipeNumber")
                .setTextAlignment(Alignment.Center)
                .setPos(8, 21)
                .setSize(80, 14)
                .setEnabled(widget -> machineMode == 3))
            .widget(new TextFieldWidget() {

                @Override
                public boolean onKeyPressed(char character, int keyCode) {
                    if (character == Character.MIN_VALUE) {
                        character = switch (keyCode) {
                            case Keyboard.KEY_NUMPAD0 -> '0';
                            case Keyboard.KEY_NUMPAD1 -> '1';
                            case Keyboard.KEY_NUMPAD2 -> '2';
                            case Keyboard.KEY_NUMPAD3 -> '3';
                            case Keyboard.KEY_NUMPAD4 -> '4';
                            case Keyboard.KEY_NUMPAD5 -> '5';
                            case Keyboard.KEY_NUMPAD6 -> '6';
                            case Keyboard.KEY_NUMPAD7 -> '7';
                            case Keyboard.KEY_NUMPAD8 -> '8';
                            case Keyboard.KEY_NUMPAD9 -> '9';
                            default -> Character.MIN_VALUE;
                        };
                    }
                    return super.onKeyPressed(character, keyCode);
                }
            }.setSetterInt(value -> cloningRecipeId = value)
                .setGetterInt(() -> cloningRecipeId)
                .setNumbers(0, Integer.MAX_VALUE)
                .setOnScrollNumbers(1, 10, 100)
                .setTextAlignment(Alignment.Center)
                .setTextColor(Color.WHITE.normal)
                .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                .setPos(18, 39)
                .setSize(60, 18)
                .setEnabled(widget -> machineMode == 3));
    }

    private void addCloningWeaponSlots(ModularWindow.Builder builder) {
        for (int index = 0; index < CLONING_MAX_WEAPON_SLOTS; index++) {
            builder.widget(createInputSlot(CLONING_WEAPON_START + index, 106 + index % 2 * 18, 30 + index / 2 * 18, 3));
        }
    }
}
