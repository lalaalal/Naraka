package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class StrikeDownSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final.strike_down");

    private int onGroundTick;

    public StrikeDownSkill(Herobrine mob) {
        super(LOCATION, 60, 0, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        onGroundTick = 0;
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(19, this::strikeDown);
        runAt(21, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 6));
        runAt(38, this::floating);
        runAfter(40, () -> lookTarget(target));
        runBetween(40, 48, () -> reduceSpeed(0.5));
        runAt(50, this::stopMoving);
        onGround(level);
    }

    private void onGround(ServerLevel level) {
        if (onGroundTick == 1) {
            level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
        }
        if ((mob.onGround() || onGroundTick > 0) && onGroundTick < 3) {
            level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 10, 0.5, 1, 0.5, 0.3);
            NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 4 + onGroundTick);
            onGroundTick += 1;
        }
    }

    private void strikeDown() {
        mob.setDeltaMovement(0, -2.5, 0);
    }

    private void floating() {
        mob.setDeltaMovement(0, 0.4, 0);
    }
}
