package com.yummy.naraka.client.animation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

/**
 * @author lalaalal
 */
public record Animation(String name, int animationLength, Set<String> partNames,
                        Map<String, PartAnimation> partAnimations, boolean holdHeadControl, boolean smoothStart) {
    public static final Animation EMPTY = Animation.builder("empty")
            .smoothStart()
            .build();

    public AnimationInstance instance(boolean repeat) {
        return new AnimationInstance(this, repeat);
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {
        private final String name;
        private final Multimap<String, Keyframe.Builder> keyframeBuilderMap = ArrayListMultimap.create();
        private final Map<String, PartAnimation> partAnimations = new HashMap<>();
        private Set<String> partNames = Set.of();
        private boolean holdHeadControl = false;
        private boolean smoothStart = false;

        public Builder(String name) {
            this.name = name;
        }

        public Builder define(String... partNames) {
            this.partNames = Set.copyOf(List.of(partNames));
            return this;
        }

        public Builder startWithZeroPose() {
            for (String partName : partNames)
                keyframe(partName, 0).zero();
            return this;
        }

        public Builder holdHeadControl() {
            this.holdHeadControl = true;
            return this;
        }

        public Builder smoothStart() {
            this.smoothStart = true;
            return this;
        }

        public Keyframe.Builder keyframe(String partName, int tick) {
            if (!partNames.contains(partName))
                throw new IllegalArgumentException("Part name \"%s\" is not supported for current animation (%s)".formatted(partName, name));
            Collection<Keyframe.Builder> builders = keyframeBuilderMap.get(partName);
            return builders.stream()
                    .filter(builder -> builder.getTick() == tick)
                    .findFirst()
                    .orElseGet(() -> {
                        Keyframe.Builder keyframeBuilder = new Keyframe.Builder(this, partName, tick);
                        keyframeBuilderMap.put(partName, keyframeBuilder);
                        return keyframeBuilder;
                    });
        }

        public Keyframe.Builder previousKeyframe(Keyframe.Builder keyframeBuilder) {
            String partName = keyframeBuilder.getPartName();
            int tick = keyframeBuilder.getTick();
            Collection<Keyframe.Builder> builders = keyframeBuilderMap.get(partName);
            return builders.stream()
                    .filter(builder -> builder.getTick() < tick)
                    .min(Comparator.comparingInt(Keyframe.Builder::getTick))
                    .orElseThrow();
        }

        public Animation build() {
            // TODO : check all parts have pose on tick 0
            for (String partName : keyframeBuilderMap.keySet()) {
                Collection<Keyframe.Builder> builders = keyframeBuilderMap.get(partName);
                ArrayList<Keyframe> keyframes = new ArrayList<>(builders.size());
                builders.forEach(builder -> keyframes.add(builder.build()));
                partAnimations.put(partName, new PartAnimation(partName, keyframes));
            }
            int length = 0;
            for (PartAnimation partAnimation : partAnimations.values()) {
                int tick = partAnimation.getLastKeyframe().tick();
                length = Math.max(length, tick);
            }
            return new Animation(name, length, partNames, partAnimations, holdHeadControl, smoothStart);
        }
    }
}
