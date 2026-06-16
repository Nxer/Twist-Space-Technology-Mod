@file:Suppress("ktlint:standard:no-wildcard-imports")

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import kotlin.time.measureTime

private val COLORS =
    mapOf(
        "BLACK" to "\u00a70",
        "DARK_BLUE" to "\u00a71",
        "DARK_GREEN" to "\u00a72",
        "DARK_AQUA" to "\u00a73",
        "DARK_RED" to "\u00a74",
        "DARK_PURPLE" to "\u00a75",
        "GOLD" to "\u00a76",
        "GRAY" to "\u00a77",
        "DARK_GRAY" to "\u00a78",
        "BLUE" to "\u00a79",
        "GREEN" to "\u00a7a",
        "AQUA" to "\u00a7b",
        "RED" to "\u00a7c",
        "LIGHT_PURPLE" to "\u00a7d",
        "YELLOW" to "\u00a7e",
        "WHITE" to "\u00a7f",
        "OBFUSCATED" to "\u00a7k",
        "BOLD" to "\u00a7l",
        "STRIKETHROUGH" to "\u00a7m",
        "UNDERLINE" to "\u00a7n",
        "ITALIC" to "\u00a7o",
        "RESET" to "\u00a7r",
        "SPACE" to " ",
        "NULL" to "",
    )

abstract class GenerateLanguageTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sources: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun execute() {
        // the language maps associates the language code to their key-value maps
        val languageMaps = mutableMapOf<String, MutableMap<String, String>>()
        // the fallback language, which is US English
        val englishMap = languageMaps.getOrPut("en_US") { mutableMapOf() }
        // traverse the codebase, find the structures and record them to the map
        collectLanguageData(sources, englishMap, languageMaps)
        // display statistics
        logger.info(
            "Collected {} languages: {}",
            languageMaps.size,
            languageMaps.entries.joinToString { (key, value) -> "$key (${value.size} entries)" },
        )
        // collect all keys of translations in the codebase
        val allKeys = languageMaps.values.flatMapTo(mutableSetOf()) { it.keys }

        // check if the key is missing in other languages
        englishMap.forEach { (key, untranslated) ->
            languageMaps.forEach { (languageCode, map) ->
                if (!map.containsKey(key)) {
                    logger.warn("Key {} is missing in language {} (EN: {})", key, languageCode, untranslated)
                }
            }
        }

        // the keys in order in the language file
        val englishKeys = mutableSetOf<String>()
        // load the translations in the language files
        collectLanguageDataLanguageFiles(outputDirectory, englishKeys, languageMaps)

        // warning the keys that only exist in language files
        val keysOnlyInLanguageFiles = englishKeys - allKeys
        keysOnlyInLanguageFiles.forEach { key ->
            logger.warn(
                "Key {} only exists in language files (EN: {}, ZH: {})",
                key,
                languageMaps["en_US"]?.get(key),
                languageMaps["zh_CN"]?.get(key),
            )
        }

        allKeys.removeAll(englishKeys)
        englishKeys.addAll(allKeys)

