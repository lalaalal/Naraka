package com.yummy.naraka.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NarakaNetworks {
    public static void initialize() {
        NetworkManager.registerS2C(SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC);
        NetworkManager.registerS2C(SyncAnimationPayload.TYPE, SyncAnimationPayload.CODEC);
        NetworkManager.registerS2C(SyncAfterimagePayload.TYPE, SyncAfterimagePayload.CODEC);
        NetworkManager.registerS2C(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC);
        NetworkManager.registerS2C(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC);
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        NetworkManager.registerClientHandler(SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC, SyncEntityDataPayload::handle);
        NetworkManager.registerClientHandler(SyncAnimationPayload.TYPE, SyncAnimationPayload.CODEC, SyncAnimationPayload::handle);
        NetworkManager.registerClientHandler(SyncAfterimagePayload.TYPE, SyncAfterimagePayload.CODEC, SyncAfterimagePayload::handle);
        NetworkManager.registerClientHandler(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC, NarakaClientboundEventHandler::handleEntityEvent);
        NetworkManager.registerClientHandler(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC, NarakaClientboundEventHandler::handleEvent);
    }
}
