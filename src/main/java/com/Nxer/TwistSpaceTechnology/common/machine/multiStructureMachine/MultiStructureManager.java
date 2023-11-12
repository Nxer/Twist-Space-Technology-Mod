/*
 * package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;
 * // spotless:off
public class MultiStructureManager {

    private static final HashMap<Integer, GT_TileEntity_MultiStructureMachine<?>> machines = new HashMap<>();
    private static final HashMap<Integer, HashSet<Integer>> mapTree = new HashMap<>();
    private static final HashMap<Integer, Integer> typeMap = new HashMap<>();
    private static int endID = 0;

    public static GT_TileEntity_MultiStructureMachine<?> getMachine(int ID) {
        if (ID == -1) {
            return null;
        }
        return machines.get(ID);
    }

    // any Time a machine is placed in the world or reloaded when chunk or dimension is reloaded, should call these
    // function
    // to let manager manage the structure main block.
    public static void registryMachine(GT_TileEntity_MultiStructureMachine<?> machine) {
        if (machine == null) {
            LOG.info("unexpected multi-structure registry");
            return;
        }
        if (machine.ID != -1) {
            machines.put(machine.ID, machine);
            return;
        }
        endID++;
        machine.ID = endID;
        machines.put(endID, machine);
        mapTree.put(endID, new HashSet<>());
        typeMap.put(endID, machine.Type);

    }

    // create a link between main structure and sub structure
    public static boolean linkMachine(GT_TileEntity_MultiStructureMachine<?> mainMachine,
        GT_TileEntity_MultiStructureMachine<?> subMachine) {
        if (mainMachine == null || subMachine == null) {
            return false;
        }
        if (mainMachine.checkStructure(false) || subMachine.checkStructure(false)) {
            return false;
        }
        int mainID = mainMachine.ID;
        int subID = subMachine.ID;
        subMachine.fatherID = mainID;
        mapTree.get(mainID)
            .add(subID);
        return true;
    }

    // remove a link between main structure and sub structure
    public static void removeLink(GT_TileEntity_MultiStructureMachine<?> mainMachine,
        GT_TileEntity_MultiStructureMachine<?> subMachine) {
        if (mainMachine == null || subMachine == null) {
            return;
        }
        int mainID = mainMachine.ID;
        int subID = subMachine.ID;
        subMachine.fatherID = -1;
        mapTree.get(mainID)
            .remove(subID);
    }

    // when machine block is destroyed in any case, call this function.
    public static void removeMachine(GT_TileEntity_MultiStructureMachine<?> machine) {
        if (machine == null) {
            return;
        }
        int ID = machine.ID;
        machines.remove(ID);
        if (machine.isMainBlock) {
            mapTree.remove(ID);
        } else {
            removeLink(getMachine(machine.fatherID), machine);
        }
        typeMap.remove(ID);
    }

    public static boolean isComplete(GT_TileEntity_MultiStructureMachine<?> machine) {
        if (machine == null) {
            return false;
        }
        int ID = machine.ID;
        for (var i : mapTree.get(ID)) {
            var subMachine = machines.get(i);
            if (subMachine == null) {
                continue;
            }
            if (!subMachine.checkMachine(subMachine.getBaseMetaTileEntity(), null)) {
                return false;
            }
        }
        return true;
    }

}
// spotless:on
 */
