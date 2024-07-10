package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.machines;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.PowerConsumptionController;
import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedHatchElement.SpeedController;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks;
import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults;
import com.Nxer.TwistSpaceTechnology.common.misc.MachineShutDownReasons.SimpleShutDownReasons;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularHatchTypes;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineSupportAllModuleBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.items.MyMaterial;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_CraftingInput_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Input_ME;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gtPlusPlus.core.material.ELEMENT;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TST_StrangeMatterAggregator extends ModularizedMachineSupportAllModuleBase<TST_StrangeMatterAggregator> {

    // region Class Constructor
    public TST_StrangeMatterAggregator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_StrangeMatterAggregator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_StrangeMatterAggregator(this.mName);
    }

    // endregion

    // region Logic

    // region Statics

    /**
     * <p>
     * 0. Molten SpaceTime 576L
     * <p>
     * 1. Molten Universium 96L
     * <p>
     * 2. Megneto Constrained Star Matter 16L
     */
    protected static Fluid[] SpaceTimeMaintenanceConsumablesFluids;

    /**
     * Byproduct fluids when continuous operation. Selected by SpaceTimeMaintenanceConsumablesFluids tier.
     * <p>
     * 1. Molten Infinity 144L and molten Hypogen 144L
     * <p>
     * 2. Molten SpaceTime 144L and molten Shirabon 144L
     * <p>
     * 2. Molten Universium 144L
     */
    protected static Fluid[][] ByproductFluids;

    protected static TST_ItemID CoreElement;
    protected static TST_ItemID AnnihilationConstrainer;
    protected static TST_ItemID AntiMatter;
    protected static TST_ItemID Tesseract;
    protected static TST_ItemID StellarFrame;
    protected static Fluid HydrogenPlasma;

    public static void initStatics() {
        // spotless:off
        SpaceTimeMaintenanceConsumablesFluids = new Fluid[] {
            MaterialsUEVplus.SpaceTime.getMolten(576).getFluid(),
            MaterialsUEVplus.Universium.getMolten(96).getFluid(),
            MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(16) .getFluid()
        };

        ByproductFluids = new Fluid[][] {
            new Fluid[] { Materials.Infinity.getMolten(1).getFluid(), ELEMENT.STANDALONE.HYPOGEN.getFluid() },
            new Fluid[] { MaterialsUEVplus.SpaceTime.getMolten(1).getFluid(), MyMaterial.shirabon.getMolten(1).getFluid() },
            new Fluid[] { MaterialsUEVplus.Universium.getMolten(1).getFluid() }
        };

        CoreElement = TST_ItemID.create(GTCMItemList.CoreElement.get(1));
        AnnihilationConstrainer = TST_ItemID.create(GTCMItemList.AnnihilationConstrainer.get(1));
        AntiMatter = TST_ItemID.create(GTCMItemList.Antimatter.get(1));
        Tesseract = TST_ItemID.create(ItemList.Tesseract.get(1));
        StellarFrame = TST_ItemID.create(GTCMItemList.StellarConstructionFrameMaterial.get(1));

        HydrogenPlasma = Materials.Hydrogen.getPlasma(1).getFluid();
        // spotless:on
    }

    // endregion

    // region Owner
    protected UUID ownerUUID;
    // endregion

    // region Extra Hatches
    protected GT_MetaTileEntity_Hatch_Input SpaceTimeMaintenanceConsumablesInputHatch;
    protected GT_MetaTileEntity_Hatch_InputBus CoreElementInputBus;

    // endregion

    // region UI settings

    // about structure
    protected int oscillatorPieceNeed = 1;
    protected int constraintorPieceNeed = 1;
    protected int mergerPieceNeed = 1;

    // about running
    protected int spaceTimeMaintenanceFluidTier = 1;

    // by UI settings
    /**
     * Current SpaceTime Maintenance Fluid, set by UI
     */
    protected Fluid spaceTimeMaintenanceFluidInUse;

    /**
     * Maintenance Fluid consumption amount per run
     */
    protected int maintenanceFluidConsumption = Config.MaintenanceFluidConsumption_T1SpaceTime_StrangeMatterAggregator;

    /**
     * Byproduct fluids output base amount set by structure and maintenance fluid tier.
     * <p>
     * Actual output amount is this * consecutivePoint
     */
    protected int byproductOutputBaseAmount = 0;

    // endregion

    // region Structure Parameters
    protected int oscillatorTier = 0;
    protected int oscillatorPiece = 0;
    protected int constraintorTier = 0;
    protected int constraintorPiece = 0;
    protected int mergerTier = 0;
    protected int mergerPiece = 0;
    protected int rings = 0;
    protected boolean wirelessMode = false;

    /**
     * Maximum number of consecutive running points
     */
    protected int maxConsecutivePoint = Config.MaxConsecutivePoint_T1SpaceTimeMerger_StrangeMatterAggregator;

    /**
     * The auxiliary materials are like StellarConstructionFrameMaterial, Tesseract,
     */
    protected double auxiliaryMaterialConsumptionRate = 1d;

    /**
     * Basic recipe time cost
     */
    protected int recipeTime = Config.RecipeTime_T1SpaceTimeOscillator_StrangeMatterAggregator;

    /**
     * Basic EU/t of running
     */
    protected long powerConsumption = Config.PowerConsume_StrangeMatterAggregator;

    /**
     * Total EU consumption in every progress, used in wireless mode.
     */
    protected long totalPowerConsumption = powerConsumption * recipeTime;

    /**
     * Basic amount of input materials, for example inputFactor = 256 then input 256 anti-matter and hydrogen plasma can
     * make one recipe
     */
    protected double inputFactor = Config.BaseInputValue_StrangeMatterAggregator;

    // endregion

    // region Parameters set by SpaceTime Maintenance fluid tier
    /**
     * How many fuel rods one Annihilation Constrainer or one Core Element can produce.
     */
    protected int constrainerFactor = Config.BasicRodAmountPerConstrainerProduce_SpaceTime_StrangeMatterAggregator;

    /**
     * Fluid's multiplier of amount about inputFactor, for example inputFactor = 10, fluidConsumptionFactor = 1000 then
     * fluid amount = 10 * 1000
     */
    protected int fluidConsumptionFactor = Config.FluidInputMultiply_SpaceTimeMaintenance_StrangeMatterAggregator;

    // endregion

    // region Running Parameters
    /**
     * Current consecutive running point
     */
    protected int consecutivePoint = 0;

    // endregion

    // endregion

    protected void calculateParametersWithStructure() {

        // set wireless mode
        wirelessMode = mEnergyHatches.isEmpty() && mExoticEnergyHatches.isEmpty();

        // set max consecutive multiplier limit by space time merger tier
        maxConsecutivePoint = switch (mergerTier) {
            case 2 -> Config.MaxConsecutivePoint_T2SpaceTimeMerger_StrangeMatterAggregator;
            case 3 -> Config.MaxConsecutivePoint_T3SpaceTimeMerger_StrangeMatterAggregator;
            default -> Config.MaxConsecutivePoint_T1SpaceTimeMerger_StrangeMatterAggregator;
        };

        // set auxiliary material consumption rate by constraintor tier
        auxiliaryMaterialConsumptionRate = switch (constraintorTier) {
            case 2 -> Config.AuxiliaryMaterialConsumptionRate_T2SpaceTimeConstraintor_StrangeMatterAggregator;
            case 3 -> Config.AuxiliaryMaterialConsumptionRate_T3SpaceTimeConstraintor_StrangeMatterAggregator;
            default -> Config.AuxiliaryMaterialConsumptionRate_T1SpaceTimeConstraintor_StrangeMatterAggregator;
        };

        // set recipe time by oscillator tier and constraintor tier
        recipeTime = constraintorTier * switch (oscillatorTier) {
            case 2 -> Config.RecipeTime_T2SpaceTimeOscillator_StrangeMatterAggregator;
            case 3 -> Config.RecipeTime_T3SpaceTimeOscillator_StrangeMatterAggregator;
            default -> Config.RecipeTime_T1SpaceTimeOscillator_StrangeMatterAggregator;
        };

        // set input factor by oscillator piece
        inputFactor = 1d + (Config.BaseInputValue_StrangeMatterAggregator - 1) / Math.pow(oscillatorPiece, 0.5);

        // set power consumption parameter by merger tier and piece
        powerConsumption = (long) (mergerTier * Math.pow(mergerPiece, 0.7d)
            * Config.PowerConsume_StrangeMatterAggregator);
        if (wirelessMode) {
            totalPowerConsumption = powerConsumption * recipeTime;
        }

        calculateParametersWithSettings();
    }

    protected void calculateParametersWithSettings() {
        if (1 > spaceTimeMaintenanceFluidTier || 3 < spaceTimeMaintenanceFluidTier) {
            spaceTimeMaintenanceFluidTier = 1;
        }

        final int tierIndex = spaceTimeMaintenanceFluidTier - 1;

        spaceTimeMaintenanceFluidInUse = SpaceTimeMaintenanceConsumablesFluids[tierIndex];
        // maintenanceFluidConsumption = base * (oscillatorTier * oscillatorPiece)^0.8 / constraintorPiece
        final int base = switch (spaceTimeMaintenanceFluidTier) {
            case 2 -> Config.MaintenanceFluidConsumption_T2Universium_StrangeMatterAggregator;
            case 3 -> Config.MaintenanceFluidConsumption_T3MagnetoConstrainedStarMatter_StrangeMatterAggregator;
            default -> Config.MaintenanceFluidConsumption_T1SpaceTime_StrangeMatterAggregator;
        };
        maintenanceFluidConsumption = (int) Math
            .max(1d, base * Math.pow(oscillatorTier * oscillatorPiece, 0.8d) / constraintorPiece);

        constrainerFactor = oscillatorTier * oscillatorPiece
            * constraintorTier
            * constraintorPiece
            * mergerTier
            * mergerPiece
            * switch (spaceTimeMaintenanceFluidTier) {
            case 2 -> Config.BasicRodAmountPerConstrainerProduce_Universium_StrangeMatterAggregator;
            case 3 -> Config.BasicRodAmountPerConstrainerProduce_MagnetoConstrainedStarMatter_StrangeMatterAggregator;
            default -> Config.BasicRodAmountPerConstrainerProduce_SpaceTime_StrangeMatterAggregator;
            };

        fluidConsumptionFactor = switch (spaceTimeMaintenanceFluidTier) {
            case 2 -> Config.FluidInputMultiply_UniversiumMaintenance_StrangeMatterAggregator;
            case 3 -> Config.FluidInputMultiply_MagnetoConstrainedStarMatterMaintenance_StrangeMatterAggregator;
            default -> Config.FluidInputMultiply_SpaceTimeMaintenance_StrangeMatterAggregator;
        };

        // set byproduct output base amount by merger piece and space-time maintenance fluid tier
        byproductOutputBaseAmount = mergerPiece * switch (spaceTimeMaintenanceFluidTier) {
            case 2 -> Config.ByproductBaseAmount_T2_StrangeMatterAggregator;
            case 3 -> Config.ByproductBaseAmount_T3_StrangeMatterAggregator;
            default -> Config.ByproductBaseAmount_T1_StrangeMatterAggregator;
        };

    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isServerSide()) {
            this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (consecutivePoint != 0 && !aBaseMetaTileEntity.isActive()) {
                consecutivePoint = 0;
            }
        }
    }

    protected CheckRecipeResult setRunning() {
        if (wirelessMode) {
            lEUt = 0;
            if (!addEUToGlobalEnergyMap(ownerUUID, (long) (getEuModifier() * totalPowerConsumption))) {
                return CheckRecipeResultRegistry.insufficientPower((long) (getEuModifier() * totalPowerConsumption));
            }
        } else {
            lEUt = -(long) (getEuModifier() * powerConsumption);
        }
        mMaxProgresstime = (int) Math.max(1, recipeTime * getSpeedBonus());
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public CheckRecipeResult checkProcessingMM() {
        // try consume space-time maintenance fluid
        if (!consumeSpaceTimeMaintenanceFluid()) {
            consecutivePoint = 0;
            stopMachine(SimpleShutDownReasons.NoSpaceTimeMaintenanceFluidInput);
            return CheckRecipeResults.NoSpaceTimeMaintenanceFluidInput;
        }

        // set continue running
        // set power and progressing time which means process succeed to start
        CheckRecipeResult setPower = setRunning();
        if (!setPower.wasSuccessful()) {
            consecutivePoint = 0;
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            return setPower;
        }

        // check Core Element and Annihilation Constrainer inputs
        Pair<List<ItemStack>, List<ItemStack>> coreElementsAndAnnihilationConstrainers = getCoreElementAndAnnihilationConstrainerStacks();
        List<ItemStack> annihilationConstrainerStacks = coreElementsAndAnnihilationConstrainers.getRight();
        if (annihilationConstrainerStacks.isEmpty()) {
            return CheckRecipeResults.NoAnnihilationConstrainerInput;
        }

        // check normal inputs
        List<ItemStack> allInputItems = getStoredInputsNoSeparation();
        List<FluidStack> allInputFluids = getStoredFluidsWithDualInput();
        if (allInputItems.isEmpty() || allInputFluids.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // total annihilation constrainer amount
        long annihilationConstrainerAmount = 0;
        for (ItemStack i : annihilationConstrainerStacks) {
            annihilationConstrainerAmount += i.stackSize;
        }

        // total core element amount
        long coreElementAmount = 0;
        List<ItemStack> coreElementStacks = coreElementsAndAnnihilationConstrainers.getLeft();
        for (ItemStack i : coreElementStacks) {
            coreElementAmount += i.stackSize;
        }

        // iterate general fluid inputs
        List<FluidStack> hydrogenPlasma = new ArrayList<>();
        long hydrogenPlasmaAmount = 0;

        for (FluidStack f : allInputFluids) {
            if (null == f && 0 >= f.amount) continue;
            if (f.getFluid() == HydrogenPlasma) {
                hydrogenPlasma.add(f);
                hydrogenPlasmaAmount += f.amount;
            }
        }

        // check general fluid inputs
        double recipeHydrogenPlasma = inputFactor * fluidConsumptionFactor;
        if (hydrogenPlasmaAmount < recipeHydrogenPlasma) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // iterate general item inputs
        List<ItemStack> antiMatters = new ArrayList<>();
        long antiMatterAmount = 0;
        List<ItemStack> stellarFrames = new ArrayList<>();
        long stellarFramesAmount = 0;
        List<ItemStack> tesseracts = new ArrayList<>();
        long tesseractAmount = 0;

        for (ItemStack i : allInputItems) {
            if (null == i || 0 >= i.stackSize) continue;
            if (AntiMatter.equalItemStack(i)) {
                antiMatters.add(i);
                antiMatterAmount += i.stackSize;
            } else if (StellarFrame.equalItemStack(i)) {
                stellarFrames.add(i);
                stellarFramesAmount += i.stackSize;
            } else if (Tesseract.equalItemStack(i)) {
                tesseracts.add(i);
                tesseractAmount += i.stackSize;
            }
        }

        // check general item inputs
        if (antiMatterAmount < inputFactor || stellarFramesAmount < 1 || tesseractAmount < 1) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // how many anti matter rod can produce
        long antiMatterRodMaxOutput = annihilationConstrainerAmount * constrainerFactor;

        // how many strange rod can produce
        long strangeRodMaxOutput = coreElementAmount * constrainerFactor;

        // output rod amount of one recipe
        long rodAmountRecipe = 1 + consecutivePoint;

        // now everything is ready

        // check how many recipe can do limited by annihilation constrainer amount
        // parallel limit = (constrainer * rod can produce) / recipe rod output
        double parallel = (antiMatterRodMaxOutput / rodAmountRecipe);

        // check how many recipe can do limited by anti matter amount
        parallel = Math.min(parallel, antiMatterAmount / inputFactor);

        // check hydrogen plasma
        parallel = Math.min(parallel, hydrogenPlasmaAmount / recipeHydrogenPlasma);;

        // check tesseract
        parallel = Math.min(parallel, tesseractAmount / auxiliaryMaterialConsumptionRate);

        // check stellar frame can do how many rods
        long rodsMax = (long) (stellarFramesAmount / auxiliaryMaterialConsumptionRate);
        long tParallel = rodsMax / rodAmountRecipe;

        // final parallel to process
        parallel = Math.min(parallel, tParallel);

        // rod amount of output
        long outputRodAmount = (long) (parallel * rodAmountRecipe);

        // anti matter consumption calculated by parallel
        long consumeAntiMatter = Math.max(1, (long) (parallel * inputFactor));
        for (ItemStack i : antiMatters) {
            // if (null == i || i.stackSize <= 0) continue;
            if (i.stackSize >= consumeAntiMatter) {
                i.stackSize -= (int) consumeAntiMatter;
                break;
            } else {
                consumeAntiMatter -= i.stackSize;
                i.stackSize = 0;
            }
        }

        // annihilation constrainer consumption calculated by output amount
        long consumeAnnihilationConstrainer = (long) Math
            .max(1, Math.ceil((double) outputRodAmount / constrainerFactor));
        long annihilationConstrainerToConsume = consumeAnnihilationConstrainer;
        for (ItemStack i : annihilationConstrainerStacks) {
            // if (null == i || i.stackSize <= 0) continue;
            if (i.stackSize >= annihilationConstrainerToConsume) {
                i.stackSize -= (int) annihilationConstrainerToConsume;
                break;
            } else {
                annihilationConstrainerToConsume -= i.stackSize;
                i.stackSize = 0;
            }
        }

        // hydrogen plasma consumption calculated by parallel
        long consumeHydrogenPlasma = (long) (Math.max(1, parallel * recipeHydrogenPlasma));
        for (FluidStack f : hydrogenPlasma) {
            // if (null == f || f.amount <= 0) continue;
            if (f.amount >= consumeHydrogenPlasma) {
                f.amount -= (int) consumeHydrogenPlasma;
                break;
            } else {
                consumeHydrogenPlasma -= f.amount;
                f.amount = 0;
            }
        }

        // tesseract consumption calculated by parallel
        long consumeTesseract = (long) (Math.max(1, parallel * auxiliaryMaterialConsumptionRate));
        for (ItemStack i : tesseracts) {
            // if (null == i || i.stackSize <= 0) continue;
            if (i.stackSize >= consumeTesseract) {
                i.stackSize -= (int) consumeTesseract;
                break;
            } else {
                consumeTesseract -= i.stackSize;
                i.stackSize = 0;
            }
        }

        // stellar frame consumption calculated by output amount
        long consumeStellarFrame = (long) Math.max(1, auxiliaryMaterialConsumptionRate * outputRodAmount);
        for (ItemStack i : stellarFrames) {
            // if (null == i || i.stackSize <= 0) continue;
            if (i.stackSize >= consumeStellarFrame) {
                i.stackSize -= (int) consumeStellarFrame;
                break;
            } else {
                consumeStellarFrame -= i.stackSize;
                i.stackSize = 0;
            }
        }

        // output productions
        List<ItemStack> outputItems = new ArrayList<>();
        long byproductAmount = (long) byproductOutputBaseAmount * consecutivePoint;

        // check Core Element
        if (coreElementAmount > 0) {
            // byproduct * 2
            byproductAmount *= 2;

            // calculate Core Element
            if (coreElementAmount >= consumeAnnihilationConstrainer) {
                // all output is advanced production

                // consume Core Element items
                long toConsume = consumeAnnihilationConstrainer;
                for (ItemStack i : coreElementStacks) {
                    if (i.stackSize >= toConsume) {
                        i.stackSize -= (int) toConsume;
                        break;
                    } else {
                        toConsume -= i.stackSize;
                        i.stackSize = 0;
                    }
                }

                // output strange annihilation fuel rods
                if (outputRodAmount > Integer.MAX_VALUE) {
                    int stack = (int) (outputRodAmount / Integer.MAX_VALUE);
                    int remainder = (int) (outputRodAmount % Integer.MAX_VALUE);
                    for (int i = 0; i < stack; i++) {
                        outputItems.add(GTCMItemList.StrangeAnnihilationFuelRod.get(Integer.MAX_VALUE));
                    }
                    outputItems.add(GTCMItemList.StrangeAnnihilationFuelRod.get(remainder));
                } else {
                    outputItems.add(GTCMItemList.StrangeAnnihilationFuelRod.get((int) outputRodAmount));
                }

            } else {
                // a part of StrangeAnnihilationFuelRod, a part of AntimatterFuelRod
                long strangeRod = coreElementAmount * constrainerFactor;
                long antiMatterRod = outputRodAmount - strangeRod;

                // output strange annihilation fuel rods
                if (strangeRod > Integer.MAX_VALUE) {
                    int stack = (int) (strangeRod / Integer.MAX_VALUE);
                    int remainder = (int) (strangeRod % Integer.MAX_VALUE);
                    for (int i = 0; i < stack; i++) {
                        outputItems.add(GTCMItemList.StrangeAnnihilationFuelRod.get(Integer.MAX_VALUE));
                    }
                    outputItems.add(GTCMItemList.StrangeAnnihilationFuelRod.get(remainder));
                } else {
                    outputItems.add(GTCMItemList.StrangeAnnihilationFuelRod.get((int) outputRodAmount));
                }

                // output anti matter fuel rods
                if (antiMatterRod > Integer.MAX_VALUE) {
                    int stack = (int) (antiMatterRod / Integer.MAX_VALUE);
                    int remainder = (int) (antiMatterRod % Integer.MAX_VALUE);
                    for (int i = 0; i < stack; i++) {
                        outputItems.add(GTCMItemList.AntimatterFuelRod.get(Integer.MAX_VALUE));
                    }
                    outputItems.add(GTCMItemList.AntimatterFuelRod.get(remainder));
                } else {
                    outputItems.add(GTCMItemList.AntimatterFuelRod.get((int) outputRodAmount));
                }

            }

        } else {

            // output anti matter fuel rods
            if (outputRodAmount > Integer.MAX_VALUE) {
                int stack = (int) (outputRodAmount / Integer.MAX_VALUE);
                int remainder = (int) (outputRodAmount % Integer.MAX_VALUE);
                for (int i = 0; i < stack; i++) {
                    outputItems.add(GTCMItemList.AntimatterFuelRod.get(Integer.MAX_VALUE));
                }
                outputItems.add(GTCMItemList.AntimatterFuelRod.get(remainder));
            } else {
                outputItems.add(GTCMItemList.AntimatterFuelRod.get((int) outputRodAmount));
            }
        }

        List<FluidStack> outputFluids = new ArrayList<>();
        if (consecutivePoint > 0) {
            // output byproduct
            Fluid[] byproducts = ByproductFluids[spaceTimeMaintenanceFluidTier - 1];
            if (byproductAmount > Integer.MAX_VALUE) {
                int stack = (int) (byproductAmount / Integer.MAX_VALUE);
                int remainder = (int) (byproductAmount % Integer.MAX_VALUE);
                for (int i = 0; i < byproducts.length; i++) {
                    for (int j = 0; j < stack; j++) {
                        outputFluids.add(new FluidStack(byproducts[i], Integer.MAX_VALUE));
                    }
                    outputFluids.add(new FluidStack(byproducts[i], remainder));
                }
            } else {
                for (int i = 0; i < byproducts.length; i++) {
                    outputFluids.add(new FluidStack(byproducts[i], (int) byproductAmount));
                }
            }

        }

        // actually output
        if (!outputItems.isEmpty()) {
            mOutputItems = outputItems.toArray(new ItemStack[0]);
        }
        if (!outputFluids.isEmpty()) {
            mOutputFluids = outputFluids.toArray(new FluidStack[0]);
        }

        updateSlots();

        // handle consecutivePoint
        if (consecutivePoint < maxConsecutivePoint) {
            consecutivePoint++;
        } else {
            consecutivePoint = maxConsecutivePoint;
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.StrangeMatterAggregatorRecipes;
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        if (null != SpaceTimeMaintenanceConsumablesInputHatch && SpaceTimeMaintenanceConsumablesInputHatch.isValid())
            SpaceTimeMaintenanceConsumablesInputHatch.updateSlots();
        if (null != CoreElementInputBus && CoreElementInputBus.isValid()) CoreElementInputBus.updateSlots();
    }

    @Override
    protected void startRecipeProcessing() {
        super.startRecipeProcessing();
        if (null != SpaceTimeMaintenanceConsumablesInputHatch && SpaceTimeMaintenanceConsumablesInputHatch.isValid()) {
            if (SpaceTimeMaintenanceConsumablesInputHatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
        if (null != CoreElementInputBus && CoreElementInputBus.isValid()) {
            if (CoreElementInputBus instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
    }

    @Override
    protected void endRecipeProcessing() {
        super.endRecipeProcessing();
        if (null != SpaceTimeMaintenanceConsumablesInputHatch && SpaceTimeMaintenanceConsumablesInputHatch.isValid()) {
            if (SpaceTimeMaintenanceConsumablesInputHatch instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
        if (null != CoreElementInputBus && CoreElementInputBus.isValid()) {
            if (CoreElementInputBus instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
    }

    /**
     * Get Core Element and Annihilation Constrainer items from the Left input bus.
     *
     * @return Pair left is the List of Core Element, right is Annihilation Constrainer. If items get failed, return
     *         empty list.
     */
    protected Pair<List<ItemStack>, List<ItemStack>> getCoreElementAndAnnihilationConstrainerStacks() {
        if (null == CoreElementInputBus || !CoreElementInputBus.isValid()
            || CoreElementInputBus instanceof GT_MetaTileEntity_Hatch_CraftingInput_ME)
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        List<ItemStack> coreElements = new ArrayList<>();
        List<ItemStack> annihilationConstrainers = new ArrayList<>();

        IGregTechTileEntity te = CoreElementInputBus.getBaseMetaTileEntity();
        for (int i = te.getSizeInventory() - 1; i >= 0; i--) {
            ItemStack itemStack = te.getStackInSlot(i);
            if (itemStack != null && itemStack.stackSize > 0) {
                if (CoreElement.equalItemStack(itemStack)) {
                    coreElements.add(itemStack);
                } else if (AnnihilationConstrainer.equalItemStack(itemStack)) {
                    annihilationConstrainers.add(itemStack);
                }
            }
        }

        return Pair.of(
            coreElements.isEmpty() ? Collections.emptyList() : coreElements,
            annihilationConstrainers.isEmpty() ? Collections.emptyList() : annihilationConstrainers);
    }

    protected boolean consumeSpaceTimeMaintenanceFluid() {
        if (null == SpaceTimeMaintenanceConsumablesInputHatch || !SpaceTimeMaintenanceConsumablesInputHatch.isValid()) {
            return false;
        }
        if (SpaceTimeMaintenanceConsumablesInputHatch instanceof GT_MetaTileEntity_Hatch_MultiInput m) {
            // multi fluid input hatch
            ArrayList<FluidStack> matchedFluidStacks = new ArrayList<>();
            int matchedAmount = 0;
            for (FluidStack f : m.getStoredFluid()) {
                if (f.getFluid() == spaceTimeMaintenanceFluidInUse) {
                    matchedFluidStacks.add(f);
                    matchedAmount += f.amount;
                    if (matchedAmount >= maintenanceFluidConsumption) break;
                }
            }
            if (matchedAmount >= maintenanceFluidConsumption) {
                int toConsume = maintenanceFluidConsumption;
                for (FluidStack f : matchedFluidStacks) {
                    if (f.amount >= toConsume) {
                        f.amount -= toConsume;
                        return true;
                    } else {
                        toConsume -= f.amount;
                        f.amount = 0;
                    }
                }
            }
            return false;

        } else if (SpaceTimeMaintenanceConsumablesInputHatch instanceof GT_MetaTileEntity_Hatch_Input_ME me) {
            // me input hatch
            for (FluidStack f : me.getStoredFluids()) {
                if (null != f && f.getFluid() == spaceTimeMaintenanceFluidInUse) {
                    if (f.amount >= maintenanceFluidConsumption) {
                        f.amount -= maintenanceFluidConsumption;
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            return false;
        } else {
            // normal input hatch
            FluidStack f = SpaceTimeMaintenanceConsumablesInputHatch.getFillableStack();
            if (null != f && f.getFluid() == spaceTimeMaintenanceFluidInUse
                && f.amount >= maintenanceFluidConsumption) {
                f.amount -= maintenanceFluidConsumption;
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        // UI Structure
        aNBT.setInteger("oscillatorPieceNeed", oscillatorPieceNeed);
        aNBT.setInteger("constraintorPieceNeed", constraintorPieceNeed);
        aNBT.setInteger("mergerPieceNeed", mergerPieceNeed);

        // UI Running
        aNBT.setInteger("spaceTimeMaintenanceFluidTier", spaceTimeMaintenanceFluidTier);

        // Structure
        aNBT.setInteger("oscillatorTier", oscillatorTier);
        aNBT.setInteger("oscillatorPiece", oscillatorPiece);
        aNBT.setInteger("constraintorTier", constraintorTier);
        aNBT.setInteger("constraintorPiece", constraintorPiece);
        aNBT.setInteger("mergerTier", mergerTier);
        aNBT.setInteger("mergerPiece", mergerPiece);
        aNBT.setInteger("rings", rings);
        aNBT.setBoolean("wirelessMode", wirelessMode);

        // Running
        aNBT.setInteger("consecutivePoint", consecutivePoint);

    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        // UI Structure
        oscillatorPieceNeed = aNBT.getInteger("oscillatorPieceNeed");
        constraintorPieceNeed = aNBT.getInteger("constraintorPieceNeed");
        mergerPieceNeed = aNBT.getInteger("mergerPieceNeed");

        // UI Running
        spaceTimeMaintenanceFluidTier = aNBT.getInteger("spaceTimeMaintenanceFluidTier");

        // Structure
        oscillatorTier = aNBT.getInteger("oscillatorTier");
        oscillatorPiece = aNBT.getInteger("oscillatorPiece");
        constraintorTier = aNBT.getInteger("constraintorTier");
        constraintorPiece = aNBT.getInteger("constraintorPiece");
        mergerTier = aNBT.getInteger("mergerTier");
        mergerPiece = aNBT.getInteger("mergerPiece");
        rings = aNBT.getInteger("rings");
        wirelessMode = aNBT.getBoolean("wirelessMode");

        // Running
        consecutivePoint = aNBT.getInteger("consecutivePoint");

        flushBuildingRingPieceArray();
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("isActive")) {
            currentTip.add(
                "" + EnumChatFormatting.WHITE
                    + EnumChatFormatting.BOLD
                    + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.consecutivePoint")
                    + EnumChatFormatting.GRAY
                    + " : "
                    + EnumChatFormatting.YELLOW
                    + tag.getInteger("consecutivePoint"));
            currentTip.add(
                "" + EnumChatFormatting.AQUA
                    + TextEnums.CurrentPowerConsumption
                    + EnumChatFormatting.GRAY
                    + " : "
                    + EnumChatFormatting.WHITE
                    + GT_Utility.formatNumbers(tag.getLong("powerConsumption"))
                    + " EU/t");
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            if (tileEntity.isActive()) {
                tag.setInteger("consecutivePoint", consecutivePoint);
                tag.setLong("powerConsumption", (long) (powerConsumption * getEuModifier()));
            }
        }
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 8];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA
            // #tr StrangeMatterAggregator.MachineInfoData.oscillatorTier
            // # SpaceTime Oscillator {\RED}Tier
            // #zh_CN 时空振荡器{\RED}等级
            + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.oscillatorTier")
            + EnumChatFormatting.WHITE
            + ": "
            + EnumChatFormatting.GOLD
            + oscillatorTier;
        ret[origin.length + 1] = EnumChatFormatting.AQUA
            // #tr StrangeMatterAggregator.MachineInfoData.oscillatorPiece
            // # SpaceTime Oscillator {\BLUE}Rings
            // #zh_CN 时空振荡器{\BLUE}环数
            + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.oscillatorPiece")
            + EnumChatFormatting.WHITE
            + ": "
            + EnumChatFormatting.GOLD
            + oscillatorPiece;
        ret[origin.length + 2] = EnumChatFormatting.AQUA
            // #tr StrangeMatterAggregator.MachineInfoData.constraintorTier
            // # SpaceTime Constraintor {\RED}Tier
            // #zh_CN 时空约束器{\RED}等级
            + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.constraintorTier")
            + EnumChatFormatting.WHITE
            + ": "
            + EnumChatFormatting.GOLD
            + constraintorTier;
        ret[origin.length + 3] = EnumChatFormatting.AQUA
            // #tr StrangeMatterAggregator.MachineInfoData.constraintorPiece
            // # SpaceTIme Constraintor {\BLUE}Rings
            // #zh_CN 时空约束器{\BLUE}环数
            + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.constraintorPiece")
            + EnumChatFormatting.WHITE
            + ": "
            + EnumChatFormatting.GOLD
            + constraintorPiece;
        ret[origin.length + 4] = EnumChatFormatting.AQUA
            // #tr StrangeMatterAggregator.MachineInfoData.mergerTier
            // # SpaceTime Merger {\RED}Tier
            // #zh_CN 时空归并器{\RED}等级
            + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.mergerTier")
            + EnumChatFormatting.WHITE
            + ": "
            + EnumChatFormatting.GOLD
            + mergerTier;
        ret[origin.length + 5] = EnumChatFormatting.AQUA
            // #tr StrangeMatterAggregator.MachineInfoData.mergerPiece
            // # SpaceTime Merger {\BLUE}Rings
            // #zh_CN 时空归并器{\BLUE}环数
            + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.mergerPiece")
            + EnumChatFormatting.WHITE
            + ": "
            + EnumChatFormatting.GOLD
            + mergerPiece;
        ret[origin.length + 6] = EnumChatFormatting.AQUA
            // #tr StrangeMatterAggregator.MachineInfoData.totalRings
            // # {\BLUE}Total Rings
            // #zh_CN {\BLUE}总环数
            + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.totalRings")
            + EnumChatFormatting.WHITE
            + ": "
            + EnumChatFormatting.GOLD
            + rings;
        ret[origin.length + 7] = "" + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD
        // #tr StrangeMatterAggregator.MachineInfoData.consecutivePoint
        // # Continuous Running Points
        // #zh_CN 连续运行点数
            + TextEnums.tr("StrangeMatterAggregator.MachineInfoData.consecutivePoint")
            + EnumChatFormatting.WHITE
            + ": "
            + EnumChatFormatting.GOLD
            + consecutivePoint;
        return ret;
    }

    public boolean addSpaceTimeMaintenanceConsumablesInputHatchToMachineList(IGregTechTileEntity aTileEntity,
        int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            setHatchRecipeMap(hatch);
            SpaceTimeMaintenanceConsumablesInputHatch = hatch;
            return true;
        }
        return false;
    }

    public boolean addCoreElementInputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;

        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            hatch.mRecipeMap = getRecipeMap();
            CoreElementInputBus = hatch;
            return true;
        }
        return false;
    }

    private static final Collection<ModularHatchTypes> supportedModularHatchTypes = ImmutableList
        .of(ModularHatchTypes.POWER_CONSUMPTION_CONTROLLER, ModularHatchTypes.SPEED_CONTROLLER);

    @Override
    public Collection<ModularHatchTypes> getSupportedModularHatchTypes() {
        return supportedModularHatchTypes;
    }

    @Override
    protected boolean canMultiplyModularHatchType() {
        return false;
    }

    @Override
    public boolean checkMachineMM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.oscillatorTier = 0;
        this.oscillatorPiece = 0;
        this.constraintorTier = 0;
        this.constraintorPiece = 0;
        this.mergerTier = 0;
        this.mergerPiece = 0;
        this.rings = 0;
        this.SpaceTimeMaintenanceConsumablesInputHatch = null;
        this.CoreElementInputBus = null;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet_main, verticalOffSet_main, depthOffSet_main)) {
            return false;
        }

        while (true) {
            if (checkPiece(
                STRUCTURE_PIECE_RING_O,
                horizontalOffSet_ring,
                verticalOffSet_ring,
                depthOffSet_ring_first - rings * depthOffSet_ring_distance)) {

                oscillatorPiece++;

            } else if (checkPiece(
                STRUCTURE_PIECE_RING_C,
                horizontalOffSet_ring,
                verticalOffSet_ring,
                depthOffSet_ring_first - rings * depthOffSet_ring_distance)) {

                    TwistSpaceTechnology.LOG.info("TEST: checkPiece RING C constraintorPiece++.");
                    constraintorPiece++;

                } else if (checkPiece(
                    STRUCTURE_PIECE_RING_M,
                    horizontalOffSet_ring,
                    verticalOffSet_ring,
                    depthOffSet_ring_first - rings * depthOffSet_ring_distance)) {

                        TwistSpaceTechnology.LOG.info("TEST: checkPiece RING M mergerPiece++.");
                        mergerPiece++;

                    } else break;

            rings++;

        }

        if (oscillatorPiece < 1 || constraintorPiece < 1 || mergerPiece < 1) {
            TwistSpaceTechnology.LOG.info(
                "TEST: checkMachine oscillatorPiece < 1 || constraintorPiece < 1 || mergerPiece < 1. return false;");
            return false;
        }

        if (!checkPiece(
            STRUCTURE_PIECE_END,
            horizontalOffSet_main,
            verticalOffSet_main,
            depthOffSet_ring_first - rings * depthOffSet_ring_distance)) {
            return false;
        }

        calculateParametersWithStructure();

        return wirelessMode || getMaxInputEu() >= Config.PowerConsume_StrangeMatterAggregator;
    }

    // endregion

    // region Structure

    protected static final int horizontalOffSet_main = 8;
    protected static final int verticalOffSet_main = 8;
    protected static final int depthOffSet_main = 0;
    protected static final int horizontalOffSet_ring = 17;
    protected static final int verticalOffSet_ring = 17;
    protected static final int depthOffSet_ring_first = -4;
    protected static final int depthOffSet_ring_distance = 16;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final String STRUCTURE_PIECE_RING_O = "ringOscillator";
    protected static final String STRUCTURE_PIECE_RING_C = "ringConstraintor";
    protected static final String STRUCTURE_PIECE_RING_M = "ringMerger";
    protected static final String STRUCTURE_PIECE_END = "end";

    protected static IStructureDefinition<TST_StrangeMatterAggregator> STRUCTURE_DEFINITION = null;
    protected String[] buildingRingPieceArray = new String[] { STRUCTURE_PIECE_RING_O, STRUCTURE_PIECE_RING_C,
        STRUCTURE_PIECE_RING_M };

    protected void flushBuildingRingPieceArray() {
        buildingRingPieceArray = new String[oscillatorPieceNeed + constraintorPieceNeed + mergerPieceNeed];
        int p = 0;
        for (; p < oscillatorPieceNeed; p++) {
            buildingRingPieceArray[p] = STRUCTURE_PIECE_RING_O;
        }
        int oc = oscillatorPieceNeed + constraintorPieceNeed;
        for (; p < oc; p++) {
            buildingRingPieceArray[p] = STRUCTURE_PIECE_RING_C;
        }
        int ocm = oc + mergerPieceNeed;
        for (; p < ocm; p++) {
            buildingRingPieceArray[p] = STRUCTURE_PIECE_RING_M;
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            horizontalOffSet_main,
            verticalOffSet_main,
            depthOffSet_main);
        for (int i = 0; i < stackSize.stackSize; i++) {
            buildPiece(
                buildingRingPieceArray[i % buildingRingPieceArray.length],
                stackSize,
                hintsOnly,
                horizontalOffSet_ring,
                verticalOffSet_ring,
                depthOffSet_ring_first - i * depthOffSet_ring_distance);
        }
        buildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            hintsOnly,
            horizontalOffSet_main,
            verticalOffSet_main,
            depthOffSet_ring_first - stackSize.stackSize * depthOffSet_ring_distance);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;

        int built = survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet_main,
            verticalOffSet_main,
            depthOffSet_main,
            elementBudget,
            env,
            false,
            true);

        if (built >= 0) return built;

        for (int i = 0; i < stackSize.stackSize; i++) {
            built = survivialBuildPiece(
                buildingRingPieceArray[i % buildingRingPieceArray.length],
                stackSize,
                horizontalOffSet_ring,
                verticalOffSet_ring,
                depthOffSet_ring_first - i * depthOffSet_ring_distance,
                elementBudget,
                env,
                false,
                true);
            if (built >= 0) return built;
        }

        return survivialBuildPiece(
            STRUCTURE_PIECE_END,
            stackSize,
            horizontalOffSet_main,
            verticalOffSet_main,
            depthOffSet_ring_first - depthOffSet_ring_distance * stackSize.stackSize,
            elementBudget,
            env,
            false,
            true);

    }

    @Override
    public IStructureDefinition<TST_StrangeMatterAggregator> getStructureDefinition() {
        if (null == STRUCTURE_DEFINITION) {
            STRUCTURE_DEFINITION = StructureDefinition.<TST_StrangeMatterAggregator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shapeMain))
                .addShape(STRUCTURE_PIECE_RING_O, transpose(shapeRing_SpaceTimeOscillator))
                .addShape(STRUCTURE_PIECE_RING_C, transpose(shapeRing_SpaceTimeConstraintor))
                .addShape(STRUCTURE_PIECE_RING_M, transpose(shapeRing_SpaceTimeMerger))
                .addShape(STRUCTURE_PIECE_END, transpose(shapeEnd))
                .addElement('A', ofBlock(sBlockCasingsBA0, 10))
                .addElement('B', ofBlock(sBlockCasingsBA0, 11))
                .addElement('C', ofBlock(sBlockCasingsBA0, 12))
                .addElement('D', ofBlock(sBlockCasingsTT, 9))
                .addElement('E', ofBlock(sBlockCasingsTT, 10))
                .addElement('F', ofBlock(sBlockCasingsTT, 14))
                .addElement('G', ofBlock(QuantumGlassBlock.INSTANCE, 0))
                .addElement(
                    'H',
                    // H -> ofBlock...(gt.blockcasingsBA0, 12, ...); // modular hatches and output bus hatches
                    GT_HatchElementBuilder.<TST_StrangeMatterAggregator>builder()
                        .atLeast(OutputBus, OutputHatch, SpeedController, PowerConsumptionController)
                        .adder(TST_StrangeMatterAggregator::addToMachineList)
                        .dot(1)
                        .casingIndex(1024)
                        .buildAndChain(ofBlock(sBlockCasingsBA0, 12)))
                .addElement(
                    'I',
                    // I -> ofBlock...(gt.blockcasingsBA0, 12, ...); // input hatch at left which input space-time
                    // holding consumables
                    GT_HatchElementBuilder.<TST_StrangeMatterAggregator>builder()
                        .atLeast(InputHatch)
                        .adder(TST_StrangeMatterAggregator::addSpaceTimeMaintenanceConsumablesInputHatchToMachineList)
                        .dot(3)
                        .casingIndex(1024)
                        .buildAndChain(ofBlock(sBlockCasingsBA0, 12)))
                .addElement(
                    'J',
                    // J -> ofBlock...(gt.blockcasingsBA0, 12, ...); // normal input at up and down hatches and buses
                    GT_HatchElementBuilder.<TST_StrangeMatterAggregator>builder()
                        .atLeast(InputBus, InputHatch)
                        .adder(TST_StrangeMatterAggregator::addToMachineList)
                        .dot(2)
                        .casingIndex(1024)
                        .buildAndChain(ofBlock(sBlockCasingsBA0, 12)))
                .addElement(
                    'K',
                    // K -> ofBlock...(gt.blockcasingsBA0, 12, ...); // input bus at right which input Core Element to
                    // set machine processing tier.
                    GT_HatchElementBuilder.<TST_StrangeMatterAggregator>builder()
                        .atLeast(InputBus)
                        .adder(TST_StrangeMatterAggregator::addCoreElementInputBusToMachineList)
                        .dot(4)
                        .casingIndex(1024)
                        .buildAndChain(ofBlock(sBlockCasingsBA0, 12)))
                .addElement(
                    'X',
                    withChannel(
                        "oscillator",
                        ofBlocksTiered(
                            TST_StrangeMatterAggregator::getSpaceTimeOscillatorTier,
                            ImmutableList.of(
                                Pair.of(BasicBlocks.SpaceTimeOscillator, 0),
                                Pair.of(BasicBlocks.SpaceTimeOscillator, 1),
                                Pair.of(BasicBlocks.SpaceTimeOscillator, 2)),
                            0,
                            (m, t) -> m.oscillatorTier = t,
                            m -> m.oscillatorTier)))
                .addElement(
                    'Y',
                    withChannel(
                        "constraintor",
                        ofBlocksTiered(
                            TST_StrangeMatterAggregator::getSpaceTimeConstraintor,
                            ImmutableList.of(
                                Pair.of(BasicBlocks.SpaceTimeConstraintor, 0),
                                Pair.of(BasicBlocks.SpaceTimeConstraintor, 1),
                                Pair.of(BasicBlocks.SpaceTimeConstraintor, 2)),
                            0,
                            (m, t) -> m.constraintorTier = t,
                            m -> m.constraintorTier)))
                .addElement(
                    'Z',
                    withChannel(
                        "merger",
                        ofBlocksTiered(
                            TST_StrangeMatterAggregator::getSpaceTimeMerger,
                            ImmutableList.of(
                                Pair.of(BasicBlocks.SpaceTimeMerger, 0),
                                Pair.of(BasicBlocks.SpaceTimeMerger, 1),
                                Pair.of(BasicBlocks.SpaceTimeMerger, 2)),
                            0,
                            (m, t) -> m.mergerTier = t,
                            m -> m.mergerTier)))
                .build();

        }
        return STRUCTURE_DEFINITION;
    }

    public static int getSpaceTimeOscillatorTier(Block b, int m) {
        if (b != BasicBlocks.SpaceTimeOscillator) return 0;
        return m + 1;
    }

    public static int getSpaceTimeConstraintor(Block b, int m) {
        if (b != BasicBlocks.SpaceTimeConstraintor) return 0;
        return m + 1;
    }

    public static int getSpaceTimeMerger(Block b, int m) {
        if (b != BasicBlocks.SpaceTimeMerger) return 0;
        return m + 1;
    }

    // spotless:off

    /*
        A -> ofBlock...(gt.blockcasingsBA0, 10, ...);
        B -> ofBlock...(gt.blockcasingsBA0, 11, ...);
        C -> ofBlock...(gt.blockcasingsBA0, 12, ...);
        D -> ofBlock...(gt.blockcasingsTT, 9, ...);
        E -> ofBlock...(gt.blockcasingsTT, 10, ...);
        F -> ofBlock...(gt.blockcasingsTT, 14, ...);
        G -> ofBlock...(tile.quantumGlass, 0, ...);
        H -> ofBlock...(gt.blockcasingsBA0, 12, ...); // modular hatches and output bus hatches
        I -> ofBlock...(gt.blockcasingsBA0, 12, ...); // input hatch at left which input space-time holding consumables
        J -> ofBlock...(gt.blockcasingsBA0, 12, ...); // normal input at up and down hatches and buses
        K -> ofBlock...(gt.blockcasingsBA0, 12, ...); // input bus at right which input Core Element to set machine processing tier.
        Z -> ofBlock...(SpaceTimeConstraintor, 0, ...); // tier block
     */
    protected static final String[][] shapeMain = new String[][]{
        {"                 ","                 ","       CCC       ","                 "},
        {"                 ","       BBB       ","     CCAAACC     ","       BBB       "},
        {"                 ","     BBBFBBB     ","    CAACCCAAC    ","     BBGGGBB     "},
        {"                 ","    BBBBBBBBB    ","   CACCCCCCCAC   ","    BGG   GGB    "},
        {"       BBB       ","   BBBBBBBBBBB   ","  CACCCCFCCCCAC  ","   BG       GB   "},
        {"       BJB       ","  BBBFBBBBBFBBB  "," CACCFCCCCCFCCAC ","  BG         GB  "},
        {"      BBBBB      ","  BBBBBBBBBBBBB  "," CACCCCCCCCCCCAC ","  BG         GB  "},
        {"    BBBHHHBBB    "," BBBBBBBBBBBBBBB ","CACCCCCCCCCCCCCAC"," BG    D D    GB "},
        {"    BIBH~HBKB    "," BFBBBBBBBBBBBFB ","CACCFCCCCCCCFCCAC"," BG     E     GB "},
        {"    BBBHHHBBB    "," BBBBBBBBBBBBBBB ","CACCCCCCCCCCCCCAC"," BG    D D    GB "},
        {"      BBBBB      ","  BBBBBBBBBBBBB  "," CACCCCCCCCCCCAC ","  BG         GB  "},
        {"       BJB       ","  BBBFBBBBBFBBB  "," CACCFCCCCCFCCAC ","  BG         GB  "},
        {"       BBB       ","   BBBBBBBBBBB   ","  CACCCCFCCCCAC  ","   BG       GB   "},
        {"                 ","    BBBBBBBBB    ","   CACCCCCCCAC   ","    BGG   GGB    "},
        {"                 ","     BBBFBBB     ","    CAACCCAAC    ","     BBGGGBB     "},
        {"                 ","       BBB       ","     CCAAACC     ","       BBB       "},
        {"                 ","                 ","       CCC       ","                 "}
    };

    protected static final String[][] shapeRing_SpaceTimeOscillator = new String[][]{
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               AAAAA               ","               AAAAA               ","               AAAAA               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               BBBBB               ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","               BBBBB               ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBB     BBB            ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","            BBB     BBB            ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","          BB           BB          ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","          BB           BB          ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","        BB               BB        ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","        BB               BB        ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","       B                   B       ","      AACC       A       CCAA      ","      AACC      ADA      CCAA      ","      AACC       A       CCAA      ","       B                   B       ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","      B         BBB         B      ","     AAC       BFAFB       CAA     ","     AAC       BAAAB       CAA     ","     AAC       BFAFB       CAA     ","      B         BBB         B      ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                BBB                ","              BBCCCBB              ","     B        BCCCCCB        B     ","    AAC      BCCAAACCB      CAA    ","    AAC      BCCACACCB      CAA    ","    AAC      BCCAAACCB      CAA    ","     B        BCCCCCB        B     ","              BBCCCBB              ","                BBB                ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                BBB                ","              BB   BB              ","             B       B             ","    B        B       B        B    ","   AAC      B   AAA   B      CAA   ","   AAC      B   ACA   B      CAA   ","   AAC      B   AAA   B      CAA   ","    B        B       B        B    ","             B       B             ","              BB   BB              ","                BBB                ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","    B           XXX           B    ","   AAC         XDEDX         CAA   ","   AAC         XEEEX         CAA   ","   AAC         XDEDX         CAA   ","    B           XXX           B    ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","   B                           B   ","  AAC                         CAA  ","  AAC                         CAA  ","  AAC                         CAA  ","   B                           B   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","   B            GGG            B   ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","   B            GGG            B   ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                "},
        {"              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","  B           GG   GG           B  "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA ","  B           GG   GG           B  ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              "},
        {"             G       G             ","             G       G             ","             G       G             ","             G       G             ","             G       G             ","        B    G       G    B        ","  B     B    G       G    B     B  "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA ","  B     B    G       G    B     B  ","        B    G       G    B        ","             G       G             ","             G       G             ","             G       G             ","             G       G             "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       ","  B    B    G         G    B    B  "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA ","  B    B    G         G    B    B  ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       "," B     C    G         G    C     B ","AAC   BC X  G         G  X CB   CAA","AAC   BC X  G         G  X CB   CAA","AAC   BC X  G         G  X CB   CAA"," B     C    G         G    C     B ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"           G           G           ","           G    D D    G           ","           G           G           ","        B  G    D D    G  B        ","       B   G           G   B       ","       C   G    D D    G   C       "," B    BC X G           G X CB    B ","AAC   FAAD G    D D    G DAAF   CAA","AAC  AAAAE G           G EAAAA  CAA","AAC   FAAD G    D D    G DAAF   CAA"," B    BC X G           G X CB    B ","       C   G    D D    G   C       ","       B   G           G   B       ","        B  G    D D    G  B        ","           G           G           ","           G    D D    G           "},
        {"           G     E     G           ","           G     E     G           ","           G     E     G           ","        B  G     E     G  B        ","       B   G     E     G   B       ","       C   G     E     G   C       "," B    BC X G     E     G X CB    B ","AAC  AAAAE G     E     G EAAAA  CAA","AAC  DACCE G     E     G ECCAD  CAA","AAC  AAAAE G     E     G EAAAA  CAA"," B    BC X G     E     G X CB    B ","       C   G     E     G   C       ","       B   G     E     G   B       ","        B  G     E     G  B        ","           G     E     G           ","           G     E     G           "},
        {"           G           G           ","           G    D D    G           ","           G           G           ","        B  G    D D    G  B        ","       B   G           G   B       ","       C   G    D D    G   C       "," B    BC X G           G X CB    B ","AAC   FAAD G    D D    G DAAF   CAA","AAC  AAAAE G           G EAAAA  CAA","AAC   FAAD G    D D    G DAAF   CAA"," B    BC X G           G X CB    B ","       C   G    D D    G   C       ","       B   G           G   B       ","        B  G    D D    G  B        ","           G           G           ","           G    D D    G           "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       "," B     C    G         G    C     B ","AAC   BC X  G         G  X CB   CAA","AAC   BC X  G         G  X CB   CAA","AAC   BC X  G         G  X CB   CAA"," B     C    G         G    C     B ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       ","  B    B    G         G    B    B  "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA ","  B    B    G         G    B    B  ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"             G       G             ","             G       G             ","             G       G             ","             G       G             ","             G       G             ","        B    G       G    B        ","  B     B    G       G    B     B  "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA ","  B     B    G       G    B     B  ","        B    G       G    B        ","             G       G             ","             G       G             ","             G       G             ","             G       G             "},
        {"              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","  B           GG   GG           B  "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA ","  B           GG   GG           B  ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              "},
        {"                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","   B            GGG            B   ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","   B            GGG            B   ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","   B                           B   ","  AAC                         CAA  ","  AAC                         CAA  ","  AAC                         CAA  ","   B                           B   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","    B           XXX           B    ","   AAC         XDEDX         CAA   ","   AAC         XEEEX         CAA   ","   AAC         XDEDX         CAA   ","    B           XXX           B    ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                BBB                ","              BB   BB              ","             B       B             ","    B        B       B        B    ","   AAC      B   AAA   B      CAA   ","   AAC      B   ACA   B      CAA   ","   AAC      B   AAA   B      CAA   ","    B        B       B        B    ","             B       B             ","              BB   BB              ","                BBB                ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                BBB                ","              BBCCCBB              ","     B        BCCCCCB        B     ","    AAC      BCCAAACCB      CAA    ","    AAC      BCCACACCB      CAA    ","    AAC      BCCAAACCB      CAA    ","     B        BCCCCCB        B     ","              BBCCCBB              ","                BBB                ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","      B         BBB         B      ","     AAC       BFAFB       CAA     ","     AAC       BAAAB       CAA     ","     AAC       BFAFB       CAA     ","      B         BBB         B      ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","       B                   B       ","      AACC       A       CCAA      ","      AACC      ADA      CCAA      ","      AACC       A       CCAA      ","       B                   B       ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","        BB               BB        ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","        BB               BB        ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","          BB           BB          ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","          BB           BB          ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBB     BBB            ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","            BBB     BBB            ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               BBBBB               ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","               BBBBB               ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               AAAAA               ","               AAAAA               ","               AAAAA               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "}
    };

    protected static final String[][] shapeRing_SpaceTimeConstraintor = new String[][]{
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               AAAAA               ","               AAAAA               ","               AAAAA               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               BBBBB               ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","               BBBBB               ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBB     BBB            ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","            BBB     BBB            ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","          BB           BB          ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","          BB           BB          ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","        BB               BB        ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","        BB               BB        ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","       B                   B       ","      AACC       A       CCAA      ","      AACC      ADA      CCAA      ","      AACC       A       CCAA      ","       B                   B       ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","      B         BBB         B      ","     AAC       BFAFB       CAA     ","     AAC       BAAAB       CAA     ","     AAC       BFAFB       CAA     ","      B         BBB         B      ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                BBB                ","              BBCCCBB              ","     B        BCCCCCB        B     ","    AAC      BCCAAACCB      CAA    ","    AAC      BCCACACCB      CAA    ","    AAC      BCCAAACCB      CAA    ","     B        BCCCCCB        B     ","              BBCCCBB              ","                BBB                ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                BBB                ","              BB   BB              ","             B       B             ","    B        B       B        B    ","   AAC      B   AAA   B      CAA   ","   AAC      B   ACA   B      CAA   ","   AAC      B   AAA   B      CAA   ","    B        B       B        B    ","             B       B             ","              BB   BB              ","                BBB                ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","    B           YYY           B    ","   AAC         YDEDY         CAA   ","   AAC         YEEEY         CAA   ","   AAC         YDEDY         CAA   ","    B           YYY           B    ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","   B                           B   ","  AAC                         CAA  ","  AAC                         CAA  ","  AAC                         CAA  ","   B                           B   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","   B            GGG            B   ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","   B            GGG            B   ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                "},
        {"              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","  B           GG   GG           B  "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA ","  B           GG   GG           B  ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              "},
        {"             G       G             ","             G       G             ","             G       G             ","             G       G             ","             G       G             ","        B    G       G    B        ","  B     B    G       G    B     B  "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA ","  B     B    G       G    B     B  ","        B    G       G    B        ","             G       G             ","             G       G             ","             G       G             ","             G       G             "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       ","  B    B    G         G    B    B  "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA ","  B    B    G         G    B    B  ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       "," B     C    G         G    C     B ","AAC   BC Y  G         G  Y CB   CAA","AAC   BC Y  G         G  Y CB   CAA","AAC   BC Y  G         G  Y CB   CAA"," B     C    G         G    C     B ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"           G           G           ","           G    D D    G           ","           G           G           ","        B  G    D D    G  B        ","       B   G           G   B       ","       C   G    D D    G   C       "," B    BC Y G           G Y CB    B ","AAC   FAAD G    D D    G DAAF   CAA","AAC  AAAAE G           G EAAAA  CAA","AAC   FAAD G    D D    G DAAF   CAA"," B    BC Y G           G Y CB    B ","       C   G    D D    G   C       ","       B   G           G   B       ","        B  G    D D    G  B        ","           G           G           ","           G    D D    G           "},
        {"           G     E     G           ","           G     E     G           ","           G     E     G           ","        B  G     E     G  B        ","       B   G     E     G   B       ","       C   G     E     G   C       "," B    BC Y G     E     G Y CB    B ","AAC  AAAAE G     E     G EAAAA  CAA","AAC  DACCE G     E     G ECCAD  CAA","AAC  AAAAE G     E     G EAAAA  CAA"," B    BC Y G     E     G Y CB    B ","       C   G     E     G   C       ","       B   G     E     G   B       ","        B  G     E     G  B        ","           G     E     G           ","           G     E     G           "},
        {"           G           G           ","           G    D D    G           ","           G           G           ","        B  G    D D    G  B        ","       B   G           G   B       ","       C   G    D D    G   C       "," B    BC Y G           G Y CB    B ","AAC   FAAD G    D D    G DAAF   CAA","AAC  AAAAE G           G EAAAA  CAA","AAC   FAAD G    D D    G DAAF   CAA"," B    BC Y G           G Y CB    B ","       C   G    D D    G   C       ","       B   G           G   B       ","        B  G    D D    G  B        ","           G           G           ","           G    D D    G           "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       "," B     C    G         G    C     B ","AAC   BC Y  G         G  Y CB   CAA","AAC   BC Y  G         G  Y CB   CAA","AAC   BC Y  G         G  Y CB   CAA"," B     C    G         G    C     B ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       ","  B    B    G         G    B    B  "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA ","  B    B    G         G    B    B  ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"             G       G             ","             G       G             ","             G       G             ","             G       G             ","             G       G             ","        B    G       G    B        ","  B     B    G       G    B     B  "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA ","  B     B    G       G    B     B  ","        B    G       G    B        ","             G       G             ","             G       G             ","             G       G             ","             G       G             "},
        {"              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","  B           GG   GG           B  "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA ","  B           GG   GG           B  ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              "},
        {"                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","   B            GGG            B   ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","   B            GGG            B   ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","   B                           B   ","  AAC                         CAA  ","  AAC                         CAA  ","  AAC                         CAA  ","   B                           B   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","    B           YYY           B    ","   AAC         YDEDY         CAA   ","   AAC         YEEEY         CAA   ","   AAC         YDEDY         CAA   ","    B           YYY           B    ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                BBB                ","              BB   BB              ","             B       B             ","    B        B       B        B    ","   AAC      B   AAA   B      CAA   ","   AAC      B   ACA   B      CAA   ","   AAC      B   AAA   B      CAA   ","    B        B       B        B    ","             B       B             ","              BB   BB              ","                BBB                ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                BBB                ","              BBCCCBB              ","     B        BCCCCCB        B     ","    AAC      BCCAAACCB      CAA    ","    AAC      BCCACACCB      CAA    ","    AAC      BCCAAACCB      CAA    ","     B        BCCCCCB        B     ","              BBCCCBB              ","                BBB                ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","      B         BBB         B      ","     AAC       BFAFB       CAA     ","     AAC       BAAAB       CAA     ","     AAC       BFAFB       CAA     ","      B         BBB         B      ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","       B                   B       ","      AACC       A       CCAA      ","      AACC      ADA      CCAA      ","      AACC       A       CCAA      ","       B                   B       ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","        BB               BB        ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","        BB               BB        ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","          BB           BB          ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","          BB           BB          ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBB     BBB            ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","            BBB     BBB            ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               BBBBB               ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","               BBBBB               ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               AAAAA               ","               AAAAA               ","               AAAAA               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "}
    };

    protected static final String[][] shapeRing_SpaceTimeMerger = new String[][]{
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               AAAAA               ","               AAAAA               ","               AAAAA               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               BBBBB               ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","               BBBBB               ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBB     BBB            ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","            BBB     BBB            ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","          BB           BB          ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","          BB           BB          ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","        BB               BB        ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","        BB               BB        ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","       B                   B       ","      AACC       A       CCAA      ","      AACC      ADA      CCAA      ","      AACC       A       CCAA      ","       B                   B       ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","      B         BBB         B      ","     AAC       BFAFB       CAA     ","     AAC       BAAAB       CAA     ","     AAC       BFAFB       CAA     ","      B         BBB         B      ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                BBB                ","              BBCCCBB              ","     B        BCCCCCB        B     ","    AAC      BCCAAACCB      CAA    ","    AAC      BCCACACCB      CAA    ","    AAC      BCCAAACCB      CAA    ","     B        BCCCCCB        B     ","              BBCCCBB              ","                BBB                ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                BBB                ","              BB   BB              ","             B       B             ","    B        B       B        B    ","   AAC      B   AAA   B      CAA   ","   AAC      B   ACA   B      CAA   ","   AAC      B   AAA   B      CAA   ","    B        B       B        B    ","             B       B             ","              BB   BB              ","                BBB                ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","    B           ZZZ           B    ","   AAC         ZDEDZ         CAA   ","   AAC         ZEEEZ         CAA   ","   AAC         ZDEDZ         CAA   ","    B           ZZZ           B    ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","   B                           B   ","  AAC                         CAA  ","  AAC                         CAA  ","  AAC                         CAA  ","   B                           B   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","   B            GGG            B   ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","   B            GGG            B   ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                "},
        {"              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","  B           GG   GG           B  "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA ","  B           GG   GG           B  ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              "},
        {"             G       G             ","             G       G             ","             G       G             ","             G       G             ","             G       G             ","        B    G       G    B        ","  B     B    G       G    B     B  "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA ","  B     B    G       G    B     B  ","        B    G       G    B        ","             G       G             ","             G       G             ","             G       G             ","             G       G             "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       ","  B    B    G         G    B    B  "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA ","  B    B    G         G    B    B  ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       "," B     C    G         G    C     B ","AAC   BC Z  G         G  Z CB   CAA","AAC   BC Z  G         G  Z CB   CAA","AAC   BC Z  G         G  Z CB   CAA"," B     C    G         G    C     B ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"           G           G           ","           G    D D    G           ","           G           G           ","        B  G    D D    G  B        ","       B   G           G   B       ","       C   G    D D    G   C       "," B    BC Z G           G Z CB    B ","AAC   FAAD G    D D    G DAAF   CAA","AAC  AAAAE G           G EAAAA  CAA","AAC   FAAD G    D D    G DAAF   CAA"," B    BC Z G           G Z CB    B ","       C   G    D D    G   C       ","       B   G           G   B       ","        B  G    D D    G  B        ","           G           G           ","           G    D D    G           "},
        {"           G     E     G           ","           G     E     G           ","           G     E     G           ","        B  G     E     G  B        ","       B   G     E     G   B       ","       C   G     E     G   C       "," B    BC Z G     E     G Z CB    B ","AAC  AAAAE G     E     G EAAAA  CAA","AAC  DACCE G     E     G ECCAD  CAA","AAC  AAAAE G     E     G EAAAA  CAA"," B    BC Z G     E     G Z CB    B ","       C   G     E     G   C       ","       B   G     E     G   B       ","        B  G     E     G  B        ","           G     E     G           ","           G     E     G           "},
        {"           G           G           ","           G    D D    G           ","           G           G           ","        B  G    D D    G  B        ","       B   G           G   B       ","       C   G    D D    G   C       "," B    BC Z G           G Z CB    B ","AAC   FAAD G    D D    G DAAF   CAA","AAC  AAAAE G           G EAAAA  CAA","AAC   FAAD G    D D    G DAAF   CAA"," B    BC Z G           G Z CB    B ","       C   G    D D    G   C       ","       B   G           G   B       ","        B  G    D D    G  B        ","           G           G           ","           G    D D    G           "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       "," B     C    G         G    C     B ","AAC   BC Z  G         G  Z CB   CAA","AAC   BC Z  G         G  Z CB   CAA","AAC   BC Z  G         G  Z CB   CAA"," B     C    G         G    C     B ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"            G         G            ","            G         G            ","            G         G            ","            G         G            ","        B   G         G   B        ","       B    G         G    B       ","  B    B    G         G    B    B  "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA "," AAC   C    G         G    C   CAA ","  B    B    G         G    B    B  ","       B    G         G    B       ","        B   G         G   B        ","            G         G            ","            G         G            ","            G         G            "},
        {"             G       G             ","             G       G             ","             G       G             ","             G       G             ","             G       G             ","        B    G       G    B        ","  B     B    G       G    B     B  "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA "," AAC   B     G       G     B   CAA ","  B     B    G       G    B     B  ","        B    G       G    B        ","             G       G             ","             G       G             ","             G       G             ","             G       G             "},
        {"              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","  B           GG   GG           B  "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA "," AAC    B     GG   GG     B    CAA ","  B           GG   GG           B  ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              ","              GG   GG              "},
        {"                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","   B            GGG            B   ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","  AAC           GGG           CAA  ","   B            GGG            B   ","                GGG                ","                GGG                ","                GGG                ","                GGG                ","                GGG                "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","   B                           B   ","  AAC                         CAA  ","  AAC                         CAA  ","  AAC                         CAA  ","   B                           B   ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","    B           ZZZ           B    ","   AAC         ZDEDZ         CAA   ","   AAC         ZEEEZ         CAA   ","   AAC         ZDEDZ         CAA   ","    B           ZZZ           B    ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                BBB                ","              BB   BB              ","             B       B             ","    B        B       B        B    ","   AAC      B   AAA   B      CAA   ","   AAC      B   ACA   B      CAA   ","   AAC      B   AAA   B      CAA   ","    B        B       B        B    ","             B       B             ","              BB   BB              ","                BBB                ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                BBB                ","              BBCCCBB              ","     B        BCCCCCB        B     ","    AAC      BCCAAACCB      CAA    ","    AAC      BCCACACCB      CAA    ","    AAC      BCCAAACCB      CAA    ","     B        BCCCCCB        B     ","              BBCCCBB              ","                BBB                ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","      B         BBB         B      ","     AAC       BFAFB       CAA     ","     AAC       BAAAB       CAA     ","     AAC       BFAFB       CAA     ","      B         BBB         B      ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","       B                   B       ","      AACC       A       CCAA      ","      AACC      ADA      CCAA      ","      AACC       A       CCAA      ","       B                   B       ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","        BB               BB        ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","       AAACC           CCAAA       ","        BB               BB        ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","          BB           BB          ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","        AAAACCC     CCCAAAA        ","          BB           BB          ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBB     BBB            ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","          AAAAACCCCCAAAAA          ","            BBB     BBB            ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               BBBBB               ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","            AAAAAAAAAAA            ","               BBBBB               ","                                   ","                                   ","                                   ","                                   ","                                   "},
        {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               AAAAA               ","               AAAAA               ","               AAAAA               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "}
    };

    protected static final String[][] shapeEnd = new String[][]{
        {"                 ","                 ","       CCC       ","                 "},
        {"                 ","       BBB       ","     CCAAACC     ","       BBB       "},
        {"       GGG       ","     BBGGGBB     ","    CAACCCAAC    ","     BBBFBBB     "},
        {"     GG   GG     ","    BGG   GGB    ","   CACCCCCCCAC   ","    BBBBBBBBB    "},
        {"    G       G    ","   BG       GB   ","  CACCCCFCCCCAC  ","   BBBBBBBBBBB   "},
        {"   G         G   ","  BG         GB  "," CACCFCCCCCFCCAC ","  BBBFBBBBBFBBB  "},
        {"   G         G   ","  BG         GB  "," CACCCCCCCCCCCAC ","  BBBBBBBBBBBBB  "},
        {"  G           G  "," BG    D D    GB ","CACCCCCCCCCCCCCAC"," BBBBBBBBBBBBBBB "},
        {"  G     E     G  "," BG     E     GB ","CACCFCCCCCCCFCCAC"," BFBBBBBBBBBBBFB "},
        {"  G           G  "," BG    D D    GB ","CACCCCCCCCCCCCCAC"," BBBBBBBBBBBBBBB "},
        {"   G         G   ","  BG         GB  "," CACCCCCCCCCCCAC ","  BBBBBBBBBBBBB  "},
        {"   G         G   ","  BG         GB  "," CACCFCCCCCFCCAC ","  BBBFBBBBBFBBB  "},
        {"    G       G    ","   BG       GB   ","  CACCCCFCCCCAC  ","   BBBBBBBBBBB   "},
        {"     GG   GG     ","    BGG   GGB    ","   CACCCCCCCAC   ","    BBBBBBBBB    "},
        {"       GGG       ","     BBGGGBB     ","    CAACCCAAC    ","     BBBFBBB     "},
        {"                 ","       BBB       ","     CCAAACC     ","       BBB       "},
        {"                 ","                 ","       CCC       ","                 "}
    };

    // spotless:on

    // endregion

    // region General
    private static GT_Multiblock_Tooltip_Builder tooltip;

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        // spotless:off
        if (tooltip == null) {
            tooltip = new GT_Multiblock_Tooltip_Builder();
            // #tr Tooltip_StrangeMatterAggregator_MachineType
            // # {\WHITE}Modularized Machine {\GRAY}- {\YELLOW}Strange Matter Aggregator
            // #zh_CN {\WHITE}模块化机械 {\GRAY}- {\YELLOW}奇异物质聚合器
            tooltip
                .addMachineType(
                    TextEnums.tr("Tooltip_StrangeMatterAggregator_MachineType"))
                // #tr Tooltip_StrangeMatterAggregator_01
                // # {\ITALIC}{\DARK_BLUE}Life is like Ephemera in the world, a drop in the sea.
                // #zh_CN {\ITALIC}{\DARK_BLUE}寄蜉蝣于天地，渺沧海之一粟。
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_01"))
                // #tr Tooltip_StrangeMatterAggregator_02
                // # Creation of specialized space-time, efficient manipulation of strange matter.
                // #zh_CN 创建专用的时空, 高效地操作奇异物质.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_02"))
                // #tr Tooltip_StrangeMatterAggregator_03
                // # The machines are stacked, and each layer can be fitted with one type of Space-Time Operator Cube.
                // #zh_CN 机器是叠层结构, 每层可以安装一种时空操作器方块.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_03"))
                // #tr Tooltip_StrangeMatterAggregator_04
                // # Higher tier, more rings, more benefits, but also costs.
                // #zh_CN 更高等级, 更多层数, 更多收益, 同时也有代价.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_04"))
                // #tr Tooltip_StrangeMatterAggregator_05
                // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\BLUE}---------------- {\GOLD}SpaceTime Oscillator {\BLUE}----------------
                // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\BLUE}---------------- {\GOLD}时空振荡器 {\BLUE}----------------
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_05"))
                // #tr Tooltip_StrangeMatterAggregator_06
                // # Base run time: the higher the tier the shorter the run time
                // #zh_CN 基础运行耗时：等级越高, 耗时越短
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_06"))
                // #tr Tooltip_StrangeMatterAggregator_07
                // # Basic materials consumption: the higher the number of rings, the lower the amount consumed
                // #zh_CN 基础原料消耗：层数越多, 消耗量越少
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_07"))
                // #tr Tooltip_StrangeMatterAggregator_08
                // # SpaceTime maintenance fluid consumption: more rings more consumed
                // #zh_CN 时空维护流体消耗：层数越多, 消耗量越多
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_08"))
                // #tr Tooltip_StrangeMatterAggregator_09
                // # Core element and annihilation constrainer consumption: higher tier, more rings, lower consumption rate
                // #zh_CN 核心素和湮灭约束器消耗：等级越高, 层数越多, 消耗率越低
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_09"))
                // #tr Tooltip_StrangeMatterAggregator_10
                // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\BLUE}---------------- {\GOLD}SpaceTime Constraintor {\BLUE}----------------
                // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\BLUE}---------------- {\GOLD}时空约束器 {\BLUE}----------------
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_10"))
                // #tr Tooltip_StrangeMatterAggregator_11
                // # Consumption rate of auxiliary materials: the higher the level, the lower the consumption rate
                // #zh_CN 辅助材料消耗率：等级越高, 消耗率越低
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_11"))
                // #tr Tooltip_StrangeMatterAggregator_12
                // # Actual running time: the higher the tier the longer the actual running time is
                // #zh_CN 实际运行耗时：等级越高, 实际运行耗时越长
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_12"))
                // #tr Tooltip_StrangeMatterAggregator_13
                // # SpaceTime maintenance fluid consumption: the more rings you have the less you consume
                // #zh_CN 时空维护流体消耗：层数越多, 消耗量越少
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_13"))
                // #tr Tooltip_StrangeMatterAggregator_14
                // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\BLUE}---------------- {\GOLD}SpaceTime Merger {\BLUE}----------------
                // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\BLUE}---------------- {\GOLD}时空归并器 {\BLUE}----------------
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_14"))
                // #tr Tooltip_StrangeMatterAggregator_15
                // # Maximum Continuous Running Points: The higher the tier, the higher the Continuous Running Points limit!
                // #zh_CN 最大连续运行点数：等级越高, 连续运行点数上限越高
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_15"))
                // #tr Tooltip_StrangeMatterAggregator_16
                // # Byproduct outputs: the more rings the more outputs
                // #zh_CN 副产物产出：层数越多, 产量越大
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_16"))
                // #tr Tooltip_StrangeMatterAggregator_17
                // # Power Consumption: higher tier, more rings, the higher the power consumption
                // #zh_CN 耗电：等级越高层数越多耗电越高
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_17"))
                // #tr Tooltip_StrangeMatterAggregator_18
                // # {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\BLUE}---------------- {\AQUA}{\BOLD}SpaceTime Maintenance Fluid {\RESET}{\BLUE}----------------
                // #zh_CN {\SPACE}{\SPACE}{\SPACE}{\SPACE}{\BLUE}---------------- {\AQUA}{\BOLD}时空维护流体 {\RESET}{\BLUE}----------------
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_18"))
                // #tr Tooltip_StrangeMatterAggregator_19
                // # Setting the used SpaceTime maintenance fluid tier within the machine UI:
                // #zh_CN 在主机UI内设置使用的时空维护流体:
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_19"))
                // #tr Tooltip_StrangeMatterAggregator_20
                // # {\SPACE}T1 = 576L Molten SpaceTime | T2 = 96L Molten Universium | T3 = 16L Molten Magnetohydrodynamically Constrained Star Matter
                // #zh_CN {\SPACE}T1 = 576L 熔融时空 | T2 = 96L 熔融宇宙素 | T3 = 16L 熔融磁流体约束恒星物质
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_20"))
                // #tr Tooltip_StrangeMatterAggregator_21
                // # Using Advanced SpaceTime Maintenance Fluid increases num of fuel rods can be crafted per Annihilation Constrainer and Core Element,
                // #zh_CN 使用高级时空维护流体可以提高每个湮灭约束器和核心素可以制作的燃料棒数量,
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_21"))
                // #tr Tooltip_StrangeMatterAggregator_22
                // # {\SPACE} and reduce the consumption of fluid materials. The type of byproducts depends on the SpaceTime maintenance fluid used.
                // #zh_CN {\SPACE}并降低流体原料的消耗量. 副产物类型取决于使用的时空维护流体.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_22"))
                // #tr Tooltip_StrangeMatterAggregator_23
                // # T1: Molten Infinity and Molten Hypogen; T2: Molten SpaceTime and Molten Shirabon; T3: Molten Universium;
                // #zh_CN T1: 熔融无尽和熔融海珀珍; T2: 熔融时空和熔融调律源金; T3: 熔融宇宙素;
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_23"))
                .addInfo(EnumChatFormatting.GOLD + TextLocalization.Text_SeparatingLine)
                // #tr Tooltip_StrangeMatterAggregator_24
                // # {\WHITE}{\BOLD}Continuous Operation{\RESET}{\WHITE} will produce by-products and more of the main product.
                // #zh_CN {\WHITE}{\BOLD}连续运行{\RESET}{\WHITE}时将产出副产物和更多的主产物.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_24"))
                // #tr Tooltip_StrangeMatterAggregator_25
                // # For each successful run, {\WHITE}Continuous Running Points{\GRAY} is increased by one.
                // #zh_CN 每次成功运行时, {\WHITE}连续运行点数{\GRAY}加一.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_25"))
                // #tr Tooltip_StrangeMatterAggregator_26
                // # Continuous provision of sufficient space-time maintenance fluid to maintain continuous operation. Otherwise, operation is interrupted.
                // #zh_CN 持续提供充足的时空维护流体可保持连续运行状态. 否则中断运行.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_26"))
                // #tr Tooltip_StrangeMatterAggregator_27
                // # {\WHITE}Provide {\YELLOW}{\BOLD}Core Elements {\WHITE}to obtain t2 products
                // #zh_CN {\WHITE}提供{\YELLOW}{\BOLD}核心素{\WHITE}获得二级产物
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_27"))
                // #tr Tooltip_StrangeMatterAggregator_28
                // # If input Core Element, machine will consume Core Element and output a portion of T2 product instead part primary product.
                // #zh_CN 如果有核心素输入则会消耗核心素并将一部分主产物替换为二级产物.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_28"))
                // #tr Tooltip_StrangeMatterAggregator_29
                // # At the same time, byproduct yields are doubled.
                // #zh_CN 同时副产物产量翻倍.
                .addInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator_29"))
                .addInfo(TextEnums.InstallingModuleNearControllerImproveMachine.getText())
                .addInfo(TextEnums.ModularizedMachineSystem.getText())
                .addSeparator()
                .addInfo(TextLocalization.StructureTooComplex)
                .addInfo(TextLocalization.BLUE_PRINT_INFO)
                .addStructureInfo(TextEnums.ModularizedMachineSystem.getText())
                .addStructureInfo(TextEnums.ModularizedMachineSystemDescription01.getText())
                .addStructureInfo(TextEnums.ModularizedMachineSystemDescription02.getText())
                .addStructureInfo(TextEnums.PowerConsumptionControllerDescription.getText())
                .addStructureInfo(TextEnums.SpeedControllerDescription.getText())
                .addStructureInfo(TextEnums.NotMultiplyInstallSameTypeModuleAll.getText())
                .addStructureInfo(TextLocalization.Text_SeparatingLine)
                // #tr Tooltip_StrangeMatterAggregator.structure.UI_Description.01
                // # {\WHITE}{\BOLD}Setting up auto-build rules for multi-rings structures within the controller UI.
                // #zh_CN {\WHITE}{\BOLD}在主机UI内设置多环结构的自动搭建规则.
                .addStructureInfo(TextEnums.tr("Tooltip_StrangeMatterAggregator.structure.UI_Description.01"))
                // #tr Tooltip_StrangeMatterAggregator.structure.SpaceTimeMaintenanceFluidInputHatch
                // # Input Hatch of SpaceTime Maintenance Fluid
                // #zh_CN 时空维护流体输入仓
                // #tr Tooltip_StrangeMatterAggregator.structure.Left
                // # Left area beside the controller block
                // #zh_CN 主方块左侧区域
                .addOtherStructurePart(TextEnums.tr("Tooltip_StrangeMatterAggregator.structure.SpaceTimeMaintenanceFluidInputHatch"), TextEnums.tr("Tooltip_StrangeMatterAggregator.structure.Left"), 3)
                // #tr Tooltip_StrangeMatterAggregator.structure.CoreElementAndAnnihilationConstrainerInputBus
                // # Input Bus of Core Element and Annihilation Constrainer
                // #zh_CN 核心素和湮灭约束器输入总线
                // #tr Tooltip_StrangeMatterAggregator.structure.Right
                // # Right area beside the controller block
                // #zh_CN 主方块右侧区域
                .addOtherStructurePart(TextEnums.tr("Tooltip_StrangeMatterAggregator.structure.CoreElementAndAnnihilationConstrainerInputBus"), TextEnums.tr("Tooltip_StrangeMatterAggregator.structure.Right"), 4)
                .addInputHatch(TextLocalization.textUseBlueprint, 2)
                .addInputBus(TextLocalization.textUseBlueprint, 2)
                .addOtherStructurePart(TextEnums.ModularHatch.getText(), TextLocalization.textUseBlueprint, 1)
                .addOutputBus(TextLocalization.textUseBlueprint, 1)
                .addOutputHatch(TextLocalization.textUseBlueprint, 1)
                .addEnergyHatch(TextLocalization.textUseBlueprint, 1)
                .toolTipFinisher(TextLocalization.ModName);
            // spotless:on
        }
        return tooltip;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    protected static final int SYNC_WINDOW_STRUCTURE_ID = 114;
    protected static final int SYNC_WINDOW_RUNNING_ID = 115;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(SYNC_WINDOW_STRUCTURE_ID, this::createStructureConfigurationWindow);
        buildContext.addSyncedWindow(SYNC_WINDOW_RUNNING_ID, this::createRunningConfigurationWindow);
        builder.widget(
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(SYNC_WINDOW_STRUCTURE_ID);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    ret.add(GT_UITextures.OVERLAY_BUTTON_CYCLIC);
                    return ret.toArray(new IDrawable[0]);
                })
                // #tr StrangeMatterAggregator.UI.BuildingInfoMenuButton.name
                // # Auto Building Configuration Menu
                // #zh_CN 自动搭建配置菜单
                .addTooltip(TextEnums.tr("StrangeMatterAggregator.UI.BuildingInfoMenuButton.name"))
                .setPos(174, 130))
            .widget(
                new ButtonWidget().setOnClick(
                    (clickData, widget) -> {
                        if (!widget.isClient()) widget.getContext()
                            .openSyncedWindow(SYNC_WINDOW_RUNNING_ID);
                    })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> ret = new ArrayList<>();
                        ret.add(GT_UITextures.BUTTON_STANDARD);
                        ret.add(GT_UITextures.OVERLAY_BUTTON_CYCLIC);
                        return ret.toArray(new IDrawable[0]);
                    })
                    // #tr StrangeMatterAggregator.UI.RunningInfoMenuButton.name
                    // # Running Configuration Menu
                    // #zh_CN 运行配置菜单
                    .addTooltip(TextEnums.tr("StrangeMatterAggregator.UI.RunningInfoMenuButton.name"))
                    .setPos(174, 112));
    }

    protected ModularWindow createStructureConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(240, 80);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());

        builder.widget(
            // spotless:off
            // #tr StrangeMatterAggregator.UI.Structure.ConfigurationDescription.text
            // # The machine will build the oscillator ring, constraintor ring, and merger ring in a set number of cycles.
            // #zh_CN 机器将按照设定数量的振荡器环, 约束器环, 归并器环依次循环搭建.
            // spotless:on
            TextWidget.localised("StrangeMatterAggregator.UI.Structure.ConfigurationDescription.text")
                .setPos(20, 10)
                .setSize(200, 14))
            .widget(
                // #tr StrangeMatterAggregator.UI.OscillatorPieceNeed.text
                // # Oscillator
                // #zh_CN 时空振荡器
                TextWidget.localised("StrangeMatterAggregator.UI.OscillatorPieceNeed.text")
                    .setPos(1, 36)
                    .setSize(100, 14))
            .widget(new TextFieldWidget().setSetterInt(val -> {
                oscillatorPieceNeed = val;
                flushBuildingRingPieceArray();
            })
                .setGetterInt(() -> oscillatorPieceNeed)
                .setNumbers(1, 64)
                .setOnScrollNumbers(1, 4, 16)
                .setTextAlignment(Alignment.Center)
                .setTextColor(Color.WHITE.normal)
                .setSize(40, 18)
                .setPos(30, 50)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD))
            .widget(
                // #tr StrangeMatterAggregator.UI.ConstraintorPieceNeed.text
                // # Constraintor
                // #zh_CN 时空约束器
                TextWidget.localised("StrangeMatterAggregator.UI.ConstraintorPieceNeed.text")
                    .setPos(71, 36)
                    .setSize(100, 14))
            .widget(new TextFieldWidget().setSetterInt(val -> {
                constraintorPieceNeed = val;
                flushBuildingRingPieceArray();
            })
                .setGetterInt(() -> constraintorPieceNeed)
                .setNumbers(1, 64)
                .setOnScrollNumbers(1, 4, 16)
                .setTextAlignment(Alignment.Center)
                .setTextColor(Color.WHITE.normal)
                .setSize(40, 18)
                .setPos(100, 50)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD))
            .widget(
                // #tr StrangeMatterAggregator.UI.MergerPieceNeed.text
                // # Merger
                // #zh_CN 时空归并器
                TextWidget.localised("StrangeMatterAggregator.UI.MergerPieceNeed.text")
                    .setPos(141, 36)
                    .setSize(100, 14))
            .widget(new TextFieldWidget().setSetterInt(val -> {
                mergerPieceNeed = val;
                flushBuildingRingPieceArray();
            })
                .setGetterInt(() -> mergerPieceNeed)
                .setNumbers(1, 64)
                .setOnScrollNumbers(1, 4, 16)
                .setTextAlignment(Alignment.Center)
                .setTextColor(Color.WHITE.normal)
                .setSize(40, 18)
                .setPos(170, 50)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));

        return builder.build();
    }

    protected ModularWindow createRunningConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(240, 80);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());

        builder.widget(
            // spotless:off
            // #tr StrangeMatterAggregator.UI.Running.ConfigurationDescription.text
            // # Set SpaceTime Maintenance Fluid Tier: 1-Molten SpaceTime, 2-Molten Universium, 3-MagnetoConstrainedStarMatter
            // #zh_CN 设置时空维护流体等级: 1-熔融时空, 2-熔融宇宙素, 3-磁流体约束恒星物质
            // spotless:on
            TextWidget.localised("StrangeMatterAggregator.UI.Running.ConfigurationDescription.text")
                .setPos(20, 10)
                .setSize(200, 14))
            .widget(new TextFieldWidget().setSetterInt(val -> {
                spaceTimeMaintenanceFluidTier = val;
                calculateParametersWithSettings();
            })
                .setGetterInt(() -> spaceTimeMaintenanceFluidTier)
                .setNumbers(1, 3)
                .setOnScrollNumbers(1, 1, 1)
                .setTextAlignment(Alignment.Center)
                .setTextColor(Color.WHITE.normal)
                .setSize(40, 18)
                .setPos(100, 36)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));

        return builder.build();
    }

    protected static Textures.BlockIcons.CustomIcon ActiveFace;
    protected static Textures.BlockIcons.CustomIcon InactiveFace;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ActiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Per_on");
        InactiveFace = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_ControlCore_Per_off");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[8][12],
                new TT_RenderedExtendedFacingTexture(active ? ActiveFace : InactiveFace) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[8][12] };
    }

}
