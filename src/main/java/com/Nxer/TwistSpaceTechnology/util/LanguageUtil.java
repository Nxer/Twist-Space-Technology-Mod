package com.Nxer.TwistSpaceTechnology.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.client.resources.I18n;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * Used to manipulate language file resources
 * LanguageUtil by abcAFCR
 */
public class LanguageUtil {

    private static final Logger LOGGER = LogManager.getLogger("abcAFCR LanguageUtil logger");

    // a language map of "reveal all the details",
    // ensure language integrity when I18n cannot get value of resources correctly
    private final Map<String, String> safetyLangMap;

    /**
     * Construct a LanguageUtil object with language map of "reveal all the details"
     *
     * @param fullSafetyLangPath full language file resource path, for example "/assets/minecraft/lang/en_US.lang"
     */
    public LanguageUtil(String fullSafetyLangPath) {
        this.safetyLangMap = parseLangFile(fullSafetyLangPath);
    }

    /**
     * Construct a LanguageUtil object with language map of "reveal all the details"
     *
     * @param safetyLangPath for example "/assets/minecraft/lang/"
     * @param safetyLangCode for example "zh_CN"
     */
    public LanguageUtil(String safetyLangPath, String safetyLangCode) {
        this.safetyLangMap = parseLangFile(safetyLangPath, safetyLangCode);
    }

    public Map<String, String> getSafetyLangMap() {
        return safetyLangMap;
    }

    public String getLangValue(String key, Object... params) {
        return getLangValue(this.safetyLangMap, key, params);
    }

    /**
     * get the value through I18n, if cannot be obtained correctly, obtain the value through safetyLangMap.
     *
     * @param safetyLangMap a language map of "reveal all the details", ensure language integrity when I18n cannot get
     *                      value of resources correctly
     * @param key           for example "item.gold_ingot.name=Gold Ingot", "item.gold_ingot.name" is the key.
     * @param params        fill Placeholder, for example, "example.text=hello %s!" you can fill key with "example.text"
     *                      and params with "jack", you will get "hello jack!".
     * @return get value through the key and params you pass in.
     */
    public static String getLangValue(Map<String, String> safetyLangMap, String key, Object... params) {
        if (key == null) {
            throw new LanguageUtilException(LOGGER, "key set should not null when you get language info");
        }

        String value = I18n.format(key, params);

        try {
            return key.equals(value) ? String.format(safetyLangMap.get(key), params) : value;
        } catch (IllegalFormatException illegalformatexception) {
            return "Format error: " + safetyLangMap.get(key);
        }
    }

    public static Map<String, String> parseLangFile(String langPath, String langCode) {
        return parseLangFile(langPath + langCode + ".lang");
    }

    /**
     * get a language resource map through fullLangPath.
     *
     * @param fullLangPath full language file resource path, for example "/assets/minecraft/lang/en_US.lang".
     * @return a language resource map.
     */
    public static Map<String, String> parseLangFile(String fullLangPath) {
        List<String> langList = getLangList(fullLangPath);

        if (langList == null) {
            String errorInfo = fullLangPath + " parsing failed! target language resource does not exist!";
            throw new LanguageUtilException(LOGGER, errorInfo);
        }

        Map<String, String> map = new HashMap<>();
        for (String s : langList) {
            // s的5种情况，前缀#的注释，\n的空字符串""，正常键值对[item.gold.name=gold],错误键值对[a=][=a],任意字符串[oojoew]
            // 剔除[前缀#的注释]与[\n的空字符串""]两种字符串类型
            if (!s.isEmpty() && s.charAt(0) != 35) {

                Splitter equalSignSplitter = Splitter.on('=')
                    .limit(2);
                String[] sArr = Iterables.toArray(equalSignSplitter.split(s), String.class);

                // 剔除[错误键值对[a=][=a]]和[任意字符串[oojoew]]两种字符串类型
                if (sArr != null && sArr.length == 2 && !"".equals(sArr[0]) && !"".equals(sArr[1])) {

                    // Pattern that matches numeric variable placeholders in a resource string, such as "%d", "%3$d",
                    // "%.2f"
                    Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
                    String s2 = pattern.matcher(sArr[1])
                        .replaceAll("%$1s");
                    map.put(sArr[0], s2);
                }
            }
        }

        return map;
    }

    private static List<String> getLangList(String fullLangPath) {

        List<String> list = null;

        try (InputStream langIS = LanguageUtil.class.getResourceAsStream(fullLangPath)) {
            if (langIS == null) return null;
            list = IOUtils.readLines(langIS, Charsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static class LanguageUtilException extends RuntimeException {

        public LanguageUtilException(Logger logger, String s) {
            super(s);
            logger.error(s);
        }
    }

}
