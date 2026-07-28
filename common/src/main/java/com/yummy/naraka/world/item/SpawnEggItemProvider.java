package com.yummy.naraka.world.item;

import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.service.NarakaServices;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public abstract class SpawnEggItemProvider {
    public static SpawnEggItem create(ValueGetter<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return NarakaServices.SPAWN_EGG_ITEM_FACTORY.create(entityType, backgroundColor, highlightColor, properties);
    }

    public interface Factory {
        SpawnEggItem create(ValueGetter<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);
    }
}
