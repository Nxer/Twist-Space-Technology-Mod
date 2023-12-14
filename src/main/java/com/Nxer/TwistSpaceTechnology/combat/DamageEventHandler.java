package com.Nxer.TwistSpaceTechnology.combat;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DamageEventHandler {

    public static final DamageEventHandler instance = new DamageEventHandler();

    @SubscribeEvent
    public void onHurting(LivingHurtEvent event) {
        float damage = event.ammount;
        if (event.source.getEntity() instanceof EntityPlayer) {

            PlayerExtendedProperties SourceStats = PlayerExtendedProperties.instance
                .from((EntityPlayer) event.source.getEntity());
            damage = (damage + SourceStats.CombatStats.get("BaseDamage"))
                * (float) (SourceStats.CombatStats.get("Strength") / 100.0 + 1)
                * (float) (SourceStats.CombatStats.get("BaseDamageMultipiler") / 100.0 + 1);
            switch (event.source.damageType) {
                case "arrow": {
                    damage *= (float) (SourceStats.CombatStats.get("RangeDamageMultipiler") / 100.0 + 1);
                    break;
                }
                case "magic": {
                    damage *= (float) (SourceStats.CombatStats.get("MagicDamageMultipiler") / 100.0 + 1);
                    break;
                }
                default: {
                    damage *= (float) (SourceStats.CombatStats.get("MeleeDamageMultipiler") / 100.0 + 1);
                    break;
                }
            }
            if (new Random().nextInt(99) < SourceStats.CombatStats.get("CritChance")) {
                damage *= (float) (SourceStats.CombatStats.get("CritDamage") / 100.0 + 1);
            }
        }
        if (event.entityLiving instanceof EntityPlayer) {
            damage *= (float) (1
                - PlayerExtendedProperties.instance.from((EntityPlayer) event.entityLiving).CombatStats.get("Resistant")
                    / 100.0);
        }
        event.ammount = damage;
    }
}
