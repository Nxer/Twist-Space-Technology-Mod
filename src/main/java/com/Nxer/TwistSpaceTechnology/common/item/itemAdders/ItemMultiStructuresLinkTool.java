
package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import static com.Nxer.TwistSpaceTechnology.util.TextHandler.texter;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.MultiStructureManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_Utility;

public class ItemMultiStructuresLinkTool extends Item {

    public int firstPosition = -1;
    public int secondPosition = -1;

    public ItemMultiStructuresLinkTool(String aName, String aMetaName, CreativeTabs aCreativeTabs) {
        super();
        this.setCreativeTab(aCreativeTabs);
        this.setUnlocalizedName(aMetaName);
        texter(aName, this.getUnlocalizedName() + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List aList) {
        aList.add(new ItemStack(aItem, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List toolTip,
        final boolean advancedToolTips) {
        toolTip.add(
            texter(
                EnumChatFormatting.LIGHT_PURPLE + "left Click to set Main machine",
                "tooltips.MultiStructuresLinkTool.line1"));
        toolTip.add(
            texter(
                EnumChatFormatting.LIGHT_PURPLE + "right Click to set Sub machine",
                "tooltips.MultiStructuresLinkTool.line2"));
    }

    public void link(EntityPlayer aPlayer) {
        if (firstPosition >= 0 && secondPosition >= 0) {
            var main = MultiStructureManager.getMachine(firstPosition);
            var sub = MultiStructureManager.getMachine(secondPosition);
            if (MultiStructureManager.linkMachine(main, sub)) {
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    "linked main structure:" + main.getLocalName() + " with sub structure:" + sub.getLocalName());
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, "the structure is incomplete please check your build");
            }
            firstPosition = -1;
            secondPosition = -1;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (player.isSneaking()) {
            firstPosition = -1;
            secondPosition = -1;
            GT_Utility.sendChatToPlayer(player, "cleared the settings");
        } else {
            var main = MultiStructureManager.getMachine(firstPosition);
            var sub = MultiStructureManager.getMachine(secondPosition);
            if (main != null) {
                GT_Utility.sendChatToPlayer(player, "linked structure:" + main.getLocalName());
            }
            if (sub != null) {
                GT_Utility.sendChatToPlayer(player, "linked structure:" + sub.getLocalName());
            }
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }

}
