package com.yummy.naraka.neoforge.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import com.yummy.naraka.client.init.ShaderRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public final class NeoForgeShaderRegistry implements NarakaEventBus, ShaderRegistry.Registrar {
    private static final Logger LOG = LogUtils.getLogger();

    @Override
    public void register(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        NARAKA_BUS.addListener(RegisterShadersEvent.class, event -> {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(), id, format), consumer);
            } catch (IOException exception) {
                LOG.error("An error occurred registering shader {}", id);
            }
        });
    }
}
