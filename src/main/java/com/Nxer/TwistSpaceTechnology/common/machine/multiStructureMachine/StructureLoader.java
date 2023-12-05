package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipInputStream;

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
            LOG.error("get OffSet is null:" + name + "->" + piece);
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
        MultiStructureDefinition.OffSet offSet = structure.machineOffSet;
        path = "C:\\Users\\bcjPr\\Desktop\\gtnh-astra-migrate\\GTNH-combination\\GTNH_Community_Mod\\src\\main\\resources\\assets\\gtnhcommunitymod\\structure\\namemegauniversalspacestation.zip";
        if (!new File(path).exists()) {
            LOG.info("structure file " + path + " not find!");
            return;
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path)
        // Objects.requireNonNull(LanguageUtil0.class.getResourceAsStream(zipFilePath))
            , StandardCharsets.UTF_8)) {
            zipInputStream.getNextEntry();
            BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream, StandardCharsets.UTF_8));
            String line;
            int cnt = 0;
            while ((line = reader.readLine()) != null) {
                String[] off = line.split(" ");
                MultiStructureDefinition.OffSet offset = new MultiStructureDefinition.OffSet(
                    offSet.horizontalOffSet - Integer.parseInt(off[2]),
                    offSet.verticalOffSet - Integer.parseInt(off[1]),
                    offSet.depthOffSet - Integer.parseInt(off[0]));
                line = reader.readLine();
                String[][] shape = new String[16][16];
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        int t = 256 * i + 16 * j;
                        shape[i][j] = line.substring(t, t + 16);
                    }
                }
                structure.shape.add(shape);
                structure.pieces.put(pieces + cnt, structure.pieces.size());
                structure.offSet.add(offset);
                LOG.info("loaded :" + name + " with sub pieces:" + pieces + cnt);
                cnt++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setOffSet(String name, int horizontalOffSet, int verticalOffSet, int depthOffSet) {
        readStructure(name).machineOffSet = new MultiStructureDefinition.OffSet(
            horizontalOffSet,
            verticalOffSet,
            depthOffSet);
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
        public OffSet machineOffSet;
        public ArrayList<String[][]> shape;
        public HashMap<String, Integer> pieces;
    }
}
