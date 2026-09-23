package com.Nxer.TwistSpaceTechnology.util.text;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.NEW_LINE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.chain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.Nxer.TwistSpaceTechnology.Tags;

import gregtech.api.enums.GTAuthors;

public interface TSTTooltipCredit {

    /** Display order is fixed by this declaration: author, maintainer, structure designer, then artist. */
    enum Role {
        AUTHOR,
        MAINTAINER,
        STRUCTURE,
        ART
    }

    /** Footer tags are independent from text effects and render in the order returned by the machine. */
    enum Tag {

        DYSON_SPHERE(() -> TSTSharedLocalization.DysonSphere.DSPName),
        MODULARIZED(() -> TSTSharedLocalization.ModularizedMachine.ModularizedMachineSystem);

        private final Supplier<String> text;

        Tag(Supplier<String> text) {
            this.text = text;
        }

        public Supplier<String> getText() {
            return text;
        }
    }

    ItemStack getStackForm(long amount);

    default Style getTooltipCreditStyle() {
        return Style.STANDARD;
    }

    default Tag[] getTooltipCreditTags() {
        return new Tag[0];
    }

    default void registerTooltipCredits(Object... credits) {
        registerTooltipCredits(getTooltipCreditStyle(), credits);
    }

    default void registerTooltipCredits(ID[] authors) {
        registerTooltipCredits(getTooltipCreditStyle(), (Object[]) authors);
    }

    /**
     * Parses entries from left to right. IDs are authors by default; a {@link Role} changes the role of following
     * IDs or ID arrays until another role appears.
     */
    default void registerTooltipCredits(Style style, Object... credits) {
        EnumMap<Role, List<ID>> creditsByRole = new EnumMap<>(Role.class);
        Role currentRole = Role.AUTHOR;
        for (Object credit : credits) {
            if (credit instanceof Role) {
                currentRole = (Role) credit;
            } else if (credit instanceof ID) {
                creditsByRole.computeIfAbsent(currentRole, ignored -> new ArrayList<>())
                    .add((ID) credit);
            } else if (credit instanceof ID[]) {
                Collections
                    .addAll(creditsByRole.computeIfAbsent(currentRole, ignored -> new ArrayList<>()), (ID[]) credit);
            } else {
                throw new IllegalArgumentException("Unsupported tooltip credit entry: " + credit);
            }
        }

        Supplier<String> tooltip = null;
        for (Role creditRole : Role.values()) {
            List<ID> ids = creditsByRole.get(creditRole);
            if (ids == null || ids.isEmpty()) continue;
            Supplier<String> line = buildCreditLine(creditRole, ids.toArray(new ID[0]));
            tooltip = tooltip == null ? line : chain(tooltip, NEW_LINE, line);
        }

        if (tooltip == null) {
            throw new IllegalArgumentException("At least one tooltip credit ID is required");
        }

        for (Tag tag : getTooltipCreditTags()) {
            tooltip = chain(tooltip, NEW_LINE, tag.getText());
        }
        addItemTooltip(getStackForm(1), chain(tooltip, NEW_LINE, buildModNameTooltip(style)));
    }

    static Supplier<String> buildModNameTooltip(Style style) {
        Supplier<String> styledModName = style.apply(Tags.MODNAME);
        return () -> "" + EnumChatFormatting.RESET
            + EnumChatFormatting.GRAY
            + StatCollector.translateToLocalFormatted(
                "tst.common.shared.credit.mod_name",
                styledModName.get() + EnumChatFormatting.RESET + EnumChatFormatting.GRAY)
            + EnumChatFormatting.RESET;
    }

    static Supplier<String> buildCreditLine(Role role, ID... ids) {
        Supplier<String> creditLine;
        if (role == Role.AUTHOR) {
            creditLine = GTAuthors.buildAuthorsWithFormatSupplier(ids);
        } else {
            String translationKey = (role == Role.MAINTAINER ? "tst.common.shared.credit.maintainer"
                : role == Role.STRUCTURE ? "tst.common.shared.credit.structure" : "tst.common.shared.credit.art")
                + (ids.length == 1 ? "" : "s");
            creditLine = () -> StatCollector.translateToLocalFormatted(
                translationKey,
                GTAuthors.formatAuthors(
                    Arrays.stream(ids)
                        .map(Supplier::get)
                        .toArray(String[]::new)));
        }
        return () -> "" + EnumChatFormatting.RESET
            + EnumChatFormatting.GRAY
            + creditLine.get()
            + EnumChatFormatting.RESET;
    }

    // #tr tst.common.shared.credit.maintainer
    // # Maintainer: %s
    // #zh_CN 维护：%s

    // #tr tst.common.shared.credit.maintainers
    // # Maintainers: %s
    // #zh_CN 维护：%s

    // #tr tst.common.shared.credit.structure
    // # Structure: %s
    // #zh_CN 结构：%s

    // #tr tst.common.shared.credit.structures
    // # Structures: %s
    // #zh_CN 结构：%s

    // #tr tst.common.shared.credit.art
    // # Artist %s
    // #zh_CN 美术 %s

    // #tr tst.common.shared.credit.arts
    // # Artists %s
    // #zh_CN 美术 %s
}
