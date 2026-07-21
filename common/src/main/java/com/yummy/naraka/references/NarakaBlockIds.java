package com.yummy.naraka.references;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class NarakaBlockIds {
    public static final ResourceKey<Block> DIAMOND_GOLEM_SPAWNER = create("diamond_golem_spawner");
    public static final ResourceKey<Block> PURIFIED_SOUL_FIRE_BLOCK = create("purified_soul_fire");
    public static final ResourceKey<Block> NARAKA_PORTAL = create("naraka_portal");

    public static ResourceKey<Block> create(String id) {
        return ResourceKey.create(Registries.BLOCK, NarakaMod.location(id));
    }
}
