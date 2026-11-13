package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.machine.ValueEnum;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.PrimordialDisjunctusCycleKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.PrimordialDisjunctusCycleKey.Cycle;
import com.Nxer.TwistSpaceTechnology.util.ItemEssentiaHelper;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.Badge;
import gregtech.api.enums.GTValues;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import thaumcraft.api.aspects.Aspect;

public class TST_PrimordialDisjunctusFrontend extends TST_EssentiaFrontend {

    public TST_PrimordialDisjunctusFrontend(BasicUIPropertiesBuilder ui, NEIRecipePropertiesBuilder nei) {
        super(ui, nei);
    }

    @Override
    protected List<Pos2d> getInputPositions(int count) {
        return singletonList(new Pos2d(75, 56));
    }

    @Override
    protected List<Pos2d> getOutputPositions(int count) {
        List<Pos2d> positions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double angle = -Math.PI / 2 + 2 * Math.PI * i / count;
            positions.add(
                new Pos2d(75 + (int) Math.round(Math.cos(angle) * 40), 56 + (int) Math.round(Math.sin(angle) * 40)));
        }
        return positions;
    }

    @Override
    protected int getAspectsY(GTRecipe recipe) {
        return 156;
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        super.prepareRecipe(recipe);
        recipe.mInputs.get(0)
            .setTooltipProvider(stack -> getInputTooltip(0));
        Cycle cycle = recipe.mRecipe.getMetadata(PrimordialDisjunctusCycleKey.INSTANCE);
        addVis(recipe, Aspect.ORDER, cycle.perditioVis() > 0 ? 200 : 0, 45);
        addVis(recipe, Aspect.ENTROPY, cycle.perditioVis(), 105);
    }

    private static void addVis(GTNEIDefaultHandler.CachedDefaultRecipe recipe, Aspect aspect, int amount, int x) {
        ItemStack vis = ItemEssentiaHelper.createAspectItem(aspect, 1);
        vis.stackSize = amount;
        vis.setItemDamage(1);
        PositionedStack position = new PositionedStack(vis, x, 134, false);
        if (amount == 0) position.setBadges(singletonList(new Badge("0")));
        position.setTooltipProvider(stack -> getInputTooltip(aspect == Aspect.ORDER ? 1 : 2));
        recipe.mInputs.add(position);
    }

    @Override
    public boolean allowsPatternTransfer() {
        return false;
    }

    @Override
    public void drawNEIBackground(GTRecipe recipe) {
        Cycle cycle = recipe.getMetadata(PrimordialDisjunctusCycleKey.INSTANCE);
        GuiDraw.drawStringC(
            GTValues.VN[cycle.voltageTier()] + " | P="
                + cycle.parallel()
                + " | x"
                + (1 + cycle.boost() * ValueEnum.BoostMultiplier_PrimordialDisjunctus),
            83,
            0,
            0x404040,
            false);
        MATRIX.draw(46.375f, 28, 73.5f, 73.5f);
        // #tr tst.nei.primordial.node_vis
        // # Node Vis / 5 ticks (optional)
        // #zh_CN 节点 Vis / 5t (可选)
        GuiDraw.drawStringC(TSTUtils.tr("tst.nei.primordial.node_vis"), 83, 119, 0x404040, false);
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawEnergyInfo(recipeInfo);
        drawDurationInfo(recipeInfo);
        // #tr tst.nei.primordial.rate
        // # Each primal: %s /s
        // #zh_CN 每种原质: %s /秒
        recipeInfo.drawTextMultipleLines(
            Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(
                TSTUtils.tr(
                    "tst.nei.primordial.rate",
                    String.format(
                        Locale.ROOT,
                        "%.2f",
                        20.0 * recipeInfo.recipe.mOutputs[0].stackSize / recipeInfo.recipe.mDuration)),
                160));
        drawRecipeOwnerInfo(recipeInfo);
    }

    private static List<String> getInputTooltip(int index) {
        List<String> tip = new ArrayList<>();
        if (index == 0) {
            // #tr tst.nei.primordial.cells
            // # Structure: four diffusion cells; mixed tiers are supported. These are not consumed.
            // #zh_CN 结构需要4个扩散单元, 可混搭, 不消耗。
            addTooltip(tip, "tst.nei.primordial.cells");
            // #tr tst.nei.primordial.parallel
            // # Higher-tier cells add parallel capacity; power is independent of parallel.
            // #zh_CN 单元按等级累加并行。更高等级可同时提高六种产出, 耗电不乘并行。
            addTooltip(tip, "tst.nei.primordial.parallel");
            // #tr tst.nei.primordial.overclock
            // # Higher voltage speeds production; below 1 second, speed becomes extra output.
            // #zh_CN 更高电压加快生产。周期至少1秒, 进一步加速折算为产量, 直到机器的超频上限。
            addTooltip(tip, "tst.nei.primordial.overclock");
            // #tr tst.nei.primordial.output
            // # All six primals exit together. Excess output is lost when hatches are full.
            // #zh_CN 周期结束时同时输出六种原质; 源质输出仓无法容纳的部分会丢失。
            addTooltip(tip, "tst.nei.primordial.output");
        } else if (index == 1) {
            // #tr tst.nei.primordial.ordo
            // # Optional Ordo node Vis builds purification over time, reducing flux gas.
            // #zh_CN 可选的秩序节点Vis逐渐积累净化效率, 减少咒波排放; 持续供给可达到完全净化。
            addTooltip(tip, "tst.nei.primordial.ordo");
            // #tr tst.nei.primordial.vis_source
            // # Supply node Vis through nearby Vis Relays; shown amounts are per update.
            // #zh_CN 通过控制器附近的源质中继器供给节点Vis。图中为每次更新的参考供给量, 与源质输出网络独立。
            addTooltip(tip, "tst.nei.primordial.vis_source");
        } else if (index == 2) {
            // #tr tst.nei.primordial.perditio
            // # Optional Perditio node Vis increases all six outputs, sampled at cycle start.
            // #zh_CN 可选的混沌节点Vis同时提高六种产出, 每轮开始时按当前供给确定加成。
            addTooltip(tip, "tst.nei.primordial.perditio");
            // #tr tst.nei.primordial.cycle_boost
            // # Pages compare no Perditio supply and full supply at each voltage.
            // #zh_CN 页面给出各电压下无混沌供给与满供给的参考产出, 中间供给量对应中间产量。
            addTooltip(tip, "tst.nei.primordial.cycle_boost");
            addTooltip(tip, "tst.nei.primordial.vis_source");
        }
        return tip;
    }

    private static void addTooltip(List<String> tip, String key, Object... values) {
        tip.addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(TSTUtils.tr(key, values), 280));
    }
}
