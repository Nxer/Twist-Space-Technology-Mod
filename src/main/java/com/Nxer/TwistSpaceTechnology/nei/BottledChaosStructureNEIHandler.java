package com.Nxer.TwistSpaceTechnology.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosComplexStructures;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureDisplay;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureDisplay.Selection;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_BottledChaosStructureFrontend;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import appeng.client.gui.implementations.GuiPatternTerm;
import appeng.client.gui.implementations.GuiPatternTermEx;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.recipe.GuiOverlayButton.ItemOverlayState;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.IRecipeHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.client.config.GuiSlider;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.nei.GTNEIDefaultHandler;

/** Material blocks remain visible but cannot reverse-query packaging examples. */
@SideOnly(Side.CLIENT)
public final class BottledChaosStructureNEIHandler extends GTNEIDefaultHandler {

    private static final int SLIDER_HEIGHT = 16;
    private static final int SELECTION_HEIGHT = SLIDER_HEIGHT + 1;

    private StructureRecipe dragOwner;
    private int dragRow;

    private List<CachedDefaultRecipe> sourceCache;
    private List<CachedDefaultRecipe> structureCache;

    public BottledChaosStructureNEIHandler() {
        this(GTCMRecipe.BottledChaosStructureRecipes.getDefaultRecipeCategory());
    }

    public BottledChaosStructureNEIHandler(RecipeCategory category) {
        super(category);
    }

    public static void register() {
        API.registerRecipeHandler(new BottledChaosStructureNEIHandler());
        API.registerUsageHandler(new BottledChaosStructureNEIHandler());
    }

    @Override
    public String getHandlerId() {
        return getOverlayIdentifier();
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new BottledChaosStructureNEIHandler(recipeCategory);
    }

    @Override
    public List<PositionedStack> getIngredientStacks(int recipe) {
        // NEI snapshots inputs and outputs together when refreshing its recipe widget. Drawing is too late:
        // the widget can keep an older snapshot while a hovered slot or overlay button pauses its cycle.
        if (arecipes.get(recipe) instanceof StructureRecipe structure) structure.update();
        return super.getIngredientStacks(recipe);
    }

    private int selectionY(StructureRecipe structure) {
        return getDescriptionYOffset() + 4
            + Minecraft.getMinecraft().fontRenderer
                .listFormattedStringToWidth(TST_BottledChaosStructureFrontend.poolText(structure.display), 156)
                .size() * 10;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (dragOwner == null) return;
        boolean visible = Minecraft.getMinecraft().currentScreen instanceof GuiRecipe<?>gui && gui.getHandler() == this
            && gui.getRecipeIndices()
                .stream()
                .anyMatch(index -> arecipes.get(index) == dragOwner);
        if (!visible || !Mouse.isButtonDown(0)) {
            GuiSlider slider = dragOwner.sliders.get(dragRow);
            slider.mouseReleased(0, 0);
            if (visible) dragOwner.select(dragRow, slider.getValueInt());
            else slider.setValue(dragOwner.selected(dragRow));
            dragOwner = null;
        }
    }

