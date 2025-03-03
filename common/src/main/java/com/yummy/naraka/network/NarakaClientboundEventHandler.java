package com.yummy.naraka.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class NarakaClientboundEventHandler {
    public static void handle(NarakaClientboundEntityEventPacket packet, NetworkManager.PacketContext context) {
        Minecraft.getInstance().execute(() -> {
            Level level = context.getPlayer().level();
            Entity entity = level.getEntity(packet.entityId());
            if (entity != null)
                packet.event().handle(entity);
        });
    }
}
