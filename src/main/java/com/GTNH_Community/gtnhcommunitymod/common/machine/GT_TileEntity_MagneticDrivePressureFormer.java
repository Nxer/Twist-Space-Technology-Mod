package com.GTNH_Community.gtnhcommunitymod.common.machine;

import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.BLUE_PRINT_INFO;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.ModName;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.StructureTooComplex;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_00;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_01;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_02;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_03;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_04;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_05;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_06;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_07;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_08;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_09;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_EnergyHatch;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_Hatches;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.Tooltip_MagneticDrivePressureFormer_MachineType;
import static com.GTNH_Community.gtnhcommunitymod.util.TextLocalization.textTopCenter;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.GTNH_Community.gtnhcommunitymod.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_TileEntity_MagneticDrivePressureFormer
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_MagneticDrivePressureFormer>
    implements IConstructable, ISurvivalConstructable {

    // region Class Constructor
    public GT_TileEntity_MagneticDrivePressureFormer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MagneticDrivePressureFormer(String aName) {
        super(aName);
    }

    // endregion

    // region Member Variables

    /**
     * Use to check recipe in mode.
     * <li>0 = Extruder
     * <li>1 = Bending Machine
     * <li>2 = Forming Press
     * <li>3 = Forge Hammer
     */
    public byte mode = 0;
    public byte glassTier;
    public boolean signHadEnabledPerfectOverclock = false;
    public HeatingCoilLevel coilLevel;

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    // endregion

    // region Processing Logic
    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus((float) ((mode == 0 ? (1.0 / 8.0) : (1.0 / 16.0)) / (1 + coilLevel.getTier())));
                setPerfectOverclock(isPerfectOverclock());
                return super.process();
            }

            public ProcessingLogic setPerfectOverclock(boolean isPerfectOverclock) {
                if (isPerfectOverclock) {
                    return this.setOverclock(2, 2);
                }
                return this.setOverclock(1, 2);
            }

            @Override
            public ProcessingLogic setCalculatedEut(long calculatedEut) {
                this.calculatedEut = calculatedEut * 3 / 4;
                return this;
            }

        }.setMaxParallel(1024);

    }

    public boolean isPerfectOverclock() {
        boolean sign = false;

        if (mode != 0) {
            // Bending & Forming Press mode
            sign = true;
        } else if (this.coilLevel.getLevel() >= 14) {
            // Extruder Mode
            // Enable Perfect overclock when Infinity Coil
            sign = true;
        }
        this.signHadEnabledPerfectOverclock = sign;
        return sign;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.glassTier = 0;
        this.coilLevel = HeatingCoilLevel.None;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }
        // Infinity Glass enable Laser Energy Hatch
        if (this.glassTier < 11) {
            for (GT_MetaTileEntity_Hatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() == GT_MetaTileEntity_Hatch.ConnectionType.LASER) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        switch (mode) {
            case 1:
                return GT_Recipe.GT_Recipe_Map.sBenderRecipes;
            case 2:
                return GT_Recipe.GT_Recipe_Map.sPressRecipes;
            case 3:
                return GT_Recipe.GT_Recipe_Map.sHammerRecipes;
            default:
                return GT_Recipe.GT_Recipe_Map.sExtruderRecipes;
        }
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 4);

            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("MagneticDrivePressureFormer.modeMsg." + this.mode));
        }
    }

    // endregion

    // region Structure
    // spotless:off
    private final int horizontalOffSet = 7;
    private final int verticalOffSet = 7;
    private final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final String[][] shape = new String[][]{
        {"     DDDDD     ","       D       ","       D       ","       D       ","     DDDDD     ","       D       ","       D       ","       D       ","     DDDDD     ","       D       ","       D       ","       D       ","       D       ","       D       ","       D       ","       D       ","     DDDDD     ","       D       ","       D       ","       D       ","     DDDDD     ","       D       ","       D       ","       D       ","     DDDDD     "},
        {"   DDFFDFFDD   ","     FFFFF     ","     FFFFF     ","     FFFFF     ","   DDFFFFFDD   ","     AAAAA     ","     AAAAA     ","     AAAAA     ","   DDAAAAADD   ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","   DDAAAAADD   ","     AAAAA     ","     AAAAA     ","     AAAAA     ","   DDFFFFFDD   ","     FFFFF     ","     FFFFF     ","     FFFFF     ","   DDFFDFFDD   "},
        {"  DFFFFDFFFFD  ","   FFBBBBBFF   ","   FFBBBBBFF   ","   FFBBBBBFF   ","  DFF     FFD  ","   AA     AA   ","   AA     AA   ","   AA     AA   ","  DAA     AAD  ","   AA     AA   ","   AA     AA   ","   AA     AA   ","   AA     AA   ","   AA     AA   ","   AA     AA   ","   AA     AA   ","  DAA     AAD  ","   AA     AA   ","   AA     AA   ","   AA     AA   ","  DFF     FFD  ","   FFBBBBBFF   ","   FFBBBBBFF   ","   FFBBBBBFF   ","  DFFFFDFFFFD  "},
        {" DFFFFFDFFFFFD ","  FBBBBBBBBBF  ","  FBBBBBBBBBF  ","  FBBBBBBBBBF  "," DF         FD ","  A         A  ","  A         A  ","  A         A  "," DA         AD ","  A         A  ","  A         A  ","  A         A  ","  A         A  ","  A         A  ","  A         A  ","  A         A  "," DA         AD ","  A         A  ","  A         A  ","  A         A  "," DF         FD ","  FBBBBBBBBBF  ","  FBBBBBBBBBF  ","  FBBBBBBBBBF  "," DFFFFFDFFFFFD "},
        {" DFFFFFDFFFFFD ","  FBBB   BBBF  ","  FBBB   BBBF  ","  FBBB   BBBF  "," DF         FD ","  A         A  ","  A         A  ","  A         A  "," DA   EEE   AD ","  A         A  ","  A   EEE   A  ","  A         A  ","  A         A  ","  A         A  ","  A   EEE   A  ","  A         A  "," DA   EEE   AD ","  A         A  ","  A         A  ","  A         A  "," DF         FD ","  FBBB   BBBF  ","  FBBB   BBBF  ","  FBBB   BBBF  "," DFFFFFDFFFFFD "},
        {"DFFFFFDDDFFFFFD"," FBBB     BBBF "," FBBB     BBBF "," FBBB     BBBF ","DF           FD"," A           A "," A           A "," A           A ","DA   E   E   AD"," A           A "," A   E   E   A "," A           A "," A           A "," A           A "," A   E   E   A "," A           A ","DA   E   E   AD"," A           A "," A           A "," A           A ","DF           FD"," FBBB     BBBF "," FBBB     BBBF "," FBBB     BBBF ","DFFFFFDDDFFFFFD"},
        {"DFFFFDDDDDFFFFD"," FBB   C   BBF "," FBB   C   BBF "," FBB   C   BBF ","DF     C     FD"," A     C     A "," A     C     A "," A     C     A ","DA  E  C  E  AD"," A     C     A "," A  E FFF E  A "," A           A "," A           A "," A           A "," A  E FFF E  A "," A     C     A ","DA  E  C  E  AD"," A     C     A "," A     C     A "," A     C     A ","DF     C     FD"," FBB   C   BBF "," FBB   C   BBF "," FBB   C   BBF ","DFFFFDDDDDFFFFD"},
        {"DDDDDDD~DDDDDDD","DFBB  CCC  BBFD","DFBB  CCC  BBFD","DFBB  CCC  BBFD","DF    CCC    FD","DA    CCC    AD","DA    CCC    AD","DA    CCC    AD","DA  E CCC E  AD","DA    CCC    AD","DA  E FFF E  AD","DA           AD","DA           AD","DA           AD","DA  E FFF E  AD","DA    CCC    AD","DA  E CCC E  AD","DA    CCC    AD","DA    CCC    AD","DA    CCC    AD","DF    CCC    FD","DFBB  CCC  BBFD","DFBB  CCC  BBFD","DFBB  CCC  BBFD","DDDDDDDFDDDDDDD"},
        {"DFFFFDDDDDFFFFD"," FBB   C   BBF "," FBB   C   BBF "," FBB   C   BBF ","DF     C     FD"," A     C     A "," A     C     A "," A     C     A ","DA  E  C  E  AD"," A     C     A "," A  E FFF E  A "," A           A "," A           A "," A           A "," A  E FFF E  A "," A     C     A ","DA  E  C  E  AD"," A     C     A "," A     C     A "," A     C     A ","DF     C     FD"," FBB   C   BBF "," FBB   C   BBF "," FBB   C   BBF ","DFFFFDDDDDFFFFD"},
        {"DFFFFFDDDFFFFFD"," FBBB     BBBF "," FBBB     BBBF "," FBBB     BBBF ","DF           FD"," A           A "," A           A "," A           A ","DA   E   E   AD"," A           A "," A   E   E   A "," A           A "," A           A "," A           A "," A   E   E   A "," A           A ","DA   E   E   AD"," A           A "," A           A "," A           A ","DF           FD"," FBBB     BBBF "," FBBB     BBBF "," FBBB     BBBF ","DFFFFFDDDFFFFFD"},
        {" DFFFFFDFFFFFD ","  FBBB   BBBF  ","  FBBB   BBBF  ","  FBBB   BBBF  "," DF         FD ","  A         A  ","  A         A  ","  A         A  "," DA   EEE   AD ","  A         A  ","  A   EEE   A  ","  A         A  ","  A         A  ","  A         A  ","  A   EEE   A  ","  A         A  "," DA   EEE   AD ","  A         A  ","  A         A  ","  A         A  "," DF         FD ","  FBBB   BBBF  ","  FBBB   BBBF  ","  FBBB   BBBF  "," DFFFFFDFFFFFD "},
        {" DFFFFFDFFFFFD ","  FBBBBBBBBBF  ","  FBBBBBBBBBF  ","  FBBBBBBBBBF  "," DF         FD ","  A         A  ","  A         A  ","  A         A  "," DA         AD ","  A         A  ","  A         A  ","  A         A  ","  A         A  ","  A         A  ","  A         A  ","  A         A  "," DA         AD ","  A         A  ","  A         A  ","  A         A  "," DF         FD ","  FBBBBBBBBBF  ","  FBBBBBBBBBF  ","  FBBBBBBBBBF  "," DFFFFFDFFFFFD "},
        {"  DFFFFDFFFFD  ","   FFBBBBBFF   ","   FFBBBBBFF   ","   FFBBBBBFF   ","  DFF     FFD  ","   AA     AA   ","   AA     AA   ","   AA     AA   ","  DAA     AAD  ","   AA     AA   ","   AA     AA   ","   AA     AA   ","   AA     AA   ","   AA     AA   ","   AA     AA   ","   AA     AA   ","  DAA     AAD  ","   AA     AA   ","   AA     AA   ","   AA     AA   ","  DFF     FFD  ","   FFBBBBBFF   ","   FFBBBBBFF   ","   FFBBBBBFF   ","  DFFFFDFFFFD  "},
        {"   DDFFDFFDD   ","     FFFFF     ","     FFFFF     ","     FFFFF     ","   DDFFFFFDD   ","     AAAAA     ","     AAAAA     ","     AAAAA     ","   DDAAAAADD   ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","     AAAAA     ","   DDAAAAADD   ","     AAAAA     ","     AAAAA     ","     AAAAA     ","   DDFFFFFDD   ","     FFFFF     ","     FFFFF     ","     FFFFF     ","   DDFFDFFDD   "},
        {"     DDDDD     ","       D       ","       D       ","       D       ","     DDDDD     ","       D       ","       D       ","       D       ","     DDDDD     ","       D       ","       D       ","       D       ","       D       ","       D       ","       D       ","       D       ","     DDDDD     ","       D       ","       D       ","       D       ","     DDDDD     ","       D       ","       D       ","       D       ","     DDDDD     "}
    };
    
    /*
    Blocks:
        A -> ofBlock...(BW_GlasBlocks, 14, ...);    // glass
        B -> ofBlock...(compactFusionCoil, 0, ...);
        C -> ofBlock...(gt.blockcasings2, 5, ...);
        D -> ofBlock...(gt.blockcasings4, 14, ...); // Hatches
        E -> ofBlock...(gt.blockcasings5, 13, ...); // Coil
        F -> ofBlock...(gt.blockcasings8, 7, ...);  // Energy Hatch
     */
    @Override
    public IStructureDefinition<GT_TileEntity_MagneticDrivePressureFormer> getStructureDefinition() {
        IStructureDefinition<GT_TileEntity_MagneticDrivePressureFormer> structure = StructureDefinition
            .<GT_TileEntity_MagneticDrivePressureFormer>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            .addElement(
                'A',
                withChannel("glass",
                    BorosilicateGlass.ofBoroGlass(
                        (byte) 0,
                        (byte) 1,
                        Byte.MAX_VALUE,
                        (te, t) -> te.glassTier = t,
                        te -> te.glassTier
                    ))
            )
            .addElement('B', ofBlock(compactFusionCoil,0))
            .addElement('C', ofBlock(GregTech_API.sBlockCasings2, 5))
            .addElement(
                'D',
                GT_HatchElementBuilder.<GT_TileEntity_MagneticDrivePressureFormer>builder()
                    .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance)
                    .adder(GT_TileEntity_MagneticDrivePressureFormer::addToMachineList)
                    .dot(1)
                    .casingIndex(62)
                    .buildAndChain(GregTech_API.sBlockCasings4, 14))
            .addElement(
                'E',
                withChannel("coil",
                ofCoil(
                    GT_TileEntity_MagneticDrivePressureFormer::setCoilLevel,
                    GT_TileEntity_MagneticDrivePressureFormer::getCoilLevel)))
            .addElement(
                'F',
                GT_HatchElementBuilder.<GT_TileEntity_MagneticDrivePressureFormer>builder()
                    .atLeast(Energy.or(ExoticEnergy))
                    .adder(GT_TileEntity_MagneticDrivePressureFormer::addToMachineList)
                    .dot(2)
                    .casingIndex(183)
                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
            .build();
            
        return structure;
    }
    
    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }
    
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }
    
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
            false,
            true);
    }
    
    // spotless:on

    // endregion

    // region Overrides

    // Scanner Info
    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length - 2] = EnumChatFormatting.AQUA + "Glass Tier: " + EnumChatFormatting.GOLD + this.glassTier;
        ret[origin.length - 1] = EnumChatFormatting.AQUA + "Coil Level: "
            + EnumChatFormatting.GOLD
            + this.coilLevel.getLevel();
        ret[origin.length] = EnumChatFormatting.AQUA + "Enabled Perfect Overclock: "
            + EnumChatFormatting.GOLD
            + this.signHadEnabledPerfectOverclock;
        return ret;
    }

    // tooltips
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(Tooltip_MagneticDrivePressureFormer_MachineType)
            .addInfo(Tooltip_MagneticDrivePressureFormer_00)
            .addInfo(Tooltip_MagneticDrivePressureFormer_01)
            .addInfo(Tooltip_MagneticDrivePressureFormer_02)
            .addInfo(Tooltip_MagneticDrivePressureFormer_03)
            .addInfo(Tooltip_MagneticDrivePressureFormer_04)
            .addInfo(Tooltip_MagneticDrivePressureFormer_05)
            .addInfo(Tooltip_MagneticDrivePressureFormer_06)
            .addSeparator()
            .addInfo(Tooltip_MagneticDrivePressureFormer_09)
            .addInfo(Tooltip_MagneticDrivePressureFormer_07)
            .addInfo(Tooltip_MagneticDrivePressureFormer_08)
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(15, 25, 15, false)
            .addController(textTopCenter)
            .addInputHatch(Tooltip_MagneticDrivePressureFormer_Hatches, 1)
            .addOutputHatch(Tooltip_MagneticDrivePressureFormer_Hatches, 1)
            .addInputBus(Tooltip_MagneticDrivePressureFormer_Hatches, 1)
            .addOutputBus(Tooltip_MagneticDrivePressureFormer_Hatches, 1)
            .addMaintenanceHatch(Tooltip_MagneticDrivePressureFormer_Hatches, 1)
            .addEnergyHatch(Tooltip_MagneticDrivePressureFormer_EnergyHatch, 2)
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MagneticDrivePressureFormer(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(62) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setByte("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mode = aNBT.getByte("mode");
    }

    // endregion
}
