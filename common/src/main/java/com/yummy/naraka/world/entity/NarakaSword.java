package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class NarakaSword extends Entity {
    public static final EntityDataAccessor<SoulType> SOUL_TYPE = SynchedEntityData.defineId(NarakaSword.class, NarakaEntityDataSerializers.SOUL_TYPE);
    public static final Vec3 DIRECTION = new Vec3(0, 0.1f, 0);
    public static final float LENGTH = 2.75f;

    private final List<SwordEffectData> swordEffectData = new ArrayList<>();
    private float zRot;
    private float zRot0;

    public NarakaSword(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
        for (int i = 0; i < 16; i++)
            swordEffectData.add(SwordEffectData.of(position(), DIRECTION, LENGTH, 0, 0));
    }

    public NarakaSword(Level level, SoulType soulType) {
        this(NarakaEntityTypes.NARAKA_SWORD.get(), level);
        setSoulType(soulType);
    }

    public List<SwordEffectData> getSwordEffectData() {
        return swordEffectData;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SOUL_TYPE, SoulType.REDSTONE);
    }

    @Override
    public void tick() {
        super.tick();

        zRot0 = zRot;
        swordEffectData.removeLast();
        swordEffectData.addFirst(SwordEffectData.of(position(), DIRECTION, LENGTH, getZRot(), getYRot()));
        zRot += 5;
        setYRot(getYRot() + 15);
    }

    public float getZRot() {
        return zRot;
    }

    public float getZRot(float partialTicks) {
        return Mth.lerp(partialTicks, zRot0, zRot);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    public void setSoulType(SoulType soulType) {
        entityData.set(SOUL_TYPE, soulType);
    }

    public SoulType getSoulType() {
        return entityData.get(SOUL_TYPE);
    }

    public int getColor() {
        return getSoulType().getColor();
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        SoulType soulType = input.read("SoulType", SoulType.CODEC).orElse(SoulType.NONE);
        setSoulType(soulType);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.store("SoulType", SoulType.CODEC, getSoulType());
    }
}
