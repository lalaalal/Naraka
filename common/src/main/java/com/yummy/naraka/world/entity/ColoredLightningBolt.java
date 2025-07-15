package com.yummy.naraka.world.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ColoredLightningBolt extends LightningBolt {
    public static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ColoredLightningBolt.class, EntityDataSerializers.INT);

    public ColoredLightningBolt(EntityType<? extends ColoredLightningBolt> entityType, Level level) {
        super(entityType, level);
        setVisualOnly(true);
    }

    public ColoredLightningBolt(Level level, Vec3 position, int color) {
        this(NarakaEntityTypes.COLORED_LIGHTNING_BOLT.get(), level);
        setPos(position);
        entityData.set(COLOR, color);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR, -1);
    }

    public int getColor() {
        return entityData.get(COLOR);
    }
}
