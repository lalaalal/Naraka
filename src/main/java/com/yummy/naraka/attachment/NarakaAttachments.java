package com.yummy.naraka.attachment;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class NarakaAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, NarakaMod.MOD_ID);

    /**
     * Use {@link StigmaHelper#getStigma(LivingEntity)} to get value of LivingEntity.
     *
     * @see StigmaHelper
     */
    public static final Supplier<AttachmentType<Integer>> STIGMA = ATTACHMENT_TYPES.register(
            "stigma",
            () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .build()
    );

    /**
     * Use {@link DeathCountHelper#getDeathCount(LivingEntity)} to get value of LivingEntity.
     * Default value is {@link DeathCountHelper#MAX_DEATH_COUNT}
     *
     * @see DeathCountHelper
     */
    public static final Supplier<AttachmentType<Integer>> DEATH_COUNT = ATTACHMENT_TYPES.register(
            "death_count",
            () -> AttachmentType.builder(() -> DeathCountHelper.maxDeathCount())
                    .serialize(Codec.INT)
                    .copyOnDeath()
                    .build()
    );

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
