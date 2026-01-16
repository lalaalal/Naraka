package com.yummy.naraka.neoforge.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.neoforge.init.NeoForgeBiomeModificationRegistry;
import net.minecraft.core.RegistrySetBuilder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;

@EventBusSubscriber(modid = NarakaMod.MOD_ID)
public class NarakaDataGenerator {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NeoForgeBiomeModificationRegistry::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        NarakaMod.isDataGeneration = true;

        event.createDatapackRegistryObjects(BUILDER, Set.of("minecraft", "naraka"));
    }
}
