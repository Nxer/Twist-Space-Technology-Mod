package com.Nxer.TwistSpaceTechnology.command;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.OrePrefixes.ParsedOreDictName;
import gregtech.api.util.GTOreDictUnificator;

/**
 * Turns an ItemStack into a source expression that can be pasted into recipe code.
 *
 * Resolution order:
 * 1. Material form: the stack has an ore dict tag with a material prefix. GT materials use
 * GTOreDictUnificator.get(OrePrefixes.X, <material>, n); GT++ and BartWorks materials use their own getters,
 * e.g. MaterialsElements.STANDALONE.HYPOGEN.getIngot(1) or GGMaterial.signalium.get(OrePrefixes.gearGt, 1).
 * 2. Mod class form: known item registries of the item's mod are scanned in order, e.g. ItemList.X.get(n),
 * GTCMItemList.X.get(n) or new ItemStack(TstBlocks.X, n, meta).
 * 3. If neither matches, null is returned and the caller falls back to ModItemHandler.ModItem.getModItem.
 */
public final class ContainerItemSourceResolver {

    /** Registries of TST items. */
    private static final String[] TST_REFERENCE_CLASSES = { "com.Nxer.TwistSpaceTechnology.common.GTCMItemList",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialPool",
        "com.Nxer.TwistSpaceTechnology.common.init.TstBlocks" };

    /** TST machines and GT++ material items use gregtech ids, so check their registries too. */
    private static final String[] GREGTECH_REFERENCE_CLASSES = { "com.Nxer.TwistSpaceTechnology.common.GTCMItemList",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialPool",
        "com.Nxer.TwistSpaceTechnology.common.init.TstBlocks", "gregtech.api.enums.ItemList",
        "gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList", "gtPlusPlus.core.material.MaterialsAlloy",
        "gtPlusPlus.core.material.MaterialsElements", "gtPlusPlus.core.material.MaterialsElements$STANDALONE",
        "gtPlusPlus.core.material.MaterialMisc", "gtPlusPlus.core.material.MaterialsOres",
        "gtPlusPlus.core.material.MaterialsOther", "gtPlusPlus.core.material.nuclear.MaterialsFluorides",
        "gtPlusPlus.core.material.nuclear.MaterialsNuclides", "gtPlusPlus.core.block.ModBlocks",
        "bartworks.system.material.WerkstoffLoader", "goodgenerator.items.GGMaterial",
        "gtnhlanth.common.register.LanthItemList", "gtnhlanth.common.register.WerkstoffMaterialPool",
        "gtnhlanth.common.register.BotWerkstoffMaterialPool" };

    /** Registries of GT++ items. */
    private static final String[] GTPLUSPLUS_REFERENCE_CLASSES = {
        "gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList", "gtPlusPlus.core.material.MaterialsAlloy",
        "gtPlusPlus.core.material.MaterialsElements", "gtPlusPlus.core.material.MaterialsElements$STANDALONE",
        "gtPlusPlus.core.material.MaterialMisc", "gtPlusPlus.core.material.MaterialsOres",
        "gtPlusPlus.core.material.MaterialsOther", "gtPlusPlus.core.material.nuclear.MaterialsFluorides",
        "gtPlusPlus.core.material.nuclear.MaterialsNuclides", "gtPlusPlus.core.block.ModBlocks",
        "gtPlusPlus.core.item.ModItems", "gtPlusPlus.core.item.init.ItemsFoods" };

    /** Registries of BartWorks items. */
    private static final String[] BARTWORKS_REFERENCE_CLASSES = { "bartworks.system.material.WerkstoffLoader",
        "bartworks.common.loaders.BioItemList" };

    /** Registries of GoodGenerator items. */
    private static final String[] GOODGENERATOR_REFERENCE_CLASSES = { "goodgenerator.util.ItemRefer",
        "goodgenerator.items.GGMaterial" };

    /** Registries of GTNH-Lanth items. */
    private static final String[] GTNHLANTH_REFERENCE_CLASSES = { "gtnhlanth.common.register.LanthItemList",
        "gtnhlanth.common.register.WerkstoffMaterialPool", "gtnhlanth.common.register.BotWerkstoffMaterialPool" };

    /** Registries of Thaumcraft items. */
    private static final String[] THAUMCRAFT_REFERENCE_CLASSES = { "thaumcraft.common.config.ConfigItems",
        "thaumcraft.common.config.ConfigBlocks" };

