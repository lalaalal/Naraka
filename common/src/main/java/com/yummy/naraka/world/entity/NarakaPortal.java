package com.yummy.naraka.world.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
