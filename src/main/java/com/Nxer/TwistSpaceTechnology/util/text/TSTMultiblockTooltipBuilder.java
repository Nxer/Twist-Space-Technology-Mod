package com.Nxer.TwistSpaceTechnology.util.text;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.GTMod;
import gregtech.api.util.MultiblockTooltipBuilder;

/**
 * Backports the current GregTech structure-tooltip footer while TST still targets an older GregTech API.
 * This keeps the fix local and allows the compatibility layer to be removed once the dependency catches up.
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
