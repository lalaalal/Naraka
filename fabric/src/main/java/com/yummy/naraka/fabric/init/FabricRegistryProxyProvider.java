package com.yummy.naraka.fabric.init;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public final class FabricRegistryProxyProvider extends RegistryProxyProvider {
    public FabricRegistryProxyProvider() {
        add(new FabricRecipeSerializerRegistry());
    }

    @Override
    protected <T> RegistryProxy<T> create(ResourceKey<Registry<T>> key) {
        return new FabricRegistryProxy<>(key);
    }

    private static class FabricRegistryProxy<T> implements RegistryProxy<T> {
        private final ResourceKey<Registry<T>> key;

        public FabricRegistryProxy(ResourceKey<Registry<T>> key) {
            this.key = key;
        }

        @Override
        public ResourceKey<? extends Registry<T>> getRegistryKey() {
            return key;
        }

        @Override
        public <V extends T> HolderProxy<T, V> register(String name, Supplier<V> value) {
            Registry.register(getRegistryOrThrow(), NarakaMod.identifier(name), value.get());
            return createHolder(name);
        }
    }

    private static class FabricRecipeSerializerRegistry extends FabricRegistryProxy<RecipeSerializer<?>> {
        public FabricRecipeSerializerRegistry() {
            super(Registries.RECIPE_SERIALIZER);
        }

        @Override
        public <V extends RecipeSerializer<?>> HolderProxy<RecipeSerializer<?>, V> register(String name, Supplier<V> value) {
            RecipeSerializer<?> serializer = value.get();
            RecipeSynchronization.synchronizeRecipeSerializer(serializer);
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, NarakaMod.identifier(name), serializer);
            return createHolder(name);
        }
    }
}
