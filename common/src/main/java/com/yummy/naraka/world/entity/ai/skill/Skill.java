package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class Skill<T extends SkillUsingMob> {
    public final String name;
    protected final T mob;
    protected final int duration;
    protected final int cooldown;

    protected int tickCount = 0;
    protected int cooldownTick;
    protected boolean disabled = false;

    @Nullable
    protected Skill<?> linkedSkill;

    public Skill(String name, int duration, int cooldown, T mob) {
        this.name = name;
        this.mob = mob;
        this.duration = duration;
        this.cooldown = cooldown;
        this.cooldownTick = cooldown;
    }

    protected Level level() {
        return mob.level();
    }

    public abstract boolean canUse();

    public void disable() {
        disabled = true;
    }

    public void enable() {
        disabled = false;
    }

    public boolean readyToUse() {
        return !disabled && cooldownTick >= cooldown;
    }

    public void prepare() {
        tickCount = 0;
    }

    public boolean isStarted() {
        return tickCount > 0;
    }

    public boolean isEnded() {
        return tickCount >= duration;
    }

    public boolean hasLinkedSkill() {
        return linkedSkill != null;
    }

    public void setLinkedSkill(@Nullable Skill<?> skill) {
        this.linkedSkill = skill;
    }

    @Nullable
    public Skill<?> getLinkedSkill() {
        return linkedSkill;
    }

    public final void tick() {
        if (tickCount == 0)
            onFirstTick();
        if (tickCount < duration)
            skillTick();
        if (tickCount == duration - 1)
            onLastTick();
        tickCount += 1;
    }

    public void setCooldown() {
        cooldownTick = 0;
    }

    public void reduceCooldown() {
        cooldownTick += 1;
    }

    protected void onFirstTick() {

    }

    protected void onLastTick() {

    }

    protected abstract void skillTick();
}