    /** Fallback list, scanned when no per-mod list matched. */
    private static final String[] ALL_REFERENCE_CLASSES = { "com.Nxer.TwistSpaceTechnology.common.GTCMItemList",
        "gregtech.api.enums.ItemList", "gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList", "ggfab.GGItemList",
        "kubatech.api.enums.ItemList", "tectech.thing.CustomItemList", "goodgenerator.util.ItemRefer",
        "goodgenerator.items.GGMaterial", "bartworks.system.material.WerkstoffLoader",
        "gtnhlanth.common.register.LanthItemList", "gtnhlanth.common.register.WerkstoffMaterialPool",
        "gtnhlanth.common.register.BotWerkstoffMaterialPool", "gtnhintergalactic.recipe.SpaceMiningRecipes",
        "bartworks.common.loaders.BioItemList", "gtPlusPlus.core.material.MaterialsAlloy",
        "gtPlusPlus.core.material.MaterialsElements", "gtPlusPlus.core.material.MaterialsElements$STANDALONE",
        "gtPlusPlus.core.material.MaterialsOres", "gtPlusPlus.core.material.MaterialsOther",
        "gtPlusPlus.core.material.nuclear.MaterialsFluorides", "gtPlusPlus.core.material.nuclear.MaterialsNuclides",
        "gtPlusPlus.core.material.MaterialMisc", "gtPlusPlus.core.block.ModBlocks", "gtPlusPlus.core.item.ModItems",
        "gtPlusPlus.core.item.init.ItemsFoods", "kekztech.Items", "kekztech.common.Blocks",
        "thaumcraft.common.config.ConfigItems", "thaumcraft.common.config.ConfigBlocks",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialPool",
        "com.Nxer.TwistSpaceTechnology.common.init.TstBlocks" };

    /** Classes holding TST materials (GT Materials instances). */
    private static final String[] TST_MATERIAL_CLASSES = { "com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialPool" };

    /** Classes holding GT++ materials. */
    private static final String[] GTPLUSPLUS_MATERIAL_CLASSES = { "gtPlusPlus.core.material.MaterialsAlloy",
        "gtPlusPlus.core.material.MaterialsElements", "gtPlusPlus.core.material.MaterialsElements$STANDALONE",
        "gtPlusPlus.core.material.MaterialsOres", "gtPlusPlus.core.material.MaterialsOther",
        "gtPlusPlus.core.material.MaterialMisc", "gtPlusPlus.core.material.nuclear.MaterialsFluorides",
        "gtPlusPlus.core.material.nuclear.MaterialsNuclides" };

    /** Classes holding Werkstoffe; GoodGenerator and Lanth declare their own. */
    private static final String[] WERKSTOFF_OWNER_CLASSES = { "bartworks.system.material.WerkstoffLoader",
        "goodgenerator.items.GGMaterial", "gtnhlanth.common.register.WerkstoffMaterialPool",
        "gtnhlanth.common.register.BotWerkstoffMaterialPool" };

    /** Classes scanned for molten/gas fluids, in order. */
    private static final String[] FLUID_OWNER_CLASSES = { "gregtech.api.enums.Materials",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST",
        "com.Nxer.TwistSpaceTechnology.common.material.MaterialPool", "bartworks.system.material.WerkstoffLoader",
        "goodgenerator.items.GGMaterial", "gtnhlanth.common.register.WerkstoffMaterialPool",
        "gtnhlanth.common.register.BotWerkstoffMaterialPool", "gtPlusPlus.core.material.MaterialsAlloy",
        "gtPlusPlus.core.material.MaterialsElements", "gtPlusPlus.core.material.MaterialsElements$STANDALONE",
        "gtPlusPlus.core.material.MaterialsOres", "gtPlusPlus.core.material.MaterialsOther",
        "gtPlusPlus.core.material.MaterialMisc", "gtPlusPlus.core.material.nuclear.MaterialsFluorides",
        "gtPlusPlus.core.material.nuclear.MaterialsNuclides" };

    /** GT++ item getters, indexed by ore prefix name. */
    private static final Map<String, String> GTPLUSPLUS_GETTERS = new HashMap<>();

    /** Prefixes that must not count as material forms. */
    private static final List<String> NON_MATERIAL_PREFIXES = Collections.singletonList("circuit");

    /** Item getters probed on material instances; the identity check picks the right one. */
    private static final String[] MATERIAL_METHODS = { "getDust", "getDustSmall", "getDustTiny", "getSmallDust",
        "getTinyDust", "getIngot", "getIngots", "getHotIngot", "getNugget", "getNuggets", "getPlate", "getPlates",
        "getPlateDouble", "getPlateDense", "getGem", "getGems", "getBlock", "getBlocks", "getCell", "getCells",
        "getGear", "getGearSmall", "getRod", "getLongRod", "getBolt", "getScrew", "getRing", "getRotor", "getFrameBox",
        "getFoil", "getFineWire", "getOre", "getNanite", "getWire01", "getWire02", "getWire04", "getWire08",
        "getWire12", "getWire16", "getCable01", "getCable02", "getCable04", "getCable08", "getCable12", "getCable16" };

