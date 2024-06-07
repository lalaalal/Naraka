package com.yummy.naraka.client.animation;

import com.yummy.naraka.NarakaUtil;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * @param xRot
 * @param yRot
 * @param zRot
 * @author lalaalal
 */
public record ModelPartPose(float xRot, float yRot, float zRot) {
    public static final ModelPartPose ZERO = new ModelPartPose(0, 0, 0);

    public static ModelPartPose lerp(float delta, ModelPartPose from, ModelPartPose to) {
        float xRot = Mth.rotLerp(delta, from.xRot, to.xRot);
        float yRot = Mth.rotLerp(delta, from.yRot, to.yRot);
        float zRot = Mth.rotLerp(delta, from.zRot, to.zRot);
        return new ModelPartPose(xRot, yRot, zRot);
    }

    public static ModelPartPose easeInOut(float delta, ModelPartPose from, ModelPartPose to) {
        float xRot = NarakaUtil.ease(delta, from.xRot, to.xRot);
        float yRot = NarakaUtil.ease(delta, from.yRot, to.yRot);
        float zRot = NarakaUtil.ease(delta, from.zRot, to.zRot);
        return new ModelPartPose(xRot, yRot, zRot);
    }

    public static ModelPartPose easeIn(float delta, ModelPartPose from, ModelPartPose to) {
        if (delta < 0.5f)
            return easeInOut(delta, from, to);
        return lerp(delta, from, to);
    }

    public static ModelPartPose easeOut(float delta, ModelPartPose from, ModelPartPose to) {
        if (delta < 0.5f)
            return lerp(delta, from, to);
        return easeInOut(delta, from, to);
    }

    public static ModelPartPose from(ModelPart modelPart) {
        return new ModelPartPose(
                modelPart.xRot, modelPart.yRot, modelPart.zRot
        );
    }

    public void applyTo(ModelPart part) {
        part.xRot = this.xRot;
        part.yRot = this.yRot;
        part.zRot = this.zRot;
    }
}
