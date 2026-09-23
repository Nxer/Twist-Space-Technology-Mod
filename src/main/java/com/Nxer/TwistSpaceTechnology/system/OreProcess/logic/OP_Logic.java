package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.OreProcessingVisualRecipeMap;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeDuration;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeEUt;
import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.SpecialProcessingLineMaterialInstead;
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
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.RUNITE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.api.giver.ItemStacksGiver;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.google.common.collect.Sets;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.GregTechAPI;
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
import gtnhlanth.common.register.WerkstoffMaterialPool;
import ic2.core.Ic2Items;

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

    public static ItemStacksGiver getOutput(TST_ItemID input) {
        return OP_GIVER_MAP.get(input);
    }

    public static ItemStacksGiver getOutput(ItemStack input) {
        return getOutput(TST_ItemID.create(input));
    }

    public final FluidStack LUBRICANT = Materials.Lubricant.getFluid(1);

    public void init() {
        TwistSpaceTechnology.LOG.info("Initializing TST Ore Processing System.");
        prepare();
        gt();
        gtpp();
        bw();
        special();
        TwistSpaceTechnology.LOG.info("TST Ore Processing System initialized.");
    }

    // region GT
    List<StoneType> stoneTypeList = new ArrayList<>();
    List<StoneType> richStoneTypeList = new ArrayList<>();

    // mapping special materials about special ore resource processing line using
    Map<Materials, ItemStack> processingLineMaterials = new HashMap<>();

    public void prepare() {
        STONE_TYPES.forEach(s -> {
            if (s.isRich()) {
                richStoneTypeList.add(s);
            } else {
                stoneTypeList.add(s);
            }
        });

        processingLineMaterials.put(Materials.Platinum, WerkstoffLoader.PTMetallicPowder.get(OrePrefixes.dust, 1));
        processingLineMaterials.put(Materials.Palladium, WerkstoffLoader.PDMetallicPowder.get(OrePrefixes.dust, 1));
        processingLineMaterials.put(Materials.Iridium, WerkstoffLoader.IrLeachResidue.get(OrePrefixes.dust, 1));
        processingLineMaterials.put(Materials.Osmium, WerkstoffLoader.IrOsLeachResidue.get(OrePrefixes.dust, 1));
        processingLineMaterials
            .put(Materials.Samarium, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
        processingLineMaterials
            .put(Materials.Cerium, WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 1));

    }

    public void special() {

        // TODO special materials like naq or lanthanides.

        // Cerium
        gtGenerate(
            Materials.Cerium,
            ItemStacksGiver.create(WerkstoffMaterialPool.CeriumOreConcentrate.get(OrePrefixes.dust, 11)));

        // Samarium
        gtGenerate(
            Materials.Samarium,
            ItemStacksGiver.create(WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 11)));

        // Naquadah
        gtGenerate(
            Materials.Naquadah,
            ItemStacksGiver.create(
                GGMaterial.naquadahEarth.get(OrePrefixes.dust, 8),
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 4)));

        // NaquadahEnriched
        gtGenerate(
            Materials.NaquadahEnriched,
            ItemStacksGiver.create(
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 8),
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 4)));

        // Naquadria
        gtGenerate(
            Materials.Naquadria,
            ItemStacksGiver.create(
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 8),
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 4)));

        // Tinker Construct
        // Cobalt ore
        addOne(
            createGTOreOutputs(Materials.Cobalt),
            getModItem("TConstruct", "SearedBrick", 1, 1),
            getModItem("TConstruct", "GravelOre", 1, 5));

        // Ardite ore
        addOne(createGTOreOutputs(Materials.Ardite), getModItem("TConstruct", "SearedBrick", 1, 2));

        // IC2 Uranium ore
        addOne(createGTOreOutputs(Materials.Uranium), GTUtility.copyAmountUnsafe(1, Ic2Items.uraniumOre));

        // HEE end powder
        addOne(
            ItemStacksGiver.create(getModItem("HardcoreEnderExpansion", "end_powder", 24)),
            getModItem("HardcoreEnderExpansion", "end_powder_ore", 1));

        // Minecraft Iron ore
        addOne(createGTOreOutputs(Materials.Iron), new ItemStack(Blocks.iron_ore));

    }

    public void gt() {
        Set<Materials> skipThese = Sets.newHashSet(
            Materials.Cerium,
            Materials.Samarium,
            Materials.Naquadah,
            Materials.NaquadahEnriched,
            Materials.Naquadria);

        for (Materials m : GregTechAPI.sGeneratedMaterials) {
            if (null == m) continue;

            if (!skipThese.isEmpty() && skipThese.contains(m)) {
                skipThese.remove(m);
                continue;
            }

            if (GTOreDictUnificator.get(OrePrefixes.ore, m, 1) == null) {
                // TwistSpaceTechnology.LOG.info("Skip a non-ore material {}", m);
                continue;
            }

            gtGenerate(m, createGTOreOutputs(m));

        }

    }

    public ItemStack getDustStack(Materials material, int amount) {
        if (SpecialProcessingLineMaterialInstead) {
            ItemStack t = processingLineMaterials.get(material);
            if (t != null) {
                return GTUtility.copyAmountUnsafe(amount * 3, t);
            }
        }
        return GTUtility.copyAmountUnsafe(amount, GTOreDictUnificator.get(OrePrefixes.dust, material, 1));
    }

    public ItemStacksGiver createGTOreOutputs(Materials material) {
        List<ItemStack> outputs = new ArrayList<>();

        // check byproduct
        if (!material.mOreByProducts.isEmpty()) {
            // the basic output the material
            outputs.add(getDustStack(material, 4));
            if (material.mOreByProducts.size() == 1) {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null) continue;
                    outputs.add(getDustStack(byproduct, 3));
                }
            } else {
                for (Materials byproduct : material.mOreByProducts) {
                    if (byproduct == null || byproduct == Materials.Netherrack
                        || byproduct == Materials.Endstone
                        || byproduct == Materials.Stone) continue;

                    outputs.add(getDustStack(byproduct, 2));
                }
            }

        } else {
            outputs.add(getDustStack(material, 8));
        }

        // check gem style
        if (GTOreDictUnificator.get(OrePrefixes.gem, material, 1) != null) {
            if (GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1) != null) {
                // has gem style
                outputs.add(GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, 1));
                outputs.add(GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, 2));
                outputs.add(GTOreDictUnificator.get(OrePrefixes.gem, material, 2));

            } else {
                // just normal gem
                outputs.add(GTOreDictUnificator.get(OrePrefixes.gem, material, 4));
            }
        }

        return ItemStacksGiver.create(outputs);

    }

    /**
     * @param m           The material to generating. This method will auto generate all stone type for input.
     * @param outputGiver Prepare a general output info, and it will be auto created in rich generating.
     */
    public void gtGenerate(Materials m, ItemStacksGiver outputGiver) {

        if (outputGiver == null) {
            TwistSpaceTechnology.LOG.warn("Null outputGiver with {}", m);
            return;
        }

        if (outputGiver.isEmpty()) {
            TwistSpaceTechnology.LOG.warn("Empty outputGiver with {}", m);
            return;
        }

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

        // sanity checking
        if (inputs.isEmpty()) {
            TwistSpaceTechnology.LOG.warn("Empty input ore form with {}", m);
            return;
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

        addToVisualRecipeMap(or, inputsRich.toArray(new ItemStack[0]));

        for (ItemStack i : inputsRich) {
            TST_ItemID id = TST_ItemID.create(i);
            OP_GIVER_MAP.put(id, ItemStacksGiver.create(or));
            OP_IO_MAP.put(id, or);
        }

    }

    public void addOne(ItemStacksGiver outputs, ItemStack... inputs) {
        addToVisualRecipeMap(outputs.getAsArray(1), inputs);
        for (ItemStack i : inputs) {
            TST_ItemID id = TST_ItemID.create(i);
            OP_GIVER_MAP.put(id, outputs);
            OP_IO_MAP.put(id, outputs.getAsArray(1));
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
        mOres.add(RUNITE);
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

    public void addToVisualRecipeMap(ItemStack[] output, ItemStack... input) {
        OreProcessingVisualRecipeMap.add(
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
