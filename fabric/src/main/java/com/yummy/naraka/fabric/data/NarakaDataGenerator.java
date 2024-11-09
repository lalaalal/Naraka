package com.yummy.naraka.fabric.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.fabric.data.advancement.NarakaAdvancementProvider;
import com.yummy.naraka.fabric.data.lang.NarakaLanguageProviders;
import com.yummy.naraka.fabric.data.loot.NarakaBlockLootProvider;
import com.yummy.naraka.fabric.data.loot.NarakaEntityLootProvider;
import com.yummy.naraka.fabric.data.tags.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NarakaDataGenerator implements DataGeneratorEntrypoint {
    private @Nullable CompletableFuture<HolderLookup.Provider> patched;

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        NarakaMod.isDataGeneration = true;
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        NarakaDatapackProvider datapackProvider = pack.addProvider(NarakaDatapackProvider::new);
        patched = datapackProvider.getRegistryProvider();

        new NarakaLanguageProviders("en_us", "ko_kr")
                .addProvidersTo(pack::addProvider);
        pack.addProvider(NarakaRecipeProvider::new);
        pack.addProvider(NarakaModelProvider::new);

        pack.addProvider(NarakaBlockLootProvider::new);
        pack.addProvider(NarakaEntityLootProvider::new);
        pack.addProvider(NarakaBlockTagsProvider::new);
        pack.addProvider(NarakaItemTagsProvider::new);
        pack.addProvider(NarakaEntityTypeTagsProvider::new);
        pack.addProvider(patched(NarakaBiomeTagsProvider::new));
        pack.addProvider(patched(NarakaAdvancementProvider::new));
        pack.addProvider(patched(NarakaDamageTypeTagsProvider::new));
        pack.addProvider(patched(NarakaPlacementTagsProvider::new));
        pack.addProvider(patched(NarakaStructureSetsTagProvider::new));
    }

    private class PatchedRegistryFactory<T extends DataProvider> implements FabricDataGenerator.Pack.Factory<T> {
        private final FabricDataGenerator.Pack.RegistryDependentFactory<T> factory;

        private PatchedRegistryFactory(FabricDataGenerator.Pack.RegistryDependentFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public T create(FabricDataOutput output) {
            if (patched == null)
                throw new IllegalStateException();
            return factory.create(output, patched);
        }
    }

    private <T extends DataProvider> FabricDataGenerator.Pack.Factory<T> patched(FabricDataGenerator.Pack.RegistryDependentFactory<T> factory) {
        return new PatchedRegistryFactory<>(factory);
    }
}
