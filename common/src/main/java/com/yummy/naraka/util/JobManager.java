package com.yummy.naraka.util;

import java.util.ArrayList;
import java.util.List;

public class JobManager<T> {
    private final List<Job<T>> jobs = new ArrayList<>();

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
