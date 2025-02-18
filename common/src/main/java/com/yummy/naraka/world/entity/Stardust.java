package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
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
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Stardust extends Entity {
    public static final EntityDataAccessor<Integer> WAITING_TICK = SynchedEntityData.defineId(Stardust.class, EntityDataSerializers.INT);
    private int waitingTickCount = 0;
    @Nullable
    private Entity owner;

    public Stardust(EntityType<? extends Stardust> entityType, Level level) {
        super(entityType, level);
    }

    public Stardust(Level level, LivingEntity owner, Vec3 shootingVector, double power, int waitingTick) {
        super(NarakaEntityTypes.STARDUST.get(), level);
        setPos(owner.getEyePosition());
        setRot(owner.getYRot(), owner.getXRot());
        setDeltaMovement(shootingVector.normalize().scale(power));
        entityData.set(WAITING_TICK, waitingTick);
        this.owner = owner;
    }

    public @Nullable Entity getOwner() {
        return owner;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(WAITING_TICK, 10);
    }

    @Override
    public void tick() {
        if (isFalling())
            setDeltaMovement(getDeltaMovement().scale(1.2));
        else
            waitFalling();
        Vec3 movement = getDeltaMovement();
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, entity -> true, ClipContext.Block.COLLIDER);
        if (hitResult.getType() == HitResult.Type.BLOCK)
            this.onHit(hitResult);
        setPos(position().add(movement));
    }

    private void waitFalling() {
        if (getDeltaMovement().length() < 0.005) {
            waitingTickCount += 1;
            int waitingTick = entityData.get(WAITING_TICK);
            if (waitingTickCount >= waitingTick) {
                RandomSource random = RandomSource.create(getId() + waitingTick);
                double y = random.nextIntBetweenInclusive(4, 4) * -0.1;
                double x = (random.nextDouble() - 0.5) * 0.5;
                double z = (random.nextDouble() - 0.5) * 0.5;
                setDeltaMovement(x, y, z);
            }

        } else {
            setDeltaMovement(getDeltaMovement().scale(0.75));
        }
    }

    public void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            Entity source = owner == null ? this : owner;
            level().explode(source, NarakaDamageSources.stardust(this), null, position(), 6, false, Level.ExplosionInteraction.NONE);
            discard();
        }
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (level() instanceof ServerLevel serverLevel && compound.contains("Owner"))
            this.owner = serverLevel.getEntity(compound.getUUID("Owner"));
        entityData.set(WAITING_TICK, compound.getInt("WaitingTick"));
        this.waitingTickCount = compound.getInt("WaitingTickCount");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (owner != null)
            compound.putUUID("Owner", owner.getUUID());
        compound.putInt("WaitingTick", entityData.get(WAITING_TICK));
        compound.putInt("WaitingTickCount", waitingTickCount);
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
        Vec3 deltaMovement = new Vec3(packet.getXa(), packet.getYa(), packet.getZa());
        this.setDeltaMovement(deltaMovement);
    }

    public boolean isFalling() {
        return getDeltaMovement().y < 0;
    }
}
