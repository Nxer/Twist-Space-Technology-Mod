package com.Nxer.TwistSpaceTechnology.common.material;

import goodgenerator.util.CharExchanger;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.MaterialIconRegistry.IconType;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IMaterialHandler;
import gregtech.common.render.items.InfinityRenderer;

/**
 * Register new material here by Gregtech Material System
 */
public class MaterialsTST implements IMaterialHandler {

    // spotless:off
    static final int offsetID = 260;
    // ID form 2242 ~ 2298 is available

    // Full-colour overlays keep the material's original fluid/fallback tint intact.
    private static final TextureSet AXONIUM_TEXTURES = TextureSet.SET_SHINY.withCustomTextures(
        "tst_axonium",
        IconType.INGOT,
        IconType.INGOT_HOT,
        IconType.NUGGET,
        IconType.DUST,
        IconType.DUST_SMALL,
        IconType.DUST_TINY,
        IconType.PLATE,
        IconType.PLATE_DOUBLE,
        IconType.PLATE_TRIPLE,
        IconType.PLATE_QUADRUPLE,
        IconType.PLATE_QUINTUPLE,
        IconType.PLATE_DENSE,
        IconType.PLATE_SUPERDENSE,
        IconType.FOIL,
        IconType.STICK,
        IconType.STICK_LONG,
        IconType.BOLT,
        IconType.SCREW,
        IconType.RING,
        IconType.ROUND,
        IconType.GEAR_GT,
        IconType.GEAR_GT_SMALL,
        IconType.ROTOR,
        IconType.SPRING,
        IconType.SPRING_SMALL,
        IconType.WIRE_FINE,
        IconType.TURBINE_BLADE,
        IconType.CASING_SMALL,
        IconType.NANITES,
        IconType.TOOL_HEAD_HAMMER,
        IconType.TOOL_HEAD_MALLET,
        IconType.TOOL_HEAD_FILE,
        IconType.TOOL_HEAD_SAW,
        IconType.TOOL_HEAD_DRILL,
        IconType.TOOL_HEAD_CHAINSAW,
        IconType.TOOL_HEAD_WRENCH,
        IconType.TOOL_HEAD_SCREWDRIVER,
        IconType.TOOL_HEAD_BUZZSAW,
        IconType.TOOL_HEAD_SOLDERING,
        IconType.BLOCK1,
        IconType.BLOCK2,
        IconType.BLOCK3,
        IconType.BLOCK4,
        IconType.BLOCK5,
        IconType.BLOCK6,
        IconType.SHEETMETAL);

    private static final TextureSet AXONIS_ALLOY_TEXTURES = TextureSet.SET_SHINY.withCustomTextures(
        "tst_axonis",
        IconType.INGOT,
        IconType.INGOT_HOT,
        IconType.NUGGET,
        IconType.DUST,
        IconType.DUST_SMALL,
        IconType.DUST_TINY,
        IconType.PLATE,
        IconType.PLATE_DOUBLE,
        IconType.PLATE_TRIPLE,
        IconType.PLATE_QUADRUPLE,
        IconType.PLATE_QUINTUPLE,
        IconType.PLATE_DENSE,
        IconType.PLATE_SUPERDENSE,
        IconType.FOIL,
        IconType.STICK,
        IconType.STICK_LONG,
        IconType.BOLT,
        IconType.SCREW,
        IconType.RING,
        IconType.ROUND,
        IconType.GEAR_GT,
        IconType.GEAR_GT_SMALL,
        IconType.ROTOR,
        IconType.SPRING,
        IconType.SPRING_SMALL,
        IconType.WIRE_FINE,
        IconType.TURBINE_BLADE,
        IconType.CASING_SMALL,
        IconType.TOOL_HEAD_HAMMER,
        IconType.TOOL_HEAD_MALLET,
        IconType.TOOL_HEAD_FILE,
        IconType.TOOL_HEAD_SAW,
        IconType.TOOL_HEAD_DRILL,
        IconType.TOOL_HEAD_CHAINSAW,
        IconType.TOOL_HEAD_WRENCH,
        IconType.TOOL_HEAD_SCREWDRIVER,
        IconType.TOOL_HEAD_BUZZSAW,
        IconType.TOOL_HEAD_SOLDERING,
        IconType.BLOCK1,
        IconType.BLOCK2,
        IconType.BLOCK3,
        IconType.BLOCK4,
        IconType.BLOCK5,
        IconType.BLOCK6,
        IconType.SHEETMETAL);

    static {
        // Only item/block icons are custom; keep GT's automatic fluid and plasma generation.
        AXONIUM_TEXTURES.is_custom = false;
        AXONIS_ALLOY_TEXTURES.is_custom = false;
    }

    private static boolean registered = false;

    public static Materials NeutroniumAlloy;
    public static Materials AxonisAlloy;
    public static Materials Axonium;
    public static Materials Dubnium;

