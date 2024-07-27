package com.yummy.naraka.world.block.grower;

import com.yummy.naraka.data.worldgen.features.NarakaTreeFeatures;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.trunkplacers.TrunkPlacerPatches;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class NarakaTreeGrowers {
    public static final TreeGrower EBONY = new TreeGrower(
            "ebony", 0.5f,
            Optional.empty(),
            Optional.empty(),
            Optional.of(NarakaTreeFeatures.EBONY),
            Optional.of(NarakaTreeFeatures.EBONY_CHERRY),
            Optional.empty(),
            Optional.empty()
    );

    public static void initialize() {
        TrunkPlacerPatches.register(NarakaBlocks.EBONY_LOG, NarakaBlocks.EBONY_WOOD.defaultBlockState());
    }
}
