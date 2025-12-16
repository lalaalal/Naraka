package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class NarakaBiomeTags {
    public static final TagKey<Biome> HEROBRINE_SANCTUARY_BIOMES = create("herobrine_sanctuary_biomes");

    public static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, NarakaMod.identifier(name));
    }
}
