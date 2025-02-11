package com.Nxer.TwistSpaceTechnology.common.item;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.common.init.TstBlocks;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.GregTechAPI;
import gregtech.api.damagesources.GTDamageSources;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import ic2.api.item.IElectricItem;
import ic2.core.init.InternalName;
import ic2.core.item.armor.ItemArmorElectric;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@EventBusSubscriber
public class ItemCardigan extends ItemArmorElectric implements IElectricItem {

    // spotless:off
    // @formatter:off
    // tier [ULV, LV, MV, HV]
    private static final double[] TIERED_MAX_CHARGE =     {60_000, 600_000, 2_000_000, 20_000_000};
    private static final double[] TIERED_TRANSFER_LIMIT = {   100,   1_000,     1_000,      5_000};
    // @formatter:on
    // spotless:on

    private static final int CHARGE_BASE = 1;

    private static final int MULTIPLIER_SPRINTING = 20;
    private static final int MULTIPLIER_RUBBING_CATS = 40;
    private static final int MULTIPLIER_RUBBING_SHEEP = 30;
    private static final int MULTIPLIER_RUBBING_WOOLS = 25;

    // don't set to final to change in debug
    @SuppressWarnings("FieldMayBeFinal")
    private static boolean CARDIGAN_DEBUG_MODE = Boolean.getBoolean("cardigan_debug_mode");

    /**
     * The {@link InternalName} instance for Cardigans, created by the mixin ({@link com.Nxer.TwistSpaceTechnology.mixin.IC2_InternalName_Adder_Mixin}).
     */
    public static final InternalName Cardigan = Objects.requireNonNull(InternalName.valueOf("Cardigan"), "Failed to get InternalName instance for Cardigan!");
    
    public static ItemStack CardiganULV, CardiganLV, CardiganMV, CardiganHV, CardiganHV_Charged;

    public ItemCardigan() {
        // this item will be registered in the super class constructor, so don't register twice!
        super(Cardigan, Cardigan, 1, TIERED_MAX_CHARGE[0], TIERED_TRANSFER_LIMIT[0], 0);

        // #tr item.chestplateCardigan.name
        // # Cardigan
        // #zh_CN 羊毛衫

        // #tr item.chestplateCardigan.0.name
        // # Cardigan
        // #zh_CN 羊毛衫

        // #tr item.chestplateCardigan.1.name
        // # Advanced Cardigan
        // #zh_CN 高级羊毛衫

        // #tr item.chestplateCardigan.2.name
        // # Flawless Cardigan
        // #zh_CN 无暇羊毛衫

        // #tr item.chestplateCardigan.3.name
        // # Exquisite Cardigan
        // #zh_CN 精致羊毛衫
        setUnlocalizedName("chestplateCardigan");

        setCreativeTab(TstCreativeTabs.TabGeneral);
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 0.0;
    }

    @Override
    public int getEnergyPerDamage() {
        return 0;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        int tier = tag.getInteger("cardiganTier");
        return tier >= 0 && tier < TIERED_MAX_CHARGE.length ? tier : 0;
    }

