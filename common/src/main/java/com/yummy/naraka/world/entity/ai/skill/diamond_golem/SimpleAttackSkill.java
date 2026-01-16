package com.yummy.naraka.world.entity.ai.skill.diamond_golem;

import com.yummy.naraka.sounds.NarakaSoundEvents;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SimpleAttackSkill extends AttackSkill<SkillUsingMob> {
    public static final Identifier BASIC = skillIdentifier("diamond_golem.basic_attack");
    public static final Identifier SWIPE = skillIdentifier("diamond_golem.swipe_attack");
    public static final Identifier STRONG = skillIdentifier("diamond_golem.strong_attack");

    public static SimpleAttackSkill basic(SkillUsingMob mob) {
        return new SimpleAttackSkill(BASIC, mob, 45, 80, 20, 7, 36, NarakaSoundEvents.DIAMOND_GOLEM_BASIC.value());
    }

    public static SimpleAttackSkill swipe(SkillUsingMob mob) {
        return new SimpleAttackSkill(SWIPE, mob, 25, 80, 5, 5, 25, NarakaSoundEvents.DIAMOND_GOLEM_SWIPE.value());
    }

    public static SimpleAttackSkill strong(SkillUsingMob mob) {
        return new SimpleAttackSkill(STRONG, mob, 85, 80, 40, 9, 64, NarakaSoundEvents.DIAMOND_GOLEM_STRONG.value());
    }

    private final int attackTick;
    private final int attackRange;
    private final int usdDistance;
    private final SoundEvent sound;

    protected SimpleAttackSkill(Identifier location, SkillUsingMob mob, int duration, int cooldown, int attackTick, int attackRange, int usdDistance, SoundEvent sound) {
        super(location, mob, duration, cooldown);
        this.attackTick = attackTick;
        this.attackRange = attackRange;
        this.usdDistance = usdDistance;
        this.sound = sound;
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(usdDistance);
    }

    private boolean canHurtTarget(LivingEntity target) {
        return target.getType() != NarakaEntityTypes.DIAMOND_GOLEM.get();
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(attackTick, () -> hurtEntities(level, this::canHurtTarget, attackRange));
        runAt(0, () -> level.playSound(null, mob, sound, SoundSource.HOSTILE, 1, 1));
        runAt(attackTick, () -> level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY() + 0.1, mob.getZ(), 15, 1, 0.1, 1, 0.3));
        runAt(attackTick, () -> level.playSound(null, mob, NarakaSoundEvents.DIAMOND_GOLEM_ATTACK.value(), SoundSource.HOSTILE, 1, 1));
    }
}
