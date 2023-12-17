package com.Nxer.TwistSpaceTechnology.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BaseDamageHandler {

    public static final BaseDamageHandler instance = new BaseDamageHandler();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHurting(LivingHurtEvent event) {
        float damage = event.ammount;
        if (event.source.getEntity() instanceof EntityPlayer) {
            ArmorEventHandler.INSTANCE.updatePlayerStats((EntityPlayer) event.source.getEntity());

                PlayerExtendedProperties SourceStats = PlayerExtendedProperties.instance
                    .from((EntityPlayer) event.source.getEntity());
                if ((event.source.getEntity().motionY < (double) 0) && !event.source.getEntity().onGround
                    && !event.source.getEntity()
                        .isRiding()
                    && event.source.damageType == "player")
                    damage = (float) ( 1.5
                        * (1 + SourceStats.CombatStats.get("BaseDamage") + SourceStats.CombatStats.get("Strength") / 50.0));
                else if (event.source.damageType != "indirectMagic") damage = (float) (SourceStats.CombatStats.get("BaseDamage")
                    + SourceStats.CombatStats.get("Strength") / 50.0 + 1);
                else damage = 1 + SourceStats.CombatStats.get("BaseDamage");

        }
        if (event.entityLiving instanceof EntityPlayer && event.source.damageType != "outOfWorld"
            && event.source.damageType != "generic") {
            ArmorEventHandler.INSTANCE.updatePlayerStats((EntityPlayer) event.entityLiving);
            damage *= 1.0F/(1.0F
                + (PlayerExtendedProperties.instance.from((EntityPlayer) event.entityLiving).CombatStats.get("Defence")
                    / 100.0F));
            event.source.setDamageBypassesArmor();
        }
        event.ammount = damage;
    }
}
