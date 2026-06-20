package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.init.SpawnPlacementRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import java.util.function.Supplier;

public class NeoForgeSpawnPlacementRegistry implements NarakaEventBus {
    @MethodProxy(SpawnPlacementRegistry.class)
    public static <T extends Mob> void register(Supplier<EntityType<T>> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        NARAKA_BUS.addListener(RegisterSpawnPlacementsEvent.class, event -> {
            event.register(entityType.get(), spawnPlacementType, heightmapType, predicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        });
    }
}
