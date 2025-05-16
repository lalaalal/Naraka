package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SuperHitSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "super_hit";
    private int onGroundTick = 0;

    public SuperHitSkill(ComboSkill<AbstractHerobrine> comboSkill, AbstractHerobrine mob) {
        super(createLocation(NAME), 40, 0, 1, comboSkill, 40, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        onGroundTick = 0;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(5, this::superHit);
        runAfter(5, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 1.5));
        runAfter(5, () -> this.stopOnGround(level));
    }

    private void superHit() {
        mob.setNoGravity(false);
        Vec3 lookVector = mob.getLookAngle().scale(0.5f);
        mob.setDeltaMovement(lookVector.x, -1.2, lookVector.z);
    }

    private void stopOnGround(ServerLevel level) {
        if (onGroundTick > 1) {
            duration = 0;
            level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
            mob.setDeltaMovement(Vec3.ZERO);
        }
        onGroundTick += 1;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(5, () -> rotateTowardTarget(target));
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 60, 15))
            return;
        super.hurtHitEntity(level, target);
        StunHelper.stunEntity(target, 20, true);
        mob.stigmatizeEntity(level, target);
        level.playSound(mob, mob.blockPosition(), SoundEvents.STONE_BREAK, SoundSource.HOSTILE, 1, 1);
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
