package com.yummy.naraka.client.model;

import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class FinalHerobrineModel<S extends AbstractHerobrineRenderState> extends AbstractHerobrineModel<S> {
    private final ModelPart main;
    private final ModelPart upperBody;
    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftHand;
    private final ModelPart rightHand;
    private final ModelPart independentPickaxe;
    private final ModelPart pickaxe;

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition independent_pickaxe = partdefinition.addOrReplaceChild("independent_pickaxe", CubeListBuilder.create(), PartPose.offset(-8.5F, 5.5F, 0.0F));

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition chest = main.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(32, 56).addBox(-5.0F, -8.5F, -3.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.5F))
                .texOffs(0, 40).addBox(-8.0F, -9.5F, -3.0F, 16.0F, 8.0F, 6.0F, new CubeDeformation(1.0F))
                .texOffs(0, 54).addBox(-4.0F, -17.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 4.5F, 0.0F));

        PartDefinition head = chest.addOrReplaceChild("head", CubeListBuilder.create().texOffs(44, 40).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 81).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, -9.5F, 0.0F));

        PartDefinition right_arm = chest.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 70).addBox(-5.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.75F))
                .texOffs(16, 70).addBox(-5.0F, 2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -6.5F, 0.0F));

        PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(64, 56).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.75F))
                .texOffs(32, 70).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 8.0F, 2.0F));

        PartDefinition pickaxe = right_hand.addOrReplaceChild("pickaxe", CubeListBuilder.create(), PartPose.offset(-0.5F, 3.5F, -2.0F));

        PartDefinition left_arm = chest.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 70).mirror().addBox(1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false)
                .texOffs(16, 70).mirror().addBox(1.0F, 2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -6.5F, 0.0F));

        PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(64, 56).mirror().addBox(-2.0F, -2.0F, -4.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false)
                .texOffs(32, 70).mirror().addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 8.0F, 2.0F));

        PartDefinition middle = main.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(60, 0).addBox(-4.0F, 0.5F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 4.5F, 0.0F));

        PartDefinition right_leg = middle.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(60, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 4.5F, 0.0F));

        PartDefinition right_legdown = right_leg.addOrReplaceChild("right_legdown", CubeListBuilder.create().texOffs(60, 8).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 70).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(64, 68).addBox(-2.0F, 2.0F, 0.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 8.0F, -2.0F));

        PartDefinition left_leg = middle.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(60, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 4.5F, 0.0F));

        PartDefinition left_legdown = left_leg.addOrReplaceChild("left_legdown", CubeListBuilder.create().texOffs(60, 8).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 70).mirror().addBox(-2.0F, -1.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(64, 68).mirror().addBox(-2.0F, 2.0F, 0.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false), PartPose.offset(0.0F, 8.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public FinalHerobrineModel(ModelPart root) {
        super(root);
        this.main = root.getChild("main");
        this.upperBody = main.getChild("chest");
        this.head = upperBody.getChild("head");
        this.rightArm = upperBody.getChild("right_arm");
        this.leftArm = upperBody.getChild("left_arm");
        this.rightHand = rightArm.getChild("right_hand");
        this.leftHand = leftArm.getChild("left_hand");
        this.independentPickaxe = root.getChild("independent_pickaxe");
        this.pickaxe = rightHand.getChild("pickaxe");
    }

    @Override
    public ModelPart head() {
        return this.head;
    }

    @Override
    public ModelPart main() {
        return this.main;
    }

    @Override
    public ModelPart upperBody() {
        return this.upperBody;
    }

    @Override
    public ModelPart rightArm() {
        return this.rightArm;
    }

    @Override
    public ModelPart rightHand() {
        return rightHand;
    }

    @Override
    public ModelPart leftArm() {
        return this.leftArm;
    }

    @Override
    public ModelPart leftHand() {
        return leftHand;
    }
}
