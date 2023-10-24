/**
 * Copyright (C) 2023 abcAFCR
 */

package com.GTNH_Community.gtnhcommunitymod.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public class LanguageUtil {

    private static Map<String, String> zhCN;
    private static Map<String, String> currentLang;
    private static List<String> itemNameKey;
    private static List<String> blockNameKey;
    private static Map<String, String> nameKey2DescriptionKey;

    static {
        init();
        System.out.println("abcAFCR:LanguageUtil init completed");
    }

    public static Map<String, String> getZhCN() {
        return zhCN;
    }

    public static Map<String, String> getCurrentLang() {
        return currentLang;
    }

    public static List<String> getItemNameKey() {
        return itemNameKey;
    }

    public static List<String> getBlockNameKey() {
        return blockNameKey;
    }

    public static String getCurLangDescription(String nameKey) {
        String descriptionKey = nameKey2DescriptionKey.get(nameKey);
        String description = currentLang.get(descriptionKey);
        return description == null ? zhCN.get(descriptionKey) : description;
    }

    public static String getCurLangItemName(String nameKey) {
        String name = currentLang.get(nameKey);
        return name == null ? zhCN.get(nameKey) : name;
    }

    private static Map<String, String> parseLangFile() {
        return parseLangFile("zh_CN");
    }

    public static Map<String, String> parseLangFile(String currentLangCode) {
        return parseLangFile("/assets/gtnhcommunitymod/lang/", currentLangCode);
    }

    public static Map<String, String> parseLangFile(String langPath, String currentLangCode) {
        String fullLangPath = langPath + currentLangCode + ".lang";
        List<String> langList = getLangList(fullLangPath);
        if (langList == null) return null;

        Map<String, String> map = new HashMap<String, String>();
        Splitter equalSignSplitter = Splitter.on('=')
            .limit(2);

        for (String s : langList) {
            // s的5种情况，前缀#的注释，\n的空字符串""，正常键值对[item.gold.name=gold],错误键值对[a=][=a],任意字符串[oojoew]
            // 剔除[前缀#的注释]与[\n的空字符串""]两种字符串类型
            if (!s.isEmpty() && s.charAt(0) != 35) {
                String[] sArr = Iterables.toArray(equalSignSplitter.split(s), String.class);

                // 剔除[错误键值对[a=][=a]]和[任意字符串[oojoew]]两种字符串类型
                if (sArr != null && sArr.length == 2 && !"".equals(sArr[0]) && !"".equals(sArr[1])) {
                    map.put(sArr[0], sArr[1]);
                }
            }
        }

        return map;
    }

    private static List<String> getLangList(String fullLangPath) {
        InputStream langIS = LanguageUtil.class.getResourceAsStream(fullLangPath);
        if (langIS == null) return null;

        List<String> list = null;
        try {
            list = IOUtils.readLines(langIS, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                langIS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private static void init() {
        zhCN = parseLangFile();
        String currentLangCode = Minecraft.getMinecraft()
            .getLanguageManager()
            .getCurrentLanguage()
            .getLanguageCode();
        currentLang = parseLangFile(currentLangCode);
        itemNameKey = new ArrayList<String>();
        blockNameKey = new ArrayList<String>();
        nameKey2DescriptionKey = new HashMap<String, String>();

        for (String s : zhCN.keySet()) {

            if (s.startsWith("item.")) {
                itemNameKey.add(s);
            } else if (s.startsWith("tile.")) {
                blockNameKey.add(s);
            } else if (s.startsWith("noteBook.") && s.endsWith(".description")) {
                String name = s.substring(s.indexOf(".") + 1, s.lastIndexOf(".")) + ".name";
                nameKey2DescriptionKey.put(name, s);
            }

        }
    }

}
