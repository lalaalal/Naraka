package com.yummy.naraka.world.entity.ai.skill;

import com.mojang.math.Axis;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class DashAroundSkill<T extends SkillUsingMob & AfterimageEntity> extends Skill<T> {
    public static final String NAME = "dash_around";
    private boolean secondUse;
    private Vec3 deltaMovement = Vec3.ZERO;

    public DashAroundSkill(T mob) {
        super(NAME, 7, 50, mob);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null;
    }

    @Override
    protected void onFirstTick() {
        LivingEntity target = mob.getTarget();
        if (target != null)
            mob.lookAt(target, 360, 0);
        if (secondUse) {
            secondUse = false;
            setLinkedSkill(null);
        } else if (mob.getRandom().nextBoolean()) {
            setLinkedSkill(this);
            secondUse = true;
        }
    }

    @Override
    protected void skillTick() {
        mob.getNavigation().stop();

        if (tickCount == 0)
            updateDeltaMovement();
        if (0 <= tickCount && tickCount <= 3) {
            NarakaEntityUtils.updatePositionForUpStep(level(), mob, deltaMovement, 0.4);
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

        int rotationDegrees = 60 * (mob.getRandom().nextInt(2) * 2 - 1);
        if (target == null)
            rotationDegrees *= 4;
        Vector3f vector = NarakaEntityUtils.getDirectionNormalVector(mob.position(), targetPosition)
                .multiply(0.8, 0, 0.8)
                .toVector3f()
                .mul(Axis.YP.rotationDegrees(rotationDegrees).get(new Matrix3f()));
        this.deltaMovement = new Vec3(vector);
    }

    public void preventSecondUse() {
        this.secondUse = true;
        setLinkedSkill(null);
    }
}
