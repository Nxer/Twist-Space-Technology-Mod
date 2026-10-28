package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.recipes.RecipeMaterialUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import kubatech.loaders.DEFCRecipes;
import tectech.recipe.TecTechRecipeMaps;

/**
 * Shared recipe resources. Access after material and fluid registration (recipe loading at load-complete).
 * Elemental forms follow atomic-number order; compounds and mixtures are added only when used.
 * Chemical names are written in full. Undefined sludge/solution compositions retain their source names.
 * IDs are read-only templates; obtain an ItemStack copy before modifying its NBT.
 */
public final class BottledChaosRecipeResources {

    public static final int PLATINUM_LEAVES_PER_POWDER = 4;
    private static final Map<TST_ItemID, TST_ItemID> UNIFIED_DUSTS = new HashMap<>();
    private static final List<ItemStack> FRUIT_INPUTS = GTOreDictUnificator.getOresImmutable("listAllfruit");

    /** Tag identities and runtime indexes; capacity costs are published by route registration. */
    public static final class CredentialModel {

        // Item names are queried during post-init, before all recipe resources have been registered.
        private static final boolean HAS_HONEYCOMBS = Mods.Forestry.isModLoaded() && GTBees.combs != null;
        private static final boolean HAS_NETHERITE = Mods.EtFuturumRequiem.isModLoaded();
        private static final boolean HAS_ETERNITY = Mods.EternalSingularity.isModLoaded();
        private static final boolean HAS_CRYSTALLINE_ALLOY = Mods.EnderIO.isModLoaded();
        public static final String TAGS_NBT_KEY = "bottledChaosTags";
        public static final String OCCUPIED_WEIGHT_NBT_KEY = "bottledChaosOccupiedWeight";

        /** Declaration order determines display priority and runtime indexes; NBT stores tag names. */
        @Desugar
        private record TagDefinition(String tag, boolean enabled, String family, BottledChaosRecipePage page) {}

        // Material bindings stay in CredentialRoutes and are not initialized with this metadata.
        // spotless:off
        private static final TagDefinition[] DEFINITIONS = {
            new TagDefinition("honeycomb", HAS_HONEYCOMBS, "honeycomb", BottledChaosRecipePage.DEFAULT),
            new TagDefinition("titanium", true, "titanium", BottledChaosRecipePage.TITANIUM),
            new TagDefinition("tungsten", true, "tungsten", BottledChaosRecipePage.TUNGSTEN),
            new TagDefinition("indium", true, "indium", BottledChaosRecipePage.INDIUM),
            new TagDefinition("platinum_metallic_powder", true, "platinum", BottledChaosRecipePage.PLATINUM),
            new TagDefinition("palladium_metallic_powder", true, "platinum", BottledChaosRecipePage.PLATINUM),
            new TagDefinition("platinum_residue", true, "platinum", BottledChaosRecipePage.PLATINUM),
            new TagDefinition("crude_rhodium_metal", true, "platinum", BottledChaosRecipePage.PLATINUM),
            new TagDefinition("leach_residue", true, "platinum", BottledChaosRecipePage.PLATINUM),
            new TagDefinition("rarest_metal_residue", true, "platinum", BottledChaosRecipePage.PLATINUM),
            new TagDefinition("iridium_metal_residue", true, "platinum", BottledChaosRecipePage.PLATINUM),
            new TagDefinition("monazite", true, "lanthanide", BottledChaosRecipePage.LANTHANIDE),
            new TagDefinition("bastnasite", true, "lanthanide", BottledChaosRecipePage.LANTHANIDE),
            new TagDefinition("cerium", true, "lanthanide", BottledChaosRecipePage.LANTHANIDE),
            new TagDefinition("rare_earth_cerium", true, "lanthanide", BottledChaosRecipePage.LANTHANIDE),
            new TagDefinition("samarium", true, "lanthanide", BottledChaosRecipePage.LANTHANIDE),
            new TagDefinition("naquadah", true, "naquadah", BottledChaosRecipePage.NAQUADAH),
            new TagDefinition("enriched_naquadah", true, "naquadah", BottledChaosRecipePage.NAQUADAH),
            new TagDefinition("naquadria", true, "naquadah", BottledChaosRecipePage.NAQUADAH),
            new TagDefinition("naquadah_fuel_preparation", true, "naquadah_fuel_preparation", BottledChaosRecipePage.NAQUADAH_FUEL),
            new TagDefinition("naquadah_fuel_3", true, "naquadah_fuel_3", BottledChaosRecipePage.NAQUADAH_FUEL),
            new TagDefinition("naquadah_fuel_4", true, "naquadah_fuel_4", BottledChaosRecipePage.NAQUADAH_FUEL),
            new TagDefinition("naquadah_fuel_5", true, "naquadah_fuel_5", BottledChaosRecipePage.NAQUADAH_FUEL),
            new TagDefinition("naquadah_fuel_6", true, "naquadah_fuel_6", BottledChaosRecipePage.NAQUADAH_FUEL),
            new TagDefinition("fusion", true, "fusion", BottledChaosRecipePage.FUSION),
            new TagDefinition("ancient_debris", HAS_NETHERITE, "ancient_debris", BottledChaosRecipePage.NETHERITE),
            new TagDefinition("isamill", true, "isamill", BottledChaosRecipePage.ISA_MILL),
            new TagDefinition("crystalline_alloy_molten", HAS_CRYSTALLINE_ALLOY, "prismatic", BottledChaosRecipePage.PRISMATIC),
            new TagDefinition("purified_water_1", true, "purified_water", BottledChaosRecipePage.PURIFIED_WATER),
            new TagDefinition("purified_water_2", true, "purified_water", BottledChaosRecipePage.PURIFIED_WATER),
            new TagDefinition("prismatic_acid", true, "prismatic", BottledChaosRecipePage.PRISMATIC),
            new TagDefinition("purified_water_3", true, "purified_water", BottledChaosRecipePage.PURIFIED_WATER),
            new TagDefinition("purified_water_4", true, "purified_water", BottledChaosRecipePage.PURIFIED_WATER),
            new TagDefinition("kevlar", true, "kevlar", BottledChaosRecipePage.KEVLAR),
            new TagDefinition("super_heavy_radox", true, "radox", BottledChaosRecipePage.RADOX),
            new TagDefinition("heavy_radox", true, "radox", BottledChaosRecipePage.RADOX),
            new TagDefinition("light_radox", true, "radox", BottledChaosRecipePage.RADOX),
            new TagDefinition("super_light_radox", true, "radox", BottledChaosRecipePage.RADOX),
            new TagDefinition("purified_water_5", true, "purified_water", BottledChaosRecipePage.PURIFIED_WATER),
            new TagDefinition("purified_water_6", true, "purified_water", BottledChaosRecipePage.PURIFIED_WATER),
            new TagDefinition("growth_medium_sterilized", true, "biological", BottledChaosRecipePage.BIOLOGICAL),
            new TagDefinition("stem_cells", true, "biological", BottledChaosRecipePage.BIOLOGICAL),
            new TagDefinition("seaweed_broth", true, "biological", BottledChaosRecipePage.BIOLOGICAL),
            new TagDefinition("tau_ceti_e_seaweed_extract", true, "biological", BottledChaosRecipePage.BIOLOGICAL),
            new TagDefinition("bio_medium_sterilized", true, "biological", BottledChaosRecipePage.BIOLOGICAL),
            new TagDefinition("bio_cells", true, "biological", BottledChaosRecipePage.BIOLOGICAL),
            new TagDefinition("xenoxene", true, "radox", BottledChaosRecipePage.RADOX),
            new TagDefinition("purified_water_7", true, "purified_water", BottledChaosRecipePage.PURIFIED_WATER),
            new TagDefinition("purified_water_8", true, "purified_water", BottledChaosRecipePage.PURIFIED_WATER),
            new TagDefinition("eternity", HAS_ETERNITY, "eternity", BottledChaosRecipePage.DEFAULT),
            new TagDefinition("magmatter", true, "magmatter", BottledChaosRecipePage.DEFAULT),
            new TagDefinition("phonon_medium", true, "phonon_medium", BottledChaosRecipePage.DEFAULT),
            new TagDefinition("quark_gluon_plasma", true, "quark_gluon_plasma", BottledChaosRecipePage.DEFAULT)
        };
        // spotless:on
        private static final TagDefinition[] ACTIVE = Arrays.stream(DEFINITIONS)
            .filter(TagDefinition::enabled)
            .toArray(TagDefinition[]::new);
        public static final int ROUTE_COUNT = ACTIVE.length;
        public static final long ALL_ROUTE_MASK = (1L << ROUTE_COUNT) - 1;
        private static final int[] DISPLAY_ORDER = IntStream.range(0, ROUTE_COUNT)
            .toArray();
        private static final Map<String, Integer> TAG_INDICES = new HashMap<>();

        static {
            if (ROUTE_COUNT >= Long.SIZE) throw new IllegalStateException("Too many credential tags for a route mask");
            for (int index = 0; index < ROUTE_COUNT; index++) {
                if (TAG_INDICES.put(ACTIVE[index].tag(), index) != null)
                    throw new IllegalStateException("Duplicate credential tag: " + ACTIVE[index].tag());
            }
        }

        private static volatile int[] registeredCapacityCosts;

        static void publishCapacityCosts(int[] costs) {
            if (costs.length != ROUTE_COUNT) throw new IllegalArgumentException("incomplete credential capacity costs");
            for (int cost : costs)
                if (cost <= 0) throw new IllegalArgumentException("invalid credential capacity cost");
            registeredCapacityCosts = costs.clone();
        }

        public static int[] displayOrder() {
            return DISPLAY_ORDER.clone();
        }

        /** Honeycomb variants share their production tag's position, regardless of the authorization bit. */
        public static int displayIndex(long requiredTagMask) {
            int tagIndex = ownerTagIndex(requiredTagMask);
            return tagIndex < ROUTE_COUNT ? tagIndex : Integer.MAX_VALUE;
        }

        public static String tag(int index) {
            return index >= 0 && index < ROUTE_COUNT ? ACTIVE[index].tag() : null;
        }

        public static int indexForTag(String tag) {
            return TAG_INDICES.getOrDefault(tag, -1);
        }

        /** Processing families are independent of NEI pages; additional honeycomb authorization keeps its owner. */
        public static String processingFamily(long requiredTagMask) {
            int index = ownerTagIndex(requiredTagMask);
            return index >= 0 && index < ROUTE_COUNT ? ACTIVE[index].family() : null;
        }

        /** Shared production owner for honeycomb authorization, runtime families and presentation. */
        public static int ownerTagIndex(long requiredTagMask) {
            if (HAS_HONEYCOMBS) requiredTagMask &= ~(1L << indexForTag("honeycomb"));
            return Long.numberOfTrailingZeros(requiredTagMask);
        }

        public static BottledChaosRecipePage displayPage(int index) {
            return index >= 0 && index < ROUTE_COUNT ? ACTIVE[index].page() : BottledChaosRecipePage.DEFAULT;
        }

        public static String translationKey(int index) {
            String name = tag(index);
            return name == null ? null : "tst.bottledchaos.route." + name;
        }

