package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModNameDesc;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.util.GTUtility.dropItemToBlockPos;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.treefarm.EcoSphereUpgradeType;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.shapes.Rectangle;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.GTMod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

@SkipGenerateDescription
public final class TST_EcoSphereUpgradeInterfaceHatch extends MTEHatch implements IAddUIWidgets {

    private static final int MAX_UPGRADE_SLOTS = 4;

    private int machineMode = -1;
    private int structureTier = 1;

    public TST_EcoSphereUpgradeInterfaceHatch(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, MAX_UPGRADE_SLOTS, new String[] { ModNameDesc });
    }

    private TST_EcoSphereUpgradeInterfaceHatch(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, MAX_UPGRADE_SLOTS, description, textures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new TST_EcoSphereUpgradeInterfaceHatch(mName, mTier, mDescriptionArray, mTextures);
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
        return index >= 0 && index < MAX_UPGRADE_SLOTS;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player) {
        openGui(player);
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity baseMetaTileEntity, int index, ForgeDirection side,
        ItemStack stack) {
        return side == baseMetaTileEntity.getFrontFacing() && isSlotActive(index);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity baseMetaTileEntity, int index, ForgeDirection side,
        ItemStack stack) {
        return side == baseMetaTileEntity.getFrontFacing() && isSlotActive(index) && isUpgradeValid(index, stack);
    }

    public void setMachineState(int mode, int tier) {
        int newStructureTier = Math.max(1, tier);
        // Ignore the temporary unbound state so replacing a beacon does not eject every upgrade.
        if (machineMode >= 0 && mode >= 0 && mode != machineMode) dropUpgradesInvalidForMode(mode);
        if (machineMode == mode && structureTier == newStructureTier) return;
        machineMode = mode;
        structureTier = newStructureTier;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null && base.isServerSide()) base.markDirty();
    }

    private void dropUpgradesInvalidForMode(int mode) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null || !base.isServerSide() || base.getWorld() == null) return;

        boolean changed = false;
        for (int slot = 0; slot < mInventory.length; slot++) {
            ItemStack stack = mInventory[slot];
            if (stack == null || stack.stackSize <= 0) continue;
            EcoSphereUpgradeType type = EcoSphereUpgradeType.fromStack(stack);
            if (type != null && type.isAllowedForMode(mode)) continue;
            dropItemToBlockPos(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord(), stack);
            mInventory[slot] = null;
            changed = true;
        }
        if (changed) base.markDirty();
    }

    public ItemStack[] getUpgrades() {
        List<ItemStack> upgrades = new ArrayList<>(getActiveSlots());
        for (int i = 0; i < getActiveSlots(); i++) {
            if (mInventory[i] != null && mInventory[i].stackSize > 0) upgrades.add(mInventory[i]);
        }
        return upgrades.toArray(new ItemStack[0]);
    }

    private int getActiveSlots() {
        return structureTier >= 2 ? 4 : 1;
    }

    private boolean isSlotActive(int index) {
        return index >= 0 && index < getActiveSlots();
    }

    private boolean isUpgradeValid(int index, ItemStack stack) {
        EcoSphereUpgradeType type = EcoSphereUpgradeType.fromStack(stack);
        if (type == null || !type.isAllowedForMode(machineMode)) return false;
        if (type.getSpecialUpgrade() == null) return true;
        for (int i = 0; i < getActiveSlots(); i++) {
            if (i != index && type.matches(mInventory[i])) return false;
        }
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setInteger("ecoSphereMachineMode", machineMode);
        nbt.setInteger("ecoSphereStructureTier", structureTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        machineMode = -1;
        if (nbt.hasKey("ecoSphereMachineMode")) {
            machineMode = nbt.getInteger("ecoSphereMachineMode");
        } else if (nbt.hasKey("machineMode")) {
            machineMode = nbt.getInteger("machineMode");
        }

        int savedStructureTier = nbt.getInteger("structureTier");
        if (nbt.hasKey("ecoSphereStructureTier")) {
            savedStructureTier = nbt.getInteger("ecoSphereStructureTier");
        }
        structureTier = Math.max(1, savedStructureTier);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        SlotWidget[] upgradeSlots = new SlotWidget[MAX_UPGRADE_SLOTS];
        for (int index = 0; index < MAX_UPGRADE_SLOTS; index++) {
            Pos2d position = getUpgradeSlotPosition(index);
            upgradeSlots[index] = createUpgradeSlot(index, position.x, position.y);
        }

        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> machineMode, value -> machineMode = value))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> structureTier, value -> {
                structureTier = value;
                for (int index = 0; index < MAX_UPGRADE_SLOTS; index++) {
                    upgradeSlots[index].setPosSilent(getUpgradeSlotPosition(index));
                }
            }))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal(
                            "GT5U.gui.text.recipe_result.eco_sphere_simulator_waiting_for_mode_beacon"))
                    .setTextAlignment(Alignment.Center)
                    .setDefaultColor(Color.WHITE.normal)
                    .setPos(20, 35)
                    .setSize(136, 28)
                    .setEnabled(widget -> machineMode < 0));

        for (int index = 0; index < MAX_UPGRADE_SLOTS; index++) {
            builder.widget(upgradeSlots[index]);
        }
    }

    private Pos2d getUpgradeSlotPosition(int index) {
        if (structureTier < 2) return new Pos2d(79, 35);
        return new Pos2d(70 + index % 2 * 18, 26 + index / 2 * 18);
    }

    private SlotWidget createUpgradeSlot(int index, int x, int y) {
        BaseSlot slot = new BaseSlot(inventoryHandler, index) {

            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            @Override
            public boolean isEnabled() {
                return machineMode >= 0 && isSlotActive(index);
            }

            @Override
            public boolean isItemValidPhantom(ItemStack stack) {
                return isEnabled() && isUpgradeValid(index, stack) && super.isItemValidPhantom(stack);
            }
        };
        SlotWidget widget = new SlotWidget(slot);
        widget.setBackground(
            () -> isSlotActive(index) ? new IDrawable[] { getGUITextureSet().getItemSlot() }
                : new IDrawable[] { getGUITextureSet().getItemSlot(), new Rectangle().setColor(0xB0000000) });
        widget.setPos(x, y);
        widget.setEnabled(value -> machineMode >= 0 && isSlotActive(index));
        return widget;
    }
}
