package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ShadowPunchSkill extends PunchSkill<ShadowHerobrine> {
    public ShadowPunchSkill(ShadowHerobrine mob) {
        super(mob, 30, false, null);
    }

    @Override
    public boolean readyToUse() {
        if (isHerobrineHibernated())
            return !disabled;
        return super.readyToUse();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return super.canUse(level) && mob.otherShadowNotUsingSkill(level);
    }

    private boolean isHerobrineHibernated() {
        return mob.getHerobrine()
                .map(Herobrine::isHibernateMode)
                .orElse(false);
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.getHerobrine().ifPresent(herobrine -> {
            herobrine.getShadowController().consumeFlickerStack(level, List.of(mob));
        });
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }
}
