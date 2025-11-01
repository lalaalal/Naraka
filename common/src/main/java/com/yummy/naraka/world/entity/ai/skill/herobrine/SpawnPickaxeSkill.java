package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.NarakaPickaxe;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.ai.skill.naraka_pickaxe.StrikeSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SpawnPickaxeSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final_herobrine.pickaxe_strike");

    private static final int SHOOT_TICK = 10;
    private static final int SHOOT_DURATION = 20;

    private Vec3 destination = Vec3.ZERO;

    public SpawnPickaxeSkill(Herobrine mob) {
        super(LOCATION, mob, 70, 400);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    public void prepare() {
        super.prepare();
        destination = mob.position().add(0, 15, 0);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        sendShootingParticles(level);
        runAt(SHOOT_TICK + SHOOT_DURATION, () -> spawnNarakaPickaxe(level));
        stopMoving();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(SHOOT_TICK - 1, () -> destination = target.position().add(0, 15, 0));
    }

    private void sendShootingParticles(ServerLevel level) {
        if (SHOOT_TICK <= tickCount && tickCount < SHOOT_TICK + SHOOT_DURATION) {
            int tick = tickCount - SHOOT_TICK;
            float deltaStart = tick / (float) SHOOT_DURATION;
            float deltaEnd = tick + 1 / (float) SHOOT_DURATION;

            Vec3 from = NarakaUtils.interpolateVec3(deltaStart, mob.position(), destination, NarakaUtils::fastStepIn);
            Vec3 to = NarakaUtils.interpolateVec3(deltaEnd, mob.position(), destination, NarakaUtils::fastStepIn);

            for (float i = 0; i <= 1; i += 0.1f) {
                Vec3 position = NarakaUtils.lerp(i, from, to);
                level.sendParticles(NarakaFlameParticleOption.EMERALD, position.x, position.y, position.z, 5, 0.2, 0.2, 0.2, 0);
            }
        }
    }

    private void spawnNarakaPickaxe(ServerLevel level) {
        NarakaPickaxe narakaPickaxe = new NarakaPickaxe(level, mob);
        narakaPickaxe.setPos(destination);
        level.addFreshEntity(narakaPickaxe);
        narakaPickaxe.useSkill(StrikeSkill.LOCATION);
    }
}
