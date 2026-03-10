package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.NarakaSword;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SwordMotion {
    private final Set<SwordMotionChannel<?>> channels;

    public static Builder builder() {
        return new Builder();
    }

    public SwordMotion(Set<SwordMotionChannel<?>> channels) {
        this.channels = Set.copyOf(channels);
    }

    public void tick(NarakaSword entity) {
        for (SwordMotionChannel<?> channel : channels)
            channel.tick(entity);
    }

    public static class Builder {
        private final Set<SwordMotionChannel.Builder<?>> builders = new HashSet<>();

        public Builder channel(SwordMotionChannel.Builder<?> channel) {
            this.builders.add(channel);
            return this;
        }

        public SwordMotion build() {
            return new SwordMotion(
                    builders.stream()
                            .map(SwordMotionChannel.Builder::build)
                            .collect(Collectors.toUnmodifiableSet())
            );
        }
    }
}
