package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class UppercutSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "uppercut";

    public UppercutSkill(@Nullable ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob) {
        super(createLocation(NAME), 60, 0, 0.1f, comboSkill, 15, mob);
    }

    @Override
    protected float getBonusChance() {
        if (targetInRange(9))
            return 0.6f;
        return 0;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(10, this::jump);
        runAt(12, this::floating);
    }

    private void jump() {
        mob.addDeltaMovement(new Vec3(0, 0.5, 0));
    }

    private void floating() {
        if (hasLinkedSkill()) {
            mob.setNoGravity(true);
            mob.setDeltaMovement(0, 0, 0);
        }
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(10, this::hurtHitEntity, level, target);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (!targetInRange(target, 6))
            return;
        super.hurtHitEntity(level, target);
        target.addDeltaMovement(new Vec3(0, 0.4, 0));
        mob.stigmatizeEntity(target);
        level.playSound(mob, mob.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }

    @Override
    public void interrupt() {
        mob.setNoGravity(false);
    }
}
