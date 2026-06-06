package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.MotionData;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import com.yummy.naraka.world.entity.motion.Motion;
import com.yummy.naraka.world.entity.motion.MotionTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

import java.util.List;

public abstract class MotionEntity extends Entity implements Motionable {
    public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(MotionEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Quaternionf> ROTATION = SynchedEntityData.defineId(MotionEntity.class, EntityDataSerializers.QUATERNION);

    protected final Quaternionf prevRotation = new Quaternionf();
    protected Motion motion = MotionTypes.EMPTY_TYPE.create();

    protected MotionEntity(EntityType<? extends MotionEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SCALE, 1.0f)
                .define(ROTATION, new Quaternionf());
    }

    @Override
    public void tick() {
        super.tick();
        prevRotation.set(getRotation());

        if (level().isClientSide())
            updateMotion();
    }

    public void setMotion(ResourceLocation id, List<Vec3> positions, List<Quaternionf> rotations) {
        EntityDataHelper.setEntityData(
                this,
                NarakaEntityDataTypes.MOTION_DATA.get(),
                new MotionData(id, positions, rotations)
        );
    }

    public void setMotion(ResourceLocation id) {
        setMotion(id, List.of(), List.of());
    }

    private void updateMotion() {
        MotionData motionData = EntityDataHelper.getRawEntityData(this, NarakaEntityDataTypes.MOTION_DATA.get());
        if (!motion.getId().equals(motionData.id())) {
            motion = MotionTypes.create(motionData);
            onMotionUpdated(motionData);
        }
        motion.tick(this);
    }

    protected void onMotionUpdated(MotionData motionData) {

    }

    @Override
    public Vec3 getPosition() {
        return position();
    }

    @Override
    public void setPosition(Vec3 position) {
        setPos(position);
    }

    @Override
    public Quaternionf getRotation() {
        return entityData.get(ROTATION);
    }

    public Quaternionf getRotation(float partialTicks) {
        return prevRotation.slerp(getRotation(), partialTicks, new Quaternionf());
    }

    @Override
    public void setRotation(Quaternionfc rotation) {
        entityData.set(ROTATION, new Quaternionf(rotation));
    }

    @Override
    public float getScale() {
        return entityData.get(SCALE);
    }

    @Override
    public void setScale(float scale) {
        entityData.set(SCALE, scale);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag output) {
        output.putFloat("Scale", getScale());
        NarakaNbtUtils.store(output, "Rotation", ExtraCodecs.QUATERNIONF, getRotation());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag input) {
        setScale(input.getFloat("Scale"));
        NarakaNbtUtils.read(input, "Rotation", ExtraCodecs.QUATERNIONF)
                .ifPresent(this::setRotation);
    }
}
