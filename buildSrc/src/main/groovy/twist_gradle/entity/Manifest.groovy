package twist_gradle.entity

import groovy.transform.ToString

@ToString
class Manifest {
    String version
    String last_version // previous version
    String last_updated
    String config
    Map<String, Mod> github_mods
    Map<String, Mod> external_mods
}
