package com.Nxer.TwistSpaceTechnology.compatibility.GTNH260.system.VoidMinerRework.logic;

import static bloodasp.galacticgreg.registry.GalacticGregRegistry.getModContainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;

import com.Nxer.TwistSpaceTechnology.compatibility.GTNH260.Reflector;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.oregen.BW_OreLayer;
import com.github.bartimaeusnek.bartworks.util.Pair;

import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_Layer_Space;
import bloodasp.galacticgreg.GT_Worldgen_GT_Ore_SmallPieces_Space;
import bloodasp.galacticgreg.GalacticGreg;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_Layer_Space;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_SmallOre_Space;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.GT_Worldgen_GT_Ore_SmallPieces;

public class VoidMinerDataGenerator {

    private final int dimID;
    private float totalWeight = 0;
    private final World world;
    private Map<Pair<Integer, Boolean>, Float> dropmap;

    private VoidMinerDataGenerator(World world) {
        this.world = world;
        this.dimID = world.provider.dimensionId;
    }

    public static VoidMinerDataGenerator generate(World world) {
        return new VoidMinerDataGenerator(world);
    }

    public void done() {
        VoidMinerData.OrePool.put(dimID, new HashMap<>());
        dropmap = VoidMinerData.OrePool.get(dimID);
        calculateDropMap();
        VoidMinerData.OrePoolTotalWeightPool.put(dimID, totalWeight);
    }

    private void calculateDropMap() {
        this.handleModDimDef(dimID);
        this.handleExtraDrops(dimID);
        this.calculateTotalWeight();
    }

    private void calculateTotalWeight() {
        this.totalWeight = 0.0f;
        this.dropmap.values()
            .forEach(f -> this.totalWeight += f);
    }

    private void handleExtraDrops(int id) {
        Optional.ofNullable(
            Reflector.getExtraDropsDimMap()
                .get(id))
            .ifPresent(e -> e.forEach(f -> this.addDrop(f.getKey(), f.getValue())));
    }

    private void handleModDimDef(int id) {
        // vanilla dims or TF
        if (id <= 1 && id >= -1 || id == 7) {
            this.getDropsVanillaVeins(this.makeOreLayerPredicate());
            this.getDropsVanillaSmallOres(this.makeSmallOresPredicate());

            // ross dims
        } else if (id == ConfigHandler.ross128BID || id == ConfigHandler.ross128BAID) {
            this.getDropMapRoss(id);

            // other space dims
        } else {
            Optional.ofNullable(this.makeModDimDef())
                .ifPresent(def -> {
                    // normal space dim
                    this.getDropsOreVeinsSpace(def);
                    this.getDropsSmallOreSpace(def);

                    // BW space dim
                    Consumer<BW_OreLayer> addToList = this.makeAddToList();
                    this.addOresVeinsBartworks(def, addToList);
                    this.addSmallOresBartworks(def);
                });
        }
    }

    private void addSmallOresBartworks(ModDimensionDef finalDef) {
        try {
            GalacticGreg.smallOreWorldgenList.stream()
                .filter(
                    gt_worldgen -> gt_worldgen.mEnabled
                        && gt_worldgen instanceof BW_Worldgen_Ore_SmallOre_Space smallOreSpace
                        && smallOreSpace.isEnabledForDim(finalDef))
                .map(gt_worldgen -> (BW_Worldgen_Ore_SmallOre_Space) gt_worldgen)
                .forEach(
                    element -> this.addDrop(new Pair<>(element.mPrimaryMeta, element.bwOres != 0), element.mDensity));
        } catch (NullPointerException ignored) {}
    }

    private void addOresVeinsBartworks(ModDimensionDef finalDef, Consumer<BW_OreLayer> addToList) {
        try {
            GalacticGreg.oreVeinWorldgenList.stream()
                .filter(
                    gt_worldgen -> gt_worldgen.mEnabled
                        && gt_worldgen instanceof BW_Worldgen_Ore_Layer_Space oreLayerSpace
                        && oreLayerSpace.isEnabledForDim(finalDef))
                .map(gt_worldgen -> (BW_Worldgen_Ore_Layer_Space) gt_worldgen)
                .forEach(addToList);
        } catch (NullPointerException ignored) {}
    }

    private void getDropsSmallOreSpace(ModDimensionDef finalDef) {
        GalacticGreg.smallOreWorldgenList.stream()
            .filter(
                gt_worldgen -> gt_worldgen.mEnabled
                    && gt_worldgen instanceof GT_Worldgen_GT_Ore_SmallPieces_Space oreSmallPiecesSpace
                    && oreSmallPiecesSpace.isEnabledForDim(finalDef))
            .map(gt_worldgen -> (GT_Worldgen_GT_Ore_SmallPieces_Space) gt_worldgen)
            .forEach(element -> this.addDrop(new Pair<>((int) element.mMeta, false), element.mAmount));
    }

