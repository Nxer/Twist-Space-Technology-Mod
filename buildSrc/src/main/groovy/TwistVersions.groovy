import org.slf4j.Logger
import org.slf4j.LoggerFactory
import twist_gradle.ManifestLoader
import twist_gradle.entity.Manifest
import twist_gradle.util.TwistFileUtils

class TwistVersions {

    private static final Logger LOG = LoggerFactory.getLogger("TwistVersions")

    private static Manifest man
    private static Map<String, String> versionLookup

    static {
        // get the manifest from either cache or fetch online.
        man = loadOrFetchManifest(new File("build"), "2.8.0-beta-2")

        // mapping the mods to versions
        versionLookup = new HashMap<>()
        man.github_mods.each { key, value ->
            versionLookup[key] = value.version
        }
        man.external_mods.each { key, value ->
            versionLookup[key] = value.version
        }
    }

    /**
     * A magic function provided by groovy where you can use {@code TwistVersions["SOME_STRING"]} to invoke this.
     *
     * @param propertyName
     * @return
     */
    static String getAt(String propertyName) {
        return versionLookup[propertyName]
    }

    private static Manifest loadOrFetchManifest(File cacheDir, String manifestVersion, boolean forceFetch = false) {
        var cachedFile = new File(cacheDir, "gtnhManifest-${manifestVersion}.json")

        // for 3 special versions, we enable force fetch automatically.
        if(manifestVersion in ["daily", "experimental", "nightly"]) {
            forceFetch = true
        }

        if (!forceFetch && cachedFile.exists()) {
            try {
                return ManifestLoader.loadManifest(cachedFile.newReader())
            } catch (Exception e) {
                LOG.warn("Cached manifest at {} was corrupted and unable to be loaded.", cachedFile, e)
                cachedFile.delete()
            }
        }

        Manifest manifest
        try {
            manifest = ManifestLoader.fetchManifest(manifestVersion)
        } catch (Exception e) {
            LOG.error("Failed to fetch the manifest (version = {})", manifestVersion, e)
            throw new RuntimeException(e)
        }

        try {
            TwistFileUtils.ensureFile(cachedFile, true, true)
            cachedFile.write(ManifestLoader.saveManifest(manifest))
        } catch (Exception e) {
            LOG.warn("Failed to cache the manifest at {}", cachedFile, e)
        }

        return manifest
    }
}
