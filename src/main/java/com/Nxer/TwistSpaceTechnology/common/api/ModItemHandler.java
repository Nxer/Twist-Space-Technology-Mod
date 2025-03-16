package com.Nxer.TwistSpaceTechnology.common.api;

import static gregtech.api.util.GTUtility.copyAmount;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;

import fox.spiteful.avaritia.items.LudicrousItems;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public class ModItemHandler {

    /**
     * Use for mod items that are not loaded or registered in the dev environment
     */

    public static final BloodArsenalItemHandler BloodArsenal = new BloodArsenalItemHandler();
    public static final AmunRaItemHandler AmunRa = new AmunRaItemHandler();
    public static final GraviSuiteItemHandler GraviSuite = new GraviSuiteItemHandler();
    public static final EternalSingularityItemHandler EternalSingularity = new EternalSingularityItemHandler();
    public static final OpenComputersItemHandler OpenComputers = new OpenComputersItemHandler();

    @Nullable
    public static ItemStack getInfinityCatalyst(int amount) {
        if (LudicrousItems.resource != null) {
            return new ItemStack(LudicrousItems.resource, amount, 5);
        }
        return null;
    }

    public static class BloodArsenalItemHandler extends ModHandler {

        public final ModItem AmorphicCatalyst;

        public BloodArsenalItemHandler() {
            super(Mods.BloodArsenal);
            AmorphicCatalyst = new ModItem(mod, "amorphic_catalyst", 0, "Amorphic Catalyst");
        }
    }

    public static class AmunRaItemHandler extends ModHandler {

        public final ModItem LightWeightPlate;
        public final ModItem ShuttleNoseCone;
        public final ModItem IonThrusterJet;

        public AmunRaItemHandler() {
            super(Mods.GalacticraftAmunRa);
            LightWeightPlate = new ModItem(mod, "item.baseItem", 15, "Light Weight Plate");
            ShuttleNoseCone = new ModItem(mod, "item.baseItem", 16, "Shuttle Nose Cone");
            IonThrusterJet = new ModItem(mod, "tile.machines2", 11, "IonThruster Jet");
        }
    }

    public static class GraviSuiteItemHandler extends ModHandler {

        public final ModItem CoolingCore;
        public final ModItem GravitationEngine;

        public GraviSuiteItemHandler() {
            super(Mods.GraviSuite);
            CoolingCore = new ModItem(mod, "itemSimpleItem", 2, "Cooling Core");
            GravitationEngine = new ModItem(mod, "itemSimpleItem", 3, "Gravitation Engine");
        }
    }

    public static class EternalSingularityItemHandler extends ModHandler {

        public final ModItem NitronicSingularity;
        public final ModItem PsychoticSingularity;
        public final ModItem SphaghetticSingularity;
        public final ModItem PneumaticSingularity;
        public final ModItem CrypticSingularity;
        public final ModItem HistoricSingularity;
        public final ModItem MeteoricSingularity;

        public EternalSingularityItemHandler() {
            super(Mods.EternalSingularity);
            NitronicSingularity = new ModItem(mod, "combined_singularity", 0, "Nitronic Singularity");
            PsychoticSingularity = new ModItem(mod, "combined_singularity", 1, "Psychotic Singularity");
            SphaghetticSingularity = new ModItem(mod, "combined_singularity", 2, "Sphaghettic Singularity");
            PneumaticSingularity = new ModItem(mod, "combined_singularity", 3, "Pneumatic Singularity");
            CrypticSingularity = new ModItem(mod, "combined_singularity", 4, "Cryptic Singularity");
            HistoricSingularity = new ModItem(mod, "combined_singularity", 5, "Historic Singularity");
            MeteoricSingularity = new ModItem(mod, "combined_singularity", 6, "Meteoric Singularity");
        }
    }

    public static class OpenComputersItemHandler extends ModHandler {

        public final ModItem CentralProcessingUnit_T3;

        public OpenComputersItemHandler() {
            super(Mods.OpenComputers);
            CentralProcessingUnit_T3 = new ModItem(mod, "item", 43, "Central Processing Unit T3");
        }
    }

    public static abstract class ModHandler {

        public final String mod;

        public ModHandler(String modID) {
            this.mod = modID;
        }

        public ModHandler(Mods mod) {
            this(mod.ID);
        }
    }

    public static class ModItem {

        public final String unlocalizedName;
        public final String localizedName;
        public final int meta;
        public final String modID;
        protected ItemStack itemStack;

        public ModItem(@NotNull Mods mod, @NotNull String unlocalizedName, int meta, @NotNull String localizedName) {
            this(mod.ID, unlocalizedName, meta, localizedName);
        }

        public ModItem(@NotNull String modID, @NotNull String unlocalizedName, int meta,
            @NotNull String localizedName) {
            this.modID = modID;
            this.unlocalizedName = unlocalizedName;
            this.meta = meta;
            this.localizedName = localizedName;
        }

        public ItemStack get(int count) {
            if (itemStack == null) {
                itemStack = GTModHandler.getModItem(modID, unlocalizedName, 1, meta);
                if (itemStack == null) {
                    itemStack = createFallbackItem(localizedName, count);
                }
            }
            return copyAmount(count, itemStack);
        }

        public ItemStack createFallbackItem(String name, int count) {
            ItemStack stack = GTCMItemList.TestItem0.get(count);
            String stackName = EnumChatFormatting.WHITE + modID + " : " + name;
            stack.setStackDisplayName(EnumChatFormatting.RESET + stackName);
            return stack;
        }
    }
}
