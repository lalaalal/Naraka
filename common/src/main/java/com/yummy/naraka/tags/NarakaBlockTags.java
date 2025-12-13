package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class NarakaBlockTags {
    public static final TagKey<Block> NEEDS_NETHERITE_TOOL = create("needs_netherite_tool");
    public static final TagKey<Block> NECTARIUM_ORES = create("nectarium_ores");
    public static final TagKey<Block> AMETHYST_ORES = create("amethyst_ores");
    public static final TagKey<Block> HEROBRINE_SANCTUARY_AIR_WRAP_TARGETS = create("herobrine_sanctuary_air_wrap_targets");
    public static final TagKey<Block> HEROBRINE_SANCTUARY_LAVA_WRAP_TARGETS = create("herobrine_sanctuary_lava_wrap_targets");
    public static final TagKey<Block> MINABLE_WITH_NARAKA_PICKAXE = create("minable_with_naraka_pickaxe");

    public static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, NarakaMod.location(name));
    }
}
