package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.Motionable;
import net.minecraft.resources.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Motion {
    private final Identifier id;
    private final Set<MotionChannel<?>> channels;

    public static Builder builder() {
        return new Builder();
    }

    public Motion(Identifier id, Set<MotionChannel<?>> channels) {
        this.id = id;
        this.channels = Set.copyOf(channels);
    }

    public Identifier getId() {
        return id;
    }

    public void tick(Motionable motionable) {
        for (MotionChannel<?> channel : channels)
            channel.tick(motionable);
    }

    public static class Builder {
        private final Set<MotionChannel.Builder<?>> builders = new HashSet<>();

        public Builder channel(MotionChannel.Builder<?> channel) {
            this.builders.add(channel);
            return this;
        }

        public Motion build(Identifier id) {
            return new Motion(
                    id,
                    builders.stream()
                            .map(MotionChannel.Builder::build)
                            .collect(Collectors.toUnmodifiableSet())
            );
        }
    }
}
