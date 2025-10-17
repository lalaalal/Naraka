package com.yummy.naraka.client.model;

import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.world.entity.SpearOfLonginus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class SpearOfLonginusModel extends SimpleEntityModel<SpearOfLonginus> {
    public SpearOfLonginusModel(ModelPart root) {
        super(root, (resourceLocation) -> NarakaRenderTypes.longinus());
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition tipleft = partdefinition.addOrReplaceChild("tipleft", CubeListBuilder.create().texOffs(5, 0).addBox(3.0F, 10.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(2.0F, 9.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(1.0F, 8.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(0.0F, 7.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(0.0F, 6.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(1.0F, 5.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(2.0F, 4.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(3.0F, 3.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(4.0F, 2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(5.0F, 1.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(6.0F, 0.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 3).addBox(6.0F, -39.0F, -4.0F, 1.0F, 40.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -51.0F, 3.0F));

        PartDefinition tipleft2 = partdefinition.addOrReplaceChild("tipleft2", CubeListBuilder.create().texOffs(5, 0).addBox(3.0F, 10.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(2.0F, 9.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(1.0F, 8.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(0.0F, 7.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(0.0F, 6.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(1.0F, 5.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(2.0F, 4.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(3.0F, 3.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(4.0F, 2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(5.0F, 1.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(6.0F, 0.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 3).addBox(6.0F, -39.0F, -4.0F, 1.0F, 40.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -51.0F, -4.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -63.0F, -1.0F, 1.0F, 63.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}