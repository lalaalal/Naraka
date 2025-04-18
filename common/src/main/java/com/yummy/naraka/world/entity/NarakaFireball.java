package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
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

import java.util.ArrayList;
import java.util.List;

public class NarakaFireball extends Fireball implements ItemSupplier {
    protected static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(NarakaFireball.class, EntityDataSerializers.INT);

    private @Nullable Entity cachedTarget;
    private boolean fixedDamage = false;
    private DamageCalculator damageCalculator = fireball -> 10;
    private final List<HurtTargetListener> listeners = new ArrayList<>();

    public NarakaFireball(EntityType<? extends NarakaFireball> entityType, Level level) {
        super(entityType, level);
        setItem(NarakaItems.NARAKA_FIREBALL.get().getDefaultInstance());
    }

    public NarakaFireball(Mob owner, Vec3 movement, Level level) {
        this(owner, owner.getTarget(), movement, level);
    }

    public NarakaFireball(Mob owner, Vec3 movement, Level level, boolean fixedDamage) {
        this(owner, owner.getTarget(), movement, level);
        this.fixedDamage = fixedDamage;
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

    public void setDamageCalculator(DamageCalculator damageCalculator) {
        this.damageCalculator = damageCalculator;
    }

    public void addHurtTargetListener(HurtTargetListener listener) {
        this.listeners.add(listener);
    }

    public NarakaFireball withStigma(LivingEntity stigmatizingEntity) {
        addHurtTargetListener((target, damage) -> StigmaHelper.increaseStigma(target, stigmatizingEntity));
        return this;
    }

    public NarakaFireball withFixedDamage() {
        fixedDamage = true;
        return this;
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
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if (dataAccessor == TARGET_ID)
            cachedTarget = null;
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
            Vec3 projectionVector = NarakaUtils.projection(targetVector, movingVector);
            Vec3 tracingVector = targetVector.subtract(projectionVector);
            double tracingVectorLength = tracingVector.length();

            Vec3 deltaMovement = getDeltaMovement();
            double length = deltaMovement.length();
            if (tracingVectorLength > 8 && length > 0.7)
                setDeltaMovement(deltaMovement.scale(0.9));
            if (tracingVectorLength < 8 && length < 1)
                setDeltaMovement(deltaMovement.scale(1.1));
            double scale = Mth.clamp(tracingVectorLength, 0, 0.08);
            if (fixedDamage)
                scale = scale * 0.3;
            addDeltaMovement(tracingVector.scale(scale));
        }
    }

    @Override
    protected void onDeflection(@Nullable Entity entity, boolean deflectedByPlayer) {
        super.onDeflection(entity, deflectedByPlayer);
        setTarget(null);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            level().explode(this, damageSources().fireball(this, getOwner()), null, position(), 2, false, Level.ExplosionInteraction.NONE);
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();
        Entity owner = getOwner();
        if (hitEntity != owner && hitEntity instanceof LivingEntity livingEntity) {
            float damage = damageCalculator.calculateDamage(this);
            livingEntity.hurt(getDamageSource(owner), damage);
            for (HurtTargetListener listener : listeners)
                listener.onHurtTarget(livingEntity, damage);
        }
    }

    protected DamageSource getDamageSource(@Nullable Entity owner) {
        if (fixedDamage)
            return NarakaDamageSources.projectileFixed(this, owner);
        return damageSources().fireball(this, owner);
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

    @FunctionalInterface
    public interface DamageCalculator {
        float calculateDamage(NarakaFireball fireball);
    }

    @FunctionalInterface
    public interface HurtTargetListener {
        void onHurtTarget(LivingEntity target, float damage);
    }
}
