package com.yummy.naraka.network;

public class NarakaNetworks {
    public static void initialize() {
        NetworkManager.clientbound().register(SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC, SyncEntityDataPayload::handle);
        NetworkManager.clientbound().register(SyncAnimationPayload.TYPE, SyncAnimationPayload.CODEC, SyncAnimationPayload::handle);
        NetworkManager.clientbound().register(SyncAfterimagePayload.TYPE, SyncAfterimagePayload.CODEC, SyncAfterimagePayload::handle);
        NetworkManager.clientbound().register(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC, NarakaClientboundEventHandler::handleEntityEvent);
        NetworkManager.clientbound().register(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC, NarakaClientboundEventHandler::handleEvent);

        NetworkManager.serverbound().register(SkillRequestPayload.TYPE, SkillRequestPayload.CODEC, SkillRequestPayload::handle);
    }
}
