package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.getModeMultiplier;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm.queryTreeProduct;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTETreeFarm.treeProductsMap;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import galaxyspace.BarnardsSystem.BRFluids;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTETreeFarm.Mode;

public class TreeGrowthSimulatorWithoutToolFakeRecipe {

    static FluidStack WaterStack = Materials.Water.getFluid(1000);
    static FluidStack UnknowWaterStack = new FluidStack(BRFluids.UnknowWater, 1000);
    static FluidStack TemporalLiquidStack = new FluidStack(FluidRegistry.getFluid("temporalfluid"), 100);
    static FluidStack DeathWaterStack = new FluidStack(FluidRegistry.getFluid("fluiddeath"), 1000);
    static FluidStack UUMatterStack = Materials.UUMatter.getFluid(500);

    static ItemStack[] IntegratedCircuitStack = { GTUtility.getIntegratedCircuit(1), GTUtility.getIntegratedCircuit(2),
        GTUtility.getIntegratedCircuit(3), GTUtility.getIntegratedCircuit(4), };
    static ItemStack[] allSaplingsIn;
    static ItemStack[] allSaplingWithTag;
    static ItemStack[] allLogs;
    static ItemStack[] allSaplings;
    static ItemStack[] allLeaves;
    static ItemStack[] allFruits;
    public static ItemStack[][] allProducts;

    public static void loadRecipes() {
        initStatic();
        loadTreeFarmWithoutToolRecipe();
        loadManualRecipes();
    }

    static void initStatic() {
        ArrayList<ItemStack> allSaplingsInCopy = new ArrayList<>();
        EnumMap<Mode, ArrayList<ItemStack>> allProductsMap = new EnumMap<>(Mode.class);

        // init allSaplingsIn
        ItemStack saplingIn;
        for (Map.Entry<String, EnumMap<Mode, ItemStack>> entry : treeProductsMap.entrySet()) {
            String key = entry.getKey();
            String[] keyPart;
            keyPart = key.split(":");
            saplingIn = entry.getValue()
                .get(Mode.SAPLING);
            if (!key.contains("Forestry:")) saplingIn = GTModHandler
                .getModItem(keyPart[0], keyPart[1], 0, keyPart[2] == null ? 0 : Integer.parseInt(keyPart[2]));
            saplingIn.stackSize = 0;
            allSaplingsInCopy.add(saplingIn);
        }
        allSaplingsIn = allSaplingsInCopy.toArray(new ItemStack[0]);

        ArrayList<ItemStack> allSaplingWithTagCopy = new ArrayList<>();
        for (ItemStack aSapling : allSaplingsIn) {
            ItemStack aStack = aSapling.copy();
            aStack.setStackDisplayName(TextEnums.tr("ESS.TreeGrowthSimulator.nei.tooltip.7"
            // #tr ESS.TreeGrowthSimulator.nei.tooltip.7
            // # Any Sapling
            // #zh_CN 任意树苗
            ));
            allSaplingWithTagCopy.add(aStack);
        }
        allSaplingWithTag = allSaplingWithTagCopy.toArray(new ItemStack[0]);

        // init allOuts
        for (ItemStack aSapling : allSaplingsIn) {
            EnumMap<Mode, ItemStack> productMap = queryTreeProduct(aSapling);
            for (Mode mode : Mode.values()) {
                ItemStack aStack = productMap.get(mode);
                if (aStack == null) continue;
                ItemStack aStackCopy = aStack.copy();
                aStackCopy.stackSize *= getModeMultiplier(mode);
                ArrayList<ItemStack> productList = allProductsMap.computeIfAbsent(mode, k -> new ArrayList<>());
                productList.add(aStackCopy);
            }
        }

        allLogs = allProductsMap.get(Mode.LOG)
            .toArray(new ItemStack[0]);
        allSaplings = allProductsMap.get(Mode.SAPLING)
            .toArray(new ItemStack[0]);
        allLeaves = allProductsMap.get(Mode.LEAVES)
            .toArray(new ItemStack[0]);
        allFruits = allProductsMap.get(Mode.FRUIT)
            .toArray(new ItemStack[0]);
        // allProducts = allProductsMap.values()
        // .stream()
        // .flatMap(ArrayList::stream)
        // .toArray(ItemStack[]::new);
        allProducts = new ItemStack[][] { allLogs, allSaplings, allLeaves, allFruits };
    }

    static void loadTreeFarmWithoutToolRecipe() {
        for (ItemStack Sapling : allSaplingsIn) {
            addFakeRecipe(Sapling, new ItemStack[] { Sapling }, WaterStack);
        }
    }

