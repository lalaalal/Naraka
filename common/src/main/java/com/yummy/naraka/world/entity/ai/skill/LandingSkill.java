package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LandingSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "landing";

    public LandingSkill(AbstractHerobrine mob) {
        super(createLocation(NAME), 50, 0, 0, null, 50, mob);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(15, () -> this.land(level));
        run(between(15, 20) && tickCount % 2 == 0, () -> {
            level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 10, 0.5, 1, 0.5, 0.3);
            BlockPos floor = NarakaUtils.findFloor(level, mob.blockPosition());
            NarakaUtils.circle(floor, (tickCount - 9) / 2, NarakaUtils.OUTLINE, blockPos -> {
                BlockState state = level.getBlockState(blockPos);
                FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, blockPos, state);
                fallingBlockEntity.setDeltaMovement(0, 0.3, 0);
            });
        });
    }

    private void land(ServerLevel level) {
        hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 4);
        level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        if (NarakaEntityUtils.disableAndHurtShield(target, 60, 15))
            return;
        super.hurtHitEntity(level, target);
        mob.stigmatizeEntity(level, target);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }
}
