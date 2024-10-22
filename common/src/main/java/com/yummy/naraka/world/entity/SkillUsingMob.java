package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class SkillUsingMob extends PathfinderMob {
    private final SkillManager skillManager = new SkillManager();

    protected SkillUsingMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        registerSkills();
    }

    protected abstract void registerSkills();

    public void registerSkill(Skill skill) {
        skillManager.addSkill(skill);
    }

    @Override
    protected void customServerAiStep() {
        skillManager.tick();
    }
}
