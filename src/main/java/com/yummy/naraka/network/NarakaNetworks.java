package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.network.payload.ChangeDeathCountVisibilityPayload;
import com.yummy.naraka.network.payload.SyncEntityAttachmentPayload;
import com.yummy.naraka.network.payload.SyncEntityIntAttachmentPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NarakaNetworks {
    public static void initialize() {
        SyncEntityAttachmentPayload.registerFactory(Integer.class, SyncEntityIntAttachmentPayload::createForInteger);
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("3");
        registrar.playBidirectional(
                SyncEntityIntAttachmentPayload.TYPE,
                SyncEntityIntAttachmentPayload.CODEC,
                SyncEntityIntAttachmentPayload::handle
        );
        registrar.playToClient(
                ChangeDeathCountVisibilityPayload.TYPE,
                ChangeDeathCountVisibilityPayload.CODEC,
                ChangeDeathCountVisibilityPayload::handle
        );
    }
}
