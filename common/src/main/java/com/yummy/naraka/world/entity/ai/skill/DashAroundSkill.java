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
        super(NAME, 17, 100, mob);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && mob.distanceToSqr(target) > 3 * 3;
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

        if (tickCount == 10) {
            updateDeltaMovement();
        }
        if (10 <= tickCount && tickCount < 15) {
            mob.setDeltaMovement(deltaMovement);
            mob.addAfterimage(Afterimage.of(mob, 15));
        }
        if (tickCount == 15)
            mob.setDeltaMovement(Vec3.ZERO);
    }

    private void updateDeltaMovement() {
        LivingEntity target = mob.getTarget();
        if (target == null)
            return;

        int rotationDegrees = 60 * (mob.getRandom().nextInt(2) * 2 - 1);
        Vector3f vector = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                .toVector3f()
                .mul(Axis.YP.rotationDegrees(rotationDegrees).get(new Matrix3f()));
        this.deltaMovement = new Vec3(vector);
    }
}
