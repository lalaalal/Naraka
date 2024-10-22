package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public interface NarakaStructureSetTags {
    TagKey<StructureSet> HEROBRINE_SANCTUARY_EXCLUSIVE = create("herobrine_sanctuary_exclusive");

    private static TagKey<StructureSet> create(String name) {
        return TagKey.create(Registries.STRUCTURE_SET, NarakaMod.location(name));
    }
}
