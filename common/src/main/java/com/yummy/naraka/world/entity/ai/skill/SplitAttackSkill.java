package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class SplitAttackSkill extends SpawnInstantShadowSkill {
    public static final ResourceLocation LOCATION = createLocation("final.split_attack");

    public SplitAttackSkill(Skill<?> nextSkill, Herobrine mob) {
        super(LOCATION, 50, 200, 1, nextSkill, 50, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(36);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        runBetween(15, 20, () -> moveToTarget(target, 1));
        runBetween(15, 20, () -> rotateTowardTarget(target));
        runAt(22, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 2));
        runAt(20, this::stopMoving);
        runAt(25, () -> spawnShadowHerobrine(level));
        runAt(26, () -> shadowUseSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_1));

        runAt(35, () -> spawnShadowHerobrine(level));
        runAt(36, () -> shadowUseSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_2));
    }

    @Override
    protected boolean hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (super.hurtHitEntity(level, target)) {
            mob.stigmatizeEntity(level, target);
            level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
            return true;
        }
        return false;
    }
}
