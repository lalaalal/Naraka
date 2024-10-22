package com.yummy.naraka.world.entity.ai.skill;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SkillManager {
    private final ArrayList<Skill> skills = new ArrayList<>();
    private @Nullable Skill currentSkill = null;

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    private List<Skill> getUsableSkills() {
        return skills.stream()
                .filter(skill -> skill.readyToUse() && skill.canUse())
                .toList();
    }

    public void tick() {
        if (currentSkill != null) {
            currentSkill.tick();
            if (currentSkill.isEnded()) {
                currentSkill.setCooldown();
                currentSkill = null;
            }
        } else {
            List<Skill> usable = getUsableSkills();
            if (!usable.isEmpty()) {
                currentSkill = usable.getFirst();
                currentSkill.prepare();
            }
        }

        for (Skill skill : skills) {
            if (!skill.readyToUse())
                skill.reduceCooldown();
        }
    }
}