        writeLanguageFiles(outputDirectory, englishKeys, languageMaps, false, keysOnlyInLanguageFiles, englishMap)
    }

    private sealed interface MatchingState

    private data object Idle : MatchingState

    private data class TrMatched(
        val key: String,
    ) : MatchingState

    // matching "#tr <key> [optional English translation]"
    private val trHeadMatcher = Regex("""//\s*#tr\s+(?<key>[a-zA-Z0-9._]+)\s*(?<data>.+)?\s*""")

    // matching "# [optional language code, English by default] <translation>"
    private val translationMatcher = Regex("""//\s*#(?<lang>[a-z]{2}_[A-Z]{2})?\s+(?<data>.+)""")

    // "searchSourceFiles"
    private fun collectLanguageData(
        sources: ConfigurableFileCollection,
        englishMap: MutableMap<String, String>,
        languageMaps: MutableMap<String, MutableMap<String, String>>,
    ) {
        // the state machine
        var state: MatchingState = Idle

        sources.forEach { source ->
            // common logic for check key duplication and confliction
            fun MutableMap<String, String>.putTranslation(
                key: String,
                value: String,
            ) {
                val existingValue = this[key]
                if (existingValue != null) {
                    if (existingValue == value) {
                        logger.warn("Key {} duplicated from file {}", key, source.path)
                    } else {
                        logger.warn(
                            "Key {} is existent {} from file {}",
                            key,
                            existingValue,
                            source.path,
                        )
                    }
                } else {
                    this[key] = value
                }
            }

            val elapsedTime =
                measureTime {
                    source.useLines(Charsets.UTF_8) { lines ->
                        lines.forEach { line ->
                            when (val s = state) {
                                is TrMatched -> {
                                    val matched = translationMatcher.find(line)
                                    if (matched != null) {
                                        val groups = matched.groups
                                        val lang = groups["lang"]?.value
                                        val data = groups["data"]?.value
                                        // unreachable, Regex forces it to be non-null
                                        checkNotNull(data) { "Regex error: didn't matched any data?!" }
                                        // get the language map if specified, or fallback to the English map
                                        val languageMap =
                                            lang?.let { languageMaps.getOrPut(it) { mutableMapOf() } } ?: englishMap
                                        languageMap.putTranslation(s.key, data.replaceColor())
                                    } else {
                                        state = Idle
                                    }
                                }

                                is Idle -> {
                                    val matched = trHeadMatcher.find(line)
                                    if (matched != null) {
                                        val groups = matched.groups
                                        val key = groups["key"]?.value
                                        val data = groups["data"]?.value
                                        // unreachable, Regex forces it to be non-null
                                        checkNotNull(key) { "Regex error: didn't matched key?!" }
                                        // update state
                                        state = TrMatched(key)
                                        // store English translation if present
                                        if (data != null) {
                                            englishMap.putTranslation(key, data.replaceColor())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            logger.info("{} elapsed for {}", elapsedTime, source.path)
        }
    }

    private fun collectLanguageDataLanguageFiles(
        outputDirectory: DirectoryProperty,
        englishKeys: MutableSet<String>,
        languageMap: MutableMap<String, MutableMap<String, String>>,
    ) {
        // select *.lang under the output directory
        outputDirectory.asFileTree.matching { include("*.lang") }.forEach { file ->
            val languageCode = file.nameWithoutExtension
            val needSort = languageCode == "en_US"

            val languageMap = languageMap.getOrPut(languageCode) { mutableMapOf() }
            file.useLines(Charsets.UTF_8) { lines ->
                lines.forEach { line ->
                    if (line.startsWith("#") || line.trim().isEmpty()) {
                        if (needSort) englishKeys.add("//$line")
                    } else {
                        val parts = line.split("=", limit = 2)
                        if (parts.size == 2) {
                            val key = parts[0].trim()
                            val value = parts[1]
                            if (needSort) englishKeys += key
                            val existingValue = languageMap[key]
                            if (existingValue != null) {
                                if (value != existingValue) {
                                    logger.info("Key {} has been defined in the code. Values will be overwritten!", key)
                                    logger.info("Code {}, Language {}", existingValue, value)
                                }
                            } else {
                                languageMap[key] = value
                            }
                        }
                    }
                }
            }
        }
    }

    private fun writeLanguageFiles(
        outputDirectory: DirectoryProperty,
        englishKeys: Set<String>,
        languageMaps: MutableMap<String, MutableMap<String, String>>,
        ignoreKeyInFile: Boolean,
        keysOnlyInLanguageFiles: Set<String>,
        englishMap: Map<String, String>,
    ) {
        languageMaps.forEach { (languageCode, languageMap) ->
            val outputFile = outputDirectory.asFile.get().resolve("$languageCode.lang")
            outputFile.bufferedWriter(Charsets.UTF_8).use { w ->
                if (englishKeys.isNotEmpty()) { // follow the same order as the english
                    englishKeys.forEach { key ->
                        if (key.startsWith("//")) {
                            w.write(key.substring(2) + "\n")
                        } else {
                            val value = languageMap[key]
                            if (value != null) {
                                if (ignoreKeyInFile && key in keysOnlyInLanguageFiles) {
                                    logger.info("Line {} ignored, because it only exists in the language file!", key)
                                } else {
                                    w.write("$key=$value\n")
                                }
                            } else {
                                logger.warn("$languageCode doesn't contain $key")
                                w.write("$key=${englishMap[key]}\n")
                            }
                        }
                    }
                } else { // or ignore the order
                    languageMaps.forEach { (key, value) ->
                        w.write("$key=$value\n")
                    }
                }
            }
        }
    }
}

/**
 * Replace color blocks like `{GOLD}` will be replaced with `§6`.
 */
private fun String.replaceColor(): String =
    replace(Regex("""\{\\([A-Z_]+)\}""")) { match ->
        val color = match.groupValues[1]
        COLORS[color] ?: color
    }
