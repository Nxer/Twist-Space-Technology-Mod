package com.Nxer.TwistSpaceTechnology.combat.items.ItemAdders.Swords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.combat.items.ICombatGear;

public abstract class ItemAdder_Sword extends ItemSword implements ICombatGear {

    List<String> tooltips = new ArrayList<String>();

    public ItemAdder_Sword(ToolMaterial p_i45356_1_) {
        super(p_i45356_1_);
    }

    public ItemAdder_Sword(ToolMaterial p_i45356_1_, String Name, String unlocalizedname, CreativeTabs aCreativeTabs) {
        super(p_i45356_1_);
        this.setCreativeTab(aCreativeTabs);
        this.setUnlocalizedName(unlocalizedname);
    }

    public ItemAdder_Sword(ToolMaterial p_i45356_1_, String Name, String unlocalizedname, CreativeTabs aCreativeTabs,
        List<String> tooltips) {
        super(p_i45356_1_);
        this.tooltips = tooltips;
        this.setCreativeTab(aCreativeTabs);
        this.setUnlocalizedName(unlocalizedname);
    }

    public ItemStack initiItemStack(String Name, Item item) {
        return new ItemStack(item, 1, 0);
    }

    @Override
    public void addInformation(ItemStack aItemStack, EntityPlayer p_77624_2_, List theTooltipsList,
        boolean p_77624_4_) {
        if (!tooltips.isEmpty()) theTooltipsList.addAll(Arrays.asList(tooltips));
        theTooltipsList.add((String) ("Damage: " + EnumChatFormatting.RED + "+" + this.getBaseDamage()));
        if (this.getDefence() != 0)
            theTooltipsList.add((String) ("Defence: " + EnumChatFormatting.GREEN + "+" + this.getDefence()));
        if (this.getStrength() != 0)
            theTooltipsList.add((String) ("Strength: " + EnumChatFormatting.RED + "+" + this.getStrength()));
        if (this.getCritChance() != 0) theTooltipsList
            .add((String) ("Crit Chance: " + EnumChatFormatting.BLUE + "+" + this.getCritChance() + "%"));
        if (this.getCritDamage() != 0) theTooltipsList
            .add((String) ("Crit Damage: " + EnumChatFormatting.BLUE + "+" + this.getCritDamage() + "%"));
        if (this.getIntelligence() != 0)
            theTooltipsList.add((String) ("Intelligence: " + EnumChatFormatting.AQUA + "+" + this.getIntelligence()));
    }
}
