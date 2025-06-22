package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.Stardust;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CarpetBombingSkill extends TargetSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final.carpet_bombing");

    private final Map<Stardust, Integer> stardusts = new HashMap<>();
    private int onGroundTick = 0;

    public CarpetBombingSkill(Herobrine mob) {
        super(LOCATION, 90, 400, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        stardusts.clear();
        onGroundTick = 0;
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        for (int i = 0; i < 16; i++) {
            int spawnTick = mob.getRandom().nextIntBetweenInclusive(1, 10);
            float yRot = 360f / 16 * i;
            float xRot = mob.getRandom().nextFloat() * 30 + 15;
            Vec3 shootingVector = mob.calculateViewVector(-xRot, yRot);
            int power = mob.getRandom().nextIntBetweenInclusive(3, 5);
            int waitingTick = mob.getRandom().nextIntBetweenInclusive(2, 3) * 10;
            Stardust stardust = new Stardust(level, mob, shootingVector, power, waitingTick, false);
            stardusts.put(stardust, spawnTick);
        }
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(10, () -> mob.setDeltaMovement(0, 1.2, 0));
        runBetween(11, 25, () -> reduceSpeed(0.8f));
        runFrom(0, () -> spawnStardust(level, 0));

        runBetween(10, 50, () -> rotateTowardTarget(target));
        runAfter(10, () -> lookTarget(target));
        runBetween(50, 70, () -> NarakaSkillUtils.moveDown(target, mob, 0.3, -2.1));
        runAt(70, () -> mob.setDeltaMovement(0, 0.4, 0));
        runBetween(71, 90, () -> reduceSpeed(0.4f));
        runAt(90, () -> mob.setDeltaMovement(Vec3.ZERO));

        onGround(level);
    }

    private void onGround(ServerLevel level) {
        if (onGroundTick == 1) {
            level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
        }
        if ((mob.onGround() || onGroundTick > 0) && onGroundTick < 3) {
            level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 10, 0.5, 1, 0.5, 0.3);
            Supplier<Vec3> movementSupplier = () -> new Vec3(0, mob.getRandom().nextFloat() * 0.5 + 0.5, 0);
            NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 4 + onGroundTick, movementSupplier);
            onGroundTick += 1;
        }
    }

    private void spawnStardust(ServerLevel level, int startTick) {
        int spawnTick = tickCount - startTick;
        stardusts.entrySet().stream()
                .filter(entry -> entry.getValue() == spawnTick)
                .forEach(entry -> {
                    level.addFreshEntity(entry.getKey());
                });
    }
}
