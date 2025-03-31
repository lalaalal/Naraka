package com.yummy.naraka.client.init;

import com.yummy.naraka.proxy.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class ModelLayerRegistry {
    public static void register(ModelLayerLocation location, Supplier<LayerDefinition> factory) {
        MethodInvoker.invoke(ModelLayerRegistry.class, "register", location, factory);
    }
}
