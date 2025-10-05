package com.yummy.naraka.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public class AbsoluteHerobrineModel extends EntityModel<LivingEntityRenderState> {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        return LayerDefinition.create(mesh, 16, 16);
    }

    public AbsoluteHerobrineModel(ModelPart root) {
        super(root);
    }
}
