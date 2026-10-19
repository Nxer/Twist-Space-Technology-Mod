package com.Nxer.TwistSpaceTechnology.nei;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosRecipeMetadata.DISPLAY_BATCH;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosExpandedFrontend.COMPACT_SLOT_COUNT;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosExpandedFrontend.usesExpandedLayout;
import static java.util.Collections.singletonList;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosCredential;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosExpandedFrontend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosFrontend;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.StackInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.nei.GTNEIDefaultHandler;

/** Highlights searchable endpoints while retaining GT's layout, alternatives, fluid lookup and category cache. */
@SideOnly(Side.CLIENT)
public final class BottledChaosNEIHandler extends GTNEIDefaultHandler {

    private static final int KEY_INPUT = 1;
    private static final int KEY_OUTPUT = 2;
    private static final int INPUT_COLOR = 0xFF45E6FF;
    private static final int OUTPUT_COLOR = 0xFFFFD24A;

    private final TST_BottledChaosFrontend compactFrontend;
    private final ModularWindow compactWindow;
    private final LinkedList<RecipeTransferRect> compactTransferRects = new LinkedList<>();
    private final LinkedList<RecipeTransferRect> expandedTransferRects;

    public BottledChaosNEIHandler(RecipeCategory category) {
        super(category);
        compactFrontend = ((TST_BottledChaosExpandedFrontend) frontend).getCompactFrontend();
        compactWindow = compactFrontend.createNEITemplate(templateContext)
            .build();
        UIInfos.initializeWindow(Minecraft.getMinecraft().thePlayer, compactWindow);
        expandedTransferRects = transferRects;
        for (Rectangle bounds : compactFrontend.getUIProperties().neiTransferRect) {
            Rectangle translated = new Rectangle(bounds);
            translated.translate(WINDOW_OFFSET.x, WINDOW_OFFSET.y);
            compactTransferRects.add(new RecipeTransferRect(translated, recipeMap.unlocalizedName));
        }
    }

    /** GT still supplies category icons, layout information and controller catalysts for these maps. */
    public static void register() {
        if (!GTCMItemList.BottledChaos.hasBeenSet()) return;
        for (RecipeCategory category : RecipeCategory.ALL_RECIPE_CATEGORIES.values()) {
            if (category.recipeMap != GTCMRecipe.BottledChaosRecipes) continue;
            var handler = new BottledChaosNEIHandler(category);
            API.registerRecipeHandler(handler);
            API.registerUsageHandler(handler);
        }
    }

