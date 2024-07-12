package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface NarakaBlockTags {
    TagKey<Block> NECTARIUM_ORES = create("nectarium_ores");
    TagKey<Block> EBONY_LOGS = create("ebony_log");
    TagKey<Block> HEROBRINE_SANCTUARY_WRAP_TARGETS = create("herobrine_sanctuary_wrap_targets");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, NarakaMod.location(name));
    }
}