    /** Cache for resolved stacks. */
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    static {
        GTPLUSPLUS_GETTERS.put("ingot", "getIngot");
        GTPLUSPLUS_GETTERS.put("nugget", "getNugget");
        GTPLUSPLUS_GETTERS.put("dust", "getDust");
        GTPLUSPLUS_GETTERS.put("dustSmall", "getSmallDust");
        GTPLUSPLUS_GETTERS.put("dustTiny", "getTinyDust");
        GTPLUSPLUS_GETTERS.put("plate", "getPlate");
        GTPLUSPLUS_GETTERS.put("plateDouble", "getPlateDouble");
        GTPLUSPLUS_GETTERS.put("plateDense", "getPlateDense");
        GTPLUSPLUS_GETTERS.put("gear", "getGear");
        GTPLUSPLUS_GETTERS.put("gearGt", "getGear");
        GTPLUSPLUS_GETTERS.put("gearSmall", "getGearSmall");
        GTPLUSPLUS_GETTERS.put("rod", "getRod");
        GTPLUSPLUS_GETTERS.put("rodLong", "getLongRod");
        GTPLUSPLUS_GETTERS.put("bolt", "getBolt");
        GTPLUSPLUS_GETTERS.put("screw", "getScrew");
        GTPLUSPLUS_GETTERS.put("ring", "getRing");
        GTPLUSPLUS_GETTERS.put("rotor", "getRotor");
        GTPLUSPLUS_GETTERS.put("frameGt", "getFrameBox");
        GTPLUSPLUS_GETTERS.put("cell", "getCell");
        GTPLUSPLUS_GETTERS.put("foil", "getFoil");
        GTPLUSPLUS_GETTERS.put("wireGt01", "getWire01");
        GTPLUSPLUS_GETTERS.put("wireGt02", "getWire02");
        GTPLUSPLUS_GETTERS.put("wireGt04", "getWire04");
        GTPLUSPLUS_GETTERS.put("wireGt08", "getWire08");
        GTPLUSPLUS_GETTERS.put("wireGt12", "getWire12");
        GTPLUSPLUS_GETTERS.put("wireGt16", "getWire16");
        GTPLUSPLUS_GETTERS.put("cableGt01", "getCable01");
        GTPLUSPLUS_GETTERS.put("cableGt02", "getCable02");
        GTPLUSPLUS_GETTERS.put("cableGt04", "getCable04");
        GTPLUSPLUS_GETTERS.put("cableGt08", "getCable08");
        GTPLUSPLUS_GETTERS.put("cableGt12", "getCable12");
        GTPLUSPLUS_GETTERS.put("cableGt16", "getCable16");
        GTPLUSPLUS_GETTERS.put("ore", "getOre");
        GTPLUSPLUS_GETTERS.put("block", "getBlock");
    }

    private ContainerItemSourceResolver() {}

    /**
     * Resolves the stack to a source expression, or null to fall back to getModItem.
     * Each tier is protected, so one failure never breaks the whole dump.
     */
    public static String resolve(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;
        try {
            String key = key(stack);
            String cached = CACHE.get(key);
            if (cached != null) return cached;

            List<ParsedOreDictName> parsed = parseOreDict(stack);

            String result = null;
            try {
                result = resolveMaterialForm(stack, parsed);
            } catch (Throwable t) {
                TwistSpaceTechnology.LOG.error("[TST Dump] material form failed for " + stack, t);
            }
            if (result == null) {
                try {
                    result = resolveKnownLists(stack, parsed);
                } catch (Throwable t) {
                    TwistSpaceTechnology.LOG.error("[TST Dump] class form failed for " + stack, t);
                }
            }
            if (result != null) CACHE.put(key, result);
            else TwistSpaceTechnology.LOG.debug("[TST Dump] no source form for " + stack + " -> getModItem fallback");
            return result;
        } catch (Throwable t) {
            // One bad item must not break the whole dump.
            TwistSpaceTechnology.LOG.error("[TST Dump] resolve failed for " + stack, t);
            return null;
        }
    }

    // ----Material form---- //

    private static List<ParsedOreDictName> parseOreDict(ItemStack stack) {
        try {
            List<ParsedOreDictName> parsed = OrePrefixes.detectPrefix(stack);
            TwistSpaceTechnology.LOG.debug("[TST Dump] ore dict tags of {} parsed: {}", stack, parsed);
            return parsed;
        } catch (Throwable t) {
            TwistSpaceTechnology.LOG.error("[TST Dump] OrePrefixes.detectPrefix failed for " + stack, t);
            return Collections.emptyList();
        }
    }

