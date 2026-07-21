package com.yummy.naraka.client;

import com.yummy.naraka.invoker.MethodInvoker;
import com.yummy.naraka.network.*;

public class NarakaClientNetworks {
    private static final ServerboundNetworkManager SERVERBOUND = MethodInvoker.of(NarakaClientNetworks.class, "serverbound")
            .invoke().result(ServerboundNetworkManager.class);

    private static final PacketRegistrar CLIENT_PACKET_REGISTRAR = getClientPacketRegistrar();

    public static ServerboundNetworkManager serverbound() {
        return SERVERBOUND;
    }

    public static PacketRegistrar getClientPacketRegistrar() {
        return MethodInvoker.of(NarakaClientNetworks.class, "getClientPacketRegistrar")
                .invoke()
                .result(PacketRegistrar.class);
    }

    public static void initialize() {
        CLIENT_PACKET_REGISTRAR.register(SyncEntityDataPacket.TYPE, SyncEntityDataPacket::handle);
        CLIENT_PACKET_REGISTRAR.register(SyncAnimationPacket.TYPE, SyncAnimationPacket::handle);
        CLIENT_PACKET_REGISTRAR.register(SyncAfterimagePacket.TYPE, SyncAfterimagePacket::handle);
        CLIENT_PACKET_REGISTRAR.register(SyncPlayerMovementPacket.TYPE, SyncPlayerMovementPacket::handle);
        CLIENT_PACKET_REGISTRAR.register(NarakaClientboundEntityEventPacket.TYPE, NarakaClientboundEventHandler::handleEntityEvent);
        CLIENT_PACKET_REGISTRAR.register(NarakaClientboundEventPacket.TYPE, NarakaClientboundEventHandler::handleEvent);
        CLIENT_PACKET_REGISTRAR.register(AddBeamEffectPacket.TYPE, AddBeamEffectPacket::handle);
        CLIENT_PACKET_REGISTRAR.register(SyncProgressOverlayExtensionPacket.TYPE, SyncProgressOverlayExtensionHandler::handle);
        CLIENT_PACKET_REGISTRAR.register(SyncEntityMotionPacket.TYPE, SyncEntityMotionPacket::handle);
    }
}
