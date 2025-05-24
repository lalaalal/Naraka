package com.yummy.naraka.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NarakaNetworks {
    public static void initialize() {
        NetworkManager.clientbound().define(SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC);
        NetworkManager.clientbound().define(SyncAnimationPayload.TYPE, SyncAnimationPayload.CODEC);
        NetworkManager.clientbound().define(SyncAfterimagePayload.TYPE, SyncAfterimagePayload.CODEC);
        NetworkManager.clientbound().define(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC);
        NetworkManager.clientbound().define(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC);

        NetworkManager.serverbound().define(SkillRequestPayload.TYPE, SkillRequestPayload.CODEC);

        initializeServer();
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        NetworkManager.clientbound().register(SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC, SyncEntityDataPayload::handle);
        NetworkManager.clientbound().register(SyncAnimationPayload.TYPE, SyncAnimationPayload.CODEC, SyncAnimationPayload::handle);
        NetworkManager.clientbound().register(SyncAfterimagePayload.TYPE, SyncAfterimagePayload.CODEC, SyncAfterimagePayload::handle);
        NetworkManager.clientbound().register(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC, NarakaClientboundEventHandler::handleEntityEvent);
        NetworkManager.clientbound().register(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC, NarakaClientboundEventHandler::handleEvent);
    }

    public static void initializeServer() {
        NetworkManager.serverbound().register(SkillRequestPayload.TYPE, SkillRequestPayload.CODEC, SkillRequestPayload::handle);
    }
}
