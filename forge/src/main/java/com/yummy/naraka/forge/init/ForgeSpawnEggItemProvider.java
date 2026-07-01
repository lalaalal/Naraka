package com.yummy.naraka.forge.init;

import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.world.item.SpawnEggItemProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.Supplier;

public final class ForgeSpawnEggItemProvider {
    @MethodProxy(SpawnEggItemProvider.class)
    public static SpawnEggItem create(Supplier<? extends EntityType<? extends Mob>> entityType, Integer backgroundColor, Integer highlightColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, properties);
    }
}
