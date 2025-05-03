package com.yummy.naraka.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

@Environment(EnvType.CLIENT)
public class SoulSmithingBlockSpecialRenderer implements NoDataSpecialModelRenderer {
    private final Model model;

    public SoulSmithingBlockSpecialRenderer(Model model) {
        this.model = model;
    }

    @Override
    public void render(ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        poseStack.pushPose();
        poseStack.rotateAround(Axis.XP.rotation(Mth.PI), 0.5f, 0.5f, 0.5f);
        RenderType renderType = model.renderType(NarakaTextures.SOUL_SMITHING_BLOCK);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

        model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            ModelPart root = modelSet.bakeLayer(NarakaModelLayers.SOUL_SMITHING_BLOCK);
            Model model = new Model.Simple(root, RenderType::entityTranslucent);
            return new SoulSmithingBlockSpecialRenderer(model);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }
    }
}
