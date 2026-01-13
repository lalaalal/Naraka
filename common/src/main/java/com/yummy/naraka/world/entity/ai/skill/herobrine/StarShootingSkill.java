package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.world.entity.CorruptedStar;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class StarShootingSkill extends TargetSkill<Herobrine> {
    public static final Identifier IDENTIFIER = skillIdentifier("final_herobrine.star_shooting");

    private final List<CorruptedStar> corruptedStars = new ArrayList<>();
    private final List<CorruptedStar> followingStars = new ArrayList<>();

    public StarShootingSkill(Herobrine mob) {
        super(IDENTIFIER, mob, 80, 600);
    }

    @Override
    public void prepare() {
        super.prepare();
        corruptedStars.clear();
        followingStars.clear();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetOutOfRange(7 * 7);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);
        runBetween(0, 9, () -> spawnCorruptedStar(level, target, 10, 15, 11, false));
        runBetween(0, 6, () -> spawnCorruptedStar(level, target, 5, 12, 14, false));
        runBetween(6, 9, () -> spawnCorruptedStar(level, target, 3, 12, 14, true));
        runBetween(30, 55, () -> followTargetPosition(target));
        runAt(55, this::stopFollowingTarget);
        run(at(55) && !followingStars.isEmpty(), () -> shootCorruptedStars(followingStars, 0.75f, 3));
        run(after(56) && tickCount % 2 == 0 && !corruptedStars.isEmpty(), () -> shootCorruptedStars(corruptedStars, 0.35f, 2));
    }

    private void spawnCorruptedStar(ServerLevel level, LivingEntity target, int count, float radius, float height, boolean followTarget) {
        int index = tickCount;
        float yRot = (float) Math.toRadians(90 - mob.getYRot());
        float angle = Mth.TWO_PI / count * index + level.getRandom().nextFloat() * Mth.TWO_PI / count;
        float middleAngle = level.getRandom().nextFloat() * Mth.TWO_PI;
        float middleX = Mth.cos(middleAngle) * radius * 0.75f + level.getRandom().nextFloat() - 0.5f;
        float middleY = mob.getEyeHeight() + level.getRandom().nextFloat();
        float middleZ = Mth.sin(middleAngle) * radius * 0.75f + level.getRandom().nextFloat() - 0.5f;

        float targetX = Mth.cos(angle) * radius + level.getRandom().nextFloat();
        float targetY = height + level.getRandom().nextFloat() - 0.5f;
        float targetZ = Mth.sin(angle) * radius + level.getRandom().nextFloat();
        QuadraticBezier bezier = QuadraticBezier.fromZero(
                new Vec3(middleX, middleY, middleZ),
                new Vec3(targetX, targetY, targetZ)
        ).rotated(yRot);
        CorruptedStar corruptedStar = new CorruptedStar(level, mob, mob.getEyePosition(), bezier);
        if (followTarget) {
            followingStars.add(corruptedStar);
            corruptedStar.setFollowingTarget(target);
        } else {
            corruptedStars.add(corruptedStar);
            determineTargetPositions(level, corruptedStar, corruptedStar.position().add(bezier.v3()));
        }

        level.addFreshEntity(corruptedStar);
        level.playSound(null, mob, SoundEvents.IRON_GOLEM_DEATH, SoundSource.HOSTILE, 0.4f, 1.9f);
        level.playSound(null, mob, SoundEvents.IRON_GOLEM_DEATH, SoundSource.HOSTILE, 0.5f, 2);
    }

    private void followTargetPosition(LivingEntity target) {
        for (CorruptedStar corruptedStar : followingStars)
            corruptedStar.setTargetPosition(target.position());
    }

    private void determineTargetPositions(ServerLevel level, CorruptedStar corruptedStar, Vec3 position) {
        double x = position.x() + corruptedStar.getRandom().nextFloat() * 16 - 8;
        double z = position.z() + corruptedStar.getRandom().nextFloat() * 16 - 8;
        double y = NarakaUtils.findFloor(level, BlockPos.containing(x, mob.getY(), z)).getY() + 1;

        Vec3 targetPosition = new Vec3(x, y, z);
        corruptedStar.setTargetPosition(targetPosition);
    }

    private void stopFollowingTarget() {
        for (CorruptedStar followingStar : followingStars) {
            followingStar.removeFollowingTarget();
        }
    }

    private void shootCorruptedStars(List<CorruptedStar> stars, float velocity, int repeat) {
        for (int i = 0; i < repeat && !stars.isEmpty(); i++) {
            int randomIndex = mob.getRandom().nextInt(stars.size());
            CorruptedStar corruptedStar = stars.remove(randomIndex);
            shootCorruptedStar(corruptedStar, velocity);
        }
    }

    private void shootCorruptedStar(CorruptedStar corruptedStar, float velocity) {
        Vec3 targetPosition = corruptedStar.getTargetPosition();
        Vec3 direction = targetPosition.subtract(corruptedStar.position())
                .normalize();
        corruptedStar.shoot(direction.x, direction.y, direction.z, velocity, 0);
    }

    @Override
    public void interrupt() {
        for (CorruptedStar corruptedStar : corruptedStars)
            corruptedStar.discard();
        for (CorruptedStar followingStar : followingStars)
            followingStar.discard();
    }
}
