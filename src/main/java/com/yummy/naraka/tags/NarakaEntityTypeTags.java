package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public interface NarakaEntityTypeTags {
    /**
     * Entities that should apply death count<br>
     * Player and bosses
     */
    TagKey<EntityType<?>> APPLY_DEATH_COUNT = create("apply_death_count");
    /**
     * Entity that counting death of other entities
     */
    TagKey<EntityType<?>> DEATH_COUNTING_ENTITY = create("death_counting_entity");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, NarakaMod.location(name));
    }
}
