package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.SpawnPlacementRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public final class FabricSpawnPlacementRegistry {
    @MethodProxy(SpawnPlacementRegistry.class)
    public static <T extends Mob> void register(Supplier<EntityType<T>> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnPlacements.register(entityType.get(), spawnPlacementType, heightmapType, predicate);
    }
}
