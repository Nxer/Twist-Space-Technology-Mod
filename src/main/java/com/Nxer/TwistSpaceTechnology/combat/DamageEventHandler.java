package com.Nxer.TwistSpaceTechnology.combat;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DamageEventHandler {

    public static final DamageEventHandler instance = new DamageEventHandler();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHurting(LivingHurtEvent event) {
        float damage = event.ammount;

        if (event.source.getEntity() instanceof EntityPlayer) {
            PlayerExtendedProperties stats = (PlayerExtendedProperties) event.source.getEntity()
                .getExtendedProperties("COMBAT_STATS");
            damage = (damage + stats.getStrength() / 10.0F) * (1 + stats.getStrength() / 100.0F);
            if (new Random().nextFloat() < stats.getCritChance() / 100.0F) {
                damage *= (float) (stats.getCritDamage() / 100.0 + 1);
            }
            if (event.source.isProjectile()) {
                damage *= (1 + stats.getRangeDamageMultipiler() / 100.0F);
            }
            if (event.source.isMagicDamage()) {
                damage *= (1 + stats.getMagicDamageMultipiler() / 100.0F);
            }
            if (event.source.damageType == "player") {
                damage *= (1 + stats.getMeleeDamageMultipiler() / 100.0F);
            }
            damage *= (1 + stats.getBaseDamageMultipiler() / 100.0F);
            event.source.setDamageIsAbsolute();
        }
        if (event.entityLiving instanceof EntityPlayer && !event.source.isUnblockable()) {
            PlayerExtendedProperties stats = (PlayerExtendedProperties) event.entityLiving
                .getExtendedProperties("COMBAT_STATS");
            damage *= 1.0F / (1.0F + (stats.getDefence() / 100.0F));
            damage *= (1.0F - stats.getResistance() / 100.0F);
            event.source.setDamageBypassesArmor();
        }
        event.ammount = damage;
    }
}