    @Override
    public void onMaterialsInit() {
        // #tr Material.neutroniumalloy
        // # Neutronium Alloy
        // #zh_CN 中子合金
        NeutroniumAlloy = withMetaItemSubID(
            new MaterialBuilder().setName("NeutroniumAlloy")
                .setDefaultLocalName("Neutronium Alloy")
                .setChemicalFormula("Nt₈Du₂Fl[Fe/C]")
                .setIconSet(TextureSet.SET_SHINY)
                .setColor(Dyes._NULL)
                .setARGB(0x00D9DCCB)
                .setTool(98304000, 18, 32.0F)
                .setTurbine(2, 1, 1)
                .addDustItems()
                .addMetalItems()
                .addToolHeadItems()
                .addGearItems()
                .setMeltingPoint(12000)
                .setBlastFurnaceTemp(12500)
                .setBlastFurnaceRequired(true)
                .setAutoGenerateBlastFurnaceRecipes(false)
                .setAutoGeneratedVacuumFreezerRecipe(false)
                .addAspect(TCAspects.ORDO, 1)
                .addAspect(TCAspects.ALIENIS, 2)
                .addAspect(TCAspects.PERDITIO, 3)
                .addAspect(TCAspects.METALLUM, 5)
                .addSubTag(SubTag.METAL)
                .constructMaterial()
                .setProcessingMaterialTierEU(TierEU.RECIPE_UEV),
            offsetID);

        // #tr Material.axonisalloy
        // # Axonis Alloy
        // #zh_CN 灵韵合金
        AxonisAlloy = withMetaItemSubID(
            new MaterialBuilder().setName("AxonisAlloy")
                .setDefaultLocalName("Axonis Alloy")
                .setChemicalFormula("۞₅(Hy⚶)₃⸎₂(IcMa)TbSh" + CharExchanger.shifter(9191))
                .setIconSet(AXONIS_ALLOY_TEXTURES)
                .setColor(Dyes._NULL)
                .setARGB(0x00C54324)
                .setTool(98304000, 18, 96.0F)
                .setTurbine(2, 1, 1)
                .addDustItems()
                .addMetalItems()
                .addToolHeadItems()
                .addGearItems()
                .setMeltingPoint(13000)
                .setBlastFurnaceTemp(13200)
                .setBlastFurnaceRequired(true)
                .setAutoGenerateBlastFurnaceRecipes(false)
                .addAspect(TCAspects.SENSUS, 1)
                .addAspect(TCAspects.PRAECANTATIO, 2)
                .addAspect(TCAspects.METALLUM, 4)
                .addSubTag(SubTag.METAL)
                .constructMaterial()
                .setProcessingMaterialTierEU(TierEU.RECIPE_UIV),
            offsetID + 1);

        // #tr Material.axonium
        // # Axonium
        // #zh_CN 焕律璨金
        Axonium = withMetaItemSubID(
            new MaterialBuilder().setName("Axonium")
                .setDefaultLocalName("Axonium")
                .setChemicalFormula("A☀")
                .setIconSet(AXONIUM_TEXTURES)
                .setColor(Dyes._NULL)
                .setARGB(0x00F32D1B)
                .setTool(1048576000, 24, 480.0F)
                .setTurbine(2, 1, 1)
                .addDustItems()
                .addMetalItems()
                .addPlasma()
                .addToolHeadItems()
                .addGearItems()
                .setMeltingPoint(23000)
                .setBlastFurnaceTemp(26000)
                .setBlastFurnaceRequired(true)
                .setAutoGenerateBlastFurnaceRecipes(false)
                .setAutoGeneratedVacuumFreezerRecipe(false)
                .addAspect(TCAspects.SENSUS, 1)
                .addAspect(TCAspects.PRAECANTATIO, 2)
                .addAspect(TCAspects.VICTUS, 3)
                .addAspect(TCAspects.METALLUM, 5)
                .addSubTag(SubTag.METAL)
                .addOrePrefix(OrePrefixes.nanite)
                .constructMaterial()
                .setProcessingMaterialTierEU(TierEU.RECIPE_UMV),
            offsetID + 2);

        // #tr Material.dubnium
        // # Dubnium
        // #zh_CN 钅杜
        Dubnium = withMetaItemSubID(
            new MaterialBuilder().setName("Dubnium")
                .setDefaultLocalName("Dubnium")
                .setElement(Element.Db)
                .setIconSet(TextureSet.SET_METALLIC)
                .setColor(Dyes._NULL)
                .setARGB(0x00C8C8C8)
                .setTool(0, 2, 1.0F)
                .setTurbine(2, 1, 1)
                .addDustItems()
                .addMetalItems()
                .addPlasma()
                .addToolHeadItems()
                .setMeltingPoint(3231)
                .setBlastFurnaceTemp(3231)
                .setAutoGenerateBlastFurnaceRecipes(false)
                .setAutoGeneratedVacuumFreezerRecipe(false)
                .addAspect(TCAspects.RADIO, 5)
                .addAspect(TCAspects.METALLUM, 3)
                .constructMaterial()
                .setProcessingMaterialTierEU(TierEU.RECIPE_LV),
            offsetID + 3);

        OrePrefixes.nanite.mGeneratedItems.add(Materials.CosmicNeutronium);
        OrePrefixes.nanite.mGeneratedItems.add(MaterialsTST.Axonium);
    }

    private static Materials withMetaItemSubID(Materials material, int metaItemSubID) {
        material.mMetaItemSubID = metaItemSubID;
        return material;
    }

    public static void initClient() {
        Axonium.renderer = new InfinityRenderer();
    }

    public static void init() {
        if (!registered) {
            Materials.add(new MaterialsTST());
            registered = true;
        }
    }

}
