package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DashAroundSkill<T extends SkillUsingMob & AfterimageEntity> extends Skill<T> {
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
    protected void onFirstTick(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        if (target != null)
            mob.lookAt(target, 360, 0);
    }

    @Override
    protected void skillTick(ServerLevel level) {
        mob.getNavigation().stop();

        if (tickCount == 0)
            updateDeltaMovement();
        if (0 <= tickCount && tickCount <= 3) {
            NarakaEntityUtils.updatePositionForUpStep(level, mob, deltaMovement, 0.4);
            mob.setDeltaMovement(deltaMovement);
            mob.addAfterimage(Afterimage.of(mob, 10), 2, tickCount < 3);
        }
        if (tickCount == 3)
            mob.setDeltaMovement(Vec3.ZERO);
    }

    private void updateDeltaMovement() {
        LivingEntity target = mob.getTarget();
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
