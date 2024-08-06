package com.Nxer.TwistSpaceTechnology.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public enum ModelEnum {

    PowerChair,
    Yamato,
    STAR;

    private IModelCustom Model;

    ModelEnum(){}

    public ModelEnum set(ResourceLocation resourceLocation){
        this.Model = AdvancedModelLoader
                .loadModel(resourceLocation);;
        return this;
    }

    public IModelCustom get(){
        return this.Model;
    }
}
