package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class StrikeDownSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("strike_down");

    public StrikeDownSkill(Herobrine mob) {
        super(LOCATION, 60, 0, mob);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runAt(19, this::strikeDown);
        runAt(21, () ->  hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 3));
        runAt(38, this::floating);
        runAfter(40, () -> lookTarget(target));
        runBetween(40, 48, () -> reduceSpeed(0.5));
        runAt(50, this::stopMoving);
    }

    private void strikeDown() {
        mob.setDeltaMovement(0, -2.5, 0);
    }

    private void floating() {
        mob.setDeltaMovement(0, 0.4, 0);
    }
}
