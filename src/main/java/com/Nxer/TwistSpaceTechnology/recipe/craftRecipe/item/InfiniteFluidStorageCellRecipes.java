package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.init.TstItems;
import com.glodblock.github.loader.ItemAndBlockHolder;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class InfiniteFluidStorageCellRecipes {

    // spotless:off
    private static final String[] CELL_LAYOUT = {
        "--ebcbe--",
        "-dalflad-",
        "egllhllge",
        "illljllli",
        "cfhjkjhfc",
        "illljllli",
        "egllhllge",
        "-dalflad-",
        "--ebcbe--"
    };

    private static final String[] AIR_CELL_LAYOUT = {
        "--ebcbe--",
        "-dalflad-",
        "egllhllge",
        "illljllli",
        "cfhmkmhfc",
        "illljllli",
        "egllhllge",
        "-dalflad-",
        "--ebcbe--"
    };
    // spotless:on

    public static void loadRecipes() {
        Item placeholder = GTCMItemList.TestItem0.getItem();
        ItemStack infinityCatalyst = getModItem(Mods.Avaritia.ID, "Resource", 1, 5);
        if (infinityCatalyst.getItem() == placeholder) return;

        ExtremeCraftingManager crafting = ExtremeCraftingManager.getInstance();
        ItemStack advancedHousing = new ItemStack(ItemAndBlockHolder.CELL_HOUSING, 1, 1);

        // spotless:off
        crafting.addExtremeShapedOreRecipe(
            new ItemStack(TstItems.InfinityDistilledWaterStorageCell),
            CELL_LAYOUT,
            'a', "plateSuperdenseLaurenium",
            'b', "plateSuperdenseAstralTitanium",
            'c', "plateSuperdenseVividAlloy",
            'd', "plateSuperdenseThulium",
            'e', "blockCosmicNeutronium",
            'f', ItemList.Field_Generator_UV.get(1),
            'g', "plateSuperdenseAdvancedNitinol",
            'h', infinityCatalyst,
            'i', "plateSuperdenseMagnalium",
            'j', GregtechItemList.Hatch_Reservoir.get(1),
            'k', advancedHousing,
            'l', GTCMItemList.DistilledWaterSingularity.get(1));

        crafting.addExtremeShapedOreRecipe(
            new ItemStack(TstItems.InfinityAirStorageCell),
            AIR_CELL_LAYOUT,
            'a', "plateSuperdenseNeutronium",
            'b', "plateSuperdenseRhodium-PlatedPalladium",
            'c', "plateSuperdenseIridium",
            'd', "plateSuperdenseTrinium",
            'e', "blockCosmicNeutronium",
            'f', ItemList.Field_Generator_UEV.get(1),
            'g', "plateSuperdenseQuantum",
            'h', infinityCatalyst,
            'i', "plateSuperdenseAntimony",
            'j', GTCMItemList.InfiniteAirHatch.get(1),
            'k', advancedHousing,
            'l', GTCMItemList.AirSingularity.get(1),
            'm', GregtechItemList.Hatch_Air_Intake_Atmospheric.get(1));

        if (GTCMItemList.BloodOrbHatch.hasBeenSet() && Mods.BloodMagic.isModLoaded()
            && FluidRegistry.getFluid("lifeessence") != null) {
            ItemStack armokOrb = getModItem(Mods.Avaritia.ID, "Orb_Armok", 1);
            if (armokOrb.getItem() == placeholder) return;

            crafting.addExtremeShapedOreRecipe(
                new ItemStack(TstItems.InfinityLifeEssenceStorageCell),
                CELL_LAYOUT,
                'a', "plateSuperdenseRubracium",
                'b', "plateSuperdenseTritanium",
                'c', "plateSuperdenseMAR-Ce-M200Steel",
                'd', "plateSuperdensePlutonium",
                'e', "blockCosmicNeutronium",
                'f', ItemList.Field_Generator_UHV.get(1),
                'g', "plateSuperdenseDragonblood",
                'h', infinityCatalyst,
                'i', "plateSuperdenseRubidium",
                'j', GTCMItemList.BloodOrbHatch.get(1),
                'k', armokOrb,
                'l', GTCMItemList.LifeEssenceSingularity.get(1));
        }
        // spotless:on
    }
}