    private static boolean contains(GuiSlider slider, Point mouse) {
        return new Rectangle(slider.xPosition, slider.yPosition, slider.width, slider.height).contains(mouse);
    }

    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        if (!(arecipes.get(recipe) instanceof StructureRecipe structure)) return;
        Point mouse = Minecraft.getMinecraft().currentScreen instanceof GuiRecipe<?>gui
            ? gui.getRecipeMousePosition(recipe)
            : new Point(-1, -1);
        var font = Minecraft.getMinecraft().fontRenderer;
        int y = selectionY(structure);
        for (int row = 0; row < structure.sliders.size(); row++) {
            GuiSlider slider = structure.sliders.get(row);
            slider.displayString = structure.selections.get(row)
                .text(slider.getValueInt());
            slider.drawButton(Minecraft.getMinecraft(), mouse.x, mouse.y);
            y += SELECTION_HEIGHT;
        }
        if (structure.mRecipe.mSpecialValue != 0) {
            String[] notes = structure.mRecipe.getNeiDesc();
            if (notes != null) for (String note : notes) {
                for (String line : font.listFormattedStringToWidth(note, 156)) {
                    font.drawString(line, 7, y, 0x404040);
                    y += 10;
                }
            }
        }
    }

    @Override
    public int getRecipeHeight(int recipe) {
        if (!(arecipes.get(recipe) instanceof StructureRecipe structure)) return super.getRecipeHeight(recipe);
        int height = selectionY(structure) + structure.selections.size() * SELECTION_HEIGHT + 20;
        String[] notes = structure.mRecipe.getNeiDesc();
        if (notes != null) for (String note : notes)
            height += Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(note, 156)
                .size() * 10;
        return Math.max(TST_BottledChaosStructureFrontend.PAGE_HEIGHT, height);
    }

    @Override
    public List<String> handleTooltip(GuiRecipe<?> gui, List<String> tooltip, int recipe) {
        if (arecipes.get(recipe) instanceof StructureRecipe structure) {
            Point mouse = gui.getRecipeMousePosition(recipe);
            int y = selectionY(structure);
            for (Selection selection : structure.selections) {
                if (new Rectangle(7, y, 156, SLIDER_HEIGHT).contains(mouse) && !selection.detail()
                    .isEmpty()) tooltip.add(selection.detail());
                y += SELECTION_HEIGHT;
            }
        }
        return super.handleTooltip(gui, tooltip, recipe);
    }

    @Override
    public boolean mouseClicked(GuiRecipe<?> gui, int button, int recipe) {
        if (button == 0 && arecipes.get(recipe) instanceof StructureRecipe structure) {
            Point mouse = gui.getRecipeMousePosition(recipe);
            for (int row = 0; row < structure.sliders.size(); row++) {
                GuiSlider slider = structure.sliders.get(row);
                if (!contains(slider, mouse)) continue;
                if (dragOwner != null) {
                    GuiSlider previous = dragOwner.sliders.get(dragRow);
                    previous.mouseReleased(0, 0);
                    previous.setValue(dragOwner.selected(dragRow));
                }
                if (slider.mousePressed(Minecraft.getMinecraft(), mouse.x, mouse.y)) {
                    dragOwner = structure;
                    dragRow = row;
                    return true;
                }
            }
        }
        return super.mouseClicked(gui, button, recipe);
    }

    @Override
    public boolean mouseScrolled(GuiRecipe<?> gui, int scroll, int recipe) {
        if (scroll != 0 && dragOwner == null && arecipes.get(recipe) instanceof StructureRecipe structure) {
            Point mouse = gui.getRecipeMousePosition(recipe);
            for (int row = 0; row < structure.sliders.size(); row++) {
                if (contains(structure.sliders.get(row), mouse)) {
                    structure.select(row, structure.selected(row) + Integer.signum(scroll));
                    return true;
                }
            }
        }
        return super.mouseScrolled(gui, scroll, recipe);
    }

    @Override
    public List<CachedDefaultRecipe> getCache() {
        List<CachedDefaultRecipe> source = super.getCache();
        if (sourceCache != source) {
            List<CachedDefaultRecipe> isolated = new ArrayList<>();
            for (CachedDefaultRecipe recipe : source) isolated.add(createCachedRecipe(recipe.mRecipe));
            sourceCache = source;
            structureCache = isolated;
        }
        // GT shares its cache by category. Each viewer must own its animated stacks and transfer snapshot.
        return structureCache;
    }

    @Override
    protected int getDescriptionYOffset() {
        return TST_BottledChaosStructureFrontend.DESCRIPTION_Y + WINDOW_OFFSET.y;
    }

    @Override
    protected CachedDefaultRecipe createCachedRecipe(GTRecipe recipe) {
        return recipe.getMetadata(BottledChaosStructureDisplay.DISPLAY) == null ? super.createCachedRecipe(recipe)
            : new StructureRecipe(recipe);
    }

    /** GTRecipe.copy() drops metadata; animation needs an isolated recipe retaining its display data. */
    private static GTRecipe copyDisplayRecipe(GTRecipe recipe) {
        GTRecipe copy = recipe.copy();
        return GTValues.RA.stdBuilder()
            // Cached animation frames pad the input grid; the builder requires compact material arrays.
            .itemInputsUnsafe(
                Arrays.stream(copy.mInputs)
                    .filter(Objects::nonNull)
                    .toArray(ItemStack[]::new))
            .itemOutputs(copy.mOutputs)
            .metadata(BottledChaosStructureDisplay.DISPLAY, recipe.getMetadata(BottledChaosStructureDisplay.DISPLAY))
            .recipeCategory(recipe.getRecipeCategory())
            .specialValue(recipe.mSpecialValue)
            .duration(1)
            .eut(0)
            .fake()
            .build()
            .orElseThrow(() -> new IllegalArgumentException("Invalid structure display recipe"));
    }

    /** Inputs, output and transfer metadata advance atomically in NEI's snapshot refresh. */
    private final class StructureRecipe extends CachedDefaultRecipe {

        private final BottledChaosStructureDisplay display;
        private final Map<String, Integer> choices = new LinkedHashMap<>();
        private List<Selection> selections = new ArrayList<>();
        private final List<GuiSlider> sliders = new ArrayList<>();
        private final Map<String, Integer> appliedChoices = new LinkedHashMap<>();
        private boolean selectionDirty = true;

        private StructureRecipe(GTRecipe recipe) {
            super(copyDisplayRecipe(recipe));
            display = recipe.getMetadata(BottledChaosStructureDisplay.DISPLAY);
            update();
        }

        private int selected(int row) {
            Selection selection = selections.get(row);
            return Math.floorMod(choices.getOrDefault(selection.key(), 0), selection.count());
        }

        private void select(int row, int value) {
            Selection selection = selections.get(row);
            value = Math.max(0, Math.min(selection.count() - 1, value));
            sliders.get(row)
                .setValue(value);
            if (value == selected(row)) return;
            choices.put(selection.key(), value);
            selectionDirty = true;
        }

        private void update() {
            if (!selectionDirty || dragOwner == this) return;
            // Accumulate all wheel events immediately; resolve only the latest choice at the next NEI snapshot.
            var frame = display.frame(choices);
            if (frame == null) {
                choices.clear();
                choices.putAll(appliedChoices);
                frame = display.frame(choices);
            }
            selectionDirty = false;
            appliedChoices.clear();
            appliedChoices.putAll(choices);
            selections = display.selections(choices);
            sliders.clear();
            int y = selectionY(this);
            for (int row = 0; row < selections.size(); row++) {
                Selection selection = selections.get(row);
                // Same Forge track/handle textures as the structure-preview channel sliders, without a BR dependency.
                GuiSlider slider = new GuiSlider(
                    row,
                    7,
                    y,
                    156,
                    SLIDER_HEIGHT,
                    "",
                    "",
                    0,
                    selection.count() - 1,
                    selected(row),
                    false,
                    false) {

                    @Override
                    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
                        if (dragging) {
                            sliderValue = (mouseX - (xPosition + 4)) / (double) (width - 8);
                            updateSlider();
                        }
                        // Match BlockRenderer's channel handle: 8 px wide, stretched to the 16 px track height.
                        int x = xPosition + (int) (sliderValue * (width - 8));
                        GL11.glColor4f(1, 1, 1, 1);
                        GuiUtils.drawContinuousTexturedBox(x, yPosition, 0, 66, 8, height, 200, 20, 2, 3, 2, 2, 0);
                    }

                    @Override
                    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
                        String label = displayString;
                        displayString = "";
                        GL11.glColor4f(1, 1, 1, 1);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glEnable(GL11.GL_BLEND);
                        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                        super.drawButton(mc, mouseX, mouseY);
                        displayString = label;
                        drawCenteredString(
                            mc.fontRenderer,
                            label,
                            xPosition + width / 2,
                            yPosition - 1 + height - mc.fontRenderer.FONT_HEIGHT,
                            BottledChaosStructureNEIHandler.contains(this, new Point(mouseX, mouseY)) ? 0xFFFFA0
                                : 0xE0E0E0);
                    }
                };
                slider.displayString = selection.text(selected(row));
                sliders.add(slider);
                y += SELECTION_HEIGHT;
            }
            mInputs.clear();
            mOutputs.clear();
            mRecipe.mInputs = new ItemStack[TST_BottledChaosStructureFrontend.INPUT_SLOTS];
            addInputs(frame.fixed(), 0);
            addInputs(frame.variable(), TST_BottledChaosStructureFrontend.REGION_SLOTS);
            addInputs(new ItemStack[] { frame.carrier() }, TST_BottledChaosStructureFrontend.CARRIER_SLOT);
            mRecipe.mOutputs = frame.outputs();
            mRecipe.mSpecialValue = frame.profile();
            mRecipe.setNeiDesc(
                BottledChaosComplexStructures.notes(frame.structure())
                    .toArray(new String[0]));
            for (int i = 0; i < frame.outputs().length; i++) {
                Pos2d output = TST_BottledChaosStructureFrontend.OUTPUT_POSITION.add(i * 18, 0)
                    .add(WINDOW_OFFSET);
                mOutputs.add(
                    new FixedPositionedStack(
                        this,
                        frame.outputs()[i],
                        true,
                        output.x + 1,
                        output.y + 1,
                        PositionedStack.CHANCE_FULL,
                        false,
                        false));
            }
        }

        private void addInputs(ItemStack[] materials, int start) {
            for (int i = 0; i < materials.length; i++) {
                addInput(new ItemStack[] { materials[i] }, start + i);
            }
        }

        private void addInput(ItemStack[] alternatives, int slot) {
            mRecipe.mInputs[slot] = alternatives[0];
            Pos2d position = TST_BottledChaosStructureFrontend.inputPosition(slot)
                .add(WINDOW_OFFSET);
            mInputs.add(
                new FixedPositionedStack(
                    this,
                    alternatives,
                    true,
                    position.x + 1,
                    position.y + 1,
                    PositionedStack.CHANCE_FULL,
                    false,
                    true));
        }
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return getOverlayHandler(gui, recipe) != null || super.hasOverlay(gui, container, recipe);
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        if (!(gui instanceof GuiPatternTerm) && !(gui instanceof GuiPatternTermEx))
            return super.getOverlayHandler(gui, recipe);
        IOverlayHandler delegate = RecipeInfo.getOverlayHandler(gui, RecipeMaps.assemblerRecipes.unlocalizedName);
        if (delegate == null) return super.getOverlayHandler(gui, recipe);
        // Reuse the installed GT processing-pattern importer. Keep the packaging page's own identity and layout.
        return new IOverlayHandler() {

            @Override
            public void overlayRecipe(GuiContainer target, IRecipeHandler handler, int index, boolean maxTransfer) {
                delegate
                    .overlayRecipe(target, BottledChaosStructureNEIHandler.this.transferRecipe(index), 0, maxTransfer);
            }

            @Override
            public int transferRecipe(GuiContainer target, IRecipeHandler handler, int index, int multiplier) {
                return delegate
                    .transferRecipe(target, BottledChaosStructureNEIHandler.this.transferRecipe(index), 0, multiplier);
            }

            @Override
            public boolean canFillCraftingGrid(GuiContainer target, IRecipeHandler handler, int index) {
                return delegate
                    .canFillCraftingGrid(target, BottledChaosStructureNEIHandler.this.transferRecipe(index), 0);
            }

            @Override
            public List<ItemOverlayState> presenceOverlay(GuiContainer target, IRecipeHandler handler, int index) {
                return delegate.presenceOverlay(target, BottledChaosStructureNEIHandler.this.transferRecipe(index), 0);
            }

            @Override
            public boolean requireShiftForOverlayRecipe() {
                // Encoding a processing pattern does not pull physical materials into a crafting grid.
                return false;
            }
        };
    }

    private GTNEIDefaultHandler transferRecipe(int index) {
        var cached = (CachedDefaultRecipe) arecipes.get(index);
        List<PositionedStack> inputs = new ArrayList<>();
        for (PositionedStack input : cached.mInputs)
            if (input.item.stackSize > 0) inputs.add(new PositionedStack(input.item.copy(), input.relx, input.rely));
        List<PositionedStack> outputs = new ArrayList<>();
        for (PositionedStack output : cached.mOutputs) {
            outputs.add(
                new PositionedStack(
                    ItemBottledChaosMachineModule.copyForPattern(output.item),
                    output.relx,
                    output.rely));
        }
        return new GTNEIDefaultHandler(RecipeMaps.assemblerRecipes.getDefaultRecipeCategory()) {

            @Override
            public PositionedStack getResultStack(int recipe) {
                return null;
            }

            @Override
            public List<PositionedStack> getIngredientStacks(int recipe) {
                return inputs;
            }

            @Override
            public List<PositionedStack> getOtherStacks(int recipe) {
                return outputs;
            }
        };
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (ItemBottledChaosMachineModule.isModule(result)) loadModule(result);
    }

    @Override
    public void loadUsageRecipes(ItemStack input) {
        if (GTCMItemList.BottledChaos.hasBeenSet()
            && GTUtility.areStacksEqual(input, GTCMItemList.BottledChaos.get(1), true)) arecipes.addAll(getCache());
        else if (ItemBottledChaosMachineModule.isModule(input)) loadModule(input);
        else for (CachedDefaultRecipe recipe : getCache()) {
            if (recipe instanceof StructureRecipe structure && structure.display.hasController(input)) {
                arecipes.add(recipe);
                continue;
            }
            for (ItemStack output : recipe.mRecipe.mOutputs) {
                ItemStack controller = ItemBottledChaosMachineModule.controller(output);
                if (controller != null && controller.isItemEqual(input)) {
                    arecipes.add(recipe);
                    break;
                }
            }
        }
    }

    private void loadModule(ItemStack module) {
        String key = ItemBottledChaosMachineModule.machineKey(module);
        for (CachedDefaultRecipe recipe : getCache()) {
            if (key.isEmpty() || recipe instanceof StructureRecipe structure && structure.display.hasMachine(key)
                || hasMachine(recipe.mRecipe.mInputs, key)
                || hasMachine(recipe.mRecipe.mOutputs, key)) arecipes.add(recipe);
        }
    }

    private static boolean hasMachine(ItemStack[] stacks, String key) {
        for (ItemStack stack : stacks) {
            if (stack == null) continue;
            if (key.equals(stack.getUnlocalizedName()) || key.equals(ItemBottledChaosMachineModule.machineKey(stack)))
                return true;
        }
        return false;
    }

    @Override
    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
        return getUsageHandler(inputId, ingredients);
    }
}
