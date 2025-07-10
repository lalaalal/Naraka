package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class SplitAttackSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final.split_attack");
    private InstantShadowSpawner firstShadowSpawner = InstantShadowSpawner.EMPTY;
    private InstantShadowSpawner secondShadowSpawner = InstantShadowSpawner.EMPTY;

    public SplitAttackSkill(Herobrine mob, Skill<?> nextSkill) {
        super(LOCATION, mob, 50, 200, 1, 50, nextSkill);
    }

    @Override
    public void prepare() {
        super.prepare();
        firstShadowSpawner = InstantShadowSpawner.simple(mob);
        secondShadowSpawner = InstantShadowSpawner.simple(mob);
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
        runBetween(15, 20, () -> moveToTarget(target, 1.7));
        runBetween(15, 20, () -> rotateTowardTarget(target));
        runAt(22, () -> hurtEntities(level, this::checkTarget, 1.5));
        runAt(20, this::stopMoving);
        runAt(25, () -> firstShadowSpawner.spawn(level, mob.position(), mob.getYRot()));
        runAt(26, () -> firstShadowSpawner.control(this::displayShadowPickaxe).useSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_1));

        runAt(35, () -> secondShadowSpawner.spawn(level, mob.position(), mob.getYRot()));
        runAt(36, () -> secondShadowSpawner.control(this::displayShadowPickaxe).useSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_2));
    }

    private void displayShadowPickaxe(ShadowHerobrine shadowHerobrine) {
        shadowHerobrine.setDisplayPickaxe(true);
    }

    private boolean checkTarget(LivingEntity target) {
        return targetInLookAngle(target, -Mth.HALF_PI * 0.3f, Mth.HALF_PI * 0.3f) && AbstractHerobrine.isNotHerobrine(target);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }
}
