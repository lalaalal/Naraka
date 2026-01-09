package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CorruptedStar extends LightTailEntity {
    public static final EntityDataAccessor<QuadraticBezier> PREPARE_BEZIER = SynchedEntityData.defineId(CorruptedStar.class, NarakaEntityDataSerializers.BEZIER);
    public static final EntityDataAccessor<Integer> PREPARE_DURATION = SynchedEntityData.defineId(CorruptedStar.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Vec3> BASE_POSITION = SynchedEntityData.defineId(CorruptedStar.class, NarakaEntityDataSerializers.VEC3);

    public CorruptedStar(EntityType<? extends CorruptedStar> entityType, Level level) {
        super(entityType, level, 80, 8);
        setTailColor(SoulType.COPPER.color);
    }

    public CorruptedStar(Level level, Vec3 position, QuadraticBezier bezier, int prepareDuration) {
        this(NarakaEntityTypes.CORRUPTED_STAR.get(), level);
        setPos(position);
        entityData.set(PREPARE_DURATION, prepareDuration);
        entityData.set(BASE_POSITION, position);
        entityData.set(PREPARE_BEZIER, bezier);
    }

    public CorruptedStar(Level level, Vec3 position, QuadraticBezier bezier) {
        this(level, position, bezier, 40);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PREPARE_BEZIER, QuadraticBezier.ZERO)
                .define(PREPARE_DURATION, 40)
                .define(BASE_POSITION, Vec3.ZERO);
    }

    @Override
    public void tick() {
        if (tickCount <= entityData.get(PREPARE_DURATION))
            preparingTick();
        else {
            super.tick();
            needsSync = true;
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected @Nullable ParticleOptions getTrailParticle() {
        return null;
    }

    private void preparingTick() {
        QuadraticBezier bezier = entityData.get(PREPARE_BEZIER);
        Vec3 basePosition = entityData.get(BASE_POSITION);
        float prepareDuration = entityData.get(PREPARE_DURATION);

        float delta = NarakaUtils.interpolate(tickCount / prepareDuration, 0, 1, NarakaUtils::fastStepIn);

        Vec3 position = basePosition.add(bezier.interpolate(delta));
        setPos(position);

        final int tailUpdateCount = entityData.get(TAIL_UPDATE_COUNT);
        for (float count = 1; count <= tailUpdateCount; count++) {
            float positionDelta = count / (float) tailUpdateCount;
            float tailDelta = NarakaUtils.interpolate((tickCount + positionDelta - 1) / prepareDuration, 0, 1, NarakaUtils::fastStepIn);
            Vec3 tailPosition = basePosition.add(bezier.interpolate(tailDelta));
            tailPositions.addFirst(tailPosition);
            tailPositions.removeLast();
        }
    }
}
