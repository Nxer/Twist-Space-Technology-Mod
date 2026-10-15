package com.Nxer.TwistSpaceTechnology.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Updates only TST's block and additions to existing GT groups. */
final class NEICollapsibleConfig {

    private static final String BEGIN = "# BEGIN TST COLLAPSIBLE ITEMS";
    private static final String END = "# END TST COLLAPSIBLE ITEMS";

    static String update(String content, Map<String, String> fallbackGroups, Map<String, String> groups,
        Map<String, List<String>> mergedGroups) {
        String newline = content.contains("\r\n") ? "\r\n" : "\n";
        List<String> lines = new ArrayList<>(Arrays.asList(content.split("\\r\\n|\\n|\\r", -1)));
        int begin = lines.indexOf(BEGIN);
        int end = lines.indexOf(END);
        if ((begin == -1) != (end == -1) || begin > end) {
            throw new IllegalArgumentException("Incomplete TST collapsible items block");
        }

        for (Map.Entry<String, List<String>> entry : mergedGroups.entrySet()) {
            if (entry.getValue()
                .isEmpty()) continue;
            int ruleIndex = findRule(lines, entry.getKey());
            if (ruleIndex == -1) {
                String fallback = fallbackGroups.get(entry.getKey());
                if (fallback == null || fallback.isEmpty()) {
                    throw new IllegalArgumentException("Missing NEI default group: " + entry.getKey());
                }
                // A custom config may omit this GT group. Restore its default rule as well as TST's members.
                lines.add(entry.getKey());
                lines.add(fallback);
                ruleIndex = lines.size() - 1;
            }
            String line = lines.get(ruleIndex);
            int commentIndex = line.indexOf('#');
            String rule = (commentIndex == -1 ? line : line.substring(0, commentIndex)).trim();
            List<String> branches = Arrays.asList(rule.split("\\s*\\|\\s*"));
            for (String selector : entry.getValue()) {
                if (!branches.contains(selector)) rule += " | " + selector;
            }
            lines.set(ruleIndex, rule + (commentIndex == -1 ? "" : " " + line.substring(commentIndex)));
        }

        List<String> block = new ArrayList<>();
        block.add(BEGIN);
        for (Map.Entry<String, String> entry : groups.entrySet()) {
            if (entry.getValue()
                .isEmpty()) continue;
            block.add("# TST " + entry.getKey());
            block.add(entry.getValue());
        }
        block.add(END);

        begin = lines.indexOf(BEGIN);
        if (begin != -1) {
            lines.subList(begin, lines.indexOf(END) + 1)
                .clear();
            lines.addAll(begin, block);
        } else {
            if (!lines.get(lines.size() - 1)
                .isEmpty()) lines.add("");
            lines.addAll(lines.size() - 1, block);
        }
        return String.join(newline, lines);
    }

    private static int findRule(List<String> lines, String heading) {
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i)
                .trim()
                .equalsIgnoreCase(heading)) continue;
            for (int j = i + 1; j < lines.size(); j++) {
                String line = lines.get(j)
                    .trim();
                if (line.isEmpty() || line.startsWith(";")) continue;
                if (line.startsWith("#")) break;
                return j;
            }
        }
        return -1;
    }
}
