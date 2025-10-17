package com.yummy.naraka.world.entity.ai.skill.naraka_pickaxe;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.NarakaPickaxe;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ExplodeSkill extends AttackSkill<NarakaPickaxe> {
    public static final ResourceLocation LOCATION = createLocation("naraka_pickaxe.explode");

    private final Supplier<Vec3> floatingBlockMovement = () -> new Vec3(0, mob.getRandom().nextDouble() * 0.2 + 0.1, 0);

    public ExplodeSkill(NarakaPickaxe mob) {
        super(LOCATION, mob, 300, 600);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return target.getMaxHealth() * 0.05f;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    private Vec3 applyMovement(Vec3 original) {
        double scale = (40 - tickCount) * 0.01f;
        return original.add(0, 8, 0).scale(scale);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBetween(0, 40, () -> moveToTarget(target, false, this::applyMovement));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        if (target == null) {
            runAt(0, () -> mob.setDeltaMovement(0, 2, 0));
            runBetween(0, 40, () -> reduceSpeed(0.9));
        }

        runBetween(20, 80, () -> sendCircleParticlesOnFloor(level));
        runAt(80, () -> mob.setDeltaMovement(0, -8, 0));
        runAt(82, () -> level.playSound(null, mob, SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1));
        runAt(83, () -> NarakaSkillUtils.shockwaveBlocks(level, mob.players(), mob.blockPosition(), 3, floatingBlockMovement));
        runAt(84, () -> NarakaSkillUtils.shockwaveBlocks(level, mob.players(), mob.blockPosition(), 4, floatingBlockMovement));
        runAfter(90, () -> sendCircleParticles(level, 25, mob.getY() + 0.1, 0.05));
        run(after(90) && tickCount % 80 == 0, () -> hurtAndSendParticles(level));
    }

    private void sendCircleParticlesOnFloor(ServerLevel level) {
        double y = NarakaUtils.findFloor(level, mob.blockPosition()).above().getY() + 0.1;
        sendCircleParticles(level, 10, y, 0.05);
    }

    private void sendCircleParticles(ServerLevel level, int count, double y, double ySpeed) {
        for (int i = 0; i < count; i++) {
            double distance = mob.getRandom().nextDouble() * 8;
            double yRot = mob.getRandom().nextDouble() * Math.TAU;
            double x = Math.cos(yRot) * distance + mob.getX();
            double z = Math.sin(yRot) * distance + mob.getZ();
            double ySpeedMultiplier = mob.getRandom().nextDouble() * 0.5 + 0.5;

            level.sendParticles(NarakaFlameParticleOption.EMERALD, x, y, z, 0, 0, ySpeed * ySpeedMultiplier, 0, 1);
        }
    }

    private void hurtAndSendParticles(ServerLevel level) {
        level.playSound(null, mob, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1, 1);
        hurtEntities(level, AbstractHerobrine::isNotHerobrine, 8);
        sendCircleParticles(level, 150, mob.getY() + 0.1, 0.5);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        Stigma stigma = StigmaHelper.get(target);
        if (stigma.value() > 0) {
            StigmaHelper.decreaseStigma(target);
            target.hurt(NarakaDamageSources.stigmaConsume(mob), stigma.value() * calculateDamage(target));
            level.playSound(null, mob, SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE, 1, 1);
        }
    }
}
