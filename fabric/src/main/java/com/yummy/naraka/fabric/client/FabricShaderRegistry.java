package com.yummy.naraka.fabric.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.client.init.ShaderRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricShaderRegistry {
    @MethodProxy(ShaderRegistry.class)
    public static void register(ResourceLocation id, VertexFormat format, Consumer<ShaderProgram> consumer) {
        
    }
}
