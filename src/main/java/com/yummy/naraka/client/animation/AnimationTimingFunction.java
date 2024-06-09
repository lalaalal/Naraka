package com.yummy.naraka.client.animation;

import com.yummy.naraka.NarakaUtil;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.function.TriFunction;

/**
 * @author lalaalal
 */
@FunctionalInterface
public interface AnimationTimingFunction {
    AnimationTimingFunction EASE_IN_OUT = create(NarakaUtil::easeInOut);
    AnimationTimingFunction EASE_IN = create(NarakaUtil::easeIn);
    AnimationTimingFunction EASE_OUT = create(NarakaUtil::easeOut);
    AnimationTimingFunction LINEAR = create(Mth::lerp);

    static AnimationTimingFunction create(TriFunction<Float, Float, Float, Float> function) {
        return (delta, from, to) -> PartPose.offsetAndRotation(
                function.apply(delta, from.x, to.x),
                function.apply(delta, from.y, to.y),
                function.apply(delta, from.z, to.z),
                NarakaUtil.wrapRadian(function.apply(delta, from.xRot, to.xRot)),
                NarakaUtil.wrapRadian(function.apply(delta, from.yRot, to.yRot)),
                NarakaUtil.wrapRadian(function.apply(delta, from.zRot, to.zRot))
        );
    }

    PartPose transform(float delta, PartPose from, PartPose to);
}
