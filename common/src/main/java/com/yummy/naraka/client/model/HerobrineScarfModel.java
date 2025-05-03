package com.yummy.naraka.client.model;

import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class HerobrineScarfModel extends EntityModel<AbstractHerobrineRenderState> {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("bb_main",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-7.0F, -24.0F, -2.0F, 14.0F, 6.0F, 4.0F, new CubeDeformation(0.51F))
                        .texOffs(0, 0).addBox(-4.0F, -30.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, 12, 2.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public HerobrineScarfModel(ModelPart root) {
        super(root);
    }
}
