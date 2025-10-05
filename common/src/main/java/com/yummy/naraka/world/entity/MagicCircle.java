package com.yummy.naraka.world.entity;

import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MagicCircle extends Entity {
    public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(MagicCircle.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(MagicCircle.class, EntityDataSerializers.INT);

    @Nullable
    private Herobrine owner;
    private float prevScale = 0;
    private float currentScale = 0;
    private final List<Double> heightList = new ArrayList<>();

    public MagicCircle(EntityType<? extends MagicCircle> entityType, Level level) {
        super(entityType, level);
    }

    public MagicCircle(Level level, Herobrine owner, int lifetime, float scale) {
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

    private boolean isValidTarget(LivingEntity livingEntity) {
        double radius = getScale() * 0.5;
        return AbstractHerobrine.isNotHerobrine(livingEntity) && distanceToSqr(livingEntity) < radius * radius;
    }

    private boolean isInCircle(double x, double y, double z) {
        double radius = getScale() * 0.5;
        return distanceToSqr(x, y, z) < radius * radius;
    }

    private void serverTick(ServerLevel level) {
        int remainTick = getLifetime() - tickCount;
        if (remainTick <= 40 && remainTick % 5 == 0) {
            Collection<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0, 7, 0), this::isValidTarget);
            DamageSource damageSource = damageSources().magic();
            for (LivingEntity entity : entities) {
                entity.hurtServer(level, damageSource, 10);
                if (owner != null)
                    owner.stigmatizeEntity(level, entity);
            }
        }
        if (tickCount >= getLifetime()) {
            discard();
        }
    }

    private void updateHeights() {
        heightList.clear();
        for (int i = 0; i < 18; i++)
            heightList.add(random.nextDouble() * 3);
    }

    private double getHeight(int angle) {
        int range = 360 / heightList.size();
        double delta = (angle % range) / (double) range;
        int from = angle / range;
        int to = (from + 1) % heightList.size();
        return Mth.lerp(delta, heightList.get(from), heightList.get(to));
    }

    private void clientTick(Level level) {
        setYRot(getYRot() + 0.5f);
        float scale = getScale();
        int remainTick = getLifetime() - tickCount;
        if (remainTick % 7 == 3)
            updateHeights();
        if (20 < remainTick && remainTick <= 40 && remainTick % 7 < 4) {
            NarakaClientContext.CAMERA_SHAKE_TICK.set(10);
            for (int yRot = 0; yRot < 360; yRot++) {
                double x = Math.cos(Math.toRadians(yRot)) * scale * 0.5 + getX() + random.nextDouble() * 0.4;
                double z = Math.sin(Math.toRadians(yRot)) * scale * 0.5 + getZ() + random.nextDouble() * 0.4;
                double y = getHeight(yRot) + getY() + random.nextDouble() * 0.4;

                level.addParticle(NarakaFlameParticleOption.GOLD, true, true, x, y, z, 0, 1, 0);
            }
            level.playLocalSound(getX(), getY(), getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 2, 1, false);
        } else {
            for (int i = 0; i < getScale() * 0.5; i++) {
                double x = random.nextDouble() * scale - scale * 0.5 + getX();
                double y = random.nextDouble() * 0.3 + 0.1 + getY();
                double z = random.nextDouble() * scale - scale * 0.5 + getZ();
                double ySpeed = random.nextDouble() * 0.1 + 0.05;
                if (isInCircle(x, y, z))
                    level.addParticle(NarakaFlameParticleOption.GOLD, x, y, z, 0, ySpeed, 0);
            }
        }
        prevScale = currentScale;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if (dataAccessor == SCALE) {
            currentScale = getScale();
            setBoundingBox(makeBoundingBox(position()));
        }
    }

    public void setScale(float scale) {
        entityData.set(SCALE, scale);
        setBoundingBox(makeBoundingBox(position()));
    }

    public float getScale() {
        return entityData.get(SCALE);
    }

    public float getScale(float partialTick) {
        return Mth.lerp(partialTick, prevScale, currentScale);
    }

    @Override
    protected AABB makeBoundingBox(Vec3 position) {
        float scale = getScale() / 2;
        return super.makeBoundingBox(position).inflate(scale, 0, scale);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SCALE, 0f)
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
        tag.getFloatOr("Scale", 1);
        tag.getIntOr("Lifetime", 1);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Scale", getScale());
        tag.putInt("Lifetime", getLifetime());
    }
}
