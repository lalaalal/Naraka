package com.yummy.naraka.tags;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class ConventionalTags {
    public static final class Blocks {
        public static final TagKey<Block> ORES = get("ores");

        public static TagKey<Block> get(String name) {
            return ConventionalTags.get(Registries.BLOCK, name);
        }
    }

    public static final class Biomes {
        public static final TagKey<Biome> IS_OVERWORLD = get("is_overworld");
        public static final TagKey<Biome> IS_PLAINS = get("is_plains");

        public static TagKey<Biome> get(String name) {
            return ConventionalTags.get(Registries.BIOME, name);
        }
    }

    public static final class Entities {
        public static final TagKey<EntityType<?>> BOSSES = get("bosses");

        public static TagKey<EntityType<?>> get(String name) {
            return ConventionalTags.get(Registries.ENTITY_TYPE, name);
        }
    }

    public static <T> TagKey<T> get(ResourceKey<Registry<T>> registryKey, String name) {
        return TagKey.create(registryKey, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
