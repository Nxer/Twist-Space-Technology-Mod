package com.Nxer.TwistSpaceTechnology.nei;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_EssentiaFrontend;

import codechicken.nei.api.API;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.event.NEIConfigsLoadedEvent;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.IRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.nei.GTNEIDefaultHandler;

@SideOnly(Side.CLIENT)
public final class EssentiaNEIHandler extends GTNEIDefaultHandler {

    private final TST_EssentiaFrontend essentiaFrontend;

    private EssentiaNEIHandler(RecipeCategory category) {
        super(category);
        essentiaFrontend = (TST_EssentiaFrontend) frontend;
    }

    public static void register() {
        register(GTCMItemList.IndustrialMagicMatrix, GTCMRecipe.IndustrialMagicMatrixRecipeMap);
        register(GTCMItemList.IndustrialAlchemyTower, GTCMRecipe.IndustrialAlchemyTowerRecipeMap);
        register(GTCMItemList.SkypiercerTower, GTCMRecipe.SkypiercerTowerRecipeMap);
        register(GTCMItemList.PrimordialDisjunctus, GTCMRecipe.PrimordialDisjunctusVisualRecipeMap);
    }

    private static void register(GTCMItemList controller, RecipeMap<?> map) {
        if (controller.hasBeenSet()) {
            MinecraftForge.EVENT_BUS.register(new EssentiaNEIHandler(map.getDefaultRecipeCategory()));
        }
    }

    @SubscribeEvent
    public void onNEIConfigsLoaded(NEIConfigsLoadedEvent event) {
        // NEE discovers GT recipe maps through registerNEI. Replace only the rendered handler after all plugins load.
        GuiCraftingRecipe.craftinghandlers.removeIf(this::isDefaultHandler);
        GuiUsageRecipe.usagehandlers.removeIf(this::isDefaultHandler);
        API.registerRecipeHandler(this);
        API.registerUsageHandler(this);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    private boolean isDefaultHandler(IRecipeHandler handler) {
        return handler.getClass() == GTNEIDefaultHandler.class
            && ((GTNEIDefaultHandler) handler).getRecipeMap() == recipeMap;
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new EssentiaNEIHandler(recipeCategory);
    }

    @Override
    public String getHandlerId() {
        return getOverlayIdentifier();
    }

    @Override
    public void drawBackground(int recipeIndex) {
        super.drawBackground(recipeIndex);
        CachedDefaultRecipe recipe = (CachedDefaultRecipe) arecipes.get(recipeIndex);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        essentiaFrontend.drawNEIBackground(recipe.mRecipe);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public int getRecipeHeight(int recipeIndex) {
        return essentiaFrontend.getContentHeight(((CachedDefaultRecipe) arecipes.get(recipeIndex)).mRecipe) + 80;
    }

    @Override
    protected int getDescriptionYOffset() {
        return essentiaFrontend.getContentHeight(templateContext.recipeSupplier.get().mRecipe);
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return essentiaFrontend.allowsPatternTransfer() && super.hasOverlay(gui, container, recipe);
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        return essentiaFrontend.allowsPatternTransfer() ? super.getOverlayHandler(gui, recipe) : null;
    }
}
