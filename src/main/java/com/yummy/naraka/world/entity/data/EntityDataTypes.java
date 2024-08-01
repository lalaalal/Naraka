package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import java.util.function.Function;
import java.util.function.Supplier;

public class EntityDataTypes {
    public static final StigmaEntityDataType STIGMA = register("stigma", StigmaEntityDataType::new);
    public static final IntegerEntityDataType DEATH_COUNT = register("death_count", IntegerEntityDataType::new);

    private static <D, T extends EntityDataType<D>> T register(String name, Function<String, T> factory) {
        return Registry.register(NarakaRegistries.ENTITY_DATA_TYPE, NarakaMod.location(name), factory.apply(name));
    }

    private static <D, T extends EntityDataType<D>> T register(String name, Supplier<T> factory) {
        return Registry.register(NarakaRegistries.ENTITY_DATA_TYPE, NarakaMod.location(name), factory.get());
    }

    public static Holder<EntityDataType<?>> holder(EntityDataType<?> type) {
        return NarakaRegistries.ENTITY_DATA_TYPE.wrapAsHolder(type);
    }

    public static void initialize() {

    }
}
