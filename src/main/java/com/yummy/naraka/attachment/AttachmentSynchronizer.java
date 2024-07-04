package com.yummy.naraka.attachment;

import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.text.html.Option;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.NarakaRegistries;

import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public abstract class AttachmentSynchronizer {
    public static final Codec<Holder<AttachmentSynchronizer>> CODEC = RegistryFixedCodec.create(NarakaRegistries.ATTACHMENT_SYNCHRONIZER);

    protected final Supplier<Holder<AttachmentSynchronizer>> holderSupplier;

    public AttachmentSynchronizer(Supplier<Holder<AttachmentSynchronizer>> holdSupplier) {
        this.holderSupplier = holdSupplier;
    }

    public void handle(Player player, Entity target, Object rawValue) {
        if (player.level().isClientSide)
            handleClient(player, target, value(rawValue));
        else
            handleServer(player, target, value(rawValue));
    }

    protected abstract void handleClient(Player player, Entity target, AttachmentValue attachmentValue);
    protected abstract void handleServer(Player player, Entity target, AttachmentValue attachmentValue);

    protected static AttachmentValue value(Object value) {
        return new AttachmentValue(value);
    }

    protected static class AttachmentValue {
        private final Object value;

        protected AttachmentValue(Object value) {
            this.value = value;
        }

        public <T> Optional<T> get(Class<T> type) {
            if (type.isInstance(value))
                return Optional.of(type.cast(value));
            return Optional.empty();
        }

        public <T> T getOrThrow(Class<T> type) {
            if (type.isInstance(value))
                return type.cast(value);
            throw new IllegalStateException("Cannot convert %s to (%s)".formatted(value, type));
        }
    }
}
