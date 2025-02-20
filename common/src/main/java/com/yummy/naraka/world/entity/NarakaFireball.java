package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
    private @Nullable Entity target;

    public NarakaFireball(EntityType<? extends NarakaFireball> entityType, Level level) {
        super(entityType, level);
        setItem(NarakaItems.NARAKA_FIREBALL.get().getDefaultInstance());
    }

    public NarakaFireball(Mob owner, Vec3 movement, Level level) {
        super(NarakaEntityTypes.NARAKA_FIREBALL.get(), owner, movement, level);
        this.target = owner.getTarget();
        setItem(NarakaItems.NARAKA_FIREBALL.get().getDefaultInstance());
    }

    public NarakaFireball(LivingEntity owner, Entity target, Vec3 movement, Level level) {
        super(NarakaEntityTypes.NARAKA_FIREBALL.get(), owner, movement, level);
        this.target = target;
        setItem(NarakaItems.NARAKA_FIREBALL.get().getDefaultInstance());
    }

    @Override
    public void setOwner(@Nullable Entity owner) {
        if (this.getOwner() == null)
            super.setOwner(owner);
    }

    @Override
    public void tick() {
        traceTarget();
        super.tick();
    }

    private void traceTarget() {
        if (target != null) {
            double length = getDeltaMovement().length();
            Vec3 delta = target.getEyePosition().subtract(position());
            setDeltaMovement(getDeltaMovement().scale(2).add(delta).normalize().scale(length));
        }
    }

    @Override
    protected void onDeflection(@Nullable Entity entity, boolean deflectedByPlayer) {
        super.onDeflection(entity, deflectedByPlayer);
        target = null;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            level().explode(this, level().damageSources().fireball(this, getOwner()), null, position(), 3, false, Level.ExplosionInteraction.NONE);
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();
        if (hitEntity == getOwner() && hitEntity instanceof LivingEntity livingEntity)
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (target != null)
            compound.putUUID("Target", target.getUUID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Target") && level() instanceof ServerLevel serverLevel)
            target = serverLevel.getEntity(compound.getUUID("Target"));
    }
}
