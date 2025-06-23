package com.yummy.naraka.client.animation;

import com.yummy.naraka.util.NarakaUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationChannel;
import org.joml.Vector3f;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class NarakaInterpolations {

    public static final AnimationChannel.Interpolation FAST_STEP_IN = (vector3f, delta, keyframes, start, end, scale) -> {
        Vector3f vectorStart = keyframes[start].target();
        Vector3f vectorEnd = keyframes[end].target();
        return interpolate(delta, vectorStart, vectorEnd, NarakaUtils::fastStepIn, vector3f);
    };

    public static final AnimationChannel.Interpolation FAST_STEP_OUT = (vector3f, delta, keyframes, start, end, scale) -> {
        Vector3f vectorStart = keyframes[start].target();
        Vector3f vectorEnd = keyframes[end].target();
        return interpolate(delta, vectorStart, vectorEnd, NarakaUtils::fastStepOut, vector3f);
    };

    public static final AnimationChannel.Interpolation EASE_IN_OUT = (vector3f, delta, keyframes, start, end, scale) -> {
        Vector3f vectorStart = keyframes[start].target();
        Vector3f vectorEnd = keyframes[end].target();
        return NarakaUtils.easeInOut(delta, 0.5f, vectorStart, vectorEnd, vector3f);
    };

    public static Vector3f interpolate(float delta, Vector3f start, Vector3f end, Function<Float, Float> function, Vector3f dest) {
        return dest.set(
                NarakaUtils.interpolate(delta, start.x, end.x, function),
                NarakaUtils.interpolate(delta, start.y, end.y, function),
                NarakaUtils.interpolate(delta, start.z, end.z, function)
        );
    }
}
