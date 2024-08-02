package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public interface DeathCountingEntity {
    LivingEntity living();

    DeathCountingInstance getCountingInstance();

    default void countDeath(LivingEntity livingEntity) {
        getCountingInstance().add(livingEntity);
        DeathCountHelper.applyDeathCount(livingEntity);
    }

    default void forget(LivingEntity livingEntity) {
        getCountingInstance().remove(livingEntity);
        DeathCountHelper.unapplyDeathCount(livingEntity);
    }

    default boolean isCounting(LivingEntity livingEntity) {
        return getCountingInstance().contains(livingEntity);
    }

    default void releaseAllCounting() {
        if (living().level() instanceof ServerLevel serverLevel) {
            for (LivingEntity countedEntity : getCountingInstance().countedEntities(serverLevel))
                DeathCountHelper.unapplyDeathCount(countedEntity);
            getCountingInstance().countedEntities.clear();
        }
    }

    class DeathCountingInstance {
        private final Set<UUID> countedEntities = new HashSet<>();

        private CompoundTag write(UUID value, CompoundTag tag, HolderLookup.Provider provider) {
            tag.putUUID("UUID", value);
            return tag;
        }

        private UUID read(CompoundTag tag, HolderLookup.Provider provider) {
            return tag.getUUID("UUID");
        }

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
            NarakaNbtUtils.writeCollection(compoundTag, "DeathCountedEntities", countedEntities, this::write, null);
        }

        public void load(CompoundTag compoundTag) {
            NarakaNbtUtils.readCollection(compoundTag, "DeathCountedEntities", () -> countedEntities, this::read, null);
        }
    }
}