    private static String resolveMaterialForm(ItemStack stack, List<ParsedOreDictName> parsedList) {
        int size = stack.stackSize;
        for (ParsedOreDictName parsed : parsedList) {
            if (parsed == null || parsed.prefix == null || !isMaterialPrefix(parsed.prefix)) continue;
            String materialName = parsed.material;
            if (materialName == null || materialName.isEmpty()) continue;
            OrePrefixes prefix = parsed.prefix;
            String oredictName = prefix.name() + materialName;

            // GT material system: Materials.<Name> must be a real field.
            if (hasStaticField(Materials.class, materialName)) {
                return "GTOreDictUnificator.get(OrePrefixes." + prefix
                    .name() + ", Materials." + materialName + ", " + size + ")";
            }

            // TST materials, e.g. MaterialsTST.NeutroniumAlloy.
            try {
                String tst = probeMaterialFields(TST_MATERIAL_CLASSES, stack, size, prefix);
                if (tst != null) return tst;
            } catch (Throwable t) {
                TwistSpaceTechnology.LOG.error("[TST Dump] TST material scan failed for " + stack, t);
            }

            // GT++ materials, e.g. MaterialsElements.STANDALONE.HYPOGEN.
            try {
                String gtpp = probeGtPlusPlus(stack, size, prefix);
                if (gtpp != null) return gtpp;
            } catch (Throwable t) {
                TwistSpaceTechnology.LOG.error("[TST Dump] GT++ material scan failed for " + stack, t);
            }

            // Werkstoffe, e.g. GGMaterial.signalium or WerkstoffLoader.Zirconium.
            try {
                String bart = probeBart(stack, size, prefix);
                if (bart != null) return bart;
            } catch (Throwable t) {
                TwistSpaceTechnology.LOG.error("[TST Dump] BartWorks material scan failed for " + stack, t);
            }

            // Fallback: ore dict name, works for any registered entry.
            return "GTOreDictUnificator.get(\"" + oredictName + "\", " + size + ")";
        }
        return null;
    }

    private static boolean isMaterialPrefix(OrePrefixes prefix) {
        if (prefix == null || !prefix.isMaterialBased()) return false;
        String name = prefix.name();
        for (String excluded : NON_MATERIAL_PREFIXES) {
            if (excluded.equals(name)) return false;
        }
        return true;
    }

    /** Finds a GT Materials field that produces the stack for the given prefix. */
    private static String probeMaterialFields(String[] ownerClasses, ItemStack stack, int size, OrePrefixes prefix) {
        for (String className : ownerClasses) {
            Class<?> type = loadClass(className);
            if (type == null) continue;
            String owner = sourceType(type);
            for (Field field : allFields(type)) {
                if (!Modifier.isStatic(field.getModifiers())) continue;
                Object value = readStatic(field);
                if (!(value instanceof Materials)) continue;
                ItemStack generated = GTOreDictUnificator.get(prefix, value, (long) size);
                if (sameIdentity(generated, stack)) {
                    return "GTOreDictUnificator.get(OrePrefixes." + prefix
                        .name() + ", " + owner + "." + field.getName() + ", " + size + ")";
                }
            }
        }
        return null;
    }

    private static String probeGtPlusPlus(ItemStack stack, int size, OrePrefixes prefix) {
        String getter = GTPLUSPLUS_GETTERS.get(prefix.name());
        if (getter == null) return null;
        Class<?> gtppMaterial = loadClass("gtPlusPlus.core.material.Material");
        if (gtppMaterial == null) return null;
        for (String className : GTPLUSPLUS_MATERIAL_CLASSES) {
            Class<?> type = loadClass(className);
            if (type == null) continue;
            String owner = sourceType(type);
            for (Field field : allFields(type)) {
                if (!Modifier.isStatic(field.getModifiers())) continue;
                Object value = readStatic(field);
                if (value == null || !gtppMaterial.isInstance(value)) continue;
                ItemStack generated = invokeGetter(value, getter, size);
                if (sameIdentity(generated, stack)) {
                    return owner + "." + field.getName() + "." + getter + "(" + size + ")";
                }
            }
        }
        return null;
    }

    /** Finds a Werkstoff whose {@code get(prefix, size)} matches the stack. */
    private static String probeBart(ItemStack stack, int size, OrePrefixes prefix) {
        for (String className : WERKSTOFF_OWNER_CLASSES) {
            Class<?> loader = loadClass(className);
            if (loader == null) continue;
            String owner = sourceType(loader);
            for (Field field : allFields(loader)) {
                if (!Modifier.isStatic(field.getModifiers())) continue;
                Object value = readStatic(field);
                if (value == null) continue;
                ItemStack generated = invokeGet(value, prefix, size);
                if (sameIdentity(generated, stack)) {
                    return owner + "." + field.getName() + ".get(OrePrefixes." + prefix.name() + ", " + size + ")";
                }
            }
        }
        return null;
    }

    // ----Fluid form---- //

    /**
     * Resolves a fluid to a source expression, e.g. {@code Materials.RadoxPolymer.getMolten(1152)} for GT
     * materials or {@code GGMaterial.signalium.getMolten(1152)} for Werkstoffe. Falls back to
     * {@code FluidRegistry.getFluidStack(name, n)}.
     */
    public static String resolveFluid(FluidStack fluid) {
        if (fluid == null || fluid.getFluid() == null) return null;
        try {
            String fluidName = fluid.getFluid()
                .getName();
            String key = "fluid:" + fluidName + ":" + fluid.amount;
            String cached = CACHE.get(key);
            if (cached != null) return cached;

            String result = null;
            try {
                result = resolveFluidOwner(fluidName, fluid.amount);
            } catch (Throwable t) {
                TwistSpaceTechnology.LOG.error("[TST Dump] fluid owner scan failed for " + fluidName, t);
            }
            if (result == null) result = "FluidRegistry.getFluidStack(\"" + fluidName + "\", " + fluid.amount + ")";
            CACHE.put(key, result);
            return result;
        } catch (Throwable t) {
            TwistSpaceTechnology.LOG.error("[TST Dump] resolveFluid failed for " + fluid, t);
            return null;
        }
    }

