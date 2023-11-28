package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.ORES;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static gtPlusPlus.core.material.ALLOY.KOBOLDITE;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_HIGH;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_LOW;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_MID;
import static gtPlusPlus.core.material.ORES.AGARDITE_CD;

public class OP_GTPP_OreHandler {
    public static final OP_GTPP_OreHandler instance = new OP_GTPP_OreHandler();

    public Set<Material> addSpecials(Set<Material> set){
        set.add(RARE_EARTH_LOW);
        set.add(RARE_EARTH_MID);
        set.add(RARE_EARTH_HIGH);
        set.add(KOBOLDITE);
        return set;
    }
    public Set<Material> getGTPPOreMaterials(){
        Set<Material> gtppOres = new HashSet<>(51);
        for (Field field : ORES.class.getFields()){
            if (field.getType() != Material.class) continue;
            try {
                Object object = field.get(AGARDITE_CD);
                if (!(object instanceof Material)) continue;
                gtppOres.add((Material) object);
            } catch (IllegalAccessException e){
                TwistSpaceTechnology.LOG.info("Catch an IllegalAccessException in OP_GTPP_OreHandler.processGTPPOreRecipes");
            }
        }
        return gtppOres;
    }

    public void processGTPPOreRecipes(){
        for (Material ore : addSpecials(getGTPPOreMaterials())){
            GT_Values.RA
                .stdBuilder()
                .itemInputs(ore.getOre(16))
                .itemOutputs(
                    ore.getDust(64),
                    ore.getDust(64),
                    ore.getDust(64))
                .noFluidInputs()
                .noFluidOutputs()
                .eut(120)
                .duration(128)
                .addTo(GTCMRecipe.instance.OreProcessingRecipes);
        }
    }

}
