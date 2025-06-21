package com.yummy.naraka.world.entity;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class MagicCircle extends Entity {
    public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(MagicCircle.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(MagicCircle.class, EntityDataSerializers.INT);

    @Nullable
    private LivingEntity owner;

    public MagicCircle(EntityType<? extends MagicCircle> entityType, Level level) {
        super(entityType, level);
    }

    public MagicCircle(Level level, LivingEntity owner, int lifetime, float scale) {
        this(NarakaEntityTypes.MAGIC_CIRCLE.get(), level);
        this.owner = owner;
        entityData.set(SCALE, scale);
        entityData.set(LIFETIME, lifetime);
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel)
            serverTick(serverLevel);
        if (level().isClientSide)
            clientTick(level());
    }

    public int getLifetime() {
        return entityData.get(LIFETIME);
    }

    private void serverTick(ServerLevel level) {
        int remainTick = getLifetime() - tickCount;
        if (remainTick <= 20 && remainTick % 5 == 0) {
            Collection<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0, 7, 0), AbstractHerobrine::isNotHerobrine);
            DamageSource damageSource = damageSources().explosion(owner, this);
            for (LivingEntity entity : entities)
                entity.hurtServer(level, damageSource, 10);
        }
        if (tickCount >= getLifetime()) {
            discard();
        }
    }

    private void clientTick(Level level) {
        setYRot(getYRot() + Mth.PI * 0.1f);
        double multiplier = 1;
        if (getLifetime() - tickCount < 20) {
            multiplier = 10;
            level.playSound(null, getX(), getY(), getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 1, 1);
        }
        for (int i = 0; i < getScale() * 0.5 * multiplier; i++) {
            double x = random.nextDouble() * getScale() - getScale() * 0.5 + getX();
            double y = random.nextDouble() * 0.3 + 0.1 + getY();
            double z = random.nextDouble() * getScale() - getScale() * 0.5 + getZ();
            double ySpeed = random.nextDouble() * 0.1 + 0.05;

            level.addParticle(NarakaParticleTypes.GOLDEN_FLAME.get(), x, y, z, 0, ySpeed * multiplier, 0);
        }
    }

    public float getScale() {
        return entityData.get(SCALE);
    }

    @Override
    protected AABB makeBoundingBox(Vec3 position) {
        float scale = getScale() / 2;
        return super.makeBoundingBox(position).inflate(scale, 0, scale);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SCALE, 1f)
                .define(LIFETIME, 1200);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            discard();
            return true;
        }
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }
}
