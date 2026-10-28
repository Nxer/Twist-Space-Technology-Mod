package com.Nxer.TwistSpaceTechnology.common.item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineDescriptions;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Rule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureCatalog.Structure;

/** One physical item owns the paid structure assets; machine quantity never uses ItemStack.stackSize. */
public final class ItemBottledChaosMachineModule extends AbstractTstMetaItem {

    private static final String ASSEMBLY = "bottledChaosAssembly";
    private static final String PARAMETER_SEPARATOR = EnumChatFormatting.GRAY.toString()
        + EnumChatFormatting.STRIKETHROUGH
        + "-----------------------------------------"
        + EnumChatFormatting.RESET;

    private IIcon packedIcon;

    // #tr tst.bottledchaos.machine_module.0.name
    // # Bottled Chaos Machine Structure Module
    // #zh_CN 瓶中混沌机器结构模组
    public ItemBottledChaosMachineModule() {
        super("BottledChaosMachineModule", "bottled_chaos.machine_module");
        setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return StatCollector.translateToLocal("tst.bottledchaos.machine_module.0.name");
    }

    public static boolean isModule(ItemStack stack) {
        return stack != null && stack.getItem() == TstItems.BottledChaosMachineModule
            && stack.getItemDamage() == 0
            && stack.stackSize == 1;
    }

    private static NBTTagCompound assembly(ItemStack stack) {
        return isModule(stack) && stack.hasTagCompound() ? stack.getTagCompound()
            .getCompoundTag(ASSEMBLY) : new NBTTagCompound();
    }

    public static String machineKey(ItemStack stack) {
        return assembly(stack).getString("machine");
    }

    public static int profile(ItemStack stack) {
        return assembly(stack).getInteger("profile");
    }

    /** Decode the stored controller for tooltips and recipe-query identity checks. */
    public static ItemStack controller(ItemStack stack) {
        return ItemStack.loadItemStackFromNBT(assembly(stack).getCompoundTag("controller"));
    }

    public static boolean isDisplay(ItemStack stack) {
        return assembly(stack).getBoolean("display");
    }

    /** AE patterns describe the real output. Only the copy loses its NEI marker; no physical asset is created. */
    public static ItemStack copyForPattern(ItemStack stack) {
        ItemStack copy = stack.copy();
        if (isModule(copy)) assembly(copy).removeTag("display");
        return copy;
    }

    public static long quantity(ItemStack stack) {
        byte[] bytes = assembly(stack).getByteArray("quantity");
        long quantity = 0;
        for (byte value : bytes) {
            int unsigned = value & 0xff;
            if (quantity > (Long.MAX_VALUE - unsigned) / 256) return -1;
            quantity = quantity * 256 + unsigned;
        }
        return quantity;
    }

    public static int quantitySpeedExponent(long quantity) {
        return quantity <= 0 ? 0 : (63 - Long.numberOfLeadingZeros(quantity)) / 3;
    }

    public static int quantitySpeed(long quantity) {
        return 1 + quantitySpeedExponent(quantity);
    }

    public static Structure structure(ItemStack stack) {
        NBTTagCompound data = assembly(stack);
        return data.getBoolean("display") ? null
            : BottledChaosStructureCatalog
                .resolve(data.getString("machine"), data.getInteger("profile"), materials(stack));
    }

    /** NEI's alternatives have no single fixed temperature and are never executable assets. */
    public static ItemStack createDisplay(Rule rule) {
        ItemStack stack = new ItemStack(TstItems.BottledChaosMachineModule);
        NBTTagCompound data = new NBTTagCompound();
        data.setBoolean("display", true);
        data.setString("machine", rule.machineKey());
        data.setInteger("profile", rule.profile());
        data.setTag(
            "controller",
            rule.controller()
                .writeToNBT(new NBTTagCompound()));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag(ASSEMBLY, data);
        stack.setTagCompound(tag);
        return stack;
    }

    /** A grouped NEI frame carries exact materials for its tooltip, but is not a paid executable asset. */
    public static ItemStack createDisplay(Structure structure) {
        ItemStack stack = create(structure, 1);
        assembly(stack).setBoolean("display", true);
        return stack;
    }

    public static ItemStack create(Structure structure, long quantity) {
        ItemStack stack = new ItemStack(TstItems.BottledChaosMachineModule);
        if (quantity <= 0) return stack;
        NBTTagCompound data = new NBTTagCompound();
        data.setString("machine", structure.machineKey());
        data.setInteger("profile", structure.profile());
        data.setLong("heat", structure.heat());
        // Keep the existing unsigned byte representation; all arithmetic and validation now use long.
        byte[] bytes = new byte[Long.BYTES];
        for (int i = bytes.length - 1; i >= 0; i--) {
            bytes[i] = (byte) quantity;
            quantity >>>= 8;
        }
        data.setByteArray("quantity", bytes);
        data.setTag(
            "controller",
            structure.controller()
                .writeToNBT(new NBTTagCompound()));
        data.setTag("materials", structure.materialTag());
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag(ASSEMBLY, data);
        stack.setTagCompound(nbt);
        return stack;
    }

