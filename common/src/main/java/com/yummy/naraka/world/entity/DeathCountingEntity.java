package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;

public interface DeathCountingEntity {
    LivingEntity living();

    DeathCountingInstance getCountingInstance();

    default void countDeath(LivingEntity livingEntity) {
        getCountingInstance().add(livingEntity);
        DeathCountHelper.applyDeathCount(livingEntity);
    }

    default void forget(LivingEntity livingEntity) {
        getCountingInstance().remove(livingEntity);
        DeathCountHelper.removeDeathCount(livingEntity);
    }

    default void releaseAllCounting() {
        if (living().level() instanceof ServerLevel serverLevel) {
            for (LivingEntity countedEntity : getCountingInstance().countedEntities(serverLevel))
                DeathCountHelper.removeDeathCount(countedEntity);
            getCountingInstance().countedEntities.clear();
        }
    }

    default boolean isCounting(LivingEntity livingEntity) {
        return getCountingInstance().contains(livingEntity);
    }

    void onEntityUseDeathCount(LivingEntity livingEntity);

    class DeathCountingInstance {
        private final List<UUID> countedEntities = new ArrayList<>();

        public void add(LivingEntity livingEntity) {
            countedEntities.add(livingEntity.getUUID());
        }

        public void remove(LivingEntity livingEntity) {
            countedEntities.remove(livingEntity.getUUID());
        }

        public boolean contains(LivingEntity livingEntity) {
            return countedEntities.contains(livingEntity.getUUID());
        }

        public Set<LivingEntity> countedEntities(ServerLevel serverLevel) {
            Set<LivingEntity> result = new HashSet<>();
            for (UUID uuid : countedEntities) {
                LivingEntity livingEntity = NarakaEntityUtils.findEntityByUUID(serverLevel, uuid, LivingEntity.class);
                if (livingEntity != null) {
                    result.add(livingEntity);
                }
            }
            return result;
        }

        public void save(CompoundTag compoundTag) {
            NarakaNbtUtils.store(compoundTag, "DeathCountedEntities", UUIDUtil.CODEC.listOf(), countedEntities);
        }

        public void load(CompoundTag compoundTag) {
            NarakaNbtUtils.read(compoundTag, "DeathCountedEntities", UUIDUtil.CODEC.listOf()).ifPresent(countedEntities::addAll);
        }
    }
}
