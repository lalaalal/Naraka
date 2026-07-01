package com.yummy.naraka.world.item;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public abstract class SpawnEggItemProvider {
    public static SpawnEggItem create(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return MethodInvoker.of(SpawnEggItemProvider.class, "create")
                .withParameterTypes(Supplier.class, Integer.class, Integer.class, Item.Properties.class)
                .invoke(entityType, backgroundColor, highlightColor, properties)
                .result(SpawnEggItem.class);
    }
}
