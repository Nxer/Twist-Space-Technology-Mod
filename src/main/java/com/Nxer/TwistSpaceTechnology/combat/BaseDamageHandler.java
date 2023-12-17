package com.Nxer.TwistSpaceTechnology.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BaseDamageHandler {

    public static final BaseDamageHandler instance = new BaseDamageHandler();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHurting(LivingHurtEvent event) {
        ;
        float damage = event.ammount;
        if (event.source.getEntity() instanceof EntityPlayer) {
            PlayerExtendedProperties SourceStats = PlayerExtendedProperties.instance
                .from((EntityPlayer) event.source.getEntity());
            if ((event.source.getEntity().motionY < (double) 0) && !event.source.getEntity().onGround
                && !event.source.getEntity()
                    .isRiding()
                && event.source.damageType == "player")
                damage = (float) (damage + 1.5
                    * (SourceStats.CombatStats.get("BaseDamage") + SourceStats.CombatStats.get("Strength") / 50.0));
            else damage = (float) (damage + SourceStats.CombatStats.get("BaseDamage")
                + SourceStats.CombatStats.get("Strength") / 50.0);
        }
        event.ammount = damage;
    }
}
