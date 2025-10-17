package com.yummy.naraka.client.model;

import com.yummy.naraka.world.entity.AbsoluteHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class AbsoluteHerobrineModel extends SimpleEntityModel<AbsoluteHerobrine> {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        return LayerDefinition.create(mesh, 16, 16);
    }

    public AbsoluteHerobrineModel(ModelPart root) {
        super(root);
    }
}
