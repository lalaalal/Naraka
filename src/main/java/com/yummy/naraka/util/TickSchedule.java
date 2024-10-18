package com.yummy.naraka.util;

import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TickSchedule {
    private static final List<TickSchedule> schedule = new ArrayList<>();

    public static void executeOn(Predicate<Long> timePredicate, Runnable runnable) {
        schedule.add(new TickSchedule(timePredicate, runnable));
    }

    public static void executeAt(long gameTime, Runnable runnable) {
        schedule.add(new TickSchedule(gameTime, runnable));
    }

    public static void executeAfter(long gameTime, int tick, Runnable runnable) {
        schedule.add(new TickSchedule(gameTime + tick, runnable));
    }

    public static void tick(Level level) {
        List<TickSchedule> executed = new ArrayList<>();
        for (TickSchedule tickSchedule : schedule) {
            if (tickSchedule.canRun(level.getGameTime())) {
                tickSchedule.execute();
                executed.add(tickSchedule);
            }
        }
        schedule.removeAll(executed);
    }

    private final Predicate<Long> timePredicate;
    private final Runnable runnable;

    public TickSchedule(Predicate<Long> timePredicate, Runnable runnable) {
        this.timePredicate = timePredicate;
        this.runnable = runnable;
    }

    public TickSchedule(long gameTimeToRun, Runnable runnable) {
        this.timePredicate = gameTime -> gameTime >= gameTimeToRun;
        this.runnable = runnable;
    }

    public boolean canRun(long gameTime) {
        return timePredicate.test(gameTime);
    }

    public void execute() {
        runnable.run();
    }
}
