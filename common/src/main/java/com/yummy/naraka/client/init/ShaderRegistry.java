package com.yummy.naraka.client.init;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public abstract class ShaderRegistry {
    public static void register(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        MethodInvoker.invoke(ShaderRegistry.class, "register", id, format, consumer);
    }
}
