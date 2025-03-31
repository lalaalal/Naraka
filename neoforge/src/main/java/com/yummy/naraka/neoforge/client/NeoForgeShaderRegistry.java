package com.yummy.naraka.neoforge.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.ShaderRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import com.yummy.naraka.proxy.MethodProxy;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Consumer;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class NeoForgeShaderRegistry implements NarakaEventBus {
    @MethodProxy(ShaderRegistry.class)
    public static void register(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        NARAKA_BUS.addListener(RegisterShadersEvent.class, event -> {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(), id, format), consumer);
            } catch (IOException exception) {
                NarakaMod.LOGGER.error("An error occurred registering shader {}", id);
            }
        });
    }
}
