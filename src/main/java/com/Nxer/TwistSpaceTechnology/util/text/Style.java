package com.Nxer.TwistSpaceTechnology.util.text;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.animatedText;

import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;

/** Reusable text effects. Every target is injected through {@link #apply(String)}. */
public final class Style {

    public static final Style STANDARD = Style.of(EnumChatFormatting.GREEN);
    public static final Style MODULARIZED = new Style(Style::modularized);
    public static final Style DYSON_SPHERE = new Style(Style::dysonSphere);
    public static final Style INFUSION = new Style(Style::infusion);
    public static final Style RAINBOW = new Style(Style::rainbow);

    private static final char[] INFUSION_GLYPHS = "@#$%&?0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final EnumChatFormatting[] RAINBOW_COLORS = { EnumChatFormatting.RED, EnumChatFormatting.GOLD,
        EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.BLUE,
        EnumChatFormatting.LIGHT_PURPLE };

    private final TextRenderer textRenderer;

    private Style(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    /** Creates a static text style from one or more Minecraft formatting codes. */
    public static Style of(EnumChatFormatting... formatting) {
        StringBuilder prefixBuilder = new StringBuilder(formatting.length * 2);
        for (EnumChatFormatting format : formatting) {
            prefixBuilder.append(format);
        }
        String prefix = prefixBuilder.toString();
        return of(text -> prefix + text);
    }

    /** Creates a text style for formatting that needs to inspect or split the original text. */
    public static Style of(TextFormatter formatter) {
        return new Style(text -> () -> formatter.format(text));
    }

    /**
     * Applies only this style's text effect, without adding category markers or the "Added by" wrapper.
     * Animated styles must be evaluated every frame: pass the supplier to AnimatedTooltipHandler, or call
     * {@link Supplier#get()} while rebuilding a normal tooltip list.
     *
     * <pre>
     * addItemTooltip(stack, Style.INFUSION.apply("Arcane text"));
     * tooltips.add(
     *     Style.INFUSION.apply("Arcane text")
     *         .get());
     * GTAuthors.buildAuthorsWithFormatSupplier(Style.INFUSION.apply(ID.NXER.getMinecraftId()));
     * registerTooltipCredits(ID.NXER.withStyle(Style.INFUSION));
     * registerTooltipCredits(ID.NXER.withStyle(Style.of(EnumChatFormatting.LIGHT_PURPLE)));
     * </pre>
     */
    public Supplier<String> apply(String text) {
        return textRenderer.render(text);
    }

    private static Supplier<String> modularized(String text) {
        return animatedText(
            text,
            1,
            180,
            EnumChatFormatting.DARK_GRAY.toString(),
            EnumChatFormatting.DARK_GRAY.toString(),
            EnumChatFormatting.DARK_BLUE.toString(),
            EnumChatFormatting.DARK_BLUE.toString(),
            EnumChatFormatting.BLUE.toString(),
            EnumChatFormatting.DARK_AQUA.toString(),
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE,
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE,
            EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE,
            EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE,
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE,
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE,
            EnumChatFormatting.DARK_AQUA.toString(),
            EnumChatFormatting.BLUE.toString(),
            EnumChatFormatting.DARK_BLUE.toString(),
            EnumChatFormatting.DARK_BLUE.toString(),
            EnumChatFormatting.DARK_GRAY.toString(),
            EnumChatFormatting.DARK_GRAY.toString());
    }

    /** A bright point crosses the name, followed by a whole-line pulse and a pause before the next cycle. */
    private static Supplier<String> dysonSphere(String text) {
        return sweepingText(
            text,
            EnumChatFormatting.DARK_BLUE,
            EnumChatFormatting.BLUE,
            EnumChatFormatting.DARK_AQUA,
            EnumChatFormatting.AQUA,
            2,
            6,
            6,
            animationStep(EnumChatFormatting.BLUE, 150),
            animationStep(EnumChatFormatting.DARK_AQUA, 150),
            animationStep(EnumChatFormatting.AQUA, 150),
            animationStep(EnumChatFormatting.WHITE, 600),
            animationStep(EnumChatFormatting.AQUA, 150),
            animationStep(EnumChatFormatting.DARK_AQUA, 150),
            animationStep(EnumChatFormatting.BLUE, 200),
            animationStep(EnumChatFormatting.DARK_BLUE, 600));
    }

    /**
     * Builds a left-to-right point-and-trail animation, then applies timed colors to the entire text.
     * Frame counts use 100 ms units; {@link AnimationStep} durations are milliseconds.
     */
    private static Supplier<String> sweepingText(String text, EnumChatFormatting baseColor,
        EnumChatFormatting farTrailColor, EnumChatFormatting nearTrailColor, EnumChatFormatting pointColor,
        int trailExitFrames, int preEffectPauseFrames, int postEffectPauseFrames, AnimationStep... effectSteps) {
        final int sweepFrames = visibleCharacterCount(text) + trailExitFrames;
        final long sweepDuration = sweepFrames * 100L;
        final long effectStart = (sweepFrames + preEffectPauseFrames) * 100L;
        long effectDuration = 0;
        for (AnimationStep effectStep : effectSteps) {
            effectDuration += effectStep.durationMillis;
        }
        final long effectEnd = effectStart + effectDuration;
        final long cycleDuration = effectEnd + postEffectPauseFrames * 100L;
        return () -> {
            long timeline = Math.floorMod(System.currentTimeMillis(), cycleDuration);
            int pointIndex = timeline < sweepDuration ? (int) (timeline / 100L) : -1;
            EnumChatFormatting wholeTextColor = baseColor;
            boolean colorWholeText = false;

            if (timeline >= effectStart && timeline < effectEnd) {
                long effectTimeline = timeline - effectStart;
                for (AnimationStep effectStep : effectSteps) {
                    if (effectTimeline < effectStep.durationMillis) {
                        wholeTextColor = effectStep.color;
                        break;
                    }
                    effectTimeline -= effectStep.durationMillis;
                }
                colorWholeText = true;
            }

            StringBuilder result = new StringBuilder(text.length() * 4);
            int visibleIndex = -1;
            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);
                if (character == ' ') {
                    result.append(EnumChatFormatting.RESET)
                        .append(character);
                    continue;
                }
                visibleIndex++;

                int distanceBehindPoint = pointIndex - visibleIndex;
                if (colorWholeText) {
                    result.append(wholeTextColor);
                } else if (distanceBehindPoint == 0) {
                    result.append(pointColor);
                } else if (distanceBehindPoint == 1) {
                    result.append(nearTrailColor);
                } else if (distanceBehindPoint == 2) {
                    result.append(farTrailColor);
                } else {
                    result.append(baseColor);
                }
                result.append(character);
            }
            return result.toString();
        };
    }

    /** Adds sparse, deterministic glyph corruption and dark flashes to the purple infusion style. */
    private static Supplier<String> infusion(String text) {
        return () -> {
            long frame = System.currentTimeMillis() / 220L;
            StringBuilder result = new StringBuilder(text.length() * 3);
            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);
                if (character == ' ') {
                    result.append(EnumChatFormatting.RESET)
                        .append(character);
                    continue;
                }

                long animationValue = animationValue(frame, i, 0x713BF1A92C4D86E5L);
                int state = (int) Math.floorMod(animationValue, 24L);
                if (state <= 1) {
                    result.append(EnumChatFormatting.DARK_PURPLE)
                        .append(EnumChatFormatting.ITALIC);
                    character = INFUSION_GLYPHS[(int) Math.floorMod(animationValue >>> 8, INFUSION_GLYPHS.length)];
                } else if (state == 2) {
                    result.append(EnumChatFormatting.BLACK);
                } else if (state == 3) {
                    result.append(EnumChatFormatting.DARK_RED)
                        .append(EnumChatFormatting.ITALIC);
                } else if (state == 4) {
                    result.append(EnumChatFormatting.DARK_GRAY)
                        .append(EnumChatFormatting.STRIKETHROUGH);
                } else if (state <= 7) {
                    result.append(EnumChatFormatting.LIGHT_PURPLE)
                        .append(EnumChatFormatting.ITALIC);
                } else if (state <= 11) {
                    result.append(EnumChatFormatting.DARK_PURPLE)
                        .append(EnumChatFormatting.ITALIC);
                } else {
                    result.append(EnumChatFormatting.DARK_PURPLE);
                }
                result.append(character);
            }
            return result.toString();
        };
    }

    /** Shifts a per-character rainbow every 200 ms, leaving whitespace uncolored. */
    private static Supplier<String> rainbow(String text) {
        return () -> {
            if (text.isEmpty()) return text;

            int colorOffset = (int) ((System.currentTimeMillis() / 200L) % RAINBOW_COLORS.length);
            StringBuilder result = new StringBuilder(text.length() * 3);
            int coloredCharacterIndex = 0;
            for (int index = 0; index < text.length(); index++) {
                char character = text.charAt(index);
                if (Character.isWhitespace(character)) {
                    result.append(character);
                    continue;
                }
                result.append(RAINBOW_COLORS[(colorOffset + coloredCharacterIndex) % RAINBOW_COLORS.length])
                    .append(character);
                coloredCharacterIndex++;
            }
            return result.append(EnumChatFormatting.RESET)
                .toString();
        };
    }

    private static AnimationStep animationStep(EnumChatFormatting color, int durationMillis) {
        return new AnimationStep(color, durationMillis);
    }

    private static int visibleCharacterCount(String text) {
        int result = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ') result++;
        }
        return result;
    }

    /** Stable per-frame/per-character mixing keeps the distortion animated without mutable random state. */
    private static long animationValue(long frame, int characterIndex, long salt) {
        long value = frame + salt + characterIndex * 0x9E3779B97F4A7C15L;
        value = (value ^ (value >>> 30)) * 0xBF58476D1CE4E5B9L;
        value = (value ^ (value >>> 27)) * 0x94D049BB133111EBL;
        return value ^ (value >>> 31);
    }

    @FunctionalInterface
    private interface TextRenderer {

        Supplier<String> render(String text);
    }

    @FunctionalInterface
    public interface TextFormatter {

        String format(String text);
    }

    private static final class AnimationStep {

        private final EnumChatFormatting color;
        private final int durationMillis;

        private AnimationStep(EnumChatFormatting color, int durationMillis) {
            this.color = color;
            this.durationMillis = durationMillis;
        }
    }
}
