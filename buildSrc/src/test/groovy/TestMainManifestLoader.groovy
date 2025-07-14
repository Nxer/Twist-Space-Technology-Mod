import twist_gradle.ManifestLoader

class TestMainManifestLoader {

    static void main(String[] args) {
        try {
            ManifestLoader.fetchManifest("2.7.4").tap { println it }
            ManifestLoader.fetchManifest("daily").tap { println it }
        } catch (Exception e) {
            e.printStackTrace()
            println "Failed to run the tests."
        }
    }

}
