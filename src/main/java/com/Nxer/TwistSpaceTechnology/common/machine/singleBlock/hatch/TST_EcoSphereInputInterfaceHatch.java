package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.util.GTUtility.dropItemToBlockPos;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.Handler.DirectedMobClonerWeaponHandler;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;
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

@SkipGenerateDescription
public final class TST_EcoSphereInputInterfaceHatch extends MTEHatch implements IAddUIWidgets {

    private static final int TREE_INPUT_SLOT = 0;
    private static final int AQUATIC_INPUT_START = 1;
    private static final int AQUATIC_MAX_SLOTS = 16;
    private static final int GREENHOUSE_INPUT_START = AQUATIC_INPUT_START + AQUATIC_MAX_SLOTS;
    private static final int GREENHOUSE_MAX_SLOTS = 16;
    private static final int CLONING_WEAPON_START = GREENHOUSE_INPUT_START + GREENHOUSE_MAX_SLOTS;
    private static final int CLONING_MAX_WEAPON_SLOTS = 4;
    private static final int MAX_INPUT_SLOTS = CLONING_WEAPON_START + CLONING_MAX_WEAPON_SLOTS;
    private static final int AQUATIC_BASE_SLOTS = 1;
    private static final int GREENHOUSE_BASE_SLOTS = 1;
    private static final InputSlotLayout EMPTY_INPUT_LAYOUT = new InputSlotLayout(0, 0, 0, 0);
    private static final InputSlotLayout[] MODE_INPUT_LAYOUTS = { new InputSlotLayout(TREE_INPUT_SLOT, 1, 1, 0),
        new InputSlotLayout(AQUATIC_INPUT_START, AQUATIC_BASE_SLOTS, AQUATIC_MAX_SLOTS, 5),
        new InputSlotLayout(GREENHOUSE_INPUT_START, GREENHOUSE_BASE_SLOTS, GREENHOUSE_MAX_SLOTS, 5),
        new InputSlotLayout(CLONING_WEAPON_START, 0, CLONING_MAX_WEAPON_SLOTS, 1) };

    private final boolean[] selectedTreeOutputs = new boolean[Mode.values().length];
    private int machineMode = -1;
    private int capacityUpgrades = 0;
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
        if (mode != machineMode || newCapacityUpgrades != capacityUpgrades) {
            // Preserve inactive modes, but eject inaccessible inputs when their mode is selected again.
            int firstSlot = getFirstInputSlot(mode);
            int activeSlots = getActiveInputSlots(mode, newCapacityUpgrades);
            dropInventoryRange(firstSlot + activeSlots, firstSlot + getMaxInputSlots(mode));
        }
        machineMode = mode;
        capacityUpgrades = newCapacityUpgrades;
    }

    public ItemStack[] getModeInputs() {
        int slots = getActiveInputSlots();
        int firstSlot = getFirstInputSlot();
        List<ItemStack> inputs = new ArrayList<>(slots);
        Set<TST_ItemID> greenhouseInputs = machineMode == 2 ? new HashSet<>() : null;
        ItemStack aquaticTarget = null;
        for (int i = firstSlot; i < firstSlot + slots; i++) {
            ItemStack stack = mInventory[i];
            if (stack == null || stack.stackSize <= 0) continue;
            if (machineMode == 1) {
                if (aquaticTarget != null && !aquaticTarget.isItemEqual(stack)) continue;
                aquaticTarget = stack;
            }
            if (greenhouseInputs != null) {
                if (!greenhouseInputs.add(TST_ItemID.create(stack))) continue;
                ItemStack seed = stack.copy();
                seed.stackSize = 1;
                inputs.add(seed);
            } else {
                inputs.add(stack);
            }
        }
        return inputs.toArray(new ItemStack[0]);
    }

    public EnumSet<Mode> getSelectedTreeOutputs() {
        EnumSet<Mode> selected = EnumSet.noneOf(Mode.class);
        for (Mode mode : Mode.values()) {
            if (selectedTreeOutputs[mode.ordinal()]) selected.add(mode);
        }
        return selected;
    }

    public int getCloningRecipeId() {
        return cloningRecipeId;
    }

    public ItemStack[] getCloningWeapons() {
        return getModeInputs();
    }

    public int getAquaticTargetingMultiplier() {
        ItemStack[] targets = getModeInputs();
        if (targets.length == 0) return 0;
        boolean rareTarget = GTCMItemList.OffSpring.equal(targets[0]) || GTCMItemList.FountOfEcology.equal(targets[0]);
        if (rareTarget) return targets.length * 41;
        int itemCount = 0;
        for (ItemStack target : targets) itemCount += target.stackSize;
        return itemCount;
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

    private static int getMaxInputSlots(int mode) {
        return getInputSlotLayout(mode).maximumSlots();
    }

    private static InputSlotLayout getInputSlotLayout(int mode) {
        if (mode < 0 || mode >= MODE_INPUT_LAYOUTS.length) return EMPTY_INPUT_LAYOUT;
        return MODE_INPUT_LAYOUTS[mode];
    }

    private boolean isInputValid(int index, ItemStack stack) {
        if (stack == null || stack.getItem() == null) return false;
        if (machineMode == 3) return DirectedMobClonerWeaponHandler.isSupportedWeapon(stack);
        if (machineMode != 1) return true;
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
            .widget(createInputSlot(TREE_INPUT_SLOT, 47, 35, 0))
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
        for (int index = 0; index < AQUATIC_MAX_SLOTS; index++) {
            builder.widget(createInputSlot(AQUATIC_INPUT_START + index, 52 + index % 4 * 18, 8 + index / 4 * 18, 1));
        }
    }

    private void addGreenhouseSlots(ModularWindow.Builder builder) {
        for (int index = 0; index < GREENHOUSE_MAX_SLOTS; index++) {
            builder.widget(createInputSlot(GREENHOUSE_INPUT_START + index, 52 + index % 4 * 18, 8 + index / 4 * 18, 2));
        }
    }

    private SlotWidget createInputSlot(int index, int x, int y, int requiredMode) {
        BaseSlot slot = new BaseSlot(inventoryHandler, index) {

            @Override
            public int getSlotStackLimit() {
                return requiredMode == 3 ? 1 : 64;
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
