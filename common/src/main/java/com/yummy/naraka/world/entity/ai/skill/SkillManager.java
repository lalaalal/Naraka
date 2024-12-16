package com.yummy.naraka.world.entity.ai.skill;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillManager {
    private final Map<String, Skill> skills = new HashMap<>();
    private @Nullable Skill currentSkill = null;

    public void addSkill(Skill skill) {
        this.skills.put(skill.name, skill);
    }

    private List<Skill> getUsableSkills() {
        return skills.values().stream()
                .filter(skill -> skill.readyToUse() && skill.canUse())
                .toList();
    }

    public void setCurrentSkill(String name) {
        if (currentSkill == null) {
            currentSkill = skills.get(name);
            currentSkill.prepare();
        }
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

        for (Skill skill : skills.values()) {
            if (!skill.readyToUse())
                skill.reduceCooldown();
        }
    }

    @Nullable
    public Skill getCurrentSkill() {
        return currentSkill;
    }
}
