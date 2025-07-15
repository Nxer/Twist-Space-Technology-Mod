package twist_gradle.util


import org.gradle.api.file.FileSystemLocation

import java.nio.file.Path

class TwistFileUtils {

    static void ensureDirectory(Object filelike, boolean allowDelete = false) {
        var file = filelikeToFile(filelike)
        if(!file.isDirectory()) {
            if(allowDelete) {
                file.delete()
            } else {
                throw new IllegalStateException("File ${file} exists but it wasn't a directory.")
            }
        }
        if(!file.exists()) {
            file.mkdirs()
        }
    }

    static void ensureFile(Object filelike, boolean allowDelete = false, boolean allowCreateParent = true) {
        var file = filelikeToFile(filelike)
        var parent = file.parentFile

        if(!parent.exists()) {
            if(allowCreateParent) {
                ensureDirectory(parent, allowDelete)
            } else {
                throw new IllegalStateException("Parent file ${parent} of file ${file} doesn't exists.")
            }
        }

        if(!file.isFile()) {
            if(allowDelete) {
                file.delete()
            } else {
                throw new IllegalStateException("File ${file} exists but it wasn't a directory.")
            }
        }

        if(!file.exists()) {
            file.createNewFile()
        }
    }

    static File filelikeToFile(Object filelike) {
        if (filelike instanceof File) {
            return filelike
        } else if (filelike instanceof Path) {
            return filelike.toFile()
        } else if (filelike instanceof String) {
            return new File(filelike)
        } else if (filelike instanceof FileSystemLocation) { // gradle support
            return filelike.getAsFile()
        } else {
            throw new IllegalStateException("Type ${filelike.class.canonicalName} is not supported.")
        }
    }

}
