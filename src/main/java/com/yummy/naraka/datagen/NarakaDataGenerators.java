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

@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NarakaDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new NarakaWorldGenProvider(packOutput, provider));
        generator.addProvider(event.includeServer(), new NarakaEntityTypeTagsProvider(packOutput, provider, existingFileHelper));
        generator.addProvider(event.includeServer(), new NarakaDamageTypeTagsProvider(packOutput, provider, existingFileHelper));

        generator.addProvider(event.includeClient(), new NarakaItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new NarakaBlockStateProvider(packOutput, existingFileHelper));        
        generator.addProvider(event.includeClient(), new NarakaLanguageProvider.EN(packOutput));
        generator.addProvider(event.includeClient(), new NarakaLanguageProvider.KR(packOutput));
    }
}
