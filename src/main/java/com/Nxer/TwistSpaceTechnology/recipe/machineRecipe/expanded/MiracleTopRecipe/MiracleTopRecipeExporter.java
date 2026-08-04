package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import cpw.mods.fml.relauncher.FMLInjectionData;
import gregtech.api.util.GTRecipe;

public final class MiracleTopRecipeExporter {

    private MiracleTopRecipeExporter() {}

    // Write scanned MT recipes to the Minecraft folder before custom recipes are loaded.
    static void export(Collection<GTRecipe> recipes) {
        File minecraftFolder = (File) FMLInjectionData.data()[6];
        if (minecraftFolder == null) {
            TwistSpaceTechnology.LOG.error("Cannot export Miracle Top recipes: Minecraft folder is not available.");
            return;
        }
        File outputFile = new File(minecraftFolder, "MiracleTopRecipes.txt");

        try (BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
            writer.write("Miracle Top scanned recipes: " + recipes.size());
            writer.newLine();

            int recipeNumber = 1;
            for (GTRecipe recipe : recipes) {
                writer.newLine();
                writer.write("Recipe " + recipeNumber++);
                writer.newLine();
                writeItems(writer, "Item Inputs", recipe.mInputs);
                writeFluids(writer, "Fluid Inputs", recipe.mFluidInputs);
                writeItems(writer, "Item Outputs", recipe.mOutputs);
                writeFluids(writer, "Fluid Outputs", recipe.mFluidOutputs);
                writer.write("EU/t: " + recipe.mEUt);
                writer.newLine();
                writer.write("Duration: " + recipe.mDuration + " ticks (" + recipe.mDuration / 20.0D + " s)");
                writer.newLine();
            }

            TwistSpaceTechnology.LOG.info("Exported Miracle Top recipes to {}", outputFile.getAbsolutePath());
        } catch (IOException exception) {
            TwistSpaceTechnology.LOG.error("Failed to export Miracle Top recipes to " + outputFile, exception);
        }
    }

    private static void writeItems(BufferedWriter writer, String title, ItemStack[] stacks) throws IOException {
        writer.write(title + ':');
        writer.newLine();
        if (stacks == null || stacks.length == 0) {
            writer.write("  <none>");
            writer.newLine();
            return;
        }

        for (ItemStack stack : stacks) {
            if (stack == null) continue;
            String itemName = Item.itemRegistry.getNameForObject(stack.getItem());
            writer.write("  " + itemName + ':' + stack.getItemDamage() + " x " + stack.stackSize);
            if (stack.hasTagCompound()) writer.write(" NBT=" + stack.getTagCompound());
            writer.newLine();
        }
    }

    private static void writeFluids(BufferedWriter writer, String title, FluidStack[] stacks) throws IOException {
        writer.write(title + ':');
        writer.newLine();
        if (stacks == null || stacks.length == 0) {
            writer.write("  <none>");
            writer.newLine();
            return;
        }

        for (FluidStack stack : stacks) {
            if (stack == null) continue;
            writer.write(
                "  " + stack.getFluid()
                    .getName() + " x " + stack.amount + " mB");
            if (stack.tag != null) writer.write(" NBT=" + stack.tag);
            writer.newLine();
        }
    }
}
