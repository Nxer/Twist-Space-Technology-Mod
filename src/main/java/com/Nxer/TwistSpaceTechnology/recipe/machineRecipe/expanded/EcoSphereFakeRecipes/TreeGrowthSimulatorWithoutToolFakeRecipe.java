package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.EcoSphereFakeRecipes;

import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache.TREE_MODE;
import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.EcoSphereFluidCache.cacheRecipeFluids;
import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.TreeGrowthSimulatorMode.getModeMultiplier;
import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.TreeGrowthSimulatorMode.queryTimeTreeProduct;
import static com.Nxer.TwistSpaceTechnology.common.machine.EcoSphere.Mode.TreeGrowthSimulatorMode.queryTreeProduct;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator.MODE_RECIPE_DURATION;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorBeaconRequirementKey.INSTANCE;
import static gregtech.common.tileentities.machines.multi.MTETreeFarm.treeProductsMap;

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
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorTierRequirementKey;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.MTETreeFarm.Mode;

public class TreeGrowthSimulatorWithoutToolFakeRecipe {

    public static final int WATER_PER_PARALLEL = 2000;
    public static final int TEMPORAL_FLUID_PER_PARALLEL = 100;
    public static final int DEATH_WATER_PER_PARALLEL = 1000;
    public static final int UNKNOWN_WATER_PER_PARALLEL = 1000;
    public static final int UU_MATTER_PER_PARALLEL = 500;
    public static final FluidStack WATER_STACK = Materials.Water.getFluid(WATER_PER_PARALLEL);
    public static final FluidStack TEMPORAL_FLUID_STACK = FluidRegistry
        .getFluidStack("temporalfluid", TEMPORAL_FLUID_PER_PARALLEL);
    public static final FluidStack DEATH_WATER_STACK = FluidRegistry
        .getFluidStack("fluiddeath", DEATH_WATER_PER_PARALLEL);
    public static final FluidStack UNKNOWN_WATER_STACK = FluidRegistry
        .getFluidStack("unknowwater", UNKNOWN_WATER_PER_PARALLEL);
    public static final FluidStack UU_MATTER_STACK = Materials.UUMatter.getFluid(UU_MATTER_PER_PARALLEL);

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
        cacheRecipeFluids(TREE_MODE, GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes);
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
            allSaplingWithTagCopy.add(aSapling.copy());
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
            addFakeRecipe(Sapling, new ItemStack[] { Sapling }, WATER_STACK, 1);
        }
    }

    static void loadManualRecipes() {
        // Time
        ItemStack timeSapling = Mods.TwilightForest.isModLoaded()
            ? GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 0, 5)
            : null;
        if (timeSapling != null && TEMPORAL_FLUID_STACK != null) {
            addSpecialFakeRecipe(timeSapling, TEMPORAL_FLUID_STACK);
        }
        ItemStack taintedSapling = Mods.ForbiddenMagic.isModLoaded()
            ? GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintSapling", 0, 0)
            : null;
        if (taintedSapling != null && DEATH_WATER_STACK != null) {
            addSpecialFakeRecipe(taintedSapling, DEATH_WATER_STACK);
        }
        ItemStack barnardaCSapling = Mods.GalaxySpace.isModLoaded()
            ? GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 0, 0)
            : null;
        if (barnardaCSapling != null && UNKNOWN_WATER_STACK != null) {
            addSpecialFakeRecipe(barnardaCSapling, UNKNOWN_WATER_STACK);
        }

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

        if (UU_MATTER_STACK != null) addFakeRecipe(
            IntegratedCircuitStack,
            new ItemStack[] { LogSymbol, SaplingSymbol, LeavesSymbol, FruitSymbol },
            allSaplingWithTag,
            UU_MATTER_STACK,
            2);
    }

    public static void addEnchantmentLight(ItemStack aStack) {

        if (!aStack.hasTagCompound()) {
            aStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = aStack.getTagCompound();
        NBTTagList enchantments = new NBTTagList();
        tag.setTag("ench", enchantments);
    }

    static void addSpecialFakeRecipe(ItemStack specialSapling, FluidStack specialFluid) {
        EnumMap<Mode, ItemStack> productMap = specialFluid.getFluid() == TEMPORAL_FLUID_STACK.getFluid()
            ? queryTimeTreeProduct(specialSapling)
            : queryTreeProduct(specialSapling);
        addFakeRecipe(productMap, allSaplingWithTag, specialFluid, 2);
    }

    static void addFakeRecipe(ItemStack Sapling, ItemStack[] specialStacks, FluidStack inputFluid, int requiredTier) {
        addFakeRecipe(queryTreeProduct(Sapling), specialStacks, inputFluid, requiredTier);
    }

    static void addFakeRecipe(EnumMap<Mode, ItemStack> ProductMap, ItemStack[] specialStacks, FluidStack inputFluid,
        int requiredTier) {

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
        addFakeRecipe(i, o, specialStacks, inputFluid, requiredTier);
    }

    static void addFakeRecipe(ItemStack[] inputStacks, ItemStack[] outputStacks, ItemStack[] specialStacks,
        FluidStack inputFluid, int requiredTier) {
        GTValues.RA.stdBuilder()
            .itemInputs(inputStacks)
            .itemOutputs(outputStacks)
            .fluidInputs(inputFluid)
            .special(specialStacks)
            .metadata(EcoSphereSimulatorTierRequirementKey.INSTANCE, 1)
            .metadata(INSTANCE, requiredTier)
            .fake()
            .duration(MODE_RECIPE_DURATION)
            .eut(0)
            .addTo(GTCMRecipe.TreeGrowthSimulatorWithoutToolFakeRecipes);
    }
}
