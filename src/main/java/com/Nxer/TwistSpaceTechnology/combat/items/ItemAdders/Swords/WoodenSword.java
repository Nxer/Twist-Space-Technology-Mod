package com.Nxer.TwistSpaceTechnology.combat.items.ItemAdders.Swords;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WoodenSword extends ItemAdder_Sword {

    public WoodenSword(ToolMaterial p_i45356_1_) {
        super(p_i45356_1_);
    }

    public WoodenSword(ToolMaterial p_i45356_1_, String Name, String unlocalizedname, CreativeTabs aCreativeTabs) {
        super(p_i45356_1_, Name, unlocalizedname, aCreativeTabs);
    }

    public WoodenSword(ToolMaterial p_i45356_1_, String Name, String unlocalizedname, CreativeTabs aCreativeTabs,
        List<String> tooltips) {
        super(p_i45356_1_, Name, unlocalizedname, aCreativeTabs, tooltips);
    }

    @Override
    public float func_150931_i() {
        return getBaseDamage();
    }

    @Override
    public float getBaseDamage() {
        return 20.0F;
    }

    @Override
    public float getDefence() {
        return 1.0F;
    }

    @Override
    public float getStrength() {
        return 1.0F;
    }

    @Override
    public float getIntelligence() {
        return 4.0F;
    }

    @Override
    public float getCritChance() {
        return 5.0F;
    }

    @Override
    public float getCritDamage() {
        return 1.0F;
    }

    @Override
    public float getResistance() {
        return 4.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        this.itemIcon = iconRegister.registerIcon("gtnhcommunitymod:ItemCombatGear/Sword/WoodenSword");
    }
}
