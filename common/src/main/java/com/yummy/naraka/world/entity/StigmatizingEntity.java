package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.data.Stigma;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface StigmatizingEntity {
    void stigmatizeEntity(ServerLevel level, LivingEntity target);

    /**
     * Collect stigma
     *
     * @param original Collecting stigma
     * @see Stigma#consume(ServerLevel, LivingEntity, Entity)
     */
    void collectStigma(ServerLevel level, LivingEntity target, Stigma original);
}
