package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.world.entity.CorruptedStar;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class StarShootingSkill extends TargetSkill<Herobrine> {
    public static final Identifier IDENTIFIER = skillIdentifier("final_herobrine.star_shooting");
    private final List<QuadraticBezier> BEZIERS = List.of(
            QuadraticBezier.fromZero(
                    new Vec3(6, 3, -6),
                    new Vec3(0, 6, 6)
            ),
            QuadraticBezier.fromZero(
                    new Vec3(6, 3, -6),
                    new Vec3(0, 8, 3)
            ),
            QuadraticBezier.fromZero(
                    new Vec3(6, 3, -6),
                    new Vec3(0, 9, 0)
            ),
            QuadraticBezier.fromZero(
                    new Vec3(6, 3, -6),
                    new Vec3(0, 8, -3)
            ),
            QuadraticBezier.fromZero(
                    new Vec3(6, 3, -6),
                    new Vec3(0, 6, -6)
            )
    );

    private final List<CorruptedStar> corruptedStars = new ArrayList<>();

    public StarShootingSkill(Herobrine mob) {
        super(IDENTIFIER, mob, 50, 200);
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
        run(tickCount % 3 == 0 && tickCount < BEZIERS.size() * 3, () -> spawnCorruptedStar(level));
        run(tickCount % 3 == 0 && 27 <= tickCount && tickCount < 27 + corruptedStars.size() * 3, () -> shootCorruptedStar(target));
    }

    private void spawnCorruptedStar(ServerLevel level) {
        int index = tickCount / 3;
        QuadraticBezier bezier = BEZIERS.get(index);
        float yRot = (float) Math.toRadians(90 - mob.getYRot());
        CorruptedStar corruptedStar = new CorruptedStar(level, mob, mob.getEyePosition(), bezier.rotated(yRot), 20);
        level.addFreshEntity(corruptedStar);
        corruptedStars.add(corruptedStar);
    }

    private void shootCorruptedStar(LivingEntity target) {
        int index = (tickCount - 27) / 3;
        CorruptedStar corruptedStar = corruptedStars.get(index);
        Vec3 delta = target.position().subtract(corruptedStar.position());
        Vec3 direction = delta.normalize();
        corruptedStar.shoot(direction.x, direction.y, direction.z, 1.5f, 0);
    }
}
