package com.yummy.naraka.forge.init;

import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.world.item.SpawnEggItemProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;

public final class ForgeSpawnEggItemProvider implements SpawnEggItemProvider.Factory {
    @Override
    public SpawnEggItem create(ValueGetter<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(entityType::getConcreteValue, backgroundColor, highlightColor, properties);
    }
}
