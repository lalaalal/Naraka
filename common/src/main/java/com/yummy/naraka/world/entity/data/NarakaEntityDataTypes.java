package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.ScarfWavingData;
import net.minecraft.world.entity.LivingEntity;

public class NarakaEntityDataTypes {
    public static final HolderProxy<EntityDataType<?>, EntityDataType<Stigma>> STIGMA = register(
            "stigma", EntityDataType.builder(Stigma.CODEC)
                    .defaultValue(Stigma.ZERO)
    );
    public static final HolderProxy<EntityDataType<?>, EntityDataType<Integer>> DEATH_COUNT = register(
            "death_count", EntityDataType.builder(Codec.INT)
                    .defaultValue(-1)
    );
    public static final HolderProxy<EntityDataType<?>, EntityDataType<Double>> LOCKED_HEALTH = register(
            "locked_health", EntityDataType.builder(Codec.DOUBLE)
                    .defaultValue(0.0)
    );
    public static final HolderProxy<EntityDataType<?>, EntityDataType<Integer>> PURIFIED_SOUL_FIRE_TICK = register(
            "purified_soul_fire_tick", EntityDataType.builder(Codec.INT)
                    .defaultValue(0)
                    .ticker(NarakaEntityDataTypes::tickPurifiedSoulFire)
    );
    public static final HolderProxy<EntityDataType<?>, EntityDataType<ScarfWavingData>> SCARF_WAVING_DATA = register(
            "scarf_waving_data", EntityDataType.builder(Codec.unit(ScarfWavingData::new))
                    .defaultValue(ScarfWavingData::new)
                    .ticker(NarakaEntityDataTypes::tickScarfWavingData)
    );

    private static void tickPurifiedSoulFire(LivingEntity livingEntity, int purifiedSoulFireTick) {
        if (purifiedSoulFireTick > 0) {
            if (purifiedSoulFireTick % 20 == 0)
                livingEntity.hurt(NarakaDamageSources.purifiedSoulFire(livingEntity.registryAccess()), 6);
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get(), purifiedSoulFireTick - 1);
        }
    }

    private static void tickScarfWavingData(LivingEntity livingEntity, ScarfWavingData scarfWavingData) {
        if (livingEntity.level().isClientSide())
            scarfWavingData.update(livingEntity.getDeltaMovement(), livingEntity.yBodyRot - livingEntity.yBodyRotO, livingEntity.onGround());
    }

    private static <T> HolderProxy<EntityDataType<?>, EntityDataType<T>> register(String name, EntityDataType.Builder<T> builder) {
        return RegistryProxy.register(NarakaRegistries.Keys.ENTITY_DATA_TYPE, name, () -> builder.id(NarakaMod.location(name)).build());
    }

    public static void initialize(NarakaInitializer initializer) {
        initializer.runAfterRegistryLoaded(StigmaHelper::initialize);
    }
}
