package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NarakaEntityDataTypes {
    private static final DeferredRegister<EntityDataType<?>> ENTITY_DATA_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, NarakaRegistries.Keys.ENTITY_DATA_TYPE);

    public static final RegistrySupplier<StigmaEntityDataType> STIGMA = register("stigma", StigmaEntityDataType::new);
    public static final RegistrySupplier<IntegerEntityDataType> DEATH_COUNT = register("death_count", IntegerEntityDataType::new, -1);

    private static <D, T extends EntityDataType<D>> RegistrySupplier<T> register(String name, BiFunction<ResourceLocation, Supplier<D>, T> factory, Supplier<D> defaultValue) {
        ResourceLocation id = NarakaMod.location(name);
        return ENTITY_DATA_TYPES.register(id, () -> factory.apply(id, defaultValue));
    }

    private static <D, T extends EntityDataType<D>> RegistrySupplier<T> register(String name, BiFunction<ResourceLocation, D, T> factory, D defaultValue) {
        ResourceLocation id = NarakaMod.location(name);
        return ENTITY_DATA_TYPES.register(id, () -> factory.apply(id, defaultValue));
    }

    private static <D, T extends EntityDataType<D>> RegistrySupplier<T> register(String name, Supplier<T> factory) {
        return ENTITY_DATA_TYPES.register(name, factory);
    }

    public static Holder<EntityDataType<?>> holder(RegistryAccess registryAccess, EntityDataType<?> type) {
        return registryAccess.registryOrThrow(NarakaRegistries.Keys.ENTITY_DATA_TYPE).wrapAsHolder(type);
    }

    public static HolderSet<EntityDataType<?>> full(RegistryAccess registryAccess) {
        return HolderSet.direct(registryAccess.registryOrThrow(NarakaRegistries.Keys.ENTITY_DATA_TYPE)
                .holders().toList());
    }

    public static void initialize() {
        ENTITY_DATA_TYPES.register();

        StigmaHelper.initialize();
    }
}
