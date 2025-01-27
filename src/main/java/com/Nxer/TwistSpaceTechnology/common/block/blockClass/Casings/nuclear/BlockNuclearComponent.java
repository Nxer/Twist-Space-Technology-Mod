package com.Nxer.TwistSpaceTechnology.common.block.blockClass.Casings.nuclear;

import java.util.HashMap;

import net.minecraft.block.Block;

import com.Nxer.TwistSpaceTechnology.common.block.blockClass.ItemBlockBase01;

import ic2.api.reactor.IReactorComponent;

public class BlockNuclearComponent extends BlockNuclearReactor {

    public static HashMap<Integer, IReactorComponent> components;

    public BlockNuclearComponent(String unlocalizedName, String localName) {
        super();
    }

    public static class innerItemBlock extends ItemBlockBase01 {

        public innerItemBlock(Block aBlock) {
            super(aBlock);
        }
    }
}
