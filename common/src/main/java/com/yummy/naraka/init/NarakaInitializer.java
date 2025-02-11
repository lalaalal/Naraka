package com.yummy.naraka.init;

import com.yummy.naraka.Platform;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.item.NarakaCreativeModTabs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;

public interface NarakaInitializer extends RegistryLoadedListener {
    Platform getPlatform();

    RegistryInitializer getRegistryInitializer();

    RegistryFactory getRegistryFactory();

    void modifyCreativeModeTab(ResourceKey<CreativeModeTab> tabKey, Consumer<NarakaCreativeModTabs.TabEntries> entries);

    NarakaBiomes.Modifier getBiomeModifier();

    void registerEntityDataSerializer(String name, EntityDataSerializer<?> serializer);
}
