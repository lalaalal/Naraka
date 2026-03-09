package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.motion.Interpolation;
import com.yummy.naraka.world.entity.motion.SwordMotion;
import com.yummy.naraka.world.entity.motion.SwordMotionChannel;
import com.yummy.naraka.world.entity.motion.SwordMotionTypes;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

public class NarakaSword extends Entity {
    public static final EntityDataAccessor<SoulType> SOUL_TYPE = SynchedEntityData.defineId(NarakaSword.class, NarakaEntityDataSerializers.SOUL_TYPE);
    public static final EntityDataAccessor<Vector3fc> ROTATION = SynchedEntityData.defineId(NarakaSword.class, EntityDataSerializers.VECTOR3);

    public static final Vec3 DIRECTION = new Vec3(0, 0.1f, 0);
    public static final float LENGTH = 2.75f;

    private final List<SwordEffectData> swordEffectData = new ArrayList<>();
    private final Vector3f prevRotation = new Vector3f();
    private SwordMotion motion = SwordMotionTypes.LONGINUS.build();

    public NarakaSword(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
        for (int i = 0; i < 64; i++)
            swordEffectData.add(SwordEffectData.of(Vec3.ZERO, DIRECTION, prevRotation, 0));
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
                .define(ROTATION, new Vector3f());
    }

    @Override
    public void tick() {
        super.tick();

        prevRotation.set(getRotation());

        final int updateCount = 4;
        SwordEffectData prevData = swordEffectData.getFirst();
        for (float count = 1; count <= updateCount; count++) {
            float delta = Mth.lerp(count / (float) updateCount, 0, 1);
            Vec3 base = prevData.base().lerp(position(), delta);
            Vector3f rotation = prevData.rotation().lerp(getRotation(), delta, new Vector3f());
            swordEffectData.removeLast();
            swordEffectData.addFirst(SwordEffectData.of(base, DIRECTION, rotation, LENGTH));
        }

        if (level().isClientSide()) {
            if (tickCount % 100 == 0)
                motion = SwordMotion.builder()
                        .channel(SwordMotionChannel.rotation()
                                .keyframe(0).build()
                                .keyframe(20).value(0, 1, -Math.PI * 0.8).interpolation(Interpolation.FAST_STEP_IN).build()
                                .keyframe(60).build()
                        )
                        .channel(SwordMotionChannel.translation()
                                .keyframe(0).build()
                                .keyframe(20).value(4, -3, 0).interpolation(Interpolation.FAST_STEP_IN).build()
                                .keyframe(60).build()
                        )
                        .build();
            motion.tick(this);
        }
    }

    public void setRotation(Vector3fc rotation) {
        entityData.set(ROTATION, rotation);
    }

    public Vector3fc getRotation() {
        return entityData.get(ROTATION);
    }

    public Vector3fc getRotation(float partialTicks) {
        return prevRotation.lerp(getRotation(), partialTicks);
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
