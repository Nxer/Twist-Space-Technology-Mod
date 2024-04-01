package com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.util.CommonValues.MULTI_CHECK_AT;
import static com.github.technus.tectech.util.TT_Utility.getUniqueIdentifier;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.OpenComputers;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;

public class GT_Hatch_RackComputationMonitor extends GT_MetaTileEntity_Hatch
    implements IAddGregtechLogo, IAddUIWidgets {

    private static Textures.BlockIcons.CustomIcon EM_R;
    private static Textures.BlockIcons.CustomIcon EM_R_ACTIVE;
    public int heat = 0;
    private float overClock = 1, overVolt = 1;

    private boolean isMeanHatch = false;
    public static final Map<String, RackComponent> componentBinds = new HashMap<>();

    private String clientLocale = "en_US";

    public GT_Hatch_RackComputationMonitor(int aID, String aName, String aNameRegional, int aTier,
        boolean isMeanHatch) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            64,
            new String[] { TextLocalization.Mark_TwistSpaceTechnology_TecTech,
                translateToLocal("tst.computationhatchmonitor.desc1"),
                EnumChatFormatting.AQUA + translateToLocal("tst.computationhatchmonitor.desc2") });
        TT_Utility.setTier(aTier, this);
        this.isMeanHatch = isMeanHatch;
    }

    public GT_Hatch_RackComputationMonitor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        boolean isMeanHatch) {
        super(aName, aTier, 64, aDescription, aTextures);
        this.isMeanHatch = isMeanHatch;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (!isMeanHatch) return;
        aNBT.setInteger("eHeat", heat);
        aNBT.setFloat("eOverClock", overClock);
        aNBT.setFloat("eOverVolt", overVolt);
    }

    public boolean isMeanHatch() {
        return isMeanHatch;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!isMeanHatch) return;
        heat = aNBT.getInteger("eHeat");
        overClock = aNBT.getFloat("eOverClock");
        overVolt = aNBT.getFloat("eOverVolt");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        // GT_MetaTileEntity_Hatch_Rack.RackComponent
        EM_R_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/EM_RACK_ACTIVE");
        EM_R = new Textures.BlockIcons.CustomIcon("iconsets/EM_RACK");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(EM_R_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(EM_R) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_Hatch_RackComputationMonitor(mName, mTier, mDescriptionArray, mTextures, isMeanHatch);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return facing.offsetY == 0;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return isMeanHatch;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return isMeanHatch;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!isMeanHatch || aBaseMetaTileEntity.isActive() || heat > 500) {
            return false;
        }
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!isMeanHatch || aBaseMetaTileEntity.isActive() || heat > 500) {
            return false;
        }
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        if (!isMeanHatch) return true;
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
        // if(aBaseMetaTileEntity.isActive())
        // aPlayer.addChatComponentMessage(new ChatComponentText("It is still active..."));
        // else if(heat>0)
        // aPlayer.addChatComponentMessage(new ChatComponentText("It is still warm..."));
        // else
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    private int getComputationPower(float overclock, float overvolt, boolean tickingComponents) {
        if (!isMeanHatch) return 0;
        float computation = 0, heat = 0;
        for (int i = 0; i < mInventory.length; i++) {
            if (mInventory[i] == null) {
                continue;
            }
            RackComponent comp = componentBinds.get(getUniqueIdentifier(mInventory[i]));
            if (comp == null) {
                continue;
            }
            if (tickingComponents && comp.subZero || this.heat >= 0) {
                heat += (1f + comp.coEff * this.heat / 10000f) * mInventory[i].stackSize
                    * (comp.heat > 0 ? comp.heat * overclock * overclock * overvolt : comp.heat);
                // =MAX(0;MIN(MIN($B4;1*C$3+C$3-0,25);1+RAND()+(C$3-1)-($B4-1)/2))
                if (overvolt * 10f > 7f + TecTech.RANDOM.nextFloat()) {
                    computation += (float) (comp.computation * Math.max(
                        0,
                        Math.min(
                            Math.min(overclock, overvolt + overvolt - 0.25),
                            1 + TecTech.RANDOM.nextFloat() + (overvolt - 1) - (overclock - 1) / 2)))
                        * mInventory[i].stackSize;
                }

            } else {
                computation += comp.computation * overclock * mInventory[i].stackSize;
            }
        }
        if (tickingComponents) {
            this.heat += (int) Math.ceil(heat);
        }
        return (int) Math.floor(computation);
    }

    public void postProcessAfterCoolant() {
        for (int i = 0; i < mInventory.length; i++) {
            if (mInventory[i] == null) continue;
            RackComponent comp = componentBinds.get(getUniqueIdentifier(mInventory[i]));
            if (comp == null) continue;
            if (this.heat > comp.maxHeat * 64 * 64) {
                mInventory[i] = null;
                // continue;
            }
        }
    }

    @Override
    public int getInventoryStackLimit() {
        if (!isMeanHatch) return 0;
        return 64;
    }

    public int tickComponents(float oc, float ov) {
        if (!isMeanHatch) return 0;
        if (oc > 1000 + TecTech.RANDOM.nextFloat() || ov > 1000 + TecTech.RANDOM.nextFloat()) {
            getBaseMetaTileEntity().setToFire();
        }
        overClock = oc;
        overVolt = ov;
        return getComputationPower(overClock, overVolt, true);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (!isMeanHatch) return;
            if (aTick % 20 == MULTI_CHECK_AT) {
                if (heat > 0) {
                    heat -= 1;
                } else if (heat < 0) {
                    heat -= -1;
                }

                if (heat > 40960000) {
                    aBaseMetaTileEntity.setToFire();
                } else if (heat < -10000) {
                    heat = -10000;
                }
                if (heat > 0) {
                    float heatC = 0;
                    for (int i = 0; i < mInventory.length; i++) {
                        if (mInventory[i] == null) {
                            continue;
                        }

                        RackComponent comp = componentBinds.get(getUniqueIdentifier(mInventory[i]));
                        if (comp == null) {
                            continue;
                        }
                        if (heat > comp.maxHeat * 64 * 64) {
                            mInventory[i] = null;
                        } else if (comp.heat < 0) {
                            heatC += comp.heat * mInventory[i].stackSize * (heat / 10000f);
                        }
                    }
                    heat += (int) Math.max(-heat, Math.ceil(heatC));
                }

            }
        }
    }

    @Override
    public int getSizeInventory() { // HACK TO NOT DROP CONTENTS!!!
        if (!isMeanHatch) return 0;
        return heat > 500 || getBaseMetaTileEntity().isActive() ? 0 : mInventory.length;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (!isMeanHatch) return new String[] { translateToLocalFormatted("tst.computationhatchmonitor.desc3") };
        return new String[] {
            translateToLocalFormatted("tt.keyphrase.Base_computation", clientLocale) + ": "
                + EnumChatFormatting.AQUA
                + getComputationPower(1, 0, false),
            translateToLocalFormatted("tt.keyphrase.After_overclocking", clientLocale) + ": "
                + EnumChatFormatting.AQUA
                + getComputationPower(overClock, 0, false),
            translateToLocalFormatted("tt.keyphrase.Heat_Accumulated", clientLocale) + ": "
                + EnumChatFormatting.RED
                + (heat + 99) / 100
                + EnumChatFormatting.RESET
                + " %" };
        // heat==0? --> ((heat+9)/10) = 0
        // Heat==1-10? --> 1
    }

    @Override
    public boolean useModularUI() {
        return isMeanHatch;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_TECTECH_LOGO)
                .setSize(18, 18)
                .setPos(181, 63));
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (!isMeanHatch) return;
        // builder.addWidgetInternal();
        builder.setSize(206, 270);
        // builder.addPlayerInventoryLabel(2,200);
        builder.bindPlayerInventory(buildContext.getPlayer(), new Pos2d(8, 181), getGUITextureSet().getItemSlot());
        // builder.widget(
        // new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_HEAT_SINK).setPos(46, 17).setSize(84, 60));

        // Pos2d[] positions = new Pos2d[]{
        // new Pos2d(68, 27), new Pos2d(90, 27),
        // new Pos2d(68, 49), new Pos2d(90, 49),
        // };
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Pos2d position = new Pos2d(8 + 22 * x, 5 + 22 * y);
                int i = 8 * x + y;
                builder.widget(new SlotWidget(new BaseSlot(inventoryHandler, i) {

                    @Override
                    public int getSlotStackLimit() {
                        return 64;
                    }

                    @Override
                    public boolean isEnabled() {
                        return !getBaseMetaTileEntity().isActive() && heat <= 0;
                    }
                }).setBackground(getGUITextureSet().getItemSlot(), TecTechUITextures.OVERLAY_SLOT_RACK)
                    .setPos(position));

            }
        }
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.BUTTON_STANDARD_LIGHT_16x16)
                .setPos(181, 24)
                .setSize(16, 16))
            .widget(
                new DrawableWidget()
                    .setDrawable(
                        () -> getBaseMetaTileEntity().isActive() ? TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON
                            : TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED)
                    .setPos(181, 24)
                    .setSize(16, 16))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().isActive(),
                    val -> getBaseMetaTileEntity().setActive(val)));
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.BUTTON_STANDARD_LIGHT_16x16)
                .setPos(181, 41)
                .setSize(16, 16))
            .widget(
                new DrawableWidget()
                    .setDrawable(
                        () -> heat > 0 ? TecTechUITextures.OVERLAY_BUTTON_HEAT_ON
                            : TecTechUITextures.OVERLAY_BUTTON_HEAT_OFF)
                    .setPos(181, 41)
                    .setSize(16, 16))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> heat, val -> heat = val));
    }

    public static void run() { // 20k heat cap max!

        new RackComponent(ItemList.Circuit_Primitive.get(1), 1, 4, 0, 500, true); // Primitive Circuit
        new RackComponent(ItemList.Circuit_Basic.get(1), 4, 8, 0, 1000, true); // Basic Circuit
        new RackComponent(ItemList.Circuit_Microprocessor.get(1), 6, 8, 0, 1250, true);
        new RackComponent(ItemList.Circuit_Good.get(1), 6, 9, -.05f, 1500, true); // Good Circuit
        new RackComponent(ItemList.Circuit_Integrated_Good.get(1), 7, 9, -.075f, 1750, true);
        new RackComponent(ItemList.Circuit_Processor.get(1), 8, 9, -.07f, 1800, true);
        new RackComponent(ItemList.Circuit_Parts_Advanced.get(1), 1, 2, -.05f, 2000, true);
        new RackComponent(ItemList.Circuit_Nanoprocessor.get(1), 8, 10, -.09f, 2250, true); // Advanced Circuit
        new RackComponent(ItemList.Circuit_Advanced.get(1), 8, 10, -.1f, 2500, true);
        new RackComponent(ItemList.Circuit_Data.get(1), 9, 1, -.1f, 3000, true); // EV Circuit
        new RackComponent(ItemList.Circuit_Nanocomputer.get(1), 11, 10, -.125f, 3300, true);
        new RackComponent(ItemList.Circuit_Quantumprocessor.get(1), 13, 10, -.15f, 3600, true);
        new RackComponent(ItemList.Circuit_Elite.get(1), 12, 10, -.15F, 3500, true); // IV Circuit
        new RackComponent(ItemList.Circuit_Elitenanocomputer.get(1), 14, 10, -.15F, 4000, true);
        new RackComponent(ItemList.Circuit_Quantumcomputer.get(1), 16, 10, -.15F, 4500, true);
        new RackComponent(ItemList.Circuit_Crystalprocessor.get(1), 18, 10, -.15F, 5000, true);
        new RackComponent(ItemList.Circuit_Master.get(1), 16, 12, -.2F, 5000, true); // LuV Circuit
        new RackComponent(ItemList.Circuit_Masterquantumcomputer.get(1), 16, 13, -.2F, 5100, true);
        new RackComponent(ItemList.Circuit_Crystalcomputer.get(1), 20, 14, -.25F, 5200, true);
        new RackComponent(ItemList.Circuit_Neuroprocessor.get(1), 24, 15, -.3F, 5300, true);
        new RackComponent(ItemList.Circuit_Quantummainframe.get(1), 22, 14, -.3F, 5200, true); // ZPM Circuit
        new RackComponent(ItemList.Circuit_Ultimatecrystalcomputer.get(1), 26, 16, -.3F, 5400, true);
        new RackComponent(ItemList.Circuit_Wetwarecomputer.get(1), 30, 18, -.3F, 5600, true);
        new RackComponent(ItemList.Circuit_Crystalmainframe.get(1), 30, 18, -.35F, 5500, true); // UV Circuit
        new RackComponent(ItemList.Circuit_Wetwaresupercomputer.get(1), 35, 22, -.3F, 5700, true);
        new RackComponent(ItemList.Circuit_Wetwaremainframe.get(1), 38, 25, -.4F, 6000, true); // UHV Circuit

        new RackComponent("IC2:ic2.reactorVent", 0, -1, 10f, 1000, false);
        new RackComponent("IC2:ic2.reactorVentCore", 0, -1, 20f, 2500, false);
        new RackComponent("IC2:ic2.reactorVentGold", 0, -1, 40f, 5000, false);
        new RackComponent("IC2:ic2.reactorVentDiamond", 0, -1, 80f, 10000, false); // 2x oc

        if (NewHorizonsCoreMod.isModLoaded()) {
            // GTNH-GT5u circuits
            // these components causes crashes when used with the original GT5u
            new RackComponent(ItemList.NandChip.get(1), 2, 6, 0, 750, true); // Primitive Circuit
            new RackComponent(ItemList.Circuit_Biowarecomputer.get(1), 40, 26, -.35F, 5900, true);
            new RackComponent(ItemList.Circuit_Biowaresupercomputer.get(1), 42, 30, -.4F, 6200, true);
            new RackComponent(ItemList.Circuit_Biomainframe.get(1), 44, 28, -.4F, 6000, true); // UEV Circuit
            new RackComponent(ItemList.Circuit_Bioprocessor.get(1), 34, 20, -.35F, 5800, true);

            new RackComponent("dreamcraft:item.HighEnergyCircuitParts", 3, 2, -.1f, 9001, true);
            new RackComponent("dreamcraft:item.HighEnergyFlowCircuit", 24, 16, -.25f, 10000, true);
            new RackComponent("dreamcraft:item.NanoCircuit", 50, 35, -.45f, 8000, true);
            new RackComponent("dreamcraft:item.PikoCircuit", 64, 40, -.5f, 8500, true);
            new RackComponent("dreamcraft:item.QuantumCircuit", 128, 48, -.6f, 9000, true);
        }

        if (OpenComputers.isModLoaded()) {
            new RackComponent("OpenComputers:item.oc.Transistor", 0, 1, 0f, 100, true); // Transistor
            new RackComponent("OpenComputers:item.oc.Microchip0", 7, 12, -.05f, 1500, true); // chip t1
            new RackComponent("OpenComputers:item.oc.Microchip1", 18, 20, -.1f, 3000, true); // chip t2
            new RackComponent("OpenComputers:item.oc.Microchip2", 25, 22, -.15f, 4500, true); // chip t3
            new RackComponent("OpenComputers:item.oc.ALU", 10, 15, -.05f, 3000, true); // alu
            new RackComponent("OpenComputers:item.oc.ControlUnit", 25, 18, -.05f, 1500, true); // cu

            new RackComponent("OpenComputers:item.oc.ComponentBus0", 42, 30, -.05f, 1500, true); // bus t1
            new RackComponent("OpenComputers:item.oc.ComponentBus1", 70, 50, -.1f, 3000, true); // bus t2
            new RackComponent("OpenComputers:item.oc.ComponentBus2", 105, 72, -.15f, 4500, true); // bus t3

            new RackComponent("OpenComputers:item.oc.CPU0", 106, 73, -.1f, 1500, true); // cpu t1
            new RackComponent("OpenComputers:item.oc.CPU1", 226, 153, -.15f, 3000, true); // cpu t2
            new RackComponent("OpenComputers:item.oc.CPU2", 374, 241, -.2f, 4500, true); // cpu t3

            new RackComponent("OpenComputers:item.oc.GraphicsCard0", 20, 27, -.1f, 1500, true); // gpu t1
            new RackComponent("OpenComputers:item.oc.GraphicsCard1", 62, 67, -.2f, 3000, true); // gpu t2
            new RackComponent("OpenComputers:item.oc.GraphicsCard2", 130, 111, -.3f, 4500, true); // gpu t3

            new RackComponent("OpenComputers:item.oc.APU0", 350, 234, -.1f, 1500, true); // apu t2
            new RackComponent("OpenComputers:item.oc.APU1", 606, 398, -.2f, 4500, true); // apu t3
            new RackComponent("OpenComputers:item.oc.APU2", 1590, 1006, -.3f, 9000, true); // apu tC
        }
    }

    public static class RackComponent implements Comparable<RackComponent> {

        private final String unlocalizedName;
        private final float heat, coEff, computation, maxHeat;
        private final boolean subZero;

        RackComponent(ItemStack is, float computation, float heat, float coEff, float maxHeat, boolean subZero) {
            this(getUniqueIdentifier(is), computation, heat, coEff, maxHeat, subZero);
        }

        RackComponent(String is, float computation, float heat, float coEff, float maxHeat, boolean subZero) {
            unlocalizedName = is;
            this.heat = heat;
            this.coEff = coEff;
            this.computation = computation;
            this.maxHeat = maxHeat;
            this.subZero = subZero;
            componentBinds.put(unlocalizedName, this);
            if (DEBUG_MODE) {
                TecTech.LOGGER.info("Component registered: " + unlocalizedName);
            }
        }

        @Override
        public int compareTo(RackComponent o) {
            return unlocalizedName.compareTo(o.unlocalizedName);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof RackComponent) {
                return compareTo((RackComponent) obj) == 0;
            }
            return false;
        }
    }
}
