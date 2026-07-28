package com.yummy.naraka.forge.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import com.yummy.naraka.client.init.ShaderRegistry;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public final class ForgeShaderRegistry implements ShaderRegistry.Registrar, NarakaEventBus {
    private static final Logger LOG = LogUtils.getLogger();

    @Override
    public void register(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, RegisterShadersEvent.class, event -> {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(), id, format), consumer);
            } catch (IOException exception) {
                LOG.error("An error occurred registering shader {}", id);
            }
        });
    }
}
