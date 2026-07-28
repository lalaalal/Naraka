package com.yummy.naraka.forge.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.features.NarakaConfiguredFeatures;
import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.forge.init.ForgeBiomeModificationRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NarakaDataGenerator {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.PLACED_FEATURE, NarakaPlacements::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, NarakaConfiguredFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ForgeBiomeModificationRegistry::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        NarakaMod.isDataGeneration = true;

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, registries, BUILDER, Set.of("minecraft", "naraka")));
        generator.addProvider(event.includeServer(), new NarakaForgeBlockTagsProvider(output, registries, fileHelper));
        generator.addProvider(event.includeServer(), new NarakaForgeBiomeTagsProvider(output, registries, fileHelper));
        generator.addProvider(event.includeServer(), new NarakaForgeEntityTypeTagsProvider(output, registries, fileHelper));
    }
}