    public void setTier(ItemStack itemStack, int tier) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();
        tag.setInteger("cardiganTier", tier);
        itemStack.setTagCompound(tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List itemList) {
        // normal ones
        CardiganULV = new ItemStack(this, 1, getMaxDamage());
        setTier(CardiganULV, 0);
        itemList.add(CardiganULV);

        CardiganLV = new ItemStack(this, 1, getMaxDamage());
        setTier(CardiganLV, 1);
        itemList.add(CardiganLV);

        CardiganMV = new ItemStack(this, 1, getMaxDamage());
        setTier(CardiganMV, 2);
        itemList.add(CardiganMV);

        CardiganHV = new ItemStack(this, 1, getMaxDamage());
        setTier(CardiganHV, 3);
        itemList.add(CardiganHV);

        // fully charged one
        CardiganHV_Charged = new ItemStack(this, 1, getMaxDamage());
        setTier(CardiganHV_Charged, 3);
        GTModHandler.chargeElectricItem(CardiganHV_Charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        itemList.add(CardiganHV_Charged);
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return TIERED_MAX_CHARGE[getTier(itemStack)];
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return TIERED_TRANSFER_LIMIT[getTier(itemStack)];
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
        //noinspection unchecked
        this.addInformationTypeChecked(itemStack, (List<String>) info);
    }

    private void addInformationTypeChecked(ItemStack itemStack, List<String> tooltips) {
        int tier = getTier(itemStack);
        tooltips.add(EnumChatFormatting.GRAY + "Tier: " + GTUtility.getColoredTierNameFromTier((byte) tier));

        Optional<Long[]> chargeOptional = GTModHandler.getElectricItemCharge(itemStack);
        if (chargeOptional.isPresent()) {
            var current = chargeOptional.get()[0];
            var max = chargeOptional.get()[1];

            // #tr tst.cardigan.tooltip.power
            // # {\GRAY}Charged: {\GREEN}%s{\GRAY}/{\GREEN}%s
            // #zh_CN {\GRAY}充能: {\GREEN}%s{\GRAY}/{\GREEN}%s
            String s = StatCollector.translateToLocalFormatted("tst.cardigan.tooltip.power",
                GTUtility.formatNumbers(current), GTUtility.formatNumbers(max));
            tooltips.add(s);
        } else {
            // #tr tst.cardigan.tooltip.error
            // # {\RED}Something is missing!
            // #zh_CN {\RED}不对劲！
            tooltips.add(StatCollector.translateToLocal("tst.cardigan.tooltip.error"));
        }

        if (tier == VoltageIndex.ULV) {
            // #tr tst.cardigan.tooltip.ulv
            // # {\RED}Useless Bullshit!
            // #zh_CN {\RED}没用的废物！
            tooltips.add(StatCollector.translateToLocal("tst.cardigan.tooltip.ulv"));
        }
    }

    @Override // to override IC2 special logic
    public String getUnlocalizedName() {
        return "item." + unlocalizedName;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + unlocalizedName + "." + getTier(itemStack);
    }

    @Override // to override IC2 special logic
    public String getItemStackDisplayName(ItemStack itemStack) {
        return StatCollector.translateToLocal(getUnlocalizedName(itemStack) + ".name").trim();
    }

    @Override // to override IC2 special logic
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(TwistSpaceTechnology.RESOURCE_ROOT_ID + ":Cardigan");
    }

