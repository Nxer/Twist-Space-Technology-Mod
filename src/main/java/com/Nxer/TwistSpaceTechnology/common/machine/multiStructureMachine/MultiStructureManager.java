// spotless:off
package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import com.Nxer.TwistSpaceTechnology.config.Config;

public class MultiStructureManager extends WorldSavedData {

    private static final HashMap<Integer, GT_TileEntity_MultiStructureMachine<?>> machines = new HashMap<>();
    private static final HashMap<Integer, HashSet<Integer>> subMachines = new HashMap<>();
    private static final HashMap<Integer, ArrayList<Integer>> validSubTypeCode = new HashMap<>();
    private static Integer endID = 0;

    public MultiStructureManager() {
        super("MultiStructureManagementSavedData");
    }

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

        } else {
            endID++;
            machine.ID = endID;
            machines.put(endID, machine);
            subMachines.put(endID, new HashSet<>());
        }
        LOG.info("machine registered:" + machine.getLocalName() + " with ID:" + endID.toString());

    }

    public static void registryTypeMap(Integer type, ArrayList<Integer> typeCode) {
        validSubTypeCode.put(type, typeCode);
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
        if (!validSubTypeCode.get(mainMachine.Type)
            .contains(subMachine.Type)) {
            return false;
        }
        subMachine.fatherID = mainID;
        subMachines.get(mainID)
            .add(subID);
        LOG.info("machine linked:" + mainMachine.getLocalName() + " and " + subMachine.getLocalName());
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
        subMachines.get(mainID)
            .remove(subID);
    }

    // when machine block is destroyed in any case, call this function.
    public static void removeMachine(GT_TileEntity_MultiStructureMachine<?> machine) {
        if (machine == null) {
            return;
        }
        int ID = machine.ID;
        machines.remove(ID);
        subMachines.remove(ID);
        removeLink(getMachine(machine.fatherID), machine);
        LOG.info("machine removed:" + machine.getLocalName());
    }

    // public static boolean isComplete(GT_TileEntity_MultiStructureMachine<?> machine) {
    // if (machine == null) {
    // return false;
    // }
    // int ID = machine.ID;
    // for (var i : subMachines.get(ID)) {
    // var subMachine = machines.get(i);
    // if (subMachine == null) {
    // continue;
    // }
    // if (!subMachine.checkMachine(subMachine.getBaseMetaTileEntity(), null)) {
    // return false;
    // }
    // }
    // return true;
    // }

    @Override
    public void readFromNBT(NBTTagCompound p_76184_1_) {

    }

    @Override
    public void writeToNBT(NBTTagCompound p_76187_1_) {

    }
}

class structureChecker implements Runnable {

    // TODO
    public static final structureChecker checker = new structureChecker();
    final Object lock = new Object();
    public final Queue<GT_TileEntity_MultiStructureMachine<?>> checkQueue = new ConcurrentLinkedQueue<>();

    public static final Thread thread = new Thread(checker);

    public static void add(GT_TileEntity_MultiStructureMachine<?> machine) {
        checker.checkQueue.add(machine);
    }

    static {
        if (Config.activateMegaSpaceStation) {
            thread.start();
        }
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (true) {
                var machine = checkQueue.poll();
                if (machine != null) {
                    machine.checkStructure(false, machine.getBaseMetaTileEntity());
                }
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}
