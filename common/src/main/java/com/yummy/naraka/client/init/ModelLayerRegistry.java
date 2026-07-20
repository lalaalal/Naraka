package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public abstract class ModelLayerRegistry {
    public static void register(ModelLayerLocation location, Supplier<LayerDefinition> factory) {
        MethodInvoker.invoke(ModelLayerRegistry.class, "register", location, factory);
    }
}
