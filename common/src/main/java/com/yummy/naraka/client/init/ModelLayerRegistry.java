package com.yummy.naraka.client.init;

import com.yummy.naraka.client.service.NarakaClientServices;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public abstract class ModelLayerRegistry {
    public static void register(ModelLayerLocation location, Supplier<LayerDefinition> factory) {
        NarakaClientServices.MODEL_LAYER_REGISTRY.register(location, factory);
    }

    public interface Registrar {
        void register(ModelLayerLocation location, Supplier<LayerDefinition> factory);
    }
}
