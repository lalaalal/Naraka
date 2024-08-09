package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class EntityDataTypes {
    public static final StigmaEntityDataType STIGMA = register("stigma", StigmaEntityDataType::new);
    public static final IntegerEntityDataType DEATH_COUNT = register("death_count", IntegerEntityDataType::new, -1);

    private static <D, T extends EntityDataType<D>> T register(String name, BiFunction<ResourceLocation, Supplier<D>, T> factory, Supplier<D> defaultValue) {
        ResourceLocation id = NarakaMod.location(name);
        return Registry.register(NarakaRegistries.ENTITY_DATA_TYPE, id, factory.apply(id, defaultValue));
    }

    private static <D, T extends EntityDataType<D>> T register(String name, BiFunction<ResourceLocation, D, T> factory, D defaultValue) {
        ResourceLocation id = NarakaMod.location(name);
        return Registry.register(NarakaRegistries.ENTITY_DATA_TYPE, id, factory.apply(id, defaultValue));
    }

    private static <D, T extends EntityDataType<D>> T register(String name, Supplier<T> factory) {
        return Registry.register(NarakaRegistries.ENTITY_DATA_TYPE, NarakaMod.location(name), factory.get());
    }

    public static Holder<EntityDataType<?>> holder(EntityDataType<?> type) {
        return NarakaRegistries.ENTITY_DATA_TYPE.wrapAsHolder(type);
    }

    public static void initialize() {
        StigmaHelper.initialize();
    }
}
