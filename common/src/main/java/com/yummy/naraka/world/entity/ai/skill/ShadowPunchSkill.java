package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.server.level.ServerLevel;

public class ShadowPunchSkill extends PunchSkill<ShadowHerobrine> {
    public ShadowPunchSkill(ComboSkill<?> comboSkill, ShadowHerobrine mob) {
        super(comboSkill, mob, false);
    }

    @Override
    public boolean readyToUse() {
        if (isHerobrineHibernated())
            return !disabled;
        return super.readyToUse();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return super.canUse(level) && !otherShadowJustUsedSkill(level);
    }

    private boolean isHerobrineHibernated() {
        return mob.getHerobrine()
                .map(Herobrine::isHibernateMode)
                .orElse(false);
    }

    private boolean otherShadowJustUsedSkill(ServerLevel level) {
        return mob.getHerobrine()
                .map(herobrine -> herobrine.getShadowController().someoneJustUsedSkill(level))
                .orElse(false);
    }
}
