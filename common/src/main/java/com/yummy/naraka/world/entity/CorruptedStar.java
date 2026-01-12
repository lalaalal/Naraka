package com.yummy.naraka.world.entity;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CorruptedStar extends LightTailEntity implements StigmatizingEntity {
    public static final EntityDataAccessor<QuadraticBezier> PREPARE_BEZIER = SynchedEntityData.defineId(CorruptedStar.class, NarakaEntityDataSerializers.BEZIER);
    public static final EntityDataAccessor<Integer> PREPARE_DURATION = SynchedEntityData.defineId(CorruptedStar.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Vec3> BASE_POSITION = SynchedEntityData.defineId(CorruptedStar.class, NarakaEntityDataSerializers.VEC3);
    public static final EntityDataAccessor<Vec3> TARGET_POSITION = SynchedEntityData.defineId(CorruptedStar.class, NarakaEntityDataSerializers.VEC3);
    public static final EntityDataAccessor<Integer> FOLLOWING_TARGET = SynchedEntityData.defineId(CorruptedStar.class, EntityDataSerializers.INT);

    private int hitTick = 0;
    private final boolean verticalShine;
    private float shineScale;
    private int shineStartTick;

    public CorruptedStar(EntityType<? extends CorruptedStar> entityType, Level level) {
        super(entityType, level, 80, 8);
        setTailColor(SoulType.COPPER.color);
        verticalShine = random.nextBoolean();
        shineScale = random.nextFloat() + 0.5f;
        shineStartTick = random.nextIntBetweenInclusive(20, 35);
    }

    public CorruptedStar(Level level, @Nullable Entity owner, Vec3 position, QuadraticBezier bezier, int prepareDuration) {
        this(NarakaEntityTypes.CORRUPTED_STAR.get(), level);
        setPos(position);
        setOwner(owner);
        entityData.set(PREPARE_DURATION, prepareDuration);
        entityData.set(BASE_POSITION, position);
        entityData.set(PREPARE_BEZIER, bezier);
    }

    public CorruptedStar(Level level, @Nullable Entity owner, Vec3 position, QuadraticBezier bezier) {
        this(level, owner, position, bezier, 40);
    }

    public boolean isVerticalShine() {
        return verticalShine;
    }

    public float getShineScale() {
        return shineScale;
    }

    public int getShineStartTick() {
        return shineStartTick;
    }

    public Vec3 getTargetPosition() {
        return entityData.get(TARGET_POSITION);
    }

    public Vec3 getTargetPosition(float partialTicks) {
        int followingTargetId = entityData.get(FOLLOWING_TARGET);
        Entity entity = level().getEntity(followingTargetId);
        if (entity != null)
            return entity.getPosition(partialTicks);
        return entityData.get(TARGET_POSITION);
    }

    public void setTargetPosition(Vec3 position) {
        entityData.set(TARGET_POSITION, position);
    }

    public void setFollowingTarget(LivingEntity target) {
        entityData.set(FOLLOWING_TARGET, target.getId());
    }

    public void removeFollowingTarget() {
        entityData.set(FOLLOWING_TARGET, -1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PREPARE_BEZIER, QuadraticBezier.ZERO)
                .define(PREPARE_DURATION, 40)
                .define(BASE_POSITION, Vec3.ZERO)
                .define(TARGET_POSITION, Vec3.ZERO)
                .define(FOLLOWING_TARGET, -1);
    }

    @Override
    public void tick() {
        if (tickCount <= entityData.get(PREPARE_DURATION))
            preparingTick();
        else {
            super.tick();
            needsSync = true;
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected @Nullable ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (level().isClientSide() && hitTick == 0) {
            addParticles(0.1, 120);
            addParticles(0.3, 60);
            hitTick = tickCount;
            shineStartTick = tickCount;
            shineScale = 0.5f;
        } else if (hitTick > 0 && tickCount > hitTick + 20) {
            discard();
        } else if (hitTick == 0) {
            Vec3 hitLocation = result.getLocation();
            level().explode(this, damageSources().explosion(this, getOwner()), null, hitLocation, 2, false, Level.ExplosionInteraction.NONE);
            hitTick = tickCount;
            setTargetPosition(Vec3.ZERO);
        }
    }

    private void addParticles(double baseSpeed, int count) {
        for (int i = 0; i < count; i++) {
            double yRot = random.nextDouble() * 360;
            double xRot = random.nextDouble() * 180;
            double speed = random.nextDouble() * 0.2 + baseSpeed;
            double xSpeed = Math.cos(Math.toRadians(yRot)) * speed;
            double zSpeed = Math.sin(Math.toRadians(yRot)) * speed;
            double ySpeed = Math.sin(Math.toRadians(xRot)) * speed;
            level().addParticle(NarakaFlameParticleOption.COPPER, getX(), getY(), getZ(), xSpeed, ySpeed, zSpeed);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level() instanceof ServerLevel serverLevel && result.getEntity() instanceof LivingEntity livingEntity) {
            stigmatizeEntity(serverLevel, livingEntity);
        }
    }

    private void preparingTick() {
        QuadraticBezier bezier = entityData.get(PREPARE_BEZIER);
        Vec3 basePosition = entityData.get(BASE_POSITION);
        float prepareDuration = entityData.get(PREPARE_DURATION);

        float delta = NarakaUtils.interpolate(tickCount / prepareDuration, 0, 1, NarakaUtils::fastStepIn);

        Vec3 position = basePosition.add(bezier.interpolate(delta));
        setPos(position);

        final int tailUpdateCount = entityData.get(TAIL_UPDATE_COUNT);
        for (float count = 1; count <= tailUpdateCount; count++) {
            float positionDelta = count / (float) tailUpdateCount;
            float tailDelta = NarakaUtils.interpolate((tickCount + positionDelta - 0.75f) / prepareDuration, 0, 1, NarakaUtils::fastStepIn);
            Vec3 tailPosition = basePosition.add(bezier.interpolate(tailDelta));
            tailPositions.addFirst(tailPosition);
            tailPositions.removeLast();
        }
    }

    @Override
    public ProjectileDeflection deflection(Projectile projectile) {
        return ProjectileDeflection.NONE;
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable EntityReference<Entity> owner, boolean deflectionByPlayer) {
        return false;
    }

    @Override
    public void stigmatizeEntity(ServerLevel level, LivingEntity target) {
        Entity entity = getOwner();
        if (entity instanceof StigmatizingEntity stigmatizingEntity)
            stigmatizingEntity.stigmatizeEntity(level, target);
    }

    @Override
    public void collectStigma(ServerLevel level, LivingEntity target, Stigma original) {

    }
}
