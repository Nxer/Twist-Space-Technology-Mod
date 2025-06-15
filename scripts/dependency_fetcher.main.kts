#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2")
@file:DependsOn("io.ktor:ktor-client-core-jvm:3.1.3")
@file:DependsOn("io.ktor:ktor-client-cio-jvm:3.1.3")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.1")

// SPDX-License-Identifier: MIT

import io.ktor.client.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

val modpackVersion = "daily"

val manifestUrl =
    "https://github.com/GTNewHorizons/DreamAssemblerXXL/raw/refs/heads/master/releases/manifests/${modpackVersion}.json"

@OptIn(ExperimentalSerializationApi::class)
val http = HttpClient {
    install(HttpCache)
}

private data class DependencyMapper(val manifestKey: String, val mapping: (String) -> String)

private fun standardNHOrganizationProject(name: String, depConfiguration: String = "implementation"): DependencyMapper =
    DependencyMapper(name) { version -> "${depConfiguration}(\"com.github.GTNewHorizons:${name}:${version}:dev\")" }

private val dependencyMappers = listOf(
    standardNHOrganizationProject("GT5-Unofficial"),
    standardNHOrganizationProject("GTNHLib"),
    standardNHOrganizationProject("AE2FluidCraft-Rework"),
    standardNHOrganizationProject("NewHorizonsCoreMod"),
    standardNHOrganizationProject("Avaritia"),
    standardNHOrganizationProject("Avaritiaddons"),
    standardNHOrganizationProject("Eternal-Singularity"),
    standardNHOrganizationProject("Universal-Singularities"),
    standardNHOrganizationProject("MagicBees"),
    standardNHOrganizationProject("ThaumicEnergistics"),
    standardNHOrganizationProject("Botania"),
    standardNHOrganizationProject("supersolarpanels"),
    standardNHOrganizationProject("Electro-Magic-Tools"),
    standardNHOrganizationProject("Hodgepodge"),
    standardNHOrganizationProject("WitcheryExtras"),
    standardNHOrganizationProject("BloodMagic"),
    standardNHOrganizationProject("EnderIO"),
    standardNHOrganizationProject("EnderCore"),
    standardNHOrganizationProject("Galaxy-Space-GTNH"),
    standardNHOrganizationProject("BlockRenderer6343", depConfiguration = "runtimeOnlyNonPublishable"),
)

private data class ModInfo(val version: String, val side: String)

private fun readGithubMods(manifest: JsonObject): Map<String, ModInfo> {
    val githubModsObject = manifest["github_mods"]!!.jsonObject
    return githubModsObject.mapValues { (_, modInfoElement) ->
        val modInfoObject = modInfoElement.jsonObject
        val modVersion = modInfoObject["version"]!!.jsonPrimitive.content
        val modSide = modInfoObject["side"]!!.jsonPrimitive.content
        ModInfo(modVersion, modSide)
    }
}

suspend fun main() {
    println("Fetching the manifest")
    val manifestText = http.get(manifestUrl).bodyAsText()
    println("Reading the manifest")
    val manifest = Json.decodeFromString<JsonObject>(manifestText)
    val githubMods = readGithubMods(manifest)
    println("Mapping to dependency notations")
    dependencyMappers.map { (key, mapping) ->
        val modInfo =
            githubMods[key] ?: return@map println("Dependency $key is not found in the Manifest. Is it removed?")
        mapping(modInfo.version)
    }.let { notations ->
        // print them in one go, to prevent being disturbed by error messages.
        println("===[ GRADLE DEPENDENCY NOTATIONS START ]===")
        notations.forEach { println(it) }
        println("===[ GRADLE DEPENDENCY NOTATIONS END ]===")
    }
}

runBlocking { main() }
