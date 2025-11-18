package com.yummy.naraka.client.model;

import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.renderer.entity.state.NarakaSwordRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class NarakaSwordModel extends EntityModel<NarakaSwordRenderState> {
    private final ModelPart body;
    private final ModelPart core;

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -16.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(8, 0).addBox(-2.0F, -7.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public NarakaSwordModel(ModelPart root) {
        super(root, NarakaRenderTypes::longinusCutout);
        this.body = root.getChild("body");
        this.core = root.getChild("core");
    }

    public ModelPart body() {
        return this.body;
    }

    public ModelPart core() {
        return this.core;
    }
}
