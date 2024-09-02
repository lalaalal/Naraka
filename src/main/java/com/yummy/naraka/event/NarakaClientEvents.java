package com.yummy.naraka.event;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaShaders;
import com.yummy.naraka.util.ComponentStyles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.Minecraft;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class NarakaClientEvents {
    public static void initialize() {
        CoreShaderRegistrationCallback.EVENT.register(NarakaClientEvents::registerShaders);
        ClientTickEvents.START_CLIENT_TICK.register(NarakaClientEvents::onClientTick);
    }

    private static void registerShaders(CoreShaderRegistrationCallback.RegistrationContext context) throws IOException {
        context.register(NarakaMod.location("longinus"), DefaultVertexFormat.POSITION, shaderInstance -> {
            NarakaShaders.longinus = shaderInstance;
        });
    }

    private static void onClientTick(Minecraft minecraft) {
        ComponentStyles.tick();
    }
}
