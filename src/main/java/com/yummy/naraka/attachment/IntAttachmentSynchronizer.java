package com.yummy.naraka.attachment;

import java.util.function.Supplier;

import com.yummy.naraka.network.payload.SyncEntityIntAttachmentPayload;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;

public class IntAttachmentSynchronizer extends AttachmentSynchronizer {
    private final Supplier<AttachmentType<Integer>> attachmentType;

    public IntAttachmentSynchronizer(Supplier<AttachmentType<Integer>> attachmentType, Supplier<Holder<AttachmentSynchronizer>> holderSupplier) {
        super(holderSupplier);
        this.attachmentType = attachmentType;
    }

    @Override
    protected void handleClient(Player player, Entity target, AttachmentValue attachmentValue) {
        int value = attachmentValue.getOrThrow(Integer.class);
        if (player instanceof ServerPlayer serverPlayer) {
            SyncEntityIntAttachmentPayload payload = new SyncEntityIntAttachmentPayload(target, value, holderSupplier.get());
            serverPlayer.connection.send(payload);
        }
    }

    @Override
    protected void handleServer(Player player, Entity target, AttachmentValue attachmentValue) {
        int value = attachmentValue.getOrThrow(Integer.class);
        if (target instanceof LivingEntity livingEntity)
            livingEntity.setData(attachmentType, value);
    }
}
