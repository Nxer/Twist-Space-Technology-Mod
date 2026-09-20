package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.OreProcessingVisualRecipes;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeDuration;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeEUt;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.gemExquisite;
import static gregtech.api.enums.OrePrefixes.gemFlawless;
import static gregtech.api.enums.OrePrefixes.ore;
import static gregtech.api.enums.OrePrefixes.rawOre;
import static gregtech.api.enums.StoneType.STONE_TYPES;
import static gtPlusPlus.core.material.MaterialMisc.RARE_EARTH_HIGH;
import static gtPlusPlus.core.material.MaterialMisc.RARE_EARTH_LOW;
import static gtPlusPlus.core.material.MaterialMisc.RARE_EARTH_MID;
import static gtPlusPlus.core.material.MaterialsAlloy.KOBOLDITE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.api.giver.ItemStacksGiver;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.OreInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.state.MaterialState;

public class OP_Logic {

    public static boolean initialized = false;

    /**
     * Like a GT recipe, this map contains the simple recipes about ore to productions.
     */
    public static Map<TST_ItemID, ItemStack[]> OP_IO_MAP = new HashMap<>();

    /**
     * Use this map we can quickly get an item stacks array factor by the ore ItemID,
     * then quickly generating the machine's output.
     */
    public static Map<TST_ItemID, ItemStacksGiver> OP_GIVER_MAP = new HashMap<>();

    public static void initialize() {
        if (initialized) return;
        initialized = true;
        new OP_Logic().init();
    }

    public final FluidStack LUBRICANT = Materials.Lubricant.getFluid(1);

    public void init() {
        prepare();
        gt();
        gtpp();
        bw();
        special();
    }

    // region GT
    List<StoneType> stoneTypeList = new ArrayList<>();
    List<StoneType> richStoneTypeList = new ArrayList<>();

    public void prepare() {
        STONE_TYPES.forEach(s -> {
            if (s.isRich()) {
                richStoneTypeList.add(s);
            } else {
                stoneTypeList.add(s);
            }
        });
    }

    public void special() {

        // TODO special materials like naq or lanthanides.

    }

    public void gt() {

        // TODO general gt material ore system.

    }

    /**
     * @param m           The material to generating.
     * @param outputGiver Prepare a general output info, and it will be auto created in rich generating.
     */
    public void gtGenerate(Materials m, ItemStacksGiver outputGiver) {

        List<ItemStack> inputs = new ArrayList<>();
        List<ItemStack> inputsRich = new ArrayList<>();

        ItemStack raw = GTOreDictUnificator.get(OrePrefixes.rawOre, m, 1);
        if (raw != null) {
            inputs.add(raw);
        }

        try (OreInfo<Materials> info = OreInfo.getNewInfo()) {
            info.material = m;
            info.isSmall = false;

            for (StoneType st : stoneTypeList) {

                info.stoneType = st;
                info.isNatural = false;

                {
                    ItemStack output = GTOreAdapter.INSTANCE.getStack(info, 1);
                    if (output != null) {
                        inputs.add(output);
                    }
                }

                info.isNatural = true;
                {
                    ItemStack output = GTOreAdapter.INSTANCE.getStack(info, 1);
                    if (output != null) {
                        inputs.add(output);
                    }
                }

            }

            for (StoneType rst : richStoneTypeList) {

                info.stoneType = rst;
                info.isNatural = false;
                {
                    ItemStack output = GTOreAdapter.INSTANCE.getStack(info, 1);
                    if (output != null) {
                        inputsRich.add(output);
                    }
                }

                info.isNatural = true;
                {
                    ItemStack output = GTOreAdapter.INSTANCE.getStack(info, 1);
                    if (output != null) {
                        inputsRich.add(output);
                    }
                }
            }

        }

        // mapping
        ItemStack[] o = outputGiver.getAsArray(1);

        addToVisualRecipeMap(o, inputs.toArray(new ItemStack[0]));

        for (ItemStack i : inputs) {
            TST_ItemID id = TST_ItemID.create(i);
            OP_GIVER_MAP.put(id, outputGiver);
            OP_IO_MAP.put(id, o);
        }

        // mapping rich
        ItemStack[] or = outputGiver.getAsArray(2);

        addToVisualRecipeMap(o, inputsRich.toArray(new ItemStack[0]));

        for (ItemStack i : inputsRich) {
            TST_ItemID id = TST_ItemID.create(i);
            OP_GIVER_MAP.put(id, ItemStacksGiver.create(or));
            OP_IO_MAP.put(id, or);
        }

    }

    // endregion

    // region GTPP

