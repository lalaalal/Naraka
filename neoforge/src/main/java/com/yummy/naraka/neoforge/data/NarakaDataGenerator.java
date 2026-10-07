package com.yummy.naraka.neoforge.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.features.NarakaFeatures;
import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.neoforge.init.NeoForgeBiomeModificationRegistry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;

@EventBusSubscriber(modid = NarakaMod.MOD_ID)
public class NarakaDataGenerator {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.FEATURE, NarakaFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, NarakaPlacements::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NeoForgeBiomeModificationRegistry::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        NarakaMod.isDataGeneration = true;

        event.createWorldRegistryObjects(BUILDER, Set.of("minecraft", "naraka"));
    }
}
