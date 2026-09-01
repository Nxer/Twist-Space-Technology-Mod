package com.Nxer.TwistSpaceTechnology.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.network.TST_Network;
import com.Nxer.TwistSpaceTechnology.network.packet.ClipboardPacket;
import com.Nxer.TwistSpaceTechnology.network.packet.ContainerDumpRequestPacket;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_DataCell;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Galaxy;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Planet;
import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.IDSP_IO;
import com.Nxer.TwistSpaceTechnology.util.TstSharedLocalization;

import cpw.mods.fml.common.registry.GameRegistry;

public class TST_CommandMethods implements IDSP_IO {

    public static final TST_CommandMethods INSTANCE = new TST_CommandMethods();

    public void dsp_info(ICommandSender sender) {
        int dim = sender.getEntityWorld().provider.dimensionId;
        DSP_Galaxy galaxy = DSP_Galaxy.getGalaxyFromDimID(dim);
        DSP_Planet planet = DSP_Planet.getPlanetFromDimID(dim);
        sender.addChatMessage(
            new ChatComponentText(
                "Current Galaxy: " + EnumChatFormatting.GOLD
                    + galaxy
                    + EnumChatFormatting.RESET
                    + " , Stellar Coefficient : "
                    + EnumChatFormatting.GREEN
                    + galaxy.stellarCoefficient));
        sender.addChatMessage(
            new ChatComponentText(
                "Current Planet: " + EnumChatFormatting.GOLD
                    + planet
                    + EnumChatFormatting.RESET
                    + " , Planetary Coefficient : "
                    + EnumChatFormatting.GREEN
                    + planet.planetaryCoefficient));
    }

    /**
     * Usage: /tst get_container_items <source|getModItem>
     *
     * Reads the chest under the crosshair in slot order. Source mode emits the best form per item:
     * material form (ore dict with a material prefix, resolved through GT/GT++/BartWorks), then mod class
     * form (ItemList, GTCMItemList, MaterialsTST, TstBlocks, ...), then ModItem.getModItem.
     * getModItem mode always emits ModItem.getModItem. GT fluid display stacks use FluidName,amount.
     * Chat shows at most 32 entries while the clipboard always receives the complete list.
     */
    public void dumpContainer(ICommandSender sender) {
        dumpContainer(sender, ContainerDumpMode.GET_MOD_ITEM);
    }

    public void dumpContainer(ICommandSender sender, ContainerDumpMode mode) {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.addChatMessage(new ChatComponentText("This command can only be used by a player."));
            return;
        }

