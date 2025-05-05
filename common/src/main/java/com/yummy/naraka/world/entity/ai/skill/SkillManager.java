package com.yummy.naraka.world.entity.ai.skill;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SkillManager {
    private final RandomSource random;
    private final Set<Entry> skills = new HashSet<>();
    private final List<Consumer<Skill<?>>> skillStartListeners = new ArrayList<>();
    private final List<Consumer<Skill<?>>> skillEndListeners = new ArrayList<>();
    private final List<Consumer<Skill<?>>> skillSelectListeners = new ArrayList<>();
    private boolean paused = false;
    private int waitingTick = 0;

    public SkillManager(RandomSource random) {
        this.random = random;
    }

    @Nullable
    private Skill<?> currentSkill = null;

    public void addSkill(int priority, Skill<?> skill) {
        this.skills.add(new Entry(priority, skill));
    }

    public void enableOnly(Collection<Skill<?>> skillsToEnable) {
        for (Entry entry : skills)
            entry.setEnabled(skillsToEnable.contains(entry.skill));
    }

    public void runOnSkillStart(Consumer<Skill<?>> listener) {
        this.skillStartListeners.add(listener);
    }

    public void runOnSkillEnd(Consumer<Skill<?>> listener) {
        this.skillEndListeners.add(listener);
    }

    public void runOnSkillSelect(Consumer<Skill<?>> listener) {
        this.skillSelectListeners.add(listener);
    }

    @Nullable
    private Skill<?> selectSkill() {
        if (paused || waitingTick > 0)
            return null;

        Optional<Entry> minimum = this.skills.stream()
                .filter(Entry::prepared)
                .min(Comparator.comparingInt(Entry::priority));

        if (minimum.isEmpty())
            return null;
        List<Skill<?>> usableSkills = this.skills.stream()
                .filter(Entry::prepared)
                .filter(entry -> entry.priority == minimum.get().priority())
                .map(Entry::skill)
                .collect(Collectors.toUnmodifiableList());
        int randomIndex = random.nextInt(usableSkills.size());
        return usableSkills.get(randomIndex);
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

    public void waitNextSkill(int waitingTick) {
        this.waitingTick = waitingTick;
    }

    public void tick(ServerLevel level) {
        if (currentSkill != null) {
            currentSkill.tick(level);
            if (currentSkill.isEnded()) {
                currentSkill.setCooldown();
                for (Consumer<Skill<?>> listener : skillEndListeners)
                    listener.accept(currentSkill);
                if (currentSkill.hasLinkedSkill()) {
                    Skill<?> previousSkill = currentSkill;
                    setCurrentSkill(currentSkill.getLinkedSkill());
                    previousSkill.setLinkedSkill(null);
                }
                else
                    currentSkill = null;
            }
        } else {
            Skill<?> usable = selectSkill();
            if (usable != null)
                setCurrentSkill(usable);
            for (Consumer<Skill<?>> listener : skillSelectListeners)
                listener.accept(usable);
        }

        for (Entry entry : skills)
            entry.tryReduceCooldown();
        if (waitingTick > 0)
            waitingTick -= 1;
    }

    @Nullable
    public Skill<?> getCurrentSkill() {
        return currentSkill;
    }

    private record Entry(int priority, Skill<?> skill) {
        void tryReduceCooldown() {
            if (!skill.readyToUse())
                skill.reduceCooldown();
        }

        void setEnabled(boolean value) {
            skill.setEnabled(value);
        }

        boolean prepared() {
            return skill.readyToUse() && skill.canUse();
        }
    }
}
