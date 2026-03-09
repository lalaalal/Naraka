package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.NarakaSword;

import java.util.HashMap;
import java.util.Map;

public class SwordMotion {
    private final Map<MotionApplier, SwordMotionChannel> channels;

    public static Builder builder() {
        return new Builder();
    }

    public SwordMotion(Map<MotionApplier, SwordMotionChannel> channels) {
        this.channels = Map.copyOf(channels);
    }

    public void tick(NarakaSword entity) {
        for (SwordMotionChannel channel : channels.values()) {
            channel.tick(entity);
        }
    }

    public static class Builder {
        private final Map<MotionApplier, SwordMotionChannel.Builder> builders = new HashMap<>();

        public Builder channel(SwordMotionChannel.Builder channel) {
            this.builders.put(channel.getMotionApplier(), channel);
            return this;
        }

        public SwordMotion build() {
            Map<MotionApplier, SwordMotionChannel> channels = new HashMap<>();
            this.builders.forEach((applier, builder) -> {
                channels.put(applier, builder.build());
            });
            return new SwordMotion(channels);
        }
    }
}
