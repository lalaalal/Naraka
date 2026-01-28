package com.yummy.naraka.world.entity;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class LightTailEntity extends AbstractHurtingProjectile {
    public static final EntityDataAccessor<Integer> TAIL_COLOR = SynchedEntityData.defineId(LightTailEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> TAIL_UPDATE_COUNT = SynchedEntityData.defineId(LightTailEntity.class, EntityDataSerializers.INT);

    protected final List<Vec3> tailPositions;

    public LightTailEntity(EntityType<? extends LightTailEntity> entityType, Level level) {
        this(entityType, level, 20, 2);
    }

    public LightTailEntity(EntityType<? extends LightTailEntity> entityType, Level level, int tailLength, int tailUpdateCount) {
        super(entityType, level);
        entityData.set(TAIL_UPDATE_COUNT, tailUpdateCount);
        tailPositions = new LinkedList<>(Collections.nCopies(tailLength, Vec3.ZERO));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TAIL_COLOR, 0)
                .define(TAIL_UPDATE_COUNT, 2);
    }

    public void setTailColor(int color) {
        entityData.set(TAIL_COLOR, color);
    }

    public int getTailColor() {
        return entityData.get(TAIL_COLOR);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Collections.fill(tailPositions, this.position());
    }

    public List<Vec3> getTailPositions() {
        return tailPositions;
    }

    protected void updateTailPositions() {
        final int tailUpdateCount = entityData.get(TAIL_UPDATE_COUNT);
        Vec3 prevPosition = tailPositions.getFirst();
        for (float count = 1; count <= tailUpdateCount; count++) {
            float tailDelta = Mth.lerp(count / (float) tailUpdateCount, 0, 1);
            Vec3 position = prevPosition.lerp(position(), tailDelta);
            tailPositions.addFirst(position);
            tailPositions.removeLast();
        }
    }

    @Override
    public void tick() {
        updateTailPositions();
        super.tick();
    }
}
