package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ParryingSkill extends AttackSkill<AbstractHerobrine> {
    public static final ResourceLocation LOCATION = createLocation("final_herobrine.parrying");

    private static final int PARRYING_START_TICK = 10;
    private static final int PARRYING_END_TICK = 40;
    private static final int PARRYING_DURATION = 55;
    private boolean succeed;
    private float originalHealth;
    private float hurtDamage;

    public ParryingSkill(AbstractHerobrine mob) {
        super(LOCATION, 60, 300, 100, 5, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        succeed = false;
        originalHealth = mob.getHealth();
        hurtDamage = 0;
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return hurtDamage;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(25);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        run(succeed, () -> handleSucceed(level, target));
    }

    private boolean hurtJustNow() {
        return mob.tickCount - mob.getLastHurtByMobTimestamp() < 2;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        run(succeed, () -> handleSucceed(level));
        run(at(PARRYING_END_TICK) && !succeed, () -> mob.setAnimation(HerobrineAnimationLocations.PARRYING_FAILED));
        if (between(PARRYING_START_TICK, PARRYING_END_TICK) && hurtJustNow() && !succeed) {
            mob.setAnimation(HerobrineAnimationLocations.PARRYING_SUCCEED);
            hurtDamage = originalHealth - mob.getHealth();
            mob.heal(hurtDamage * 2);
            succeed = true;
            tickCount = duration - PARRYING_DURATION;
        }
        runBetween(PARRYING_START_TICK, PARRYING_START_TICK + 5, () -> level.sendParticles(NarakaParticleTypes.PARRYING.get(), mob.getX(), mob.getY() + 1, mob.getZ(), 25, 0, 0.5, 0, 1));
    }

    private int tickCount(int succeedTick) {
        return succeedTick + (duration - PARRYING_DURATION);
    }

    private Vec3 movement(Vec3 original) {
        return original.multiply(0.8, 0, 0.8);
    }

    private void handleSucceed(ServerLevel level, LivingEntity target) {
        run(at(tickCount(2)) && targetInLookAngle(target, -Mth.HALF_PI * 0.67f, Mth.HALF_PI * 0.67f), () -> hurtEntity(level, target));
        runAt(tickCount(2), () -> level.sendParticles(NarakaFlameParticleOption.NECTARIUM, mob.getX(), mob.getY() + 1, mob.getZ(), 15, 1, 0.3, 1, 0.1));
        runBetween(tickCount(20), tickCount(40), () -> moveToTarget(target, false, this::movement));
        runBetween(tickCount(20), tickCount(30), () -> level.sendParticles(NarakaFlameParticleOption.NECTARIUM, mob.getX(), mob.getY() + 1, mob.getZ(), 15, 2, 0.3, 2, 0.1));
        runAt(tickCount(40), this::stopMoving);
    }

    private void handleSucceed(ServerLevel level) {
        runBetween(tickCount(20), tickCount(35), () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 1.5));
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
    }
}
