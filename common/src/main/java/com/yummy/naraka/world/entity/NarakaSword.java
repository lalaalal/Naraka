package com.yummy.naraka.world.entity;

import com.mojang.math.Axis;
import com.yummy.naraka.world.entity.motion.*;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

public class NarakaSword extends Entity {
    public static final EntityDataAccessor<SoulType> SOUL_TYPE = SynchedEntityData.defineId(NarakaSword.class, NarakaEntityDataSerializers.SOUL_TYPE);
    public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(NarakaSword.class, EntityDataSerializers.FLOAT);

    public static final Vector3f DIRECTION = new Vector3f(0, 0.1f, 0);
    public static final float LENGTH = 2.75f;

    private final List<SwordEffectData> swordEffectData = new ArrayList<>();
    private final Quaternionf prevRotation = new Quaternionf();
    private final Quaternionf rotation = new Quaternionf();
    private SwordMotion motion = SwordMotionTypes.LONGINUS.build();

    public NarakaSword(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
        for (int i = 0; i < 64; i++)
            swordEffectData.add(SwordEffectData.of(new Vector3f(), DIRECTION, rotation, 0, getScale()));
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
        builder.define(SOUL_TYPE, SoulType.REDSTONE)
                .define(SCALE, 5.0f);
    }

    @Override
    public void tick() {
        super.tick();

        prevRotation.set(getRotation());

        final int updateCount = 4;
        SwordEffectData prevData = swordEffectData.getFirst();
        for (float count = 1; count <= updateCount; count++) {
            float delta = Mth.lerp(count / (float) updateCount, 0, 1);
            Vector3fc base = prevData.base().lerp(position().toVector3f(), delta, new Vector3f());
            Quaternionfc rotation = prevData.rotation().slerp(getRotation(), delta, new Quaternionf());
            swordEffectData.removeLast();
            swordEffectData.addFirst(SwordEffectData.of(base, DIRECTION, rotation, LENGTH, getScale()));
        }

        if (level().isClientSide()) {
            if (tickCount % 100 == 0) {
                motion = SwordMotion.builder()
                        .channel(SwordMotionChannel.rotation(true)
                                .keyframe(MotionKeyframe.rotation(0))
                                .keyframe(MotionKeyframe.rotation(40)
                                        .value(Axis.XP.rotationDegrees(90)
                                                .rotateZ(Mth.PI)
                                        )
                                        .interpolation(Interpolation.Q_FAST_STEP_OUT)
                                )
                                .keyframe(MotionKeyframe.rotation(42)
                                        .value(Axis.XP.rotationDegrees(90)
                                                .rotateZ(-Mth.TWO_PI)
                                        )
                                )
                                .keyframe(MotionKeyframe.rotation(45)
                                        .value(Axis.XP.rotationDegrees(90)
                                                .rotateZ(Mth.HALF_PI * 0.5f)
                                        )
                                )
                                .keyframe(MotionKeyframe.rotation(65)
                                        .value(Axis.XP.rotationDegrees(180)
                                                .rotateY(Mth.PI)
                                        )
                                        .interpolation(Interpolation.Q_FAST_STEP_IN)
                                )
                                .keyframe(MotionKeyframe.rotation(75)
                                        .value(Axis.XP.rotationDegrees(180)
                                                .rotateZ(-Mth.HALF_PI * 0.3f)
                                        )
                                )
                                .keyframe(MotionKeyframe.rotation(90)
                                        .value(Axis.XP.rotationDegrees(180)
                                                .rotateZ(Mth.PI * 0.8f)
                                        )
                                        .interpolation(Interpolation.Q_FAST_STEP_IN)
                                )
                                .keyframe(MotionKeyframe.rotation(99))
                        )
                        .channel(SwordMotionChannel.translation(true)
                                .keyframe(MotionKeyframe.position(0))
                                .keyframe(MotionKeyframe.position(20, 0, -2, 0)
                                        .interpolation(Interpolation.V_FAST_STEP_OUT)
                                )
                                .keyframe(MotionKeyframe.position(40, 0, -2, -4))
                                .keyframe(MotionKeyframe.position(41, 4, -2, 0))
                                .keyframe(MotionKeyframe.position(42, 0, -2, 4))
                                .keyframe(MotionKeyframe.position(65, 0, -4, 0)
                                        .interpolation(Interpolation.V_FAST_STEP_IN)
                                )
                                .keyframe(MotionKeyframe.position(75, 0, -4, 0))
                                .keyframe(MotionKeyframe.position(90, -2, -0.5, 0)
                                        .interpolation(Interpolation.V_FAST_STEP_IN)
                                )
                                .keyframe(MotionKeyframe.position(99))
                        )
                        .build();
            }
            motion.tick(this);
        }
    }

    public void setScale(float scale) {
        entityData.set(SCALE, scale);
    }

    public float getScale() {
        return entityData.get(SCALE);
    }

    public void setRotation(Quaternionfc rotation) {
        this.rotation.set(rotation);
    }

    public Quaternionfc getRotation() {
        return this.rotation;
    }

    public Quaternionfc getRotation(float partialTicks) {
        return prevRotation.slerp(rotation, partialTicks, new Quaternionf());
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
