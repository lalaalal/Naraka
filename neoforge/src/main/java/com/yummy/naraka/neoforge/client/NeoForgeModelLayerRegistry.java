package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.ModelLayerRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class NeoForgeModelLayerRegistry implements NarakaEventBus, ModelLayerRegistry.Registrar {
    @Override
    public void register(ModelLayerLocation location, Supplier<LayerDefinition> factory) {
        NARAKA_BUS.addListener(EntityRenderersEvent.RegisterLayerDefinitions.class, event -> {
            event.registerLayerDefinition(location, factory);
        });
    }
}
