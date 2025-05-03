package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.entity.state.SpearRenderState;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.Spear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class SpearRenderer extends EntityRenderer<Spear, SpearRenderState> {
    private static final Map<EntityType<? extends Spear>, ResourceLocation> TEXTURE_MAP = Map.of(
            NarakaEntityTypes.THROWN_SPEAR.get(), NarakaTextures.SPEAR,
            NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR.get(), NarakaTextures.MIGHTY_HOLY_SPEAR,
            NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS.get(), NarakaTextures.LONGINUS
    );

    protected final EntityModel<SpearRenderState> model;
    private final int yOffset;

    public static SpearRenderer longinus(EntityRendererProvider.Context context) {
        SpearOfLonginusModel model = new SpearOfLonginusModel(context.bakeLayer(NarakaModelLayers.SPEAR_OF_LONGINUS));
        return new SpearRenderer(context, model, 3);
    }

    @Override
    public SpearRenderState createRenderState() {
        return new SpearRenderState();
    }

    @Override
    public void extractRenderState(Spear spear, SpearRenderState renderState, float partialTick) {
        super.extractRenderState(spear, renderState, partialTick);
        renderState.setRotation(spear, partialTick);
        renderState.setType(spear);
    }

    public SpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new SpearModel(context.bakeLayer(NarakaModelLayers.SPEAR));
        this.yOffset = 0;
    }

    public SpearRenderer(EntityRendererProvider.Context context, EntityModel<SpearRenderState> model, int yOffset) {
        super(context);
        this.model = model;
        this.yOffset = yOffset;
    }

    @Override
    public boolean shouldRender(Spear spear, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public ResourceLocation getTextureLocation(SpearRenderState renderState) {
        if (TEXTURE_MAP.containsKey(renderState.type))
            return TEXTURE_MAP.get(renderState.type);
        return NarakaTextures.SPEAR;
    }

    @Override
    public void render(SpearRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(renderState.yRotation);
        poseStack.mulPose(renderState.xRotation);
        poseStack.translate(0, yOffset, 0);

        if (renderState.isLonginus && NarakaConfig.CLIENT.enableNonShaderLonginusRendering.getValue()) {
            packedLight = LightTexture.FULL_BRIGHT;
            renderNonShaderLonginus(model, renderState.ageInTicks, poseStack, buffer);
        }
        RenderType renderType = model.renderType(getTextureLocation(renderState));
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(buffer, renderType, false, renderState.hasFoil);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xffffffff);

        poseStack.popPose();
    }

    public static void renderShaderLonginus(EntityModel<SpearRenderState> model, PoseStack poseStack, MultiBufferSource buffer) {
        VertexConsumer vertexConsumer = buffer.getBuffer(NarakaRenderTypes.longinus());
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xffffffff);
    }

    public static void renderNonShaderLonginus(EntityModel<SpearRenderState> model, float ageInTicks, PoseStack poseStack, MultiBufferSource buffer) {
        VertexConsumer background = buffer.getBuffer(RenderType.entityCutout(NarakaTextures.LONGINUS));
        model.renderToBuffer(poseStack, background, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xff000000);

        renderLonginus(model, ageInTicks, 0.001f, 0.01f, poseStack, buffer);
        renderLonginus(model, ageInTicks, 0.002f, 0.005f, poseStack, buffer);
        renderLonginus(model, ageInTicks, 0.0015f, 0.0025f, poseStack, buffer);
    }

    private static void renderLonginus(EntityModel<SpearRenderState> model, float ageInTicks, float uMultiplier, float vMultiplier, PoseStack poseStack, MultiBufferSource buffer) {
        RenderType renderType = RenderType.energySwirl(NarakaTextures.LONGINUS, (ageInTicks * uMultiplier) % 1, (ageInTicks * vMultiplier) % 1);
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xaa888888);
    }
}
