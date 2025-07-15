package twist_gradle

import com.google.gson.Gson
import twist_gradle.entity.Manifest

class ManifestLoader {

    private static final Gson GSON = new Gson()
    private static final String MANIFEST_URL = "https://raw.githubusercontent.com/GTNewHorizons/DreamAssemblerXXL/refs/heads/master/releases/manifests/%s.json"

    static Manifest fetchManifest(String version) {
        var urlString = MANIFEST_URL.formatted(version)
        var url = new URI(urlString).toURL()
        return GSON.fromJson(url.newReader(), Manifest.class)
    }

    static Manifest loadManifest(Reader strReader) {
        return GSON.fromJson(strReader, Manifest.class)
    }

    static String saveManifest(Manifest manifest) {
        return GSON.toJson(manifest, Manifest.class)
    }

}
