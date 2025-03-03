package com.yummy.naraka.network;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NarakaNetworks {
    public static void initialize() {

    }

    public static void initializeServer() {
        NetworkManager.registerS2CPayloadType(SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC);
        NetworkManager.registerS2CPayloadType(SyncAnimationPayload.TYPE, SyncAnimationPayload.CODEC);
        NetworkManager.registerS2CPayloadType(SyncAfterimagePayload.TYPE, SyncAfterimagePayload.CODEC);
        NetworkManager.registerS2CPayloadType(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC);
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC, SyncEntityDataPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.s2c(), SyncAnimationPayload.TYPE, SyncAnimationPayload.CODEC, SyncAnimationPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.s2c(), SyncAfterimagePayload.TYPE, SyncAfterimagePayload.CODEC, SyncAfterimagePayload::handle);
        NetworkManager.registerReceiver(NetworkManager.s2c(), NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC, NarakaClientboundEventHandler::handle);
    }
}
