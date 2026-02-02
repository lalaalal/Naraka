package com.yummy.naraka.world.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NarakaPortal extends Entity {
    public NarakaPortal(EntityType<? extends NarakaPortal> entityType, Level level) {
        super(entityType, level);
    }

    public NarakaPortal(Level level, Vec3 position) {
        this(NarakaEntityTypes.NARAKA_PORTAL.get(), level);
        setPos(position);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 100)
            discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }
}