    private static String resolveFluidOwner(String fluidName, int amount) {
        Class<?> werkstoffType = loadClass("bartworks.system.material.Werkstoff");
        Class<?> gtppMaterialType = loadClass("gtPlusPlus.core.material.Material");
        for (String className : FLUID_OWNER_CLASSES) {
            Class<?> type = loadClass(className);
            if (type == null) continue;
            String owner = sourceType(type);
            for (Field field : allFields(type)) {
                if (!Modifier.isStatic(field.getModifiers())) continue;
                Object value = readStatic(field);
                if (value == null) continue;
                if (value instanceof Materials) {
                    String form = matchFluidGetter(
                        value,
                        field.getName(),
                        owner,
                        fluidName,
                        amount,
                        new String[] { "getMolten", "getGas", "getFluid" },
                        long.class);
                    if (form != null) return form;
                } else if (werkstoffType != null && werkstoffType.isInstance(value)) {
                    String form = matchFluidGetter(
                        value,
                        field.getName(),
                        owner,
                        fluidName,
                        amount,
                        new String[] { "getMolten", "getFluidOrGas" },
                        int.class);
                    if (form != null) return form;
                } else if (gtppMaterialType != null && gtppMaterialType.isInstance(value)) {
                    String form = matchFluidGetter(
                        value,
                        field.getName(),
                        owner,
                        fluidName,
                        amount,
                        new String[] { "getFluidStack" },
                        int.class);
                    if (form != null) return form;
                }
            }
        }
        return null;
    }

    private static String matchFluidGetter(Object owner, String fieldName, String ownerType, String fluidName,
        int amount, String[] getterNames, Class<?> amountType) {
        for (String getterName : getterNames) {
            try {
                Method method = owner.getClass()
                    .getMethod(getterName, amountType);
                Object probeArg = amountType == long.class ? (Object) 1L : (Object) 1;
                Object generated = method.invoke(owner, probeArg);
                if (generated instanceof FluidStack) {
                    FluidStack probe = (FluidStack) generated;
                    if (probe.getFluid() != null && fluidName.equals(
                        probe.getFluid()
                            .getName())) {
                        return ownerType + "." + fieldName + "." + getterName + "(" + amount + ")";
                    }
                }
            } catch (Throwable ignored) {
                // Optional integrations may not implement every fluid accessor.
            }
        }
        return null;
    }

    // ----Mod class form---- //

    private static String resolveKnownLists(ItemStack stack, List<ParsedOreDictName> parsedList) {
        String[] preferred = referenceClasses(stack);
        for (String className : preferred) {
            String result = safeResolveClass(className, stack, parsedList);
            if (result != null) return result;
        }
        for (String className : ALL_REFERENCE_CLASSES) {
            if (contains(preferred, className)) continue;
            String result = safeResolveClass(className, stack, parsedList);
            if (result != null) return result;
        }
        return null;
    }

    private static String safeResolveClass(String className, ItemStack stack, List<ParsedOreDictName> parsedList) {
        try {
            return resolveClass(className, stack, parsedList);
        } catch (Throwable t) {
            TwistSpaceTechnology.LOG
                .error("[TST Dump] registry scan failed for " + className + " (stack " + stack + ")", t);
            return null;
        }
    }

    private static boolean contains(String[] values, String target) {
        for (String value : values) {
            if (value.equals(target)) return true;
        }
        return false;
    }

    private static String[] referenceClasses(ItemStack stack) {
        String modId = "";
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (id != null && id.modId != null) modId = id.modId.toLowerCase();
        String itemClass = stack.getItem()
            .getClass()
            .getName()
            .toLowerCase();

        if (modId.equals("twistspacetechnology") || itemClass.startsWith("com.nxer.twistspacetechnology")) {
            return TST_REFERENCE_CLASSES;
        }
        if (modId.equals("gregtech") || itemClass.startsWith("gregtech.")) {
            return GREGTECH_REFERENCE_CLASSES;
        }
        if (modId.equals("gtplusplus") || modId.equals("miscutils") || itemClass.startsWith("gtplusplus.")) {
            return GTPLUSPLUS_REFERENCE_CLASSES;
        }
        if (modId.equals("bartworks") || modId.equals("bwcrossmod") || itemClass.startsWith("bartworks.")) {
            return BARTWORKS_REFERENCE_CLASSES;
        }
        if (modId.equals("goodgenerator") || itemClass.startsWith("goodgenerator.")) {
            return GOODGENERATOR_REFERENCE_CLASSES;
        }
        if (modId.equals("gtnhlanth") || itemClass.startsWith("gtnhlanth.")) {
            return GTNHLANTH_REFERENCE_CLASSES;
        }
        if (modId.equals("thaumcraft") || itemClass.startsWith("thaumcraft.")) {
            return THAUMCRAFT_REFERENCE_CLASSES;
        }
        if (modId.equals("ggfab")) return new String[] { "ggfab.GGItemList" };
        if (modId.equals("kubatech")) return new String[] { "kubatech.api.enums.ItemList" };
        if (modId.equals("tectech")) return new String[] { "tectech.thing.CustomItemList" };
        if (modId.equals("kekztech")) return new String[] { "kekztech.Items", "kekztech.common.Blocks" };
        if (modId.equals("gtnhintergalactic")) return new String[] { "gtnhintergalactic.recipe.SpaceMiningRecipes" };
        return new String[0];
    }

