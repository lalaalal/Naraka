package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DashAroundSkill<T extends SkillUsingMob & AfterimageEntity> extends TargetSkill<T> {
    public static final String NAME = "dash_around";
    private boolean secondUse;
    private Vec3 deltaMovement = Vec3.ZERO;

    public DashAroundSkill(T mob) {
        super(NAME, 7, 50, mob);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(25);
    }

    @Override
    public void prepare() {
        super.prepare();
        if (secondUse) {
            secondUse = false;
            setLinkedSkill(null);
        } else if (mob.getRandom().nextBoolean()) {
            setLinkedSkill(this);
            secondUse = true;
        }
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(0, () -> this.updateDeltaMovement(target));
        runBetween(0, 4, () -> move(level));
        runAt(4, () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    private void move(ServerLevel level) {
        NarakaEntityUtils.updatePositionForUpStep(level, mob, deltaMovement, 0.4);
        mob.setDeltaMovement(deltaMovement);
        if (tickCount % 2 == 0)
            mob.addAfterimage(Afterimage.of(mob, 10), 1, tickCount < 5);
    }

    private void updateDeltaMovement(@Nullable LivingEntity target) {
        Vec3 targetPosition = mob.position().add(mob.getViewVector(0));
        if (target != null)
            targetPosition = target.position();

        float rotation = 150 * (mob.getRandom().nextInt(2) * 2 - 1);
        if (mob.getRandom().nextBoolean())
            rotation += 60 * (mob.getRandom().nextInt(2) * 2 - 1);

        this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob.position(), targetPosition)
                .multiply(0.8, 0, 0.8)
                .yRot(rotation);
    }

    public void preventSecondUse() {
        this.secondUse = true;
        setLinkedSkill(null);
    }
}