    /** An asset snapshot permits exact unloading even when a later version changes a structure definition. */
    public static ItemStack[] materials(ItemStack stack) {
        NBTTagList list = assembly(stack).getTagList("materials", 10);
        ItemStack[] materials = new ItemStack[list.tagCount()];
        for (int i = 0; i < materials.length; i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            materials[i] = ItemStack.loadItemStackFromNBT(tag);
            if (materials[i] == null || tag.getInteger("amount") <= 0) return new ItemStack[0];
            materials[i].stackSize = tag.getInteger("amount");
        }
        return materials;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        String path = TwistSpaceTechnology.RESOURCE_ROOT_ID + ":BottledChaosMachineModule/";
        itemIcon = register.registerIcon(path + "structure_module_blank");
        packedIcon = register.registerIcon(path + "structure_module_packed");
        iconMap.put(0, itemIcon);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        return quantity(stack) > 0 || isDisplay(stack) ? packedIcon : itemIcon;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconIndex(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean advanced) {
        long quantity = quantity(stack);
        NBTTagCompound data = assembly(stack);
        ItemStack controller = controller(stack);
        if (controller != null) lines.add(EnumChatFormatting.GOLD + controller.getDisplayName());
        boolean display = data.getBoolean("display");
        Structure structure = structure(stack);
        long poolMask = structure == null ? 0 : structure.poolMask();
        if (display) {
            if (data.hasKey("materials")) {
                structure = BottledChaosStructureCatalog
                    .resolve(data.getString("machine"), data.getInteger("profile"), materials(stack));
                if (structure != null) poolMask = structure.poolMask();
            }
            if (structure == null) for (Rule rule : BottledChaosStructureCatalog.displayRules()) if (rule.machineKey()
                .equals(data.getString("machine")) && rule.profile() == data.getInteger("profile")) {
                    structure = rule.example();
                    poolMask = rule.poolMask();
                    break;
                }
        }
        if (!display && quantity <= 0) {
            // #tr tst.bottledchaos.machine_module.empty
            // # Place an empty module in the carrier slot to package a structure.
            // #zh_CN 空模组放入载体槽后可封装结构.
            lines.add(StatCollector.translateToLocal("tst.bottledchaos.machine_module.empty"));
            return;
        }
        if (!display) {
            // #tr tst.bottledchaos.machine_module.unload
            // # Unload with an ME item disk in the controller workbench. Cannot add or merge structures.
            // #zh_CN 放入载体槽, 材料槽放ME物品磁盘即可卸载; 不支持追加或合并结构.
            lines.add(StatCollector.translateToLocal("tst.bottledchaos.machine_module.unload"));
        }
        if (BottledChaosStructureCatalog.isAuxiliaryController(controller)) {
            lines.add(PARAMETER_SEPARATOR);
            // #tr tst.bottledchaos.machine_module.plant_capacity
            // # Each purification unit's quantity is limited by this plant; no processing parameters
            // #zh_CN 限制每台净化水机器的堆叠组数, 不参与配方折算
            if (display) lines.add(StatCollector.translateToLocal("tst.bottledchaos.machine_module.plant_capacity"));
            else {
                // #tr tst.bottledchaos.machine_module.plant_quantity
                // # Structure quantity limit per purification unit: %s
                // #zh_CN 每台净化水机器的结构数量上限: %s
                lines.add(
                    EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                        "tst.bottledchaos.machine_module.plant_quantity",
                        EnumChatFormatting.GOLD.toString() + quantity));
            }
            return;
        }
        // Merge only complete matching parameter blocks, retaining the configured pool order within each group.
        Map<List<String>, StringJoiner> parameterGroups = new LinkedHashMap<>();
        for (int slot = 0; slot < BottledChaosMachineConfiguration.poolCount(); slot++) {
            if ((poolMask & (1L << slot)) == 0) continue;
            parameterGroups
                .computeIfAbsent(
                    BottledChaosMachineDescriptions.describe(
                        controller,
                        structure,
                        BottledChaosMachineConfiguration.pool(slot),
                        quantitySpeedExponent(quantity),
                        !display),
                    parameters -> new StringJoiner(", "))
                .add(BottledChaosMachineConfiguration.poolName(slot));
        }
        for (var group : parameterGroups.entrySet()) {
            lines.add(PARAMETER_SEPARATOR);
            // #tr tst.bottledchaos.machine_module.pool
            // # Recipe pool: %s
            // #zh_CN 配方池: %s
            lines.add(
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.machine_module.pool",
                    EnumChatFormatting.WHITE.toString() + group.getValue()));
            lines.addAll(group.getKey());
        }
        if (display) return;
        // #tr tst.bottledchaos.machine_module.quantity
        // # Structure quantity: x%s
        // #zh_CN 结构数量: x%s
        lines.add(
            EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                "tst.bottledchaos.machine_module.quantity",
                EnumChatFormatting.GOLD.toString() + quantity));
    }
}
