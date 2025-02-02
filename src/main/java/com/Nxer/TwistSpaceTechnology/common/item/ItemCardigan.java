package com.Nxer.TwistSpaceTechnology.common.item;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import gregtech.api.util.GTModHandler;
import ic2.api.item.IElectricItem;
import ic2.core.init.InternalName;
import ic2.core.item.armor.ItemArmorElectric;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ItemCardigan extends ItemArmorElectric implements IElectricItem {

    private static final double[] TIERED_MAX_CHARGE = {2.0E7};
    private static final double[] TIERED_TRANSFER_LIMIT = {2500.0};

    /**
     * The {@link InternalName} instance for Cardigans, created by the mixin ({@link com.Nxer.TwistSpaceTechnology.mixin.IC2_InternalName_Adder_Mixin}).
     */
    public static final InternalName Cardigan = Objects.requireNonNull(InternalName.valueOf("Cardigan"), "Failed to get InternalName instance for Cardigan!");

    public ItemCardigan(int tier) {
        // this item will be registered in the super class constructor, so don't register twice!
        super(Cardigan, Cardigan, 1, TIERED_MAX_CHARGE[tier], TIERED_TRANSFER_LIMIT[tier], tier);

        // #tr item.chestplateCardigan.name
        // # Cardigan
        // #zh_CN 羊毛衫
        setUnlocalizedName("chestplateCardigan");

        setCreativeTab(TstCreativeTabs.TabGeneral);
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 0.0;
    }

    @Override
    public int getEnergyPerDamage() {
        return 0;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
        //noinspection unchecked
        this.addInformationTypeChecked(itemStack, (List<String>) info);
    }

    private void addInformationTypeChecked(ItemStack itemStack, List<String> tooltips) {
        int tier = getTier(itemStack);
        tooltips.add(EnumChatFormatting.GRAY + "Tier: " + EnumChatFormatting.AQUA + tier);

        Optional<Long[]> chargeOptional = GTModHandler.getElectricItemCharge(itemStack);
        if (chargeOptional.isPresent()) {
            var current = chargeOptional.get()[0];
            var max = chargeOptional.get()[1];
            tooltips.add(EnumChatFormatting.GRAY + "Charged: " +
                EnumChatFormatting.GREEN + current +
                EnumChatFormatting.GRAY + "/" +
                EnumChatFormatting.GREEN + max);
        } else {
            tooltips.add(EnumChatFormatting.RED + "Information is not provided?");
        }
    }

    @Override // to override IC2 special logic
    public String getUnlocalizedName() {
        return "item." + unlocalizedName;
    }

    @Override // to override IC2 special logic
    public String getItemStackDisplayName(ItemStack itemStack) {
        return StatCollector.translateToLocal(getUnlocalizedName() + ".name").trim();
    }

    @Override // to override IC2 special logic
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":Cardigan");
    }

    @Override // to override IC2 special logic
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return TwistSpaceTechnology.RESOURCE_ROOT_ID + ":textures/armor/Cardigan_1.png";
    }
}
