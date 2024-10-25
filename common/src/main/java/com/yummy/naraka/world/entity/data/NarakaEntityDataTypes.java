package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NarakaEntityDataTypes {
    public static final LazyHolder<EntityDataType<?>, StigmaEntityDataType> STIGMA = register("stigma", StigmaEntityDataType::new);
    public static final LazyHolder<EntityDataType<?>, IntegerEntityDataType> DEATH_COUNT = register("death_count", IntegerEntityDataType::new, -1);

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

    public static Holder<EntityDataType<?>> holder(RegistryAccess registryAccess, EntityDataType<?> type) {
        return registryAccess.registryOrThrow(NarakaRegistries.Keys.ENTITY_DATA_TYPE).wrapAsHolder(type);
    }

    public static HolderSet<EntityDataType<?>> full(RegistryAccess registryAccess) {
        return HolderSet.direct(registryAccess.registryOrThrow(NarakaRegistries.Keys.ENTITY_DATA_TYPE)
                .holders().toList());
    }

    public static void initialize() {
        RegistryInitializer.get(NarakaRegistries.Keys.ENTITY_DATA_TYPE)
                .onRegistrationFinished();
        StigmaHelper.initialize();
    }
}
