package com.yummy.naraka.client.model;

import com.yummy.naraka.client.renderer.entity.state.HerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class HerobrineFinalModel extends AbstractHerobrineModel<HerobrineRenderState> {
    private final ModelPart main;
    private final ModelPart upperBody;
    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public static LayerDefinition createForHerobrineFinal() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(44, 40).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(1, 81).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F))
                        .texOffs(0, 54).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition chest = main.addOrReplaceChild("chest", CubeListBuilder.create()
                        .texOffs(32, 56).addBox(-5.0F, -4.0F, -3.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 40).addBox(-8.0F, -4.0F, -3.0F, 16.0F, 8.0F, 6.0F, new CubeDeformation(0.75F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = chest.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(48, 70).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F))
                        .texOffs(16, 70).addBox(-4.0F, 2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, -2.0F, 0.0F));

        PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create()
                        .texOffs(64, 56).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.25F))
                        .texOffs(32, 70).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 8.0F, 2.0F));

        PartDefinition left_arm = chest.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(48, 70).mirror().addBox(0.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false)
                        .texOffs(16, 70).mirror().addBox(0.0F, 2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(5.0F, -2.0F, 0.0F));

        PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create()
                        .texOffs(64, 56).mirror().addBox(-2.0F, -2.0F, -4.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false)
                        .texOffs(32, 70).mirror().addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(2.0F, 8.0F, 2.0F));

        PartDefinition middle = chest.addOrReplaceChild("middle", CubeListBuilder.create()
                        .texOffs(60, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition right_leg = middle.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(60, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 4.0F, 0.0F));

        PartDefinition right_legdown = right_leg.addOrReplaceChild("right_legdown", CubeListBuilder.create()
                        .texOffs(60, 8).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 70).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.15F))
                        .texOffs(64, 68).addBox(-2.0F, 2.0F, 0.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 8.0F, -2.0F));

        PartDefinition left_leg = middle.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(60, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(2.0F, 4.0F, 0.0F));

        PartDefinition left_legdown = left_leg.addOrReplaceChild("left_legdown", CubeListBuilder.create()
                        .texOffs(60, 8).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 70).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.15F)).mirror(false)
                        .texOffs(64, 68).mirror().addBox(-2.0F, 2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition weapon = root.addOrReplaceChild("weapon", CubeListBuilder.create(), PartPose.offset(-6.0F, 6.0F, 2.0F));

        PartDefinition cube_r1 = weapon.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                        .texOffs(107, 57).addBox(-8.0F, -27.0F, 0.0F, 8.0F, 66.0F, 1.0F, new CubeDeformation(0.25F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -27.0F, -1.5708F, -1.5708F, 0.0F));

        PartDefinition cube_r2 = weapon.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                        .texOffs(85, 1).addBox(-1.0F, -27.0F, 0.0F, 1.0F, 66.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public HerobrineFinalModel(ModelPart root) {
        super(root);
        this.main = root.getChild("main");
        this.head = main.getChild("head");
        this.upperBody = main.getChild("chest");
        this.rightArm = upperBody.getChild("right_arm");
        this.leftArm = upperBody.getChild("left_arm");
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
    public ModelPart leftArm() {
        return this.leftArm;
    }
}
