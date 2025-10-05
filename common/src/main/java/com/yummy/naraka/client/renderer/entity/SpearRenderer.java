package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.entity.state.SpearRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.Spear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
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
    public SpearRenderState createRenderState() {
        return new SpearRenderState();
    }

    @Override
    public void extractRenderState(Spear spear, SpearRenderState renderState, float partialTick) {
        super.extractRenderState(spear, renderState, partialTick);
        renderState.setRotation(spear, partialTick);
        renderState.setType(spear);
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
    public void submit(SpearRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(renderState.yRotation);
        poseStack.mulPose(renderState.xRotation);
        poseStack.translate(0, yOffset, 0);

        if (renderState.isLonginus && NarakaClientContext.SHADER_ENABLED.getValue()) {
            renderNonShaderLonginus(model, renderState.ageInTicks, poseStack, submitNodeCollector);
        } else {
            if (renderState.isLonginus)
                renderState.lightCoords = LightTexture.FULL_BRIGHT;
            RenderType renderType = model.renderType(getTextureLocation(renderState));
            NarakaRenderUtils.submitModelWithFoilRenderTypes(renderState, model, poseStack, renderType, submitNodeCollector, renderState.hasFoil);
        }

        poseStack.popPose();
    }

    public static void renderShaderLonginus(EntityModel<SpearRenderState> model, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        submitNodeCollector.submitModelPart(model.root(), poseStack, NarakaRenderTypes.longinus(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, -1, null);
    }

    public static void renderNonShaderLonginus(EntityModel<SpearRenderState> model, float ageInTicks, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        submitNodeCollector.submitModelPart(model.root(), poseStack, RenderType.entityCutout(NarakaTextures.LONGINUS), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, 0xff000000, null);

        renderLonginus(model, ageInTicks, 0.001f, 0.01f, poseStack, submitNodeCollector);
        renderLonginus(model, ageInTicks, 0.002f, 0.005f, poseStack, submitNodeCollector);
        renderLonginus(model, ageInTicks, 0.0015f, 0.0025f, poseStack, submitNodeCollector);
    }

    private static void renderLonginus(EntityModel<SpearRenderState> model, float ageInTicks, float uMultiplier, float vMultiplier, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        RenderType renderType = RenderType.energySwirl(NarakaTextures.LONGINUS, (ageInTicks * uMultiplier) % 1, (ageInTicks * vMultiplier) % 1);
        submitNodeCollector.submitModelPart(model.root(), poseStack, renderType, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, 0xdd888888, null);
    }
}
