package com.yummy.naraka.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

@Environment(EnvType.CLIENT)
public class SpearSpecialRenderer implements NoDataSpecialModelRenderer {
    private final Model model;
    private final ResourceLocation texture;

    public SpearSpecialRenderer(Model model, ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void render(ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        poseStack.pushPose();
        poseStack.scale(1, -1, 1);
        RenderType renderType = model.renderType(texture);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    public record Unbaked(ModelLayerLocation modelLayer,
                          ResourceLocation texture) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ResourceLocation.CODEC.fieldOf("model_location").forGetter(unbaked -> unbaked.modelLayer.model()),
                        PrimitiveCodec.STRING.fieldOf("layer").forGetter(unbaked -> unbaked.modelLayer.layer()),
                        ResourceLocation.CODEC.fieldOf("texture").forGetter(unbaked -> unbaked.texture)
                ).apply(instance, Unbaked::new)
        );

        private Unbaked(ResourceLocation model, String layer, ResourceLocation texture) {
            this(new ModelLayerLocation(model, layer), texture);
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            ModelPart root = modelSet.bakeLayer(modelLayer);
            Model model = new Model.Simple(root, RenderType::entityCutout);
            return new SpearSpecialRenderer(model, texture);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }
    }
}
