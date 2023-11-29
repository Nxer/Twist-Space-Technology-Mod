package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import static com.Nxer.TwistSpaceTechnology.system.OreProcess.logic.OP_Values.OreProcessRecipeDuration;
import static gtPlusPlus.core.material.ALLOY.KOBOLDITE;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_HIGH;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_LOW;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_MID;
import static gtPlusPlus.core.material.ORES.AGARDITE_CD;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.ORES;

public class OP_GTPP_OreHandler {

    public static final OP_GTPP_OreHandler instance = new OP_GTPP_OreHandler();

    public Set<Material> addSpecials(Set<Material> set) {
        set.add(RARE_EARTH_LOW);
        set.add(RARE_EARTH_MID);
        set.add(RARE_EARTH_HIGH);
        set.add(KOBOLDITE);
        return set;
    }

    public Set<Material> getGTPPOreMaterials() {
        Set<Material> gtppOres = new HashSet<>(51);
        for (Field field : ORES.class.getFields()) {
            if (field.getType() != Material.class) continue;
            try {
                Object object = field.get(AGARDITE_CD);
                if (!(object instanceof Material)) continue;
                gtppOres.add((Material) object);
            } catch (IllegalAccessException e) {
                TwistSpaceTechnology.LOG
                    .info("Catch an IllegalAccessException in OP_GTPP_OreHandler.processGTPPOreRecipes");
            }
        }
        return gtppOres;
    }

    public void processGTPPOreRecipes() {
        for (Material ore : addSpecials(getGTPPOreMaterials())) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ore.getOre(1))
                .itemOutputs(ore.getDust(12))
                .noFluidInputs()
                .noFluidOutputs()
                .eut(120)
                .duration(OreProcessRecipeDuration)
                .addTo(GTCMRecipe.instance.OreProcessingRecipes);
        }
    }

}
