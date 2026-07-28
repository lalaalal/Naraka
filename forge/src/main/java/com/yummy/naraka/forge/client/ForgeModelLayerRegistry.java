package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.ModelLayerRegistry;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class ForgeModelLayerRegistry implements ModelLayerRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(ModelLayerLocation location, Supplier<LayerDefinition> factory) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterLayerDefinitions.class, event -> {
            event.registerLayerDefinition(location, factory);
        });
    }
}
