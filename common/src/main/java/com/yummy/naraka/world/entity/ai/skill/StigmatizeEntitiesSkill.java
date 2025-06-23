package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class StigmatizeEntitiesSkill extends Skill<Herobrine> {
    public static final String NAME = "stigmatize_entities";

    public StigmatizeEntitiesSkill(Herobrine mob) {
        super(NAME, Integer.MAX_VALUE, 0, mob);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return true;
    }

    @Override
    public void prepare() {
        super.prepare();
        mob.setNoGravity(true);
        duration = Integer.MAX_VALUE;
    }

    @Override
    protected void skillTick(ServerLevel level) {
        runAt(20, () -> mob.setDeltaMovement(0, 0.3, 0));
        runBefore(40, () -> mob.setDeltaMovement(mob.getDeltaMovement().scale(0.8)));
        runAfter(39, () -> mob.setDeltaMovement(Vec3.ZERO));
        if ((tickCount - 40) % 60 == 0)
            mob.setAnimation(AnimationLocations.STIGMATIZE_ENTITIES);
        if ((tickCount - 57) % 60 == 0)
            NarakaSkillUtils.stigmatize(level, mob, 20);
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.setNoGravity(false);
    }

    @Override
    public void interrupt() {
        mob.setNoGravity(false);
    }
}
