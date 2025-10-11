package com.yummy.naraka.client.model;

import com.yummy.naraka.client.renderer.entity.state.NarakaPickaxeRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class NarakaPickaxeModel extends SkillUsingModModel<NarakaPickaxeRenderState> {
    public NarakaPickaxeModel(ModelPart root) {
        super(root, "naraka_pickaxe");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();
        root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(-0.75F, -9.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(NarakaPickaxeRenderState renderState) {
        super.setupAnim(renderState);
        animate(renderState);
    }
}
