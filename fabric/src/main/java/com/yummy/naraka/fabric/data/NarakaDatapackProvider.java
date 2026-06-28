package com.yummy.naraka.fabric.data;

import com.mojang.serialization.Lifecycle;
import com.yummy.naraka.data.worldgen.*;
import com.yummy.naraka.data.worldgen.features.NarakaConfiguredFeatures;
import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class NarakaDatapackProvider extends RegistriesDatapackGenerator {
    private static final Set<ResourceKey<? extends Registry<?>>> BUILDER_KEYS = new HashSet<>();

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(store(Registries.DAMAGE_TYPE), NarakaDamageTypes::bootstrap)
            .add(store(Registries.CONFIGURED_FEATURE), NarakaConfiguredFeatures::bootstrap)
            .add(store(Registries.PLACED_FEATURE), NarakaPlacements::bootstrap)
            .add(store(Registries.STRUCTURE), NarakaStructures::bootstrap)
            .add(store(Registries.STRUCTURE_SET), NarakaStructureSets::bootstrap)
            .add(store(Registries.TRIM_PATTERN), NarakaTrimPatterns::bootstrap)
            .add(store(Registries.TRIM_MATERIAL), NarakaTrimMaterials::bootstrap)
            .add(store(Registries.CONFIGURED_CARVER), NarakaConfiguredWorldCarvers::bootstrap)
            .add(store(Registries.BIOME), NarakaBiomeData::bootstrap)
            .add(store(Registries.DIMENSION_TYPE), NarakaDimensionTypes::bootstrap);

    private final CompletableFuture<HolderLookup.Provider> narakaRegistries;

    private static <T> ResourceKey<? extends Registry<T>> store(ResourceKey<? extends Registry<T>> key) {
        BUILDER_KEYS.add(key);
        return key;
    }

    public NarakaDatapackProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, createLookup(registries, BUILDER));
        narakaRegistries = createFullLookup(registries, BUILDER);
    }

    public CompletableFuture<HolderLookup.Provider> getRegistryProvider() {
        return narakaRegistries;
    }

    private static CompletableFuture<HolderLookup.Provider> createLookup(CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder builder) {
        return registries.thenApply(
                provider -> {
                    Stream.concat(RegistryDataLoader.WORLDGEN_REGISTRIES.stream(), RegistryDataLoader.DIMENSION_REGISTRIES.stream())
                            .filter(data -> !BUILDER_KEYS.contains(data.key()))
                            .forEach(data -> {
                                BUILDER_KEYS.add(data.key());
                                builder.add(data.key(), context -> {
                                });
                            });
                    return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider);
                }
        );
    }

    private static CompletableFuture<HolderLookup.Provider> createFullLookup(CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder builder) {
        return registries.thenApply(
                provider -> {
                    RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
                    Stream.concat(RegistryDataLoader.WORLDGEN_REGISTRIES.stream(), RegistryDataLoader.DIMENSION_REGISTRIES.stream())
                            .filter(data -> !BUILDER_KEYS.contains(data.key()))
                            .forEach(data -> {
                                BUILDER_KEYS.add(data.key());
                                builder.add(data.key(), context -> {
                                });
                            });
                    HolderLookup.Provider patched = builder.buildPatch(registryAccess, provider);

                    Stream<HolderLookup.RegistryLookup<?>> builtinRegistryLookups = registryAccess.registries()
                            .map(registryEntry -> registryEntry.value().asLookup());
                    Stream<HolderLookup.RegistryLookup<?>> datapackRegistryLookups = RegistryDataLoader.WORLDGEN_REGISTRIES.stream()
                            .map(data -> new CombinedRegistryLookup<>(patched.lookupOrThrow(data.key()), provider.lookupOrThrow(data.key())));
                    return HolderLookup.Provider.create(Stream.concat(builtinRegistryLookups, datapackRegistryLookups));
                }
        );
    }

    private record CombinedRegistryLookup<T>(RegistryLookup<T> defaultLookup, RegistryLookup<T> otherLookup)
            implements HolderLookup.RegistryLookup<T> {
        @Override
        public ResourceKey<? extends Registry<? extends T>> key() {
            return defaultLookup.key();
        }

        @Override
        public Lifecycle registryLifecycle() {
            return defaultLookup.registryLifecycle();
        }

        @Override
        public Stream<Holder.Reference<T>> listElements() {
            return Stream.concat(defaultLookup.listElements(), otherLookup.listElements());
        }

        @Override
        public Stream<HolderSet.Named<T>> listTags() {
            return Stream.concat(defaultLookup.listTags(), otherLookup.listTags());
        }

        @Override
        public Optional<Holder.Reference<T>> get(ResourceKey<T> resourceKey) {
            return defaultLookup.get(resourceKey).or(() -> otherLookup.get(resourceKey))
                    .map(reference -> Holder.Reference.createStandAlone(this, resourceKey));
        }

        @Override
        public Optional<HolderSet.Named<T>> get(TagKey<T> tagKey) {
            return defaultLookup.get(tagKey).or(() -> otherLookup.get(tagKey))
                    .map(named -> HolderSet.emptyNamed(this, tagKey));
        }

        @Override
        public boolean canSerializeIn(HolderOwner<T> owner) {
            return owner == this || defaultLookup.canSerializeIn(owner) || otherLookup.canSerializeIn(owner);
        }
    }
}
