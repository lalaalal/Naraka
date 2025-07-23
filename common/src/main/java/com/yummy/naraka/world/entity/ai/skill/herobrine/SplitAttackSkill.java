package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.ComboSkill;
import com.yummy.naraka.world.entity.ai.skill.InstantShadowSpawner;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class SplitAttackSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final_herobrine.split_attack");
    private InstantShadowSpawner firstShadowSpawner = InstantShadowSpawner.EMPTY;
    private InstantShadowSpawner secondShadowSpawner = InstantShadowSpawner.EMPTY;

    public SplitAttackSkill(Herobrine mob, Skill<?> nextSkill) {
        super(LOCATION, mob, 60, 200, 0.5f, 45, nextSkill);
    }

    @Override
    public void prepare() {
        super.prepare();
        firstShadowSpawner = InstantShadowSpawner.simple(mob);
        secondShadowSpawner = InstantShadowSpawner.simple(mob);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.1f;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        run(at(0) && targetOutOfRange(target, 9), () -> NarakaSkillUtils.sendParticleFront(level, mob, target, NarakaParticleTypes.TELEPORT.get()));
        run(at(3) && targetOutOfRange(target, 9), () -> teleportToTarget(target, 3));
        runBetween(0, 10, () -> rotateTowardTarget(target));
        runBetween(15, 20, () -> moveToTarget(target, true, 1));
        runBetween(15, 20, () -> NarakaSkillUtils.sendTraceParticles(level, mob, NarakaFlameParticleOption.DIAMOND));
        runBetween(15, 20, () -> rotateTowardTarget(target));
        runBetween(18, 20, () -> hurtEntities(level, this::checkTarget, 1.8));
        runAt(20, this::stopMoving);
        runAt(25, () -> firstShadowSpawner.spawnAndUseSkill(level, mob, SimpleComboAttackSkill.FINAL_COMBO_ATTACK_1));
        run(at(35) && hasLinkedSkill(), () -> secondShadowSpawner.spawnAndUseSkill(level, mob, SimpleComboAttackSkill.FINAL_COMBO_ATTACK_2));
        runAt(45, () -> mob.setAnimation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_1_RETURN));
    }

    private boolean checkTarget(LivingEntity target) {
        return targetInLookAngle(target, -Mth.HALF_PI * 0.67f, Mth.HALF_PI * 0.67f) && AbstractHerobrine.isNotHerobrine(target);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }
}
