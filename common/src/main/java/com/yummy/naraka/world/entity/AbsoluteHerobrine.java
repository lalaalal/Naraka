package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.absolute_herobrine.SwordSwingSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AbsoluteHerobrine extends SkillUsingMob {
    private final SwordSwingSkill swordSwingSkill = registerSkill(this, SwordSwingSkill::new, HerobrineAnimationIdentifiers.SWORD_ATTACK_SPIN);

    public AbsoluteHerobrine(EntityType<? extends AbsoluteHerobrine> entityType, Level level) {
        super(entityType, level);

        registerAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);
        updateAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);

        skillManager.runOnSkillEnd(this::updateAnimationOnSkillEnd);
    }

    protected void updateAnimationOnSkillEnd(Skill<?> skill) {
        if (!skill.hasLinkedSkill()) {
            setAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);
        }
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);
    }
}