    private void getDropsOreVeinsSpace(ModDimensionDef finalDef) {
        GalacticGreg.oreVeinWorldgenList.stream()
            .filter(
                gt_worldgen -> gt_worldgen.mEnabled
                    && gt_worldgen instanceof GT_Worldgen_GT_Ore_Layer_Space oreLayerSpace
                    && oreLayerSpace.isEnabledForDim(finalDef))
            .map(gt_worldgen -> (GT_Worldgen_GT_Ore_Layer_Space) gt_worldgen)
            .forEach(element -> {
                this.addDrop(new Pair<>((int) element.mPrimaryMeta, false), element.mWeight);
                this.addDrop(new Pair<>((int) element.mSecondaryMeta, false), element.mWeight);
                this.addDrop(new Pair<>((int) element.mSporadicMeta, false), element.mWeight / 8f);
                this.addDrop(new Pair<>((int) element.mBetweenMeta, false), element.mWeight / 8f);
            });
    }

    private ModDimensionDef makeModDimDef() {
        return getModContainers().stream()
            .flatMap(
                modContainer -> modContainer.getDimensionList()
                    .stream())
            .filter(
                modDimensionDef -> modDimensionDef.getChunkProviderName()
                    .equals(
                        ((ChunkProviderServer) this.world.getChunkProvider()).currentChunkProvider.getClass()
                            .getName()))
            .findFirst()
            .orElse(null);
    }

    private void getDropMapRoss(int aID) {
        Consumer<BW_OreLayer> addToList = this.makeAddToList();
        BW_OreLayer.sList.stream()
            .filter(
                gt_worldgen -> gt_worldgen.mEnabled && gt_worldgen instanceof BW_OreLayer
                    && gt_worldgen.isGenerationAllowed((World) null, aID, 0))
            .forEach(addToList);
    }

    private Consumer<BW_OreLayer> makeAddToList() {
        return element -> {
            List<Pair<Integer, Boolean>> data = element.getStacksRawData();
            for (int i = 0; i < data.size(); i++) {
                if (i < data.size() - 2) this.addDrop(data.get(i), element.mWeight);
                else this.addDrop(data.get(i), element.mWeight / 8f);
            }
        };
    }

    private Predicate<GT_Worldgen_GT_Ore_SmallPieces> makeSmallOresPredicate() {
        World world = this.world;
        return switch (world.provider.dimensionId) {
            case -1 -> gt_worldgen -> gt_worldgen.mNether;
            case 0 -> gt_worldgen -> gt_worldgen.mOverworld;
            case 1 -> gt_worldgen -> gt_worldgen.mEnd;
            /*
             * explicitely giving different dim numbers so it default to false in the config, keeping compat with the
             * current worldgen config
             */
            case 7 -> gt_worldgen -> gt_worldgen.isGenerationAllowed(world, 0, 7);
            default -> throw new IllegalStateException();
        };
    }

    private void getDropsVanillaSmallOres(Predicate<GT_Worldgen_GT_Ore_SmallPieces> smallOresPredicate) {
        GT_Worldgen_GT_Ore_SmallPieces.sList.stream()
            .filter(gt_worldgen -> gt_worldgen.mEnabled && smallOresPredicate.test(gt_worldgen))
            .forEach(element -> this.addDrop(new Pair<>((int) element.mMeta, false), element.mAmount));
    }

    private void getDropsVanillaVeins(Predicate<GT_Worldgen_GT_Ore_Layer> oreLayerPredicate) {
        GT_Worldgen_GT_Ore_Layer.sList.stream()
            .filter(gt_worldgen -> gt_worldgen.mEnabled && oreLayerPredicate.test(gt_worldgen))
            .forEach(element -> {
                this.addDrop(new Pair<>((int) element.mPrimaryMeta, false), element.mWeight);
                this.addDrop(new Pair<>((int) element.mSecondaryMeta, false), element.mWeight);
                this.addDrop(new Pair<>((int) element.mSporadicMeta, false), element.mWeight / 8f);
                this.addDrop(new Pair<>((int) element.mBetweenMeta, false), element.mWeight / 8f);
            });
    }

    private Predicate<GT_Worldgen_GT_Ore_Layer> makeOreLayerPredicate() {
        World world = this.world;
        return switch (world.provider.dimensionId) {
            case -1 -> gt_worldgen -> gt_worldgen.mNether;
            case 0 -> gt_worldgen -> gt_worldgen.mOverworld;
            case 1 -> gt_worldgen -> gt_worldgen.mEnd || gt_worldgen.mEndAsteroid;
            /*
             * explicitely giving different dim numbers so it default to false in the config, keeping compat with the
             * current worldgen config
             */
            case 7 -> gt_worldgen -> gt_worldgen.isGenerationAllowed(world, 0, 7);
            default -> throw new IllegalStateException();
        };
    }

    private void addDrop(Pair<Integer, Boolean> key, float value) {
        final ItemStack ore = this.getOreItemStack(key);
        if (ConfigHandler.voidMinerBlacklist.contains(
            String.format(
                "%s:%d",
                GameRegistry.findUniqueIdentifierFor(ore.getItem())
                    .toString(),
                ore.getItemDamage())))
            return;
        if (!this.dropmap.containsKey(key)) this.dropmap.put(key, value);
        else this.dropmap.put(key, this.dropmap.get(key) + value);
    }

    private ItemStack getOreItemStack(Pair<Integer, Boolean> stats) {
        return new ItemStack(stats.getValue() ? WerkstoffLoader.BWOres : GregTech_API.sBlockOres1, 1, stats.getKey());
    }
}
