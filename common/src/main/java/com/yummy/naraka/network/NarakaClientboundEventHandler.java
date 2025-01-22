package com.yummy.naraka.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class NarakaClientboundEventHandler implements NarakaClientboundEventPacket.EventHandler {
    private static final NarakaClientboundEventPacket.EventHandler INSTANCE = new NarakaClientboundEventHandler();

    public static void handle(NarakaClientboundEventPacket packet, NetworkManager.PacketContext context) {
        Minecraft.getInstance().execute(
                () -> packet.event().handle(INSTANCE, context)
        );
    }

    private NarakaClientboundEventHandler() {
    }
}
