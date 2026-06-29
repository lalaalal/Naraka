package com.yummy.naraka.forge.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.ShaderRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.io.IOException;
import java.util.function.Consumer;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ForgeShaderRegistry implements NarakaEventBus {
    @MethodProxy(ShaderRegistry.class)
    public static void register(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, RegisterShadersEvent.class, event -> {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(), id, format), consumer);
            } catch (IOException exception) {
                NarakaMod.LOGGER.error("An error occurred registering shader {}", id);
            }
        });
    }
}
