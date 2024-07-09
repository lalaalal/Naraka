package com.yummy.naraka.network.payload;

import com.yummy.naraka.network.AttachmentTypeProvider;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.HashMap;
import java.util.Map;

public abstract class SyncEntityAttachmentPayload<T, P extends AttachmentTypeProvider<T>> implements CustomPacketPayload {
    private static final Map<Class<?>, Factory> factories = new HashMap<>();

    protected final int targetEntityId;
    protected final P attachmentTypeProvider;
    protected final AttachmentType<T> attachmentType;
    protected final T value;

    public static void registerFactory(Class<?> type, Factory factory) {
        factories.put(type, factory);
    }

    public static <T> SyncEntityAttachmentPayload<?, ?> create(Entity target, AttachmentType<T> attachmentType) {
        T value = target.getData(attachmentType);
        Factory factory = factories.get(value.getClass());
        return factory.create(target, attachmentType);
    }

    public SyncEntityAttachmentPayload(int targetEntityId, P attachmentTypeProvider, T value) {
        this.targetEntityId = targetEntityId;
        this.attachmentTypeProvider = attachmentTypeProvider;
        this.attachmentType = attachmentTypeProvider.get();
        this.value = value;
    }

    public SyncEntityAttachmentPayload(Entity target, P attachmentTypeProvider) {
        this(target.getId(), attachmentTypeProvider, target.getData(attachmentTypeProvider.get()));
    }

    public int getTargetEntityId() {
        return targetEntityId;
    }

    public P getAttachmentTypeProvider() {
        return attachmentTypeProvider;
    }

    public T getValue() {
        return value;
    }

    public void synchronize(Player player) {
        Entity target = player.level().getEntity(targetEntityId);
        if (target == null)
            throw new IllegalStateException();
        if (player.level().isClientSide)
            onClient(player, target);
        else
            onServer(player, target);
    }

    protected void onServer(Player player, Entity target) {
        if (player instanceof ServerPlayer serverPlayer)
            send(serverPlayer, target);
    }

    protected abstract void send(ServerPlayer player, Entity target);

    protected void onClient(Player player, Entity target) {
        if (target instanceof LivingEntity livingEntity)
            livingEntity.setData(attachmentType, value);
    }

    public interface Factory {
        SyncEntityAttachmentPayload<?, ?> create(Entity target, AttachmentType<?> attachmentType);
    }
}