    private static String resolveClass(String className, ItemStack stack, List<ParsedOreDictName> parsedList) {
        final Class<?> type = loadClass(className);
        if (type == null) return null;

        if (type.isEnum()) {
            String ownerType = displayType(type);
            Method[] methods = type.getMethods();
            // Cheap raw-stack compare first.
            for (Object constant : type.getEnumConstants()) {
                String result = rawStackMatch(constant, ownerType, ((Enum<?>) constant).name(), stack, methods);
                if (result != null) return result;
            }
            // Full matching, but only for entries that were set.
            for (Object constant : type.getEnumConstants()) {
                if (!hasBeenSet(constant, methods)) continue;
                String result = invokeItemFactory(
                    constant,
                    ownerType,
                    ((Enum<?>) constant).name(),
                    stack,
                    parsedList,
                    methods);
                if (result != null) return result;
            }
        }

        String sourceType = sourceType(type);
        for (Field field : allFields(type)) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            try {
                Object value = readStatic(field);
                if (value instanceof ItemStack && sameStack((ItemStack) value, stack)) {
                    return copyAmount(sourceType + "." + field.getName(), stack.stackSize);
                }
                if (value instanceof ItemStack[]) {
                    ItemStack[] stacks = (ItemStack[]) value;
                    for (int index = 0; index < stacks.length; index++) {
                        if (sameStack(stacks[index], stack)) {
                            return copyAmount(sourceType + "." + field.getName() + "[" + index + "]", stack.stackSize);
                        }
                    }
                }
                if (value instanceof Item && sameItem((Item) value, stack.getItem())) {
                    return "new ItemStack(" + sourceType
                        + "."
                        + field.getName()
                        + ", "
                        + stack.stackSize
                        + ", "
                        + stack.getItemDamage()
                        + ")";
                }
                if (value instanceof Block && sameBlock((Block) value, stack.getItem())) {
                    return "new ItemStack(" + sourceType
                        + "."
                        + field.getName()
                        + ", "
                        + stack.stackSize
                        + ", "
                        + stack.getItemDamage()
                        + ")";
                }
                if (value instanceof Materials) {
                    // Materials owned by other classes, e.g. MaterialsTST.
                    String result = invokeItemFactory(
                        value,
                        sourceType,
                        field.getName(),
                        stack,
                        parsedList,
                        value.getClass()
                            .getMethods());
                    if (result != null) return result;
                }
                String result = invokeItemFactory(
                    value,
                    sourceType,
                    field.getName(),
                    stack,
                    parsedList,
                    value.getClass()
                        .getMethods());
                if (result != null) return result;
            } catch (Throwable ignored) {
                // Optional integrations may be absent or not initialized.
            }
        }

