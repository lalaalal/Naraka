package com.yummy.naraka.client.model;

import com.yummy.naraka.client.animation.herobrine.HerobrineAnimation;
import com.yummy.naraka.client.animation.herobrine.HerobrinePunchAnimation;
import com.yummy.naraka.client.animation.herobrine.HerobrineSkillAnimation;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class HerobrineModel<T extends Herobrine> extends HierarchicalModel<T> {
    private final ModelPart root;

    public HerobrineModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));
        body.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(6.0F, -1.0F, 0.0F));
        left_arm.addOrReplaceChild("left_arm_upper", CubeListBuilder.create().texOffs(40, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        left_arm.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(36, 10).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 2.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-6.0F, -1.0F, 0.0F));
        right_arm.addOrReplaceChild("right_arm_upper", CubeListBuilder.create().texOffs(32, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        right_arm.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(16, 36).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 2.0F));

        PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 11.0F, 0.0F));
        left_leg.addOrReplaceChild("left_leg_upper", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        left_leg.addOrReplaceChild("left_leg_lower", CubeListBuilder.create().texOffs(24, 26).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 11.0F, 0.0F));
        right_leg.addOrReplaceChild("right_leg_upper", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        right_leg.addOrReplaceChild("right_leg_lower", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.animateWalk(HerobrineAnimation.WALKING, limbSwing, limbSwingAmount, 2, 2.5f);
        this.animate(entity.punchAnimationState, HerobrinePunchAnimation.PUNCH_1, ageInTicks);
        this.animate(entity.throwFireballAnimationState, HerobrineSkillAnimation.THROW_NARAKA_FIREBALL, ageInTicks);
        this.animate(entity.blockingSkillAnimationState, HerobrineAnimation.BLOCKING, ageInTicks);
    }
}
