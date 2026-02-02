package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.NarakaPortal;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DespawnSkill extends Skill<Herobrine> {
    public static final Identifier LOCATION = skillIdentifier("final_herobrine.despawn");

    private Vec3 originalPosition = Vec3.ZERO;
    private Vec3 targetPosition = Vec3.ZERO;

    public DespawnSkill(Herobrine mob) {
        super(LOCATION, mob, 70, Integer.MAX_VALUE);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    public void prepare() {
        super.prepare();
        originalPosition = mob.position();
        targetPosition = mob.getEyePosition()
                .subtract(mob.getLookAngle().multiply(1, 0, 1));
    }

    @Override
    protected void skillTick(ServerLevel level) {
        runAt(0, () -> {
            NarakaPortal narakaPortal = new NarakaPortal(level, targetPosition);
            narakaPortal.setYRot(mob.getYRot());
            level.addFreshEntity(narakaPortal);
            mob.setNoGravity(true);
        });
        runAfter(0, () -> {
            float delta = Mth.clamp(tickCount / 60f, 0, 1);
            int alpha = (int) Mth.clamp((1 - delta) * 255, 0, 255);
            Vec3 currentPosition = NarakaUtils.interpolateVec3(delta, originalPosition, targetPosition, NarakaUtils::fastStepIn);
            mob.setPos(currentPosition);
            mob.setAlpha(alpha);
        });
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.remove(Entity.RemovalReason.DISCARDED);
    }
}
