package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import org.jetbrains.annotations.Nullable;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

/** Conditions of a reference cycle, rather than consumable recipe ingredients. */
public final class PrimordialDisjunctusCycleKey extends RecipeMetadataKey<PrimordialDisjunctusCycleKey.Cycle> {

    public static final PrimordialDisjunctusCycleKey INSTANCE = new PrimordialDisjunctusCycleKey();

    private PrimordialDisjunctusCycleKey() {
        super(Cycle.class, "tst_primordial_disjunctus_cycle");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {}

    @Desugar
    public record Cycle(int cellTier, int voltageTier, int parallel, int perditioVis, int boost, int overclockTicks) {

        public int displayOrder() {
            return voltageTier * 8 + cellTier * 2 + (perditioVis > 0 ? 1 : 0);
        }
    }
}
