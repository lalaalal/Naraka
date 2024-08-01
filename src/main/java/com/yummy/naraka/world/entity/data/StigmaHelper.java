package com.yummy.naraka.world.entity.data;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class StigmaHelper {
    private static final List<LivingEntity> MARKED_ENTITIES = new ArrayList<>();

    public static void increaseStigma(LivingEntity livingEntity, Entity cause) {
        Stigma stigma = EntityDataHelper.getEntityData(livingEntity, EntityDataTypes.STIGMA);
        if (!MARKED_ENTITIES.contains(livingEntity))
            MARKED_ENTITIES.add(livingEntity);
        stigma.increaseStigma(livingEntity, cause);
        sync(livingEntity, stigma);
    }

    public static void tick() {
        List<LivingEntity> liberatedEntities = new ArrayList<>();
        for (LivingEntity markedEntity : MARKED_ENTITIES) {
            Stigma stigma = EntityDataHelper.getEntityData(markedEntity, EntityDataTypes.STIGMA);
            stigma.tryRelease(markedEntity);
            if (stigma.tryDecrease(markedEntity) || markedEntity.isDeadOrDying())
                liberatedEntities.add(markedEntity);
            sync(markedEntity, stigma);
        }
        MARKED_ENTITIES.removeAll(liberatedEntities);
    }

    private static void sync(LivingEntity livingEntity, Stigma stigma) {
        if (stigma.isDirty())
            EntityDataHelper.syncEntityData(livingEntity, EntityDataTypes.STIGMA);
        stigma.setDirty(false);
    }
}
