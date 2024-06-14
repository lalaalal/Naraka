package com.yummy.naraka.client.animation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.yummy.naraka.client.model.AnimatedEntityModel;
import com.yummy.naraka.client.renderer.AnimatedLivingEntityRenderer;
import com.yummy.naraka.entity.Animatable;
import com.yummy.naraka.entity.AnimatableMonster;

import java.util.*;

/**
 * Animation for entity<br>
 * Use {@link Animation#builder(String)} to create an animation
 * Entity should be an {@linkplain Animatable}, also see {@linkplain AnimatedEntityModel} and {@linkplain  AnimatedLivingEntityRenderer}
 *
 * @author lalaalal
 * @see Animation.Builder
 * @see Animatable
 * @see AnimatableMonster
 * @see AnimatedEntityModel
 * @see AnimatedLivingEntityRenderer
 */
public record Animation(String name, int animationLength, Set<String> partNames,
                        Map<String, PartAnimation> partAnimations, boolean holdHeadControl, boolean smoothStart) {
    public static final Animation EMPTY = Animation.builder("empty")
            .smoothStart()
            .build();

    public AnimationInstance instance(boolean repeat) {
        return new AnimationInstance(this, repeat);
    }

    /**
     * <p>Create an {@linkplain Builder}</p>
     * Required to call
     * <ul>
     *     <li>{@link Builder#define(String...)}</li>
     *     <li>{@link Builder#setAllPartsZeroPose(int, AnimationTiming)} with tick 0</li>
     *     <li>{@link Builder#smoothStart()} if no {@linkplain Keyframe} on tick 0</li>
     * </ul>
     *
     * @param name Name of animation
     * @return Builder of animation
     */
    public static Builder builder(String name) {
        return new Builder(name);
    }

    /**
     * @see Animation#builder(String)
     */
    public static class Builder {
        private final String name;
        private final Multimap<String, Keyframe.Builder> keyframeBuilderMap = ArrayListMultimap.create();
        private final Map<String, PartAnimation> partAnimations = new HashMap<>();
        private Set<String> partNames = Set.of();
        private boolean holdHeadControl = false;
        private boolean smoothStart = false;

        private Builder(String name) {
            this.name = name;
        }

        /**
         * Define part names to animating
         * Should be elements of model
         *
         * @param partNames Part names to animate
         * @return Current builder
         */
        public Builder define(String... partNames) {
            this.partNames = Set.copyOf(List.of(partNames));
            return this;
        }

        /**
         * Set all defined parts' pose to 0
         *
         * @param tick            Animation tick
         * @param animationTiming Animation timing function
         * @return Current builder
         */
        public Builder setAllPartsZeroPose(int tick, AnimationTiming animationTiming) {
            for (String partName : partNames)
                keyframe(partName, tick).zero().animationTiming(animationTiming);
            return this;
        }

        public Builder holdHeadControl() {
            this.holdHeadControl = true;
            return this;
        }

        /**
         * Set animation starting with last pose
         *
         * @return Current builder
         */
        public Builder smoothStart() {
            this.smoothStart = true;
            return this;
        }

        /**
         * Find existing {@link Keyframe.Builder} or create one<br>
         * Default animation timing is {@link AnimationTiming#LINEAR}
         *
         * @param partName Part name
         * @param tick     Animation tick
         * @return Existing {@linkplain Keyframe.Builder} or create
         */
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

        protected Keyframe.Builder previousKeyframe(Keyframe.Builder keyframeBuilder) {
            String partName = keyframeBuilder.getPartName();
            int tick = keyframeBuilder.getTick();
            Collection<Keyframe.Builder> builders = keyframeBuilderMap.get(partName);
            return builders.stream()
                    .filter(builder -> builder.getTick() < tick)
                    .min(Comparator.comparingInt(Keyframe.Builder::getTick))
                    .orElseThrow();
        }

        /**
         * Build
         *
         * @return An {@linkplain Animation}
         */
        public Animation build() {
            if (!smoothStart) {
                for (String partName : keyframeBuilderMap.keySet()) {
                    boolean hasKeyframeOnStart = keyframeBuilderMap.get(partName).stream()
                            .anyMatch(builder -> builder.getTick() == 0);
                    if (!hasKeyframeOnStart)
                        throw new IllegalStateException("Should set all part pose at tick 0. Or set smoothStart (%s) not set".formatted(partName));
                }
            }
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
