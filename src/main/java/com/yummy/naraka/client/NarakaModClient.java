package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.HerobrineRenderer;
import com.yummy.naraka.client.renderer.SpearRenderer;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;

public class NarakaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.HEROBRINE, HerobrineModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.SPEAR, SpearModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.SPEAR_OF_LONGINUS, SpearOfLonginusModel::createBodyLayer);

        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, SpearRenderer::longinus);

        CoreShaderRegistrationCallback.EVENT.register(context -> {
            context.register(NarakaMod.location("longinus"), DefaultVertexFormat.POSITION, shaderInstance -> {
                NarakaShaders.longinus = shaderInstance;
            });
        });
        ClientTickEvents.START_CLIENT_TICK.register(new ClientTickEvents.StartTick() {
            @Override
            public void onStartTick(Minecraft client) {
                ComponentStyles.tick();
            }
        });

    }
}
