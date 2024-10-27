package com.yummy.naraka.tags;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ConventionalTags {
    public static class Blocks {
        public static final TagKey<Block> ORES = get("ores");

        public static TagKey<Block> get(String name) {
            return ConventionalTags.get(Registries.BLOCK, name);
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> IS_PLAIN = get("is_plain");

        public static TagKey<Biome> get(String name) {
            return ConventionalTags.get(Registries.BIOME, name);
        }
    }

    public static <T> TagKey<T> get(ResourceKey<Registry<T>> registryKey, String name) {
        return TagKey.create(registryKey, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}