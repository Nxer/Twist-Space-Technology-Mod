package com.Nxer.TwistSpaceTechnology.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.item.ItemBottledChaosMachineModule;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosComplexStructures;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureDisplay;
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

    private int structureCycleTicks;
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
    public void onUpdate() {
        super.onUpdate();
        if (!structureCyclePaused()) structureCycleTicks++;
    }

    private boolean structureCyclePaused() {
        if (GuiScreen.isShiftKeyDown()) return true;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiRecipe<?>gui && gui.getHandler() == this) {
            for (int index : gui.getRecipeIndices()) {
                for (PositionedStack input : getIngredientStacks(index)) if (gui.isMouseOver(input, index)) return true;
            }
        }
        return false;
    }

    @Override
    public List<PositionedStack> getIngredientStacks(int recipe) {
        // NEI snapshots inputs and outputs together when refreshing its recipe widget. Drawing is too late:
        // the widget can keep an older snapshot while a hovered slot or overlay button pauses its cycle.
        if (arecipes.get(recipe) instanceof StructureRecipe structure) structure.update(structureCycleTicks);
        return super.getIngredientStacks(recipe);
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

    /** Inputs and output advance together once per two-second structure frame. */
    private final class StructureRecipe extends CachedDefaultRecipe {

        private final BottledChaosStructureDisplay display;
        private BottledChaosStructureDisplay.Frame currentFrame;
        private int currentCycle = -1;
        private int lastCycleTick;

        private StructureRecipe(GTRecipe recipe) {
            // Never mutate the map's fake recipe while animating the client cache.
            super(copyDisplayRecipe(recipe));
            display = recipe.getMetadata(BottledChaosStructureDisplay.DISPLAY);
            update(0);
        }

        private void update(int tick) {
            if (currentCycle >= 0 && tick - lastCycleTick < 40) return;
            // A hovered overlay can delay NEI's refresh. Resume with the next complete frame, without skipping ahead.
            lastCycleTick = tick;
            var frame = display.frame(++currentCycle);
            if (frame == currentFrame) return;
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
            currentFrame = frame;
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
