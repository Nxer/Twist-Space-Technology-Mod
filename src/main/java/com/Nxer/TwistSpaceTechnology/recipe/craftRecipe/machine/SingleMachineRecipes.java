package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import appeng.block.crafting.BlockCraftingUnit;
import appeng.items.materials.MaterialType;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler;
import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import appeng.core.api.definitions.ApiBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tectech.recipe.TTRecipeAdder;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.dreammaster.gthandler.CustomItemList.PicoWafer;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.util.GTModHandler.getModItem;

public class SingleMachineRecipes implements IRecipePool {
    // spotless:off
    @Override
    public void loadRecipes() {
        if(Config.Enable_HighEnergyStateThermalTransferDevice) {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemRefer.Extreme_Heat_Exchanger.get(1),
                128_000,
                64,
                800_000,
                4,
                new Object[]{
                    ItemRefer.Extreme_Heat_Exchanger.get(64),
                    ItemRefer.Extreme_Heat_Exchanger.get(64),
                    ItemRefer.Extreme_Heat_Exchanger.get(64),
                    ItemRefer.Extreme_Heat_Exchanger.get(64),

                    GregtechItemList.XL_HeatExchanger.get(64),
                    GregtechItemList.XL_HeatExchanger.get(64),
                    GregtechItemList.XL_HeatExchanger.get(64),
                    GregtechItemList.XL_HeatExchanger.get(64),

                    getModItem(AppliedEnergistics2.ID, "tile.BlockCraftingUnit", 64,2),
                    ModItemHandler.OpenComputers.CentralProcessingUnit_T3.get(64),
                    new Object[]{OrePrefixes.circuit.get(Materials.ZPM), 64},
                    ItemList.Electric_Pump_UV.get(64),

                    ItemList.Reactor_Coolant_Sp_6.get(1),
                    ItemList.Reactor_Coolant_Sp_6.get(1),
                    ItemList.Reactor_Coolant_Sp_6.get(1),
                    ItemList.Reactor_Coolant_Sp_6.get(1)
                },
                new FluidStack[]{
                    MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 32),
                    MaterialsAlloy.HASTELLOY_C276.getFluidStack(144 * 256),
                    Materials.CosmicNeutronium.getMolten(144 * 8),
                    Materials.SuperCoolant.getFluid(1000 * 128)
                },
                GTCMItemList.HighEnergyStateThermalTransferDevice.get(1),
                20 * 80,
                (int) RECIPE_UV
            );
        }
    }
    // spotless:on
}
