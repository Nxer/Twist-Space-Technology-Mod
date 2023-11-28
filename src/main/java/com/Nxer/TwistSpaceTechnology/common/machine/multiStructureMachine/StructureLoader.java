package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.zip.ZipInputStream;

import com.Nxer.TwistSpaceTechnology.util.LanguageUtil0;

public class StructureLoader {

    public static HashMap<String, MultiStructureDefinition> structureDefinition = new HashMap<>();

    public static MultiStructureDefinition readStructure(String name) {

        if (!structureDefinition.containsKey(name)) {
            structureDefinition.put(name, new MultiStructureDefinition());
        }
        return structureDefinition.get(name);
    }

    public static MultiStructureDefinition.OffSet getOffSet(String name, String piece) {

        if (readStructure(name).pieces == null) {
            LOG.error("pieces is null");
        } else if (readStructure(name).pieces.get(piece) == null) {
            for (var i : readStructure(name).pieces.keySet()) {
                LOG.info(i);
            }
            LOG.error("get pieces is null:" + piece);
        }

        var structure = readStructure(name);

        return structure.offSet.get(structure.pieces.get(piece));
    }

    // public static String assetRoot =
    // "C:\\Users\\bcjPr\\Desktop\\gtnh-astra-migrate\\GTNH-combination\\GTNH_Community_Mod\\src\\main\\resources\\assets\\gtnhcommunitymod\\structure\\";
    public static String assetRoot = "/assets/gtnhcommunitymod/structure/";

    public static String[][] getShape(String name, String piece) {

        var structure = readStructure(name);
        if (structure.pieces.get(piece) == null) {
            load(name, piece);
        }
        return structure.shape.get(structure.pieces.get(piece));
    }

    public static HashMap<String, Integer> getPieces(String name) {

        return readStructure(name).pieces;
    }

    // should call in preload;
    public static void load(String name, String pieces) {
        var structure = readStructure(name);
        if (structure.pieces.get(pieces) != null) {
            return;
        }
        String path = assetRoot + name + ".zip";
        if (!structureDefinition.containsKey(name)) {
            structureDefinition.put(name, new MultiStructureDefinition());
        }
        structure.shape.add(readAndUnzip(path));
        structure.pieces.put(pieces, structure.shape.size() - 1);
        structure.offSet.add(new MultiStructureDefinition.OffSet(0, 0, 0));

    }

    public static void setOffSet(String name, String piece, int x, int y, int z) {
        if (readStructure(name).pieces.get(piece) == null) {
            for (var i : readStructure(name).pieces.keySet()) {
                LOG.info(i);
            }
            LOG.error("get pieces is null:" + name + "-" + piece);
        }
        int num = readStructure(name).pieces.get(piece);
        readStructure(name).offSet.set(num, new MultiStructureDefinition.OffSet(x, y, z));
    }

    public static String[][] readAndUnzip(String zipFilePath) {
        StringBuilder result = new StringBuilder();
        if (!new File(zipFilePath).exists()) {
            LOG.info("structure file " + zipFilePath + " not find!");
            return null;
        }
        try (ZipInputStream zipInputStream = new ZipInputStream(
            Objects.requireNonNull(LanguageUtil0.class.getResourceAsStream(zipFilePath)),
            StandardCharsets.UTF_8)) {
            zipInputStream.getNextEntry();
            BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line)
                    .append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String unzippedStructure = result.toString();
        String[] lines = unzippedStructure.split("\\|\n");
        String[][] results = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            results[i] = lines[i].split("\n");
            for (int j = 0; j < results[i].length; j++) {
                results[i][j] = results[i][j].substring(5, results[i][j].length() - 3);
            }
        }
        return results;

    }

    public static class MultiStructureDefinition {

        public static class OffSet {

            public int horizontalOffSet;
            public int verticalOffSet;
            public int depthOffSet;

            public OffSet(int horizontalOffSet, int verticalOffSet, int depthOffSet) {
                this.horizontalOffSet = horizontalOffSet;
                this.verticalOffSet = verticalOffSet;
                this.depthOffSet = depthOffSet;
            }
        }

        MultiStructureDefinition() {
            offSet = new ArrayList<>();
            shape = new ArrayList<>();
            pieces = new HashMap<>();
        }

        public ArrayList<OffSet> offSet;
        public ArrayList<String[][]> shape;
        public HashMap<String, Integer> pieces;
    }
}
