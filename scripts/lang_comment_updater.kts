// SPDX-License-Identifier: MIT

/*

A KotlinScript used to add Chinese Translation Structure Comment back to the Java sources.

Author is Taskeren.
Better ask me before you use this.

The input sources are just ones updated to use new Structured Comment Translation Generator from `texter` API.
Since the Chinese translations were hard-coded in the language files, so we need to extract them from the language files
and inject back to the Java sources.

You need to put the file path to `javaFileToMigrate` variable.
The recommended way is to use "Copy Path/Reference > Path from Repository Root" function in the right-click (context) menu of the file, in IDEA.

The output is directly printed into the console, so we don't break files if the script is broken.
You should manually copy the codes from "===[ Code Starts Here ]===" to "===[ Code Ends Here ]===" to the file.

By the way, if you somehow changed some keys in pattern, you can add the key transformers in `getValue` function to replace them.

After that, you'd better to check if there are error lines.
For example, if the English translation line is glitched, the injected Chinese translation line will have no indents,
so you can find by Regex "^ // #" to search them.



这是一个用来把结构化中文翻译文本添加回 Java 源码的 KotlinScript 脚本。

作者：Taskeren
使用前最好先问问我。

作为输入的源代码是那些刚刚从 `texter` API 升级为结构化翻译注释（#tr）的文件，
因为老的 API 的中文内容是直接硬编码在 lang 文件里的，所以我们需要把它读出来，然后插回源代码中。

你需要把需要插入的源代码文件赋值给 `javaFileToMigrate` 变量，推荐使用 IDEA 提供的 “Copy Path/Reference > Path from Repository Root” 功能，在右键那个文件的菜单里。

输出的源代码是直接放在控制台里的，避免因为脚本的错误破坏源文件；你需要手动把这些内容覆盖到那个文件里。

如果你改动了某些翻译的键（key），你可以在 `getValue` 函数里插入转换的方法。

完成之后记得检查一下有没有错误，一个比较明显的问题是，如果英文翻译的那一行没有被正确读取的时候，生成的中文翻译会没有缩进，你可以使用 Regex “^ // #” 来找到它们。

 */

import java.io.File

// better use Copy Path/Reference -> Path from Repository Root
val javaFileToMigrate = File("../src/main/java/com/Nxer/TwistSpaceTechnology/common/item/itemAdders/ItemProofOfHeroes.java")

println("Working Directory: " + File(".").absolutePath)

// the referencing language files
// don't use the one in resources, because the zh_CN.lang will be filled with English value if the value is not present.
val zhLangFile = File("cfc9fbe_zh_CN.lang")
val enLangFile = File("cfc9fbe_en_US.lang")

fun List<String>.linesToLangMap(): Map<String, String> = filter { it.isNotEmpty() }
    .filterIndexed { index, line ->
        val v = '=' in line
        if(!v) {
            println("Skipped line ${index + 1}, cannot find '=' in the line.")
        }
        v
    }
    .associate {
        val (key, value) = it.split("=", limit = 2)
        key to value
    }

val zhLangMap = zhLangFile.readLines().linesToLangMap()
val enLangMap = enLangFile.readLines().linesToLangMap()

if(!javaFileToMigrate.exists()) {
    println("File $javaFileToMigrate does not exist.")
}

fun getValue(key: String): String? {
    var tKey = key

    // MetaItemXXX -> item.MetaItemXXX
    if(tKey.startsWith("item.MetaItem")) {
        tKey = tKey.removePrefix("item.")
    }

    return zhLangMap[tKey]
}

enum class Color(val value: Char, val isFancyStyling: Boolean = false) {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    OBFUSCATED('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r'),
    ;
}

val styleRegex = Regex("§([0-9a-fk-or])")

/**
 * Turn ALL texts like "§0" to "{\BLACK}" in this String.
 */
fun String.revertStyleCodes() = replace(styleRegex) { result ->
    val styleValue = result.groupValues[1]
    val colorName = Color.entries.first { it.value == styleValue[0] }.name
    "{\\$colorName}"
}

val trKeyRegex = Regex("\\s*//\\s*#tr ([^\\s]+)")
val enValRegex = Regex("\\s*//\\s*#\\s+.*")
val zhValRegex = Regex("\\s*//\\s*#zh_CN\\s+.*")

val indentRegex = Regex("(\\s*).*")

// read the source codes
val javaFileLines = javaFileToMigrate.readLines().toMutableList()

fun injectChineseTranslations(javaFileLines: MutableList<String>, errorMessages: MutableList<String>) {
    var index = 0
    while(index < javaFileLines.size) {
        val line = javaFileLines[index]

        val keyMatch = trKeyRegex.matchEntire(line)
        if(keyMatch == null) {
            index++
            continue
        }

        val key = keyMatch.groupValues[1]
        println("[OK] Matched key $key")

        val zhValue = getValue(key)?.revertStyleCodes()

        if(zhValue == null) {
            errorMessages += "[Value Missing] Key '$key' doesn't exist in cfc9fbe_zh_CN.lang (en_US=${enLangMap[key]})"
            index++
            continue
        }

        val nextLine = javaFileLines.getOrNull(index + 1)
        if(nextLine != null) {
            if(nextLine.matches(enValRegex)) {
                index++ // skip if the next line is the english value

                // there is possible that have a Chinese translation below
                val nextNextLine = javaFileLines.getOrNull(index + 1)
                if(nextNextLine?.matches(zhValRegex) == true) {
                    // gotcha! skip it.
                    index++
                    continue
                }
            } else if(nextLine.matches(zhValRegex)) {
                // there is small possible that a Chinese translation is directly attached below the #tr key.
                index++
                continue
            }
        }

        val indent = nextLine?.let {
            val indentMatch = indentRegex.matchEntire(it)
            indentMatch?.groupValues?.get(1)
        } ?: ""

        // insert the zh translation into the code
        javaFileLines.add(index + 1, "$indent// #zh_CN $zhValue")

        index++
    }
}

// call the magic function
val errorMessages = mutableListOf<String>()
injectChineseTranslations(javaFileLines, errorMessages)

// output
println("===[ Code Starts Here ]===")
println(javaFileLines.joinToString(separator = "\n"))
println("===[ Code Ends Here ]===")

if(errorMessages.isNotEmpty()) {
    errorMessages.forEach { println(it) }
    println("${errorMessages.size} error occurred, see above.")
}