    public void gtpp() {
        Set<Material> mOres = new HashSet<>();
        mOres.add(RARE_EARTH_LOW);
        mOres.add(RARE_EARTH_MID);
        mOres.add(RARE_EARTH_HIGH);
        mOres.add(KOBOLDITE);
        for (Material m : Material.mMaterialMap) {
            if (m == null) continue;
            if (MaterialState.ORE.equals(m.getState())) {
                mOres.add(m);
            }
        }

        gtppGenerate(MaterialsFluorides.FLUORITE, 64);
        mOres.forEach(m -> gtppGenerate(m, 12));
    }

    public void gtppGenerate(Material m, int amount) {
        ItemStack[] output = new ItemStack[] { m.getDust(amount) };
        ItemStack[] inputs = new ItemStack[] { m.getOre(1), m.getRawOre(1) };
        ItemStacksGiver thisGiver = ItemStacksGiver.create(output);
        addToVisualRecipeMap(output, inputs);

        for (ItemStack i : inputs) {
            TST_ItemID id = TST_ItemID.create(i);
            OP_GIVER_MAP.put(id, thisGiver);
            OP_IO_MAP.put(id, output);
        }
    }

    // endregion

    // region Bartworks

    public void bw() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (!werkstoff.hasItemType(ore)) continue;

            ArrayList<ItemStack> outputs = new ArrayList<>();
            // basic output
            outputs.add(werkstoff.get(dust, 4));

            // gem output
            if (werkstoff.hasItemType(gem)) {
                if (werkstoff.hasItemType(gemExquisite)) {
                    outputs.add(werkstoff.get(gemExquisite, 1));
                    outputs.add(werkstoff.get(gemFlawless, 2));
                    outputs.add(werkstoff.get(gem, 2));
                } else {
                    outputs.add(werkstoff.get(gem, 4));
                }
            }

            // byproducts
            if (werkstoff.getNoOfByProducts() >= 1) {
                if (werkstoff.getNoOfByProducts() == 1) {
                    outputs.add(GTUtility.copyAmountUnsafe(3, werkstoff.getOreByProduct(0, dust)));
                } else {
                    for (int i = 0; i < werkstoff.getNoOfByProducts(); i++) {
                        outputs.add(GTUtility.copyAmountUnsafe(2, werkstoff.getOreByProduct(i, dust)));
                    }
                }
            } else {
                outputs.add(werkstoff.get(dust, 3));
            }

            ItemStacksGiver thisGiver = ItemStacksGiver.create(outputs);

            // collecting the input forms
            ArrayList<ItemStack> inputs = new ArrayList<>();

            ItemStack r = werkstoff.get(rawOre, 1);
            if (r != null) inputs.add(r);

            try (OreInfo<Werkstoff> info = OreInfo.getNewInfo()) {
                info.material = werkstoff;
                info.stoneType = StoneType.Stone;
                info.isSmall = false;
                info.isNatural = false;

                inputs.add(BWOreAdapter.INSTANCE.getStack(info, 1));

                info.isNatural = true;
                inputs.add(BWOreAdapter.INSTANCE.getStack(info, 1));

                info.stoneType = StoneType.Moon;
                info.isNatural = false;
                inputs.add(BWOreAdapter.INSTANCE.getStack(info, 1));

                info.isNatural = true;
                inputs.add(BWOreAdapter.INSTANCE.getStack(info, 1));
            }

            // mapping
            ItemStack[] o = thisGiver.getAsArray(1);

            addToVisualRecipeMap(o, inputs.toArray(new ItemStack[0]));

            for (ItemStack i : inputs) {
                TST_ItemID id = TST_ItemID.create(i);
                OP_GIVER_MAP.put(id, thisGiver);
                OP_IO_MAP.put(id, o);
            }

        }
    }

    // endregion

    @Nullable
    public ItemStack getBartWorksOre(Werkstoff material, int amount, StoneType stoneType) {
        try (OreInfo<Werkstoff> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = stoneType;
            info.isSmall = false;
            info.isNatural = false;

            return BWOreAdapter.INSTANCE.getStack(info, amount);
        }
    }

    public void addToVisualRecipeMap(ItemStack[] output, ItemStack... input) {
        OreProcessingVisualRecipes.add(
            new GTRecipe.GTRecipe_WithAlt(
                false,
                null,
                output,
                null,
                null,
                null,
                null,
                null,
                new FluidStack[] { LUBRICANT },
                null,
                OreProcessRecipeDuration,
                OreProcessRecipeEUt,
                0,
                new ItemStack[][] { input },
                null

            ));
    }

}
