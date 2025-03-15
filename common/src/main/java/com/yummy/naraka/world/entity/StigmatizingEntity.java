package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.data.Stigma;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface StigmatizingEntity {
    void stigmatizeEntity(LivingEntity target);

    /**
     * Collects stigma
     *
     * @param stigma Collecting stigma
     * @see Stigma#consume(LivingEntity, Entity)
     */
    void collectStigma(Stigma stigma);
}
