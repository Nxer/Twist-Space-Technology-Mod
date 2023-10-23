package com.GTNH_Community.gtnhcommunitymod.util;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Map;

public class TextHandler {

    public static Map<String, String> LangMap;

    public String texter(String aKey, String aTextLine) {

        if (LangMap.get(aKey) == null) {
            LangMap.put(aKey, aTextLine);
            return aTextLine;
        } else {
            return translateToLocalFormatted(aKey);
        }
    }

    public static void initLangMap() {
        LangMap = LanguageUtil.parseLangFile("en_US");
    }

    public void serializeLangMap() {

    }

}
