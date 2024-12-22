package com.yummy.naraka.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public class NarakaClientboundEventHandler implements NarakaClientboundEventPacket.EventHandler {
    private static final NarakaClientboundEventPacket.EventHandler INSTANCE = new NarakaClientboundEventHandler();

    public static void handle(NarakaClientboundEventPacket packet, NetworkManager.PacketContext context) {
        Minecraft.getInstance().execute(
                () -> packet.event().handle(INSTANCE)
        );
    }

    private NarakaClientboundEventHandler() {
    }

    @Override
    public void handleDeathCountUsed() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null)
            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, player.getSoundSource(), 1, 1, false);
    }
}
