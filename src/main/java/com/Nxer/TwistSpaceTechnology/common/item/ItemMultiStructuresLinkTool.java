
package com.Nxer.TwistSpaceTechnology.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine.MultiStructureManager;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;

public class ItemMultiStructuresLinkTool extends Item {

    public int firstPosition = -1;
    public int secondPosition = -1;

    public ItemMultiStructuresLinkTool(CreativeTabs aCreativeTabs) {
        super();
        this.setCreativeTab(aCreativeTabs);
        // #tr item.MultiStructuresLinkTool.name
        // # Multi-Structures Link Tool
        // #zh_CN Multi-Structures Link Tool
        this.setUnlocalizedName("MultiStructuresLinkTool");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTabs, List aList) {
        aList.add(new ItemStack(aItem, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List<String> toolTip,
        final boolean advancedToolTips) {
        // #tr tooltips.MultiStructuresLinkTool.line1
        // # {\LIGHT_PURPLE}left Click to set Main machine
        toolTip.add(TextEnums.tr("tooltips.MultiStructuresLinkTool.line1"));
        // #tr tooltips.MultiStructuresLinkTool.line2
        // # {\LIGHT_PURPLE}right Click to set Sub machine
        toolTip.add(TextEnums.tr("tooltips.MultiStructuresLinkTool.line2"));
    }

    public void link(EntityPlayer aPlayer) {
        if (firstPosition >= 0 && secondPosition >= 0) {
            var main = MultiStructureManager.getMachine(firstPosition);
            var sub = MultiStructureManager.getMachine(secondPosition);
            if (MultiStructureManager.linkMachine(main, sub)) {
                GTUtility.sendChatToPlayer(
                    aPlayer,
                    "linked main structure:" + main.getLocalName() + " with sub structure:" + sub.getLocalName());
            } else {
                GTUtility.sendChatToPlayer(aPlayer, "the structure is incomplete please check your build");
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
            GTUtility.sendChatToPlayer(player, "cleared the settings");
        } else {
            var main = MultiStructureManager.getMachine(firstPosition);
            var sub = MultiStructureManager.getMachine(secondPosition);
            if (main != null) {
                GTUtility.sendChatToPlayer(player, "linked structure:" + main.getLocalName());
            }
            if (sub != null) {
                GTUtility.sendChatToPlayer(player, "linked structure:" + sub.getLocalName());
            }
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }

}
