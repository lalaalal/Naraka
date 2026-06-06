package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.world.entity.Motionable;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class Motion {
    private final ResourceLocation id;
    private final Map<Class<?>, MotionChannel<?>> channels;

    public static Builder builder() {
        return new Builder();
    }

    private Motion(ResourceLocation id, Map<Class<?>, MotionChannel<?>> channels) {
        this.id = id;
        this.channels = Map.copyOf(channels);
    }

    public ResourceLocation getId() {
        return id;
    }

    public void tick(Motionable motionable) {
        for (MotionChannel<?> channel : channels.values())
            channel.tick(motionable);
    }

    @SuppressWarnings("unchecked")
    public <T> void setModifier(Class<T> dataType, UnaryOperator<MotionKeyframe<T>> keyframeModifier) {
        MotionChannel<T> channel = (MotionChannel<T>) channels.get(dataType);
        if (channel != null)
            channel.setModifier(keyframeModifier);
    }

    public static class Builder {
        private final Map<Class<?>, MotionChannel.Builder<?>> builders = new HashMap<>();

        public Builder channel(MotionChannel.Builder<?> channel) {
            this.builders.put(channel.getDataType(), channel);
            return this;
        }

        public Motion build(ResourceLocation id) {
            Map<Class<?>, MotionChannel<?>> channels = new HashMap<>();
            builders.forEach((dataType, builder) -> channels.put(dataType, builder.build()));
            return new Motion(id, channels);
        }
    }
}
