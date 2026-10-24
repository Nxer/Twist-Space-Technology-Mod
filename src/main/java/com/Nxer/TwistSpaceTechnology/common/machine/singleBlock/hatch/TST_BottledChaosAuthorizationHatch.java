package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DATA_ACCESS;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

public abstract class TST_BottledChaosAuthorizationHatch extends MTEHatch implements IAddUIWidgets {

    protected TST_BottledChaosAuthorizationHatch(int id, String name, String nameRegional, int tier,
        String[] description) {
        super(id, name, nameRegional, tier, 1, description);
    }

    protected TST_BottledChaosAuthorizationHatch(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, 1, description, textures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(OVERLAY_DATA_ACCESS) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(OVERLAY_DATA_ACCESS) };
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
        return index == 0;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0 && isAuthorizationItem(stack);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player) {
        openGui(player);
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity baseMetaTileEntity, int index, ForgeDirection side,
        ItemStack stack) {
        return index == 0;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity baseMetaTileEntity, int index, ForgeDirection side,
        ItemStack stack) {
        return index == 0 && isAuthorizationItem(stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public ItemStack getAuthorizationStack() {
        ItemStack stack = mInventory[0];
        return isAuthorizationItem(stack) ? stack : null;
    }

    protected abstract boolean isAuthorizationItem(ItemStack stack);

    protected IDrawable[] getSlotBackground() {
        return new IDrawable[] { getGUITextureSet().getItemSlot() };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(new BaseSlot(inventoryHandler, 0)).setFilter(this::isAuthorizationItem)
                .setBackground(getSlotBackground())
                .setPos(79, 34));
    }
}