    static void loadManualRecipes() {
        // Barnarda C
        if (Mods.GalaxySpace.isModLoaded()) addSpecialFakeRecipe(
            GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 0, 1),
            UnknowWaterStack);
        // Time
        if (Mods.TwilightForest.isModLoaded()) addSpecialFakeRecipe(
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 0, 5),
            TemporalLiquidStack);
        // Death Water
        // Thaumic Tentacle?

        // UU Matter
        ItemStack LogSymbol = new ItemStack(Blocks.log, 1, 0);
        LogSymbol.setStackDisplayName(TextEnums.tr("ESS.TreeGrowthSimulator.nei.fakeItem.0"
        // #tr ESS.TreeGrowthSimulator.nei.fakeItem.0
        // # Random logs
        // #zh_CN 随机原木
        ));
        addEnchantmentLight(LogSymbol);
        ItemStack SaplingSymbol = new ItemStack(Blocks.sapling, 1, 0);
        SaplingSymbol.setStackDisplayName(TextEnums.tr("ESS.TreeGrowthSimulator.nei.fakeItem.1"
        // #tr ESS.TreeGrowthSimulator.nei.fakeItem.1
        // # Random saplings
        // #zh_CN 随机树苗
        ));
        addEnchantmentLight(SaplingSymbol);
        ItemStack LeavesSymbol = new ItemStack(Blocks.leaves, 1, 0);
        LeavesSymbol.setStackDisplayName(TextEnums.tr("ESS.TreeGrowthSimulator.nei.fakeItem.2"
        // #tr ESS.TreeGrowthSimulator.nei.fakeItem.2
        // # Random leaves
        // #zh_CN 随机树叶
        ));
        addEnchantmentLight(LeavesSymbol);
        ItemStack FruitSymbol = new ItemStack(Items.apple, 1, 0);
        FruitSymbol.setStackDisplayName(TextEnums.tr("ESS.TreeGrowthSimulator.nei.fakeItem.3"
        // #tr ESS.TreeGrowthSimulator.nei.fakeItem.3
        // # Random fruits
        // #zh_CN 随机果实
        ));
        addEnchantmentLight(FruitSymbol);

        addFakeRecipe(
            IntegratedCircuitStack,
            new ItemStack[] { LogSymbol, SaplingSymbol, LeavesSymbol, FruitSymbol },
            allSaplingWithTag,
            UUMatterStack);
    }

    public static void addEnchantmentLight(ItemStack aStack) {

        if (!aStack.hasTagCompound()) {
            aStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = aStack.getTagCompound();
        NBTTagList enchantments = new NBTTagList();
        tag.setTag("ench", enchantments);
    }

    static void addSpecialFakeRecipe(ItemStack SpecialSapling, FluidStack SpecialFluid) {
        addFakeRecipe(SpecialSapling, allSaplingWithTag, SpecialFluid);
    }

    static void addFakeRecipe(ItemStack Sapling, ItemStack[] specialStacks, FluidStack inputFluid) {
        EnumMap<Mode, ItemStack> ProductMap = queryTreeProduct(Sapling);

        // ItemStack[] inputStacks = new ItemStack[Mode.values().length];
        // ItemStack[] outputStacks = new ItemStack[Mode.values().length];

        ArrayList<ItemStack> input = new ArrayList<>();
        ArrayList<ItemStack> output = new ArrayList<>();

        int count = 0;
        for (Mode mode : Mode.values()) {
            if (ProductMap != null && ProductMap.get(mode) != null) {

                input.add(IntegratedCircuitStack[count]);
                ItemStack stack = ProductMap.get(mode)
                    .copy();
                stack.stackSize *= getModeMultiplier(mode);
                output.add(stack);

                // inputStacks[count] = IntegratedCircuitStack[count];
                // outputStacks[count] = ProductMap.get(mode)
                // .copy();
                // outputStacks[count].stackSize *= getModeMultiplier(mode);
            }
            count++;
        }
        var i = input.toArray(new ItemStack[0]);
        var o = output.toArray(new ItemStack[0]);
        addFakeRecipe(i, o, specialStacks, inputFluid);
    }

    static void addFakeRecipe(ItemStack[] inputStacks, ItemStack[] outputStacks, ItemStack[] specialStacks,
        FluidStack inputFluid) {
        GTValues.RA.stdBuilder()
            .itemInputs(inputStacks)
            .itemOutputs(outputStacks)
            .fluidInputs(inputFluid)
            .special(specialStacks)
            .noOptimize()
            .fake()
            .duration(20 * 5)
            .eut(0)
            .addTo(GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes);
    }
}
