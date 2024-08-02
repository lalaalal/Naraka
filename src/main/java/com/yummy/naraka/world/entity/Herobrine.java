package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.data.DeathCountHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class Herobrine extends Monster implements DeathCountingEntity {
    private final DeathCountingInstance countingInstance = new DeathCountingInstance();

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20);
    }

    public Herobrine(EntityType<? extends Herobrine> entityType, Level level) {
        super(entityType, level);
        DeathCountHelper.attachCountingEntity(this);
    }

    public Herobrine(Level level, Vec3 pos) {
        this(NarakaEntityTypes.HEROBRINE, level);
        setPos(pos);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        Entity cause = damageSource.getEntity();
        if (cause instanceof LivingEntity livingEntity)
            countDeath(livingEntity);
        return super.hurt(damageSource, damage);
    }

    @Override
    public void remove(RemovalReason removalReason) {
        super.remove(removalReason);
        DeathCountHelper.detachCountingEntity(this);
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.is(DamageTypes.IN_WALL))
            return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean canStandOnFluid(FluidState pFluidState) {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        countingInstance.save(compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        countingInstance.load(compoundTag);
    }

    @Override
    public LivingEntity living() {
        return this;
    }

    @Override
    public DeathCountingInstance getCountingInstance() {
        return countingInstance;
    }
}
