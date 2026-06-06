package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

public class NarakaSword extends MotionEntity {
    public static final EntityDataAccessor<SoulType> SOUL_TYPE = SynchedEntityData.defineId(NarakaSword.class, NarakaEntityDataSerializers.SOUL_TYPE);
    public static final EntityDataAccessor<Float> ALPHA = SynchedEntityData.defineId(NarakaSword.class, EntityDataSerializers.FLOAT);

    public static final Vector3f DIRECTION = new Vector3f(0, 0.1f, 0);
    public static final float LENGTH = 2.75f;

    private final List<SwordEffectData> swordEffectData = new ArrayList<>();
    private float prevAlpha = 0f;

    public NarakaSword(EntityType<? extends NarakaSword> entityType, Level level) {
        super(entityType, level);
    }

    public NarakaSword(Level level, Vec3 position, SoulType soulType) {
        this(NarakaEntityTypes.NARAKA_SWORD.get(), level);
        setSoulType(soulType);
        setPos(position);
    }

    public List<SwordEffectData> getSwordEffectData(float partialTicks) {
        int startIndex = (int) (getSwordEffectUpdateCount() * (1 - partialTicks));
        if (swordEffectData.size() > startIndex)
            return swordEffectData.subList(startIndex, swordEffectData.size() - 1);
        return swordEffectData;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SOUL_TYPE, SoulType.NONE)
                .define(ALPHA, 0f);
    }

    public int getSwordEffectUpdateCount() {
        return 16;
    }

    @Override
    public void tick() {
        super.tick();
        prevAlpha = getAlpha();

        if (tickCount == 5) {
            for (int i = 0; i < 128; i++)
                swordEffectData.add(new SwordEffectData(position().toVector3f(), DIRECTION, getRotation(), 0, 0));
        }

        if (level().isClientSide() && tickCount > 10) {
            final int updateCount = getSwordEffectUpdateCount();
            SwordEffectData prevData = swordEffectData.getFirst();
            for (float count = 1; count <= updateCount; count++) {
                float delta = count / (float) updateCount;
                Vector3fc base = prevData.base().lerp(position().toVector3f(), delta, new Vector3f());
                Quaternionfc rotation = prevData.rotation().slerp(getRotation(), delta, new Quaternionf());
                swordEffectData.removeLast();
                swordEffectData.addFirst(new SwordEffectData(base, DIRECTION, rotation, LENGTH, getScale()));
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
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

    public float getAlpha() {
        return entityData.get(ALPHA);
    }

    public float getAlpha(float partialTicks) {
        return Mth.lerp(partialTicks, prevAlpha, getAlpha());
    }

    public void setAlpha(float alpha) {
        entityData.set(ALPHA, alpha);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag input) {
        NarakaNbtUtils.read(input, "SoulType", SoulType.CODEC)
                .ifPresent(this::setSoulType);
        setAlpha(input.getFloat("Alpha"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag output) {
        NarakaNbtUtils.store(output, "SoulType", SoulType.CODEC, getSoulType());
        output.putFloat("Alpha", getAlpha());
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}
