package com.yummy.naraka.event;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.attachment.AttachmentSyncHelper;
import com.yummy.naraka.entity.Herobrine;
import com.yummy.naraka.entity.NarakaEntities;
import com.yummy.naraka.gui.layer.DeathCountLayer;
import com.yummy.naraka.gui.layer.NarakaGuiLayers;
import com.yummy.naraka.gui.layer.StigmaLayer;
import com.yummy.naraka.networking.payload.IntAttachmentSyncHandler;
import com.yummy.naraka.networking.payload.SyncEntityIntAttachmentPayload;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NarakaCommonEventBus {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AttachmentSyncHelper.initialize();
            IntAttachmentSyncHandler.initialize();
        });
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                SyncEntityIntAttachmentPayload.TYPE,
                SyncEntityIntAttachmentPayload.CODEC,
                SyncEntityIntAttachmentPayload::handle
        );
    }

    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(NarakaEntities.HEROBRINE.get(), Herobrine.getAttributeSupplier());
    }
}
