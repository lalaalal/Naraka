package com.yummy.naraka.client.model;

import com.yummy.naraka.client.renderer.entity.state.FlatImageRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class StardustModel extends EntityModel<FlatImageRenderState> {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5147F, 8.4853F, -5.6569F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-6.5147F, 9.4853F, -4.6569F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.4853F, 7.5147F, 5.6569F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public StardustModel(ModelPart root) {
        super(root);
    }
}
