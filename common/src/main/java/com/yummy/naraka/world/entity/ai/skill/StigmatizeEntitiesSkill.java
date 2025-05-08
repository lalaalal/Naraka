package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.StigmatizingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.Collection;

public class StigmatizeEntitiesSkill<T extends SkillUsingMob & StigmatizingEntity> extends Skill<T> {
    public static final String NAME = "stigmatize_entities";

    public StigmatizeEntitiesSkill(T mob) {
        super(NAME, 10, 60, mob);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    protected void skillTick(ServerLevel level) {

    }

    @Override
    protected void onLastTick(ServerLevel level) {
        Collection<LivingEntity> entities = level.getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.forCombat(),
                mob,
                mob.getBoundingBox().inflate(20)
        );
        for (LivingEntity target : entities)
            mob.stigmatizeEntity(level, target);
    }
}
