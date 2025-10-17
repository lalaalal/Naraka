package com.yummy.naraka.client.model;

import com.yummy.naraka.client.animation.DiamondGolemAnimation;
import com.yummy.naraka.world.entity.DiamondGolem;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class DiamondGolemModel extends SkillUsingModModel<DiamondGolem> {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -20.0F, 0.0F, 0, 3.1416F, 0));
        PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));
        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-20.0F, -16.0F, -10.0F, 40.0F, 32.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 10.0F));
        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(124, 8).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition block = body.addOrReplaceChild("block", CubeListBuilder.create().texOffs(49, 90).addBox(-8.0F, -8.0F, -16.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, -2.0F));
        PartDefinition arm_right = body.addOrReplaceChild("arm_right", CubeListBuilder.create(), PartPose.offset(20.0F, -23.0F, 0.0F));
        PartDefinition arm_right_r1 = arm_right.addOrReplaceChild("arm_right_r1", CubeListBuilder.create().texOffs(0, 52).addBox(-13.0F, -24.0F, -1.0F, 14.0F, 24.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 17.0F, 5.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition hand_right = arm_right.addOrReplaceChild("hand_right", CubeListBuilder.create(), PartPose.offset(7.0F, 17.0F, 0.0F));
        PartDefinition hand_right_r1 = hand_right.addOrReplaceChild("hand_right_r1", CubeListBuilder.create().texOffs(3, 88).addBox(-10.5F, -22.0F, -1.0F, 11.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 22.0F, 5.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition thumb_right = hand_right.addOrReplaceChild("thumb_right", CubeListBuilder.create().texOffs(81, 57).mirror().addBox(-1.5F, -2.0F, -2.5F, 3.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 22.0F, 2.5F));
        PartDefinition finger3 = hand_right.addOrReplaceChild("finger3", CubeListBuilder.create().texOffs(97, 55).mirror().addBox(-1.5F, -3.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, 22.0F, 2.5F));
        PartDefinition finger4 = hand_right.addOrReplaceChild("finger4", CubeListBuilder.create().texOffs(97, 55).mirror().addBox(-1.5F, -3.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, 22.0F, -3.3F));
        PartDefinition arm_left = body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(3, 122).addBox(-14.0F, -7.0F, -6.0F, 14.0F, 24.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-20.0F, -23.0F, 0.0F));
        PartDefinition hand_left = arm_left.addOrReplaceChild("hand_left", CubeListBuilder.create().texOffs(55, 124).addBox(-9.5F, 0.0F, -6.0F, 11.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 17.0F, 0.0F));
        PartDefinition thumb_left = hand_left.addOrReplaceChild("thumb_left", CubeListBuilder.create().texOffs(81, 57).addBox(-1.5F, -2.0F, -2.5F, 3.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 22.0F, 2.5F));
        PartDefinition finger1 = hand_left.addOrReplaceChild("finger1", CubeListBuilder.create().texOffs(97, 55).addBox(-1.5F, -3.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 22.0F, 2.5F));
        PartDefinition finger2 = hand_left.addOrReplaceChild("finger2", CubeListBuilder.create().texOffs(97, 55).addBox(-1.5F, -3.0F, -2.5F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 22.0F, -3.3F));

        PartDefinition middle = main.addOrReplaceChild("middle", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));
        PartDefinition middle_r1 = middle.addOrReplaceChild("middle_r1", CubeListBuilder.create().texOffs(120, 36).addBox(-21.0F, -8.0F, -1.0F, 22.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 8.0F, 6.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition leg_left = middle.addOrReplaceChild("leg_left", CubeListBuilder.create(), PartPose.offset(-14.0F, 18.0F, 0.0F));
        PartDefinition leg_left_r1 = leg_left.addOrReplaceChild("leg_left_r1", CubeListBuilder.create().texOffs(161, 58).addBox(-11.0F, -20.0F, -1.0F, 12.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 10.0F, 5.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition leg_right = middle.addOrReplaceChild("leg_right", CubeListBuilder.create(), PartPose.offset(14.0F, 18.0F, 0.0F));
        PartDefinition leg_right_r1 = leg_right.addOrReplaceChild("leg_right_r1", CubeListBuilder.create().texOffs(113, 58).addBox(-11.0F, -20.0F, -1.0F, 12.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 10.0F, 5.0F, 0.0F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    public DiamondGolemModel(ModelPart root) {
        super(root, "diamond_golem");
    }

    @Override
    public void setupAnim(DiamondGolem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animateWalk(DiamondGolemAnimation.WALKING, limbSwing, limbSwingAmount, 3, 3);
        playAnimations(entity, ageInTicks);
    }
}
