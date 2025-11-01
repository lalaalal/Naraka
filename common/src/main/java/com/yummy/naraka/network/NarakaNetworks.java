package com.yummy.naraka.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NarakaNetworks {
    public static void initialize() {
        NetworkManager.clientbound().define(SyncEntityDataPacket.TYPE, SyncEntityDataPacket.CODEC);
        NetworkManager.clientbound().define(SyncAnimationPacket.TYPE, SyncAnimationPacket.CODEC);
        NetworkManager.clientbound().define(SyncAfterimagePacket.TYPE, SyncAfterimagePacket.CODEC);
        NetworkManager.clientbound().define(SyncPlayerMovementPacket.TYPE, SyncPlayerMovementPacket.CODEC);
        NetworkManager.clientbound().define(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC);
        NetworkManager.clientbound().define(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC);

        NetworkManager.serverbound().define(SkillRequestPacket.TYPE, SkillRequestPacket.CODEC);

        initializeServer();
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        NetworkManager.clientbound().register(SyncEntityDataPacket.TYPE, SyncEntityDataPacket.CODEC, SyncEntityDataPacket::handle);
        NetworkManager.clientbound().register(SyncAnimationPacket.TYPE, SyncAnimationPacket.CODEC, SyncAnimationPacket::handle);
        NetworkManager.clientbound().register(SyncAfterimagePacket.TYPE, SyncAfterimagePacket.CODEC, SyncAfterimagePacket::handle);
        NetworkManager.clientbound().register(SyncPlayerMovementPacket.TYPE, SyncPlayerMovementPacket.CODEC, SyncPlayerMovementPacket::handle);
        NetworkManager.clientbound().register(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEntityEventPacket.CODEC, NarakaClientboundEventHandler::handleEntityEvent);
        NetworkManager.clientbound().register(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventPacket.CODEC, NarakaClientboundEventHandler::handleEvent);
    }

    public static void initializeServer() {
        NetworkManager.serverbound().register(SkillRequestPacket.TYPE, SkillRequestPacket.CODEC, SkillRequestPacket::handle);
    }
}
