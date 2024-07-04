package com.yummy.naraka.network.payload;

import com.yummy.naraka.attachment.AttachmentSynchronizer;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public abstract class SyncEntityAttachmentPayload<T> implements CustomPacketPayload {
    protected final int targetEntityId;
    protected final T value;
    protected final Holder<AttachmentSynchronizer> synchronizer;

    public SyncEntityAttachmentPayload(int targetEntityId, T value, Holder<AttachmentSynchronizer> synchronizer) {
        this.targetEntityId = targetEntityId;
        this.value = value;
        this.synchronizer = synchronizer;
    }

    public SyncEntityAttachmentPayload(Entity target, T value, Holder<AttachmentSynchronizer> synchronizer) {
        this(target.getId(), value, synchronizer);
    }

    public int getTargetEntityId() {
        return targetEntityId;
    }

    public T getValue() {
        return value;
    }

    public Holder<AttachmentSynchronizer> getSynchronizer() {
        return synchronizer;
    }

    public void synchronize(Player player) {
        Entity target = player.level().getEntity(targetEntityId);
        if (target == null)
            throw new IllegalStateException();
        synchronizer.value().handle(player, target, value);
    }
}
