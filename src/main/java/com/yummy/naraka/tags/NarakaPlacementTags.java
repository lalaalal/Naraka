package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface NarakaPlacementTags {
    TagKey<PlacedFeature> NECTARIUM = create("nectarium");

    private static TagKey<PlacedFeature> create(String name) {
        return TagKey.create(Registries.PLACED_FEATURE, NarakaMod.location(name));
    }
}
