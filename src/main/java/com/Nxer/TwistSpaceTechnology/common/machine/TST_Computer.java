package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WirelessUpdateItem;
import static com.Nxer.TwistSpaceTechnology.common.machine.TST_Computer.ComputerHatches.ComputationMonitor;
import static com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs.SimpleStructureErrors.special_hatch_amount_wrong;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.loader.Loaders.FRF_Coil_1;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static goodgenerator.loader.Loaders.radiationProtectionSteelFrame;
import static gregtech.api.GregTechAPI.sBlockCasingsSE;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static gtPlusPlus.core.block.ModBlocks.blockCasings3Misc;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.casing.BlockGTCasingsTT.textureOffset;
import static tectech.thing.casing.BlockGTCasingsTT.texturePage;
import static tectech.thing.casing.TTCasingsContainer.sBlockCasingsTT;
import static tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_NEUTRAL;
import static tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.OutputData;
import static tectech.util.CommonValues.MULTI_CHECK_AT;
import static vazkii.botania.common.block.ModBlocks.pylon;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.TT_MultiMachineBase_EM;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_RackComputationMonitor;
import com.Nxer.TwistSpaceTechnology.system.WirelessDataNetWork.WirelessDataPacket;
import com.Nxer.TwistSpaceTechnology.util.text.ID;
import com.Nxer.TwistSpaceTechnology.util.text.TSTMultiblockTooltipBuilder;
import com.Nxer.TwistSpaceTechnology.util.text.TSTTooltipCredit;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity.SkipGenerateDescription;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.thing.block.BlockQuantumGlass;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

@SkipGenerateDescription
public class TST_Computer extends TT_MultiMachineBase_EM implements ISurvivalConstructable, TSTTooltipCredit {

