package com.yummy.naraka.util;

import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class JobManager<T> {
    private final Map<Identifier, Job<T>> jobs = new HashMap<>();

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

    public void register(Identifier id, Job<T> job) {
        jobs.put(id, job);
    }

    public void tick(T instance) {
        Set<Identifier> removal = new HashSet<>();
        jobs.forEach((id, job) -> {
            job.tick(instance);
            if (job.finished())
                removal.add(id);
        });
        removal.forEach(jobs::remove);
    }

    public interface Job<T> {
        void tick(T instance);

        boolean finished();
    }
}
