package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.OriginHerobrineRenderState;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.entity.OriginHerobrine;
import com.yummy.naraka.world.item.SoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.LinkedHashMap;

@Environment(EnvType.CLIENT)
public class OriginHerobrineRenderer extends LivingEntityRenderer<OriginHerobrine, OriginHerobrineRenderState, HerobrineModel<OriginHerobrineRenderState>> {
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    public OriginHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE), "origin_herobrine"), 0);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(OriginHerobrineRenderState renderState) {
        return NarakaTextures.HEROBRINE;
    }

    @Override
    public OriginHerobrineRenderState createRenderState() {
        return new OriginHerobrineRenderState();
    }

    @Override
    public void extractRenderState(OriginHerobrine livingEntity, OriginHerobrineRenderState renderState, float partialTicks) {
        super.extractRenderState(livingEntity, renderState, partialTicks);
        renderState.setupAnimationStates(livingEntity);
        renderState.lightCoords = 0;
        renderState.alpha = livingEntity.getAlpha();
        renderState.soulTypeAlpha = new LinkedHashMap<>(livingEntity.getSoulTypeAlpha());
        renderState.hasRedOverlay = false;
        renderState.yRot = 0;
        renderState.bodyRot = 0;
        renderState.outlineColor = 0xffffffff;
    }

    @Override
    public void submit(OriginHerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0, 2.5, -1);

        float shinyTick = 0x80;
        if (renderState.alpha < 0xff)
            shinyTick += (0xff - renderState.alpha) / 2f;
        ShinyEffectRenderer.submitShiny(shinyTick, 0xff, 0.25f, renderState.yRot + 180, false, 0xffffff, poseStack, submitNodeCollector);
        ShinyEffectRenderer.submitShiny(shinyTick, 0xff, 0.25f, renderState.yRot, false, 0xffffff, poseStack, submitNodeCollector);
        submitColors(renderState, poseStack, submitNodeCollector);

        poseStack.popPose();

        if (renderState.deathTime > 0.0F) {
            float deathProgress = renderState.deathTime / 200.0F;
            poseStack.pushPose();
            poseStack.translate(0, 1, 0);
            submitRays(poseStack, deathProgress, submitNodeCollector, RenderTypes.dragonRays());
            submitRays(poseStack, deathProgress, submitNodeCollector, RenderTypes.dragonRaysDepth());
            poseStack.popPose();
        }

        renderState.deathTime = 0;
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    private static void submitRays(PoseStack poseStack, float deathProgress, SubmitNodeCollector submitNodeCollector, RenderType renderType) {
        submitNodeCollector.submitCustomGeometry(
                poseStack,
                renderType,
                (pose, vertexConsumer) -> {
                    float alpha = Math.min(deathProgress > 0.8F ? (deathProgress - 0.8F) / 0.2F : 0.0F, 1.0F);
                    int white = ARGB.colorFromFloat(1.0F - alpha, 1, 1, 1);
                    int color = ComponentStyles.RAINBOW_COLOR.getCurrentColor().withAlpha(2).pack();
                    RandomSource randomSource = RandomSource.create(432L);
                    Vector3f v1 = new Vector3f();
                    Vector3f v2 = new Vector3f();
                    Vector3f v3 = new Vector3f();
                    Vector3f v4 = new Vector3f();
                    Quaternionf quaternionf = new Quaternionf();
                    int k = Mth.floor((deathProgress + deathProgress * deathProgress) / 2.0F * 60.0F);

                    for (int l = 0; l < k; l++) {
                        quaternionf.rotationXYZ(
                                        randomSource.nextFloat() * (float) (Math.PI * 2), randomSource.nextFloat() * (float) (Math.PI * 2), randomSource.nextFloat() * (float) (Math.PI * 2)
                                )
                                .rotateXYZ(
                                        randomSource.nextFloat() * (float) (Math.PI * 2),
                                        randomSource.nextFloat() * (float) (Math.PI * 2),
                                        randomSource.nextFloat() * (float) (Math.PI * 2) + deathProgress * (float) (Math.PI / 2)
                                );
                        pose.rotate(quaternionf);
                        float y = randomSource.nextFloat() * 20.0F + 5.0F + alpha * 10.0F;
                        float z = randomSource.nextFloat() * 2.0F + 1.0F + alpha * 2.0F;
                        v2.set(-HALF_SQRT_3 * z, y, -0.5F * z);
                        v3.set(HALF_SQRT_3 * z, y, -0.5F * z);
                        v4.set(0.0F, y, z);
                        vertexConsumer.addVertex(pose, v1).setColor(white);
                        vertexConsumer.addVertex(pose, v2).setColor(color);
                        vertexConsumer.addVertex(pose, v3).setColor(color);
                        vertexConsumer.addVertex(pose, v1).setColor(white);
                        vertexConsumer.addVertex(pose, v3).setColor(color);
                        vertexConsumer.addVertex(pose, v4).setColor(color);
                        vertexConsumer.addVertex(pose, v1).setColor(white);
                        vertexConsumer.addVertex(pose, v4).setColor(color);
                        vertexConsumer.addVertex(pose, v2).setColor(color);
                    }
                }
        );
    }

    private void submitColors(OriginHerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        int index = 0;
        for (SoulType soulType : renderState.soulTypeAlpha.keySet()) {
            float alpha = renderState.soulTypeAlpha.getOrDefault(soulType, 0f);
            int halfIndex = index / 2;
            double xOffset = 0.6 * (halfIndex + 1);
            if (index % 2 == 0)
                xOffset *= -1;
            float scale = 0.066f - Mth.log2(halfIndex) * 0.03f;

            poseStack.pushPose();
            poseStack.translate(xOffset, 0, 0);
            poseStack.scale(4 + halfIndex, 1, 1);
            ShinyEffectRenderer.submitShiny(alpha * 50, 100, scale, renderState.yRot + 180, true, soulType.color, poseStack, submitNodeCollector);
            ShinyEffectRenderer.submitShiny(alpha * 50, 100, scale, renderState.yRot, true, soulType.color, poseStack, submitNodeCollector);

            poseStack.popPose();

            index++;
        }
    }

    @Override
    protected RenderType getRenderType(OriginHerobrineRenderState renderState, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderTypes.entityTranslucent(getTextureLocation(renderState));
    }

    @Override
    protected int getModelTint(OriginHerobrineRenderState renderState) {
        return ARGB.color(renderState.alpha, 0);
    }

    @Override
    protected boolean shouldShowName(OriginHerobrine livingEntity, double d) {
        return false;
    }
}
