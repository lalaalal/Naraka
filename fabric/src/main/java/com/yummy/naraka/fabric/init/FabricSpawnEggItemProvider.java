package com.yummy.naraka.fabric.init;

import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.world.item.SpawnEggItemProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public final class FabricSpawnEggItemProvider {
    @MethodProxy(SpawnEggItemProvider.class)
    public static SpawnEggItem create(Supplier<? extends EntityType<? extends Mob>> entityType, Integer backgroundColor, Integer highlightColor, Item.Properties properties) {
        return new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, properties);
    }
}
