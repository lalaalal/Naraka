package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.BeamEffect;
import com.yummy.naraka.world.entity.ScarfWavingData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class NarakaEntityDataTypes {
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Stigma, LivingEntity>> STIGMA = register(
            "stigma", EntityDataType.living(Stigma.CODEC)
                    .defaultValue(Stigma.ZERO)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Integer, LivingEntity>> DEATH_COUNT = register(
            "death_count", EntityDataType.living(Codec.INT)
                    .defaultValue(-1)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Double, LivingEntity>> LOCKED_HEALTH = register(
            "locked_health", EntityDataType.living(Codec.DOUBLE)
                    .defaultValue(0.0)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Integer, LivingEntity>> PURIFIED_SOUL_FIRE_TICK = register(
            "purified_soul_fire_tick", EntityDataType.living(Codec.INT)
                    .defaultValue(0)
                    .ticker(NarakaEntityDataTypes::tickPurifiedSoulFire)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<ScarfWavingData, LivingEntity>> SCARF_WAVING_DATA = register(
            "scarf_waving_data", EntityDataType.living(Codec.unit(ScarfWavingData::new))
                    .defaultValue(ScarfWavingData::new)
                    .ticker(NarakaEntityDataTypes::tickScarfWavingData)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<List<BeamEffect>, Entity>> BEAM_EFFECTS = register(
            "beam_effects", EntityDataType.common(BeamEffect.CODEC.listOf())
                    .defaultValue(List::of)
                    .ticker(NarakaEntityDataTypes::tickBeamEffects)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Boolean, Entity>> KEEP_UNFROZEN = register(
            "keep_unfrozen", EntityDataType.common(Codec.BOOL)
                    .defaultValue(false)
    );

    private static void tickPurifiedSoulFire(LivingEntity livingEntity, int purifiedSoulFireTick) {
        if (purifiedSoulFireTick > 0 && !livingEntity.level().isClientSide()) {
            if (purifiedSoulFireTick % 20 == 0)
                livingEntity.hurt(NarakaDamageSources.purifiedSoulFire(livingEntity.registryAccess()), 6);
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get(), purifiedSoulFireTick - 1);
        }
    }

    private static void tickScarfWavingData(LivingEntity livingEntity, ScarfWavingData scarfWavingData) {
        if (livingEntity.level().isClientSide())
            scarfWavingData.update(livingEntity.getDeltaMovement(), livingEntity.yBodyRot - livingEntity.yBodyRotO, livingEntity.onGround());
    }

    private static void tickBeamEffects(Entity entity, List<BeamEffect> beamEffects) {
        if (entity.level().isClientSide()) {
            List<BeamEffect> finished = beamEffects.stream()
                    .filter(beamEffect -> beamEffect.isFinished(entity.tickCount))
                    .toList();
            BeamEffectsHelper.remove(entity, finished);
        }
    }

    private static <T, E extends Entity> HolderProxy<EntityDataType<?, ?>, EntityDataType<T, E>> register(String name, EntityDataType.Builder<T, E> builder) {
        return RegistryProxy.register(NarakaRegistries.Keys.ENTITY_DATA_TYPE, name, () -> builder.id(NarakaMod.location(name)).build());
    }

    public static void initialize(NarakaInitializer initializer) {
        initializer.runAfterRegistryLoaded(StigmaHelper::initialize);
    }
}
