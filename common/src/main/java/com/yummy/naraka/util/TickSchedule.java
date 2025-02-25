package com.yummy.naraka.util;

import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TickSchedule {
    private static final List<TickSchedule> SCHEDULES = new ArrayList<>();

    public static TickSchedule executeAt(long gameTime, Runnable runnable) {
        TickSchedule schedule = new TickSchedule(gameTime, runnable);
        SCHEDULES.add(schedule);
        return schedule;
    }

    public static TickSchedule executeAfter(long gameTime, int tick, Runnable runnable) {
        return executeAt(gameTime + tick, runnable);
    }

    public static void cancel(TickSchedule schedule) {
        SCHEDULES.remove(schedule);
    }

    public static void tick(Level level) {
        List<TickSchedule> executed = new ArrayList<>();
        for (TickSchedule tickSchedule : SCHEDULES) {
            if (tickSchedule.canRun(level.getGameTime())) {
                tickSchedule.execute();
                executed.add(tickSchedule);
            }
        }
        SCHEDULES.removeAll(executed);
    }

    private final Runnable runnable;
    private long timeToRun;

    public TickSchedule(long gameTimeToRun, Runnable runnable) {
        this.timeToRun = gameTimeToRun;
        this.runnable = runnable;
    }

    public long getTimeToRun() {
        return timeToRun;
    }

    public boolean canRun(long gameTime) {
        return timeToRun <= gameTime;
    }

    public void execute() {
        runnable.run();
    }

    public void update(long timeToRun) {
        this.timeToRun = timeToRun;
    }

    public boolean isExpired(long gameTime) {
        return timeToRun <= gameTime;
    }
}
