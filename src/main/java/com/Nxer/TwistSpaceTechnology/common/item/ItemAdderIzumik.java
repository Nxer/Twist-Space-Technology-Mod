package com.Nxer.TwistSpaceTechnology.common.item;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.common.api.IHasVariantAndTooltips;

public class ItemAdderIzumik extends AbstractTstMetaItem implements IHasVariantAndTooltips {

    private final Map<Integer, String[]> MetaItemTooltipsMapIzumik = new HashMap<>();
    private final Map<Integer, String[]> MetaItemTooltipsMapIzumikShift = new HashMap<>();

    public ItemAdderIzumik() {
        // #tr item.MetaItemIzumik.name
        // # Meta Item Izumik
        // #zh_CN Meta Item Izumik
        super("MetaItemIzumik");
        setTextureName("gtnhcommunitymod:MetaItem01/0");
    }

    @Override
    public void setTooltips(int metaValue, @Nullable String[] tooltips, boolean advancedMode) {
        if (advancedMode) {
            MetaItemTooltipsMapIzumikShift.put(metaValue, tooltips);
        } else {
            MetaItemTooltipsMapIzumik.put(metaValue, tooltips);
        }
    }

    @Override
    public @Nullable String[] getTooltips(int metaValue, boolean advancedMode) {
        if (advancedMode) {
            return MetaItemTooltipsMapIzumikShift.get(metaValue);
        } else {
            return MetaItemTooltipsMapIzumik.get(metaValue);
        }
    }

}
