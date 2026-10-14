package com.yummy.naraka.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JobManager<T> {
    private final List<Job<T>> jobs = new ArrayList<>();

    public static <T> Job<T> createSimple(final int waitTick, Consumer<T> action) {
        return new Job<>() {
            private int tickCount = 0;

            @Override
            public void tick(T instance) {
                if (tickCount == waitTick)
                    action.accept(instance);
                tickCount += 1;
            }

            @Override
            public boolean finished() {
                return tickCount > waitTick;
            }
        };
    }

    public void register(Job<T> job) {
        jobs.add(job);
    }

    public void tick(T instance) {
        jobs.removeIf(job -> {
            job.tick(instance);
            return job.finished();
        });
    }

    public interface Job<T> {
        void tick(T instance);

        boolean finished();
    }
}
