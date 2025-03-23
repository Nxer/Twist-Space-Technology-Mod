package com.Nxer.TwistSpaceTechnology.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class FMLPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        // Return your Transformer class
        return new String[] { "com.Nxer.TwistSpaceTechnology.hook.GregTechPreInitHook" };
    }

    @Override
    public String getModContainerClass() {
        // If no additional mod container needs to be registered, return null
        return null;
    }

    @Override
    public String getSetupClass() {
        // If there is no special initialization, return null
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        // This method can be used to obtain loading environment information and can generally be left blank
    }

    @Override
    public String getAccessTransformerClass() {
        // If no access converter is used, return null
        return null;
    }
}
