package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GodBlowSkill extends Skill {
    private final boolean[] hurt = new boolean[5];
    @Nullable
    private LivingEntity target;

    public GodBlowSkill(SkillUsingMob mob) {
        super("god_blow", 100, 600, mob);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && mob.distanceToSqr(target) <= 5 * 5;
    }

    @Override
    public void prepare() {
        super.prepare();
        this.target = mob.getTarget();
        Arrays.fill(hurt, false);
    }

    @Override
    protected void onFirstTick() {
        mob.getNavigation().stop();
    }

    @Override
    protected void skillTick() {
        dashAtTick(10, 8, 0);
        dashAtTick(30, 8, 1);
        dashAtTick(50, 8, 2);
        dashAtTick(80, 6, 3);
        dashAtTick(90, 10, 4);
    }

    private void dashAtTick(int tick, int damage, int hurtIndex) {
        stop(tick + 5);
        if (tick + 2 <= tickCount && tickCount < tick + 5)
            hurtTarget(damage, hurtIndex);
        if (target == null || tickCount != tick)
            return;

        mob.lookAt(target, 360, 360);
        mob.setDeltaMovement(Vec3.ZERO);
        Vec3 delta = target.position().subtract(mob.position()).normalize().scale(2);
        mob.setDeltaMovement(mob.getDeltaMovement().add(delta));
    }

    private void hurtTarget(int damage, int hurtIndex) {
        if (target == null)
            return;
        if (mob.distanceToSqr(target) <= 3 * 3 && !hurt[hurtIndex]) {
            target.hurt(mob.damageSources().mobAttack(mob), damage);
            hurt[hurtIndex] = true;
        }
    }

    private void stop(int tick) {
        if (tickCount == tick)
            mob.setDeltaMovement(Vec3.ZERO);
    }
}
