package com.Nxer.TwistSpaceTechnology.util.text;

import java.util.Objects;
import java.util.StringJoiner;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.GTMod;
import gregtech.api.util.MultiblockTooltipBuilder;

/**
 * Adds dimensions derived from structure strings and backports the current GregTech structure-tooltip footer.
 */
public class TSTMultiblockTooltipBuilder extends MultiblockTooltipBuilder {

    private static final String TAB = "   ";
    private static final String OLD_PROJECTOR_TOOLTIP = EnumChatFormatting.WHITE
        + StatCollector.translateToLocal("GT5U.MBTT.Structure.SeeStructure1")
        + EnumChatFormatting.BLUE
        + " Structure"
        + EnumChatFormatting.DARK_BLUE
        + "Lib "
        + EnumChatFormatting.RESET
        + EnumChatFormatting.WHITE
        + StatCollector.translateToLocal("GT5U.MBTT.Structure.SeeStructure2");

    @Override
    public TSTMultiblockTooltipBuilder addMachineType(String machine) {
        super.addMachineType(machine);
        return this;
    }

    @Override
    public TSTMultiblockTooltipBuilder addInfo(String info) {
        super.addInfo(info);
        return this;
    }

    @Override
    public TSTMultiblockTooltipBuilder addSeparator() {
        super.addSeparator();
        return this;
    }

    @Override
    public TSTMultiblockTooltipBuilder addPollutionAmount(int pollution) {
        super.addPollutionAmount(pollution);
        return this;
    }

    /**
     * Returns width, height and length for a shape arranged as {@code [height][length][width]}, before
     * {@code StructureUtility.transpose}. Spaces, including outer padding, count towards the dimensions.
     */
    public static int[] getStructureDimensions(String[][] shape) {
        Objects.requireNonNull(shape, "shape");
        int width = 0;
        int rows = 0;
        for (String[] layer : shape) {
            Objects.requireNonNull(layer, "shape layer");
            rows = Math.max(rows, layer.length);
            for (String row : layer) {
                width = Math.max(
                    width,
                    Objects.requireNonNull(row, "shape row")
                        .length());
            }
        }
        if (shape.length == 0 || rows == 0 || width == 0) {
            throw new IllegalArgumentException("Structure shape must have positive width, height and length");
        }
        return new int[] { width, shape.length, rows };
    }

    /**
     * Begins a non-hollow structure tooltip. Lists the complete dimensions of up to five alternative shapes
     * in their input order, separated by commas, with one shared axis label.
     * Pass shapes before {@code StructureUtility.transpose}, not separate pieces of one structure.
     */
    public TSTMultiblockTooltipBuilder beginStructureBlock(String[][]... shapes) {
        return beginStructureBlock(false, shapes);
    }

    /**
     * Begins a structure tooltip with an optional hollow tag, using the same shape layout as
     * {@link #getStructureDimensions(String[][])}. Existing numeric GregTech overloads remain available.
     */
    public TSTMultiblockTooltipBuilder beginStructureBlock(boolean hollow, String[][]... shapes) {
        Objects.requireNonNull(shapes, "shapes");
        if (shapes.length == 0 || shapes.length > 5) {
            throw new IllegalArgumentException("Between one and five structure shapes are required");
        }
        if (shapes.length == 1) {
            int[] dimensions = getStructureDimensions(shapes[0]);
            super.beginStructureBlock(dimensions[0], dimensions[1], dimensions[2], hollow);
            return this;
        }
        StringJoiner dimensionGroups = new StringJoiner(EnumChatFormatting.GRAY + ", ");
        for (String[][] shape : shapes) {
            int[] dimensions = getStructureDimensions(shape);
            dimensionGroups.add(
                EnumChatFormatting.GOLD + Integer.toString(dimensions[2])
                    + EnumChatFormatting.GRAY
                    + "x"
                    + EnumChatFormatting.GOLD
                    + dimensions[0]
                    + EnumChatFormatting.GRAY
                    + "x"
                    + EnumChatFormatting.GOLD
                    + dimensions[1]);
        }
        return beginStructureBlockWithDimensions(dimensionGroups.toString(), hollow);
    }

    /**
     * Begins a manually sized structure tooltip. Use LxWxH groups separated by commas, including formulas
     * such as {@code "15x15x(8+4xn)"} for a fixed section plus n repeatable slices.
     */
    public TSTMultiblockTooltipBuilder beginStructureBlock(String dimensions, boolean hollow) {
        Objects.requireNonNull(dimensions, "dimensions");
        if (dimensions.trim()
            .isEmpty() || dimensions.split(",").length > 5) {
            throw new IllegalArgumentException("Between one and five dimension groups are required");
        }
        return beginStructureBlockWithDimensions(
            EnumChatFormatting.GRAY
                + dimensions.replaceAll("([0-9n]+)", EnumChatFormatting.GOLD + "$1" + EnumChatFormatting.GRAY),
            hollow);
    }

    private TSTMultiblockTooltipBuilder beginStructureBlockWithDimensions(String dimensions, boolean hollow) {
        super.addStructureFooter(
            EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.MBTT.Dimensions")
                + ": "
                + dimensions
                + EnumChatFormatting.GRAY
                + " ("
                + EnumChatFormatting.GOLD
                + "L"
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + "W"
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + "H"
                + EnumChatFormatting.GRAY
                + ") "
                + (hollow ? EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.MBTT.Hollow") : ""));
        super.addStructureFooter(
            EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.MBTT.Structure") + ": ");
        return this;
    }

    @Override
    public String[] getStructureInformation() {
        String[] structureInformation = super.getStructureInformation();
        if (structureInformation == null || structureInformation.length == 0) return structureInformation;

        String[] correctedInformation = structureInformation.clone();
        int projectorIndex = correctedInformation.length - 1;
        // Match the complete legacy sentence so unrelated structure lines are never rewritten.
        if (OLD_PROJECTOR_TOOLTIP.equals(correctedInformation[projectorIndex])) {
            correctedInformation[projectorIndex] = StatCollector.translateToLocal("GT5U.MBTT.Structure.Projector");
        }

        int separatorIndex = projectorIndex - 1;
        // Old GT indents the separator as structure text; current GT renders the footer separator at full width.
        if (GTMod.proxy.tooltipFinisherStyle != 0 && separatorIndex >= 0
            && correctedInformation[separatorIndex].startsWith(TAB)) {
            correctedInformation[separatorIndex] = correctedInformation[separatorIndex].substring(TAB.length());
        }
        return correctedInformation;
    }

}
