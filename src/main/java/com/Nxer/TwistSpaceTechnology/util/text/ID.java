package com.Nxer.TwistSpaceTechnology.util.text;

import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;

/** Minecraft IDs used for author, maintainer, and structure-designer tooltip credits. */
public final class ID implements Supplier<String> {

    private static final Style DEFAULT_STYLE = Style.of(EnumChatFormatting.WHITE);
    private static final String NXER_DECORATION = EnumChatFormatting.BOLD.toString() + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE;
    private static final EnumChatFormatting[] NXER_COLORS = { EnumChatFormatting.RED, EnumChatFormatting.GREEN,
        EnumChatFormatting.AQUA, EnumChatFormatting.BLUE };

    // spotless:off
    // Plain IDs use the common white credit style.
    public static final ID REOBF = new ID("reobf");
    public static final ID SHORDINGER = new ID("shordinger");
    public static final ID THE__FLAMES = new ID("The__Flames");
    public static final ID KERIILS = new ID("Keriils_");
    public static final ID HOLEFISH = new ID("holefish");
    public static final ID LUO_YANG_YU_LI = new ID("LuoYangYuLi");
    public static final ID RH_NU = new ID("Rh_Nu");
    public static final ID YUE_LENG_M = new ID("YueLeng_M");
    public static final ID XIAO_XING_521 = new ID("xiao_xing521");
    public static final ID SNOW_DREAM = new ID("SnowDream");
    public static final ID KO_TORI_MINAMI = new ID("ko_tori_minami");
    public static final ID TC_TRAVELER = new ID("Tc_traveler");
    public static final ID AEFHMV = new ID("Aefhmv");
    public static final ID ABLAZING = new ID("ablazing");
    public static final ID LONEI = new ID("Lonei");
    public static final ID KISARACHAN = new ID("kisarachan");

    // These IDs retain the formatting used by their original tooltip credits.
    public static final ID NXER = new ID("Nxer", Style.of(ID::formatNxer));
    public static final ID GODERIUM = new ID("Goderium_", Style.of(EnumChatFormatting.WHITE, EnumChatFormatting.BOLD));
    public static final ID TASKEREN = new ID("Taskeren", Style.of(EnumChatFormatting.GOLD));
    public static final ID TOTTO = new ID("Totto", Style.of(EnumChatFormatting.AQUA));
    public static final ID FAOTIK = new ID("Faotik", Style.of(EnumChatFormatting.YELLOW));
    public static final ID EVGEN_WAR_GOLD = new ID("EvgenWarGold", Style.of(ID::formatEvgenWarGold));
    // spotless:on

    private final String minecraftId;
    private final Supplier<String> formattedId;

    private ID(String minecraftId) {
        this(minecraftId, DEFAULT_STYLE);
    }

    /** Example for an animated credit declaration: {@code new ID("Example", Style.INFUSION)}. */
    private ID(String minecraftId, Style style) {
        this.minecraftId = minecraftId;
        this.formattedId = style.apply(minecraftId);
    }

    private static String formatNxer(String id) {
        StringBuilder result = new StringBuilder(id.length() * 6);
        for (int i = 0; i < id.length(); i++) {
            result.append(NXER_COLORS[i % NXER_COLORS.length])
                .append(NXER_DECORATION)
                .append(id.charAt(i));
        }
        return result.toString();
    }

    private static String formatEvgenWarGold(String id) {
        return EnumChatFormatting.RED + id
            .substring(0, 5) + EnumChatFormatting.BLUE + id.substring(5, 8) + EnumChatFormatting.GOLD + id.substring(8);
    }

    @Override
    public String get() {
        return formattedId.get();
    }

    public String getMinecraftId() {
        return minecraftId;
    }

    /** Returns a copy whose display text uses the selected Style; the stored Minecraft ID stays unchanged. */
    public ID withStyle(Style style) {
        return new ID(minecraftId, style);
    }
}
