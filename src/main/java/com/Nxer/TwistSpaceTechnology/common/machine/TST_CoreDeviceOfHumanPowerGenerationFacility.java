package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.Nxer.TwistSpaceTechnology.util.TextLocalization;
import com.Nxer.TwistSpaceTechnology.util.enums.TierName;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class TST_CoreDeviceOfHumanPowerGenerationFacility
    extends GTCM_MultiMachineBase<TST_CoreDeviceOfHumanPowerGenerationFacility> {

    // region Class Constructor
    public TST_CoreDeviceOfHumanPowerGenerationFacility(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_CoreDeviceOfHumanPowerGenerationFacility(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_CoreDeviceOfHumanPowerGenerationFacility(this.mName);
    }

    // endregion

    // region Processing Logic
    private float speedBonus = 1;
    private byte glassTier = 0;
    private String glassTierName = "NONE";
    private HeatingCoilLevel coilLevel;

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + TextEnums.GlassTier.toString()
            + ": "
            + EnumChatFormatting.GOLD
            + this.glassTier
            + EnumChatFormatting.RESET
            + " --> "
            + EnumChatFormatting.GOLD
            + this.glassTierName;
        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setFloat("speedBonus", speedBonus);
        aNBT.setByte("glassTier", glassTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        speedBonus = aNBT.getFloat("speedBonus");
        glassTier = aNBT.getByte("glassTier");
    }

    public int getCoilTier() {
        return 1 + coilLevel.getTier();
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidHeaterRecipes;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    protected float getSpeedBonus() {
        return speedBonus;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        glassTier = 0;
        coilLevel = HeatingCoilLevel.None;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (glassTier == 0 || coilLevel == HeatingCoilLevel.None) return false;
        if (glassTier < 12) {
            for (GT_MetaTileEntity_Hatch hatch : this.mExoticEnergyHatches) {
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
        }

        speedBonus = 1F / getCoilTier();
        glassTierName = TierName.getVoltageName(glassTier);
        return true;
    }
    // endregion

    // region Structure
    // spotless:off
    private static final int horizontalOffSet = 7;
    private static final int verticalOffSet = 18;
    private static final int depthOffSet = 0;
    private static final String STRUCTURE_PIECE_MAIN = "mainCoreDeviceOfHumanPowerGenerationFacility";
    private static IStructureDefinition<TST_CoreDeviceOfHumanPowerGenerationFacility> STRUCTURE_DEFINITION = null;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<TST_CoreDeviceOfHumanPowerGenerationFacility> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION =
                StructureDefinition
                    .<TST_CoreDeviceOfHumanPowerGenerationFacility>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                                  {"               ","               ","               ","               ","               ","      FFF      ","     FFFFF     ","     FFFFF     ","     FFFFF     ","      FFF      ","               ","               ","               ","               ","               "},
                                  {"               ","      BBB      ","    BBBBBBB    ","   BBBBBBBBB   ","  BBBBBBBBBBB  ","  BBBBFFFBBBB  "," BBBBFFFFFBBBB "," BBBBFFDFFBBBB "," BBBBFFFFFBBBB ","  BBBBFFFBBBB  ","  BBBBBBBBBBB  ","   BBBBBBBBB   ","    BBBBBBB    ","      BBB      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A         A  "," A           A "," A     D     A "," A           A ","  A         A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AAEEEAA    ","   AEE   EEA   ","  AE       EA  ","  AE       EA  "," AE         EA "," AE GGGDGGG EA "," AE         EA ","  AE       EA  ","  AE       EA  ","   AEE   EEA   ","    AAEEEAA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A         A  "," A           A "," A  GGGDGGG  A "," A           A ","  A         A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A      G  A  "," A      G    A "," A     D     A "," A    G      A ","  A  G      A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A      G  A  "," A      G    A "," A     D     A "," A    G      A ","  A  G      A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AAEEEAA    ","   AEE   EEA   ","  AE   G   EA  ","  AE   G   EA  "," AE    G    EA "," AE    D    EA "," AE    G    EA ","  AE   G   EA  ","  AE   G   EA  ","   AEE   EEA   ","    AAEEEAA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A    G    A  ","  A    G    A  "," A     G     A "," A     D     A "," A     G     A ","  A    G    A  ","  A    G    A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A  G      A  "," A    G      A "," A     D     A "," A      G    A ","  A      G  A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A  G      A  "," A    G      A "," A     D     A "," A      G    A ","  A      G  A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AAEEEAA    ","   AEE   EEA   ","  AE       EA  ","  AE       EA  "," AE         EA "," AE GGGDGGG EA "," AE         EA ","  AE       EA  ","  AE       EA  ","   AEE   EEA   ","    AAEEEAA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A         A  "," A           A "," A  GGGDGGG  A "," A           A ","  A         A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A      G  A  "," A      G    A "," A     D     A "," A    G      A ","  A  G      A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A         A  ","  A      G  A  "," A      G    A "," A     D     A "," A    G      A ","  A  G      A  ","  A         A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AAEEEAA    ","   AEE   EEA   ","  AE   G   EA  ","  AE   G   EA  "," AE    G    EA "," AE    D    EA "," AE    G    EA ","  AE   G   EA  ","  AE   G   EA  ","   AEE   EEA   ","    AAEEEAA    ","      AAA      ","               "},
                                  {"               ","      AAA      ","    AA   AA    ","   A       A   ","  A    G    A  ","  A    G    A  "," A     G     A "," A     D     A "," A     G     A ","  A    G    A  ","  A    G    A  ","   A       A   ","    AA   AA    ","      AAA      ","               "},
                                  {"               ","      BBB      ","    BBBBBBB    ","   BBBBBBBBB   ","  BBBBBBBBBBB  ","  BBBBFFFBBBB  "," BBBBFFFFFBBBB "," BBBBFFDFFBBBB "," BBBBFFFFFBBBB ","  BBBBFFFBBBB  ","  BBBBBBBBBBB  ","   BBBBBBBBB   ","    BBBBBBB    ","      BBB      ","               "},
                                  {"     CC~CC     ","   CCCCCCCCC   ","  CCCCCCCCCCC  "," CCCCCCCCCCCCC "," CCCCCCCCCCCCC ","CCCCCCFFFCCCCCC","CCCCCFFFFFCCCCC","CCCCCFFFFFCCCCC","CCCCCFFFFFCCCCC","CCCCCCFFFCCCCCC"," CCCCCCCCCCCCC "," CCCCCCCCCCCCC ","  CCCCCCCCCCC  ","   CCCCCCCCC   ","     CCCCC     "},
                                  {"     CCCCC     ","   CCCCCCCCC   ","  CCCCCCCCCCC  "," CCCCCCCCCCCCC "," CCCCCCCCCCCCC ","CCCCCCFFFCCCCCC","CCCCCFFFFFCCCCC","CCCCCFFFFFCCCCC","CCCCCFFFFFCCCCC","CCCCCCFFFCCCCCC"," CCCCCCCCCCCCC "," CCCCCCCCCCCCC ","  CCCCCCCCCCC  ","   CCCCCCCCC   ","     CCCCC     "}
                              })
                    )
                    .addElement(
                        'A', // BW Glasses
                        withChannel("glass",
                                    BorosilicateGlass.ofBoroGlass(
                                        (byte) 0,
                                        (byte) 1,
                                        Byte.MAX_VALUE,
                                        (te, t) -> te.glassTier = t,
                                        te -> te.glassTier
                                    ))
                    )
                    .addElement(
                        'B', // gt.blockcasings, 11 : Fluid IO Hatches
                        GT_HatchElementBuilder
                            .<TST_CoreDeviceOfHumanPowerGenerationFacility>builder()
                            .atLeast(InputHatch, OutputHatch)
                            .adder(TST_CoreDeviceOfHumanPowerGenerationFacility::addToMachineList)
                            .dot(1)
                            .casingIndex(11)
                            .buildAndChain(GregTech_API.sBlockCasings1, 11)
                    )
                    .addElement(
                        'C', // gt.blockcasings2, 0 : Item IO Buses
                        GT_HatchElementBuilder
                            .<TST_CoreDeviceOfHumanPowerGenerationFacility>builder()
                            .atLeast(InputBus, OutputBus)
                            .adder(TST_CoreDeviceOfHumanPowerGenerationFacility::addToMachineList)
                            .dot(2)
                            .casingIndex(16)
                            .buildAndChain(GregTech_API.sBlockCasings2, 0)
                    )
                    .addElement('D', ofBlock(GregTech_API.sBlockCasings2, 4))
                    .addElement(
                        'E', // coils
                        withChannel(
                            "coil",
                            ofCoil(
                                TST_CoreDeviceOfHumanPowerGenerationFacility::setCoilLevel,
                                TST_CoreDeviceOfHumanPowerGenerationFacility::getCoilLevel
                            )
                        )
                    )
                    .addElement(
                        'F', // gt.blockcasings8, 7 : Energy Hatches
                        GT_HatchElementBuilder
                            .<TST_CoreDeviceOfHumanPowerGenerationFacility>builder()
                            .atLeast(Energy.or(ExoticEnergy))
                            .adder(TST_CoreDeviceOfHumanPowerGenerationFacility::addToMachineList)
                            .dot(3)
                            .casingIndex(183)
                            .buildAndChain(GregTech_API.sBlockCasings8, 7)
                    )
                    .addElement('G', ofFrame(Materials.Osmiridium))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }
    // spotless:on
    // endregion

    // region General
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        // #tr Tooltip_CoreDeviceOfHumanPowerGenerationFacility_MachineType
        // # Fluid Heater
        // #zh_CN 流体加热器
        tt.addMachineType(TextEnums.tr("Tooltip_CoreDeviceOfHumanPowerGenerationFacility_MachineType"))
            // #tr Tooltip_CoreDeviceOfHumanPowerGenerationFacility_Controller
            // # Controller block for the Core Device of Human Power Generation Facility
            // #zh_CN 人类能源设施的核心装置的控制器方块
            .addInfo(TextEnums.tr("Tooltip_CoreDeviceOfHumanPowerGenerationFacility_Controller"))
            // #tr Tooltip_CoreDeviceOfHumanPowerGenerationFacility_01
            // # {\RED}The use of "The Fast-Heater" is prohibited in the dormitories.
            // #zh_CN {\RED}禁止在宿舍使用 "热得快"。
            .addInfo(TextEnums.tr("Tooltip_CoreDeviceOfHumanPowerGenerationFacility_01"))
            // #tr Tooltip_CoreDeviceOfHumanPowerGenerationFacility_02
            // # Upgrade coils for faster speeds.
            // #zh_CN 升级线圈以获得更快的速度.
            .addInfo(TextEnums.tr("Tooltip_CoreDeviceOfHumanPowerGenerationFacility_02"))
            .addInfo(TextLocalization.Tooltip_GlassTierLimitEnergyHatchTier)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(15, 20, 15, false)
            .addInputHatch(TextLocalization.textUseBlueprint, 1)
            .addOutputHatch(TextLocalization.textUseBlueprint, 1)
            .addInputBus(TextLocalization.textUseBlueprint, 2)
            .addOutputBus(TextLocalization.textUseBlueprint, 2)
            .addEnergyHatch(TextLocalization.textUseBlueprint, 3)
            .toolTipFinisher(TextLocalization.ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][16] };
    }
}
