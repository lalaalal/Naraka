package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class NarakaBlockTags {
    public static final TagKey<Block> NEEDS_NETHERITE_TOOL = create("needs_netherite_tool");
    public static final TagKey<Block> NECTARIUM_ORES = create("nectarium_ores");
    public static final TagKey<Block> AMETHYST_ORES = create("amethyst_ores");
    public static final TagKey<Block> EBONY_LOGS = create("ebony_log");
    public static final TagKey<Block> EBONY_ROOTS_CAN_GROW_THROUGH = create("ebony_roots_can_grow_through");
    public static final TagKey<Block> HEROBRINE_SANCTUARY_WRAP_TARGETS = create("herobrine_sanctuary_wrap_targets");

    public static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, NarakaMod.location(name));
    }
}