    @Override // to override IC2 special logic
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return TwistSpaceTechnology.RESOURCE_ROOT_ID + ":textures/armor/Cardigan_1.png";
    }

    public static boolean isCardigan(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof ItemCardigan;
    }

    public static final Map<UUID, Integer> CHARGE_COOLDOWN_MAP = new HashMap<>();
    public static final int CHARGE_COOLDOWN_TICK = 10 * 20;

    private static boolean checkCooldown(EntityPlayer player) {
        int tickNow = MinecraftServer.getServer().getTickCounter();
        return !CHARGE_COOLDOWN_MAP.containsKey(player.getUniqueID()) || tickNow > CHARGE_COOLDOWN_MAP.get(player.getUniqueID());
    }

    /**
     * Charge the cardigan if it has cooled down, and reset the cooldown timer.
     *
     * @param player        the player holding the cardigan
     * @param cardiganStack the cardigan item
     * @param value         the eu to charge
     */
    private static void tryCharge(EntityPlayer player, ItemStack cardiganStack, int value) {
        int tickNow = MinecraftServer.getServer().getTickCounter();
        int charged = GTModHandler.chargeElectricItem(cardiganStack, value, Integer.MAX_VALUE, true, false);
        if (charged > 0) {
            CHARGE_COOLDOWN_MAP.put(player.getUniqueID(), tickNow + CHARGE_COOLDOWN_TICK);
            TwistSpaceTechnology.LOG.info("Charged: {}", charged);
        }
    }

    private static int discharge(ItemStack cardiganStack, int value) {
        return GTModHandler.dischargeElectricItem(cardiganStack, value, Integer.MAX_VALUE, false, false, true);
    }

    private static void applyElectricDamage(Entity target, float damage) {
        target.attackEntityFrom(GTDamageSources.getElectricDamage(), damage);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.worldObj.isRemote) return;

        EntityPlayer player = event.player;

        // check charge cooldown
        if (checkCooldown(player)) {
            for (ItemStack armorStack : player.inventory.armorInventory) {
                if (armorStack == null) continue;

                if (isCardigan(armorStack)) {
                    if (player.isSprinting()) {
                        tryCharge(player, armorStack, CHARGE_BASE * MULTIPLIER_SPRINTING);
                    }
                    return; // only works for 1 cardigan, if the player somehow put 2 or more on his armor inventory.
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRubbingEntity(EntityInteractEvent event) {
        if (event.entityPlayer.worldObj.isRemote) return;

        EntityPlayer player = event.entityPlayer;

        for (ItemStack armorStack : player.inventory.armorInventory) {
            if (armorStack == null) continue;

            if (isCardigan(armorStack)) {
                Entity target = event.target;

                // charging by interacting with entities
                if (checkCooldown(player)) {
                    if (target instanceof EntityOcelot) {
                        tryCharge(player, armorStack, CHARGE_BASE * MULTIPLIER_RUBBING_CATS);
                    } else if (target instanceof EntitySheep) {
                        tryCharge(player, armorStack, CHARGE_BASE * MULTIPLIER_RUBBING_SHEEP);
                    }
                }

                // shocking other players
                if (target instanceof EntityPlayer targetPlayer) {
                    discharge(armorStack, 10);
                    applyElectricDamage(player, Math.min(player.getHealth() - 1, 6));
                    applyElectricDamage(targetPlayer, 4);
                }

                return;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRubbingBlock(PlayerInteractEvent event) {
        if (event.world.isRemote) return;

        // must be rightclick
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

        EntityPlayer player = event.entityPlayer;

        for (ItemStack armorStack : player.inventory.armorInventory) {
            if (armorStack == null) continue;

            if (isCardigan(armorStack)) {
                World world = event.world;
                Block target = world.getBlock(event.x, event.y, event.z);

                // discharging by interacting with stone
                if (target == Blocks.stone) {
                    int discharge = discharge(armorStack, 1000);

                    if (discharge > 0) {
                        // have 10% chance to shock the wearer
                        if (CARDIGAN_DEBUG_MODE || world.rand.nextInt(10) > 8) {
                            applyElectricDamage(player, Math.min(player.getHealth() - 1, 4));
                            // #tr tst.cardigan.ouch
                            // # Ouch!
                            // #zh_CN 啊！
                            player.addChatComponentMessage(new ChatComponentTranslation("tst.cardigan.ouch"));
                        }
                    }
                }

                // charging unlimited by interacting with the power chair
                if (target == TstBlocks.BlockPowerChair) {
                    tryCharge(player, armorStack, Integer.MAX_VALUE);

                    // #tr tst.cardigan.powah
                    // # Oh, I feel power going through my whole body!
                    // #zh_CN 哇，我感觉能量流经我的身体！
                    player.addChatComponentMessage(new ChatComponentTranslation("tst.cardigan.powah"));
                }

                // harming the machines
                if (target == GregTechAPI.sBlockMachines) {
                    TileEntity te = world.getTileEntity(event.x, event.y, event.z);
                    if (te != null) {
                        if (te instanceof BaseMetaTileEntity bmte) {
                            IMetaTileEntity mte = bmte.getMetaTileEntity();

                            // explode single machines
                            if (mte instanceof MTEBasicMachine basicMachine) {
                                int tier = TstItems.Cardian.getTier(armorStack);
                                int targetTier = basicMachine.mTier;

                                if (tier > targetTier) {
                                    // have 1% chance to cause the machine to explode!
                                    if (CARDIGAN_DEBUG_MODE || world.rand.nextInt(100) == 99) {
                                        bmte.doExplosion(GTValues.V[VoltageIndex.LV]);
                                    }
                                }
                            }

                            // explode multiblock machines
                            if (mte instanceof MTEMultiBlockBase multiBlockBase) {
                                // have 1% chance to cause maintanence issues, regardless the voltage tier
                                if (CARDIGAN_DEBUG_MODE || world.rand.nextInt(100) == 99) {
                                    multiBlockBase.causeMaintenanceIssue();

                                    // #tr tst.cardigan.damageMachine
                                    // # {\GRAY}{\ITALIC}The machine is making strange noises
                                    // #zh_CN {\GRAY}{\ITALIC}机器正在发出奇怪的声音
                                    player.addChatComponentMessage(new ChatComponentTranslation("tst.cardigan.damageMachine"));
                                }
                            }
                        }
                    }
                }

                // charging by interacting with wools
                if (checkCooldown(player)) {
                    if (target == Blocks.wool) {
                        tryCharge(player, armorStack, CHARGE_BASE * MULTIPLIER_RUBBING_WOOLS);
                    }
                }

                return;
            }
        }
    }

}
