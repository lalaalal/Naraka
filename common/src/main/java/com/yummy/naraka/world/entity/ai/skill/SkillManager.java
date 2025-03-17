package com.yummy.naraka.world.entity.ai.skill;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class SkillManager {
    private final RandomSource random;
    private final Set<Skill<?>> skills = new HashSet<>();
    private final List<Consumer<Skill<?>>> skillStartListeners = new ArrayList<>();
    private final List<Consumer<Skill<?>>> skillEndListeners = new ArrayList<>();
    private boolean paused = false;

    public SkillManager(RandomSource random) {
        this.random = random;
    }

    @Nullable
    private Skill<?> currentSkill = null;

    public void addSkill(Skill<?> skill) {
        this.skills.add(skill);
    }

    public void enableOnly(Collection<Skill<?>> skillsToEnable) {
        for (Skill<?> skill : this.skills)
            skill.setEnabled(skillsToEnable.contains(skill));
    }

    public void runOnSkillStart(Consumer<Skill<?>> listener) {
        this.skillStartListeners.add(listener);
    }

    public void runOnSkillEnd(Consumer<Skill<?>> listener) {
        this.skillEndListeners.add(listener);
    }

    private List<Skill<?>> getUsableSkills() {
        if (paused)
            return List.of();
        return skills.stream()
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

    public void pause(boolean interrupt) {
        this.paused = true;
        if (interrupt)
            this.interrupt();
    }

    public void resume() {
        this.paused = false;
    }

    public void interrupt() {
        if (currentSkill != null) {
            for (Consumer<Skill<?>> listener : skillEndListeners)
                listener.accept(currentSkill);
            this.currentSkill.interrupt();
        }
        currentSkill = null;
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

        for (Skill<?> skill : skills) {
            if (!skill.readyToUse())
                skill.reduceCooldown();
        }
    }

    @Nullable
    public Skill<?> getCurrentSkill() {
        return currentSkill;
    }
}
