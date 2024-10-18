package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.tags.NarakaEntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class StigmaHelper {
    private static final List<LivingEntity> MARKED_ENTITIES = new ArrayList<>();

    public static Stigma get(LivingEntity livingEntity) {
        return EntityDataHelper.getEntityData(livingEntity, NarakaEntityDataTypes.STIGMA);
    }

    private static void set(LivingEntity livingEntity, Stigma stigma) {
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STIGMA, stigma);
    }

    public static void increaseStigma(LivingEntity livingEntity, Entity cause) {
        if (livingEntity.getType().is(NarakaEntityTypeTags.HEROBRINE))
            return;
        Stigma stigma = get(livingEntity);
        Stigma increased = stigma.increase(livingEntity, cause);
        set(livingEntity, increased);
    }

    public static void tick() {
        List<LivingEntity> liberatedEntities = new ArrayList<>();
        for (LivingEntity markedEntity : MARKED_ENTITIES) {
            Stigma stigma = get(markedEntity);
            Stigma decreased = stigma.tryDecrease(markedEntity);

            if ((stigma != decreased && decreased.value() == 0) || markedEntity.isRemoved())
                liberatedEntities.add(markedEntity);
            if (stigma != decreased)
                set(markedEntity, decreased);
        }
        MARKED_ENTITIES.removeAll(liberatedEntities);
    }

    public static void initialize() {
        EntityDataHelper.registerDataChangeListener(NarakaEntityDataTypes.STIGMA, StigmaHelper::watchEntity);
    }

    public static void clear() {
        MARKED_ENTITIES.clear();
    }

    private static void watchEntity(LivingEntity livingEntity, EntityDataType<Stigma> entityDataType, Stigma from, Stigma to) {
        if (to.value() > 0 && !MARKED_ENTITIES.contains(livingEntity))
            MARKED_ENTITIES.add(livingEntity);
    }
}
