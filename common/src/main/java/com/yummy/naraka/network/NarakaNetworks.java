package com.yummy.naraka.network;

import com.yummy.naraka.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NarakaNetworks {
    public static void initialize() {
        if (Platform.getInstance().getSide() == Platform.Side.SERVER) {
            NetworkManager.clientbound().define(SyncEntityDataPacket.TYPE);
            NetworkManager.clientbound().define(SyncAnimationPacket.TYPE);
            NetworkManager.clientbound().define(SyncAfterimagePacket.TYPE);
            NetworkManager.clientbound().define(SyncPlayerMovementPacket.TYPE);
            NetworkManager.clientbound().define(NarakaClientboundEntityEventPacket.TYPE);
            NetworkManager.clientbound().define(NarakaClientboundEventPacket.TYPE);
            NetworkManager.clientbound().define(AddBeamEffectPacket.TYPE);
            NetworkManager.clientbound().define(SyncProgressOverlayExtensionPacket.TYPE);
            NetworkManager.clientbound().define(SyncEntityMotionPacket.TYPE);
        }

        initializeServer();
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        NetworkManager.clientbound().register(SyncEntityDataPacket.TYPE, SyncEntityDataPacket::handle);
        NetworkManager.clientbound().register(SyncAnimationPacket.TYPE, SyncAnimationPacket::handle);
        NetworkManager.clientbound().register(SyncAfterimagePacket.TYPE, SyncAfterimagePacket::handle);
        NetworkManager.clientbound().register(SyncPlayerMovementPacket.TYPE, SyncPlayerMovementPacket::handle);
        NetworkManager.clientbound().register(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEventHandler::handleEntityEvent);
        NetworkManager.clientbound().register(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventHandler::handleEvent);
        NetworkManager.clientbound().register(AddBeamEffectPacket.TYPE, AddBeamEffectPacket::handle);
        NetworkManager.clientbound().register(SyncProgressOverlayExtensionPacket.TYPE, SyncProgressOverlayExtensionHandler::handle);
        NetworkManager.clientbound().register(SyncEntityMotionPacket.TYPE, SyncEntityMotionPacket::handle);
    }

    public static void initializeServer() {
        NetworkManager.serverbound().register(SkillRequestPacket.TYPE, SkillRequestPacket::handle);
    }
}