    @Override
    public String getHandlerId() {
        return getOverlayIdentifier();
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new BottledChaosNEIHandler(recipeCategory);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!isController(result) && !loadCredential(result)) super.loadCraftingRecipes(result);
    }

    @Override
    public void loadUsageRecipes(ItemStack input) {
        if (isController(input)) arecipes.addAll(getCache());
        else if (!loadCredential(input)) super.loadUsageRecipes(input);
    }

    @Override
    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
        // Controller and credential browsing must not inherit GT's catalyst voltage filter.
        if (inputId.equals("item") && ingredients.length > 0
            && ingredients[0] instanceof ItemStack stack
            && (isController(stack) || ItemBottledChaosCredential.getCapacity(stack) > 0))
            return getUsageHandler(inputId, ingredients);
        return super.getUsageAndCatalystHandler(inputId, ingredients);
    }

    private static boolean isController(ItemStack stack) {
        return GTCMItemList.BottledChaos.hasBeenSet()
            && GTUtility.areStacksEqual(stack, GTCMItemList.BottledChaos.get(1), true);
    }

    /** Blank frames browse all entries; bound credentials browse every entry their tags authorize. */
    private boolean loadCredential(ItemStack stack) {
        if (ItemBottledChaosCredential.getCapacity(stack) == 0) return false;
        long tags = ItemBottledChaosCredential.getRouteMask(stack);
        for (CachedDefaultRecipe recipe : getCache()) {
            var display = recipe.mRecipe.getMetadata(DISPLAY_BATCH);
            if (display != null && (tags == 0 || (tags & display.requiredTagMask()) == display.requiredTagMask()))
                arecipes.add(recipe);
        }
        return true;
    }

    @Override
    protected CachedDefaultRecipe createCachedRecipe(GTRecipe recipe) {
        return new SearchableRecipe(recipe);
    }

    @Override
    public int getRecipeHeight(int recipeIndex) {
        GTRecipe recipe = ((CachedDefaultRecipe) arecipes.get(recipeIndex)).mRecipe;
        var display = recipe.getMetadata(DISPLAY_BATCH);
        return backgroundHeight(recipe) + 50
            + (display == null ? 0
                : 10 * display.processingTimes()
                    .size());
    }

    private int backgroundHeight(GTRecipe recipe) {
        return (usesExpandedLayout(recipe) ? neiProperties
            : compactFrontend.getNEIProperties()).recipeBackgroundSize.height;
    }

    @Override
    protected void drawUI(ModularWindow window) {
        super.drawUI(usesExpandedLayout(templateContext.recipeSupplier.get().mRecipe) ? window : compactWindow);
    }

    @Override
    protected int getDescriptionYOffset() {
        return super.getDescriptionYOffset() + backgroundHeight(templateContext.recipeSupplier.get().mRecipe)
            - neiProperties.recipeBackgroundSize.height;
    }

    private void selectTransferRects(int recipeIndex) {
        transferRects = usesExpandedLayout(((CachedDefaultRecipe) arecipes.get(recipeIndex)).mRecipe)
            ? expandedTransferRects
            : compactTransferRects;
    }

    @Override
    public List<String> handleTooltip(GuiRecipe<?> gui, List<String> currentTip, int recipeIndex) {
        selectTransferRects(recipeIndex);
        return super.handleTooltip(gui, currentTip, recipeIndex);
    }

    @Override
    public boolean mouseClicked(GuiRecipe<?> gui, int button, int recipeIndex) {
        selectTransferRects(recipeIndex);
        return super.mouseClicked(gui, button, recipeIndex);
    }

    @Override
    public boolean keyTyped(GuiRecipe<?> gui, char keyChar, int keyCode, int recipeIndex) {
        selectTransferRects(recipeIndex);
        return super.keyTyped(gui, keyChar, keyCode, recipeIndex);
    }

    @Override
    public void drawForeground(int recipeIndex) {
        super.drawForeground(recipeIndex);
        if (!(arecipes.get(recipeIndex) instanceof SearchableRecipe recipe) || recipe.markers.isEmpty()) return;
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_CURRENT_BIT);
        try {
            NEIClientUtils.gl2DRenderContext(
                () -> {
                    for (var marker : recipe.markers.entrySet()) drawMarkerFrame(marker.getKey(), marker.getValue());
                });
        } finally {
            GL11.glPopAttrib();
        }
    }

    private static void drawMarkerFrame(PositionedStack stack, int role) {
        int x = stack.relx;
        int y = stack.rely;
        int right = x + stack.width;
        int bottom = y + stack.height;
        int color = role == KEY_INPUT ? INPUT_COLOR : OUTPUT_COLOR;
        // Keep every colored pixel outside the icon, including its native quantity and catalyst badges.
        Gui.drawRect(x - 1, y - 1, right + 1, y, color);
        Gui.drawRect(x - 1, bottom, right + 1, bottom + 1, color);
        Gui.drawRect(x - (role == KEY_INPUT ? 2 : 1), y, x, bottom, color);
        Gui.drawRect(right, y, right + (role == KEY_OUTPUT ? 2 : 1), bottom, color);
        // Reinforce the four corners without covering the first or last row of icon pixels.
        Gui.drawRect(x - 2, y - 2, x + 3, y, color);
        Gui.drawRect(x - 2, y, x, y + 3, color);
        Gui.drawRect(right - 3, y - 2, right + 2, y, color);
        Gui.drawRect(right, y, right + 2, y + 3, color);
        Gui.drawRect(x - 2, bottom, x + 3, bottom + 2, color);
        Gui.drawRect(x - 2, bottom - 3, x, bottom, color);
        Gui.drawRect(right - 3, bottom, right + 2, bottom + 2, color);
        Gui.drawRect(right, bottom - 3, right + 2, bottom, color);
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack stack, List<String> currentTip, int recipeIndex) {
        List<String> tooltip = super.handleItemTooltip(gui, stack, currentTip, recipeIndex);
        if (stack == null || !(arecipes.get(recipeIndex) instanceof SearchableRecipe recipe)) return tooltip;
        for (var marker : recipe.markers.entrySet()) {
            if (!gui.isMouseOver(marker.getKey(), recipeIndex)) continue;
            // #tr tst.bottledchaos.recipe.key_input
            // # Key Input
            // #zh_CN 关键输入
            if (marker.getValue() == KEY_INPUT) tooltip
                .add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tst.bottledchaos.recipe.key_input"));
            // #tr tst.bottledchaos.recipe.key_output
            // # Key Output
            // #zh_CN 关键输出
            if (marker.getValue() == KEY_OUTPUT) tooltip
                .add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("tst.bottledchaos.recipe.key_output"));
            break;
        }
        return tooltip;
    }

    /** Search slots and visual roles are prepared once; rendering keeps the original stacks and alternatives. */
    private final class SearchableRecipe extends CachedDefaultRecipe {

        private final List<PositionedStack> lookupInputs;
        private final List<PositionedStack> lookupOutputs;
        private final Map<PositionedStack, Integer> markers = new IdentityHashMap<>();

        private SearchableRecipe(GTRecipe recipe) {
            super(recipe);
            if (!usesExpandedLayout(recipe)) {
                moveSlots(
                    mInputs,
                    frontend.getItemInputPositions(COMPACT_SLOT_COUNT),
                    compactFrontend.getItemInputPositions(COMPACT_SLOT_COUNT));
                moveSlots(
                    mInputs,
                    singletonList(frontend.getSpecialItemPosition()),
                    singletonList(compactFrontend.getSpecialItemPosition()));
                moveSlots(
                    mInputs,
                    frontend.getFluidInputPositions(COMPACT_SLOT_COUNT),
                    compactFrontend.getFluidInputPositions(COMPACT_SLOT_COUNT));
                moveSlots(
                    mOutputs,
                    frontend.getItemOutputPositions(COMPACT_SLOT_COUNT),
                    compactFrontend.getItemOutputPositions(COMPACT_SLOT_COUNT));
                moveSlots(
                    mOutputs,
                    frontend.getFluidOutputPositions(COMPACT_SLOT_COUNT),
                    compactFrontend.getFluidOutputPositions(COMPACT_SLOT_COUNT));
            }
            var display = recipe.getMetadata(DISPLAY_BATCH);
            if (display == null) {
                lookupInputs = mInputs;
                lookupOutputs = mOutputs;
            } else {
                lookupInputs = markedSlots(mInputs, display.lookupInputs());
                lookupOutputs = markedSlots(mOutputs, display.lookupOutputs());
                for (PositionedStack stack : lookupInputs) markers.put(stack, KEY_INPUT);
                for (PositionedStack stack : lookupOutputs) markers.put(stack, KEY_OUTPUT);
            }
        }

        @Override
        public boolean contains(Collection<PositionedStack> stacks, ItemStack ingredient) {
            return super.contains(
                stacks == mInputs ? lookupInputs : stacks == mOutputs ? lookupOutputs : stacks,
                ingredient);
        }
    }

    private static void moveSlots(List<PositionedStack> stacks, List<Pos2d> from, List<Pos2d> to) {
        for (PositionedStack stack : stacks) {
            for (int index = 0; index < from.size(); index++) {
                Pos2d origin = from.get(index);
                if (stack.relx == origin.x + WINDOW_OFFSET.x + 1 && stack.rely == origin.y + WINDOW_OFFSET.y + 1) {
                    stack.relx = to.get(index).x + WINDOW_OFFSET.x + 1;
                    stack.rely = to.get(index).y + WINDOW_OFFSET.y + 1;
                    break;
                }
            }
        }
    }

    private static List<PositionedStack> markedSlots(List<PositionedStack> stacks, Object... keys) {
        List<PositionedStack> result = new ArrayList<>();
        for (PositionedStack stack : stacks) {
            for (Object key : keys) {
                if (isMarkedSlot(stack, key)) {
                    result.add(stack);
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isMarkedSlot(PositionedStack stack, Object key) {
        if (key instanceof TST_ItemID item) return stack.containsWithNBT(item.getItemStack());
        if (key instanceof FluidId fluid) for (ItemStack alternative : stack.items) {
            FluidStack displayed = StackInfo.getFluid(alternative);
            if (displayed != null && fluid.equals(FluidId.create(displayed))) return true;
        }
        return false;
    }
}
