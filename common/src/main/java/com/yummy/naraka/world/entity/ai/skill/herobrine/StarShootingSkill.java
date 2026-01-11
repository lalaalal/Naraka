package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.world.entity.CorruptedStar;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class StarShootingSkill extends TargetSkill<Herobrine> {
    public static final Identifier IDENTIFIER = skillIdentifier("final_herobrine.star_shooting");

    private final List<CorruptedStar> corruptedStars = new ArrayList<>();

    public StarShootingSkill(Herobrine mob) {
        super(IDENTIFIER, mob, 80, 200);
    }

    @Override
    public void prepare() {
        super.prepare();
        corruptedStars.clear();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetOutOfRange(7 * 7);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);
        runBetween(0, 9, () -> spawnCorruptedStar(level, 10, 15, 8));
        runBetween(0, 9, () -> spawnCorruptedStar(level, 8, 12, 11));
        run(at(55) && !corruptedStars.isEmpty(), () -> shootCorruptedStars(level, target, 3, true));
        run(after(56) && tickCount % 2 == 0 && !corruptedStars.isEmpty(), () -> shootCorruptedStars(level, target, 2, false));
    }

    private void spawnCorruptedStar(ServerLevel level, int count, float radius, float height) {
        int index = tickCount;
        float yRot = (float) Math.toRadians(90 - mob.getYRot());
        float angle = Mth.TWO_PI / count * index + level.getRandom().nextFloat() * Mth.TWO_PI / count;
        float middleAngle = level.getRandom().nextFloat() * Mth.TWO_PI;
        float middleX = Mth.cos(middleAngle) * radius + level.getRandom().nextFloat() - 0.5f;
        float middleY = mob.getEyeHeight() + level.getRandom().nextFloat();
        float middleZ = Mth.sin(middleAngle) * radius + level.getRandom().nextFloat() - 0.5f;

        float targetX = Mth.cos(angle) * radius + level.getRandom().nextFloat();
        float targetY = height + level.getRandom().nextFloat() - 0.5f;
        float targetZ = Mth.sin(angle) * radius + level.getRandom().nextFloat();
        QuadraticBezier bezier = QuadraticBezier.fromZero(
                new Vec3(middleX, middleY, middleZ),
                new Vec3(targetX, targetY, targetZ)
        ).rotated(yRot);
        CorruptedStar corruptedStar = new CorruptedStar(level, mob, mob.getEyePosition(), bezier);
        level.addFreshEntity(corruptedStar);
        corruptedStars.add(corruptedStar);
    }

    private void shootCorruptedStars(ServerLevel level, LivingEntity target, int repeat, boolean followTarget) {
        for (int i = 0; i < repeat && !corruptedStars.isEmpty(); i++) {
            int randomIndex = mob.getRandom().nextInt(corruptedStars.size());
            CorruptedStar corruptedStar = corruptedStars.remove(randomIndex);
            if (followTarget)
                shootCorruptedStarToTarget(target, corruptedStar);
            else
                shootCorruptedStar(level, corruptedStar);
        }
    }

    private void shootCorruptedStar(ServerLevel level, CorruptedStar corruptedStar) {
        double x = corruptedStar.getX() + corruptedStar.getRandom().nextFloat() * 16 - 8;
        double z = corruptedStar.getZ() + corruptedStar.getRandom().nextFloat() * 16 - 8;
        double y = NarakaUtils.findFloor(level, corruptedStar.blockPosition()).getY();

        Vec3 targetPosition = new Vec3(x, y, z);
        Vec3 direction = targetPosition.subtract(corruptedStar.position())
                .normalize();
        corruptedStar.shoot(direction.x, direction.y, direction.z, 1.5f, 0);
    }

    private void shootCorruptedStarToTarget(LivingEntity target, CorruptedStar corruptedStar) {
        Vec3 direction = target.position().subtract(corruptedStar.position())
                .normalize();
        corruptedStar.shoot(direction.x, direction.y, direction.z, 1.2f, 0);
    }
}
