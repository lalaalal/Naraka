package com.yummy.naraka.world.entity;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class MagicCircle extends Entity {
    public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(MagicCircle.class, EntityDataSerializers.FLOAT);

    private int lifetime = 1200;
    @Nullable
    private LivingEntity owner;

    public MagicCircle(EntityType<? extends MagicCircle> entityType, Level level) {
        super(entityType, level);
    }

    public MagicCircle(Level level, LivingEntity owner, int lifetime, float scale) {
        this(NarakaEntityTypes.MAGIC_CIRCLE.get(), level);
        this.lifetime = lifetime;
        this.owner = owner;
        entityData.set(SCALE, scale);
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel)
            serverTick(serverLevel);
        if (level().isClientSide)
            clientTick(level());
    }

    private void serverTick(ServerLevel level) {
        if (tickCount >= lifetime) {
            float scale = getScale() / 2;
            Collection<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(scale, 7, scale), AbstractHerobrine::isNotHerobrine);
            DamageSource damageSource = damageSources().explosion(owner, this);
            for (LivingEntity entity : entities)
                entity.hurtServer(level, damageSource, 10);
            discard();
        }
    }

    private void clientTick(Level level) {
        setYRot(getYRot() + Mth.PI * 0.1f);
        for (int i = 0; i < getScale() / 2; i++) {
            double x = random.nextDouble() * getScale() - getScale() * 0.5 + getX();
            double y = random.nextDouble() * 0.3 + 0.1 + getY();
            double z = random.nextDouble() * getScale() - getScale() * 0.5 + getZ();
            double ySpeed = random.nextDouble() * 0.1 + 0.05;

            level.addParticle(NarakaParticleTypes.GOLDEN_FLAME.get(), x, y, z, 0, ySpeed, 0);
        }
    }

    public float getScale() {
        return entityData.get(SCALE);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SCALE, 1f);
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
