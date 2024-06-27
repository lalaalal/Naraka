package com.yummy.naraka.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HerobrineModel<T extends Herobrine> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart left_arm_upper;
    private final ModelPart left_arm_lower;
    private final ModelPart right_arm;
    private final ModelPart right_arm_upper;
    private final ModelPart right_arm_lower;
    private final ModelPart left_leg;
    private final ModelPart left_leg_upper;
    private final ModelPart left_leg_lower;
    private final ModelPart right_leg;
    private final ModelPart right_leg_upper;
    private final ModelPart right_leg_lower;

    public HerobrineModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.left_arm_upper = left_arm.getChild("left_arm_upper");
        this.left_arm_lower = left_arm.getChild("left_arm_lower");
        this.right_arm = root.getChild("right_arm");
        this.right_arm_upper = right_arm.getChild("right_arm_upper");
        this.right_arm_lower = right_arm.getChild("right_arm_lower");
        this.left_leg = root.getChild("left_leg");
        this.left_leg_upper = left_leg.getChild("left_leg_upper");
        this.left_leg_lower = left_leg.getChild("left_leg_lower");
        this.right_leg = root.getChild("right_leg");
        this.right_leg_upper = right_leg.getChild("right_leg_upper");
        this.right_leg_lower = right_leg.getChild("right_leg_lower");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));
        left_arm.addOrReplaceChild("left_arm_upper", CubeListBuilder.create().texOffs(40, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(42, 31).addBox(-2.0F, 3.0F, -2.25F, 4.2F, 1.0F, 4.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        left_arm.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(36, 10).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-6.0F, 0.0F, 0.0F));
        right_arm.addOrReplaceChild("right_arm_upper", CubeListBuilder.create().texOffs(32, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(42, 31).addBox(-2.2F, 3.0F, -2.25F, 4.2F, 1.0F, 4.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        right_arm.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(16, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));
        left_leg.addOrReplaceChild("left_leg_upper", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        left_leg.addOrReplaceChild("left_leg_lower", CubeListBuilder.create().texOffs(24, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F));
        right_leg.addOrReplaceChild("right_leg_upper", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        right_leg.addOrReplaceChild("right_leg_lower", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }
}
