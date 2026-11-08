package com.Nxer.TwistSpaceTechnology.util.text;

import java.util.function.ToIntFunction;

/** Font-width-based single-line text fitting, with no client-only dependencies. */
public final class TSTTextLayout {

    public static String ellipsize(String text, int width, ToIntFunction<String> measure) {
        text = text.replace('\n', ' ')
            .replace('\r', ' ');
        if (measure.applyAsInt(text) <= width) return text;
        if (measure.applyAsInt("...") > width) return "";
        int end = 0;
        while (end < text.length()) {
            int next = text.charAt(end) == '\u00a7' && end + 1 < text.length() ? end + 2
                : text.offsetByCodePoints(end, 1);
            if (measure.applyAsInt(text.substring(0, next) + "...") > width) break;
            end = next;
        }
        return text.substring(0, end) + "...";
    }
}
