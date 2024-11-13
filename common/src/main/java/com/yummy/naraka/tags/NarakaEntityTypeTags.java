package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class NarakaEntityTypeTags {
    /**
     * Entities that should apply death count<br>
     * Player and bosses
     */
    public static final TagKey<EntityType<?>> DEATH_COUNTABLE = create("death_countable");
    public static final TagKey<EntityType<?>> DEATH_COUNTING = create("death_count_exclude");
    public static final TagKey<EntityType<?>> HEROBRINE = create("herobrine");

    public static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, NarakaMod.location(name));
    }
}