        /** One representative per NEI page; unrelated routes on the main page remain independent. */
        public static List<String> groupedTranslationKeys(long mask) {
            Map<String, Integer> lastByPage = new HashMap<>();
            for (int index = 0; index < ROUTE_COUNT; index++) {
                if ((mask & (1L << index)) == 0) continue;
                lastByPage.put(displayPage(index).groupKey(tag(index)), index);
            }
            long representatives = 0;
            for (int index : lastByPage.values()) representatives |= 1L << index;
            List<String> keys = new ArrayList<>();
            for (int index = 0; index < ROUTE_COUNT; index++) {
                if ((representatives & (1L << index)) == 0) continue;
                keys.add(displayPage(index).summaryKey(translationKey(index)));
            }
            if (keys.isEmpty()) keys.add("tst.bottledchaos.unbound");
            return keys;
        }

        public static long readRouteMask(NBTTagCompound tag) {
            if (tag == null) return 0;
            NBTTagList tags = tag.getTagList(TAGS_NBT_KEY, 8);
            long mask = 0;
            for (int i = 0; i < tags.tagCount(); i++) {
                int index = indexForTag(tags.getStringTagAt(i));
                if (index >= 0) mask |= 1L << index;
            }
            return mask;
        }

        public static void writeRouteMask(NBTTagCompound tag, long routeMask) {
            NBTTagList tags = new NBTTagList();
            for (int index = 0; index < ROUTE_COUNT; index++) {
                if ((routeMask & (1L << index)) != 0) tags.appendTag(new NBTTagString(tag(index)));
            }
            tag.setTag(TAGS_NBT_KEY, tags);
        }

        /** Every selected route pays its registered cost independently. */
        public static int getRequiredWeight(long routeMask) {
            if (routeMask == 0) return 0;
            int[] costs = registeredCapacityCosts;
            if (costs == null) return Integer.MAX_VALUE;
            int weight = 0;
            for (int index = 0; index < ROUTE_COUNT; index++) {
                if ((routeMask & (1L << index)) != 0) weight = Math.addExact(weight, costs[index]);
            }
            return weight;
        }
    }

    /**
     * Credential identity: tag/text key, runtime mask bit, binding marker and display template.
     * Material templates are accessed after registration, independently of frame metadata and chain entry order.
     */
    @Desugar
    public record CredentialRoute(int index, String tag, Object root, Object display, String translationKey) {

        public long mask() {
            return 1L << index;
        }

        public ItemStack rootStartStack() {
            return root instanceof TST_ItemID item ? item.getItemStack()
                : GTUtility.fillFluidContainer(
                    new FluidStack((Fluid) root, 1000),
                    ItemList.Cell_Universal_Fluid.get(1),
                    false,
                    true);
        }

        public ItemStack displayStack() {
            return display instanceof TST_ItemID item ? item.getItemStack()
                : GTUtility.getFluidDisplayStack((Fluid) display);
        }
    }

    /** Access after material registration, like the other recipe templates in this class. */
    public static final class CredentialRoutes {

        private static final CredentialRoute[] ROUTES = new CredentialRoute[CredentialModel.ROUTE_COUNT];

