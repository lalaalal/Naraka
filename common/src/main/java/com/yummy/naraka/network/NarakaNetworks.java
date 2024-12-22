package com.yummy.naraka.network;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NarakaNetworks {
    public static void initialize() {

    }

    public static void initializeServer() {
        NetworkManager.registerS2CPayloadType(SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC);
        NetworkManager.registerS2CPayloadType(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC);
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC, SyncEntityDataPayload::handleClient);
        NetworkManager.registerReceiver(NetworkManager.s2c(), NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC, NarakaClientboundEventPacket::handle);
    }
}
