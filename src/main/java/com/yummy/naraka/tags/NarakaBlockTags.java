package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface NarakaBlockTags {
    TagKey<Block> NEEDS_NETHERITE_TOOL = create("needs_netherite_tool");
    TagKey<Block> NECTARIUM_ORES = create("nectarium_ores");
    TagKey<Block> EBONY_LOGS = create("ebony_log");
    TagKey<Block> EBONY_ROOTS_CAN_GROW_THROUGH = create("ebony_roots_can_grow_through");
    TagKey<Block> HEROBRINE_SANCTUARY_WRAP_TARGETS = create("herobrine_sanctuary_wrap_targets");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, NarakaMod.location(name));
    }
}