        static {
            // spotless:off
            if (CredentialModel.HAS_HONEYCOMBS) register("honeycomb", recipeItemId(GTBees.combs.getStackForType(CombType.INDIUM, 1)), recipeItemId(GTBees.combs.getStackForType(CombType.INDIUM, 1)));
            register("titanium", RutileDust, TitaniumIngot);
            register("tungsten", CalciumTungstateDust, TungstenTrioxideDust);
            register("indium", SphaleriteDust, IndiumDust);
            register("platinum_metallic_powder", PlatinumMetallicPowder, PlatinumMetallicPowder);
            register("palladium_metallic_powder", PalladiumMetallicPowder, PalladiumMetallicPowder);
            register("platinum_residue", PlatinumResidueDust, PlatinumResidueDust);
            register("crude_rhodium_metal", CrudeRhodiumMetalDust, CrudeRhodiumMetalDust);
            register("leach_residue", LeachResidueDust, LeachResidueDust);
            register("rarest_metal_residue", RarestMetalResidueDust, RarestMetalResidueDust);
            register("iridium_metal_residue", IridiumMetalResidueDust, IridiumMetalResidueDust);
            register("monazite", MonaziteDust, DilutedMonaziteMud);
            register("bastnasite", BastnasiteDust, RoastedRareEarthOxidesDust);
            register("cerium", CeriumRichMixtureDust, CeriumDust);
            register("rare_earth_cerium", CeriumDioxideDust, CeriumDust);
            register("samarium", SamariumOreConcentrateDust, SamariumDust);
            register("naquadah", NaquadahOxideMixtureDust, NaquadahDust);
            register("enriched_naquadah", EnrichedNaquadahOxideMixtureDust, EnrichedNaquadahDust);
            register("naquadria", NaquadriaOxideMixtureDust, NaquadriaDust);
            register("naquadah_fuel_preparation", NaquadriaDust, GGMaterial.heavyNaquadahFuel.getFluidOrGas(1).getFluid());
            register("naquadah_fuel_3", recipeItemId(GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 1)), GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1).getFluid());
            register("naquadah_fuel_4", GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1).getFluid(), GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(1).getFluid());
            register("naquadah_fuel_5", GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(1).getFluid(), GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(1).getFluid());
            register("naquadah_fuel_6", GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(1).getFluid(), GGMaterial.naquadahBasedFuelMkVI.getFluidOrGas(1).getFluid());
            register("fusion", recipeItemId(Materials.Cobalt.getDust(1)), MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma());
            if (Mods.EtFuturumRequiem.isModLoaded()) register("ancient_debris", AncientDebris, NetheriteNanoparticles);
            register("isamill", MilledSphalerite, MilledSphalerite);
            if (CredentialModel.HAS_CRYSTALLINE_ALLOY) register("crystalline_alloy_molten", CrystallineAlloyMolten, CrystallineAlloyMolten);
            register("purified_water_1", recipeItemId(Materials.Grade1PurifiedWater.getCells(1)), recipeItemId(Materials.Grade1PurifiedWater.getCells(1)));
            register("purified_water_2", recipeItemId(Materials.Grade2PurifiedWater.getCells(1)), recipeItemId(Materials.Grade2PurifiedWater.getCells(1)));
            register("prismatic_acid", PrismarineShard, PrismaticAcid);
            register("purified_water_3", recipeItemId(Materials.Grade3PurifiedWater.getCells(1)), recipeItemId(Materials.Grade3PurifiedWater.getCells(1)));
            register("purified_water_4", recipeItemId(Materials.Grade4PurifiedWater.getCells(1)), recipeItemId(Materials.Grade4PurifiedWater.getCells(1)));
            register("kevlar", KevlarFiber, KevlarPlate);
            register("super_heavy_radox", SuperHeavyRadox, SuperHeavyRadox);
            register("heavy_radox", HeavyRadox, HeavyRadox);
            register("light_radox", LightRadox, LightRadox);
            register("super_light_radox", SuperLightRadox, SuperLightRadox);
            register("purified_water_5", recipeItemId(Materials.Grade5PurifiedWater.getCells(1)), recipeItemId(Materials.Grade5PurifiedWater.getCells(1)));
            register("purified_water_6", recipeItemId(Materials.Grade6PurifiedWater.getCells(1)), recipeItemId(Materials.Grade6PurifiedWater.getCells(1)));
            register("growth_medium_sterilized", GrowthMediumSterilized, GrowthMediumSterilized);
            register("stem_cells", StemCells, StemCells);
            register("seaweed_broth", SeaweedBroth, SeaweedBroth);
            register("tau_ceti_e_seaweed_extract", TauCetiESeaweedExtract, TauCetiESeaweedExtract);
            register("bio_medium_sterilized", BioMediumSterilized, BioMediumSterilized);
            register("bio_cells", BioCells, BioCells);
            register("xenoxene", Xenoxene, RawRadox);
            register("purified_water_7", recipeItemId(Materials.Grade7PurifiedWater.getCells(1)), recipeItemId(Materials.Grade7PurifiedWater.getCells(1)));
            register("purified_water_8", recipeItemId(Materials.Grade8PurifiedWater.getCells(1)), recipeItemId(Materials.Grade8PurifiedWater.getCells(1)));
            if (CredentialModel.HAS_ETERNITY) register("eternity", EternalSingularity, EternityMolten);
            register("magmatter", recipeItemId(Materials.MagMatter.getDust(1)), recipeItemId(Materials.MagMatter.getDust(1)));
            register("phonon_medium", MagMatterRound, PhononMedium);
            register("quark_gluon_plasma", recipeItemId(Materials.QuarkGluonPlasma.getCells(1)), recipeItemId(Materials.QuarkGluonPlasma.getCells(1)));
            // spotless:on
        }

        private static void register(String tag, Object root, Object display) {
            int index = CredentialModel.indexForTag(tag);
            ROUTES[index] = new CredentialRoute(index, tag, root, display, CredentialModel.translationKey(index));
        }

        public static CredentialRoute get(int index) {
            return index >= 0 && index < CredentialModel.ROUTE_COUNT ? ROUTES[index] : null;
        }
    }

    /** Exact item, metadata and NBT identity; marker stack size has no bearing on its route. */
    public static long getRouteMaskForMarker(ItemStack marker) {
        if (marker == null || marker.getItem() == null) return 0;
        TST_ItemID id = TST_ItemID.create(marker);
        FluidStack contents = GTUtility.getFluidForFilledItem(marker, true);
        for (CredentialRoute route : CredentialRoutes.ROUTES) {
            if (route.root() instanceof TST_ItemID item && item.equals(id)
                || route.root() instanceof Fluid fluid && contents != null && contents.getFluid() == fluid)
                return route.mask();
        }
        return 0;
    }

    /** May be used before material registration without initializing the material IDs in the outer class. */
    public static final class RecipePools {

        // spotless:off
        public static final RecipeMap<?> BlastFurnace = RecipeMaps.blastFurnaceRecipes;
        public static final RecipeMap<?> ArcFurnace = RecipeMaps.arcFurnaceRecipes;
        public static final RecipeMap<?> LargeChemicalReactor = RecipeMaps.multiblockChemicalReactorRecipes;
        public static final RecipeMap<?> Sifter = RecipeMaps.sifterRecipes;
        public static final RecipeMap<?> DistillationTower = RecipeMaps.distillationTowerRecipes;
        public static final RecipeMap<?> Distillery = RecipeMaps.distilleryRecipes;
        public static final RecipeMap<?> Brewery = RecipeMaps.brewingRecipes;
        public static final RecipeMap<?> Fermenter = RecipeMaps.fermentingRecipes;
        public static final RecipeMap<?> MixerNonCell = RecipeMaps.mixerNonCellRecipes;
        public static final RecipeMap<?> ElectrolyzerNonCell = RecipeMaps.electrolyzerNonCellRecipes;
        public static final RecipeMap<?> CentrifugeNonCell = RecipeMaps.centrifugeNonCellRecipes;
        public static final RecipeMap<?> FluidHeater = RecipeMaps.fluidHeaterRecipes;
        public static final RecipeMap<?> FluidSolidifier = RecipeMaps.fluidSolidifierRecipes;
        public static final RecipeMap<?> FluidExtractor = RecipeMaps.fluidExtractionRecipes;
        public static final RecipeMap<?> Bender = RecipeMaps.benderRecipes;
        public static final RecipeMap<?> Compressor = RecipeMaps.compressorRecipes;
        public static final RecipeMap<?> Cutter = RecipeMaps.cutterRecipes;
        public static final RecipeMap<?> Furnace = RecipeMaps.furnaceRecipes;
        public static final RecipeMap<?> Macerator = RecipeMaps.maceratorRecipes;
        public static final RecipeMap<?> ForgeHammer = RecipeMaps.hammerRecipes;
        public static final RecipeMap<?> ChemicalBath = RecipeMaps.chemicalBathRecipes;
        public static final RecipeMap<?> ChemicalDehydratorNonCell = RecipeMaps.chemicalDehydratorNonCellRecipes;
        public static final RecipeMap<?> VacuumFreezer = RecipeMaps.vacuumFreezerRecipes;
        public static final RecipeMap<?> OilCracker = RecipeMaps.crackingRecipes;
        public static final RecipeMap<?> VacuumFurnace = RecipeMaps.vacuumFurnaceRecipes;
        public static final RecipeMap<?> FlotationCell = RecipeMaps.flotationCellRecipes;
        public static final RecipeMap<?> Milling = RecipeMaps.millingRecipes;
        public static final RecipeMap<?> Digester = LanthanidesRecipeMaps.digesterRecipes;
        public static final RecipeMap<?> DissolutionTank = LanthanidesRecipeMaps.dissolutionTankRecipes;
        public static final RecipeMap<?> ChemicalPlant = RecipeMaps.chemicalPlantRecipes;
        public static final RecipeMap<?> LaserEngraver = RecipeMaps.laserEngraverRecipes;
        public static final RecipeMap<?> AlloyBlastSmelter = RecipeMaps.alloyBlastSmelterRecipes;
        public static final RecipeMap<?> ElectromagneticSeparator = RecipeMaps.electroMagneticSeparatorRecipes;
        public static final RecipeMap<?> ThermalCentrifuge = RecipeMaps.thermalCentrifugeRecipes;
        public static final RecipeMap<?> Autoclave = RecipeMaps.autoclaveRecipes;
        public static final RecipeMap<?> NeutronActivator = GTCMRecipe.NeutronActivatorWithEURecipeMap;
        public static final RecipeMap<?> BacterialVat = BartWorksRecipeMaps.bacterialVatRecipes;
        public static final RecipeMap<?> Extractor = RecipeMaps.extractorRecipes;
        public static final RecipeMap<?> ElectricImplosionCompressor = BartWorksRecipeMaps.electricImplosionCompressorRecipes;
        public static final RecipeMap<?> Assembler = RecipeMaps.assemblerRecipes;
        public static final RecipeMap<?> IndustrialCokeOven = RecipeMaps.industrialCokeOvenRecipes;
        public static final RecipeMap<?> QuantumForceTransformer = RecipeMaps.quantumForceTransformerRecipes;
        public static final RecipeMap<?> FusionCrafting = DEFCRecipes.fusionCraftingRecipes;
        public static final RecipeMap<?> Fusion = RecipeMaps.fusionRecipes;
        public static final RecipeMap<?> GodforgePlasma = TecTechRecipeMaps.godforgePlasmaRecipes;
        // spotless:on
    }

    // spotless:off
    // Elemental full dusts, ordered by atomic number. Initializers also register native dust aliases.
    public static final TST_ItemID LithiumDust = dust(Materials.Lithium);
    public static final TST_ItemID BerylliumDust = dust(Materials.Beryllium);
    public static final TST_ItemID BoronDust = dust(Materials.Boron);
    public static final TST_ItemID CarbonDust = dust(Materials.Carbon);
    public static final TST_ItemID SodiumDust = dust(Materials.Sodium);
    public static final TST_ItemID MagnesiumDust = dust(Materials.Magnesium);
    public static final TST_ItemID AluminiumDust = dust(Materials.Aluminium);
    public static final TST_ItemID SiliconDust = dust(Materials.Silicon);
    public static final TST_ItemID PhosphorusDust = dust(Materials.Phosphorus);
    public static final TST_ItemID SulfurDust = dust(Materials.Sulfur);
    public static final TST_ItemID PotassiumDust = dust(Materials.Potassium);
    public static final TST_ItemID CalciumDust = dust(Materials.Calcium);
    public static final TST_ItemID ScandiumDust = dust(Materials.Scandium);
    public static final TST_ItemID TitaniumDust = dust(Materials.Titanium);
    public static final TST_ItemID VanadiumDust = dust(Materials.Vanadium);
    public static final TST_ItemID ChromiumDust = dust(Materials.Chrome);
    public static final TST_ItemID ManganeseDust = dust(Materials.Manganese);
    public static final TST_ItemID IronDust = dust(Materials.Iron);
    public static final TST_ItemID CobaltDust = dust(Materials.Cobalt);
    public static final TST_ItemID NickelDust = dust(Materials.Nickel);
    public static final TST_ItemID CopperDust = dust(Materials.Copper);
    public static final TST_ItemID ZincDust = dust(Materials.Zinc);
    public static final TST_ItemID GalliumDust = dust(Materials.Gallium);
    public static final TST_ItemID GermaniumDust = dust(MaterialsElements.getInstance().GERMANIUM.getDust(1));
    public static final TST_ItemID ArsenicDust = dust(Materials.Arsenic);
    public static final TST_ItemID SeleniumDust = dust(MaterialsElements.getInstance().SELENIUM.getDust(1));
    public static final TST_ItemID RubidiumDust = dust(Materials.Rubidium);
    public static final TST_ItemID StrontiumDust = dust(Materials.Strontium);
    public static final TST_ItemID YttriumDust = dust(Materials.Yttrium);
    public static final TST_ItemID ZirconiumDust = dust(WerkstoffLoader.Zirconium);
    public static final TST_ItemID NiobiumDust = dust(Materials.Niobium);
    public static final TST_ItemID MolybdenumDust = dust(Materials.Molybdenum);
    public static final TST_ItemID TechnetiumDust = dust(MaterialsElements.getInstance().TECHNETIUM.getDust(1));
    public static final TST_ItemID RutheniumDust = dust(WerkstoffLoader.Ruthenium);
    public static final TST_ItemID RhodiumDust = dust(WerkstoffLoader.Rhodium);
    public static final TST_ItemID PalladiumDust = dust(Materials.Palladium);
    public static final TST_ItemID SilverDust = dust(Materials.Silver);
    public static final TST_ItemID CadmiumDust = dust(Materials.Cadmium);
    public static final TST_ItemID IndiumDust = dust(Materials.Indium);
    public static final TST_ItemID TinDust = dust(Materials.Tin);
    public static final TST_ItemID AntimonyDust = dust(Materials.Antimony);
    public static final TST_ItemID TelluriumDust = dust(Materials.Tellurium);
    public static final TST_ItemID IodineDust = dust(MaterialsElements.getInstance().IODINE.getDust(1));
    public static final TST_ItemID CaesiumDust = dust(Materials.Caesium);
    public static final TST_ItemID BariumDust = dust(Materials.Barium);
    public static final TST_ItemID LanthanumDust = dust(Materials.Lanthanum);
    public static final TST_ItemID CeriumDust = dust(Materials.Cerium);
    public static final TST_ItemID PraseodymiumDust = dust(Materials.Praseodymium);
    public static final TST_ItemID NeodymiumDust = dust(Materials.Neodymium);
    public static final TST_ItemID PromethiumDust = dust(Materials.Promethium);
    public static final TST_ItemID SamariumDust = dust(Materials.Samarium);
    public static final TST_ItemID EuropiumDust = dust(Materials.Europium);
    public static final TST_ItemID GadoliniumDust = dust(Materials.Gadolinium);
    public static final TST_ItemID TerbiumDust = dust(Materials.Terbium);
    public static final TST_ItemID DysprosiumDust = dust(Materials.Dysprosium);
    public static final TST_ItemID HolmiumDust = dust(Materials.Holmium);
    public static final TST_ItemID ErbiumDust = dust(Materials.Erbium);
    public static final TST_ItemID ThuliumDust = dust(Materials.Thulium);
    public static final TST_ItemID YtterbiumDust = dust(Materials.Ytterbium);
    public static final TST_ItemID LutetiumDust = dust(Materials.Lutetium);
    public static final TST_ItemID HafniumDust = dust(WerkstoffMaterialPool.Hafnium);
    public static final TST_ItemID TantalumDust = dust(Materials.Tantalum);
    public static final TST_ItemID TungstenDust = dust(Materials.Tungsten);
    public static final TST_ItemID RheniumDust = dust(MaterialsElements.getInstance().RHENIUM.getDust(1));
    public static final TST_ItemID OsmiumDust = dust(Materials.Osmium);
    public static final TST_ItemID IridiumDust = dust(Materials.Iridium);
    public static final TST_ItemID PlatinumDust = dust(Materials.Platinum);
    public static final TST_ItemID GoldDust = dust(Materials.Gold);
    public static final TST_ItemID ThalliumDust = dust(MaterialsElements.getInstance().THALLIUM.getDust(1));
    public static final TST_ItemID LeadDust = dust(Materials.Lead);
    public static final TST_ItemID BismuthDust = dust(Materials.Bismuth);
    public static final TST_ItemID PoloniumDust = dust(MaterialsElements.getInstance().POLONIUM.getDust(1));
    public static final TST_ItemID FranciumDust = dust(Materials.Francium);
    public static final TST_ItemID RadiumDust = dust(MaterialsElements.getInstance().RADIUM.getDust(1));
    public static final TST_ItemID ThoriumDust = dust(Materials.Thorium);
    public static final TST_ItemID ProtactiniumDust = dust(MaterialsElements.getInstance().PROTACTINIUM.getDust(1));
    public static final TST_ItemID UraniumDust = dust(Materials.Uranium);
    public static final TST_ItemID NeptuniumDust = dust(MaterialsElements.getInstance().NEPTUNIUM.getDust(1));
    public static final TST_ItemID PlutoniumDust = dust(Materials.Plutonium);
    public static final TST_ItemID AmericiumDust = dust(Materials.Americium);
    public static final TST_ItemID CuriumDust = dust(MaterialsElements.getInstance().CURIUM.getDust(1));
    public static final TST_ItemID CaliforniumDust = dust(WerkstoffLoader.Californium);
    public static final TST_ItemID FermiumDust = dust(MaterialsElements.getInstance().FERMIUM.getDust(1));
    public static final TST_ItemID DubniumDust = dust(MaterialsTST.Dubnium);
    public static final TST_ItemID FleroviumDust = dust(Materials.Flerovium);

    // Fictional elemental full dusts used by the naquadah processing family.
    public static final TST_ItemID NaquadahDust = dust(Materials.Naquadah);
    public static final TST_ItemID EnrichedNaquadahDust = dust(Materials.NaquadahEnriched);
    public static final TST_ItemID NaquadriaDust = dust(Materials.Naquadria);
    public static final TST_ItemID TriniumDust = dust(Materials.Trinium);
    public static final TST_ItemID AdamantiumDust = dust(Materials.Adamantium);

    // Elemental ingots currently used by recipes.
    public static final TST_ItemID PotassiumIngot = TST_ItemID.create(Materials.Potassium.getIngots(1));
    public static final TST_ItemID StrontiumIngot = TST_ItemID.create(Materials.Strontium.getIngots(1));
    public static final TST_ItemID TitaniumIngot = TST_ItemID.create(Materials.Titanium.getIngots(1));
    public static final TST_ItemID HotTitaniumIngot = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Titanium, 1));
    public static final TST_ItemID EnrichedNaquadahIngot = TST_ItemID.create(Materials.NaquadahEnriched.getIngots(1));
    public static final TST_ItemID HotEnrichedNaquadahIngot = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.NaquadahEnriched, 1));
    public static final TST_ItemID NaquadriaIngot = TST_ItemID.create(Materials.Naquadria.getIngots(1));
    public static final TST_ItemID HotNaquadriaIngot = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Naquadria, 1));
    public static final TST_ItemID HafniumIngot = TST_ItemID.create(WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingot, 1));
    public static final TST_ItemID HotHafniumIngot = TST_ItemID.create(WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 1));
    public static final TST_ItemID ZirconiumIngot = TST_ItemID.create(WerkstoffLoader.Zirconium.get(OrePrefixes.ingot, 1));
    public static final TST_ItemID HotZirconiumIngot = TST_ItemID.create(WerkstoffLoader.Zirconium.get(OrePrefixes.ingotHot, 1));

    // Alloy ingots used by the crystalline-alloy input path.
    public static final TST_ItemID CrystallineAlloyIngot = TST_ItemID.create(Materials.CrystallineAlloy.getIngots(1));
    public static final TST_ItemID HotCrystallineAlloyIngot = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.CrystallineAlloy, 1));
    public static final TST_ItemID PulsatingIronIngot = TST_ItemID.create(Materials.PulsatingIron.getIngots(1));

    // Elemental isotope full dusts.
    public static final TST_ItemID Lithium7Dust = dust(MaterialsElements.getInstance().LITHIUM7.getDust(1));
    public static final TST_ItemID Uranium232Dust = dust(MaterialsElements.getInstance().URANIUM232.getDust(1));
    public static final TST_ItemID Uranium233Dust = dust(MaterialsElements.getInstance().URANIUM233.getDust(1));
    public static final TST_ItemID Uranium235Dust = dust(Materials.Uranium235);
    public static final TST_ItemID Plutonium238Dust = dust(MaterialsElements.getInstance().PLUTONIUM238.getDust(1));
    public static final TST_ItemID Plutonium241Dust = dust(Materials.Plutonium241);

    // Compound full dusts currently used by recipes.
    public static final TST_ItemID AdamantiumOxideDust = dust(GGMaterial.adamantine);
    public static final TST_ItemID CalciumChlorideDust = dust(WerkstoffLoader.CalciumChloride);
    public static final TST_ItemID CalciumCarbonateDust = dust(GregtechItemList.CalciumCarbonateDust.get(1));
    public static final TST_ItemID CalciumHydrideDust = dust(Materials.Calciumhydride);
    public static final TST_ItemID CalciumDisilicideDust = dust(Materials.CalciumDisilicide);
    public static final TST_ItemID IridiumChlorideDust = dust(WerkstoffLoader.IridiumChloride);
    public static final TST_ItemID IridiumDioxideDust = dust(WerkstoffLoader.IridiumDioxide);
    public static final TST_ItemID PotassiumDichromateDust = dust(Materials.Potassiumdichromate);
    public static final TST_ItemID PotassiumDisulfateDust = dust(WerkstoffLoader.PotassiumDisulfate);
    public static final TST_ItemID PotassiumHydroxideDust = dust(Materials.PotassiumHydroxide);
    public static final TST_ItemID PotassiumNitrateDust = dust(Materials.Saltpeter);
    public static final TST_ItemID RhodiumNitrateDust = dust(WerkstoffLoader.RHNitrate);
    public static final TST_ItemID RhodiumIIIChlorideDust = dust(Materials.RhodiumChloride);
    public static final TST_ItemID RutheniumTetroxideDust = dust(WerkstoffLoader.RutheniumTetroxide);
    public static final TST_ItemID SiliconDioxideDust = dust(Materials.SiliconDioxide);
    public static final TST_ItemID SodiumChlorideDust = dust(Materials.Salt);
    public static final TST_ItemID SodiumHydrideDust = dust(Materials.SodiumHydride);
    public static final TST_ItemID SodiumBorohydrideDust = dust(Materials.SodiumBorohydride);
    public static final TST_ItemID SodiumMethoxideDust = dust(Materials.SodiumMethoxide);
    public static final TST_ItemID SodiumHydroxideDust = dust(Materials.SodiumHydroxide);
    public static final TST_ItemID SodiumAluminateDust = dust(Materials.SodiumAluminate);
    public static final TST_ItemID AluminiumHydroxideDust = dust(Materials.Aluminiumhydroxide);
    public static final TST_ItemID SodiumNitrateDust = dust(WerkstoffLoader.SodiumNitrate);
    public static final TST_ItemID SodiumRuthenateDust = dust(WerkstoffLoader.SodiumRuthenate);
    public static final TST_ItemID SodiumSulfateDust = dust(WerkstoffLoader.Sodiumsulfate);
    public static final TST_ItemID ZincSulfateDust = dust(WerkstoffLoader.ZincSulfate);
    public static final TST_ItemID AmmoniumNitrateDust = dust(BotWerkstoffMaterialPool.AmmoniumNitrate);
    public static final TST_ItemID RedZirconDust = dust(WerkstoffLoader.RedZircon);
    public static final TST_ItemID ThoriumDioxideDust = dust(WerkstoffLoader.Thorianit);
    public static final TST_ItemID HafniumDioxideDust = dust(WerkstoffMaterialPool.Hafnia);
    public static final TST_ItemID ZirconiumDioxideDust = dust(WerkstoffMaterialPool.Zirconia);
    public static final TST_ItemID HafniumTetrachlorideDust = dust(WerkstoffMaterialPool.HafniumTetrachloride);
    public static final TST_ItemID ZirconiumTetrachlorideDust = dust(WerkstoffMaterialPool.ZirconiumTetrachloride);
    public static final TST_ItemID HafniumIodideDust = dust(WerkstoffMaterialPool.HafniumIodide);
    public static final TST_ItemID MagnesiumChlorideDust = dust(Materials.Magnesiumchloride);
    public static final TST_ItemID LithiumTungstateDust = dust(Materials.Tungstate);
    public static final TST_ItemID ManganeseTungstateDust = dust(WerkstoffLoader.Huebnerit);
    public static final TST_ItemID IronTungstateDust = dust(WerkstoffLoader.Ferberite);
    public static final TST_ItemID CalciumTungstateDust = dust(Materials.Scheelite);
    public static final TST_ItemID TungsticAcidDust = dust(BotWerkstoffMaterialPool.TungsticAcid);
    public static final TST_ItemID TungstenTrioxideDust = dust(BotWerkstoffMaterialPool.TungstenTrioxide);
    public static final TST_ItemID RutileDust = dust(Materials.Rutile);
    public static final TST_ItemID SphaleriteDust = dust(Materials.Sphalerite);
    public static final TST_ItemID GalenaDust = dust(Materials.Galena);
    public static final TST_ItemID CalciteDust = dust(Materials.Calcite);
    public static final TST_ItemID SodiumCarbonateDust = dust(Materials.SodiumCarbonate);
    public static final TST_ItemID HafniumZirconiumOxideBlendDust = dust(WerkstoffMaterialPool.HafniaZirconiaBlend);
    public static final TST_ItemID LowPurityHafniumDust = dust(WerkstoffMaterialPool.LowPurityHafnium);
    public static final TST_ItemID OrangeMetalCatalyst = TST_ItemID.create(GregtechItemList.OrangeMetalCatalyst.get(1));
    public static final TST_ItemID SugarDust = dust(Materials.Sugar);
    public static final TST_ItemID VanadiumPentoxideDust = dust(GGMaterial.vanadiumPentoxide);
    public static final TST_ItemID CeriumDioxideDust = dust(WerkstoffMaterialPool.CeriumDioxide);
    public static final TST_ItemID CeriumChlorideDust = dust(WerkstoffMaterialPool.CeriumChloride);
    public static final TST_ItemID CeriumOxalateDust = dust(WerkstoffMaterialPool.CeriumOxalate);
    public static final TST_ItemID CeriumIIIOxideDust = dust(WerkstoffMaterialPool.CeriumIIIOxide);
    public static final TST_ItemID EuropiumIIIOxideDust = dust(WerkstoffMaterialPool.EuropiumIIIOxide);
    public static final TST_ItemID EuropiumOxideDust = dust(WerkstoffMaterialPool.EuropiumOxide);
    public static final TST_ItemID LanthanumChlorideDust = dust(WerkstoffMaterialPool.LanthanumChloride);
    public static final TST_ItemID NeodymiumOxideDust = dust(WerkstoffMaterialPool.NeodymiumOxide);
    public static final TST_ItemID TerbiumNitrateDust = dust(WerkstoffMaterialPool.TerbiumNitrate);
    public static final TST_ItemID StrontiumHydroxideDust = dust(MaterialMisc.STRONTIUM_HYDROXIDE.getDust(1));
    public static final TST_ItemID TitaniumTrifluorideDust = dust(GGMaterial.titaniumTrifluoride);
    public static final TST_ItemID NaquadahDioxideDust = dust(GGMaterial.naquadahine);
    public static final TST_ItemID EnrichedNaquadahSulfateDust = dust(GGMaterial.enrichedNaquadahSulphate);
    public static final TST_ItemID NaquadriaSulfateDust = dust(GGMaterial.naquadriaSulphate);
    public static final TST_ItemID MagnesiumSulfateDust = dust(GGMaterial.magnesiumSulphate);
    public static final TST_ItemID TriniumSulfateDust = dust(GGMaterial.triniumSulphate);
    public static final TST_ItemID GalliumHydroxideDust = dust(GGMaterial.galliumHydroxide);
    public static final TST_ItemID IndiumPhosphateDust = dust(GGMaterial.indiumPhosphate);
    public static final TST_ItemID RoquesiteDust = dust(WerkstoffLoader.Roquesit);
    public static final TST_ItemID SodiumOxideDust = dust(Materials.SodiumOxide);
    public static final TST_ItemID QuicklimeDust = dust(Materials.Quicklime);
    public static final TST_ItemID BariumSulfateDust = dust(Materials.Barite);
    public static final TST_ItemID TricalciumPhosphateDust = dust(Materials.TricalciumPhosphate);
    public static final TST_ItemID PhosphateDust = dust(Materials.Phosphate);
    public static final TST_ItemID CalciumFluorideDust = dust(WerkstoffMaterialPool.CalciumFluoride);
    public static final TST_ItemID FluorsparDust = dust(WerkstoffLoader.Fluorspar);
    public static final TST_ItemID BoraxDust = dust(Materials.Borax);
    public static final TST_ItemID TriphenylphosphineDust = dust(Materials.Triphenylphosphene);

    // Purification reagents and reusable optical tools.
    public static final TST_ItemID PreActivatedCarbonDust = dust(Materials.PreActivatedCarbon);
    public static final TST_ItemID DirtyActivatedCarbonDust = dust(Materials.DirtyActivatedCarbon);
    public static final TST_ItemID ActivatedCarbonDust = dust(Materials.ActivatedCarbon);
    public static final TST_ItemID ActivatedCarbonFilterMesh = TST_ItemID.create(ItemList.ActivatedCarbonFilterMesh.get(1));
    public static final TST_ItemID ZincFoil = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 1));
    public static final TST_ItemID ZincIngot = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Zinc, 1));
    public static final TST_ItemID BlueTopazLens = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1));
    public static final TST_ItemID LapisDust = dust(Materials.Lapis);
    public static final TST_ItemID CallistoIceDust = dust(Materials.CallistoIce);
    public static final TST_ItemID LedoxDust = dust(Materials.Ledox);


    // Biological feedstocks, polymer intermediates and non-consumed tools.
    public static final TST_ItemID RawMeatDust = dust(Materials.MeatRaw);
    public static final TST_ItemID BoneDust = dust(Materials.Bone);
    public static final TST_ItemID OsmiridiumDust = dust(Materials.Osmiridium);
    public static final TST_ItemID MysteriousCrystalDust = dust(Materials.MysteriousCrystal);
    public static final TST_ItemID TritaniumDust = dust(Materials.Tritanium);
    public static final TST_ItemID CosmicNeutroniumDust = dust(Materials.CosmicNeutronium);
    public static final TST_ItemID MytrylDust = dust(Materials.Mytryl);
    public static final TST_ItemID RubyDust = dust(Materials.Ruby);
    public static final TST_ItemID AshDust = dust(Materials.Ash);
    public static final TST_ItemID EnergiumDust = TST_ItemID.create(ItemList.IC2_Energium_Dust.get(1));
    public static final TST_ItemID StemCells = TST_ItemID.create(ItemList.Circuit_Chip_Stemcell.get(1));
    public static final TST_ItemID BioCells = TST_ItemID.create(ItemList.Circuit_Chip_Biocell.get(1));
    public static final TST_ItemID Agar = modItem(Mods.NewHorizonsCoreMod, "GTNHBioItems", 2);
    public static final TST_ItemID UnknownCrystal = modItem(Mods.GalaxySpace, "item.UnknowCrystal", 0);
    public static final TST_ItemID TauCetiESeaweed = modItem(Mods.GalaxySpace, "tcetiedandelions", 0);
    public static final TST_ItemID TauCetiESeaweedExtract = modItem(Mods.NewHorizonsCoreMod, "TCetiESeaweedExtract", 0);
    public static final TST_ItemID PamSeaweed = modItem(Mods.PamsHarvestCraft, "seaweedItem", 0);
    public static final TST_ItemID KevlarPlate = TST_ItemID.create(Materials.Kevlar.getPlates(1));
    public static final TST_ItemID KevlarFiber = TST_ItemID.create(ItemList.KevlarFiber.get(1));
    public static final TST_ItemID WovenKevlar = TST_ItemID.create(ItemList.WovenKevlar.get(1));
    public static final TST_ItemID GoldenBrownCelluloseFiber = TST_ItemID.create(GregtechItemList.GoldenBrownCelluloseFiber.get(1));
    public static final TST_ItemID RedCelluloseFiber = TST_ItemID.create(GregtechItemList.RedCelluloseFiber.get(1));
    public static final TST_ItemID GoldenBrownAlgaeBiomass = TST_ItemID.create(GregtechItemList.GoldenBrownAlgaeBiomass.get(1));
    public static final TST_ItemID RedAlgaeBiomass = TST_ItemID.create(GregtechItemList.RedAlgaeBiomass.get(1));
    public static final TST_ItemID FormaldehydeCatalyst = TST_ItemID.create(GregtechItemList.FormaldehydeCatalyst.get(1));
    public static final TST_ItemID KevlarCatalystDust = dust(Materials.KevlarCatalyst);
    public static final TST_ItemID PentaerythritolDust = dust(Materials.Pentaerythritol);
    public static final TST_ItemID DiphenylmethaneDiisocyanateDust = dust(Materials.DiphenylmethaneDiisocyanate);
    public static final TST_ItemID ParaphenylenediamineDust = dust(Materials.ParaPhenylenediamine);
    public static final TST_ItemID TerephthaloylChlorideDust = dust(Materials.TerephthaloylChloride);
    public static final TST_ItemID OrganorhodiumCatalystDust = dust(Materials.OrganorhodiumCatalyst);
    public static final TST_ItemID CopperIIOxideDust = dust(Materials.CupricOxide);
    public static final TST_ItemID BismuthIIIOxideDust = dust(Materials.BismuthIIIOxide);
    public static final TST_ItemID AluminiumOxideDust = dust(Materials.Aluminiumoxide);
    public static final TST_ItemID ButynediolDust = dust(Materials.IIButinIIVdiol);
    public static final TST_ItemID ActivatedRaneyNickelDust = dust(Materials.RaneyNickelActivated);
    public static final TST_ItemID CalciumCarbideDust = dust(Materials.CalciumCarbide);
    public static final TST_ItemID CobaltNaphthenateDust = dust(Materials.CobaltIINaphthenate);
    public static final TST_ItemID CobaltHydroxideDust = dust(Materials.CobaltIIHydroxide);
    public static final TST_ItemID CobaltNitrateDust = dust(Materials.CobaltIINitrate);
    public static final TST_ItemID NickelAluminideIngot = TST_ItemID.create(Materials.NickelAluminide.getIngots(1));
    public static final TST_ItemID InfinityCatalystDust = dust(Materials.InfinityCatalyst);

    // Mixture full dusts currently used by recipes.
    public static final TST_ItemID IlmeniteDust = dust(Materials.Ilmenite);
    public static final TST_ItemID BauxiteDust = dust(Materials.Bauxite);
    public static final TST_ItemID IlmeniteSlagDust = dust(Materials.IlmeniteSlag);
    public static final TST_ItemID BauxiteSlagDust = dust(Materials.BauxiteSlag);

    // Source markers retain native crushed inputs; preprocessing replaces only these with full mineral dusts.
    public static final TST_ItemID CrushedIlmenite = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Ilmenite, 1));
    public static final TST_ItemID CrushedBauxite = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Bauxite, 1));
    public static final TST_ItemID PurifiedSphalerite = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sphalerite, 1));
    public static final TST_ItemID PurifiedGalena = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Galena, 1));

    // Native crushed inputs and milled intermediates for indium flotation.
    public static final TST_ItemID CrushedSphalerite = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Sphalerite, 1));
    public static final TST_ItemID CrushedChalcopyrite = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Chalcopyrite, 1));
    public static final TST_ItemID MilledSphalerite = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.milled, Materials.Sphalerite, 1));
    public static final TST_ItemID MilledChalcopyrite = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.milled, Materials.Chalcopyrite, 1));
    public static final TST_ItemID CrushedPineMaterials = TST_ItemID.create(GregtechItemList.CrushedPineMaterials.get(1));

    public static final TST_ItemID CrystallineAlloyDust = dust(Materials.CrystallineAlloy);
    public static final TST_ItemID PulsatingIronDust = dust(Materials.PulsatingIron);
    public static final TST_ItemID RedstoneAlloyDust = dust(Materials.RedstoneAlloy);
    public static final TST_ItemID RedstoneDust = dust(Materials.Redstone);
    public static final TST_ItemID CoalDust = dust(Materials.Coal);
    public static final TST_ItemID EnderPearlDust = dust(Materials.EnderPearl);
    public static final TST_ItemID EmeraldDust = dust(Materials.Emerald);
    public static final TST_ItemID CrudeRhodiumMetalDust = dust(WerkstoffLoader.CrudeRhMetall);
    public static final TST_ItemID IridiumMetalResidueDust = dust(WerkstoffLoader.IrLeachResidue);
    public static final TST_ItemID LeachResidueDust = dust(WerkstoffLoader.LeachResidue);
    public static final TST_ItemID MetallicSludgeResidueDust = dust(WerkstoffLoader.PGSDResidue2);
    public static final TST_ItemID PalladiumMetallicPowder = dust(WerkstoffLoader.PDMetallicPowder);
    public static final TST_ItemID PalladiumSaltDust = dust(WerkstoffLoader.PDSalt);
    public static final TST_ItemID PlatinumMetallicPowder = dust(WerkstoffLoader.PTMetallicPowder);
    public static final TST_ItemID PlatinumResidueDust = dust(WerkstoffLoader.PTResidue);
    public static final TST_ItemID PlatinumSaltDust = dust(WerkstoffLoader.PTSaltCrude);
    public static final TST_ItemID RarestMetalResidueDust = dust(WerkstoffLoader.IrOsLeachResidue);
    public static final TST_ItemID RefinedPlatinumSaltDust = dust(WerkstoffLoader.PTSaltRefined);
    public static final TST_ItemID ReprecipitatedPalladiumDust = dust(WerkstoffLoader.PDRawPowder);
    public static final TST_ItemID ReprecipitatedPlatinumDust = dust(WerkstoffLoader.PTRawPowder);
    public static final TST_ItemID ReprecipitatedRhodiumDust = dust(WerkstoffLoader.ReRh);
    public static final TST_ItemID RhodiumFilterCakeDust = dust(WerkstoffLoader.RhFilterCake);
    public static final TST_ItemID RhodiumSaltDust = dust(WerkstoffLoader.RHSalt);
    public static final TST_ItemID SludgeResidueDust = dust(WerkstoffLoader.PGSDResidue);
    public static final TST_ItemID MonaziteSulfateDust = dust(WerkstoffMaterialPool.MonaziteSulfate);
    public static final TST_ItemID AcidicMonaziteDust = dust(WerkstoffMaterialPool.AcidicMonazitePowder);
    public static final TST_ItemID MonaziteRareEarthFiltrateDust = dust(WerkstoffMaterialPool.MonaziteRareEarthFiltrate);
    public static final TST_ItemID NeutralizedMonaziteRareEarthFiltrateDust = dust(WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate);
    public static final TST_ItemID MonaziteRareEarthHydroxideConcentrateDust = dust(WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate);
    public static final TST_ItemID DriedMonaziteRareEarthConcentrateDust = dust(WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate);
    public static final TST_ItemID CooledMonaziteRareEarthConcentrateDust = dust(WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate);
    public static final TST_ItemID MonaziteRarerEarthSedimentDust = dust(WerkstoffMaterialPool.MonaziteRarerEarthSediment);
    public static final TST_ItemID MonaziteHalogenicRareEarthMixtureDust = dust(WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture);
    public static final TST_ItemID SaturatedMonaziteRareEarthMixtureDust = dust(WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture);
    public static final TST_ItemID BastnasiteRareEarthOxideDust = dust(WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder);
    public static final TST_ItemID LeachedBastnasiteRareEarthOxidesDust = dust(WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides);
    public static final TST_ItemID RoastedRareEarthOxidesDust = dust(WerkstoffMaterialPool.RoastedRareEarthOxides);
    public static final TST_ItemID WetRareEarthOxidesDust = dust(WerkstoffMaterialPool.WetRareEarthOxides);
    public static final TST_ItemID CeriumOxidizedRareEarthOxidesDust = dust(WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides);
    public static final TST_ItemID BastnasiteRarerEarthOxidesDust = dust(WerkstoffMaterialPool.BastnasiteRarerEarthOxides);
    public static final TST_ItemID NeodymicRareEarthConcentrateDust = dust(WerkstoffMaterialPool.NeodymicRareEarthConcentrate);
    public static final TST_ItemID SamaricRareEarthConcentrateDust = dust(WerkstoffMaterialPool.SamaricRareEarthConcentrate);
    public static final TST_ItemID FluorinatedSamaricConcentrateDust = dust(WerkstoffMaterialPool.FluorinatedSamaricConcentrate);
    public static final TST_ItemID SamariumTerbiumMixtureDust = dust(WerkstoffMaterialPool.SamariumTerbiumMixture);
    public static final TST_ItemID NitratedSamariumTerbiumMixtureDust = dust(WerkstoffMaterialPool.NitratedSamariumTerbiumMixture);
    public static final TST_ItemID SamaricResidueDust = dust(WerkstoffMaterialPool.SamaricResidue);
    public static final TST_ItemID CeriumRichMixtureDust = dust(WerkstoffMaterialPool.CeriumRichMixture);
    public static final TST_ItemID SamariumOreConcentrateDust = dust(WerkstoffMaterialPool.SamariumOreConcentrate);
    public static final TST_ItemID DephosphatedSamariumConcentrateDust = dust(WerkstoffMaterialPool.DephosphatedSamariumConcentrate);
    public static final TST_ItemID SamariumOxalateDust = dust(WerkstoffMaterialPool.SamariumOxalate);
    public static final TST_ItemID SamariumChlorideDust = dust(WerkstoffMaterialPool.SamariumChloride);
    public static final TST_ItemID SamariumChlorideSodiumChlorideBlendDust = dust(WerkstoffMaterialPool.SamariumChlorideSodiumChlorideBlend);
    public static final TST_ItemID RarestEarthResidueDust = dust(WerkstoffMaterialPool.RarestEarthResidue);
    public static final TST_ItemID ThoriumPhosphateCakeDust = dust(WerkstoffMaterialPool.ThoriumPhosphateCake);
    public static final TST_ItemID ThoriumPhosphateConcentrateDust = dust(WerkstoffMaterialPool.ThoriumPhosphateConcentrate);
    public static final TST_ItemID UraniumFiltrateDust = dust(WerkstoffMaterialPool.UraniumFiltrate);
    public static final TST_ItemID NeutralizedUraniumFiltrateDust = dust(WerkstoffMaterialPool.NeutralizedUraniumFiltrate);
    public static final TST_ItemID NaquadahOxideMixtureDust = dust(GGMaterial.naquadahEarth);
    public static final TST_ItemID EnrichedNaquadahOxideMixtureDust = dust(GGMaterial.enrichedNaquadahEarth);
    public static final TST_ItemID NaquadriaOxideMixtureDust = dust(GGMaterial.naquadriaEarth);
    public static final TST_ItemID ConcentratedEnrichedNaquadahSludgeDust = dust(GGMaterial.concentratedEnrichedNaquadahSludge);
    public static final TST_ItemID LowQualityNaquadriaPhosphateDust = dust(GGMaterial.lowQualityNaquadriaPhosphate);
    public static final TST_ItemID LowQualityNaquadriaSulfateDust = dust(GGMaterial.lowQualityNaquadriaSulphate);
    public static final TST_ItemID PrismaticNaquadahDust = dust(Materials.PrismaticNaquadah);

    // Physical entry markers and prismatic process intermediates.
    public static final TST_ItemID Fruit = TST_ItemID.create(Items.apple);
    public static final TST_ItemID BastnasiteDust = dust(Materials.Bastnasite);
    public static final TST_ItemID MonaziteDust = dust(Materials.Monazite);
    public static final TST_ItemID PrismarineShard = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.shard, Materials.Prismarine, 1));
    public static final TST_ItemID PrismarinePrecipitate = TST_ItemID.create(ItemList.Prismarine_Precipitate.get(1));
    public static final TST_ItemID PrismaticCrystal = TST_ItemID.create(ItemList.Prismatic_Crystal.get(1));
    public static final TST_ItemID DiamondGem = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 1));
    public static final TST_ItemID EnderPearl = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1));
    public static final TST_ItemID PulsatingCrystal = enderIOItem(5);
    public static final TST_ItemID PulsatingCrystalDust = enderIOItem(14);

    // Advanced-material process items and optional singularity identities.
    public static final TST_ItemID MagMatterRound = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.round, Materials.MagMatter, 1));
    public static final TST_ItemID PhononicSeedCrystal = TST_ItemID.create(ItemList.Phononic_Seed_Crystal.get(1));
    public static final TST_ItemID EternityDust = dust(Materials.Eternity);
    public static final TST_ItemID EternalSingularity = modItem(Mods.EternalSingularity, "eternal_singularity", 0);
    public static final TST_ItemID ChronicSingularity = modItem(Mods.EternalSingularity, "combined_singularity", 15);

    // Registered process items used by the netherite trunks and reagent branches.
    public static final TST_ItemID AncientDebris = etFuturumItem("ancient_debris");
    public static final TST_ItemID NetheriteScrap = etFuturumItem("netherite_scrap");
    public static final TST_ItemID HotNetheriteScrap = TST_ItemID.create(ItemList.Hot_Netherite_Scrap.get(1));
    public static final TST_ItemID BrittleNetheriteScrap = TST_ItemID.create(ItemList.Brittle_Netherite_Scrap.get(1));
    public static final TST_ItemID NetheriteNanoparticles = TST_ItemID.create(ItemList.Netherite_Nanoparticles.get(1));
    public static final TST_ItemID IntenselyBondedNetheriteNanoparticles = TST_ItemID.create(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1));
    public static final TST_ItemID HeavyHellishMud = TST_ItemID.create(ItemList.Heavy_Hellish_Mud.get(1));
    public static final TST_ItemID HellishMetalBlock = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.block, Materials.HellishMetal, 1));
    public static final TST_ItemID RhodiumBlock = TST_ItemID.create(MaterialsElements.getInstance().RHODIUM.getBlock(1));
    public static final TST_ItemID Netherrack = TST_ItemID.create(new ItemStack(Blocks.netherrack));
    public static final TST_ItemID MilledNetherrack = TST_ItemID.create(GTOreDictUnificator.get(OrePrefixes.milled, Materials.Netherrack, 1));
    public static final TST_ItemID AluminaMillingBall = TST_ItemID.create(GregtechItemList.Milling_Ball_Alumina.get(1));

    public static TST_ItemID resolvePlatinumLeafAlias() {
        return resolveCropsLeafAlias(12);
    }

    public static TST_ItemID resolveCropsLeafAlias(int metadata) {
        if (!Mods.CropsNH.isModLoaded()) return null;
        ItemStack fallback = new ItemStack(Items.stick);
        ItemStack leaf = ModItem.getModItem("cropsnh", "materialLeaf", 1, metadata, fallback);
        return leaf == fallback ? null : TST_ItemID.create(leaf);
    }

    public static ItemStack[] platinumInputAlternatives(TST_ItemID platinumLeafAlias) {
        return platinumLeafAlias == null ? null
            : new ItemStack[] { PlatinumMetallicPowder.getItemStack(1),
                platinumLeafAlias.getItemStack(PLATINUM_LEAVES_PER_POWDER) };
    }

    // Elemental gases, liquids and melts, ordered by atomic number.
    public static final Fluid HydrogenGas = fluid(Materials.Hydrogen.getGas(1));
    public static final Fluid NitrogenGas = fluid(Materials.Nitrogen.getGas(1));
    public static final Fluid OxygenGas = fluid(Materials.Oxygen.getGas(1));
    public static final Fluid FluorineGas = fluid(Materials.Fluorine.getGas(1));
    public static final Fluid ChlorineGas = fluid(Materials.Chlorine.getGas(1));
    public static final Fluid PotassiumMolten = fluid(Materials.Potassium.getMolten(1));
    public static final Fluid StrontiumMolten = fluid(Materials.Strontium.getMolten(1));
    public static final Fluid XenonGas = fluid(WerkstoffLoader.Xenon.getFluidOrGas(1));
    public static final Fluid OganessonGas = fluid(WerkstoffLoader.Oganesson.getFluidOrGas(1));

    // Compound fluids currently used by recipes.
    public static final Fluid TitaniumTetrachloride = fluid(Materials.Titaniumtetrachloride.getFluid(1));
    public static final Fluid SodiumTungstate = fluid(BotWerkstoffMaterialPool.SodiumTungstate.getFluidOrGas(1));
    public static final Fluid IndiumConcentrate = fluid(new FluidStack(ItemList.sIndiumConcentrate, 1));
    public static final Fluid LeadZincSolution = fluid(new FluidStack(ItemList.sLeadZincSolution, 1));
    public static final Fluid NitricAcid = fluid(Materials.NitricAcid.getFluid(1));
    public static final Fluid HydrochloricAcid = fluid(Materials.HydrochloricAcid.getFluid(1));
    public static final Fluid AmmoniaGas = fluid(Materials.Ammonia.getGas(1));
    public static final Fluid Water = fluid(Materials.Water.getFluid(1));
    public static final Fluid Steam = fluid(Materials.Steam.getGas(1));
    public static final Fluid SuperheatedSteam = fluid(GTModHandler.getSuperHeatedSteam(1));
    public static final Fluid Grade2PurifiedWater = fluid(Materials.Grade2PurifiedWater.getFluid(1));
    public static final Fluid Air = fluid(Materials.Air.getGas(1));
    public static final Fluid OzoneGas = fluid(Materials.Ozone.getGas(1));
    public static final Fluid PolyAluminiumChloride = fluid(Materials.PolyAluminiumChloride.getFluid(1));
    public static final Fluid FlocculationWasteLiquid = fluid(Materials.FlocculationWasteLiquid.getFluid(1));
    public static final Fluid SuperCoolant = fluid(Materials.SuperCoolant.getFluid(1));
    public static final Fluid IC2Coolant = fluid(GTModHandler.getIC2Coolant(1));
    public static final Fluid CarbonMonoxideGas = fluid(Materials.CarbonMonoxide.getGas(1));
    public static final Fluid CarbonDioxideGas = fluid(Materials.CarbonDioxide.getGas(1));
    public static final Fluid MethaneGas = fluid(Materials.Methane.getGas(1));
    public static final Fluid Benzene = fluid(Materials.Benzene.getFluid(1));
    public static final Fluid Chlorobenzene = fluid(Materials.Chlorobenzene.getFluid(1));
    public static final Fluid PhosphorusTrichloride = fluid(Materials.PhosphorusTrichloride.getFluid(1));
    public static final Fluid TrimethylBorate = fluid(Materials.TrimethylBorate.getFluid(1));
    public static final Fluid BoricAcid = Objects.requireNonNull(FluidRegistry.getFluid("boricacid"), "boricacid");
    public static final Fluid Dimethylbenzene = fluid(Materials.Dimethylbenzene.getFluid(1));
    public static final Fluid SulfuricAcid = fluid(Materials.SulfuricAcid.getFluid(1));
    public static final Fluid DilutedSulfuricAcid = fluid(Materials.DilutedSulfuricAcid.getFluid(1));
    public static final Fluid Phenol = fluid(Materials.Phenol.getFluid(1));
    public static final Fluid EthyleneGas = fluid(Materials.Ethylene.getGas(1));
    public static final Fluid NitrogenDioxideGas = fluid(Materials.NitrogenDioxide.getGas(1));
    public static final Fluid NitrogenMonoxideGas = fluid(Materials.NitricOxide.getGas(1));
    public static final Fluid AmmoniumChlorideFluid = fluid(WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1));
    public static final Fluid SodiumFormateFluid = fluid(WerkstoffLoader.Sodiumformate.getFluidOrGas(1));
    public static final Fluid FormicAcid = fluid(WerkstoffLoader.FormicAcid.getFluidOrGas(1));
    public static final Fluid CalciumChlorideFluid = fluid(WerkstoffLoader.CalciumChloride.getFluidOrGas(1));
    public static final Fluid RutheniumTetroxideFluid = fluid(WerkstoffLoader.RutheniumTetroxide.getFluidOrGas(1));
    public static final Fluid RhodiumSulfateGas = fluid(WerkstoffLoader.RHSulfate.getFluidOrGas(1));
    public static final Fluid PotassiumDisulfateMolten = fluid(WerkstoffLoader.PotassiumDisulfate.getMolten(1));
    public static final Fluid OxalicAcid = fluid(GGMaterial.oxalate.getFluidOrGas(1));
    public static final Fluid AmmoniumNitrateSolution = fluid(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1));
    public static final Fluid SodiumFluorosilicate = fluid(WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(1));
    public static final Fluid CalciumFluorideFluid = fluid(WerkstoffMaterialPool.CalciumFluoride.getFluidOrGas(1));
    public static final Fluid Acetone = fluid(Materials.Acetone.getFluid(1));
    public static final Fluid AceticAcid = fluid(Materials.AceticAcid.getFluid(1));
    public static final Fluid ChloromethaneGas = fluid(Materials.Chloromethane.getGas(1));
    public static final Fluid HexafluorosilicicAcid = fluid(WerkstoffLoader.HexafluorosilicicAcid.getFluidOrGas(1));
    public static final Fluid HydratedAmmoniumNitrateSlurry = fluid(new FluidStack(GTPPFluids.HydratedAmmoniumNitrateSlurry, 1));
    public static final Fluid PhosphoricAcid = fluid(Materials.PhosphoricAcid.getFluid(1));
    public static final Fluid Nitrobenzene = fluid(new FluidStack(GTPPFluids.Nitrobenzene, 1));
    public static final Fluid HydrogenPeroxide = fluid(new FluidStack(GTPPFluids.HydrogenPeroxide, 1));
    public static final Fluid Ethylanthraquinone = fluid(new FluidStack(GTPPFluids.Ethylanthraquinone, 1));
    public static final Fluid Ethylanthrahydroquinone = fluid(new FluidStack(GTPPFluids.Ethylanthrahydroquinone, 1));
    public static final Fluid Anthracene = fluid(new FluidStack(GTPPFluids.Anthracene, 1));
    public static final Fluid DistilledWater = fluid(GTModHandler.getDistilledWater(1));
    public static final Fluid LiquidNitrogen = fluid(Materials.LiquidNitrogen.getGas(1));
    public static final Fluid HafniumTetrachlorideSolution = fluid(WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1));
    public static final Fluid ZirconiumTetrachlorideSolution = fluid(WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1));


    // Biological media and polymer-processing fluids used by the registered chains.
    public static final Fluid Xenoxene = fluid(Materials.Xenoxene.getFluid(1));
    public static final Fluid RawRadox = fluid(Materials.RadoxRaw.getFluid(1));
    public static final Fluid DilutedXenoxene = fluid(Materials.XenoxeneDiluted.getFluid(1));
    public static final Fluid SuperHeavyRadox = fluid(Materials.RadoxSuperHeavy.getFluid(1));
    public static final Fluid HeavyRadox = fluid(Materials.RadoxHeavy.getFluid(1));
    public static final Fluid LightRadox = fluid(Materials.RadoxLight.getGas(1));
    public static final Fluid SuperLightRadox = fluid(Materials.RadoxSuperLight.getGas(1));
    public static final Fluid CrackedRadox = fluid(Materials.RadoxCracked.getGas(1));
    public static final Fluid RadoxGas = fluid(Materials.RadoxGas.getGas(1));
    public static final Fluid RadoxPolymerMolten = fluid(Materials.RadoxPolymer.getMolten(1));
    public static final Fluid GrowthMediumRaw = fluid(Materials.GrowthMediumRaw.getFluid(1));
    public static final Fluid GrowthMediumSterilized = fluid(Materials.GrowthMediumSterilized.getFluid(1));
    public static final Fluid BioMediumRaw = fluid(Materials.BioMediumRaw.getFluid(1));
    public static final Fluid BioMediumSterilized = fluid(Materials.BioMediumSterilized.getFluid(1));
    public static final Fluid EnrichedBacterialSludge = fluid(Materials.EnrichedBacterialSludge.getFluid(1));
    public static final Fluid FermentedBacterialSludge = fluid(Materials.FermentedBacterialSludge.getFluid(1));
    public static final Fluid HydrogenSulfideGas = fluid(Materials.HydricSulfide.getGas(1));
    public static final Fluid LiquidCrystalKevlar = fluid(Materials.LiquidCrystalKevlar.getFluid(1));
    public static final Fluid PolyurethaneResin = fluid(Materials.PolyurethaneResin.getFluid(1));
    public static final Fluid Methanol = fluid(Materials.Methanol.getFluid(1));
    public static final Fluid EthyleneOxideGas = fluid(Materials.EthyleneOxide.getGas(1));
    public static final Fluid EthyleneGlycol = fluid(Materials.Ethyleneglycol.getFluid(1));
    public static final Fluid SiliconeOil = fluid(Materials.SiliconOil.getFluid(1));
    public static final Fluid Dimethyldichlorosilane = fluid(Materials.Dimethyldichlorosilane.getFluid(1));
    public static final Fluid MethylamineGas = fluid(Materials.Methylamine.getGas(1));
    public static final Fluid GammaButyrolactone = fluid(Materials.GammaButyrolactone.getFluid(1));
    public static final Fluid AcetyleneGas = fluid(Materials.Acetylene.getGas(1));
    public static final Fluid Butyraldehyde = fluid(Materials.Butyraldehyde.getFluid(1));
    public static final Fluid Nitroaniline = fluid(Materials.IVNitroaniline.getFluid(1));
    public static final Fluid NitrationMixture = fluid(Materials.NitrationMixture.getFluid(1));
    public static final Fluid NMethylpyrrolidone = fluid(Materials.NMethylIIPyrrolidone.getFluid(1));
    public static final Fluid SulfurTrioxideGas = fluid(Materials.SulfurTrioxide.getGas(1));
    public static final Fluid SulfurDichloride = fluid(Materials.SulfurDichloride.getFluid(1));
    public static final Fluid ThionylChloride = fluid(Materials.ThionylChloride.getFluid(1));
    public static final Fluid DimethylTerephthalate = fluid(Materials.DimethylTerephthalate.getFluid(1));
    public static final Fluid TerephthalicAcid = fluid(Materials.TerephthalicAcid.getFluid(1));
    public static final Fluid Paraxylene = fluid(Materials.IVDimethylbenzene.getFluid(1));
    public static final Fluid NaphthenicAcid = fluid(Materials.NaphthenicAcid.getFluid(1));
    public static final Fluid DiphenylmethaneDiisocyanateMixture = fluid(Materials.DiphenylmethaneDiisocyanateMixture.getFluid(1));
    public static final Fluid DiaminodiphenylmethaneMixture = fluid(Materials.DiaminodiphenylmethanMixture.getFluid(1));
    public static final Fluid AcetaldehydeGas = fluid(Materials.Acetaldehyde.getGas(1));
    public static final Fluid NickelTetracarbonyl = fluid(Materials.NickelTetracarbonyl.getFluid(1));
    public static final Fluid IronIIIChloride = fluid(Materials.IronIIIChloride.getFluid(1));
    public static final Fluid SulfurDioxideGas = fluid(Materials.SulfurDioxide.getGas(1));
    public static final Fluid DilutedHydrochloricAcid = fluid(Materials.DilutedHydrochloricAcid.getFluid(1));
    public static final Fluid MeatExtract = Objects.requireNonNull(FluidRegistry.getFluid("meatextract"), "meatextract");
    public static final Fluid UnknownNutrientAgar = Objects.requireNonNull(FluidRegistry.getFluid("unknownnutrientagar"), "unknownnutrientagar");
    public static final Fluid SeaweedBroth = Objects.requireNonNull(FluidRegistry.getFluid("seaweedbroth"), "seaweedbroth");
    public static final Fluid BacterialSludge = Objects.requireNonNull(FluidRegistry.getFluid("bacterialsludge"), "bacterialsludge");
    public static final Fluid Mutagen = Objects.requireNonNull(FluidRegistry.getFluid("mutagen"), "mutagen");
    public static final Fluid Butanediol = Objects.requireNonNull(FluidRegistry.getFluid("1,4-butanediol"), "1,4-butanediol");
    public static final Fluid Phosgene = fluid(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1));
    public static final Fluid Butanol = Objects.requireNonNull(GTPPFluids.Butanol, "Butanol");
    public static final Fluid Aniline = Objects.requireNonNull(GTPPFluids.Aniline, "Aniline");
    public static final Fluid Formaldehyde = Objects.requireNonNull(GTPPFluids.Formaldehyde, "Formaldehyde");
    public static final Fluid PropionicAcid = Objects.requireNonNull(GTPPFluids.PropionicAcid, "PropionicAcid");
    public static final Fluid FermentationBase = Objects.requireNonNull(GTPPFluids.FermentationBase, "FermentationBase");
    public static final Fluid AceticAnhydride = fluid(MaterialMisc.ACETIC_ANHYDRIDE.getFluidStack(1));

    // Mixture fluids currently used by recipes.
    public static final Fluid BauxiteSlurry = fluid(Materials.BauxiteSlurry.getFluid(1));
    public static final Fluid HeatedBauxiteSlurry = fluid(Materials.HeatedBauxiteSlurry.getFluid(1));
    public static final Fluid IronIISulfateSolution = ItemList.sGreenVitriol;
    public static final Fluid SluiceJuice = fluid(Materials.SluiceJuice.getFluid(1));
    public static final Fluid CrystallineAlloyMolten = fluid(Materials.CrystallineAlloy.getMolten(1));
    public static final Fluid PulsatingIronMolten = fluid(Materials.PulsatingIron.getMolten(1));
    public static final Fluid SaltWater = fluid(Materials.SaltWater.getFluid(1));
    public static final Fluid Lava = fluid(Materials.Lava.getFluid(1));
    public static final Fluid PoorNetherWaste = fluid(Materials.PoorNetherWaste.getFluid(1));
    public static final Fluid NetherAir = fluid(Materials.NetherAir.getFluid(1));
    public static final Fluid NetherSemiFluid = fluid(Materials.NetherSemiFluid.getFluid(1));
    public static final Fluid NefariousGas = fluid(Materials.NefariousGas.getFluid(1));
    public static final Fluid NefariousOil = fluid(Materials.NefariousOil.getFluid(1));
    public static final Fluid HellishMetalMolten = fluid(Materials.HellishMetal.getMolten(1));
    public static final Fluid NetherrackFlotationFroth = fluid(new FluidStack(GTPPFluids.NetherrackFlotationFroth, 1));
    public static final Fluid SphaleriteFlotationFroth = fluid(new FluidStack(GTPPFluids.SphaleriteFlotationFroth, 1));
    public static final Fluid ChalcopyriteFlotationFroth = fluid(new FluidStack(GTPPFluids.ChalcopyriteFlotationFroth, 1));
    public static final Fluid PineOil = fluid(new FluidStack(GTPPFluids.PineOil, 1));
    public static final Fluid RedMud = fluid(new FluidStack(GTPPFluids.RedMud, 1));
    public static final Fluid HydrofluoricAcid = fluid(Materials.HydrofluoricAcid.getFluid(1));
    public static final Fluid PhthalicAcid = fluid(Materials.PhthalicAcid.getFluid(1));
    public static final Fluid PlatinumConcentrateFluid = fluid(WerkstoffLoader.PTConcentrate.getFluidOrGas(1));
    public static final Fluid PalladiumEnrichedAmmoniaFluid = fluid(WerkstoffLoader.PDAmmonia.getFluidOrGas(1));
    public static final Fluid RutheniumTetroxideSolution = fluid(WerkstoffLoader.RutheniumTetroxideSollution.getFluidOrGas(1));
    public static final Fluid HotRutheniumTetroxideSolution = fluid(WerkstoffLoader.HotRutheniumTetroxideSollution.getFluidOrGas(1));
    public static final Fluid AcidicOsmiumSolution = fluid(WerkstoffLoader.AcidicOsmiumSolution.getFluidOrGas(1));
    public static final Fluid OsmiumSolution = fluid(WerkstoffLoader.OsmiumSolution.getFluidOrGas(1));
    public static final Fluid AcidicIridiumSolution = fluid(WerkstoffLoader.AcidicIridiumSolution.getFluidOrGas(1));
    public static final Fluid RhodiumSulfateSolution = fluid(WerkstoffLoader.RHSulfateSolution.getFluidOrGas(1));
    public static final Fluid RhodiumSaltSolution = fluid(WerkstoffLoader.RHSaltSolution.getFluidOrGas(1));
    public static final Fluid RhodiumFilterCakeSolution = fluid(WerkstoffLoader.RHFilterCakeSolution.getFluidOrGas(1));
    public static final Fluid AquaRegia = fluid(WerkstoffLoader.AquaRegia.getFluidOrGas(1));
    public static final Fluid MuddyMonaziteSolution = fluid(WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(1));
    public static final Fluid DilutedMonaziteMud = fluid(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(1));
    public static final Fluid DilutedMonaziteSulfate = fluid(WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(1));
    public static final Fluid NitratedMonaziteConcentrate = fluid(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1));
    public static final Fluid NitricLeachedMonaziteMixture = fluid(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1));
    public static final Fluid NitricMonaziteLeachedConcentrate = fluid(WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1));
    public static final Fluid MuddySamariumRareEarthSolution = fluid(WerkstoffMaterialPool.MuddySamariumRareEarthSolution.getFluidOrGas(1));
    public static final Fluid SamariumRareEarthMud = fluid(WerkstoffMaterialPool.SamariumRareEarthMud.getFluidOrGas(1));
    public static final Fluid DilutedSamariumRareEarthSolution = fluid(WerkstoffMaterialPool.DilutedSamariumRareEarthSolution.getFluidOrGas(1));
    public static final Fluid MuddyBastnasiteSolution = fluid(WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(1));
    public static final Fluid SteamCrackedBastnasiteSolution = fluid(WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.getFluidOrGas(1));
    public static final Fluid ConditionedBastnasiteMud = fluid(WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1));
    public static final Fluid DilutedBastnasiteMud = fluid(WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(1));
    public static final Fluid FilteredBastnasiteMud = fluid(WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(1));
    public static final Fluid NitratedBastnasiteRarerEarthOxides = fluid(WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.getFluidOrGas(1));
    public static final Fluid SaturatedBastnasiteRarerEarthOxides = fluid(WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1));
    public static final Fluid DilutedAcetone = fluid(WerkstoffMaterialPool.DilutedAcetone.getFluidOrGas(1));
    public static final Fluid LowQualityNaquadahEmulsion = fluid(GGMaterial.lowQualityNaquadahEmulsion.getFluidOrGas(1));
    public static final Fluid LowQualityNaquadahSolution = fluid(GGMaterial.lowQualityNaquadahSolution.getFluidOrGas(1));
    public static final Fluid NaquadahAdamantiumSolution = fluid(GGMaterial.naquadahAdamantiumSolution.getFluidOrGas(1));
    public static final Fluid NaquadahRichSolution = fluid(GGMaterial.naquadahRichSolution.getFluidOrGas(1));
    public static final Fluid EnrichedNaquadahRichSolution = fluid(GGMaterial.enrichedNaquadahRichSolution.getFluidOrGas(1));
    public static final Fluid NaquadriaRichSolution = fluid(GGMaterial.naquadriaRichSolution.getFluidOrGas(1));
    public static final Fluid LowQualityNaquadriaSolution = fluid(GGMaterial.lowQualityNaquadriaSolution.getFluidOrGas(1));
    public static final Fluid FluorineRichWasteLiquid = fluid(GGMaterial.fluorineRichWasteLiquid.getFluidOrGas(1));
    public static final Fluid NaquadahWasteLiquid = fluid(GGMaterial.wasteLiquid.getFluidOrGas(1));
    public static final Fluid PrismarineSolution = fluid(Materials.PrismarineSolution.getFluid(1));
    public static final Fluid PrismarineContaminatedHydrogenPeroxide = fluid(Materials.PrismarineContaminatedHydrogenPeroxide.getFluid(1));
    public static final Fluid PrismarineRichNitrobenzeneSolution = fluid(Materials.PrismarineRichNitrobenzeneSolution.getFluid(1));
    public static final Fluid PrismarineContaminatedNitrobenzeneSolution = fluid(Materials.PrismarineContaminatedNitrobenzeSolution.getFluid(1));
    public static final Fluid PrismaticGas = fluid(Materials.PrismaticGas.getFluid(1));
    public static final Fluid PrismaticAcid = fluid(Materials.PrismaticAcid.getFluid(1));
    public static final Fluid PhononCrystalSolution = fluid(Materials.PhononCrystalSolution.getFluid(1));
    public static final Fluid PhononMedium = fluid(Materials.PhononMedium.getFluid(1));
    public static final Fluid EternityMolten = fluid(Materials.Eternity.getMolten(1));
    // spotless:on

    static {
        // The rocket-fuel source produces the same ore-dictionary reagent; cache the native Werkstoff powder.
        UNIFIED_DUSTS.put(TST_ItemID.create(GregtechItemList.AmmoniumNitrateDust.get(1)), AmmoniumNitrateDust);
    }

    /** Runtime lookup and consumption share the cached fruit and native full-dust identities. */
    public static TST_ItemID inputItemId(ItemStack stack) {
        // Native chemical catalysts match item and metadata even after their wear NBT changes.
        if (MTEChemicalPlant.isCatalyst(stack)) return TST_ItemID.createNoNBT(stack);
        TST_ItemID physical = TST_ItemID.create(stack);
        for (ItemStack fruit : FRUIT_INPUTS) {
            if (GTUtility.areStacksEqual(fruit, stack, true)) return Fruit;
        }
        return UNIFIED_DUSTS.getOrDefault(physical, physical);
    }

    /** Source preprocessing uses the same normalization as runtime input lookup. */
    public static TST_ItemID recipeItemId(ItemStack stack) {
        return inputItemId(stack);
    }

    /** Sensitive source inputs keep their exact identity instead of joining a permissive fruit group. */
    static TST_ItemID recipeItemId(ItemStack stack, boolean nbtSensitive) {
        if (nbtSensitive) {
            TST_ItemID physical = TST_ItemID.create(stack);
            return UNIFIED_DUSTS.getOrDefault(physical, physical);
        }
        ItemStack template = stack.copy();
        template.setTagCompound(null);
        return inputItemId(template);
    }

    private static TST_ItemID modItem(Mods mod, String name, int metadata) {
        if (!mod.isModLoaded()) return null;
        ItemStack fallback = new ItemStack(Items.stick);
        ItemStack stack = ModItem.getModItem(mod.ID, name, 1, metadata, fallback);
        return stack == fallback ? null : TST_ItemID.create(stack);
    }

    private static TST_ItemID etFuturumItem(String name) {
        if (!Mods.EtFuturumRequiem.isModLoaded()) return null;
        ItemStack fallback = new ItemStack(Items.stick);
        ItemStack stack = ModItem.getModItem(Mods.EtFuturumRequiem.ID, name, 1, fallback);
        return stack == fallback ? null : TST_ItemID.create(stack);
    }

    private static TST_ItemID enderIOItem(int metadata) {
        if (!Mods.EnderIO.isModLoaded()) return null;
        ItemStack fallback = new ItemStack(Items.stick);
        ItemStack stack = ModItem.getModItem(Mods.EnderIO.ID, "itemMaterial", 1, metadata, fallback);
        return stack == fallback ? null : TST_ItemID.create(stack);
    }

    private static TST_ItemID dust(Materials material) {
        return dust(RecipeMaterialUtils.nativeDust(material));
    }

    private static TST_ItemID dust(Werkstoff material) {
        return dust(material.get(OrePrefixes.dust));
    }

    private static TST_ItemID dust(ItemStack stack) {
        if (stack == null || stack.getItem() == null) throw new IllegalStateException("missing full dust template");
        TST_ItemID nativeId = TST_ItemID.create(stack.copy());
        // GT has already unified so
        // urce recipes, but their outputs must retain our native dust identities.
        TST_ItemID unified = TST_ItemID.create(GTOreDictUnificator.get(false, stack));
        if (!unified.equals(nativeId)) UNIFIED_DUSTS.put(unified, nativeId);
        return nativeId;
    }

    private static Fluid fluid(FluidStack stack) {
        return Objects.requireNonNull(
            Objects.requireNonNull(stack, "missing fluid template")
                .getFluid(),
            "missing fluid");
    }
}
