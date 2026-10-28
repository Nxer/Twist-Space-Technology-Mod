package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

/** Break descriptions at semicolons, then hard-wrap long clauses by rendered width. */
final class BottledChaosTextLayout {

    static List<String> wrap(String text, int width, ToIntFunction<String> measure) {
        List<String> lines = new ArrayList<>();
        for (String paragraph : text.replace("; ", "\n")
            .replace(';', '\n')
            .split("\n", -1)) {
            if (paragraph.isEmpty()) {
                lines.add("");
                continue;
            }
            int start = 0;
            while (start < paragraph.length()) {
                int end = start;
                while (end < paragraph.length()) {
                    int next = paragraph.offsetByCodePoints(end, 1);
                    if (end > start && measure.applyAsInt(paragraph.substring(start, next)) > Math.max(1, width)) break;
                    end = next;
                }
                lines.add(paragraph.substring(start, end));
                start = end;
            }
        }
        return lines;
    }
}
