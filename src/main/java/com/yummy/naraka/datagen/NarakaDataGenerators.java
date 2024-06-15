package com.yummy.naraka.datagen;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NarakaDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        NarakaDatapackProvider datapackProvider = new NarakaDatapackProvider(packOutput, event.getLookupProvider());
        CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), datapackProvider);
        NarakaBlockTagsProvider blockTagsProvider = new NarakaBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new NarakaItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new NarakaEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new NarakaDamageTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeClient(), new NarakaItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new NarakaBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new NarakaLanguageProvider.EN(packOutput));
        generator.addProvider(event.includeClient(), new NarakaLanguageProvider.KR(packOutput));
    }
}
