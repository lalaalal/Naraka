package com.yummy.naraka.world.block.grower;

import com.yummy.naraka.world.features.NarakaTreeFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class NarakaTreeGrowers {
    public static final TreeGrower EBONY = new TreeGrower(
            "ebony",
            Optional.empty(),
            Optional.of(NarakaTreeFeatures.EBONY_CHERRY),
            Optional.empty()
    );

    public static void initialize() {

    }
}
