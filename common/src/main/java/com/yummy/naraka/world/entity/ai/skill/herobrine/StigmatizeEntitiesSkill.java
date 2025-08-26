package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public class StigmatizeEntitiesSkill extends Skill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("herobrine.stigmatize_entities");

    public StigmatizeEntitiesSkill(Herobrine mob) {
        super(LOCATION, mob, Integer.MAX_VALUE, 0);
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
        runAt(20, () -> mob.setDeltaMovement(0, 0.3, 0));
        runBefore(40, () -> mob.setDeltaMovement(mob.getDeltaMovement().scale(0.8)));
        runAfter(39, () -> mob.setDeltaMovement(Vec3.ZERO));
        if ((tickCount - 40) % 60 == 0)
            mob.setAnimation(HerobrineAnimationLocations.STIGMATIZE_ENTITIES);
        if ((tickCount - 57) % 60 == 0)
            stigmatize(level, 20);
    }

    public void stigmatize(ServerLevel level, int radius) {
        Collection<LivingEntity> entities = level.getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.forCombat(),
                mob,
                mob.getBoundingBox().inflate(radius)
        );
        for (LivingEntity target : entities)
            mob.stigmatizeEntity(level, target);

        for (int yRot = 0; yRot < 360; yRot++) {
            double xSpeed = Math.cos(Math.toRadians(yRot));
            double zSpeed = Math.sin(Math.toRadians(yRot));
            level.sendParticles(NarakaFlameParticleOption.REDSTONE, mob.getX(), mob.getY() + 1.5, mob.getZ(), 0, xSpeed, 0, zSpeed, 1);
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