        // Some registries expose their entries through getInstance().
        try {
            Method getter = type.getMethod("getInstance");
            if (Modifier.isStatic(getter.getModifiers()) && getter.getParameterTypes().length == 0) {
                Object instance = getter.invoke(null);
                if (instance != null)
                    return resolveInstanceFields(instance, sourceType + ".getInstance()", stack, parsedList);
            }
        } catch (Throwable ignored) {
            // Optional integrations may not expose a singleton.
        }
        return null;
    }

    private static String resolveInstanceFields(Object instance, String owner, ItemStack stack,
        List<ParsedOreDictName> parsedList) {
        for (Field field : allFields(instance.getClass())) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            try {
                Object value = field.get(instance);
                Method[] methods = value == null ? new Method[0]
                    : value.getClass()
                        .getMethods();
                String result = invokeItemFactory(value, owner, field.getName(), stack, parsedList, methods);
                if (result != null) return result;
            } catch (Throwable ignored) {
                // Ignore unavailable optional fields.
            }
        }
        return null;
    }

    private static String invokeItemFactory(Object owner, String ownerType, String ownerName, ItemStack stack,
        List<ParsedOreDictName> parsedList, Method[] methods) {
        if (owner == null) return null;

        // Fast path: compare the raw registered stack.
        String raw = rawStackMatch(owner, ownerType, ownerName, stack, methods);
        if (raw != null) return raw;

        // GT-style item containers: isStackEqual.
        for (Method method : methods) {
            if (!"isStackEqual".equals(method.getName()) || method.getParameterTypes().length != 1) continue;
            try {
                if (Boolean.TRUE.equals(method.invoke(owner, stack))) {
                    return ownerType + "." + ownerName + ".get(" + stack.stackSize + ")";
                }
            } catch (Throwable ignored) {
                // Optional lists may use a different signature.
            }
        }

        // TST item lists use equal(ItemStack), item + damage only.
        for (Method method : methods) {
            if (!"equal".equals(method.getName()) || method.getParameterTypes().length != 1) continue;
            try {
                if (Boolean.TRUE.equals(method.invoke(owner, stack))) {
                    return ownerType + "." + ownerName + ".get(" + stack.stackSize + ")";
                }
            } catch (Throwable ignored) {
                // Optional lists may use a different signature.
            }
        }

        // get(amount)
        for (Method method : methods) {
            if (!"get".equals(method.getName()) || method.getParameterTypes().length != 1) continue;
            Class<?> parameter = method.getParameterTypes()[0];
            if (!(parameter == long.class || parameter == Long.class
                || parameter == int.class
                || parameter == Integer.class)) continue;
            ItemStack generated = invoke(method, owner, stack.stackSize);
            if (sameIdentity(generated, stack)) return ownerType + "." + ownerName + ".get(" + stack.stackSize + ")";
        }

        // GT5U-style item containers: get(amount, Object...).
        for (Method method : methods) {
            Class<?>[] parameters = method.getParameterTypes();
            if (!"get".equals(method.getName()) || parameters.length != 2
                || parameters[1] != Object[].class
                || !(parameters[0] == long.class || parameters[0] == Long.class
                    || parameters[0] == int.class
                    || parameters[0] == Integer.class))
                continue;
            ItemStack generated = invoke(method, owner, amountArgument(parameters[0], stack.stackSize), new Object[0]);
            if (sameIdentity(generated, stack)) return ownerType + "." + ownerName + ".get(" + stack.stackSize + ")";
        }

        // Werkstoff-style get(OrePrefixes, amount); only probe the stack's own prefixes.
        // Werkstoff.get() throws and logs "NO SUCH ITEM!" when the prefix is not generated, so gate on hasItemType().
        for (Method method : methods) {
            if (!"get".equals(method.getName()) || method.getParameterTypes().length != 2
                || method.getParameterTypes()[0] != OrePrefixes.class) continue;
            for (ParsedOreDictName parsed : parsedList) {
                if (parsed == null || parsed.prefix == null) continue;
                if (!supportsPrefix(owner, parsed.prefix)) continue;
                ItemStack generated = invoke(method, owner, parsed.prefix, stack.stackSize);
                if (sameIdentity(generated, stack)) {
                    return ownerType + "."
                        + ownerName
                        + ".get(OrePrefixes."
                        + parsed.prefix.name()
                        + ", "
                        + stack.stackSize
                        + ")";
                }
            }
        }

        // Material item getters; the identity check picks the right one.
        for (String methodName : MATERIAL_METHODS) {
            for (Method method : methods) {
                if (!methodName.equals(method.getName()) || method.getParameterTypes().length != 1) continue;
                ItemStack generated = invoke(method, owner, stack.stackSize);
                if (sameIdentity(generated, stack))
                    return ownerType + "." + ownerName + "." + methodName + "(" + stack.stackSize + ")";
            }
        }
        return null;
    }

    // ----Helpers---- //

    /** Cheap match against the raw registered stack, without calling get(). */
    private static String rawStackMatch(Object owner, String ownerType, String ownerName, ItemStack stack,
        Method[] methods) {
        if (owner == null) return null;
        for (Method method : methods) {
            if (method.getParameterTypes().length != 0) continue;
            String name = method.getName();
            if (!"getInternalStack_unsafe".equals(name) && !"getStack".equals(name)) continue;
            try {
                ItemStack registered = (ItemStack) method.invoke(owner);
                if (sameStack(registered, stack)) {
                    return ownerType + "." + ownerName + ".get(" + stack.stackSize + ")";
                }
            } catch (Throwable ignored) {
                // Optional lists may not expose a raw stack accessor.
            }
        }
        return null;
    }

    /** True when the entry was set; lists without a gate are always set. */
    private static boolean hasBeenSet(Object owner, Method[] methods) {
        try {
            for (Method method : methods) {
                if (!"hasBeenSet".equals(method.getName()) || method.getParameterTypes().length != 0) continue;
                return Boolean.TRUE.equals(method.invoke(owner));
            }
        } catch (Throwable ignored) {
            // Lists without a usable hasBeenSet gate are always considered set.
        }
        return true;
    }

    private static Field[] allFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try {
                for (Field field : current.getDeclaredFields()) {
                    fields.add(field);
                }
            } catch (Throwable ignored) {
                // Optional integrations may hide their implementation details.
            }
        }
        return fields.toArray(new Field[0]);
    }

    private static Object readStatic(Field field) {
        try {
            if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(
                field.getDeclaringClass()
                    .getModifiers())) {
                field.setAccessible(true);
            }
            return field.get(null);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static ItemStack invokeGetter(Object owner, String getter, int size) {
        try {
            Method method = owner.getClass()
                .getMethod(getter, int.class);
            return (ItemStack) method.invoke(owner, size);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static ItemStack invokeGet(Object owner, OrePrefixes prefix, int size) {
        if (owner == null) return null;
        // Werkstoff.get() throws and logs "NO SUCH ITEM!" when the prefix is not generated, so gate on hasItemType().
        if (!supportsPrefix(owner, prefix)) return null;
        for (Method method : owner.getClass()
            .getMethods()) {
            if (!"get".equals(method.getName()) || method.getParameterTypes().length != 2) continue;
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters[0] != OrePrefixes.class) continue;
            if (!(parameters[1] == int.class || parameters[1] == Integer.class)) continue;
            try {
                return (ItemStack) method.invoke(owner, prefix, size);
            } catch (Throwable ignored) {
                // Try the next matching overload.
            }
        }
        return null;
    }

    /**
     * True when the owner generates items for the prefix. Used before calling {@code get(prefix, size)} on
     * Werkstoffe, which throw and log an error for prefixes they do not generate.
     */
    private static boolean supportsPrefix(Object owner, OrePrefixes prefix) {
        try {
            Method method = owner.getClass()
                .getMethod("hasItemType", OrePrefixes.class);
            return Boolean.TRUE.equals(method.invoke(owner, prefix));
        } catch (Throwable ignored) {
            // Non-Werkstoff owners: let get() decide.
            return true;
        }
    }

    private static boolean hasStaticField(Class<?> type, String name) {
        try {
            return type.getField(name) != null;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static ItemStack invoke(Method method, Object owner, Object... args) {
        try {
            return (ItemStack) method.invoke(owner, args);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Object amountArgument(Class<?> type, int amount) {
        return type == int.class || type == Integer.class ? amount : (long) amount;
    }

    private static String displayType(Class<?> type) {
        if ("gregtech.api.enums.ItemList".equals(type.getName())) return "ItemList";
        if ("com.Nxer.TwistSpaceTechnology.common.GTCMItemList".equals(type.getName())) return "GTCMItemList";
        return "ItemList".equals(type.getSimpleName()) ? type.getName() : type.getSimpleName();
    }

    private static String sourceType(Class<?> type) {
        String name = type.getName();
        int packageEnd = name.lastIndexOf('.');
        String simple = packageEnd < 0 ? name : name.substring(packageEnd + 1);
        return simple.replace('$', '.');
    }

    private static String copyAmount(String expression, int amount) {
        return amount == 1 ? expression : "GTUtility.copyAmount(" + amount + ", " + expression + ")";
    }

    private static Class<?> loadClass(String className) {
        ClassLoader contextLoader = Thread.currentThread()
            .getContextClassLoader();
        try {
            return Class.forName(className, false, contextLoader);
        } catch (Throwable ignored) {
            try {
                return Class.forName(className, false, ContainerItemSourceResolver.class.getClassLoader());
            } catch (Throwable ignoredAgain) {
                return null;
            }
        }
    }

    private static boolean sameStack(ItemStack first, ItemStack second) {
        return first != null && second != null
            && first.getItem() == second.getItem()
            && first.getItemDamage() == second.getItemDamage()
            && ItemStack.areItemStackTagsEqual(first, second);
    }

    private static boolean sameIdentity(ItemStack first, ItemStack second) {
        return first != null && second != null
            && first.getItem() == second.getItem()
            && first.getItemDamage() == second.getItemDamage();
    }

    private static boolean sameItem(Item first, Item second) {
        if (first == null || second == null) return false;
        if (first == second) return true;
        GameRegistry.UniqueIdentifier firstId = GameRegistry.findUniqueIdentifierFor(first);
        GameRegistry.UniqueIdentifier secondId = GameRegistry.findUniqueIdentifierFor(second);
        return firstId != null && secondId != null
            && firstId.modId.equals(secondId.modId)
            && firstId.name.equals(secondId.name);
    }

    private static boolean sameBlock(Block block, Item item) {
        if (block == null || item == null) return false;
        if (Block.getBlockFromItem(item) == block) return true;
        if (sameItem(Item.getItemFromBlock(block), item)) return true;
        String blockName = block.getUnlocalizedName();
        String itemName = item.getUnlocalizedName();
        if (blockName == null || itemName == null) return false;
        return blockName.equals(itemName) || blockName.replace("tile.", "")
            .equals(itemName.replace("item.", ""));
    }

    private static String key(ItemStack stack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        return (id == null ? String.valueOf(stack.getItem()) : id.modId + ":" + id.name) + ":"
            + stack.getItemDamage()
            + ":"
            + stack.stackSize
            + ":"
            + String.valueOf(stack.getTagCompound());
    }
}
