package com.yummy.naraka.world.structure;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class NarakaStructures {
    public static final ResourceKey<Structure> HEROBRINE_SANCTUARY = create("herobrine_sanctuary");
    public static final BlockPos HEROBRINE_SANCTUARY_OFFSET = new BlockPos(-4, -6, 48 * 2);
    public static final BlockPos HEROBRINE_SANCTUARY_MAIN_OFFSET = new BlockPos(-63, 0, -48 * 4);

    private static ResourceKey<Structure> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE, NarakaMod.location(name));
    }
}
