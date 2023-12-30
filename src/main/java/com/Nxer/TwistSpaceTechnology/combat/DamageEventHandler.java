package com.Nxer.TwistSpaceTechnology.combat;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.util.GT_Utility;

public class DamageEventHandler {

    public static final DamageEventHandler instance = new DamageEventHandler();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHurting(LivingHurtEvent event) {
        float damage = event.ammount;
        if (event.source.getEntity() instanceof EntityPlayer) {

            PlayerExtendedProperties SourceStats = PlayerExtendedProperties.instance
                .from((EntityPlayer) event.source.getEntity());
            switch (event.source.damageType) {
                case "arrow": {
                    damage = damage * (float) (SourceStats.CombatStats.get("Strength") / 100.0 + 1)
                        * (float) (SourceStats.CombatStats.get("BaseDamageMultipiler") / 100.0 + 1);
                    damage *= (float) (SourceStats.CombatStats.get("RangeDamageMultipiler") / 100.0 + 1);
                    break;
                }
                case "indirectMagic": {
                    damage = damage * (float) (SourceStats.CombatStats.get("Intelligence") / 100.0 + 1)
                        * (float) (SourceStats.CombatStats.get("BaseDamageMultipiler") / 100.0 + 1);
                    damage *= (float) (SourceStats.CombatStats.get("MagicDamageMultipiler") / 100.0 + 1);
                    break;
                }
                case "player": {
                    damage = damage * (float) (SourceStats.CombatStats.get("Strength") / 100.0 + 1)
                        * (float) (SourceStats.CombatStats.get("BaseDamageMultipiler") / 100.0 + 1);
                    damage *= (float) (SourceStats.CombatStats.get("MeleeDamageMultipiler") / 100.0 + 1);
                    break;
                }
            }
            if (new Random().nextInt(100) < SourceStats.CombatStats.get("CritChance")) {
                damage *= (float) (SourceStats.CombatStats.get("CritDamage") / 100.0 + 1);
            }
            // debug info
            if (((EntityPlayer) event.source.getEntity()).getHeldItem() != null) GT_Utility.sendChatToPlayer(
                (EntityPlayer) event.source.getEntity(),
                event.source.damageType + " by "
                    + ((EntityPlayer) event.source.getEntity()).getHeldItem()
                        .getUnlocalizedName());
        }
        if (event.entityLiving instanceof EntityPlayer && event.source.damageType != "outOfWorld"
            && event.source.damageType != "generic") {
            ArmorEventHandler.INSTANCE.updatePlayerStats((EntityPlayer) event.entityLiving);
            damage *= (float) (1
                - PlayerExtendedProperties.getStat((EntityPlayer) event.entityLiving, "Resistance") / 100.0);
        }
        event.ammount = damage * (1F + (new Random().nextFloat() - 0.5F) * 0.1F);
    }
}
