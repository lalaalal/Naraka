package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SimpleComboAttackSkill extends ComboSkill<AbstractHerobrine> {
    public static final ResourceLocation FINAL_COMBO_ATTACK_1 = createLocation("final.combo_attack_1");
    public static final ResourceLocation FINAL_COMBO_ATTACK_2 = createLocation("final.combo_attack_2");
    public static final ResourceLocation FINAL_COMBO_ATTACK_3 = createLocation("final.combo_attack_3");

    protected int attackTick;
    protected int attackRange;
    protected int moveStartTick;
    protected int moveEndTick;
    protected boolean lookTarget;
    protected boolean moveToTarget;
    protected double moveSpeed;

    public static SimpleComboAttackSkill combo1(Skill<?> nextSkill, AbstractHerobrine mob) {
        return builder(FINAL_COMBO_ATTACK_1, 40, 100, mob)
                .nextSkill(nextSkill)
                .moveToTarget(15, 20)
                .moveSpeed(0.7)
                .attackTick(22).attackRange(3)
                .build();
    }

    public static SimpleComboAttackSkill combo2(Skill<?> nextSkill, AbstractHerobrine mob) {
        return builder(FINAL_COMBO_ATTACK_2, 40, 0, mob)
                .nextSkill(nextSkill)
                .attackTick(22).attackRange(2)
                .lookTarget()
                .moveToTarget(15, 25)
                .moveSpeed(1.3)
                .build();
    }

    public static SimpleComboAttackSkill combo3(AbstractHerobrine mob) {
        return builder(FINAL_COMBO_ATTACK_3, 60, 0, mob)
                .attackTick(22).attackRange(3)
                .lookTarget()
                .moveToTarget(15, 20)
                .moveSpeed(1.3)
                .build();
    }

    public static Builder builder(ResourceLocation location, int duration, int cooldown, AbstractHerobrine mob) {
        return new Builder(location, duration, cooldown, mob);
    }

    public SimpleComboAttackSkill(ResourceLocation location, int duration, int cooldown, @Nullable Skill<?> nextSkill, AbstractHerobrine mob) {
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
        runAt(attackTick, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, attackRange));
        if (lookTarget) {
            lookTarget(target);
            runBetween(moveStartTick, moveEndTick, () -> rotateTowardTarget(target));
        }
        if (moveToTarget)
            runBetween(moveStartTick, moveEndTick, () -> moveToTarget(target));
        runAt(moveEndTick, () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }

    private void moveToTarget(LivingEntity target) {
        if (mob.distanceToSqr(target) < 6) {
            mob.setDeltaMovement(Vec3.ZERO);
            return;
        }
        Vec3 deltaMovement = target.position().subtract(mob.position())
                .normalize()
                .scale(moveSpeed)
                .add(0, -0.5, 0);
        if (mob.distanceToSqr(target) > 3)
            mob.setDeltaMovement(deltaMovement);
    }

    public static class Builder {
        private final ResourceLocation location;
        private final int duration;
        private final int cooldown;
        private final AbstractHerobrine mob;
        private int attackTick = 20;
        private int attackRange = 5;
        private boolean lookTarget = false;
        private boolean moveToTarget = false;
        private int moveStartTick = 0;
        private int moveEndTick = 0;
        private double moveSpeed = 1;
        @Nullable
        private Skill<?> nextSkill;

        public Builder(ResourceLocation location, int duration, int cooldown, AbstractHerobrine mob) {
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

        public Builder moveSpeed(double moveSpeed) {
            this.moveSpeed = moveSpeed;
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
            skill.moveSpeed = this.moveSpeed;

            return skill;
        }
    }
}