        TST_Network.tst.sendTo(new ContainerDumpRequestPacket(mode), (EntityPlayerMP) sender);
    }

    public void dumpContainer(EntityPlayerMP player, int x, int y, int z) {
        dumpContainer(player, x, y, z, ContainerDumpMode.GET_MOD_ITEM);
    }

    public void dumpContainer(EntityPlayerMP player, int x, int y, int z, ContainerDumpMode mode) {
        if (y < 0 || player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) > 64.0D) {
            player.addChatMessage(new ChatComponentText("Point at a chest first."));
            return;
        }

        IInventory inventory = getInventory(player.worldObj.getTileEntity(x, y, z));
        if (inventory == null) {
            player.addChatMessage(new ChatComponentText("The targeted chest has no readable inventory."));
            return;
        }

        List<String> lines = new ArrayList<>();
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack == null || stack.getItem() == null) continue;

            String formatted = formatStack(stack, mode);
            if (formatted == null) continue;
            lines.add(formatted);
        }

        int visibleLines = Math.min(32, lines.size());
        for (int line = 0; line < visibleLines; line++) {
            player.addChatMessage(new ChatComponentText(lines.get(line)));
        }
        if (lines.size() > visibleLines) player.addChatMessage(new ChatComponentText("..."));
        if (lines.isEmpty()) player.addChatMessage(new ChatComponentText("The chest is empty."));

        if (mode == ContainerDumpMode.SOURCE) {
            int sourceCount = 0;
            for (String line : lines) {
                if (!line.startsWith("ModItem.getModItem(")) sourceCount++;
            }
            player.addChatMessage(
                new ChatComponentText(
                    "Source mode: " + sourceCount
                        + "/"
                        + lines.size()
                        + " items resolved to source forms; the rest fell back to getModItem."));
        }

        TST_Network.tst.sendTo(new ClipboardPacket(String.join("\n", lines)), player);
    }

    private static IInventory getInventory(TileEntity tileEntity) {
        if (!(tileEntity instanceof IInventory)) return null;

        if (tileEntity instanceof TileEntityChest) return getVanillaChestInventory((TileEntityChest) tileEntity);
        return (IInventory) tileEntity;
    }

    private static IInventory getVanillaChestInventory(TileEntityChest chest) {
        IInventory inventory = chest;
        int x = chest.xCoord;
        int y = chest.yCoord;
        int z = chest.zCoord;

        // Match vanilla double-chest ordering so the west/north half always precedes east/south.
        if (chest.getWorldObj()
            .getBlock(x - 1, y, z) == chest.getBlockType()) {
            inventory = new InventoryLargeChest(
                "container.chestDouble",
                (TileEntityChest) chest.getWorldObj()
                    .getTileEntity(x - 1, y, z),
                inventory);
        } else if (chest.getWorldObj()
            .getBlock(x + 1, y, z) == chest.getBlockType()) {
                inventory = new InventoryLargeChest(
                    "container.chestDouble",
                    inventory,
                    (TileEntityChest) chest.getWorldObj()
                        .getTileEntity(x + 1, y, z));
            }

        if (chest.getWorldObj()
            .getBlock(x, y, z - 1) == chest.getBlockType()) {
            inventory = new InventoryLargeChest(
                "container.chestDouble",
                (TileEntityChest) chest.getWorldObj()
                    .getTileEntity(x, y, z - 1),
                inventory);
        } else if (chest.getWorldObj()
            .getBlock(x, y, z + 1) == chest.getBlockType()) {
                inventory = new InventoryLargeChest(
                    "container.chestDouble",
                    inventory,
                    (TileEntityChest) chest.getWorldObj()
                        .getTileEntity(x, y, z + 1));
            }
        return inventory;
    }

    private static String formatStack(ItemStack stack, ContainerDumpMode mode) {
        FluidStack fluidStack = getFluidDisplayStack(stack);
        if (fluidStack != null) {
            if (mode == ContainerDumpMode.SOURCE) {
                // Fluids: class form first, then the fluid-name form.
                String fluidSource = ContainerItemSourceResolver.resolveFluid(fluidStack);
                if (fluidSource != null) return fluidSource;
            }
            return fluidStack.getFluid()
                .getName() + ","
                + fluidStack.amount;
        }

        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (identifier == null) return null;
        if (mode == ContainerDumpMode.SOURCE) {
            String source = ContainerItemSourceResolver.resolve(stack);
            if (source != null) return appendNbt(source, stack);
        }
        // TST's own getModItem wrapper has a fallback item and works better than the raw GT call.
        String output = "ModItem.getModItem(\"" + escape(identifier.modId)
            + "\", \""
            + escape(identifier.name)
            + "\", "
            + stack.stackSize
            + ", "
            + stack.getItemDamage()
            + ")";
        return appendNbt(output, stack);
    }

    private static String appendNbt(String output, ItemStack stack) {
        return stack.getTagCompound() == null ? output : output + " /* NBT: " + stack.getTagCompound() + " */";
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }

    private static FluidStack getFluidDisplayStack(ItemStack stack) {
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (identifier == null || !"gregtech".equals(identifier.modId)
            || !"gt.GregTech_FluidDisplay".equals(identifier.name)) return null;

        Fluid fluid = FluidRegistry.getFluid(stack.getItemDamage());
        if (fluid == null || stack.getTagCompound() == null) return null;

        long amountPerItem = stack.getTagCompound()
            .getLong("mFluidDisplayAmount");
        if (amountPerItem < 0L || stack.stackSize <= 0 || amountPerItem > Integer.MAX_VALUE / (long) stack.stackSize)
            return null;

        // Heat and state are display metadata; stacked displays multiply only the fluid amount.
        return new FluidStack(fluid, (int) (amountPerItem * stack.stackSize));
    }

    public void dsp_setNode(ICommandSender sender, String amount, String dim, String aName) {
        if (amount == null) {
            help_dsp_setSolarSail(sender);
            return;
        }

        long nodeAmount;

        try {
            nodeAmount = Long.parseLong(amount);
        } catch (NumberFormatException e) {
            sendFormatError(sender);
            return;
        }

        String userName = "defaultPlayerWithErrorInformation";
        int dimID = -114;

        if (dim == null) {
            dimID = sender.getEntityWorld().provider.dimensionId;
        } else {
            try {
                dimID = Integer.parseInt(dim);
            } catch (NumberFormatException e) {
                sendFormatError(sender);
                return;
            }
        }

        if (aName == null) {
            userName = sender.getCommandSenderName();
        } else {
            userName = aName;
        }

        DSP_DataCell dataCell = getOrInitDSPData(userName, dimID);
        dataCell.setDSPNode(nodeAmount);
        sender.addChatMessage(
            new ChatComponentText(
                EnumChatFormatting.GOLD + "Succeed to set Node amount to "
                    + EnumChatFormatting.GREEN
                    + nodeAmount
                    + EnumChatFormatting.GOLD
                    + " , team "
                    + EnumChatFormatting.RESET
                    + dataCell.getOwnerName()
                    + EnumChatFormatting.GOLD
                    + " in Galaxy "
                    + EnumChatFormatting.RESET
                    + dataCell.getGalaxy()));
        sender.addChatMessage(
            new ChatComponentText(EnumChatFormatting.BLUE + "-----------------------------------------------"));
    }

    public void help_dsp_setSolarSail(ICommandSender sender) {
        sender.addChatMessage(
            new ChatComponentText(
                "↓ Use this to set Dyson Sphere Node " + EnumChatFormatting.GREEN
                    + "amount"
                    + EnumChatFormatting.RESET
                    + " of you or your "
                    + EnumChatFormatting.AQUA
                    + "team"
                    + EnumChatFormatting.RESET
                    + " in current galaxy or in "
                    + EnumChatFormatting.AQUA
                    + " dimension's galaxy "
                    + EnumChatFormatting.RESET
                    + "↓"));
        sender.addChatMessage(
            new ChatComponentText(
                "/tst_admin dsp_setNode " + EnumChatFormatting.GREEN
                    + "amount "
                    + EnumChatFormatting.AQUA
                    + "<dimID> <team name>"));
    }

    public void sendDSPInfo(ICommandSender sender) {

    }

    public void sendFormatError(ICommandSender sender) {
        sender.addChatMessage(TstSharedLocalization.Command.formatError());
    }

    public void printHelp(ICommandSender sender) {
        // #tr TST_Command.printHelp.00
        // # {\GOLD} --- Twist Space Technology Mod : Dyson Sphere System Controller ---
        // #zh_CN {\GOLD} --- Twist Space Technology Mod : 戴森球系统控制 ---
        sender.addChatMessage(new ChatComponentTranslation("TST_Command.printHelp.00"));
        sender.addChatMessage(
            new ChatComponentText(
                "↓ Use this to join " + EnumChatFormatting.AQUA
                    + "User1"
                    + EnumChatFormatting.RESET
                    + " to "
                    + EnumChatFormatting.AQUA
                    + "User2"
                    + EnumChatFormatting.RESET
                    + " team ↓"));
        sender.addChatMessage(
            new ChatComponentText(
                "/tst dsp_join " + EnumChatFormatting.AQUA + "User1 " + EnumChatFormatting.AQUA + "User2"));
        sender.addChatMessage(
            new ChatComponentText(
                "↓ Use this to check " + EnumChatFormatting.AQUA
                    + "User1"
                    + EnumChatFormatting.RESET
                    + " Dyson Sphere Program Information. ↓"));
        sender.addChatMessage(new ChatComponentText("/tst dsp_check " + EnumChatFormatting.AQUA + "User1"));
        sender.addChatMessage(
            new ChatComponentText(
                "↓ Use this to set Dyson Sphere Solar Sail " + EnumChatFormatting.GREEN
                    + "amount"
                    + EnumChatFormatting.RESET
                    + " of you or your "
                    + EnumChatFormatting.AQUA
                    + "team"
                    + EnumChatFormatting.RESET
                    + " in current galaxy or in "
                    + EnumChatFormatting.AQUA
                    + " dimension's galaxy "
                    + EnumChatFormatting.RESET
                    + "↓"));
        sender.addChatMessage(
            new ChatComponentText(
                "/tst_admin dsp_setSolarSail " + EnumChatFormatting.GREEN
                    + "amount "
                    + EnumChatFormatting.AQUA
                    + "<dimID> <team name>"));
        sender.addChatMessage(new ChatComponentText("/tst get_container_items <source|getModItem>"));

        help_dsp_setSolarSail(sender);
        sender.addChatMessage(
            new ChatComponentText(
                "↓ [AE&TC] Toggle AE2 pattern conversion on encode: glass ampoules / crystal essences → aspects. ↓"));
        sender.addChatMessage(
            new ChatComponentText(
                "/tst ae_pattern_conversion " + EnumChatFormatting.GREEN
                    + "<status>"
                    + EnumChatFormatting.RESET
                    + "  or  "
                    + EnumChatFormatting.AQUA
                    + "<ampoule|crystal> "
                    + EnumChatFormatting.GREEN
                    + "<on|off>"));
        sender.addChatMessage(
            new ChatComponentText(EnumChatFormatting.BLUE + "-----------------------------------------------"));
    }

}
