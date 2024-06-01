package com.yummy.naraka.event;

import com.yummy.naraka.NarakaContext;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.attachment.AttachmentSyncHelper;
import com.yummy.naraka.entity.Herobrine;
import com.yummy.naraka.entity.NarakaEntities;
import com.yummy.naraka.networking.payload.ChangeDeathCountVisibilityPayload;
import com.yummy.naraka.networking.payload.IntAttachmentSyncHandler;
import com.yummy.naraka.networking.payload.SyncEntityIntAttachmentPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NarakaCommonEventBus {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AttachmentSyncHelper.initialize();
            IntAttachmentSyncHandler.initialize();
            NarakaContext.initialize();
        });
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("2");
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

    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(NarakaEntities.HEROBRINE.get(), Herobrine.getAttributeSupplier());
    }
}
