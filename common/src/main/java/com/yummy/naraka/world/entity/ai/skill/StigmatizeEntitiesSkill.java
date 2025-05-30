package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public class StigmatizeEntitiesSkill extends Skill<Herobrine> {
    public static final String NAME = "stigmatize_entities";

    public StigmatizeEntitiesSkill(Herobrine mob) {
        super(NAME, Integer.MAX_VALUE, 0, mob);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return true;
    }

    @Override
    public void prepare() {
        super.prepare();
        mob.setNoGravity(true);
        duration = Integer.MAX_VALUE;
    }

    @Override
    protected void skillTick(ServerLevel level) {
        if (tickCount == 20)
            mob.setDeltaMovement(0, 0.3, 0);
        if (tickCount == 30)
            stigmatize(level);

        if (tickCount < 40)
            mob.setDeltaMovement(mob.getDeltaMovement().scale(0.8));
        else
            mob.setDeltaMovement(Vec3.ZERO);

        if ((tickCount - 40) % 60 == 0)
            mob.setAnimation(AnimationLocations.STIGMATIZE_ENTITIES);
        if ((tickCount - 57) % 60 == 0)
            stigmatize(level);
    }

    private void stigmatize(ServerLevel level) {
        Collection<LivingEntity> entities = level.getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.forCombat(),
                mob,
                mob.getBoundingBox().inflate(20)
        );
        for (LivingEntity target : entities)
            mob.stigmatizeEntity(level, target);

        for (int yRot = 0; yRot < 360; yRot++) {
            double xSpeed = Math.cos(Math.toRadians(yRot));
            double zSpeed = Math.sin(Math.toRadians(yRot));
            level.sendParticles(NarakaParticleTypes.GOLDEN_FLAME.get(), mob.getX(), mob.getY() + 1.5, mob.getZ(), 0, xSpeed, 0, zSpeed, 1);
        }
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.setNoGravity(false);
    }

    @Override
    public void interrupt() {
        mob.setNoGravity(false);
    }
}
