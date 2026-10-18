package com.yummy.naraka.network.handler;

import com.yummy.naraka.client.renderer.uniform.UniformLayoutRegistry;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.packet.PostEffectConfigPacket;
import net.minecraft.client.Minecraft;

public class PostEffectConfigHandler {
    public static void handle(PostEffectConfigPacket packet, NetworkManager.Context context) {
        Minecraft.getInstance().execute(() -> {
            UniformLayoutRegistry.getInstance().setConfig(context.player().tickCount, packet.config());
        });
    }
}
