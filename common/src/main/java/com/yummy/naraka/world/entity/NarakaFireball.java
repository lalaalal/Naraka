package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class NarakaFireball extends Fireball implements ItemSupplier {
    protected static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(NarakaFireball.class, EntityDataSerializers.INT);

    private @Nullable Entity cachedTarget;

    public NarakaFireball(EntityType<? extends NarakaFireball> entityType, Level level) {
        super(entityType, level);
        setItem(NarakaItems.NARAKA_FIREBALL.get().getDefaultInstance());
    }

    public NarakaFireball(Mob owner, Vec3 movement, Level level) {
        this(owner, owner.getTarget(), movement, level);
    }

    public NarakaFireball(LivingEntity owner, @Nullable Entity target, Vec3 movement, Level level) {
        super(NarakaEntityTypes.NARAKA_FIREBALL.get(), owner, movement, level);
        setTarget(target);
        setItem(NarakaItems.NARAKA_FIREBALL.get().getDefaultInstance());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TARGET_ID, -1);
    }

    public boolean hasTarget() {
        return entityData.get(TARGET_ID) != -1;
    }

    public void setTarget(@Nullable Entity target) {
        this.cachedTarget = target;
        int targetId = target == null ? -1 : target.getId();
        entityData.set(TARGET_ID, targetId);
    }

    @Nullable
    protected Entity getTarget() {
        if (cachedTarget == null) {
            int targetId = entityData.get(TARGET_ID);
            if (targetId == -1)
                return null;
            return this.cachedTarget = level().getEntity(targetId);
        }
        return cachedTarget;
    }

    @Override
    public void tick() {
        traceTarget();
        super.tick();
    }

    private void traceTarget() {
        Entity target = getTarget();
        if (target != null) {
            Vec3 targetVector = target.getEyePosition().subtract(position());
            Vec3 movingVector = getDeltaMovement().normalize();
            if (movingVector.equals(Vec3.ZERO))
                return;
            Vec3 projectionVector = movingVector.scale(targetVector.dot(movingVector) / movingVector.length());
            Vec3 tracingVector = targetVector.subtract(projectionVector);
            double tracingVectorLength = tracingVector.length();

            Vec3 deltaMovement = getDeltaMovement();
            double length = deltaMovement.length();
            if (tracingVectorLength > 8 && length > 0.7)
                setDeltaMovement(deltaMovement.scale(0.9));
            if (tracingVectorLength < 8 && length < 1)
                setDeltaMovement(deltaMovement.scale(1.1));
            double scale = Mth.clamp(tracingVectorLength, 0, 0.08);
            addDeltaMovement(tracingVector.scale(scale));
        }
    }

    @Override
    protected void onDeflection(@Nullable Entity entity, boolean deflectedByPlayer) {
        super.onDeflection(entity, deflectedByPlayer);
        setTarget(null);
        if (level().isClientSide)
            setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            level().explode(this, damageSources().fireball(this, getOwner()), null, position(), 1, false, Level.ExplosionInteraction.NONE);
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();
        if (hitEntity == getOwner() && hitEntity instanceof LivingEntity livingEntity)
            livingEntity.hurt(damageSources().fireball(this, getOwner()), 10);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (cachedTarget != null)
            compound.putUUID("Target", cachedTarget.getUUID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Target") && level() instanceof ServerLevel serverLevel) {
            setTarget(serverLevel.getEntity(compound.getUUID("Target")));
        }
    }
}
