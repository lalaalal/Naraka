package com.yummy.naraka.network;

import com.yummy.naraka.Platform;
import com.yummy.naraka.invoker.MethodInvoker;

public class NarakaNetworks {
    private static final PacketRegistrar SERVER_PACKET_REGISTRAR = getServerPacketRegistrar();

    public static PacketRegistrar getServerPacketRegistrar() {
        return MethodInvoker.of(NarakaNetworks.class, "getServerPacketRegistrar")
                .invoke()
                .result(PacketRegistrar.class);
    }

    public static void initialize() {
        SERVER_PACKET_REGISTRAR.register(SkillRequestPacket.TYPE, SkillRequestPacket::handle);

        if (Platform.getInstance().getSide() == Platform.Side.SERVER) {
            SERVER_PACKET_REGISTRAR.define(SyncEntityDataPacket.TYPE);
            SERVER_PACKET_REGISTRAR.define(SyncAnimationPacket.TYPE);
            SERVER_PACKET_REGISTRAR.define(SyncAfterimagePacket.TYPE);
            SERVER_PACKET_REGISTRAR.define(SyncPlayerMovementPacket.TYPE);
            SERVER_PACKET_REGISTRAR.define(NarakaClientboundEntityEventPacket.TYPE);
            SERVER_PACKET_REGISTRAR.define(NarakaClientboundEventPacket.TYPE);
            SERVER_PACKET_REGISTRAR.define(AddBeamEffectPacket.TYPE);
            SERVER_PACKET_REGISTRAR.define(SyncProgressOverlayExtensionPacket.TYPE);
            SERVER_PACKET_REGISTRAR.define(SyncEntityMotionPacket.TYPE);
        }
    }
}
