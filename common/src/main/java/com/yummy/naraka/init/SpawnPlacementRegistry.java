package com.yummy.naraka.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public abstract class SpawnPlacementRegistry {
    public static <T extends Mob> void register(Supplier<EntityType<T>> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        MethodInvoker.of(SpawnPlacementRegistry.class, "register")
                .invoke(entityType, spawnPlacementType, heightmapType, predicate);
    }
}
