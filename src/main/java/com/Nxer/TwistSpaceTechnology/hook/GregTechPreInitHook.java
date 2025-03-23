package com.Nxer.TwistSpaceTechnology.hook;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

public class GregTechPreInitHook implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("gregtech.GTMod".equals(transformedName)) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);

            for (MethodNode method : classNode.methods) {
                if ("onPreLoad".equals(method.name)
                    && "(Lcpw/mods/fml/common/event/FMLPreInitializationEvent;)V".equals(method.desc)) {
                    // Locate Materials.init()
                    for (AbstractInsnNode node : method.instructions.toArray()) {
                        if (node instanceof MethodInsnNode methodInsn) {
                            // Match Materials.init()
                            if ("gregtech/api/enums/Materials".equals(methodInsn.owner)
                                && "init".equals(methodInsn.name)
                                && "()V".equals(methodInsn.desc)) {
                                // Add code before Materials.init()
                                InsnList instructions = new InsnList();
                                // new MaterialsTST()
                                instructions.add(
                                    new TypeInsnNode(
                                        NEW,
                                        "com/Nxer/TwistSpaceTechnology/common/material/MaterialsTST"));
                                instructions.add(new InsnNode(DUP));
                                instructions.add(
                                    new MethodInsnNode(
                                        INVOKESPECIAL,
                                        "com/Nxer/TwistSpaceTechnology/common/material/MaterialsTST",
                                        "<init>",
                                        "()V",
                                        false));
                                // Pop up the newly created object to ensure consistent stack state
                                instructions.add(new InsnNode(POP));

                                // Add code
                                method.instructions.insertBefore(node, instructions);
                                break;
                            }
                        }
                    }
                }
            }

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return basicClass;
    }
}
