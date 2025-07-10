package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
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
    public static final ResourceLocation LOCATION = createLocation("final.spin_up");

    public SpinUpSkill(Herobrine mob, @Nullable Skill<?> nextSkill) {
        super(LOCATION, mob, 40, 0, 1, 40, nextSkill);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        if (tickCount > 27) {
            lookTarget(target);
            rotateTowardTarget(target);
        }
        runAt(20, () -> spinUp(target));
        runBetween(20, 32, () -> blowBlocks(level, 3, 20));
        runBetween(23, 35, () -> blowBlocks(level, 4, 23));
        runBetween(25, 37, () -> blowBlocks(level, 5, 25));
        runBetween(24, 37, () -> blowBlocks(level, 3, 24));
        runBetween(26, 37, () -> blowBlocks(level, 5, 26));
        runBetween(21, 25, () -> reduceSpeed(0.3));
        runBetween(20, 25, () -> hurtEntities(level, this::checkTarget, 3));
        runAt(27, this::stopMoving);
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
