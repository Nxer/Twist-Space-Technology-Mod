package com.GTNH_Community.gtnhcommunitymod.common.item.items;

import static com.GTNH_Community.gtnhcommunitymod.common.item.itemAdders.ItemAdder01.initItem01;
import static com.GTNH_Community.gtnhcommunitymod.common.item.items.BasicItems.MetaItem01;
import static com.GTNH_Community.gtnhcommunitymod.common.item.items.BasicItems.ProofOfHeroes;
import static com.GTNH_Community.gtnhcommunitymod.util.TextHandler.texter;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegister {

    public static void registryItems() {
        Item[] itemsToReg = { MetaItem01, ProofOfHeroes };

        for (Item item : itemsToReg) {
            GameRegistry.registerItem(item, item.getUnlocalizedName());
        }

    }

    // spotless:off
    public static void registryItemContainers() {
        GTCMItemList.TestItem0.set(initItem01("Test Item 0", 0, new String[] { texter("A test item, no use.", "tooltips.TestItem0.line1") }));
        GTCMItemList.SpaceWarper.set(initItem01("Space Warper", 1, new String[] {texter(EnumChatFormatting.DARK_BLUE + "Power of gravitation !", "tooltips.SpaceWarper.line1") }));
        GTCMItemList.OpticalSOC.set(initItem01("Gravitational Constraint Optical Quantum Crystal", 2, new String[]{ texter("These Photons have their own mind.","tooltips.OpticalSOC.line1")}));
        GTCMItemList.MoldSingularity.set(initItem01("Mold(Singularity)", 3, new String[]{ texter("Mold for making Singularity","tooltips.MoldSingularity.line1")}));
        GTCMItemList.ParticleTrapTimeSpaceShield.set(initItem01("Particle Trap - SpaceTime Shield", 4, new String[]{ texter("Constrain the operator(the photon) to a miniature spacetime.","tooltips.ParticleTrapTimeSpaceShield.line1")}));
        GTCMItemList.LapotronShard.set(initItem01("Lapotron Shard", 5, new String[]{ texter("Even though it's just a shard, the energy fluctuations inside are also visible to the naked eye.","tooltips.LapotronShard.line1")}));
        GTCMItemList.PerfectLapotronCrystal.set(initItem01("Perfect Lapotron Crystal", 6, new String[]{ texter("Immaculate !","tooltips.PerfectLapotronCrystal.line1")}));
        GTCMItemList.EnergyCrystalShard.set(initItem01("Energy Crystal Shard", 7, new String[]{ texter("A red crystal shard, doesn't look like anything special.","tooltips.EnergyCrystalShard.line1")}));
        GTCMItemList.PerfectEnergyCrystal.set(initItem01("Perfect Energy Crystal", 8, new String[]{ texter("As it grew in size, it displayed incredible traits on energy control.","tooltips.PerfectEnergyCrystal.line1")}));
        
        
        
        GTCMItemList.ProofOfHeroes.set(new ItemStack(ProofOfHeroes,1));
    }
// spotless:on
    public static void registry() {
        registryItems();
        registryItemContainers();
    }
}
