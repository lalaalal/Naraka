package com.yummy.naraka.client.animation;

import com.yummy.naraka.NarakaUtil;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.util.Mth;

/**
 * Apply model pose between keyframes by timing function
 * <ul>
 *     <li>{@linkplain AnimationTiming#LINEAR}</li>
 *     <li>{@linkplain AnimationTiming#EASE_IN_OUT}</li>
 *     <li>{@linkplain AnimationTiming#EASE_IN}</li>
 *     <li>{@linkplain AnimationTiming#EASE_OUT}</li>
 * </ul>
 *
 * @author lalaalal
 */
@FunctionalInterface
public interface AnimationTiming {
    AnimationTiming EASE_IN_OUT = create(NarakaUtil::easeInOut);
    AnimationTiming EASE_IN = create(NarakaUtil::easeIn);
    AnimationTiming EASE_OUT = create(NarakaUtil::easeOut);
    AnimationTiming LINEAR = create(Mth::lerp);

    static AnimationTiming create(Function function) {
        return (delta, from, to) -> PartPose.offsetAndRotation(
                function.apply(delta, from.x, to.x),
                function.apply(delta, from.y, to.y),
                function.apply(delta, from.z, to.z),
                NarakaUtil.wrapRadian(function.apply(delta, from.xRot, to.xRot)),
                NarakaUtil.wrapRadian(function.apply(delta, from.yRot, to.yRot)),
                NarakaUtil.wrapRadian(function.apply(delta, from.zRot, to.zRot))
        );
    }

    PartPose apply(float delta, PartPose from, PartPose to);

    interface Function {
        float apply(float delta, float from, float to);
    }
}
