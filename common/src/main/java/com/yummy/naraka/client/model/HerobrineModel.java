package com.yummy.naraka.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.animation.herobrine.HerobrineAnimation;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class HerobrineModel<T extends AbstractHerobrine> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart main;
    private final ModelPart upperBody;
    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private boolean renderShadow = false;

    public HerobrineModel(ModelPart root) {
        this.root = root;
        this.main = root.getChild("main");
        this.upperBody = main.getChild("upper_body");
        this.head = upperBody.getChild("head");
        this.leftArm = upperBody.getChild("left_arm");
        this.rightArm = upperBody.getChild("right_arm");
    }

    public static LayerDefinition createForHerobrine() {
        return createBodyLayer(true);
    }

    public static LayerDefinition createForShadowArmor() {
        return createBodyLayer(false);
    }

    public static LayerDefinition createBodyLayer(boolean withLayer) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();
        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));
        PartDefinition upper_body = main.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -12.0F, 0.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));
        upper_body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 2.0F));

        PartDefinition right_arm = upper_body.addOrReplaceChild("right_arm",
                Util.make(CubeListBuilder.create().texOffs(32, 36).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                        builder -> {
                            if (withLayer)
                                builder.texOffs(32, 46).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F));
                        }),
                PartPose.offset(-4.0F, -10.0F, 2.0F)
        );
        right_arm.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(16, 36).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 4.0F, 2.0F));

        PartDefinition left_arm = upper_body.addOrReplaceChild("left_arm",
                Util.make(CubeListBuilder.create().texOffs(40, 20).addBox(0.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                        builder -> {
                            if (withLayer)
                                builder.texOffs(0, 42).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F));
                        }),
                PartPose.offset(4.0F, -10.0F, 2.0F)
        );
        left_arm.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(36, 10).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 4.0F, 2.0F));

        PartDefinition legs = main.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -2.0F));
        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 2.0F));
        right_leg.addOrReplaceChild("right_leg_lower", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));
        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 2.0F));
        left_leg.addOrReplaceChild("left_leg_lower", CubeListBuilder.create().texOffs(24, 26).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    public void renderShadow() {
        this.renderShadow = true;
    }

    public ModelPart body() {
        return main;
    }

    public ModelPart upperBody() {
        return upperBody;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (renderShadow) {
            color = NarakaConfig.CLIENT.shadowHerobrineColor.getValue().withAlpha(0x88).pack();
            packedLight = LightTexture.FULL_BRIGHT;
        }
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
        renderShadow = false;
    }

    @Override
    public void setupAnim(T herobrine, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        if (!herobrine.getCurrentAnimation().equals(AnimationLocations.STAGGERING))
            applyHeadRotation(netHeadYaw, headPitch);
        if (herobrine.getCurrentAnimation().equals(AnimationLocations.IDLE))
            AnimationUtils.bobArms(rightArm, leftArm, ageInTicks);
        this.animateWalk(HerobrineAnimation.WALKING, limbSwing, limbSwingAmount, 2, 2);
        herobrine.forEachAnimations((animationLocation, animationState) -> {
            this.animate(animationState, AnimationMapper.get(animationLocation), ageInTicks);
        });
    }

    private void applyHeadRotation(float netHeadYaw, float headPitch) {
        netHeadYaw = Mth.clamp(netHeadYaw, -30, 30);
        headPitch = Mth.clamp(headPitch, -25, 45);

        this.head.yRot = netHeadYaw * (Mth.PI / 180f);
        this.head.xRot = headPitch * (Mth.PI / 180f);
    }
}
