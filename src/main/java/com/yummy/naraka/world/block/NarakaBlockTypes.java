package com.yummy.naraka.world.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class NarakaBlockTypes {
    public static class BlockSet {
        public static final BlockSetType EBONY = BlockSetType.register(new BlockSetType("naraka:ebony"));
    }

    public static class Wood {
        public static final WoodType EBONY = WoodType.register(new WoodType("naraka:ebony", BlockSet.EBONY));
    }
}
