package com.yummy.naraka.network;

import com.yummy.naraka.Platform;
import com.yummy.naraka.service.NarakaServices;

public class NarakaNetworks {
    public static void initialize() {
        NarakaServices.SERVER_PACKET_REGISTRAR.register(SkillRequestPacket.TYPE, SkillRequestPacket::handle);

        if (Platform.getInstance().getSide() == Platform.Side.SERVER) {
            NarakaServices.SERVER_PACKET_REGISTRAR.define(SyncEntityDataPacket.TYPE);
            NarakaServices.SERVER_PACKET_REGISTRAR.define(SyncAnimationPacket.TYPE);
            NarakaServices.SERVER_PACKET_REGISTRAR.define(SyncAfterimagePacket.TYPE);
            NarakaServices.SERVER_PACKET_REGISTRAR.define(SyncPlayerMovementPacket.TYPE);
            NarakaServices.SERVER_PACKET_REGISTRAR.define(NarakaClientboundEntityEventPacket.TYPE);
            NarakaServices.SERVER_PACKET_REGISTRAR.define(NarakaClientboundEventPacket.TYPE);
            NarakaServices.SERVER_PACKET_REGISTRAR.define(AddBeamEffectPacket.TYPE);
            NarakaServices.SERVER_PACKET_REGISTRAR.define(SyncProgressOverlayExtensionPacket.TYPE);
            NarakaServices.SERVER_PACKET_REGISTRAR.define(SyncEntityMotionPacket.TYPE);
        }
    }
}
