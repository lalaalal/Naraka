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
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class StrikeDownSkill extends SpawnInstantShadowSkill {
    public static final ResourceLocation LOCATION = createLocation("final.strike_down");

    private final Supplier<Vec3> movementSupplier = () -> new Vec3(0, mob.getRandom().nextFloat() * 0.4 + 0.3, 0);
    private int onGroundTick;
    private Vec3 shadowPosition = Vec3.ZERO;

    public StrikeDownSkill(Herobrine mob) {
        super(LOCATION, 60, 0, 0, null, 60, mob);
        this.shieldCooldown = 100;
        this.shieldDamage = 15;
    }

    @Override
    public void prepare() {
        super.prepare();
        onGroundTick = 0;
        shadowPosition = mob.position();
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(15, () -> rotateTowardTarget(target));
        runBefore(15, () -> lookTarget(target));
        runBetween(19, 27, () -> NarakaSkillUtils.moveToTarget(target, mob, 0.5, -2));
        runAt(21, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 5));

        runAt(20, () -> spawnShadowHerobrine(level, shadowPosition));
        runAt(21, () -> shadowUseSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_3));

        runAt(38, () -> mob.setDeltaMovement(0, 0.4, 0));
        runAfter(40, () -> lookTarget(target));
        runBetween(40, 48, () -> reduceSpeed(0.5));
        runAt(50, this::stopMoving);
        onGround(level);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
    }

    private void onGround(ServerLevel level) {
        if (onGroundTick == 1) {
            level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
        }
        if ((mob.onGround() || onGroundTick > 0) && onGroundTick < 5) {
            level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 15, 0.5, 1, 0.5, 0.3);
            NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 3 + onGroundTick, movementSupplier);
            onGroundTick += 1;
        }
    }
}
