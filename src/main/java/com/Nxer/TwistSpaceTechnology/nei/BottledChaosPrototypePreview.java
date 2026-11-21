package com.Nxer.TwistSpaceTechnology.nei;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosPrototypeStructure;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;

import blockrenderer6343.client.utils.ConstructableData;
import blockrenderer6343.integration.gregtech.GTConstructableScan;
import blockrenderer6343.integration.gregtech.GTGuiMultiblockHandler;
import blockrenderer6343.integration.gregtech.GTNEIMultiblockHandler;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.RecipeCatalysts;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

/** Customize only Bottled Chaos controllers; other queries keep BlockRenderer's native handler and GUI. */
public final class BottledChaosPrototypePreview extends GTNEIMultiblockHandler {

    private static MM_BottledChaos preview;
    private static WeakReference<BottledChaosPrototypePreview> materialOwner = new WeakReference<>(null);
    private List<ItemStack> previewIngredients = Collections.emptyList();

    public BottledChaosPrototypePreview() {
        guiHandler = new PrototypeGuiHandler();
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
        // Reuse native layout/order fallback, but keep the subclass overlay ID for its own material cache.
        return GTNEIMultiblockHandler.class.getName();
    }

    @Override
    protected ObjectSet<IConstructable> tryLoadingMultiblocks(ItemStack candidate) {
        if (!MM_BottledChaos.isPrototype(candidate)) return super.tryLoadingMultiblocks(candidate);
        return ObjectSets.singleton(getPrototypePreview(candidate));
    }

    @Override
    public void resetPositionedIngredients(List<ItemStack> ingredients) {
        previewIngredients = ingredients.stream()
            .map(ItemStack::copy)
            .collect(Collectors.toList());
        super.resetPositionedIngredients(ingredients);
    }

    @Override
    public void drawBackground(int recipe) {
        super.drawBackground(recipe);
        // NEI stores materials by overlay ID. Restore this query's list when returning through history.
        if (materialOwner.get() != this) {
            RecipeCatalysts.putRecipeCatalysts(getOverlayIdentifier(), previewIngredients);
            materialOwner = new WeakReference<>(this);
        }
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

    /** Keep the native registration slot, but intercept only an explicit Bottled Chaos controller query. */
    private static final class PrototypeQueryHandler extends GTNEIMultiblockHandler {

        private final GTNEIMultiblockHandler nativeHandler;

        private PrototypeQueryHandler(GTNEIMultiblockHandler nativeHandler) {
            this.nativeHandler = nativeHandler;
        }

        private GTNEIMultiblockHandler handlerFor(String id, Object[] arguments) {
            if ("item".equals(id) && arguments.length > 0
                && arguments[0] instanceof ItemStack stack
                && GTCMItemList.BottledChaos.hasBeenSet()
                && GTUtility.areStacksEqual(stack, GTCMItemList.BottledChaos.get(1), true)) {
                return new BottledChaosPrototypePreview();
            }
            return nativeHandler;
        }

        @Override
        public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
            return handlerFor(outputId, results).getRecipeHandler(outputId, results);
        }

        @Override
        public IUsageHandler getUsageHandler(String inputId, Object... ingredients) {
            return handlerFor(inputId, ingredients).getUsageHandler(inputId, ingredients);
        }

        @Override
        public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
            return handlerFor(inputId, ingredients).getUsageAndCatalystHandler(inputId, ingredients);
        }

        @Override
        public TemplateRecipeHandler newInstance() {
            return nativeHandler.newInstance();
        }

        @Override
        public String getHandlerId() {
            return nativeHandler.getHandlerId();
        }

        @Override
        public String getOverlayIdentifier() {
            return nativeHandler.getOverlayIdentifier();
        }
    }

    public static final class Registration {

        private boolean craftingInstalled;
        private boolean usageInstalled;

        @SubscribeEvent
        public void onClientTick(ClientTickEvent event) {
            if (!NEIClientConfig.isLoaded()) return;
            if (!craftingInstalled) {
                for (int i = 0; i < GuiCraftingRecipe.craftinghandlers.size(); i++) {
                    if (GuiCraftingRecipe.craftinghandlers.get(i)
                        .getClass() == GTNEIMultiblockHandler.class) {
                        GTNEIMultiblockHandler nativeHandler = (GTNEIMultiblockHandler) GuiCraftingRecipe.craftinghandlers
                            .get(i);
                        GuiCraftingRecipe.craftinghandlers.set(i, new PrototypeQueryHandler(nativeHandler));
                        craftingInstalled = true;
                    }
                }
            }
            if (!usageInstalled) {
                for (int i = 0; i < GuiUsageRecipe.usagehandlers.size(); i++) {
                    if (GuiUsageRecipe.usagehandlers.get(i)
                        .getClass() == GTNEIMultiblockHandler.class) {
                        GTNEIMultiblockHandler nativeHandler = (GTNEIMultiblockHandler) GuiUsageRecipe.usagehandlers
                            .get(i);
                        GuiUsageRecipe.usagehandlers.set(i, new PrototypeQueryHandler(nativeHandler));
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
