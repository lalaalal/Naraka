package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SimpleComboAttackSkill extends ComboSkill<SkillUsingMob> {
    private int attackTick;
    private int attackRange;
    private int moveStartTick;
    private int moveEndTick;
    private boolean lookTarget;
    private boolean moveToTarget;

    public static SimpleComboAttackSkill combo1(Skill<?> combo2, SkillUsingMob mob) {
        return builder(createLocation("final.combo_attack_1"), 40, 100, mob)
                .nextSkill(combo2)
                .attackTick(22).attackRange(3)
                .build();
    }

    public static SimpleComboAttackSkill combo2(Skill<?> combo3, SkillUsingMob mob) {
        return builder(createLocation("final.combo_attack_2"), 40, 0, mob)
                .nextSkill(combo3)
                .attackTick(20).attackRange(4)
                .lookTarget()
                .moveToTarget(12, 19)
                .build();
    }

    public static SimpleComboAttackSkill combo3(SkillUsingMob mob) {
        return builder(createLocation("final.combo_attack_3"), 60, 0, mob)
                .attackTick(20).attackRange(3)
                .build();
    }

    public static Builder builder(ResourceLocation location, int duration, int cooldown, SkillUsingMob mob) {
        return new Builder(location, duration, cooldown, mob);
    }

    public SimpleComboAttackSkill(ResourceLocation location, int duration, int cooldown, @Nullable Skill<?> nextSkill, SkillUsingMob mob) {
        super(location, duration, cooldown, 1, nextSkill, duration, mob);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(attackRange * attackRange);
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(attackTick, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 5));
        if (lookTarget) {
            lookTarget(target);
            runBetween(moveStartTick, moveEndTick, () -> rotateTowardTarget(target));
        }
        if (moveToTarget)
            runBetween(moveStartTick, moveEndTick, () -> moveToTarget(target));
        runAt(moveEndTick, () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    private void moveToTarget(LivingEntity target) {
        if (mob.distanceToSqr(target) < 6) {
            mob.setDeltaMovement(Vec3.ZERO);
            return;
        }
        Vec3 deltaMovement = target.position().subtract(mob.position())
                .normalize()
                .scale(1.5);
        if (mob.distanceToSqr(target) > 3)
            mob.setDeltaMovement(deltaMovement);
    }

    public static class Builder {
        private final ResourceLocation location;
        private final int duration;
        private final int cooldown;
        private final SkillUsingMob mob;
        private int attackTick = 20;
        private int attackRange = 5;
        private boolean lookTarget = false;
        private boolean moveToTarget = false;
        private int moveStartTick = 0;
        private int moveEndTick = 0;
        @Nullable
        private Skill<?> nextSkill;

        public Builder(ResourceLocation location, int duration, int cooldown, SkillUsingMob mob) {
            this.location = location;
            this.duration = duration;
            this.cooldown = cooldown;
            this.mob = mob;
        }

        public Builder attackTick(int attackTick) {
            this.attackTick = attackTick;
            return this;
        }

        public Builder attackRange(int attackRange) {
            this.attackRange = attackRange;
            return this;
        }

        public Builder lookTarget() {
            this.lookTarget = true;
            return this;
        }

        public Builder moveToTarget(int moveStartTick, int moveEndTick) {
            this.moveToTarget = true;
            this.moveStartTick = moveStartTick;
            this.moveEndTick = moveEndTick;
            return this;
        }

        public Builder nextSkill(@Nullable Skill<?> nextSkill) {
            this.nextSkill = nextSkill;
            return this;
        }

        public SimpleComboAttackSkill build() {
            SimpleComboAttackSkill skill = new SimpleComboAttackSkill(location, duration, cooldown, nextSkill, mob);
            skill.attackTick = this.attackTick;
            skill.attackRange = this.attackRange;
            skill.moveToTarget = this.moveToTarget;
            skill.lookTarget = this.lookTarget;
            skill.moveStartTick = this.moveStartTick;
            skill.moveEndTick = this.moveEndTick;

            return skill;
        }
    }
}
