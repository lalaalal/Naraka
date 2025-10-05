package com.yummy.naraka.world.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class LightningCircle extends Entity {
    public static final int MAX_SCALE = 128;

    @Nullable
    private AbstractHerobrine herobrine;
    private float scale = 0;
    private float prevScale = 0;

    public LightningCircle(EntityType<? extends LightningCircle> entityType, Level level) {
        super(entityType, level);
    }

    public LightningCircle(Level level, AbstractHerobrine herobrine) {
        this(NarakaEntityTypes.LIGHTNING_CIRCLE.get(), level);
        this.herobrine = herobrine;
        setPos(herobrine.getX(), herobrine.getY() + 0.1, herobrine.getZ());
    }

    private boolean canHurtTarget(LivingEntity target) {
        double distanceToSqr = distanceToSqr(target);
        double radius = scale * 0.5;
        double from = radius - 1;
        double to = radius + 1;
        return from * from < distanceToSqr && distanceToSqr < to * to
                && AbstractHerobrine.isNotHerobrine(target)
                && target.onGround();
    }

    @Override
    public void tick() {
        super.tick();
        this.prevScale = this.scale;
        scale += 1.5f;

        if (scale > MAX_SCALE)
            discard();
        if (level() instanceof ServerLevel serverLevel)
            serverTick(serverLevel);
    }

    public void serverTick(ServerLevel level) {
        float radius = scale * 0.5f;
        level.getEntitiesOfClass(LivingEntity.class,
                getBoundingBox().inflate(radius, 5, radius),
                this::canHurtTarget
        ).forEach(target -> {
            if (target.hurtServer(level, damageSources().magic(), 10) && herobrine != null)
                herobrine.stigmatizeEntity(level, target);
        });
    }

    public float getScale(float partialTick) {
        return Mth.lerp(partialTick, prevScale, scale);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

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
