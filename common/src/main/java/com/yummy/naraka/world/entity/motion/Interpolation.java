package com.yummy.naraka.world.entity.motion;

import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public interface Interpolation {
    Interpolation LINEAR = Mth::lerp;
    Interpolation FAST_STEP_IN = (delta, from, to) -> NarakaUtils.interpolateVec3(delta, from, to, NarakaUtils::fastStepIn);
    Interpolation FAST_STEP_OUT = (delta, from, to) -> NarakaUtils.interpolateVec3(delta, from, to, NarakaUtils::fastStepOut);

    Vec3 interpolate(float delta, Vec3 from, Vec3 to);
}
