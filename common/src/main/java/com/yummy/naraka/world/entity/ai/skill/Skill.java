package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

public abstract class Skill<T extends SkillUsingMob> {
    public final ResourceLocation location;
    protected final T mob;
    protected final int duration;
    protected int cooldown;

    protected int tickCount = 0;
    protected int cooldownTick;
    protected boolean disabled = false;

    protected static ResourceLocation createLocation(String name) {
        return NarakaMod.location("skill", name);
    }

    @Nullable
    protected Skill<?> linkedSkill;

    protected Skill(ResourceLocation location, int duration, int cooldown, T mob, @Nullable Skill<?> linkedSkill) {
        this.location = location;
        this.mob = mob;
        this.duration = duration;
        this.cooldown = cooldown;
        this.cooldownTick = cooldown;
        this.linkedSkill = linkedSkill;
    }

    protected Skill(String name, int duration, int cooldown, T mob) {
        this(createLocation(name), duration, cooldown, mob, null);
    }

    protected Skill(ResourceLocation location, int duration, int cooldown, T mob) {
        this(location, duration, cooldown, mob, null);
    }

    public abstract boolean canUse();

    public void setEnabled(boolean value) {
        disabled = !value;
    }

    public void disable() {
        disabled = true;
    }

    public void enable() {
        disabled = false;
    }

    public boolean isEnabled() {
        return !disabled;
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

    public int getCurrentTickCount() {
        return tickCount;
    }

    public int getCooldown() {
        return cooldown;
    }

    @Nullable
    public Skill<?> getLinkedSkill() {
        return linkedSkill;
    }

    public final void tick(ServerLevel level) {
        if (tickCount == 0)
            onFirstTick(level);
        if (tickCount < duration)
            skillTick(level);
        if (tickCount == duration - 1)
            onLastTick(level);
        tickCount += 1;
    }

    public void changeCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setCooldown() {
        cooldownTick = 0;
    }

    public void reduceCooldown() {
        cooldownTick += 1;
    }

    protected void onFirstTick(ServerLevel level) {

    }

    protected void onLastTick(ServerLevel level) {

    }

    protected abstract void skillTick(ServerLevel level);

    public void interrupt() {

    }
}
