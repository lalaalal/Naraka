package com.yummy.naraka.client;

import com.yummy.naraka.client.service.NarakaClientServices;
import com.yummy.naraka.network.*;

public class NarakaClientNetworks {
    public static ServerboundNetworkManager serverbound() {
        return NarakaClientServices.SERVERBOUND_NETWORK_MANAGER;
    }

    public static void initialize() {
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(SyncEntityDataPacket.TYPE, SyncEntityDataPacket::handle);
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(SyncAnimationPacket.TYPE, SyncAnimationPacket::handle);
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(SyncAfterimagePacket.TYPE, SyncAfterimagePacket::handle);
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(SyncPlayerMovementPacket.TYPE, SyncPlayerMovementPacket::handle);
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEventHandler::handleEntityEvent);
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventHandler::handleEvent);
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(AddBeamEffectPacket.TYPE, AddBeamEffectPacket::handle);
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(SyncProgressOverlayExtensionPacket.TYPE, SyncProgressOverlayExtensionHandler::handle);
        NarakaClientServices.CLIENT_PACKET_REGISTRAR.register(SyncEntityMotionPacket.TYPE, SyncEntityMotionPacket::handle);
    }
}
