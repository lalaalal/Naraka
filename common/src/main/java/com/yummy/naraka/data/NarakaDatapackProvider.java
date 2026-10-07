package com.yummy.naraka.data;

import com.yummy.naraka.advancements.NarakaAdvancementProvider;
import com.yummy.naraka.data.worldgen.NarakaBiomeData;
import com.yummy.naraka.data.worldgen.NarakaDimensionTypes;
import com.yummy.naraka.data.worldgen.NarakaStructureSets;
import com.yummy.naraka.data.worldgen.NarakaStructures;
import com.yummy.naraka.data.worldgen.features.NarakaFeatures;
import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.item.NarakaJukeboxSongs;
import com.yummy.naraka.world.item.enchantment.NarakaEnchantments;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimPatterns;
import com.yummy.naraka.world.item.trading.NarakaVillagerTrades;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.RegistryDataLoader;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class NarakaDatapackProvider extends RegistriesDatapackGenerator {
    private static final RegistrySetBuilder WORLD_BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, NarakaDamageTypes::bootstrap)
            .add(Registries.ENCHANTMENT, NarakaEnchantments::bootstrap)
            .add(Registries.FEATURE, NarakaFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, NarakaPlacements::bootstrap)
            .add(Registries.STRUCTURE, NarakaStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, NarakaStructureSets::bootstrap)
            .add(Registries.TRIM_PATTERN, NarakaTrimPatterns::bootstrap)
            .add(Registries.TRIM_MATERIAL, NarakaTrimMaterials::bootstrap)
            .add(Registries.JUKEBOX_SONG, NarakaJukeboxSongs::bootstrap)
            .add(Registries.BIOME, NarakaBiomeData::bootstrap)
            .add(Registries.DIMENSION_TYPE, NarakaDimensionTypes::bootstrap)
            .add(Registries.VILLAGER_TRADE, NarakaVillagerTrades::bootstrap);

    private static final RegistrySetBuilder RELOADABLE_LAYER = new RegistrySetBuilder()
            .add(Registries.ADVANCEMENT, NarakaAdvancementProvider.create())
            .add(NarakaRecipeProvider.create());

    private final CompletableFuture<HolderLookup.Provider> registries;

    public NarakaDatapackProvider(PackOutput output, String name, Collection<RegistryDataLoader.RegistryData<?>> registryData, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries) {
        super(output, name, registryData, registries.thenApply(RegistrySetBuilder.PatchedRegistries::patches));
        this.registries = registries.thenApply(RegistrySetBuilder.PatchedRegistries::full);
    }

    public static NarakaDatapackProvider forWorldLookup(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        CompletableFuture<RegistrySetBuilder.PatchedRegistries> patchedRegistries = createLookup(
                registries,
                WORLD_BUILDER
        );
        return new NarakaDatapackProvider(output, "world", RegistryDataLoader.WORLD_REGISTRIES, patchedRegistries);
    }

    public static NarakaDatapackProvider forReloadableLookup(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, CompletableFuture<HolderLookup.Provider> vanilla) {
        CompletableFuture<RegistrySetBuilder.PatchedRegistries> patchedRegistries = createLookup(
                registries,
                RELOADABLE_LAYER
        );
        return new NarakaDatapackProvider(output, "reloadable", RegistryDataLoader.RELOADABLE_REGISTRIES, patchedRegistries);
    }

    public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(
            final CompletableFuture<HolderLookup.Provider> vanilla, final RegistrySetBuilder packBuilder
    ) {
        return vanilla.thenApply(parent -> {
            RegistryAccess.Frozen staticRegistries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
            Cloner.Factory cloner = new Cloner.Factory();
            RegistryDataLoader.WORLD_REGISTRIES.forEach(registryData -> registryData.runWithArguments(cloner::addCodec));
            RegistryDataLoader.RELOADABLE_REGISTRIES.forEach(registryData -> registryData.runWithArguments(cloner::addCodec));

            return packBuilder.buildPatch(staticRegistries, parent, cloner);
        });
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return super.run(cache);
    }

    public CompletableFuture<HolderLookup.Provider> patched() {
        return registries;
    }
}
