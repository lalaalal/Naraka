package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MassiveLightning extends Entity {
    public static final EntityDataAccessor<Integer> MAX_SIZE = SynchedEntityData.defineId(MassiveLightning.class, EntityDataSerializers.INT);
    private static final int LIFETIME = 30;

    private float size = 10;
    private float prevSize = 10;
    @Nullable
    private Herobrine herobrine;

    public MassiveLightning(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public MassiveLightning(Level level, Herobrine herobrine, int maxSize) {
        super(NarakaEntityTypes.MASSIVE_LIGHTNING.get(), level);
        setMaxSize(maxSize);
        this.herobrine = herobrine;
    }

    @Override
    public void tick() {
        super.tick();
        prevSize = size;

        float delta = Mth.clamp((tickCount / (float) LIFETIME), 0, 1);
        float multiplier = NarakaUtils.interpolate(delta, 1, 0, NarakaUtils::fastStepOut);
        size = getMaxSize() * multiplier;

        if (level() instanceof ServerLevel serverLevel)
            serverTick(serverLevel);
        else
            clientTick();
        if (tickCount >= LIFETIME + 20)
            discard();
    }

    private boolean canHurtTarget(LivingEntity target) {
        return AbstractHerobrine.isNotHerobrine(target);
    }

    private void serverTick(ServerLevel level) {
        level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(size), this::canHurtTarget).forEach(target -> {
            if (target.hurtServer(level, damageSources().lightningBolt(), 10) && herobrine != null) {
                herobrine.stigmatizeEntity(level, target);
            }
        });
    }

    private void clientTick() {
        if (tickCount == 2)
            level().playLocalSound(blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 1000, 1, false);
    }

    public void setMaxSize(int size) {
        this.size = this.prevSize = size;
        entityData.set(MAX_SIZE, size);
    }

    public int getMaxSize() {
        return entityData.get(MAX_SIZE);
    }

    public float getSize(float partialTick) {
        return Mth.lerp(partialTick, prevSize, size);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(MAX_SIZE, 10);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setMaxSize(tag.getIntOr("MaxSize", 10));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("MaxSize", getMaxSize());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity, getMaxSize());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.size = this.prevSize = packet.getData();
    }
}
