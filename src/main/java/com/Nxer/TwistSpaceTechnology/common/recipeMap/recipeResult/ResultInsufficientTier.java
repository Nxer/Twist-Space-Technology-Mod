package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeResult;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_Utility;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.NotNull;

/**
 * This class adds a new "failed recipe result" to show that something of some type is not met the requirement like
 * {@link gregtech.api.recipe.check.ResultInsufficientMachineTier ResultInsufficientMachineTier}.
 * <p>
 * When you need a new type, you should declare it like below with the localization text. This class has already registered,
 * so you must not register twice.
 */
public class ResultInsufficientTier implements CheckRecipeResult {

    private int typeRequired;
    private int tierRequired;

    // #tr tst.gui.text.insufficient_tier.1
    // # Blood Altar Tier
    // #zh_CN 血魔法祭坛
    private static final int TYPE_BLOOD_ALTAR = 1;

    // #tr tst.gui.text.insufficient_tier.2
    // # Blood Orb
    // #zh_CN 血气宝珠
    private static final int TYPE_BLOOD_ORB = 2;

    // #tr tst.gui.text.insufficient_tier.3
    // # Activation Crystal
    // #zh_CN 激活水晶
    private static final int TYPE_ACTIVATION_CRYSTAL = 3;

    private static ResultInsufficientTier of(int typeRequired, int tierRequired) {
        return new ResultInsufficientTier(typeRequired, tierRequired);
    }

    public static ResultInsufficientTier ofBloodAltar(int required) {
        return of(TYPE_BLOOD_ALTAR, required);
    }

    public static ResultInsufficientTier ofBloodOrb(int required) {
        return of(TYPE_BLOOD_ORB, required);
    }

    public static ResultInsufficientTier ofActivationCrystal(int required) {
        return of(TYPE_ACTIVATION_CRYSTAL, required);
    }

    public ResultInsufficientTier(int typeRequired, int tierRequired) {
        this.typeRequired = typeRequired;
        this.tierRequired = tierRequired;
    }

    @NotNull
    @Override
    public String getID() {
        return "insufficient_tier";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @NotNull
    @Override
    public String getDisplayString() {
        // #tr tst.gui.text.insufficient_tier
        // # §7Recipe needs higher %s tier. Required: %s
        // #zh_CN §7配方需要更高的%s等级。需要：%s
        return StatCollector.translateToLocalFormatted(
            "tst.gui.text.insufficient_tier",
            StatCollector.translateToLocalFormatted("tst.gui.text.insufficient_tier." + typeRequired),
            GT_Utility.formatNumbers(tierRequired));
    }

    @NotNull
    @Override
    public CheckRecipeResult newInstance() {
        return new ResultInsufficientTier((byte) 0, 0);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(typeRequired);
        buffer.writeVarIntToBuffer(tierRequired);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        typeRequired = buffer.readVarIntFromBuffer();
        tierRequired = buffer.readVarIntFromBuffer();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ResultInsufficientTier other)) return false;
        return typeRequired == other.typeRequired && tierRequired == other.tierRequired;
    }
}
