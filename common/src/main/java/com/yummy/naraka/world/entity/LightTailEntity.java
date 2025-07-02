package com.yummy.naraka.world.entity;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class LightTailEntity extends AbstractHurtingProjectile {
    protected final List<Vec3> tailPositions;

    public LightTailEntity(EntityType<? extends LightTailEntity> entityType, Level level) {
        this(entityType, level, 20);
    }

    public LightTailEntity(EntityType<? extends LightTailEntity> entityType, Level level, int tailLength) {
        super(entityType, level);
        tailPositions = new LinkedList<>(Collections.nCopies(tailLength, Vec3.ZERO));
    }

    public abstract int getTailColor();

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Collections.fill(tailPositions, this.position());
    }

    public List<Vec3> getTailPositions() {
        return tailPositions;
    }

    protected void updateTailPositions() {
        Vec3 prevPosition = tailPositions.getFirst();
        tailPositions.addFirst(position().lerp(prevPosition, 0.5));
        tailPositions.addFirst(position());
        tailPositions.removeLast();
        tailPositions.removeLast();
    }

    @Override
    public void tick() {
        updateTailPositions();
        super.tick();
    }
}
