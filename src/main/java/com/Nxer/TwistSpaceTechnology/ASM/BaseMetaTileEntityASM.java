package com.Nxer.TwistSpaceTechnology.ASM;

import net.minecraft.launchwrapper.IClassTransformer;

import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class BaseMetaTileEntityASM implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        // 捕获BaseMetaTileEntity类的加载
        if ("gregtech.api.metatileentity.BaseMetaTileEntity".equals(transformedName)) {
            return transformBaseClass(basicClass);
        }
        return basicClass;
    }

    private byte[] transformBaseClass(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new BaseClassVisitor(Opcodes.ASM5, classWriter);
        classReader.accept(classVisitor, 0);
        return classWriter.toByteArray();
    }

    static class BaseClassVisitor extends ClassVisitor {

        public BaseClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public void visitEnd() {
            // 访问getRenderBoundingBox方法
            MethodVisitor mv = cv.visitMethod(
                Opcodes.ACC_PUBLIC,
                "getRenderBoundingBox",
                "()Lnet/minecraft/util/AxisAlignedBB;",
                null,
                null);
            if (mv != null) {
                mv.visitCode();

                // 将“this”加载到堆栈上
                mv.visitVarInsn(Opcodes.ALOAD, 0);

                // 调用getMetaTileEntity方法
                mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "gregtech/api/metatileentity/BaseMetaTileEntity",
                    "getMetaTileEntity",
                    "()Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;",
                    false);

                // 判断是否来自于IMachineTESR的实例
                mv.visitTypeInsn(Opcodes.INSTANCEOF, "com/Nxer/TwistSpaceTechnology/client/render/IMachineTESR");

                // 如果为false则跳过调用
                Label ifFalse = new Label();
                mv.visitJumpInsn(Opcodes.IFEQ, ifFalse);

                // 将类型转换为IMachineTESR，并调用IMachineTESR接口的getRenderBoundingBox方法
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "gregtech/api/metatileentity/BaseMetaTileEntity",
                    "getMetaTileEntity",
                    "()Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;",
                    false);
                mv.visitTypeInsn(Opcodes.CHECKCAST, "com/Nxer/TwistSpaceTechnology/client/render/IMachineTESR");
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(
                    Opcodes.INVOKEINTERFACE,
                    "com/Nxer/TwistSpaceTechnology/client/render/IMachineTESR",
                    "getRenderBoundingBox",
                    "(Lgregtech/api/metatileentity/BaseMetaTileEntity;)Lnet/minecraft/util/AxisAlignedBB;",
                    true);
                mv.visitInsn(Opcodes.ARETURN);

                mv.visitLabel(ifFalse);

                // 如果为false则调用原来父类的方法
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(
                    Opcodes.INVOKESPECIAL,
                    "gregtech/api/metatileentity/CommonMetaTileEntity",
                    "getRenderBoundingBox",
                    "()Lnet/minecraft/util/AxisAlignedBB;",
                    false);
                mv.visitInsn(Opcodes.ARETURN);

                mv.visitMaxs(3, 1);
                mv.visitEnd();
            }
            MethodVisitor mv1 = cv.visitMethod(Opcodes.ACC_PUBLIC, "getMaxRenderDistanceSquared", "()D", null, null);
            if (mv1 != null) {
                mv1.visitCode();

                // 将“this”加载到堆栈上
                mv1.visitVarInsn(Opcodes.ALOAD, 0);

                // 调用getMetaTileEntity方法
                mv1.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "gregtech/api/metatileentity/BaseMetaTileEntity",
                    "getMetaTileEntity",
                    "()Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;",
                    false);

                // 判断是否来自于IMachineTESR的实例
                mv1.visitTypeInsn(Opcodes.INSTANCEOF, "com/Nxer/TwistSpaceTechnology/client/render/IMachineTESR");

                // 如果为false则跳过调用
                Label ifFalse = new Label();
                mv1.visitJumpInsn(Opcodes.IFEQ, ifFalse);

                // 将类型转换为IMachineTESR，并调用IMachineTESR接口的getMaxRenderDistanceSquared方法
                mv1.visitVarInsn(Opcodes.ALOAD, 0);
                mv1.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "gregtech/api/metatileentity/BaseMetaTileEntity",
                    "getMetaTileEntity",
                    "()Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;",
                    false);
                mv1.visitTypeInsn(Opcodes.CHECKCAST, "com/Nxer/TwistSpaceTechnology/client/render/IMachineTESR");
                mv1.visitMethodInsn(
                    Opcodes.INVOKEINTERFACE,
                    "com/Nxer/TwistSpaceTechnology/client/render/IMachineTESR",
                    "getMaxRenderDistanceSquared",
                    "()D",
                    true);
                mv1.visitInsn(Opcodes.DRETURN);

                mv1.visitLabel(ifFalse);

                // 如果为false则调用原来父类的方法
                mv1.visitVarInsn(Opcodes.ALOAD, 0);
                mv1.visitMethodInsn(
                    Opcodes.INVOKESPECIAL,
                    "gregtech/api/metatileentity/CommonMetaTileEntity",
                    "getMaxRenderDistanceSquared",
                    "()D",
                    false);
                mv1.visitInsn(Opcodes.DRETURN);

                mv1.visitMaxs(3, 1);
                mv1.visitEnd();
            }
            super.visitEnd();
        }
    }
}
