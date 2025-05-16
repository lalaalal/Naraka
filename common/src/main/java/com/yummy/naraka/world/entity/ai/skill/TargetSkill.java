package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class TargetSkill<T extends SkillUsingMob> extends Skill<T> {
    protected TargetSkill(ResourceLocation location, int duration, int cooldown, T mob, @Nullable Skill<?> linkedSkill) {
        super(location, duration, cooldown, mob, linkedSkill);
    }

    protected TargetSkill(String name, int duration, int cooldown, T mob) {
        super(name, duration, cooldown, mob);
    }

    protected TargetSkill(ResourceLocation location, int duration, int cooldown, T mob) {
        super(location, duration, cooldown, mob);
    }

    @Override
    protected final void skillTick(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        tickAlways(level, target);
        if (target != null)
            tickWithTarget(level, target);
    }

    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {

    }

    /**
     * Called only when target entity is not null.
     */
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {

    }

    protected void rotateTowardTarget(LivingEntity target) {
        mob.lookAt(target, 180, 0);
    }

    protected void lookTarget(LivingEntity target) {
        mob.getLookControl().setLookAt(target);
    }

    protected static Predicate<Integer> at(int tick) {
        return tickCount -> tickCount == tick;
    }

    protected static Predicate<Integer> after(int tick) {
        return tickCount -> tickCount > tick;
    }

    protected static Predicate<Integer> before(int tick) {
        return tickCount -> tickCount < tick;
    }

    protected static Predicate<Integer> between(int from, int to) {
        return tickCount -> from <= tickCount && tickCount < to;
    }

    protected static Predicate<Integer> and(Predicate<Integer> p1, Predicate<Integer> p2) {
        return tickCount -> p1.test(tickCount) && p2.test(tickCount);
    }

    protected static Predicate<Integer> or(Predicate<Integer> p1, Predicate<Integer> p2) {
        return tickCount -> p1.test(tickCount) || p2.test(tickCount);
    }

    protected final void run(Predicate<Integer> predicate, Runnable action) {
        if (predicate.test(tickCount))
            action.run();
    }

    protected final void runAt(int tick, Runnable action) {
        run(at(tick), action);
    }

    protected final void runAfter(int tick, Runnable action) {
        run(after(tick), action);
    }

    protected final void runBefore(int tick, Runnable action) {
        run(before(tick), action);
    }

    protected final void runBetween(int from, int to, Runnable action) {
        run(between(from, to), action);
    }
}
