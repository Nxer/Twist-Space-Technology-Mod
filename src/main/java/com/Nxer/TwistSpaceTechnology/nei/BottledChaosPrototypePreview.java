package com.Nxer.TwistSpaceTechnology.nei;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosPrototypeStructure;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;

import blockrenderer6343.client.utils.ConstructableData;
import blockrenderer6343.integration.gregtech.GTConstructableScan;
import blockrenderer6343.integration.gregtech.GTGuiMultiblockHandler;
import blockrenderer6343.integration.gregtech.GTNEIMultiblockHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

/** Preserve the queried controller's NBT without changing the other GT recipe handlers. */
public final class BottledChaosPrototypePreview extends GTNEIMultiblockHandler {

    private static final GTGuiMultiblockHandler GUI_HANDLER = new PrototypeGuiHandler();
    private static MM_BottledChaos preview;

    public BottledChaosPrototypePreview() {
        guiHandler = GUI_HANDLER;
        guiHandler.setOnIngredientChanged(this::resetPositionedIngredients);
        guiHandler.setOnCandidateChanged(this::setResults);
    }

    public static void register() {
        // NEI plugins may load in either order. Install after all of them have registered their handlers.
        FMLCommonHandler.instance()
            .bus()
            .register(new Registration());
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new BottledChaosPrototypePreview();
    }

    @Override
    public String getHandlerId() {
        return GTNEIMultiblockHandler.class.getName();
    }

    @Override
    protected ObjectSet<IConstructable> tryLoadingMultiblocks(ItemStack candidate) {
        if (!MM_BottledChaos.isPrototype(candidate)) return super.tryLoadingMultiblocks(candidate);
        return ObjectSets.singleton(getPrototypePreview(candidate));
    }

    private static synchronized MM_BottledChaos getPrototypePreview(ItemStack candidate) {
        if (preview == null) {
            BaseMetaTileEntity tile = new BaseMetaTileEntity();
            tile.setInitialValuesAsNBT(null, (short) candidate.getItemDamage());
            preview = (MM_BottledChaos) tile.getMetaTileEntity();
            preview.initDefaultModes(candidate.getTagCompound());
            // The unregistered NBT variant also needs native tier/channel discovery for the preview sliders.
            new GTConstructableScan(components -> {}, Collections.singletonList(preview)).run();
        }
        return preview;
    }

    private static final class PrototypeGuiHandler extends GTGuiMultiblockHandler {

        @Override
        protected void loadChannels() {
            ConstructableData original = constructableData;
            if (!original.getChannelMaxTierMap()
                .containsKey(BottledChaosPrototypeStructure.PREVIEW_CHANNEL)) {
                super.loadChannels();
                return;
            }
            // Keep the shared scan data intact for GuideNH; NEI already selects the variant through item NBT.
            ConstructableData filtered = new ConstructableData() {

                @Override
                public Collection<String> getCurrentChannels() {
                    return original.getCurrentChannels();
                }

                @Override
                public int getCurrentTier() {
                    return original.getCurrentTier();
                }
            };
            filtered.getChannelMaxTierMap()
                .putAll(original.getChannelMaxTierMap());
            filtered.getChannelMaxTierMap()
                .removeInt(BottledChaosPrototypeStructure.PREVIEW_CHANNEL);
            filtered.getChannelDefinitions()
                .putAll(original.getChannelDefinitions());
            constructableData = filtered;
            try {
                super.loadChannels();
            } finally {
                constructableData = original;
            }
        }

        @Override
        protected ItemStack getBuildTriggerStack() {
            ItemStack trigger = super.getBuildTriggerStack();
            ChannelDataAccessor.unsetChannelData(trigger, BottledChaosPrototypeStructure.PREVIEW_CHANNEL);
            return trigger;
        }

        @Override
        protected String getMultiblockName() {
            return MM_BottledChaos.isPrototype(stackForm) ? TSTUtils.tr(MM_BottledChaos.PROTOTYPE_NAME_KEY)
                : super.getMultiblockName();
        }

        @Override
        public List<String> getHoveredTooltip(ItemStack stack) {
            // The native block picker only retains the GT meta ID, so restore the preview controller's NBT.
            if (MM_BottledChaos.isPrototype(stackForm) && GTUtility.areStacksEqual(stackForm, stack, true)) {
                return super.getHoveredTooltip(stackForm);
            }
            return super.getHoveredTooltip(stack);
        }
    }

    public static final class Registration {

        private boolean craftingInstalled;
        private boolean usageInstalled;

        @SubscribeEvent
        public void onClientTick(ClientTickEvent event) {
            if (!craftingInstalled) {
                for (int i = 0; i < GuiCraftingRecipe.craftinghandlers.size(); i++) {
                    if (GuiCraftingRecipe.craftinghandlers.get(i)
                        .getClass() == GTNEIMultiblockHandler.class) {
                        GuiCraftingRecipe.craftinghandlers.set(i, new BottledChaosPrototypePreview());
                        craftingInstalled = true;
                    }
                }
            }
            if (!usageInstalled) {
                for (int i = 0; i < GuiUsageRecipe.usagehandlers.size(); i++) {
                    if (GuiUsageRecipe.usagehandlers.get(i)
                        .getClass() == GTNEIMultiblockHandler.class) {
                        GuiUsageRecipe.usagehandlers.set(i, new BottledChaosPrototypePreview());
                        usageInstalled = true;
                    }
                }
            }
            if (craftingInstalled && usageInstalled) FMLCommonHandler.instance()
                .bus()
                .unregister(this);
        }
    }
}
