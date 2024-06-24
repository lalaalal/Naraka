package com.yummy.naraka.world.block.grower;

import com.yummy.naraka.data.worldgen.features.NarakaTreeFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class NarakaTreeGrowers {
    public static final TreeGrower EBONY = new TreeGrower("ebony", Optional.empty(), Optional.of(NarakaTreeFeatures.EBONY), Optional.empty());
}
