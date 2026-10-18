package com.yummy.naraka.network;

import com.yummy.naraka.client.renderer.UniformLayoutRegistry;
import net.minecraft.client.Minecraft;

public class PostEffectConfigHandler {
    public static void handle(PostEffectConfigPacket packet, NetworkManager.Context context) {
        Minecraft.getInstance().execute(() -> {
            UniformLayoutRegistry.getInstance().setConfig(context.level().getGameTime(), packet.config());
        });
    }
}
