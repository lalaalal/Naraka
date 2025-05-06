package com.yummy.naraka.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class NarakaInterpolations {
    private static final float A2 = 0.41421f;
    private static final float A3 = 0.30277f;

    public static final AnimationChannel.Interpolation FAST_STEP_IN = (vector3f, delta, keyframes, start, end, scale) -> {
        Vector3f vectorStart = keyframes[start].target();
        Vector3f vectorEnd = keyframes[end].target();
        return interpolate(delta, vectorStart, vectorEnd, NarakaInterpolations::fastStepIn, vector3f);
    };

    public static final AnimationChannel.Interpolation FAST_STEP_OUT = (vector3f, delta, keyframes, start, end, scale) -> {
        Vector3f vectorStart = keyframes[start].target();
        Vector3f vectorEnd = keyframes[end].target();
        return interpolate(delta, vectorStart, vectorEnd, NarakaInterpolations::fastStepOut, vector3f);
    };

    private static float fastStepIn(float x) {
        final float a = A3;
        return ((-1 / (3 * x + a)) + a) / 3 + 1;
    }

    private static float fastStepOut(float x) {
        final float a = A3;
        return ((-1 / (3 * (x - 1) - a)) - a) / 3;
    }

    public static float interpolate(float delta, float start, float end, Function<Float, Float> function) {
        float newDelta = function.apply(delta);
        return Mth.lerp(Math.clamp(newDelta, 0, 1), start, end);
    }

    public static Vector3f interpolate(float delta, Vector3f start, Vector3f end, Function<Float, Float> function, Vector3f dest) {
        return dest.set(
                interpolate(delta, start.x, end.x, function),
                interpolate(delta, start.y, end.y, function),
                interpolate(delta, start.z, end.z, function)
        );
    }
}
