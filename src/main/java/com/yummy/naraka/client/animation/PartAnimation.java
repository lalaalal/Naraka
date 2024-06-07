package com.yummy.naraka.client.animation;

import java.util.Comparator;
import java.util.List;

/**
 * @author lalaalal
 */
public class PartAnimation {
    private final String name;
    private final List<Keyframe> keyframes;

    public PartAnimation(String name, List<Keyframe> keyframes) {
        this.name = name;
        this.keyframes = keyframes;
        this.keyframes.sort(Comparator.comparingInt(Keyframe::tick));
    }

    public String getName() {
        return name;
    }

    public PartAnimationInstance createInstance(Animation animation, boolean repeat) {
        return new PartAnimationInstance(animation, this, repeat);
    }

    public Keyframe getFirstKeyframe() {
        return keyframes.getFirst();
    }

    public Keyframe getLastKeyframe() {
        return keyframes.getLast();
    }

    public boolean hasNextKeyframe(Keyframe keyframe) {
        return keyframes.contains(keyframe) &&
                keyframes.indexOf(keyframe) < keyframes.size() - 1;
    }

    public Keyframe getNextKeyframe(Keyframe keyframe) {
        int index = keyframes.indexOf(keyframe);
        if (index == -1 || index == keyframes.size() - 1)
            throw new IllegalArgumentException();
        return keyframes.get(index + 1);
    }

    public Keyframe getPreviousKeyframe(Keyframe keyframe) {
        int index = keyframes.indexOf(keyframe);
        if (index - 1 < 0)
            throw new IllegalArgumentException();
        return keyframes.get(index - 1);
    }

    public boolean isFirstKeyframe(Keyframe keyframe) {
        return keyframes.indexOf(keyframe) == 0;
    }
}
