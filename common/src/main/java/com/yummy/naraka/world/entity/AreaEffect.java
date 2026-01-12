package com.yummy.naraka.world.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class AreaEffect extends Entity {
    public static final EntityDataAccessor<Float> X_WIDTH = SynchedEntityData.defineId(AreaEffect.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> Z_WIDTH = SynchedEntityData.defineId(AreaEffect.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(AreaEffect.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(AreaEffect.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> INDEX = SynchedEntityData.defineId(AreaEffect.class, EntityDataSerializers.INT);

    public AreaEffect(EntityType<? extends AreaEffect> entityType, Level level) {
        super(entityType, level);
    }

    public AreaEffect(Level level, Vec3 position, int lifetime, float xWidth, float zWidth, int color, int index) {
        this(NarakaEntityTypes.AREA_EFFECT.get(), level);
        setPos(position);
        entityData.set(X_WIDTH, xWidth);
        entityData.set(Z_WIDTH, zWidth);
        entityData.set(LIFETIME, lifetime);
        entityData.set(COLOR, color);
        entityData.set(INDEX, index);
    }

    public float getXWidth() {
        return entityData.get(X_WIDTH);
    }

    public float getZWidth() {
        return entityData.get(Z_WIDTH);
    }

    public int getLifetime() {
        return entityData.get(LIFETIME);
    }

    public int getColor() {
        return entityData.get(COLOR);
    }

    public int getIndex() {
        return entityData.get(INDEX);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(X_WIDTH, 3f)
                .define(Z_WIDTH, 3f)
                .define(LIFETIME, 20)
                .define(COLOR, 0xffffff)
                .define(INDEX, 0);
    }

    @Override
    public void tick() {
        if (tickCount > getLifetime())
            discard();
        tickCount++;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }
}