    // region Class Constructor
    public TST_Computer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        registerTooltipCredits(ID.SHORDINGER);
        eCertainMode = 0;
        useLongPower = true;
    }

    public TST_Computer(String aName) {
        super(aName);
        eCertainMode = 0;
        useLongPower = true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_Computer(mName);
    }
    // endregion

    // region Structure
    // spotless:off
    public static final String[][] shape = new String[][]{
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      III                      ","                      III                      ","                      III                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     IIIII                     ","                     IIIII                     ","                     IIIII                     ","                     IIIII                     ","                     IIIII                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      III                      ","                     IIIII                     ","                    IIIIIII                    ","                    IIIIIII                    ","                    IIIIIII                    ","                     IIIII                     ","                      III                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     MMMMM                     ","                    M     M                    ","                   M  III  M                   ","                  M  IIIII  M                  ","                  M IIIIIII M                  ","                  M IIIIIII M                  ","                  M IIIIIII M                  ","                  M  IIIII  M                  ","                   M  III  M                   ","                    M     M                    ","                     MMMMM                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     MMMMM                     ","                    M     M                    ","                   M  III  M                   ","                  M  IIIII  M                  ","                  M IIIIIII M                  ","                  M IIIIIII M                  ","                  M IIIIIII M                  ","                  M  IIIII  M                  ","                   M  III  M                   ","                    M     M                    ","                     MMMMM                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","                    MM   MM                    ","                   M       M                   ","                   M IIIII M                   ","                  M  IIIII  M                  ","                  M  IIIII  M                  ","                  M  IIIII  M                  ","                   M IIIII M                   ","                   M       M                   ","                    MM   MM                    ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     MMMMM                     ","                    M     M                    ","                   M       M                   ","                   M  III  M                   ","                   M  III  M                   ","                   M  III  M                   ","                   M       M                   ","                    M     M                    ","                     MMMMM                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     MMMMM                     ","                    MM   MM                    ","                    M     M                    ","                    M     M                    ","                    M     M                    ","                    MM   MM                    ","                     MMMMM                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","                     MMMMM                     ","                     MMMMM                     ","                     MMMMM                     ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MDM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MDM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MDM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MDM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MDM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MDM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MDM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","                      MDM                      ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","      JJJ                             JJJ      ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","                      MDM                      ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","      JJJ                             JJJ      ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","                      MDM                      ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","                     M   M                     ","                     M D M                     ","                     M   M                     ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","                     M   M                     ","                     M D M                     ","                     M   M                     ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","     JJJJJ                           JJJJJ     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                       M                       ","                                               ","                                               ","      JJJ                             JJJ      ","      JJJ                             JJJ      ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","                     M   M                     ","   M                 M D M                 M   ","                     M   M                     ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","      JJJ                             JJJ      ","      JJJ                             JJJ      ","                                               ","                                               ","                       M                       ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                       M                       ","                     MM MM                     ","                    M     M                    ","                                               ","       A                               A       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","     M                                   M     ","    M                 MMM                 M    ","    M                M   M                M    ","   M                 M D M                 M   ","    M                M   M                M    ","    M                 MMM                 M    ","     M                                   M     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","       A                               A       ","                                               ","                    M     M                    ","                     MM MM                     ","                       M                       ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                      MMM                      ","                    MM   MM                    ","      JJJ           M     M           JJJ      ","     J L J         M       M         J L J     ","     JLALJ                           JLALJ     ","     J L J                           J L J     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      M                                 M      ","    MM                                   MM    ","    M                 MMM                 M    ","   M                 M   M                 M   ","   M                 M D M                 M   ","   M                 M   M                 M   ","    M                 MMM                 M    ","    MM                                   MM    ","      M                                 M      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     J L J                           J L J     ","     JLALJ                           JLALJ     ","     J L J         M       M         J L J     ","      JJJ           M     M           JJJ      ","                    MM   MM                    ","                      MMM                      ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                     MMMMM                     ","                    M     M                    ","                   M       M                   ","                   M       M                   ","       A           M       M           A       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","     MMM                               MMM     ","    M                 MMM                 M    ","   M                 M   M                 M   ","   M                M     M                M   ","   M                M  D  M                M   ","   M                M     M                M   ","   M                 M   M                 M   ","    M                 MMM                 M    ","     MMM                               MMM     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","       A           M       M           A       ","                   M       M                   ","                   M       M                   ","                    M     M                    ","                     MMMMM                     ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                      MMM                      ","                    MM   MM                    ","      JJJ           M     M           JJJ      ","     J L J         M       M         J L J     ","     JLALJ         M   O   M         JLALJ     ","     J L J         M       M         J L J     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      MMM                             MMM      ","    MM                MMM                MM    ","    M                M   M                M    ","   M                M     M                M   ","   M   O            M  D  M            O   M   ","   M                M     M                M   ","    M                M   M                M    ","    MM                MMM                MM    ","      MMM                             MMM      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     J L J         M       M         J L J     ","     JLALJ         M   O   M         JLALJ     ","     J L J         M       M         J L J     ","      JJJ           M     M           JJJ      ","                    MM   MM                    ","                      MMM                      ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                       M                       ","                     MM MM                     ","                    M     M                    ","                    M     M                    ","       A           M       M           A       ","                    M     M                    ","                    M     M                    ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","       M                               M       ","     MM MM            MMM            MM MM     ","    M                M   M                M    ","    M               M     M               M    ","   M                M  D  M                M   ","    M               M     M               M    ","    M                M   M                M    ","     MM MM            MMM            MM MM     ","       M                               M       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                    M     M                    ","                    M     M                    ","       A           M       M           A       ","                    M     M                    ","                    M     M                    ","                     MM MM                     ","                       M                       ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                      MMM                      ","      JJJ            MM MM            JJJ      ","     J L J          MM   MM          J L J     ","     JLALJ          M     M          JLALJ     ","     J L J          MM   MM          J L J     ","      JJJ            MM MM            JJJ      ","                      MMM                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     MMMMM                     ","      MMM           M     M           MMM      ","     MM MM         M       M         MM MM     ","    MM   MM        M       M        MM   MM    ","    M     M        M   D   M        M     M    ","    MM   MM        M       M        MM   MM    ","     MM MM         M       M         MM MM     ","      MMM           M     M           MMM      ","                     MMMMM                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      MMM                      ","      JJJ            MM MM            JJJ      ","     J L J          MM   MM          J L J     ","     JLALJ          M     M          JLALJ     ","     J L J          MM   MM          J L J     ","      JJJ            MM MM            JJJ      ","                      MMM                      ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MMM                      ","       A             MMMMM             A       ","                      MMM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     MMMMM                     ","                    M     M                    ","       M           M       M           M       ","      MMM          M       M          MMM      ","     MMMMM         M   D   M         MMMMM     ","      MMM          M       M          MMM      ","       M           M       M           M       ","                    M     M                    ","                     MMMMM                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                       M                       ","                      MMM                      ","       A             MMMMM             A       ","                      MMM                      ","                       M                       ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     J L J                           J L J     ","     JLALJ             C             JLALJ     ","     J L J                           J L J     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     MMMMM                     ","                    M     M                    ","                   M       M                   ","                  M         M                  ","                  M         M                  ","       C          M    D    M          C       ","                  M         M                  ","                  M         M                  ","                   M       M                   ","                    M     M                    ","                     MMMMM                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","      JJJ                             JJJ      ","     J L J                           J L J     ","     JLALJ             C             JLALJ     ","     J L J                           J L J     ","      JJJ                             JJJ      ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","       A              CCC              A       ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                     MMMMM                     ","                   MM     MM                   ","                  M         M                  ","                  M         M                  ","                 M           M                 ","      CCC        M           M        CCC      ","      CCC        M     D     M        CCC      ","      CCC        M           M        CCC      ","                 M           M                 ","                  M         M                  ","                  M         M                  ","                   MM     MM                   ","                     MMMMM                     ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","       A              CCC              A       ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                     C   C                     ","       A             C C C             A       ","                     C   C                     ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                KKKKKKKKKKKKKKK                ","                KKKKKKKKKKKKKKK                ","                KKKKKKKKKKKKKKK                ","                KKKKKKKKKKKKKKK                ","                KKKKKKKKKKKKKKK                ","      CCC       KKKKKKKKKKKKKKK       CCC      ","     C   C      KKKKKKKKKKKKKKK      C   C     ","     C C C      KKKKKKKDKKKKKKK      C C C     ","     C   C      KKKKKKKKKKKKKKK      C   C     ","      CCC       KKKKKKKKKKKKKKK       CCC      ","                KKKKKKKKKKKKKKK                ","                KKKKKKKKKKKKKKK                ","                KKKKKKKKKKKKKKK                ","                KKKKKKKKKKKKKKK                ","                KKKKKKKKKKKKKKK                ","                                               ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","                     C   C                     ","       A             C C C             A       ","                     C   C                     ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                                               ","                      CCC                      ","      BBB            C   C            BBB      ","     B   B          C     C          B   B     ","     B A B          C  C  C          B A B     ","     B   B          C     C          B   B     ","      BBB            C   C            BBB      ","                      CCC                      ","                                               ","                                               ","                                               ","                                               ","                                               ","                KKKKKKKKKKKKKKK                ","                KIHHHHHHHHHHHIK                ","                KHIHHHHHHHHHIHK                ","                KHHIHHHHHHHIHHK                ","      CCC       KHHHIHHHHHIHHHK       CCC      ","     C   C      KHHHHIHHHIHHHHK      C   C     ","    C     C     KHHHHHIHIHHHHHK     C     C    ","    C  C  C     KHHHHHHDHHHHHHK     C  C  C    ","    C     C     KHHHHHIHIHHHHHK     C     C    ","     C   C      KHHHHIHHHIHHHHK      C   C     ","      CCC       KHHHIHHHHHIHHHK       CCC      ","                KHHIHHHHHHHIHHK                ","                KHIHHHHHHHHHIHK                ","                KIHHHHHHHHHHHIK                ","                KKKKKKKKKKKKKKK                ","                                               ","                                               ","                                               ","                                               ","                                               ","                      CCC                      ","      BBB            C   C            BBB      ","     B   B          C     C          B   B     ","     B A B          C  C  C          B A B     ","     B   B          C     C          B   B     ","      BBB            C   C            BBB      ","                      CCC                      ","                                               ","                                               ","                                               ","                                               "},
        {"                                               ","                                               ","                                               ","                     CCCCC                     ","    PPEEEPPPPPPPPPPPCCCCCCCPPPPPPPPPPPEEEPP    ","    PEEEEE         CCCCCCCCC         EEEEEP    ","    EEBBBEEPPPPPPPPCCCCCCCCCPPPPPPPPEEBBBEE    ","    EEBABEE       PCCCCCCCCCP       EEBABEE    ","    EEBBBEEPPPPPP PCCCCCCCCCP PPPPPPEEBBBEE    ","    PEEEEE      P PCCCCCCCCCP P      EEEEEP    ","    P EEE       P P CCCCCCC P P       EEE P    ","    P P P       P P PCCCCCP P P       P P P    ","    P P P       P P P P P P P P       P P P    ","    P P P       P P P P P P P P       P P P    ","    P P P       P P P P P P P P       P P P    ","    P P P       P P P P P P P P       P P P    ","    P P PPPPPPPPPKPKPKPKPKPKPKPPPPPPPPP P P    ","    P P         KHQQQQQQQQQQQHK         P P    ","    P PPPPPPPPPPPQHQQQQQQQQQHQPPPPPPPPPPP P    ","    PCCCCC      KQQHQQQQQQQHQQK      CCCCCP    ","    CCCCCCCPPPPPPQQQHQQQQQHQQQPPPPPPCCCCCCC    ","   CCCCCCCCC    KQQQQHQQQHQQQQK    CCCCCCCCC   ","   CCCCCCCCCPPPPPQQQQQHQHQQQQQPPPPPCCCCCCCCC   ","   CCCCCCCCC    KQQQQQQDQQQQQQK    CCCCCCCCC   ","   CCCCCCCCCPPPPPQQQQQHQHQQQQQPPPPPCCCCCCCCC   ","   CCCCCCCCC    KQQQQHQQQHQQQQK    CCCCCCCCC   ","    CCCCCCCPPPPPPQQQHQQQQQHQQQPPPPPPCCCCCCC    ","    PCCCCC      KQQHQQQQQQQHQQK      CCCCCP    ","    P PPPPPPPPPPPQHQQQQQQQQQHQPPPPPPPPPPP P    ","    P P         KHQQQQQQQQQQQHK         P P    ","    P P PPPPPPPPPKPKPKPKPKPKPKPPPPPPPPP P P    ","    P P P       P P P P P P P P       P P P    ","    P P P       P P P P P P P P       P P P    ","    P P P       P P P P P P P P       P P P    ","    P P P       P P P P P P P P       P P P    ","    P P P       P P PCCCCCP P P       P P P    ","    P EEE       P P CCCCCCC P P       EEE P    ","    PEEEEE      P PCCCCCCCCCP P      EEEEEP    ","    EEBBBEEPPPPPP PCCCCCCCCCP PPPPPPEEBBBEE    ","    EEBABEE       PCCCCCCCCCP       EEBABEE    ","    EEBBBEEPPPPPPPPCCCCCCCCCPPPPPPPPEEBBBEE    ","    PEEEEE         CCCCCCCCC         EEEEEP    ","    PPEEEPPPPPPPPPPPCCCCCCCPPPPPPPPPPPEEEPP    ","                     CCCCC                     ","                                               ","                                               ","                                               "},
        {"EEEEEEEEEEEEEEEEEEEEEEE~EEEEEEEEEEEEEEEEEEEEEEE","EFFFFFIFIFFFFFFFFFFFIFIFIFIFFFFFFFFFFFIFIFFFFFE","EFFFFFIFIFFFFFFFFFFFIFIFIFIFFFFFFFFFFFIFIFFFFFE","EFFGGGIGIGGGGGGGGGGGIGGGGGIGGGGGGGGGGGIGIGGGFFE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EIIIGGGGGGGIIIIIIIIGGGGGGGGGIIIIIIIIGGGGGGGIIIE","EFFGGGGGGGGGGGGGGGIGGGGGGGGGIGGGGGGGGGGGGGGGFFE","EIIIGGGGGGGIIIIIIGIGGGGGGGGGIGIIIIIIGGGGGGGIIIE","EFFGGGGGGGGGGGGGIGIGGGGGGGGGIGIGGGGGGGGGGGGGFFE","EFFGGGGGGGIIIIIGIGIGGGGGGGGGIGIGIIIIIGGGGGGGFFE","EFFGGGIGIGIGGGIGIGIGIGGGGGIGIGIGIGGGIGIGIGGGFFE","EFFGGGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGGGFFE","EFFGGGIGIGIGGGIGIGIGIGIGIGIGIGIGIGGGIGIGIGGGFFE","EFFGGGIGIGIIIIIGIGIGIGIGIGIGIGIGIIIIIGIGIGGGFFE","EFFGGGIGIGGGGGGGIGIGIGIGIGIGIGIGGGGGGGIGIGGGFFE","EFFGGGIGIIIIIIIIGGGGGGGGGGGGGGGIIIIIIIIGIGGGFFE","EFFGGGIGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGIGGGFFE","EFFGGGIIIIIIIIIIGGGGGGGGGGGGGGGIIIIIIIIIIGGGFFE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EIIIGGGGGGGIIIIIGGGGGGGGGGGGGGGIIIIIGGGGGGGIIIE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EIIGGGGGGGGGIIIIGGGGGGGGGGGGGGGIIIIGGGGGGGGGIIE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EIIGGGGGGGGGIIIIGGGGGGGGGGGGGGGIIIIGGGGGGGGGIIE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EIIIGGGGGGGIIIIIGGGGGGGGGGGGGGGIIIIIGGGGGGGIIIE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EFFGGGIIIIIIIIIIGGGGGGGGGGGGGGGIIIIIIIIIIGGGFFE","EFFGGGIGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGIGGGFFE","EFFGGGIGIIIIIIIIGGGGGGGGGGGGGGGIIIIIIIIGIGGGFFE","EFFGGGIGIGGGGGGGIGIGIGIGIGIGIGIGGGGGGGIGIGGGFFE","EFFGGGIGIGIIIIIGIGIGIGIGIGIGIGIGIIIIIGIGIGGGFFE","EFFGGGIGIGIGGGIGIGIGIGIGIGIGIGIGIGGGIGIGIGGGFFE","EFFGGGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGIGGGFFE","EFFGGGIGIGIGGGIGIGIGIGGGGGIGIGIGIGGGIGIGIGGGFFE","EFFGGGGGGGIIIIIGIGIGGGGGGGGGIGIGIIIIIGGGGGGGFFE","EFFGGGGGGGGGGGGGIGIGGGGGGGGGIGIGGGGGGGGGGGGGFFE","EIIIGGGGGGGIIIIIIGIGGGGGGGGGIGIIIIIIGGGGGGGIIIE","EFFGGGGGGGGGGGGGGGIGGGGGGGGGIGGGGGGGGGGGGGGGFFE","EIIIGGGGGGGIIIIIIIIGGGGGGGGGIIIIIIIIGGGGGGGIIIE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EFFGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGFFE","EFFGGGIGIGGGGGGGGGGGIGGGGGIGGGGGGGGGGGIGIGGGFFE","EFFFFFIFIFFFFFFFFFFFIFIFIFIFFFFFFFFFFFIFIFFFFFE","EFFFFFIFIFFFFFFFFFFFIFIFIFIFFFFFFFFFFFIFIFFFFFE","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"}
    };

    // Structure:
    // Blocks:

    // Tiles:
    // Special Tiles:
    // N -> ofSpecialTileAdder(thaumcraft.common.tiles.TileNode, ...); // You will probably want to change it to
    // something else
    // O -> ofSpecialTileAdder(vazkii.botania.common.block.tile.TilePylon, ...); // You will probably want to change it
    // to something else
    // P -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaPipeEntity, ...); // You will probably want to change
    // it to something else
    // Q -> ofSpecialTileAdder(gregtech.api.metatileentity.BaseMetaTileEntity, ...); // You will probably want to change
    // it to something else
    // spotless:on

    private static IStructureDefinition<TST_Computer> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<TST_Computer> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = IStructureDefinition.<TST_Computer>builder()
                .addShape(MAIN, transpose(shape))// FRF_Coil_1
                .addElement('A', ofBlock(FRF_Coil_1, 0))// A -> ofBlock...(FRF_Coil_1, 0, ...);
                .addElement('B', ofBlock(compactFusionCoil, 0))// B -> ofBlock...(compactFusionCoil, 2, ...);
                .addElement('C', ofBlock(sBlockCasingsSE, 0))// C -> ofBlock...(gt.blockcasingsSE, 0, ...);
                .addElement('D', ofBlock(sBlockCasingsSE, 1))// D -> ofBlock...(gt.blockcasingsSE, 1, ...);
                // .addElement('E', ofBlock(GregTechAPI.sBlockCasingsSE, 2))// E -> ofBlock...(gt.blockcasingsSE, 2,
                // ...);
                .addElement('F', ofBlock(sBlockCasingsTT, 0))// F -> ofBlock...(gt.blockcasingsTT, 0, ...);
                .addElement('G', ofBlock(sBlockCasingsTT, 1))// G -> ofBlock...(gt.blockcasingsTT, 1, ...);
                .addElement('H', ofBlock(sBlockCasingsTT, 2))// H -> ofBlock...(gt.blockcasingsTT, 2, ...);
                .addElement('I', ofBlock(sBlockCasingsTT, 3))// I -> ofBlock...(gt.blockcasingsTT, 3, ...);
                .addElement('J', ofBlock(sBlockCasingsTT, 7))// J -> ofBlock...(gt.blockcasingsTT, 7, ...);
                .addElement('L', ofBlock(radiationProtectionSteelFrame, 0)) // L ->
                // ofBlock...(radiationProtectionSteelFrame, 0,
                // ...);
                .addElement('K', ofBlock(blockCasings3Misc, 15)) // K -> ofBlock...(gtplusplus.blockcasings.3, 15, ...);
                .addElement('M', ofBlock(BlockQuantumGlass.INSTANCE, 0)) // M -> ofBlock...(tile.quantumGlass, 0, ...);
                .addElement('O', ofBlock(pylon, 1))
                // .addElement('N', ofBlock(Block.getBlockById(1), 0))
                .addElement('P', ofBlock(sBlockCasingsTT, 2))
                .addElement(
                    'E',
                    HatchElementBuilder.<TST_Computer>builder()
                        .atLeast(ComputationMonitor, OutputData, InputHatch, Energy.or(ExoticEnergy))
                        .adder(TST_Computer::superAddToMachineList)
                        .hint(1)
                        .casingIndex(textureOffset + 2)
                        .buildAndChain(ofBlock(sBlockCasingsSE, 2)))
                .addElement('Q', ofBlock(sBlockCasingsTT, 3))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(MAIN, stackSize, hintsOnly, offsetX, offsetY, offsetZ);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(MAIN, stackSize, offsetX, offsetY, offsetZ, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        realMonitor = null;
        // eRacks.clear();
        if (!checkPiece(MAIN, offsetX, offsetY, offsetZ, errors)) {
            return;
        }

        checkHasInputHatch(errors);
        if (realMonitor == null || eOutputData.isEmpty() || !eInputData.isEmpty()) {
            errors.add(special_hatch_amount_wrong);
        }

    }
    // endregion

    // region Processing Logic
    private GT_Hatch_RackComputationMonitor realMonitor;
    private double multiplier = 1;

    // GT_MetaTileEntity_AssemblyLine
    // GT_MetaTileEntity_EM_computer
    private static boolean localWirelessTag = false;

    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("tst.computer.hint.0"), // 1 - Classic/Data Hatches or
        // Computer casing
        translateToLocal("tst.computer.hint.1"), // 2 - Rack Hatches or Advanced
        // computer casing
    };

    private static Vec3Impl controllerPosition;
    public static final int offsetX = 23, offsetY = 34, offsetZ = 0;
    private static final String MAIN = "main";
    protected Parameters.Group.ParameterIn overclock, overvolt;
    protected Parameters.Group.ParameterOut maxCurrentTemp, availableData;

    private static final INameFunction<TST_Computer> OC_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.0");

    // Overclock ratio
    private static final INameFunction<TST_Computer> OV_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.1");

    // Overvoltage ratio
    private static final INameFunction<TST_Computer> MAX_TEMP_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.0");

    // Current max. heat
    private static final INameFunction<TST_Computer> COMPUTE_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.1");

    // Produced computation
    private static final IStatusFunction<TST_Computer> OC_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 1, 3);

    private static final IStatusFunction<TST_Computer> OV_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), .7, .8, 1.2, 2);

    private static final IStatusFunction<TST_Computer> MAX_TEMP_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), -10000, 0, 0, 5000);

    private static final IStatusFunction<TST_Computer> COMPUTE_STATUS = (base, p) -> {
        if (base.eAvailableData < 0) {
            return STATUS_TOO_LOW;
        }
        if (base.eAvailableData == 0) {
            return STATUS_NEUTRAL;
        }
        return STATUS_OK;
    };

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0);
        overclock = hatch_0.makeInParameter(0, 1, OC_NAME, OC_STATUS);
        overvolt = hatch_0.makeInParameter(1, 1, OV_NAME, OV_STATUS);
        maxCurrentTemp = hatch_0.makeOutParameter(0, 0, MAX_TEMP_NAME, MAX_TEMP_STATUS);
        availableData = hatch_0.makeOutParameter(1, 0, COMPUTE_NAME, COMPUTE_STATUS);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && localWirelessTag) {
            WirelessDataPacket.updatePacket(aBaseMetaTileEntity, aTick);
        }

    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 20 == 0 && aBaseMetaTileEntity.isServerSide()) {
            ItemStack stack = getControllerSlot();
            if (stack != null && stack.getItem() != null
                && stack.getItem()
                    .equals(WirelessUpdateItem.getItem())
                && stack.stackSize > 0) {
                if (WirelessDataPacket.enableWirelessNetWork(aBaseMetaTileEntity)) {
                    localWirelessTag = true;
                }
            } else {
                WirelessDataPacket.disableWirelessNetWork(aBaseMetaTileEntity);
                localWirelessTag = false;
            }
        }
        if (aBaseMetaTileEntity.isServerSide() && mMachine
            && !aBaseMetaTileEntity.isActive()
            && aTick % 20 == MULTI_CHECK_AT) {
            double maxTemp = 0;
            maxCurrentTemp.set(maxTemp);
        }
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        parametrization.setToDefaults(false, true);
        eAvailableData = 0;
        double maxTemp = 0;
        double overClockRatio = overclock.get();
        double overVoltageRatio = overvolt.get();
        if (Double.isNaN(overClockRatio) || Double.isNaN(overVoltageRatio)) {
            return SimpleCheckRecipeResult.ofFailure("no_computing");
        }
        if (overclock.getStatus(true).isOk && overvolt.getStatus(true).isOk) {
            float eut = GTValues.V[8] * (float) overVoltageRatio * (float) overClockRatio;
            if (eut < Integer.MAX_VALUE - 7) {
                lEUt = (long) -eut;
            } else {
                lEUt = -GTValues.V[8];
                return CheckRecipeResultRegistry.POWER_OVERFLOW;
            }
            long thingsActive = 0;
            long rackComputation;
            if (realMonitor != null) {
                if (realMonitor.heat > maxTemp) {
                    maxTemp = realMonitor.heat;
                }
                rackComputation = realMonitor.tickComponents((float) overClockRatio, (float) overVoltageRatio);
                realMonitor.heat = coolTheRackHatchByAnyCoolant(realMonitor.heat);
                realMonitor.postProcessAfterCoolant();
                rackComputation *= (long) (multiplier * multiplier);
                if (rackComputation > 0) {
                    eAvailableData += rackComputation;
                    thingsActive += (long) (4 * multiplier * multiplier);
                }
                realMonitor.getBaseMetaTileEntity()
                    .setActive(true);
            }

            for (MTEHatchDataInput di : eInputData) {
                if (di.q != null) {
                    thingsActive++;
                }
            }
            if (localWirelessTag) thingsActive *= 4;
            if (thingsActive > 0) {
                thingsActive += eOutputData.size();
                eAmpereFlow = 1 + (thingsActive >> 2);
                eAmpereFlow *= (long) (multiplier * multiplier);
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return SimpleCheckRecipeResult.ofSuccess("computing");
            } else {
                eAvailableData = 0;
                lEUt = -GTValues.V[8];
                eAmpereFlow = 1;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return SimpleCheckRecipeResult.ofSuccess("no_computing");
            }
        } else {
            // LOG.info("what?");
        }
        return SimpleCheckRecipeResult.ofFailure("no_computing");
    }

    public int coolTheRackHatchByAnyCoolant(int prevHeat) {
        FluidStack coolant = null;
        for (var input : filterValidMTEs(mInputHatches)) {
            FluidStack fluid = input.getFluid();
            if (fluid == null) continue;
            // LOG.info(fluid.getLocalizedName());
            if (coolant == null && validCoolant(fluid) > -1) coolant = fluid.copy();
            else if (coolant != null && coolant.getFluid() == fluid.getFluid()) coolant.amount += fluid.amount;
        }
        // LOG.info("coolant is null?" + (coolant == null));
        if (coolant == null || coolant.amount <= 0) {
            multiplier = 1.0;
            return prevHeat;
        }
        double maxHeatCanCool = validCoolant(coolant) * coolant.amount;
        multiplier = 1.0 + Math.log10(1.0 + maxHeatCanCool * prevHeat);
        long realHeatCanCool = (int) Math.min(maxHeatCanCool, (prevHeat - (prevHeat / (multiplier * multiplier))));
        long requiredAmount = (long) (realHeatCanCool / validCoolant(coolant));

        // FluidStack output = null;
        for (var input : filterValidMTEs(mInputHatches)) {
            FluidStack fluid = input.getFluid();
            if (fluid == null || fluid.amount < 1) continue;
            if (requiredAmount == 0) break;
            if (coolant.getFluid() == fluid.getFluid()) {
                int mx = (int) Math.min(requiredAmount, fluid.amount);
                fluid.amount -= mx;
                requiredAmount -= mx;

            }
        }

        return (int) (prevHeat - realHeatCanCool);
    }

    public static double validCoolant(FluidStack fluid) {
        if (fluid.getFluid() == FluidRegistry.getFluid("ic2coolant")) return 0.001;
        if (fluid.getFluid() == Materials.SuperCoolant.mFluid) return 0.01;
        if (fluid.getFluid() == Materials.Cryotheum.mFluid) return 0.1;
        return -100;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eOutputData.isEmpty()) {
            Vec3Impl pos = new Vec3Impl(
                getBaseMetaTileEntity().getXCoord(),
                getBaseMetaTileEntity().getYCoord(),
                getBaseMetaTileEntity().getZCoord());

            QuantumDataPacket pack = new QuantumDataPacket(eAvailableData / eOutputData.size()).unifyTraceWith(pos);
            if (pack == null) {
                return;
            }
            for (MTEHatchDataInput hatch : eInputData) {
                if (hatch.q == null || hatch.q.contains(pos)) {
                    continue;
                }
                pack = pack.unifyPacketWith(hatch.q);
                if (pack == null) {
                    return;
                }
            }

            for (MTEHatchDataOutput o : eOutputData) {
                o.q = pack;
            }
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        // for (GT_Hatch_RackComputationMonitor rack : filterValidMTEs(eRacks)) {
        // rack.getBaseMetaTileEntity()
        // .setActive(false);
        // }
        if (realMonitor != null) realMonitor.getBaseMetaTileEntity()
            .setActive(false);
    }

    @Override
    protected void extraExplosions_EM() {
        // for (MetaTileEntity tTileEntity : eRacks) {
        // tTileEntity.getBaseMetaTileEntity()
        // .doExplosion(V[9]);
        // }
        realMonitor.getBaseMetaTileEntity()
            .doExplosion(GTValues.V[9]);
    }

    @Override
    protected long getAvailableData_EM() {
        return eAvailableData;
    }

    @Override
    public void stopMachine() {
        // LOG.info("SOMETHING stop the machine");
        super.stopMachine();
        eAvailableData = 0;
        // for (GT_Hatch_RackComputationMonitor rack : filterValidMTEs(eRacks)) {
        // rack.getBaseMetaTileEntity()
        // .setActive(false);
        // }
        if (realMonitor != null) realMonitor.getBaseMetaTileEntity()
            .setActive(false);
    }

    @Override
    protected void afterRecipeCheckFailed() {
        super.afterRecipeCheckFailed();
        // for (GT_Hatch_RackComputationMonitor rack : filterValidMTEs(eRacks)) {
        // rack.getBaseMetaTileEntity()
        // .setActive(false);
        // }
        if (realMonitor != null) realMonitor.getBaseMetaTileEntity()
            .setActive(false);
    }

    // endregion

    // region NBT

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setDouble("computation", availableData.get());
        aNBT.setBoolean("localWirelessTag", localWirelessTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (availableData != null) {
            availableData.set(aNBT.getDouble("computation"));
            eAvailableData = (long) availableData.get();
        }
        localWirelessTag = aNBT.getBoolean("localWirelessTag");
    }

    // endregion

    // region Textures
    private static IIconContainer ScreenOFF;
    private static IIconContainer ScreenON;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = Textures.BlockIcons.custom("iconsets/EM_COMPUTER");
        ScreenON = Textures.BlockIcons.custom("iconsets/EM_COMPUTER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3],
                new TTRenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3] };
    }

    // endregion

    // region Tooltip

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new TSTMultiblockTooltipBuilder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.computer.name")) // Machine Type: Quantum
            // Computer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.0")) // Controller block of
            // the Quantum Computer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.computer.desc.1")) // Used to generate
            // computation (and heat)
            .addSeparator()
            // .addInfo("what the fuck")
            .addInfo(translateToLocal("tst.computer.desc.0"))
            .addInfo(translateToLocal("tst.computer.desc.1"))
            .addInfo(translateToLocal("tst.computer.desc.2"))
            .addInfo(translateToLocal("tst.computer.desc.3"))
            .addInfo(translateToLocal("tst.computer.desc.4"))
            .addInfo(translateToLocal("tst.computer.desc.5"))
            .addInfo(translateToLocal("tst.computer.desc.6"))
            .addInfo(translateToLocal("tst.computer.desc.7"))
            .addInfo(translateToLocal("tst.computer.desc.8"))
            .addInfo(translateToLocal("tst.computer.desc.9"))
            .addInfo(translateToLocal("tst.computer.desc.10"))
            .addInfo(translateToLocal("tst.computer.desc.11"))
            .addInfo(translateToLocal("tst.computer.desc.12"))
            // .beginVariableStructureBlock(2, 2, 4, 4, 5, 16, false)
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.certain.tier.07.name"),
                "no need uncertain hatch!",
                1)
            .toolTipFinisher();
        return tt;
    }

    // endregion

    // region Hatch Registration

    public final boolean superAddToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addRackComputationMonitorToMachine(aTileEntity, aBaseCasingIndex)
            || super.addToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addRackComputationMonitorToMachine(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aTileEntity.getMetaTileEntity() instanceof GT_Hatch_RackComputationMonitor monitor) {
            if (monitor.isMeanHatch()) {
                realMonitor = monitor;
                realMonitor.updateTexture(aBaseCasingIndex);
                return true;
            }
        }
        return false;
    }

    // endregion

    // region Nested Classes

    public enum ComputerHatches implements IHatchElement<TST_Computer> {

        ComputationMonitor;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_Hatch_RackComputationMonitor.class);
        }

        @Override
        public IGTHatchAdder<? super TST_Computer> adder() {
            return TST_Computer::addRackComputationMonitorToMachine;
        }

        @Override
        public long count(TST_Computer tstComputer) {
            return tstComputer.realMonitor != null ? 1 : 0;
        }
    }

    // endregion

}
