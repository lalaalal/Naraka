package com.yummy.naraka.world.entity;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Stardust extends LightTailEntity {
    public static final int EXPLOSION_WAITING_TICK = 120;
    public static final EntityDataAccessor<Integer> WAITING_TICK = SynchedEntityData.defineId(Stardust.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> HIT_BLOCK = SynchedEntityData.defineId(Stardust.class, EntityDataSerializers.BOOLEAN);
    private int waitingTickCount = 0;
    private int explosionWaitingTickCount = 0;
    @Nullable
    private Entity owner;
    private boolean followTarget;

    public Stardust(EntityType<? extends Stardust> entityType, Level level) {
        super(entityType, level);
        setTailColor(0xED7419);
    }

    public Stardust(Level level, LivingEntity owner, Vec3 shootingVector, double power, int waitingTick, boolean followTarget) {
        this(NarakaEntityTypes.STARDUST.get(), level);
        setPos(owner.getEyePosition());
        setRot(owner.getYRot(), owner.getXRot());
        setDeltaMovement(shootingVector.normalize().scale(power));
        entityData.set(WAITING_TICK, waitingTick);
        this.owner = owner;
        this.followTarget = followTarget;
        if (followTarget)
            explosionWaitingTickCount = EXPLOSION_WAITING_TICK - 20;
    }

    public @Nullable Entity getOwner() {
        return owner;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WAITING_TICK, 10)
                .define(HIT_BLOCK, false);
    }

    private void handleOnHitBlock() {
        if (explosionWaitingTickCount <= EXPLOSION_WAITING_TICK) {
            explosionWaitingTickCount += 1;
        } else {
            discard();
        }
        if (explosionWaitingTickCount == EXPLOSION_WAITING_TICK)
            explode(2);
    }

    @Override
    public void tick() {
        updateTailPositions();
        if (entityData.get(HIT_BLOCK)) {
            handleOnHitBlock();
            return;
        }
        if (isFalling()) {
            setDeltaMovement(getDeltaMovement().scale(1.2));
        } else if (getDeltaMovement().length() < 0.005) {
            waitFalling();
        } else {
            setDeltaMovement(getDeltaMovement().scale(0.75));
        }
        if (tickCount > 40) {
            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, entity -> entity.getClass() != Stardust.class, ClipContext.Block.COLLIDER);
            this.onHit(hitResult);
        }
        setPos(position().add(getDeltaMovement()));
    }

    private void waitFalling() {
        waitingTickCount += 1;
        int waitingTick = entityData.get(WAITING_TICK);
        if (waitingTickCount == waitingTick - 5)
            level().addParticle(NarakaParticleTypes.STARDUST.get(), true, true, getX(), getY(), getZ(), 0, 0, 0);
        if (waitingTickCount >= waitingTick) {
            Entity target = getTarget();
            if (followTarget && target != null) {
                Vec3 delta = target.getEyePosition()
                        .subtract(this.position())
                        .normalize()
                        .scale(0.1);
                setDeltaMovement(delta);
            } else {
                RandomSource random = RandomSource.create(getId() + waitingTick);
                double y = random.nextIntBetweenInclusive(4, 4) * -0.1;
                double x = (random.nextDouble() - 0.5) * 0.5;
                double z = (random.nextDouble() - 0.5) * 0.5;
                setDeltaMovement(x, y, z);
            }
        }
    }

    @Nullable
    private LivingEntity getTarget() {
        if (owner instanceof Mob mob)
            return mob.getTarget();
        return null;
    }

    public void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            setDeltaMovement(Vec3.ZERO);
            entityData.set(HIT_BLOCK, true);
            explode(1);
            setPos(hitResult.getLocation());
        }
        if (hitResult instanceof EntityHitResult entityHitResult && level() instanceof ServerLevel serverLevel) {
            Entity source = owner == null ? this : owner;
            explode(1);
            if (entityHitResult.getEntity() instanceof Player livingEntity
                    && !livingEntity.isInvulnerableTo(serverLevel, NarakaDamageSources.stardust(this))) {
                StigmaHelper.increaseStigma(serverLevel, livingEntity, source);
            }
        }
    }

    private void addParticles(ParticleOptions particle, double baseSpeed, int count) {
        for (int i = 0; i < count; i++) {
            double yRot = random.nextDouble() * 360;
            double xRot = random.nextDouble() * 180;
            double speed = random.nextDouble() * 0.2 + baseSpeed;
            double xSpeed = Math.cos(Math.toRadians(yRot)) * speed;
            double zSpeed = Math.sin(Math.toRadians(yRot)) * speed;
            double ySpeed = Math.sin(Math.toRadians(xRot)) * speed;
            level().addParticle(particle, getX(), getY(), getZ(), xSpeed, ySpeed, zSpeed);
        }
    }

    private void explode(int radius) {
        Entity source = owner == null ? this : owner;
        if (level().isClientSide) {
            addParticles(NarakaParticleTypes.STARDUST_FLAME.get(), 0.1, 120 * radius);
            addParticles(NarakaParticleTypes.GOLDEN_FLAME.get(), 0.3, 60 * radius);
        } else {
            level().explode(source, NarakaDamageSources.stardust(this), null, position(), radius, false, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    @Nullable
    protected ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (level() instanceof ServerLevel serverLevel && compound.contains("Owner"))
            this.owner = serverLevel.getEntity(UUID.fromString(compound.getStringOr("Owner", "")));
        entityData.set(HIT_BLOCK, compound.getBooleanOr("HitBlock", true));
        entityData.set(WAITING_TICK, compound.getIntOr("WaitingTick", 0));
        waitingTickCount = compound.getIntOr("WaitingTickCount", 0);
        explosionWaitingTickCount = compound.getIntOr("ExplosionWaitingTickCount", 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (owner != null)
            compound.putString("Owner", owner.getUUID().toString());
        compound.putBoolean("HitBlock", entityData.get(HIT_BLOCK));
        compound.putInt("WaitingTick", entityData.get(WAITING_TICK));
        compound.putInt("WaitingTickCount", waitingTickCount);
        compound.putInt("ExplosionWaitingTickCount", explosionWaitingTickCount);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        int data = owner == null ? 0 : owner.getId();
        return new ClientboundAddEntityPacket(this, entity, data);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        int ownerId = packet.getData();
        this.owner = level().getEntity(ownerId);
    }

    public boolean isFalling() {
        return getDeltaMovement().y < 0;
    }
}
