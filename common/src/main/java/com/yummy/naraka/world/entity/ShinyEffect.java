package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class ShinyEffect extends Entity {
    public static final EntityDataAccessor<Boolean> IS_VERTICAL = SynchedEntityData.defineId(ShinyEffect.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(ShinyEffect.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(ShinyEffect.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ShinyEffect.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(ShinyEffect.class, EntityDataSerializers.FLOAT);

    public static ShinyEffect spawnShinySpark(ServerLevel level, Vec3 position, RandomSource random, double range, int lifetime, int color) {
        float rotation = random.nextFloat() * 120 - 60;
        float scale = random.nextFloat() * 0.5f + 0.25f;

        double x = position.x() + random.nextFloat() * range - (range / 2);
        double z = position.z() + random.nextFloat() * range - (range / 2);
        double y = NarakaUtils.findFloor(level, BlockPos.containing(position)).getY() + 1;

        ShinyEffect shinyEffect = new ShinyEffect(level, lifetime, true, scale, rotation, color);
        shinyEffect.setPos(x, y, z);

        level.addFreshEntity(shinyEffect);
        level.playSound(null, position.x(), position.y(), position.z(), SoundEvents.TRIDENT_HIT_GROUND, SoundSource.HOSTILE, 1.0F, 1.0F);
        return shinyEffect;
    }

    public ShinyEffect(EntityType<? extends ShinyEffect> entityType, Level level) {
        super(entityType, level);
    }

    public ShinyEffect(Level level, int lifetime, boolean isVertical, float scale, float rotation, int color) {
        this(NarakaEntityTypes.SHINY_EFFECT.get(), level);
        entityData.set(LIFETIME, lifetime);
        entityData.set(IS_VERTICAL, isVertical);
        entityData.set(SCALE, scale);
        entityData.set(COLOR, color);
        entityData.set(ROTATION, rotation);
    }

    public boolean isVertical() {
        return entityData.get(IS_VERTICAL);
    }

    public float getScale() {
        return entityData.get(SCALE);
    }

    public int getLifetime() {
        return entityData.get(LIFETIME);
    }

    public int getColor() {
        return entityData.get(COLOR);
    }

    public float getRotation() {
        return entityData.get(ROTATION);
    }

    @Override
    public void tick() {
        if (tickCount > getLifetime())
            discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(IS_VERTICAL, false)
                .define(SCALE, 1.0f)
                .define(LIFETIME, 20)
                .define(COLOR, 0xffffff)
                .define(ROTATION, 0.0f);
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
