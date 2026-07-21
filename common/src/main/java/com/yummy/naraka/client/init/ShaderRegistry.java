package com.yummy.naraka.client.init;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public abstract class ShaderRegistry {
    public static void register(ResourceLocation location, VertexFormat vertexFormat, Consumer<ShaderInstance> consumer) {
        MethodInvoker.of(ShaderRegistry.class, "register")
                .invoke(location, vertexFormat, consumer);
    }
}
