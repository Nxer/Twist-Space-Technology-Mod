package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellAlchemicTierKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellTierKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeResult.ResultInsufficientTier;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;

public class TST_BloodyHell extends GTCM_MultiMachineBase<TST_BloodyHell> implements ISurvivalConstructable {

    private static final String[][] STRUCTURE_STRING = new String[][] { { "     ", "     ", "  ~  ", "     ", "     " },
        { "XXXXX", "XRRRX", "XRRRX", "XRRRX", "XXXXX" }, };

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static IStructureDefinition<TST_BloodyHell> StructureDef;

    private static final ITexture[] FACING_ACTIVE = {
        TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE), TextureFactory.builder()
            .addIcon(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW)
            .glow()
            .build() };

    private static final int MODE_ALTAR = 0;
    private static final int MODE_ALCHEMIC = 1;
    private static final int MODE_BINDING = 2;

    private static final int[] MODES = new int[] { MODE_ALTAR, MODE_ALCHEMIC, MODE_BINDING };

    private int mode = MODE_ALTAR;

    public TST_BloodyHell(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_BloodyHell(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_BloodyHell(mName);
    }

    public int getAltarTier() {
        // TODO: check altar tiers from structure
        return 5;
    }

    public int getOrbTier() {
        return BloodMagicHelper.getBloodOrbTier(getControllerSlot());
    }

    public int getActivationCrystalTier() {
        return BloodMagicHelper.getActivationCrystalTier(getControllerSlot());
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 0;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        var tt = new GT_Multiblock_Tooltip_Builder();
        // #tr Tooltip_BloodyHell_MachineType
        // # Altar of Blood | Alchemic Chemistry Set | Ritual of Binding
        // #zh_CN 血之祭坛 | 炼金术台 | 绑定仪式
        tt.addMachineType(TextEnums.tr("Tooltip_BloodyHell_MachineType"))
            // #tr Tooltip_BloodyHell_0
            // # After years of researching, you finally find the last piece to mass-produce the Blood Magic things!
            // #zh_CN 经过多年的研究，你终于找到了量产血魔法物品的最后一块拼图！
            .addInfo(TextEnums.tr("Tooltip_BloodyHell_0"))
            .addSeparator()
            .addInfo(textScrewdriverChangeMode)
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addController(textUseBlueprint)
            // TODO: add structure tooltip after done with the structure
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        // TODO: texture
        return FACING_ACTIVE;
    }

    @Override
    public IStructureDefinition<TST_BloodyHell> getStructureDefinition() {
        if (StructureDef == null) {
            StructureDef = StructureDefinition.<TST_BloodyHell>builder()
                .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(STRUCTURE_STRING))
                .addElement('R', ofBlockAnyMeta(WayofTime.alchemicalWizardry.ModBlocks.bloodRune))
                .addElement(
                    'X',
                    ofChain(
                        ofBlock(ModBlocks.blockCasings2Misc, 15),
                        GT_HatchElementBuilder.<TST_BloodyHell>builder()
                            .atLeast(InputBus, InputHatch, OutputBus)
                            .adder(TST_BloodyHell::addToMachineList)
                            .dot(1)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .build()))
                .build();
        }
        return StructureDef;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 0, 2)) {
            return false;
        }
        // TODO: add rune check maybe?
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 0, 2);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return super.getStructureDescription(stackSize);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return this.survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 0, 2, elementBudget, env, false, true);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % MODES.length;

            // #tr BloodyHell.modeMsg.0
            // # Altar Mode
            // #zh_CN 祭坛模式

            // #tr BloodyHell.modeMsg.1
            // # Alchemic Chemistry Mode
            // #zh_CN 炼金模式

            // #tr BloodyHell.modeMsg.2
            // # Binding Ritual Mode
            // #zh_CN 绑定仪式模式

            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("BloodyHell.modeMsg." + this.mode));
        }
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(
            GTCMRecipe.BloodyHellRecipes,
            GTCMRecipe.BloodyHellRecipe_Alchemic,
            GTCMRecipe.BloodyHellRecipe_Binding);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (mode) {
            case MODE_ALTAR -> {
                return GTCMRecipe.BloodyHellRecipes;
            }
            case MODE_ALCHEMIC -> {
                return GTCMRecipe.BloodyHellRecipe_Alchemic;
            }
            case MODE_BINDING -> {
                return GTCMRecipe.BloodyHellRecipe_Binding;
            }
        }
        return GTCMRecipe.BloodyHellRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                // check altar tier
                int requiredTier = recipe.getMetadataOrDefault(BloodyHellTierKey.INSTANCE, 0);
                if (requiredTier > getAltarTier()) {
                    return ResultInsufficientTier.ofBloodAltar(requiredTier);
                }

                // check blood orb tier
                int requiredOrbTier = recipe.getMetadataOrDefault(BloodyHellAlchemicTierKey.INSTANCE, 0);
                if (requiredOrbTier > getOrbTier()) {
                    return ResultInsufficientTier.ofBloodOrb(requiredOrbTier);
                }

                // check weak activation crystal
                if (mode == MODE_BINDING && getActivationCrystalTier() < 1) {
                    return ResultInsufficientTier.ofActivationCrystal(1);
                }

                return super.validateRecipe(recipe);
            }
        };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("mode", this.mode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mode = aNBT.getInteger("mode");
    }
}
