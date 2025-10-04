package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.Stardust;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CarpetBombingSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final_herobrine.carpet_bombing");

    private final Map<Stardust, Integer> stardusts = new HashMap<>();
    private int onGroundTick = 0;

    public CarpetBombingSkill(Herobrine mob) {
        super(LOCATION, mob, 90, 400, 120, 15);
    }

    @Override
    public void prepare() {
        super.prepare();
        stardusts.clear();
        onGroundTick = 0;
    }

    private void addStardust(ServerLevel level, float yRot, int basePower, boolean followTarget) {
        int spawnTick = mob.getRandom().nextIntBetweenInclusive(1, 10);
        float xRot = mob.getRandom().nextFloat() * 30 + 15;
        Vec3 shootingVector = mob.calculateViewVector(-xRot, yRot);
        int power = mob.getRandom().nextIntBetweenInclusive(basePower, basePower + 1);
        int waitingTick = mob.getRandom().nextIntBetweenInclusive(2, 3) * 10 - 5;
        Stardust stardust = new Stardust(level, mob, shootingVector, power, waitingTick, followTarget);
        stardusts.put(stardust, spawnTick);
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        List<Integer> candidates = new ArrayList<>();
        for (int index = 0; index < 16; index++)
            candidates.add(index);
        List<Integer> followingIndex = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int randomIndex = mob.getRandom().nextInt(candidates.size());
            candidates.remove(randomIndex);
            followingIndex.add(randomIndex);
        }

        for (int i = 0; i < 16; i++) {
            float yRot = 360f / 16 * i;
            addStardust(level, yRot + 360f / 32, 4, false);
            addStardust(level, yRot, 6, followingIndex.contains(i));
        }
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.1f;
    }

    private Vec3 modifyMovement(Vec3 original) {
        return original.scale(0.5).add(0, -3, 0);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(10, () -> mob.setDeltaMovement(0, 1.5, 0));
        runBetween(11, 25, () -> reduceSpeed(0.8f));
        runFrom(0, () -> spawnStardust(level));

        runBetween(10, 50, () -> rotateTowardTarget(target));
        runAfter(10, () -> lookTarget(target));
        runBetween(50, 70, () -> moveToTarget(target, true, this::modifyMovement));
        runAt(55, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 3));
        runAt(70, () -> mob.setDeltaMovement(0, 0.4, 0));
        runBetween(71, 90, () -> reduceSpeed(0.4f));
        runAt(90, () -> mob.setDeltaMovement(Vec3.ZERO));

        onGround(level);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
    }

    private void onGround(ServerLevel level) {
        if (onGroundTick == 1) {
            level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
            mob.shakeCamera();
        }
        if ((mob.onGround() || onGroundTick > 0) && onGroundTick < 3) {
            level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 10, 0.5, 1, 0.5, 0.3);
            Supplier<Vec3> movementSupplier = () -> new Vec3(0, mob.getRandom().nextFloat() * 0.5 + 0.5, 0);
            NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 4 + onGroundTick, movementSupplier);
            onGroundTick += 1;
        }
    }

    private void spawnStardust(ServerLevel level) {
        stardusts.entrySet().stream()
                .filter(entry -> entry.getValue() == tickCount)
                .forEach(entry -> {
                    level.addFreshEntity(entry.getKey());
                });
    }
}
