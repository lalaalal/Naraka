package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ModelLayerRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricModelLayerRegistry {
    @MethodProxy(ModelLayerRegistry.class)
    public static void register(ModelLayerLocation location, Supplier<LayerDefinition> factory) {
        EntityModelLayerRegistry.registerModelLayer(location, factory::get);
    }
}
