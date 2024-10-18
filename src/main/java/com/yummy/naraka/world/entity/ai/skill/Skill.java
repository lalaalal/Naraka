package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;

public abstract class Skill {
    public final String name;
    protected final SkillUsingMob mob;
    protected final int duration;
    protected final int cooldown;

    protected int tickCount = 0;
    protected int cooldownTick;

    public Skill(String name, int duration, int cooldown, SkillUsingMob mob) {
        this.name = name;
        this.mob = mob;
        this.duration = duration;
        this.cooldown = cooldown;
        this.cooldownTick = cooldown;
    }

    public abstract boolean canUse();

    public boolean readyToUse() {
        return cooldownTick >= cooldown;
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

    public final void tick() {
        if (tickCount == 0)
            onFirstTick();
        if (tickCount < duration)
            skillTick();
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

    protected abstract void skillTick();
}
