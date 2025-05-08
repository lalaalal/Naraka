package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class LandingSkill extends ComboSkill<AbstractHerobrine> {
    public static final String NAME = "landing";

    public LandingSkill(AbstractHerobrine mob) {
        super(createLocation(NAME), 57, 0, 0, null, 57, mob);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(30, this::land, level);
    }

    private void land(ServerLevel level) {
        hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 3);
        level.playSound(mob, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        super.hurtHitEntity(level, target);
        mob.stigmatizeEntity(level, target);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }
}
