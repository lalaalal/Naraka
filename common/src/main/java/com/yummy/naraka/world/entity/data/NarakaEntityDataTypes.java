package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.NarakaInitializer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NarakaEntityDataTypes {
    public static final LazyHolder<EntityDataType<?>, StigmaEntityDataType> STIGMA = register("stigma", StigmaEntityDataType::new);
    public static final LazyHolder<EntityDataType<?>, IntegerEntityDataType> DEATH_COUNT = register("death_count", IntegerEntityDataType::new, -1);
    public static final LazyHolder<EntityDataType<?>, DoubleEntityDataType> LOCKED_HEALTH = register("locked_health", DoubleEntityDataType::new, 0.0);

    private static <D, T extends EntityDataType<D>> LazyHolder<EntityDataType<?>, T> register(String name, BiFunction<ResourceLocation, Supplier<D>, T> factory, Supplier<D> defaultValue) {
        ResourceLocation id = NarakaMod.location(name);
        return RegistryProxy.register(NarakaRegistries.Keys.ENTITY_DATA_TYPE, name, () -> factory.apply(id, defaultValue));
    }

    private static <D, T extends EntityDataType<D>> LazyHolder<EntityDataType<?>, T> register(String name, BiFunction<ResourceLocation, D, T> factory, D defaultValue) {
        ResourceLocation id = NarakaMod.location(name);
        return RegistryProxy.register(NarakaRegistries.Keys.ENTITY_DATA_TYPE, name, () -> factory.apply(id, defaultValue));
    }

    private static <D, T extends EntityDataType<D>> LazyHolder<EntityDataType<?>, T> register(String name, Supplier<T> factory) {
        return RegistryProxy.register(NarakaRegistries.Keys.ENTITY_DATA_TYPE, name, factory);
    }

    public static Holder<EntityDataType<?>> holder(EntityDataType<?> type) {
        return NarakaRegistries.ENTITY_DATA_TYPE.wrapAsHolder(type);
    }

    public static HolderSet<EntityDataType<?>> full() {
        return HolderSet.direct(NarakaRegistries.ENTITY_DATA_TYPE.holders().toList());
    }

    public static void initialize(NarakaInitializer initializer) {
        RegistryInitializer.get(NarakaRegistries.Keys.ENTITY_DATA_TYPE)
                .onRegistrationFinished();
        initializer.runAfterRegistryLoaded(StigmaHelper::initialize);
    }
}
