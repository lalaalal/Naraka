package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.ComboSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SpinUpSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final_herobrine.spin_up");

    public SpinUpSkill(Herobrine mob, @Nullable Skill<?> nextSkill) {
        super(LOCATION, mob, 55, 0, 0.75f, 40, nextSkill);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.1f;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        if (tickCount > 27) {
            lookTarget(target);
            rotateTowardTarget(target);
        }
        runAt(20, () -> spinUp(target));
        runBetween(20, 25, () -> NarakaSkillUtils.sendTraceParticles(level, mob, NarakaParticleTypes.CORRUPTED_SOUL_FIRE_FLAME.get()));
        runBetween(20, 32, () -> blowBlocks(level, 3, 20));
        runBetween(23, 35, () -> blowBlocks(level, 4, 23));
        runBetween(25, 37, () -> blowBlocks(level, 5, 25));
        runBetween(24, 37, () -> blowBlocks(level, 3, 24));
        runBetween(26, 37, () -> blowBlocks(level, 5, 26));
        runBetween(21, 25, () -> reduceSpeed(0.3));
        runBetween(20, 25, () -> hurtEntities(level, this::checkTarget, 3));
        runAt(27, this::stopMoving);

        runAt(40, () -> mob.setAnimation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_2_RETURN));
    }

    private boolean checkTarget(LivingEntity target) {
        return targetInLookAngle(target, -Mth.HALF_PI, Mth.HALF_PI) && AbstractHerobrine.isNotHerobrine(target);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }

    private void blowBlocks(ServerLevel level, double distance, int startTick) {
        int blowTick = tickCount - startTick;
        if (blowTick < 12) {
            Vec3 current = mob.getLookAngle()
                    .yRot(-Mth.HALF_PI * 0.67f)
                    .scale(distance)
                    .yRot(Mth.HALF_PI * 0.167f * blowTick);

            BlockPos pos = BlockPos.containing(mob.position().add(current));
            BlockPos floor = NarakaUtils.findFloor(level, pos);
            BlockState state = level.getBlockState(floor);
            Vec3 movement = current.scale(0.2).add(0, 0.7, 0);
            NarakaEntityUtils.createFloatingBlock(level, floor.above(), state, movement);
            level.playSound(mob, mob.blockPosition(), SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), SoundSource.HOSTILE, 1, 1);
        }
    }

    private void spinUp(LivingEntity target) {
        Vec3 deltaMovement = target.position().subtract(mob.position())
                .scale(0.5)
                .add(0, 2, 0);
        mob.setDeltaMovement(deltaMovement);
    }
}
