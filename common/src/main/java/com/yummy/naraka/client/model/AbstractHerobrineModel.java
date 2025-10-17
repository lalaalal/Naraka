package com.yummy.naraka.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.animation.herobrine.HerobrineAnimation;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class AbstractHerobrineModel<T extends AbstractHerobrine> extends SkillUsingModModel<T> {
    protected final boolean forShadow;
    private int alpha = 0xff;

    protected AbstractHerobrineModel(ModelPart root, String entityName, boolean forShadow) {
        super(root, entityName);
        this.forShadow = forShadow;
    }

    public abstract ModelPart head();

    public abstract ModelPart main();

    public abstract ModelPart upperBody();

    public abstract ModelPart rightArm();

    public abstract ModelPart rightHand();

    public abstract ModelPart leftArm();

    public abstract ModelPart leftHand();

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        resetPose();
        applyHeadRotation(netHeadYaw, headPitch);
        if (!entity.isFinalModel())
            animateWalk(HerobrineAnimation.WALKING, limbSwing, limbSwingAmount, 5, 6);
        playAnimations(entity, ageInTicks);
        alpha = entity.getAlpha();
    }

    public void renderForEye(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float alpha) {
        root().render(poseStack, buffer, packedLight, packedOverlay, FastColor.ARGB32.colorFromFloat(alpha, 1, 1, 1));
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (forShadow) {
            color = NarakaConfig.CLIENT.shadowHerobrineColor.getValue().withAlpha(alpha).pack();
            packedOverlay = OverlayTexture.NO_OVERLAY;
        }
        root().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public void applyHeadRotation(float headYaw, float headPitch) {
        float yRot = Mth.clamp(headYaw, -45, 45);
        float xRot = Mth.clamp(headPitch, -80, 45);

        this.head().yRot = yRot * (Mth.PI / 180f);
        this.head().xRot = xRot * (Mth.PI / 180f);
    }
}
