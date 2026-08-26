package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.BeamEffect;
import com.yummy.naraka.world.entity.MotionEntity;
import com.yummy.naraka.world.entity.ScarfWavingData;
import com.yummy.naraka.world.item.equipmentset.EquipmentSetGroup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class NarakaEntityDataTypes {
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Stigma, LivingEntity>> STIGMA = register(
            "stigma", EntityDataType.living(Stigma.CODEC)
                    .networkSynchronized(Stigma.STREAM_CODEC)
                    .defaultValue(Stigma.ZERO)
                    .serverTicker(StigmaHelper::tick)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Integer, LivingEntity>> DEATH_COUNT = register(
            "death_count", EntityDataType.living(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .defaultValue(-1)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Double, LivingEntity>> LOCKED_HEALTH = register(
            "locked_health", EntityDataType.living(Codec.DOUBLE)
                    .networkSynchronized(ByteBufCodecs.DOUBLE)
                    .defaultValue(0.0)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Integer, LivingEntity>> PURIFIED_SOUL_FIRE_TICK = register(
            "purified_soul_fire_tick", EntityDataType.living(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .defaultValue(0)
                    .serverTicker(NarakaEntityDataTypes::tickPurifiedSoulFire)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<ScarfWavingData, LivingEntity>> SCARF_WAVING_DATA = register(
            "scarf_waving_data", EntityDataType.living(Codec.unit(ScarfWavingData::new))
                    .defaultValue(ScarfWavingData::new)
                    .clientTicker(NarakaEntityDataTypes::tickScarfWavingData)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<List<BeamEffect>, Entity>> BEAM_EFFECTS = register(
            "beam_effects", EntityDataType.common(BeamEffect.CODEC.listOf())
                    .networkSynchronized(ByteBufCodecs.fromCodec(BeamEffect.CODEC).apply(ByteBufCodecs.list()))
                    .defaultValue(List::of)
                    .clientTicker(NarakaEntityDataTypes::tickBeamEffects)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Boolean, Entity>> KEEP_UNFROZEN = register(
            "keep_unfrozen", EntityDataType.common(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .defaultValue(false)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<Integer, LivingEntity>> STUN_TICK = register(
            "stun_tick", EntityDataType.living(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .defaultValue(0)
                    .clientTicker(NarakaEntityDataTypes::clientTickStun)
                    .serverTicker(NarakaEntityDataTypes::serverTickStun)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<MotionData, MotionEntity>> MOTION_DATA = register(
            "motion_data", EntityDataType.builder(MotionData.CODEC, MotionEntity.class)
                    .networkSynchronized(MotionData.STREAM_CODEC)
                    .defaultValue(MotionData.DEFAULT)
    );
    public static final HolderProxy<EntityDataType<?, ?>, EntityDataType<EquipmentSetGroup, LivingEntity>> ACTIVE_EQUIPMENT_SET_GROUP = register(
            "active_equipment_set_group", EntityDataType.living(EquipmentSetGroup.CODEC)
                    .networkSynchronized(EquipmentSetGroup.STREAM_CODEC)
                    .defaultValue(EquipmentSetGroup.EMPTY)
    );

    private static void tickPurifiedSoulFire(ServerLevel level, LivingEntity livingEntity, int purifiedSoulFireTick) {
        if (purifiedSoulFireTick > 0) {
            if (purifiedSoulFireTick % 20 == 0)
                livingEntity.hurt(NarakaDamageSources.purifiedSoulFire(livingEntity.registryAccess()), 6);
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get(), purifiedSoulFireTick - 1);
        }
    }

    private static void tickScarfWavingData(Level level, LivingEntity livingEntity, ScarfWavingData scarfWavingData) {
        scarfWavingData.update(livingEntity.getDeltaMovement(), livingEntity.yBodyRot - livingEntity.yBodyRotO, livingEntity.onGround());
    }

    private static void tickBeamEffects(Level level, Entity entity, List<BeamEffect> beamEffects) {
        List<BeamEffect> finished = beamEffects.stream()
                .filter(beamEffect -> beamEffect.isFinished(entity.tickCount))
                .toList();
        BeamEffectsHelper.remove(entity, finished);
    }

    private static void clientTickStun(Level level, LivingEntity livingEntity, int stunTick) {
        if (livingEntity.tickCount % 2 == 0) {
            double width = livingEntity.getBbWidth();
            double x = livingEntity.getX() + Math.cos(livingEntity.tickCount * 0.5) * width * 0.5;
            double z = livingEntity.getZ() + Math.sin(livingEntity.tickCount * 0.5) * width * 0.5;
            double y = livingEntity.getBoundingBox().maxY + 0.25;
            livingEntity.level().addParticle(ParticleTypes.FIREWORK, x, y, z, 0, 0, 0);
        }
    }

    private static void serverTickStun(ServerLevel level, LivingEntity livingEntity, int stunTick) {
        if (stunTick > 0) {
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STUN_TICK.get(), stunTick - 1);
        } else {
            EntityDataHelper.removeEntityData(livingEntity, NarakaEntityDataTypes.STUN_TICK.get());
            StunHelper.releaseEntity(livingEntity);
        }
    }

    private static <T, E extends Entity> HolderProxy<EntityDataType<?, ?>, EntityDataType<T, E>> register(String name, EntityDataType.Builder<T, E> builder) {
        return RegistryProxy.register(NarakaRegistries.Keys.ENTITY_DATA_TYPE, name, () -> builder.id(NarakaMod.location(name)).build());
    }

    public static void initialize(NarakaInitializer initializer) {
        initializer.runAfterRegistryLoaded(StigmaHelper::initialize);
    }
}
