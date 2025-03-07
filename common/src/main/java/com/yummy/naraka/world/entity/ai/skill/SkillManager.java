package com.yummy.naraka.world.entity.ai.skill;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class SkillManager {
    private final RandomSource random;
    private final Map<String, Skill<?>> skills = new HashMap<>();
    private final List<Consumer<Skill<?>>> skillStartListeners = new ArrayList<>();
    private final List<Consumer<Skill<?>>> skillEndListeners = new ArrayList<>();

    public SkillManager(RandomSource random) {
        this.random = random;
    }

    @Nullable
    private Skill<?> currentSkill = null;

    public void addSkill(Skill<?> skill) {
        this.skills.put(skill.name, skill);
    }

    public Collection<Skill<?>> getSkills() {
        return skills.values();
    }

    public void runOnSkillStart(Consumer<Skill<?>> listener) {
        this.skillStartListeners.add(listener);
    }

    public void runOnSkillEnd(Consumer<Skill<?>> listener) {
        this.skillEndListeners.add(listener);
    }

    private List<Skill<?>> getUsableSkills() {
        return skills.values().stream()
                .filter(skill -> skill.readyToUse() && skill.canUse())
                .toList();
    }

    public void setCurrentSkillIfAbsence(Skill<?> skill) {
        if (currentSkill == null)
            setCurrentSkill(skill);
    }

    public void setCurrentSkill(@Nullable Skill<?> skill) {
        if (skill == null)
            return;
        currentSkill = skill;
        currentSkill.prepare();
        for (Consumer<Skill<?>> listener : skillStartListeners)
            listener.accept(currentSkill);
    }

    public void tick() {
        if (currentSkill != null) {
            currentSkill.tick();
            if (currentSkill.isEnded()) {
                for (Consumer<Skill<?>> listener : skillEndListeners)
                    listener.accept(currentSkill);
                currentSkill.setCooldown();
                if (currentSkill.hasLinkedSkill())
                    setCurrentSkill(currentSkill.getLinkedSkill());
                else
                    currentSkill = null;
            }
        } else {
            List<Skill<?>> usable = getUsableSkills();
            if (!usable.isEmpty()) {
                Skill<?> skill = usable.get(random.nextInt(usable.size()));
                setCurrentSkillIfAbsence(skill);
            }
        }

        for (Skill<?> skill : skills.values()) {
            if (!skill.readyToUse())
                skill.reduceCooldown();
        }
    }

    @Nullable
    public Skill<?> getCurrentSkill() {
        return currentSkill;
    }
}
