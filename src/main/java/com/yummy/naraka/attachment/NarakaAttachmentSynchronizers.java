package com.yummy.naraka.attachment;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.NarakaRegistries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaAttachmentSynchronizers {
    private static final DeferredRegister<AttachmentSynchronizer> ATTACHMENT_SYNCHRONIZERS = DeferredRegister.create(NarakaRegistries.ATTACHMENT_SYNCHRONIZER, NarakaMod.MOD_ID);

    public static final DeferredHolder<AttachmentSynchronizer, IntAttachmentSynchronizer> STIGMA_SYNCHRONIZER = ATTACHMENT_SYNCHRONIZERS.register(
            "stigma_synchronizer",
            () -> new IntAttachmentSynchronizer(NarakaAttachments.STIGMA, () -> NarakaAttachmentSynchronizers.STIGMA_SYNCHRONIZER)
    );

    public static final DeferredHolder<AttachmentSynchronizer, IntAttachmentSynchronizer> DEATH_COUNT_SYNCHRONIZER = ATTACHMENT_SYNCHRONIZERS.register(
            "death_count_synchronizer",
            () -> new IntAttachmentSynchronizer(NarakaAttachments.DEATH_COUNT, () -> NarakaAttachmentSynchronizers.DEATH_COUNT_SYNCHRONIZER)
    );

    public static void register(IEventBus bus) {
        ATTACHMENT_SYNCHRONIZERS.register(bus);
    }
}
