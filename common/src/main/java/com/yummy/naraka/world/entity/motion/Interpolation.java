package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

import java.util.function.Function;

public interface Interpolation<T> {
    Interpolation<Vec3> V_LINEAR = Mth::lerp;
    Interpolation<Vec3> V_FAST_STEP_IN = fastStepIn(NarakaUtils::fastStepIn, V_LINEAR);
    Interpolation<Vec3> V_FAST_STEP_OUT = fastStepOut(NarakaUtils::fastStepOut, V_LINEAR);

    Interpolation<Quaternionfc> Q_LINEAR = (delta, from, to) -> from.slerp(to, delta, new Quaternionf());
    Interpolation<Quaternionfc> Q_FAST_STEP_IN = fastStepIn(NarakaUtils::fastStepIn, Q_LINEAR);
    Interpolation<Quaternionfc> Q_FAST_STEP_OUT = fastStepOut(NarakaUtils::fastStepOut, Q_LINEAR);

    static <T> Interpolation<T> fastStepIn(Function<Float, Float> deltaModifier, Interpolation<T> linearInterpolation) {
        return (delta, from, to) -> linearInterpolation.interpolate(deltaModifier.apply(delta), from, to);
    }

    static <T> Interpolation<T> fastStepOut(Function<Float, Float> deltaModifier, Interpolation<T> linearInterpolation) {
        return (delta, from, to) -> linearInterpolation.interpolate(deltaModifier.apply(delta), from, to);
    }

    static Interpolation<Float> bezier(float middle, Function<Float, Float> deltaModifier) {
        return (delta, from, to) -> NarakaUtils.quadraticBezier(deltaModifier.apply(delta), from, middle, to);
    }

    static Interpolation<Float> bezier(float middle) {
        return bezier(middle, Function.identity());
    }

    T interpolate(float delta, T from, T to);
}
