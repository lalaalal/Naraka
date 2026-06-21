package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public final class FabricModelLayerRegistry implements ModelLayerRegistry.Registrar {
    @Override
    public void register(ModelLayerLocation location, Supplier<LayerDefinition> factory) {
        net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry.registerModelLayer(location, factory::get);
    }
}
