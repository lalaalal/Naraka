package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SpinUpSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final.spin_up");

    @Nullable
    private ShadowHerobrine shadowHerobrine;
    private int blowTick = 0;

    public SpinUpSkill(@Nullable Skill<?> nextSkill, Herobrine mob) {
        super(LOCATION, 40, 0, 1, nextSkill, 40, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        blowTick = 0;
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);
        runAt(20, this::spinUp);
        runBetween(20, 32, () -> blowBlocks(level));
        runBetween(21, 25, () -> reduceSpeed(0.3));
        runAt(23, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 3));
        runAt(27, this::stopMoving);

        runAt(25, () -> shadowHerobrine = ShadowHerobrine.createInstantFinalShadow(level, mob));
        runAt(26, this::shadowUseSkill);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        super.hurtHitEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }

    private void shadowUseSkill() {
        if (shadowHerobrine != null && shadowHerobrine.isAlive()) {
            shadowHerobrine.useSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_3);
            shadowHerobrine.setDisplayPickaxe(true);
        }
    }

    private void blowBlocks(ServerLevel level) {
        if (blowTick < 12) {
            Vec3 current = mob.getLookAngle()
                    .yRot(-Mth.HALF_PI * 0.67f)
                    .scale(3)
                    .yRot(Mth.HALF_PI * 0.167f * blowTick);

            BlockPos pos = BlockPos.containing(mob.position().add(current));
            BlockPos floor = NarakaUtils.findFloor(level, pos);
            BlockState state = level.getBlockState(floor);
            FallingBlockEntity fallingBlockEntity = NarakaEntityUtils.createFloatingBlock(level, floor.above(), state);
            fallingBlockEntity.setDeltaMovement(current.scale(0.2).add(0, 0.7, 0));
            blowTick += 1;
            level.playSound(mob, mob.blockPosition(), SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), SoundSource.HOSTILE, 1, 1);
        }
    }

    private void spinUp() {
        Vec3 deltaMovement = mob.getLookAngle()
                .multiply(5, 3, 5)
                .add(0, 2, 0);
        mob.setDeltaMovement(deltaMovement);
    }
}
