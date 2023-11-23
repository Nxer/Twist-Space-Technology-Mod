package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import java.util.ArrayList;
import java.util.HashMap;

public class StructureLoader {

    public static HashMap<String,MultiStructureDefinition> structureDefinition;
    public static MultiStructureDefinition readStructure(String name){
        //todo
        return structureDefinition.get(name);
    }

    public static MultiStructureDefinition.OffSet getOffSet(String name,String piece){
        var structure = readStructure(name);
        return structure.offSet.get(structure.pieces.get(piece));
    }

    public static String[][] getShape(String name,String piece){
        var structure = readStructure(name);
        return structure.shape.get(structure.pieces.get(piece));
    }

    public static HashMap<String,Integer> getPieces(String name){
        return readStructure(name).pieces;
    }



    public static void load(){

    }

    public static class MultiStructureDefinition{
        public static class OffSet{
            public int horizontalOffSet;
            public int verticalOffSet;
            public int depthOffSet;

            public OffSet(int horizontalOffSet, int verticalOffSet, int depthOffSet) {
                this.horizontalOffSet = horizontalOffSet;
                this.verticalOffSet = verticalOffSet;
                this.depthOffSet = depthOffSet;
            }
        }
        public ArrayList<OffSet> offSet;
        public ArrayList<String[][]> shape;
        public HashMap<String,Integer> pieces;
    }
}
