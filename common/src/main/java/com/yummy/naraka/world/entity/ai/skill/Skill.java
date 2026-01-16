package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class Skill<T extends SkillUsingMob> {
    public final Identifier identifier;
    protected final T mob;
    protected int duration;
    protected final int defaultCooldown;
    protected int cooldown;

    protected int tickCount = 0;
    protected int cooldownTick;
    protected boolean disabled = false;

    protected static Identifier skillIdentifier(String name) {
        return NarakaMod.identifier("skill", name);
    }

    @Nullable
    protected Skill<?> linkedSkill;

    protected Skill(Identifier identifier, T mob, int duration, int cooldown, @Nullable Skill<?> linkedSkill) {
        this.identifier = identifier;
        this.mob = mob;
        this.duration = duration;
        this.defaultCooldown = cooldown;
        this.cooldown = cooldown;
        this.cooldownTick = cooldown;
        this.linkedSkill = linkedSkill;
    }

    protected Skill(Identifier identifier, T mob, int duration, int cooldown) {
        this(identifier, mob, duration, cooldown, null);
    }

    public abstract boolean canUse(ServerLevel level);

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

    public void restoreCooldown() {
        this.cooldown = defaultCooldown;
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

    protected boolean at(int tick) {
        return tickCount == tick;
    }

    protected boolean after(int tick) {
        return tickCount > tick;
    }

    protected boolean before(int tick) {
        return tickCount < tick;
    }

    protected boolean between(int from, int to) {
        return from <= tickCount && tickCount < to;
    }

    protected final void run(Predicate<Integer> predicate, Runnable action) {
        if (predicate.test(tickCount))
            action.run();
    }

    protected final void run(boolean predicate, Runnable action) {
        if (predicate)
            action.run();
    }

    protected final void runAt(int tick, Runnable action) {
        run(at(tick), action);
    }

    protected final void runAfter(int tick, Runnable action) {
        run(after(tick), action);
    }

    protected final void runFrom(int tick, Runnable action) {
        run(at(tick) || after(tick), action);
    }

    protected final void runBefore(int tick, Runnable action) {
        run(before(tick), action);
    }

    protected final void runBetween(int from, int to, Runnable action) {
        run(between(from, to), action);
    }

    protected final void reduceSpeed(double scale) {
        mob.setDeltaMovement(mob.getDeltaMovement().scale(scale));
    }

    protected final void stopMoving() {
        mob.setDeltaMovement(Vec3.ZERO);
    }

    public void interrupt() {

    }
}
